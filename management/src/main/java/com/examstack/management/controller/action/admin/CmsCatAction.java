package com.examstack.management.controller.action.admin;

import com.examstack.common.domain.exam.Message;
import com.examstack.common.domain.rules.CmsCat;
import com.examstack.common.domain.rules.CmsRules;
import com.examstack.common.util.Page;
import com.examstack.common.util.PagingUtil;
import com.examstack.management.security.UserInfo;
import com.examstack.management.service.ICmsCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Controller
public class CmsCatAction {

    @Autowired
    private ICmsCatService cmsCatService;

    @RequestMapping(value = "/admin/cms/cms-cat-list", method = RequestMethod.GET)
    public String cmsRulesListPage(Model model) {

        return "redirect:cms-cat-list/filter-1.html";
    }

    @RequestMapping(value = "/admin/cms/cms-cat-list/filter-{page}.html", method = RequestMethod.GET)
    public String questionListFilterPage(Model model,
                                         @PathVariable("page") int page) {
        Page<CmsCat> pageModel = new Page<CmsCat>();
        pageModel.setPageNo(page);
        pageModel.setPageSize(20);
        List<CmsCat> cmsCatList1 = cmsCatService.getCmsCatList(pageModel,"1");
        List<CmsCat> cmsCatList2 = cmsCatService.getCmsCatList(pageModel,"2");

        String pageStr = PagingUtil.getPageBtnlink(page,
                pageModel.getTotalPage());
        for(CmsCat  cat: cmsCatList1){
            System.out.println("rules = " + cat.getCatName() + "   " + cat.getCreateTime());
        }

        model.addAttribute("pageStr", pageStr);
        model.addAttribute("cmsCatList1", cmsCatList1);
        model.addAttribute("cmsCatList2", cmsCatList2);
        return "cms-cat-list";
    }

    @RequestMapping(value = "/admin/rules/cat_update/{id}", method = RequestMethod.GET)
    private String  rulesPaperUpdatePage(Model model, @PathVariable("id") Integer id){
        try{
            CmsCat cmsCat = cmsCatService.getCmsCatById(id);
            model.addAttribute("cmsCat",cmsCat);
            System.out.println("cmsCat:"+cmsCat.getCatName());
        }catch(Exception e){
            e.printStackTrace();
        }

        return "cat_update";
    }

    @RequestMapping(value = "/admin/rules/cmsCat_update_up/{id}", method = RequestMethod.POST)
    private @ResponseBody
    Message cmsPaperUpdate(@RequestBody CmsCat cat, @PathVariable("id") Integer id){

        Message message = new Message();
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        cat.setId(id);
        cat.setCreatorId(userInfo.getUserid());
        cat.setCreator(userInfo.getUsername());
        try{
            cmsCatService.updateCmsCat(cat);

        }catch(Exception e){
            message.setResult(e.getClass().getName());
            e.printStackTrace();
        }

        return message;
    }

    @RequestMapping(value = "/admin/cms/cms_cat_add_in", method = RequestMethod.POST)
    private @ResponseBody Message cmsCatAddPageIn(@RequestBody CmsCat cmsCat){

        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        //cmsCat.setType(1);//1:资料库 2：专家库
        cmsCat.setCreateTime(new Date());
        cmsCat.setCreatorId(userInfo.getUserid());
        cmsCat.setCreator(userInfo.getUsername());
        Message message = new Message();

        try{
            cmsCatService.addCmsCat(cmsCat);
        }catch(Exception e){
            message.setResult(e.getClass().getName());
            e.printStackTrace();
        }

        return message;
    }

    @RequestMapping(value = "/admin/exoert/expert_cat_add", method = RequestMethod.GET)
    private String expertCatAddPage(HttpServletRequest request){

        return "expert_cat_add";
    }

    @RequestMapping(value = "/admin/cms/delete-cat/{id}", method = RequestMethod.GET)
    public @ResponseBody Message deleteCmsRules(Model model, @PathVariable("id") Integer id) {
        Message message = new Message();
        try {
            cmsCatService.deleteCmsCatById(id);
        } catch (Exception ex) {
            message.setResult(ex.getClass().getName());
        }
        return message;
    }

}
