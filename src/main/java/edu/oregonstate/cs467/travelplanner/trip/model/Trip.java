package edu.oregonstate.cs467.travelplanner.trip.model;

import edu.oregonstate.cs467.travelplanner.util.validation.ValidDateRange;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@ValidDateRange
@Table(name = "trip")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tripId;

    @NotNull(message = "User ID cannot be null")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotBlank(message = "Trip title cannot be blank")
    @Size(max = 100, message = "Trip title must not exceed 100 characters")
    @Column(name = "trip_title", nullable = false)
    private String tripTitle;

    @FutureOrPresent(message = "Start date must be today or in the future")
    @NotNull(message = "Start date is required")
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @FutureOrPresent(message = "End date must be today or in the future")
    @NotNull(message = "End date is required")
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @NotNull
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "trip_experience_ids", joinColumns = @JoinColumn(name = "trip_id"))
    @Column(name = "experience_id")
    private Set<Long> experienceList = new LinkedHashSet<>();

    public Trip() {
    }

    public Trip(Long tripId, Long userId, String tripTitle, LocalDate startDate, LocalDate endDate, Set<Long> experienceList,
                Instant createdAt, Instant updatedAt, Instant deletedAt) {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        this.tripId = tripId;
        this.userId = userId;
        this.tripTitle = tripTitle;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdAt = createdAt != null ? createdAt : Instant.now();
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        if (experienceList != null) {
            this.experienceList = experienceList;
        }
    }

    // Getters and Setters
    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
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
        return experienceList;
    }

    public void setExperienceList(Set<Long> experienceList) {
        this.experienceList = experienceList;
    }

    public boolean addExperience(Long experienceId) {
        return this.experienceList.add(experienceId); // Returns true if the set was changed
    }

    public boolean removeExperience(Long experienceId) {
        return this.experienceList.remove(experienceId); // Returns true if the set was changed
    }
}
