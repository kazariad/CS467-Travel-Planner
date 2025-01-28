package edu.oregonstate.cs467.travelplanner.experience.persistence;

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
@Sql(scripts = "/schema.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(statements = "ALTER TABLE experience AUTO_INCREMENT = 1")
class ExperienceDaoTests extends AbstractBaseTest {
    @Autowired
    private ExperienceDao dao;

    @Test
    @Sql(scripts = "/sql/experience/1.sql")
    @Sql(scripts = "/sql/experience/2.sql")
    void findById() throws Exception {
        var actual1 = dao.findById(1);
        var expected1 = objectMapper.readValue(new ClassPathResource("/json/experience/1.json").getInputStream(), Experience.class);
        assertThat(actual1.get()).usingRecursiveComparison().isEqualTo(expected1);

        var actual2 = dao.findById(2);
        var expected2 = objectMapper.readValue(new ClassPathResource("/json/experience/2.json").getInputStream(), Experience.class);
        assertThat(actual2.get()).usingRecursiveComparison().isEqualTo(expected2);

        var actual3 = dao.findById(3);
        assertThat(actual3).isEmpty();
    }

    @Test
    void persist() throws Exception {
        var exp1 = objectMapper.readValue(new ClassPathResource("/json/experience/1.json").getInputStream(), Experience.class);
        exp1.setExperienceId(null);
        dao.persist(exp1);
        assertThat(exp1.getExperienceId()).isEqualTo(1);
        var actual1 = dao.findById(1);
        assertThat(actual1.get()).usingRecursiveComparison().isEqualTo(exp1);

        var exp2 = objectMapper.readValue(new ClassPathResource("/json/experience/2.json").getInputStream(), Experience.class);
        exp2.setExperienceId(null);
        dao.persist(exp2);
        assertThat(exp2.getExperienceId()).isEqualTo(2);
        var actual2 = dao.findById(2);
        assertThat(actual2.get()).usingRecursiveComparison().isEqualTo(exp2);
    }
}