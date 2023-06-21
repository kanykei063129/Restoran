package peaksoft.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import peaksoft.dto.SimpleResponse;
import peaksoft.dto.request.RestaurantRequest;
import peaksoft.dto.response.RestaurantResponse;
import peaksoft.entity.Restaurant;
import peaksoft.exceptions.BadRequestException;
import peaksoft.exceptions.NotFoundException;
import peaksoft.repository.RestaurantRepository;
import peaksoft.service.RestaurantService;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepository restaurantRepository;

    @Override
    public SimpleResponse save(RestaurantRequest request) {
        List<Restaurant> all = restaurantRepository.findAll();
        if (all.isEmpty()) {
            Restaurant rest = new Restaurant();
            rest.setName(request.name());
            rest.setRestType(request.restType());
            rest.setLocation(request.location());
            rest.setService(request.service());
            restaurantRepository.save(rest);
            return SimpleResponse.builder().status(HttpStatus.OK).message("Restaurant with name: " + rest.getName() + " is saved!").build();
        } else {
            throw new BadRequestException("Restaurant is already exist!");
        }
    }
    @Override
    public SimpleResponse update(Long resId, RestaurantRequest request) {
        Restaurant rest = restaurantRepository.findById(resId).orElseThrow(() -> new NotFoundException("Restaurant with id: " + resId + " is no exist!"));
        rest.setName(request.name());
        rest.setLocation(request.location());
        rest.setRestType(request.restType());
        rest.setService(request.service());
        restaurantRepository.save(rest);
        return SimpleResponse.builder().status(HttpStatus.OK).message( "Restaurant with name: " + rest.getName() + " is updated!!").build();
    }

    @Override
    public SimpleResponse deleteById(Long resId) {
        restaurantRepository.findById(resId).orElseThrow(()->new NotFoundException("Restaurant with id: "+resId+" is no exist"));
        restaurantRepository.deleteById(resId);
        return SimpleResponse.builder().status(HttpStatus.OK).message( "Restaurant with name: " + resId+ " is deleted!").build();
    }

    @Override
    public RestaurantResponse getById(Long resId) {
        Restaurant restaurant = restaurantRepository.findById(resId)
                .orElseThrow(() -> new NotFoundException("Restaurant with id: " + resId + " not found!"));
        restaurant.setNumberOfEmployees(restaurant.getUsers().size());

        restaurantRepository.save(restaurant);

        return restaurantRepository.getRestaurantById(resId);    }
}