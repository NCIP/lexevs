
package org.LexGrid.LexBIG.Impl.Extensions;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Extendable;
import org.LexGrid.LexBIG.Extensions.ExtensionRegistry;

/**
 * The Class AbstractExtendable.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractExtendable implements Extendable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -4053427150129368803L;
    
    /** The extension description. */
    private ExtensionDescription extensionDescription;
    
    /**
     * Instantiates a new abstract extendable.
     */
    protected AbstractExtendable(){
        this.setExtensionDescription(
                this.buildExtensionDescription());
    }

    /**
     * Sets the extension description.
     * 
     * @param extensionDescription the new extension description
     */
    private void setExtensionDescription(ExtensionDescription extensionDescription){
        this.extensionDescription = buildExtensionDescription();
    }

    /**
     * Gets the extension description.
     * 
     * @return the extension description
     */
    public ExtensionDescription getExtensionDescription() {
        return extensionDescription;
    }

    protected abstract void doRegister(
            ExtensionRegistry registry, 
            ExtensionDescription description) throws LBParameterException;
    
    public void register() throws LBParameterException, LBException {
        this.doRegister(ExtensionRegistryImpl.instance(), this.getExtensionDescription());
    }
    
    /**
     * Builds the extension description.
     * 
     * @return the extension description
     */
    protected abstract ExtensionDescription buildExtensionDescription();
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Extendable#getDescription()
     */
    public String getDescription() {
        return extensionDescription.getDescription();
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Extendable#getName()
     */
    public String getName() {
       return extensionDescription.getName();
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Extendable#getProvider()
     */
    public String getProvider() {
        return extensionDescription.getExtensionProvider().getContent();
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Extendable#getVersion()
     */
    public String getVersion() {
        return extensionDescription.getVersion();
    } 
}