package edu.oregonstate.cs467.travelplanner.experience.model;

import edu.oregonstate.cs467.travelplanner.AbstractBaseTest;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class GeoPointTests extends AbstractBaseTest {
    @Autowired
    private Validator validator;

    @Test
    void validation() {
        var violations = validator.validate(new GeoPoint(-90.000000001, -180.000000001));
        assertThat(violations.size()).isEqualTo(2);
        violations = validator.validate(new GeoPoint(-90.00000000, -180.00000000));
        assertThat(violations.size()).isEqualTo(0);
        violations = validator.validate(new GeoPoint(90.00000000, 180.00000000));
        assertThat(violations.size()).isEqualTo(0);
        violations = validator.validate(new GeoPoint(90.000000001, 180.000000001));
        assertThat(violations.size()).isEqualTo(2);
    }
}