package com.thaysvieira.user;

import java.util.UUID;

public interface UserDao {

    User[] getAllUsers();

    User getUserById(UUID userId);
}
