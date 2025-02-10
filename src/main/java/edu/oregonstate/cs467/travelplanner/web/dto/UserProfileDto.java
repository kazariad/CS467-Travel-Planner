package edu.oregonstate.cs467.travelplanner.web.dto;

import edu.oregonstate.cs467.travelplanner.experience.model.Experience;
import edu.oregonstate.cs467.travelplanner.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {
    private String fullName;
    private String username;
    private List<Experience> experienceList;

    public User toEntity() {
        User user = new User();
        user.setFullName(this.fullName);
        user.setUsername(this.username);
        user.setExperienceList(this.experienceList);
        return user;
    }
}
