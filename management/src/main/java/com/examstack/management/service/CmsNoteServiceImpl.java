package com.examstack.management.service;

import com.examstack.common.domain.cms.CmsNote;

import com.examstack.common.util.Page;
import com.examstack.management.persistence.CmsNoteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName : CmsNoteServiceImpl  //类名
 * @Description : cmsnote  //描述
 * @Author : Airman  //作者
 * @Date: 2020-09-19 23:20  //时间
 * @Version: 1.0
 */
@Service("cmsNoteService")
public class CmsNoteServiceImpl implements ICmsNoteService{

    @Autowired
    private CmsNoteMapper cmsNoteMapper;

    public void addCmsNote(CmsNote cmsNote){
        cmsNoteMapper.addCmsNote(cmsNote);
    }

    @Override
    public List<CmsNote> getCmsNoteList(Page<CmsNote> pageModel) {
        // TODO Auto-generated method stub
        return cmsNoteMapper.getCmsNoteList(pageModel);
    }

    @Override
    public void deleteCmsNoteById(Long id) {
        // TODO Auto-generated method stub
        cmsNoteMapper.deleteCmsNoteById(id);
    }

    @Override
    public void updateCmsNote(CmsNote cmsNote) {
        // TODO Auto-generated method stub
        cmsNoteMapper.updateCmsNote(cmsNote);
    }
    @Override
    public CmsNote getCmsNoteById(Long id){
        return cmsNoteMapper.getCmsNoteById(id);
    }
}
