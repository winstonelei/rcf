/**
 * 
 */
package com.github.rcf.core.server.process.serverFactory;


import com.github.rcf.core.server.process.AbstractRcfRpcTcpServerHandler;

/**
 * @author winstone
 *
 */
public class CommonRpcServerHandlerFactory {
	
	private static AbstractRcfRpcTcpServerHandler tcpServerHandler  = null;//= new AbstractRcfRpcTcpServerHandler();//AbstractRpcTcpServerHandler[1];
	
	///private static AbstractRpcHttpServerHandler httpServerHandler = new RpcHttpServerHandlerImpl();//AbstractRpcHttpServerHandler[2];
	

	public static AbstractRcfRpcTcpServerHandler getServerHandler(){
		return tcpServerHandler;//serverHandlers[0];
	}
	
	/*public static AbstractRpcHttpServerHandler getHttpServerHandler(){
		return  httpServerHandler;//httpserverHandlers[0];
	}*/
}
