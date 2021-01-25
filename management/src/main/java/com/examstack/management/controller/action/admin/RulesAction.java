package com.examstack.management.controller.action.admin;


import com.examstack.common.domain.rules.CmsCat;
import com.examstack.common.domain.rules.CmsRules;
import com.examstack.common.domain.exam.Message;
import com.examstack.common.util.Page;
import com.examstack.common.util.PagingUtil;
import com.examstack.management.security.UserInfo;
import com.examstack.management.service.ICmsCatService;
import com.examstack.management.service.ICmsRulesService;
import com.examstack.management.service.UserService;
import com.examstack.management.ueditor.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * @ClassName : RulesAction  //类名
 * @Description : 規章制度信息发布  //描述
 * @Author : Airman  //作者
 * @Date: 2020-09-19 12:21  //时间
 * @Version: 1.0
 */
@Controller
public class RulesAction {

    @Autowired
    private ICmsRulesService cmsRulesService;
    @Autowired
    private ICmsCatService cmsCatService;
    @Autowired
    private UserService userService;


    @RequestMapping(value = "/admin/rules/rules_add", method = RequestMethod.GET)
    private String rulesPaperAddPage(Model model, HttpServletRequest request){
        Page<CmsCat> pageModel = new Page<CmsCat>();
        List<CmsCat> cmsCatList = cmsCatService.getCmsCatList(pageModel,"1");
        model.addAttribute("catList",cmsCatList);
        return "rules_add";
    }

    @RequestMapping(value = "/admin/rules/rules_cat_add", method = RequestMethod.GET)
    private String rulesCatAddPage(HttpServletRequest request){

        return "rules_cat_add";
    }

    @RequestMapping(value = "/admin/rules/rules_update/{id}", method = RequestMethod.GET)
    private String  rulesPaperUpdatePage(Model model, @PathVariable("id") Long id){
        try{
            Page<CmsCat> pageModel2 = new Page<CmsCat>();
            //获取1：资料库的目录列表
            List<CmsCat> cmsCatList = cmsCatService.getCmsCatList(pageModel2,"1");
            model.addAttribute("catList",cmsCatList);
            //获取当前ID的资料信息
            CmsRules cmsRules = cmsRulesService.getCmsRulesById(id);
            model.addAttribute("cmsRules",cmsRules);
            model.addAttribute("catId",cmsRules.getCatId());
            System.out.println("cmsRules"+cmsRules.getTitle()+"  "+cmsRules.getContent()+" ");
        }catch(Exception e){
            e.printStackTrace();
        }

        return "rules_update";
    }

    @RequestMapping(value = "/admin/rules/cmsRules_update_up/{id}", method = RequestMethod.POST)
    private @ResponseBody String  cmsPaperUpdate(@RequestBody CmsRules rules, @PathVariable("id") Long id){
        System.out.println("-------- cms update page start --------");
        Message message = new Message();
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        rules.setId(id);
        rules.setCreatorId(userInfo.getUserid());
        rules.setCreator(userInfo.getUsername());
        try{
            cmsRulesService.updateCmsRules(rules);
            return Constants.CONTEXT_PATH + "/admin/cms/cms-rules-list";

        }catch(Exception e){
            message.setResult(e.getClass().getName());
            e.printStackTrace();
            return "rules_update";
        }
//        return message;
    }

    @RequestMapping(value = "/admin/cms/cmsRules_add_in", method = RequestMethod.POST)
    private @ResponseBody String cmsPaperAddPageIn(@RequestBody CmsRules rules){

        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        rules.setCreatorId(userInfo.getUserid());
        rules.setCreator(userInfo.getUsername());
        Message message = new Message();

        try{
            cmsRulesService.addCmsRules(rules);

            return Constants.CONTEXT_PATH + "/admin/cms/cms-rules-list";
        }catch(Exception e){
            message.setResult(e.getClass().getName());
            e.printStackTrace();
            return "rules_add";
        }

//        return message;
    }

    @RequestMapping(value = "/admin/cms/cms-rules-list", method = RequestMethod.GET)
    public String cmsRulesListPage(Model model, String catId, RedirectAttributes attr,String page) {

        attr.addAttribute("catId",catId);

        if(page == null){
            return "redirect:cms-rules-list/filter-1.html";
        } else {
            return "redirect:cms-rules-list/filter-" + page + ".html";
        }
    }

    @RequestMapping(value = "/admin/cms/cms-rules-list/filter-{page}.html", method = RequestMethod.GET)
    public String questionListFilterPage(Model model,
                                         @PathVariable("page") int page, String catId) {
        Page<CmsRules> pageModel = new Page<CmsRules>();
        pageModel.setPageNo(page);
        pageModel.setPageSize(10);
        List<CmsRules> cmsRulesList = cmsRulesService.getCmsRulesList(pageModel, catId);

        String pageStr = PagingUtil.getPagelink(page,
                pageModel.getTotalPage(),"",Constants.CONTEXT_PATH + "/admin/cms/cms-rules-list");
        for(CmsRules  rules: cmsRulesList){
            System.out.println("rules = " + rules.getTitle() + "   " + rules.getCreateTime());
        }

        Page<CmsCat> pageModel2 = new Page<CmsCat>();
        //获取1：资料库的目录列表
        List<CmsCat> cmsCatList = cmsCatService.getCmsCatList(pageModel2,"1");
        model.addAttribute("catList",cmsCatList);
        model.addAttribute("catId",catId);
        model.addAttribute("pageStr", pageStr);
        model.addAttribute("cmsRulesList", cmsRulesList);
        return "cms-rules-list";
    }

    @RequestMapping(value = "/admin/cms/delete-cmsRules/{id}", method = RequestMethod.GET)
    public @ResponseBody Message deleteCmsRules(Model model, @PathVariable("id") Long id) {
        Message message = new Message();
        try {
            cmsRulesService.deleteCmsRulesById(id);
        } catch (Exception ex) {
            message.setResult(ex.getClass().getName());
        }
        return message;
    }
    @RequestMapping(value = "/admin/rules/cms-rules-show/{id}", method = RequestMethod.GET)
    public String questionListFilterPage(Model model,
                                         @PathVariable("id") Long id) {
       CmsRules rules  = cmsRulesService.getCmsRulesById(id);
        model.addAttribute("cmsRules", rules);
        return "cms-rules-show";
    }

}
