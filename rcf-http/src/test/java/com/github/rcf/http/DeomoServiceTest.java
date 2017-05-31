package com.github.rcf.http; /**
 * 
 */

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author winstone
 *
 */
public class DeomoServiceTest {
	
	public static void main(String[] args) {
		new ClassPathXmlApplicationContext("CommonRpcHttpServer.xml");
	}
}
