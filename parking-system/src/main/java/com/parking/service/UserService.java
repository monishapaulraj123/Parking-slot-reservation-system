package com.parking.service;

import com.parking.entity.User;
import com.parking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

/**
 * Service layer — converted from original User class logic.
 * Handles: Registration, Login (simple password check, no JWT).
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // ---- Converted from: registerUser() in console app ----
    public User register(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already registered!");
        }
        user.setRole("USER"); // Default role
        return userRepository.save(user);
    }

    // ---- Converted from: login() in console app ----
    public User login(String email, String password) {
        Optional<User> found = userRepository.findByEmail(email);
        if (found.isPresent() && found.get().getPassword().equals(password)) {
            return found.get();
        }
        throw new RuntimeException("Invalid email or password!");
    }

    public User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
