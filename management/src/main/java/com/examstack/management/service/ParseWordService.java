package com.examstack.management.service;

import com.examstack.common.domain.question.KnowledgePoint;
import com.examstack.common.domain.question.Question;

import java.util.List;
import java.util.Map;

public interface ParseWordService {

	public List<Question> parseWord(String filePath, Map<String, KnowledgePoint> pointMap) throws Exception;
	public List<Question> parseWord(String filePath, Map<String, KnowledgePoint> pointMap, List<Integer> pointList2) throws Exception;
	public List<Question> parseWord(Question question2, Map<String, KnowledgePoint> pointMap) throws Exception;
	public List<Question> parseWordAnswer(Question question2, int testId) throws Exception;
	
}
