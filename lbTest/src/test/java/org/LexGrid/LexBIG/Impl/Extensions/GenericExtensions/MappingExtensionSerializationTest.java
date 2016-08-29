package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.Mapping;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.Mapping.SearchContext;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;

public class MappingExtensionSerializationTest extends LexBIGServiceTestCase {

	@Override
	protected String getTestID() {
		return "Mapping Serialization";
	}

	
	public void testIsMappingCodingScheme() throws LBException, IOException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
		
		assertNotNull(serialize(mappingExtension));

	}
	
	public void testIsMappingCodingSchemeRndTrip() throws LBException, IOException, ClassNotFoundException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
		byte[] bytes = serialize(mappingExtension);
		assertNotNull(deSerialize(bytes, MappingExtension.class));
	}
	
	public void testIsMappingCodingSchemeIterator() throws LBException, IOException, ClassNotFoundException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
		ResolvedConceptReferencesIterator iterator = mappingExtension.resolveMapping(MAPPING_SCHEME_URI,
				Constructors.createCodingSchemeVersionOrTagFromVersion(MAPPING_SCHEME_VERSION), null, null);
		assertNotNull(serialize(iterator));
	}
	
	
	public void testIsMappingCodingSchemeIteratorRndTrp() throws LBException, IOException, ClassNotFoundException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
		ResolvedConceptReferencesIterator iterator = mappingExtension.resolveMapping(MAPPING_SCHEME_URI,
				Constructors.createCodingSchemeVersionOrTagFromVersion(MAPPING_SCHEME_VERSION), null, null);
		byte[] bytes = serialize(iterator);
		assertNotNull(deSerialize(bytes, ResolvedConceptReferencesIterator.class));
	}
	
	public void testIsMappingCodingSchemeMapping() throws LBException, IOException, ClassNotFoundException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
		Mapping mapping = mappingExtension.getMapping(MAPPING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(MAPPING_SCHEME_VERSION), null);
		byte[] bytes = serialize(mapping);
		assertNotNull(deSerialize(bytes, Mapping.class));
	}
	
	public void testIsMappingCodingSchemeMappingWthRestriction() throws LBException, IOException, ClassNotFoundException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
		Mapping mapping = mappingExtension.getMapping(MAPPING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(MAPPING_SCHEME_VERSION), null);
		mapping = mapping.restrictToMatchingDesignations(
				"car", SearchDesignationOption.ALL, "LuceneQuery", null, SearchContext.SOURCE_OR_TARGET_CODES);
		byte[] bytes = serialize(mapping);
		assertNotNull(deSerialize(bytes, Mapping.class));
	}
	
	public void testIsMappingCodingSchemeMappingWthRestrictionIterator() throws LBException, IOException, ClassNotFoundException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
		Mapping mapping = mappingExtension.getMapping(MAPPING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(MAPPING_SCHEME_VERSION), null);
		mapping = mapping.restrictToMatchingDesignations(
				"car", SearchDesignationOption.ALL, "LuceneQuery", null, SearchContext.SOURCE_OR_TARGET_CODES);
		ResolvedConceptReferencesIterator iterator = mapping.resolveMapping();
		byte[] bytes = serialize(iterator);
		ResolvedConceptReferencesIterator itr = deSerialize(bytes, ResolvedConceptReferencesIterator.class);
		assertNotNull(itr);
		assertTrue(iterator.hasNext());
		assertTrue(itr.hasNext());
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
