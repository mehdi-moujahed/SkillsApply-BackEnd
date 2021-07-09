package com.reactit.Skillsapply.repository;

import com.reactit.Skillsapply.model.Result;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ResultRepository extends MongoRepository<Result,String> {

    List <Result> findByManagerId(String managerId);

}
