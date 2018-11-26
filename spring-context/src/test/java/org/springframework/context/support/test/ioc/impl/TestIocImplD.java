package org.springframework.context.support.test.ioc.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: Pimow
 **/
//@Service
public class TestIocImplD  {


    @Autowired
    private  TestIocImplB testIocImplB;

    public void test() {
    }

}
