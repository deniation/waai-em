package com.asisst.waaiem.user.main.pages.messages.pojo;

public class Chats {
    private String type;
    private Boolean seen;
    private Long timestamp;
    private String from;
    private String to;

    public Chats() {
    }

    public Chats(String type, Boolean seen, Long timestamp, String from, String to) {
        this.type = type;
        this.seen = seen;
        this.timestamp = timestamp;
        this.from = from;
        this.to = to;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setFrom(String from) {
        this.from = from;
    }

}
