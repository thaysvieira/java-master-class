package com.thaysvieira.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


class UserArrayDataAccessServiceTest {

    private UserArrayDataAccessService underTest;

    @BeforeEach
    void setUp() {
        underTest = new UserArrayDataAccessService();
    }

    @Test
    void shouldGetAllUsers() {
        List<User> userList = underTest.getAllUsers();
        assertThat(userList).isNotNull();
        assertThat(userList.size()).isEqualTo(2);
    }

    @Test
    void shouldGetUserById() {
        UUID id = UUID.fromString("1be9ed11-0893-4734-9a24-83c6f6aa6474");
        User user = underTest.getUserById(id);
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(id);
        assertThat(user.getName()).isEqualTo("john");
    }

    @Test
    void shouldReturnNullWhenUserDoesNotExist() {
        UUID id = UUID.randomUUID();
        User user = underTest.getUserById(id);
        assertThat(user).isNull();
    }
}