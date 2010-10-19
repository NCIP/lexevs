/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package edu.mayo.informatics.lexgrid.convert.directConversions.LgXMLCommon;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;

/**
 * @author  <A HREF="mailto:scott.bauer@mayo.edu">Scott Bauer </A>
 *
 */
public class SystemReleaseSurvey {
    private AbsoluteCodingSchemeVersionReference codingScheme;
    private boolean propertiesPresent;
    private boolean relationsPropertiesPresent;
    private boolean isLoaded = false;
    private String revisionId;
    private String editHistoryEntryPoint;
    
    public String getEditHistoryEntryPoint() {
        return editHistoryEntryPoint;
    }
    public void setEditHistoryEntryPoint(String editHistoryEntryPoint) {
        this.editHistoryEntryPoint = editHistoryEntryPoint;
    }
    public boolean isLoaded() {
        return isLoaded;
    }
    public void setLoaded(boolean isLoaded) {
        this.isLoaded = isLoaded;
    }
    public String getRevisionId() {
        return revisionId;
    }
    public void setRevisionId(String revisionId) {
        this.revisionId = revisionId;
    }
    public AbsoluteCodingSchemeVersionReference getCodingScheme() {
        return codingScheme;
    }
    public void setCodingScheme(AbsoluteCodingSchemeVersionReference codingScheme) {
        this.codingScheme = codingScheme;
    }
    public boolean isPropertiesPresent() {
        return propertiesPresent;
    }
    public void setPropertiesPresent(boolean propertiesPresent) {
        this.propertiesPresent = propertiesPresent;
    }
    public boolean isRelationsPropertiesPresent() {
        return relationsPropertiesPresent;
    }
    public void setRelationsPropertiesPresent(boolean relationsPropertiesPresent) {
        this.relationsPropertiesPresent = relationsPropertiesPresent;
    }
    public String toString(){
        
        
        return "URI: " + codingScheme.getCodingSchemeURN()
        + "\nversion: " + codingScheme.getCodingSchemeVersion()
        + "\ncoding scheme has properties: " + propertiesPresent
        + "\ncoding scheme relations have properties: " + relationsPropertiesPresent
        + "\nrevision id: " + revisionId    
        + "\nedit history 1st revision id: " + editHistoryEntryPoint;  
        
    }
    
    
}