/**
 * 
 */
package com.github.rcf.core.server.process.impl;

import com.github.rcf.core.bean.RcfRequest;
import com.github.rcf.core.bean.RcfResponse;
import com.github.rcf.core.serializable.RcfCodes;
import com.github.rcf.core.server.process.AbstractRcfRpcTcpServerHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author winstone
 * 服务端处理请求类
 *
 */
public class RpcTcpServerHandlerImpl extends AbstractRcfRpcTcpServerHandler {
	
	private static final Log LOGGER = LogFactory.getLog(RpcTcpServerHandlerImpl.class);
	
	private static Map<String, RpcServerBean> processors = new HashMap<String, RpcServerBean>();
	
	private static Map<String, Method> cacheMethods = new HashMap<String, Method>();

	@Override
	public void registerProcessor(String instanceName, Object instance) {
		RpcServerBean filterServerBean=new RpcServerBean(instance);
		processors.put(instanceName, filterServerBean);
		Class<?> instanceClass = instance.getClass();
		Method[] methods = instanceClass.getDeclaredMethods();
		for (Method method : methods) {
			Class<?>[] argTypes = method.getParameterTypes();
			StringBuilder methodKeyBuilder = new StringBuilder();
			methodKeyBuilder.append(instanceName).append("#");
			methodKeyBuilder.append(method.getName()).append("$");
			for (Class<?> argClass : argTypes) {
				methodKeyBuilder.append(argClass.getName()).append("_");
			}
			cacheMethods.put(methodKeyBuilder.toString(), method);
		}
	}


	public RcfResponse handleRequestWithCallable(RcfRequest request,RcfResponse response) {
		response.setRequestId(request.getId());
		response.setCodecType(request.getCodecType());
		return this.getResonseWrapper(request,response);
	}


	@Override
	public RcfResponse handleRequest(RcfRequest request, int codecType) {
		RcfResponse responseWrapper = new RcfResponse(request.getId(),codecType);
		responseWrapper = getResonseWrapper(request, responseWrapper);
		return responseWrapper;
	}

	private RcfResponse getResonseWrapper(RcfRequest request, RcfResponse responseWrapper) {
		String targetInstanceName = new String(request.getTargetInstanceName());
		String methodName = new String(request.getMethodName());
		byte[][] argTypeBytes  = request.getArgTypes();
		String[] argTypes = new String[argTypeBytes.length];
		for(int i = 0; i <argTypeBytes.length; i++) {
		    argTypes[i] = new String(argTypeBytes[i]);
		}
		Object[] requestObjects = null;
		Method method = null;
		try{
			RpcServerBean rpcServerBean = processors.get(targetInstanceName);
			if(rpcServerBean == null){
				throw new Exception("no "+targetInstanceName+" instance exists on the server");
			}
			if (argTypes != null && argTypes.length > 0) {
				StringBuilder methodKeyBuilder = new StringBuilder();
				methodKeyBuilder.append(targetInstanceName).append("#");
				methodKeyBuilder.append(methodName).append("$");
				//Class<?>[] argTypeClasses = new Class<?>[argTypes.length];
				for (int i = 0; i < argTypes.length; i++) {
					methodKeyBuilder.append(argTypes[i]).append("_");
					//argTypeClasses[i] = Class.forName(argTypes[i]);
				}
				requestObjects = new Object[argTypes.length];
				method = cacheMethods.get(methodKeyBuilder.toString());
				if(method == null){
					throw new Exception("no method: "+methodKeyBuilder.toString()+" find in "+targetInstanceName+" on the server");
				}
				Object[] tmprequestObjects = request
						.getRequestObjects();
				for (int i = 0; i < tmprequestObjects.length; i++) {
					try{
						requestObjects[i] = RcfCodes.getDecoder(request.getCodecType()).decode(argTypes[i],(byte[])tmprequestObjects[i]);
					}
					catch(Exception e){
						throw new Exception("decode request object args error",e);
					}
				}
			} else {
				method = rpcServerBean.getObject().getClass().getMethod(methodName,
						new Class<?>[] {});
				if(method == null){
					throw new Exception("no method: "+methodName+" find in "+targetInstanceName+" on the server");
				}
				requestObjects = new Object[] {};
			}

			method.setAccessible(true);

			responseWrapper.setResponse(method.invoke(rpcServerBean.getObject(), requestObjects));

		}catch(Exception e){
			LOGGER.error("server handle request error",e);
			responseWrapper.setException(e);
		}
		return responseWrapper;
	}

	@Override
	public void clear() {
		processors.clear();
		cacheMethods.clear();
	}
	
	
	class RpcServerBean {
		
		private Object object;
		

		/**
		 * @return the object
		 */
		public Object getObject() {
			return object;
		}

		/**
		 * @param object the object to set
		 */
		public void setObject(Object object) {
			this.object = object;
		}


		public RpcServerBean(Object object) {
			super();
			this.object = object;
		}
		
		
	}
}
