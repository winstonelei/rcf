package com.github.rcf.core.serializable;

import com.github.rcf.core.serializable.impl.hession.HessianDecoder;
import com.github.rcf.core.serializable.impl.hession.HessianEncoder;
import com.github.rcf.core.serializable.impl.jdk.JavaDecoder;
import com.github.rcf.core.serializable.impl.jdk.JavaEncoder;
import com.github.rcf.core.serializable.impl.kryo.KryoDecoder;
import com.github.rcf.core.serializable.impl.kryo.KryoEncoder;
import com.github.rcf.core.serializable.impl.protostuff.ProtocolBufDecoder;
import com.github.rcf.core.serializable.impl.protostuff.ProtocolBufEncoder;

/**
 * @author winstone
 * 
 */
public class RcfCodes {

	public static final int JAVA_CODEC = 1;

	public static final int HESSIAN_CODEC = 2;

	public static final int PB_CODEC = 3;

	public static final int KRYO_CODEC = 4;

	private static RcfEncoder[] encoders = new RcfEncoder[5];

	private static RcfDecoder[] decoders = new RcfDecoder[5];

	static {
		addEncoder(JAVA_CODEC, new JavaEncoder());
		addEncoder(HESSIAN_CODEC, new HessianEncoder());
		addEncoder(PB_CODEC, new ProtocolBufEncoder());
		addEncoder(KRYO_CODEC, new KryoEncoder());

		addDecoder(JAVA_CODEC, new JavaDecoder());
		addDecoder(HESSIAN_CODEC, new HessianDecoder());
		addDecoder(PB_CODEC, new ProtocolBufDecoder());
		addDecoder(KRYO_CODEC, new KryoDecoder());
	}

	public static void addEncoder(int encoderKey, RcfEncoder encoder) {
		if (encoderKey > encoders.length) {
			RcfEncoder[] newEncoders = new RcfEncoder[encoderKey + 1];
			System.arraycopy(encoders, 0, newEncoders, 0, encoders.length);
			encoders = newEncoders;
		}
		encoders[encoderKey] = encoder;
	}

	public static void addDecoder(int decoderKey, RcfDecoder decoder) {
		if (decoderKey > decoders.length) {
			RcfDecoder[] newDecoders = new RcfDecoder[decoderKey + 1];
			System.arraycopy(decoders, 0, newDecoders, 0, decoders.length);
			decoders = newDecoders;
		}
		decoders[decoderKey] = decoder;
	}

	public static RcfEncoder getEncoder(int encoderKey) {
		return encoders[encoderKey];
	}

	public static RcfDecoder getDecoder(int decoderKey) {
		return decoders[decoderKey];
	}
}
