package edu.oregonstate.cs467.travelplanner.experience.persistence;

import edu.oregonstate.cs467.travelplanner.experience.model.Experience;
import edu.oregonstate.cs467.travelplanner.experience.service.dto.ExperienceSearchParams;
import edu.oregonstate.cs467.travelplanner.experience.service.dto.ExperienceSearchParams.ExperienceSearchSort;
import edu.oregonstate.cs467.travelplanner.experience.service.dto.ExperienceSearchResult;
import edu.oregonstate.cs467.travelplanner.experience.service.dto.ExperienceSearchResult.ExperienceDetails;
import jakarta.validation.Valid;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import java.util.List;

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
        sb.append("SELECT t1.*, ST_Latitude(t1.location) AS location_lat, ST_Longitude(t1.location) AS location_lng");
        sb.append(",\nt2.username");
        if (params.getKeywords() != null) {
            sb.append(",\nMATCH(t1.title, t1.description, t1.address) AGAINST (? IN NATURAL LANGUAGE MODE) AS match_score");
        }
        if (params.getLocation() != null) {
            sb.append(",\nST_Distance_Sphere(t1.location, ST_PointFromText(?, 4326)) AS distance");
        }
        if (params.getSort() == ExperienceSearchSort.RATING) {
            sb.append(",\n(t1.rating_sum / t1.rating_cnt) AS rating");
        }
        sb.append("\nFROM experience AS t1");
        sb.append("\nLEFT JOIN user AS t2 ON t1.user_id = t2.user_id");
        sb.append("\nWHERE t1.deleted_at IS NULL");
        if (params.getLocation() != null && params.getLocation().distanceMeters() != null) {
            sb.append("\nAND ST_Distance_Sphere(t1.location, ST_PointFromText(?, 4326)) <= ?");
        }
        if (params.getKeywords() != null) {
            sb.append("\nAND MATCH(t1.title, t1.description, t1.address) AGAINST (? IN NATURAL LANGUAGE MODE) > 0");
        }
        sb.append("\nORDER BY ");
        switch (params.getSort()) {
            case BEST_MATCH -> sb.append("match_score DESC");
            case DISTANCE -> sb.append("distance ASC");
            case RATING -> sb.append("rating DESC");
            case NEWEST -> sb.append("t1.created_at DESC");
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

        List<ExperienceDetails> experienceDetailsList = ss.query((rs, rowNum) -> {
            Experience experience = rowMapper.mapRow(rs, rowNum);
            String username = rs.getString("username");
            Double distanceMeters = params.getLocation() != null ? rs.getDouble("distance") : null;
            return new ExperienceDetails(experience, username, distanceMeters);
        }).list();

        boolean hasNext = experienceDetailsList.size() > params.getLimit();
        if (hasNext) experienceDetailsList.removeLast();
        return new ExperienceSearchResult(experienceDetailsList, params.getOffset(), hasNext);
    }
}
