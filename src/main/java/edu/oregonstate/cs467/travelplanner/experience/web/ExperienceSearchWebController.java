package edu.oregonstate.cs467.travelplanner.experience.web;

import edu.oregonstate.cs467.travelplanner.experience.service.ExperienceService;
import edu.oregonstate.cs467.travelplanner.experience.service.dto.ExperienceSearchParams;
import edu.oregonstate.cs467.travelplanner.experience.service.dto.ExperienceSearchParams.ExperienceSearchLocationParams;
import edu.oregonstate.cs467.travelplanner.experience.service.dto.ExperienceSearchParams.ExperienceSearchSort;
import edu.oregonstate.cs467.travelplanner.experience.service.dto.ExperienceSearchResult;
import edu.oregonstate.cs467.travelplanner.experience.web.form.ExperienceSearchForm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Controller
@RequestMapping("/experience")
public class ExperienceSearchWebController {
    private final ExperienceService experienceService;
    private final String gmapsApiKey;

    public ExperienceSearchWebController(
            ExperienceService experienceService,
            @Value("${google.maps.api.key}")
            String gmapsApiKey
    ) {
        this.experienceService = experienceService;
        this.gmapsApiKey = gmapsApiKey;
    }

    @ModelAttribute
    public void initModel(Model model) {
        model.addAttribute("gmapsApiKey", gmapsApiKey);
    }

    @GetMapping(path = "/search")
    public String searchExperiences(@ModelAttribute ExperienceSearchForm searchForm) {
        searchForm.normalize();
        UriBuilder resultUrlBuilder = UriComponentsBuilder
                .fromPath("/experience/results")
                .queryParamIfPresent("keywords", Optional.ofNullable(searchForm.getKeywords()))
                .queryParamIfPresent("locationText", Optional.ofNullable(searchForm.getLocationText()))
                .queryParamIfPresent("locationLat", Optional.ofNullable(searchForm.getLocationLat()))
                .queryParamIfPresent("locationLng", Optional.ofNullable(searchForm.getLocationLng()))
                .queryParamIfPresent("distanceMiles", Optional.ofNullable(searchForm.getDistanceMiles()))
                .queryParamIfPresent("sort", Optional.ofNullable(searchForm.getSort()));
        return "redirect:" + resultUrlBuilder.toUriString();
    }

    @GetMapping(path = "/results")
    public String viewResults(@ModelAttribute("searchForm") ExperienceSearchForm searchForm, Model model) {
        searchForm.normalize();
        ExperienceSearchParams searchParams = convertSearchFormToParams(searchForm);
        ExperienceSearchResult searchResult = experienceService.search(searchParams);
        model.addAttribute("searchResult", searchResult);
        return "experience/view-experience-results";
    }

    ExperienceSearchParams convertSearchFormToParams(ExperienceSearchForm form) {
        ExperienceSearchParams params = new ExperienceSearchParams();
        params.setKeywords(form.getKeywords());

        if (form.getLocationLat() != null && form.getLocationLng() != null && form.getDistanceMiles() != null) {
            params.setLocation(new ExperienceSearchLocationParams(
                    form.getLocationLat(), form.getLocationLng(), form.getDistanceMiles() * 1609.34));
        }

        switch (form.getSort()) {
            case "bestmatch":
                params.setSort(ExperienceSearchSort.KEYWORD_MATCH);
                break;
            case "distance":
                params.setSort(ExperienceSearchSort.DISTANCE);
                break;
            case "rating":
                params.setSort(ExperienceSearchSort.RATING);
                break;
            case "newest":
                params.setSort(ExperienceSearchSort.NEWEST);
                break;
        }

        params.setOffset(form.getOffset());
        params.setLimit(10);
        return params;
    }
}
