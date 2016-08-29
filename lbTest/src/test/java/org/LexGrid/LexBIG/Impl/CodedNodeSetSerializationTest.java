package org.LexGrid.LexBIG.Impl;

import junit.framework.TestCase;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class CodedNodeSetSerializationTest extends TestCase {
	LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
	CodedNodeSet cns;
	private CodedNodeSet cnsNoRest;
	private CodedNodeSet cns3;
	
	@Before
	public void setUp() throws LBException{

	}

	@Test
	public void testBasicSerialization() throws IOException {
		CodedNodeSetImpl impl = new CodedNodeSetImpl();
		new ObjectOutputStream(new ByteArrayOutputStream()).writeObject(impl);
	}

	
	@Test 
	public void testDoesQueryResultsAtTarget() throws IOException, ClassNotFoundException, LBException{
		CodedNodeSet cns = getCodedNodeSet();
		cns = cns.restrictToCodes(Constructors.createConceptReferenceList("A0001"));
		byte[] ba = serialize(cns);
		CodedNodeSetImpl deSerializedCns =  deSerialize(ba, CodedNodeSetImpl.class);
		ResolvedConceptReferenceList targetlist = deSerializedCns.resolveToList(null, null, null, -1);
		assertTrue(targetlist.getResolvedConceptReferenceCount() > 0);
	}

	public void testSerializeDeserializeCnsWithRestrictToMatchingProperties() throws IOException, ClassNotFoundException, LBException{
		CodedNodeSet cns = getCodedNodeSet();
		cns = cns.restrictToMatchingProperties(Constructors.createLocalNameList("test"), null, "test", "LuceneQuery", null);

		deSerialize(serialize(cns), CodedNodeSetImpl.class);
	}

	public void testDoesQueryReturnSameNumberOfResults() throws IOException, ClassNotFoundException, LBException{
		CodedNodeSet cns = getCodedNodeSet();
		cns = cns.restrictToCodes(Constructors.createConceptReferenceList("A0001"));
		byte[] ba = serialize(cns);
		CodedNodeSetImpl deSerializedCns =  deSerialize(ba, CodedNodeSetImpl.class);
		ResolvedConceptReferenceList sourcelist = cns.resolveToList(null, null, null, -1);
		ResolvedConceptReferenceList targetlist = deSerializedCns.resolveToList(null, null, null, -1);
		assertTrue(targetlist.getResolvedConceptReferenceCount() > 0);
		assertEquals(sourcelist.getResolvedConceptReferenceCount(), targetlist.getResolvedConceptReferenceCount());
	}
	
	public void testDoesQueryReturnResult() throws IOException, ClassNotFoundException, LBException{
		CodedNodeSet cns = getCodedNodeSet();
		cns = cns.restrictToCodes(Constructors.createConceptReferenceList("A0001"));
		byte[] ba = serialize(cns);
		CodedNodeSetImpl deSerializedCns =  deSerialize(ba, CodedNodeSetImpl.class);
		ResolvedConceptReferenceList sourcelist = cns.resolveToList(null, null, null, -1);
		ResolvedConceptReferenceList targetlist = deSerializedCns.resolveToList(null, null, null, -1);
		assertEquals(sourcelist.getResolvedConceptReference(0).getCode(), targetlist.getResolvedConceptReference(0).getCode());
		assertEquals(sourcelist.getResolvedConceptReference(0).getEntityDescription().getContent(),
				targetlist.getResolvedConceptReference(0).getEntityDescription().getContent());
	}

	public void testDoesQueryReturnSpanQueryResult() throws IOException, ClassNotFoundException, LBException{
		CodedNodeSet cns = getCodedNodeSet();
		cns.restrictToMatchingDesignations("ncept for testing gr", CodedNodeSet.SearchDesignationOption.ALL, "subString", null);
		byte[] ba = serialize(cns);
		CodedNodeSetImpl deSerializedCns =  deSerialize(ba, CodedNodeSetImpl.class);
		ResolvedConceptReferenceList sourcelist = cns.resolveToList(null, null, null, -1);
		ResolvedConceptReferenceList targetlist = deSerializedCns.resolveToList(null, null, null, -1);
		String sourceQuery = ((CodedNodeSetImpl)cns).getQuery().toString();
		String targetQuery = ((CodedNodeSetImpl)deSerializedCns).getQuery().toString();
		assertTrue(sourceQuery.equals(targetQuery));
		assertTrue(targetlist.getResolvedConceptReferenceCount() > 0);
		assertEquals(sourcelist.getResolvedConceptReference(0).getCode(), targetlist.getResolvedConceptReference(0).getCode());
		assertEquals(sourcelist.getResolvedConceptReference(0).getEntityDescription().getContent(),
				targetlist.getResolvedConceptReference(0).getEntityDescription().getContent());
	}

	public void testDoesQueryReturnWildCardQueryResult() throws IOException, ClassNotFoundException, LBException{
		CodedNodeSet cns = getCodedNodeSet();
		cns.restrictToMatchingDesignations("graph", CodedNodeSet.SearchDesignationOption.ALL, "subString", null);
		byte[] ba = serialize(cns);
		CodedNodeSetImpl deSerializedCns =  deSerialize(ba, CodedNodeSetImpl.class);
		ResolvedConceptReferenceList sourcelist = cns.resolveToList(null, null, null, -1);
		ResolvedConceptReferenceList targetlist = deSerializedCns.resolveToList(null, null, null, -1);
		String sourceQuery = ((CodedNodeSetImpl)cns).getQuery().toString();
		String targetQuery = ((CodedNodeSetImpl)deSerializedCns).getQuery().toString();
		assertTrue(sourceQuery.equals(targetQuery));
		assertTrue(targetlist.getResolvedConceptReferenceCount() > 0);
		assertEquals(sourcelist.getResolvedConceptReference(0).getCode(), targetlist.getResolvedConceptReference(0).getCode());
		assertEquals(sourcelist.getResolvedConceptReference(0).getEntityDescription().getContent(),
				targetlist.getResolvedConceptReference(0).getEntityDescription().getContent());
	}

	public void testDoesQueryReturnLeadingWildCardQueryResult() throws IOException, ClassNotFoundException, LBException{
		CodedNodeSet cns = getCodedNodeSet();
		cns.restrictToMatchingDesignations("ncept for testing graph", CodedNodeSet.SearchDesignationOption.ALL, "subString", null);
		byte[] ba = serialize(cns);
		CodedNodeSetImpl deSerializedCns =  deSerialize(ba, CodedNodeSetImpl.class);
		ResolvedConceptReferenceList sourcelist = cns.resolveToList(null, null, null, -1);
		ResolvedConceptReferenceList targetlist = deSerializedCns.resolveToList(null, null, null, -1);
		String sourceQuery = ((CodedNodeSetImpl)cns).getQuery().toString();
		String targetQuery = ((CodedNodeSetImpl)deSerializedCns).getQuery().toString();
		assertTrue(sourceQuery.equals(targetQuery));
		assertTrue(targetlist.getResolvedConceptReferenceCount() > 0);
		assertEquals(sourcelist.getResolvedConceptReference(0).getCode(), targetlist.getResolvedConceptReference(0).getCode());
		assertEquals(sourcelist.getResolvedConceptReference(0).getEntityDescription().getContent(), 
				targetlist.getResolvedConceptReference(0).getEntityDescription().getContent());
	}
	
	public void testDoesQueryReturnTrailingWildCardQueryResult() throws IOException, ClassNotFoundException, LBException{
		CodedNodeSet cns = getCodedNodeSet();
		cns.restrictToMatchingDesignations("concept for testing gra", CodedNodeSet.SearchDesignationOption.ALL, "subString", null);
		byte[] ba = serialize(cns);
		CodedNodeSetImpl deSerializedCns =  deSerialize(ba, CodedNodeSetImpl.class);
		ResolvedConceptReferenceList sourcelist = cns.resolveToList(null, null, null, -1);
		ResolvedConceptReferenceList targetlist = deSerializedCns.resolveToList(null, null, null, -1);
		String sourceQuery = ((CodedNodeSetImpl)cns).getQuery().toString();
		String targetQuery = ((CodedNodeSetImpl)deSerializedCns).getQuery().toString();
		assertTrue(sourceQuery.equals(targetQuery));
		assertTrue(targetlist.getResolvedConceptReferenceCount() > 0);
		assertEquals(sourcelist.getResolvedConceptReference(0).getCode(), targetlist.getResolvedConceptReference(0).getCode());
		assertEquals(sourcelist.getResolvedConceptReference(0).getEntityDescription().getContent(), 
				targetlist.getResolvedConceptReference(0).getEntityDescription().getContent());
	}
	
	public void testDoesQueryReturnPrefixQueryResult() throws IOException, ClassNotFoundException, LBException{
		CodedNodeSet cns = getCodedNodeSet();
		cns.restrictToMatchingDesignations("Automob", CodedNodeSet.SearchDesignationOption.ALL, "startsWith", null);
		byte[] ba = serialize(cns);
		CodedNodeSetImpl deSerializedCns =  deSerialize(ba, CodedNodeSetImpl.class);
		ResolvedConceptReferenceList sourcelist = cns.resolveToList(null, null, null, -1);
		ResolvedConceptReferenceList targetlist = deSerializedCns.resolveToList(null, null, null, -1);
		String sourceQuery = ((CodedNodeSetImpl)cns).getQuery().toString();
		String targetQuery = ((CodedNodeSetImpl)deSerializedCns).getQuery().toString();
		assertTrue(sourceQuery.equals(targetQuery));
		assertTrue(targetlist.getResolvedConceptReferenceCount() > 0);
		assertEquals(sourcelist.getResolvedConceptReference(0).getCode(), targetlist.getResolvedConceptReference(0).getCode());
		assertEquals(sourcelist.getResolvedConceptReference(0).getEntityDescription().getContent(), 
				targetlist.getResolvedConceptReference(0).getEntityDescription().getContent());
	}
	
	public void testDoesQueryReturnPhraseQueryResults() throws IOException, ClassNotFoundException, LBException{
		CodedNodeSet cns = getCodedNodeSet();
		cns.restrictToMatchingDesignations("kar", CodedNodeSet.SearchDesignationOption.ALL, "SpellingErrorTolerantSubStringSearch", null);
		byte[] ba = serialize(cns);
		CodedNodeSetImpl deSerializedCns =  deSerialize(ba, CodedNodeSetImpl.class);
		ResolvedConceptReferenceList sourcelist = cns.resolveToList(null, null, null, -1);
		ResolvedConceptReferenceList targetlist = deSerializedCns.resolveToList(null, null, null, -1);
		String sourceQuery = ((CodedNodeSetImpl)cns).getQuery().toString();
		String targetQuery = ((CodedNodeSetImpl)deSerializedCns).getQuery().toString();
		assertTrue(sourceQuery.equals(targetQuery));
		assertTrue(targetlist.getResolvedConceptReferenceCount() > 0);
		assertEquals(sourcelist.getResolvedConceptReference(0).getCode(), targetlist.getResolvedConceptReference(0).getCode());
		assertEquals(sourcelist.getResolvedConceptReference(0).getEntityDescription().getContent(), 
				targetlist.getResolvedConceptReference(0).getEntityDescription().getContent());
	}
	
	private CodedNodeSet getCodedNodeSet() throws LBException{
		return lbs.getCodingSchemeConcepts("Automobiles", null);
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
