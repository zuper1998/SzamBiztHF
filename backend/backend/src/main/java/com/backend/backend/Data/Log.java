package com.backend.backend.Data;

import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;


@Entity
public class Log {

    @Id
    @GeneratedValue
    @Type(type="uuid-char")
    private UUID id;

    private String event;

    private String date;

    public Log(String event, String date) {
        this.event = event;
        this.date = date;
    }
    public Log() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEvent() {return this.event;}
    public void  setEvent(String event) {this.event = event;}

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
