package org.springframework.context.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * @author: Pimow
 **/
public class TestIocImpl1 implements TestIoc1, BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
//        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) beanFactory;
//        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(TestIocImpl4.class);
//        defaultListableBeanFactory.registerBeanDefinition("test4", builder.getBeanDefinition());
        System.out.println(10000011);
    }
}
