package org.lexgrid.loader.processor;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.LexGrid.relations.AssociationSource;
import org.lexgrid.loader.dao.template.SupportedAttributeTemplate;
import org.lexgrid.loader.wrappers.ParentIdHolder;


public class MedRtEntityAssnsToEntityProcessor<I> extends EntityAssnsToEntityProcessor<I> {
	
	/** The iso map. */
	private Map<String,String> isoMap;
	
	@Override
	protected void registerSupportedAttributes(SupportedAttributeTemplate template,
			ParentIdHolder<AssociationSource> item) {

		// only registers the association
		super.registerSupportedAttributes(template, item);

		// likely not needed -- there is only one target
		Set<String> set = item.getItem().getTargetAsReference().stream().map(x -> x.getTargetEntityCodeNamespace())
				.distinct().collect(Collectors.toSet());
		// namespaces might be the same we'll eliminate them as duplicates
		set.add(item.getItem().getSourceEntityCodeNamespace());
		//These are all sabs, we'll see if they are MED-RT, if not we'll create
		//supported attibutes for external value sets
		for (String sab : set) {
			if (!this.getCodingSchemeIdSetter().getCodingSchemeName().equals(sab)) {
				this.getSupportedAttributeTemplate().addSupportedCodingScheme(sab, null, sab, isoMap.get(sab), sab,
						false);
				this.getSupportedAttributeTemplate().addSupportedNamespace(sab, null, sab, isoMap.get(sab), sab, sab);
			}
		}
	}

	public Map<String, String> getIsoMap() {
		return isoMap;
	}

	public void setIsoMap(Map<String, String> isoMap) {
		this.isoMap = isoMap;
	}

}
