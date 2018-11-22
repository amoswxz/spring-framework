package org.springframework.context.support.test;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: Pimow
 **/
@Service
public class TestIocImplA {


    @Autowired
    private TestIocImplB testIocImpl3;

}
