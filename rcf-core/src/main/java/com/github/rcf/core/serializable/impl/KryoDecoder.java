/**
 * 
 */
package com.github.rcf.core.serializable.impl;
import com.github.rcf.core.serializable.RcfDecoder;

/**
 * @author winstone
 * Kryo 解码
 */
public class KryoDecoder implements RcfDecoder {

	@Override
	public Object decode(String className, byte[] bytes) throws Exception {
		// TODO Auto-generated method stub
		/*Input input = new Input(bytes);
		return KryoUtils.getKryo().readClassAndObject(input);*/
		return null;
	}

}
