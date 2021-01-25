package com.examstack.common.domain.cms;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName : CmsNote  //类名
 * @Description : cms  //描述
 * @Author : Airman  //作者
 * @Date: 2020-09-19 22:57  //时间
 * @Version: 1.0
 */

public class CmsNote implements Serializable {

    private static final long serialVersionUID = -5809782578272943999L;
    private Long id;
    private Integer catId;
    private String noteName;
    private String noteContent;
    private Date createTime;
    private int creatorId;
    private String creator;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public Integer getCatId() {
        return catId;
    }

    public void setCatId(Integer catId) {
        this.catId = catId;
    }

    public String getNoteName() {
        return noteName;
    }

    public String getNoteContent() {
        return noteContent;
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

    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
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
