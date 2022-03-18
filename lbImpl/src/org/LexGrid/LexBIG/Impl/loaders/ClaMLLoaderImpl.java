
package org.LexGrid.LexBIG.Impl.loaders;

import java.net.URI;
import java.net.URISyntaxException;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Load.ClaML_Loader;
import org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder;
import org.LexGrid.LexBIG.Utility.logging.CachingMessageDirectorIF;
import org.LexGrid.codingSchemes.CodingScheme;

import edu.mayo.informatics.lexgrid.convert.directConversions.claml.ClaML2LGMain;
import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

public class ClaMLLoaderImpl extends BaseLoader implements ClaML_Loader{
    private static final long serialVersionUID = 5525808812315726290L;
    public static final String name = "ClaMLLoader";
    public static final String description = "This loader loads ClaML files into the LexGrid database.";

/**
     * 
     */
@Override
    protected OptionHolder declareAllowedOptions(OptionHolder holder) {
        return holder;
    }

    @Override
    protected URNVersionPair[] doLoad() throws Exception {
        ClaML2LGMain main = new ClaML2LGMain();
        CodingScheme codingScheme = main.map(this.getResourceUri(), null, this.getLogger());
        
        this.persistCodingSchemeToDatabase(codingScheme);
        
        return this.constructVersionPairsFromCodingSchemes(codingScheme);
    }

    @Override
    protected ExtensionDescription buildExtensionDescription() {
        ExtensionDescription temp = new ExtensionDescription();
        temp.setExtensionBaseClass(ClaMLLoaderImpl.class.getInterfaces()[0].getName());
        temp.setExtensionClass(ClaMLLoaderImpl.class.getName());
        temp.setDescription(description);
        temp.setName(name);

        return temp;
    }

    public void validate(URI source, int validationLevel) throws LBException {
        // TODO Auto-generated method stub
        
    }

    public void load(String resource, boolean stopOnErrors, boolean async) throws LBException {
        this.getOptions().getBooleanOption(FAIL_ON_ERROR_OPTION).setOptionValue(stopOnErrors);
        this.getOptions().getBooleanOption(ASYNC_OPTION).setOptionValue(async);
        
        try {
            this.load(new URI(resource));
        } catch (URISyntaxException e) {
            CachingMessageDirectorIF md = getMessageDirector();
            md = md == null? createCachingMessageDirectorIF(): md;
            md.error(e.getMessage());
           throw new LBException(e.getMessage());
        }   
    }
}