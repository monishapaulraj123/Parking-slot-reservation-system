package com.parking.controller;

import com.parking.entity.User;
import com.parking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * UserController - REST API endpoints for User operations.
 *
 * Endpoints:
 *   POST /api/users/register  → Register new user
 *   POST /api/users/login     → Login and get user info + role
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * POST /api/users/register
     * Request body: { name, email, phone, password, role }
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        try {
            // Build User entity from request body
            User user = new User();
            user.setName(body.get("name"));
            user.setEmail(body.get("email"));
            user.setPhone(body.get("phone"));
            user.setPassword(body.get("password"));
            // Allow role override only if provided, default to USER
            user.setRole(body.getOrDefault("role", "USER"));

            User saved = userService.register(user);
            saved.setPassword(null); // Never send password back
            return ResponseEntity.ok(saved);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * POST /api/users/login
     * Request body: { email, password }
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        try {
            User user = userService.login(body.get("email"), body.get("password"));

            // Return user details — frontend stores userId and role in sessionStorage
            return ResponseEntity.ok(Map.of(
                    "id",    user.getId(),
                    "name",  user.getName(),
                    "email", user.getEmail(),
                    "role",  user.getRole()
            ));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
