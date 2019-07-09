package org.LexGrid.LexBIG.Impl;

/**
 * Class that displays the version and timestamp of the last LexEVS service 
 * that was built. 
 * 
 * During the ant build, this file is copied to 
 * lbImpl/src/org/LexGrid/LexBIG/Impl/LexEVSVersion.java and the version 
 * and timestamp are updated with the current values.
 */
public class LexEVSVersion {
    private static final String VERSION = "SNAPSHOT.DEV.SPRINT110.2019.06.10";
    private static final String TIMESTAMP = "2019-07-09_15:03:34";
    
    public static String getLexEVSBuildVersion(){
        return VERSION;
    }
    
    public static String getLexEVSBuildTimestamp(){
        return TIMESTAMP; 
    } 
}
