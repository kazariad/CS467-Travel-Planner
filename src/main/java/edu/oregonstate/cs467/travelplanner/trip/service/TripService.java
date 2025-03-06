package edu.oregonstate.cs467.travelplanner.trip.service;

import edu.oregonstate.cs467.travelplanner.trip.dto.TripDto;
import edu.oregonstate.cs467.travelplanner.trip.model.Trip;
import edu.oregonstate.cs467.travelplanner.trip.repository.TripRepository;
import edu.oregonstate.cs467.travelplanner.user.model.User;
import edu.oregonstate.cs467.travelplanner.util.security.AuthenticatedUserProvider;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

@Service
public class TripService {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private AuthenticatedUserProvider authUserProvider;

    /**
     * Retrieves a list of trips associated with a specific user.
     * Only trips that are not marked as deleted are included in the result.
     *
     * @param userId the unique identifier of the user whose trips are to be retrieved
     * @return a list of TripDto objects representing the trips of the specified user,
     *         or an empty list if no trips are found, or all trips are marked as deleted
     */
    public List<Trip> getTripsByUserId(Long userId) {
        List<Trip> trips = tripRepository.findAllByUserId(userId);
        if (trips.isEmpty()) {
            return List.of();
        }
        List<Trip> activeTrips = new ArrayList<>();
        for (Trip trip : trips) {
            if (trip.getDeletedAt() == null) {
                activeTrips.add(trip);
            }
        }
        return activeTrips;
    }

    /**
     * Retrieves a trip by its unique identifier.
     *
     * @param tripId the unique identifier of the trip to be retrieved
     * @return the Trip object if found, or null if no trip exists.
     */
    public Trip getTripById(Long tripId) {
        User currentUser = authUserProvider.getUser();
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("Trip not found"));
        return trip;
    }

    public void createTrip(TripDto tripDto) {
        User user = authUserProvider.getUser();
        Trip trip = tripDto.toEntity();
        trip.setUser(user);
        tripRepository.save(trip);
    }

    @Transactional
    public void updateTrip(Long tripId, TripDto tripDto) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("Trip with ID " + tripId + " does not exist"));
        trip.setEndDate(tripDto.getEndDate());
        trip.setStartDate(tripDto.getStartDate());
        trip.setTripTitle(tripDto.getTripTitle());
        tripRepository.save(trip);
    }

    public void deleteTrip(long tripId) {
        Trip trip = tripRepository.findById(tripId).orElse(null);
        if (trip == null || trip.getDeletedAt() != null) return;
        trip.setDeletedAt(Instant.now());
        tripRepository.save(trip);
    }

    @Transactional
    public void addExperienceToTrip(Long experienceId, Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("Trip with ID " + tripId + " does not exist"));

        if (trip.getExperienceList().contains(experienceId)) {
            throw new IllegalArgumentException("Experience with ID " + experienceId + " is already added to this trip.");
        }
        trip.getExperienceList().add(experienceId);
        tripRepository.save(trip);
    }

    public List<Trip> getRecentTripsByUserId(Long userId) {
        return tripRepository.findTop10ByUserIdOrderByCreatedDateDesc(userId);
    }
}