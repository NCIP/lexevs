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
//		List<ContextReference> list = new ArrayList<ContextReference>();
		for (String content: source.getUsageContext()) {
			ContextReference context = new ContextReference();
			context.setContent(content);
			destination.addUsageContext(context);
//			list.add(context);
		}
//		destination.setUsageContext((ContextReference[]) list.toArray());
		return destination;
	}

	@Override
	public Presentation convertFrom(Designation source, Presentation destination) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
