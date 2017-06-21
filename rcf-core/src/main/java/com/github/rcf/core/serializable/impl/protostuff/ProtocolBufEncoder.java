/**
 * 
 */
package com.github.rcf.core.serializable.impl.protostuff;

import com.github.rcf.core.serializable.RcfEncoder;
import com.google.common.io.Closer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author winstone
 *
 */
public class ProtocolBufEncoder implements RcfEncoder {

	private static Closer closer = Closer.create();
	private ProtostuffSerializePool pool = ProtostuffSerializePool.getProtostuffPoolInstance();
	private boolean rpcDirect = false;

	public boolean isRpcDirect() {
		return rpcDirect;
	}

	public void setRpcDirect(boolean rpcDirect) {
		this.rpcDirect = rpcDirect;
	}

	public byte[] encode(Object message) throws IOException {
		try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			closer.register(byteArrayOutputStream);
			ProtostuffSerialize protostuffSerialization = pool.borrow();
			protostuffSerialization.serialize(byteArrayOutputStream, message);
			byte[] body = byteArrayOutputStream.toByteArray();
			pool.restore(protostuffSerialization);
			return body;
		} finally {
			closer.close();
		}
	}
}
