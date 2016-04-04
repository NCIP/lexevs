package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.search;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.apache.lucene.search.ScoreDoc;
import org.junit.Test;

public class ScoreDocIteratoSerializationTest extends LexBIGServiceTestCase {

	@Override
	protected String getTestID() {
		return "ScoreDocIterator Serialization Test";
	}
	
	@Test
	public void testSerialization() throws LBException, IOException, ClassNotFoundException {
		ScoreDoc sd = new ScoreDoc(0, 0);
		List<ScoreDoc> list = new ArrayList<ScoreDoc>();
		list.add(sd);
		SearchScoreDocIterator searchExtension = new SearchScoreDocIterator(list);
       
		byte[] ba = serialize(searchExtension);
		SearchScoreDocIterator searchTarget = deSerialize(ba, SearchScoreDocIterator.class);

	}
	
	@Test
	public void testSerializationHasScoreDoc() throws LBException, IOException, ClassNotFoundException {
		ScoreDoc sd = new ScoreDoc(3, 1);
		List<ScoreDoc> list = new ArrayList<ScoreDoc>();
		list.add(sd);
		SearchScoreDocIterator searchExtension = new SearchScoreDocIterator(list);
       
		byte[] ba = serialize(searchExtension);
		SearchScoreDocIterator searchTarget = deSerialize(ba, SearchScoreDocIterator.class);		
		assertTrue(searchTarget.numberRemaining() > 0);
		searchTarget.next();

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



}
