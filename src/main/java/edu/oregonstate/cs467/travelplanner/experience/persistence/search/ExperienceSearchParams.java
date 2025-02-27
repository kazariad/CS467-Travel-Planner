package edu.oregonstate.cs467.travelplanner.experience.persistence.search;

import edu.oregonstate.cs467.travelplanner.util.validation.NotBlankNull;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public class ExperienceSearchParams {
    public record ExperienceSearchLocationParams(
            @Min(-90) @Max(90)
            double lat,
            @Min(-180) @Max(180)
            double lng,
            @PositiveOrZero
            double distanceMeters
    ) {}

    public enum ExperienceSearchSort {
        KEYWORD_MATCH, DISTANCE, EVENT_DATE, RATING, NEWEST
    }

    @NotBlankNull
    private String keywords;

    @Valid
    private ExperienceSearchLocationParams location;

    @NotNull
    private ExperienceSearchSort sort;

    @PositiveOrZero
    private int offset;

    @Positive
    private int limit;

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public ExperienceSearchLocationParams getLocation() {
        return location;
    }

    public void setLocation(ExperienceSearchLocationParams location) {
        this.location = location;
    }

    public ExperienceSearchSort getSort() {
        return sort;
    }

    public void setSort(ExperienceSearchSort sort) {
        this.sort = sort;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
