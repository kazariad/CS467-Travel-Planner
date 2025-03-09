package edu.oregonstate.cs467.travelplanner.experience.web.form;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ExperienceSearchFormSortFormatterTests {
    private ExperienceSearchFormSortFormatter formatter = new ExperienceSearchFormSortFormatter();

    @Test
    void print() {
        for (var sort : ExperienceSearchFormSort.values()) {
            assertThat(formatter.print(sort, null)).isEqualTo(sort.toString());
        }
    }

    @Test
    void parse() throws Exception {
        for (var sort : ExperienceSearchFormSort.values()) {
            assertThat(formatter.parse(sort.toString(), null)).isEqualTo(sort);
        }
        assertThat(formatter.parse("xxxx", null)).isNull();
    }
}