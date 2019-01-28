/**
 * TODO - Need to add Mayo copyright here
 */
package org.LexGrid.LexBIG.Impl.loaders;

import java.net.URI;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Load.Loader;
import org.LexGrid.LexBIG.Extensions.Load.MIFVocabularyLoader;
import org.LexGrid.LexBIG.Extensions.Load.OntologyFormat;
import org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.codingSchemes.CodingScheme;
import org.apache.xerces.parsers.SAXParser;

import edu.mayo.informatics.lexgrid.convert.directConversions.hl7.mif.vocabulary.MifVocabParserHandler;
import edu.mayo.informatics.lexgrid.convert.directConversions.hl7.mif.vocabulary.MifVocabulary2LGMain;
import edu.mayo.informatics.lexgrid.convert.directConversions.hl7.mif.vocabulary.MifVocabularyModel;
import edu.mayo.informatics.lexgrid.convert.options.BooleanOption;
import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

/**
 * The loader interface validates and/or loads HL7 Model Interchange Format (MIF) sources that are of
 * the VocabularyModel type content for a service.
 * 
 * @author m046445
 *
 */
public class MIFVocabularyLoaderImpl extends BaseLoader implements MIFVocabularyLoader {

    private static final long serialVersionUID = -8302176248821776249L;

//    public final static String NAME = "MIFVocabularyLoaderImpl";
//    public static String VERSION = "1.0";
    public final static String VALIDATE = "Validate";
    private static boolean validate = true;

    public MIFVocabularyLoaderImpl() {
        super();
     }
 
/*    
    public void register() throws LBParameterException, LBException {
        ExtensionDescription temp = new ExtensionDescription();
        temp.setExtensionBaseClass(MIFVocabularyLoaderImpl.class.getInterfaces()[0].getName());
        temp.setExtensionClass(MIFVocabularyLoaderImpl.class.getName());
        temp.setDescription(DESCRIPTION);
        temp.setName(NAME);
        temp.setVersion(VERSION);

        // I'm registering them this way to avoid the lexBig service manager
        // API.
        // If you are writing an add-on extension, you should register them
        // through the
        // proper interface.
        ExtensionRegistryImpl.instance().registerLoadExtension(temp);
    }

*/ 
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.loaders.BaseLoader#doLoad()
     */
    @Override
    protected URNVersionPair[] doLoad() throws Exception {
        
        // Unmarshall MIF XML file into MIF Vocabulary objects
        MifVocabularyModel vocabularyModel = parseMifObjects(this.getResourceUri(), this.getMessageDirector(), 
                this.getOptions().getBooleanOption(MIFVocabularyLoaderImpl.VALIDATE).getOptionValue());
        
        // Transform XML parsed objects into LexGrid objects?
        MifVocabulary2LGMain mifVocabLoader = new MifVocabulary2LGMain();
        try {
           CodingScheme codingScheme = mifVocabLoader.map(this.getMessageDirector(), vocabularyModel);
           persistCodingSchemeToDatabase(codingScheme);
           
           return this.constructVersionPairsFromCodingSchemes(codingScheme);
        } catch (Exception e) {
            throw new RuntimeException("HL7 MIF Load Failed", e);
        }
    }
    
    private MifVocabularyModel parseMifObjects(URI uri, LgMessageDirectorIF messageDirector, 
            boolean validateXML) {
        
        MifVocabularyModel vocabularyModel = null;

        MifVocabParserHandler mifVocabSaxHandler = new MifVocabParserHandler();
        SAXParser p = new SAXParser();
        p.setContentHandler(mifVocabSaxHandler);
        
        try {
            p.parse(uri.toURL().getFile());
        } catch (Exception e) {
            messageDirector.error("The action to parse the source file into MIF Vocabulary objects detected a problem");
            e.printStackTrace();
        }

        vocabularyModel = mifVocabSaxHandler.getVocabularyModel();

        return vocabularyModel;
        
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.loaders.BaseLoader#declareAllowedOptions(org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder)
     */
    @Override
    protected OptionHolder declareAllowedOptions(OptionHolder holder) {
        BooleanOption forceValidation = new BooleanOption(LexGridMultiLoaderImpl.VALIDATE, validate);
        holder.getBooleanOptions().add(forceValidation);
        return holder;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable#buildExtensionDescription()
     */
    @Override
    protected ExtensionDescription buildExtensionDescription() {
        ExtensionDescription temp = new ExtensionDescription();
        temp.setExtensionBaseClass(Loader.class.getName());
        temp.setExtensionClass(MIFVocabularyLoaderImpl.class.getName());
        temp.setDescription(description);
        temp.setName(name);

        return temp;
    }

    @Override
    public void load(URI source, URI codingSchemeManifestURI, boolean stopOnErrors, boolean async) throws LBException {
        if(codingSchemeManifestURI != null) {
            this.setCodingSchemeManifestURI(codingSchemeManifestURI);
        }
        this.getOptions().getBooleanOption(FAIL_ON_ERROR_OPTION).setOptionValue(stopOnErrors);
        this.getOptions().getBooleanOption(ASYNC_OPTION).setOptionValue(async);
        
        this.load(source);
    }

    @Override
    public void validate(URI source, int validationLevel) throws LBException {
        // TODO Verify source has a single <vocabularyModel> start element???
        throw new UnsupportedOperationException();
    }

    public OntologyFormat getOntologyFormat(){
        return OntologyFormat.MIFVOCABULARY;
    }

    
    
}
