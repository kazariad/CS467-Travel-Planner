package edu.oregonstate.cs467.travelplanner.user.model;

import edu.oregonstate.cs467.travelplanner.experience.model.Experience;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "user", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public void eraseCredentials() {
        this.password = null;
    }
}
