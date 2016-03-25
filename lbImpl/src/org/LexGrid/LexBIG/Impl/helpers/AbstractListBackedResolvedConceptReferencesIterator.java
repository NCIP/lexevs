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
package org.LexGrid.LexBIG.Impl.helpers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.annotations.LgProxyClass;

public abstract class AbstractListBackedResolvedConceptReferencesIterator<T> implements ResolvedConceptReferencesIterator {

    private static final long serialVersionUID = -9172975996526240085L;
    
    private int pos = 0;
    
    private TransformerExecutor<T> transformerExecutor = new TransformerExecutor<T>();
   
    transient protected List<T> list;
    private Transformer<T> transformer;

    protected AbstractListBackedResolvedConceptReferencesIterator(List<T> list, Transformer<T> transformer){
        super();
        this.list = list;
        this.transformer = transformer;
    }
    
    public static interface Transformer<T> extends Serializable {
        public ResolvedConceptReferenceList transform(Iterable<T> items);
    }
    
    @LgProxyClass
    public static class TransformerExecutor<T> implements Serializable {

        private static final long serialVersionUID = -6065733883014163743L;

        public ResolvedConceptReferenceList transform(Transformer<T> transformer, Iterable<T> items) {
            return transformer.transform(items);
        }
        
    }

    @Override
    public int numberRemaining() throws LBResourceUnavailableException {
        return this.list.size() - this.pos;
    }

    @Override
    public void release() throws LBResourceUnavailableException {
        //no-op
    }

    @Override
    public boolean hasNext() throws LBResourceUnavailableException {
        return this.pos < this.list.size();
    }

    @Override
    public ResolvedConceptReference next() throws LBResourceUnavailableException, LBInvocationException {
        ResolvedConceptReferenceList result = this.next(1);
        
        if(result == null || result.getResolvedConceptReferenceCount() == 0){
            return null;
        } else {
            if(result.getResolvedConceptReferenceCount() != 1){
                throw new IllegalStateException("Must have one and only one result.");
            }
            
            return result.getResolvedConceptReference(0);
        }
    }

    @Override
    public ResolvedConceptReferenceList next(int maxToReturn) throws LBResourceUnavailableException,
            LBInvocationException {
        ResolvedConceptReferenceList results = 
               this.transformerExecutor.transform(
                      this.transformer, 
                      new ArrayList<T>(this.list.subList(pos, this.adjustEndPos(pos + maxToReturn))));
        
        pos += results.getResolvedConceptReferenceCount();
        
        return results;
    }

    @Override
    public ResolvedConceptReferenceList get(int start, int end) throws LBResourceUnavailableException,
            LBInvocationException, LBParameterException {
        List<T> subList = this.list.subList(start, this.adjustEndPos(end));
        
        return this.transformerExecutor.transform(this.transformer, subList);
    }

    private int adjustEndPos(int requestedEnd){
        if(requestedEnd < this.list.size()){
        	return requestedEnd;
        } else {
        	return this.list.size();
        }
    }
    
    @Override
    public ResolvedConceptReferencesIterator scroll(int maxToReturn) throws LBResourceUnavailableException,
            LBInvocationException {
        throw new UnsupportedOperationException("Scroll unsupported.");
    }

    @Override
    public ResolvedConceptReferenceList getNext() {
        throw new UnsupportedOperationException("GetNext unsupported.");
    }
}