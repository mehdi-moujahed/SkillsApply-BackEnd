package com.reactit.Skillsapply.repository;

import com.reactit.Skillsapply.model.Membership;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MembershipRepository extends MongoRepository<Membership, String > {

    Membership findByLabel(String label);

}
