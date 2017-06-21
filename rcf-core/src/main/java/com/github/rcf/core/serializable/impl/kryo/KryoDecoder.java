/**
 * 
 */
package com.github.rcf.core.serializable.impl.kryo;
import com.esotericsoftware.kryo.pool.KryoPool;
import com.github.rcf.core.serializable.RcfDecoder;
import com.google.common.io.Closer;

import java.io.ByteArrayInputStream;

/**
 * @author winstone
 * Kryo 解码
 */
public class KryoDecoder implements RcfDecoder {

	private KryoPool pool = KryoPoolFactory.getKryoPoolInstance();
	private static Closer closer = Closer.create();

	@Override
	public Object decode(String className, byte[] body) throws Exception {
		try {
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
			closer.register(byteArrayInputStream);
			KryoSerialize kryoSerialization = new KryoSerialize(pool);
			Object obj = kryoSerialization.deserialize(byteArrayInputStream);
			return obj;
		} finally {
			closer.close();
		}
	}

}
