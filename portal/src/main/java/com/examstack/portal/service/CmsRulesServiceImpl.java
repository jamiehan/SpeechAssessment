package com.examstack.portal.service;

import com.examstack.common.domain.rules.CmsRules;
import com.examstack.common.util.Page;
import com.examstack.portal.persistence.CmsRulesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName : CmsRulesServiceImpl  //类名
 * @Description : cmsRules  //描述
 * @Author : Airman  //作者
 * @Date: 2020-09-19 23:20  //时间
 * @Version: 1.0
 */
@Service("cmsRulesService")
public class CmsRulesServiceImpl implements CmsRulesService {

    @Autowired
    private CmsRulesMapper cmsRulesMapper;

    public void addCmsRules(CmsRules cmsRules){
        cmsRulesMapper.addCmsRules(cmsRules);
    }

    @Override
    public List<CmsRules> getCmsRulesList(Page<CmsRules> pageModel,String catId) {
        // TODO Auto-generated method stub
        return cmsRulesMapper.getCmsRulesList(pageModel,catId);
    }

    @Override
    public void deleteCmsRulesById(Long id) {
        // TODO Auto-generated method stub
        cmsRulesMapper.deleteCmsRulesById(id);
    }

    @Override
    public void updateCmsRules(CmsRules cmsRules) {
        // TODO Auto-generated method stub
        cmsRulesMapper.updateCmsRules(cmsRules);
    }
    @Override
    public CmsRules getCmsRulesById(Long id){
        return cmsRulesMapper.getCmsRulesById(id);
    }
}
