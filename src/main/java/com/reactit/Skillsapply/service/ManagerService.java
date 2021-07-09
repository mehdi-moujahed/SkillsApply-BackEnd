package com.reactit.Skillsapply.service;


import com.reactit.Skillsapply.dto.ResultDTO.CorrectResultDTO;
import com.reactit.Skillsapply.dto.TestsDTO.Question;
import com.reactit.Skillsapply.dto.TestsDTO.Test;
import com.reactit.Skillsapply.dto.TestsDTO.TestManager;

import java.util.ArrayList;
import java.util.List;

public interface ManagerService {
    public List<TestManager> getAggregationTestById(String idTest);
    public List<Question> getAggregationQuestionById(String idQuestion);
    public CorrectResultDTO getAggregationResultQuestion(String idResult);
}
