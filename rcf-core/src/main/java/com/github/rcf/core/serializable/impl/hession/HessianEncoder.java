/**
 * 
 */
package com.github.rcf.core.serializable.impl.hession;

import com.github.rcf.core.serializable.RcfEncoder;
import com.google.common.io.Closer;

import java.io.ByteArrayOutputStream;

/**
 * @author winstone
 *
 */
public class HessianEncoder implements RcfEncoder {

	private HessianSerializePool pool = HessianSerializePool.getHessianPoolInstance();
	private static Closer closer = Closer.create();

	@Override
	public byte[] encode(Object object) throws Exception {
		try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			closer.register(byteArrayOutputStream);
			HessianSerialize hessianSerialization = pool.borrow();
			hessianSerialization.serialize(byteArrayOutputStream, object);
			byte[] body = byteArrayOutputStream.toByteArray();
			pool.restore(hessianSerialization);
			return body;
		} finally {
			closer.close();
		}
	}
}