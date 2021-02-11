
package org.lexgrid.loader.rrf.processor.support;

import org.lexgrid.loader.rrf.model.Mrrel;

public class MrrelAuiResolver implements AuiResolver<Mrrel> {

	public enum AuiType {SOURCE_AUI, TARGET_AUI};
	
	private AuiType auiToReturn;
	
	public String getAui(Mrrel item) {
		if(auiToReturn == null){
			throw new RuntimeException("Please Specify an AUI type.");
		}
		
		if(auiToReturn.equals(AuiType.SOURCE_AUI)){
			return item.getAui1();
		} else {
			return item.getAui2();
		}
	}

	public AuiType getAuiToReturn() {
		return auiToReturn;
	}

	public void setAuiToReturn(AuiType auiToReturn) {
		this.auiToReturn = auiToReturn;
	}
}