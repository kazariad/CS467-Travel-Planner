package edu.oregonstate.cs467.travelplanner.util;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class TimeUtilTests {
    private TimeUtil timeUtil = new TimeUtil();

    @Test
    void coarseDuration() {
        assertThat(timeUtil.coarseDuration(Duration.ofNanos(0))).isEqualTo("1 minute");

        assertThat(timeUtil.coarseDuration(Duration.ofMinutes(1))).isEqualTo("1 minute");
        assertThat(timeUtil.coarseDuration(Duration.ofHours(1).minusNanos(1))).isEqualTo("59 minutes");

        assertThat(timeUtil.coarseDuration(Duration.ofHours(1))).isEqualTo("1 hour");
        assertThat(timeUtil.coarseDuration(Duration.ofDays(1).minusNanos(1))).isEqualTo("23 hours");

        assertThat(timeUtil.coarseDuration(Duration.ofDays(1))).isEqualTo("1 day");
        assertThat(timeUtil.coarseDuration(Duration.ofDays(30).minusNanos(1))).isEqualTo("29 days");

        assertThat(timeUtil.coarseDuration(Duration.ofDays(30))).isEqualTo("1 month");
        assertThat(timeUtil.coarseDuration(Duration.ofDays(360).minusNanos(1))).isEqualTo("11 months");

        assertThat(timeUtil.coarseDuration(Duration.ofDays(365).minusNanos(1))).isEqualTo("1 year");
        assertThat(timeUtil.coarseDuration(Duration.ofDays(365))).isEqualTo("1 year");
        assertThat(timeUtil.coarseDuration(Duration.ofDays(365 * 10))).isEqualTo("10 years");
    }
}