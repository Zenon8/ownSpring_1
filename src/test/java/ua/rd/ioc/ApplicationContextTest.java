package ua.rd.ioc;

import org.junit.Ignore;
import org.junit.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class ApplicationContextTest {

    @Test(expected = NoSuchBeanException.class)
    public void getBeanWithEmptyContext() throws Exception {
        Context context = new ApplicationContext();
        context.getBean("abc");
    }

    @Test
    public void getBeanDefinitionNamesWithEmptyContext() throws Exception {
        //given
        Context context = new ApplicationContext();

        //when
        String[] actual = context.getBeanDefinitionNames();

        //tnen
        String[] expected = {};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void getBeanDefinitionNamesWithOneBeanDefinition() throws Exception {
        String beanName = "FirstBean";
        List<String> beanDescriptions = Arrays.asList(beanName);
        Config config = new JavaMapConfig(convertTestListToMap(beanDescriptions));
        Context context = new ApplicationContext(config);

        String[] actual = context.getBeanDefinitionNames();

        String[] expected = {beanName};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void getBeanDefinitionNamesWithSeveralBeanDefinitions() throws Exception {
        String beanName1 = "FirstBean";
        String beanName2 = "SecondBean";
        List<String> beanDescriptions = Arrays.asList(beanName1, beanName2);
        Config config = new JavaMapConfig(convertTestListToMap(beanDescriptions));
        Context context = new ApplicationContext(config);

        String[] actual = context.getBeanDefinitionNames();

        String[] expected = {beanName1, beanName2};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void getBeanDefinitionNamesWithEmptyBeanDefinition() throws Exception {
        List<String> beanDescriptions = Collections.emptyList();
        Config config = new JavaMapConfig(convertTestListToMap(beanDescriptions));
        Context context = new ApplicationContext(config);

        String[] actual = context.getBeanDefinitionNames();

        String[] expected = {};
        assertArrayEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getBeanWithOneBeanDefinitionWithoutType() throws Exception {
        String beanName = "FirstBean";
        List<String> beanDescriptions = Arrays.asList(beanName);
        Config config = new JavaMapConfig(convertTestListToMap(beanDescriptions));
        Context context = new ApplicationContext(config);

        Object bean = context.getBean(beanName);

        assertNotNull(bean);
    }

    @Test
    public void getBeanWithOneBeanDefinition() throws Exception {
        String beanName = "FirstBean";
        Class<TestBean> beanType = TestBean.class;
//        List<String> beanDescriptions = Arrays.asList(beanName);
        Map<String, Map<String, Object>> beanDescriptions =
                new HashMap<String, Map<String, Object>>() {{
                    put(beanName,
                            new HashMap<String, Object>() {{
                                put("type", beanType);
                            }});
                }};

        Config config = new JavaMapConfig(beanDescriptions);
        Context context = new ApplicationContext(config);

        TestBean bean = (TestBean) context.getBean(beanName);

        assertNotNull(bean);
    }

    private Map<String, Map<String, Object>> convertTestListToMap(List<String> beanDescriptionWithBeanNamesOnly) {
        return beanDescriptionWithBeanNamesOnly.stream()
                .collect(
                        Collectors.toMap(
                                Function.identity(),
                                beanName -> new HashMap<>()));
    }

    @Test
    public void getBeanIsSingleton() throws Exception {
        String beanName = "FirstBean";
        Class<TestBean> beanType = TestBean.class;

        Map<String, Map<String, Object>> beanDescriptions =
                new HashMap<String, Map<String, Object>>() {{
                    put(beanName,
                            new HashMap<String, Object>() {{
                                put("type", beanType);
                            }});
                }};

        Config config = new JavaMapConfig(beanDescriptions);
        Context context = new ApplicationContext(config);

        TestBean bean1 = (TestBean) context.getBean(beanName);
        TestBean bean2 = (TestBean) context.getBean(beanName);

        assertSame(bean1, bean2);
    }

    @Test
    public void getBeanNotSameInstancesWithSameType() throws Exception {
        String beanName1 = "FirstBean";
        String beanName2 = "SecondBean";
        Class<TestBean> beanType = TestBean.class;

        Map<String, Map<String, Object>> beanDescriptions =
                new HashMap<String, Map<String, Object>>() {{
                    put(beanName1,
                            new HashMap<String, Object>() {{
                                put("type", beanType);
                            }});
                    put(beanName2,
                            new HashMap<String, Object>() {{
                                put("type", beanType);
                            }});
                }};

        Config config = new JavaMapConfig(beanDescriptions);
        Context context = new ApplicationContext(config);

        TestBean bean1 = (TestBean) context.getBean(beanName1);
        TestBean bean2 = (TestBean) context.getBean(beanName2);

        assertNotSame(bean1, bean2);
    }

    @Test
    public void getBeanIsPrototype() throws Exception {
        String beanName1 = "FirstBean";
        Class<TestBean> beanType = TestBean.class;

        Map<String, Map<String, Object>> beanDescriptions =
                new HashMap<String, Map<String, Object>>() {{
                    put(beanName1,
                            new HashMap<String, Object>() {{
                                put("type", beanType);
                                put("isPrototype", true);
                            }});
                }};

        Config config = new JavaMapConfig(beanDescriptions);
        Context context = new ApplicationContext(config);

        TestBean bean1 = (TestBean) context.getBean(beanName1);
        TestBean bean2 = (TestBean) context.getBean(beanName1);

        assertNotSame(bean1, bean2);
    }
}