package ua.rd.ioc;

import java.util.*;

public class ApplicationContext implements Context {

    private List<BeanDefinition> beanDefinitions;
    private Map<String, Object> beans = new HashMap<>();

    public ApplicationContext(Config config) {
        beanDefinitions = Arrays.asList(config.beanDefinitions());
    }

    public ApplicationContext() {
        beanDefinitions = Config.EMPTY_BEANDEFINITION;//new BeanDefinition[0];
    }

    public Object getBean(String beanName) {
        BeanDefinition beanDefinition = getBeanDefinitionByName(beanName);
        Object bean = beans.get(beanName);

        if (bean == null) {
            bean = createNewBean(beanDefinition);
            if (!beanDefinition.isPrototype()) {
                beans.put(beanName, bean);
            }
        }
        return bean;
    }

    private Object createNewBean(BeanDefinition beanDefinition) {
        return createNewBeanInstance(beanDefinition);
    }

    private BeanDefinition getBeanDefinitionByName(String beanName) {
        return beanDefinitions.stream()
                .filter(bd -> Objects.equals(bd.getBeanName(), beanName))
                .findAny().orElseThrow(NoSuchBeanException::new);
    }

    public Object createNewBeanInstance(BeanDefinition bd) {
        try {
            return bd.getBeanType().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public String[] getBeanDefinitionNames() {
        return beanDefinitions.stream()
                .map(BeanDefinition::getBeanName)
                .toArray(String[]::new);
    }
}
