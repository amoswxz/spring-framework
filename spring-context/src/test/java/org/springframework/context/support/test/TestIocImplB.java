package org.springframework.context.support.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: Pimow
 **/
@Service
public class TestIocImplB {


    @Autowired
    private TestIocImplA testIocImplA;
//
//    public TestIocImpl3(TestIocImpl4 testIocImpl4) {
//        this.testIocImpl4 = testIocImpl4;
//    }
}
