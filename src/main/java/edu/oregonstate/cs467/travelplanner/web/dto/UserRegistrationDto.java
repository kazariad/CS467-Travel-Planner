package edu.oregonstate.cs467.travelplanner.web.dto;

import edu.oregonstate.cs467.travelplanner.experience.model.Experience;
import edu.oregonstate.cs467.travelplanner.user.model.User;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
