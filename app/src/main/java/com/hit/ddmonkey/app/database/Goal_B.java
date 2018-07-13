package com.hit.ddmonkey.app.database;

import cn.bmob.v3.BmobObject;

public class Goal_B extends BmobObject {
    private Integer id;
    private String userid;
    private String goalname;
    private int time;
    private String date;
    private double money;

    public String getUserid() {
        return userid;
    }

    public String getGoalname() {
        return goalname;
    }

    public String getDate() {
        return date;
    }

    public Integer getId() {
        return id;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setGoalname(String goalname) {
        this.goalname = goalname;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTime() {
        return time;
    }

    public double getMoney() {
        return money;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public void setTime(int time) {
        this.time = time;
    }

    public void setMoney(double money) {
        this.money = money;
    }


}
