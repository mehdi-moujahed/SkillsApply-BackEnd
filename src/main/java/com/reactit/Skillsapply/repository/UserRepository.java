package com.reactit.Skillsapply.repository;


import com.reactit.Skillsapply.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    User findByEmail(String email);

    User findByEmailAndRoles(String email,String roles);

    Optional <User> findByIdAndRoles(String id, String roles);

    List <User> findByRoles (String roles);

    Page<User> findByManagerIDAndCreatedAtBetweenAndRolesAndEmailContainingAndDiplomaAllIgnoreCase(String managerID,  Date date1, Date date2,String roles, String email, int diploma  ,Pageable pageable);

    Page<User> findByManagerIDAndCreatedAtBetweenAndRolesAndEmailContainingAllIgnoreCase(String managerID, Date date1, Date date2,String roles, String email , Pageable pageable);

    Page<User> findByManagerIDAndRolesAndDiploma(String managerID, String roles,int diploma ,Pageable pageable);

    Page <User> findByManagerID (String managerID, Pageable pageable);
}
