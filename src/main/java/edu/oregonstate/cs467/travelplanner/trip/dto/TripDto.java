package edu.oregonstate.cs467.travelplanner.trip.dto;

import edu.oregonstate.cs467.travelplanner.trip.model.Trip;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TripDto {
    private String tripTitle;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Long> experienceList; // IDs of related experiences

    public TripDto() {
        this.experienceList = new ArrayList<>();
    }

    public TripDto(String tripTitle, LocalDate startDate, LocalDate endDate, List<Long> experienceList) {
        this.tripTitle = tripTitle;
        this.startDate = startDate;
        this.endDate = endDate;
        this.experienceList = (experienceList != null) ? experienceList : new ArrayList<>();
    }

    // Getters and setters
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
    public List<Long> getExperienceList() {
        if (experienceList == null) {
            experienceList = new ArrayList<>();
        }
        return experienceList;
    }
    public void setExperienceList(List<Long> experienceList) {
        this.experienceList = experienceList;
    }

    /**
     * Converts the current TripDto object into a Trip entity object.
     *
     * @return a Trip entity object populated with the corresponding data
     *         from this TripDto instance.
     */
    public Trip toEntity() {
        Trip trip = new Trip();
        trip.setTripTitle(this.getTripTitle());
        trip.setStartDate(this.getStartDate());
        trip.setEndDate(this.getEndDate());
        trip.setExperienceList(this.getExperienceList());
        trip.validateTripDates();
        return trip;
    }
}