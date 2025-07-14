package com.info.develop.controller;

import com.info.develop.model.User;
import com.info.develop.dto.PasswordUpdateRequest;
import com.info.develop.service.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    // GET /api/users - Get all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    // GET /api/users/{id} - Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }
    
    // POST /api/users - Create new user
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (DataIntegrityViolationException e) {
            // This is more specific and helpful for duplicate username/email scenarios.
            return ResponseEntity.status(HttpStatus.CONFLICT).body("A user with this username or email already exists.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An unexpected error occurred while creating the user.");
        }
    }
    
    // PUT /api/users/{id} - Update user
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        Optional<User> updatedUser = userService.updateUser(id, userDetails);
        return updatedUser.map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
    }
    
    // DELETE /api/users/{id} - Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        return deleted ? ResponseEntity.noContent().build() 
                      : ResponseEntity.notFound().build();
    }
    
    // GET /api/users/search/email?q={email} - Search users by email
    @GetMapping("/search/email")
    public ResponseEntity<List<User>> searchUsersByEmail(@RequestParam("q") String email) {
        List<User> users = userService.searchUsersByEmail(email);
        return ResponseEntity.ok(users);
    }
    
    // GET /api/users/search/name?q={name} - Search users by name
    @GetMapping("/search/name")
    public ResponseEntity<List<User>> searchUsersByName(@RequestParam("q") String name) {
        List<User> users = userService.searchUsersByName(name);
        return ResponseEntity.ok(users);
    }
    
    // PUT /api/users/{id}/password - Update user password
    @PutMapping("/{id}/password")
    public ResponseEntity<User> updateUserPassword(@PathVariable Long id, @RequestBody PasswordUpdateRequest request) {
        Optional<User> updatedUser = userService.updateUserPassword(id, request.getNewPassword());
        return updatedUser.map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
    }
    
    // GET /api/users/search/username?q={username} - Search user by username
    @GetMapping("/search/username")
    public ResponseEntity<User> getUserByUsername(@RequestParam("q") String username) {
        Optional<User> user = userService.getUserByUsername(username);
        return user.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }
}