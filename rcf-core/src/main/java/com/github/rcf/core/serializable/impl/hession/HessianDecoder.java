/**
 * 
 */
package com.github.rcf.core.serializable.impl.hession;

import com.github.rcf.core.serializable.RcfDecoder;
import com.google.common.io.Closer;

import java.io.ByteArrayInputStream;

/**
 * @author winstone
 *
 */
public class HessianDecoder implements RcfDecoder {

	private HessianSerializePool pool = HessianSerializePool.getHessianPoolInstance();
	private static Closer closer = Closer.create();

	@Override
	public Object decode(String className, byte[] body) throws Exception {
		try {
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
			closer.register(byteArrayInputStream);
			HessianSerialize hessianSerialization = pool.borrow();
			Object object = hessianSerialization.deserialize(byteArrayInputStream);
			pool.restore(hessianSerialization);
			return object;
		} finally {
			closer.close();
		}	}

}
