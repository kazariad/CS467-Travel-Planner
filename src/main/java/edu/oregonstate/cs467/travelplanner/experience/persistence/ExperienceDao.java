package edu.oregonstate.cs467.travelplanner.experience.persistence;

import edu.oregonstate.cs467.travelplanner.experience.model.Experience;
import jakarta.validation.Valid;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import java.sql.Types;
import java.util.List;
import java.util.Optional;

/**
 * Data access object for Experience objects, supporting basic CRUD and other specialized functions.
 */
@Repository
// activate validation for @Valid method arguments
@Validated
public class ExperienceDao {
    private final JdbcClient jdbcClient;
    private final RowMapper<Experience> rowMapper;

    public ExperienceDao(JdbcClient jdbcClient, RowMapper<Experience> rowMapper) {
        this.jdbcClient = jdbcClient;
        this.rowMapper = rowMapper;
    }

    /**
     * Retrieve an Experience by its ID.
     * @param experienceId
     * @return {@link Optional} containing the Experience, or empty if no matching results
     */
    public Optional<Experience> findById(long experienceId) {
        // use SQL functions ST_Latitude/Longitude to extract values as doubles
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

    /**
     * Save a new Experience in the database.  Experience's ID will be updated with the generated key.
     * @param experience
     */
    public void persist(@Valid Experience experience) {
        if (experience.getExperienceId() != null) throw new IllegalArgumentException("Experience ID not null");
        String sql = """
                INSERT INTO experience
                VALUES (NULL, ?, ?, ?, ST_PointFromText(?, 4326), ?, ?, ?, ?, ?, ?, ?, ?, ?)""";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int idx = 1;
        jdbcClient.sql(sql)
                .param(idx++, experience.getTitle())
                .param(idx++, experience.getDescription())
                .param(idx++, experience.getEventDate(), Types.DATE)
                // %f only prints 6 decimals, %s prints with full precision
                .param(idx++, String.format("POINT(%s %s)", experience.getLocationLat(), experience.getLocationLng()))
                .param(idx++, experience.getAddress())
                .param(idx++, experience.getPlaceId())
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

    /**
     * Update an existing Experience in the database.
     * @param experience
     */
    public void update(@Valid Experience experience) {
        String sql = """
                UPDATE experience SET
                    title = ?,
                    description = ?,
                    event_date = ?,
                    location = ST_PointFromText(?, 4326),
                    address = ?,
                    place_id = ?,
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
                .param(idx++, String.format("POINT(%s %s)", experience.getLocationLat(), experience.getLocationLng()))
                .param(idx++, experience.getAddress())
                .param(idx++, experience.getPlaceId())
                .param(idx++, experience.getImageUrl())
                .param(idx++, experience.getRatingCnt())
                .param(idx++, experience.getRatingSum())
                .param(idx++, experience.getUserId())
                .param(idx++, experience.getCreatedAt(), Types.TIMESTAMP)
                .param(idx++, experience.getUpdatedAt(), Types.TIMESTAMP)
                .param(idx++, experience.getDeletedAt(), Types.TIMESTAMP)
                .param(idx++, experience.getExperienceId())
                .update();
        // make sure that an Experience was actually matched and updated
        if (affectedRows == 0) throw new IncorrectResultSizeDataAccessException(1, 0);
    }

    /**
     * Retrieves a list of experiences associated with a specific user ID, excluding soft-deleted records
     * @param userId The ID of the user whose experiences are to be retrieved.
     * @return A list of Experience objects belonging to the user.
     */
    public List<Experience> findByUserId(long userId) {
        String sql = """
                SELECT *, 
                       ST_Latitude(location) AS location_lat,
                       ST_Longitude(location) AS location_lng 
                FROM experience
                WHERE user_id = ?
                AND deleted_at IS NULL""";
        return jdbcClient.sql(sql)
                .param(userId)
                .query(rowMapper).list();
    }
}
