package peaksoft.dto;

import lombok.Builder;

@Builder
public record AuthenticationResponse(
        String email,
        String token
) {
}
