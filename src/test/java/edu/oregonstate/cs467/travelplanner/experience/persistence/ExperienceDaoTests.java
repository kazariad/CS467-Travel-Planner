package edu.oregonstate.cs467.travelplanner.experience.persistence;

import edu.oregonstate.cs467.travelplanner.AbstractBaseTest;
import edu.oregonstate.cs467.travelplanner.experience.model.Experience;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.context.jdbc.SqlMergeMode.MergeMode;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SqlMergeMode(MergeMode.MERGE)
@Sql(scripts = "file:sql/schema.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "/sql/User/1.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "/sql/User/2.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(statements = "ALTER TABLE experience AUTO_INCREMENT = 1")
class ExperienceDaoTests extends AbstractBaseTest {
    @Autowired
    private ExperienceDao dao;

    private Experience exp1, exp2, exp3, exp4, exp5;

    @BeforeEach
    void beforeEach() throws Exception {
        exp1 = objectMapper.readValue(new ClassPathResource("/json/Experience/1.json").getInputStream(), Experience.class);
        exp2 = objectMapper.readValue(new ClassPathResource("/json/Experience/2.json").getInputStream(), Experience.class);
        exp3 = objectMapper.readValue(new ClassPathResource("/json/Experience/3.json").getInputStream(), Experience.class);
        exp4 = objectMapper.readValue(new ClassPathResource("/json/Experience/4.json").getInputStream(), Experience.class);
        exp5 = objectMapper.readValue(new ClassPathResource("/json/Experience/5.json").getInputStream(), Experience.class);
    }

    @Test
    @Sql(scripts = "/sql/Experience/1.sql")
    @Sql(scripts = "/sql/Experience/2.sql")
    void findById() throws Exception {
        var actual1 = dao.findById(1);
        assertThat(actual1.get()).usingRecursiveComparison().isEqualTo(exp1);

        var actual2 = dao.findById(2);
        assertThat(actual2.get()).usingRecursiveComparison().isEqualTo(exp2);

        var actual3 = dao.findById(3);
        assertThat(actual3).isEmpty();
    }

    @Test
    @Sql(scripts = "/sql/Experience/1.sql")
    @Sql(scripts = "/sql/Experience/2.sql")
    @Sql(scripts = "/sql/Experience/3.sql")
    void findByIds() throws Exception {
        var actual = dao.findByIds(List.of(1L, 2L, 3L));
        assertThat(actual).usingRecursiveComparison().isEqualTo(List.of(exp2, exp3));

        actual = dao.findByIds(List.of(2L));
        assertThat(actual).usingRecursiveComparison().isEqualTo(List.of(exp2));

        actual = dao.findByIds(List.of());
        assertThat(actual).isEmpty();
    }

    @Test
    void persist() throws Exception {
        exp1.setExperienceId(null);
        dao.persist(exp1);
        var actual1 = dao.findById(exp1.getExperienceId());
        assertThat(actual1.get()).usingRecursiveComparison().isEqualTo(exp1);

        exp2.setExperienceId(null);
        dao.persist(exp2);
        var actual2 = dao.findById(exp2.getExperienceId());
        assertThat(actual2.get()).usingRecursiveComparison().isEqualTo(exp2);
    }

    @Test
    @Sql(scripts = "/sql/Experience/1.sql")
    @Sql(scripts = "/sql/Experience/2.sql")
    void update() throws Exception {
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

    @Test
    @Sql(scripts = "/sql/Experience/1.sql")
    @Sql(scripts = "/sql/Experience/2.sql")
    @Sql(scripts = "/sql/Experience/3.sql")
    @Sql(scripts = "/sql/Experience/4.sql")
    void findByUserId() throws Exception {
        var actual = dao.findByUserId(1);
        assertThat(actual).usingRecursiveComparison().isEqualTo(List.of(exp3, exp4));

        actual = dao.findByUserId(2);
        assertThat(actual).usingRecursiveComparison().isEqualTo(List.of(exp2));

        actual = dao.findByUserId(3);
        assertThat(actual).isEmpty();
    }

    @Test
    @Sql(scripts = "/sql/Experience/1.sql")
    @Sql(scripts = "/sql/Experience/2.sql")
    @Sql(scripts = "/sql/Experience/3.sql")
    @Sql(scripts = "/sql/Experience/4.sql")
    @Sql(scripts = "/sql/Experience/5.sql")
    void findRandomFeatured() throws Exception {
        var actual = dao.findRandomFeatured(2, 3);
        assertThat(new HashSet<>(actual)).usingRecursiveComparison().isEqualTo(Set.of(exp4, exp5));

        actual = dao.findRandomFeatured(4.25, 3);
        assertThat(new HashSet<>(actual)).usingRecursiveComparison().isEqualTo(Set.of(exp4));

        actual = dao.findRandomFeatured(5, 3);
        assertThat(actual).isEmpty();

        actual = dao.findRandomFeatured(0, 0);
        assertThat(actual).isEmpty();
    }
}