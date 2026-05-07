package com.thaysvieira.user;

import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserFakerDataAccessService implements UserDao {
    private static final List<User> users = new ArrayList<>();

    static {
        Faker faker = new Faker();
        users.add(new User(UUID.fromString("9b4b3c7f-1d3e-4c82-a3f9-5d6e2a8f71b4"), faker.name().fullName()));
    }

    @Override
    public List<User> getAllUsers() {
        Faker faker = new Faker();
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            users.add(new User(UUID.randomUUID(), faker.name().fullName()));
        }
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
