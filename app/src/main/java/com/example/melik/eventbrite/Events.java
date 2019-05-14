package com.example.melik.eventbrite;

import android.media.Image;

import java.net.URL;

public class Events {
    private String name;
    private String description;
    private String start;
    private String end;
    private String startCalendar;
    private String endCalendar;
    private String Url;

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getStartCalendar() {
        return startCalendar;
    }

    public void setStartCalendar(String startCalendar) {
        this.startCalendar = startCalendar;
    }

    public String getEndCalendar() {
        return endCalendar;
    }

    public void setEndCalendar(String endCalendar) {
        this.endCalendar = endCalendar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
