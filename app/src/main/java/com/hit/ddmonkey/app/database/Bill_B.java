package com.hit.ddmonkey.app.database;

import cn.bmob.v3.BmobObject;

public class Bill_B extends BmobObject {

    private Integer id;
    private String userId;
    private Integer goalid;
    private Integer categaryid;
    private String io;
    private Float money;
    private String category;
    private String note;
    private String date;
    private String dateid;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setGoalid(Integer goalid) {
        this.goalid = goalid;
    }

    public void setCategaryid(Integer categaryid) {
        this.categaryid = categaryid;
    }

    public void setIo(String io) {
        this.io = io;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDateid(String dateid) {
        this.dateid = dateid;
    }

    public Integer getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public Integer getGoalid() {
        return goalid;
    }

    public Integer getCategaryid() {
        return categaryid;
    }

    public String getIo() {
        return io;
    }

    public Float getMoney() {
        return money;
    }

    public String getCategory() {
        return category;
    }

    public String getNote() {
        return note;
    }

    public String getDate() {
        return date;
    }

    public String getDateid() {
        return dateid;
    }
}
