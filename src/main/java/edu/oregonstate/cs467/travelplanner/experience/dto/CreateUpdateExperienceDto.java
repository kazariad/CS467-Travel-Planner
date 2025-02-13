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

    @Min(-90)
    @Max(90)
    private double locationLat;

    @Min(-180)
    @Max(180)
    private double locationLng;

    @NotBlankNull
    @Size(max = 255)
    private String address;

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
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public double getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(double locationLat) {
        this.locationLat = locationLat;
    }

    public double getLocationLng() {
        return locationLng;
    }

    public void setLocationLng(double locationLng) {
        this.locationLng = locationLng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
