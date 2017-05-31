/**
 * 
 */
package com.github.rcf.core.serializable.impl;

import com.github.rcf.core.serializable.RcfEncoder;

/**
 * @author winstone
 *
 */
public class KryoEncoder implements RcfEncoder {

	/* (non-Javadoc)
	 * @see com.jd.cross.plateform.rocketrpc.core.codec.RocketRPCEncoder#encode(java.lang.Object)
	 */
	@Override
	public byte[] encode(Object object) throws Exception {
		/*// TODO Auto-generated method stub
		Output output = new Output(256);
		KryoUtils.getKryo().writeClassAndObject(output, object);
		return output.toBytes();*/
		return  null;
	}

}
