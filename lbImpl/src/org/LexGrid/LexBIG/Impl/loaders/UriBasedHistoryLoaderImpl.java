package org.LexGrid.LexBIG.Impl.loaders;

import java.io.File;
import java.net.URI;
import java.util.Date;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.LogEntry;
import org.LexGrid.LexBIG.DataModel.Core.types.LogLevel;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.LoadStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Load.OntologyFormat;
import org.LexGrid.LexBIG.Extensions.Load.UriBasedHistoryLoader;
import org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Preferences.loader.LoadPreferences.LoaderPreferences;
import org.LexGrid.LexBIG.Utility.logging.CachingMessageDirectorIF;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.LexGrid.LexOnt.CodingSchemeManifest;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.logging.LoggerFactory;
import org.lexevs.logging.messaging.impl.CachingMessageDirectorImpl;
import org.lexevs.system.service.SystemResourceService;

import edu.mayo.informatics.lexgrid.convert.directConversions.NCIThesaurusHistoryFileToSQL;
import edu.mayo.informatics.lexgrid.convert.formats.Option;
import edu.mayo.informatics.lexgrid.convert.options.BooleanOption;
import edu.mayo.informatics.lexgrid.convert.options.DefaultOptionHolder;
import edu.mayo.informatics.lexgrid.convert.options.StringOption;
import edu.mayo.informatics.lexgrid.convert.options.URIOption;

public class UriBasedHistoryLoaderImpl implements UriBasedHistoryLoader {
    

    
    /**
     * 
     */
    private static final long serialVersionUID = -3252187227741353311L;
    public final static String name = "UriBasedHistoryLoader";
    private final static String description = "This loader loads Files for any source based on Thesaurus history formatted files.";

    private static String VERSIONS_OPTION = "Versions";
    private static String OVERWRITE_OPTION = "Overwrite";
    public static String ASYNC_OPTION = "Async Load";
    
    private  String uri;
    
    private OptionHolder options;
    
    private URI resourceUri;
    
    private CachingMessageDirectorIF messageDirector;
    
    private LoadStatus loadStatus;

    public UriBasedHistoryLoaderImpl(String uri) {
        super();
        options = declareAllowedOptions(new DefaultOptionHolder());
        loadStatus = new LoadStatus();
        this.messageDirector = new CachingMessageDirectorImpl( new MessageDirector(getName(), loadStatus));
        this.uri = uri;
    }
    
    public static void main(String[] args) throws Exception {
        
        UriBasedHistoryLoaderImpl hloader = new UriBasedHistoryLoaderImpl("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl");

        hloader.load(new File("../lbTest/resources/testData/owl2/owl2historytest.txt").toURI(), new File(
                "../lbTest/resources/testData/owl2/owl2systemReleaseTest.txt").toURI(), false, true, true);
    }
    
    protected LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
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
    
    public void setOptions(OptionHolder options) {
        this.options = options;
    }
    
    @Override
    public OptionHolder getOptions() {
        return options;
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
public OntologyFormat getOntologyFormat() {
    return OntologyFormat.NICHISTORY;
}


    @Override
    public String getName() {
       return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getProvider() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getVersion() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void load(URI source, URI versions, boolean append, boolean stopOnErrors, boolean async) throws LBException {
        this.getOptions().getURIOption(VERSIONS_OPTION).setOptionValue(versions);
        this.getOptions().getBooleanOption(OVERWRITE_OPTION).setOptionValue(!append);
        this.getOptions().getBooleanOption(Option.getNameForType(Option.FAIL_ON_ERROR)).setOptionValue(stopOnErrors);
        this.getOptions().getBooleanOption(ASYNC_OPTION).setOptionValue(async);
        this.load(source);
    }

    @Override
    public void validate(URI source, URI versions, int validationLevel) throws LBException {
        // TODO Auto-generated method stub
        
    }
    
    protected OptionHolder declareAllowedOptions(OptionHolder holder) {
        holder.getBooleanOptions().add(new BooleanOption(ASYNC_OPTION, true));
        holder.getBooleanOptions().add(new BooleanOption(Option.getNameForType(Option.FAIL_ON_ERROR), false));
        holder.getStringOptions().add(new StringOption(Option.getNameForType(Option.DELIMITER)));
        holder.getURIOptions().add(new URIOption(VERSIONS_OPTION));
        holder.getBooleanOptions().add(new BooleanOption(OVERWRITE_OPTION, false));
        
        return holder;
    }
    
    private class RunLoad implements Runnable {

        @Override
        public void run() {
            
            boolean overwrite = getOptions().getBooleanOption(OVERWRITE_OPTION).getOptionValue();
            
            SystemResourceService resourceService = LexEvsServiceLocator.getInstance().
                getSystemResourceService();
            try {
                if(resourceService.containsNonCodingSchemeResource(uri)) {
                    if(overwrite) {
                        removePreviousHistory();
                    }
                } else {
                    resourceService.addNciHistoryResourceToSystem(uri);
                }
            } catch (LBParameterException e) {
               throw new RuntimeException(e);
            }
            
            try {
               
                new NCIThesaurusHistoryFileToSQL(
                        uri, 
                        getResourceUri(), 
                        getOptions().getURIOption(VERSIONS_OPTION).getOptionValue(),
                        getOptions().getStringOption(Option.getNameForType(Option.DELIMITER)).getOptionValue(),
                        getOptions().getBooleanOption(Option.getNameForType(Option.FAIL_ON_ERROR)).getOptionValue(),
                        messageDirector);

            } catch (Exception e) {
                loadStatus.setEndTime(new Date());
                loadStatus.setState(ProcessState.FAILED);
                messageDirector.fatal("Error loading NCI History", e);
                loadStatus.setErrorsLogged(true);
     
                messageDirector.info("Removing Resources...");
                try {
                    LexBIGServiceImpl.defaultInstance().getServiceManager(null).removeHistoryService(uri);
                } catch (Exception e1) {
                    messageDirector.warn("Resources cound not be removed.", e1);
                } 
            }
            
            loadStatus.setEndTime(new Date());
            loadStatus.setState(ProcessState.COMPLETED);
            loadStatus.setErrorsLogged(false);
        }
    }

    private void removePreviousHistory() {
        LexEvsServiceLocator.getInstance().
            getDatabaseServiceManager().getNciHistoryService().removeNciHistory(this.uri);
    }
    
    public void setResourceUri(URI resourceUri) {
        this.resourceUri = resourceUri;
    }

    public URI getResourceUri() {
        return resourceUri;
    }
}
