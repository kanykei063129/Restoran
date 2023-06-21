package peaksoft.dto.response;

import lombok.Builder;

import java.time.LocalDate;
@Builder
public record UserResponse(
        Long id,
        String fullName,
        LocalDate dateOfBirth,
        String email,
        String phoneNumber,
        int experience
) {
    public UserResponse(Long id, String fullName, LocalDate dateOfBirth, String email, String phoneNumber, int experience) {
        this.id = id;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.experience = experience;
    }
}
