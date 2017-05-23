package org.LexGrid.LexBIG.Impl.loaders;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Extensions.Load.SourceAssertedValueSetDefinitionLoader;
import org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder;
import org.LexGrid.concepts.Entity;

import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

public class SourceAssertedValueSetLoaderImpl extends BaseLoader implements SourceAssertedValueSetDefinitionLoader {
    

    /**
     * 
     */
    private static final long serialVersionUID = 1817759866573084934L;

    
//    public void init(String associationName, String codingSchemeName, String codingSchemeVersion, boolean targetToSource) throws LBParameterException{
//        csUri = resourceService.getInternalCodingSchemeNameForUserCodingSchemeName(codingSchemeName, codingSchemeVersion);
//        guid = getPredicateGuidForValueSetRelation(associationName, codingSchemeVersion, codingSchemeVersion);
//    }

    @Override
    public void load(String vsAssociationName, String codingSchemeName, String codingSchemeVersion, boolean targetToSource) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void validate(Entity entity) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected URNVersionPair[] doLoad() throws Exception {
   
        return null;
    }

    @Override
    protected OptionHolder declareAllowedOptions(OptionHolder holder) {
        return holder;
    }

    @Override
    protected ExtensionDescription buildExtensionDescription(){
        ExtensionDescription temp = new ExtensionDescription();
        temp.setExtensionBaseClass(SourceAssertedValueSetLoaderImpl.class.getInterfaces()[0].getName());
        temp.setExtensionClass(SourceAssertedValueSetLoaderImpl.class.getName());
        temp.setDescription(DESCRIPTION);
        temp.setName(NAME);

        return temp;
    }
    


}
