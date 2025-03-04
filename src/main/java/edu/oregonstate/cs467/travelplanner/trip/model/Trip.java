package edu.oregonstate.cs467.travelplanner.trip.model;

import edu.oregonstate.cs467.travelplanner.user.model.User;
import edu.oregonstate.cs467.travelplanner.util.validation.ValidDateRange;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Represents a planned or completed trip for a user. Each trip is associated with a specific user
 * and includes details such as the title, start and end dates, creation and update timestamps,
 * and a list of associated experiences.

 * Relationships:
 * - Many trips can be associated with a single user (Many-to-One relationship).
 * - A trip can contain multiple experiences (not directly persisted).
 */
@Entity
@ValidDateRange
@Table(name = "trip")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tripId;

    // Many trips to one user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank(message = "Trip title cannot be blank")
    @Size(max = 100, message = "Trip title must not exceed 100 characters")
    @Column(name = "trip_title", nullable = false)
    private String tripTitle;

    @FutureOrPresent(message = "Start date must not be in the past")
    @NotNull(message = "Start date is required")
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @FutureOrPresent(message = "End date must not be in the past")
    @NotNull(message = "End date is required")
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @NotNull(message = "Creation timestamp cannot be null")
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @ElementCollection
    @CollectionTable(
            name = "trip_experience",
            joinColumns = @JoinColumn(name = "trip_id")
    )
    @Column(name = "experience_id")
    private Set<Long> experienceList;

    public Trip() {
        this.createdAt = Instant.now();
        this.experienceList = new LinkedHashSet<>();
    }

    public Trip(Long tripId, User user, String tripTitle, LocalDate startDate, LocalDate endDate, Set<Long> experienceList,
                Instant createdAt, Instant updatedAt, Instant deletedAt) {
        this.tripId = tripId;
        this.user = user;
        this.tripTitle = tripTitle;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdAt = (createdAt == null) ? Instant.now() : createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.experienceList = experienceList != null ? Set.copyOf(experienceList) : Set.of();
    }

    // Getters and Setters
    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTripTitle() {
        return tripTitle;
    }

    public void setTripTitle(String tripTitle) {
        this.tripTitle = tripTitle;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        if (this.endDate != null && startDate.isAfter(this.endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        if (this.startDate != null && endDate.isBefore(this.startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
        this.endDate = endDate;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Set<Long> getExperienceList() {
        if (this.experienceList == null) {
            this.experienceList = new LinkedHashSet<>();
        }
        return this.experienceList;
    }

    public void setExperienceList(Set<Long> experienceList) {
        this.experienceList = experienceList != null ? new LinkedHashSet<>(experienceList) : new LinkedHashSet<>();
    }
}
