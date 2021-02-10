
package org.LexGrid.LexBIG.Impl.loaders;

import java.net.URI;
import java.net.URISyntaxException;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.LogEntry;
import org.LexGrid.LexBIG.DataModel.Core.types.LogLevel;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.LoadStatus;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.ExtensionRegistry;
import org.LexGrid.LexBIG.Extensions.Load.GraphingDBLoader;
import org.LexGrid.LexBIG.Extensions.Load.OntologyFormat;
import org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder;
import org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable;
import org.LexGrid.LexBIG.Preferences.loader.LoadPreferences.LoaderPreferences;
import org.LexGrid.LexOnt.CodingSchemeManifest;
import org.lexevs.locator.LexEvsServiceLocator;

public class LexEVSArangoGraphingDbLoader extends AbstractExtendable implements GraphingDBLoader {

    private String versionOrTag;

/**
     * 
     */
private static final long serialVersionUID = -8949642539009509699L;

    //TODO: Move To Launcher and set up command line processing
    public static void main(String[] args) {
        LexEVSArangoGraphingDbLoader loader = new LexEVSArangoGraphingDbLoader();
        loader.useVersionOrTag("18.05b");
        try {
            loader.load(new URI("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#"));
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Override
    public void load(URI resource) {
        long start = System.currentTimeMillis();
        LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getGraphingDatabaseService()
                .loadGraphsForTerminologyURIAndVersion(resource.toString(), versionOrTag);
        System.out.println("Total graph load time for  " + resource.toString() + ": "
                + ((System.currentTimeMillis() - start) / 1000));
    }
    
    public void useVersionOrTag(String versionOrTag){
        this.versionOrTag = versionOrTag;
    }
    
    public void loadGraph(String associationName, String codingSchemeUri, String version){
        LexEvsServiceLocator
        .getInstance()
        .getDatabaseServiceManager()
        .getGraphingDatabaseService()
        .loadGraph(associationName, codingSchemeUri, version);
    }


    @Override
    public OptionHolder getOptions() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void clearLog() {
        // TODO Auto-generated method stub

    }

    @Override
    public AbsoluteCodingSchemeVersionReference[] getCodingSchemeReferences() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public LogEntry[] getLog(LogLevel level) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public LoadStatus getStatus() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setCodingSchemeManifest(CodingSchemeManifest codingSchemeManifest) {
        // TODO Auto-generated method stub

    }

    @Override
    public CodingSchemeManifest getCodingSchemeManifest() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setCodingSchemeManifestURI(URI codingSchemeManifestUri) throws LBException {
        // TODO Auto-generated method stub

    }

    @Override
    public URI getCodingSchemeManifestURI() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public LoaderPreferences getLoaderPreferences() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setLoaderPreferences(LoaderPreferences loaderPreferences) throws LBParameterException {
        // TODO Auto-generated method stub

    }

    @Override
    public void setLoaderPreferences(URI loaderPreferencesURI) throws LBParameterException {
        // TODO Auto-generated method stub

    }

    @Override
    public OntologyFormat getOntologyFormat() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
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
    protected void doRegister(ExtensionRegistry registry, ExtensionDescription description)
            throws LBParameterException {
        registry.registerLoadExtension(description);
    }

    @Override
    protected ExtensionDescription buildExtensionDescription() {
        ExtensionDescription temp = new ExtensionDescription();
        temp.setExtensionBaseClass(LexEVSArangoGraphingDbLoader.class.getInterfaces()[0].getName());
        temp.setExtensionClass(LexEVSArangoGraphingDbLoader.class.getName());
        temp.setDescription(LexEVSArangoGraphingDbLoader.description);
        temp.setName(LexEVSArangoGraphingDbLoader.name);
        
        return temp;
    }


}