
package org.LexGrid.LexBIG.Impl.loaders;

import java.net.URI;
import java.util.Date;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.LogEntry;
import org.LexGrid.LexBIG.DataModel.Core.types.LogLevel;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.LoadStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.ExtensionRegistry;
import org.LexGrid.LexBIG.Extensions.Load.OntologyFormat;
import org.LexGrid.LexBIG.Extensions.Load.UMLSHistoryLoader;
import org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable;
import org.LexGrid.LexBIG.Preferences.loader.LoadPreferences.LoaderPreferences;
import org.LexGrid.LexBIG.Utility.logging.CachingMessageDirectorIF;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.LexGrid.LexOnt.CodingSchemeManifest;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.logging.LoggerFactory;
import org.lexevs.logging.messaging.impl.CachingMessageDirectorImpl;
import org.lexevs.system.service.SystemResourceService;

import edu.mayo.informatics.lexgrid.convert.directConversions.UMLSHistoryFileToSQL;
import edu.mayo.informatics.lexgrid.convert.formats.Option;
import edu.mayo.informatics.lexgrid.convert.options.BooleanOption;
import edu.mayo.informatics.lexgrid.convert.options.DefaultOptionHolder;
import edu.mayo.informatics.lexgrid.convert.options.StringOption;

public class UMLSHistoryLoaderImpl extends AbstractExtendable implements UMLSHistoryLoader {

/** Serial Version ID */
private static final long serialVersionUID = 1L;
    /** Holds the loader name String Constant */
    public final static String name = "NCIMetaHistoryLoader";
    /** Holds the loader description string constant. */
    private final static String description = "This loader loads NCI Metathesaurus history files into the database.";

    private static String OVERWRITE_OPTION = "Overwrite";
    public static String ASYNC_OPTION = "Async Load";
    
    private static final String NCIM_URN = "urn:oid:2.16.840.1.113883.3.26.1.2";
    
    private OptionHolder options;
    
    private URI resourceUri;
    
    private CachingMessageDirectorIF messageDirector;
    
    private LoadStatus loadStatus;

    public UMLSHistoryLoaderImpl() {
       super();
       options = declareAllowedOptions(new DefaultOptionHolder());
       loadStatus = new LoadStatus();
       this.messageDirector = new CachingMessageDirectorImpl( new MessageDirector(getName(), loadStatus));
    }

    protected LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }
    
    protected ExtensionDescription buildExtensionDescription(){
        ExtensionDescription temp = new ExtensionDescription();
        temp.setExtensionBaseClass(UMLSHistoryLoaderImpl.class.getInterfaces()[0].getName());
        temp.setExtensionClass(UMLSHistoryLoaderImpl.class.getName());
        temp.setDescription(description);
        temp.setName(name);

        return temp;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.Loaders.LoaderExtensions.NCIHistoryLoader#load(java
     * .net.URI, boolean)
     */
    public void load(URI source, URI versions, boolean append, boolean stopOnErrors, boolean async) throws LBException {
        loadStatus = new LoadStatus();

        loadStatus.setState(ProcessState.PROCESSING);
        loadStatus.setStartTime(new Date(System.currentTimeMillis()));
        this.messageDirector = new CachingMessageDirectorImpl( new MessageDirector(getName(), loadStatus));
        this.load(source);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.Loaders.LoaderExtensions.NCIHistoryLoader#validate
     * (java.net.URI, int)
     */
    public void validate(URI source, URI versions, int validationLevel) throws LBException {
        //
    }

    protected OptionHolder declareAllowedOptions(OptionHolder holder) {
        holder.setIsResourceUriFolder(true);
        
        holder.getBooleanOptions().add(new BooleanOption(ASYNC_OPTION, true));
        holder.getBooleanOptions().add(new BooleanOption(Option.getNameForType(Option.FAIL_ON_ERROR), false));
        holder.getStringOptions().add(new StringOption(Option.getNameForType(Option.DELIMITER)));
        holder.getBooleanOptions().add(new BooleanOption(OVERWRITE_OPTION, false));
        
        return holder;
    }

    @Override
    public void load(URI resource){
        this.setResourceUri(resource);

        loadStatus.setState(ProcessState.PROCESSING);
        loadStatus.setStartTime(new Date(System.currentTimeMillis()));
        
        RunLoad runner = new RunLoad();
        
        if(this.getOptions().getBooleanOption(ASYNC_OPTION).getOptionValue()) {
            Thread conversion = new Thread(runner);
            conversion.start();
        } else {
            runner.run();
        }
    }
    
    private class RunLoad implements Runnable {

        @Override
        public void run() {
            boolean overwrite = getOptions().getBooleanOption(OVERWRITE_OPTION).getOptionValue();

            SystemResourceService resourceService = LexEvsServiceLocator.getInstance().
                getSystemResourceService();
            try {
                if(resourceService.containsNonCodingSchemeResource(NCIM_URN)) {
                    if(overwrite) {
                        removePreviousHistory();
                        messageDirector.info("Overwriting previous history");
                    }
                } else {
                    resourceService.addNciHistoryResourceToSystem(NCIM_URN);
                    messageDirector.info("Adding to any current histories in system");
                }
            } catch (LBParameterException e) {
               throw new RuntimeException("UMLS/Meta History load failed", e);
            }
            
            try {
               
                UMLSHistoryFileToSQL loader = new UMLSHistoryFileToSQL(
                        NCIM_URN, 
                        getOptions().getBooleanOption(Option.getNameForType(Option.FAIL_ON_ERROR)).getOptionValue(),
                        messageDirector, 
                        getOptions().getStringOption(Option.getNameForType(Option.DELIMITER)).getOptionValue());
                
                loader.loadUMLSHistory(getResourceUri());

            } catch (Exception e) {
                loadStatus.setEndTime(new Date());
                loadStatus.setState(ProcessState.FAILED);
                messageDirector.fatal("Error loading NCI History", e);
                loadStatus.setErrorsLogged(true);
                
                messageDirector.info("Removing Resources...");
                try {
                    LexBIGServiceImpl.defaultInstance().getServiceManager(null).removeHistoryService(NCIM_URN);
                } catch (Exception e1) {
                    messageDirector.warn("Resources cound not be removed.", e1);
                } 
     
                return;
            }
            
            loadStatus.setEndTime(new Date());
            loadStatus.setState(ProcessState.COMPLETED);
            loadStatus.setErrorsLogged(false);
        }
        
    }
    
    private void removePreviousHistory() {
        LexEvsServiceLocator.getInstance().
            getDatabaseServiceManager().getNciHistoryService().removeNciHistory(NCIM_URN);
    }
    
    @Override
    public void load(URI source, boolean append, boolean stopOnErrors, boolean async) throws LBException {
        this.getOptions().getBooleanOption(OVERWRITE_OPTION).setOptionValue(!append);
        this.getOptions().getBooleanOption(Option.getNameForType(Option.FAIL_ON_ERROR)).setOptionValue(stopOnErrors);
        this.getOptions().getBooleanOption(ASYNC_OPTION).setOptionValue(async);
        this.load(source);
    }

    @Override
    public void validate(URI source, int validationLevel) throws LBException {
        //
    }

    public void setOptions(OptionHolder options) {
        this.options = options;
    }

    public OptionHolder getOptions() {
        return options;
    }

    public void setResourceUri(URI resourceUri) {
        this.resourceUri = resourceUri;
    }

    public URI getResourceUri() {
        return resourceUri;
    }

    @Override
    public void clearLog() {
        this.messageDirector.clearLog();
    }

    @Override
    public CodingSchemeManifest getCodingSchemeManifest() {
        throw new UnsupportedOperationException();
    }

    @Override
    public URI getCodingSchemeManifestURI() {
        throw new UnsupportedOperationException();
    }

    @Override
    public AbsoluteCodingSchemeVersionReference[] getCodingSchemeReferences() {
        throw new UnsupportedOperationException();
    }

    @Override
    public LoaderPreferences getLoaderPreferences() {
        throw new UnsupportedOperationException();
    }

    @Override
    public LogEntry[] getLog(LogLevel level) {
        return this.messageDirector.getLog(level);
    }

    @Override
    public LoadStatus getStatus() {
        return this.loadStatus;
    }

    @Override
    public void setCodingSchemeManifest(CodingSchemeManifest codingSchemeManifest) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCodingSchemeManifestURI(URI codingSchemeManifestUri) throws LBException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLoaderPreferences(LoaderPreferences loaderPreferences) throws LBParameterException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLoaderPreferences(URI loaderPreferencesURI) throws LBParameterException {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void doRegister(ExtensionRegistry registry, ExtensionDescription description) throws LBParameterException {
        registry.registerLoadExtension(description);
    }
    
    @Override
    public OntologyFormat getOntologyFormat() {
        return OntologyFormat.UMLSHISTORY;
    }
}