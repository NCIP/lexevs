package org.cts2.internal.mapper.converter;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.concepts.Presentation;
import org.cts2.core.ContextReference;
import org.cts2.entity.Designation;
import org.dozer.DozerConverter;

public class UsageContextListToContextReferenceListConverter extends DozerConverter<Presentation, Designation>{

	public UsageContextListToContextReferenceListConverter() {
		super(Presentation.class, Designation.class);
	}

	@Override
	public Designation convertTo(Presentation source, Designation destination) {
		for (String content: source.getUsageContext()) {
			ContextReference context = new ContextReference();
			context.setContent(content);
			destination.addUsageContext(context);
		}
		return destination;
	}

	@Override
	public Presentation convertFrom(Designation source, Presentation destination) {
		return null;
	}

	
}
