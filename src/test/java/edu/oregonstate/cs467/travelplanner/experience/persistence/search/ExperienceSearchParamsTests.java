package edu.oregonstate.cs467.travelplanner.experience.persistence.search;

import edu.oregonstate.cs467.travelplanner.AbstractBaseTest;
import edu.oregonstate.cs467.travelplanner.experience.persistence.search.ExperienceSearchParams.ExperienceSearchLocationParams;
import edu.oregonstate.cs467.travelplanner.experience.persistence.search.ExperienceSearchParams.ExperienceSearchSort;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class ExperienceSearchParamsTests extends AbstractBaseTest {
    @Autowired
    private Validator validator;

    @Test
    void validation() {
        var params = new ExperienceSearchParams();
        params.setKeywords("  ");
        params.setLocation(new ExperienceSearchLocationParams(-90.01, 180.01, -1));
        params.setSort(null);
        params.setOffset(-1);
        params.setLimit(0);
        var violations = validator.validate(params);
        assertThat(violations.size()).isEqualTo(7);

        params.setKeywords(null);
        params.setLocation(null);
        params.setSort(ExperienceSearchSort.KEYWORD_MATCH);
        params.setOffset(1);
        params.setLimit(1);
        violations = validator.validate(params);
        assertThat(violations.size()).isEqualTo(1);

        params.setSort(ExperienceSearchSort.DISTANCE);
        violations = validator.validate(params);
        assertThat(violations.size()).isEqualTo(1);
    }
}