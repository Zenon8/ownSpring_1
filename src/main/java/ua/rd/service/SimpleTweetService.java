package ua.rd.service;

import ua.rd.domain.Tweet;
import ua.rd.ioc.Context;
import ua.rd.repository.TweetRepository;


public class SimpleTweetService implements TweetService {

    private TweetRepository tweetRepository;
    private Tweet tweet;

    public SimpleTweetService(TweetRepository tweetRepository, Tweet tweet) {
        this.tweetRepository = tweetRepository;
        this.tweet = tweet;
    }

    public SimpleTweetService(TweetRepository tweetRepository) {
        this.tweetRepository = tweetRepository;
    }

    @Override
    public Iterable<Tweet> allTweets() {
        return tweetRepository.allTweets();
    }

    @Override
    public TweetRepository getRepository() {
        return tweetRepository;
    }

    @Override
    public Tweet newTweet() {
        return tweet;
    }

    public void setTweet(Tweet tweet) {
        this.tweet = tweet;
    }
}

class PrototypeTweetProxy implements TweetService {

    private Context context;
    private TweetService tweetService;

    public PrototypeTweetProxy(TweetService tweetService, Context context) {
        this.context = context;
    }

    @Override
    public Iterable<Tweet> allTweets() {
        return tweetService.allTweets();
    }

    @Override
    public TweetRepository getRepository() {
        return tweetService.getRepository();
    }

    @Override
    public Tweet newTweet() {
        return (Tweet) context.getBean("tweet");
    }
}
