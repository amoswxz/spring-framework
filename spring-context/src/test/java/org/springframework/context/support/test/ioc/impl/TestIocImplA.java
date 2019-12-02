package org.springframework.context.support.test.ioc.impl;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;

/**
 * @author: Pimow
 **/
@Service
public class TestIocImplA implements ITestIocImplA {

//    @Autowired
//    private TestIocImplB testIocImplB;
//
////    private TestIocImplD testIocImplD;
////
////    public void setTestIocImplD(TestIocImplD testIocImplD) {
////        this.testIocImplD = testIocImplD;
////    }
//
//    public void test() {
//        testIocImplB.test();
//    }


    @Override
    public void test() {
        System.out.println("testIocImplA");
    }
}
