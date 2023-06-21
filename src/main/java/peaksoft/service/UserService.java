package peaksoft.service;

import peaksoft.dto.AuthenticationResponse;
import peaksoft.dto.SignInRequest;
import peaksoft.dto.SimpleResponse;
import peaksoft.dto.request.UserRequest;
import peaksoft.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    void init();
    SimpleResponse userApp(UserRequest request);
    SimpleResponse saveUserByAdmin(UserRequest request);
    List<UserResponse> jobApplication(Long id, String word);
    SimpleResponse assignUserToRest(Long userId, Long restId);
    UserResponse getById(Long userId);
    List<UserResponse> getAll(Long restId);
    SimpleResponse update(Long userId, UserRequest request);
    SimpleResponse deleteById(Long userId);
    AuthenticationResponse authenticate(SignInRequest request);
}
