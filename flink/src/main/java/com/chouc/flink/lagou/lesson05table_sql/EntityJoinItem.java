package com.chouc.flink.lagou.lesson05table_sql;

import java.io.Serializable;

/**
 * @author chouc
 * @version V1.0
 * @Title: EntityJoinItem
 * @Package com.chouc.flink.lagou.lession05table_sql
 * @Description:
 * @date 2020/8/25
 */
public class EntityJoinItem implements Serializable {
    private static final long serialVersionUID = 1L;
    public Integer fId;
    public String fName;
    public Integer sId;
    public String sName;

    public EntityJoinItem() {
    }

    public EntityJoinItem(Integer fId, String fName, Integer sId, String sName) {
        this.fId = fId;
        this.fName = fName;
        this.sId = sId;
        this.sName = sName;
    }

    public Integer getfId() {
        return fId;
    }

    public void setfId(Integer fId) {
        this.fId = fId;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public Integer getsId() {
        return sId;
    }

    public void setsId(Integer sId) {
        this.sId = sId;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    @Override
    public String toString() {
        return "EntityJoinItem{" +
                "fId=" + fId +
                ", fName='" + fName + '\'' +
                ", sId=" + sId +
                ", sName='" + sName + '\'' +
                '}';
    }
}
