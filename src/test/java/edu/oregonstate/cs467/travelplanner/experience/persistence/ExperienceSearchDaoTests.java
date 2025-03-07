package edu.oregonstate.cs467.travelplanner.experience.persistence;

import edu.oregonstate.cs467.travelplanner.AbstractBaseTest;
import edu.oregonstate.cs467.travelplanner.experience.model.Experience;
import edu.oregonstate.cs467.travelplanner.experience.service.dto.ExperienceSearchParams;
import edu.oregonstate.cs467.travelplanner.experience.service.dto.ExperienceSearchParams.ExperienceSearchLocationParams;
import edu.oregonstate.cs467.travelplanner.experience.service.dto.ExperienceSearchParams.ExperienceSearchSort;
import edu.oregonstate.cs467.travelplanner.experience.service.dto.ExperienceSearchResult.ExperienceDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@Sql(scripts = "file:sql/schema.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "/sql/User/1.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "/sql/User/2.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "/sql/Experience/1.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "/sql/Experience/3.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "/sql/Experience/4.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "/sql/Experience/5.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
class ExperienceSearchDaoTests extends AbstractBaseTest {
    @Autowired
    private ExperienceSearchDao dao;
    private static Map<Long, Experience> experiences = new HashMap<>();

    @BeforeEach
    void beforeEach() throws Exception {
        if (!experiences.isEmpty()) return;
        experiences.put(1L, objectMapper.readValue(new ClassPathResource("/json/Experience/1.json").getInputStream(), Experience.class));
        experiences.put(3L, objectMapper.readValue(new ClassPathResource("/json/Experience/3.json").getInputStream(), Experience.class));
        experiences.put(4L, objectMapper.readValue(new ClassPathResource("/json/Experience/4.json").getInputStream(), Experience.class));
        experiences.put(5L, objectMapper.readValue(new ClassPathResource("/json/Experience/5.json").getInputStream(), Experience.class));
    }

    @Test
    void search_by_keywords() {
        var params = new ExperienceSearchParams();
        params.setKeywords("echo velvet breeze");
        params.setSort(ExperienceSearchSort.BEST_MATCH);
        params.setOffset(0);
        params.setLimit(10);
        var result = dao.search(params);
        assertThat(result.getExperienceDetailsList()).usingRecursiveComparison().isEqualTo(List.of(
                new ExperienceDetails(experiences.get(3L), "user1", null),
                new ExperienceDetails(experiences.get(5L), "user2", null),
                new ExperienceDetails(experiences.get(4L), "user1", null)
        ));

        params.setKeywords("Los Angeles");
        result = dao.search(params);
        assertThat(result.getExperienceDetailsList()).usingRecursiveComparison().isEqualTo(List.of(
                new ExperienceDetails(experiences.get(3L), "user1", null),
                new ExperienceDetails(experiences.get(4L), "user1", null)
        ));

        params.setKeywords("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        result = dao.search(params);
        assertThat(result.getExperienceDetailsList()).isEmpty();
    }

    @Test
    void search_by_location() {
        var params = new ExperienceSearchParams();
        params.setLocation(new ExperienceSearchLocationParams(34.06413001564114, -118.35870084280937, null));
        params.setSort(ExperienceSearchSort.DISTANCE);
        params.setOffset(0);
        params.setLimit(10);
        var result = dao.search(params);
        assertThat(result.getExperienceDetailsList()).usingRecursiveComparison().isEqualTo(List.of(
                new ExperienceDetails(experiences.get(4L), "user1", 0.0),
                new ExperienceDetails(experiences.get(3L), "user1", 8171.669809040829),
                new ExperienceDetails(experiences.get(5L), "user2", 9086160.393496402)
        ));

        params.setLocation(new ExperienceSearchLocationParams(34.06413001564114, -118.35870084280937, 10.0*1000));
        result = dao.search(params);
        assertThat(result.getExperienceDetailsList()).usingRecursiveComparison().isEqualTo(List.of(
                new ExperienceDetails(experiences.get(4L), "user1", 0.0),
                new ExperienceDetails(experiences.get(3L), "user1", 8171.669809040829)
        ));

        params.setLocation(new ExperienceSearchLocationParams(34.06413001564114, -118.35870084280937, 1.0*1000));
        result = dao.search(params);
        assertThat(result.getExperienceDetailsList()).usingRecursiveComparison().isEqualTo(List.of(
                new ExperienceDetails(experiences.get(4L), "user1", 0.0)
        ));

        params.setLocation(new ExperienceSearchLocationParams(0, 0, 0.0));
        result = dao.search(params);
        assertThat(result.getExperienceDetailsList()).isEmpty();
    }

    @Test
    void search_by_keywords_location() {
        var params = new ExperienceSearchParams();
        params.setKeywords("lantern");
        params.setLocation(new ExperienceSearchLocationParams(34.06413001564114, -118.35870084280937, 10.0*1000));
        params.setSort(ExperienceSearchSort.BEST_MATCH);
        params.setOffset(0);
        params.setLimit(10);
        var result = dao.search(params);
        assertThat(result.getExperienceDetailsList()).usingRecursiveComparison().isEqualTo(List.of(
                new ExperienceDetails(experiences.get(3L), "user1", 8171.669809040829)
        ));
    }

    @Test
    void sort_by_rating() {
        var params = new ExperienceSearchParams();
        params.setSort(ExperienceSearchSort.RATING);
        params.setOffset(0);
        params.setLimit(10);
        var result = dao.search(params);
        assertThat(result.getExperienceDetailsList()).usingRecursiveComparison().isEqualTo(List.of(
                new ExperienceDetails(experiences.get(4L), "user1", null),
                new ExperienceDetails(experiences.get(5L), "user2", null),
                new ExperienceDetails(experiences.get(3L), "user1", null)
        ));
    }

    @Test
    void sort_by_newest() {
        var params = new ExperienceSearchParams();
        params.setSort(ExperienceSearchSort.NEWEST);
        params.setOffset(0);
        params.setLimit(10);
        var result = dao.search(params);
        assertThat(result.getExperienceDetailsList()).usingRecursiveComparison().isEqualTo(List.of(
                new ExperienceDetails(experiences.get(5L), "user2", null),
                new ExperienceDetails(experiences.get(4L), "user1", null),
                new ExperienceDetails(experiences.get(3L), "user1", null)
        ));
    }

    @Test
    void offset_limit() {
        var params = new ExperienceSearchParams();
        params.setSort(ExperienceSearchSort.NEWEST);
        params.setOffset(0);
        params.setLimit(5);
        var result = dao.search(params);
        assertThat(result.getExperienceDetailsList()).usingRecursiveComparison().isEqualTo(List.of(
                new ExperienceDetails(experiences.get(5L), "user2", null),
                new ExperienceDetails(experiences.get(4L), "user1", null),
                new ExperienceDetails(experiences.get(3L), "user1", null)
        ));
        assertThat(result.getOffset()).isEqualTo(0);
        assertThat(result.getHasNext()).isFalse();

        params.setOffset(0);
        params.setLimit(2);
        result = dao.search(params);
        assertThat(result.getExperienceDetailsList()).usingRecursiveComparison().isEqualTo(List.of(
                new ExperienceDetails(experiences.get(5L), "user2", null),
                new ExperienceDetails(experiences.get(4L), "user1", null)
        ));
        assertThat(result.getOffset()).isEqualTo(0);
        assertThat(result.getHasNext()).isTrue();

        params.setOffset(1);
        params.setLimit(1);
        result = dao.search(params);
        assertThat(result.getExperienceDetailsList()).usingRecursiveComparison().isEqualTo(List.of(
                new ExperienceDetails(experiences.get(4L), "user1", null)
        ));
        assertThat(result.getOffset()).isEqualTo(1);
        assertThat(result.getHasNext()).isTrue();

        params.setOffset(2);
        params.setLimit(1);
        result = dao.search(params);
        assertThat(result.getExperienceDetailsList()).usingRecursiveComparison().isEqualTo(List.of(
                new ExperienceDetails(experiences.get(3L), "user1", null)
        ));
        assertThat(result.getOffset()).isEqualTo(2);
        assertThat(result.getHasNext()).isFalse();

        params.setOffset(3);
        params.setLimit(1);
        result = dao.search(params);
        assertThat(result.getExperienceDetailsList()).isEmpty();
        assertThat(result.getOffset()).isEqualTo(3);
        assertThat(result.getHasNext()).isFalse();
    }
}