package peaksoft.dto.response;

import lombok.Builder;

@Builder
public record RestaurantResponse(
        Long id,
        String name,
        String location,
        String restType,
        int numberOfEmployees
) {
    public RestaurantResponse(Long id, String name, String location, String restType, int numberOfEmployees) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.restType = restType;
        this.numberOfEmployees = numberOfEmployees;
    }
}
