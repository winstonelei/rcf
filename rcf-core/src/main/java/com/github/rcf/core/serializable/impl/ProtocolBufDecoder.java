/**
 * 
 */
package com.github.rcf.core.serializable.impl;

import com.github.rcf.core.serializable.RcfDecoder;
import com.google.protobuf.Message;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author winstone
 *
 */
public class ProtocolBufDecoder implements RcfDecoder {
	
	private static ConcurrentHashMap<String, Message> messages = new ConcurrentHashMap<String, Message>();

	public static void addMessage(String className,Message message){
		messages.putIfAbsent(className, message);
	}
	@Override
	public Object decode(String className, byte[] bytes) throws Exception {
		// TODO Auto-generated method stub
		Message message = messages.get(className);
		return message.newBuilderForType().mergeFrom(bytes).build();
	}

}
