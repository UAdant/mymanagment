package com.example.myapplicationmanagment;

public class ScheduleItem {
    private String name;
    private String dateTime;
    private String type;

    public ScheduleItem(String name, String dateTime, String type) {
        this.name = name;
        this.dateTime = dateTime;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getType() {
        return type;
    }
}
