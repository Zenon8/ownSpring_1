package ua.rd.ioc;

import java.lang.reflect.*;
import java.util.*;

public class ApplicationContext implements Context {

    private List<BeanDefinition> beanDefinitions;
    private Map<String, Object> beans = new HashMap<>();

    public ApplicationContext(Config config) {
        beanDefinitions = Arrays.asList(config.beanDefinitions());
        initContext(beanDefinitions);
    }

    private void initContext(List<BeanDefinition> beanDefinitions) {
        beanDefinitions.forEach(bd -> getBean(bd.getBeanName()));
    }

    public ApplicationContext() {
        beanDefinitions = Config.EMPTY_BEANDEFINITION;//new BeanDefinition[0];
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
        Object bean = createNewBeanInstance(beanDefinition);
        callInitMethod(bean);
        bean = processAnnotation(bean);
        return bean;
    }

    private Object processAnnotation(Object bean) {
        Method[] methods = bean.getClass().getMethods();
        for (Method method : methods) {
            callPostCostructAnnotation(method, bean);
            callBenchmarkAnnotation(method, bean);
        }
        return bean;
    }

    private void callPostCostructAnnotation(Method method, Object bean) {
        if (method.isAnnotationPresent(MyPostConstruct.class)) {
            try {
                method.invoke(bean);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Object callBenchmarkAnnotation(Method method, Object bean) {
        Object proxyBean = bean;

        if (method.isAnnotationPresent(Benchmark.class)) {
           proxyBean = Proxy.newProxyInstance(
                   bean.getClass().getClassLoader(),
                   new Class[]{TestBeanInterface.class},
                   new InvocationHandler() {
                       @Override
                       public Object invoke(Object proxy, Method methodObj, Object[] args) throws Throwable {
                           System.out.println(methodObj.getName());
                           return methodObj.invoke(bean, args);
                       }
                   }
           );
        }
        return proxyBean;
    }

    private void callInitMethod(Object bean) {
        try {
            Method init = bean.getClass().getMethod("init");
            init.invoke(bean);
        } catch (NoSuchMethodException ignored) {
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
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
        String[] paramNames = getParamNamesFromParameterTypes(parameterTypes);
        Object[] paramInstance = Arrays.stream(paramNames).map(this::getBean).toArray();

        Constructor<?> currentConstructor = null;

        try {
            currentConstructor = type.getConstructor(parameterTypes);
            return currentConstructor.newInstance(paramInstance);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private String[] getParamNamesFromParameterTypes(Class<?>[] parameterTypes) {
        return Arrays.stream(parameterTypes).map(Class::getSimpleName).map(simpleName -> simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1)).toArray(String[]::new);
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
