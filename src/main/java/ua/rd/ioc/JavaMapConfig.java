package ua.rd.ioc;

import java.util.List;

public class JavaMapConfig implements Config {

    private List<String> beanDescriptions;

    public JavaMapConfig(List<String> beanDescriptions) {
        this.beanDescriptions = beanDescriptions;
    }

    private BeanDefinition beanDefinition(String name) {
        return new BeanDefinition() {
            @Override
            public String getBeanName() {
                return name;
            }

            @Override
            public Class<?> getBeanType() {
                return null;
            }
        };
    }

    @Override
    public BeanDefinition[] beanDefinitions() {
        BeanDefinition[] beanDefinitions =
                beanDescriptions.stream()
               .map(this::beanDefinition)
               .toArray(BeanDefinition[]::new);

        return beanDefinitions;
    }
}
