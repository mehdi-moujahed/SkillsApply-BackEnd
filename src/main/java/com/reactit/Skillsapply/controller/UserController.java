package com.reactit.Skillsapply.controller;

import com.reactit.Skillsapply.dto.UpdatePassword;
import com.reactit.Skillsapply.dto.UpdateUserProfile;
import com.reactit.Skillsapply.model.Manager;
import com.reactit.Skillsapply.model.Test;
import com.reactit.Skillsapply.model.User;
import com.reactit.Skillsapply.repository.UserRepository;
import com.reactit.Skillsapply.service.FilesStorageService;
import com.reactit.Skillsapply.service.FilesStorageServiceImpl;
import com.reactit.Skillsapply.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolationException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Api( description="API For All Operations Concerning Candidates")
@RestController
//@EnableMongoAuditing
@RequestMapping("/users")
@Validated
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    FilesStorageService storageService;

    @Autowired
    FilesStorageServiceImpl filesStorageServiceImpl;

    public static String uploadDirectory = System.getProperty("user.dir")+"/photos";



    @ApiOperation(value = "Update User Profile")
    @PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value = "/UpdateProfile/{id}",method = RequestMethod.POST, consumes = { "multipart/form-data" })
    public ResponseEntity<UpdateUserProfile> updateProfile(@ModelAttribute UpdateUserProfile updateUser,
                                        @RequestParam("files") MultipartFile[] files,
                                        @PathVariable("id") String id){
        Optional<User> dbUser = userRepository.findById(id);
        if(dbUser.isPresent()){
            StringBuilder fileNames = new StringBuilder();
            String uploadDir = "E:\\boudj\\Documents\\uploads\\photos\\"+"User "+dbUser.get().getId()+"\\";
            String filename = "Avatar";
            for (MultipartFile file : files) {
                if(files[0].getContentType().equals("image/jpeg") || files[0].getContentType().equals("image/png")){
                    Path fileNameAndPath = Paths.get(uploadDirectory, file.getOriginalFilename());
                    fileNames.append(file.getOriginalFilename()+" ");
                    try {
                        if(Files.notExists(Path.of(uploadDir)))
                        Files.createDirectory(Path.of(uploadDir));
                        System.out.println(" file directory "+fileNames.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else
                    return new ResponseEntity("Pleaser verify your image extension !",HttpStatus.BAD_REQUEST);

            }
            storageService.save(files[0],uploadDir,filename);

            User userToEdit = dbUser.get();
            userToEdit.setFirstName(updateUser.getFirstName());
            userToEdit.setLastName(updateUser.getLastName());
            userToEdit.setAddress(updateUser.getAddress());
            userToEdit.setEmail(updateUser.getEmail());
            userToEdit.setImg(uploadDir+filename+"."+FilenameUtils.getExtension(files[0].getOriginalFilename()));

            this.userRepository.save(userToEdit);

            return ResponseEntity.ok(updateUser);
        }
        else
            return new ResponseEntity("USER NOT FOUND !",HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "Update User Password")
    @PreAuthorize("hasAuthority('USER')")
    @PatchMapping(value ="/updatePassword/{id}")
    public ResponseEntity<Void> updatePassword(@RequestBody UpdatePassword updatePassword,
                                               @RequestHeader("Authorization") String token,
                                               @PathVariable("id") String id) {

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

    @ApiOperation(value = "Candidate update password")
//    @PreAuthorize("hasAuthority('USER') or hasRole('MANAGER')")
    @PatchMapping(value = "/candidateUpdatePassword/{id}")
    public ResponseEntity<Map<String,Object>> updateCandidatePassword(@PathVariable("id") String id,
                                                        @RequestBody UpdatePassword updatePassword) {

        Map<String, Object> response = new HashMap<>();

        Optional<User> user = userRepository.findByIdAndRoles(id,"USER");
        if(user.isPresent()) {
            if(updatePassword.getNewPassword().equals(updatePassword.getConfirmNewPassword())){
                User editUser = user.get();
                Pattern p = Pattern.compile("[^A-Za-z0-9]");
                Matcher m = p.matcher(updatePassword.getNewPassword());
                boolean passwordLengthCheck = m.find();
                if(passwordLengthCheck){
                    editUser.setPassword(passwordEncoder.encode(updatePassword.getNewPassword()));
                    userRepository.save(editUser);
                    response.put("message","Inscription Terminé avec succés");
                    return new ResponseEntity(response, HttpStatus.OK);
                } else {
                    response.put("message","Le Mot de passe doit contenir 8 caractéres et au moins : une lettre majiscule, une lettre miniscule et un nombre ");
                    return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
                }
            } else {
                response.put("message","Les deux mots de passe ne sont pas identiques !");
                return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
            }
        } else
        return new ResponseEntity("Candidat Inexistant", HttpStatus.NOT_FOUND);
    }


    @ApiOperation(value = "Candidate Registration")
//    @PreAuthorize("hasAuthority('USER') or hasRole('MANAGER')")
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
                        user.setRoles("USER");
                        User userAdded = userRepository.save(user);
                        return new ResponseEntity("User added successfully, Details :" + "\n"
                                + user.getFirstName() + "\n"
                                + user.getLastName() + "\n" + "User ID : "
                                + user.getId() + "\n" + "Role : "
                                + user.getRoles(),
                                HttpStatus.OK);
                    }
                }
            } else {
                return new ResponseEntity("User Already exists !", HttpStatus.BAD_REQUEST);
            }

        } catch (ConstraintViolationException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    @ApiOperation(value = "Delete Candidate")
//    @PreAuthorize("hasAuthority('USER') or hasRole('MANAGER')")
    @DeleteMapping(value = "/deleteCandidate/{id}")
    public ResponseEntity<Void> deleteCandidateByID(@PathVariable("id") String id ){
        Map<String, Object> response = new HashMap<>();
        Optional<User> candidate = userRepository.findById(id);
        if(candidate.isPresent()) {
            userRepository.deleteById(id);
            response.put("message","candidat supprimé avec succés");
            return new ResponseEntity(response,HttpStatus.OK);
        } else {
            response.put("message","Candidat Inexistant !");
            return new ResponseEntity(response,HttpStatus.NO_CONTENT);
        }
    }

    @ApiOperation(value = "Get Candidates by managerID")
//    @PreAuthorize("hasAuthority('USER') or hasRole('MANAGER')")
    @GetMapping(value = "/getAllCandidates/{id}")
    public ResponseEntity<Void> getAllCandidates(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "5") int size,
                                                 @PathVariable("id") String id,
                                                 @RequestParam(defaultValue = "10") int diploma,
                                                 @RequestParam(defaultValue ="") String email,
                                                 @RequestParam(defaultValue = "2020-01-01") String date1,
                                                 @RequestParam(defaultValue = "2121-01-26") String date2) {
        Map<String, Object> response = new HashMap<>();
        try {

                List<User> candidates = new ArrayList<User>();
                Pageable paging = PageRequest.of(page, size);

                Page<User> pageTuts;

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

                Date date11 = formatter.parse(date1);
                Date date22 = formatter.parse(date2);

                if(diploma==50){
                    pageTuts = userRepository.findByManagerIDAndCreatedAtBetweenAndRolesAndEmailContainingAllIgnoreCase(id,date11,date22,"USER",email,paging);
                } else
                pageTuts = userRepository.findByManagerIDAndCreatedAtBetweenAndRolesAndEmailContainingAndDiplomaAllIgnoreCase(id,date11,date22,"USER",email,diploma,paging);
                candidates = pageTuts.getContent();
                response.put("candidates",candidates);
                response.put("currentPage", pageTuts.getNumber());
                response.put("totalItems", pageTuts.getTotalElements());
                response.put("totalPages", pageTuts.getTotalPages());
                return new ResponseEntity(response,HttpStatus.OK);

        } catch (Exception exception) {
            response.put("error",exception);
            return new ResponseEntity(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Get Candidates by managerID")
//    @PreAuthorize("hasAuthority('USER') or hasRole('MANAGER')")
    @GetMapping(value = "/getNumberOfCandidates/{id}")
    public ResponseEntity<Void> getNumberOfCandidates(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "5") int size,
                                                 @PathVariable("id") String id
                                                ) {
        Map<String, Object> response = new HashMap<>();
        try {

            Pageable paging = PageRequest.of(page, size);
            Page<User> pageTuts;
            pageTuts = userRepository.findByManagerID(id,paging);
            response.put("totalCandidates", pageTuts.getTotalElements());
            return new ResponseEntity(response,HttpStatus.OK);

        } catch (Exception exception) {
            response.put("error",exception);
            return new ResponseEntity(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Candidate Registration")
//    @PreAuthorize("hasAuthority('USER') or hasRole('MANAGER')")
    @PostMapping(value = "/candidateSignup")
    public ResponseEntity<Void> candidateRegistration(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (userRepository.findByEmail(user.getEmail()) == null) {
                    Pattern pattern = Pattern.compile("^[0-9]{8}$");
                    Matcher matcher = pattern.matcher(user.getPhoneNumber());
                    boolean phoneNumberLengthCheck = matcher.find();

                     if (phoneNumberLengthCheck == false)
                        return new ResponseEntity("phoneNumber must have 8 numbers ", HttpStatus.BAD_REQUEST);
                    else if (phoneNumberLengthCheck) {

                        user.setFirstName(user.getFirstName());
                        user.setLastName(user.getLastName());
                        user.setBirthDate(user.getBirthDate());
                        user.setDiploma(user.getDiploma());
                        user.setPhoneNumber(user.getPhoneNumber());
                        user.setCreatedAt(new Date());
                        user.setRoles("USER");
                        user.setManagerID("60a79878a0784e4240d4a619");
                        userRepository.save(user);
                        response.put("message","Candidat ajouté avec succés");
                        return new ResponseEntity(response,HttpStatus.OK);
                     }
            } else {
                response.put("message","email déja utilisé !");
                return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
            }

        } catch (ConstraintViolationException e) {
            response.put("error",e.getMessage());
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    @ApiOperation(value = "Updating Candidate Profile")
//    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping(value = "/updateCandidate/{id}")
    public ResponseEntity<Void> updateCandidate(@RequestBody User user, @PathVariable String id){
        Map<String, Object> response = new HashMap<>();
        Optional<User> dbUser = userRepository.findById(id);
        if(dbUser.isPresent()){
            User userToEdit = dbUser.get();
            userToEdit.setFirstName(user.getFirstName());
            userToEdit.setLastName(user.getLastName());
            userToEdit.setEmail(user.getEmail());
            userToEdit.setPhoneNumber(user.getPhoneNumber());
            userToEdit.setBirthDate(user.getBirthDate());
            userToEdit.setDiploma(user.getDiploma());
            userRepository.save(userToEdit);
            response.put("message","Candidat modifié avec succés");
            return new ResponseEntity(response,HttpStatus.OK);
        } else {
            response.put("message", "erreur lors du modification du candidat");
            return new ResponseEntity(response, HttpStatus.NOT_FOUND);
        }
    }
}
