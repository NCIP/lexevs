/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.apache.ddlutils.DdlUtilsException;
import org.apache.ddlutils.io.converters.ByteArrayBase64Converter;

public class BinaryObjectsHelper {
    public byte[] serialize(Object obj) {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ObjectOutputStream objOut = new ObjectOutputStream(output);
            objOut.writeObject(obj);
            objOut.close();
            return output.toByteArray();
        }
        catch (IOException ex) {
            throw new DdlUtilsException("Could not serialize object", ex);
        }
    }

    public Object deserialize(byte[] serializedForm) {
        try {
            ByteArrayInputStream input = new ByteArrayInputStream(serializedForm);
            ObjectInputStream objIn = new ObjectInputStream(input);
            return objIn.readObject();
        }
        catch (IOException ex) {
            throw new DdlUtilsException("Could not deserialize object", ex);
        }
        catch (ClassNotFoundException ex) {
            throw new DdlUtilsException("Could find class for deserialized object", ex);
        }
    }

    public String encode(Object obj) {
        return this.encodeByteArray(this.serialize(obj));
    }

    public String encodeByteArray(byte[] data) {
        return new ByteArrayBase64Converter().convertToString(data, -2);
    }

    public Object decode(String base64Rep) {
        return this.deserialize(this.decodeByteArray(base64Rep));
    }

    public byte[] decodeByteArray(String base64Rep) {
        return (byte[])new ByteArrayBase64Converter().convertFromString(base64Rep, -2);
    }
}

