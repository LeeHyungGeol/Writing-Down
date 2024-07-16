package com.brothergeol;import java.lang.reflect.InvocationHandler;import java.lang.reflect.Method;import java.lang.reflect.Proxy;import org.junit.jupiter.api.Test;public class ProxyServiceTest {    SampleService bookService = (SampleService) Proxy.newProxyInstance(SampleService.class.getClassLoader(), new Class[]{SampleService.class},        new InvocationHandler() {            SampleService bookService = new DefaultSampleService();            @Override            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {                if (method.getName().equals("test")) {                    System.out.println("aaaa");                    Object invoke = method.invoke(bookService, args);                    System.out.println("bbbb");                    return invoke;                }                return method.invoke(bookService, args); }        });    @Test    void proxy_test() {        Sample sample = new Sample();        sample.setName("spring");        bookService.test(sample);    }}