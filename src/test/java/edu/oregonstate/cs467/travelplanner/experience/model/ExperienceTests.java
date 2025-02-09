package edu.oregonstate.cs467.travelplanner.experience.model;

import edu.oregonstate.cs467.travelplanner.AbstractBaseTest;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class ExperienceTests extends AbstractBaseTest {
    @Autowired
    private Validator validator;

    @Test
    void validation() {
        var exp = new Experience();
        exp.setTitle("   ");
        exp.setDescription("abcdef1234".repeat(1000));
        exp.setAddress("\t\n");
        exp.setImageUrl("absadf");
        exp.setRatingCnt(-1);
        exp.setRatingSum(-1);
        exp.setUserId(0);
        var violations = validator.validate(exp);
        assertThat(violations.size()).isEqualTo(10);
    }
}