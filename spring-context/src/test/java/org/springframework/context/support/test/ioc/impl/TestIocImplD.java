package org.springframework.context.support.test.ioc.impl;

import org.springframework.context.support.test.ioc.TestIocD;
import org.springframework.stereotype.Service;

/**
 * @author: Pimow
 **/
@Service
public class TestIocImplD implements TestIocD {


    @Override
    public void test() {
        System.out.println("测试aop");
    }

}
