package com.github.rcf.registry.serializable;



import com.github.rcf.registry.io.UnsafeByteArrayInputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author  winstone
 *
 */
public class SerializableSerializer implements ZkSerializer {
    @Override
    public byte[] serialize(Object serializable) throws ZkMarshallingException {
       /* try {
            if (serializable == null) {
                return null;
            }
            UnsafeByteArrayOutputStream byteArrayOS = new UnsafeByteArrayOutputStream();
            ObjectOutputStream stream = new ObjectOutputStream(byteArrayOS);
            stream.writeObject(serializable);
            stream.close();
            return byteArrayOS.toByteArray();
        } catch (IOException e) {
            throw new ZkMarshallingException(e);
        }
*/
        byte[] bytes = null;
        try {
            // object to bytearray
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(serializable);

            bytes = bo.toByteArray();

            bo.close();
            oo.close();
        } catch (Exception e) {
            System.out.println("translation" + e.getMessage());
            e.printStackTrace();
        }
        return bytes;
    }

    @Override
    public Object deserialize(byte[] bytes) throws ZkMarshallingException {
        if (bytes == null) {
            return null;
        }
        try {
            return new ObjectInputStream(new UnsafeByteArrayInputStream(bytes)).readObject();
        } catch (ClassNotFoundException e) {
            throw new ZkMarshallingException("Unable to find object class.", e);
        } catch (IOException e) {
            throw new ZkMarshallingException(e);
        }
    }
}
