package ua.rd.service;

import ua.rd.domain.Tweet;

public interface TweetService {

    Iterable<Tweet> allTweats();
}
