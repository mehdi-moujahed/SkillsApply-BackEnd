package com.reactit.Skillsapply.repository;

import com.reactit.Skillsapply.model.Answers;
import com.reactit.Skillsapply.model.Questions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AnswersRepository extends MongoRepository<Answers, String> {


}

// interface AnswerCustomRepository {
//
//    List<Answers> findAllAnswersByQuestion();
//}
//
//
// class AnswerCustomRepositoryImpl implements AnswerCustomRepository {
//
//    @Autowired
//    private MongoTemplate mongoTemplate;
//
//    @Override
//    public List<Answers> findAllAnswersByQuestion() {
//        LookupOperation lookup = LookupOperation.newLookup()
//                .from("answers")
//                .localField("id")
//                .foreignField("answersID")
//                .as("join_questions");
//        Aggregation aggregation = Aggregation.newAggregation(
//                Aggregation.match(Criteria.where("answersID").is("answersID")),
//                lookup);
//        return mongoTemplate.aggregate(aggregation, Questions.class, Answers.class).getMappedResults();
//    }
//}