package ua.rd.service;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ua.rd.domain.Tweet;
import ua.rd.ioc.Benchmark;
import ua.rd.repository.TweetRepository;

@Service("tweetService")
@Scope
public class SimpleTweetService implements TweetService, ApplicationContextAware {

    private ApplicationContext context;
    private TweetRepository tweetRepository;
    private Tweet tweet;


    public SimpleTweetService(Tweet tweet) {
        this.tweet = tweet;
    }


    public SimpleTweetService(TweetRepository tweetRepository) {
        this.tweetRepository = tweetRepository;
    }

    @Autowired
    public SimpleTweetService(TweetRepository tweetRepository, Tweet tweet) {
        this.tweetRepository = tweetRepository;
        this.tweet = tweet;
    }

    public SimpleTweetService() {
    }

    public void fillTweet(Tweet tweet) {
        this.tweet = tweet;
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
    @Lookup
    @Benchmark
    //TODO С помощью BeanPostProcessor реализовать свою аннотацию @Benchmark.  Поместить аннотацию над методом newTweet().
    public Tweet newTweet() {

        return (Tweet) context.getBean("abc");
    }

    @Autowired
    @Required
    public void setTweet(Tweet tweet) {
        this.tweet = tweet;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}

/*
class PrototypeTweetServiceProxy implements TweetService {

    private Context context;
    private TweetService tweetService;

    public PrototypeTweetServiceProxy(TweetService tweetService, Context context) {
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
}*/
