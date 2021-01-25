package com.examstack.management.service;

import com.examstack.common.domain.rules.CmsCat;
import com.examstack.common.util.Page;
import com.examstack.management.persistence.CmsCatMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName : CmsCatServiceImpl  //类名
 * @Description : cmsCat  //描述
 * @Author : Airman  //作者
 * @Date: 2020-09-19 23:20  //时间
 * @Version: 1.0
 */
@Service("cmsCatService")
public class CmsCatServiceImpl implements ICmsCatService{

    @Autowired
    private CmsCatMapper cmsCatMapper;

    public void addCmsCat(CmsCat cmsCat){
        cmsCatMapper.addCmsCat(cmsCat);
    }

    @Override
    public List<CmsCat> getCmsCatList(Page<CmsCat> pageModel,String type) {
        // TODO Auto-generated method stub
        return cmsCatMapper.getCmsCatList(pageModel,type);
    }

    @Override
    public void deleteCmsCatById(Integer id) {
        // TODO Auto-generated method stub
        cmsCatMapper.deleteCmsCatById(id);
    }

    @Override
    public void updateCmsCat(CmsCat cmsCat) {
        // TODO Auto-generated method stub
        cmsCatMapper.updateCmsCat(cmsCat);
    }
    @Override
    public CmsCat getCmsCatById(Integer id){
        return cmsCatMapper.getCmsCatById(id);
    }
}
