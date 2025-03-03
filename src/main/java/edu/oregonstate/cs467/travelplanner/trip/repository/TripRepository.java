package edu.oregonstate.cs467.travelplanner.trip.repository;

import edu.oregonstate.cs467.travelplanner.trip.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    @Query("SELECT t FROM Trip t WHERE t.user.userId = :userId")
    Optional<Trip> findByUserId(Long userId);
}