package edu.oregonstate.cs467.travelplanner.experience.service;

import edu.oregonstate.cs467.travelplanner.experience.dto.CreateUpdateExperienceDto;
import edu.oregonstate.cs467.travelplanner.experience.model.Experience;
import edu.oregonstate.cs467.travelplanner.experience.persistence.ExperienceDao;
import edu.oregonstate.cs467.travelplanner.user.model.User;
import edu.oregonstate.cs467.travelplanner.util.exception.ResourceNotFoundException;
import edu.oregonstate.cs467.travelplanner.util.security.AuthenticatedUserProvider;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.time.Clock;
import java.time.Instant;
import java.util.Optional;
import java.util.ArrayList;

@Service
public class ExperienceService {
    private final AuthenticatedUserProvider authUserProvider;
    private final ExperienceDao experienceDao;
    private final Clock clock;

    public ExperienceService(AuthenticatedUserProvider authUserProvider, ExperienceDao experienceDao, Clock clock) {
        this.authUserProvider = authUserProvider;
        this.experienceDao = experienceDao;
        this.clock = clock;
    }

    public Experience getExperience(long experienceId) {
        Experience experience = experienceDao.findById(experienceId).orElse(null);
        if (experience == null || experience.getDeletedAt() != null) return null;
        return experience;
    }

    public List<Experience> getExperiencesByIds(List<Long> experienceIds) {
        if (experienceIds == null || experienceIds.isEmpty()) {
            return Collections.emptyList();
        }
        return experienceDao.findByIds(experienceIds);
    }

    public List<Experience> findByUserId(long userId) {
        return Optional.ofNullable(experienceDao.findByUserId(userId))
                .orElseGet(ArrayList::new);
    }

    public long createExperience(CreateUpdateExperienceDto experienceDto) {
        User user = authUserProvider.getUser();
        if (user == null) throw new AccessDeniedException("Access denied");

        Experience experience = experienceDto.transferTo(new Experience());
        experience.setRatingCnt(0);
        experience.setRatingSum(0);
        experience.setUserId(user.getUserId());
        experience.setCreatedAt(Instant.now(clock));
        experienceDao.persist(experience);
        return experience.getExperienceId();
    }

    public void updateExperience(long experienceId, CreateUpdateExperienceDto experienceDto) {
        Experience experience = experienceDao.findById(experienceId).orElse(null);
        if (experience == null || experience.getDeletedAt() != null) throw new ResourceNotFoundException();
        if (!authUserProvider.isUserWithId(experience.getUserId())) throw new AccessDeniedException("Access denied");

        experienceDto.transferTo(experience);
        experience.setUpdatedAt(Instant.now(clock));
        experienceDao.update(experience);
    }

    public void deleteExperience(long experienceId) {
        Experience experience = experienceDao.findById(experienceId).orElse(null);
        if (experience == null || experience.getDeletedAt() != null) return;
        if (!authUserProvider.isUserWithId(experience.getUserId())) throw new AccessDeniedException("Access denied");

        experience.setDeletedAt(Instant.now(clock));
        experienceDao.update(experience);
    }
}
