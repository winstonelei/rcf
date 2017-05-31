/**
 * 
 */
package com.github.rcf.core.serializable.impl;

import com.caucho.hessian.io.Hessian2Output;
import com.github.rcf.core.serializable.RcfEncoder;

import java.io.ByteArrayOutputStream;

/**
 * @author winstone
 *
 */
public class HessianEncoder implements RcfEncoder {

	@Override
	public byte[] encode(Object object) throws Exception {
		// TODO Auto-generated method stub
		ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
		Hessian2Output output = new Hessian2Output(byteArray);
		output.writeObject(object);
		output.close();
		byte[] bytes = byteArray.toByteArray();
		return bytes;
	}

}
