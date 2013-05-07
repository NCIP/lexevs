package org.cts2.internal.mapper.converter;

import org.LexGrid.concepts.Definition;
import org.cts2.internal.lexevs.identity.LexEvsIdentityConverter;
import org.dozer.DozerConverter;

public class DefinitionPreferredToDefinitionRoleConverter extends
		DozerConverter<org.LexGrid.concepts.Definition, org.cts2.core.Definition> {
	private LexEvsIdentityConverter lexEvsIdentityConverter;

	public DefinitionPreferredToDefinitionRoleConverter() {
		super(org.LexGrid.concepts.Definition.class, org.cts2.core.Definition.class);
	}

	@Override
	public org.cts2.core.Definition convertTo(org.LexGrid.concepts.Definition source, org.cts2.core.Definition destination) {
		boolean b;
		if (source.getIsPreferred() == null)
			b= false;
		else
			b = source.getIsPreferred();
		destination.setDefinitionRole(this.lexEvsIdentityConverter
				.preferredtoDefinitionRole(b));
		return destination;
	}

	@Override
	public Definition convertFrom(org.cts2.core.Definition arg0, org.LexGrid.concepts.Definition arg1) {
		return null;
	}


	public void setLexEvsIdentityConverter(
			LexEvsIdentityConverter lexEvsIdentityConverter) {
		this.lexEvsIdentityConverter = lexEvsIdentityConverter;
	}

	public LexEvsIdentityConverter getLexEvsIdentityConverter() {
		return lexEvsIdentityConverter;
	}
}
