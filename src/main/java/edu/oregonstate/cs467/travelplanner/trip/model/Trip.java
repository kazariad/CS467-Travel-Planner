package edu.oregonstate.cs467.travelplanner.trip.model;

import edu.oregonstate.cs467.travelplanner.experience.model.Experience;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "trip")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tripId;

    @Column(name = "trip_title", nullable = false)
    private String tripTitle;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Transient
    private List<Experience> experienceList;

    public Trip() {
    }

    public Trip(Long tripId, String tripTitle, LocalDate startDate, LocalDate endDate, List<Experience> experienceList) {
        this.tripId = tripId;
        this.tripTitle = tripTitle;
        this.startDate = startDate;
        this.endDate = endDate;
        if (experienceList != null) {
            this.experienceList = experienceList;
        } else {
            this.experienceList = new ArrayList<>();
        }
    }

    // Getters and Setters
    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
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

    public List<Experience> getExperienceList() {
        return experienceList;
    }

    public void setExperienceList(List<Experience> experienceList) {
        this.experienceList = experienceList;
    }

    // Add an individual experience to the list
    public void addExperience(Experience experience) {
        if (this.experienceList == null) {
            this.experienceList = new ArrayList<>();
        }
        this.experienceList.add(experience);
    }

    // Remove an individual experience from the list
    public void removeExperience(String experience) {
        if (this.experienceList != null) {
            this.experienceList.remove(experience);
        }
    }

    @Override
    public String toString() {
        return "Trip{" +
                "tripId=" + tripId +
                ", tripTitle='" + tripTitle + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", experienceList=" + experienceList +
                '}';
    }
}
