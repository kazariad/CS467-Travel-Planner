package edu.oregonstate.cs467.travelplanner.experience.service.dto;

import edu.oregonstate.cs467.travelplanner.experience.model.Experience;

import java.util.List;
import java.util.Map;

public record ExperienceSearchResult(
        List<Experience> experiences,
        Map<Long, String> experienceIdAuthors,
        int offset,
        boolean hasNext
) {}
