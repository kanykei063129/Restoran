package peaksoft.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import peaksoft.dto.response.UserResponse;
import peaksoft.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Boolean existsByEmail(String email);
    Boolean existsByPhoneNumber(String phoneNumber);
    Optional<User> findByEmail(String email);
    @Query("select new peaksoft.dto.response.UserResponse(l.id,concat(l.firstName,l.lastName),l.dateOfBirth,l.email,l.phoneNumber,l.experience) from User l where l.restaurant.id=:resId")
    List<UserResponse> getAllUsers(Long resId);
    @Query("select new peaksoft.dto.response.UserResponse(l.id,concat(l.firstName,l.lastName),l.dateOfBirth,l.email,l.phoneNumber,l.experience) from User l where l.id=:id")
    Optional<UserResponse> getUserById(Long id);
    @Query("select new peaksoft.dto.response.UserResponse(l.id,concat(l.firstName,l.lastName),l.dateOfBirth,l.email,l.phoneNumber,l.experience) from User l where l.restaurant.id=null")
    List<UserResponse> getAllApp();
}
