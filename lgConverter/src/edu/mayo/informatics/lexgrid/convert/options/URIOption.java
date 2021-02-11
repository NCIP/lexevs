
package edu.mayo.informatics.lexgrid.convert.options;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class URIOption.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class URIOption extends AbstractOption<URI> implements org.LexGrid.LexBIG.Extensions.Load.options.URIOption {

    /** The allowed file extensions. */
    private List<String> allowedFileExtensions = new ArrayList<String>();
    
    /** The is folder. */
    private boolean isFolder;
    
    /**
     * Instantiates a new uRI option.
     * 
     * @param optionName the option name
     */
    public URIOption(String optionName) {
        super(optionName);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Load.options.URIOption#getAllowedFileExtensions()
     */
    public List<String> getAllowedFileExtensions() {
        return allowedFileExtensions;
    }
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Load.options.URIOption#addAllowedFileExtensions(java.lang.String)
     */
    public void addAllowedFileExtensions(String allowedFileExtensions) {
        this.getAllowedFileExtensions().add(allowedFileExtensions);
    }

    /**
     * Sets the allowed file extensions.
     * 
     * @param allowedFileExtensions the new allowed file extensions
     */
    public void setAllowedFileExtensions(List<String> allowedFileExtensions) {
        this.allowedFileExtensions = allowedFileExtensions;
    }

    /**
     * Sets the folder.
     * 
     * @param isFolder the new folder
     */
    public void setFolder(boolean isFolder) {
        this.isFolder = isFolder;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Load.options.URIOption#isFolder()
     */
    public boolean isFolder() {
        return isFolder;
    }
}