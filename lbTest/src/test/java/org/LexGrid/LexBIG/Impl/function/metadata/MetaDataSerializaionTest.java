
package org.LexGrid.LexBIG.Impl.function.metadata;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceMetadataImpl;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceMetadata;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.junit.Test;

public class MetaDataSerializaionTest extends LexBIGServiceTestCase {

	@Test
	public void testBooleanQueryInRestriction() throws IOException, ClassNotFoundException, LBParameterException {
		LexBIGServiceMetadataImpl lbMetadata = new LexBIGServiceMetadataImpl();
		lbMetadata.restrictToCodingScheme(Constructors.createAbsoluteCodingSchemeVersionReference("urn", "version"));
	    byte[] b = serialize(lbMetadata);
	    deSerialize(b, LexBIGServiceMetadataImpl.class);
	}

	private static <T extends Serializable> byte[] serialize(T obj) 
		       throws IOException 
		{
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    ObjectOutputStream oos = new ObjectOutputStream(baos);
		    oos.writeObject(obj);
		    oos.close();
		    return baos.toByteArray();
		}

	private static <T extends Serializable> T deSerialize(byte[] b, Class<T> cl)
		       throws IOException, ClassNotFoundException 
		{
		    ByteArrayInputStream bais = new ByteArrayInputStream(b);
		    ObjectInputStream ois = new ObjectInputStream(bais);
		    Object o = ois.readObject();
		    return cl.cast(o);
		}

	@Override
	protected String getTestID() {
		return "Meta Data Serialization Test";
	}
}