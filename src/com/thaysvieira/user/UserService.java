package com.thaysvieira.user;

import java.util.UUID;

public class UserService {
    private final UserDao userArrayDataAccessService;

    public UserService(UserDao userArrayDataAccessService) {
        this.userArrayDataAccessService = userArrayDataAccessService;
    }

    public User[] getAllUsers() {
        return userArrayDataAccessService.getAllUsers();
    }

    public User getUserById(UUID userId) {

        if (userId == null) {
            throw new IllegalArgumentException("User id cannot be null");
        }
        User user = userArrayDataAccessService.getUserById(userId);
        if (user == null) {
            throw new IllegalStateException("User not found");
        }
        return user;
    }
}

