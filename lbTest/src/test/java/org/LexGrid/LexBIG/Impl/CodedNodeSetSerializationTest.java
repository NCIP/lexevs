package org.LexGrid.LexBIG.Impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import junit.framework.TestCase;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.junit.Before;
import org.junit.Test;

public class CodedNodeSetSerializationTest extends TestCase {
	LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
	CodedNodeSet cns;
	private CodedNodeSet cnsNoRest;
	
	@Before
	public void setUp() throws LBException{
		cns = lbs.getCodingSchemeConcepts("Automobiles", null);
		cns = cns.restrictToCodes(Constructors.createConceptReferenceList("A0001"));
		cnsNoRest = lbs.getCodingSchemeConcepts("Automobiles", null);
	}

	@Test
	public void testBasicSerialization() throws IOException {
		CodedNodeSetImpl impl = new CodedNodeSetImpl();
		new ObjectOutputStream(new ByteArrayOutputStream()).writeObject(impl);
	}
	
	@Test
	public void testIsDeserializedBuilderEqual() throws IOException, ClassNotFoundException{
		byte[] ba = serialize(cns);
		CodedNodeSetImpl deSerializedCns =  deSerialize(ba, CodedNodeSetImpl.class);
		assertEquals(deSerializedCns.getQuery(), ((CodedNodeSetImpl)cns).getQuery());
	}
	
	@Test 
	public void testDoesQueryResultsAtTarget() throws IOException, ClassNotFoundException, LBInvocationException, LBParameterException{
		byte[] ba = serialize(cns);
		CodedNodeSetImpl deSerializedCns =  deSerialize(ba, CodedNodeSetImpl.class);
		ResolvedConceptReferenceList targetlist = deSerializedCns.resolveToList(null, null, null, -1);
		assertTrue(targetlist.getResolvedConceptReferenceCount() > 0);
	}
	
	public void testDoesQueryReturnSameNumberOfResults() throws IOException, ClassNotFoundException, LBInvocationException, LBParameterException{
		byte[] ba = serialize(cns);
		CodedNodeSetImpl deSerializedCns =  deSerialize(ba, CodedNodeSetImpl.class);
		ResolvedConceptReferenceList sourcelist = cns.resolveToList(null, null, null, -1);
		ResolvedConceptReferenceList targetlist = deSerializedCns.resolveToList(null, null, null, -1);
		assertTrue(targetlist.getResolvedConceptReferenceCount() > 0);
		assertEquals(sourcelist.getResolvedConceptReferenceCount(), targetlist.getResolvedConceptReferenceCount());
	}
	
	public void testDoesQueryReturnResult() throws IOException, ClassNotFoundException, LBInvocationException, LBParameterException{
		byte[] ba = serialize(cns);
		CodedNodeSetImpl deSerializedCns =  deSerialize(ba, CodedNodeSetImpl.class);
		ResolvedConceptReferenceList sourcelist = cns.resolveToList(null, null, null, -1);
		ResolvedConceptReferenceList targetlist = deSerializedCns.resolveToList(null, null, null, -1);
		assertEquals(sourcelist.getResolvedConceptReference(0).getCode(), targetlist.getResolvedConceptReference(0).getCode());
		assertEquals(sourcelist.getResolvedConceptReference(0).getEntityDescription().getContent(), 
				targetlist.getResolvedConceptReference(0).getEntityDescription().getContent());
	}
	
	public void testDoesQueryReturnSpanQueryResult() throws IOException, ClassNotFoundException, LBInvocationException, LBParameterException{
		cnsNoRest.restrictToMatchingDesignations("ncept for testing gr", SearchDesignationOption.ALL, "subString", null);
		byte[] ba = serialize(cnsNoRest);
		CodedNodeSetImpl deSerializedCns =  deSerialize(ba, CodedNodeSetImpl.class);
		ResolvedConceptReferenceList sourcelist = cnsNoRest.resolveToList(null, null, null, -1);
		ResolvedConceptReferenceList targetlist = deSerializedCns.resolveToList(null, null, null, -1);
		String sourceQuery = ((CodedNodeSetImpl)cnsNoRest).getQuery().toString();
		String targetQuery = ((CodedNodeSetImpl)deSerializedCns).getQuery().toString();
		assertTrue(sourceQuery.equals(targetQuery));
		assertTrue(targetlist.getResolvedConceptReferenceCount() > 0);
		assertEquals(sourcelist.getResolvedConceptReference(0).getCode(), targetlist.getResolvedConceptReference(0).getCode());
		assertEquals(sourcelist.getResolvedConceptReference(0).getEntityDescription().getContent(), 
				targetlist.getResolvedConceptReference(0).getEntityDescription().getContent());
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
