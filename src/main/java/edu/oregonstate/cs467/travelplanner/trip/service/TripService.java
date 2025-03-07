package edu.oregonstate.cs467.travelplanner.trip.service;

import edu.oregonstate.cs467.travelplanner.trip.dto.TripDto;
import edu.oregonstate.cs467.travelplanner.trip.model.Trip;
import edu.oregonstate.cs467.travelplanner.trip.repository.TripRepository;
import edu.oregonstate.cs467.travelplanner.user.model.User;
import edu.oregonstate.cs467.travelplanner.util.security.AuthenticatedUserProvider;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.ArrayList;

@Service
public class TripService {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private AuthenticatedUserProvider authUserProvider;

    /**
     * Retrieves a list of active trips associated with a specific user, identified by their user ID.
     * Active trips are those that have not been marked as deleted.
     *
     * @param userId the ID of the user
     * @return a list of active trips, or an empty list if no active trips exist
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
     * @param tripId the ID of the trip to be retrieved
     * @return the trip associated with the provided ID
     * @throws IllegalArgumentException if no trip is found
     */
    public Trip getTripById(Long tripId) {
        User currentUser = authUserProvider.getUser();
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("Trip not found"));
        return trip;
    }

    /**
     * Creates a new trip for the currently authenticated user based on the provided trip data.
     *
     * @param tripDto the data transfer object containing details of the trip
     * @throws IllegalArgumentException if the start date or end date is missing, or if the start date is after the end
     * date.
     */
    public void createTrip(TripDto tripDto) {
        if (tripDto.getStartDate() == null || tripDto.getEndDate() == null) {
            throw new IllegalArgumentException("Start date and end date are required");
        }
        if (tripDto.getStartDate().isAfter(tripDto.getEndDate())) {
            throw new IllegalArgumentException("Start date must not be after the end date");
        }
        User user = authUserProvider.getUser();
        Trip trip = tripDto.toEntity();
        trip.setUser(user);
        tripRepository.save(trip);
    }

    /**
     * Updates the details of an existing trip with the provided information.
     * If the trip does not exist, an IllegalArgumentException is thrown.
     *
     * @param tripId the ID of the trip to update
     * @param tripDto an object containing the updated trip details such as title, start date, and end date
     */
    @Transactional
    public void updateTrip(Long tripId, TripDto tripDto) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("Trip with ID " + tripId + " does not exist"));
        trip.setEndDate(tripDto.getEndDate());
        trip.setStartDate(tripDto.getStartDate());
        trip.setTripTitle(tripDto.getTripTitle());
        tripRepository.save(trip);
    }

    /**
     * Marks the specified trip as deleted by setting the current timestamp to its "deletedAt" field.
     * If the trip is not found or is already marked as deleted, no action is taken.
     *
     * @param tripId the ID of the trip to be deleted
     */
    public void deleteTrip(long tripId) {
        Trip trip = tripRepository.findById(tripId).orElse(null);
        if (trip == null || trip.getDeletedAt() != null) return;
        trip.setDeletedAt(Instant.now());
        tripRepository.save(trip);
    }

    /**
     * Adds an experience to the specified trip.
     *
     * @param experienceId the ID of the experience to add to the trip
     * @param tripId the ID of the trip
     * @throws IllegalArgumentException if the trip does not exist or if the experience is already associated with the
     * trip
     */
    @Transactional
    public void addExperienceToTrip(Long experienceId, Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("Trip with ID " + tripId + " does not exist"));

        if (trip.getExperienceList().contains(experienceId)) {
            throw new IllegalArgumentException("Experience with ID " + experienceId
                    + " is already added to this trip.");
        }
        trip.getExperienceList().add(experienceId);
        tripRepository.save(trip);
    }


    /**
     * Retrieves a list of created by a specified user.
     *
     * @param userId the ID of the user whose recent trips are to be retrieved
     * @return a list of the top 10 most recent trips associated with the specified user,
     *         or an empty list if the user has no trips
     */
    public List<Trip> getRecentTripsByUserId(Long userId) {
        Pageable pageable = PageRequest.of(0, 10);
        return tripRepository.findTop10ByUserIdOrderByCreatedDateDesc(userId, pageable);
    }
}