package com.reactit.Skillsapply.controller;

import com.reactit.Skillsapply.dto.UpdatePassword;
import com.reactit.Skillsapply.model.User;
import com.reactit.Skillsapply.repository.UserRepository;
import com.reactit.Skillsapply.service.FilesStorageService;
import com.reactit.Skillsapply.service.FilesStorageServiceImpl;
import com.reactit.Skillsapply.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
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

    @Autowired
    FilesStorageService storageService;






    @PostMapping("/uploadPhoto")
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            storageService.save(file);
            return new ResponseEntity("Uploaded the file successfully: "+ file.getOriginalFilename(),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity("Could not upload the file: " + file.getOriginalFilename() + "!"
                   ,HttpStatus.EXPECTATION_FAILED);
        }
    }


    @ApiOperation(value = "List All Candidates")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/getAllCandidates")
    public List<User> getAllCandidates() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        System.out.println("authentication : "+authentication);
//        Object details = authentication.getDetails();
//        System.out.println("details : "+details);
//        if ( details instanceof OAuth2AuthenticationDetails){
//            OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails)details;
//
//           System.out.println("Decoded DETAILS : "+oAuth2AuthenticationDetails.getDecodedDetails());
////            Map<String, Object> decodedDetails = (Map<String, Object>)oAuth2AuthenticationDetails.getDecodedDetails();
//
////            System.out.println( "My custom claim value: " + decodedDetails.get("MyClaim") );
//        }

        return this.userService.getAll();
    }



//    @PostMapping("/photos/add")
//    public ResponseEntity addPhoto(@RequestParam("title") String title,
//                           @RequestParam("image") MultipartFile image, Model model)
//            throws IOException {
//        String id = photoService.addPhoto(title, image);
//        return new ResponseEntity( "Photo Added Successfully\nPhoto id :"+id, HttpStatus.OK);
//    }
//
//    @GetMapping("/photos/{id}")
//    public ResponseEntity getPhoto(@PathVariable String id, Model model) {
//        Photo photo = photoService.getPhoto(id);
//        model.addAttribute("title", photo.getTitle());
//        model.addAttribute("image",
//                Base64.getEncoder().encodeToString(photo.getImage().getData()));
//        return new ResponseEntity( "photo exists with id : "+photo.getId(), HttpStatus.OK);
//    }

    @ApiOperation(value = "Update User Password")
    @PreAuthorize("hasAuthority('USER')")
    @PatchMapping(value ="/updatePassword/{id}")
    public ResponseEntity<Void> updatePassword(@RequestBody UpdatePassword updatePassword, @RequestHeader("Authorization") String token, @PathVariable("id") String id) {

//        User user = userRepository.findUserById(id);
        System.out.println(token);

        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            boolean verifOldPassword = passwordEncoder.matches(updatePassword.getOldPassword(),user.get().getPassword());
            if(verifOldPassword){
                System.out.println("New Password "+updatePassword.getNewPassword());
                System.out.println("Confirm New Password "+updatePassword.getConfirmNewPassword());
                if(updatePassword.getNewPassword().equals(updatePassword.getConfirmNewPassword())){
                    User editUser = user.get();
                    Pattern p = Pattern.compile("[^A-Za-z0-9]");
                    Matcher m = p.matcher(updatePassword.getNewPassword());
                    boolean passwordLengthCheck = m.find();
                    if(passwordLengthCheck){
                        editUser.setPassword(passwordEncoder.encode(updatePassword.getNewPassword()));
                        userRepository.save(editUser);
                    } else {
                        return new ResponseEntity("Password must contain at least one : Capital letter, " +
                                "Number and Special Characters", HttpStatus.BAD_REQUEST);
                    }
                } else {
                    return new ResponseEntity("New Password and Confirm Password doesn't match ! ", HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity("Old password is not valid ! ", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    return new ResponseEntity("Password Updated Successfully",HttpStatus.OK);
    }


    @ApiOperation(value = "Candidate Registration")
    @PostMapping(value = "/signup")
    public ResponseEntity<Void> addCandidate(@RequestBody User user) {
        try {
            if (userRepository.findByEmail(user.getEmail()) == null) {
                if (user.getPassword().length() < 8 || user.getPassword().length() > 16) {
                    return new ResponseEntity("Password must be between 8 and 16 characters",
                            HttpStatus.BAD_REQUEST);
                } else {
                    Pattern p = Pattern.compile("[^A-Za-z0-9]");
                    Matcher m = p.matcher(user.getPassword());
                    boolean passwordLengthCheck = m.find();

                    Pattern pattern = Pattern.compile("^[0-9]{8}$");
                    Matcher matcher = pattern.matcher(user.getPhoneNumber());
                    boolean phoneNumberLengthCheck = matcher.find();
                    System.out.println("Password Length : " + user.getPassword().length());

                    if (passwordLengthCheck == false)
                        return new ResponseEntity("Password must contain at least one : Capital letter, " +
                                "Number and Special Characters", HttpStatus.BAD_REQUEST);

                    else if (phoneNumberLengthCheck == false)
                        return new ResponseEntity("phoneNumber must have 8 numbers ", HttpStatus.BAD_REQUEST);

                    else if (passwordLengthCheck && phoneNumberLengthCheck) {
                        user.setPassword(passwordEncoder.encode(user.getPassword()));
                        user.setCreatedAt(new Date());
                        User userAdded = userRepository.save(user);
                        return new ResponseEntity("User added successfully, Details :" + "\n"
                                + user.getFirstName() + "\n"
                                + user.getLastName() + "\n" + "User ID : "
                                + user.getId() + "\n" + "Role : "
                                + user.getRoles(),
                                HttpStatus.OK);
                    }

//                else{
//                        return new ResponseEntity("Password must contain at least one : Capital letter, Number and Special Characters \n" +
//                                "phoneNumber must have 8 numbers ", HttpStatus.BAD_REQUEST);
//                    }
                }

            } else {
                return new ResponseEntity("User Already exists !", HttpStatus.BAD_REQUEST);
            }

        } catch (ConstraintViolationException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return null;
    }
}
