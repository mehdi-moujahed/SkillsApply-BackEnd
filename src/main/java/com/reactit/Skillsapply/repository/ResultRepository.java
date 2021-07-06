package com.reactit.Skillsapply.repository;

import com.reactit.Skillsapply.model.Result;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ResultRepository extends MongoRepository<Result,String> {

}
