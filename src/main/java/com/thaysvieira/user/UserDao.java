package com.thaysvieira.user;

import java.util.List;
import java.util.UUID;

public interface UserDao {

    List<User> getAllUsers();

    User getUserById(UUID userId);
}
