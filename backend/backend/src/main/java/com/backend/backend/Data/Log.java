package com.backend.backend.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;


@Entity
public class Log {

    @Id
    @GeneratedValue
    private UUID id;

    private String event;

    private LocalDateTime date;

    public Log(String event, LocalDateTime date) {
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
