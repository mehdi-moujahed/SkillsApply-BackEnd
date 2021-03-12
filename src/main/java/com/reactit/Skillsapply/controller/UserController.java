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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.security.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
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

//    @PostMapping("/user/updatePassword")
//    @PreAuthorize("hasRole('READ_PRIVILEGE')")
//    public GenericResponse changeUserPassword(Locale locale,
//                                              @RequestParam("password") String password,
//                                              @RequestParam("oldpassword") String oldPassword) {
//        User user = userService.findUserByEmail(
//                SecurityContextHolder.getContext().getAuthentication().getName());
//
//        if (!userService.checkIfValidOldPassword(user, oldPassword)) {
//            throw new InvalidOldPasswordException();
//        }
//        userService.changeUserPassword(user, password);
//        return new GenericResponse(messages.getMessage("message.updatePasswordSuc", null, locale));
//    }

//    @PostMapping(value = "/updatePassword")
//    @PreAuthorize("hasAuthority('USER')")
//    public ResponseEntity<Void> updatePassword(@RequestBody User user2) {
//
//        User user = userRepository.findByEmail(
//                SecurityContextHolder.getContext().getAuthentication().getName());
//
//        if(!userRepository.check)
//
//        return new ResponseEntity(HttpStatus.OK);
//    }


    @ApiOperation(value = "Candidate Registration")
    @PostMapping(value = "/signup")
    public ResponseEntity<Void> addCandidate(@RequestBody User user) {


//       int length = String.valueOf(user.getPhoneNumber()).length();
//       System.out.println("phoneNumber length "+length);

        try{
            if(userRepository.findByEmail(user.getEmail())== null) {
                if(user.getPassword().length()<8 || user.getPassword().length()>16 ){
                    return new ResponseEntity("Password must be between 8 and 16 characters",
                            HttpStatus.BAD_REQUEST);
                } else {
                    Pattern p = Pattern.compile("[^A-Za-z0-9]");
                    Matcher m = p.matcher(user.getPassword());
                    boolean passwordLengthCheck = m.find();

                    Pattern pattern = Pattern.compile("^[0-9]{8}$");
                    Matcher matcher = pattern.matcher(user.getPhoneNumber());
                    boolean phoneNumberLengthCheck = matcher.find();
                System.out.println("Password Length : "+user.getPassword().length());
                    if (passwordLengthCheck && phoneNumberLengthCheck ){
//                        System.out.println("Password is after verif : "+user.getPassword());
                        user.setPassword(passwordEncoder.encode(user.getPassword()));
//                        System.out.println("Password is after Hash : "+user.getPassword());
//                        user.setCreatedAt(registerTimeDate);

//
//                         LocalDateTime now = LocalDateTime.now();
//                        ZonedDateTime timeAndDate = now.atZone(ZoneId.systemDefault());
//                        Date registerTimeDate = Date.from(timeAndDate.toInstant());
//                        user.setCreatedAt(registerTimeDate);


//
                        user.setCreatedAt(new Date());

                        User userAdded = userRepository.save(user);
                        return new ResponseEntity("User added successfully, Details :" + "\n"
                                + user.getFirstName() + "\n"
                                + user.getLastName() + "\n" + "User ID : "
                                + user.getId(),
                                HttpStatus.OK);
                    }else{
                        return new ResponseEntity("Password must contain at least one : Capital letter, Number and Special Characters \n" +
                                "phoneNumber must have 8 numbers ", HttpStatus.BAD_REQUEST);
                    }
                }
            }
            else
                {
                return new ResponseEntity("User Already exists !", HttpStatus.BAD_REQUEST);
            }

        } catch (ConstraintViolationException e) {
            return new ResponseEntity( e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

}