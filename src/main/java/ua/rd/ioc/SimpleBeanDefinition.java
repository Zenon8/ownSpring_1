package ua.rd.ioc;

public class SimpleBeanDefinition implements BeanDefinition {

    private final String beanName;
    private final Class<?> beanType;
    private final boolean isProtoType;

    public SimpleBeanDefinition(String beanName, Class<?> beanType, boolean isProtoType) {
        this.beanName = beanName;
        this.beanType = beanType;
        this.isProtoType = isProtoType;
    }

    public String getBeanName() {
        return beanName;
    }


    public Class<?> getBeanType() {
        return beanType;
    }


    public boolean isPrototype() {
        return isProtoType;
    }
}
