package com.reactit.Skillsapply.repository;


import com.reactit.Skillsapply.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

    User findByEmail(String email);

//    User findUserById(String id);



}
