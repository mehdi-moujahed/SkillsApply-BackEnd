package com.reactit.Skillsapply.controller;

import com.reactit.Skillsapply.dto.QuestionDTO;
import com.reactit.Skillsapply.model.*;
import com.reactit.Skillsapply.repository.AnswersRepository;
import com.reactit.Skillsapply.repository.ManagerRepository;
import com.reactit.Skillsapply.repository.QuestionsRepository;
import com.reactit.Skillsapply.repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RequestMapping("/manager")
@Api( description="API For All Operations Concerning RH Managers")
@RestController
public class ManagerController {

    @Autowired
    ManagerRepository managerRepository;


    @Autowired
    UserRepository userRepository;

    @Autowired
    QuestionsRepository questionsRepository;

    @Autowired
    AnswersRepository answersRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    @ApiOperation(value = "List All Candidates")
//    @PreAuthorize("hasAuthority('ADMIN') or hasAnyAuthority('Manager')")
    @GetMapping("/getAllCandidates")
    public List<User> getAllCandidates() {
        return userRepository.findByRoles("USER");
    }


    @ApiOperation(value = "List All Candidates")
//    @PreAuthorize("hasAuthority('ADMIN') or hasAnyAuthority('Manager')")
    @GetMapping("/getAllQuestions")
    public ResponseEntity<Questions> getAllQuestions() {
//        questionsRepository.findAllAnswersByQuestion().forEach((n)-> System.out.println(n.getAnswer()));
        return new ResponseEntity(questionsRepository.findAllAnswersByQuestion(),HttpStatus.OK);
    }

    

    @ApiOperation(value = "Adding New Question")
//    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/addQuestion")
    public ResponseEntity<Void> addQuestion(@RequestBody QuestionDTO questions ) {

        ArrayList<String> answersID = new ArrayList<>();

        questions.getAnswers().forEach((n)-> {
            Answers answers = new Answers();
            answers.setAnswer(n.getAnswer());
            answers.setStatus(n.isStatus());
           Answers addedAnswer =  answersRepository.save(answers);
            answersID.add(addedAnswer.getId());
            System.out.println("answer :" + n.getAnswer()
                    + " Status : " + n.isStatus());
        });

        Questions questions1 = new Questions();
        questions1.setQuestion(questions.getQuestion());
        questions1.setAnswers(answersID);
        questionsRepository.save(questions1);

        return new ResponseEntity("Question Added Successfully !", HttpStatus.OK);

    }


    @ApiOperation(value = "Updating Question And Answers")
//    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping(value = "/updateQuestion/{id}")
    public ResponseEntity<Void> updateQuestionAndAnswers(@RequestBody QuestionDTO questions, @PathVariable("id") String id ) {


        if(questions.getAnswers()!= null) {
            if(!questions.getQuestion().equals(null)) {
                Optional<Questions> dbQuestions = questionsRepository.findById(id);
                if(dbQuestions.isPresent()) {
                    Questions questionToEdit = dbQuestions.get();
                    questionToEdit.setQuestion(questions.getQuestion());
                    questionsRepository.save(questionToEdit);
                } else
                    return new ResponseEntity("Question does not exists ",HttpStatus.NOT_FOUND);
            }
            questions.getAnswers().forEach((n)-> {
                Optional<Answers> dbAnswers = answersRepository.findById(n.getId());
                Answers answersToEdit = dbAnswers.get();
                answersToEdit.setAnswer(n.getAnswer());
                answersToEdit.setStatus(n.isStatus());
                answersRepository.save(answersToEdit);
            });
        } else
            return new ResponseEntity("Answers must not be null ! ",HttpStatus.BAD_REQUEST);

        return new ResponseEntity("Question And Answers are updated Successfully ",HttpStatus.OK);
    }



//    @ApiOperation(value = "Updating Question")
//    @PatchMapping(value = "/updateQuestion/{id}")
//    public ResponseEntity updateQuestion(@RequestBody Questions questions, @PathVariable String id) {
//        Optional<Questions> dbQuestions = questionsRepository.findById(id);
//        if (dbQuestions.isPresent()) {
//
////            dbQuestions.get().getAnswers().forEach((n) -> System.out.println(n));
//            dbQuestions.get().setQuestion(questions.getQuestion());
//            dbQuestions.get().setType(questions.getType());
//        } else
//        return new ResponseEntity("Question not found !",HttpStatus.NOT_FOUND);
//        return null;
//    }

    @ApiOperation(value = "Delete Question By ID")
//    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "/deleteQuestion/{id}")
    public ResponseEntity deleteQuestionById(@PathVariable String id) throws Exception {
        Optional<Questions> questions = questionsRepository.findById(id);
        if(questions.isPresent()) {
            questionsRepository.deleteById(id);
            HashMap<String, String> resp = new HashMap<>();
            return new ResponseEntity<>("Question Deleted Successfully ", HttpStatus.OK);
        } else
            return new ResponseEntity("Question Not Found !",HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "Adding new RH Manager")
//    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PostMapping(value = "/signup")
    public ResponseEntity<Void> addNewManager(@RequestBody Manager manager){
        try {
            if (managerRepository.findByEmail(manager.getEmail()) == null) {
                if (manager.getPassword().length() < 8 || manager.getPassword().length() > 16) {
                    return new ResponseEntity("Password must be between 8 and 16 characters",
                            HttpStatus.BAD_REQUEST);
                } else {
                    Pattern p = Pattern.compile("[^A-Za-z0-9]");
                    Matcher m = p.matcher(manager.getPassword());
                    boolean passwordLengthCheck = m.find();

                    Pattern pattern = Pattern.compile("^[0-9]{8}$");
                    Matcher matcher = pattern.matcher(manager.getPhoneNumber());
                    boolean phoneNumberLengthCheck = matcher.find();
                    System.out.println("Password Length : " + manager.getPassword().length());

                    if (passwordLengthCheck == false)
                        return new ResponseEntity("Password must contain at least one : Capital letter, " +
                                "Number and Special Characters", HttpStatus.BAD_REQUEST);

                    else if (phoneNumberLengthCheck == false)
                        return new ResponseEntity("phoneNumber must have 8 numbers ", HttpStatus.BAD_REQUEST);

                    else if (passwordLengthCheck && phoneNumberLengthCheck) {
                        manager.setPassword(passwordEncoder.encode(manager.getPassword()));
                        manager.setCreateAt(new Date());
                        manager.setRoles("MANAGER");
                        Manager managerAdded = managerRepository.save(manager);
                        return new ResponseEntity("RH Manager added successfully, Details :" + "\n"
                                + manager.getFirstName() + "\n"
                                + manager.getLastName() + "\n" + "Manager ID : "
                                + manager.getId() + "\n" + "Role : "
                                + manager.getRoles() + "\n" +"Company : "
                                + manager.getCompanyName(),
                                HttpStatus.OK);
                    }
                }
            } else {
                return new ResponseEntity("Email is Already in use !", HttpStatus.BAD_REQUEST);
            }

        } catch (ConstraintViolationException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return null;
    }

}
