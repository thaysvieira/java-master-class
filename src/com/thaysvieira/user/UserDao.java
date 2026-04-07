package com.thaysvieira.user;

import java.util.UUID;

public class UserDao {
    private static User[] users;

    static {
        users = new User[]{
                new User(UUID.fromString("1be9ed11-0893-4734-9a24-83c6f6aa6474"), "john"),
                new User(UUID.fromString("d91db0f0-b24a-4eef-8f77-85778fb7641f"), "marie")
        };
    }

    public User[] getAllUsers() {
        return users;
    }

    public User getUserById(UUID userId) {

        for (User user : users) {
            if (user != null && user.getId().equals(userId)) {
                return user;
            }
        }
        return null;
    }
}
