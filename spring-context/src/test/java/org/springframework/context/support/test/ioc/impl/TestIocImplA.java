package org.springframework.context.support.test.ioc.impl;

import javax.annotation.PostConstruct;

/**
 * @author: Pimow
 **/
public class TestIocImplA {



    private TestIocImplB testIocImplB;

    public void setTestIocImplB(TestIocImplB testIocImplB) {
        this.testIocImplB = testIocImplB;
    }

    @PostConstruct
    public void init(){
        System.out.println("postConstruct");
    }

    public void init1(){
        System.out.println("init");
    }

//    private TestIocImplD testIocImplD;
//
//    public void setTestIocImplD(TestIocImplD testIocImplD) {
//        this.testIocImplD = testIocImplD;
//    }

    public void test() {
        System.out.println("A");
    }


}
