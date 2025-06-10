package com.david.travel_booking_system.config.seeder;

import com.david.travel_booking_system.config.DataSeeder;
import com.david.travel_booking_system.dto.request.auth.RegisterRequestDTO;
import com.david.travel_booking_system.model.User;
import com.david.travel_booking_system.repository.UserRepository;
import com.david.travel_booking_system.security.UserRole;
import com.david.travel_booking_system.service.UserService;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;

@Component
@Profile("dev")
@Order(1)
public class UserSeeder implements DataSeeder {

    private final UserService userService;

    public UserSeeder(UserService userService) {
        this.userService = userService;
    }

    @Override
    @Transactional
    public void seed() {
        // Id - 1
        seedUser(
                "admin@example.com", "Admin", "User", "123", "000000000",
                "Admin Address", LocalDate.now().minusYears(60), Set.of(UserRole.ROLE_ADMIN)
        );
        // Id - 2
        seedUser(
                "manager@example.com", "Manager", "User", "123", "111111111",
                "Manager Address", LocalDate.now().minusYears(50), Set.of(UserRole.ROLE_MANAGER)
        );
        // Id - 3
        seedUser(
                "host@example.com", "Host", "User", "123", "222222222",
                "Host Address", LocalDate.now().minusYears(40), Set.of(UserRole.ROLE_HOST)
        );
        // Id - 4
        seedUser(
                "john.doe@example.com", "John", "Doe", "123", "333333333",
                "John Doe Address", LocalDate.now().minusYears(30), Set.of(UserRole.ROLE_USER)
        );
        // Id - 5
        seedUser(
                "jane.smith@example.com", "Jane", "Smith", "123", "444444444",
                "Jane Smith Address", LocalDate.now().minusYears(20), Set.of(UserRole.ROLE_USER)
        );
    }

    private void seedUser(String email,
                          String firstName,
                          String lastName,
                          String password,
                          String phoneNumber,
                          String address,
                          LocalDate dateOfBirth,
                          Set<UserRole> rolesToAssign) {
        // Build RegisterRequestDTO
        RegisterRequestDTO dto = RegisterRequestDTO.builder()
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .password(password)
                .phoneNumber(phoneNumber)
                .address(address)
                .dateOfBirth(dateOfBirth)
                .build();

        // Create user and assign given roles
        User user = userService.createUser(dto);
        userService.changeUserRoles(user.getId(), rolesToAssign);
    }
}
