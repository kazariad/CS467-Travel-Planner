package edu.oregonstate.cs467.travelplanner.experience.web;

import edu.oregonstate.cs467.travelplanner.experience.model.Experience;
import edu.oregonstate.cs467.travelplanner.experience.service.ExperienceService;
import edu.oregonstate.cs467.travelplanner.experience.service.dto.ExperienceSearchParams;
import edu.oregonstate.cs467.travelplanner.experience.service.dto.ExperienceSearchResult;
import edu.oregonstate.cs467.travelplanner.experience.service.dto.ExperienceSearchResult.ExperienceDetails;
import edu.oregonstate.cs467.travelplanner.experience.web.form.ExperienceSearchForm;
import edu.oregonstate.cs467.travelplanner.experience.web.form.ExperienceSearchFormSort;
import edu.oregonstate.cs467.travelplanner.util.TimeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/experience")
public class ExperienceSearchWebController {
    private final ExperienceService experienceService;
    private final Formatter<ExperienceSearchFormSort> searchFormSortFormatter;
    private final TimeUtils timeUtils;
    private final String gmapsApiKey;
    private final int limit;

    public ExperienceSearchWebController(
            ExperienceService experienceService,
            Formatter<ExperienceSearchFormSort> searchFormSortFormatter,
            TimeUtils timeUtils,
            @Value("${google.maps.api.key}")
            String gmapsApiKey,
            @Value("10")
            int limit
    ) {
        this.experienceService = experienceService;
        this.searchFormSortFormatter = searchFormSortFormatter;
        this.timeUtils = timeUtils;
        this.gmapsApiKey = gmapsApiKey;
        this.limit = limit;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addCustomFormatter(searchFormSortFormatter);
    }

    @ModelAttribute
    public void initModel(Model model) {
        model.addAttribute("gmapsApiKey", gmapsApiKey);
    }

    @GetMapping(path = "/search")
    public String searchExperiences(@ModelAttribute ExperienceSearchForm searchForm) {
        searchForm.normalize();
        UriComponentsBuilder resultUriBuilder = createResultUriBuilder(searchForm)
                .queryParam("sort", searchForm.getSort());
        return "redirect:" + resultUriBuilder.toUriString();
    }

    @GetMapping(path = "/results")
    public String viewResults(@ModelAttribute("searchForm") ExperienceSearchForm searchForm, Model model) {
        searchForm.normalize();
        ExperienceSearchParams searchParams = searchForm.convertToSearchParams();
        searchParams.setLimit(limit);

        ExperienceSearchResult searchResult = experienceService.search(searchParams);
        model.addAttribute("searchResult", searchResult);

        Map<Long, String> submittedDurationMap = new HashMap<>();
        Map<Long, String> labelMap = new HashMap<>();
        char c = 'A';
        for (ExperienceDetails details : searchResult.getExperienceDetailsList()) {
            Experience experience = details.getExperience();
            submittedDurationMap.put(experience.getExperienceId(),
                    timeUtils.formatDuration(Duration.between(experience.getCreatedAt(), Instant.now())));
            labelMap.put(experience.getExperienceId(), String.valueOf(c++));
            if (c > 'Z') c = 'A';
        }
        model.addAttribute("submittedDurationMap", submittedDurationMap);
        model.addAttribute("labelMap", labelMap);

        UriComponentsBuilder resultUriBuilder = createResultUriBuilder(searchForm);

        Map<String, String> sortByUrlMap = new LinkedHashMap<>();
        for (var sort : ExperienceSearchFormSort.values()) {
            if (sort == ExperienceSearchFormSort.BEST_MATCH && searchForm.getKeywords() == null) continue;
            if (sort == ExperienceSearchFormSort.DISTANCE && searchForm.getLocationLat() == null) continue;
            String url = sort == searchForm.getSort() ? null :
                    resultUriBuilder.cloneBuilder().queryParam("sort", sort).toUriString();
            sortByUrlMap.put(sort.getDisplayName(), url);
        }
        model.addAttribute("sortByUrlMap", sortByUrlMap);

        if (searchResult.getHasNext()) {
            String nextPageUrl = resultUriBuilder.cloneBuilder()
                    .queryParam("sort", searchForm.getSort())
                    .queryParam("offset", searchResult.getOffset() + limit)
                    .toUriString();
            model.addAttribute("nextPageUrl", nextPageUrl);
        }

        if (searchResult.getOffset() > 0) {
            String prevPageUrl = resultUriBuilder.cloneBuilder()
                    .queryParam("sort", searchForm.getSort())
                    .queryParam("offset", searchResult.getOffset() - limit)
                    .toUriString();
            model.addAttribute("prevPageUrl", prevPageUrl);
        }

        return "experience/view-experience-results";
    }

    UriComponentsBuilder createResultUriBuilder(ExperienceSearchForm searchForm) {
        return UriComponentsBuilder.fromPath("/experience/results")
                .queryParamIfPresent("keywords", Optional.ofNullable(searchForm.getKeywords()))
                .queryParamIfPresent("locationText", Optional.ofNullable(searchForm.getLocationText()))
                .queryParamIfPresent("locationLat", Optional.ofNullable(searchForm.getLocationLat()))
                .queryParamIfPresent("locationLng", Optional.ofNullable(searchForm.getLocationLng()))
                .queryParamIfPresent("distanceMiles", Optional.ofNullable(searchForm.getDistanceMiles()));
    }
}
