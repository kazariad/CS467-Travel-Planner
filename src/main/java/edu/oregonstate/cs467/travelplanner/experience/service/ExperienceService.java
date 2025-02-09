package edu.oregonstate.cs467.travelplanner.experience.service;

import edu.oregonstate.cs467.travelplanner.experience.model.Experience;
import edu.oregonstate.cs467.travelplanner.experience.persistence.ExperienceDao;
import org.springframework.stereotype.Service;

@Service
public class ExperienceService {
    private final ExperienceDao experienceDao;

    public ExperienceService(ExperienceDao experienceDao) {
        this.experienceDao = experienceDao;
    }

    public Experience getExperience(long experienceId) {
        Experience experience = experienceDao.findById(experienceId).orElse(null);
        if (experience == null || experience.getDeletedAt() != null) return null;
        return experience;
    }
}
