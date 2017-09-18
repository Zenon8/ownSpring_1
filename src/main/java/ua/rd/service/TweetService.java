package ua.rd.service;

import ua.rd.domain.Tweet;
import ua.rd.repository.TweetRepository;

public interface TweetService {

    Iterable<Tweet> allTweets();

    TweetRepository getRepository();

    Tweet newTweet();
}
