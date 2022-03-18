
package org.LexGrid.LexBIG.Impl.loaders;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Extensions.Load.OntologyFormat;

public class LexGridMultiResolvedValueSetLoaderImpl extends LexGridMultiLoaderImpl {

    private static final long serialVersionUID = 1L;
    public final static String name = "LexGrid_ResolvedValueSetLoader";
    private final static String description = "This loader loads LexGrid XML files into the LexGrid database as resolved value sets.";
    
    /* (non-Javadoc)
     * @see java.lang.Object#finalize()
     */
    public void finalize() throws Throwable {
        getLogger().loadLogDebug("Freeing LexGridMultiResolvedValueSetLoaderImpl");
        super.finalize();
    }
    
    @Override
    public OntologyFormat getOntologyFormat() {
        return OntologyFormat.RESOLVEDVALUESET;
    }
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable#buildExtensionDescription()
     */
    @Override
    protected ExtensionDescription buildExtensionDescription() {
        ExtensionDescription temp = new ExtensionDescription();
        temp.setExtensionBaseClass(LexGridMultiResolvedValueSetLoaderImpl.class.getName());
        temp.setExtensionClass(LexGridMultiResolvedValueSetLoaderImpl.class.getName());
        temp.setDescription(LexGridMultiResolvedValueSetLoaderImpl.description);
        temp.setName(LexGridMultiResolvedValueSetLoaderImpl.name);
        
        return temp;
    }
    
}