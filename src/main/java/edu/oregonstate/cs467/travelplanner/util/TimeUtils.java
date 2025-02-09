package edu.oregonstate.cs467.travelplanner.util;

import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class TimeUtils {
    public String formatDuration(Duration duration) {
        duration = duration.abs();

        long minutes = duration.toMinutes();
        if (minutes < 1) minutes = 1;
        if (minutes < 60) return minutes + " minute" + (minutes > 1 ? "s" : "");

        long hours = duration.toHours();
        if (hours < 24) return hours + " hour" + (hours > 1 ? "s" : "");

        long days = duration.toDays();
        if (days < 30) return days + " day" + (days > 1 ? "s" : "");

        long months = days / 30;
        if (months < 12) return months + " month" + (months > 1 ? "s" : "");

        long years = days / 365;
        if (years < 1) years = 1;
        return years + " year" + (years > 1 ? "s" : "");
    }
}
