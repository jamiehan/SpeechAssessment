package com.examstack.management.service;

import com.examstack.common.domain.cms.CmsNote;
import com.examstack.common.util.Page;

import java.util.List;

public interface ICmsNoteService {
    /**
     * 添加通知
     * @param cmsNote
     */
    public void addCmsNote(CmsNote cmsNote);

    public List<CmsNote> getCmsNoteList(Page<CmsNote> pageModel);

    public void deleteCmsNoteById(Long id);

    public void updateCmsNote(CmsNote cmsNote);

    public CmsNote getCmsNoteById(Long id);
}
