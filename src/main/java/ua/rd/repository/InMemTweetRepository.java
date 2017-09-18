package ua.rd.repository;

import ua.rd.domain.Tweet;
import ua.rd.ioc.Benchmark;

import java.util.Arrays;
import java.util.List;

public class InMemTweetRepository implements TweetRepository {

    private List<Tweet> tweets;

    {
        tweets = Arrays.asList(
                new Tweet(),
                new Tweet()
//                new Tweet(1L, "First Mesg", null),
//                new Tweet(2L, "Second Mesg", null)
        );
    }

    public void init() {}

    @Override
    @Benchmark
    public Iterable<Tweet> allTweets() {
        return tweets;
    }

    public void destroy() {

    }
}
