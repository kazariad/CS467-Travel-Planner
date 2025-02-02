package edu.oregonstate.cs467.travelplanner.experience.persistence;

import edu.oregonstate.cs467.travelplanner.AbstractBaseTest;
import edu.oregonstate.cs467.travelplanner.experience.model.Experience;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.context.jdbc.SqlMergeMode.MergeMode;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SqlMergeMode(MergeMode.MERGE)
@Sql(scripts = "/sql/schema.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "/sql/user/1.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "/sql/user/2.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(statements = "ALTER TABLE experience AUTO_INCREMENT = 1")
class ExperienceDaoTests extends AbstractBaseTest {
    @Autowired
    private ExperienceDao dao;

    @Test
    @Sql(scripts = "/sql/experience/1.sql")
    @Sql(scripts = "/sql/experience/2.sql")
    void findById() throws Exception {
        var exp1 = objectMapper.readValue(new ClassPathResource("/json/experience/1.json").getInputStream(), Experience.class);
        var actual1 = dao.findById(1);
        assertThat(actual1.get()).usingRecursiveComparison().isEqualTo(exp1);

        var exp2 = objectMapper.readValue(new ClassPathResource("/json/experience/2.json").getInputStream(), Experience.class);
        var actual2 = dao.findById(2);
        assertThat(actual2.get()).usingRecursiveComparison().isEqualTo(exp2);

        var actual3 = dao.findById(3);
        assertThat(actual3).isEmpty();
    }

    @Test
    void persist() throws Exception {
        var exp1 = objectMapper.readValue(new ClassPathResource("/json/experience/1.json").getInputStream(), Experience.class);
        exp1.setExperienceId(null);
        dao.persist(exp1);
        var actual1 = dao.findById(exp1.getExperienceId());
        assertThat(actual1.get()).usingRecursiveComparison().isEqualTo(exp1);

        var exp2 = objectMapper.readValue(new ClassPathResource("/json/experience/2.json").getInputStream(), Experience.class);
        exp2.setExperienceId(null);
        dao.persist(exp2);
        var actual2 = dao.findById(exp2.getExperienceId());
        assertThat(actual2.get()).usingRecursiveComparison().isEqualTo(exp2);
    }

    @Test
    @Sql(scripts = "/sql/experience/1.sql")
    @Sql(scripts = "/sql/experience/2.sql")
    void update() throws Exception {
        var exp1 = objectMapper.readValue(new ClassPathResource("/json/experience/1.json").getInputStream(), Experience.class);
        var exp2 = objectMapper.readValue(new ClassPathResource("/json/experience/2.json").getInputStream(), Experience.class);
        exp1.setExperienceId(2L);
        exp2.setExperienceId(1L);
        dao.update(exp1);
        dao.update(exp2);
        var actual1 = dao.findById(1);
        var actual2 = dao.findById(2);
        assertThat(actual1.get()).usingRecursiveComparison().isEqualTo(exp2);
        assertThat(actual2.get()).usingRecursiveComparison().isEqualTo(exp1);

        exp1.setExperienceId(3L);
        assertThatThrownBy(() -> dao.update(exp1)).isInstanceOf(IncorrectResultSizeDataAccessException.class);
    }
}