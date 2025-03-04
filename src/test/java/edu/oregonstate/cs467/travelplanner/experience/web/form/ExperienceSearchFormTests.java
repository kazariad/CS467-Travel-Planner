package edu.oregonstate.cs467.travelplanner.experience.web.form;

import edu.oregonstate.cs467.travelplanner.experience.service.dto.ExperienceSearchParams;
import edu.oregonstate.cs467.travelplanner.experience.service.dto.ExperienceSearchParams.ExperienceSearchLocationParams;
import edu.oregonstate.cs467.travelplanner.experience.service.dto.ExperienceSearchParams.ExperienceSearchSort;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ExperienceSearchFormTests {
    @Test
    void normalize_all_blank() {
        var form = new ExperienceSearchForm();
        form.normalize();
        var expected = new ExperienceSearchForm();
        expected.setSort(ExperienceSearchFormSort.NEWEST);
        expected.setOffset(0);
        assertThat(form).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void normalize_blank_keywords_locationText() {
        var form = new ExperienceSearchForm();
        form.setKeywords("\n \t");
        form.setLocationText("");
        form.setSort(ExperienceSearchFormSort.BEST_MATCH);
        form.normalize();
        var expected = new ExperienceSearchForm();
        expected.setSort(ExperienceSearchFormSort.NEWEST);
        expected.setOffset(0);
        assertThat(form).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void normalize_invalid_location() {
        var expected = new ExperienceSearchForm();
        expected.setSort(ExperienceSearchFormSort.NEWEST);
        expected.setOffset(0);

        var form = new ExperienceSearchForm();
        form.setLocationLat(0.0);
        form.setLocationLng(0.0);
        form.setDistanceMiles(-1);
        form.setSort(ExperienceSearchFormSort.DISTANCE);
        form.normalize();
        assertThat(form).usingRecursiveComparison().isEqualTo(expected);

        form = new ExperienceSearchForm();
        form.setLocationLat(-90.1);
        form.setLocationLng(0.0);
        form.setDistanceMiles(0);
        form.setSort(ExperienceSearchFormSort.DISTANCE);
        form.normalize();
        assertThat(form).usingRecursiveComparison().isEqualTo(expected);

        form = new ExperienceSearchForm();
        form.setLocationLat(-90.0);
        form.setLocationLng(null);
        form.setDistanceMiles(0);
        form.setSort(ExperienceSearchFormSort.DISTANCE);
        form.normalize();
        assertThat(form).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void normalize_all_valid() {
        var form = new ExperienceSearchForm();
        form.setKeywords(" abc 123 \t");
        form.setLocationText("xyz");
        form.setLocationLat(-90.0);
        form.setLocationLng(180.0);
        form.setDistanceMiles(1);
        form.normalize();
        var expected = new ExperienceSearchForm();
        expected.setKeywords("abc 123");
        expected.setLocationText("xyz");
        expected.setLocationLat(-90.0);
        expected.setLocationLng(180.0);
        expected.setDistanceMiles(1);
        expected.setSort(ExperienceSearchFormSort.DISTANCE);
        expected.setOffset(0);
        assertThat(form).usingRecursiveComparison().isEqualTo(expected);

        form.setSort(ExperienceSearchFormSort.BEST_MATCH);
        form.normalize();
        expected.setSort(ExperienceSearchFormSort.BEST_MATCH);
        assertThat(form).usingRecursiveComparison().isEqualTo(expected);

        form.setSort(ExperienceSearchFormSort.RATING);
        form.normalize();
        expected.setSort(ExperienceSearchFormSort.RATING);
        assertThat(form).usingRecursiveComparison().isEqualTo(expected);

        form.setOffset(50);
        expected.setOffset(50);
        assertThat(form).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void convertToSearchParams() {
        var form = new ExperienceSearchForm();
        form.setSort(ExperienceSearchFormSort.BEST_MATCH);
        form.setOffset(0);
        var params = form.convertToSearchParams();
        var expected = new ExperienceSearchParams();
        expected.setSort(ExperienceSearchSort.BEST_MATCH);
        expected.setOffset(0);
        assertThat(params).usingRecursiveComparison().isEqualTo(expected);

        form.setKeywords("abc 123");
        form.setLocationLat(0.0);
        form.setLocationLng(0.0);
        form.setDistanceMiles(2);
        form.setSort(ExperienceSearchFormSort.DISTANCE);
        params = form.convertToSearchParams();
        expected.setKeywords("abc 123");
        expected.setLocation(new ExperienceSearchLocationParams(0.0, 0.0, 3218.68));
        expected.setSort(ExperienceSearchSort.DISTANCE);
        assertThat(params).usingRecursiveComparison().isEqualTo(expected);
    }
}