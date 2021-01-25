package com.examstack.portal.service;

import com.examstack.common.domain.expert.CmsExpert;
import com.examstack.common.util.Page;

import java.util.List;

public interface CmsExpertService {
    /**
     * 添加规则
     * @param cmsExpert
     */
    public void addCmsExpert(CmsExpert cmsExpert);

    public List<CmsExpert> getCmsExpertList(Page<CmsExpert> pageModel, String catId);

    public void deleteCmsExpertById(Long id);

    public void updateCmsExpert(CmsExpert cmsExpert);

    public CmsExpert getCmsExpertById(Long id);
}
