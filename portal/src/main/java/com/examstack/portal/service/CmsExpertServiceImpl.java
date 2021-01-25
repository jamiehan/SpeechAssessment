package com.examstack.portal.service;

import com.examstack.common.domain.expert.CmsExpert;
import com.examstack.common.util.Page;
import com.examstack.portal.persistence.CmsExpertMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName : CmsExpertServiceImpl  //类名
 * @Description : cmsExpert  //描述
 * @Author : Airman  //作者
 * @Date: 2020-09-19 23:20  //时间
 * @Version: 1.0
 */
@Service("cmsExpertService")
public class CmsExpertServiceImpl implements CmsExpertService {

    @Autowired
    private CmsExpertMapper cmsExpertMapper;

    public void addCmsExpert(CmsExpert cmsExpert){
        cmsExpertMapper.addCmsExpert(cmsExpert);
    }

    @Override
    public List<CmsExpert> getCmsExpertList(Page<CmsExpert> pageModel,String catId) {
        // TODO Auto-generated method stub
        return cmsExpertMapper.getCmsExpertList(pageModel,catId);
    }

    @Override
    public void deleteCmsExpertById(Long id) {
        // TODO Auto-generated method stub
        cmsExpertMapper.deleteCmsExpertById(id);
    }

    @Override
    public void updateCmsExpert(CmsExpert cmsExpert) {
        // TODO Auto-generated method stub
        cmsExpertMapper.updateCmsExpert(cmsExpert);
    }
    @Override
    public CmsExpert getCmsExpertById(Long id){
        return cmsExpertMapper.getCmsExpertById(id);
    }
}
