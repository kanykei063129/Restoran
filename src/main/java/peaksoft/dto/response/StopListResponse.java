package peaksoft.dto.response;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record StopListResponse(
        String menuItemName,
        Long id,
        String reason,
        LocalDate date
) {
    public StopListResponse(String menuItemName, Long id, String reason, LocalDate date) {
        this.menuItemName = menuItemName;
        this.id = id;
        this.reason = reason;
        this.date = date;
    }
}
