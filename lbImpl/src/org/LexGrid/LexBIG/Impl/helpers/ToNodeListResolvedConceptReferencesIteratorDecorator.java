package org.LexGrid.LexBIG.Impl.helpers;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Utility.ServiceUtility;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.annotations.LgClientSideSafe;

@LgClientSideSafe
public class ToNodeListResolvedConceptReferencesIteratorDecorator implements ResolvedConceptReferencesIterator {

    private static final long serialVersionUID = 765976499602536922L;
    
    private CodeHolder toNodeListCodes;
    private ResolvedConceptReferencesIterator delegate;
    
    public ToNodeListResolvedConceptReferencesIteratorDecorator(ResolvedConceptReferencesIterator delegate, CodeHolder toNodeListCodes) {
        this.toNodeListCodes = toNodeListCodes;
        this.delegate = delegate;
    }
    
    @Override
    public ResolvedConceptReferenceList get(int arg0, int arg1) throws LBResourceUnavailableException,
            LBInvocationException, LBParameterException {
        return delegate.get(arg0, arg1);
    }

    @Override
    public ResolvedConceptReferenceList getNext() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResolvedConceptReference next() throws LBResourceUnavailableException, LBInvocationException {
        ResolvedConceptReference ref = null;;
        try {
            ref = delegate.next();
        } catch (LBResourceUnavailableException e) {
            //delegate iterator is exhausted
        }
        if(ref != null) {
            toNodeListCodes.remove(new CodeToReturn(ref.getCode(), ref.getCodeNamespace()));
        } else {
            if(toNodeListCodes.getAllCodes().size() > 0) {
                
                CodeToReturn codeToReturn = toNodeListCodes.getAllCodes().get(0);
                while(toNodeListCodes.getAllCodes().remove(codeToReturn));
               
                ref = toResolvedConceptReference(codeToReturn);
            }
        }
        
        return ref;
    }

    @Override
    public ResolvedConceptReferenceList next(int arg0) throws LBResourceUnavailableException, LBInvocationException {
        ResolvedConceptReferenceList list = new ResolvedConceptReferenceList();
        try {
            list = delegate.next(arg0);
        } catch (LBResourceUnavailableException e) {
            //delegate iterator is exhausted
        }
        for(ResolvedConceptReference ref : list.getResolvedConceptReference()) {
            if(ref != null) {
                CodeToReturn codeToReturn = new CodeToReturn(ref.getCode(), ref.getCodeNamespace());
                while(toNodeListCodes.getAllCodes().remove(codeToReturn));
            } 
        }
        
        if(list.getResolvedConceptReferenceCount() < arg0) {
            int deficit = arg0 - list.getResolvedConceptReferenceCount();
            
            while(deficit > 0 && this.toNodeListCodes.getAllCodes().size() > 0) {
                CodeToReturn codeToReturn = toNodeListCodes.getAllCodes().get(0);
                
                list.addResolvedConceptReference(this.toResolvedConceptReference(codeToReturn));
                while(toNodeListCodes.getAllCodes().remove(codeToReturn));
            }
        }
        
        return list;
    }

    @Override
    public ResolvedConceptReferencesIterator scroll(int arg0) throws LBResourceUnavailableException,
            LBInvocationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasNext() throws LBResourceUnavailableException {
        boolean delegateHasNext;
        try {
            delegateHasNext = delegate.hasNext();
        } catch (LBResourceUnavailableException e) {
            delegateHasNext = false;
        }
        if(! delegateHasNext ) {
           return toNodeListCodes.getAllCodes().size() > 0;
        } else {
            return true;
        }
    }

    @Override
    public int numberRemaining() throws LBResourceUnavailableException {
        return -1;
    }

    @Override
    public void release() throws LBResourceUnavailableException {
        delegate.release();
    }
    
    private ResolvedConceptReference toResolvedConceptReference(CodeToReturn codeToReturn) {
        ResolvedConceptReference returnRef = new ResolvedConceptReference();
        returnRef.setCode(codeToReturn.getCode());
        returnRef.setCodeNamespace(codeToReturn.getNamespace());
        returnRef.setCodingSchemeURI(codeToReturn.getUri());
        returnRef.setCodingSchemeVersion(codeToReturn.getVersion());
        
        String codingSchemeName = null;
        try {
            codingSchemeName = ServiceUtility.getCodingSchemeName(
                    codeToReturn.getUri(), 
                    codeToReturn.getVersion());
        } catch (LBParameterException e) {
            //no-op -- don't assing coding scheme name
            //if we can't find it anywhere.
        }
        
        returnRef.setCodingSchemeName(codingSchemeName);
        
        return returnRef;
    }
}
