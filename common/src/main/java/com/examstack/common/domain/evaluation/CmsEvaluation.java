package com.examstack.common.domain.evaluation;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName : CmsEvaluation  //类名
 * @Description : CmsEvaluation  //描述
 * @Author : Airman  //作者
 * @Date: 2020-09-19 22:57  //时间
 * @Version: 1.0
 */

public class CmsEvaluation implements Serializable {
    private static final long serialVersionUID = 7458914369159214366L;
    private Long id;
    private String title;
    private String content;
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
    public Date getCreateTime() {
        return createTime;
    }
    public int getCreatorId() {
        return creatorId;
    }
    public String getCreator() { return creator; }

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
