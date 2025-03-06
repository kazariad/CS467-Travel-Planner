package edu.oregonstate.cs467.travelplanner.experience.persistence;

import edu.oregonstate.cs467.travelplanner.experience.model.Experience;
import edu.oregonstate.cs467.travelplanner.experience.service.dto.ExperienceSearchParams;
import edu.oregonstate.cs467.travelplanner.experience.service.dto.ExperienceSearchParams.ExperienceSearchSort;
import edu.oregonstate.cs467.travelplanner.experience.service.dto.ExperienceSearchResult;
import jakarta.validation.Valid;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Validated
public class ExperienceSearchDao {
    private final JdbcClient jdbcClient;
    private final RowMapper<Experience> rowMapper;

    public ExperienceSearchDao(JdbcClient jdbcClient, RowMapper<Experience> rowMapper) {
        this.jdbcClient = jdbcClient;
        this.rowMapper = rowMapper;
    }

    public ExperienceSearchResult search(@Valid ExperienceSearchParams params) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT *, ST_Latitude(location) AS location_lat, ST_Longitude(location) AS location_lng");
        sb.append(",\nuser.username");
        if (params.getKeywords() != null) {
            sb.append(",\nMATCH(title, description, address) AGAINST (? IN NATURAL LANGUAGE MODE) AS match_score");
        }
        if (params.getLocation() != null) {
            sb.append(",\nST_Distance_Sphere(location, ST_PointFromText(?, 4326)) AS distance");
        }
        if (params.getSort() == ExperienceSearchSort.RATING) {
            sb.append(",\n(rating_sum / rating_cnt) AS rating");
        }
        sb.append("\nFROM experience");
        sb.append("\nLEFT JOIN user ON experience.user_id = user.user_id");
        sb.append("\nWHERE deleted_at IS NULL");
        if (params.getLocation() != null && params.getLocation().distanceMeters() != null) {
            sb.append("\nAND ST_Distance_Sphere(location, ST_PointFromText(?, 4326)) <= ?");
        }
        if (params.getKeywords() != null) {
            sb.append("\nAND MATCH(title, description, address) AGAINST (? IN NATURAL LANGUAGE MODE) > 0");
        }
        sb.append("\nORDER BY ");
        switch (params.getSort()) {
            case BEST_MATCH -> sb.append("match_score DESC");
            case DISTANCE -> sb.append("distance ASC");
            case RATING -> sb.append("rating DESC");
            case NEWEST -> sb.append("experience.created_at DESC");
        }
        sb.append("\nLIMIT ? OFFSET ?");

        var ss = jdbcClient.sql(sb.toString());
        int idx = 1;
        if (params.getKeywords() != null) ss = ss.param(idx++, params.getKeywords());
        if (params.getLocation() != null) {
            String point = String.format("POINT(%s %s)", params.getLocation().lat(), params.getLocation().lng());
            ss = ss.param(idx++, point);
            if (params.getLocation().distanceMeters() != null) {
                ss = ss.param(idx++, point);
                ss = ss.param(idx++, params.getLocation().distanceMeters());
            }
        }
        if (params.getKeywords() != null) ss = ss.param(idx++, params.getKeywords());
        ss = ss.param(idx++, params.getLimit() + 1);
        ss = ss.param(idx++, params.getOffset());

        Map<Long, String> experienceIdAuthors = new HashMap<>();
        List<Experience> experiences = ss.query((rs, rowNum) -> {
            Experience experience = rowMapper.mapRow(rs, rowNum);
            String username = rs.getString("username");
            experienceIdAuthors.put(experience.getExperienceId(), username);
            return experience;
        }).list();

        boolean hasNext = experiences.size() > params.getLimit();
        if (hasNext) experienceIdAuthors.remove(experiences.removeLast().getExperienceId());

        return new ExperienceSearchResult(experiences, experienceIdAuthors, params.getOffset(), hasNext);
    }
}
