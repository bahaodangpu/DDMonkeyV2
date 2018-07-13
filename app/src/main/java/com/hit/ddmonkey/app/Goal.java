package com.hit.ddmonkey.app;


public class Goal {
    private Integer id;
    private String name;
    private int time;
    private double money;

    public Goal(String name, int time, double money) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.money = money;
    }

    public Goal(){}

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }
}
