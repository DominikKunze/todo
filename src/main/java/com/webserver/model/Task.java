package com.webserver.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    protected String title;
    protected String inhalt;
    protected int status;

    private Task(){

    }

    public Task(String title, String inhalt, int status){
        this.title = title;
        this.inhalt = inhalt;
        this.status = status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public int getStatus() {
        return status;
    }

    public String getInhalt() {
        return inhalt;
    }

    public String getTitle() {
        return title;
    }

    public void setInhalt(String inhalt) {
        this.inhalt = inhalt;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Todo {id: "+id+", title: "+title+", inhalt: "+inhalt+", status: "+status+"}";
    }
}
