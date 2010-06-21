package org.LexGrid.LexBIG.Impl.loaders;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.LoadStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Load.LexGrid_Loader;
import org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.util.SimpleMemUsageReporter;
import org.LexGrid.util.SimpleMemUsageReporter.Snapshot;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.logging.messaging.impl.CachingMessageDirectorImpl;
import org.lexevs.system.service.SystemResourceService;

import edu.mayo.informatics.lexgrid.convert.directConversions.StreamingXMLToSQL;
import edu.mayo.informatics.lexgrid.convert.directConversions.LgXMLCommon.LexGridXMLProcessor;
import edu.mayo.informatics.lexgrid.convert.options.BooleanOption;
import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

public class LexGridMultiLoaderImpl extends BaseLoader implements LexGrid_Loader {

    private static final long serialVersionUID = 5405545553067402760L;
    public final static String name = "LexGrid_Loader";
    private final static String description = "This loader loads LexGrid XML files into the LexGrid database.";
    
    public final static String VALIDATE = "Validate";
    private static boolean validate = true;
    
    public LexGridMultiLoaderImpl() {
       super();
    }
    public void LexGridMultiLoaderbaseLoad(boolean async)throws LBInvocationException {
        
        setStatus(new LoadStatus());

        getStatus().setState(ProcessState.PROCESSING);
        getStatus().setStartTime(new Date(System.currentTimeMillis()));
        setMd_(new CachingMessageDirectorImpl( new MessageDirector(getName(), getStatus())));

        if (async) {
            Thread conversion = new Thread(new DoConversion());
            conversion.start();
        } else {
            new DoConversion().run();
        }
     }
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Load.LexGrid_Loader#validate(java.net.URI, int)
     */
    public void validate(URI uri, int validationLevel) throws LBParameterException {
        throw new UnsupportedOperationException();
    }
    
    public void load(URI source, boolean stopOnErrors, boolean async) throws LBException {
        this.getOptions().getBooleanOption(FAIL_ON_ERROR_OPTION).setOptionValue(stopOnErrors);
        this.getOptions().getBooleanOption(ASYNC_OPTION).setOptionValue(async);
        
        this.load(source);
    }
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Load.LexGrid_Loader#load(java.net.URI, boolean, boolean)
     */
    public void load(URI uri) {
        setResourceUri(uri);
        try {
           boolean async = this.getOptions().getBooleanOption(ASYNC_OPTION).getOptionValue();
           this.setCodingSchemeManifestURI(this.getOptions().getURIOption(MANIFEST_FILE_OPTION).getOptionValue());
           this.setLoaderPreferences(this.getOptions().getURIOption(LOADER_PREFERENCE_FILE_OPTION).getOptionValue());
            LexGridMultiLoaderbaseLoad(async);
        } catch (LBException e) {
            throw new RuntimeException(e);
        }
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
     * @see org.LexGrid.LexBIG.Impl.loaders.BaseLoader#doLoad()
     */
    @Override
    protected URNVersionPair[] doLoad() throws CodingSchemeAlreadyLoadedException {
        StreamingXMLToSQL loader = new StreamingXMLToSQL();
        
        CodingScheme[] codingScheme = loader.load(
                this.getResourceUri(),
                this.getMd_(),
                this.getOptions().getBooleanOption(LexGridMultiLoaderImpl.VALIDATE).getOptionValue(),
                this.getCodingSchemeManifest());
   
        return this.constructVersionPairsFromCodingSchemes(codingScheme);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable#buildExtensionDescription()
     */
    @Override
    protected ExtensionDescription buildExtensionDescription() {
        ExtensionDescription temp = new ExtensionDescription();
        temp.setExtensionBaseClass(LexGridMultiLoaderImpl.class.getInterfaces()[0].getName());
        temp.setExtensionClass(LexGridMultiLoaderImpl.class.getName());
        temp.setDescription(LexGridMultiLoaderImpl.description);
        temp.setName(LexGridMultiLoaderImpl.name);
        
        return temp;
    }
    
    private class DoConversion implements Runnable {
        
       
        public void run() {
            URNVersionPair[] locks = null;
            try {          
                
                // Actually do the load
                URNVersionPair[] loadedCodingSchemes = doLoad();
                
                //Must be loading a value domain or picklist if this condition is true ... exit
                if(loadedCodingSchemes[0].getUrn()== LexGridXMLProcessor.NO_SCHEME_URL 
                        &&  loadedCodingSchemes[0].getVersion()== LexGridXMLProcessor.NO_SCHEME_VERSION){
                    
                    if (getStatus().getErrorsLogged() != null && !getStatus().getErrorsLogged().booleanValue()) {

                        getMd_().info("Finished loading the DB");
                        Snapshot snap = SimpleMemUsageReporter.snapshot();
                        getMd_().info("Read Time : " + SimpleMemUsageReporter.formatTimeDiff(snap.getTimeDelta(null))
                                + " Heap Usage: " + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsage())
                                + " Heap Delta:" + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsageDelta(null)));

                        getStatus().setState(ProcessState.COMPLETED);
                        getMd_().info("Load process completed without error");
                    }
                    return;
                }
                
                setCodingSchemeReferences(urnVersionPairToAbsoluteCodingSchemeVersionReference(loadedCodingSchemes));

                String[] codingSchemeNames = new String[loadedCodingSchemes.length];
                for (int i = 0; i < loadedCodingSchemes.length; i++) {
                    codingSchemeNames[i] = loadedCodingSchemes[i].getUrn();
                }

                if (getStatus().getErrorsLogged() != null && !getStatus().getErrorsLogged().booleanValue()) {

                    getMd_().info("Finished loading the DB");
                    Snapshot snap = SimpleMemUsageReporter.snapshot();
                    getMd_().info("Read Time : " + SimpleMemUsageReporter.formatTimeDiff(snap.getTimeDelta(null))
                            + " Heap Usage: " + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsage())
                            + " Heap Delta:" + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsageDelta(null)));

                    //Pre-register to make this available before indexing.
                    if(isDoRegister()) {
                        register(loadedCodingSchemes);
                    }
                    
                    doPostProcessing(getOptions(), getCodingSchemeReferences());
                    
                    doTransitiveAndIndex(getCodingSchemeReferences());

                    getMd_().info("After Indexing");
                    snap = SimpleMemUsageReporter.snapshot();
                    getMd_().info("Read Time : " + SimpleMemUsageReporter.formatTimeDiff(snap.getTimeDelta(null))
                            + " Heap Usage: " + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsage())
                            + " Heap Delta:" + SimpleMemUsageReporter.formatMemStat(snap.getHeapUsageDelta(null)));

                    getStatus().setState(ProcessState.COMPLETED);
                    getMd_().info("Load process completed without error");
                    
                    //Register again (to set as INACTIVE)
                    if(isDoRegister()) {
                        register(loadedCodingSchemes);
                    }
                    
                }
                
            } catch (CodingSchemeAlreadyLoadedException e) {
                getStatus().setState(ProcessState.FAILED);
                getMd_().fatal(e.getMessage());
            } catch (Exception e) {
                getStatus().setState(ProcessState.FAILED);
                getMd_().fatal("Failed while running the conversion", e);
            } finally {
                if (getStatus().getState() == null || !getStatus().getState().equals(ProcessState.COMPLETED)) {
                    getStatus().setState(ProcessState.FAILED);

                    try {
                        if (locks != null) {
                            for (int i = 0; i < locks.length; i++) {
                                unlock(locks[i]);
                            }
                        }

                        getLogger().warn("Load failed.  Removing temporary resources...");
                        SystemResourceService service = 
                               LexEvsServiceLocator.getInstance().getSystemResourceService();
                        
                        for(AbsoluteCodingSchemeVersionReference ref : getCodingSchemeReferences()) {
                            service.removeCodingSchemeResourceFromSystem(ref.getCodingSchemeURN(), ref.getCodingSchemeVersion());
                        }

                    } catch (LBParameterException e) {
                        // do nothing - means that the requested delete item
                        // didn't exist.
                    } catch (Exception e) {
                        getLogger().warn("Problem removing temporary resources", e);
                    }

                }
                getStatus().setEndTime(new Date(System.currentTimeMillis()));
                inUse = false;
            }

        }
    }
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Load.LexGrid_Loader#getSchemaURL()
     */
    public URI getSchemaURL() {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Load.LexGrid_Loader#getSchemaVersion()
     */
    public String getSchemaVersion() {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#finalize()
     */
    public void finalize() throws Throwable {
        getLogger().loadLogDebug("Freeing LexGridMultiLoaderImpl");
        super.finalize();
    }
    /**
     * @param args
     */
    public static void main(String[] args){
        LexGridMultiLoaderImpl loader = new LexGridMultiLoaderImpl();
        loader.addBooleanOptionValue(LexGridMultiLoaderImpl.VALIDATE, validate);
        URI uri = null;
        try {
            uri = new URI(args[0]);
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        loader.load(uri);
    }


}
