package edu.oregonstate.cs467.travelplanner.user.dto;

import edu.oregonstate.cs467.travelplanner.experience.model.Experience;
import edu.oregonstate.cs467.travelplanner.user.model.User;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.List;

public class UserRegistrationDto {

    private Long userId;

    @NotBlank(message = "Full Name cannot be blank")
    private String fullName;

    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    private String password;
    private Instant createdAt = Instant.now();
    private Instant updatedAt;
    private List<Experience> experienceList;

    public UserRegistrationDto() {
    }

    public UserRegistrationDto(Long userId, String fullName, String username, String password, Instant createdAt, Instant updatedAt, List<Experience> experienceList) {
        this.userId = userId;
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.experienceList = experienceList;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public List<Experience> getExperienceList() {
        return experienceList;
    }

    public void setExperienceList(List<Experience> experienceList) {
        this.experienceList = experienceList;
    }

    public User toEntity() {
        User user = new User();
        user.setUserId(this.userId);
        user.setFullName(this.fullName);
        user.setUsername(this.username);
        user.setPassword(this.password);
        if (this.createdAt == null) {
            user.setCreatedAt(Instant.now());
        } else {
            user.setCreatedAt(this.createdAt);
        }
        return user;
    }
}
