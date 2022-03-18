
package org.lexgrid.loader.meta.integration.beans;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.lexgrid.loader.rrf.model.Mrsab;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * The Class MrsabXmlWriterTestIT.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrsabXmlWriterTestIT extends BeanTestBase {	
	
	/** The mrsab xml writer. */
	@Autowired
	@Qualifier("mrsabXmlWriter")
	private StaxEventItemWriter<Mrsab> mrsabXmlWriter;
	
	/** The mrsab. */
	private Mrsab mrsab;
	
	/**
	 * Buildmrsab.
	 */
	@Before
	public void buildmrsab(){
		mrsab = new Mrsab();
		mrsab.setAtnl("atnl");
		mrsab.setCenc("cenc");
		mrsab.setCfr("cfr");
	}
	
	/**
	 * Test write xml.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testWriteXml() throws Exception {
		List<Mrsab> mrsabList = new ArrayList<Mrsab>();
		mrsabList.add(mrsab);
	
		File xmlFile = File.createTempFile("metaxml", ".xml");
		Resource resource = new FileSystemResource(xmlFile);
		mrsabXmlWriter.setResource(resource);
		
		mrsabXmlWriter.afterPropertiesSet();
		mrsabXmlWriter.open(new ExecutionContext());
		mrsabXmlWriter.write(mrsabList);
		
		assertTrue(resource.getFile().exists());
	}
}