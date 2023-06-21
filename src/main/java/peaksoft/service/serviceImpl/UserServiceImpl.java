package peaksoft.service.serviceImpl;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import peaksoft.config.JwtService;
import peaksoft.dto.AuthenticationResponse;
import peaksoft.dto.SignInRequest;
import peaksoft.dto.SimpleResponse;
import peaksoft.dto.request.UserRequest;
import peaksoft.dto.response.UserResponse;
import peaksoft.entity.Restaurant;
import peaksoft.entity.User;
import peaksoft.enums.Role;
import peaksoft.exceptions.AlreadyExistException;
import peaksoft.exceptions.BadCredentialException;
import peaksoft.exceptions.BadRequestException;
import peaksoft.exceptions.NotFoundException;
import peaksoft.repository.RestaurantRepository;
import peaksoft.repository.UserRepository;
import peaksoft.service.UserService;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostConstruct
    @Override
    public void init() {
        User user = new User();
        user.setRole(Role.ADMIN);
        user.setDateOfBirth(LocalDate.of(2006, 03, 19));
        user.setFirstName("Admin");
        user.setLastName("Admin kyzy");
        user.setPhoneNumber("+996706050119");
        user.setEmail("admin@gmail.com");
        user.setPassword(passwordEncoder.encode("admin123"));
        user.setExperience(3);
        if (!userRepository.existsByEmail("admin@gmail.com")) {
            userRepository.save(user);
        }
    }

    @Override
    public SimpleResponse userApp(UserRequest request) {
        Boolean exists = userRepository.existsByEmail(request.email());
        check(request);
        if (!exists) {
            User user = new User();
            user.setFirstName(request.firstName());
            user.setLastName(request.lastName());
            user.setDateOfBirth(request.dateOfBirth());
            user.setEmail(request.email());
            user.setPassword(passwordEncoder.encode(request.password()));
            user.setPhoneNumber(request.phoneNumber());
            user.setExperience(request.experience());
            user.setRole(request.role());
            userRepository.save(user);
        } else {
            return SimpleResponse.builder().status(HttpStatus.CONFLICT).message("Email already exist!").build();
        }
        return null;
    }

    @Override
    public SimpleResponse saveUserByAdmin(UserRequest request) {
        Restaurant restaurant = restaurantRepository.findById(request.restaurantId()).orElseThrow(() -> new NotFoundException("Restaurant with id: " + request.restaurantId() + " is no exist!"));
        Boolean exists = userRepository.existsByEmail(request.email());
        List<UserResponse> users = userRepository.getAllUsers(restaurant.getId());
        check(request);
        if (!exists) {
            if (users.size() <= 15) {
                User user = new User();
                user.setFirstName(request.firstName());
                user.setLastName(request.lastName());
                user.setDateOfBirth(request.dateOfBirth());
                user.setEmail(request.email());
                user.setPassword(passwordEncoder.encode(request.password()));
                user.setPhoneNumber(request.phoneNumber());
                user.setExperience(request.experience());
                user.setRole(request.role());
                user.setRestaurant(restaurant);
                userRepository.save(user);
                return SimpleResponse.builder().status(HttpStatus.OK).message("User with id: " + user.getId() + " is saved").build();
            } else {
                return SimpleResponse.builder().status(HttpStatus.BAD_REQUEST).message("No vacancy").build();
            }
        } else {
            return SimpleResponse.builder().status(HttpStatus.CONFLICT).message("Already exist email").build();
        }
    }

    @Override
    public List<UserResponse> jobApplication(Long id, String word) {
        Restaurant restaurant = restaurantRepository.findById(restaurantRepository.findAll().get(0).getId()).orElseThrow(() -> new NotFoundException("Restaurant with no exist"));
        if (word.equalsIgnoreCase("Vacancy")) {
            return userRepository.getAllApp();
        } else if (word.equalsIgnoreCase("accept")) {
            List<UserResponse> users = userRepository.getAllUsers(restaurant.getId());
            if (users.size() <= 15) {
                assignUserToRest(id, restaurantRepository.findAll().get(0).getId());
            } else
                throw new BadCredentialException("No vacancy");
        } else if (word.equalsIgnoreCase("cancel")) {
            deleteById(id);
        } else {
            throw new BadCredentialException("User id or keyWord not matched!");
        }
        return null;
    }

    @Override
    public SimpleResponse assignUserToRest(Long userId, Long restId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with id: " + userId + " is no exist!"));
        Restaurant rest = restaurantRepository.findById(restId).orElseThrow(() -> new NotFoundException("Restaurant with id:" + restId + "is no exist"));
        user.setRestaurant(rest);
        rest.addUser(user);
        userRepository.save(user);
        restaurantRepository.save(rest);
        return SimpleResponse.builder().status(HttpStatus.OK).message("User with id:" + user.getId() + " is successfully assigned!").build();

    }

    @Override
    public UserResponse getById(Long userId) {
        return userRepository.getUserById(userId).orElseThrow(() -> new NotFoundException("User with id: " + userId + " is no exist!"));
    }

    @Override
    public List<UserResponse> getAll(Long restId) {
        restaurantRepository.findById(restId).orElseThrow(()->new NotFoundException("Restaurant with id: "+restId+" is no exist!"));
        return userRepository.getAllUsers(restId);
    }

    @Override
    public SimpleResponse update(Long userId, UserRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with id: " + userId + " is no exist!"));
        List<User> all = userRepository.findAll();
        all.remove(user);
        check(request);
        for (User user1 : all) {
            if (!user1.getEmail().equals(request.email())) {
                user.setFirstName(request.firstName());
                user.setLastName(request.lastName());
                user.setDateOfBirth(request.dateOfBirth());
                user.setEmail(request.email());
                user.setPassword(request.password());
                user.setPhoneNumber(request.phoneNumber());
                user.setExperience(request.experience());
                userRepository.save(user);
                return SimpleResponse.builder().status(HttpStatus.OK).message("User with id: " + userId + " is updated!").build();
            } else {
                return SimpleResponse.builder().status(HttpStatus.FORBIDDEN).message("Email is already exists!").build();
            }
        }
        return null;
    }

    @Override
    public SimpleResponse deleteById(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with id: " + userId + " is no exist"));
        userRepository.deleteById(userId);
        return SimpleResponse.builder().status(HttpStatus.OK).message("User with id: " + userId + " is deleted!").build();
    }

    @Override
    public AuthenticationResponse authenticate(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()));
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new NotFoundException(String.format
                        ("User with email: %s doesn't exists", request.email())));
        String token = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .email(user.getEmail())
                .build();
    }
        private void check(UserRequest request) {
        Boolean existsPh = userRepository.existsByPhoneNumber(request.phoneNumber());
        if (existsPh) {
            throw new AlreadyExistException("User with phone number: " + request.phoneNumber() + " is already exist!");
        }
        if (!request.phoneNumber().startsWith("+996")) {
            throw new BadRequestException("Phone number should starts with +996");
        }
        int year = LocalDate.now().minusYears(request.dateOfBirth().getYear()).getYear();
        if (request.role().equals(Role.CHEF)) {
            if (year <= 25 || year >= 45 && request.experience() <= 2) {
                throw new BadRequestException("Chef's years old should be between 25-45 and experience>=2");
            }
        } else if (request.role().equals(Role.WAITER)) {
            if (year <= 18 || year >= 30 && request.experience() <= 1) {
                throw new BadRequestException("Waiter's years old should be between 18-30 and experience>=1");
            }
        }
    }
}
