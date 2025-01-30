package edu.oregonstate.cs467.travelplanner.experience.persistence;

import edu.oregonstate.cs467.travelplanner.experience.model.Experience;
import edu.oregonstate.cs467.travelplanner.experience.model.GeoPoint;
import jakarta.validation.Valid;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import java.sql.Timestamp;
import java.sql.Types;
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
        exp.setLocation(new GeoPoint(rs.getDouble("location_lat"), rs.getDouble("location_lng")));
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

    public void persist(@Valid Experience experience) {
        String sql = """
                INSERT INTO experience
                VALUES (NULL, ?, ?, ?, ST_PointFromText(?, 4326), ?, ?, ?, ?, ?, ?, ?, ?)""";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int idx = 1;
        jdbcClient.sql(sql)
                .param(idx++, experience.getTitle())
                .param(idx++, experience.getDescription())
                .param(idx++, experience.getEventDate(), Types.DATE)
                // %f only prints 6 decimals, %s prints with full precision
                .param(idx++, String.format("POINT(%s %s)", experience.getLocation().lat(), experience.getLocation().lng()))
                .param(idx++, experience.getAddress())
                .param(idx++, experience.getImageUrl())
                .param(idx++, experience.getRatingCnt())
                .param(idx++, experience.getRatingSum())
                .param(idx++, experience.getUserId())
                .param(idx++, experience.getCreatedAt(), Types.TIMESTAMP)
                .param(idx++, experience.getUpdatedAt(), Types.TIMESTAMP)
                .param(idx++, experience.getDeletedAt(), Types.TIMESTAMP)
                .update(keyHolder);
        experience.setExperienceId(keyHolder.getKey().longValue());
    }

    public void update(@Valid Experience experience) {
        String sql = """
                UPDATE experience SET
                    title = ?,
                    description = ?,
                    event_date = ?,
                    location = ST_PointFromText(?, 4326),
                    address = ?,
                    image_url = ?,
                    rating_cnt = ?,
                    rating_sum = ?,
                    user_id = ?,
                    created_at = ?,
                    updated_at = ?,
                    deleted_at = ?
                WHERE experience_id = ?""";

        int idx = 1;
        int affectedRows = jdbcClient.sql(sql)
                .param(idx++, experience.getTitle())
                .param(idx++, experience.getDescription())
                .param(idx++, experience.getEventDate(), Types.DATE)
                .param(idx++, String.format("POINT(%s %s)", experience.getLocation().lat(), experience.getLocation().lng()))
                .param(idx++, experience.getAddress())
                .param(idx++, experience.getImageUrl())
                .param(idx++, experience.getRatingCnt())
                .param(idx++, experience.getRatingSum())
                .param(idx++, experience.getUserId())
                .param(idx++, experience.getCreatedAt(), Types.TIMESTAMP)
                .param(idx++, experience.getUpdatedAt(), Types.TIMESTAMP)
                .param(idx++, experience.getDeletedAt(), Types.TIMESTAMP)
                .param(idx++, experience.getExperienceId())
                .update();
        if (affectedRows == 0) throw new IncorrectResultSizeDataAccessException(1, 0);
    }
}
