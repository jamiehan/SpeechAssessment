package com.examstack.management.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.examstack.common.domain.question.*;
import org.apache.ibatis.annotations.Param;

import com.examstack.common.util.Page;

/**
 * @author Ocelot
 * @date 2014年6月8日 下午5:52:44
 */
public interface QuestionService {

	public List<Question> getQuestionList(Page<Question> pageModel, QuestionFilter qf);

	public List<Field> getAllField(Page<Field> page);

	public List<KnowledgePoint> getKnowledgePointByFieldId(int FieldId, Page<KnowledgePoint> page);

	public Map<String, KnowledgePoint> getKnowledgePointMapByFieldId(int fieldId, Page<KnowledgePoint> page);

	public List<QuestionType> getQuestionTypeList();

	/**
	 * @param page
	 * @return
	 */
	public List<Tag> getTags(Page<Tag> page);

	/**
	 * 增加一个标签
	 *
	 * @param tag
	 */
	public void addTag(Tag tag);

	/**
	 * 添加试题，同时添加试题知识点对应关系
	 * 
	 * @param question
	 */
	public void addQuestion(Question question);

	/**
	 * 添加题库
	 * 
	 * @param field
	 */
	public void addField(Field field);

	/**
	 * 添加知识点
	 * 
	 * @param point
	 */
	public void addKnowledgePoint(KnowledgePoint point);

	/**
	 * 获取试题的tag，只包含公有tag和自己的私有tag
	 * 
	 * @param questionId
	 * @param userId
	 * @param page
	 * @return
	 */
	public List<QuestionTag> getQuestionTagByQuestionIdAndUserId(int questionId, int userId, Page<QuestionTag> page);

	/**
	 * 给题目打标签
	 * 
	 * @param questionId
	 * @param userId
	 */
	public void addQuestionTag(int questionId, int userId, List<QuestionTag> questionTagList);

	/**
	 * 重载，整合了tag的功能
	 * 
//	 * @see com.extr.service.QuestionService
	 * #updateQuestionPoint(Question
	 *      question)
	 * @param question
	 * @param userId
	 * @param questionTagList
	 */
	public void updateQuestionPoint(Question question, int userId, List<QuestionTag> questionTagList);

	/**
	 * 删除专业
	 * 
	 * @param idList
	 */
	public void deleteFieldByIdList(List<Integer> idList);

	/**
	 * 删除知识分类
	 * 
	 * @param idList
	 */
	public void deleteKnowledgePointByIdList(List<Integer> idList);

	/**
	 * 删除一个标签
	 * 
	 * @param idList
	 */
	public void deleteTagByIdList(List<Integer> idList);

	public Question getQuestionByQuestionId(int questionId);

	public List<QuestionQueryResult> getQuestionDescribeListByIdList(List<Integer> idList);

	public void deleteQuestionByQuestionId(int questionId);

	public void deleteQuestionTestByTestId(int testId);

	public HashMap<Integer, HashMap<Integer, List<QuestionStruts>>> getQuestionStrutsMap(List<Integer> idList);

	/**
	 * 导入试题
	 * 
	 * @param filePath
	 * @param username
	 * @param fieldId
	 */
	public void uploadQuestions(String filePath, String username, int fieldId);

	/**
	 * 导入试题
	 *
//	 * @param filePath
	 * @param username
	 * @param fieldId
//	 * @param pointList2
	 * @param question2
	 *  //String filePath,List<Integer> pointList2//Question question2
	 */
	public void uploadQuestions2(Question question2, String username, int fieldId);

	/**
	 * 批量修改试卷答案
	 *
	 //
	 * @param question2
	 * @param username
	 * @param testId
	 * @param question2
	 *  //String filePath,List<Integer> pointList2//Question question2
	 */
	public void uploadQuestionsAnswer(Question question2, String username, int testId);
	
	/**
	 * 根据fieldId,pointId,typeId分组统计试题数量
	 * @param fieldId
	 * @return
	 */
	public Map<Integer,Map<Integer,QuestionStatistic>> getTypeQuestionStaticByFieldId(int fieldId);
	
	/**
	 * 获取知识点字典
	 * @param fieldId
	 * @return
	 */
	public Map<Integer,String> getKnowledgePointMap(int fieldId);
	
	/**
	 * 获取试题类型字典
	 * @return
	 */
	public Map<Integer,String> getQuestionTypeMap();
	
	/**
	 * 更新试题
	 * @param question
	 * @param questionTagList
	 */
	public void updateQuestion(Question question, List<QuestionTag> questionTagList);

	public void updateQuestionTest(QuestionTest question);
	
	/**
	 * 获取试题信息（详细）
	 * @param questionId
	 */
	public Question getQuestionDetail(int questionId, int userId);
	
	/**
	 * 获取知识点统计信息
	 * @param fieldId
	 * @return
	 */
	public List<PointStatistic> getPointCount(int fieldId, Page<PointStatistic> page);

	/**
	 * 获取导入试题标题列表
	 * @param
	 * @return
	 */
	public List<QuestionTest> getQuestionTestList(int start, int end);

	public List<QuestionTest> getQuestionTestPageList(Page<Question> pageModel, QuestionFilter qf);


	/**
	 * 获取某一试题标题记录
	 * @param id
	 * @return
	 */
	public QuestionTest getQuestionTestById(@Param("id") int id);

}
