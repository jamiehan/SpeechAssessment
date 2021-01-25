package com.examstack.management.persistence;

import com.examstack.common.domain.expert.CmsExpert;
import com.examstack.common.util.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface CmsExpertMapper {
    /**
     * 添加规则
     * @param cmsExpert
     */
    public void addCmsExpert(CmsExpert cmsExpert);

    public List<CmsExpert> getCmsExpertList(
            @Param("page") Page<CmsExpert> page, @Param("catId") String catId);

    public void deleteCmsExpertById(
            @Param("id") Long id);

    public void updateCmsExpert(CmsExpert cmsExpert);

    public CmsExpert getCmsExpertById(Long id);
}
