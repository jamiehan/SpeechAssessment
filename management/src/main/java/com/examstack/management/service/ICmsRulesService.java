package com.examstack.management.service;

import com.examstack.common.domain.rules.CmsRules;
import com.examstack.common.util.Page;

import java.util.List;

public interface ICmsRulesService {
    /**
     * 添加规则
     * @param cmsRules
     */
    public void addCmsRules(CmsRules cmsRules);

    public List<CmsRules> getCmsRulesList(Page<CmsRules> pageModel, String catId);

    public void deleteCmsRulesById(Long id);

    public void updateCmsRules(CmsRules cmsRules);

    public CmsRules getCmsRulesById(Long id);
}
