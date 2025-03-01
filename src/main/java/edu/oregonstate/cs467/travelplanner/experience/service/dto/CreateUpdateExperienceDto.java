package edu.oregonstate.cs467.travelplanner.experience.service.dto;

import edu.oregonstate.cs467.travelplanner.experience.model.Experience;
import edu.oregonstate.cs467.travelplanner.util.validation.NotBlankNull;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;

public class CreateUpdateExperienceDto {
    @NotBlank
    @Size(max = 100)
    private String title;

    @NotBlankNull
    @Size(max = 1000)
    private String description;

    @NotNull
    private LocalDate eventDate;

    @NotNull
    @Min(-90)
    @Max(90)
    private Double locationLat;

    @NotNull
    @Min(-180)
    @Max(180)
    private Double locationLng;

    @NotBlankNull
    @Size(max = 255)
    private String address;

    @NotBlankNull
    private String placeId;

    @NotBlankNull
    @URL
    private String imageUrl;

    public CreateUpdateExperienceDto() {}

    public CreateUpdateExperienceDto(Experience experience) {
        this.title = experience.getTitle();
        this.description = experience.getDescription();
        this.eventDate = experience.getEventDate();
        this.locationLat = experience.getLocationLat();
        this.locationLng = experience.getLocationLng();
        this.address = experience.getAddress();
        this.placeId = experience.getPlaceId();
        this.imageUrl = experience.getImageUrl();
    }

    public Experience transferTo(Experience experience) {
        experience.setTitle(this.title);
        experience.setDescription(this.description);
        experience.setEventDate(this.eventDate);
        experience.setLocationLat(this.locationLat);
        experience.setLocationLng(this.locationLng);
        experience.setAddress(this.address);
        experience.setPlaceId(this.placeId);
        experience.setImageUrl(this.imageUrl);
        return experience;
    }

    private String formatString(String s) {
        if (s == null) return s;
        s = s.trim();
        return s.isEmpty() ? null : s;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = formatString(title);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = formatString(description);
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = formatString(address);
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = formatString(placeId);
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = formatString(imageUrl);
    }
}
