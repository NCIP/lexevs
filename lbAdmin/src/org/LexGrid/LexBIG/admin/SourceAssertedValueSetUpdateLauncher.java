package org.LexGrid.LexBIG.admin;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Load.NCItSourceAssertedValueSetUpdateService;
import org.LexGrid.LexBIG.Impl.loaders.SourceAssertedValueSetBatchUpdater;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class SourceAssertedValueSetUpdateLauncher {
    @Option(name="-cs", aliases={"--codingScheme"}, usage="Name of Coding Scheme that asserts values sets") 
    private String codingScheme = "NCI_Thesaurus";
    
    @Option(name="-pv", aliases={"--pversion"}, usage="Previous version of the coding scheme. Optional") 
    private String previousVersion;
    
    @Option(name="-cv", aliases={"--cversion"}, usage="Version of the coding scheme.") 
    private String currentVersion;
    
    @Option(name="-a", aliases={"--association"}, usage="Relationship name asserted by the codingScheme") 
    private String association = "Concept_In_Subset";
    
    @Option(name="-t", aliases={"--target"}, usage="Target to Source resolution.") 
    private String target = "true";
    
    @Option(name="-uri", aliases={"--uri"}, usage="Base uri to build the conding scheme uri upon") 
    private String uri = "http://evs.nci.nih.gov/valueset/";
    
    @Option(name="-o", aliases={"--owner"}, usage="Owener of the value set assertioin") 
    private String owner="NCI";
    
    @Option(name="-s", aliases={"--sourceName"}, usage="Gives the name of the property to resolve the source value against") 
    private String source = "Contributing_Source";

    @Option(name="-cd", aliases={"--conceptDomainName"}, usage="Gives the name of the property to resolve the concept domain value against") 
    private String conceptDomainIndicator = "Semantic_Type";
    
    @Option(name="-sUri", aliases={"--schemeUri"}, usage="Scheme Uri for the coding scheme containing the value set updates") 
    private String schemeUri = NCItSourceAssertedValueSetUpdateService.NCIT_URI;


    public static void main(String[] args) {
       try {
        new SourceAssertedValueSetUpdateLauncher().run(args);
    } catch (LBException | InterruptedException | CmdLineException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    }
    
    public void run(String[] args) throws LBParameterException, LBException, InterruptedException, CmdLineException{
        CmdLineParser parser = new CmdLineParser(this);
        parser.parseArgument(args);   
        new SourceAssertedValueSetBatchUpdater(codingScheme, previousVersion, currentVersion, association, target, uri, owner, source, conceptDomainIndicator, schemeUri).run();
    }


}
