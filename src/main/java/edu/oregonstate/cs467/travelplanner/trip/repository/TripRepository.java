package edu.oregonstate.cs467.travelplanner.trip.repository;

import edu.oregonstate.cs467.travelplanner.trip.model.Trip;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    @Query("SELECT t FROM Trip t WHERE t.user.userId = :userId")
    List<Trip> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT t FROM Trip t WHERE t.user.userId = :userId AND t.deletedAt IS NULL ORDER BY t.createdAt DESC")
    List<Trip> findTop10ByUserIdOrderByCreatedDateDesc(Long userId, Pageable pageable);
}