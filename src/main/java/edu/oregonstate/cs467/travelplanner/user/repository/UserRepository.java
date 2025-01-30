package edu.oregonstate.cs467.travelplanner.user.repository;

import edu.oregonstate.cs467.travelplanner.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}

