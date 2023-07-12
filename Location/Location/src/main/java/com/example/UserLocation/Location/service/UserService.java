package com.example.UserLocation.Location.service;

import com.example.UserLocation.Location.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(User user);

    Optional<User> updateUser(User user);

    void deleteUser(Long id);

    List<User> getNearestUsers(int count);

    Object getUserById(long l);
}
