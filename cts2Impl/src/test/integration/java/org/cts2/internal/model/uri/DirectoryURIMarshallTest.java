package org.cts2.internal.model.uri;

import static org.junit.Assert.assertTrue;

import java.io.StringWriter;

import org.cts2.codesystem.CodeSystemDirectory;
import org.cts2.internal.model.uri.factory.DirectoryUriUtils;
import org.cts2.uri.CodeSystemDirectoryURI;
import org.exolab.castor.xml.Marshaller;
import org.junit.Test;

public class DirectoryURIMarshallTest {

	@Test
	public void testMarshall() throws Exception{
		
		CodeSystemDirectory dir = new CodeSystemDirectory();
		
		CodeSystemDirectoryURI uri = DirectoryUriUtils.createUrlBasedDirectoryUri("http://test.org", CodeSystemDirectoryURI.class);
		
		dir.setNext(uri);
		
		StringWriter sw = new StringWriter();
		Marshaller.marshal(dir, sw);
		
		assertTrue(sw.toString().contains("next=\"http://test.org\""));
	}
}
