
package org.LexGrid.LexBIG.Impl.loaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Load.OBO_Loader;
import org.LexGrid.LexBIG.Extensions.Load.OntologyFormat;
import org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.relations.Relations;
import org.jdom.input.SAXBuilder;
import org.lexevs.dao.database.operation.LexEvsDatabaseOperations.RootOrTail;
import org.lexevs.dao.database.operation.LexEvsDatabaseOperations.TraverseAssociations;
import org.springframework.util.Assert;

import edu.mayo.informatics.lexgrid.convert.directConversions.obo1_2.OBO2LGMain;
import edu.mayo.informatics.lexgrid.convert.directConversions.obo1_2.OBOFormatValidator;
import edu.mayo.informatics.lexgrid.convert.exceptions.ConnectionFailure;
import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

/**
 * Class to load OBO files.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class OBOLoaderImpl extends BaseLoader implements OBO_Loader {
    private static final long serialVersionUID = 8418561158634673381L;

    public OBOLoaderImpl() {
        super();
        this.setDoApplyPostLoadManifest(false);
    }

    public String getOBOVersion() {
        return "1.2";
    }

    protected ExtensionDescription buildExtensionDescription(){
        ExtensionDescription temp = new ExtensionDescription();
        temp.setExtensionBaseClass(OBOLoaderImpl.class.getInterfaces()[0].getName());
        temp.setExtensionClass(OBOLoaderImpl.class.getName());
        temp.setDescription(description);
        temp.setName(name);
        temp.setVersion( getOBOVersion() );

        return temp;
    }

    public void validate(URI uri, URI metaSource, int validationLevel) throws LBParameterException {
        try {
            setInUse();

            if (validationLevel == 0) {
                if (!OBOFormatValidator.isValidDocumentHeader(uri)) {
                    throw new LBParameterException("The OBO file header was  malformed while validating  "+ uri);
                }
                if (!OBOFormatValidator.isValidDocumentContent(uri)) {
                    throw new LBParameterException("The OBO file content was malformed while validating "+ uri);
                }

            }
            if (metaSource != null) {
                try {
                    Reader temp;
                    if (metaSource.getScheme().equals("file")) {
                        temp = new FileReader(new File(metaSource));
                    } else {
                        temp = new InputStreamReader(metaSource.toURL().openConnection().getInputStream());
                    }
                    if (validationLevel == 0) {
                        SAXBuilder saxBuilder = new SAXBuilder();
                        saxBuilder.build(new BufferedReader(temp));
                    }
                }

                catch (Exception e) {
                    throw new ConnectionFailure("The meta source file '" + metaSource
                            + "' cannot be read or is invalid");
                }
            }
        } catch (ConnectionFailure e) {
            throw new LBParameterException("The OBO file path appears to be invalid - " + e);
        } catch (LBInvocationException e) {
            throw new LBParameterException(
                    "Each loader can only do one thing at a time.  Please create a new loader to do multiple loads at once.");
        } catch (Exception e) {
            throw new LBParameterException(e.getMessage());
        } finally {
            inUse = false;
        }

    }

    public void load(URI uri, URI metaSource, boolean stopOnErrors, boolean async) throws LBParameterException,
            LBInvocationException {
        this.getOptions().getBooleanOption(FAIL_ON_ERROR_OPTION).setOptionValue(stopOnErrors);
        this.getOptions().getBooleanOption(ASYNC_OPTION).setOptionValue(async);
        
        this.load(uri);
    }

    @Override
    protected OptionHolder declareAllowedOptions(OptionHolder holder) {
        return holder;
    }

    @Override
    protected URNVersionPair[] doLoad() throws Exception {
      
        OBO2LGMain mainTxfm = new OBO2LGMain();
        CodingScheme codingScheme = mainTxfm.map(this.getResourceUri(), null, this.getMessageDirector());
  
        this.persistCodingSchemeToDatabase(codingScheme);
        
        URNVersionPair urnVersion = new URNVersionPair(codingScheme.getCodingSchemeURI(), codingScheme.getRepresentsVersion());
     
        this.buildRootNode(
                Constructors.createAbsoluteCodingSchemeVersionReference(
                codingScheme.getCodingSchemeURI(), codingScheme.getRepresentsVersion()), 
                null, 
                getRelationsContainerName(codingScheme), 
                RootOrTail.TAIL,
                TraverseAssociations.TOGETHER);
        
        return new URNVersionPair[]{urnVersion};
    }
    
    private String getRelationsContainerName(CodingScheme codingScheme) {
        Relations[] relations = codingScheme.getRelations();
        Assert.state(relations.length == 1);
        
        return relations[0].getContainerName();
    }
    
    @Override
    public OntologyFormat getOntologyFormat() {
        return OntologyFormat.OBO;
    }

    public void finalize() throws Throwable {
        getLogger().loadLogDebug("Freeing OBOLoaderImpl");
        super.finalize();
    }

}