package com.examstack.management.controller.page.admin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;

import com.examstack.common.domain.exam.Message;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.examstack.common.domain.exam.Exam;
import com.examstack.common.domain.exam.ExamHistory;
import com.examstack.common.domain.exam.ExamPaper;
import com.examstack.common.domain.question.QuestionQueryResult;
import com.examstack.common.domain.user.Group;
import com.examstack.common.util.Page;
import com.examstack.common.util.PagingUtil;
import com.examstack.common.util.QuestionAdapter;
import com.examstack.management.security.UserInfo;
import com.examstack.management.service.ExamPaperService;
import com.examstack.management.service.ExamService;
import com.examstack.management.service.UserService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Controller
public class ExamPageAdmin {

	@Autowired
	private UserService userService;
	@Autowired
	private ExamPaperService examPaperService;
	@Autowired
	private ExamService examService;
	/**
	 * 考试管理
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/admin/exam/exam-list", method = RequestMethod.GET)
	private String examListPage(Model model, HttpServletRequest request, @RequestParam(value="page",required=false,defaultValue="1") int page) {
		
		Page<Exam> pageModel = new Page<Exam>();
		pageModel.setPageNo(page);
		pageModel.setPageSize(8);
		List<Exam> examList = examService.getExamList(pageModel,1,2);
		String pageStr = PagingUtil.getPagelink(page, pageModel.getTotalPage(), "", "admin/exam/exam-list");

		model.addAttribute("examList", examList);
		model.addAttribute("pageStr", pageStr);
		return "exam-list";
	}
	
	/**
	 * 模拟考试列表
	 * @param model
	 * @param request
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/admin/exam/model-test-list", method = RequestMethod.GET)
	private String modelTestListPage(Model model, HttpServletRequest request, @RequestParam(value="page",required=false,defaultValue="1") int page) {
		
		Page<Exam> pageModel = new Page<Exam>();
		pageModel.setPageNo(page);
		pageModel.setPageSize(10);
		List<Exam> examList = examService.getExamList(pageModel,3);
		String pageStr = PagingUtil.getPageBtnlink(page,
				pageModel.getTotalPage());
		model.addAttribute("examList", examList);
		model.addAttribute("pageStr", pageStr);
		return "model-test-list";
	}

	/**
	 * 发布考试
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/admin/exam/exam-add", method = RequestMethod.GET)
	private String examAddPage(Model model, HttpServletRequest request) {
		
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
			    .getAuthentication()
			    .getPrincipal();
		List<Group> groupList = userService.getGroupListByUserId(userInfo.getUserid(), null);
		List<ExamPaper> examPaperList = examPaperService.getEnabledExamPaperList(userInfo.getUsername(), null);
		model.addAttribute("groupList", groupList);
		model.addAttribute("examPaperList", examPaperList);
		return "exam-add";
	}
	
	/**
	 * 发布考试
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/admin/exam/model-test-add", method = RequestMethod.GET)
	private String modelTestAddPage(Model model, HttpServletRequest request) {
		
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
			    .getAuthentication()
			    .getPrincipal();
		List<ExamPaper> examPaperList = examPaperService.getEnabledExamPaperList(userInfo.getUsername(), null);
		
		model.addAttribute("examPaperList", examPaperList);
		return "model-test-add";
	}

	/**
	 * 学员名单
	 * 
	 * @param model
	 * @param request
	 * @param examId
	 * @return
	 */
	@RequestMapping(value = "/admin/exam/exam-student-list/{examId}", method = RequestMethod.GET)
	private String examStudentListPage(Model model, HttpServletRequest request, @PathVariable int examId,@RequestParam(value="searchStr",required=false,defaultValue="") String searchStr,@RequestParam(value="order",required=false,defaultValue="") String order,@RequestParam(value="limit",required=false,defaultValue="0") int limit, @RequestParam(value="page",required=false,defaultValue="1") int page) {
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
			    .getAuthentication()
			    .getPrincipal();
		Page<ExamHistory> pageModel = new Page<ExamHistory>();
		pageModel.setPageNo(page);
		pageModel.setPageSize(10);
		if("".equals(searchStr))
			searchStr = null;
		if("".equals(order) || (!"desc".equals(order) && !"asc".equals(order)))
			order = null;
		List<ExamHistory> histList = examService.getUserExamHistListByExamId(examId, searchStr, order, limit, pageModel);
		String pageStr = PagingUtil.getPageBtnlink(page,
				pageModel.getTotalPage());
		List<Group> groupList = userService.getGroupListByUserId(userInfo.getUserid(), null);
		model.addAttribute("groupList", groupList);
		model.addAttribute("histList", histList);
		model.addAttribute("pageStr", pageStr);
		model.addAttribute("examId", examId);
		model.addAttribute("limit", limit);
		model.addAttribute("order", order);
		model.addAttribute("searchStr", searchStr);
		return "user-exam-list";
	}
	
	/**
	 * 学员试卷
	 * @param model
	 * @param request
	 * @param histId
	 * @return
	 */
	@RequestMapping(value = "/admin/exam/student-answer-sheet/{histId}", method = RequestMethod.GET)
	private String studentAnswerSheetPage(Model model, HttpServletRequest request, @PathVariable int histId) {
		
		ExamHistory history = examService.getUserExamHistListByHistId(histId);
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
	 * 人工阅卷
	 * @param model
	 * @param request
	 * @param examhistoryId
	 * @return
	 */
	@RequestMapping(value = "/admin/exam/mark-exampaper/{examhistoryId}", method = RequestMethod.GET)
	private String markExampaperPage(Model model, HttpServletRequest request, @PathVariable int examhistoryId) {
		
		ExamHistory history = examService.getUserExamHistListByHistId(examhistoryId);
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
		return "exampaper-mark";
	}

	/**
	 * 导出成绩
	 * @param examId
	 * @return
	 */
	@RequestMapping(value = "admin/exam/export-{examId}", method = RequestMethod.GET)
	public @ResponseBody
	Message examExport(@PathVariable("examId") int examId){
		Message msg = new Message();

		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
				.getAuthentication()
				.getPrincipal();

		List<ExamHistory> histList = examService.getUserExamHistListByExamId(examId, null, null, 0, null);

		// 创建Excel的工作书册 Workbook,对应到一个excel文档
		HSSFWorkbook wb = new HSSFWorkbook();
		// 创建Excel的工作sheet,对应到一个excel文档的tab
		HSSFSheet sheet = wb.createSheet("sheet1");

		// 标题行
		HSSFRow row = sheet.createRow(0);
		Object [] title = {"学号","姓名","准考证号","科目","成绩","联系电话"};
		writeRowData(row, title);

		String examPaperName = "";
		for (int i = 0; i < histList.size(); i++){
			ExamHistory examHistory = histList.get(i);
			examPaperName = examHistory.getExamPaperName();

			// 每行输出的内容
			Object [] data = {
					examHistory.getUserName(),
					examHistory.getTrueName(),
					examHistory.getSeriNo(),
					examHistory.getExamPaperName(),
					examHistory.getPointGet(),
					examHistory.getPhoneNum()
			};

			HSSFRow dataRow = sheet.createRow(i+1);
			writeRowData(dataRow, data);
		}

		String dateStr = new SimpleDateFormat("yyyyMMddhh24mmss").format(new Date());
		String path = System.getProperty("catalina.base")
				+ ",webapps,Management,files,tmp," + userInfo.getUsername() + "," + dateStr;
		path = path.replace(",", File.separator);
		File f = new File(path);
		if(!f.exists())
			f.mkdirs();

		FileOutputStream out = null;
		try {
			String fullPath = path + File.separatorChar + examPaperName + "成绩.xls";
			out = new FileOutputStream(fullPath);
			wb.write(out);
			out.close();
			msg.setMessageInfo(fullPath.substring(fullPath.indexOf("Management")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			msg.setResult("FAILURE");
		} catch (IOException e) {
			e.printStackTrace();
			msg.setResult("FAILURE");
		}
		return msg;
	}

	private void writeRowData(HSSFRow ssfRow, Object[] itemArray){
		if (ssfRow != null){
			for (int i = 0; i < itemArray.length; i++){
				// 创建一个Excel的单元格
				HSSFCell cell = ssfRow.createCell(i);
				if (itemArray[i] != null){
					cell.setCellValue(itemArray[i].toString());
				}else{
					cell.setCellValue("");
				}
			}
		}
	}
	
}
