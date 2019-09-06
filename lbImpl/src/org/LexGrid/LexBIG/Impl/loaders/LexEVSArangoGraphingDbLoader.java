package org.LexGrid.LexBIG.Impl.loaders;

import java.net.URI;
import java.net.URISyntaxException;

import org.lexevs.locator.LexEvsServiceLocator;

public class LexEVSArangoGraphingDbLoader extends LexEVSGraphingDBLoader {

    private String versionOrTag;
    /**
     * 
     */
    private static final long serialVersionUID = -8949642539009509699L;

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
        LexEvsServiceLocator
        .getInstance()
        .getDatabaseServiceManager()
        .getGraphingDatabaseService()
        .loadGraphsForTerminologyURIAndVersion(resource.toString(), versionOrTag);
    }
    
    public void useVersionOrTag(String versionOrTag){
        this.versionOrTag = versionOrTag;
    }

}
