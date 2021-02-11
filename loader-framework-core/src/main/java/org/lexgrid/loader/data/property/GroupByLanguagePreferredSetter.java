
package org.lexgrid.loader.data.property;

import java.util.List;

import org.LexGrid.concepts.Presentation;

/**
 * The Class GroupByLanguagePreferredSetter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class GroupByLanguagePreferredSetter implements PreferredSetter<Presentation>{

	/**
	 * Assumes a sorted list.
	 * 
	 * @param properties the properties
	 */
	public void setPreferred(List<Presentation> properties) {
		String currentLanguage = "";
		for(Presentation prop : properties){
			String language = prop.getLanguage();
			if(!language.equals(currentLanguage)){
				currentLanguage = language;
				prop.setIsPreferred(true);
			}
		}
	}
}