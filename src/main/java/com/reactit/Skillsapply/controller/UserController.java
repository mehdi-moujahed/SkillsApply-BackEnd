package com.reactit.Skillsapply.controller;

import com.reactit.Skillsapply.model.User;
import com.reactit.Skillsapply.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/user")
    public User addUser(@RequestBody User user) {
        return this.userService.saveUser(user);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public List<User> getAllUsers() {
        return this.userService.getAll();
    }

}