/**
 * 
 */
package com.github.rcf.core.server.process;


/**
 * @author winstone
 *
 */
public interface RpcServerHandler {
	
	/**
	 * 注册服务
	 * @param instanceName
	 * @param instance
	 */
	public void registerProcessor(String instanceName, Object instance);

	/**
	 * 清除
	 */
	public void clear();
}
