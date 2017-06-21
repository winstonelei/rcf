/**
 * 
 */
package com.github.rcf.core.serializable.impl.protostuff;

import com.github.rcf.core.serializable.RcfDecoder;
import com.google.common.io.Closer;

import java.io.ByteArrayInputStream;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author winstone
 *
 */
public class ProtocolBufDecoder implements RcfDecoder {
	
	private static Closer closer = Closer.create();
	private ProtostuffSerializePool pool = ProtostuffSerializePool.getProtostuffPoolInstance();
	private boolean rpcDirect = false;

	public boolean isRpcDirect() {
		return rpcDirect;
	}

	public void setRpcDirect(boolean rpcDirect) {
		this.rpcDirect = rpcDirect;
	}
	@Override
	public Object decode(String className, byte[] body) throws Exception {
		try {
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
			closer.register(byteArrayInputStream);
			ProtostuffSerialize protostuffSerialization = pool.borrow();
			protostuffSerialization.setRpcDirect(rpcDirect);
			Object obj = protostuffSerialization.deserialize(byteArrayInputStream);
			pool.restore(protostuffSerialization);
			return obj;
		} finally {
			closer.close();
		}
	}

}
