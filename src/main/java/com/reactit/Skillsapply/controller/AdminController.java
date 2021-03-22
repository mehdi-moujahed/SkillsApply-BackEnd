package com.reactit.Skillsapply.controller;

import com.reactit.Skillsapply.model.Admin;
import com.reactit.Skillsapply.model.User;
import com.reactit.Skillsapply.repository.AdminRepository;
import com.reactit.Skillsapply.repository.UserRepository;
import com.reactit.Skillsapply.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequestMapping("/admin")
@Api( description="API For All Operations Concerning Admin")
@RestController
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @ApiOperation(value = "List All Candidates")
    @PreAuthorize("hasAuthority('ADMIN') or hasAnyAuthority('Manager')")
    @GetMapping("/getAllCandidates")
    public List<User> getAllCandidates() {
        return this.userService.getAll();
    }


    @ApiOperation(value = "Admin Registration")
    @PostMapping(value = "/signup")
    public ResponseEntity<Void> adminRegistration(@RequestBody Admin admin ) {
        try {
            if (adminRepository.findByEmail(admin.getEmail()) == null) {
                if (admin.getPassword().length() < 8 || admin.getPassword().length() > 16) {
                    return new ResponseEntity("Password must be between 8 and 16 characters",
                            HttpStatus.BAD_REQUEST);
                } else {
                    Pattern p = Pattern.compile("[^A-Za-z0-9]");
                    Matcher m = p.matcher(admin.getPassword());
                    boolean passwordLengthCheck = m.find();

                    Pattern pattern = Pattern.compile("^[0-9]{8}$");
                    Matcher matcher = pattern.matcher(admin.getPhoneNumber());
                    boolean phoneNumberLengthCheck = matcher.find();
                    System.out.println("Password Length : " + admin.getPassword().length());

                    if (passwordLengthCheck == false)
                        return new ResponseEntity("Password must contain at least one : Capital letter, " +
                                "Number and Special Characters", HttpStatus.BAD_REQUEST);

                    else if (phoneNumberLengthCheck == false)
                        return new ResponseEntity("phoneNumber must have 8 numbers ", HttpStatus.BAD_REQUEST);

                    else if (passwordLengthCheck && phoneNumberLengthCheck) {
                        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
                        admin.setCreateAt(new Date());
                        admin.setRoles("ADMIN");
                        Admin adminAdded = adminRepository.save(admin);
                        return new ResponseEntity("Admin added successfully, Details :" + "\n"
                                + admin.getFirstName() + "\n"
                                + admin.getLastName() + "\n" + "admin ID : "
                                + admin.getId() + "\n" + "Role : "
                                + admin.getRoles(),
                                HttpStatus.OK);
                    }
                }
            } else {
                return new ResponseEntity("Email is Already in use !", HttpStatus.BAD_REQUEST);
            }

        } catch (ConstraintViolationException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return null;
    }

}
