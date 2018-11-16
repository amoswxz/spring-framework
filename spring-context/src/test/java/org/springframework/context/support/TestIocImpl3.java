package org.springframework.context.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @author: Pimow
 **/
public class TestIocImpl3 {


    private TestIocImpl4 testIocImpl4;

    public TestIocImpl3(TestIocImpl4 testIocImpl4) {
        this.testIocImpl4 = testIocImpl4;
    }
}
