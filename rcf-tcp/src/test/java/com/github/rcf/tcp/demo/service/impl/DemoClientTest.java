/**
 * 
 */
package com.github.rcf.tcp.demo.service.impl;

import com.github.rcf.tcp.demo.service.IDemoService;
import com.github.rcf.tcp.demo.service.Simple;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
import java.util.Map;

public class DemoClientTest {
	public static void main(String[] args) throws Exception {
		testRpc();
	}

	public static void testRpc() throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"RcfRpcClient.xml");
		final IDemoService demoService = (IDemoService) context
				.getBean("demoServiceClient");

		Map<String,Integer> map = new HashMap();
		map.put("tt1",1);
		Simple sim = demoService.getSimple(new Simple("zhang",2,map));
		System.out.println("单线程返回值====="+sim.getAge());


		System.out.println(demoService.getParam("demo_"));
	}
}
