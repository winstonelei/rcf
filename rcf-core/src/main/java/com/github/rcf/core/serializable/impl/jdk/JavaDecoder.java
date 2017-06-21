/**
 * 
 */
package com.github.rcf.core.serializable.impl.jdk;

import com.github.rcf.core.serializable.RcfDecoder;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

/**
 * @author winstone
 * jdk 序列化
 */
public class JavaDecoder implements RcfDecoder {

	@Override
	public Object decode(String className, byte[] bytes) throws Exception {
		ObjectInputStream objectIn = new ObjectInputStream(new ByteArrayInputStream(bytes));
		Object resultObject = objectIn.readObject();
		objectIn.close();
		return resultObject;
	}

}
