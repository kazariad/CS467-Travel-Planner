package edu.oregonstate.cs467.travelplanner.experience.dto;

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
        this.imageUrl = experience.getImageUrl();
    }

    public Experience transferTo(Experience experience) {
        experience.setTitle(this.title);
        experience.setDescription(this.description);
        experience.setEventDate(this.eventDate);
        experience.setLocationLat(this.locationLat);
        experience.setLocationLng(this.locationLng);
        experience.setAddress(this.address);
        experience.setImageUrl(this.imageUrl);
        return experience;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        if (this.title != null) {
            this.title = this.title.trim();
            if (this.title.isEmpty()) this.title = null;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        if (this.description != null) {
            this.description = this.description.trim();
            if (this.description.isEmpty()) this.description = null;
        }
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
        this.address = address;
        if (this.address != null) {
            this.address = this.address.trim();
            if (this.address.isEmpty()) this.address = null;
        }
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        if (this.imageUrl != null) {
            this.imageUrl = this.imageUrl.trim();
            if (this.imageUrl.isEmpty()) this.imageUrl = null;
        }
    }
}
