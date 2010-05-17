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
package org.LexGrid.LexBIG.Impl.helpers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.LexGrid.annotations.LgClientSideSafe;

/**
 * Class to hold unique concepts per code system that should be returned as a
 * result of a query.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a> 
 * @version subversion $Revision: $ checked in on $Date: $
 */
@SuppressWarnings("serial")
@LgClientSideSafe
public class DefaultCodeHolder implements AdditiveCodeHolder, Serializable {
    private List<CodeToReturn> codes_;
    private boolean collectedScores_;

    @LgClientSideSafe
    public boolean wereScoresCollected() {
        return this.collectedScores_;
    }

    public DefaultCodeHolder() {
        codes_ = new ArrayList<CodeToReturn>();
    }

    @LgClientSideSafe
    public void add(CodeToReturn code) {
        if(! this.contains(code)) {
            codes_.add(code);
        }
    }

    @LgClientSideSafe
    public void remove(CodeToReturn code) {
        codes_.remove(code);
    }

    @LgClientSideSafe
    public List<CodeToReturn> getAllCodes() {
        return codes_;
    }
    
    @LgClientSideSafe
    public boolean contains(CodeToReturn code) {
        return codes_.contains(code);
    }
    

    @LgClientSideSafe
    public void union(CodeHolder otherCodes) {
        List<CodeToReturn> codeEnum = otherCodes.getAllCodes();
        for(CodeToReturn otherCode : codeEnum) {

            
            // if it isn't in this code system yet, add it.
            if (!codes_.contains(otherCode)) {
                this.add(otherCode);
            } else {
                int index = codes_.indexOf(otherCode);
                // if it is already in this code system, and we are collecting
                // scores, average the scores.
                CodeToReturn myCode = codes_.get(index);
                myCode.setScore((otherCode.getScore() + myCode.getScore()) / 2);
            }
        }
    }

    @LgClientSideSafe
    public void intersect(CodeHolder otherCodes) {
        List<CodeToReturn> currentCodes = this.getAllCodes();
        List<CodeToReturn> allOtherCodes = otherCodes.getAllCodes();
        
        Iterator<CodeToReturn> itr = currentCodes.iterator();
        while(itr.hasNext()) {
            CodeToReturn code = itr.next();

            if (!allOtherCodes.contains(code)) {
                // code is not in the second set - remove from the first.
                itr.remove();
            }
        }

    }

    @LgClientSideSafe
    public void difference(CodeHolder otherCodes) {
        List<CodeToReturn> allOtherCodes = otherCodes.getAllCodes();
        for (CodeToReturn otherCode : allOtherCodes) {
           
            // if the key is in the current set, remove it.
            this.remove(otherCode);
        }
    }
    
    @LgClientSideSafe
    public int getNumberOfCodes(){
        return this.codes_.size();
    }
}