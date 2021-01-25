package com.examstack.management.controller.page.admin;

import com.examstack.common.domain.question.Question;
import com.examstack.management.service.ParseWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class ParseWordController {
	
	@Autowired
	private ParseWordService parseWordService;

	@RequestMapping(value = "/admin/exam/parseword", method = RequestMethod.GET)
	public List<Question> parseWord(String path) throws Exception{

		List<Question> questionList = parseWordService.parseWord(path,null);

        return questionList;
	}
}
