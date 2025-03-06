package edu.oregonstate.cs467.travelplanner.experience.web;

import edu.oregonstate.cs467.travelplanner.experience.service.ExperienceService;
import edu.oregonstate.cs467.travelplanner.experience.service.dto.ExperienceSearchParams;
import edu.oregonstate.cs467.travelplanner.experience.service.dto.ExperienceSearchResult;
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

        Map<Long, String> submittedDurations = new HashMap<>();
        Map<Long, String> ratings = new HashMap<>();
        searchResult.experiences().forEach(experience -> {
            submittedDurations.put(experience.getExperienceId(),
                    timeUtils.formatDuration(Duration.between(experience.getCreatedAt(), Instant.now())));
            if (experience.getRatingCnt() > 0) {
                String rating = String.format("%.1f / 5.0",
                        (double) experience.getRatingSum() / (double) experience.getRatingCnt());
                ratings.put(experience.getExperienceId(), rating);
            }
        });
        model.addAttribute("submittedDurations", submittedDurations);
        model.addAttribute("ratings", ratings);

        UriComponentsBuilder resultUriBuilder = createResultUriBuilder(searchForm);

        Map<String, String> sortByUrls = new LinkedHashMap<>();
        for (var sort : ExperienceSearchFormSort.values()) {
            if (sort == ExperienceSearchFormSort.BEST_MATCH && searchForm.getKeywords() == null) continue;
            if (sort == ExperienceSearchFormSort.DISTANCE && searchForm.getLocationLat() == null) continue;
            String url = sort == searchForm.getSort() ? null :
                    resultUriBuilder.cloneBuilder().queryParam("sort", sort).toUriString();
            sortByUrls.put(sort.getDisplayName(), url);
        }
        model.addAttribute("sortByUrls", sortByUrls);

        if (searchResult.hasNext()) {
            String nextPageUrl = resultUriBuilder.cloneBuilder()
                    .queryParam("sort", searchForm.getSort())
                    .queryParam("offset", searchResult.offset() + limit)
                    .toUriString();
            model.addAttribute("nextPageUrl", nextPageUrl);
        }

        if (searchResult.offset() > 0) {
            String prevPageUrl = resultUriBuilder.cloneBuilder()
                    .queryParam("sort", searchForm.getSort())
                    .queryParam("offset", searchResult.offset() - limit)
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
