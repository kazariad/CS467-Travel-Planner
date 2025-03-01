package edu.oregonstate.cs467.travelplanner.experience.web;

import edu.oregonstate.cs467.travelplanner.AbstractBaseTest;
import edu.oregonstate.cs467.travelplanner.experience.service.dto.ExperienceSearchParams;
import edu.oregonstate.cs467.travelplanner.experience.service.dto.ExperienceSearchParams.ExperienceSearchLocationParams;
import edu.oregonstate.cs467.travelplanner.experience.service.dto.ExperienceSearchParams.ExperienceSearchSort;
import edu.oregonstate.cs467.travelplanner.experience.web.form.ExperienceSearchForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class ExperienceWebControllerTests extends AbstractBaseTest {
    @Autowired
    private ExperienceWebController controller;

    @Test
    void convertSearchFormToParams() {
        var form = new ExperienceSearchForm();
        form.setSort("bestmatch");
        form.setOffset(0);
        var params = controller.convertSearchFormToParams(form);
        var expected = new ExperienceSearchParams();
        expected.setSort(ExperienceSearchSort.KEYWORD_MATCH);
        expected.setOffset(0);
        expected.setLimit(10);
        assertThat(params).usingRecursiveComparison().isEqualTo(expected);

        form.setKeywords("abc 123");
        form.setLocationLat(0.0);
        form.setLocationLng(0.0);
        form.setDistanceMiles(2.0);
        form.setSort("distance");
        params = controller.convertSearchFormToParams(form);
        expected.setKeywords("abc 123");
        expected.setLocation(new ExperienceSearchLocationParams(0.0, 0.0, 3218.68));
        expected.setSort(ExperienceSearchSort.DISTANCE);
        assertThat(params).usingRecursiveComparison().isEqualTo(expected);
    }
}