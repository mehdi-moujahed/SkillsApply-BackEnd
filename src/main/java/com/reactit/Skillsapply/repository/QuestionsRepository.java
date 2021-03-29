package com.reactit.Skillsapply.repository;

import com.reactit.Skillsapply.dto.QuestionDTO;
import com.reactit.Skillsapply.model.Answers;
import com.reactit.Skillsapply.model.Questions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface QuestionsRepository  extends MongoRepository<Questions, String>, AnswerCustomRepository{

    Questions findByQuestion(String question);

}

interface AnswerCustomRepository {

    List<Questions> findAllAnswersByQuestion();
}


class AnswerCustomRepositoryImpl implements AnswerCustomRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Questions> findAllAnswersByQuestion() {
        LookupOperation lookup = LookupOperation.newLookup()
                .from("Questions")
                .localField("answersID")
                .foreignField("_id")
                .as("answer");
        Aggregation aggregation = Aggregation.newAggregation(

                lookup);
        return mongoTemplate.aggregate(aggregation, Answers.class, Questions.class).getMappedResults();
    }
}
