package com.examstack.management.service;

import java.io.File;
import java.util.*;

import com.examstack.common.domain.question.KnowledgePoint;
import com.examstack.common.domain.question.Question;
import com.examstack.common.domain.question.QuestionContent;
import com.examstack.common.util.file.ExcelUtil;
import com.examstack.management.security.UserInfo;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.examstack.common.domain.user.Department;
import com.examstack.common.domain.user.Group;
import com.examstack.common.domain.user.Role;
import com.examstack.common.domain.user.User;
import com.examstack.common.util.Page;
import com.examstack.common.util.StandardPasswordEncoderForSha1;
import com.examstack.management.persistence.UserMapper;

/**
 * @author Ocelot
 * @date 2014年6月8日 下午8:21:31
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	public UserMapper userMapper;

	@Override
	@Transactional
	public int addUser(User user,String authority,int groupId,HashMap<String,Role> roleMap) {
		try {
			int userId = -1;
			userMapper.insertUser(user);
			userId = user.getUserId();
			userMapper.grantUserRole(userId, roleMap.get(authority).getRoleId());
			if(user.getDepId() != 0 && user.getDepId() != -1)
				userMapper.addUser2Dep(userId, user.getDepId());
			if("ROLE_TEACHER".equals(authority)){
				Group group = new Group();
				group.setGroupName("默认分组");
				group.setDefaultt(true);
				group.setUserId(userId);
				userMapper.addGroup(group);
			}if("ROLE_STUDENT".equals(authority)){
				//只能给学员分配自己已经有的分组
				List<Group> groupList = userMapper.getGroupListByUserId(user.getCreateBy(), null);
				boolean flag = false;
				for(Group group : groupList){
					if(group.getGroupId() == groupId){
						flag = true;
						break;
					}
				}
				if(groupId != 0){
					if(flag){
						userMapper.addUserGroup(userId, groupId);
					}else
						throw new Exception("不能将学员分配给一个不存在的分组");
				}
				
			}
			return userId;
		} catch (Exception e) {
			String cause = e.getCause().getMessage();
			throw new RuntimeException(cause);
		}
	}
	
	@Override
	public List<User> getUserListByRoleId(int roleId,Page<User> page) {
		// TODO Auto-generated method stub
		List<User> userList = userMapper.getUserListByRoleId(roleId, page);
		return userList;
	}
	
	@Override
	@Transactional
	public void updateUser(User user, String oldPassword) {
		// TODO Auto-generated method stub
		try {
			userMapper.updateUser(user, oldPassword);
			
			if(user.getDepId() != -1){
				userMapper.deleteUser2Dep(user.getUserId());
				userMapper.addUser2Dep(user.getUserId(), user.getDepId());
			}	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<Group> getGroupListByUserId(int userId, Page<Group> page) {
		// TODO Auto-generated method stub
		return userMapper.getGroupListByUserId(userId, page);
	}

	@Override
	public void addGroup(Group group) {
		// TODO Auto-generated method stub
		userMapper.addGroup(group);
	}

	@Override
	public HashMap<String, Role> getRoleMap() {
		// TODO Auto-generated method stub
		List<Role> roleList = userMapper.getRoleList();
		HashMap<String,Role> map = new HashMap<String,Role>();
		for(Role r : roleList){
			map.put(r.getAuthority(), r);
		}
		return map;
	}

	@Override
	public void changeUserStatus(List<Integer> idList,boolean enabled) {
		// TODO Auto-generated method stub
		userMapper.changeUserStatus(idList, enabled);
	}

	@Override
	public void updateGroup(int groupId, String groupName) {
		// TODO Auto-generated method stub
		userMapper.updateGroup(groupId, groupName);
	}

	@Override
	public void deleteGroup(int groupId) {
		// TODO Auto-generated method stub
		userMapper.deleteGroup(groupId);
	}

	@Override
	public void addUserGroup(int userId, int groupId) {
		// TODO Auto-generated method stub
		userMapper.addUserGroup(userId, groupId);
	}

	@Override
	public List<User> getUserListByGroupIdAndParams(int groupId, String authority, String searchStr, Page<User> page) {
		// TODO Auto-generated method stub
		return userMapper.getUserListByGroupIdAndParams(groupId, authority, searchStr, page);
	}

	@Override
	public void addUsers2Group(String[] userNames, int groupId,HashMap<String,Role> roleMap) {
		// TODO Auto-generated method stub
		List<User> userList = userMapper.getUserByNames(userNames,roleMap.get("ROLE_STUDENT").getRoleId());
		List<Integer> idList = new ArrayList<Integer>();
		for(User user : userList){
			idList.add(user.getUserId());
		}
		userMapper.addUsers2Group(idList, groupId);
	}

	@Override
	public void deleteUserGroup(int userId, int groupId, int managerId) {
		// TODO Auto-generated method stub
		userMapper.deleteUserGroup(userId, groupId, managerId);
	}

	@Override
	public List<Department> getDepList(Page<Department> page) {
		// TODO Auto-generated method stub
		return userMapper.getDepList(page);
	}

	@Override
	public void addDep(Department dep) {
		// TODO Auto-generated method stub
		userMapper.addDep(dep);
	}

	@Override
	public void updateDep(Department dep) {
		// TODO Auto-generated method stub
		userMapper.updateDep(dep);
	}

	@Override
	public void deleteDep(int depId) {
		// TODO Auto-generated method stub
		userMapper.deleteDep(depId);
	}

	@Override
	public void updateUserPwd(String userName, String password, String authority) throws Exception {
		// TODO Auto-generated method stub
		User user = userMapper.getUserByName(userName);
		if(user.getRoles().contains(authority) && !"ROLE_ADMIN".equals(authority))
			throw new Exception("教师只能更新学员的密码！");
		PasswordEncoder passwordEncoder = new StandardPasswordEncoderForSha1();
		password = passwordEncoder.encode(password + "{" + userName + "}");
		User tmpUser = new User();
		tmpUser.setUserId(user.getUserId());
		tmpUser.setPassword(password);
		userMapper.updateUser(tmpUser, null);
		
	}

	@Transactional
	@Override
	public void userImport(User user, String username) {
		// TODO Auto-generated method stub
		//String filePath,List<Integer> pointList2 // Question question
		String filePath = user.getFilePath();
		String fileName = user.getFileName();

		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		String strPath = ",webapps,files,training," + username;

		filePath = System.getProperty("catalina.base") + strPath.replace(',', File.separatorChar) + File.separatorChar
				+ filePath;
		user.setFilePath(filePath);
//		Map<String, KnowledgePoint> pointMap = this.getKnowledgePointMapByFieldId(fieldId, null);
		int index = 2;
		try {
			String fileType = filePath.substring(filePath.lastIndexOf(".") + 1);
			if("xls".equals(fileType.toLowerCase()) || "xlsx".equals(fileType.toLowerCase())) {

				List<Map<String, String>> userMapList = ExcelUtil.ExcelToList(filePath);
				for (Map<String, String> map : userMapList) {

					user = new User();
					user.setUserName(map.get("用户名"));
					user.setPassword(map.get("密码"));
					user.setTrueName(map.get("姓名"));
					user.setNationalId(map.get("身份证号"));
					user.setPhoneNum(map.get("手机"));
					user.setEmail(map.get("email"));
					user.setCompany(map.get("所属单位"));
					user.setDepartment(map.get("所属部门"));

					this.addUser(user,"ROLE_STUDENT",1,userInfo.getRoleMap());

//					System.out.println(map);
//					User question = new User();
//					question.setName(map.get("题目").length() > 10 ? map.get("题目").substring(0, 10) + "..." : map.get("题目"));
//					if (map.get("类型").equals("单选题") || map.get("类型").equals("单项选择题"))
//						question.setQuestion_type_id(1);
//					else if (map.get("类型").equals("多选题") || map.get("类型").equals("多项选择题"))
//						question.setQuestion_type_id(2);
//					else if (map.get("类型").equals("判断题"))
//						question.setQuestion_type_id(3);
//					else if (map.get("类型").equals("填空题"))
//						question.setQuestion_type_id(4);
//					else if (map.get("类型").equals("简答题"))
//						question.setQuestion_type_id(5);
//					else if (map.get("类型").equals("论述题"))
//						question.setQuestion_type_id(6);
//					else if (map.get("类型").equals("分析题"))
//						question.setQuestion_type_id(7);
//
//					question.setAnalysis(map.get("解析"));
//					question.setAnswer(map.get("答案"));
//					if (question.getQuestion_type_id() == 3) {
//						if (map.get("答案").equals("对"))
//							question.setAnswer("T");
//						if (map.get("答案").equals("错"))
//							question.setAnswer("F");
//					}
//
//					KnowledgePoint kp = pointMap.get(map.get("知识类"));
//					if (kp == null)
//						throw new Exception("知识类不存在：" + map.get("知识类"));
//					List<Integer> pointList = new ArrayList<Integer>();
//					pointList.add(kp.getPointId());
//					question.setReferenceName(map.get("出处"));
//					question.setExamingPoint(map.get("知识点"));
//					question.setKeyword(map.get("知识关键点"));
//					question.setPoints(map.get("分值").equals("") ? 0 : Float.parseFloat(map.get("分值")));
//					QuestionContent qc = new QuestionContent();
//
//					Iterator<String> it = map.keySet().iterator();
//					List<String> keyStr = new ArrayList<String>();
//					while (it.hasNext()) {
//						String key = it.next();
//						if (key.contains("选项"))
//							keyStr.add(key.replace("选项", ""));
//					}
//
//					Collections.sort(keyStr);
//					LinkedHashMap<String, String> choiceList = new LinkedHashMap<String, String>();
//					for (int i = 0; i < keyStr.size(); i++) {
//						if (!map.get("选项" + keyStr.get(i)).trim().equals(""))
//							choiceList.put(keyStr.get(i), map.get("选项" + keyStr.get(i)));
//					}
//					if (question.getQuestion_type_id() == 1 || question.getQuestion_type_id() == 2)
//						qc.setChoiceList(choiceList);
//					qc.setTitle(map.get("题目"));
//					Gson gson = new Gson();
//					String content = gson.toJson(qc);
//					question.setContent(content);
//					question.setCreator(username);
//					question.setPointList(pointList);
//					this.addQuestion(question);
//					index++;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
			throw new RuntimeException("第" + index + "行有错误，请检查！" + e.getMessage());
		}
	}
}
