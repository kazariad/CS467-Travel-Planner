package edu.oregonstate.cs467.travelplanner.experience.persistence;

import edu.oregonstate.cs467.travelplanner.experience.model.Experience;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;

@Component
public class ExperienceRowMapper implements RowMapper<Experience> {
    @Override
    public Experience mapRow(ResultSet rs, int rowNum) throws SQLException {
        Experience exp = new Experience();
        exp.setExperienceId(rs.getLong("experience_id"));
        exp.setTitle(rs.getString("title"));
        exp.setDescription(rs.getString("description"));
        exp.setEventDate(rs.getObject("event_date", LocalDate.class));
        exp.setLocationLat(rs.getDouble("location_lat"));
        exp.setLocationLng(rs.getDouble("location_lng"));
        exp.setAddress(rs.getString("address"));
        exp.setPlaceId(rs.getString("place_id"));
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
    }
}
