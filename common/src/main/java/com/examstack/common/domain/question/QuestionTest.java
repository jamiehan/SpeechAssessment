package com.examstack.common.domain.question;

import java.io.Serializable;
import java.util.Date;

public class QuestionTest implements Serializable {
    /**
	 * 
	 */
    private static final long serialVersionUID = -2219557393557077011L;
	private int id;
    private String name;
    private String point_ids;
    private Date createTime;
    private int creator;
    private String creatorName;
    private boolean privatee;
    private String memo;

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getPoint_ids() { return point_ids; }

    public void setPoint_ids(String point_ids) { this.point_ids = point_ids; }

    public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }

    public boolean isPrivatee() {
        return privatee;
    }

    public void setPrivatee(boolean privatee) {
        this.privatee = privatee;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
