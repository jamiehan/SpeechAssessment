package com.examstack.management.controller.action.admin;


import com.examstack.common.domain.exam.Message;
import com.examstack.common.domain.rules.CmsCat;
import com.examstack.common.domain.expert.CmsExpert;
import com.examstack.common.util.Page;
import com.examstack.common.util.PagingUtil;
import com.examstack.management.security.UserInfo;
import com.examstack.management.service.ICmsCatService;
import com.examstack.management.service.ICmsExpertService;
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
 * @ClassName : expertAction  //类名
 * @Description : 規章制度信息发布  //描述
 * @Author : Airman  //作者
 * @Date: 2020-09-19 12:21  //时间
 * @Version: 1.0
 */
@Controller
public class ExpertAction {

    @Autowired
    private ICmsExpertService cmsExpertService;
    @Autowired
    private ICmsCatService cmsCatService;
    @Autowired
    private UserService userService;


    @RequestMapping(value = "/admin/expert/expert_add", method = RequestMethod.GET)
    private String expertPaperAddPage(Model model, HttpServletRequest request){
        Page<CmsCat> pageModel = new Page<CmsCat>();
        List<CmsCat> cmsCatList = cmsCatService.getCmsCatList(pageModel,"2");
        model.addAttribute("catList",cmsCatList);
        return "expert_add";
    }

    @RequestMapping(value = "/admin/expert/expert_cat_add", method = RequestMethod.GET)
    private String expertCatAddPage(HttpServletRequest request){

        return "expert_cat_add";
    }

    @RequestMapping(value = "/admin/expert/expert_update/{id}", method = RequestMethod.GET)
    private String  expertPaperUpdatePage(Model model, @PathVariable("id") Long id){
        try{
            Page<CmsCat> pageModel2 = new Page<CmsCat>();
            //获取2：专家库的目录列表
            List<CmsCat> cmsCatList = cmsCatService.getCmsCatList(pageModel2,"2");
            model.addAttribute("catList",cmsCatList);
            //获取当前ID的资料信息
            CmsExpert cmsExpert = cmsExpertService.getCmsExpertById(id);
            model.addAttribute("cmsExpert",cmsExpert);
            model.addAttribute("catId",cmsExpert.getCatId());
            System.out.println("cmsExpert"+cmsExpert.getTitle()+"  "+cmsExpert.getContent()+" ");
        }catch(Exception e){
            e.printStackTrace();
        }

        return "expert_update";
    }

    @RequestMapping(value = "/admin/expert/cmsExpert_update_up/{id}", method = RequestMethod.POST)
    private @ResponseBody String  cmsPaperUpdate(@RequestBody CmsExpert expert, @PathVariable("id") Long id){
        System.out.println("-------- cms update page start --------");
        Message message = new Message();
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        expert.setId(id);
        expert.setCreatorId(userInfo.getUserid());
        expert.setCreator(userInfo.getUsername());
        try{
            cmsExpertService.updateCmsExpert(expert);
            return "/admin/cms/cms-expert-list";

        }catch(Exception e){
            message.setResult(e.getClass().getName());
            e.printStackTrace();
            return "expert_update";
        }
//        return message;
    }

    @RequestMapping(value = "/admin/cms/cmsExpert_add_in", method = RequestMethod.POST)
    private @ResponseBody String cmsPaperAddPageIn(@RequestBody CmsExpert expert){

        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        expert.setCreatorId(userInfo.getUserid());
        expert.setCreator(userInfo.getUsername());
        Message message = new Message();

        try{
            cmsExpertService.addCmsExpert(expert);

            return Constants.CONTEXT_PATH + "/admin/cms/cms-expert-list";
        }catch(Exception e){
            message.setResult(e.getClass().getName());
            e.printStackTrace();
            return "expert_add";
        }

//        return message;
    }

    @RequestMapping(value = "/admin/cms/cms-expert-list", method = RequestMethod.GET)
    public String cmsExpertListPage(Model model, String catId, RedirectAttributes attr, String page) {

        attr.addAttribute("catId",catId);

        if ( page == null ) {
            return "redirect:cms-expert-list/filter-1.html";
        } else {
            return "redirect:cms-expert-list/filter-" + page + ".html";
        }

    }

    @RequestMapping(value = "/admin/cms/cms-expert-list/filter-{page}.html", method = RequestMethod.GET)
    public String questionListFilterPage(Model model,
                                         @PathVariable("page") int page, String catId) {
        Page<CmsExpert> pageModel = new Page<CmsExpert>();
        pageModel.setPageNo(page);
        pageModel.setPageSize(10);
        List<CmsExpert> cmsExpertList = cmsExpertService.getCmsExpertList(pageModel, catId);

        String pageStr = PagingUtil.getPagelink(page,
                pageModel.getTotalPage(),"", Constants.CONTEXT_PATH + "/admin/cms/cms-expert-list");
        for(CmsExpert  expert: cmsExpertList){
            System.out.println("expert = " + expert.getTitle() + "   " + expert.getCreateTime());
        }

        Page<CmsCat> pageModel2 = new Page<CmsCat>();
        //获取1：资料库的目录列表
        List<CmsCat> cmsCatList = cmsCatService.getCmsCatList(pageModel2,"2");
        model.addAttribute("catList",cmsCatList);
        model.addAttribute("catId",catId);
        model.addAttribute("pageStr", pageStr);
        model.addAttribute("cmsExpertList", cmsExpertList);
        return "cms-expert-list";
    }

    @RequestMapping(value = "/admin/cms/delete-cmsExpert/{id}", method = RequestMethod.GET)
    public @ResponseBody Message deleteCmsExpert(Model model, @PathVariable("id") Long id) {
        Message message = new Message();
        try {
            cmsExpertService.deleteCmsExpertById(id);
        } catch (Exception ex) {
            message.setResult(ex.getClass().getName());
        }
        return message;
    }
    @RequestMapping(value = "/admin/expert/cms-expert-show/{id}", method = RequestMethod.GET)
    public String questionListFilterPage(Model model,
                                         @PathVariable("id") Long id) {
       CmsExpert expert  = cmsExpertService.getCmsExpertById(id);
        model.addAttribute("cmsExpert", expert);
        return "cms-expert-show";
    }

}
