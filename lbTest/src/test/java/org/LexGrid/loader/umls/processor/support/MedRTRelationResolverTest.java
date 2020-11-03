package org.LexGrid.loader.umls.processor.support;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lexgrid.loader.rrf.model.Mrrel;
import org.lexgrid.loader.rrf.staging.MrconsoStagingDao;
import org.lexgrid.loader.rrf.staging.model.CodeSabPair;
import org.lexgrid.loader.umls.processor.support.MedRTRelationResolver;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MedRTRelationResolverTest {
	
	Mrrel mrel;
	
	@Mock
	MrconsoStagingDao mrconsoStagingDao;
	
	@InjectMocks
	MedRTRelationResolver resolver;

	@Before
	public void setUp() throws Exception {
		mrel = new Mrrel();
		mrel.setAui2("A14778828");
		mrel.setCui2("C0000005");
		mrel.setAui1("C1373164");
		mrel.setCui1("A15554347");
		CodeSabPair pair1 = new CodeSabPair();
		pair1.setCode("N0000000009");
		pair1.setSab("MED-RT");
		
		CodeSabPair pair2 = new CodeSabPair();
		pair2.setCode("D012711");
		pair2.setSab("MSH");
		
		when(mrconsoStagingDao.getCodeAndSab(mrel.getCui2(), mrel.getAui2())).thenReturn(pair2);
		when(mrconsoStagingDao.getCodeAndSab(mrel.getCui1(), mrel.getAui1())).thenReturn(pair1);
	}

	@Test
	public void testGetSource() {
		 assertEquals("D012711", resolver.getSource(mrel));
	}

	@Test
	public void testGetTarget() {
		assertEquals("N0000000009", resolver.getTarget(mrel));
	}

	@Test
	public void testGetSourceNamespace() {
		assertEquals("MSH", resolver.getSourceNamespace(mrel));
	}

	@Test
	public void testGetTargetNamespace() {
		assertEquals("MED-RT", resolver.getTargetNamespace(mrel));
	}

}
