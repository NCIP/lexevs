
package edu.mayo.informatics.lexgrid.convert.Conversions;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.types.PropertyTypes;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedAssociation;
import org.LexGrid.naming.SupportedAssociationQualifier;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.SupportedContainerName;
import org.LexGrid.naming.SupportedContext;
import org.LexGrid.naming.SupportedDataType;
import org.LexGrid.naming.SupportedDegreeOfFidelity;
import org.LexGrid.naming.SupportedEntityType;
import org.LexGrid.naming.SupportedHierarchy;
import org.LexGrid.naming.SupportedLanguage;
import org.LexGrid.naming.SupportedNamespace;
import org.LexGrid.naming.SupportedProperty;
import org.LexGrid.naming.SupportedPropertyLink;
import org.LexGrid.naming.SupportedPropertyQualifier;
import org.LexGrid.naming.SupportedPropertyQualifierType;
import org.LexGrid.naming.SupportedPropertyType;
import org.LexGrid.naming.SupportedRepresentationalForm;
import org.LexGrid.naming.SupportedSortOrder;
import org.LexGrid.naming.SupportedSource;
import org.LexGrid.naming.SupportedSourceRole;
import org.LexGrid.naming.SupportedStatus;
import org.LexGrid.naming.URIMap;
import org.lexevs.dao.database.access.property.PropertyDao.PropertyType;

/**
 * Helper class to maintain supported mappings during EMF conversions
 * and apply them to an EMF-based CodingScheme.
 */
public class SupportedMappings {
    private Map<String, SupportedAssociation> supportedAssociations = null;
    private Map<String, SupportedAssociationQualifier> supportedAssociationQualifiers = null;
    private Map<String, SupportedCodingScheme> supportedCodingSchemes = null;
    private Map<String, SupportedContainerName> supportedContainerName = null;
    private Map<String, SupportedContext> supportedContexts = null;
    private Map<String, SupportedDataType> supportedDataTypes = null;
    private Map<String, SupportedDegreeOfFidelity> supportedDegreesOfFidelity = null;
    private Map<String, SupportedEntityType> supportedEntityTypes = null;
    private Map<String, SupportedHierarchy> supportedHierarchies = null;
    private Map<String, SupportedLanguage> supportedLanguages = null;
    private Map<String, SupportedNamespace> supportedNamespaces = null;
    private Map<String, SupportedProperty> supportedProperties = null;
    private Map<String, SupportedPropertyLink> supportedPropertyLinks = null;
    private Map<String, SupportedPropertyType> supportedPropertyTypes = null;
    private Map<String, SupportedPropertyQualifier> supportedPropertyQualifiers = null;
    private Map<String, SupportedPropertyQualifierType> supportedPropertyQualifierTypes = null;
    private Map<String, SupportedRepresentationalForm> supportedRepresentationalForms = null;
    private Map<String, SupportedSortOrder> supportedSortOrders = null;
    private Map<String, SupportedSource> supportedSources = null;
    private Map<String, SupportedSourceRole> supportedSourceRoles = null;
    private Map<String, SupportedStatus> supportedStatus = null;

    private LgMessageDirectorIF messages_ = null;

    public SupportedMappings(LgMessageDirectorIF messages) {
        messages_ = messages;
    }

    /**
     * Apply all registered values to the given coding scheme.
     * @param codingScheme
     */
    public void applyToCodingScheme(CodingScheme codingScheme){
        // Resolve the coding scheme mappings; create now if not present.
        Mappings mappings = codingScheme.getMappings();
        if (mappings == null) {
            mappings = new Mappings();
            codingScheme.setMappings(mappings);
        }
        // Apply all registered values.
        messages_.info("Applying supported mappings.");
        mappings.setSupportedAssociation(getSupportedAssociations().values().toArray(new SupportedAssociation[0]));
        mappings.setSupportedAssociationQualifier(getSupportedAssociationQualifiers().values().toArray(new SupportedAssociationQualifier[0]));
        mappings.setSupportedCodingScheme(getSupportedCodingSchemes().values().toArray(new SupportedCodingScheme[0]));
        mappings.setSupportedContainerName(getSupportedContainerNames().values().toArray(new SupportedContainerName[0]));
        mappings.setSupportedContext(getSupportedContexts().values().toArray(new SupportedContext[0]));
        mappings.setSupportedDataType(getSupportedDataTypes().values().toArray(new SupportedDataType[0]));
        mappings.setSupportedDegreeOfFidelity(getSupportedDegreesOfFidelity().values().toArray(new SupportedDegreeOfFidelity[0]));
        mappings.setSupportedEntityType(getSupportedEntityTypes().values().toArray(new SupportedEntityType[0]));
        mappings.setSupportedHierarchy(getSupportedHierarchies().values().toArray(new SupportedHierarchy[0]));
        mappings.setSupportedLanguage(getSupportedLanguages().values().toArray(new SupportedLanguage[0]));
        mappings.setSupportedNamespace(getSupportedNamespaces().values().toArray(new SupportedNamespace[0]));
        mappings.setSupportedProperty(getSupportedProperties().values().toArray(new SupportedProperty[0]));
        mappings.setSupportedPropertyLink(getSupportedPropertyLinks().values().toArray(new SupportedPropertyLink[0]));
        mappings.setSupportedPropertyQualifier(getSupportedPropertyQualifiers().values().toArray(new SupportedPropertyQualifier[0]));
        mappings.setSupportedPropertyQualifierType(getSupportedPropertyQualifierTypes().values().toArray(new SupportedPropertyQualifierType[0]));
        mappings.setSupportedPropertyType(getSupportedPropertyTypes().values().toArray(new SupportedPropertyType[0]));
        mappings.setSupportedRepresentationalForm(getSupportedRepresentationalForms().values().toArray(new SupportedRepresentationalForm[0]));
        mappings.setSupportedSortOrder(getSupportedSortOrders().values().toArray(new SupportedSortOrder[0]));
        mappings.setSupportedSource(getSupportedSources().values().toArray(new SupportedSource[0]));
        mappings.setSupportedSourceRole(getSupportedSourceRoles().values().toArray(new SupportedSourceRole[0]));
        mappings.setSupportedStatus(getSupportedStatus().values().toArray(new SupportedStatus[0]));
    }
    
    /////////////////////////////////////////////////////////////////
    // Standard getters - lazy init
    /////////////////////////////////////////////////////////////////
    public Map<String, SupportedAssociation> getSupportedAssociations() {
        if (supportedAssociations == null)
            supportedAssociations = new TreeMap<String, SupportedAssociation>();
        return supportedAssociations;
    }
    public Map<String, SupportedAssociationQualifier> getSupportedAssociationQualifiers() {
        if (supportedAssociationQualifiers == null)
            supportedAssociationQualifiers = new TreeMap<String, SupportedAssociationQualifier>();
        return supportedAssociationQualifiers;
    }
    public Map<String, SupportedCodingScheme> getSupportedCodingSchemes() {
        if (supportedCodingSchemes == null)
            supportedCodingSchemes = new TreeMap<String, SupportedCodingScheme>();
        return supportedCodingSchemes;
    }
    public Map<String, SupportedContainerName> getSupportedContainerNames() {
        if (supportedContainerName == null)
            supportedContainerName = new TreeMap<String, SupportedContainerName>();
        return supportedContainerName;
    }
    public Map<String, SupportedContext> getSupportedContexts() {
        if (supportedContexts == null)
            supportedContexts = new TreeMap<String, SupportedContext>();
        return supportedContexts;
    }
    public Map<String, SupportedDataType> getSupportedDataTypes() {
        if (supportedDataTypes == null)
            supportedDataTypes = new TreeMap<String, SupportedDataType>();
        return supportedDataTypes;
    }
    public Map<String, SupportedDegreeOfFidelity> getSupportedDegreesOfFidelity() {
        if (supportedDegreesOfFidelity == null)
            supportedDegreesOfFidelity = new TreeMap<String, SupportedDegreeOfFidelity>();
        return supportedDegreesOfFidelity;
    }
    public Map<String, SupportedEntityType> getSupportedEntityTypes() {
        if (supportedEntityTypes == null)
            supportedEntityTypes = new TreeMap<String, SupportedEntityType>();
        return supportedEntityTypes;
    }
    public Map<String, SupportedHierarchy> getSupportedHierarchies() {
        if (supportedHierarchies == null)
            supportedHierarchies = new TreeMap<String, SupportedHierarchy>();
        return supportedHierarchies;
    }
    public Map<String, SupportedLanguage> getSupportedLanguages() {
        if (supportedLanguages == null)
            supportedLanguages = new TreeMap<String, SupportedLanguage>();
        return supportedLanguages;
    }
    public Map<String, SupportedNamespace> getSupportedNamespaces() {
        if (supportedNamespaces == null)
            supportedNamespaces = new TreeMap<String, SupportedNamespace>();
        return supportedNamespaces;
    }
    public Map<String, SupportedProperty> getSupportedProperties() {
        if (supportedProperties == null)
            supportedProperties = new TreeMap<String, SupportedProperty>();
        return supportedProperties;
    }
    public Map<String, SupportedPropertyLink> getSupportedPropertyLinks() {
        if (supportedPropertyLinks == null)
            supportedPropertyLinks = new TreeMap<String, SupportedPropertyLink>();
        return supportedPropertyLinks;
    }
    public Map<String, SupportedPropertyQualifier> getSupportedPropertyQualifiers() {
        if (supportedPropertyQualifiers == null)
            supportedPropertyQualifiers = new TreeMap<String, SupportedPropertyQualifier>();
        return supportedPropertyQualifiers;
    }
    public Map<String, SupportedPropertyQualifierType> getSupportedPropertyQualifierTypes() {
        if (supportedPropertyQualifierTypes == null)
            supportedPropertyQualifierTypes = new TreeMap<String, SupportedPropertyQualifierType>();
        return supportedPropertyQualifierTypes;
    }
    public Map<String, SupportedPropertyType> getSupportedPropertyTypes() {
        if (supportedPropertyTypes == null)
            supportedPropertyTypes = new TreeMap<String, SupportedPropertyType>();
        return supportedPropertyTypes;
    }
    public Map<String, SupportedRepresentationalForm> getSupportedRepresentationalForms() {
        if (supportedRepresentationalForms == null)
            supportedRepresentationalForms = new TreeMap<String, SupportedRepresentationalForm>();
        return supportedRepresentationalForms;
    }
    public Map<String, SupportedSortOrder> getSupportedSortOrders() {
        if (supportedSortOrders == null)
            supportedSortOrders = new TreeMap<String, SupportedSortOrder>();
        return supportedSortOrders;
    }
    public Map<String, SupportedSource> getSupportedSources() {
        if (supportedSources == null)
            supportedSources = new TreeMap<String, SupportedSource>();
        return supportedSources;
    }
    public Map<String, SupportedSourceRole> getSupportedSourceRoles() {
        if (supportedSourceRoles == null)
            supportedSourceRoles = new TreeMap<String, SupportedSourceRole>();
        return supportedSourceRoles;
    }
    public Map<String, SupportedStatus> getSupportedStatus() {
        if (supportedStatus == null)
            supportedStatus = new TreeMap<String, SupportedStatus>();
        return supportedStatus;
    }

    /////////////////////////////////////////////////////////////////
    // Helper methods to register new supported values.
    /////////////////////////////////////////////////////////////////
    /**
     * Registers a new mapping, keyed by case-insensitive local id.
     * If an existing item is already registered with the given key,
     * the existing item is replaced if overwrite is specified.
     * @return The item registered for the given local id.
     */
    public SupportedAssociation registerSupportedAssociation(String localId, String uri, String value, boolean overwrite) {
       return this.registerSupportedAssociation(localId, uri, value, null, null, overwrite);
    }
    
    public SupportedAssociation registerSupportedAssociation(String localId, String uri, String value, 
            String entityCode, String entityNamespace, boolean overwrite) {
        Map<String, SupportedAssociation> registry = getSupportedAssociations();
        String key = localId.toLowerCase();
        SupportedAssociation emfObj = registry.get(key);
        if (emfObj == null || overwrite) {
            emfObj = new SupportedAssociation();
            setBaseAttributes(emfObj, localId, uri, value);
            emfObj.setEntityCode(entityCode);
            emfObj.setEntityCodeNamespace(entityNamespace);
            registry.put(key, emfObj);
        }
        return emfObj;
    }

    /**
     * Registers a new mapping, keyed by case-insensitive local id.
     * If an existing item is already registered with the given key,
     * the existing item is replaced if overwrite is specified.
     * @return The item registered for the given local id.
     */
    public SupportedAssociationQualifier registerSupportedAssociationQualifier(String localId, String uri, String value, boolean overwrite) {
        Map<String, SupportedAssociationQualifier> registry = getSupportedAssociationQualifiers();
        String key = localId.toLowerCase();
        SupportedAssociationQualifier emfObj = registry.get(key);
        if (emfObj == null || overwrite) {
            emfObj = new SupportedAssociationQualifier();
            setBaseAttributes(emfObj, localId, uri, value);
            registry.put(key, emfObj);
        }
        return emfObj;
    }

    /**
     * Registers a new mapping, keyed by case-insensitive local id.
     * If an existing item is already registered with the given key,
     * the existing item is replaced if overwrite is specified.
     * @return The item registered for the given local id.
     */
    public SupportedCodingScheme registerSupportedCodingScheme(String localId, String uri, String value, boolean isImported, boolean overwrite) {
        Map<String, SupportedCodingScheme> registry = getSupportedCodingSchemes();
        String key = localId.toLowerCase();
        SupportedCodingScheme emfObj = registry.get(key);
        if (emfObj == null || overwrite) {
            emfObj = new SupportedCodingScheme();
            setBaseAttributes(emfObj, localId, uri, value);
            emfObj.setIsImported(isImported);
            registry.put(key, emfObj);
        }
        return emfObj;
    }

    /**
     * Registers a new mapping, keyed by case-insensitive local id.
     * If an existing item is already registered with the given key,
     * the existing item is replaced if overwrite is specified.
     * @return The item registered for the given local id.
     */
    public SupportedContainerName registerSupportedContainerName(String localId, String uri, String value, boolean overwrite) {
        Map<String, SupportedContainerName> registry = getSupportedContainerNames();
        String key = localId.toLowerCase();
        SupportedContainerName emfObj = registry.get(key);
        if (emfObj == null || overwrite) {
            emfObj = new SupportedContainerName();
            setBaseAttributes(emfObj, localId, uri, value);
            registry.put(key, emfObj);
        }
        return emfObj;
    }

    /**
     * Registers a new mapping, keyed by case-insensitive local id.
     * If an existing item is already registered with the given key,
     * the existing item is replaced if overwrite is specified.
     * @return The item registered for the given local id.
     */
    public SupportedContext registerSupportedContext(String localId, String uri, String value, boolean overwrite) {
        Map<String, SupportedContext> registry = getSupportedContexts();
        String key = localId.toLowerCase();
        SupportedContext emfObj = registry.get(key);
        if (emfObj == null || overwrite) {
            emfObj = new SupportedContext();
            setBaseAttributes(emfObj, localId, uri, value);
            registry.put(key, emfObj);
        }
        return emfObj;
    }

    /**
     * Registers a new mapping, keyed by case-insensitive local id.
     * If an existing item is already registered with the given key,
     * the existing item is replaced if overwrite is specified.
     * @return The item registered for the given local id.
     */
    public SupportedDataType registerSupportedDataType(String localId, String uri, String value, boolean overwrite) {
        Map<String, SupportedDataType> registry = getSupportedDataTypes();
        String key = localId.toLowerCase();
        SupportedDataType emfObj = registry.get(key);
        if (emfObj == null || overwrite) {
            emfObj = new SupportedDataType();
            setBaseAttributes(emfObj, localId, uri, value);
            registry.put(key, emfObj);
        }
        return emfObj;
    }

    /**
     * Registers a new mapping, keyed by case-insensitive local id.
     * If an existing item is already registered with the given key,
     * the existing item is replaced if overwrite is specified.
     * @return The item registered for the given local id.
     */
    public SupportedDegreeOfFidelity registerSupportedDegreeOfFidelity(String localId, String uri, String value, boolean overwrite) {
        Map<String, SupportedDegreeOfFidelity> registry = getSupportedDegreesOfFidelity();
        String key = localId.toLowerCase();
        SupportedDegreeOfFidelity emfObj = registry.get(key);
        if (emfObj == null || overwrite) {
            emfObj = new SupportedDegreeOfFidelity();
            setBaseAttributes(emfObj, localId, uri, value);
            registry.put(key, emfObj);
        }
        return emfObj;
    }

    /**
     * Registers a new mapping, keyed by case-insensitive local id.
     * If an existing item is already registered with the given key,
     * the existing item is replaced if overwrite is specified.
     * @return The item registered for the given local id.
     */
    public SupportedEntityType registerSupportedEntityType(String localId, String uri, String value, boolean overwrite) {
        Map<String, SupportedEntityType> registry = getSupportedEntityTypes();
        String key = localId.toLowerCase();
        SupportedEntityType emfObj = registry.get(key);
        if (emfObj == null || overwrite) {
            emfObj = new SupportedEntityType();
            setBaseAttributes(emfObj, localId, uri, value);
            registry.put(key, emfObj);
        }
        return emfObj;
    }

    /**
     * Registers a new mapping, keyed by case-insensitive local id.
     * If an existing item is already registered with the given key,
     * the existing item is replaced if overwrite is specified.
     * @return The item registered for the given local id.
     */
    public SupportedHierarchy registerSupportedHierarchy(String localId, String uri, String value,
            String rootCode, List<String> associationNames, boolean isForwardNavigable, boolean overwrite)
    {
        Map<String, SupportedHierarchy> registry = getSupportedHierarchies();
        String key = localId.toLowerCase();
        SupportedHierarchy emfObj = registry.get(key);
        if (emfObj == null || overwrite) {
            emfObj = new SupportedHierarchy();
            setBaseAttributes(emfObj, localId, uri, value);
            emfObj.setIsForwardNavigable(isForwardNavigable);
            emfObj.setRootCode(rootCode);
            emfObj.setAssociationNames(associationNames);
            registry.put(key, emfObj);
        }
        return emfObj;
    }

    /**
     * Registers a new mapping, keyed by case-insensitive local id.
     * If an existing item is already registered with the given key,
     * the existing item is replaced if overwrite is specified.
     * @return The item registered for the given local id.
     */
    public SupportedLanguage registerSupportedLanguage(String localId, String uri, String value, boolean overwrite) {
        Map<String, SupportedLanguage> registry = getSupportedLanguages();
        String key = localId.toLowerCase();
        SupportedLanguage emfObj = registry.get(key);
        if (emfObj == null || overwrite) {
            emfObj = new SupportedLanguage();
            setBaseAttributes(emfObj, localId, uri, value);
            registry.put(key, emfObj);
        }
        return emfObj;
    }

    /**
     * Registers a new mapping, keyed by case-insensitive local id.
     * If an existing item is already registered with the given key,
     * the existing item is replaced if overwrite is specified.
     * @return The item registered for the given local id.
     */
    public SupportedNamespace registerSupportedNamespace(String localId, String uri, String value,
            String equivalentCodingScheme, boolean overwrite)
    {
        Map<String, SupportedNamespace> registry = getSupportedNamespaces();
        String key = localId.toLowerCase();
        SupportedNamespace emfObj = registry.get(key);
        if (emfObj == null || overwrite) {
            emfObj = new SupportedNamespace();
            setBaseAttributes(emfObj, localId, uri, value);
            emfObj.setEquivalentCodingScheme(equivalentCodingScheme);
            registry.put(key, emfObj);
        }
        return emfObj;
    }

    /**
     * Registers a new mapping, keyed by case-insensitive local id.
     * If an existing item is already registered with the given key,
     * the existing item is replaced if overwrite is specified.
     * @return The item registered for the given local id.
     */
    public SupportedProperty registerSupportedProperty(String localId, String uri, String value, PropertyTypes propertyType, boolean overwrite) {
        Map<String, SupportedProperty> registry = getSupportedProperties();
        String key = localId.toLowerCase();
        SupportedProperty emfObj = registry.get(key);
        if (emfObj == null || overwrite) {
            emfObj = new SupportedProperty();
            setBaseAttributes(emfObj, localId, uri, value);
            emfObj.setPropertyType(propertyType);
            registry.put(key, emfObj);
        }
        return emfObj;
    }

    /**
     * Registers a new mapping, keyed by case-insensitive local id.
     * If an existing item is already registered with the given key,
     * the existing item is replaced if overwrite is specified.
     * @return The item registered for the given local id.
     */
    public SupportedPropertyLink registerSupportedPropertyLink(String localId, String uri, String value, boolean overwrite) {
        Map<String, SupportedPropertyLink> registry = getSupportedPropertyLinks();
        String key = localId.toLowerCase();
        SupportedPropertyLink emfObj = registry.get(key);
        if (emfObj == null || overwrite) {
            emfObj = new SupportedPropertyLink();
            setBaseAttributes(emfObj, localId, uri, value);
            registry.put(key, emfObj);
        }
        return emfObj;
    }

    /**
     * Registers a new mapping, keyed by case-insensitive local id.
     * If an existing item is already registered with the given key,
     * the existing item is replaced if overwrite is specified.
     * @return The item registered for the given local id.
     */
    public SupportedPropertyQualifier registerSupportedPropertyQualifier(String localId, String uri, String value, boolean overwrite) {
        Map<String, SupportedPropertyQualifier> registry = getSupportedPropertyQualifiers();
        String key = localId.toLowerCase();
        SupportedPropertyQualifier emfObj = registry.get(key);
        if (emfObj == null || overwrite) {
            emfObj = new SupportedPropertyQualifier();
            setBaseAttributes(emfObj, localId, uri, value);
            registry.put(key, emfObj);
        }
        return emfObj;
    }

    /**
     * Registers a new mapping, keyed by case-insensitive local id.
     * If an existing item is already registered with the given key,
     * the existing item is replaced if overwrite is specified.
     * @return The item registered for the given local id.
     */
    public SupportedPropertyQualifierType registerSupportedPropertyQualifierType(String localId, String uri, String value, boolean overwrite) {
        Map<String, SupportedPropertyQualifierType> registry = getSupportedPropertyQualifierTypes();
        String key = localId.toLowerCase();
        SupportedPropertyQualifierType emfObj = registry.get(key);
        if (emfObj == null || overwrite) {
            emfObj = new SupportedPropertyQualifierType();
            setBaseAttributes(emfObj, localId, uri, value);
            registry.put(key, emfObj);
        }
        return emfObj;
    }

    /**
     * Registers a new mapping, keyed by case-insensitive local id.
     * If an existing item is already registered with the given key,
     * the existing item is replaced if overwrite is specified.
     * @return The item registered for the given local id.
     */
    public SupportedPropertyType registerSupportedPropertyType(String localId, String uri, String value, boolean overwrite) {
        Map<String, SupportedPropertyType> registry = getSupportedPropertyTypes();
        String key = localId.toLowerCase();
        SupportedPropertyType emfObj = registry.get(key);
        if (emfObj == null || overwrite) {
            emfObj = new SupportedPropertyType();
            setBaseAttributes(emfObj, localId, uri, value);
            registry.put(key, emfObj);
        }
        return emfObj;
    }

    /**
     * Registers a new mapping, keyed by case-insensitive local id.
     * If an existing item is already registered with the given key,
     * the existing item is replaced if overwrite is specified.
     * @return The item registered for the given local id.
     */
    public SupportedRepresentationalForm registerSupportedRepresentationalForm(String localId, String uri, String value, boolean overwrite) {
        Map<String, SupportedRepresentationalForm> registry = getSupportedRepresentationalForms();
        String key = localId.toLowerCase();
        SupportedRepresentationalForm emfObj = registry.get(key);
        if (emfObj == null || overwrite) {
            emfObj = new SupportedRepresentationalForm();
            setBaseAttributes(emfObj, localId, uri, value);
            registry.put(key, emfObj);
        }
        return emfObj;
    }

    /**
     * Registers a new mapping, keyed by case-insensitive local id.
     * If an existing item is already registered with the given key,
     * the existing item is replaced if overwrite is specified.
     * @return The item registered for the given local id.
     */
    public SupportedSortOrder registerSupportedSortOrder(String localId, String uri, String value, boolean overwrite) {
        Map<String, SupportedSortOrder> registry = getSupportedSortOrders();
        String key = localId.toLowerCase();
        SupportedSortOrder emfObj = registry.get(key);
        if (emfObj == null || overwrite) {
            emfObj = new SupportedSortOrder();
            setBaseAttributes(emfObj, localId, uri, value);
            registry.put(key, emfObj);
        }
        return emfObj;
    }

    /**
     * Registers a new mapping, keyed by case-insensitive local id.
     * If an existing item is already registered with the given key,
     * the existing item is replaced if overwrite is specified.
     * @return The item registered for the given local id.
     */
    public SupportedSource registerSupportedSource(String localId, String uri, String value,
            String assemblyRule, boolean overwrite)
    {
        Map<String, SupportedSource> registry = getSupportedSources();
        String key = localId.toLowerCase();
        SupportedSource emfObj = registry.get(key);
        if (emfObj == null || overwrite) {
            emfObj = new SupportedSource();
            setBaseAttributes(emfObj, localId, uri, value);
            emfObj.setAssemblyRule(assemblyRule);
            registry.put(key, emfObj);
        }
        return emfObj;
    }

    /**
     * Registers a new mapping, keyed by case-insensitive local id.
     * If an existing item is already registered with the given key,
     * the existing item is replaced if overwrite is specified.
     * @return The item registered for the given local id.
     */
    public SupportedSourceRole registerSupportedSourceRole(String localId, String uri, String value, boolean overwrite) {
        Map<String, SupportedSourceRole> registry = getSupportedSourceRoles();
        String key = localId.toLowerCase();
        SupportedSourceRole emfObj = registry.get(key);
        if (emfObj == null || overwrite) {
            emfObj = new SupportedSourceRole();
            setBaseAttributes(emfObj, localId, uri, value);
            registry.put(key, emfObj);
        }
        return emfObj;
    }

    /**
     * Registers a new mapping, keyed by case-insensitive local id.
     * If an existing item is already registered with the given key,
     * the existing item is replaced if overwrite is specified.
     * @return The item registered for the given local id.
     */
    public SupportedStatus registerSupportedStatus(String localId, String uri, String value, boolean overwrite) {
        Map<String, SupportedStatus> registry = getSupportedStatus();
        String key = localId.toLowerCase();
        SupportedStatus emfObj = registry.get(key);
        if (emfObj == null || overwrite) {
            emfObj = new SupportedStatus();
            setBaseAttributes(emfObj, localId, uri, value);
            registry.put(key, emfObj);
        }
        return emfObj;
    }
    
    /**
     * Helper method to apply values standard to all URIMap objects.
     * @param emfObj
     * @param localId
     * @param uri
     * @param value
     */
    private void setBaseAttributes(URIMap emfObj, String localId, String uri, String value) {
        emfObj.setLocalId(localId);
        emfObj.setUri(uri);
        emfObj.setContent(value);
    }
}