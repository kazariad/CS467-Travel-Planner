package edu.oregonstate.cs467.travelplanner.experience.dto;

import edu.oregonstate.cs467.travelplanner.AbstractBaseTest;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class CreateUpdateExperienceDtoTests extends AbstractBaseTest {
    @Autowired
    private Validator validator;

    @Test
    void validation() {
        var dto = new CreateUpdateExperienceDto();
        dto.setTitle("");
        dto.setDescription("abcdef1234".repeat(1000));
        dto.setEventDate(null);
        dto.setLocationLat(null);
        dto.setLocationLng(180.000001);
        dto.setAddress("\t\n");
        dto.setImageUrl("absadf");
        var violations = validator.validate(dto);
        assertThat(violations.size()).isEqualTo(7);
    }
}