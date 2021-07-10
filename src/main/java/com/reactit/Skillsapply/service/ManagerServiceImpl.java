package com.reactit.Skillsapply.service;

import com.reactit.Skillsapply.dto.ResultDTO.CorrectAllResultDTO;
import com.reactit.Skillsapply.dto.ResultDTO.CorrectQuestionDTO;
import com.reactit.Skillsapply.dto.ResultDTO.CorrectResultDTO;
import com.reactit.Skillsapply.dto.TestsDTO.Question;
import com.reactit.Skillsapply.dto.TestsDTO.TestManager;
import com.reactit.Skillsapply.model.Answers;
import com.reactit.Skillsapply.model.Questions;
import com.reactit.Skillsapply.model.Result;
import com.reactit.Skillsapply.model.User;
import com.reactit.Skillsapply.repository.ResultRepository;
import com.reactit.Skillsapply.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class ManagerServiceImpl implements ManagerService{

    private final MongoTemplate mongoTemplate;

    @Autowired
    ResultRepository resultRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    public ManagerServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<TestManager> getAggregationTestById(String idTest) {

        AggregationResults<TestManager> result =  getAggregationManager(idTest);
        return result.getMappedResults();
    }

    @Override
    public ArrayList<CorrectAllResultDTO> getAggregationAllTests(String idManager) {
        CorrectAllResultDTO correctResult = new CorrectAllResultDTO();
        ArrayList<Result> result = (ArrayList<Result>) resultRepository.findByManagerId(idManager);

        ArrayList<CorrectAllResultDTO> listCorrectRsult = new ArrayList<>();
            result.forEach((resultItem) -> {
        AggregationResults<TestManager> Test = getAggregationManager(resultItem.getTestId());
                System.out.println(Test.getMappedResults());
        TestManager testManager =  Test.getMappedResults().get(0);
        AtomicReference<Float> score = new AtomicReference<>((float) 0);

        testManager.getQuestions().forEach((question) -> {
            ArrayList<String> correctAnswers= new ArrayList<>();
            question.getAnswers().forEach((answer) -> {
                if(answer.isStatus()){
                    correctAnswers.add(answer.getId());
                }
            });
            resultItem.getResult().forEach((questionResult) -> {
                if(questionResult.getQuestionId().equals(question.getId())){
                    CorrectQuestionDTO correctQuestionDTO = new CorrectQuestionDTO();
                    ArrayList<CorrectQuestionDTO> correctQ = new ArrayList<>();

                    if(question.getQuestionType().equals("10")){

                        if(questionResult.getAnswersId().size() == correctAnswers.size() && correctAnswers.containsAll(questionResult.getAnswersId())){
                            correctQuestionDTO.setQuestion(question);
                            correctQuestionDTO.setCorrect(true);
                            correctQ.addAll(correctResult.getCorrectQuestion());
                            correctQ.add(correctQuestionDTO);
                            correctResult.setCorrectQuestion(correctQ);
                            score.updateAndGet(v -> new Float((float) (v + question.getPoints())));
                        }else{
                            correctQuestionDTO.setQuestion(question);
                            correctQuestionDTO.setCorrect(false);
                            correctQ.addAll(correctResult.getCorrectQuestion());
                            correctQ.add(correctQuestionDTO);
                            correctResult.setCorrectQuestion(correctQ);
                        }
                    }
                    if(question.getQuestionType().equals("20")){
                        if(questionResult.getAnswer().toLowerCase(Locale.ROOT).trim().equals(question.getAnswers().get(0).getAnswer().toLowerCase(Locale.ROOT).trim())){
                            correctQuestionDTO.setQuestion(question);
                            correctQuestionDTO.setCorrect(true);
                            correctQ.addAll(correctResult.getCorrectQuestion());
                            correctQ.add(correctQuestionDTO);
                            correctResult.setCorrectQuestion(correctQ);
                            score.updateAndGet(v -> new Float((float) (v + question.getPoints())));
                        }else{
                            correctQuestionDTO.setQuestion(question);
                            correctQuestionDTO.setCorrect(false);
                            correctQ.addAll(correctResult.getCorrectQuestion());
                            correctQ.add(correctQuestionDTO);
                            correctResult.setCorrectQuestion(correctQ);
                        }
                    }

                }
            });
        });

        correctResult.setIdTest(testManager.getIdTest());
        correctResult.setDescription(testManager.getDescription());
        correctResult.setLevel(testManager.getLevel());
        correctResult.setDuration(testManager.getDuration());
        correctResult.setScore(testManager.getScore());
        correctResult.setName(testManager.getName());
        correctResult.setDurationSpent(resultItem.getDuration());
        correctResult.setScoreCollected(Float.parseFloat(String.valueOf(score)));
        correctResult.setScorePercentage((int) ((Float.parseFloat(String.valueOf(score))/testManager.getScore())*100));
        correctResult.setUser(userRepository.findById(resultItem.getUserId()).get());
                listCorrectRsult.add(correctResult);
            });
        return listCorrectRsult;
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

    public CorrectResultDTO getAggregationResultQuestion(String idResult) {
        CorrectResultDTO correctResult = new CorrectResultDTO();
       Optional<Result> result = resultRepository.findById(idResult);

        AggregationResults<TestManager> Test = getAggregationManager(result.get().getTestId());

        TestManager testManager =  Test.getMappedResults().get(0);
        AtomicReference<Float> score = new AtomicReference<>((float) 0);

        testManager.getQuestions().forEach((question) -> {
            ArrayList<String> correctAnswers= new ArrayList<>();
            question.getAnswers().forEach((answer) -> {
                if(answer.isStatus()){
                    correctAnswers.add(answer.getId());
                }
           });
            result.get().getResult().forEach((questionResult) -> {
                if(questionResult.getQuestionId().equals(question.getId())){
                    CorrectQuestionDTO correctQuestionDTO = new CorrectQuestionDTO();
                    ArrayList<CorrectQuestionDTO> correctQ = new ArrayList<>();

                    if(question.getQuestionType().equals("10")){

                        if(questionResult.getAnswersId().size() == correctAnswers.size() && correctAnswers.containsAll(questionResult.getAnswersId())){
                            correctQuestionDTO.setQuestion(question);
                            correctQuestionDTO.setCorrect(true);
                            correctQ.addAll(correctResult.getCorrectQuestion());
                            correctQ.add(correctQuestionDTO);
                            correctResult.setCorrectQuestion(correctQ);
                            score.updateAndGet(v -> new Float((float) (v + question.getPoints())));
                        }else{
                            correctQuestionDTO.setQuestion(question);
                            correctQuestionDTO.setCorrect(false);
                            correctQ.addAll(correctResult.getCorrectQuestion());
                            correctQ.add(correctQuestionDTO);
                            correctResult.setCorrectQuestion(correctQ);
                        }
                    }
                    if(question.getQuestionType().equals("20")){
                        if(questionResult.getAnswer().toLowerCase(Locale.ROOT).trim().equals(question.getAnswers().get(0).getAnswer().toLowerCase(Locale.ROOT).trim())){
                            correctQuestionDTO.setQuestion(question);
                            correctQuestionDTO.setCorrect(true);
                            correctQ.addAll(correctResult.getCorrectQuestion());
                            correctQ.add(correctQuestionDTO);
                            correctResult.setCorrectQuestion(correctQ);
                            score.updateAndGet(v -> new Float((float) (v + question.getPoints())));
                        }else{
                            correctQuestionDTO.setQuestion(question);
                            correctQuestionDTO.setCorrect(false);
                            correctQ.addAll(correctResult.getCorrectQuestion());
                            correctQ.add(correctQuestionDTO);
                            correctResult.setCorrectQuestion(correctQ);
                        }
                    }

                }
            });
        });

        correctResult.setIdTest(testManager.getIdTest());
        correctResult.setDescription(testManager.getDescription());
        correctResult.setLevel(testManager.getLevel());
        correctResult.setDuration(testManager.getDuration());
        correctResult.setScore(testManager.getScore());
        correctResult.setName(testManager.getName());
        correctResult.setDurationSpent(result.get().getDuration());
        correctResult.setScoreCollected(Float.parseFloat(String.valueOf(score)));
        correctResult.setScorepercentage((int) ((Float.parseFloat(String.valueOf(score))/testManager.getScore())*100));

        return correctResult;

    }

    public AggregationResults<TestManager> getAggregationManager(String idResult) {

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
                        Criteria.where("_id").is(idResult)
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
                l -> new Document("$addFields", new Document("answersId" ,new Document("$reduce",
                        new Document("input", "$Test")
                                .append("initialValue",Arrays.asList())
                                .append("in",
                                        new Document("$concatArrays", Arrays.asList("$$value", "$$this.answersID"))
                                )))
                ),

                l -> new Document("$lookup",
                        new Document("from", mongoTemplate.getCollectionName(Answers.class))
                                .append("let", new Document("idQuestion",
                                        new Document("$map",
                                                new Document("input", "$answersId")
                                                        .append("in",
                                                                new Document("$toObjectId", "$$this")
                                                        )
                                        )
                                ))
                                .append("pipeline",
                                        Arrays.asList(new Document("$match",
                                                new Document("$expr",
                                                        new Document("$in", Arrays.asList("$_id", "$$idQuestion"))))
                                        ))
                                .append("as", "answers")),

                l -> new Document("$addFields", new Document("Test" ,new Document("$map",
                        new Document("input", "$Test")
                                .append("in",
                                        new Document("$mergeObjects", Arrays.asList("$$this",
                                                new Document("answers",new Document("$map", new Document(
                                                        new Document("input", "$$this.answersID")
                                                                .append("in",new Document("$mergeObjects",
                                                                        Arrays.asList(new Document("$arrayElemAt",
                                                                                Arrays.asList("$answers",new Document("$indexOfArray",
                                                                                                Arrays.asList("$answersId","$$this")
                                                                                        )
                                                                                )
                                                                        ))

                                                                ))
                                                )

                                                ))
                                        ))
                                )))
                ),
                projectionOperation
        );


        return mongoTemplate.aggregate(
                aggregation, "Tests", TestManager.class);
    }
}
