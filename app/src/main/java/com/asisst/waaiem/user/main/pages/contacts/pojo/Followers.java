package com.asisst.waaiem.user.main.pages.contacts.pojo;

public class Followers {
    private String user_id;
    private String post_owner_id;
    private Long timestamp;

    public Followers() {
    }

    public Followers(String user_id, String post_owner_id, Long timestamp) {
        this.user_id = user_id;
        this.post_owner_id = post_owner_id;
        this.timestamp = timestamp;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPost_owner_id() {
        return post_owner_id;
    }

    public void setPost_owner_id(String post_owner_id) {
        this.post_owner_id = post_owner_id;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
