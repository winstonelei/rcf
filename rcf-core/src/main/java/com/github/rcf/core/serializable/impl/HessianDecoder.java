/**
 * 
 */
package com.github.rcf.core.serializable.impl;

import com.caucho.hessian.io.Hessian2Input;
import com.github.rcf.core.serializable.RcfDecoder;

import java.io.ByteArrayInputStream;

/**
 * @author winstone
 *
 */
public class HessianDecoder implements RcfDecoder {

	@Override
	public Object decode(String className, byte[] bytes) throws Exception {
		// TODO Auto-generated method stub
		Hessian2Input input = new Hessian2Input(new ByteArrayInputStream(bytes));
		Object resultObject = input.readObject();
		input.close();
		return resultObject;
	}

}
