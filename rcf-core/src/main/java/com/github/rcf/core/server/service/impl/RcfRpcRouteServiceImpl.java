/**
 * 
 */
package com.github.rcf.core.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.rcf.core.route.RpcServiceRouteMessage;
import com.github.rcf.core.server.service.IRcfRpcRouteService;
import com.github.rcf.core.util.ClassPoolUtils;
import com.github.rcf.core.util.StringUtils;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author winstone
 *
 */
public class RcfRpcRouteServiceImpl implements IRcfRpcRouteService {
	
	public static final int TYPE = 0;
	
	private static List<RpcServiceRouteMessage> RpcServiceRouteMessages=new ArrayList<RpcServiceRouteMessage>();
	
	@Override
	public RpcServiceRouteMessage isRouteInfos(String route, String methodType,Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		RpcServiceRouteMessage rpcRoute=null;
		for(RpcServiceRouteMessage RpcServiceRouteMessage:RpcServiceRouteMessages){
			if(RpcServiceRouteMessage.getRoute().equals(route)&&RpcServiceRouteMessage.getHttpType().equals(methodType)){
				RpcServiceRouteMessage.setParams(params);
				rpcRoute=RpcServiceRouteMessage;
				break;
			}
		}
		return rpcRoute;
	}

	@Override
	public Object methodInvoke(RpcServiceRouteMessage routeInfo) throws Exception {
		// TODO Auto-generated method stub
		Method[] mds = routeInfo.getMethods();
		for(Method method:mds){
			if (routeInfo.getMethod().equals(method.getName())) {
				Class<?>[] mdTypes = method.getParameterTypes();
				
				Object[] objs = new Object[mdTypes.length];
				String[] paramNames = ClassPoolUtils.getMethodVariableName(routeInfo.getObjCls().getName(), routeInfo.getMethod());
				for (int j = 0; j < mdTypes.length; j++) {
					
					if (mdTypes[j].isAssignableFrom(Map.class)) {//hashmap 
						objs[j] = routeInfo.getParams();
					}else if (mdTypes[j].isArray()) {//数组
						Map<String, Object> params = routeInfo.getParams();
						Object[] object = new Object[params.keySet()
								.size()];
						int k = 0;
						for (String key : params.keySet()) {
							object[k] = params.get(key);
							k++;
						}
						objs[j] = object;
					} else if (Collection.class.isAssignableFrom(mdTypes[j])) {//list 类型
						Map<String, Object> params = routeInfo
								.getParams();
						List<Object> result = new LinkedList<Object>();
						for (String key : params.keySet()) {
							result.add(params.get(key));
						}
						objs[j] = result;
					}else if (mdTypes[j].isPrimitive()
							|| mdTypes[j].getName().startsWith("java.")) {// java 私有类型
						Map<String, Object> params = routeInfo.getParams();
						
						if(!StringUtils.isNullOrEmpty(paramNames)&&paramNames[j]!=null){
							if(params.containsKey(paramNames[j])){
								objs[j] = params.get(paramNames[j]);
							}
						}
						
					} else {// javabean
						objs[j] = mdTypes[j].newInstance();
						String json = JSONObject.toJSONString(routeInfo.getParams());
						objs[j] = JSONObject.parseObject(json, objs[j].getClass());
					}
				}//参数循环结束
				
				method.setAccessible(true);
				
				Object object=routeInfo.getObjCls().newInstance();

				Object result=getResult(method, object, objs);
					
				return result;
			}
		}
		return null;
	}
	
	private Object getResult(Method method,Object object,Object... args) throws Exception{
//		if (args.length > 0) {
//			return method.invoke(object, args);// 执行
//		} else {
//			return method.invoke(object, null);
//		}
		return method.invoke(object, args);// 执行
	}


	@Override
	public void registerProcessor(String projectname, Object instance,String httpType,String returnType) {
		Method[] methods = instance.getClass().getDeclaredMethods();//自定义方法
		for(Method method:methods){
			RpcServiceRouteMessage RpcServiceRouteMessage=new RpcServiceRouteMessage();
			RpcServiceRouteMessage.setObjCls(instance.getClass());
			String simplename=instance.getClass().getSimpleName();
			simplename= simplename.substring(0, 1).toLowerCase()+ simplename.substring(1);
			
			RpcServiceRouteMessage.setHttpType(httpType);
			RpcServiceRouteMessage.setReturnType(returnType);
			RpcServiceRouteMessage.setMethods(methods);
			RpcServiceRouteMessage.setMethod(method.getName());
			RpcServiceRouteMessage.setReturnType(returnType);
			String route=projectname+"/"+simplename+"/"+RpcServiceRouteMessage.getMethod();
			RpcServiceRouteMessage.setRoute(route);
			
			RpcServiceRouteMessages.add(RpcServiceRouteMessage);
		}
		
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		RpcServiceRouteMessages.clear();
	}

}
