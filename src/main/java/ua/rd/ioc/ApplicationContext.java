package ua.rd.ioc;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

public class ApplicationContext implements Context {

    private List<BeanDefinition> beanDefinitions;
    private Map<String, Object> beans = new HashMap<>();

    public ApplicationContext() {
        beanDefinitions = Config.EMPTY_BEANDEFINITION;//new BeanDefinition[0];
    }

    public ApplicationContext(Config config) {
        beanDefinitions = Arrays.asList(config.beanDefinitions());
        initContext(beanDefinitions);
    }

    private void initContext(List<BeanDefinition> beanDefinitions) {
        beanDefinitions.forEach(bd -> getBean(bd.getBeanName()));
    }

    public Object getBean(String beanName) {
        BeanDefinition beanDefinition = getBeanDefinitionByName(beanName);
        Object bean = beans.get(beanName);
        if (bean != null) {
            return bean;
        }
        bean = createNewBean(beanDefinition);
        if (!beanDefinition.isPrototype()) {
            beans.put(beanName, bean);
        }
        return bean;
    }

    private Object createNewBean(BeanDefinition beanDefinition) {
        BeanBuilder beanBuilder = new BeanBuilder(beanDefinition);
        beanBuilder.createNewBeanInstance();
        beanBuilder.callPostCostructAnnotatedMethod();
        beanBuilder.callInitMethod();
        beanBuilder.createBenchmarkProxy();

        Object bean = beanBuilder.build();
        return bean;
    }

    private BeanDefinition getBeanDefinitionByName(String beanName) {
        return beanDefinitions.stream()
                .filter(bd -> Objects.equals(bd.getBeanName(), beanName))
                .findAny().orElseThrow(NoSuchBeanException::new);
    }

    public String[] getBeanDefinitionNames() {
        return beanDefinitions.stream()
                .map(BeanDefinition::getBeanName)
                .toArray(String[]::new);
    }




    class BeanBuilder {
        private BeanDefinition beanDefinition;
        private Object bean;

        public BeanBuilder(BeanDefinition beanDefinition) {
            this.beanDefinition = beanDefinition;
        }

        public void createNewBeanInstance() {
            Class<?> type = beanDefinition.getBeanType();
            Constructor<?> constructor = type.getDeclaredConstructors()[0];

            if (constructor.getParameterCount() == 0) {
                bean = createBeanWithDefaultConstructor(type);
            } else {
                bean = createBeanWithConstructorWithParams(type);
            }
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

        private Object createBeanWithConstructorWithParams(Class<?> type) {
            Constructor<?> constructor = type.getDeclaredConstructors()[0];
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            String[] paramBean = getBeanNameByType(parameterTypes);
            Object[] paramInstance = new Object[parameterTypes.length];

            for (int i = 0; i < paramBean.length; i++) {
                paramInstance[i] = getBean(paramBean[i]);
            }

            Constructor<?> currentConstructor = null;
            try {
                currentConstructor = type.getConstructor(parameterTypes);
                return currentConstructor.newInstance(paramInstance);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        private String[] getBeanNameByType(Class<?>[] parameterTypes) {
            return Arrays.stream(parameterTypes).map(Class::getSimpleName).map(simpleName -> simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1)).toArray(String[]::new);
        }

        private void callPostCostructAnnotatedMethod() {
            Method[] methods = bean.getClass().getDeclaredMethods();

            for (Method method : methods) {
                if (method.isAnnotationPresent(MyPostConstruct.class)) {
                    try {
                        method.invoke(bean);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        private void callInitMethod() {
            try {
                Method init = bean.getClass().getMethod("init");
                init.invoke(bean);
            } catch (NoSuchMethodException ignored) {
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        private void createBenchmarkProxy() {
            Class<?> beanClass = bean.getClass();
            Method[] methods = beanClass.getDeclaredMethods();

            for (Method methodName : methods) {
                Benchmark annotation = methodName.getAnnotation(Benchmark.class);

                if (annotation != null && annotation.enabled()) {
                    Object oldBean = bean;
                    bean = Proxy.newProxyInstance(
                            ClassLoader.getSystemClassLoader(),
                            beanClass.getInterfaces(),
                            (proxy, method, args) -> {
                                Long start = System.nanoTime();
                                Object proxyBean = method.invoke(oldBean, args);
                                Long end = System.nanoTime();
                                System.out.println(end - start + " ns\n");
                                return proxyBean;
                            }
                    );
                }
            }
        }

        public Object build() {
            return bean;
        }
    }
}
