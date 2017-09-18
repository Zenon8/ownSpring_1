package ua.rd.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
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
}
