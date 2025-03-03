package edu.oregonstate.cs467.travelplanner.user.model;

import edu.oregonstate.cs467.travelplanner.experience.model.Experience;
import edu.oregonstate.cs467.travelplanner.trip.model.Trip;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents a user within the system. This entity is mapped to the "user" table and includes
 * user-related data such as full name, username, password, and timestamps for creation and updates.

 * Relationships:
 * - One-To-Many: Each user can have multiple trips associated with them.
 * - Transient: Each user can have a list of experiences that are not directly persisted in the database.
 */
@Entity
@Table(name = "user", uniqueConstraints = @UniqueConstraint(columnNames = "username"))

public class User implements UserDetails, CredentialsContainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, updatable = false)
    private Long userId;

    @Column(name = "full_name", nullable = false, length = 255)
    @NotBlank(message = "Full Name cannot be blank")
    private String fullName;

    @Column(name = "username", nullable = false, length = 50, unique = true)
    @NotBlank(message = "Username cannot be blank")
    private String username;

    @Column(name = "password", nullable = false, length = 255)
    @NotBlank(message = "Password cannot be blank")
    private String password;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Transient
    private List<Experience> experienceList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Trip> tripList = new ArrayList<>();

    public User() {
    }

    public User(Long userId, String fullName, String username, String password, Instant createdAt, Instant updatedAt, List<Experience> experienceList, List<Trip> tripList) {
        this.userId = userId;
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.experienceList = experienceList;
        this.tripList = tripList;
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

    public List<Trip> getTripList() {
        return tripList;
    }

    public void setTripList(List<Trip> tripList) {
        this.tripList = tripList;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public void eraseCredentials() {
        this.password = null;
    }
}
