package com.reactit.Skillsapply.controller;

import com.reactit.Skillsapply.model.Admin;
import com.reactit.Skillsapply.model.Manager;
import com.reactit.Skillsapply.model.Membership;
import com.reactit.Skillsapply.model.User;
import com.reactit.Skillsapply.repository.AdminRepository;
import com.reactit.Skillsapply.repository.ManagerRepository;
import com.reactit.Skillsapply.repository.MembershipRepository;
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
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
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
    private UserRepository userRepository;

    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @ApiOperation(value = "List All Admins")
//    @PreAuthorize("hasAuthority('ADMIN') or hasAnyAuthority('Manager')")
    @GetMapping("/getAllAdmins")
    public List<Admin> getAllAdmins() {
        return adminRepository.findByRoles("ADMIN");
    }

    @ApiOperation(value = "List All Candidates")
//    @PreAuthorize("hasAuthority('ADMIN') or hasAnyAuthority('Manager')")
    @GetMapping("/getAllCandidates")
    public List<User> getAllCandidates() {
        return userRepository.findByRoles("USER");
    }

    @ApiOperation(value = "List All Memberships Packs")
//    @PreAuthorize("hasAuthority('ADMIN') or hasAnyAuthority('Manager')")
    @GetMapping("/getAllMemberships")
    public List<Membership> getAllMemberships() {
        return membershipRepository.findAll();
    }

    @ApiOperation(value = "Updating Membership Pack")
//    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping(value = "/updateMembership/{id}")
    public ResponseEntity<Void> updateMembership(@RequestBody Membership membership, @PathVariable String id){
        Optional<Membership> dbMembership = membershipRepository.findById(id);
        if(dbMembership.isPresent()){
            Membership membershipEdit = dbMembership.get();
            membershipEdit.setDuration(membership.getDuration());
            membershipEdit.setLabel(membership.getLabel());
            membershipEdit.setPrice(membership.getPrice());
            membershipEdit.setDescription(membership.getDescription());
            membershipRepository.save(membershipEdit);
            return new ResponseEntity("Membership pack updated successfully",HttpStatus.OK);
        } else
            return new ResponseEntity("Membership pack does not exist",HttpStatus.NOT_FOUND);
    }


    @ApiOperation(value = "Adding New Membership Pack")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/addNewMembership")
    public ResponseEntity<Void> addMembership(@RequestBody Membership membership) {
        if (membershipRepository.findByLabel(membership.getLabel())== null){
            if (membershipRepository.count() < 3) {
                membershipRepository.save(membership);
                return new ResponseEntity("Membership Pack added successfully, Details :" + "\n"
                        + "Pack Name : " + membership.getLabel() + "\n"
                        + "Advantages : " + membership.getDescription() + "\n"
                        + "Price : " + membership.getPrice()+ "TND",
                        HttpStatus.OK);
            } else
                return new ResponseEntity("You cannot add more than 3 Memberships Packs", HttpStatus.BAD_REQUEST);
        }  else
            return new ResponseEntity("Membership Already exists !", HttpStatus.BAD_REQUEST);
    }

    @ApiOperation(value = "Delete Membership Pack By ID")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "/deleteMembership/{id}")
    public ResponseEntity deleteMembershipById(@PathVariable String id) throws Exception {
        Optional<Membership> membership = membershipRepository.findById(id);
        if(membership.isPresent()) {
            membershipRepository.deleteById(id);
            HashMap<String, String> resp = new HashMap<>();
            return new ResponseEntity<>("Membership Pack Deleted Successfully ", HttpStatus.OK);
        } else
            return new ResponseEntity("Membership Pack Not Found !",HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "Admin Registration")
//    @PreAuthorize("hasAuthority('ADMIN')")
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

    @ApiOperation(value = "Adding new RH Manager")
//    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PostMapping(value = "/addManager")
    public ResponseEntity<Void> addNewManager(@RequestBody Manager manager){
        try {
            if (managerRepository.findByEmail(manager.getEmail()) == null) {
                if (manager.getPassword().length() < 8 || manager.getPassword().length() > 16) {
                    return new ResponseEntity("Password must be between 8 and 16 characters",
                            HttpStatus.BAD_REQUEST);
                } else {
                    Pattern p = Pattern.compile("[^A-Za-z0-9]");
                    Matcher m = p.matcher(manager.getPassword());
                    boolean passwordLengthCheck = m.find();

                    Pattern pattern = Pattern.compile("^[0-9]{8}$");
                    Matcher matcher = pattern.matcher(manager.getPhoneNumber());
                    boolean phoneNumberLengthCheck = matcher.find();
                    System.out.println("Password Length : " + manager.getPassword().length());

                    if (passwordLengthCheck == false)
                        return new ResponseEntity("Password must contain at least one : Capital letter, " +
                                "Number and Special Characters", HttpStatus.BAD_REQUEST);

                    else if (phoneNumberLengthCheck == false)
                        return new ResponseEntity("phoneNumber must have 8 numbers ", HttpStatus.BAD_REQUEST);

                    else if (passwordLengthCheck && phoneNumberLengthCheck) {
                        manager.setPassword(passwordEncoder.encode(manager.getPassword()));
                        manager.setCreateAt(new Date());
                        manager.setRoles("MANAGER");
                        Manager managerAdded = managerRepository.save(manager);
                        return new ResponseEntity("RH Manager added successfully, Details :" + "\n"
                                + manager.getFirstName() + "\n"
                                + manager.getLastName() + "\n" + "Manager ID : "
                                + manager.getId() + "\n" + "Role : "
                                + manager.getRoles() + "\n" +"Company : "
                                + manager.getCompanyName(),
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


    @ApiOperation(value = "Listing All RH Managers")
//    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/getAllManagers")
    public List<Manager> getAllManagers() {
        return managerRepository.findByRoles("MANAGER");
    }

    @ApiOperation(value = "Updating RH Manager Account")
//    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping(value = "/updateManagerAccount/{id}")
    public ResponseEntity<Void> updateManagerAccount(@RequestBody Manager manager, @PathVariable String id){
        Optional<Manager> dbManager = managerRepository.findById(id);
        if(dbManager.isPresent()){
            Manager managerEdit = dbManager.get();
            managerEdit.setFirstName(manager.getFirstName());
            managerEdit.setLastName(manager.getLastName());
            managerEdit.setAddress(manager.getAddress());
            managerEdit.setEmail(manager.getEmail());
            managerEdit.setBirthDate(manager.getBirthDate());
            managerEdit.setPhoneNumber(manager.getPhoneNumber());
            managerRepository.save(managerEdit);
            return new ResponseEntity("Manager Account updated successfully",HttpStatus.OK);
        } else
            return new ResponseEntity("Manager Account does not exist !",HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "Delete RH Manager Account By ID")
//    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "/deleteManagerAccount/{id}")
    public ResponseEntity deleteManagerAccountById(@PathVariable String id) throws Exception {
        Optional<Manager> dbManager = managerRepository.findById(id);
        if(dbManager.isPresent()) {
            managerRepository.deleteById(id);
            HashMap<String, String> resp = new HashMap<>();
            return new ResponseEntity<>("Manager Account with \n ID : "+dbManager.get().getId()+
                                        " is Deleted Successfully ", HttpStatus.OK);
        } else
            return new ResponseEntity("Manager Account with this ID does Not Exists !",HttpStatus.NOT_FOUND);
    }


}
