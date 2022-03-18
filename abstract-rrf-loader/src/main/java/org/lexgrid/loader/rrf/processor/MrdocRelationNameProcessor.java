
package org.lexgrid.loader.rrf.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.lexgrid.loader.rrf.model.Mrdoc;
import org.springframework.batch.item.ItemProcessor;

/**
 * The Class MrdocRelationNameProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrdocRelationNameProcessor implements ItemProcessor<Mrdoc, String>{
	
	/** The forward name comparator. */
	private Comparator<String> forwardNameComparator = new ForwardNameComparator();
	
	/** The processed names. */
	private List<String> processedNames = new ArrayList<String>();

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	public String process(Mrdoc item) throws Exception {
		String firstName = item.getValue();
		String secondName = item.getExpl();

		if(!processedNames.contains(firstName) && !processedNames.contains(secondName)){
			if(forwardNameComparator.compare(firstName, secondName) > 0){
				return firstName;
			} else {
				return secondName;
			}
		}
		return null;
	}

	// *_of* is reverse
	// *_by* is reverse
	/**
	 * The Class ForwardNameComparator.
	 * 
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	private class ForwardNameComparator implements Comparator<String> {
		
		/** The inverse keys. */
		private final List<String> inverseKeys = Arrays.asList("PAR");
		
		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(String name1, String name2) {
			processedNames.add(name1);
			processedNames.add(name2);
			
			if(containsStringInList(name1) && !containsStringInList(name2)){
				return -1;
			}
			
			if(containsStringInList(name2) && !containsStringInList(name1)){
				return 1;
			}

			return 1;
		}
		
		/**
		 * Contains string in list.
		 * 
		 * @param name the name
		 * 
		 * @return true, if successful
		 */
		protected boolean containsStringInList(String name){
			for(String key : inverseKeys){
				if(name.contains(key)){
					return true;
				}
			}
			return false;
		}
		
	}
	
}