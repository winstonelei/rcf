package com.github.rcf.tcp.demo.service.impl;

import com.github.rcf.tcp.demo.service.IDemoService;
import com.github.rcf.tcp.demo.service.Simple;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;


/**
 * Created by winstone on 2017/6/1.
 */
public class DemoClientMultiThread {


    public static void main(String[] args) throws Exception {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                "RcfRpcClient.xml");
        final IDemoService demoService = (IDemoService) context
                .getBean("demoServiceClient");

        long time1 = System.currentTimeMillis();

        int threadCount = 50;

        final CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        for(int j=0;j<=threadCount;j++){
            new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            int i =0;
                            i++;
                            Map<String,Integer> map = new HashMap();
                            map.put("tt"+i,i);
                            Simple sim = demoService.getSimple(new Simple("zhang"+i,(i+1),map));
                            System.out.println("得到的返回值是======>>>>"+sim.getAge()+"=名称="+sim.getName());
                            countDownLatch.countDown();
                        }
                    }
            ).start();

        }

        countDownLatch.await();
        long end1 = System.currentTimeMillis();
        System.out.println("完成时间:" + (end1 - time1) + ",平均时间："
                + ((double) (end1 - time1) / (double) (threadCount)));


    }
}
