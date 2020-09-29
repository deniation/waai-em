package com.asisst.waaiem.user.main.pages.pojo;

public class Notifications {
    private String post_owner_id;
    private String user_id;
    private String post_id;
    private String type;
    private Long timestamp;
    private Boolean seen;

    public Notifications() {
    }

    public Notifications(String post_owner_id, String user_id, String post_id, String type, Long timestamp, Boolean seen) {
        this.post_owner_id = post_owner_id;
        this.user_id = user_id;
        this.post_id = post_id;
        this.type = type;
        this.timestamp = timestamp;
        this.seen = seen;
    }

    public String getPost_owner_id() {
        return post_owner_id;
    }

    public void setPost_owner_id(String post_owner_id) {
        this.post_owner_id = post_owner_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }
}
