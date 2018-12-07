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
package edu.mayo.informatics.lexgrid.convert.directConversions.owlapi;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.LexGrid.LexBIG.Preferences.loader.LoadPreferences.LoaderPreferences;
import org.LexGrid.LexBIG.Preferences.loader.OWLLoadPreferences.OWLLoaderPreferences;

public class PreferenceManager {
    // Constants for preferences loading -- these default to the value in
    // ProtegeOwl2EMFConstants but can be overridden by passing in an
    // OWLLoaderPreferences object.
    private Map<String, String> mapForwardNames = new HashMap<String, String>();
    private Map<String, String> mapReverseNames = new HashMap<String, String>();
    private String matchPattern_conceptCode = OwlApi2LGConstants.MATCH_CONCEPT_CODE;
    private String matchPattern_conceptStatus = OwlApi2LGConstants.MATCH_CONCEPT_STATUS;
    private String matchPattern_inactiveStatus = OwlApi2LGConstants.MATCH_INACTIVE_STATUS;
    private String matchPattern_noopNamespaces = OwlApi2LGConstants.MATCH_NOOP_NAMESPACES;
    private String matchPattern_rootName = OwlApi2LGConstants.MATCH_ROOT_NAME;
    private String matchPattern_xmlRepFormNames = OwlApi2LGConstants.MATCH_XMLREPFORM_NAMES;
    private String matchPattern_xmlSourceNames = OwlApi2LGConstants.MATCH_XMLSOURCE_NAMES;
    private String matchPattern_xmlTextNames = OwlApi2LGConstants.MATCH_XMLTEXT_NAMES;
    private List<String> prioritized_presentation_names = OwlApi2LGConstants.PRIORITIZED_PRESENTATION_NAMES;
    private List<String> prioritized_definition_names = OwlApi2LGConstants.PRIORITIZED_DEFINITION_NAMES;
    private List<String> prioritized_comment_names = OwlApi2LGConstants.PRIORITIZED_COMMENT_NAMES;
    private String propertyName_primitive = OwlApi2LGConstants.PROPNAME_PRIMITIVE;
    private String propertyName_type = OwlApi2LGConstants.PROPNAME_TYPE;
    private boolean processComplexProperties = OwlApi2LGConstants.PROCESS_COMPLEX_PROP;
    private boolean complexProps_isDbxRefSource = OwlApi2LGConstants.IS_DBXREF_SOURCE;
    private boolean complexProps_isDbxRefRepForm = OwlApi2LGConstants.IS_DBXREF_REPFORM;
    private boolean processStrictOWL = OwlApi2LGConstants.STRICT_OWL_IMPLEMENTATION;
    private boolean processConceptsForObjectProperties = OwlApi2LGConstants.CREATE_CONCEPT_FOR_OBJECT_PROP;
    private String dataTypePropertySwitch = OwlApi2LGConstants.DATATYPE_PROP_SWITCH;
    private LoaderPreferences loaderPreference = null;
    private Pattern matchRootName = null;

    public PreferenceManager(LoaderPreferences loaderPreference_) {
        this.loaderPreference = loaderPreference_;
        initPreferences();
    }

    public String getDataTypePropertySwitch() {
        return dataTypePropertySwitch;
    }

    public Map<String, String> getMapForwardNames() {
        return mapForwardNames;
    }

    public Map<String, String> getMapReverseNames() {
        return mapReverseNames;
    }

    public String getMatchPattern_conceptCode() {
        return matchPattern_conceptCode;
    }

    public String getMatchPattern_conceptStatus() {
        return matchPattern_conceptStatus;
    }

    public String getMatchPattern_inactiveStatus() {
        return matchPattern_inactiveStatus;
    }

    public String getMatchPattern_noopNamespaces() {
        return matchPattern_noopNamespaces;
    }

    public String getMatchPattern_rootName() {
        return matchPattern_rootName;
    }

    public String getMatchPattern_xmlRepFormNames() {
        return matchPattern_xmlRepFormNames;
    }

    public String getMatchPattern_xmlSourceNames() {
        return matchPattern_xmlSourceNames;
    }

    public String getMatchPattern_xmlTextNames() {
        return matchPattern_xmlTextNames;
    }

    public Pattern getMatchRootName() {
        return matchRootName;
    }

    public List<String> getPrioritized_comment_names() {
        return prioritized_comment_names;
    }

    public List<String> getPrioritized_definition_names() {
        return prioritized_definition_names;
    }

    public List<String> getPrioritized_presentation_names() {
        return prioritized_presentation_names;
    }

    public String getPropertyName_primitive() {
        return propertyName_primitive;
    }

    public String getPropertyName_type() {
        return propertyName_type;
    }

    /**
     * Loads preferences. If the value is not null in the Preferences Object,
     * that value is used. Otherwise, the default value from
     * ProtegeOwl2EMFConstants is used.
     */
    protected void initPreferences() {
        if (loaderPreference == null)
            return;

        OWLLoaderPreferences owlLoadPrefs = (OWLLoaderPreferences) loaderPreference;
        if (owlLoadPrefs.getPrioritizedPresentationNames() != null) {
            prioritized_presentation_names = Arrays.asList(owlLoadPrefs.getPrioritizedPresentationNames().getName());
        }
        if (owlLoadPrefs.getPrioritizedDefinitionNames() != null) {
            prioritized_definition_names = Arrays.asList(owlLoadPrefs.getPrioritizedDefinitionNames().getName());
        }
        if (owlLoadPrefs.getPrioritizedCommentNames() != null) {
            prioritized_comment_names = Arrays.asList(owlLoadPrefs.getPrioritizedCommentNames().getName());
        }
        if (owlLoadPrefs.getPropnamePrimitive() != null) {
            propertyName_primitive = owlLoadPrefs.getPropnamePrimitive();
        }
        if (owlLoadPrefs.getPropnameType() != null) {
            propertyName_type = owlLoadPrefs.getPropnameType();
        }
        if (owlLoadPrefs.getMatchConceptCode() != null) {
            matchPattern_conceptCode = owlLoadPrefs.getMatchConceptCode();
        }
        if (owlLoadPrefs.getMatchConceptStatus() != null) {
            matchPattern_conceptStatus = owlLoadPrefs.getMatchConceptStatus();
        }
        if (owlLoadPrefs.getMatchNoopNamespaces() != null) {
            matchPattern_noopNamespaces = owlLoadPrefs.getMatchNoopNamespaces();
        }
        if (owlLoadPrefs.getMatchRootName() != null) {
            matchPattern_rootName = owlLoadPrefs.getMatchRootName();
            matchRootName = Pattern.compile(matchPattern_rootName, Pattern.CASE_INSENSITIVE);
        }
        if (owlLoadPrefs.getMatchXMLRepformNames() != null) {
            matchPattern_xmlRepFormNames = owlLoadPrefs.getMatchXMLRepformNames();
        }
        if (owlLoadPrefs.getMatchXMLSourceNames() != null) {
            matchPattern_xmlSourceNames = owlLoadPrefs.getMatchXMLSourceNames();
        }
        if (owlLoadPrefs.getMatchXMLTextNames() != null) {
            matchPattern_xmlTextNames = owlLoadPrefs.getMatchXMLTextNames();
        }
        if (owlLoadPrefs.isIsDBXrefSource() != null) {
            complexProps_isDbxRefSource = owlLoadPrefs.isIsDBXrefSource();
        }
        if (owlLoadPrefs.isIsDBXrefRepform() != null) {
            complexProps_isDbxRefRepForm = owlLoadPrefs.isIsDBXrefRepform();
        }
        if (owlLoadPrefs.isProcessComplexProps() != null) {
            processComplexProperties = owlLoadPrefs.isProcessComplexProps();
        }
        if (owlLoadPrefs.isStrictOWLImplementation() != null) {
            processStrictOWL = owlLoadPrefs.isStrictOWLImplementation();
        }
        if (owlLoadPrefs.isCreateConceptForObjectProp() != null) {
            processConceptsForObjectProperties = owlLoadPrefs.isCreateConceptForObjectProp();
        }
        if (owlLoadPrefs.getDatatypePropSwitch() != null) {
            dataTypePropertySwitch = owlLoadPrefs.getDatatypePropSwitch();
        }
    }

    public boolean isComplexProps_isDbxRefRepForm() {
        return complexProps_isDbxRefRepForm;
    }

    public boolean isComplexProps_isDbxRefSource() {
        return complexProps_isDbxRefSource;
    }

    public boolean isProcessComplexProperties() {
        return processComplexProperties;
    }

    public boolean isProcessConceptsForObjectProperties() {
        return processConceptsForObjectProperties;
    }

    public boolean isProcessStrictOWL() {
        return processStrictOWL;
    }

    public void setComplexProps_isDbxRefRepForm(boolean complexProps_isDbxRefRepForm) {
        this.complexProps_isDbxRefRepForm = complexProps_isDbxRefRepForm;
    }

    public void setComplexProps_isDbxRefSource(boolean complexProps_isDbxRefSource) {
        this.complexProps_isDbxRefSource = complexProps_isDbxRefSource;
    }

    public void setDataTypePropertySwitch(String dataTypePropertySwitch) {
        this.dataTypePropertySwitch = dataTypePropertySwitch;
    }

    public void setMapForwardNames(Map<String, String> mapForwardNames) {
        this.mapForwardNames = mapForwardNames;
    }

    public void setMapReverseNames(Map<String, String> mapReverseNames) {
        this.mapReverseNames = mapReverseNames;
    }

    public void setMatchPattern_conceptCode(String matchPattern_conceptCode) {
        this.matchPattern_conceptCode = matchPattern_conceptCode;
    }

    public void setMatchPattern_conceptStatus(String matchPattern_conceptStatus) {
        this.matchPattern_conceptStatus = matchPattern_conceptStatus;
    }

    public void setMatchPattern_inactiveStatus(String matchPattern_inactiveStatus) {
        this.matchPattern_inactiveStatus = matchPattern_inactiveStatus;
    }

    public void setMatchPattern_noopNamespaces(String matchPattern_noopNamespaces) {
        this.matchPattern_noopNamespaces = matchPattern_noopNamespaces;
    }

    public void setMatchPattern_rootName(String matchPattern_rootName) {
        this.matchPattern_rootName = matchPattern_rootName;
    }

    public void setMatchPattern_xmlRepFormNames(String matchPattern_xmlRepFormNames) {
        this.matchPattern_xmlRepFormNames = matchPattern_xmlRepFormNames;
    }

    public void setMatchPattern_xmlSourceNames(String matchPattern_xmlSourceNames) {
        this.matchPattern_xmlSourceNames = matchPattern_xmlSourceNames;
    }

    public void setMatchPattern_xmlTextNames(String matchPattern_xmlTextNames) {
        this.matchPattern_xmlTextNames = matchPattern_xmlTextNames;
    }

    public void setMatchRootName(Pattern matchRootName) {
        this.matchRootName = matchRootName;
    }

    public void setPrioritized_comment_names(List<String> prioritized_comment_names) {
        this.prioritized_comment_names = prioritized_comment_names;
    }

    public void setPrioritized_definition_names(List<String> prioritized_definition_names) {
        this.prioritized_definition_names = prioritized_definition_names;
    }

    public void setPrioritized_presentation_names(List<String> prioritized_presentation_names) {
        this.prioritized_presentation_names = prioritized_presentation_names;
    }

    public void setProcessComplexProperties(boolean processComplexProperties) {
        this.processComplexProperties = processComplexProperties;
    }

    public void setProcessConceptsForObjectProperties(boolean processConceptsForObjectProperties) {
        this.processConceptsForObjectProperties = processConceptsForObjectProperties;
    }

    public void setProcessStrictOWL(boolean processStrictOWL) {
        this.processStrictOWL = processStrictOWL;
    }

    public void setPropertyName_primitive(String propertyName_primitive) {
        this.propertyName_primitive = propertyName_primitive;
    }

    public void setPropertyName_type(String propertyName_type) {
        this.propertyName_type = propertyName_type;
    }
}