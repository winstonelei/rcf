package com.github.rcf.tcp.demo.service.impl;

import com.github.rcf.core.compiler.AccessAdaptive;
import com.google.common.io.CharStreams;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;

import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Created by winstone on 2017/6/21.
 */
public class DemoClientAccess {

    public static void main(String[] args){

        try{
            DefaultResourceLoader resourceLoader = new DefaultResourceLoader();

            Reader input = new InputStreamReader(resourceLoader.getResource("AccessProvider.tpl").getInputStream(),"UTF-8");

            String javaSource = CharStreams.toString(input);

            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("RcfRpcClient.xml");
            AccessAdaptive provider = (AccessAdaptive) context.getBean("access");

            String result = (String) provider.invoke(javaSource, "getRpcServerTime", new Object[]{new String("winstone")});
            System.out.println(result);


            provider.invoke(javaSource, "sayHello", new Object[0]);

            input.close();
            context.destroy();
        }catch (Exception e){
          e.printStackTrace();
        }
        finally {

        }

    }
}
