package edu.oregonstate.cs467.travelplanner.experience.dto;

import edu.oregonstate.cs467.travelplanner.experience.model.Experience;
import edu.oregonstate.cs467.travelplanner.experience.model.GeoPoint;
import edu.oregonstate.cs467.travelplanner.util.validation.NotBlankNull;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @Valid
    private GeoPoint location;

    @NotBlankNull
    @Size(max = 255)
    private String address;

    @URL
    private String imageUrl;

    public Experience transferTo(Experience experience) {
        experience.setTitle(title);
        experience.setDescription(description);
        experience.setEventDate(eventDate);
        experience.setLocation(location);
        experience.setAddress(address);
        experience.setImageUrl(imageUrl);
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

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
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
