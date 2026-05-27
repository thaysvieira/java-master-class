package com.thaysvieira.user;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserDao userDao;
    @InjectMocks
    private UserService underTest;

    private static final List<User> users = new ArrayList<>();


    @BeforeAll
    static void beforeAll() {
        users.add(new User(UUID.fromString("1be9ed11-0893-4734-9a24-83c6f6aa6474"), "john"));
        users.add(new User(UUID.fromString("d91db0f0-b24a-4eef-8f77-85778fb7641f"), "marie"));
    }


    @Test
    void shouldGetAllUsers() {
        given(underTest.getAllUsers()).willReturn(users);
        List<User> userList = underTest.getAllUsers();
        assertThat(userList).isNotNull();
        assertThat(userList.size()).isEqualTo(2);
        verify(userDao).getAllUsers();
    }

    @Test
    void shouldGetUserById() {
        given(userDao.getUserById(UUID.fromString("1be9ed11-0893-4734-9a24-83c6f6aa6474"))).willReturn(users.getFirst());
        User user = userDao.getUserById(UUID.fromString("1be9ed11-0893-4734-9a24-83c6f6aa6474"));
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(UUID.fromString("1be9ed11-0893-4734-9a24-83c6f6aa6474"));
        verify(userDao).getUserById(UUID.fromString("1be9ed11-0893-4734-9a24-83c6f6aa6474"));
    }
}