
package org.lexgrid.loader.rrf.data.property;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lexgrid.loader.rrf.model.Mrrank;

/**
 * The Class DefaultMrrankUtility.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefaultMrrankUtility implements MrrankUtility {

	/** The mrrank list. */
	private List<Mrrank> mrrankList;
	
	private Map<String,Integer> mrrankCache = new HashMap<String,Integer>();
	
	private static int NOT_FOUND_SCORE = -1;
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.rrf.data.property.MrrankUtility#getRank(java.lang.String, java.lang.String)
	 */
	public synchronized int getRank(String sab, String tty) {
		String key = this.getKey(sab, tty);
		
		if(! mrrankCache.containsKey(key)){
			for(Mrrank mrrank : mrrankList){
				if(mrrank.getSab().equals(sab) && mrrank.getTty().equals(tty)){
					int rank = Integer.parseInt(mrrank.getRank());
					mrrankCache.put(key, rank);
					return rank;
				}
			}
			mrrankCache.put(key, NOT_FOUND_SCORE);
			return NOT_FOUND_SCORE;
		}
		return mrrankCache.get(key);
	}
	
	private String getKey(String sab, String tty){
		return Integer.toString(sab.hashCode() + tty.hashCode());
	}

	/**
	 * Gets the mrrank list.
	 * 
	 * @return the mrrank list
	 */
	public List<Mrrank> getMrrankList() {
		return mrrankList;
	}

	/**
	 * Sets the mrrank list.
	 * 
	 * @param mrrankList the new mrrank list
	 */
	public void setMrrankList(List<Mrrank> mrrankList) {
		this.mrrankList = mrrankList;
	}
}