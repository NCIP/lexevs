
package org.lexgrid.loader;

import java.net.URI;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Load.OntologyFormat;
import org.LexGrid.LexBIG.Extensions.Load.ResolvedValueSetDefinitionLoader;
import org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder;
import org.LexGrid.LexBIG.Impl.loaders.BaseLoader;
import org.LexGrid.codingSchemes.CodingScheme;

import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

public class ResolvedValueSetDefinitionLoaderImpl extends BaseLoader implements
		ResolvedValueSetDefinitionLoader {

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
	 * 		http://www.eclipse.org/legal/epl-v10.html
	 * 
	 */
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4792994226238454359L;
	URI valueSetDefinitionURI;
	String valueSetDefinitionRevisionId;
	AbsoluteCodingSchemeVersionReferenceList csVersionList;
	String csVersionTag;
	String vsVersion;
	@Override
	    protected OptionHolder declareAllowedOptions(OptionHolder holder) {
	        return holder;
	    }

	    @Override
	    protected URNVersionPair[] doLoad() throws Exception {
	    	TransformValueSetDefinitionToCodingScheme tvsd2cs= new TransformValueSetDefinitionToCodingScheme(valueSetDefinitionURI,
					 valueSetDefinitionRevisionId,
					 csVersionList,
					 csVersionTag,
					 vsVersion);
	        CodingScheme codingScheme = tvsd2cs.transform();
	        
	        this.persistCodingSchemeToDatabase(codingScheme);
	        
	        return this.constructVersionPairsFromCodingSchemes(codingScheme);
	    }

	    @Override
	    protected ExtensionDescription buildExtensionDescription() {
	        ExtensionDescription temp = new ExtensionDescription();
	        temp.setExtensionBaseClass(ResolvedValueSetDefinitionLoaderImpl.class.getInterfaces()[0].getName());
	        temp.setExtensionClass(ResolvedValueSetDefinitionLoaderImpl.class.getName());
	        temp.setDescription(ResolvedValueSetDefinitionLoader.DESCRIPTION);
	        temp.setName(ResolvedValueSetDefinitionLoader.NAME);

	        return temp;
	    }

	    public void validate(URI source, int validationLevel) throws LBException {
	        // TODO Auto-generated method stub
	        
	    }


		@Override
		public void load(URI valueSetDefinitionURI,
				String valueSetDefinitionRevisionId,
				AbsoluteCodingSchemeVersionReferenceList csVersionList,
				String csVersionTag, 
				String vsVersion) throws Exception {
			this.valueSetDefinitionURI= valueSetDefinitionURI;
			this.valueSetDefinitionRevisionId= valueSetDefinitionRevisionId;
			this.csVersionList= csVersionList;
			this.csVersionTag= csVersionTag;
			this.vsVersion= vsVersion;
			this.load(valueSetDefinitionURI);
			
		}
		
	
	    @Override
	    public OntologyFormat getOntologyFormat() {
	        return OntologyFormat.RESOLVEDVALUESET;
	    }

	    //Used for the lbGUI implementation
		public void loadResolvedValueSet(String valueSetDefinitionURI, String valueSetDefinitionRevisionId, AbsoluteCodingSchemeVersionReferenceList csVersionList,
				String csVersionTag)throws Exception {
			this.valueSetDefinitionURI= new URI(valueSetDefinitionURI);
			this.valueSetDefinitionRevisionId= valueSetDefinitionRevisionId;
			this.csVersionList= csVersionList;
			this.csVersionTag= csVersionTag;
			this.load(new URI(valueSetDefinitionURI));
		}

}