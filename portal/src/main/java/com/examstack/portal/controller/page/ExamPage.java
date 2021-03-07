package com.examstack.portal.controller.page;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.examstack.common.domain.user.User;
import com.examstack.common.util.*;
import com.examstack.portal.service.*;
import org.codehaus.jackson.map.util.JSONPObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.examstack.common.domain.exam.AnswerSheet;
import com.examstack.common.domain.exam.AnswerSheetItem;
import com.examstack.common.domain.exam.Exam;
import com.examstack.common.domain.exam.ExamHistory;
import com.examstack.common.domain.exam.ExamPaper;
import com.examstack.common.domain.exam.UserQuestionHistory;
import com.examstack.common.domain.question.KnowledgePoint;
import com.examstack.common.domain.question.QuestionQueryResult;
import com.examstack.common.domain.question.QuestionStatistic;
import com.examstack.common.domain.question.QuestionType;
import com.examstack.portal.security.UserInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Controller
public class ExamPage {

	@Autowired
	private ExamService examService;
	@Autowired
	private ExamPaperService examPaperService;
	@Autowired
	private QuestionHistoryService questionHistoryService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/exam-list", method = RequestMethod.GET)
	public String examListPage(Model model, HttpServletRequest request) {

		
		int userId = 0;
		if (SecurityContextHolder.getContext().getAuthentication() != null && !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().endsWith("anonymousUser")){
			
			UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			userId = userInfo.getUserid();
		}
		
		Page<Exam> page = new Page<Exam>();
		page.setPageSize(10);
		page.setPageNo(1);
		List<Exam> examListToApply = examService.getExamListToApply(userId, page);
		List<Exam> examListToStart = examService.getExamListToStart(userId, null, 1, 2);
		List<Exam> modelTestToStart = examService.getExamList(null, 3);
		model.addAttribute("examListToApply", examListToApply);
		model.addAttribute("examListToStart", examListToStart);
		model.addAttribute("modelTestToStart", modelTestToStart);
		model.addAttribute("userId", userId);
		return "exam";
	}

	@RequestMapping(value = "/exam-list-api", method = RequestMethod.GET, produces = "text/html;charset=utf-8")
	@ResponseBody
	public String examListPageApi(HttpServletRequest request, HttpServletResponse httpServletResponse,@RequestParam(value = "token") String token) {

//		String token = inUser.getToken();

		if( !TokenUtil.verify(token) || TokenUtil.getUserNameFromToken(token) == null){
			return new Gson().toJson(new OutputObject(ReturnCode.FAIL,"验证失败，您无权访问",token));
		}

		String userName = TokenUtil.getUserNameFromToken(token);
		User user = userService.getUserByName(userName);

		Map resultMap = new HashMap();

		int userId = 0;
		/*if (SecurityContextHolder.getContext().getAuthentication() != null && !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().endsWith("anonymousUser")){

			UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			userId = userInfo.getUserid();
		}*/
		if ( user != null ) {
			userId = user.getUserId();
		}

		Page<Exam> page = new Page<Exam>();
		page.setPageSize(10);
		page.setPageNo(1);
		List<Exam> examListToApply = examService.getExamListToApply(userId, page);
		List<Exam> examListToStart = examService.getExamListToStart(userId, null, 1, 2);
		List<Exam> modelTestToStart = examService.getExamList(null, 3);
		resultMap.put("examListToApply", examListToApply);
		resultMap.put("examListToStart", examListToStart);
		resultMap.put("modelTestToStart", modelTestToStart);
		resultMap.put("userId", userId);

		return new Gson().toJson(new OutputObject(ReturnCode.SUCCESS,"成功",
				resultMap));
	}



	/**
	 * 开始考试（公有考试和私有考试）
	 *
	 * @param model
	 * @param request
	 * @param examId
	 * @return
	 */
	@RequestMapping(value = "/student/exam-start/{examId}", method = RequestMethod.GET)
	public String examStartPage(Model model, HttpServletRequest request, @PathVariable("examId") int examId) {

		//TO-DO:学员开始考试时，将开始时间传到消息队列，用户更新用户开始考试的时间。如果数据库中时间不为空，则不更新
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String strUrl = "http://" + request.getServerName() // 服务器地址
				+ ":" + request.getServerPort() + "/";

		int duration = 0;
		Exam exam = examService.getExamById(examId);

		if (exam.getApproved() != 1 || exam.getExpTime().before(new Date()) || exam.getExamType() == 3) {
			model.addAttribute("errorMsg", "考试未审核或当前时间不能考试或考试类型错误");
			return "error";
		}

		ExamHistory examHistory = examService
				.getUserExamHistByUserIdAndExamId(userInfo.getUserid(), examId, 0, 1, 2, 3);
		Date startTime = examHistory.getStartTime() == null ? new Date() : examHistory.getStartTime();
		switch (examHistory.getApproved()) {
			case 0:
				model.addAttribute("errorMsg", "考试未审核");
				return "error";
			case 2:
				model.addAttribute("errorMsg", "已交卷，不能重复考试");
				return "error";
			case 3:
				model.addAttribute("errorMsg", "已阅卷，不能重复考试");
				return "error";
		}
		ExamPaper examPaper = examPaperService.getExamPaperById(examHistory.getExamPaperId());
		String content = examPaper.getContent();

		Gson gson = new Gson();
		duration = examPaper.getDuration();

		List<QuestionQueryResult> questionList = gson.fromJson(content, new TypeToken<List<QuestionQueryResult>>() {
		}.getType());

		StringBuilder sb = new StringBuilder();
		for (QuestionQueryResult question : questionList) {
			QuestionAdapter adapter = new QuestionAdapter(question, strUrl);
			sb.append(adapter.getUserExamPaper());
		}
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmss");

		model.addAttribute("startTime", startTime);
		model.addAttribute("examHistoryId", examHistory.getHistId());
		model.addAttribute("examId", examHistory.getExamId());
		model.addAttribute("examPaperId", examHistory.getExamPaperId());
		model.addAttribute("duration", duration * 60);
		model.addAttribute("htmlStr", sb.toString());

		userInfo.setHistId(0);
		return "examing";
	}

	/**
	 * 开始考试（公有考试和私有考试）
	 *
	 * @param model
	 * @param request
	 * @param examId
	 * @return
	 */
	@RequestMapping(value = "/exam-start-api", method = RequestMethod.GET,produces = "text/html;charset=utf-8")
	@ResponseBody
	public String examStartPageApi(Model model, HttpServletRequest request, @RequestParam("examId") int examId, @RequestParam("token") String token) {

		//TO-DO:学员开始考试时，将开始时间传到消息队列，用户更新用户开始考试的时间。如果数据库中时间不为空，则不更新
//		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if( !TokenUtil.verify(token) || TokenUtil.getUserNameFromToken(token) == null){
			return new Gson().toJson(new OutputObject(ReturnCode.FAIL,"验证失败，您无权访问",token));
		}

		String userName = TokenUtil.getUserNameFromToken(token);
		User user = userService.getUserByName(userName);
		int userId = 0;

		if ( user != null ) {
			userId = user.getUserId();
		}

		String strUrl = "http://" + request.getServerName() // 服务器地址
				+ ":" + request.getServerPort() + "/";

		int duration = 0;
		Exam exam = examService.getExamById(examId);

		if (exam.getApproved() != 1 || exam.getExpTime().before(new Date()) || exam.getExamType() == 3) {
//			model.addAttribute("errorMsg", "考试未审核或当前时间不能考试或考试类型错误");
			return new Gson().toJson(new OutputObject(ReturnCode.FAIL,"考试未审核或当前时间不能考试或考试类型错误",user));
//			return "error";
		}

		ExamHistory examHistory = examService
				.getUserExamHistByUserIdAndExamId(userId, examId, 0, 1, 2, 3);
		Date startTime = examHistory.getStartTime() == null ? new Date() : examHistory.getStartTime();
		switch (examHistory.getApproved()) {
			case 0:
//				model.addAttribute("errorMsg", "考试未审核");
//				return "error";
				return new Gson().toJson(new OutputObject(ReturnCode.FAIL,"考试未审核",user));
			case 2:
//				model.addAttribute("errorMsg", "已交卷，不能重复考试");
//				return "error";
				return new Gson().toJson(new OutputObject(ReturnCode.FAIL,"已交卷，不能重复考试",user));
			case 3:
//				model.addAttribute("errorMsg", "已阅卷，不能重复考试");
//				return "error";
				return new Gson().toJson(new OutputObject(ReturnCode.FAIL,"已阅卷，不能重复考试",user));
		}
		ExamPaper examPaper = examPaperService.getExamPaperById(examHistory.getExamPaperId());
		String content = examPaper.getContent();

		Gson gson = new Gson();
		duration = examPaper.getDuration();

		List<QuestionQueryResult> questionList = gson.fromJson(content, new TypeToken<List<QuestionQueryResult>>() {
		}.getType());

//		StringBuilder sb = new StringBuilder();
//		for (QuestionQueryResult question : questionList) {
//			QuestionAdapter adapter = new QuestionAdapter(question, strUrl);
//			sb.append(adapter.getUserExamPaper());
//		}
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmss");
		Map<String,Object> hs =new HashMap<>();

//		model.addAttribute("startTime", startTime);
//		model.addAttribute("examHistoryId", examHistory.getHistId());
//		model.addAttribute("examId", examHistory.getExamId());
//		model.addAttribute("examPaperId", examHistory.getExamPaperId());
//		model.addAttribute("duration", duration * 60);
//		model.addAttribute("htmlStr", sb.toString());

		hs.put("startTime", startTime);
		hs.put("examHistoryId", examHistory.getHistId());
		hs.put("examId", examHistory.getExamId());
		hs.put("examPaperId", examHistory.getExamPaperId());
		hs.put("duration", duration * 60);
//		hs.put("htmlStr", sb.toString());
		hs.put("questionList",questionList);

//		userInfo.setHistId(0);
//		return "examing";
		return new Gson().toJson(new OutputObject(String.valueOf(HttpStatus.OK.value()),"成功",hs));
	}

	/**
	 * 模拟考试
	 * 
	 * @param model
	 * @param request
	 * @param examId
	 * @return
	 */
	@RequestMapping(value = "/student/model-test-start/{examId}", method = RequestMethod.GET)
	public String modelTestStartPage(Model model, HttpServletRequest request, @PathVariable("examId") int examId) {

		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String strUrl = "http://" + request.getServerName() // 服务器地址
				+ ":" + request.getServerPort() + "/";

		int duration = 0;
		Exam exam = examService.getExamById(examId);

		if (exam.getApproved() != 1 || exam.getExpTime().before(new Date()) || exam.getExamType() != 3) {
			model.addAttribute("errorMsg", "考试未审核或当前时间不能考试或考试类型错误");
			return "error";
		}

		ExamPaper examPaper = examPaperService.getExamPaperById(exam.getExamPaperId());
		ExamHistory examHistory = examService
				.getUserExamHistByUserIdAndExamId(userInfo.getUserid(), examId, 0, 1, 2, 3);
		int historyId = 0;
		if (examHistory == null) {
			//练习默认审核通过
			historyId = examService.addUserExamHist(userInfo.getUserid(), examId, examPaper.getId(),1);
		}else{
			historyId = examHistory.getHistId();
		}

		String content = examPaper.getContent();

		Gson gson = new Gson();
		duration = examPaper.getDuration();

		List<QuestionQueryResult> questionList = gson.fromJson(content, new TypeToken<List<QuestionQueryResult>>() {
		}.getType());

		StringBuilder sb = new StringBuilder();
		for (QuestionQueryResult question : questionList) {
			QuestionAdapter adapter = new QuestionAdapter(question, strUrl);
			sb.append(adapter.getUserExamPaper());
		}

		model.addAttribute("examHistoryId", historyId);
		model.addAttribute("examId", examId);
		model.addAttribute("examPaperId", examPaper.getId());
		model.addAttribute("duration", duration * 60);
		model.addAttribute("htmlStr", sb.toString());
		userInfo.setHistId(0);
		return "examing";
	}
	
	/**
	 * 学员试卷
	 * @param model
	 * @param request
	 * @param examhistoryId
	 * @return
	 */
	@RequestMapping(value = "/student/student-answer-sheet/{examId}", method = RequestMethod.GET)
	private String studentAnswerSheetPage(Model model, HttpServletRequest request, @PathVariable int examId) {
		
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ExamHistory history = examService.getUserExamHistByUserIdAndExamId(userInfo.getUserid(), examId, 2, 3);
		int examPaperId = history.getExamPaperId();
		
		String strUrl = "http://" + request.getServerName() // 服务器地址
				+ ":" + request.getServerPort() + "/";
		
		ExamPaper examPaper = examPaperService.getExamPaperById(examPaperId);
		StringBuilder sb = new StringBuilder();
		if(examPaper.getContent() != null && !examPaper.getContent().equals("")){
			Gson gson = new Gson();
			String content = examPaper.getContent();
			List<QuestionQueryResult> questionList = gson.fromJson(content, new TypeToken<List<QuestionQueryResult>>(){}.getType());
			
			for(QuestionQueryResult question : questionList){
				QuestionAdapter adapter = new QuestionAdapter(question,strUrl);
				sb.append(adapter.getStringFromXML());
			}
		}
		
		model.addAttribute("htmlStr", sb);
		model.addAttribute("exampaperid", examPaperId);
		model.addAttribute("examHistoryId", history.getHistId());
		model.addAttribute("exampapername", examPaper.getName());
		model.addAttribute("examId", history.getExamId());
		return "student-answer-sheet";
	}

	/**
	 * 学员试卷
	 * @param model
	 * @param request
	 * @param examhistoryId
	 * @return
	 */
	@RequestMapping(value = "/student-answer-sheet-api", method = RequestMethod.GET, produces = "text/html;charset=utf-8")
	@ResponseBody
	private String studentAnswerSheetPageApi(Model model, HttpServletRequest request, @RequestParam("examId") int examId, @RequestParam("token") String token) {

//		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if( !TokenUtil.verify(token) || TokenUtil.getUserNameFromToken(token) == null){
			return new Gson().toJson(new OutputObject(ReturnCode.FAIL,"验证失败，您无权访问",token));
		}

		String userName = TokenUtil.getUserNameFromToken(token);
		User user = userService.getUserByName(userName);

		int userId = 0;

		if ( user != null ) {
			userId = user.getUserId();
		}

//		ExamHistory history = examService.getUserExamHistByUserIdAndExamId(userId, examId, 2, 3);
		ExamHistory history = examService.getUserExamHistByUserIdAndExamId(userId, examId, 1);
		int examPaperId = history.getExamPaperId();

		ExamPaper examPaper = examPaperService.getExamPaperById(examPaperId);

//		StringBuilder sb = new StringBuilder();

		List<QuestionQueryResult> questionList = null;
		if(examPaper.getContent() != null && !examPaper.getContent().equals("")){
			Gson gson = new Gson();
			String content = examPaper.getContent();
			questionList = gson.fromJson(content, new TypeToken<List<QuestionQueryResult>>(){}.getType());

//			for(QuestionQueryResult question : questionList){
//				QuestionAdapter adapter = new QuestionAdapter(question,strUrl);
//				sb.append(adapter.getStringFromXML());
//			}
		}

		Map<String,Object> hs =new HashMap<>();

		hs.put("questionList",questionList);
		hs.put("answerSheet",history.getAnswerSheet());
		hs.put("exampaperId", examPaperId);
		hs.put("examHistoryId", history.getHistId());
		hs.put("exampaperName", examPaper.getName());
		hs.put("examId", history.getExamId());
		return new Gson().toJson(new OutputObject(String.valueOf(HttpStatus.OK.value()),"成功",hs));
	}
	
	@RequestMapping(value = "student/finish-exam/{examId}", method = RequestMethod.GET)
	public String examFinishedPage(Model model,@PathVariable("examId") int examId) {
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		ExamHistory history = examService.getUserExamHistByUserIdAndExamId(userInfo.getUserid(), examId, 2, 3);
		Gson gson = new Gson();
		List<QuestionQueryResult> questionList = gson.fromJson(history.getContent(), new TypeToken<List<QuestionQueryResult>>(){}.getType());
		
		List<Integer> idList = new ArrayList<Integer>();
		for (QuestionQueryResult q : questionList) {
			idList.add(q.getQuestionId());
		}
		
		AnswerSheet as = gson.fromJson(history.getAnswerSheet(), AnswerSheet.class);
		
		HashMap<Integer, AnswerSheetItem> hm = new HashMap<Integer,AnswerSheetItem>();
		for(AnswerSheetItem item : as.getAnswerSheetItems()){
			hm.put(item.getQuestionId(), item);
		}

		int total = questionList.size();
		int wrong = 0;
		int right = 0;

		HashMap<Integer, QuestionStatistic> reportResultMap = new HashMap<Integer, QuestionStatistic>();
		List<QuestionQueryResult> questionQueryList = questionService.getQuestionAnalysisListByIdList(idList);
		Map<Integer,KnowledgePoint> pointMap = questionService.getKnowledgePointByFieldId(null);
		HashMap<Integer, Boolean> answer = new HashMap<Integer, Boolean>();
		
		for(QuestionQueryResult result : questionQueryList){
			QuestionStatistic statistic = reportResultMap.get(result.getKnowledgePointId());
			if(statistic == null)
				statistic = new QuestionStatistic();
			statistic.setPointId(result.getKnowledgePointId());
			statistic.setPointName(pointMap.get(result.getKnowledgePointId()).getPointName());
			statistic.setAmount(statistic.getAmount() + 1);
			if(hm.get(result.getQuestionId()).isRight()){
				statistic.setRightAmount(statistic.getRightAmount() + 1);
				right ++;
				answer.put(result.getQuestionId(), true);
			}else{
				statistic.setWrongAmount(statistic.getWrongAmount() + 1);
				wrong ++;
				answer.put(result.getQuestionId(), false);
			}
			total ++;
			reportResultMap.put(result.getKnowledgePointId(), statistic);
		}

		model.addAttribute("total", total);
		model.addAttribute("wrong", wrong);
		model.addAttribute("right", right);
		model.addAttribute("reportResultMap", reportResultMap);
		model.addAttribute("create_time", history.getCreateTime());
		model.addAttribute("answer", answer);
		model.addAttribute("idList", idList);
		return "exam-finished";
	}

	@RequestMapping(value = "/finish-exam-api", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String examFinishedPageApi(Model model,@RequestBody Map<String,Object> param) {

		String token = (String)param.get("token");
		Integer examId = Integer.valueOf((String)param.get("examId"));

		AnswerSheet answerSheet = new Gson().fromJson(new Gson().toJson(param.get("answerSheet")), AnswerSheet.class);

		if( !TokenUtil.verify(token) || TokenUtil.getUserNameFromToken(token) == null){
			return new Gson().toJson(new OutputObject(ReturnCode.FAIL,"验证失败，您无权访问",token));
		}

		String userName = TokenUtil.getUserNameFromToken(token);
		User user = userService.getUserByName(userName);

		int userId = 0;

		if ( user != null ) {
			userId = user.getUserId();
		}

		examService.updateUserExamHist(answerSheet,new Gson().toJson(param.get("answerSheet")),3);

		ExamHistory history = examService.getUserExamHistByUserIdAndExamId(userId, examId, 2, 3);
		Gson gson = new Gson();
		List<QuestionQueryResult> questionList = gson.fromJson(history.getContent(), new TypeToken<List<QuestionQueryResult>>(){}.getType());

		List<Integer> questionIdList = new ArrayList<Integer>();
		for (QuestionQueryResult q : questionList) {
			questionIdList.add(q.getQuestionId());
		}

		AnswerSheet as = gson.fromJson(history.getAnswerSheet(), AnswerSheet.class);

		HashMap<Integer, AnswerSheetItem> hm = new HashMap<Integer,AnswerSheetItem>();
		for(AnswerSheetItem item : as.getAnswerSheetItems()){
			hm.put(item.getQuestionId(), item);
		}

		int totalNum = questionList.size();
		int wrongNum = 0;
		int rightNum = 0;

		HashMap<Integer, QuestionStatistic> reportResultMap = new HashMap<Integer, QuestionStatistic>();
		List<QuestionQueryResult> questionQueryList = questionService.getQuestionAnalysisListByIdList(questionIdList);
		Map<Integer,KnowledgePoint> pointMap = questionService.getKnowledgePointByFieldId(null);

		HashMap<Integer, Boolean> answer = new HashMap<Integer, Boolean>();

		for(QuestionQueryResult result : questionQueryList){
			QuestionStatistic statistic = reportResultMap.get(result.getKnowledgePointId());
			if(statistic == null)
				statistic = new QuestionStatistic();
			statistic.setPointId(result.getKnowledgePointId());
			statistic.setPointName(pointMap.get(result.getKnowledgePointId()).getPointName());
			statistic.setAmount(statistic.getAmount() + 1);
			if(hm.get(result.getQuestionId()).isRight()){
				statistic.setRightAmount(statistic.getRightAmount() + 1);
				rightNum ++;
				answer.put(result.getQuestionId(), true);
			}else{
				statistic.setWrongAmount(statistic.getWrongAmount() + 1);
				wrongNum ++;
				answer.put(result.getQuestionId(), false);
			}
			totalNum ++;
			reportResultMap.put(result.getKnowledgePointId(), statistic);
		}

		Map<String,Object> hs =new HashMap<>();

		hs.put("totalNum", totalNum);
		hs.put("wrongNum", wrongNum);
		hs.put("rightNum", rightNum);
		hs.put("reportResultMap", reportResultMap);
		hs.put("create_time", history.getCreateTime());
		hs.put("answer", answer);
		hs.put("questionIdList", questionIdList);
//		return "exam-finished";
		return new Gson().toJson(new OutputObject(String.valueOf(HttpStatus.OK.value()),"成功",hs));
	}
	
	@RequestMapping(value = "student/finished-submit", method = RequestMethod.GET)
	public String finishedSubmitPage(Model model){
		return "finished-submit";
	}

}
