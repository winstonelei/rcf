/**
 * 
 */
package com.github.rcf.tcp.demo.service.impl;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author winstone
 *
 */
public class ServerStart {
	
	public static void main(String[] args) {
		new ClassPathXmlApplicationContext("RcfRpcServer.xml");
}

}
