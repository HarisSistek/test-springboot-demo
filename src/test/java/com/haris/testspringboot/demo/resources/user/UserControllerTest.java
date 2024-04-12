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
        final List<User> users = userController.getUsers();
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
        final List<User> users1 = userController.getUsers();
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
        final List<User> users1 = userController.getUsers();
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
        final List<User> users1 = userController.getUsers();
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

        final UserRequestBody updateUser1 = new UserRequestBody("Jane Smith", 45);
        final User updatedUser1 = userController.putUser(createdUser1.uuid(), updateUser1);
        assertEquals(createdUser1.uuid(), updatedUser1.uuid());
        assertNotEquals(createdUser1, updatedUser1);
        final User foundUser1 = userController.getUser(createdUser1.uuid());
        assertEquals(updatedUser1, foundUser1);

        final UserRequestBody updateUser2 = new UserRequestBody("Bobby Brown", 89);
        final User updatedUser2 = userController.putUser(createdUser2.uuid(), updateUser2);
        assertEquals(createdUser2.uuid(), updatedUser2.uuid());
        assertNotEquals(createdUser2, updatedUser2);
        final User foundUser2 = userController.getUser(createdUser2.uuid());
        assertEquals(updatedUser2, foundUser2);

        final UserRequestBody updateUser3 = new UserRequestBody("Ole Nordmanm", 101);
        final User updatedUser3 = userController.putUser(createdUser3.uuid(), updateUser3);
        assertEquals(createdUser3.uuid(), updatedUser3.uuid());
        assertNotEquals(createdUser3, updatedUser3);
        final User foundUser3 = userController.getUser(createdUser3.uuid());
        assertEquals(updatedUser3, foundUser3);
    }

    @Test
    void putUser_error_no_previous_user() {
        try {
            userController.putUser("UNKNOWN", new UserRequestBody("John Smith", 55));
            fail("Should a have thrown a 404 unknown error");
        } catch (ResponseStatusException e) {
            assertEquals("User with uuid 'UNKNOWN' not found", e.getReason());
            assertEquals(NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    void deleteUser() {
        final List<User> users1 = userController.getUsers();
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

        userController.deleteUser(createdUser1.uuid());

        final List<User> users3 = userController.getUsers();
        assertFalse(users3.isEmpty());
        assertEquals(2, users3.size());
        assertFalse(users3.contains(createdUser1));
        assertTrue(users3.contains(createdUser2));
        assertTrue(users3.contains(createdUser3));

        userController.deleteUser(createdUser2.uuid());

        final List<User> users4 = userController.getUsers();
        assertFalse(users4.isEmpty());
        assertEquals(1, users4.size());
        assertFalse(users4.contains(createdUser1));
        assertFalse(users4.contains(createdUser2));
        assertTrue(users4.contains(createdUser3));

        userController.deleteUser(createdUser3.uuid());

        final List<User> users5 = userController.getUsers();
        assertTrue(users5.isEmpty());

        try {
            userController.deleteUser(createdUser3.uuid());
            fail("Should a have thrown a 404 unknown error");
        } catch (ResponseStatusException e) {
            assertEquals("User with uuid '%s' not found".formatted(createdUser3.uuid()), e.getReason());
            assertEquals(NOT_FOUND, e.getStatusCode());
        }
    }
}
