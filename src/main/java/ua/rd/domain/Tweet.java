package ua.rd.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;


@ToString
@MyTweet(value = "abc")
public class Tweet {

    private Long tweetId;
    private String txt;
    private User user;

    public Tweet() {
    }

    public Tweet(String txt, User user) {
        this.txt = txt;
        this.user = user;
    }

    public Tweet(Long tweetId, String txt, User user) {
        this.tweetId = tweetId;
        this.txt = txt;
        this.user = user;
    }

    public void init() {
        user.setTweet(this);
    }


    public Long getTweetId() {
        return tweetId;
    }

    public void setTweetId(Long tweetId) {
        this.tweetId = tweetId;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
