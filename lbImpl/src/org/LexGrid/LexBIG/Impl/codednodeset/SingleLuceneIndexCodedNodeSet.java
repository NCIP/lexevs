/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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
package org.LexGrid.LexBIG.Impl.codednodeset;

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Impl.CodedNodeSetImpl;
import org.LexGrid.LexBIG.Impl.codedNodeSetOperations.Union;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;

/**
 * Implementation of the CodedNodeSet Interface.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:rokickik@mail.nih.gov">Konrad Rokicki</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class SingleLuceneIndexCodedNodeSet extends CodedNodeSetImpl {
    
    private static final long serialVersionUID = -5959522938971242708L;
    
    public SingleLuceneIndexCodedNodeSet() {
        super();
    }
    
    public SingleLuceneIndexCodedNodeSet(
            String codingScheme, 
            CodingSchemeVersionOrTag tagOrVersion, 
            Boolean activeOnly, 
            LocalNameList types) throws LBInvocationException, LBParameterException, LBResourceUnavailableException {
        super(codingScheme,tagOrVersion,activeOnly, types);
    }
    
    public CodedNodeSet union(CodedNodeSet codes) throws LBInvocationException, LBParameterException {
        if(codes instanceof CodedNodeSetImpl ) {
            CodedNodeSetImpl unioned = (CodedNodeSetImpl)codes;
            
            return new UnionSingleLuceneIndexCodedNodeSet(this, unioned);
        } else {
           return super.union(codes);
        }
    }
    
    public CodedNodeSet intersect(CodedNodeSet codes) throws LBInvocationException, LBParameterException {
        if(codes instanceof CodedNodeSetImpl ) {
            CodedNodeSetImpl intersected = (CodedNodeSetImpl)codes;
            
            return new IntersectSingleLuceneIndexCodedNodeSet(this, intersected);
        } else {
           return super.union(codes);
        }
    }
    
    public CodedNodeSet difference(CodedNodeSet codes) throws LBInvocationException, LBParameterException {
        if(codes instanceof CodedNodeSetImpl ) {
            CodedNodeSetImpl diffed = (CodedNodeSetImpl)codes;
            
            return new DifferenceSingleLuceneIndexCodedNodeSet(this, diffed);
        } else {
           return super.union(codes);
        }
    }
    
    @Override
    protected void doUnion(String internalCodeSystemName, String internalVersionString, Union union) throws LBException {
       //
    }
}