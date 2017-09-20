package ua.rd;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.rd.domain.Tweet;
import ua.rd.domain.User;
import ua.rd.repository.TweetRepository;
import ua.rd.service.TweetService;

import java.util.Arrays;

public class SpringXMLConfigRunner {
    public static void main(String[] args) {

        ConfigurableApplicationContext repoContext = new ClassPathXmlApplicationContext("config/repoContext.xml");

        ConfigurableApplicationContext serviceContext = new ClassPathXmlApplicationContext(
                new String[] {"config/serviceContext.xml"}, repoContext);

        System.out.println("=============================================");
        System.out.println("All beans in Spring container: ");
        Arrays.stream(serviceContext.getBeanDefinitionNames()).forEach(System.out::println);

//        TweetService tweetService = serviceContext.getBean("tweetService", TweetService.class);
//        System.out.println(tweetService.allTweets());
//
//        TweetRepository tweetRepository = serviceContext.getBean("tweetRepository", TweetRepository.class);
//        System.out.println(tweetRepository.allTweets());

//        Tweet tweet1 = (Tweet) serviceContext.getBean("tweetService");
//        System.out.println(tweet1.toString());
        System.out.println("=============================================");
        System.out.println("All tweets located in tweetService: ");
        TweetService tweetService = (TweetService) serviceContext.getBean("tweetService");
        System.out.println(tweetService.allTweets());

        System.out.println("---------------------------------------------");
        System.out.println("Equal beans with prototype scope: ");
        System.out.println(tweetService.newTweet() == tweetService.newTweet());

        System.out.println("---------------------------------------------");
        System.out.println("Get bean marked own annotation 'MyTweet':");
        Tweet tweet = (Tweet) serviceContext.getBean("abc");
        System.out.println(tweet);
        System.out.println("=============================================");

        User user = serviceContext.getBean(User.class);
        System.out.println(user);

        System.out.println("=============================================");

        System.out.println(serviceContext.getBean("tweet"));
    }
}
