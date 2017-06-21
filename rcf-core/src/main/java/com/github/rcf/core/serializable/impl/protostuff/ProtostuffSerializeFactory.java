package com.github.rcf.core.serializable.impl.protostuff;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * Created by winstone on 2017/6/21.
 */
public class ProtostuffSerializeFactory extends BasePooledObjectFactory<ProtostuffSerialize> {

    public ProtostuffSerialize create() throws Exception {
        return createProtostuff();
    }

    public PooledObject<ProtostuffSerialize> wrap(ProtostuffSerialize hessian) {
        return new DefaultPooledObject<ProtostuffSerialize>(hessian);
    }

    private ProtostuffSerialize createProtostuff() {
        return new ProtostuffSerialize();
    }
}
