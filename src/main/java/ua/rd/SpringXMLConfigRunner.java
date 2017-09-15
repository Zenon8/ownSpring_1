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

//        Arrays.stream(serviceContext.getBeanDefinitionNames()).forEach(s -> System.out.println(s));

//        TweetService tweetService = serviceContext.getBean("tweetService", TweetService.class);
//        System.out.println(tweetService.allTweats());
//
//        TweetRepository tweetRepository = serviceContext.getBean("tweetRepository", TweetRepository.class);
//        System.out.println(tweetRepository.allTweets());

        Tweet tweet1 = (Tweet) serviceContext.getBean("tweet1");
        System.out.println(tweet1);
        Tweet tweet2 = (Tweet) serviceContext.getBean("tweet2");
        System.out.println(tweet2);
        Tweet tweetParent = (Tweet) serviceContext.getBean("tweetParent");
        System.out.println(tweetParent);

    }
}
