package com.example.UserLocation.Location.service;

import com.example.UserLocation.Location.entity.User;
import com.example.UserLocation.Location.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> updateUser(User user) {
        Optional<User> existingUser = userRepository.findById(user.getId());
        existingUser.ifPresent(u -> {
            u.setName(user.getName());
            u.setLatitude(user.getLatitude());
            u.setLongitude(user.getLongitude());
            userRepository.save(u);
        });
        return existingUser;
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getNearestUsers(int count) {
        List<User> allUsers = userRepository.findAll();
        User referenceUser = userRepository.findById(1L).orElse(null); // Assuming reference user with ID 1

        if (referenceUser == null || allUsers.isEmpty()) {
            return Collections.emptyList();
        }

        List<DistanceUser> distanceUsers = new ArrayList<>();

        // Calculate distances from the reference user to all other users
        for (User user : allUsers) {
            double distance = calculateDistance(referenceUser.getLatitude(), referenceUser.getLongitude(), user.getLatitude(), user.getLongitude());
            distanceUsers.add(new DistanceUser(user, distance));
        }

        // Sort the distance users based on their distance in ascending order
        Collections.sort(distanceUsers, Comparator.comparingDouble(DistanceUser::getDistance));

        // Extract the nearest users up to the specified count
        List<User> nearestUsers = new ArrayList<>();
        for (int i = 0; i < Math.min(count, distanceUsers.size()); i++) {
            nearestUsers.add(distanceUsers.get(i).getUser());
        }

        return nearestUsers;
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS_KM = 6371; // Earth's radius in kilometers

        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        double dLat = lat2Rad - lat1Rad;
        double dLon = lon2Rad - lon1Rad;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad)
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }

    private static class DistanceUser {
        private final User user;
        private final double distance;

        public DistanceUser(User user, double distance) {
            this.user = user;
            this.distance = distance;
        }

        public User getUser() {
            return user;
        }

        public double getDistance() {
            return distance;
        }
    }
}


