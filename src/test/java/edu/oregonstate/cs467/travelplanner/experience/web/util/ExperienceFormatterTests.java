package edu.oregonstate.cs467.travelplanner.experience.web.util;

import edu.oregonstate.cs467.travelplanner.AbstractBaseTest;
import edu.oregonstate.cs467.travelplanner.experience.model.Experience;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

class ExperienceFormatterTests extends AbstractBaseTest {
    @Autowired
    private ExperienceFormatter formatter;

    @Test
    void avgRating() {
        Locale.setDefault(Locale.US);
        var exp = new Experience();
        exp.setRatingSum(5);
        exp.setRatingCnt(0);
        assertThat(formatter.avgRating(exp)).isEqualTo("N/A");

        exp.setRatingSum(5);
        exp.setRatingCnt(1);
        assertThat(formatter.avgRating(exp)).isEqualTo("5.0 / 5.0");

        exp.setRatingSum(5);
        exp.setRatingCnt(2);
        assertThat(formatter.avgRating(exp)).isEqualTo("2.5 / 5.0");

        exp.setRatingSum(467);
        exp.setRatingCnt(100);
        assertThat(formatter.avgRating(exp)).isEqualTo("4.7 / 5.0");
    }

    @Test
    void numRatings() {
        Locale.setDefault(Locale.US);
        var exp = new Experience();
        exp.setRatingCnt(0);
        assertThat(formatter.numRatings(exp)).isEqualTo("No ratings");

        exp.setRatingCnt(1);
        assertThat(formatter.numRatings(exp)).isEqualTo("1 rating");

        exp.setRatingCnt(2);
        assertThat(formatter.numRatings(exp)).isEqualTo("2 ratings");

        exp.setRatingCnt(5000);
        assertThat(formatter.numRatings(exp)).isEqualTo("5,000 ratings");
    }

    @Test
    void submittedDuration() {
        Instant now = Instant.now();
        Mockito.doReturn(now).when(clock).instant();
        var exp = new Experience();

        exp.setCreatedAt(now.plus(1, ChronoUnit.NANOS));
        assertThat(formatter.submittedDuration(exp)).isEqualTo("N/A");

        exp.setCreatedAt(now);
        assertThat(formatter.submittedDuration(exp)).isEqualTo("1 minute");

        exp.setCreatedAt(now.minus(1, ChronoUnit.MINUTES));
        assertThat(formatter.submittedDuration(exp)).isEqualTo("1 minute");

        exp.setCreatedAt(now.minus(1, ChronoUnit.HOURS).plus(1, ChronoUnit.NANOS));
        assertThat(formatter.submittedDuration(exp)).isEqualTo("59 minutes");

        exp.setCreatedAt(now.minus(1, ChronoUnit.HOURS));
        assertThat(formatter.submittedDuration(exp)).isEqualTo("1 hour");

        exp.setCreatedAt(now.minus(1, ChronoUnit.DAYS).plus(1, ChronoUnit.NANOS));
        assertThat(formatter.submittedDuration(exp)).isEqualTo("23 hours");

        exp.setCreatedAt(now.minus(1, ChronoUnit.DAYS));
        assertThat(formatter.submittedDuration(exp)).isEqualTo("1 day");

        exp.setCreatedAt(now.minus(30, ChronoUnit.DAYS).plus(1, ChronoUnit.NANOS));
        assertThat(formatter.submittedDuration(exp)).isEqualTo("29 days");

        exp.setCreatedAt(now.minus(30, ChronoUnit.DAYS));
        assertThat(formatter.submittedDuration(exp)).isEqualTo("1 month");

        exp.setCreatedAt(now.minus(360, ChronoUnit.DAYS).plus(1, ChronoUnit.NANOS));
        assertThat(formatter.submittedDuration(exp)).isEqualTo("11 months");

        exp.setCreatedAt(now.minus(365, ChronoUnit.DAYS).plus(1, ChronoUnit.NANOS));
        assertThat(formatter.submittedDuration(exp)).isEqualTo("1 year");

        exp.setCreatedAt(now.minus(365, ChronoUnit.DAYS));
        assertThat(formatter.submittedDuration(exp)).isEqualTo("1 year");

        exp.setCreatedAt(now.minus(365 * 10, ChronoUnit.DAYS));
        assertThat(formatter.submittedDuration(exp)).isEqualTo("10 years");
    }
}