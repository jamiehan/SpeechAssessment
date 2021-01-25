package com.examstack.common.domain.rules;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName : CmsRules  //类名
 * @Description : rules  //描述
 * @Author : Airman  //作者
 * @Date: 2020-09-19 22:57  //时间
 * @Version: 1.0
 */

public class CmsRules implements Serializable {
    private static final long serialVersionUID = 6937259251186631668L;
    private Long id;
    private String title;
    private String content;
    private Integer catId;
    private String catName;
    private Date createTime;
    private int creatorId;
    private String creator;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Integer getCatId() {
        return catId;
    }

    public void setCatId(Integer catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public String getCreator() {
        return creator;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
