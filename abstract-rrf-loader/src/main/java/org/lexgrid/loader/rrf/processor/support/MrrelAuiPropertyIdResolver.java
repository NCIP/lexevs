
package org.lexgrid.loader.rrf.processor.support;

import org.lexgrid.loader.processor.support.PropertyIdResolver;
import org.lexgrid.loader.rrf.model.Mrrel;
import org.springframework.util.Assert;

public class MrrelAuiPropertyIdResolver implements PropertyIdResolver<Mrrel>{

	public enum Aui {AUI1, AUI2};
	
	private Aui aui;
	
	public String getPropertyId(Mrrel item) {
		Assert.notNull(aui);
		if(aui.equals(Aui.AUI1)) {
			return item.getAui1();
		} else if(aui.equals(Aui.AUI2)) {
			return item.getAui2();
		}
		
		else throw new RuntimeException("Cannot get Aui Property Id.");	
	}

	public Aui getAui() {
		return aui;
	}

	public void setAui(Aui aui) {
		this.aui = aui;
	}
}