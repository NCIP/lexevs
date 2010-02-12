package org.lexevs.registry.xmltransfer;

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.lexevs.registry.xmltransfer.RegistryXmlToDatabaseTransfer;
import org.lexevs.system.constants.SystemVariables;

public class RegistryXmlToDatabaseTransferTest {

	@Test
	public void doesRegistryXmlExist(){
		RegistryXmlToDatabaseTransfer transfer = new RegistryXmlToDatabaseTransfer();
		
		SystemVariables sv = createMock(SystemVariables.class);
		expect(sv.getAutoLoadRegistryPath()).andReturn("src/test/resources/registrytest/registry.xml");
		replay(sv);
		transfer.setSystemVariables(sv);
		
		assertTrue(transfer.doesRegistryXmlFileExist());
	}
}
