package edu.oregonstate.cs467.travelplanner.trip.service;

import edu.oregonstate.cs467.travelplanner.trip.dto.TripDto;
import edu.oregonstate.cs467.travelplanner.trip.model.Trip;
import edu.oregonstate.cs467.travelplanner.trip.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
public class TripService {

    @Autowired
    private TripRepository tripRepository;


    /**
     * Retrieves a list of trips associated with a specific user.
     * Only trips that are not marked as deleted are included in the result.
     *
     * @param userId the unique identifier of the user whose trips are to be retrieved
     * @return a list of TripDto objects representing the trips of the specified user,
     *         or an empty list if no trips are found, or all trips are marked as deleted
     */
    public List<TripDto> getTripsByUserId(Long userId) {
        List<Trip> trips = tripRepository.findAllByUserId(userId);

        if (trips.isEmpty()) {
            return List.of();
        }

        // Prepare a list to store TripDto
        List<TripDto> tripDtoList = new ArrayList<>();
        for (Trip trip : trips) {
            if (trip.getDeletedAt() == null) {
                TripDto tripDto = new TripDto();
                tripDto.setTripTitle(trip.getTripTitle());
                tripDto.setStartDate(trip.getStartDate());
                tripDto.setEndDate(trip.getEndDate());
                tripDto.setExperienceList(trip.getExperienceList());
                tripDtoList.add(tripDto);
            }
        }
        return tripDtoList;
    }

    public void addTrip(TripDto tripDto) {
        Trip trip = tripDto.toEntity();
        tripRepository.save(trip);
    }
}