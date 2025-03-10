package edu.oregonstate.cs467.travelplanner.experience.web.util;

import org.springframework.stereotype.Component;

@Component
public class ExperienceUrlGenerator {
    private final ExperienceHashIdEncoder hashIdEncoder;

    public ExperienceUrlGenerator(ExperienceHashIdEncoder hashIdEncoder) {
        this.hashIdEncoder = hashIdEncoder;
    }

    public String view(long experienceId) {
        return String.format("/experience/%s", hashIdEncoder.encode(experienceId));
    }

    public String update(long experienceId) {
        return String.format("/experience/%s/update", hashIdEncoder.encode(experienceId));
    }

    public String delete(long experienceId) {
        return String.format("/experience/%s/delete", hashIdEncoder.encode(experienceId));
    }

    public String addToTrip(long experienceId) {
        return String.format("/experience/%s/add-to-trip", hashIdEncoder.encode(experienceId));
    }
}
