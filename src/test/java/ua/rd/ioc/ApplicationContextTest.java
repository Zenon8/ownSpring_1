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
    @Ignore
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
        Map<String, Map<String, Object>> beanDescriptions =
                new HashMap<String, Map<String, Object>>() {{
                    put(beanName,
                            new HashMap<String, Object>() {{
                                put("type", beanType);
                            }});
                }};

        Config config = new JavaMapConfig(beanDescriptions);
        Context context = new ApplicationContext(config);

        TestBeanInterface bean = (TestBeanInterface) context.getBean(beanName);

        assertNotNull(bean);
    }

    /*  Converter   */
    private Map<String, Map<String, Object>> convertTestListToMap(List<String> beanDescriptionWithBeanNamesOnly) {
        return beanDescriptionWithBeanNamesOnly.stream()
                .collect(
                        Collectors.toMap(
                                Function.identity(),
                                beanName -> new HashMap<String, Object>() {{
                                    put("type", TestBean.class);
                                }}));
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

        TestBeanInterface bean1 = (TestBeanInterface) context.getBean(beanName);
        TestBeanInterface bean2 = (TestBeanInterface) context.getBean(beanName);

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

        TestBeanInterface bean1 = (TestBeanInterface) context.getBean(beanName1);
        TestBeanInterface bean2 = (TestBeanInterface) context.getBean(beanName2);

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

        TestBeanInterface bean1 = (TestBeanInterface) context.getBean(beanName1);
        TestBeanInterface bean2 = (TestBeanInterface) context.getBean(beanName1);

        assertNotSame(bean1, bean2);
    }

    @Test
    public void getBeanWithDependedBeans() throws Exception {

        Map<String, Map<String, Object>> beanDescriptions =
                new HashMap<String, Map<String, Object>>() {{
                    put("testBeanInterface",
                            new HashMap<String, Object>() {{
                                put("type", TestBean.class);
                                put("isPrototype", false);
                            }});
                    put("testBeanWithConstructor",
                            new HashMap<String, Object>() {{
                                put("type", TestBeanWithConstructor.class);
                                put("isPrototype", false);
                            }});
                }};

        Config config = new JavaMapConfig(beanDescriptions);
        Context context = new ApplicationContext(config);

        TestBeanWithConstructor bean = (TestBeanWithConstructor) context.getBean("testBeanWithConstructor");

        assertNotNull(bean);
    }

    @Test
    public void getBeanWithDependedBeansWithTwoParam() throws Exception {
        Map<String, Map<String, Object>> beanDescriptions =
                new HashMap<String, Map<String, Object>>() {{
                    put("testBeanInterface",
                            new HashMap<String, Object>() {{
                                put("type", TestBean.class);
                                put("isPrototype", false);
                            }});
                    put("testBeanWithConstructorWithTwoParams",
                            new HashMap<String, Object>() {{
                                put("type", TestBeanWithConstructorWithTwoParams.class);
                                put("isPrototype", false);
                            }});
                }};

        Config config = new JavaMapConfig(beanDescriptions);
        Context context = new ApplicationContext(config);

        TestBeanWithConstructorWithTwoParams bean = (TestBeanWithConstructorWithTwoParams) context.getBean("testBeanWithConstructorWithTwoParams");

        assertNotNull(bean);
    }


    @Test
    public void getBeanCallInitMethod() throws Exception {
        Map<String, Map<String, Object>> beanDescriptions =
                new HashMap<String, Map<String, Object>>() {{
                    put("testBean",
                            new HashMap<String, Object>() {{
                                put("type", TestBean.class);
                                put("isPrototype", false);
                            }});
                }};

        Config config = new JavaMapConfig(beanDescriptions);
        Context context = new ApplicationContext(config);

        System.out.println("Test: getBeanCallInitMethod");
        TestBeanInterface bean = (TestBeanInterface) context.getBean("testBean");

        assertEquals("initialized", bean.getInitValue());
    }


    @Test
    public void getBeanCallPostConstruct() throws Exception {
        Map<String, Map<String, Object>> beanDescriptions =
                new HashMap<String, Map<String, Object>>() {{
                    put("testBean",
                            new HashMap<String, Object>() {{
                                put("type", TestBean.class);
                                put("isPrototype", false);
                            }});
                }};

        Config config = new JavaMapConfig(beanDescriptions);
        Context context = new ApplicationContext(config);

        System.out.println("Test: getBeanCallPostConstruct");
        TestBeanInterface bean = (TestBeanInterface) context.getBean("testBean");

        assertEquals("initByPostConstruct", bean.getPostConstructValue());
    }

    @Test
    public void getReverseString() throws Exception {
        Map<String, Map<String, Object>> beanDescriptions =
                new HashMap<String, Map<String, Object>>() {{
                    put("testBean",
                            new HashMap<String, Object>() {{
                                put("type", TestBean.class);
                                put("isPrototype", false);
                            }});
                }};

        Config config = new JavaMapConfig(beanDescriptions);
        Context context = new ApplicationContext(config);

        System.out.println("Test: getReverseString");
        TestBeanInterface bean = (TestBeanInterface) context.getBean("testBean");
        String action = bean.methodToBenchmark("init");

        assertEquals("tini", action);
    }


    /*@Test
    public void getBeanWithBenchmarkAnnotationMeasure() throws Exception {
        Map<String, Map<String, Object>> beanDescriptions =
                new HashMap<String, Map<String, Object>>() {{
                    put("testBean",
                            new HashMap<String, Object>() {{
                                put("type", TestBeanInterface.class);
                                put("isPrototype", false);
                            }});
                }};

        Config config = new JavaMapConfig(beanDescriptions);
        Context context = new ApplicationContext(config);

        TestBeanInterface bean = (TestBeanInterface) context.getBean("testBean");
        String action = bean.methodToBenchmark("init");

        assertEquals("tini", action);
    }*/


    static class TestBean implements TestBeanInterface {
        String initValue;
        String postConstructValue;

        public void init() {
            initValue = "initialized";
        }

        @MyPostConstruct
        public void postConstruct() {
            postConstructValue = "initByPostConstruct";
        }

        @Benchmark(enabled = false)
        public String methodToBenchmark(String str) {
            return new StringBuilder(str).reverse().toString();
        }

        @Override
        public String getInitValue() {
            return initValue;
        }

        @Override
        public String getPostConstructValue() {
            return postConstructValue;
        }
    }

    static class TestBeanWithConstructor {

        private final TestBeanInterface testBean1;

        public TestBeanWithConstructor(TestBeanInterface testBean1) {
            this.testBean1 = testBean1;
        }
    }

    static class TestBeanWithConstructorWithTwoParams {
        private final TestBeanInterface testBean1;
        private final TestBeanInterface testBean2;

        public TestBeanWithConstructorWithTwoParams(TestBeanInterface testBean1, TestBeanInterface testBean2) {
            this.testBean1 = testBean1;
            this.testBean2 = testBean2;
        }
    }

    interface TestBeanInterface {

        String methodToBenchmark(String str);

        String getInitValue();

        String getPostConstructValue();

        void init();

        void postConstruct();
    }
}