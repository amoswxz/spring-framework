package org.springframework.context.support.test.ioc.impl;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author: Pimow
 **/
@Component
public class TestIocImplB {

//    @Autowired
//    private TestIocImplA testIocImplA;
//
//    public TestIocImpl3(TestIocImpl4 testIocImpl4) {
//        this.testIocImpl4 = testIocImpl4;
//    }

    @Bean
    public TestIocImplE testIocImplE() {
        return new TestIocImplE();
    }

    public void test() {
        System.out.println("测试aop");
    }
}
