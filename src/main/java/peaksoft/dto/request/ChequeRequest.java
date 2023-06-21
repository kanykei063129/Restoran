package peaksoft.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record ChequeRequest(
        @NotNull(message = "User id shouldn't be null!")

        Long userId,
        @NotNull(message = "Menu items id shouldn't be null!")

        List<Long> menuItemsId
) {
}
