package peaksoft.dto.response;

import lombok.Builder;

@Builder
public record CategoryResponse(
        Long id,
        String name
) {
    public CategoryResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
