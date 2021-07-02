package com.reactit.Skillsapply.service;


import com.reactit.Skillsapply.dto.TestsDTO.Question;
import com.reactit.Skillsapply.dto.TestsDTO.Test;

import java.util.ArrayList;
import java.util.List;

public interface ManagerService {
    public List<Test> getAggregationTestById(String idTest);
    public List<Question> getAggregationQuestionById(String idQuestion);
}
