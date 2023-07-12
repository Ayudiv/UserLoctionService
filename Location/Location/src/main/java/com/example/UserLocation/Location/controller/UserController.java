package com.example.UserLocation.Location.controller;

import com.example.UserLocation.Location.entity.User;
import com.example.UserLocation.Location.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create_data")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> createData(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PatchMapping("/update_data")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> updateData(@RequestBody User user) {
        userService.updateUser(user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete_data/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteData(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get_users/{count}")
    @PreAuthorize("hasRole('READER')")
    public ResponseEntity<List<User>> getNearestUsers(@PathVariable int count) {
        List<User> nearestUsers = userService.getNearestUsers(count);
        return ResponseEntity.ok(nearestUsers);
    }
}
