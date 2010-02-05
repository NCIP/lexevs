package org.lexgrid.loader.rrf.processor;

import java.util.List;
import java.util.Map;

import org.LexGrid.persistence.model.EntityPropertyMultiAttrib;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.lexgrid.loader.dao.template.SupportedAttributeTemplate;
import org.lexgrid.loader.processor.MultipleEntityPropertyMultiAttribProcessor;

public class RrfMultipleEntityPropertyMultiAttribProcessor<I> extends MultipleEntityPropertyMultiAttribProcessor<I> {

	private Map<String,String> isoMap;
		
	@Override
	protected void registerSupportedAttributes(
			SupportedAttributeTemplate template,
			List<EntityPropertyMultiAttrib> qualifiers) {

		for(EntityPropertyMultiAttrib attrib : qualifiers){
			if(attrib.getId().getTypeName().equals(SQLTableConstants.TBLCOLVAL_SOURCE)){
				template.addSupportedSource(
						this.getCodingSchemeNameSetter().getCodingSchemeName(), 
						attrib.getId().getAttributeValue(), 
						isoMap.get(attrib.getId().getAttributeValue()),
						attrib.getId().getAttributeValue(),
						null);
			} else {
				template.addSupportedPropertyQualifier(
						this.getCodingSchemeNameSetter().getCodingSchemeName(), 
						attrib.getId().getAttributeValue(), 
						null, 
						attrib.getId().getAttributeValue());
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
