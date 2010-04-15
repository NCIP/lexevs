package org.LexGrid.LexBIG.Impl.loaders;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Load.ClaML_Loader;
import org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder;
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
        
        URNVersionPair  urnVersion= new URNVersionPair(codingScheme.getCodingSchemeURI(), codingScheme.getRepresentsVersion());
        return new URNVersionPair[]{urnVersion};
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

    public void validate() throws LBException {
        // TODO Auto-generated method stub
        
    }

    public void load() throws LBException {
        // TODO Auto-generated method stub
        
    }

}
