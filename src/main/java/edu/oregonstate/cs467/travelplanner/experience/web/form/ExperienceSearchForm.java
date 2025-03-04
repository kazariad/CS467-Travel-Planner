package edu.oregonstate.cs467.travelplanner.experience.web.form;

import edu.oregonstate.cs467.travelplanner.experience.service.dto.ExperienceSearchParams;
import edu.oregonstate.cs467.travelplanner.experience.service.dto.ExperienceSearchParams.ExperienceSearchLocationParams;

public class ExperienceSearchForm {
    private String keywords;

    private String locationText;

    private Double locationLat;

    private Double locationLng;

    private Integer distanceMiles;

    private ExperienceSearchFormSort sort;

    private Integer offset;

    public void normalize() {
        if (locationLat == null || locationLng == null || distanceMiles == null) {
            locationText = null;
            locationLat = null;
            locationLng = null;
            distanceMiles = null;
        }

        if ((sort == ExperienceSearchFormSort.BEST_MATCH && keywords == null) ||
                (sort == ExperienceSearchFormSort.DISTANCE && distanceMiles == null)) {
            sort = null;
        }
        if (sort == null) {
            sort = distanceMiles != null ? ExperienceSearchFormSort.DISTANCE :
                    keywords != null ? ExperienceSearchFormSort.BEST_MATCH :
                            ExperienceSearchFormSort.NEWEST;
        }

        if (offset == null) offset = 0;
    }

    public ExperienceSearchParams convertToSearchParams() {
        ExperienceSearchParams searchParams = new ExperienceSearchParams();
        searchParams.setKeywords(keywords);
        if (locationLat != null && locationLng != null && distanceMiles != null) {
            searchParams.setLocation(new ExperienceSearchLocationParams(
                    locationLat, locationLng, distanceMiles * 1609.34));
        }
        searchParams.setSort(sort.toSearchSort());
        searchParams.setOffset(offset);
        return searchParams;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        if (keywords != null) {
            keywords = keywords.trim();
            if (keywords.isEmpty()) keywords = null;
        }
        this.keywords = keywords;
    }

    public String getLocationText() {
        return locationText;
    }

    public void setLocationText(String locationText) {
        if (locationText != null) {
            locationText = locationText.trim();
            if (locationText.isEmpty()) locationText = null;
        }
        this.locationText = locationText;
    }

    public Double getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(Double locationLat) {
        if (locationLat != null) {
            if (locationLat < -90 || locationLat > 90) locationLat = null;
        }
        this.locationLat = locationLat;
    }

    public Double getLocationLng() {
        return locationLng;
    }

    public void setLocationLng(Double locationLng) {
        if (locationLng != null) {
            if (locationLng < -180 || locationLng > 180) locationLng = null;
        }
        this.locationLng = locationLng;
    }

    public Integer getDistanceMiles() {
        return distanceMiles;
    }

    public void setDistanceMiles(Integer distanceMiles) {
        if (distanceMiles != null && distanceMiles < 0) distanceMiles = null;
        this.distanceMiles = distanceMiles;
    }

    public ExperienceSearchFormSort getSort() {
        return sort;
    }

    public void setSort(ExperienceSearchFormSort sort) {
        this.sort = sort;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        if (offset != null && offset < 0) offset = null;
        this.offset = offset;
    }
}
