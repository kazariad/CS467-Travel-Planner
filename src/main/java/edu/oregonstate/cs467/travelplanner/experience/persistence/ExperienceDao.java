package edu.oregonstate.cs467.travelplanner.experience.persistence;

import edu.oregonstate.cs467.travelplanner.experience.model.Experience;
import edu.oregonstate.cs467.travelplanner.experience.model.GeoPoint;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Optional;

@Repository
@Validated
public class ExperienceDao {
    private final JdbcClient jdbcClient;

    private final RowMapper<Experience> rowMapper = (rs, rowNum) -> {
        Experience exp = new Experience();
        exp.setExperienceId(rs.getLong("experience_id"));
        exp.setTitle(rs.getString("title"));
        exp.setDescription(rs.getString("description"));
        exp.setEventDate(rs.getObject("event_date", LocalDate.class));
        double lat = rs.getDouble("location_lat");
        double lng = rs.getDouble("location_lng");
        exp.setLocation(new GeoPoint(lat, lng));
        exp.setAddress(rs.getString("address"));
        exp.setImageUrl(rs.getString("image_url"));
        exp.setRatingCnt(rs.getInt("rating_cnt"));
        exp.setRatingSum(rs.getInt("rating_sum"));
        exp.setUserId(rs.getLong("user_id"));
        Timestamp ts = rs.getTimestamp("created_at");
        exp.setCreatedAt(ts.toInstant());
        ts = rs.getTimestamp("updated_at");
        exp.setUpdatedAt(ts == null ? null : ts.toInstant());
        ts = rs.getTimestamp("deleted_at");
        exp.setDeletedAt(ts == null ? null : ts.toInstant());
        return exp;
    };

    public ExperienceDao(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Optional<Experience> findById(long experienceId) {
        String sql = """
                SELECT *, 
                       ST_Latitude(location) AS location_lat, 
                       ST_Longitude(location) AS location_lng 
                FROM experience
                WHERE experience_id = ?""";
        return jdbcClient.sql(sql)
                .param(experienceId)
                .query(rowMapper)
                .optional();
    }
}
