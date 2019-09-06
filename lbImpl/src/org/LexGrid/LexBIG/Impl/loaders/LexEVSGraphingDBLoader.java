package org.LexGrid.LexBIG.Impl.loaders;

import java.net.URI;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.LogEntry;
import org.LexGrid.LexBIG.DataModel.Core.types.LogLevel;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.LoadStatus;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Load.GraphingDBLoader;
import org.LexGrid.LexBIG.Extensions.Load.OntologyFormat;
import org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder;
import org.LexGrid.LexBIG.Preferences.loader.LoadPreferences.LoaderPreferences;
import org.LexGrid.LexOnt.CodingSchemeManifest;

public class LexEVSGraphingDBLoader implements GraphingDBLoader {

    /**
     * 
     */
    private static final long serialVersionUID = -8116266082860733013L;

    @Override
    public void load(URI resource) {
        // TODO Auto-generated method stub

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


}
