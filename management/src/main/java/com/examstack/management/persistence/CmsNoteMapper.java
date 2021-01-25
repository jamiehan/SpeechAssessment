package com.examstack.management.persistence;

import com.examstack.common.domain.cms.CmsNote;
import com.examstack.common.domain.question.Question;
import com.examstack.common.util.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface CmsNoteMapper {
    /**
     * 添加通知
     * @param cmsNote
     */
    public void addCmsNote(CmsNote cmsNote);

    public List<CmsNote> getCmsNoteList(
            @Param("page") Page<CmsNote> page);

    public void deleteCmsNoteById(
            @Param("id") Long id);

    public void updateCmsNote(CmsNote cmsNote);

    public CmsNote getCmsNoteById(Long id);
}
