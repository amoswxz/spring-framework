package org.springframework.context.support;

import java.beans.PropertyDescriptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;

/**
 * @author: Pimow
 **/
 class TestIocImpl4 {


        private TestIocImpl3 testIocImpl3;

    public TestIocImpl4(TestIocImpl3 testIocImpl3) {
        this.testIocImpl3 = testIocImpl3;
    }

    //    public void setTestIocImpl3(TestIocImpl3 testIocImpl3) {
//        this.testIocImpl3 = testIocImpl3;
//    }
    //    @Override
//    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
//        System.out.println("看看");
//    }

//
//    @Override
//    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//        if("test".equals(beanName)){
//            TestIocImpl testIocImpl4= (TestIocImpl) bean;
//            testIocImpl4.setName("aa");
//        }
//        return bean;
//    }
//
//    @Override
//    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//        return bean;
//    }
}
