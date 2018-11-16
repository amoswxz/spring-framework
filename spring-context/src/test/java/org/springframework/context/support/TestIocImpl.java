package org.springframework.context.support;

import jdk.nashorn.internal.objects.annotations.Setter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author: Pimow
 **/
public class TestIocImpl implements TestIoc  {


    private String name;
    private TestIoc1 testIoc1;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTestIoc1(TestIoc1 testIoc1) {
        this.testIoc1 = testIoc1;
    }



}
