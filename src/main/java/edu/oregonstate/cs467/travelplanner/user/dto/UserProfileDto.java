package edu.oregonstate.cs467.travelplanner.user.dto;

import edu.oregonstate.cs467.travelplanner.experience.model.Experience;
import edu.oregonstate.cs467.travelplanner.trip.dto.TripDto;
import edu.oregonstate.cs467.travelplanner.trip.model.Trip;
import edu.oregonstate.cs467.travelplanner.user.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserProfileDto {
    private String fullName;
    private String username;
    private List<Experience> experienceList;
    private List<TripDto> tripList;
    public UserProfileDto() {
    }

    public UserProfileDto(String fullName, String username, List<Experience> experienceList, List<TripDto> tripList) {
        this.fullName = fullName;
        this.username = username;
        this.experienceList = experienceList != null ? List.copyOf(experienceList) : List.of();
        this.tripList = tripList != null ? List.copyOf(tripList) : List.of();
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Experience> getExperienceList() {
        return experienceList != null ? List.copyOf(experienceList) : List.of();
    }

    public void setExperienceList(List<Experience> experienceList) {
        this.experienceList = experienceList != null ? List.copyOf(experienceList) : List.of();
    }

    public List<TripDto> getTripList() {
        return tripList != null ? List.copyOf(tripList) : List.of();
    }

    public void setTripList(List<TripDto> tripList) {
        this.tripList = tripList != null ? List.copyOf(tripList) : List.of();
    }

    public User toEntity() {
        User user = new User();
        user.setFullName(this.fullName);
        user.setUsername(this.username);
        user.setExperienceList(this.experienceList);
        if (this.tripList != null) {
            List<Trip> trips = new ArrayList<>();
            for (TripDto tripDto : this.tripList) {
                trips.add(tripDto.toEntity());
            }
            user.setTripList(trips);
        } else {
            user.setTripList(List.of());
        }
        return user;
    }
}
