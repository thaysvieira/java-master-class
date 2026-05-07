package com.thaysvieira.user;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserArrayDataAccessService implements UserDao {
    private static final List<User> users = new ArrayList<>();

    static {
        users.add(new User(UUID.fromString("1be9ed11-0893-4734-9a24-83c6f6aa6474"), "john"));
        users.add(new User(UUID.fromString("d91db0f0-b24a-4eef-8f77-85778fb7641f"), "marie"));
    }

    @Override
    public List<User> getAllUsers() {
        return users;
    }

    @Override
    public User getUserById(UUID userId) {
        return users.stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .orElse(null);
    }
}
