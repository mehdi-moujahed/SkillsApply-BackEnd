package com.reactit.Skillsapply.controller;

import com.reactit.Skillsapply.model.User;
import com.reactit.Skillsapply.repository.UserRepository;
import com.reactit.Skillsapply.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
	private PasswordEncoder passwordEncoder;


    @PostMapping("/user")
    public User addUser(@RequestBody User user) {
        return this.userService.saveUser(user);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/getAllUsers")
    public List<User> getAllUsers() {
        return this.userService.getAll();
    }

    @PostMapping(value = "/signup")
    public ResponseEntity<Void> addCandidate(@RequestBody User user) {
        try{
            user.setPassword( passwordEncoder.encode(user.getPassword()));
            User userAdded = userRepository.save(user);

            return new ResponseEntity("User added successfully, Details :" + "\n"
                    + user.getFirstName() + "\n"
                    + user.getLastName() + "\n" + "User ID : "
                    + user.getId(),
                    HttpStatus.OK);

        } catch (ConstraintViolationException e) {
            return new ResponseEntity( e.getMessage(),
                    HttpStatus.BAD_REQUEST);

        }

    }

}