package ua.rd.service;

import ua.rd.domain.Tweet;
import ua.rd.repository.TweetRepository;

public class SimpleTweetService implements TweetService {

    private TweetRepository tweetRepository;

    public SimpleTweetService(TweetRepository tweetRepository) {
        this.tweetRepository = tweetRepository;
    }

    @Override
    public Iterable<Tweet> allTweats() {
        return tweetRepository.allTweets();
    }
}
