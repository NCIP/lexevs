/**
 * 
 */
package org.lexevs.cts2.query;

import static org.junit.Assert.fail;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.SortOption;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.junit.Test;
import org.lexevs.cts2.LexEvsCTS2Impl;
import org.lexgrid.valuesets.dto.ResolvedValueSetDefinition;

/**
 * @author m004181
 *
 */
public class ValueSetQueryOperationImplTest {

	/**
	 * Test method for {@link org.lexevs.cts2.query.ValueSetQueryOperationImpl#ValueSetQueryOperationImpl(org.lexevs.cts2.LexEvsCTS2)}.
	 */
	@Test
	public void testValueSetQueryOperationImpl() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.lexevs.cts2.query.ValueSetQueryOperationImpl#checkConceptValueSetMembership(java.lang.String, java.net.URI, org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testCheckConceptValueSetMembership() {
		ValueSetQueryOperation vsQueryop = LexEvsCTS2Impl.defaultInstance().getQueryOperation().getValueSetQueryOperation();
		
		AbsoluteCodingSchemeVersionReference acsvr = new AbsoluteCodingSchemeVersionReference();
		acsvr.setCodingSchemeURN("urn:oid:11.11.0.1");
		acsvr.setCodingSchemeVersion("1.0");
		
		try {
			boolean member = vsQueryop.checkConceptValueSetMembership("Chevy", new URI("Automobiles"), acsvr, "SRITEST:AUTO:GM", "R104", null);
			System.out.println("member ? : " + member);
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link org.lexevs.cts2.query.ValueSetQueryOperationImpl#checkValueSetSubsumption(java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList, java.lang.String)}.
	 */
	@Test
	public void testCheckValueSetSubsumption() {
		ValueSetQueryOperation vsQueryop = LexEvsCTS2Impl.defaultInstance().getQueryOperation().getValueSetQueryOperation();
		
		AbsoluteCodingSchemeVersionReference acsvr = new AbsoluteCodingSchemeVersionReference();
		acsvr.setCodingSchemeURN("urn:oid:11.11.0.1");
		acsvr.setCodingSchemeVersion("1.1");
		
		AbsoluteCodingSchemeVersionReferenceList csList = new AbsoluteCodingSchemeVersionReferenceList();
		csList.addAbsoluteCodingSchemeVersionReference(acsvr);
		
		try {
			boolean subsume = vsQueryop.checkValueSetSubsumption("SRITEST:AUTO:DomasticLeafOnly", null, "SRITEST:AUTO:EveryThing", null, csList, null);
			System.out.println("subsumption ? : " + subsume);
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	/**
	 * Test method for {@link org.lexevs.cts2.query.ValueSetQueryOperationImpl#getValueSetDetails(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testGetValueSetDetails() {
		ValueSetQueryOperation vsQueryop = LexEvsCTS2Impl.defaultInstance().getQueryOperation().getValueSetQueryOperation();
		try {
			ValueSetDefinition vsd = vsQueryop.getValueSetDetails("SRITEST:AUTO:GMTEST", "R403");
			
			System.out.println(vsd.getValueSetDefinitionURI());
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link org.lexevs.cts2.query.ValueSetQueryOperationImpl#listValueSetContents(java.lang.String, java.lang.String, org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList, java.lang.String, org.LexGrid.LexBIG.DataModel.InterfaceElements.SortOption)}.
	 */
	@Test
	public void testListValueSetContents() {
		ValueSetQueryOperation vsQueryop = LexEvsCTS2Impl.defaultInstance().getQueryOperation().getValueSetQueryOperation();
		SortOption sortOption = new SortOption();
		sortOption.setAscending(true);
		AbsoluteCodingSchemeVersionReference acsvr1 = new AbsoluteCodingSchemeVersionReference();
		acsvr1.setCodingSchemeURN("urn:oid:11.11.0.1");
		acsvr1.setCodingSchemeVersion("1.0");
		AbsoluteCodingSchemeVersionReferenceList csList1 = new AbsoluteCodingSchemeVersionReferenceList();
		csList1.addAbsoluteCodingSchemeVersionReference(acsvr1);
		
		try {
			ResolvedValueSetDefinition vsdResolved = vsQueryop.listValueSetContents("SRITEST:AUTO:GMTEST", "R402", csList1, null, sortOption);
			System.out.println();
			if (vsdResolved != null)
			{
				AbsoluteCodingSchemeVersionReferenceList csList = vsdResolved.getCodingSchemeVersionRefList();
				if (csList != null)
				{
					for (AbsoluteCodingSchemeVersionReference acsvr : csList.getAbsoluteCodingSchemeVersionReference())
					{
						System.out.println("cs urn : " + acsvr.getCodingSchemeURN());
						System.out.println("cs version :" + acsvr.getCodingSchemeVersion());
					}
				}
				System.out.println("vsd name : " + vsdResolved.getValueSetDefinitionName());
				System.out.println("vsd uri : " + vsdResolved.getValueDomainURI());
				
				ResolvedConceptReferencesIterator cItr = vsdResolved.getResolvedConceptReferenceIterator();
				if (cItr.hasNext())
				{
					ResolvedConceptReference rcr = cItr.next();
					System.out.println("code : " + rcr.getCode());
					System.out.println("namespace : " + rcr.getCodeNamespace());
					System.out.println("cs uri : " + rcr.getCodingSchemeURI());
					System.out.println("cs name : " + rcr.getCodingSchemeName());
					System.out.println("-------------------------");
				}
			}
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link org.lexevs.cts2.query.ValueSetQueryOperationImpl#listValueSets(java.lang.String, java.lang.String, java.lang.String, org.LexGrid.LexBIG.DataModel.InterfaceElements.SortOption)}.
	 */
	@Test
	public void testListValueSets() {
		ValueSetQueryOperation vsQueryop = LexEvsCTS2Impl.defaultInstance().getQueryOperation().getValueSetQueryOperation();
		SortOption sortOption = new SortOption();
		sortOption.setAscending(true);
		try {
			List<String> vsdURIs = vsQueryop.listValueSets("Automobiles", "Autos", null, null, sortOption);
			for (String vsdURI : vsdURIs)
			{
				System.out.println(vsdURI);
			}
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link org.lexevs.cts2.query.ValueSetQueryOperationImpl#listAllValueSets(org.LexGrid.LexBIG.DataModel.InterfaceElements.SortOption)}.
	 */
	@Test
	public void testListAllValueSets() {
		ValueSetQueryOperation vsQueryop = LexEvsCTS2Impl.defaultInstance().getQueryOperation().getValueSetQueryOperation();
		SortOption sortOption = new SortOption();
		sortOption.setAscending(true);
		try {
			List<String> vsdURIs = vsQueryop.listAllValueSets(sortOption);
			for (String vsdURI : vsdURIs)
			{
				System.out.println(vsdURI);
			}
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
