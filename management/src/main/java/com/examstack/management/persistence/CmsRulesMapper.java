package com.examstack.management.persistence;

import com.examstack.common.domain.rules.CmsRules;
import com.examstack.common.util.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface CmsRulesMapper {
    /**
     * 添加规则
     * @param cmsRules
     */
    public void addCmsRules(CmsRules cmsRules);

    public List<CmsRules> getCmsRulesList(
            @Param("page") Page<CmsRules> page, @Param("catId") String catId);

    public void deleteCmsRulesById(
            @Param("id") Long id);

    public void updateCmsRules(CmsRules cmsRules);

    public CmsRules getCmsRulesById(Long id);
}
