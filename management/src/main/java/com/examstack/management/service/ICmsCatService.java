package com.examstack.management.service;

import com.examstack.common.domain.rules.CmsCat;
import com.examstack.common.util.Page;

import java.util.List;

public interface ICmsCatService {
    /**
     * 添加分类
     * @param cmsCat
     */
    public void addCmsCat(CmsCat cmsCat);

    public List<CmsCat> getCmsCatList(Page<CmsCat> pageModel, String type);

    public void deleteCmsCatById(Integer id);

    public void updateCmsCat(CmsCat cmsCat);

    public CmsCat getCmsCatById(Integer id);
}
