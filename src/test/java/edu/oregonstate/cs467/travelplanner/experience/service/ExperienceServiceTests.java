package edu.oregonstate.cs467.travelplanner.experience.service;

import edu.oregonstate.cs467.travelplanner.AbstractBaseTest;
import edu.oregonstate.cs467.travelplanner.experience.model.Experience;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.context.jdbc.SqlMergeMode.MergeMode;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

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
}
