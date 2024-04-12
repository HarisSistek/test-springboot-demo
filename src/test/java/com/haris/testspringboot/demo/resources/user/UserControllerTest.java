package com.haris.testspringboot.demo.resources.user;

import com.haris.testspringboot.demo.services.user.UserService;
import com.haris.testspringboot.demo.services.user.model.User;
import com.haris.testspringboot.demo.services.user.model.UserRequestBody;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;

class UserControllerTest {

    private UserController userController;

    @BeforeEach
    void setup() {
        final UserService userService = new UserService();
        userController = new UserController(userService);
    }

    @Test
    void getUsers_isEmpty() {
        final Collection<User> users = userController.getUsers();
        assertTrue(users.isEmpty());
    }

    @Test
    void getUser_not_found() {
        try {
            userController.getUser("UNKNOWN");
            fail("Should a have thrown a 404 unknown error");
        } catch (ResponseStatusException e) {
            assertEquals("User with uuid 'UNKNOWN' not found", e.getReason());
            assertEquals(NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    void postUser() {
        final Collection<User> users1 = userController.getUsers();
        assertTrue(users1.isEmpty());

        final User createdUser =
                userController.postUser(new UserRequestBody("John Smith", 30));

        final List<User> users2 = userController.getUsers();
        assertFalse(users2.isEmpty());
        assertEquals(createdUser, users2.getFirst());

        final User foundUser = userController.getUser(createdUser.uuid());
        assertEquals(createdUser, foundUser);
    }

    @Test
    void postUser_many_users() {
        final Collection<User> users1 = userController.getUsers();
        assertTrue(users1.isEmpty());

        final User createdUser1 =
                userController.postUser(new UserRequestBody("John Smith", 30));
        final User createdUser2 =
                userController.postUser(new UserRequestBody("Bob Mortimer", 40));
        final User createdUser3 =
                userController.postUser(new UserRequestBody("Kari Nordmann", 50));

        final List<User> users2 = userController.getUsers();
        assertFalse(users2.isEmpty());
        assertEquals(3, users2.size());
        assertTrue(users2.contains(createdUser1));
        assertTrue(users2.contains(createdUser2));
        assertTrue(users2.contains(createdUser3));

        final User foundUser1 = userController.getUser(createdUser1.uuid());
        assertEquals(createdUser1, foundUser1);

        final User foundUser2 = userController.getUser(createdUser2.uuid());
        assertEquals(createdUser2, foundUser2);

        final User foundUser3 = userController.getUser(createdUser3.uuid());
        assertEquals(createdUser3, foundUser3);
    }

    @Test
    void putUser() {
    }

    @Test
    void deleteUser() {
    }
}
