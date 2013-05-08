package org.cts2.internal.mapper.converter;

import org.LexGrid.concepts.Presentation;
import org.cts2.entity.Designation;
import org.cts2.internal.lexevs.identity.LexEvsIdentityConverter;
import org.dozer.DozerConverter;

public class PresentationPreferredToDesignationRoleConverter extends
		DozerConverter<Presentation, Designation> {
	private LexEvsIdentityConverter lexEvsIdentityConverter;

	public PresentationPreferredToDesignationRoleConverter() {
		super(Presentation.class, Designation.class);
	}

	@Override
	public Designation convertTo(Presentation source, Designation destination) {
		boolean b;
		if (source.getIsPreferred() == null)
			b= false;
		else
			b = source.getIsPreferred();
		destination.setDesignationRole(this.lexEvsIdentityConverter
				.preferredtoDestinationRole(b));
		return destination;
	}

	@Override
	public Presentation convertFrom(Designation source, Presentation destination) {
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
