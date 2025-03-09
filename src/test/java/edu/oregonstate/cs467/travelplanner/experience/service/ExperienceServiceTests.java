package edu.oregonstate.cs467.travelplanner.experience.service;

import edu.oregonstate.cs467.travelplanner.AbstractBaseTest;
import edu.oregonstate.cs467.travelplanner.experience.service.dto.CreateUpdateExperienceDto;
import edu.oregonstate.cs467.travelplanner.experience.model.Experience;
import edu.oregonstate.cs467.travelplanner.util.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.context.jdbc.SqlMergeMode.MergeMode;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SqlMergeMode(MergeMode.MERGE)
@Sql(scripts = "file:sql/schema.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "/sql/User/1.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "/sql/User/2.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(statements = "ALTER TABLE experience AUTO_INCREMENT = 1")
class ExperienceServiceTests extends AbstractBaseTest {
    @Autowired
    private ExperienceService service;

    @BeforeEach
    void beforeEach() {
        Mockito.doReturn(Instant.now().truncatedTo(ChronoUnit.MICROS)).when(clock).instant();
    }

    @Test
    @WithAnonymousUser
    @Sql(scripts = "/sql/Experience/1.sql")
    @Sql(scripts = "/sql/Experience/2.sql")
    void getExperience_as_anon() throws Exception {
        var actual1 = service.getExperience(1);
        assertThat(actual1).isNull();

        var actual2 = service.getExperience(2);
        var expected2 = objectMapper.readValue(new ClassPathResource("/json/Experience/2.json").getInputStream(), Experience.class);
        assertThat(actual2).usingRecursiveComparison().isEqualTo(expected2);

        var actual3 = service.getExperience(3);
        assertThat(actual3).isNull();
    }

    @Test
    @WithUserDetails("user1")
    void createExperience_as_user() throws Exception {
        var dtoA = objectMapper.readValue(new ClassPathResource("/json/CreateUpdateExperienceDto/A.json").getInputStream(), CreateUpdateExperienceDto.class);
        assertThat(service.createExperience(dtoA)).isEqualTo(1);
        var actual1 = service.getExperience(1);
        var expected1 = dtoA.transferTo(new Experience());
        expected1.setExperienceId(1L);
        expected1.setRatingCnt(0);
        expected1.setRatingSum(0);
        expected1.setUserId(1);
        expected1.setCreatedAt(Instant.now(clock));
        assertThat(actual1).usingRecursiveComparison().isEqualTo(expected1);

        var dtoB = objectMapper.readValue(new ClassPathResource("/json/CreateUpdateExperienceDto/B.json").getInputStream(), CreateUpdateExperienceDto.class);
        assertThat(service.createExperience(dtoB)).isEqualTo(2);
        var actual2 = service.getExperience(2);
        var expected2 = dtoB.transferTo(new Experience());
        expected2.setExperienceId(2L);
        expected2.setRatingCnt(0);
        expected2.setRatingSum(0);
        expected2.setUserId(1);
        expected2.setCreatedAt(Instant.now(clock));
        assertThat(actual2).usingRecursiveComparison().isEqualTo(expected2);
    }

    @Test
    @WithAnonymousUser
    void createExperience_as_anon() throws Exception {
        var dtoA = objectMapper.readValue(new ClassPathResource("/json/CreateUpdateExperienceDto/A.json").getInputStream(), CreateUpdateExperienceDto.class);
        assertThatThrownBy(() -> service.createExperience(dtoA)).isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @WithUserDetails("user2")
    @Sql(scripts = "/sql/Experience/2.sql")
    void updateExperience_as_owner() throws Exception {
        var dtoA = objectMapper.readValue(new ClassPathResource("/json/CreateUpdateExperienceDto/A.json").getInputStream(), CreateUpdateExperienceDto.class);
        service.updateExperience(2, dtoA);
        var expected = objectMapper.readValue(new ClassPathResource("/json/Experience/2.json").getInputStream(), Experience.class);
        dtoA.transferTo(expected);
        expected.setUpdatedAt(Instant.now(clock));
        var actual = service.getExperience(2);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);

        var dtoB = objectMapper.readValue(new ClassPathResource("/json/CreateUpdateExperienceDto/B.json").getInputStream(), CreateUpdateExperienceDto.class);
        service.updateExperience(2, dtoB);
        dtoB.transferTo(expected);
        expected.setUpdatedAt(Instant.now(clock));
        actual = service.getExperience(2);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @WithUserDetails("user1")
    @Sql(scripts = "/sql/Experience/2.sql")
    void updateExperience_as_other() throws Exception {
        var dtoA = objectMapper.readValue(new ClassPathResource("/json/CreateUpdateExperienceDto/A.json").getInputStream(), CreateUpdateExperienceDto.class);
        assertThatThrownBy(() ->  service.updateExperience(2, dtoA)).isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @WithUserDetails("user1")
    @Sql(scripts = "/sql/Experience/1.sql")
    void updateExperience_missing_or_deleted() throws Exception {
        var dtoA = objectMapper.readValue(new ClassPathResource("/json/CreateUpdateExperienceDto/A.json").getInputStream(), CreateUpdateExperienceDto.class);
        assertThatThrownBy(() ->  service.updateExperience(1, dtoA)).isInstanceOf(ResourceNotFoundException.class);
        assertThatThrownBy(() ->  service.updateExperience(2, dtoA)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @WithUserDetails("user2")
    @Sql(scripts = "/sql/Experience/2.sql")
    void deleteExperience_as_owner() {
        service.deleteExperience(2);
        var actual = service.getExperience(2);
        assertThat(actual).isNull();
    }

    @Test
    @WithUserDetails("user1")
    @Sql(scripts = "/sql/Experience/2.sql")
    void deleteExperience_as_other() {
        assertThatThrownBy(() -> service.deleteExperience(2)).isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @WithUserDetails("user1")
    @Sql(scripts = "/sql/Experience/1.sql")
    void deleteExperience_missing_or_deleted() {
        service.deleteExperience(1);
        service.deleteExperience(2);
    }
}
