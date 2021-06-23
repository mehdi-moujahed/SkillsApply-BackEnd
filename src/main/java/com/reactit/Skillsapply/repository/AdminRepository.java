package com.reactit.Skillsapply.repository;

import com.reactit.Skillsapply.model.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AdminRepository extends MongoRepository<Admin, String> {

    Admin findByEmail(String email);

    List<Admin> findByRoles(String roles);

}
