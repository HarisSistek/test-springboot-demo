package com.haris.testspringboot.demo.resources.user;

import com.haris.testspringboot.demo.services.user.UserService;
import com.haris.testspringboot.demo.services.user.model.User;
import com.haris.testspringboot.demo.services.user.model.UserRequestBody;
import java.util.Collection;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.google.common.base.Preconditions.checkNotNull;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = checkNotNull(userService);
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping(path="/{uuid}")
    public User getUser(@PathVariable final String uuid) {
        return userService.getUserOrThrow(uuid);
    }

    @PostMapping
    public User postUser(@RequestBody final UserRequestBody requestBody) {
        return userService.postUser(requestBody);
    }

    @PutMapping(path="/{uuid}")
    public User putUser(@PathVariable final String uuid,
                        @RequestBody final UserRequestBody requestBody) {
       return userService.putUser(uuid, requestBody);
    }

    @DeleteMapping(path="/{uuid}")
    public void deleteUser(@PathVariable final String uuid) {
        userService.deleteUser(uuid);
    }
}
