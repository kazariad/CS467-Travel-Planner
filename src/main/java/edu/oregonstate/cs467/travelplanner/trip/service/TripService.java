package edu.oregonstate.cs467.travelplanner.trip.service;

import edu.oregonstate.cs467.travelplanner.trip.model.Trip;
import edu.oregonstate.cs467.travelplanner.trip.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TripService {

    @Autowired
    private TripRepository tripRepository;

    /**
     * Retrieves a trip associated with the specified user ID.
     * If the trip exists but is marked as deleted (deletedAt is not null),
     * an empty Optional is returned.
     *
     * @param userId the ID of the user whose associated trip is to be retrieved
     * @return an Optional containing the trip if found and not marked as deleted;
     *         otherwise, an empty Optional
     */
    public List<Trip> getTripsByUserId(Long userId) {
        List<Trip> trips = tripRepository.findAllByUserId(userId);
        if (trips.isEmpty()) {
            return trips;
        }
        return trips.stream()
                .filter(trip -> trip.getDeletedAt() == null) // Filter out deleted trips
                .collect(Collectors.toList());
    }
}