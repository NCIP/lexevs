package org.LexGrid.LexBIG.admin;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.loaders.SourceAssertedValueSetBatchLoader;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class SourceAssertedValueSetDefinitionLauncher {
    
    @Option(name="-cs", aliases={"--codingScheme"}, usage="Name of Coding Scheme that asserts values sets") 
    private String codingScheme = "NCI_Thesaurus";
    
    @Option(name="-v", aliases={"--version"}, usage="Version of the coding scheme.") 
    private String version;
    
    @Option(name="-a", aliases={"--association"}, usage="Relationship name asserted by the codingScheme") 
    private String association = "Concept_In_Subset";
    
    @Option(name="-t", aliases={"--target"}, usage="Target to Source resolution.") 
    private boolean target=true;
    
    @Option(name="-uri", aliases={"--uri"}, usage="Base uri to build the conding scheme uri upon") 
    private String uri = "http://evs.nci.nih.gov/valueset/";
    
    @Option(name="-o", aliases={"--owner"}, usage="Owener of the value set assertioin") 
    private String owner="NCI";
    
    @Option(name="-s", aliases={"--sourceName"}, usage="Gives the name of the property to resolve the source value against") 
    private String source;

    
    
    

    public static void main(String[] args) {
        try {
            SourceAssertedValueSetDefinitionLauncher launcher = new SourceAssertedValueSetDefinitionLauncher();
            CmdLineParser parser = new CmdLineParser(launcher);
            parser.parseArgument(args);    
            new SourceAssertedValueSetDefinitionLauncher().run();
        } catch (LBParameterException e) {
            e.printStackTrace();
        } catch (CmdLineException e) {
            e.printStackTrace();
        }

    }
    
    public void run() throws LBParameterException{
        new SourceAssertedValueSetBatchLoader(codingScheme, version, association, target, uri, owner).run(source);
    }

}
