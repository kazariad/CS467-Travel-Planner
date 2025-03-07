package edu.oregonstate.cs467.travelplanner.experience.service.dto;

import edu.oregonstate.cs467.travelplanner.experience.model.Experience;
import jakarta.annotation.Nullable;

import java.util.List;

public class ExperienceSearchResult {
    private final List<ExperienceDetails> experienceDetailsList;
    private final int offset;
    private final boolean hasNext;

    public ExperienceSearchResult(List<ExperienceDetails> experienceDetailsList, int offset, boolean hasNext) {
        this.experienceDetailsList = experienceDetailsList;
        this.offset = offset;
        this.hasNext = hasNext;
    }

    public List<ExperienceDetails> getExperienceDetailsList() {
        return experienceDetailsList;
    }

    public int getOffset() {
        return offset;
    }

    public boolean getHasNext() {
        return hasNext;
    }

    public static class ExperienceDetails {
        private final Experience experience;
        private final String author;
        private final Double distanceMeters;

        public ExperienceDetails(Experience experience, String author, @Nullable Double distanceMeters) {
            this.experience = experience;
            this.author = author;
            this.distanceMeters = distanceMeters;
        }

        public Experience getExperience() {
            return experience;
        }

        public String getAuthor() {
            return author;
        }

        public Double getDistanceMeters() {
            return distanceMeters;
        }
    }
}
