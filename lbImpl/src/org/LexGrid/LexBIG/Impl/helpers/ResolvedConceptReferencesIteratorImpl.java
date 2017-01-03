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

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Extensions.Query.Filter;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.LexGrid.annotations.LgClientSideSafe;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.logging.LoggerFactory;

/**
 * Implements iterator behavior for fetching coded node sets.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 * @version subversion $Revision: $ checked in on $Date: $
 */
@LgClientSideSafe
public class ResolvedConceptReferencesIteratorImpl implements ResolvedConceptReferencesIterator, Cloneable {
    private static final long serialVersionUID = 5609297086069631237L;
    private int pos_ = 0;
    private long lastRead_;
    private CodeHolder codesToReturn_;
    private Boolean[] filterCheck_;
    private LocalNameList restrictToProperties_;
    private PropertyType[] restrictToPropertyTypes_;
    private Filter[] filters_;
    private transient CleanUpThread cuti_;
    private transient Thread cut_;
    private boolean resolveEntities_;
    int maxSizeSystemLimit = 0;
    private boolean isExhausted = false;
    
    private CodeToReturnResolver codeToReturnResolver = new DefaultCodeToReturnResolver();

    private LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }

    private ResolvedConceptReferenceList ref;

    public ResolvedConceptReferencesIteratorImpl() {
    };

    public ResolvedConceptReferencesIteratorImpl(CodeHolder codes, LocalNameList restrictToProperties,
            PropertyType[] restrictToPropertyTypes, Filter[] filters, boolean resolveEntities) {
        try {
            codesToReturn_ = codes.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        filters_ = filters;
        lastRead_ = System.currentTimeMillis();
        restrictToProperties_ = restrictToProperties;
        restrictToPropertyTypes_ = restrictToPropertyTypes;
        resolveEntities_ = resolveEntities;
        
        maxSizeSystemLimit = 
               LexEvsServiceLocator.getInstance().getSystemResourceService().
                   getSystemVariables().getMaxResultSize();

        // launch a clean up thread to recover the memory from this object.
        // this allows the JVM to exit while this thread is still active.
        cuti_ = new CleanUpThread();
        cut_ = new Thread(cuti_);
        cut_.setDaemon(true);
        cut_.start();
    }

    public class CleanUpThread implements Runnable {
        boolean continueRunning = true;

        public void run() {
            int idleTimeMillis = 
                LexEvsServiceLocator.getInstance().getSystemResourceService().
                    getSystemVariables().getIteratorIdleTime() * 60 * 1000;
            while (continueRunning) {
                try {
                    // 1 minute;
                    Thread.sleep(1 * 60 * 1000);
                } catch (InterruptedException e) {
                    if (!continueRunning) {
                        ResolvedConceptReferencesIteratorImpl.this.releaseResources();
                        return;
                    }
                }

                // check to see if enough time has elapsed yet.
                if (System.currentTimeMillis() - lastRead_ > idleTimeMillis) {
                    // clear all memory references, end this thread.
                    ResolvedConceptReferencesIteratorImpl.this.releaseResources();
                    return;
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator
     * #next()
     */
    public ResolvedConceptReference next() throws LBResourceUnavailableException, LBInvocationException {
        ResolvedConceptReferenceList rcrl = next(1);
        if (rcrl.getResolvedConceptReferenceCount() == 0) {
            return null;
        } else {
            return rcrl.getResolvedConceptReference(0);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator
     * #next(int)
     */
    @LgClientSideSafe
    public ResolvedConceptReferenceList next(int maxToReturn) throws LBResourceUnavailableException,
            LBInvocationException {
        if (codesToReturn_ == null) {
            throw new LBResourceUnavailableException("This iterator is no longer valid.");
        }

        try {
            lastRead_ = System.currentTimeMillis();

            if (maxToReturn == 0) {
                return new ResolvedConceptReferenceList();
            }

            if (pos_ == codesToReturn_.getAllCodes().size()) {
                return new ResolvedConceptReferenceList();
            }

            int max;
            if (maxToReturn < 0) {
                // they are asking for the default block size - lets go with
                // 100.
                max = pos_ + 100;
            } else {
                max = (maxToReturn > maxSizeSystemLimit ? maxSizeSystemLimit
                        : maxToReturn)
                        + pos_;
            }

            if (max > codesToReturn_.getAllCodes().size()) {
                max = codesToReturn_.getAllCodes().size();
            }

            ResolvedConceptReferenceList resolvedRefs = new ResolvedConceptReferenceList();

            while(pos_ < max){
                ResolvedConceptReferenceList returnedRefs = codeToReturnResolver.buildResolvedConceptReference(
                        getCodeToReturnFromCodeHolder(pos_, max),
                        this.restrictToProperties_,
                        this.restrictToPropertyTypes_,
                        this.filters_,
                        this.resolveEntities_);
                
                if (codesToReturn_ == null) {
                    throw new LBResourceUnavailableException("This iterator is no longer valid. "
                            + "You may be attempting to retrieve too large a list or iterator page");
                }
                
                compactCodesToReturn(pos_, max);
                
                for(ResolvedConceptReference ref : returnedRefs.getResolvedConceptReference()){
                    // rcl can be null if the filters didn't pass the result back.
                    if(ref != null){
                        resolvedRefs.addResolvedConceptReference(ref);
                    } else {
                        // filter rejected this result. need to bump up max so that
                        // I return the correct number of results.
                        if (max < codesToReturn_.getAllCodes().size()){
                            max++;
                        }
                    }
                    pos_++;
                }
            }
            
            return resolvedRefs;

        } catch (LBInvocationException e) {
            throw e;
        } catch (Exception e) {
            String id = getLogger().error("Implementation problem in the resolved concept reference iterator", e);
            throw new LBInvocationException("Unexpected system error: " + e.getMessage(), id);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator
     * #get(int, int)
     */
    public ResolvedConceptReferenceList get(int start, int end) throws LBResourceUnavailableException,
            LBInvocationException, LBParameterException {
        if (codesToReturn_ == null) {
            throw new LBResourceUnavailableException("This iterator is no longer valid.");
        }
        ResolvedConceptReferenceList rcrl = new ResolvedConceptReferenceList();

        try {
            lastRead_ = System.currentTimeMillis();

            if (start > end) {
                throw new LBParameterException("Start must be less than end");
            }

            if (start == end) {
                return rcrl;
            }
            if (start < 0) {
                start = 0;
            }

            if (end - start > maxSizeSystemLimit) {
                throw new LBParameterException(
                        "The requested result size exceeds the system limited maximum result size of "
                                + maxSizeSystemLimit);

            }

            if (end > codesToReturn_.getNumberOfCodes()) {
                end = codesToReturn_.getNumberOfCodes();
            }

            // need to make sure the start point is valid
            if (start >= codesToReturn_.getNumberOfCodes()) {
                throw new LBParameterException("Start postion is greater than available results.");
            }

            if (filters_ != null && filters_.length > 0) {
                // filters are involved. start and end points need to be
                // adjusted
                // to compensate for the results that are removed by the
                // filters.
                if (filterCheck_ == null) {
                    filterCheck_ = new Boolean[codesToReturn_.getNumberOfCodes()];
                }

                /*
                 * need to scan the filter check array. null's mean that the
                 * item hasn't been checked. false means that the filters will
                 * remove this result. true means that the filters will not
                 * remove this result. need to scan until we count 'true's equal
                 * to the start point. I don't store the actual
                 * ResolvedConceptReferences that I have to resolve to determine
                 * if the filter will remove them because I would run out of
                 * memory on large result sets.
                 */

                int trueCount = 0;
                int realStart = 0;
                for (; realStart < codesToReturn_.getNumberOfCodes(); realStart++) {
                    if (filterCheck_[realStart] == null) {
                        ResolvedConceptReference rcr = codeToReturnResolver.buildResolvedConceptReference(
                                getCodeToReturnFromCodeHolder(realStart),
                                this.restrictToProperties_,
                                this.restrictToPropertyTypes_,
                                this.filters_,
                                this.resolveEntities_);
                        if (rcr == null) {
                            filterCheck_[realStart] = new Boolean(false);
                        } else {
                            filterCheck_[realStart] = new Boolean(true);
                        }
                    }
                    if (filterCheck_[realStart].booleanValue()) {
                        trueCount++;
                    }
                    // trueCount starts at 1, start is 0 indexed..
                    // so trueCount needs to be start + 1
                    if (trueCount == start + 1) {
                        break;
                    }
                }

                if (realStart == codesToReturn_.getNumberOfCodes()) {
                    throw new LBParameterException("Start postion is greater than available results.");
                }

                int max = realStart - start + end;
                if (max > codesToReturn_.getNumberOfCodes()) {
                    max = codesToReturn_.getNumberOfCodes();
                }

                for (int i = realStart; i < max; i++) {
                    ResolvedConceptReference rcr = codeToReturnResolver.buildResolvedConceptReference(
                            getCodeToReturnFromCodeHolder(i),
                            this.restrictToProperties_,
                            this.restrictToPropertyTypes_,
                            this.filters_,
                            this.resolveEntities_);
                    if (rcr == null) {
                        // filter removed it. bump up the end variable by 1 so
                        // that
                        // we can return the correct number of results.
                        if (max + 1 < codesToReturn_.getNumberOfCodes()) {
                            max++;
                        }
                        // also note this in the filterCheck array.
                        filterCheck_[i] = new Boolean(false);
                    } else {
                        rcrl.addResolvedConceptReference(rcr);
                        filterCheck_[i] = new Boolean(true);
                    }
                }
            } else {
               ResolvedConceptReferenceList returnList = codeToReturnResolver.buildResolvedConceptReference(
                            getCodeToReturnFromCodeHolder(start, end),
                            this.restrictToProperties_,
                            this.restrictToPropertyTypes_,
                            this.filters_,
                            this.resolveEntities_);
               
               compactCodesToReturn(start,end);
               
               return returnList;

            }
        } catch (LBInvocationException e) {
            throw e;
        } catch (LBParameterException e) {
            throw e;
        } catch (Exception e) {
            String id = getLogger().error("Implementation problem in the resolved concept reference iterator", e);
            throw new LBInvocationException("Unexpected system error", id);
        }
        return rcrl;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.LexGrid.LexBIG.Utility.Iterators.EntityListIterator#hasNext()
     */
    @LgClientSideSafe
    public boolean hasNext() throws LBResourceUnavailableException {
        if(isExhausted){
            return false;
        }
        
        if (codesToReturn_ == null) {
            throw new LBResourceUnavailableException("This iterator is no longer valid.");
        }

        lastRead_ = System.currentTimeMillis();

        boolean answer = false;
        // I need to scan forward to get an accurate answer to hasNext() if
        // filters are involved.
        if (filters_ != null && filters_.length > 0) {
            for (int i = pos_; i < codesToReturn_.getNumberOfCodes(); i++, pos_++) {
                ResolvedConceptReference rcr;
                try {
                    rcr = codeToReturnResolver.buildResolvedConceptReference(
                            getCodeToReturnFromCodeHolder(i),
                            this.restrictToProperties_,
                            this.restrictToPropertyTypes_,
                            this.filters_,
                            this.resolveEntities_);
                    if (rcr != null) {
                        answer = true;
                        break;
                    }
                } catch (LBInvocationException e) {
                    getLogger().error("Unexected Error determining hasNext", e);
                    answer = true; // return true - they will likely call next()
                                   // again, which
                    // will have the same problem - and it will throw the error
                    // back to them
                }
            }
        } else {
            answer = pos_ < codesToReturn_.getNumberOfCodes();
        }

        if (answer == false) {
            isExhausted = true;
            release();
        }
        return answer;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.LexGrid.LexBIG.Utility.Iterators.EntityListIterator#release()
     */
    public void release() {
        if (cuti_ != null) {
            cuti_.continueRunning = false;
            cut_.interrupt();
        }
        releaseResources();
    }

    public ResolvedConceptReferencesIterator scroll(int maxToReturn) throws LBResourceUnavailableException,
            LBInvocationException {
        if (!hasNext()) {
            return null;
        }
        this.ref = next(maxToReturn);
        return this;
    }

    public ResolvedConceptReferenceList getNext() {
        ResolvedConceptReferenceList next = ref;
        this.ref = null;
        return next;
    }

    public int numberRemaining() throws LBResourceUnavailableException {
        if (codesToReturn_ == null) {
            throw new LBResourceUnavailableException("This iterator is no longer valid.");
        }

        if (filters_ != null && filters_.length > 0) {
            return -1;
        } else {
            return codesToReturn_.getNumberOfCodes() - pos_;
        }
    }
    
    @LgClientSideSafe
    private CodeToReturn getCodeToReturnFromCodeHolder(int position){
        return codesToReturn_.getAllCodes().get(position);
    }
    
    @LgClientSideSafe
    private List<CodeToReturn> getCodeToReturnFromCodeHolder(int start, int end){
        List<CodeToReturn> returnList = new ArrayList<CodeToReturn>();
        for(int i=start;i<end;i++){
            returnList.add(codesToReturn_.getAllCodes().get(i));
        }
        return returnList;
    }
    
    @LgClientSideSafe
    private void compactCodesToReturn(int start, int end){
        for(int i=start;i<end;i++){
            codesToReturn_.getAllCodes().get(i).compact();
        }
    }
  
    private void releaseResources() {
        codesToReturn_ = null;
        cuti_ = null;
        filters_ = null;
        filterCheck_ = null;
        restrictToProperties_ = null;
        restrictToPropertyTypes_ = null;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        ResolvedConceptReferencesIteratorImpl clone = new ResolvedConceptReferencesIteratorImpl();
        clone.codesToReturn_ = this.codesToReturn_;
        clone.codeToReturnResolver = this.codeToReturnResolver;
        clone.cut_ = this.cut_;
        clone.cuti_ = this.cuti_;
        clone.filterCheck_ = this.filterCheck_;
        clone.filters_ = this.filters_;
        clone.lastRead_ = this.lastRead_;
        clone.maxSizeSystemLimit = this.maxSizeSystemLimit;
        clone.pos_ = this.pos_;
        clone.ref = this.ref;
        clone.resolveEntities_ = this.resolveEntities_;
        clone.restrictToProperties_ = this.restrictToProperties_;
        clone.restrictToPropertyTypes_ = this.restrictToPropertyTypes_;
        
        return clone;
    }
}