package com.examstack.common.domain.rules;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName : CmsCat  //类名
 * @Description : Cat  //分类描述
 * @Author : Jamie  //作者
 * @Date: 2020-10-03 22:26  //时间
 * @Version: 1.0
 */

public class CmsCat implements Serializable {
    private static final long serialVersionUID = 6937259251186631668L;
    private Integer id;
    private Integer type;
    private String catName;
    private Integer creatorId;
    private Date createTime;
    private String creator;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

}
