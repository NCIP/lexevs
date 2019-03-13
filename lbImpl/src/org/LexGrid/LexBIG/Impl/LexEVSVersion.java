package org.LexGrid.LexBIG.Impl;

public class LexEVSVersion {
    private static final String VERSION = "@VERSION@";
    private static final String TIMESTAMP = "@TIMESTAMP@";
    
    public static String getLexEVSBuildVersion(){
        return VERSION;
    }
    
    public static String getLexEVSBuildTimestamp(){
        return TIMESTAMP; 
    } 
}
