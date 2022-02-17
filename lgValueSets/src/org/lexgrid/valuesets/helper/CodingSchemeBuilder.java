
package org.lexgrid.valuesets.helper;

import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.SupportedLanguage;
import org.LexGrid.naming.SupportedSource;
import org.apache.commons.lang.StringUtils;


public class CodingSchemeBuilder {
	
	public CodingScheme build(String csURI, String csName, String csVersion, String csFormalName, 
			List<String> csLocalNameList, String defaultLang, String copyRight, List<Source> sourceList)
			throws LBException
	{
		if (StringUtils.isEmpty(csURI) || StringUtils.isEmpty(csName))
			throw new LBException("Invalid Data : Coding Scheme URI and Name can not be empty");
		
		CodingScheme cs = new CodingScheme();
		cs.setCodingSchemeURI(csURI);
		cs.setCodingSchemeName(csName);
		cs.setRepresentsVersion(csVersion);
		cs.setFormalName(csFormalName);
		cs.setDefaultLanguage(defaultLang);
		if (sourceList != null)
			cs.setSource(sourceList);
		if (csLocalNameList != null)
			cs.setLocalName(csLocalNameList);
		if (copyRight != null)
		{
			Text text = new Text();
			text.setContent(copyRight);
			cs.setCopyright(text);
		}
		
		Mappings mappings = new Mappings();
		
		SupportedCodingScheme suppCS = new SupportedCodingScheme();
		suppCS.setLocalId(csFormalName);
		suppCS.setUri(csURI);
		mappings.addSupportedCodingScheme(suppCS);
		
		if (csLocalNameList != null)
			for (String localName : csLocalNameList)
			{
				suppCS = new SupportedCodingScheme();
				suppCS.setLocalId(localName);
				suppCS.setUri(csURI);
				mappings.addSupportedCodingScheme(suppCS);
			}
		
		if (defaultLang != null)
		{
			SupportedLanguage suppLang = new SupportedLanguage();
			suppLang.setLocalId(defaultLang);
			mappings.addSupportedLanguage(suppLang);
		}
		
		if (sourceList != null)
			for (Source src : sourceList)
			{
				SupportedSource suppSrc = new SupportedSource();
				suppSrc.setLocalId(src.getContent());
				suppSrc.setAssemblyRule(src.getRole());
				mappings.addSupportedSource(suppSrc);
			}
		
		cs.setMappings(mappings);
		return cs;
	}
}