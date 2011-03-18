package org.cts2.internal.mapper.converter;

import org.cts2.core.ContextReference;
import org.dozer.DozerConverter;

public class DefinitionUsageContextListToContextReferenceListConverter extends DozerConverter<org.LexGrid.concepts.Definition, org.cts2.core.Definition>{

	public DefinitionUsageContextListToContextReferenceListConverter() {
		super(org.LexGrid.concepts.Definition.class, org.cts2.core.Definition.class);
	}

	@Override
	public org.cts2.core.Definition convertTo(org.LexGrid.concepts.Definition source, org.cts2.core.Definition destination) {
		for (String content: source.getUsageContext()) {
			ContextReference context = new ContextReference();
			context.setContent(content);
			//TODO not an array???
			destination.setUsageContext(context);
			break;
		}
		return destination;
	}

	@Override
	public org.LexGrid.concepts.Definition convertFrom(org.cts2.core.Definition source, org.LexGrid.concepts.Definition destination) {
		return null;
	}

	
}
