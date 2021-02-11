
package org.LexGrid.LexBIG.Impl.helpers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    
    public DefaultCodeHolder(List<CodeToReturn> codes) {
        codes_ = codes;
    }

    public DefaultCodeHolder() {
        codes_ = new ArrayList<CodeToReturn>();
    }

    @LgClientSideSafe
    public void add(CodeToReturn code) {
        codes_.add(code);
    }

    @LgClientSideSafe
    public void remove(CodeToReturn code) {
        while(codes_.remove(code));
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

        Set<CodeToReturn> set1 = new HashSet<CodeToReturn>(this.codes_);
        Set<CodeToReturn> set2 = new HashSet<CodeToReturn>(otherCodes.getAllCodes());
        
        set1.addAll(set2);

        this.codes_ = new ArrayList<CodeToReturn>(set1);
    }

    @LgClientSideSafe
    public void intersect(CodeHolder otherCodes) {
        Set<CodeToReturn> set1 = new HashSet<CodeToReturn>(this.codes_);
        Set<CodeToReturn> set2 = new HashSet<CodeToReturn>(otherCodes.getAllCodes());
        
        set1.retainAll(set2);

        this.codes_ = new ArrayList<CodeToReturn>(set1);
    }

    @LgClientSideSafe
    public void difference(CodeHolder otherCodes) {
        Set<CodeToReturn> set1 = new HashSet<CodeToReturn>(this.codes_);
        Set<CodeToReturn> set2 = new HashSet<CodeToReturn>(otherCodes.getAllCodes());
        
        set1.removeAll(set2);

        this.codes_ = new ArrayList<CodeToReturn>(set1);
    }
    
    @LgClientSideSafe
    public int getNumberOfCodes(){
        return this.codes_.size();
    }

    @Override
    public CodeHolder clone() throws CloneNotSupportedException {
        return new DefaultCodeHolder(new ArrayList<CodeToReturn>(this.codes_));
    } 
}