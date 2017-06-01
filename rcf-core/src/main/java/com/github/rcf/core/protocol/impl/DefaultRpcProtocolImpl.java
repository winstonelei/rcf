/**
 * 
 */
package com.github.rcf.core.protocol.impl;

import com.github.rcf.core.bean.RcfRequest;
import com.github.rcf.core.bean.RcfResponse;
import com.github.rcf.core.buffer.RpcByteBuffer;
import com.github.rcf.core.protocol.RcfProtocol;
import com.github.rcf.core.protocol.rcf.RcfRpcCustomProtocol;
import com.github.rcf.core.serializable.RcfCodes;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;


public class DefaultRpcProtocolImpl implements RcfProtocol {
	public static final int TYPE = 1;

	private static final Log LOGGER = LogFactory
			.getLog(DefaultRpcProtocolImpl.class);
	
	private static final int REQUEST_HEADER_LEN = 1 * 6 + 5 * 4 ;

	private static final int RESPONSE_HEADER_LEN = 1 * 6 + 3 * 4 ;

	private static final byte VERSION = (byte) 1;

	private static final byte REQUEST = (byte) 0;

	private static final byte RESPONSE = (byte) 1;

	
	@Override
	public RpcByteBuffer encode(Object message,
								RpcByteBuffer bytebufferWrapper) throws Exception {
		if (!(message instanceof RcfRequest)
				&& !(message instanceof RcfResponse)) {
			throw new Exception(
					"only support send RequestWrapper && ResponseWrapper");
		}
		int id = 0;
		byte type = REQUEST;
		if (message instanceof RcfRequest) {
			try {
				int requestArgTypesLen = 0;
				int requestArgsLen = 0;
				List<byte[]> requestArgTypes = new ArrayList<byte[]>();
				List<byte[]> requestArgs = new ArrayList<byte[]>();
				RcfRequest wrapper = (RcfRequest) message;
				byte[][] requestArgTypeStrings = wrapper.getArgTypes();
				for (byte[] requestArgType : requestArgTypeStrings) {
					requestArgTypes.add(requestArgType);
					requestArgTypesLen += requestArgType.length;
				}
				Object[] requestObjects = wrapper.getRequestObjects();
				if (requestObjects != null) {
					for (Object requestArg : requestObjects) {
						byte[] requestArgByte = RcfCodes.getEncoder(
								wrapper.getCodecType()).encode(requestArg);
						requestArgs.add(requestArgByte);
						requestArgsLen += requestArgByte.length;
					}
				}
				byte[] targetInstanceNameByte = wrapper.getTargetInstanceName();
				byte[] methodNameByte = wrapper.getMethodName();

				
				id = wrapper.getId();
				int timeout = wrapper.getTimeout();
				int capacity = RcfRpcCustomProtocol.HEADER_LEN + REQUEST_HEADER_LEN
						+ requestArgs.size() * 4 * 2
						+ targetInstanceNameByte.length + methodNameByte.length
						+ requestArgTypesLen + requestArgsLen;

				RpcByteBuffer byteBuffer = bytebufferWrapper
						.get(capacity);
				byteBuffer.writeByte(RcfRpcCustomProtocol.CURRENT_VERSION);
				byteBuffer.writeByte((byte) TYPE);
				//--------------HEADER_LEN----------------
				byteBuffer.writeByte(VERSION);//1B
				byteBuffer.writeByte(type);//1B
				byteBuffer.writeByte((byte) wrapper.getCodecType());//1B
				byteBuffer.writeByte((byte) 0);//1B
				byteBuffer.writeByte((byte) 0);//1B
				byteBuffer.writeByte((byte) 0);//1B
				byteBuffer.writeInt(id);
				byteBuffer.writeInt(timeout);//4B
				byteBuffer.writeInt(targetInstanceNameByte.length);//4B

				byteBuffer.writeInt(methodNameByte.length);//4B
				
				byteBuffer.writeInt(requestArgs.size());//4B
				//---------------REQUEST_HEADER_LEN----------
				
				for (byte[] requestArgType : requestArgTypes) {
					byteBuffer.writeInt(requestArgType.length);
				}
				for (byte[] requestArg : requestArgs) {
					byteBuffer.writeInt(requestArg.length);
				}
				byteBuffer.writeBytes(targetInstanceNameByte);

				
				byteBuffer.writeBytes(methodNameByte);
				
				for (byte[] requestArgType : requestArgTypes) {
					byteBuffer.writeBytes(requestArgType);
				}
				for (byte[] requestArg : requestArgs) {
					byteBuffer.writeBytes(requestArg);
				}
				return byteBuffer;
			} catch (Exception e) {
				LOGGER.error("encode request object error", e);
				throw e;
			}
		} else {
			RcfResponse wrapper = (RcfResponse) message;
			byte[] body = new byte[0];
			byte[] className = new byte[0];
			try {
				// no return object
				if (wrapper.getResponse() != null) {
					className = wrapper.getResponse().getClass().getName()
							.getBytes();
					body = RcfCodes.getEncoder(wrapper.getCodecType())
							.encode(wrapper.getResponse());
				}
				if (wrapper.isError()) {
					className = wrapper.getException().getClass().getName()
							.getBytes();
					body = RcfCodes.getEncoder(wrapper.getCodecType())
							.encode(wrapper.getException());
				}
				id = wrapper.getRequestId();
			} catch (Exception e) {
				LOGGER.error("encode response object error", e);
				// still create responsewrapper,so client can get exception
				wrapper.setResponse(new Exception(
						"serialize response object error", e));
				className = Exception.class.getName().getBytes();
				body = RcfCodes.getEncoder(wrapper.getCodecType())
						.encode(wrapper.getResponse());
			}
			type = RESPONSE;
			int capacity = RcfRpcCustomProtocol.HEADER_LEN + RESPONSE_HEADER_LEN
					+ body.length;
			if (wrapper.getCodecType() == RcfCodes.PB_CODEC) {
				capacity += className.length;
			}
			RpcByteBuffer byteBuffer = bytebufferWrapper.get(capacity);
			byteBuffer.writeByte(RcfRpcCustomProtocol.CURRENT_VERSION);
			byteBuffer.writeByte((byte) TYPE);
			byteBuffer.writeByte(VERSION);
			byteBuffer.writeByte(type);
			byteBuffer.writeByte((byte) wrapper.getCodecType());
			byteBuffer.writeByte((byte) 0);
			byteBuffer.writeByte((byte) 0);
			byteBuffer.writeByte((byte) 0);
			byteBuffer.writeInt(id);
			if (wrapper.getCodecType() == RcfCodes.PB_CODEC) {
				byteBuffer.writeInt(className.length);
			} else {
				byteBuffer.writeInt(0);
			}
			byteBuffer.writeInt(body.length);
			if (wrapper.getCodecType() == RcfCodes.PB_CODEC) {
				byteBuffer.writeBytes(className);
			}
			byteBuffer.writeBytes(body);
			return byteBuffer;
		}
	}

	
	@Override
	public Object decode(RpcByteBuffer wrapper, Object errorObject,
			int... originPosArray) throws Exception {
		final int originPos;
		if (originPosArray != null && originPosArray.length == 1) {
			originPos = originPosArray[0];
		} else {
			originPos = wrapper.readerIndex();
		}
		if (wrapper.readableBytes() < 2) {
			wrapper.setReaderIndex(originPos);
			return errorObject;
		}
		byte version = wrapper.readByte();
		if (version == (byte) 1) {
			byte type = wrapper.readByte();
			if (type == REQUEST) {
				if (wrapper.readableBytes() < REQUEST_HEADER_LEN - 2) {
					wrapper.setReaderIndex(originPos);
					return errorObject;
				}
				int codecType = wrapper.readByte();
				wrapper.readByte();
				wrapper.readByte();
				wrapper.readByte();
				
				int requestId = wrapper.readInt();

				int timeout = wrapper.readInt();
				int targetInstanceLen = wrapper.readInt();

				int methodNameLen = wrapper.readInt();
				int argsCount = wrapper.readInt();
				int argInfosLen = argsCount * 4 * 2;
				int expectedLenInfoLen = argInfosLen + targetInstanceLen
						+ methodNameLen;
				if (wrapper.readableBytes() < expectedLenInfoLen) {
					wrapper.setReaderIndex(originPos);
					return errorObject;
				}
				int expectedLen = 0;
				int[] argsTypeLen = new int[argsCount];
				for (int i = 0; i < argsCount; i++) {
					argsTypeLen[i] = wrapper.readInt();
					expectedLen += argsTypeLen[i];
				}
				int[] argsLen = new int[argsCount];
				for (int i = 0; i < argsCount; i++) {
					argsLen[i] = wrapper.readInt();
					expectedLen += argsLen[i];
				}
				byte[] targetInstanceByte = new byte[targetInstanceLen];
				wrapper.readBytes(targetInstanceByte);
				
				byte[] methodNameByte = new byte[methodNameLen];
				wrapper.readBytes(methodNameByte);
				
				if (wrapper.readableBytes() < expectedLen) {
					wrapper.setReaderIndex(originPos);
					return errorObject;
				}
				byte[][] argTypes = new byte[argsCount][];
				for (int i = 0; i < argsCount; i++) {
					byte[] argTypeByte = new byte[argsTypeLen[i]];
					wrapper.readBytes(argTypeByte);
					argTypes[i] = argTypeByte;
				}
				Object[] args = new Object[argsCount];
				for (int i = 0; i < argsCount; i++) {
					byte[] argByte = new byte[argsLen[i]];
					wrapper.readBytes(argByte);
					args[i] = argByte;
				}

				RcfRequest rcfRPCRequest = new RcfRequest(
						targetInstanceByte, methodNameByte, argTypes, args,
						timeout, requestId, codecType);
				
				int messageLen = RcfRpcCustomProtocol.HEADER_LEN + REQUEST_HEADER_LEN
						+ expectedLenInfoLen + expectedLen;
				rcfRPCRequest.setMessageLen(messageLen);
				return rcfRPCRequest;
				
			} else if (type == RESPONSE) {
				if (wrapper.readableBytes() < RESPONSE_HEADER_LEN - 2) {
					wrapper.setReaderIndex(originPos);
					return errorObject;
				}
				int codecType = wrapper.readByte();
				wrapper.readByte();
				wrapper.readByte();
				wrapper.readByte();
				
				int requestId = wrapper.readInt();
				
				int classNameLen = wrapper.readInt();
				int bodyLen = wrapper.readInt();
				if (wrapper.readableBytes() < classNameLen + bodyLen) {
					wrapper.setReaderIndex(originPos);
					return errorObject;
				}

				byte[] classNameBytes = null;
				if (codecType == RcfCodes.PB_CODEC) {
					classNameBytes = new byte[classNameLen];
					wrapper.readBytes(classNameBytes);
				}
				byte[] bodyBytes = new byte[bodyLen];
				wrapper.readBytes(bodyBytes);
				
				RcfResponse responseWrapper = new RcfResponse(
						requestId, codecType);
				responseWrapper.setResponse(bodyBytes);
				responseWrapper.setResponseClassName(classNameBytes);
				int messageLen = RcfRpcCustomProtocol.HEADER_LEN + RESPONSE_HEADER_LEN
						+ classNameLen + bodyLen;
				responseWrapper.setMessageLen(messageLen);
				return responseWrapper;
			} else {
				throw new UnsupportedOperationException("protocol type : "
						+ type + " is not supported!");
			}
		} else {
			throw new UnsupportedOperationException("protocol version :"
					+ version + " is not supported!");
		}
	}

}
