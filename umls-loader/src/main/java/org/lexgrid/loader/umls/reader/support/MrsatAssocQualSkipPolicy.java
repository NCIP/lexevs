package org.lexgrid.loader.umls.reader.support;

import org.lexgrid.loader.rrf.model.Mrsat;

public class MrsatAssocQualSkipPolicy extends AbstractSabSkippingPolicy<Mrsat> {


	public boolean toSkip(Mrsat item) {
		if(!item.getStype().equals("RUI")) {
			return true;
		}
		
		if(super.toSkip(item)){
			return true;
		}
		
		return false;
	}
	
	@Override
	public String getSab(Mrsat item) {
		return item.getSab();
	}

}
