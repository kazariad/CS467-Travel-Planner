package edu.oregonstate.cs467.travelplanner.experience.web.form;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ExperienceSearchFormTests {
    @Test
    void normalize_all_blank() {
        var form = new ExperienceSearchForm();
        form.normalize();
        var expected = new ExperienceSearchForm();
        expected.setSort("newest");
        expected.setOffset(0);
        assertThat(form).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void normalize_blank_keywords_locationText() {
        var form = new ExperienceSearchForm();
        form.setKeywords("\n \t");
        form.setLocationText("");
        form.setSort("bestmatch");
        form.normalize();
        var expected = new ExperienceSearchForm();
        expected.setSort("newest");
        expected.setOffset(0);
        assertThat(form).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void normalize_invalid_location() {
        var expected = new ExperienceSearchForm();
        expected.setSort("newest");
        expected.setOffset(0);

        var form = new ExperienceSearchForm();
        form.setLocationLat(0.0);
        form.setLocationLng(0.0);
        form.setDistanceMiles(-1.0);
        form.setSort("distance");
        form.normalize();
        assertThat(form).usingRecursiveComparison().isEqualTo(expected);

        form = new ExperienceSearchForm();
        form.setLocationLat(-90.1);
        form.setLocationLng(0.0);
        form.setDistanceMiles(0.0);
        form.setSort("distance");
        form.normalize();
        assertThat(form).usingRecursiveComparison().isEqualTo(expected);

        form = new ExperienceSearchForm();
        form.setLocationLat(-90.0);
        form.setLocationLng(null);
        form.setDistanceMiles(0.0);
        form.setSort("distance");
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
        form.setDistanceMiles(1.0);
        form.normalize();
        var expected = new ExperienceSearchForm();
        expected.setKeywords("abc 123");
        expected.setLocationText("xyz");
        expected.setLocationLat(-90.0);
        expected.setLocationLng(180.0);
        expected.setDistanceMiles(1.0);
        expected.setSort("distance");
        expected.setOffset(0);
        assertThat(form).usingRecursiveComparison().isEqualTo(expected);

        form.setSort("bestmatch");
        form.normalize();
        expected.setSort("bestmatch");
        assertThat(form).usingRecursiveComparison().isEqualTo(expected);

        form.setSort("rating");
        form.normalize();
        expected.setSort("rating");
        assertThat(form).usingRecursiveComparison().isEqualTo(expected);

        form.setOffset(50);
        expected.setOffset(50);
        assertThat(form).usingRecursiveComparison().isEqualTo(expected);
    }
}