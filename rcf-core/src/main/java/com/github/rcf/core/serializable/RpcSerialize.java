package com.github.rcf.core.serializable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by winstone on 2017/6/21.
 */
public interface RpcSerialize {

    void serialize(OutputStream output, Object object) throws IOException;

    Object deserialize(InputStream input) throws IOException;
}
