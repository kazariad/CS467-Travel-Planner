package edu.oregonstate.cs467.travelplanner.experience.web.form;

import edu.oregonstate.cs467.travelplanner.experience.service.dto.ExperienceSearchParams.ExperienceSearchSort;

public enum ExperienceSearchFormSort {
    BEST_MATCH("bestmatch", "Best Match", ExperienceSearchSort.BEST_MATCH),

    DISTANCE("distance", "Distance", ExperienceSearchSort.DISTANCE),

    RATING("rating", "Rating", ExperienceSearchSort.RATING),

    NEWEST("newest", "Newest", ExperienceSearchSort.NEWEST);

    ExperienceSearchFormSort(String value, String displayName, ExperienceSearchSort searchSort) {
        this.value = value;
        this.displayName = displayName;
        this.searchSort = searchSort;
    }

    private final String value;

    private final String displayName;

    private final ExperienceSearchSort searchSort;

    public String getValue() {
        return value;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ExperienceSearchSort toSearchSort() {
        return searchSort;
    }

    public static ExperienceSearchFormSort fromValue(String value) {
        for (var sort : ExperienceSearchFormSort.values()) {
            if (sort.value.equals(value)) return sort;
        }
        throw new IllegalArgumentException(String.format("Invalid sort parameter value '%s'", value));
    }

    @Override
    public String toString() {
        return value;
    }
}

