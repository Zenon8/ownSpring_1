package ua.rd;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.rd.domain.Tweet;
import ua.rd.repository.TweetRepository;
import ua.rd.service.TweetService;

import java.util.Arrays;

public class SpringXMLConfigRunner {
    public static void main(String[] args) {

        ConfigurableApplicationContext repoContext = new ClassPathXmlApplicationContext("config/repoContext.xml");

        ConfigurableApplicationContext serviceContext = new ClassPathXmlApplicationContext(
                new String[] {"config/serviceContext.xml"}, repoContext);


        Arrays.stream(serviceContext.getBeanDefinitionNames()).forEach(System.out::println);

//        TweetService tweetService = serviceContext.getBean("tweetService", TweetService.class);
//        System.out.println(tweetService.allTweets());
//
//        TweetRepository tweetRepository = serviceContext.getBean("tweetRepository", TweetRepository.class);
//        System.out.println(tweetRepository.allTweets());

//        Tweet tweet1 = (Tweet) serviceContext.getBean("tweetService");
//        System.out.println(tweet1.toString());
        TweetService tweetService = (TweetService) serviceContext.getBean("tweetService");
        System.out.println(tweetService.allTweets());
        System.out.println(tweetService.newTweet() == tweetService.newTweet());

        Tweet tweet = (Tweet) serviceContext.getBean("tweet");
        System.out.println(tweet);
        System.out.println(tweetService.getClass());

    }
}
