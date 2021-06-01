package com.chouc.flink.lagou.lesson05table_sql;

import java.io.Serializable;

/**
 * @author chouc
 * @version V1.0
 * @Title: EntityItem
 * @Package com.chouc.flink.lagou.lession05table_sql
 * @Description:
 * @date 2020/8/23
 */
public class EntityItem implements Serializable {
    private Integer id;
    private String name;

    public EntityItem() {
    }

    public EntityItem(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "EntityItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
