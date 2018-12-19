package org.springframework.context.support.test.ioc.impl;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: Pimow
 **/
@Component
@Transactional
public class TestIocImplA {

    public void test() {
        System.out.println("A");
    }


}
