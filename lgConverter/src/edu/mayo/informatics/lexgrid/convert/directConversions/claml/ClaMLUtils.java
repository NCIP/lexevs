
package edu.mayo.informatics.lexgrid.convert.directConversions.claml;

import java.util.Arrays;
import java.util.List;

import org.LexGrid.LexBIG.claml.ClaML;
import org.LexGrid.LexBIG.claml.Meta;
import org.LexGrid.LexBIG.claml.UsageKind;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Text;
import org.apache.commons.lang.StringUtils;

import edu.mayo.informatics.lexgrid.convert.directConversions.claml.config.ClaMLConfig;

/**
 * The Class ClaMLUtils.
 */
public class ClaMLUtils {
    
	/**
	 * Creates the property qualifier.
	 * 
	 * @param config the config
	 * @param usageQualifier the usage qualifier
	 * 
	 * @return the property qualifier
	 */
	public static PropertyQualifier createPropertyQualifier(ClaMLConfig config, UsageKind usageQualifier)	{
		PropertyQualifier qual = new PropertyQualifier();
		qual.setPropertyQualifierName(config.getUsageAssociationQualifier());
		
		Text text = new Text();
		text.setContent(usageQualifier.getName());
		qual.setValue(text);
		
		return qual;
	}
	
	/**
	 * Gets the hierarchy roots.
	 * 
	 * @param claml the claml
	 * 
	 * @return the hierarchy roots
	 */
	public static List<String> getHierarchyRoots(ClaML claml){
		for(Meta meta : claml.getMeta()){
			if(meta.getName().equals(ClaMLConstants.META_TOP_LEVEL_SORT)){
				return Arrays.asList(StringUtils.split(meta.getValue(), ' '));
			}
		}
		
		throw new RuntimeException("Root nodes not found");
	}
}