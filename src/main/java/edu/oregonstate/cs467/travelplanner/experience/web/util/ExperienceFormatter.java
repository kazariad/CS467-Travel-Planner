package edu.oregonstate.cs467.travelplanner.experience.web.util;

import edu.oregonstate.cs467.travelplanner.experience.model.Experience;
import org.springframework.stereotype.Component;

@Component
public class ExperienceFormatter {
    public String getAvgRating(Experience experience) {
        if (experience.getRatingCnt() == 0) return "N/A";
        return String.format("%.1f / %.1f", (double) experience.getRatingSum() / (double) experience.getRatingCnt(), 5.0);
    }

    public String getNumRatings(Experience experience) {
        if (experience.getRatingCnt() == 0) return "No ratings";
        if (experience.getRatingCnt() == 1) return "1 rating";
        return String.format("%,d ratings", experience.getRatingCnt());
    }
}
