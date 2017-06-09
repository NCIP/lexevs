package org.LexGrid.LexBIG.Impl.loaders;

/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *      http://www.eclipse.org/legal/epl-v10.html
 * 
 */

import java.net.URI;
import java.net.URISyntaxException;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Load.OntologyFormat;
import org.LexGrid.LexBIG.Extensions.Load.SourceAssertedVStoCodingSchemeLoader;
import org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder;
import org.LexGrid.codingSchemes.CodingScheme;

import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

public class SourceAssertedVStoCodingSchemLoaderImpl extends BaseLoader implements
SourceAssertedVStoCodingSchemeLoader {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4792994226238454359L;

    private CodingScheme scheme;

	@Override
	    protected OptionHolder declareAllowedOptions(OptionHolder holder) {
	        return holder;
	    }

	    @Override
	    protected URNVersionPair[] doLoad() throws Exception {
	        this.persistCodingSchemeToDatabase(scheme);	        
	        return this.constructVersionPairsFromCodingSchemes(scheme);
	    }

	    @Override
	    protected ExtensionDescription buildExtensionDescription() {
	        ExtensionDescription temp = new ExtensionDescription();
	        temp.setExtensionBaseClass(SourceAssertedVStoCodingSchemLoaderImpl.class.getInterfaces()[0].getName());
	        temp.setExtensionClass(SourceAssertedVStoCodingSchemLoaderImpl.class.getName());
	        temp.setDescription(SourceAssertedVStoCodingSchemeLoader.DESCRIPTION);
	        temp.setName(SourceAssertedVStoCodingSchemeLoader.NAME);
	        return temp;
	    }

	    public void validate(URI source, int validationLevel) throws LBException {
	        // TODO Auto-generated method stub
	    }


		@Override
		public void load(CodingScheme scheme){
		this.scheme = scheme;
	    try {
            this.load(new URI(scheme.getCodingSchemeURI()));
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		}
		
	
	    @Override
	    public OntologyFormat getOntologyFormat() {
	        return OntologyFormat.RESOLVEDVALUESET;
	    }


}
