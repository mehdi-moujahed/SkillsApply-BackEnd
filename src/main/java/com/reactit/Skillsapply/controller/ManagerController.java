package com.reactit.Skillsapply.controller;

import com.reactit.Skillsapply.dto.QuestionDTO;
import com.reactit.Skillsapply.dto.TestDTO;
import com.reactit.Skillsapply.dto.UpdatePassword;
import com.reactit.Skillsapply.model.*;
import com.reactit.Skillsapply.repository.*;
import com.reactit.Skillsapply.service.ManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RequestMapping("/manager")
@Api( description="API For All Operations Concerning RH Managers")
@RestController
@CrossOrigin
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
    TestsRepository testsRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ManagerService managerService;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${SkillsApply.app.websiteBaseUrl}")
    private String websiteBaseUrl;


    @ApiOperation(value = "List All Candidates")
//    @PreAuthorize("hasAuthority('ADMIN') or hasAnyAuthority('Manager')")
    @GetMapping("/getAllCandidates")
    public List<User> getAllCandidates() {
        return userRepository.findByRoles("USER");
    }


    @ApiOperation(value = "Get Candidate By Id")
//    @PreAuthorize("hasAuthority('ADMIN') or hasAnyAuthority('Manager')")
    @GetMapping("/getCandidate/{id}")
    public ResponseEntity<Map<String, Object>> getCandidateById(@PathVariable("id") String id) {
        Optional<User> user = userRepository.findByIdAndRoles(id,"USER");
        if(user.isPresent()) {
            return new ResponseEntity(user,HttpStatus.OK);
        } else
            return new ResponseEntity("Candidat Inexistant",HttpStatus.NOT_FOUND);
    }


    @ApiOperation(value = "List All Questions")
//    @PreAuthorize("hasAuthority('ADMIN') or hasAnyAuthority('Manager')")
    @GetMapping("/getAllQuestions")
    public ResponseEntity<Questions> getAllQuestions() {
//        questionsRepository.findAllAnswersByQuestion().forEach((n)-> System.out.println(n.getAnswer()));
        return new ResponseEntity(questionsRepository.findAllAnswersByQuestion(), HttpStatus.OK);
    }



    @ApiOperation(value = "Adding New Question")
//    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/addQuestion")
    public ResponseEntity<Void> addQuestion(@RequestBody QuestionDTO questions) {

        ArrayList<String> answersID = new ArrayList<>();

        questions.getAnswers().forEach((n) -> {
            Answers answers = new Answers();
            answers.setAnswer(n.getAnswer());
            answers.setStatus(n.isStatus());
            Answers addedAnswer = answersRepository.save(answers);
            answersID.add(addedAnswer.getId());
            System.out.println("answer :" + n.getAnswer()
                    + " Status : " + n.isStatus());
        });

        Questions questions1 = new Questions();
        questions1.setQuestion(questions.getQuestion());
        questions1.setAnswersID(answersID);
        questionsRepository.save(questions1);

        return new ResponseEntity("Question Added Successfully !", HttpStatus.OK);

    }


    @ApiOperation(value = "Adding new Test")
//    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PostMapping(value = "/addTest")
    public ResponseEntity<Map<String, Object>> addTest(@RequestBody TestDTO testDTO){
        Map<String, Object> response = new HashMap<>();

        Test test = new Test();
        ArrayList<String> questionsID = new ArrayList<>();
        AtomicReference<Float> testDuration= new AtomicReference<>((float) 0);

        AtomicReference<Float> nbrTotalTestLevel= new AtomicReference<>((float) 0);

        AtomicReference<Float> nbrTotalTestPoints= new AtomicReference<>((float) 0);

        testDTO.getQuestions().forEach((n)->{
            ArrayList<String> answersID = new ArrayList<>();
            n.getAnswers().forEach((answer)->{
                Answers answers = new Answers();
                answers.setAnswer(answer.getAnswer());
                answers.setStatus(answer.isStatus());
                Answers addedAnswer = answersRepository.save(answers);
                answersID.add(addedAnswer.getId());
                System.out.println("answer :" + answer.getAnswer()
                        + " Status : " + answer.isStatus());
            });
            Questions questions1 = new Questions();
            questions1.setQuestion(n.getQuestion());
            questions1.setAnswersID(answersID);
            questions1.setQuestionType(n.getQuestionType());
            questions1.setLevel(n.getLevel());
            questions1.setDuration(n.getDuration());
            questions1.setPoints(n.getPoints());
            testDuration.updateAndGet(v -> new Float((float) (v + n.getDuration())));
            Questions questionAdded = questionsRepository.save(questions1);
            questionsID.add(questionAdded.getId());

            nbrTotalTestLevel.updateAndGet(v -> new Float((float) (v + n.getLevel())));

            nbrTotalTestPoints.updateAndGet(v -> new Float((float) (v + n.getPoints())));

            test.setLevel((Math.round(( new Float(String.valueOf(nbrTotalTestLevel))/questionsID.size())/10)*10));

            System.out.println("question saved successfully"+test.getLevel());
        });
        test.setName(testDTO.getName());
        test.setDescription(testDTO.getDescription());
        test.setScore((float)nbrTotalTestPoints.get());
        test.setRate(testDTO.getRate());
        test.setQuestionsID(questionsID);
        test.setDuration((float)testDuration.get());
        test.setManagerID("60a79878a0784e4240d4a619");
        test.setCreatedAt(new Date());
        test.setPremiumPack(false);

        testsRepository.save(test);
        response.put("message","Test Added Succesfully");
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @ApiOperation(value = "List All Professional Available Tests")
    //    @PreAuthorize("hasAuthority('ADMIN') or hasAnyAuthority('MANAGER')")
    @GetMapping("/getProfessionalAvailableTests")
    public ResponseEntity<Map<String, Object>>getProfessionalAvailableTests(@RequestParam(defaultValue = "0") int page,
                                                                            @RequestParam(defaultValue = "4") int size,
                                                                            @RequestParam(defaultValue = "10") float testLevel
    ){
        try {
            List<Test> tests = new ArrayList<Test>();
            Pageable paging = PageRequest.of(page, size);
            Page<Test> pageTuts;

            pageTuts = testsRepository.findByPremiumPackIsAndLevel(true, testLevel,paging);

            tests = pageTuts.getContent();
            Map<String, Object> response = new HashMap<>();
            response.put("availableTests", tests);
            response.put("currentPage", pageTuts.getNumber());
            response.put("totalItems", pageTuts.getTotalElements());
            response.put("totalPages", pageTuts.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity(e,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @ApiOperation(value = "List All Recent & High Rated Available Tests")
    //    @PreAuthorize("hasAuthority('ADMIN') or hasAnyAuthority('MANAGER')")
    @GetMapping("/getRecentAvailableTests")
    public ResponseEntity<Map<String, Object>>getRecentAvailableTests(@RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "4") int size,
                                                                      @RequestParam boolean isRecent
                                                                   ){
        try {
            List<Test> tests = new ArrayList<Test>();
            Pageable paging = PageRequest.of(page, size);
            Page<Test> pageTuts;

            if(isRecent==true) {
                pageTuts = testsRepository.findByPremiumPackIsOrderByCreatedAtDescAllIgnoreCase(true, paging);
            }else {
                pageTuts = testsRepository.findByPremiumPackIsOrderByRateDescAllIgnoreCase(true, paging);
            }
            tests = pageTuts.getContent();
            Map<String, Object> response = new HashMap<>();
            response.put("availableTests", tests);
            response.put("currentPage", pageTuts.getNumber());
            response.put("totalItems", pageTuts.getTotalElements());
            response.put("totalPages", pageTuts.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity(e,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @ApiOperation(value = "List All Available Tests")
    //    @PreAuthorize("hasAuthority('ADMIN') or hasAnyAuthority('MANAGER')")
    @GetMapping("/getAvailableTests")
    public ResponseEntity<Map<String, Object>>getAvailableTests(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "3") int size,
                                                              @RequestParam(defaultValue = "") String testName,
                                                              @RequestParam(defaultValue = "10") int timeFilter,
                                                              @RequestParam(defaultValue = "10") float testLevel,
                                                              @RequestParam(defaultValue = "2020-01-01") String date1,
                                                              @RequestParam(defaultValue = "2121-01-26") String date2,
                                                              @RequestParam(defaultValue = "-1")float rate1,
                                                              @RequestParam(defaultValue = "6")float rate2
                                                         )
    {
        try {
            List<Test> tests = new ArrayList<Test>();
            Pageable paging = PageRequest.of(page, size);
            Page<Test> pageTuts;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

            Date date11 = formatter.parse(date1);
            Date date22 = formatter.parse(date2);

            if(timeFilter==10 ) {
                pageTuts = testsRepository.findByAndCreatedAtBetweenAndRateBetweenAndPremiumPackIsAndNameContainingAndLevelOrderByCreatedAtDescAllIgnoreCase(date11,date22,rate1,rate2,true,testName,testLevel,paging);
            } else {
                pageTuts = testsRepository.findByCreatedAtBetweenAndRateBetweenAndPremiumPackIsAndNameContainingAndLevelOrderByRateDescAllIgnoreCase(date11,date22,rate1,rate2,true,testName,testLevel,paging);
            }
            tests = pageTuts.getContent();
            Map<String, Object> response = new HashMap<>();
            response.put("availableTests", tests);
            response.put("currentPage", pageTuts.getNumber());
            response.put("totalItems", pageTuts.getTotalElements());
            response.put("totalPages", pageTuts.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity(e,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @ApiOperation(value = "List All Tests Created")
//    @PreAuthorize("hasAuthority('ADMIN') or hasAnyAuthority('MANAGER')")
    @GetMapping("/getCreatedTests/{id}")
    public ResponseEntity<Map<String, Object>>getCreatedTests(@PathVariable("id") String id,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "3") int size,
                                                              @RequestParam(defaultValue = "") String testName,
                                                              @RequestParam(defaultValue = "10") int timeFilter,
                                                              @RequestParam(defaultValue = "10") float testLevel,
                                                              @RequestParam(defaultValue = "2020-01-01") String date1,
                                                              @RequestParam(defaultValue = "2121-01-26") String date2,
                                                              @RequestParam(defaultValue = "0")float rate1,
                                                              @RequestParam(defaultValue = "6")float rate2
                                                              ) {
        try {
            List<Test> tests = new ArrayList<Test>();
            Pageable paging = PageRequest.of(page, size);
            Page<Test> pageTuts;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

            Date date11 = formatter.parse(date1);
            Date date22 = formatter.parse(date2);

            if(timeFilter==10 ) {
                 pageTuts = testsRepository.findByCreatedAtBetweenAndRateBetweenAndManagerIDAndNameContainingAndLevelOrderByCreatedAtDescAllIgnoreCase(date11,date22,rate1,rate2,id,testName,testLevel,paging);
            } else {
                 pageTuts = testsRepository.findByCreatedAtBetweenAndRateBetweenAndManagerIDAndNameContainingAndLevelOrderByRateDescAllIgnoreCase(date11,date22,rate1,rate2,id,testName,testLevel,paging);
            }
            tests = pageTuts.getContent();
            Map<String, Object> response = new HashMap<>();
            response.put("testsCreated", tests);
            response.put("currentPage", pageTuts.getNumber());
            response.put("totalItems", pageTuts.getTotalElements());
            response.put("totalPages", pageTuts.getTotalPages());
            response.put("allTestsCreated",testsRepository.findByManagerID(id).stream().count());
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity(e,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @ApiOperation(value = "Updating Question And Answers")
//    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping(value = "/updateQuestion/{id}")
    public ResponseEntity<Void> updateQuestionAndAnswers(@RequestBody QuestionDTO questions, @PathVariable("id") String id) {

        if (questions.getAnswers() != null) {
            if (!questions.getQuestion().equals(null)) {
                Optional<Questions> dbQuestions = questionsRepository.findById(id);
                if (dbQuestions.isPresent()) {
                    Questions questionToEdit = dbQuestions.get();
                    questionToEdit.setQuestion(questions.getQuestion());
                    questionsRepository.save(questionToEdit);
                } else
                    return new ResponseEntity("Question does not exists ", HttpStatus.NOT_FOUND);
            }
            questions.getAnswers().forEach((n) -> {
                Optional<Answers> dbAnswers = answersRepository.findById(n.getId());
                Answers answersToEdit = dbAnswers.get();
                answersToEdit.setAnswer(n.getAnswer());
                answersToEdit.setStatus(n.isStatus());
                answersRepository.save(answersToEdit);
            });
        } else
            return new ResponseEntity("Answers must not be null ! ", HttpStatus.BAD_REQUEST);

        return new ResponseEntity("Question And Answers are updated Successfully ", HttpStatus.OK);
    }




    @ApiOperation(value = "Delete Question By ID")
//    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "/deleteQuestion/{id}")
    public ResponseEntity deleteQuestionById(@PathVariable String id) throws Exception {
        Optional<Questions> questions = questionsRepository.findById(id);
        if (questions.isPresent()) {
            questionsRepository.deleteById(id);
            HashMap<String, String> resp = new HashMap<>();
            return new ResponseEntity<>("Question Deleted Successfully ", HttpStatus.OK);
        } else
            return new ResponseEntity("Question Not Found !", HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "Delete Test By ID")
//    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "/deleteTest/{id}")
    public ResponseEntity deleteTestById(@PathVariable String id) throws Exception {
        Optional<Test> test = testsRepository.findById(id);
        HashMap<String, String> response = new HashMap<>();
        if (test.isPresent()) {
            testsRepository.deleteById(id);
            response.put("test name",test.get().getName());
            response.put("test id",test.get().getId());
            return new ResponseEntity<>(response,  HttpStatus.OK);
        } else
            return new ResponseEntity("Question Not Found !", HttpStatus.NOT_FOUND);
    }



    @ApiOperation(value = "Get Test By ID")
//    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/getTestManager/{id}")
    public ResponseEntity<Map<String, Object>> getTestByIdManager(@PathVariable String id) throws Exception {

       List<com.reactit.Skillsapply.dto.TestsDTO.TestManager> testAggregation
               = managerService.getAggregationTestById(id);

        return new ResponseEntity(testAggregation, HttpStatus.OK);

    }

    @ApiOperation(value = "Get Question By ID")
//    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/getQuestion/{id}")
    public ResponseEntity<Map<String, Object>> getQuestionById(@PathVariable String id) throws Exception {

        List<com.reactit.Skillsapply.dto.TestsDTO.Question> testAggregation
                = managerService.getAggregationQuestionById(id);

        return new ResponseEntity(testAggregation, HttpStatus.OK);

    }

    @ApiOperation(value = "Get Question By ID")
//    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/getResults/{id}")
    public ResponseEntity<Map<String, Object>> getResultById(@PathVariable String id) throws Exception {

        List<com.reactit.Skillsapply.dto.TestsDTO.Question> testAggregation
                = managerService.getAggregationResultQuestion(id);

        return new ResponseEntity(testAggregation, HttpStatus.OK);

    }

    @ApiOperation(value = "Get Test By ID")
//    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/getTestExam/{id}")
    public ResponseEntity<Map<String, Object>> getTestById(@PathVariable String id) throws Exception {
        Map<String, Object> response = new HashMap<>();
        Optional<Test> test = testsRepository.findById(id);
        if(test.isPresent()) {
            return new ResponseEntity(test,HttpStatus.OK);
        } else
        {
            response.put("message","Test Inexistant !");
            return new ResponseEntity(response, HttpStatus.NO_CONTENT);
        }

    }

    @ApiOperation(value = "Adding new RH Manager")
//    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PostMapping(value = "/signup")
    public ResponseEntity<Map<String, Object>> addNewManager(@RequestBody Manager manager)throws MessagingException,IOException {
        Map<String, Object> response = new HashMap<>();
        try {
            if (managerRepository.findByEmail(manager.getEmail()) == null) {
                if (manager.getPassword().length() < 8 || manager.getPassword().length() > 16) {
                    response.put("message", "Password must be between 8 and 16 characters");
                    return new ResponseEntity(response, HttpStatus.BAD_REQUEST);

                } else {
                    Pattern p = Pattern.compile("[^A-Za-z0-9]");
                    Matcher m = p.matcher(manager.getPassword());
                    boolean passwordLengthCheck = m.find();

                    Pattern pattern = Pattern.compile("^[0-9]{8}$");
                    Matcher matcher = pattern.matcher(manager.getPhoneNumber());
                    boolean phoneNumberLengthCheck = matcher.find();
                    System.out.println("Password Length : " + manager.getPassword().length());

                    if (passwordLengthCheck == false) {
                        response.put("message", "Password must contain at least one : Capital letter, Number and Special Characters");
                        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
                    } else if (phoneNumberLengthCheck == false) {
                        response.put("message","phoneNumber must have 8 numbers ");
                        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
                    } else if (passwordLengthCheck && phoneNumberLengthCheck) {
                        manager.setPassword(passwordEncoder.encode(manager.getPassword()));
                        manager.setCreatedAt(new Date());
                        manager.setRoles("MANAGER");
                        sendConrfirmAccount(manager);
                        response.put("message", "Un email vous a été envoyé pour confirmer votre inscription");
                        return new ResponseEntity(response, HttpStatus.OK);

                    } else {
                        response.put("message","Email is Already in use !");
                        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
                    }
                }
            } else {
                response.put("message","Email is Already in use !");
                return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
            }

        } catch (ConstraintViolationException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public void sendMail(String email,String subject, String message)throws MessagingException,IOException{

        MimeMessage msg = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(msg, true);

        helper.setTo(email);

        helper.setSubject(subject);

        helper.setText(message,true);

        helper.addInline("myLogo", new ClassPathResource("static/logo.png"));

        javaMailSender.send(msg) ;
    }


    @ApiOperation(value = "Sending Mail for passing test")
    @PostMapping(value = "/sendTestMail/{email}")
    public ResponseEntity<Map<String, Object>> sendPassingTestMail(@PathVariable("email") String email,
                                                                   @RequestParam String testID )throws MessagingException,IOException {
        Map<String, Object> response = new HashMap<>();
        Optional<Test> test = testsRepository.findById(testID);
        if(test.isPresent()) {
            User user = userRepository.findByEmailAndRoles(email,"USER");
            String confirmationUrl = websiteBaseUrl+ "/passingTest?testID="+test.get().getId()+"&candidateID="+user.getId();
            String mailMsg="<h1>Invitation pour le passage du test : "+test.get().getName()+" </h1> " +
                    "<p> Salut <b>"+user.getFirstName()+ "</b>, Veuillez cliquer sur le lien ci-dessous pour commencer votre test</p> " +
                    "<a href='"+confirmationUrl+"'>Commencer le test</a>"+
                    "<br> <p>Skills Apply team support</p>" +
                    " <img src='cid:myLogo'>";
            sendMail(email,"Invitation passage test",mailMsg);
            response.put("message","email envoyé avec succés");
            return new ResponseEntity(response,HttpStatus.OK);
        } else {
            response.put("message","Test Inexistant !");
            return new ResponseEntity(response,HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = " Token Verification ")
    @PostMapping(value = "/verifToken/{token}")
    public ResponseEntity<Map<String, Object>> tokenVerification(@PathVariable("token") String token ) {
        Map<String, Object> response = new HashMap<>();
        Optional<Manager> dbManager= managerRepository.findByToken(token);
        if(dbManager.isPresent()) {
            Manager manager = dbManager.get();
            Date createdAtToken = manager.getCreatedAtToken();
            DateTime dateTime = new DateTime( createdAtToken );
            DateTime yesterday = DateTime.now().minusDays(1);
            boolean isBeforeYesterday = dateTime.isBefore( yesterday );
            if (!isBeforeYesterday) {
                response.put("tokenTime",true);
                response.put("status",true);
                return new ResponseEntity(response,HttpStatus.OK);
            } else {
                response.put("tokenTime",false);
                response.put("status",true);
                return new ResponseEntity(response,HttpStatus.OK);
            }
        } else {
            response.put("tokenTime",false);
            response.put("status",false);
            return new ResponseEntity(response,HttpStatus.OK);
        }
    }

    @ApiOperation(value = "Register Confirmation")
//    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PostMapping(value = "/confirmResetPassword/{token}")
    public ResponseEntity<Map<String, Object>> confirmResetPassword(@PathVariable("token") String token, @RequestBody UpdatePassword  updatePassword ) {
        Map<String, Object> response = new HashMap<>();
        Optional<Manager> dbManager= managerRepository.findByToken(token);
        if(dbManager.isPresent()) {
            Manager manager = dbManager.get();
            Date createdAtToken = manager.getCreatedAtToken();
            DateTime dateTime = new DateTime( createdAtToken );
            DateTime yesterday = DateTime.now().minusDays(1);
            boolean isBeforeYesterday = dateTime.isBefore( yesterday );
            if(!isBeforeYesterday){
                if(updatePassword.getNewPassword().equals(updatePassword.getConfirmNewPassword())){
                    manager.setPassword(passwordEncoder.encode(updatePassword.getConfirmNewPassword()));
                    manager.setCreatedAtToken(null);
                    manager.setToken(null);
                    managerRepository.save(manager);
                    response.put("message","Votre mot de passe est réinitialisé avec succés");
                    return new ResponseEntity(response,HttpStatus.OK);
                } else {
                    response.put("message","Les deux mot de passe ne sont pas identiques !");
                    return new ResponseEntity(response,HttpStatus.BAD_REQUEST);
                }
            } else {
                manager.setCreatedAtToken(null);
                manager.setToken(null);
                managerRepository.save(manager);
                response.put("message","la durée de l'email a dépassé les 24h !");
                return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
            }
        } else
        {
            response.put("message","Invalid token !");
            return new ResponseEntity(response,HttpStatus.FORBIDDEN);
        }
    }

    @ApiOperation(value="Reset Password")
    @PostMapping(value = "/resetPassword/{email}")
    @CrossOrigin
    public ResponseEntity<Map<String, Object>> resetPassword(@PathVariable String email) throws MessagingException,IOException{
        Map<String, Object> response = new HashMap<>();
        Manager manager = managerRepository.findByEmail(email);
        if(manager!= null){
            sendResetPassword(manager);
            response.put("message","Un email vous a été envoyé");
            return new ResponseEntity(response,HttpStatus.OK);
        } else {
            response.put("message","Compte Introuvable !");
            return new ResponseEntity(response,HttpStatus.BAD_REQUEST);
        }
    }



    @CrossOrigin
    public void sendResetPassword(Manager manager)throws MessagingException,IOException{
        String token = UUID.randomUUID().toString();
        manager.setToken(token);
        manager.setCreatedAtToken(new Date());
        String confirmationUrl = websiteBaseUrl+ "/confirmResetPassword/" + token;
        Manager managerAdded = managerRepository.save(manager);
        String mailMsg="<h1>Réinitialisation mot de passe</h1> " +
                "<p>Veuillez cliquer sur le lien ci-dessous pour rénitialiser votre mot de passe de votre compte <b>SkillsApply</b></p> " +
                "<a href='"+confirmationUrl+"'>Réinitialiser mot de passe</a>"+
                "<br> <p>Skills Apply team support</p>" +
                " <img src='cid:myLogo'>";
        sendMail(manager.getEmail(),"Réinitialisation mot de passe",mailMsg);
    }

    public void sendConrfirmAccount(Manager manager)throws MessagingException,IOException{
        String token = UUID.randomUUID().toString();
        manager.setToken(token);
        manager.setCreatedAtToken(new Date());
        String confirmationUrl = websiteBaseUrl+ "/regitrationConfirm/" + token;
        Manager managerAdded = managerRepository.save(manager);
        String mailMsg="<h1>Activation du compte</h1> " +
                "<p>Veuillez cliquer sur le lien ci-dessous pour activer votre compte <b>SkillsApply</b></p> " +
                "<a href='"+confirmationUrl+"'>Activer mon compte</a>"+
                "<br> <p>Skills Apply team support</p>" +
                " <img src='cid:myLogo'>";
        sendMail(manager.getEmail(),"Activation Compte SkillsApply",mailMsg);
    }


    @ApiOperation(value = "Resend Verification Mail")
//    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @GetMapping(value = "/resendverificationmail/{token}")
    public ResponseEntity<Map<String, Object>> resendMailVerification(@PathVariable("token") String token) throws MessagingException, IOException {
        Map<String, Object> response = new HashMap<>();

        Optional<Manager> dbManager = managerRepository.findByToken(token);
        Manager manager = dbManager.get();
        if(dbManager.isPresent()){
            sendConrfirmAccount(manager);
            response.put("message","Email Renvoyé avec succées !");
            return new ResponseEntity(response,HttpStatus.OK);
        } else {
            response.put("message","Compte Introuvable !");
            return new ResponseEntity(response,HttpStatus.BAD_REQUEST);
        }
    }



    @ApiOperation(value = "Register Confirmation")
//    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @GetMapping(value = "/confirmregister/{token}")
    public ResponseEntity<Map<String, Object>> confirmRegister(@PathVariable("token") String token ) {

        Map<String, Object> response = new HashMap<>();
        Optional<Manager> dbManager= managerRepository.findByToken(token);
        Manager manager = dbManager.get();
        Date createdAtToken = manager.getCreatedAtToken();
        DateTime dateTime = new DateTime( createdAtToken );
        DateTime yesterday = DateTime.now().minusDays(1);
        boolean isBeforeYesterday = dateTime.isBefore( yesterday );
        System.out.println("date : "+isBeforeYesterday);
        System.out.println("date now  : "+DateTime.now());
        System.out.println("date token creation : "+dateTime);

        if(dbManager.isPresent()) {
            if(!isBeforeYesterday){
                manager.setEmailVerified(true);
                manager.setCreatedAtToken(null);
                managerRepository.save(manager);
                response.put("message","Votre email est vérifié avec succés !");
                return new ResponseEntity(response,HttpStatus.OK);
            } else {
                manager.setEmailVerified(false);
                response.put("message","la durée de l'email a dépassé les 24h !");
                return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
            }
        } else
        {
            response.put("message","Invalid token !");
            return new ResponseEntity(response,HttpStatus.FORBIDDEN);
        }
    }
}
