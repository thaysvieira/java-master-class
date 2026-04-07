package com.thaysvieira.user;

import java.util.UUID;

public class UserService {
    private final UserDao userDao = new UserDao();

    public User[] getAllUsers() {
        return userDao.getAllUsers();
    }

    public User getUserById(UUID userId) {

        if (userId == null) {
            throw new IllegalArgumentException("User id cannot be null");
        }
        User user = userDao.getUserById(userId);
        if (user == null) {
            throw new IllegalStateException("User not found");
        }
        return user;
    }
}

