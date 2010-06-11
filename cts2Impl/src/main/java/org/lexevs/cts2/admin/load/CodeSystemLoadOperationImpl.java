/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and Research
 * (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the triple-shield Mayo
 * logo are trademarks and service marks of MFMER.
 * 
 * Except as contained in the copyright notice above, or as used to identify
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 * 
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.lexevs.cts2.admin.load;

import java.net.URI;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder;
import org.LexGrid.LexBIG.Impl.loaders.BaseLoader;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.versions.Revision;
import org.lexevs.cts2.BaseService.LoadFormats;

import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

/**
 * @author m004181
 *
 */
public class CodeSystemLoadOperationImpl extends BaseLoader implements CodeSystemLoadOperation {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String name = "LexEVSCTS2CodeSystemLoader";
    public static final String description = "This loader loads Code System into the LexGrid database.";
	
	/* (non-Javadoc)
	 * @see org.LexGrid.LexBIG.Impl.loaders.BaseLoader#declareAllowedOptions(org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder)
	 */
	@Override
	protected OptionHolder declareAllowedOptions(OptionHolder holder) {
		return holder;
	}

	/* (non-Javadoc)
	 * @see org.LexGrid.LexBIG.Impl.loaders.BaseLoader#doLoad()
	 */
	@Override
	protected URNVersionPair[] doLoad() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable#buildExtensionDescription()
	 */
	@Override
	protected ExtensionDescription buildExtensionDescription() {
		ExtensionDescription temp = new ExtensionDescription();
        temp.setExtensionBaseClass(CodeSystemLoadOperationImpl.class.getInterfaces()[0].getName());
        temp.setExtensionClass(CodeSystemLoadOperationImpl.class.getName());
        temp.setDescription(description);
        temp.setName(name);

        return temp;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.admin.load.CodeSystemLoadOperation#changeCodeSystemStatus()
	 */
	@Override
	public void changeCodeSystemStatus() throws LBException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.admin.load.CodeSystemLoadOperation#importCodeSystem()
	 */
	@Override
	public int importCodeSystem() throws LBException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.admin.load.CodeSystemLoadOperation#importCodeSystemRevsion(org.LexGrid.versions.Revision)
	 */
	@Override
	public int importCodeSystemRevsion(Revision codeSystemRevision)
			throws LBException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.admin.load.CodeSystemLoadOperation#importCodeSystemRevsion(java.lang.String)
	 */
	@Override
	public int importCodeSystemRevsion(String xmlFileLocation)
			throws LBException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void validate(URI source, URI metaData, int validationLevel)
			throws LBException {
		// TODO Auto-generated method stub
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.admin.load.CodeSystemLoadOperation#load(java.net.URI, java.net.URI, org.lexevs.cts2.BaseService.LoadFormats, java.lang.Boolean, java.lang.Boolean)
	 */
	@Override
	public URI load(URI source, URI metadata, LoadFormats loadFormat, Boolean stopOnErrors, Boolean async) throws LBException{
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String[] args){
//		CodeSystemLoadOperation csLoad = new CodeSystemLoadOperationImpl();
//		csLoad.
	}

	@Override
	public URI load(CodingScheme codeSystem, URI metadata,
			Boolean stopOnErrors, Boolean async) throws LBException {
		// TODO Auto-generated method stub
		return null;
	}	
}
