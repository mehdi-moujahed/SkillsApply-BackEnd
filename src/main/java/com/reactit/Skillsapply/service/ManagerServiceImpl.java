package com.reactit.Skillsapply.service;

import com.reactit.Skillsapply.dto.TestsDTO.Question;
import com.reactit.Skillsapply.dto.TestsDTO.Test;
import com.reactit.Skillsapply.model.Answers;
import com.reactit.Skillsapply.model.Questions;
import org.springframework.beans.factory.annotation.Autowired;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class ManagerServiceImpl implements ManagerService{

    private final MongoTemplate mongoTemplate;

    @Autowired
    public ManagerServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Test> getAggregationTestById(String idTest) {

        ProjectionOperation projectionOperation =
                project()
                        .and("_id").as("idTest")
                        .and("name").as("name")
                        .and("description").as("description")
                        .and("duration").as("duration")
                        .and("score").as("score")
                        .and("level").as("level")
                        .and("Test").as("questions");

        Aggregation aggregation = newAggregation(
                // filter tests by id test
                match(
                        Criteria.where("_id").is(idTest)
                ),
                // get questions for the specific test
                l -> new Document("$lookup",
                        new Document("from", mongoTemplate.getCollectionName(Questions.class))
                                .append("let", new Document("questionId",
                                        new Document("$map",
                                                new Document("input", "$questionsID")
                                                        .append("in",
                                                                new Document("$toObjectId", "$$this")
                                                        )
                                        )
                                ))
                                .append("pipeline",
                                        Arrays.asList(new Document("$match",
                                                new Document("$expr",
                                                        new Document("$in", Arrays.asList("$_id", "$$questionId"))))
                                        ))
                                .append("as", "Test")),
                // user receiver
//                l -> new Document("$lookup",
//                        new Document("from", mongoTemplate.getCollectionName(Answers.class))
//                                .append("let", new Document("userReceiverId",
//                                        new Document("$map",
//                                                new Document("input", "$beneficiariesDetials")
//                                                        .append("in",
//                                                                new Document("$toObjectId", "$$this.user")
//                                                        )
//                                        )
//                                ))
//                                .append("pipeline",
//                                        Arrays.asList(new Document("$match",
//                                                new Document("$expr",
//                                                        new Document("$in", Arrays.asList("$_id", "$$userReceiverId"))))
//                                        ))
//                                .append("as", "userBeneficieries")),
                projectionOperation
        );


        AggregationResults<Test> result = mongoTemplate.aggregate(
                aggregation, "Tests", Test.class);
        return result.getMappedResults();
    }
    @Override
    public List<Question> getAggregationQuestionById(String idQuestion) {

        ProjectionOperation projectionOperation =
                project()
                        .and("question").as("question")
                        .and("questionType").as("questionType")
                        .and("duration").as("duration")
                        .and("points").as("points")
                        .and("level").as("level")
                        .and("answers").as("answers");

        Aggregation aggregation = newAggregation(
                // filter question by id question
                match(
                        Criteria.where("_id").is(idQuestion)
                ),

                // get all answers ID
                l -> new Document("$lookup",
                        new Document("from", mongoTemplate.getCollectionName(Answers.class))
                                .append("let", new Document("answersId",
                                        new Document("$map",
                                                new Document("input", "$answersID")
                                                        .append("in",
                                                                new Document("$toObjectId", "$$this")
                                                        )
                                        )
                                ))
                                .append("pipeline",
                                        Arrays.asList(new Document("$match",
                                                new Document("$expr",
                                                        new Document("$in", Arrays.asList("$_id", "$$answersId"))))
                                        ))
                                .append("as", "answers")),

                projectionOperation
        );


        AggregationResults<Question> result = mongoTemplate.aggregate(
                aggregation, "Questions", Question.class);
        return result.getMappedResults();
    }
}
