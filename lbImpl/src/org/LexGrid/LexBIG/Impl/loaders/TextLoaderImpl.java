
package org.LexGrid.LexBIG.Impl.loaders;

import java.io.File;
import java.net.URI;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Load.OntologyFormat;
import org.LexGrid.LexBIG.Extensions.Load.Text_Loader;
import org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder;
import org.LexGrid.codingSchemes.CodingScheme;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;

import edu.mayo.informatics.lexgrid.convert.directConversions.TextToSQL;
import edu.mayo.informatics.lexgrid.convert.options.BooleanOption;
import edu.mayo.informatics.lexgrid.convert.options.StringOption;
import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

/**
 * Class to load a Text files into the LexBIG API.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class TextLoaderImpl extends BaseLoader implements Text_Loader {
    private static final long serialVersionUID = -4164433097047462000L;
    
    public static final String DELIMITER_OPTION = "Delimiter";
    public static String FORCE_FORMAT_B_OPTION = "Force Format B";

    public TextLoaderImpl() {
        super();
    }

    public void validate(URI uri, Character delimiter, boolean triplesFormat, int validationLevel)
            throws LBParameterException {
        //
    }
    

    public void load(URI source, Character delimiter, boolean readDoublesAsTriples, boolean stopOnErrors, boolean async)
            throws LBException {
        this.getOptions().getStringOption(DELIMITER_OPTION).setOptionValue(delimiter == null ? null : String.valueOf(delimiter));
        this.getOptions().getBooleanOption(FORCE_FORMAT_B_OPTION).setOptionValue(readDoublesAsTriples);
        this.getOptions().getBooleanOption(FAIL_ON_ERROR_OPTION).setOptionValue(stopOnErrors);
        this.getOptions().getBooleanOption(ASYNC_OPTION).setOptionValue(async);
        
        this.load(source);
    }

    @Override
    protected URNVersionPair[] doLoad() throws CodingSchemeAlreadyLoadedException {
  
            TextToSQL loader = new TextToSQL();
            
            CodingScheme codingScheme = loader.load(
                    this.getResourceUri(), 
                    this.getOptions().getStringOption(TextLoaderImpl.DELIMITER_OPTION).getOptionValue(), 
                    this.getLoaderPreferences(), 
                    this.getLogger(), 
                    this.getOptions().getBooleanOption(TextLoaderImpl.FORCE_FORMAT_B_OPTION).getOptionValue());
       
            this.getStatus().setState(ProcessState.COMPLETED);
            this.getStatus().setErrorsLogged(false);
            
            this.persistCodingSchemeToDatabase(codingScheme);
            
            return this.constructVersionPairsFromCodingSchemes(codingScheme);
    }
    
    public static void main(String[] args){
        TextLoaderImpl loader = new TextLoaderImpl();
        loader.addBooleanOptionValue(TextLoaderImpl.FORCE_FORMAT_B_OPTION, true);
        
        loader.load(new File("C:\\workspaces\\lexevs60\\lgConverter\\commentedSamples\\textLoad_A.txt").toURI());
    }

    @Override
    protected OptionHolder declareAllowedOptions(OptionHolder holder) {
        BooleanOption forceFormatB = new BooleanOption(TextLoaderImpl.FORCE_FORMAT_B_OPTION, true);
        holder.getBooleanOptions().add(forceFormatB);
        
        StringOption delimeter = new StringOption(TextLoaderImpl.DELIMITER_OPTION);
        holder.getStringOptions().add(delimeter);
        return holder;
    }

    @Override
    protected ExtensionDescription buildExtensionDescription() {
        ExtensionDescription temp = new ExtensionDescription();
        temp.setExtensionBaseClass(TextLoaderImpl.class.getInterfaces()[0].getName());
        temp.setExtensionClass(TextLoaderImpl.class.getName());
        temp.setDescription(description);
        temp.setName(name);
        return temp;
    }
    
    @Override
    public OntologyFormat getOntologyFormat() {
        return OntologyFormat.TEXT;
    }
}