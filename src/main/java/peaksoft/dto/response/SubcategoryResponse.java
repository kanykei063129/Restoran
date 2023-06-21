package peaksoft.dto.response;

import lombok.Builder;

@Builder
public record SubcategoryResponse(
        String categoryName,
        Long id,
        String name
) {
    public SubcategoryResponse(String categoryName, Long id, String name) {
        this.categoryName = categoryName;
        this.id = id;
        this.name = name;
    }
}
