package edu.oregonstate.cs467.travelplanner.experience.web.util;

import edu.oregonstate.cs467.travelplanner.experience.model.Experience;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

@Component
public class ExperienceFormatter {
    private final Clock clock;

    public ExperienceFormatter(Clock clock) {
        this.clock = clock;
    }

    public String location(Experience experience) {
        if (experience.getAddress() != null) return experience.getAddress();
        return String.format("%.6f, %.6f", experience.getLocationLat(), experience.getLocationLng());
    }

    public String avgRating(Experience experience) {
        if (experience.getRatingCnt() == 0) return "N/A";
        return String.format("%.1f / %.1f", (double) experience.getRatingSum() / (double) experience.getRatingCnt(), 5.0);
    }

    public String numRatings(Experience experience) {
        if (experience.getRatingCnt() == 0) return "No ratings";
        if (experience.getRatingCnt() == 1) return "1 rating";
        return String.format("%,d ratings", experience.getRatingCnt());
    }

    public String submittedDuration(Experience experience) {
        Duration duration = Duration.between(experience.getCreatedAt(), Instant.now(clock));
        if (duration.isNegative()) return "N/A";

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
