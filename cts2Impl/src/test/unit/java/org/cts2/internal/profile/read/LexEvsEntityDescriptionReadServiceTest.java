package org.cts2.internal.profile.read;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.annotation.Resource;

import org.cts2.core.EntityReference;
import org.cts2.entity.EntityDescription;
import org.cts2.entity.EntityList;
import org.cts2.entity.NamedEntityDescription;
import org.cts2.internal.model.resource.factory.EntityDescriptionFactory;
import org.cts2.service.core.EntityNameOrURI;
import org.cts2.service.core.NameOrURI;
import org.cts2.test.BaseCts2UnitTest;
import org.easymock.classextension.EasyMock;
import org.junit.Test;

public class LexEvsEntityDescriptionReadServiceTest extends BaseCts2UnitTest {
	@Resource
	private LexEvsEntityDescriptionReadService lexEvsEntityDescriptionReadService;
	@Resource
	private EntityDescriptionFactory entityDescriptionFactory;

	@Test
	public void testRead() {

		EntityNameOrURI id = new EntityNameOrURI();
		NameOrURI csId = new NameOrURI();

		EntityDescription ed = new NamedEntityDescription();
		ed.setAbout("test about");

		entityDescriptionFactory = EasyMock
				.createMock(EntityDescriptionFactory.class);
		EasyMock.expect(entityDescriptionFactory.getEntityDescription(id, csId))
				.andReturn(ed);
		EasyMock.replay(entityDescriptionFactory);
		lexEvsEntityDescriptionReadService
				.setEntityDescriptionFactory(entityDescriptionFactory);

		assertEquals("test about",
				lexEvsEntityDescriptionReadService.read(id, csId, null, null)
						.getAbout());
	}

	@Test
	public void testExists() {
		EntityNameOrURI id = new EntityNameOrURI();
		NameOrURI codeSystemVersion = new NameOrURI();
		EntityDescription ed = new NamedEntityDescription();

		entityDescriptionFactory = EasyMock
				.createMock(EntityDescriptionFactory.class);
		EasyMock.expect(
				entityDescriptionFactory.getEntityDescription(id,
						codeSystemVersion)).andReturn(ed);
		EasyMock.replay(entityDescriptionFactory);

		lexEvsEntityDescriptionReadService
				.setEntityDescriptionFactory(entityDescriptionFactory);

		assertEquals(true, lexEvsEntityDescriptionReadService.exists(id,
				codeSystemVersion, null, null));

	}

	@Test
	public void testAvailableDescriptions() {
		EntityNameOrURI id = new EntityNameOrURI();

		entityDescriptionFactory = EasyMock
				.createMock(EntityDescriptionFactory.class);
		EasyMock.expect(entityDescriptionFactory.availableDescriptions(id))
				.andReturn(new EntityReference());
		EasyMock.replay(entityDescriptionFactory);

		lexEvsEntityDescriptionReadService
				.setEntityDescriptionFactory(entityDescriptionFactory);
		
		assertNotNull(lexEvsEntityDescriptionReadService.availableDescriptions(id, null));

	}
	
	@Test
	public void testReadEntityDescriptions() {
		 EntityNameOrURI id = new EntityNameOrURI();
		 
		 entityDescriptionFactory = EasyMock.createMock(EntityDescriptionFactory.class);
		 EasyMock.expect(entityDescriptionFactory.getEntityDescriptionList(id)).andReturn(new EntityList());
		 EasyMock.replay(entityDescriptionFactory);
		 
		 lexEvsEntityDescriptionReadService.setEntityDescriptionFactory(entityDescriptionFactory);
		 
		 assertNotNull(lexEvsEntityDescriptionReadService.readEntityDescriptions(id, null, null));
		 
	}

	@Test
	public void testExistsInCodeSystem() {
		// lexEvsEntityDescriptionReadService.existsInCodeSystem(id, codeSystem,
		// tag, context);
	}

	@Test
	public void testReadByCodeSystem() {
		// lexEvsEntityDescriptionReadService.readByCodeSystem(id, codeSystem,
		// tag, queryControl, context);
	}
}
