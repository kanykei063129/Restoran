package peaksoft.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import peaksoft.dto.response.RestaurantResponse;
import peaksoft.entity.Restaurant;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant,Long> {
    @Query("select new peaksoft.dto.response.RestaurantResponse(l.id,l.name,l.location,l.restType,l.numberOfEmployees) from Restaurant l where l.id=:id")
    RestaurantResponse getRestaurantById(Long id);
}
