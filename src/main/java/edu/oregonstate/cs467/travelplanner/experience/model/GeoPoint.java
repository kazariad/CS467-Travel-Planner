package edu.oregonstate.cs467.travelplanner.experience.model;

public record GeoPoint(double lat, double lng) {
    public GeoPoint(double lat, double lng) {
        if (lat < -90 || lat > 90) throw new IllegalArgumentException("Invalid latitude");
        if (lng < -180 || lng > 180) throw new IllegalArgumentException("Invalid longitude");
        this.lat = lat;
        this.lng = lng;
    }
}
