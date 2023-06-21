package peaksoft.service;

import peaksoft.dto.SimpleResponse;
import peaksoft.dto.request.RestaurantRequest;
import peaksoft.dto.response.RestaurantResponse;

public interface RestaurantService {
    SimpleResponse save(RestaurantRequest request);
    SimpleResponse update(Long resId, RestaurantRequest request);
    SimpleResponse deleteById(Long resId);
    RestaurantResponse getById(Long resId);
}
