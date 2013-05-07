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
package org.lexevs.cts2.author;

import org.lexevs.cts2.BaseService;

/**
 * Class returns individual CTS 2 Authoring Operation interfaces.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public class AuthoringOperationImpl extends BaseService implements AuthoringOperation {

	private transient ValueSetAuthoringOperation valueSetAuthop_;
	private transient CodeSystemAuthoringOperation codeSystemAuthop_;
	private transient ConceptDomainAuthoringOperation conceptDomainAuthOp_;
	private transient UsageContextAuthoringOperation usageContextAuthOp_;
	private transient AssociationAuthoringOperation assnAuthOp_;

	
	/* (non-Javadoc)
	 * @see org.lexevs.cts2.author.AuthoringOperation#getAssociationAuthoringOperation()
	 */
	@Override
	public AssociationAuthoringOperation getAssociationAuthoringOperation() {
		if (assnAuthOp_ == null)
			assnAuthOp_ = new AssociationAuthoringOperationImpl();
		return assnAuthOp_;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.author.AuthoringOperation#getCodeSystemAuthoringOperation()
	 */
	@Override
	public CodeSystemAuthoringOperation getCodeSystemAuthoringOperation() {
		if (codeSystemAuthop_ == null)
			codeSystemAuthop_  = new CodeSystemAuthoringOperationImpl();
		return codeSystemAuthop_ ;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.author.AuthoringOperation#getConceptDomainAuthoringOperation()
	 */
	@Override
	public ConceptDomainAuthoringOperation getConceptDomainAuthoringOperation() {
		if (conceptDomainAuthOp_ == null)
			conceptDomainAuthOp_ = new ConceptDomainAuthoringOperationImpl();
		return conceptDomainAuthOp_;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.author.AuthoringOperation#getUsageContextAuthoringOperation()
	 */
	@Override
	public UsageContextAuthoringOperation getUsageContextAuthoringOperation() {
		if (usageContextAuthOp_ == null)
			usageContextAuthOp_ = new UsageContextAuthoringOperationImpl();
		return usageContextAuthOp_;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.author.AuthoringOperation#getValueSetAuthoringOperation()
	 */
	@Override
	public ValueSetAuthoringOperation getValueSetAuthoringOperation() {
		if (valueSetAuthop_ == null)
			valueSetAuthop_ = new ValueSetAuthoringOperationImpl();
		return valueSetAuthop_;
	}

}