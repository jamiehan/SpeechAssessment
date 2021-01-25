package com.examstack.management.service;

import com.examstack.common.domain.question.KnowledgePoint;
import com.examstack.common.domain.question.QuestionTest;
import com.examstack.management.persistence.QuestionMapper;
import com.examstack.management.security.UserInfo;
import com.google.gson.Gson;
import com.examstack.management.util.ParseWordUtil;
import com.examstack.common.domain.question.Question;
import com.examstack.common.domain.question.QuestionContent;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;

@Service
public class ParseWordServiceImpl implements ParseWordService {

//	@Autowired
//	private QuestionDao QuestionDao;
//
//	@Override
//	public List<Question> parseWord() throws Exception {
//		// TODO Auto-generated method stub
//		return null;
//	}

    @Autowired
    private QuestionMapper questionMapper;

//    private String filePath = "d:\\project\\考试系统\\加工中心操作工技师理论(A).doc";
//一级标题类型
    private int level1 = 0;
    private int level1_old = 0;
//二级标题数字编号
    private String level2Num = "0";
//解析内容标题
    private String title = "";
//题库标题
    private String  name=null;
//三级标题开始
    private int leverl3start=-1;
//二级标题以数字开头
    private boolean rs21 = false;
    //选择题的选项字母【A-D】是否有括号标识"（A）"
    private int iskh=-1;
////試卷名称列表最大值
//    private int testMaxId = 0;
////试卷内容一级标题名称字符串
//    private String level1Title = "";
////所属知识点类别
    private String pointIds = "";

    String answerStr = null;

    //进入问答题区域
    private boolean answer_begin = false;

int nn = 0;

    public List<Question> parseWordAnswer(Question question2, int testId)  {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();

            String filePath = question2.getFilePath();
            List<String> list = ParseWordUtil.getWordText(filePath);
            ;
            int level1 = 0;
            List<Question> questionList = new ArrayList<Question>();
            for (String val : list) {
                nn = nn + 1;
                Question question = new Question();
                question.setName("");
                question.setTest_id(testId);

                if (val != null || val.length() > 0) {
                    val = val.trim();
                }
                if ("".equals(val)) {
                    continue;
                }


                if (val.indexOf("、单项选择") != -1) {
                    level1 = 1;
                    System.out.println("一级标题 类型= " + level1 + " " + val);
                } else if (val.indexOf("、多项选择") != -1) {
                    level1 = 2;
                    System.out.println("一级标题 类型= " + level1 + " " + val);
                } else if (val.trim().indexOf("、判断题") != -1) {
                    level1 = 3;
                    System.out.println("一级标题 类型= " + level1 + " " + val);
                } else if (val.trim().indexOf("、填空题") != -1) {
                    level1 = 4;
                    System.out.println("一级标题 类型= " + level1 + " " + val);

                } else if (val.trim().indexOf("、简答题") != -1) {
                    level1 = 5;
                    System.out.println("一级标题 类型= " + level1 + " " + val);
                } else if (val.trim().indexOf("、论述题") != -1) {
                    level1 = 6;
                    System.out.println("一级标题 类型= " + level1 + " " + val);
                } else if (val.trim().indexOf("、分析题") != -1) {
                    level1 = 7;
                    System.out.println("一级标题 类型= " + level1 + " " + val);
                } else if (level1 > 0){
                    //判断是否已经进入解析内容区域
                    if (level1<4){
                        //判断是否以数字开头
                        String regEx2 = "[0-9]*";
                        Pattern pattern2 = Pattern.compile(regEx2);
                        Matcher matcher2 = pattern2.matcher(val.charAt(0) + "");
                        // 字符串是否与正则表达式相匹配
                        rs21 = matcher2.matches();
                        //判断第 1 个字符为数字
                        if (rs21) {
                            System.out.println(" 二级标题 === " + val);
                            matcher2 = pattern2.matcher(val.charAt(1) + "");
                            boolean rs22 = matcher2.matches();
                            //判断第 2 个字符为数字
                            if (rs22) {// 2位数字标题
                                if (val.length() > 3) {
                                    level2Num = val.substring(0, 2);
                                    title = val.substring(3);
                                    name = val.substring(3);
                                } else {
                                    continue;
                                }
                            } else {// 1位数字标题
                                if (val.length() > 2) {
                                    level2Num = val.substring(0, 1);
                                    title = val.substring(2);
                                    name = val.substring(2);
                                } else {
                                    continue;
                                }
                            }
                            question.setTest_num(Integer.valueOf(level2Num));
                            String answer = StringUtils.substring(val,val.indexOf("、")+1);
                            question.setAnswer(answer);
                            question.setAnalysis(val);

                            questionList.add(question);
                            answer_begin = false;
                        }//end  if (rs21)

                    }else  if (level1<=7 && level1>=4){
                           // 问答题

                        String regEx2 = "[0-9]*";
                        Pattern pattern2 = Pattern.compile(regEx2);
                        Matcher matcher2 = pattern2.matcher(val.charAt(0) + "");
                        // 字符串是否与正则表达式相匹配
                        rs21 = matcher2.matches();
                        //判断第 1 个字符为数字
                        if (rs21) {
                            if(answer_begin==true){
                                question.setAnswer(answerStr);
                                question.setTest_num(Integer.valueOf(level2Num));
                                questionList.add(question);
                                answer_begin = false;
                                answerStr = null;
                            }
                            System.out.println(" 二级标题 === " + val);
                            matcher2 = pattern2.matcher(val.charAt(1) + "");
                            boolean rs22 = matcher2.matches();
                            //判断第 2 个字符为数字

                            if (rs22) {// 2位数字标题
                                if (val.length() > 3) {
                                    level2Num = val.substring(0, 2);
                                    title = val.substring(3);
                                    name = val.substring(3);
                                } else {
                                    continue;
                                }
                            } else {// 1位数字标题
                                if (val.length() > 2) {
                                    level2Num = val.substring(0, 1);
                                    title = val.substring(2);
                                    name = val.substring(2);
                                } else {
                                    continue;
                                }
                            }

//                            String answer = StringUtils.substring(val,val.indexOf("、")+1);
//                            question.setAnswer(answer);
//                            question.setAnalysis(val);
//
//                            questionList.add(question);

                        }else if(level1_old!=level1 && answer_begin==true){//end  if (rs21)

                            question.setAnswer(answerStr);
                            question.setTest_num(Integer.valueOf(level2Num));
                            questionList.add(question);
                            answer_begin = false;
                            answerStr = null;
                        }

                        //判断是否以数字开头
                        if (val.trim().indexOf("答:") != -1 || val.trim().indexOf("答：") != -1) {
                            answer_begin = true;
                            answerStr = new String();
                            answerStr = "\r\n "+val;
                            System.out.println("问答开始 " + level1 + " " + val);

                        }else if(answer_begin==true){
                            answerStr = answerStr+"\r\n &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+val;
                            System.out.println("nn ="+nn);
                            if((Integer.valueOf(level2Num)==64) && (list.size() - 1 == nn)){
                                //TODO
                                //如果上面的等式成立就说明遍历到的user就是list的最后一个元素
                                question.setAnswer(answerStr);
                                question.setTest_num(Integer.valueOf(level2Num));
                                questionList.add(question);
                                answer_begin = false;
                                answerStr = null;
                            }
                        }




                    }//end if // 问答题
                    //一级标题改变与否
                    level1_old = level1;

                    }//end if (level1 > 0) { 解析区


                }//end for

                  System.out.println("questionList.size=====>" + questionList.size());


            for(Question question : questionList) {
                System.out.println("testid="+question.getTest_id()+" testnum=" +question.getTest_num()+" answer="+question.getAnswer());
                questionMapper.updateQuestionAnswer(question);

            }


            return questionList;



                } catch (Exception e) {

                    e.printStackTrace();
                }
        return null;
    }


    public List<Question> parseWord(Question question2, Map<String, KnowledgePoint> pointMap)  {

        try {
//            变量初始化

//試卷名称列表最大值
             int testMaxId = 0;
//试卷内容一级标题名称字符串
             String level1Title = "";
//所属知识点类别
             String pointIds = "";

            String filePath = question2.getFilePath();
            List<Integer> pointList = question2.getPointList();
            String fileName = question2.getName();


            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();

            LinkedHashMap<String, String> choiceList = new LinkedHashMap<String, String>();
            QuestionContent qContent = new QuestionContent();

            List<String> list = ParseWordUtil.getWordText(filePath);

            System.out.println("list size = " + list.size());

            List<Question> questionList = new ArrayList<Question>();

//            添加试卷标题
            QuestionTest questionTest = new QuestionTest();
                questionTest.setName(fileName);
                questionTest.setCreator(userInfo.getUserid());
                questionTest.setPrivatee(false);
                questionTest.setCreateTime(new Date());
            questionMapper.addQuestionTest(questionTest);
            testMaxId = questionMapper.getQuestionTestMaxId();

            for (Integer i:pointList) {
                pointIds = pointIds+" "+i;
            }

            for (String val : list) {

                if (val != null || val.length() > 0) {
                    val = val.trim();
                }
                if ( "".equals(val)) {
                    continue;
                }

                Question question = new Question();
                question.setName("");
                question.setQuestion_type_id(1);
                question.setAnswer("");
                question.setContent("");
                question.setCreator(userInfo.getUsername());
                question.setCreate_time(new Date());
                question.setIs_visible(true);
                question.setDuration(2);
                question.setExpose_times(2);
                question.setRight_times(3);
                question.setWrong_times(4);
                question.setDifficulty(5);

                ////知识点添加

                // 知识点为空，题库类型不为空时
                if(pointList == null && pointMap != null){
                    List<Integer> pointList2 = new ArrayList<Integer>();
                    KnowledgePoint kp = pointMap.entrySet().iterator().next().getValue();
                    pointList2.add(kp.getPointId());
                    question.setPointList(pointList2);
                }else if(pointList != null){
                    question.setPointList(pointList);
                }else {
                    System.out.println("页面选择知识列表不为空");
                }
//试题中设置 试卷标题ID
                question.setTest_id(testMaxId);

//1.解析一级标题
                /*
                *
                1	单选题
                2	多选题
                3	判断题
                4	填空题
                5	简答题
                6	论述题
                7	分析题
                *
                * */


                if (val.indexOf("、单项选择") != -1) {
                    level1 = 1;
                    level1Title = level1Title +" "+ val+"-0";
                    System.out.println("一级标题 类型= "+level1+ " " + val);
                } else if (val.indexOf("、多项选择") != -1) {
                    level1 = 2;
                    level1Title = level1Title +" | "+ val+"-"+level2Num;
                    System.out.println("一级标题 类型= "+level1+ " " + val);
                } else if (val.trim().indexOf("、判断题") != -1 ) {
                    level1 = 3;
                    level1Title = level1Title +" | "+ val+"-"+level2Num;
                    System.out.println("一级标题 类型= "+level1+ " " + val);
                }else if (val.trim().indexOf("、填空题") != -1 ) {
                    level1 = 4;
                    level1Title = level1Title +" | "+ val+"-"+level2Num;
                    System.out.println("一级标题 类型= "+level1+ " " + val);

                }  else if (val.trim().indexOf("、简答题") != -1) {
                    level1 = 5;
                    level1Title = level1Title +" | "+ val+"-"+level2Num;
                    System.out.println("一级标题 类型= "+level1+ " " + val);
                }else if (val.trim().indexOf("、论述题") != -1) {
                    level1 = 6;
                    level1Title = level1Title +" | "+ val+"-"+level2Num;
                    System.out.println("一级标题 类型= "+level1+ " " + val);
                }else if (val.trim().indexOf("、分析题") != -1) {
                    level1 = 7;
                    level1Title = level1Title +" | "+ val+"-"+level2Num;
                    System.out.println("一级标题 类型= "+level1+ " " + val);
                }else if (val.trim().indexOf("、名词解释") != -1) {
                    level1 = 8;
                    level1Title = level1Title +" | "+ val+"-"+level2Num;
                    System.out.println("一级标题 类型= "+level1+ " " + val);
                }else if (val.trim().indexOf("、综合") != -1) {
                    level1 = 9;
                    level1Title = level1Title +" | "+ val+"-"+level2Num;
                    System.out.println("一级标题 类型= "+level1+ " " + val);
                }
                else if (level1>0){//判断是否已经进入解析内容区域

//2.解析二级标题

                    //特换行符号的处理
                    String tmpvalTitle = null;
                    String tmpvalContent = null;
//                    if(("40").equals(level2Num)){
                        int nr= val.indexOf((char)11);
                        if(nr!=-1){
                            System.out.println("换行符位置》》》===========indexof （char)11==>  "+nr);
                            System.out.println("换行符前半段char1==>"+val.substring(0,nr));
                            System.out.println("换行符后半段char2==>"+val.substring(nr));
                            tmpvalTitle = StringUtils.substring(val,0,nr);
                            tmpvalContent = StringUtils.substring(val,nr).trim();
                        }
//                    }


                    //判断是否以数字开头
                    String regEx2 = "[0-9]*";
                    Pattern pattern2 = Pattern.compile(regEx2);

                    Matcher matcher2 = pattern2.matcher(val.charAt(0) + "");
                    // 字符串是否与正则表达式相匹配
                     rs21 = matcher2.matches();
                    //判断第 1 个字符为数字
                    if (rs21) {
                        leverl3start=0;//3级标题逻辑

                        //如果选择题和答案直接有回车符 标题部分单独处理
                        if(tmpvalTitle!=null && (level1==1 || level1==2)){
                            val = tmpvalTitle;
                        }
                        System.out.println(" 二级标题 === " + val);
                        matcher2 = pattern2.matcher(val.charAt(1) + "");
                        boolean rs22 = matcher2.matches();
                        //判断第 2 个字符为数字
                        if (rs22){// 2位数字标题
                            if(val.length() > 3 ){
                                level2Num = val.substring(0, 2);
                                title = val.substring(3);
                                name = val.substring(3);
                            } else {
                                continue;
                            }

                        }else{// 1位数字标题
                            if(val.length() > 2) {
                                level2Num = val.substring(0, 1);
                                title = val.substring(2);
                                name =  val.substring(2);
                            }else{
                                continue;
                            }
                        }

                        question.setTest_id(testMaxId);
                        question.setTest_num(Integer.valueOf(level2Num));

                        Gson gson = new Gson();
                        String content = null;
                        System.out.println("--- leval -----" + level1);
                        question.setQuestion_type_id(level1);


                        if(level1==5 || level1==4 || level1==8){
                            Integer level2Num01 = Integer.valueOf(level2Num)+1;
                            Integer level2Num02 = Integer.valueOf(level2Num)+2;
                            Integer level2Num03 = Integer.valueOf(level2Num)+3;
                            Integer level2Num04 = Integer.valueOf(level2Num)+4;
                            String tempVal_level1_5 = val;
                            int index_level2Num01 = tempVal_level1_5.indexOf(String.valueOf(level2Num01));
                            int index_level2Num02 = tempVal_level1_5.indexOf(String.valueOf(level2Num02));
                            int index_level2Num03 = tempVal_level1_5.indexOf(String.valueOf(level2Num03));
                            int index_level2Num04 = tempVal_level1_5.indexOf(String.valueOf(level2Num04));
                            int level1_5_indexBegin = -1;
                            int level1_5_indexEnd = -1;
                            int level1_5_len = val.length();
                            String subStr_level1_5 = null;
                            if(index_level2Num01!=-1){
                                level1_5_indexBegin = 0;
                                level1_5_indexEnd  =    index_level2Num01;
                                 subStr_level1_5 = StringUtils.substring(tempVal_level1_5,level1_5_indexBegin+3,level1_5_indexEnd);
                                 name = subStr_level1_5;
                                 title = subStr_level1_5;
                                question.setTest_num(Integer.valueOf(level2Num));
                                question.setName(name);
                                qContent.setTitle(title);
                                content = gson.toJson(qContent);
                                question.setContent(content);
                                System.out.println(question);
                                questionList.add(question);

                                //////////////////////////////////////////////////////

                                level1_5_indexBegin = index_level2Num01;
                                level1_5_indexEnd  =    level1_5_len;
                                if(index_level2Num02!=-1){
                                    level1_5_indexEnd  =    index_level2Num02;
                                }
                                subStr_level1_5 = StringUtils.substring(tempVal_level1_5,level1_5_indexBegin+3,level1_5_indexEnd);
                                name = subStr_level1_5;
                                title = subStr_level1_5;
                                Question question02 = new Question();
                                BeanUtils.copyProperties(question,question02);
                                level2Num=String.valueOf(level2Num01);
                                question02.setTest_num(level2Num01);
                                question02.setName(name);
                                qContent.setTitle(title);
                                content = gson.toJson(qContent);
                                question02.setContent(content);

                                System.out.println(question02);
                                questionList.add(question02);


                                if(index_level2Num02!=-1){
                                    level1_5_indexBegin = index_level2Num02;
                                    level1_5_indexEnd  =    level1_5_len;
                                    if(index_level2Num03!=-1){
                                        level1_5_indexEnd  =    index_level2Num03;
                                    }
                                    subStr_level1_5 = StringUtils.substring(tempVal_level1_5,level1_5_indexBegin+3,level1_5_indexEnd);
                                    name = subStr_level1_5;
                                    title = subStr_level1_5;
                                    Question question03 = new Question();
                                    BeanUtils.copyProperties(question,question03);
                                    level2Num=String.valueOf(level2Num02);
                                    question03.setTest_num(level2Num02);
                                    question03.setName(name);
                                    qContent.setTitle(title);
                                    content = gson.toJson(qContent);
                                    question03.setContent(content);

                                    System.out.println(question03);
                                    questionList.add(question03);

                                    if(index_level2Num03!=-1){
                                        level1_5_indexBegin = index_level2Num03;
                                        level1_5_indexEnd  =    level1_5_len;
                                        if(index_level2Num04!=-1){
                                            level1_5_indexEnd  =    index_level2Num04;
                                        }
                                        subStr_level1_5 = StringUtils.substring(tempVal_level1_5,level1_5_indexBegin+3,level1_5_indexEnd);
                                        name = subStr_level1_5;
                                        title = subStr_level1_5;
                                        Question question04 = new Question();
                                        BeanUtils.copyProperties(question,question04);
                                        level2Num=String.valueOf(level2Num03);
                                        question04.setTest_num(level2Num03);
                                        question04.setName(name);
                                        qContent.setTitle(title);
                                        content = gson.toJson(qContent);
                                        question04.setContent(content);

                                        System.out.println(question04);
                                        questionList.add(question04);
                                    }
                                }

                                continue;
                            }
                        }



                        switch (level1) {
                            case 1:
                                question.setQuestion_type_id(level1);
                                question.setName(name);
                                break;
                            case 2:
                                question.setQuestion_type_id(level1);
                                question.setName(name);
                                break;
                            case 3:
                                question.setQuestion_type_id(level1);
                                question.setName(name);
                                qContent.setTitle(title);
                                content = gson.toJson(qContent);
                                question.setContent(content);
                                System.out.println(question);
                                questionList.add(question);

                                break;
                            case 4:
                                question.setQuestion_type_id(level1);
                                question.setName(name);
                                qContent.setTitle(title);
                                content = gson.toJson(qContent);
                                question.setContent(content);

                                System.out.println(question);
                                questionList.add(question);
                                break;
                            case 5:
                                question.setQuestion_type_id(level1);
                                question.setName(name);
                                qContent.setTitle(title);
                                content = gson.toJson(qContent);
                                question.setContent(content);

                                System.out.println(question);
                                questionList.add(question);
                                break;
                            case 6:
                                question.setQuestion_type_id(level1);
                                question.setName(name);
                                qContent.setTitle(title);
                                content = gson.toJson(qContent);
                                question.setContent(content);

                                System.out.println(question);
                                questionList.add(question);
                                break;
                            case 7:
                            case 8:
                            case 9:
                                question.setQuestion_type_id(level1);
                                question.setName(name);
                                qContent.setTitle(title);
                                content = gson.toJson(qContent);
                                question.setContent(content);

                                System.out.println(question);
                                questionList.add(question);
                                break;
                            default:
                                break;

                        }

                    }//end  二级标题

//                    else
                        if((level1==1 || level1==2) && (rs21 == false || tmpvalContent != null)){
 //3.解析三级标题 1.单项选择题 2.多项选择题 3.非数字开头
                        //如果选择题和答案直接有回车符 答案部分单独处理
                        if(tmpvalContent != null && rs21 == true  && (level1==1 || level1==2)){
                            val = tmpvalContent;
                        }
                        //三级标题 设置类型ID
                        question.setQuestion_type_id(level1);
                        if (level1>0 && val.length() > 2){
                            //解析A B C D选项
                            String regEx3 = "[A-D]";
                            Pattern pattern3 = Pattern.compile(regEx3);

                            Matcher matcher3 = null;
                            if(val.indexOf("（")!=-1){//选项字母有括号
                                iskh = 1;
                                matcher3 = pattern3.matcher(val.charAt(1) + "");
                            }else{//选项字母无括号
                                iskh = 0;
                                matcher3 = pattern3.matcher(val.charAt(0) + "");
                            }
                            // 字符串是否与正则表达式相匹配
                            boolean rs31 = matcher3.matches();
                            if (rs31) {
                                //                    System.out.println(rs2 + "--三级标题[A-D]----" + level2Num);
                                //                    System.out.println(val);

                                //	                    		String string="a   b  a  a ";
                                //	                    		for(String a:val.split("\\s+")){
                                //	                    			System.out.println(a);
                                //	                    		}

                                /*********************************************/
                                if (iskh == 1) {// 3级标题有括号
                                    int len = val.length();
                                    int indexBegin = -1;
                                    int indexEnd = -1;
                                    int indexA = val.indexOf("（A）");
                                    int indexB = val.indexOf("（B）");
                                    int indexC = val.indexOf("（C）");
                                    int indexD = val.indexOf("（D）");
                                    int indexE = val.indexOf("（E）");
                                    int indexF = val.indexOf("（F）");
                                    int indexG = val.indexOf("（G）");
                                    String tempVal = val;
                                    String subStr = null;
                                    String charA = val.charAt(1) + "";
                                    if (val!=null &&  indexA!= -1 && "A".equals(charA)) {
                                        leverl3start=1;
                                        indexBegin = indexA;
                                        indexEnd = len;
                                        if (indexB != -1)
                                            indexEnd = indexB;
                                        subStr = StringUtils.substring(tempVal,indexBegin+3,indexEnd);
                                        System.out.println(subStr);
                                        choiceList.put("A", subStr);
                                    }

                                    if (val!=null && leverl3start==1 && indexB != -1) {
                                        indexBegin = indexB;
                                        indexEnd = len;
                                        if (indexC != -1)
                                            indexEnd = indexC;
                                        subStr = StringUtils.substring(tempVal,indexBegin+3,indexEnd);
                                        System.out.println(subStr);
                                        choiceList.put("B", subStr);

                                    }

                                    if (val!=null && leverl3start==1 && indexC != -1) {
                                        indexBegin = indexC;
                                        indexEnd = len;
                                        if (indexD != -1)
                                            indexEnd = indexD;
                                        subStr = StringUtils.substring(tempVal,indexBegin+3,indexEnd);
                                        System.out.println(subStr);
                                        choiceList.put("C", subStr);

                                    }



                                    if (val!=null && leverl3start==1 && indexD != -1) {
                                        indexBegin = indexD;
                                        indexEnd = len;
                                        if (indexE != -1)
                                            indexEnd = indexE;
                                        subStr = StringUtils.substring(tempVal,indexBegin+3,indexEnd);
                                        System.out.println(subStr);
                                        choiceList.put("D", subStr);

                                        if (indexE == -1){
                                            qContent.setTitle(title);
                                            qContent.setChoiceList(choiceList);
                                            Gson gson = new Gson();
                                            String content = gson.toJson(qContent);
                                            question.setName(name);
                                            question.setContent(content);

                                            System.out.println(question);


                                            // 知识点列表为空，取题库类型的第一个知识点
                                            if(pointList == null && pointMap != null){
                                                List<Integer> pointList3 = new ArrayList<Integer>();
                                                KnowledgePoint kp = pointMap.entrySet().iterator().next().getValue();
                                                pointList3.add(kp.getPointId());
                                                question.setPointList(pointList3);
                                            }else if (pointList != null){//页面选择知识列表不为空
                                                question.setPointList(pointList);
                                            }else {
                                                System.out.println("页面选择知识列表不为空");
                                            }
//                                      添加试卷标题ＩＤ和题目序号
                                            question.setTest_id(testMaxId);
                                            question.setTest_num(Integer.valueOf(level2Num));

                                            questionList.add(question);
                                            choiceList.clear();//清空选出项列表
                                            leverl3start=0;//3级标题结束 标志清零
                                        }

                                    }

                                    if (val!=null && leverl3start==1 && indexE != -1) {
                                        indexBegin = indexE;
                                        indexEnd = len;
                                        if (indexF != -1)
                                            indexEnd = indexF;
                                        subStr = StringUtils.substring(tempVal,indexBegin+3,indexEnd);
                                        System.out.println(subStr);
                                        choiceList.put("E", subStr);

                                        if (indexF == -1){
                                            qContent.setTitle(title);
                                            qContent.setChoiceList(choiceList);
                                            Gson gson = new Gson();
                                            String content = gson.toJson(qContent);
                                            question.setName(name);
                                            question.setContent(content);

                                            System.out.println(question);


                                            // 知识点列表为空，取题库类型的第一个知识点
                                            if(pointList == null && pointMap != null){
                                                List<Integer> pointList3 = new ArrayList<Integer>();
                                                KnowledgePoint kp = pointMap.entrySet().iterator().next().getValue();
                                                pointList3.add(kp.getPointId());
                                                question.setPointList(pointList3);
                                            }else if (pointList != null){//页面选择知识列表不为空
                                                question.setPointList(pointList);
                                            }else {
                                                System.out.println("页面选择知识列表不为空");
                                            }
//                                      添加试卷标题ＩＤ和题目序号
                                            question.setTest_id(testMaxId);
                                            question.setTest_num(Integer.valueOf(level2Num));

                                            questionList.add(question);
                                            choiceList.clear();//清空选出项列表
                                            leverl3start=0;//3级标题结束 标志清零
                                        }

                                    }

                                    if (val!=null && leverl3start==1 && indexF != -1) {
                                        indexBegin = indexF;
                                        indexEnd = len;
                                        if (indexG != -1)
                                            indexEnd = indexG;
                                        subStr = StringUtils.substring(tempVal,indexBegin+3,indexEnd);
                                        System.out.println(subStr);
                                        choiceList.put("F", subStr);

                                        if (indexG == -1){
                                            qContent.setTitle(title);
                                            qContent.setChoiceList(choiceList);
                                            Gson gson = new Gson();
                                            String content = gson.toJson(qContent);
                                            question.setName(name);
                                            question.setContent(content);

                                            System.out.println(question);


                                            // 知识点列表为空，取题库类型的第一个知识点
                                            if(pointList == null && pointMap != null){
                                                List<Integer> pointList3 = new ArrayList<Integer>();
                                                KnowledgePoint kp = pointMap.entrySet().iterator().next().getValue();
                                                pointList3.add(kp.getPointId());
                                                question.setPointList(pointList3);
                                            }else if (pointList != null){//页面选择知识列表不为空
                                                question.setPointList(pointList);
                                            }else {
                                                System.out.println("页面选择知识列表不为空");
                                            }
//                                      添加试卷标题ＩＤ和题目序号
                                            question.setTest_id(testMaxId);
                                            question.setTest_num(Integer.valueOf(level2Num));

                                            questionList.add(question);
                                            choiceList.clear();//清空选出项列表
                                            leverl3start=0;//3级标题结束 标志清零
                                        }

                                    }
                                }//end 3级标题有括号
                                else if(iskh==0) {//选项【A-D】不含括号
                                    int len = val.length();
                                    int indexBegin = -1;
                                    int indexEnd = len;
                                    int indexA = val.indexOf("A");
                                    int indexB = val.indexOf("B");
                                    int indexC = val.indexOf("C");
                                    int indexD = val.indexOf("D");
                                    int indexE = val.indexOf("E");
                                    int indexF = val.indexOf("F");
                                    int indexG = val.indexOf("G");
                                    String tempVal = val;
                                    String subStr = null;
                                    String charA = val.charAt(0) + "";
                                    if (val!=null &&  indexA!= -1 && "A".equals(charA)) {
                                        leverl3start=1;
                                        indexBegin = indexA;
                                        if (indexB != -1)
                                            indexEnd = indexB;
                                        subStr = StringUtils.substring(tempVal,indexBegin+2,indexEnd);
                                        System.out.println(subStr);
                                        choiceList.put("A", subStr);
                                    }

                                    if (val!=null && leverl3start==1 && indexB != -1) {
                                        indexBegin = indexB;
                                        indexEnd = len;
                                        if (indexC != -1){
                                            indexEnd = indexC;
                                        }
                                        subStr = StringUtils.substring(tempVal,indexBegin+2,indexEnd);
                                        System.out.println(subStr);
                                        choiceList.put("B", subStr);
                                    }

                                    if (val!=null && leverl3start==1 && indexC != -1) {
                                        indexBegin = indexC;
                                        indexEnd = len;
                                        if (indexD != -1){
                                            indexEnd = indexD;
                                        }
                                        subStr = StringUtils.substring(tempVal,indexBegin+2,indexEnd);
                                        System.out.println(subStr);
                                        choiceList.put("C", subStr);
                                    }

                                    if (val!=null && leverl3start==1 && indexD != -1) {
                                        indexBegin = indexD;
                                        indexEnd = len;
                                        if (indexE != -1){
                                            indexEnd = indexE;
                                        }
                                        subStr = StringUtils.substring(tempVal,indexBegin+2,indexEnd);
                                        System.out.println(subStr);
                                        choiceList.put("D", subStr);

                                        if (indexE == -1){
                                            qContent.setTitle(title);
                                            qContent.setChoiceList(choiceList);
                                            Gson gson = new Gson();
                                            String content = gson.toJson(qContent);
                                            question.setName(name);
                                            question.setContent(content);

                                            //                        System.out.println("content ===> " + content);
                                            //                        System.out.println("question ===> " + question.toString());

                                            System.out.println(question);


//                                        添加试卷标题ＩＤ和题目序号

                                            question.setTest_id(testMaxId);
                                            question.setTest_num(Integer.valueOf(level2Num));

                                            questionList.add(question);
                                            choiceList.clear();
                                            leverl3start=0;
                                        }

                                    }

                                    if (val!=null && leverl3start==1 && indexE != -1) {
                                        indexBegin = indexE;
                                        indexEnd = len;
                                        if (indexF != -1){
                                            indexEnd = indexF;
                                        }
                                        subStr = StringUtils.substring(tempVal,indexBegin+2,indexEnd);
                                        System.out.println(subStr);
                                        choiceList.put("E", subStr);

                                        if (indexF == -1){
                                            qContent.setTitle(title);
                                            qContent.setChoiceList(choiceList);
                                            Gson gson = new Gson();
                                            String content = gson.toJson(qContent);
                                            question.setName(name);
                                            question.setContent(content);

                                            //                        System.out.println("content ===> " + content);
                                            //                        System.out.println("question ===> " + question.toString());

                                            System.out.println(question);


//                                        添加试卷标题ＩＤ和题目序号

                                            question.setTest_id(testMaxId);
                                            question.setTest_num(Integer.valueOf(level2Num));

                                            questionList.add(question);
                                            choiceList.clear();
                                            leverl3start=0;
                                        }


                                    }

                                    if (val!=null && leverl3start==1 && indexF != -1) {
                                        indexBegin = indexF;
                                        indexEnd = len;
                                        if (indexG != -1){
                                            indexEnd = indexF;
                                        }
                                        subStr = StringUtils.substring(tempVal,indexBegin+2,indexEnd);
                                        System.out.println(subStr);
                                        choiceList.put("F", subStr);

                                        if (indexG == -1){
                                            qContent.setTitle(title);
                                            qContent.setChoiceList(choiceList);
                                            Gson gson = new Gson();
                                            String content = gson.toJson(qContent);
                                            question.setName(name);
                                            question.setContent(content);
                                              System.out.println(question);
//                                        添加试卷标题ＩＤ和题目序号
                                            question.setTest_id(testMaxId);
                                            question.setTest_num(Integer.valueOf(level2Num));
                                            questionList.add(question);
                                            choiceList.clear();
                                            leverl3start=0;
                                        }


                                    }

                                }//end else else if(iskh==0)
                                /***************************************** */
                            }
                        }//end 三级标题

                    }//end else 二级标题

                }//end else 一级标题解析

            }// end for 循环
            System.out.println("questionList.size=====>" + questionList.size());
//        System.out.println(questionList);
//        questionMapper.insertForeach(questionList);

            //修改试卷名称记录的一级标题字符串
                questionTest.setId(testMaxId);
                questionTest.setMemo(level1Title);
                if(pointList!=null)
                questionTest.setPoint_ids(pointList.toString());

                System.out.println("maxid="+testMaxId+"  levelTitle="+level1Title+"  pointids="+pointIds);
//                questionTest.setPoint_ids("5");
            questionMapper.updateQuestionTest(questionTest);

            for(Question question : questionList) {
                questionMapper.insertQuestion(question);
//                questionMapper.addQuestionKnowledgePoint(question.getId(), 5);
//                questionMapper.addQuestionKnowledgePoint(question.getId(), 1);
                if(question.getPointList() == null){
                    continue;
                }
                for (Integer i : question.getPointList()) {
                    questionMapper.addQuestionKnowledgePoint(question.getId(), i);
                }
            }


            return questionList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public List<Question> parseWord(String filePath, Map<String, KnowledgePoint> pointMap) throws Exception {
        return null;
    }

    @Override
    public List<Question> parseWord(String filePath, Map<String, KnowledgePoint> pointMap, List<Integer> pointList2) throws Exception {
        return null;
    }
}
