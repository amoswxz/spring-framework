//package org.springframework.context.support.test.ioc.impl;
//
//import javax.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.DependsOn;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Service;
//
//@Service
//@Order(1)
//public class B  {
//    @Autowired
//    private A a;
//
//    public B() {
//        System.out.println("B construct");
//    }
//
//    @PostConstruct
//    public void init() {
//        initB();
//    }
//
//    private void initB(){
//        System.out.println("B init");
//    }
//}