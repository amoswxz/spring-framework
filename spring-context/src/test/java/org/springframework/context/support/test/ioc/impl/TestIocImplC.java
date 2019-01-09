package org.springframework.context.support.test.ioc.impl;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: Pimow
 **/
@Component
public class TestIocImplC {


    public void testTranc() {
        System.out.println("这个是测试事务的");
    }

}
