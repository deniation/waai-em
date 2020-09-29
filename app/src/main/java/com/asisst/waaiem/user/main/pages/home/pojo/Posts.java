package com.asisst.waaiem.user.main.pages.home.pojo;

public class Posts {
   private String user_id;
   private String post_id;
   private String content;
   private String caption;
   private Long timestamp;
   private String hashtag;

    public Posts() {
    }

    public Posts(String user_id, String post_id, String content, String caption, Long timestamp, String hashtag) {
        this.user_id = user_id;
        this.post_id = post_id;
        this.content = content;
        this.caption = caption;
        this.timestamp = timestamp;
        this.hashtag = hashtag;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }
}
