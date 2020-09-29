package com.asisst.waaiem.user.main.pages.profile.pojo;

public class Comments {
    private String comment;
    private String post_id;
    private String comment_id;
    private String user_id;
    private Long timestamp;

    public Comments() {
    }

    public Comments(String comment, String post_id, String comment_id, String user_id, Long timestamp) {
        this.comment = comment;
        this.post_id = post_id;
        this.comment_id = comment_id;
        this.user_id = user_id;
        this.timestamp = timestamp;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
