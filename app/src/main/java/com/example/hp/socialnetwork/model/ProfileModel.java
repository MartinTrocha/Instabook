package com.example.hp.socialnetwork.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProfileModel implements Serializable {

    private String name;
    private String surname;
    private String info;
    private List<StatusModel> statuses=new ArrayList<>();


    public ProfileModel(String name, String surname, String info) {
        this.name = name;
        this.surname = surname;
        this.info = info;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StatusModel> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<StatusModel> statuses) {
        this.statuses = statuses;
    }

    public void setSurname(String surname) {
        this.surname = surname;

    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getInfo() {
        return info;
    }
}
