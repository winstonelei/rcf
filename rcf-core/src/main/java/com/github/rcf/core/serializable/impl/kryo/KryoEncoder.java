/**
 * 
 */
package com.github.rcf.core.serializable.impl.kryo;

import com.esotericsoftware.kryo.pool.KryoPool;
import com.github.rcf.core.serializable.RcfEncoder;
import com.google.common.io.Closer;

import java.io.ByteArrayOutputStream;

/**
 * @author winstone
 *
 */
public class KryoEncoder implements RcfEncoder {

	private KryoPool pool = KryoPoolFactory.getKryoPoolInstance();
	private static Closer closer = Closer.create();

	@Override
	public byte[] encode(Object message) throws Exception {
	 try {
 		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		closer.register(byteArrayOutputStream);
		KryoSerialize kryoSerialization = new KryoSerialize(pool);
		kryoSerialization.serialize(byteArrayOutputStream, message);
		byte[] body = byteArrayOutputStream.toByteArray();
		return body;
	} finally {
		closer.close();
	}
	}

}
