package com.reactit.Skillsapply.repository;


import com.reactit.Skillsapply.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {

    User findByEmail(String email);

    List <User> findByRoles (String roles);

}
