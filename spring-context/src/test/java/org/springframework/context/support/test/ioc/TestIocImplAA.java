package org.springframework.context.support.test.ioc;

import javax.annotation.PostConstruct;

/**
 * @author: Pimow
 **/
public class TestIocImplAA {



    @PostConstruct
    public void init(){
        System.out.println("postConstruct");
    }

    public void init1(){
        System.out.println("AAinit");
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
