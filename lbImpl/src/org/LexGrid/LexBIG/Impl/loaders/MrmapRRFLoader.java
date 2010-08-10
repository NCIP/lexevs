package org.LexGrid.LexBIG.Impl.loaders;

import java.net.URI;
import java.net.URISyntaxException;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Load.MrMap_Loader;
import org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder;
import org.LexGrid.codingSchemes.CodingScheme;

import edu.mayo.informatics.lexgrid.convert.directConversions.MrmapToSQL;
import edu.mayo.informatics.lexgrid.convert.options.StringOption;
import edu.mayo.informatics.lexgrid.convert.options.URIOption;
import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

public class MrmapRRFLoader extends BaseLoader implements MrMap_Loader{
    
    private static final long serialVersionUID = -689653003698478622L;
    
    public final static String name = "MrMap_Loader";
    
    @SuppressWarnings("unused")
    private final static String description = "This loader loads MRMAP.RRF and MRSAT.RRF" +
    		" files into the LexGrid database as a mapping coding scheme.";
    
    public final static String VALIDATE = "Validate";
    public final static String MAPPING_SCHEME = "Mapping Scheme Name";
    public final static String MAPPING_VERSION = "Mapping Version";
    public final static String MAPPING_URI  = "Mapping URI";
    public final static String SOURCE_SCHEME = "Source Scheme Name";
    public final static String SOURCE_VERSION = "Source Version";
    public final static String SOURCE_URI = "Source URI";
    public final static String TARGET_SCHEME = "Target Scheme Name";
    public final static String TARGET_VERSION = "Target Version";
    public final static String TARGET_URI = "Target URI";
    public final static String MRSAT_URI = "MRSAT file path";
    
    
    @SuppressWarnings("unused")
    private static boolean validate = true;

    public MrmapRRFLoader(){
        super();
    }


    @Override
    protected OptionHolder declareAllowedOptions(OptionHolder holder) {
    StringOption mappingScheme = new StringOption(MAPPING_SCHEME);
    StringOption mappingVersion = new StringOption(MAPPING_VERSION);
    StringOption mappingURI = new StringOption(MAPPING_URI);
    StringOption sourceScheme = new StringOption(SOURCE_SCHEME);
    StringOption sourceVersion = new StringOption(SOURCE_VERSION);
    StringOption sourceURI = new StringOption(SOURCE_URI);
    StringOption targetScheme = new StringOption(TARGET_SCHEME);
    StringOption targetVersion = new StringOption(TARGET_VERSION );
    StringOption targetURI = new StringOption(TARGET_URI);
    URIOption mrSatURI  = new URIOption(MRSAT_URI);
    holder.getStringOptions().add(mappingScheme);
    holder.getStringOptions().add(mappingVersion);
    holder.getStringOptions().add(mappingURI);
    holder.getStringOptions().add(sourceScheme);
    holder.getStringOptions().add(sourceVersion);
    holder.getStringOptions().add(sourceURI);
    holder.getStringOptions().add(targetScheme);
    holder.getStringOptions().add(targetVersion);
    holder.getStringOptions().add(targetURI);
    holder.getURIOptions().add(mrSatURI);
        return holder;
    }

    @Override
    protected URNVersionPair[] doLoad() throws Exception {
      MrmapToSQL map = new MrmapToSQL();
     CodingScheme[] schemes = map.load(getMessageDirector(), 
              this.getResourceUri(), 
              this.getOptions().getURIOption(MRSAT_URI).getOptionValue(),
              this.getOptions().getStringOption(MAPPING_SCHEME).getOptionValue(),
              this.getOptions().getStringOption(MAPPING_VERSION).getOptionValue(),
              this.getOptions().getStringOption(MAPPING_URI).getOptionValue(),
              this.getOptions().getStringOption(SOURCE_SCHEME).getOptionValue(),
              this.getOptions().getStringOption(SOURCE_VERSION).getOptionValue(),
              this.getOptions().getStringOption(SOURCE_URI).getOptionValue(),
              this.getOptions().getStringOption(TARGET_SCHEME).getOptionValue(),
              this.getOptions().getStringOption(TARGET_VERSION).getOptionValue(),
              this.getOptions().getStringOption(TARGET_URI).getOptionValue(),
              this.getCodingSchemeManifest());
     return this.constructVersionPairsFromCodingSchemes(schemes);
    }

    @Override
    protected ExtensionDescription buildExtensionDescription() {
        ExtensionDescription temp = new ExtensionDescription();
        temp.setExtensionBaseClass(MrmapRRFLoader.class.getInterfaces()[0].getName());
        temp.setExtensionClass(MrmapRRFLoader.class.getName());
        temp.setDescription(MrmapRRFLoader.description);
        temp.setName(MrmapRRFLoader.name);
        
        return temp;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

    @Override
    public void load(URI mrMapsource, URI mrSatSource, String nameForMappingScheme, String nameForMappingVersion,
            String nameforMappingURI, String sourceScheme, String sourceVersion, String sourceURI, String targetScheme,
            String targetVersion, String targetURI, boolean stopOnErrors, boolean async) throws LBException{
        this.getOptions().getURIOption(MRSAT_URI).setOptionValue(mrSatSource);
        this.getOptions().getStringOption(MAPPING_SCHEME).setOptionValue(nameForMappingScheme);
        this.getOptions().getStringOption(MAPPING_VERSION).setOptionValue(nameForMappingVersion);
        this.getOptions().getStringOption(MAPPING_URI).setOptionValue(nameforMappingURI);
        this.getOptions().getStringOption(SOURCE_SCHEME).setOptionValue(sourceScheme);
        this.getOptions().getStringOption(SOURCE_VERSION).setOptionValue(sourceVersion);
        this.getOptions().getStringOption(SOURCE_URI).setOptionValue(sourceURI);
        this.getOptions().getStringOption(TARGET_SCHEME).setOptionValue(targetScheme);
        this.getOptions().getStringOption(TARGET_VERSION).setOptionValue(targetVersion);
        this.getOptions().getStringOption(TARGET_URI).setOptionValue(targetURI);

        this.load(mrMapsource);
    }

    @Override
    public void validate(String source, int validationLevel) throws LBException {
        throw new UnsupportedOperationException();
    }

}
