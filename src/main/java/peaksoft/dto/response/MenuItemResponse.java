package peaksoft.dto.response;

import lombok.Builder;

@Builder
public record MenuItemResponse(
        Long id,
        String name,
        String image,
        int price,
        String description,
        boolean IsVegetarian
) {
    public MenuItemResponse(Long id, String name, String image, int price, String description, boolean IsVegetarian) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.price = price;
        this.description = description;
        this.IsVegetarian = IsVegetarian;
    }
}
