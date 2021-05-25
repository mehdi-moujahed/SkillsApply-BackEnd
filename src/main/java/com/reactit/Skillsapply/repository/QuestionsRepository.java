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

    List<QuestionDTO> findAllAnswersByQuestion();
}


class AnswerCustomRepositoryImpl implements AnswerCustomRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<QuestionDTO> findAllAnswersByQuestion() {
        LookupOperation lookup = LookupOperation.newLookup()
                .from(mongoTemplate.getCollectionName(Answers.class))
                .localField("answersID[]")
                .foreignField("id")
                .as("answers");
        Aggregation aggregation = Aggregation.newAggregation(QuestionDTO.class, lookup);
        return mongoTemplate.aggregate(aggregation, mongoTemplate.getCollectionName(Questions.class),
                QuestionDTO.class).getMappedResults();
    }
}
