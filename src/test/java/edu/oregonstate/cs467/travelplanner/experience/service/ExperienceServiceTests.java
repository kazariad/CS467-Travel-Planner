package edu.oregonstate.cs467.travelplanner.experience.service;

import edu.oregonstate.cs467.travelplanner.AbstractBaseTest;
import edu.oregonstate.cs467.travelplanner.experience.model.Experience;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.context.jdbc.SqlMergeMode.MergeMode;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SqlMergeMode(MergeMode.MERGE)
@Sql(scripts = "/sql/schema.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "/sql/user/1.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "/sql/user/2.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(statements = "ALTER TABLE experience AUTO_INCREMENT = 1")
class ExperienceServiceTests extends AbstractBaseTest {
    @Autowired
    private ExperienceService service;

    @Test
    @Sql(scripts = "/sql/experience/1.sql")
    @Sql(scripts = "/sql/experience/2.sql")
    void getExperience() throws Exception {
        var actual = service.getExperience(1);
        assertThat(actual).isNull();

        var actual2 = service.getExperience(2);
        var exp2 = objectMapper.readValue(new ClassPathResource("/json/experience/2.json").getInputStream(), Experience.class);
        assertThat(actual2).usingRecursiveComparison().isEqualTo(exp2);

        var actual3 = service.getExperience(3);
        assertThat(actual3).isNull();
    }
}
