
package org.LexGrid.LexBIG.Impl.loaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.util.Date;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.LoadStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Load.MetaData_Loader;
import org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Utility.logging.CachingMessageDirectorIF;
import org.LexGrid.LexOnt.CodingSchemeManifest;
import org.jdom.input.SAXBuilder;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;
import org.lexevs.locator.LexEvsServiceLocator;

import edu.mayo.informatics.lexgrid.convert.exceptions.ConnectionFailure;
import edu.mayo.informatics.lexgrid.convert.exceptions.LgConvertException;
import edu.mayo.informatics.lexgrid.convert.options.BooleanOption;
import edu.mayo.informatics.lexgrid.convert.options.CodingSchemeReferencesStringArrayPickListOption;
import edu.mayo.informatics.lexgrid.convert.options.DefaultOptionHolder;
import edu.mayo.informatics.lexgrid.convert.options.StringOption;
import edu.mayo.informatics.lexgrid.convert.utility.ManifestUtil;
import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

/**
 * Class to load OBO files.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class MetaDataLoaderImpl extends BaseLoader implements MetaData_Loader {
    private static final long serialVersionUID = -205479865592766865L;

    private static String OVERWRITE_OPTION = "Overwrite Existing";
    private static String SCHEME_OPTION = "Coding Scheme";
    
    private ManifestUtil manifestUtil = new ManifestUtil();

    public MetaDataLoaderImpl() {
       super();
       this.setDoComputeTransitiveClosure(false);
       this.setDoIndexing(false);
       this.setDoRegister(false);
       this.setDoRemoveOnFailure(false);
       this.setDoApplyPostLoadManifest(false);
    }

    protected ExtensionDescription buildExtensionDescription(){
        ExtensionDescription temp = new ExtensionDescription();
        temp.setExtensionBaseClass(MetaDataLoaderImpl.class.getInterfaces()[0].getName());
        temp.setExtensionClass(MetaDataLoaderImpl.class.getName());
        temp.setDescription(description);
        temp.setName(name);

        return temp;
    }

    public void validateAuxiliaryData(URI source, AbsoluteCodingSchemeVersionReference codingSchemeVersion,
            int validationLevel) throws LBParameterException {
        try {
            setInUse();

            if (codingSchemeVersion == null || codingSchemeVersion.getCodingSchemeURN() == null
                    || codingSchemeVersion.getCodingSchemeURN().length() == 0
                    || codingSchemeVersion.getCodingSchemeVersion() == null
                    || codingSchemeVersion.getCodingSchemeVersion().length() == 0) {
                throw new LBParameterException("The coding scheme URN and version must be supplied.");
            }

            try {
                Reader temp;
                if (source.getScheme().equals("file")) {
                    temp = new FileReader(new File(source));
                } else {
                    temp = new InputStreamReader(source.toURL().openConnection().getInputStream());
                }
                if (validationLevel == 0) {
                    SAXBuilder saxBuilder = new SAXBuilder();
                    saxBuilder.build(new BufferedReader(temp));
                }
            }

            catch (Exception e) {
                throw new ConnectionFailure("The meta source file '" + source + "' cannot be read or is invalid.", e);
            }

        } catch (ConnectionFailure e) {
            throw new LBParameterException("The metadata file path appears to be invalid - " + e);
        } catch (LBInvocationException e) {
            throw new LBParameterException(
            "Each loader can only do one thing at a time.  Please create a new loader to do multiple loads at once.");
        } catch (LBParameterException e) {
            throw e;
        } catch (Exception e) {
            throw new LBParameterException(e.getMessage());
        } finally {
            inUse = false;
        }

    }

    public void loadAuxiliaryData(URI source, AbsoluteCodingSchemeVersionReference codingSchemeVersion,
            boolean overwrite, boolean stopOnErrors, boolean async) throws LBParameterException, LBInvocationException {
      
        String optionValue = 
               CodingSchemeReferencesStringArrayPickListOption.buildOptionValue(
                       codingSchemeVersion.getCodingSchemeURN(), 
                       codingSchemeVersion.getCodingSchemeVersion());
        
        this.getOptions().getStringOption(SCHEME_OPTION).setOptionValue(optionValue);
        this.getOptions().getBooleanOption(OVERWRITE_OPTION).setOptionValue(overwrite);
        this.getOptions().getBooleanOption(ASYNC_OPTION).setOptionValue(async);
        this.getOptions().getBooleanOption(FAIL_ON_ERROR_OPTION).setOptionValue(stopOnErrors);
        
        this.load(source);
    }

    public void loadAuxiliaryData(Map<Object, Object> source, AbsoluteCodingSchemeVersionReference codingSchemeVersion,
            boolean overwrite, boolean stopOnErrors, boolean async) throws LBException {
        throw new UnsupportedOperationException();
    }

    public void loadLexGridManifest(
            final CodingSchemeManifest source,
            final AbsoluteCodingSchemeVersionReference codingSchemeURNVersion, 
            boolean stopOnErrors, 
            boolean async)
    throws LBException {
        this.setInUse();

        LoadStatus status = new LoadStatus();
        status.setLoadSource(source.toString());

        status.setState(ProcessState.PROCESSING);
        status.setStartTime(new Date(System.currentTimeMillis()));

        this.setStatus(status);

        this.setCachingMessageDirectorIF(createCachingMessageDirectorIF());

        if (async) {
            Thread loadManifest = new Thread(new LoadManifest(
                    source, 
                    codingSchemeURNVersion, 
                    this.getMessageDirector()));
            loadManifest.start();

        } else {
            new LoadManifest(
                    source, 
                    codingSchemeURNVersion, 
                    this.getMessageDirector());
        }     
    }

    public void loadLexGridManifest(URI source, AbsoluteCodingSchemeVersionReference codingSchemeURNVersion,
            boolean stopOnErrors, boolean async) throws LBException {
        CodingSchemeManifest manifest = manifestUtil.getManifest(source);
        
        this.loadLexGridManifest(manifest, codingSchemeURNVersion, stopOnErrors, async);    
    }

    public void validateLexGridManifest(URI source, AbsoluteCodingSchemeVersionReference codingSchemeVersion,
            int validationLevel) throws LBException {
        // TODO Auto-generated method stub
    }

    @Override
    protected OptionHolder declareAllowedOptions(OptionHolder holder) {
        holder = new DefaultOptionHolder();
        
        BooleanOption asyncOption = new BooleanOption(ASYNC_OPTION, true);
        holder.getBooleanOptions().add(asyncOption);
        
        BooleanOption failOnErrorOption = new BooleanOption(FAIL_ON_ERROR_OPTION, false);
        holder.getBooleanOptions().add(failOnErrorOption);
        
        StringOption schemeOption;
        try {
            schemeOption = new CodingSchemeReferencesStringArrayPickListOption(
                    SCHEME_OPTION, 
                    LexBIGServiceImpl.defaultInstance().getSupportedCodingSchemes());
        } catch (LBInvocationException e) {
            throw new RuntimeException(e);
        }
        
        holder.getStringOptions().add(schemeOption);
        
        holder.getBooleanOptions().add(new BooleanOption(OVERWRITE_OPTION, false));
        return holder;
    }

    @Override
    protected URNVersionPair[] doLoad() throws CodingSchemeAlreadyLoadedException {
        AbsoluteCodingSchemeVersionReference scheme;
        try {
            scheme = getAbsoluteCodingSchemeVersionReferenceFromOptionString(this.getOptions().getStringOption(SCHEME_OPTION).getOptionValue());
        } catch (LBException e1) {
           throw new RuntimeException(e1);
        }
        try {
            LexEvsServiceLocator.getInstance().
            getIndexServiceManager().
            getMetadataIndexService().indexMetadata(
                    scheme.getCodingSchemeURN(),
                    scheme.getCodingSchemeVersion(),
                    this.getResourceUri(), 
                    !this.getOptions().getBooleanOption(OVERWRITE_OPTION).getOptionValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        return new URNVersionPair[] {new URNVersionPair(
                scheme.getCodingSchemeURN(),
                scheme.getCodingSchemeVersion()
        )};
    }
    
    private class LoadManifest implements Runnable {

        CodingSchemeManifest manifest_;
        AbsoluteCodingSchemeVersionReference currentURNVersion_;
        CachingMessageDirectorIF message_;

        public LoadManifest(CodingSchemeManifest source, 
                AbsoluteCodingSchemeVersionReference codingSchemeVersion,
                CachingMessageDirectorIF md) {
            manifest_ = source;
            currentURNVersion_ = codingSchemeVersion;
            message_ = md;
        }

        public void run() {

            message_.info("Start Time : " + new Date(System.currentTimeMillis()));
            try {

                String urn = currentURNVersion_.getCodingSchemeURN();
                String version = currentURNVersion_.getCodingSchemeVersion();

                URNVersionPair currentURNVersion = new URNVersionPair(urn, version);

                /* --------------------- Apply Manifest ---------------------- */
                message_.info("Applying manifest entries to the coding scheme...");
                try {

                    manifestUtil.applyManifest(manifest_, currentURNVersion);

                } catch (LgConvertException e) {
                    message_.fatalAndThrowException("Exception occured while applying manifest: " + e.getMessage());
                } 

                message_.info("Coding scheme is updated with manifest entries.");

                MetaDataLoaderImpl.this.getStatus().setState(ProcessState.COMPLETED);

                message_.info("Manifest process completed without error!");

            } catch (Exception e) {
                message_.fatal("Load failed due to exception." + e.getMessage());

            } finally {
                LoadStatus status = MetaDataLoaderImpl.this.getStatus();
                if (status.getState() == null || !status.getState().equals(ProcessState.COMPLETED)) {
                    status.setState(ProcessState.FAILED);
                }
                status.setEndTime(new Date(System.currentTimeMillis()));
                inUse = false;
            }
        }
    }
}