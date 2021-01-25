package com.examstack.management.persistence;

import com.examstack.common.domain.rules.CmsCat;
import com.examstack.common.util.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface CmsCatMapper {
    /**
     * 添加分类
     * @param cmsCat
     */
    public void addCmsCat(CmsCat cmsCat);

    public List<CmsCat> getCmsCatList(
            @Param("page") Page<CmsCat> page, @Param("type") String type);

    public void deleteCmsCatById(
            @Param("id") Integer id);

    public void updateCmsCat(CmsCat cmsCat);

    public CmsCat getCmsCatById(Integer id);
}
