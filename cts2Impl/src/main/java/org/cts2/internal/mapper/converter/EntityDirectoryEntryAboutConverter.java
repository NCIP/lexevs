package org.cts2.internal.mapper.converter;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.codingSchemes.CodingScheme;
import org.cts2.entity.EntityDirectoryEntry;
import org.cts2.internal.lexevs.identity.LexEvsIdentityConverter;
import org.dozer.DozerConverter;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.locator.LexEvsServiceLocator;

public class EntityDirectoryEntryAboutConverter extends
		DozerConverter<ResolvedConceptReference, EntityDirectoryEntry> {
	private LexEvsIdentityConverter lexEvsIdentityConverter;

	public EntityDirectoryEntryAboutConverter() {
		super(ResolvedConceptReference.class, EntityDirectoryEntry.class);
	}

	@Override
	public ResolvedConceptReference convertFrom(EntityDirectoryEntry arg0,
			ResolvedConceptReference arg1) {
		return null;
	}

	@Override
	public EntityDirectoryEntry convertTo(ResolvedConceptReference conRef,
			EntityDirectoryEntry dirEntry) {

		CodingSchemeService service = LexEvsServiceLocator.getInstance()
				.getDatabaseServiceManager().getCodingSchemeService();

		CodingScheme cs = service.getCodingSchemeByUriAndVersion(
				conRef.getCodingSchemeURI(), conRef.getCodingSchemeVersion());

		String uri = this.lexEvsIdentityConverter.namespaceAndCodeToUri(cs,
				conRef.getCodeNamespace(), conRef.getCode());
		
		dirEntry.setAbout(uri);

		return dirEntry;
	}

	public LexEvsIdentityConverter getLexEvsIdentityConverter() {
		return this.lexEvsIdentityConverter;
	}

	public void setLexEvsIdentityConverter(
			LexEvsIdentityConverter lexEvsIdentityConverter) {
		this.lexEvsIdentityConverter = lexEvsIdentityConverter;
	}

}
