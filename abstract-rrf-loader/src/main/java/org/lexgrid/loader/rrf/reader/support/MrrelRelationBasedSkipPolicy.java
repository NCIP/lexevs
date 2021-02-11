
package org.lexgrid.loader.rrf.reader.support;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.lexgrid.loader.reader.support.SkipPolicy;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;
import org.lexgrid.loader.rrf.model.Mrrel;

public class MrrelRelationBasedSkipPolicy implements SkipPolicy<Mrrel>{

/** The forward name list. */
private List<String> forwardNameList;
	
	public boolean toSkip(Mrrel item) {	
		String dirFlag = item.getDir();
		if(StringUtils.isNotEmpty(dirFlag)){
			if(dirFlag.equals(RrfLoaderConstants.ASSOC_DIR_ASSERTED_TRUE)){
				return false;
			} else if(dirFlag.equals(RrfLoaderConstants.ASSOC_DIR_ASSERTED_FALSE)){
				return true;
			}
		}
		return !processRelation(item.getRel());
	}
	
	/**
	 * Process relation.
	 * 
	 * @param relationName the relation name
	 * 
	 * @return true, if successful
	 */
	protected boolean processRelation(String relationName){
		if(StringUtils.isEmpty(relationName)){
			return false;
		}
		return forwardNameList.contains(relationName);
	}

	public List<String> getForwardNameList() {
		return forwardNameList;
	}

	public void setForwardNameList(List<String> forwardNameList) {
		this.forwardNameList = forwardNameList;
	}
}