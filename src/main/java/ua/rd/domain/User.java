package ua.rd.domain;

public class User {

    private Tweet tweet;
    private String name;

    public User(Tweet tweet) {
    }

    public User(String name) {
        this.name = name;
    }

    public void setTweet(Tweet tweet) {
        this.tweet = tweet;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                '}';
    }
}
