package edu.oregonstate.cs467.travelplanner.user.dto;

import edu.oregonstate.cs467.travelplanner.experience.model.Experience;
import edu.oregonstate.cs467.travelplanner.user.model.User;
import java.util.List;

public class UserProfileDto {
    private String fullName;
    private String username;
    private List<Experience> experienceList;

    public UserProfileDto() {
    }

    public UserProfileDto(String fullName, String username, List<Experience> experienceList) {
        this.fullName = fullName;
        this.username = username;
        this.experienceList = experienceList;
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
        return experienceList;
    }

    public void setExperienceList(List<Experience> experienceList) {
        this.experienceList = experienceList;
    }

    public User toEntity() {
        User user = new User();
        user.setFullName(this.fullName);
        user.setUsername(this.username);
        user.setExperienceList(this.experienceList);
        return user;
    }
}
