
package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.supplement;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.ExtensionRegistry;
import org.LexGrid.LexBIG.Extensions.Generic.GenericExtension;
import org.LexGrid.LexBIG.Extensions.Generic.SupplementExtension;
import org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable;
import org.LexGrid.LexBIG.Utility.ServiceUtility;

/**
 * The Class SupplementExtensionImpl.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class SupplementExtensionImpl extends AbstractExtendable implements SupplementExtension {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -2352157649159256424L;

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Generic.SupplementExtension#isSupplement(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)
     */
    @Override
    public boolean isSupplement(String codingScheme, CodingSchemeVersionOrTag tagOrVersion) throws LBParameterException {
        return ServiceUtility.isSupplement(codingScheme, tagOrVersion);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Generic.SupplementExtension#getParentOfSupplement(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)
     */
    @Override
    public AbsoluteCodingSchemeVersionReference getParentOfSupplement(
            String codingScheme, CodingSchemeVersionOrTag tagOrVersion) throws LBParameterException {
       return ServiceUtility.getParentOfSupplement(codingScheme, tagOrVersion);
    }

    @Override
    protected void doRegister(ExtensionRegistry registry, ExtensionDescription description) throws LBParameterException {
        registry.registerGenericExtension(description);
    }
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable#buildExtensionDescription()
     */
    @Override
    protected ExtensionDescription buildExtensionDescription() {
        ExtensionDescription ed = new ExtensionDescription();
        ed.setDescription("Coding Scheme Supplement Extension for LexEVS.");
        ed.setExtensionBaseClass(GenericExtension.class.getName());
        ed.setExtensionClass(SupplementExtensionImpl.class.getName());
        ed.setName("SupplementExtension");
        ed.setVersion("1.0");
        
        return ed;
    }
}