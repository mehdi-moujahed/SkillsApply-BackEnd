package com.reactit.Skillsapply.controller;

import com.reactit.Skillsapply.model.User;
import com.reactit.Skillsapply.repository.UserRepository;
import com.reactit.Skillsapply.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Api( description="API For All Operations Concerning Candidates")
@RestController
//@EnableMongoAuditing
@RequestMapping("/users")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
	private PasswordEncoder passwordEncoder;


    @ApiOperation(value = "List All Candidates")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/getAllCandidates")
    public List<User> getAllCandidates() {
        return this.userService.getAll();
    }

    @ApiOperation(value = "Candidate Registration")
    @PostMapping(value = "/signup")
    public ResponseEntity<Void> addCandidate(@RequestBody User user) {
        try{
            if(user.getPassword().length()<8 || user.getPassword().length()>16){
                return new ResponseEntity("Password must be between 8 and 16 characters" +
                        " with Upper case, number and special characters", HttpStatus.BAD_REQUEST);
            } else {
                Pattern p = Pattern.compile("[^A-Za-z0-9]");
                Matcher m = p.matcher(user.getPassword());
                boolean b = m.find();
                System.out.println("Password is before verif : "+user.getPassword());
                if (b){
                    System.out.println("Password is after verif : "+user.getPassword());
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                    System.out.println("Password is after Hash : "+user.getPassword());
                    User userAdded = userRepository.save(user);
                    return new ResponseEntity("User added successfully, Details :" + "\n"
                            + user.getFirstName() + "\n"
                            + user.getLastName() + "\n" + "User ID : "
                            + user.getId(),
                            HttpStatus.OK);
                }else{
                    return new ResponseEntity("Password must contain UpperCase, number and special characters", HttpStatus.BAD_REQUEST);
                }
            }
        } catch (ConstraintViolationException e) {
            return new ResponseEntity( e.getMessage(),
                    HttpStatus.BAD_REQUEST);

        }

    }

}