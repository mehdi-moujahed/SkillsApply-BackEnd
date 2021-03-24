package com.reactit.Skillsapply.repository;

import com.reactit.Skillsapply.model.Manager;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ManagerRepository extends MongoRepository<Manager, String> {

    Manager findByEmail(String email);

    List <Manager> findByRoles(String roles);

}
