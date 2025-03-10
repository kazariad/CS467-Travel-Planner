package edu.oregonstate.cs467.travelplanner.experience.web.util;

import edu.oregonstate.cs467.travelplanner.experience.model.Experience;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

class ExperienceFormatterTests {
    private ExperienceFormatter formatter = new ExperienceFormatter();

    @Test
    void getAvgRating() {
        Locale.setDefault(Locale.US);
        var exp = new Experience();
        exp.setRatingSum(5);
        exp.setRatingCnt(0);
        assertThat(formatter.getAvgRating(exp)).isEqualTo("N/A");

        exp.setRatingSum(5);
        exp.setRatingCnt(1);
        assertThat(formatter.getAvgRating(exp)).isEqualTo("5.0 / 5.0");

        exp.setRatingSum(5);
        exp.setRatingCnt(2);
        assertThat(formatter.getAvgRating(exp)).isEqualTo("2.5 / 5.0");

        exp.setRatingSum(467);
        exp.setRatingCnt(100);
        assertThat(formatter.getAvgRating(exp)).isEqualTo("4.7 / 5.0");
    }

    @Test
    void getNumRatings() {
        Locale.setDefault(Locale.US);
        var exp = new Experience();
        exp.setRatingCnt(0);
        assertThat(formatter.getNumRatings(exp)).isEqualTo("No ratings");

        exp.setRatingCnt(1);
        assertThat(formatter.getNumRatings(exp)).isEqualTo("1 rating");

        exp.setRatingCnt(2);
        assertThat(formatter.getNumRatings(exp)).isEqualTo("2 ratings");

        exp.setRatingCnt(5000);
        assertThat(formatter.getNumRatings(exp)).isEqualTo("5,000 ratings");
    }
}