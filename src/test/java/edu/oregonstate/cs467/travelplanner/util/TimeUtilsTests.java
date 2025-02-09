package edu.oregonstate.cs467.travelplanner.util;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class TimeUtilsTests {
    private TimeUtils timeUtils = new TimeUtils();

    @Test
    void formatDuration() {
        assertThat(timeUtils.formatDuration(Duration.ofNanos(0))).isEqualTo("1 minute");

        assertThat(timeUtils.formatDuration(Duration.ofMinutes(1))).isEqualTo("1 minute");
        assertThat(timeUtils.formatDuration(Duration.ofHours(1).minusNanos(1))).isEqualTo("59 minutes");

        assertThat(timeUtils.formatDuration(Duration.ofHours(1))).isEqualTo("1 hour");
        assertThat(timeUtils.formatDuration(Duration.ofDays(1).minusNanos(1))).isEqualTo("23 hours");

        assertThat(timeUtils.formatDuration(Duration.ofDays(1))).isEqualTo("1 day");
        assertThat(timeUtils.formatDuration(Duration.ofDays(30).minusNanos(1))).isEqualTo("29 days");

        assertThat(timeUtils.formatDuration(Duration.ofDays(30))).isEqualTo("1 month");
        assertThat(timeUtils.formatDuration(Duration.ofDays(360).minusNanos(1))).isEqualTo("11 months");

        assertThat(timeUtils.formatDuration(Duration.ofDays(365).minusNanos(1))).isEqualTo("1 year");
        assertThat(timeUtils.formatDuration(Duration.ofDays(365))).isEqualTo("1 year");
        assertThat(timeUtils.formatDuration(Duration.ofDays(365 * 10))).isEqualTo("10 years");
    }
}