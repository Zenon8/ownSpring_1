package ua.rd.ioc;

import java.lang.reflect.Constructor;
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
        Class<?> type = bd.getBeanType();
        Constructor<?> constructor = type.getDeclaredConstructors()[0];

        Object newBean;
        if (constructor.getParameterCount() == 0) {
            newBean = createBeanWithDefaultConstructor(type);
        } else {
            newBean = createBeanWithConstructorWithParams(type);
        }
        return newBean;
    }

    private Object createBeanWithConstructorWithParams(Class<?> type) {
        Constructor<?> constructor = type.getDeclaredConstructors()[0];
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        return null;
    }

    private Object createBeanWithDefaultConstructor(Class<?> type) {
        Object newBean;
        try {
            newBean = type.newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
        return newBean;
    }

    public String[] getBeanDefinitionNames() {
        return beanDefinitions.stream()
                .map(BeanDefinition::getBeanName)
                .toArray(String[]::new);
    }
}
