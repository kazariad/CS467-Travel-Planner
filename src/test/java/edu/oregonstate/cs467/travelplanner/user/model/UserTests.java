package edu.oregonstate.cs467.travelplanner.user.model;

import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Set;

@SpringBootTest
public class UserTests {

    @Autowired
    private Validator validator;

    @Test
    public void validation() {
        User user = new User();
        user.setFullName("   ");
        user.setUsername("");
        user.setPassword("        ");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations.size()).isEqualTo(3);
    }
}
