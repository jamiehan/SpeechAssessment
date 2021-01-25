package com.examstack.management.controller.action.admin;


import com.examstack.common.domain.cms.CmsNote;
import com.examstack.common.domain.exam.Message;
import com.examstack.common.domain.question.Question;
import com.examstack.common.domain.rules.CmsCat;
import com.examstack.common.util.Page;
import com.examstack.common.util.PagingUtil;
import com.examstack.management.security.UserInfo;
import com.examstack.management.service.ICmsNoteService;
import com.examstack.management.service.ICmsCatService;
import com.examstack.management.service.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.security.acl.LastOwnerException;
import java.util.Date;
import java.util.List;


/**
 * @ClassName : CmsEditAction  //类名
 * @Description : CMS信息发布  //描述
 * @Author : Airman  //作者
 * @Date: 2020-09-19 12:21  //时间
 * @Version: 1.0
 */
@Controller
public class CmsEditAction {

    @Autowired
    private ICmsNoteService cmsNoteService;
    @Autowired
    private UserService userService;
    @Autowired
    private ICmsCatService cmsCatService;


    @RequestMapping(value = "/admin/cms/cms_add", method = RequestMethod.GET)
    private String cmsPaperAddPage(HttpServletRequest request){

        return "cms_add";
    }

    @RequestMapping(value = "/admin/cms/cms_update/{id}", method = RequestMethod.GET)
    private String  cmsPaperUpdatePage(Model model, @PathVariable("id") Long id){


        try{
            CmsNote cmsNote = cmsNoteService.getCmsNoteById(id);
            model.addAttribute("cmsNote",cmsNote);
        }catch(Exception e){
            e.printStackTrace();
        }


        return "cms_update";
    }

    @RequestMapping(value = "/admin/cms/cms_update_up/{id}", method = RequestMethod.POST)
    private @ResponseBody Message  cmsPaperUpdate(@RequestBody CmsNote note, @PathVariable("id") Long id){
        System.out.println("-------- cms update page start --------");
        Message message = new Message();
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        note.setId(id);
        note.setCreatorId(userInfo.getUserid());
        note.setCreator(userInfo.getUsername());
        try{
            cmsNoteService.updateCmsNote(note);

        }catch(Exception e){
            message.setResult(e.getClass().getName());
            e.printStackTrace();
        }


        return message;
    }

    @RequestMapping(value = "/admin/cms/cms_add_in", method = RequestMethod.POST)
    private @ResponseBody Message cmsPaperAddPageIn(@RequestBody CmsNote note){

        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        note.setCreatorId(userInfo.getUserid());
        note.setCreator(userInfo.getUsername());
        Message message = new Message();

        try{
            cmsNoteService.addCmsNote(note);
        }catch(Exception e){
            message.setResult(e.getClass().getName());
            e.printStackTrace();
        }

        return message;
    }

    @RequestMapping(value = "/admin/cms/cms-note-list", method = RequestMethod.GET)
    public String cmsNoteListPage(Model model) {

        return "redirect:cms-note-list/filter-1.html";
    }

    @RequestMapping(value = "/admin/cms/cms-note-list/filter-{page}.html", method = RequestMethod.GET)
    public String questionListFilterPage(Model model,
                                         @PathVariable("page") int page) {
        Page<CmsNote> pageModel = new Page<CmsNote>();
        pageModel.setPageNo(page);
        pageModel.setPageSize(20);
        List<CmsNote> cmsNoteList = cmsNoteService.getCmsNoteList(pageModel);

        String pageStr = PagingUtil.getPageBtnlink(page,
                pageModel.getTotalPage());
        for(CmsNote  note: cmsNoteList){
            System.out.println("note = " + note.getNoteName() + "   " + note.getCreateTime());
        }

        model.addAttribute("pageStr", pageStr);
        model.addAttribute("cmsNoteList", cmsNoteList);
        return "cms-note-list";
    }

    @RequestMapping(value = "/admin/cms/delete-cms/{id}", method = RequestMethod.GET)
    public @ResponseBody Message deleteCmsnote(Model model, @PathVariable("id") Long id) {
        Message message = new Message();
        try {
            cmsNoteService.deleteCmsNoteById(id);
        } catch (Exception ex) {
            message.setResult(ex.getClass().getName());
        }
        return message;
    }
    @RequestMapping(value = "/admin/cms/cms-note-show/{id}", method = RequestMethod.GET)
    public String questionListFilterPage(Model model,
                                         @PathVariable("id") Long id) {
       CmsNote note  = cmsNoteService.getCmsNoteById(id);
        model.addAttribute("cmsNote", note);
        return "cms-note-show";
    }

}
