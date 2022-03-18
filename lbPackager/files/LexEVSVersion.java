
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
    private static final String VERSION = "@VERSION@";
    private static final String TIMESTAMP = "@TIMESTAMP@";
    
    public static String getLexEVSBuildVersion(){
        return VERSION;
    }
    
    public static String getLexEVSBuildTimestamp(){
        return TIMESTAMP; 
    } 
}