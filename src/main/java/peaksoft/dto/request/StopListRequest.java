package peaksoft.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record StopListRequest(
        @Positive(message = "menu item id should be positive number!")
        @NotNull(message = "menu item id shouldn't be null!")
        Long menuItemId,
        @NotNull(message = "reason shouldn't be null!")
        String reason,
        @NotNull(message = "date shouldn't be null!")
        @Future
        LocalDate date

) {
}
