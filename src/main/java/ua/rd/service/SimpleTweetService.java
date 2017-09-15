package ua.rd.service;

import ua.rd.domain.Tweet;
import ua.rd.repository.TweetRepository;

LazyTweetProxy{
        String bean;

        Tweet getInstance(){
            appContext.getBean(bean);
        }
        }

public class SimpleTweetService implements TweetService {

    private TweetRepository tweetRepository;
    private Tweet tweet = new LazyTweetProxy("tweet").getInstance();

    public SimpleTweetService(TweetRepository tweetRepository, Tweet tweet) {
        this.tweetRepository = tweetRepository;
        this.tweet = tweet;
    }

    public SimpleTweetService(TweetRepository tweetRepository) {
        this.tweetRepository = tweetRepository;
    }

    @Override
    public Iterable<Tweet> allTweats() {
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
