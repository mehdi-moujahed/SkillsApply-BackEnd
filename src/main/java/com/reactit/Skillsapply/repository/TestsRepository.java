package com.reactit.Skillsapply.repository;

import com.reactit.Skillsapply.model.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Date;

public interface TestsRepository extends MongoRepository<Test,String> {

    ArrayList<Test> findByManagerID(String managerID);

    Page<Test> findByCreatedAtBetweenAndRateBetweenAndManagerIDAndNameContainingAndLevelOrderByCreatedAtDescAllIgnoreCase(Date date1, Date date2,float rate1, float rate2 ,String managerID,  String name, float level, Pageable pageable);

    Page<Test> findByCreatedAtBetweenAndRateBetweenAndManagerIDAndNameContainingAndLevelOrderByRateDescAllIgnoreCase(Date date1, Date date2,float rate1, float rate2,String managerID, String name,float level ,Pageable pageable);

 Page<Test> findByAndCreatedAtBetweenAndRateBetweenAndPremiumPackIsAndNameContainingAndLevelOrderByCreatedAtDescAllIgnoreCase(Date date1, Date date2,float rate1, float rate2 ,boolean premiumPack, String name, float level, Pageable pageable);

    Page<Test> findByCreatedAtBetweenAndRateBetweenAndPremiumPackIsAndNameContainingAndLevelOrderByRateDescAllIgnoreCase(Date date1, Date date2,float rate1, float rate2,boolean premuimPack, String name,float level ,Pageable pageable);

    Page<Test> findByPremiumPackIsOrderByCreatedAtDescAllIgnoreCase(boolean premiumPack, Pageable pageable);

    Page<Test> findByPremiumPackIsOrderByRateDescAllIgnoreCase(boolean premiumPack, Pageable pageable);

    Page<Test> findByPremiumPackIsAndLevel(boolean premiumPack, float level,Pageable pageable);



}
