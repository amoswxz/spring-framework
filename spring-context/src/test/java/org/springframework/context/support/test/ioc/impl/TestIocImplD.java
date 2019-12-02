package org.springframework.context.support.test.ioc.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author: Pimow
 **/
@Service
@Scope("prototype")
public class TestIocImplD {

//    @Autowired
//    private ITestIocImplA testIocImplAA;

    @PostConstruct
    public void poc(){
        System.out.println("PostConstruct");
    }

    public void test() {
        System.out.println("D");
    }

}
