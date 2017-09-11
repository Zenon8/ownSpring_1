package ua.rd.ioc;

import java.util.Arrays;
import java.util.List;

public interface Config {

    List<BeanDefinition> EMPTY_BEANDEFINITION = Arrays.asList(new BeanDefinition[0]);

    BeanDefinition[] beanDefinitions();
}
