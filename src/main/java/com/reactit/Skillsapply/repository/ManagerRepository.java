package com.reactit.Skillsapply.repository;

import com.reactit.Skillsapply.model.Manager;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Optional;

@CrossOrigin
public interface ManagerRepository extends MongoRepository<Manager, String> {

    Manager findByEmail(String email);

    List <Manager> findByRoles(String roles);

    Optional<Manager> findByToken(String token);
}
