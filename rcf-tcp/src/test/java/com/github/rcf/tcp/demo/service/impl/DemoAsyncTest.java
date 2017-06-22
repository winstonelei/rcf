package com.github.rcf.tcp.demo.service.impl;

import com.github.rcf.core.async.AsyncCallObject;
import com.github.rcf.core.async.AsyncCallback;
import com.github.rcf.core.async.AsyncInvoker;
import com.github.rcf.tcp.demo.service.CostTime;
import com.github.rcf.tcp.demo.service.IDemoService;
import com.github.rcf.tcp.demo.service.Simple;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by winstone on 2017/6/22.
 */
public class DemoAsyncTest {

    public static void main(String[] args) {
        //ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:rpc-invoke-config-client.xml");

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("RcfRpcClient.xml");

        final IDemoService demoService = (IDemoService) context.getBean("demoServiceClient");

        long start = 0, end = 0;
        start = System.currentTimeMillis();

        AsyncInvoker invoker = new AsyncInvoker();

        CostTime elapse0 = invoker.submit(new AsyncCallback<CostTime>() {
            @Override
            public CostTime call() {
                return demoService.calculate();
            }
        });

        CostTime elapse1 = invoker.submit(new AsyncCallback<CostTime>() {
            @Override
            public CostTime call() {
                return demoService.calculate();
            }
        });

        Simple elapse2 = invoker.submit(new AsyncCallback<Simple>() {
            @Override
            public Simple call() {
                return demoService.getSimple(new Simple());
            }
        });

        System.out.println("1 async nettyrpc call:[" + "result:" + elapse0 + ", status:[" + ((AsyncCallObject) elapse0)._getStatus() + "]");
        System.out.println("2 async nettyrpc call:[" + "result:" + elapse1 + ", status:[" + ((AsyncCallObject) elapse1)._getStatus() + "]");
        System.out.println("3 async nettyrpc call:[" + "result:" + elapse2 + ", status:[" + ((AsyncCallObject) elapse2)._getStatus() + "]");

        end = System.currentTimeMillis();

        System.out.println("nettyrpc async calculate time:" + (end - start));

        context.destroy();
    }
}
