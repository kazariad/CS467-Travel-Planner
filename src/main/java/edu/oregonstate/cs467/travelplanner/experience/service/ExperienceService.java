package edu.oregonstate.cs467.travelplanner.experience.service;

import edu.oregonstate.cs467.travelplanner.experience.service.dto.CreateUpdateExperienceDto;
import edu.oregonstate.cs467.travelplanner.experience.model.Experience;
import edu.oregonstate.cs467.travelplanner.experience.persistence.ExperienceDao;
import edu.oregonstate.cs467.travelplanner.experience.persistence.ExperienceSearchDao;
import edu.oregonstate.cs467.travelplanner.experience.service.dto.ExperienceSearchParams;
import edu.oregonstate.cs467.travelplanner.experience.service.dto.ExperienceSearchResult;
import edu.oregonstate.cs467.travelplanner.user.model.User;
import edu.oregonstate.cs467.travelplanner.util.exception.ResourceNotFoundException;
import edu.oregonstate.cs467.travelplanner.util.security.AuthenticatedUserProvider;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

@Service
public class ExperienceService {
    private final AuthenticatedUserProvider authUserProvider;
    private final ExperienceDao experienceDao;
    private final ExperienceSearchDao experienceSearchDao;
    private final Clock clock;

    public ExperienceService(AuthenticatedUserProvider authUserProvider, ExperienceDao experienceDao, ExperienceSearchDao experienceSearchDao, Clock clock) {
        this.authUserProvider = authUserProvider;
        this.experienceDao = experienceDao;
        this.experienceSearchDao = experienceSearchDao;
        this.clock = clock;
    }

    public Experience getExperience(long experienceId) {
        Experience experience = experienceDao.findById(experienceId).orElse(null);
        if (experience == null || experience.getDeletedAt() != null) return null;
        return experience;
    }

    public List<Experience> getExperiencesByIds(List<Long> experienceIds) {
        return experienceDao.findByIds(experienceIds);
    }

    public List<Experience> findByUserId(long userId) {
        return experienceDao.findByUserId(userId);
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

    public ExperienceSearchResult search(ExperienceSearchParams params) {
        return experienceSearchDao.search(params);
    }
}
