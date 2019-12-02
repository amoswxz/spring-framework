//package org.springframework.context.support.test.ioc.impl;
//
//import org.springframework.beans.factory.DisposableBean;
//import org.springframework.beans.factory.FactoryBean;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.lang.reflect.InvocationHandler;
//import java.lang.reflect.Method;
//import java.lang.reflect.Proxy;
//
//@Service
//public class MyFactoryBean implements FactoryBean<Object>, InitializingBean, DisposableBean {
//
//    private String interfaceName = "org.springframework.context.support.test.ioc.impl.IMyFactoryBean";
//    @Autowired
//    private IMyFactoryBean iMyFactoryBean;
//    private Object proxyObj;
//
//    @Override
//    public void destroy() throws Exception {
//        System.out.println("destroy......");
//    }
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        proxyObj = Proxy.newProxyInstance(
//                this.getClass().getClassLoader(),
//                new Class[]{Class.forName(interfaceName)},
//                new InvocationHandler() {
//                    @Override
//                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//                        System.out.println("invoke method......" + method.getName());
//                        System.out.println("invoke method before......" + System.currentTimeMillis());
//                        Object result = method.invoke(iMyFactoryBean, args);
//                        System.out.println("invoke method after......" + System.currentTimeMillis());
//                        return result;
//                    }
//                });
//        System.out.println("afterPropertiesSet......");
//    }
//
//    @Override
//    public Object getObject() throws Exception {
//        System.out.println("getObject......");
//        return proxyObj;
//    }
//
//    @Override
//    public Class<?> getObjectType() {
//        return proxyObj == null ? Object.class : proxyObj.getClass();
//    }
//
//    @Override
//    public boolean isSingleton() {
//        return true;
//    }
//
//    public String getInterfaceName() {
//        return interfaceName;
//    }
//
//    public void setInterfaceName(String interfaceName) {
//        this.interfaceName = interfaceName;
//    }
//
//    public Object getProxyObj() {
//        return proxyObj;
//    }
//
//    public void setProxyObj(Object proxyObj) {
//        this.proxyObj = proxyObj;
//    }
//
//}