package edu.oregonstate.cs467.travelplanner.user.model;

import edu.oregonstate.cs467.travelplanner.AbstractBaseTest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTests extends AbstractBaseTest {

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
