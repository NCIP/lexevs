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
package org.lexevs.cts2.query;

import org.lexevs.cts2.BaseService;

/**
 * Class returns individual CTS 2 Query Operation interfaces.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public class QueryOperationImpl extends BaseService implements QueryOperation {
	private transient ValueSetQueryOperation valueSetQueryOp_;
	private transient ConceptDomainQueryOperation conceptDomainQueryOp_;
	private transient UsageContextQueryOperation usageContextQueryOp_;
	private transient AssociationQueryOperation assnQueryOp_;
	private transient CodeSystemQueryOperation codeSystemQueryOp_;
	
	/* (non-Javadoc)
	 * @see org.lexevs.cts2.query.QueryOperation#getAssociationQueryOperation()
	 */
	@Override
	public AssociationQueryOperation getAssociationQueryOperation() {
		if (assnQueryOp_ == null)
			assnQueryOp_ = new AssociationQueryOperationImpl();
		return assnQueryOp_;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.query.QueryOperation#getCodeSystemQueryOperation()
	 */
	@Override
	public CodeSystemQueryOperation getCodeSystemQueryOperation() {
		if (codeSystemQueryOp_ == null)
			codeSystemQueryOp_ = new CodeSystemQueryOperationImpl();
		return codeSystemQueryOp_;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.query.QueryOperation#getConceptDomainQueryOperation()
	 */
	@Override
	public ConceptDomainQueryOperation getConceptDomainQueryOperation() {
		if (conceptDomainQueryOp_ == null)
			conceptDomainQueryOp_ = new ConceptDomainQueryOperationImpl();
		
		return conceptDomainQueryOp_;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.query.QueryOperation#getUsageContextQueryOperation()
	 */
	@Override
	public UsageContextQueryOperation getUsageContextQueryOperation() {
		if (usageContextQueryOp_ == null)
			usageContextQueryOp_ = new UsageContextQueryOperationImpl();
		
		return usageContextQueryOp_;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.query.QueryOperation#getValueSetQueryOperation()
	 */
	@Override
	public ValueSetQueryOperation getValueSetQueryOperation() {
		if (valueSetQueryOp_ == null)
			valueSetQueryOp_ = new ValueSetQueryOperationImpl();
		return valueSetQueryOp_;
	}
}