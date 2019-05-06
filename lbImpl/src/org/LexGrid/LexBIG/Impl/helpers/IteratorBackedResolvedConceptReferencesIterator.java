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

import java.util.Iterator;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.annotations.LgClientSideSafe;
import org.LexGrid.annotations.LgProxyClass;

@LgProxyClass
public class IteratorBackedResolvedConceptReferencesIterator implements ResolvedConceptReferencesIterator {

    private static final long serialVersionUID = -9172975996526240085L;
    
    public static int UNKNOWN_NUMBER = -1;
    private int count;

    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }

    protected Iterator<ResolvedConceptReference> iterator;
    
    public IteratorBackedResolvedConceptReferencesIterator(){
    }
    public IteratorBackedResolvedConceptReferencesIterator(
            Iterator<ResolvedConceptReference> iterator) {
        this(iterator, UNKNOWN_NUMBER);
    }
    
    public IteratorBackedResolvedConceptReferencesIterator(
            Iterator<ResolvedConceptReference> iterator,
            int count) {
        this.iterator = iterator;
        this.count = count;
    }
    @Override
    public ResolvedConceptReferenceList get(int arg0, int arg1) throws LBResourceUnavailableException,
            LBInvocationException, LBParameterException {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResolvedConceptReferenceList getNext() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResolvedConceptReference next() throws LBResourceUnavailableException, LBInvocationException {
        if(count != UNKNOWN_NUMBER && count > 0) {
            count--;
        }
        return this.iterator.next();
    }

    @Override
    public ResolvedConceptReferenceList next(int page) throws LBResourceUnavailableException, LBInvocationException {
        ResolvedConceptReferenceList returnList = new ResolvedConceptReferenceList();
        for(int i=0;i<page && this.iterator.hasNext();i++) {
            returnList.addResolvedConceptReference(this.iterator.next());
        }
        if(count != UNKNOWN_NUMBER) {
            count -= returnList.getResolvedConceptReferenceCount();
            if(count < 0){
                count = 0;
            }
        }
        return returnList;
    }

    @Override
    public ResolvedConceptReferencesIterator scroll(int arg0) throws LBResourceUnavailableException,
            LBInvocationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasNext() throws LBResourceUnavailableException {
        boolean hasNext = this.iterator.hasNext();
        if(!hasNext){
            count = 0;
        }
        return hasNext;
    }

    @Override
    public int numberRemaining() throws LBResourceUnavailableException {
        return this.count;
    }

    @Override
    public void release() throws LBResourceUnavailableException {
        //no-op
    }
}