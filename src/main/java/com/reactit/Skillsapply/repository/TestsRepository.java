package com.reactit.Skillsapply.repository;

import com.reactit.Skillsapply.model.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;

public interface TestsRepository extends MongoRepository<Test,String> {

    ArrayList<Test> findByManagerID(String managerID);

    Page<Test> findByManagerIDAndNameContainingOrderByCreatedAtDescAllIgnoreCase(String managerID, String name,Pageable pageable);

    Page<Test> findByManagerIDAndNameContainingOrderByRateDescAllIgnoreCase(String managerID, String name,Pageable pageable);



}
