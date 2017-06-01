/**
 * 
 */
package com.github.rcf.core.server.process.handlerFactory;


import com.github.rcf.core.server.process.AbstractRcfRpcHttpServerHandler;
import com.github.rcf.core.server.process.AbstractRcfRpcTcpServerHandler;
import com.github.rcf.core.server.process.impl.RpcHttpServerHandlerImpl;
import com.github.rcf.core.server.process.impl.RpcTcpServerHandlerImpl;

/**
 * @author winstone
 *
 */
public class RcfRpcServerHandlerFactory {
	
	private static AbstractRcfRpcTcpServerHandler tcpServerHandler  = new RpcTcpServerHandlerImpl();
	
    private static AbstractRcfRpcHttpServerHandler httpServerHandler = new RpcHttpServerHandlerImpl();
	

	public static AbstractRcfRpcTcpServerHandler getTcpServerHandler(){
		return tcpServerHandler;
	}
	
	public static AbstractRcfRpcHttpServerHandler getHttpServerHandler(){
		return  httpServerHandler;
	}
}
