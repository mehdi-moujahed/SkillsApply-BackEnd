package com.reactit.Skillsapply.repository;

import com.reactit.Skillsapply.model.Membership;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MembershipRepository extends MongoRepository<Membership, String > {

    Membership findByLabel(String label);

}
