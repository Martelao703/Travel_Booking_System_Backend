package com.david.travel_booking_system.service;

import com.david.travel_booking_system.model.User;
import com.david.travel_booking_system.repository.UserRepository;
import com.david.travel_booking_system.security.CustomUserDetails;
import com.david.travel_booking_system.specification.BaseSpecifications;
import com.david.travel_booking_system.specification.UserSpecifications;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Filter by email and non-deleted
        Specification<User> spec = UserSpecifications.filterByEmail(email)
                .and(BaseSpecifications.excludeDeleted(User.class));

        User user = userRepository.findOne(spec)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Wrap the user in a CustomUserDetails object
        return new CustomUserDetails(user);
    }
}
