//package org.springframework.context.support.test.ioc.impl;
//
//import javax.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Service;
//
//@Service
//@Order(-2)
//public class A {
//
//    @Autowired
//    private B b;
//
//    public A() {
//        System.out.println("A construct");
//    }
//
//    @PostConstruct
//    public void init() {
//        initA();
//    }
//
//    private void initA() {
//        System.out.println("A init");
//    }
//}
//
