package edu.oregonstate.cs467.travelplanner.experience.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record GeoPoint(
        @Min(-90)
        @Max(90)
        double lat,
        @Min(-180)
        @Max(180)
        double lng) {}
