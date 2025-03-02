package edu.oregonstate.cs467.travelplanner.experience.web.form;

public class ExperienceSearchForm {
    private String keywords;

    private String locationText;

    private Double locationLat;

    private Double locationLng;

    private Integer distanceMiles;

    private String sort;

    private Integer offset;

    public void normalize() {
        if (keywords != null) {
            keywords = keywords.trim();
            if (keywords.isEmpty()) keywords = null;
        }

        if (locationText != null) {
            locationText = locationText.trim();
            if (locationText.isEmpty()) locationText = null;
        }

        if (locationLat == null || locationLat < -90 || locationLat > 90
                || locationLng == null || locationLng < -180 || locationLng > 180
                || distanceMiles == null || distanceMiles < 0) {
            locationText = null;
            locationLat = null;
            locationLng = null;
            distanceMiles = null;
        }

        sort = sort == null ? "" : sort.toLowerCase().trim();
        switch (sort) {
            case "bestmatch":
                if (keywords == null) sort = "newest";
                break;
            case "distance":
                if (distanceMiles == null) sort = "newest";
                break;
            case "rating":
            case "newest":
                break;
            default:
                sort = distanceMiles != null ? "distance" : keywords != null ? "bestmatch" : "newest";
        }

        if (offset == null || offset < 0) offset = 0;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getLocationText() {
        return locationText;
    }

    public void setLocationText(String locationText) {
        this.locationText = locationText;
    }

    public Double getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(Double locationLat) {
        this.locationLat = locationLat;
    }

    public Double getLocationLng() {
        return locationLng;
    }

    public void setLocationLng(Double locationLng) {
        this.locationLng = locationLng;
    }

    public Integer getDistanceMiles() {
        return distanceMiles;
    }

    public void setDistanceMiles(Integer distanceMiles) {
        this.distanceMiles = distanceMiles;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }
}
