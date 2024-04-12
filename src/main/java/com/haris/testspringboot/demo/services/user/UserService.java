package com.haris.testspringboot.demo.services.user;

import com.google.common.collect.ImmutableList;
import com.haris.testspringboot.demo.services.user.model.User;
import com.haris.testspringboot.demo.services.user.model.UserRequestBody;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class UserService {

    final ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

    public List<User> getUsers() {
        return ImmutableList.copyOf(users.values());
    }

    public User getUserOrThrow(final String uuid) {
        return getUser(uuid)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User with uuid '%s' not found".formatted(uuid)));
    }

    public User postUser(final UserRequestBody requestBody) {
        final User newUser = createUserFromRequest(UUID.randomUUID().toString(), requestBody);
        users.put(newUser.uuid(), newUser);
        return newUser;
    }

    public User putUser(final String uuid, final UserRequestBody requestBody) {
        final User foundUser = getUserOrThrow(uuid);

        // Alternative 1: if using build patter would not do something like
        // User newUser = User.builder().from(foundUser).name(requestBody.name()).age(requestBody.age()).build();
        // Alternative 2: Static builder method User newUser = User.fromRequestBody(uuid, requestBody);
        // Alternative 3: User newUser = foundUser.update(requestBody);

        final User updatedUser = createUserFromRequest(uuid, requestBody);
        users.put(uuid, updatedUser);
        return updatedUser;
    }

    public void deleteUser(final String uuid) {
        getUserOrThrow(uuid);
        users.remove(uuid);
    }

    private Optional<User> getUser(final String uuid) {
        return Optional.ofNullable(users.get(uuid));
    }

    private static User createUserFromRequest(final String uuid, final UserRequestBody requestBody) {
        return new User(uuid, requestBody.name(), requestBody.age());
    }

}
