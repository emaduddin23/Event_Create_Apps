package edu.ewubd.event_managment;

public class Event {
    String key = "";
    String name = "";
    String place = "";
    String desc = "";
    String capacity = "";
    String budget = "";
    String email = "";
    String phone = "";
    String date = "";
    String type = "";

    public Event(String key, String name, String place, String desc, String capacity, String budget, String email, String Phone, String date, String type){
        this.key = key;
        this.name = name;
        this.place = place;
        this.desc = desc;
        this.capacity = capacity;
        this.budget = budget;
        this.email = email;
        this.phone = phone;
        this.date = date;
        this.type = type;
    }
}
