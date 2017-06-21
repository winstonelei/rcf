/**
 * 
 */
package com.github.rcf.core.serializable.impl.jdk;

import com.github.rcf.core.serializable.RcfEncoder;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

/**
 * @author winstone
 * jdk 反序列化
 */
public class JavaEncoder implements RcfEncoder {

	@Override
	public byte[] encode(Object object) throws Exception {
		ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
		ObjectOutputStream output = new ObjectOutputStream(byteArray);
		output.writeObject(object);
		output.flush();
		output.close();
		return byteArray.toByteArray(); 
	}

}
