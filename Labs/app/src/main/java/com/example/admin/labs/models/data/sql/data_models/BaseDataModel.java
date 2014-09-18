package com.example.admin.labs.models.data.sql.data_models;

/**
 * Created by admin on 13.09.2014.
 */
public class BaseDataModel {
    protected long id;
    protected String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
