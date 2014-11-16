package com.example.rimolma1.buyme;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by RIMOLMA1 on 10/31/2014.
 */
public class Alarm implements Serializable {

    private Long id;
    private String description;
    private String location; // this has to be an object
    private User creator;
    private Date dueDate;
    private Date from;
    private Date to;

    public Alarm() {
    }

    public Alarm(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }
}
