
package org.LexGrid.LexBIG.Impl.dataAccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBRuntimeException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.Intersection;
import org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.RestrictToAssociations;
import org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.RestrictToCodeSystem;
import org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.RestrictToCodes;
import org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.RestrictToDirectionalNames;
import org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.RestrictToSourceCodeSystem;
import org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.RestrictToSourceCodes;
import org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.RestrictToTargetCodeSystem;
import org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.RestrictToTargetCodes;
import org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.Union;
import org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.interfaces.CodeRestriction;
import org.LexGrid.LexBIG.Impl.codedNodeGraphOperations.interfaces.Operation;
import org.LexGrid.LexBIG.Impl.helpers.GraphQuestionQuery;
import org.LexGrid.LexBIG.Impl.helpers.KnownConceptReference;
import org.LexGrid.LexBIG.Impl.helpers.graph.GAssociationInfo;
import org.LexGrid.LexBIG.Impl.helpers.graph.GHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.concepts.Comment;
import org.LexGrid.concepts.Definition;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.concepts.PropertyLink;
import org.LexGrid.util.sql.DBUtility;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.LexGrid.versions.EntryState;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.connection.SQLInterface;
import org.lexevs.dao.database.service.codednodegraph.model.CountConceptReference;
import org.lexevs.exceptions.MissingResourceException;
import org.lexevs.exceptions.UnexpectedInternalError;
import org.lexevs.system.ResourceManager;

/**
 * SQL Queries necessary for LexBIG operations.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 * @version subversion $Revision: $ checked in on $Date: $
 */
@Deprecated
public class SQLImplementedMethods {
    protected static Map<String, String> csNamespaceToName_ = new HashMap<String, String>();
    
    protected static LgLoggerIF getLogger() {
        return ResourceManager.instance().getLogger();
    }

    public static Entity buildCodedEntry(String internalCodingSchemeName, String internalVersionString,
            String code, String namespace, LocalNameList restrictToProperties, PropertyType[] restrictToPropertyTypes)
            throws UnexpectedInternalError, MissingResourceException {
  
        try {   
            Entity concept = new Entity();
            concept.setEntityCode(code);

            SQLInterface si = ResourceManager.instance().getSQLInterface(internalCodingSchemeName,
                    internalVersionString);
            
            //if the namespace is null (and its 2009 model), set it to the default (which is
            //equal to the codingSchemeName.
            //This shouldn't ever happen -- all classes that call this method should provide
            //a namespace.
            if (si.supports2009Model() && StringUtils.isBlank(namespace)){
                namespace = internalCodingSchemeName;
            }

            ArrayList<Definition> definitions = new ArrayList<Definition>();
            ArrayList<Presentation> presentations = new ArrayList<Presentation>();
            ArrayList<Property> properties = new ArrayList<Property>();
            ArrayList<Comment> comments = new ArrayList<Comment>();

            ArrayList<PropertyLink> links = new ArrayList<PropertyLink>();

            PreparedStatement getEntityCode = null;
            PreparedStatement getEntityType = null;
            PreparedStatement getEntityProperties = null;
            PreparedStatement getPropertyLinks = null;

            try {
                StringBuffer buildEntity = new StringBuffer();
                
                buildEntity.append("Select * " + " from "
                        + si.getTableName(SQLTableConstants.ENTITY)
                        + " {AS} t1 " );
                
                if(si.supports2009Model()){
                    buildEntity.append("left join " 
                            + si.getTableName(SQLTableConstants.ENTRY_STATE)
                            + " {AS} t2 "
                            + "on t1." + SQLTableConstants.TBLCOL_ENTRYSTATEID
                            + " = t2." + SQLTableConstants.TBLCOL_ENTRYSTATEID);  
                }
                
                buildEntity.append(" where "
                        + si.getSQLTableConstants().codingSchemeNameOrId + " = ? AND "
                        + si.getSQLTableConstants().entityCodeOrId + " = ?");
                
                if(si.supports2009Model()){
                    buildEntity.append(" AND "
                            + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE + " = ?");
                }
                
                getEntityCode = si.modifyAndCheckOutPreparedStatement(buildEntity.toString());

                getEntityCode.setString(1, internalCodingSchemeName);
                getEntityCode.setString(2, code);
                if(si.supports2009Model()){
                    getEntityCode.setString(3, namespace);
                }

                ResultSet results = getEntityCode.executeQuery();

                // one and only one result
                if (results.next()) {
                    concept
                            .setIsDefined(DBUtility
                                    .getBooleanFromResultSet(results, SQLTableConstants.TBLCOL_ISDEFINED));
                    concept.setIsAnonymous(DBUtility.getBooleanFromResultSet(results,
                            SQLTableConstants.TBLCOL_ISANONYMOUS));
                    concept.setIsActive(DBUtility.getBooleanFromResultSet(results, SQLTableConstants.TBLCOL_ISACTIVE));

                    if (!si.supports2009Model()) {
                        concept.setStatus(results.getString(SQLTableConstants.TBLCOL_CONCEPTSTATUS));
                    } else {
                        concept.setEntityCodeNamespace(namespace);
                    }

                    EntityDescription ed = new EntityDescription();
                    ed.setContent(results.getString(SQLTableConstants.TBLCOL_ENTITYDESCRIPTION));
                    concept.setEntityDescription(ed);
   
                if (si.supports2009Model()) {
                        String owner = results.getString(SQLTableConstants.TBLCOL_OWNER);
                        String status = results.getString(SQLTableConstants.TBLCOL_STATUS);
                        Timestamp effectiveDate = results.getTimestamp(SQLTableConstants.TBLCOL_EFFECTIVEDATE);
                        Timestamp expirationDate = results.getTimestamp(SQLTableConstants.TBLCOL_EXPIRATIONDATE);
                        String revisionId = results.getString(SQLTableConstants.TBLCOL_REVISIONID);
                        String prevRevisionId = results.getString(SQLTableConstants.TBLCOL_PREVREVISIONID);
                        String changeType = results.getString(SQLTableConstants.TBLCOL_CHANGETYPE);
                        String relativeOrder = results.getString(SQLTableConstants.TBLCOL_RELATIVEORDER);

                        EntryState es = new EntryState();

                        if (!StringUtils.isBlank(changeType)) {
                            es.setChangeType(org.LexGrid.versions.types.ChangeType.valueOf(changeType));
                        }
                        es.setContainingRevision(revisionId);
                        es.setPrevRevision(prevRevisionId);
                        
                        es.setRelativeOrder(computeRelativeOrder(relativeOrder));

                        concept.setEntryState(es);

                        if (owner != null) {
                            concept.setOwner(owner);
                        }
                        concept.setStatus(status);
                        concept.setEffectiveDate(effectiveDate);
                        concept.setExpirationDate(expirationDate);
                    }
                }
                
                results.close();
                si.checkInPreparedStatement(getEntityCode);

                if (si.supports2009Model()) {
                    getEntityType = si.checkOutPreparedStatement("Select * " + " from "
                            + si.getTableName(SQLTableConstants.ENTITY_TYPE) + " where "
                            + si.getSQLTableConstants().codingSchemeNameOrId + " = ? AND "
                            + si.getSQLTableConstants().entityCodeOrId + " = ? AND "
                            + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE + " = ?");

                    getEntityType.setString(1, internalCodingSchemeName);
                    getEntityType.setString(2, code);
                    getEntityType.setString(3, namespace);

                    results = getEntityType.executeQuery();
                    while (results.next()) {
                        concept.addEntityType(results.getString(SQLTableConstants.TBLCOL_ENTITYTYPE));
                    }

                    results.close();
                    si.checkInPreparedStatement(getEntityType);  
                } else {
                    concept.addEntityType(SQLTableConstants.ENTITYTYPE_CONCEPT);
                }     

                // populate the property links
                String addWhereSegment = (!si.supports2009Model() ? (si.getSQLTableConstants().entityType + " = '"
                        + SQLTableConstants.ENTITYTYPE_CONCEPT + "' and ") : "");

                getPropertyLinks = si.checkOutPreparedStatement("Select " + SQLTableConstants.TBLCOL_SOURCEPROPERTYID
                        + ", " + SQLTableConstants.TBLCOL_LINK + ", " + SQLTableConstants.TBLCOL_TARGETPROPERTYID
                        + " from " + si.getTableName(SQLTableConstants.ENTITY_PROPERTY_LINKS) + " where "
                        + addWhereSegment + si.getSQLTableConstants().entityCodeOrEntityId + " = ? and "
                        + si.getSQLTableConstants().codingSchemeNameOrId + " = ?");
                getPropertyLinks.setString(1, code);
                getPropertyLinks.setString(2, internalCodingSchemeName);

                results = getPropertyLinks.executeQuery();

                while (results.next()) {
                    String sourcePropertyId = results.getString(SQLTableConstants.TBLCOL_SOURCEPROPERTYID);
                    String link = results.getString(SQLTableConstants.TBLCOL_LINK);
                    String targetPropertyId = results.getString(SQLTableConstants.TBLCOL_TARGETPROPERTYID);

                    PropertyLink pl = new PropertyLink();
                    pl.setPropertyLink(link);
                    pl.setSourceProperty(sourcePropertyId);
                    pl.setTargetProperty(targetPropertyId);
                    links.add(pl);
                }
                results.close();
                si.checkInPreparedStatement(getPropertyLinks);

                // codedEntry.setModVersion(null);

                StringBuffer propertyQuery = new StringBuffer();

                // I'm constructing a left join query to get the property
                // results I need from 3 (or 2 in 1.5 table version) different
                // tables at once, rather than doing a query on each.

                propertyQuery.append("SELECT a." + SQLTableConstants.TBLCOL_PROPERTYID + ", a."
                        + SQLTableConstants.TBLCOL_PROPERTYNAME + ", a." + SQLTableConstants.TBLCOL_LANGUAGE + ", a."
                        + SQLTableConstants.TBLCOL_FORMAT + ", a." + SQLTableConstants.TBLCOL_ISPREFERRED + ", a."
                        + SQLTableConstants.TBLCOL_DEGREEOFFIDELITY + ", a."
                        + SQLTableConstants.TBLCOL_MATCHIFNOCONTEXT + ", a."
                        + SQLTableConstants.TBLCOL_REPRESENTATIONALFORM + ", a."
                        + SQLTableConstants.TBLCOL_PROPERTYVALUE + ", a." + SQLTableConstants.TBLCOL_PROPERTYTYPE
                        + ( si.supports2009Model() ? (", a." + SQLTableConstants.TBLCOL_ENTRYSTATEID) : "")
                        + ( si.supports2009Model() ? ", es.*" : "")
                        + ", b." + SQLTableConstants.TBLCOL_TYPENAME + ", b." + SQLTableConstants.TBLCOL_ATTRIBUTEVALUE
                        + ", b." + SQLTableConstants.TBLCOL_VAL1 + ", b." + SQLTableConstants.TBLCOL_VAL2);

                propertyQuery.append(" FROM ");

                String codingSchemeName = si.getSQLTableConstants().codingSchemeNameOrId;
                String concptCode = si.getSQLTableConstants().entityCodeOrEntityId;

                propertyQuery.append(si.getTableName(SQLTableConstants.ENTITY_PROPERTY) + " {AS} a ");
                propertyQuery.append(" left join "
                        + si.getTableName(SQLTableConstants.ENTITY_PROPERTY_MULTI_ATTRIBUTES));
                propertyQuery.append(" {AS} b on a." + codingSchemeName + " = b." + codingSchemeName + " and a."
                        + concptCode + " = b." + concptCode + " and a." + SQLTableConstants.TBLCOL_PROPERTYID + " = b."
                        + SQLTableConstants.TBLCOL_PROPERTYID);
                
                if(si.supports2009Model()){
                    propertyQuery.append(" left join " + si.getTableName(SQLTableConstants.ENTRY_STATE) + " {AS} es ");
                    propertyQuery.append("on a." + SQLTableConstants.TBLCOL_ENTRYSTATEID);
                    propertyQuery.append(" = es." + SQLTableConstants.TBLCOL_ENTRYSTATEID);
                }
  
                propertyQuery.append(" where a." + concptCode + " = ? " +
                		"and a." + codingSchemeName + " = ?");
                if(si.supports2009Model()){
                    propertyQuery.append(" and a." + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE + " = ?");
                }

                if (restrictToProperties != null && restrictToProperties.getEntryCount() > 0) {
                    propertyQuery.append(" AND (");
                    for (int i = 0; i < restrictToProperties.getEntryCount(); i++) {
                        propertyQuery.append("  " + si.getSQLTableConstants().propertyOrPropertyName + " = ? ");
                        if (i + 1 < restrictToProperties.getEntryCount()) {
                            propertyQuery.append(" OR ");
                        }
                    }
                    propertyQuery.append(")");

                }

                if (restrictToPropertyTypes != null && restrictToPropertyTypes.length > 0) {
                    propertyQuery.append(" AND (");

                    for (int i = 0; i < restrictToPropertyTypes.length; i++) {
                        propertyQuery.append(" " + SQLTableConstants.TBLCOL_PROPERTYTYPE + " = ? ");
                        if (i + 1 < restrictToPropertyTypes.length) {
                            propertyQuery.append(" OR ");
                        }
                    }
                    propertyQuery.append(")");

                }

                getEntityProperties = si.modifyAndCheckOutPreparedStatement(propertyQuery.toString());

                int i = 1;
                getEntityProperties.setString(i++, code);
                getEntityProperties.setString(i++, internalCodingSchemeName);
                if(si.supports2009Model()){
                    getEntityProperties.setString(i++, namespace);
                }
                
                if (restrictToProperties != null && restrictToProperties.getEntryCount() > 0) {
                    for (int j = 0; j < restrictToProperties.getEntryCount(); j++) {
                        getEntityProperties.setString(i++, restrictToProperties.getEntry(j));
                    }
                }
                if (restrictToPropertyTypes != null && restrictToPropertyTypes.length > 0) {
                    for (int j = 0; j < restrictToPropertyTypes.length; j++) {
                        String pts = RestrictionImplementations.mapPropertyType(restrictToPropertyTypes[j]);
                        getEntityProperties.setString(i++, pts);
                    }
                }

                results = getEntityProperties.executeQuery();

                // store the property from the last row
                org.LexGrid.commonTypes.Property newProperty = null;

                // all of the fields that come from the Property table
                String propertyType, property, propertyValue, language, presentationFormat, degreeOfFidelity, propertyId, representationalForm;
                Boolean matchIfNoContext, isPreferred;            

                // holders for attributes, qualifiers
                Hashtable<String, Source> sources = null;
                HashSet<String> usageContexts = null;
                Hashtable<String, PropertyQualifier> propertyQualifiers = null;

                // As I process the result rows, I will get back duplicates of
                // the property information
                // if the property has more than one qualifer and/or source ,
                // etc.

                while (results.next()) {
                    propertyId = results.getString(SQLTableConstants.TBLCOL_PROPERTYID);

                    if (newProperty == null || !propertyId.equals(newProperty.getPropertyId())) {
                        // not equal means we have started a new property
                        property = results.getString(si.getSQLTableConstants().propertyOrPropertyName);
                        propertyType = results.getString(SQLTableConstants.TBLCOL_PROPERTYTYPE);
                        propertyValue = results.getString(SQLTableConstants.TBLCOL_PROPERTYVALUE);
                        language = results.getString(SQLTableConstants.TBLCOL_LANGUAGE);
                        presentationFormat = results.getString(si.getSQLTableConstants().formatOrPresentationFormat);
                        degreeOfFidelity = results.getString(SQLTableConstants.TBLCOL_DEGREEOFFIDELITY);
                        representationalForm = results.getString(SQLTableConstants.TBLCOL_REPRESENTATIONALFORM);
                        matchIfNoContext = DBUtility.getBooleanFromResultSet(results,
                                SQLTableConstants.TBLCOL_MATCHIFNOCONTEXT);
                        isPreferred = DBUtility.getBooleanFromResultSet(results, SQLTableConstants.TBLCOL_ISPREFERRED);
                        
                        // add all of the collected sources, usage contexts, and
                        // qualifiers to
                        // the previous property
                        if (newProperty != null) {
                            newProperty.setSource(sources.values().toArray(new Source[sources.size()]));
                            newProperty.setUsageContext(usageContexts.toArray(new String[usageContexts.size()]));
                            if (!propertyQualifiers.isEmpty())
                                newProperty.setPropertyQualifier(propertyQualifiers.values().toArray(
                                        new PropertyQualifier[propertyQualifiers.size()]));
                        }

                        // we are starting a new property, so clear out the old
                        // holders.
                        sources = new Hashtable<String, Source>();
                        usageContexts = new HashSet<String>();
                        propertyQualifiers = new Hashtable<String, PropertyQualifier>();

                        // process the property portion of the result
                        if (propertyType.equals(SQLTableConstants.TBLCOLVAL_DEFINITION)) {
                            Definition def = new Definition();
                            def.setIsPreferred(isPreferred);
                            def.setLanguage(language);
                            def.setPropertyName(property);
                            def.setPropertyId(propertyId);
                            Text text = new Text();
                            text.setContent(propertyValue);
                            text.setDataType(presentationFormat);
                            def.setValue(text);
                            definitions.add(def);
                            newProperty = def;
                        } else if (propertyType.equals(SQLTableConstants.TBLCOLVAL_PRESENTATION)) {
                            Presentation presentation = new Presentation();
                            presentation.setIsPreferred(isPreferred);
                            presentation.setLanguage(language);
                            presentation.setPropertyName(property);
                            presentation.setPropertyId(propertyId);
                            Text text = new Text();
                            text.setContent(propertyValue);
                            text.setDataType(presentationFormat);
                            presentation.setValue(text);
                            presentation.setDegreeOfFidelity(degreeOfFidelity);
                            presentation.setMatchIfNoContext(matchIfNoContext);
                            presentation.setRepresentationalForm(representationalForm);

                            presentations.add(presentation);
                            newProperty = presentation;
                        } else if (propertyType.equals(SQLTableConstants.TBLCOLVAL_COMMENT)) {
                            Comment comment = new Comment();
                            comment.setLanguage(language);
                            comment.setPropertyName(property);
                            comment.setPropertyId(propertyId);
                            Text text = new Text();
                            text.setContent(propertyValue);
                            text.setDataType(presentationFormat);
                            comment.setValue(text);
                            comments.add(comment);
                            newProperty = comment;
                        } else {
                            Property theProperty = new Property();
                            theProperty.setLanguage(language);
                            theProperty.setPropertyName(property);
                            theProperty.setPropertyId(propertyId);
                            Text text = new Text();
                            text.setContent(propertyValue);
                            text.setDataType(presentationFormat);
                            theProperty.setValue(text);
                            properties.add(theProperty);
                            newProperty = theProperty;
                        }
                        
                        newProperty.setPropertyType(propertyType);
                        
                        if (si.supports2009Model()) {
                            
                            String owner = results.getString(SQLTableConstants.TBLCOL_OWNER);
                            String status = results.getString(SQLTableConstants.TBLCOL_STATUS);
                            Timestamp effectiveDate = results.getTimestamp(SQLTableConstants.TBLCOL_EFFECTIVEDATE);
                            Timestamp expirationDate = results.getTimestamp(SQLTableConstants.TBLCOL_EXPIRATIONDATE);
                            String revisionId = results.getString(SQLTableConstants.TBLCOL_REVISIONID);
                            String prevRevisionId = results.getString(SQLTableConstants.TBLCOL_PREVREVISIONID);
                            String changeType = results.getString(SQLTableConstants.TBLCOL_CHANGETYPE);
                            String relativeOrder = results.getString(SQLTableConstants.TBLCOL_RELATIVEORDER);

                            if (revisionId != null){
                                EntryState es = new EntryState();
                                if (!StringUtils.isBlank(changeType)) {
                                    es.setChangeType(org.LexGrid.versions.types.ChangeType.valueOf(changeType));
                                }
                                es.setContainingRevision(revisionId);
                                es.setPrevRevision(prevRevisionId);
                                es.setRelativeOrder(computeRelativeOrder(relativeOrder));

                                newProperty.setEntryState(es);
                            }

                            if (owner != null) {                             
                                newProperty.setOwner(owner);
                            }

                            if (status != null)
                                newProperty.setStatus(status);
                            if (effectiveDate != null)
                                newProperty.setEffectiveDate(effectiveDate);
                            if (expirationDate != null)
                                newProperty.setExpirationDate(expirationDate);
                        }
                    }

                    String type = null;
                    String value = null;
                    String val1 = null;
                    String val2 = null;

                    // collect values from the multiAttributes table
                    type = results.getString(SQLTableConstants.TBLCOL_TYPENAME);
                    value = results.getString(SQLTableConstants.TBLCOL_ATTRIBUTEVALUE);
                    val1 = results.getString(SQLTableConstants.TBLCOL_VAL1);
                    if (StringUtils.isBlank(val1))
                        val1 = null;
                    val2 = results.getString(SQLTableConstants.TBLCOL_VAL2);
                    if (StringUtils.isBlank(val2))
                        val2 = null;

                    // hashsets to remove dupes (table doesn't allow dupes, but
                    // left join will create some)
                    if (type != null) {
                        if (type.equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_SOURCE)) {
                            if (!sources.containsKey(
                                    createUniqueKeyForSource(value, val1))) {
                                Source s = new Source();
                                s.setContent(value);
                                s.setRole(val2);
                                s.setSubRef(val1);
                                sources.put(
                                        createUniqueKeyForSource(value, val1), s);
                            }
                        } else if (type.equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_USAGECONTEXT)) {
                            usageContexts.add(value);
                        } else if (type.equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_QUALIFIER)) {
                            // nulls are a side affect of left join
                            if (!propertyQualifiers.containsKey(val1 + ":" + value)) {
                                PropertyQualifier pq = new PropertyQualifier();
                                Text txt = new Text();
                                txt.setContent(val1);
                                pq.setValue(txt);
                                pq.setPropertyQualifierName(value);
                                propertyQualifiers.put(val1 + ":" + value, pq);
                            }
                        } else {
                            getLogger().warn(
                                    "There is invalid data in the 'typeName' column in the table "
                                            + si.getTableName(SQLTableConstants.ENTITY_PROPERTY_MULTI_ATTRIBUTES)
                                            + " for the concept code: " + code + " propertyId: " + propertyId
                                            + " codingSchemeName: " + internalCodingSchemeName);
                        }
                    }
                }

                // add all of the collected sources, usage contexts, and
                // qualifiers to
                // the previous property before exiting ...
                if (newProperty != null) {
                    newProperty.setSource(sources.values().toArray(new Source[sources.size()]));
                    newProperty.setUsageContext(usageContexts.toArray(new String[usageContexts.size()]));
                    if (!propertyQualifiers.isEmpty())
                        newProperty.setPropertyQualifier(propertyQualifiers.values().toArray(
                                new PropertyQualifier[propertyQualifiers.size()]));
                }
                results.close();
            } finally {
                si.checkInPreparedStatement(getEntityCode);
                si.checkInPreparedStatement(getEntityProperties);
                si.checkInPreparedStatement(getPropertyLinks);
            }

            concept.setComment(comments.toArray(new Comment[comments.size()]));
            concept.setDefinition(definitions.toArray(new Definition[definitions.size()]));
            concept.setPropertyLink(links.toArray(new PropertyLink[links.size()]));
            concept.setPresentation(presentations.toArray(new Presentation[presentations.size()]));
            concept.setProperty(properties.toArray(new Property[properties.size()]));
            return concept;
        } catch (MissingResourceException e) {
            throw e;
        } catch (Exception e) {
            throw new UnexpectedInternalError("There was an unexpected internal error.", e);
        }
    }
   
    /**
     * Some databases (mainly MS Access) will return the 'relativeOrder' column value as a decimal.
     * In order to convert it to a Long, we have to cut off the decimal places.
     * 
     * If the String value does not contain decimal places, the value is simply converted to a Long and
     * return.
     * 
     * @param relativeOrder The String value (with or without decimal places).
     * @return The converted Long value (without the decimals).
     */
    private static Long computeRelativeOrder(String relativeOrder){
        if(relativeOrder == null){
            return null;
        }
        
        Long returnLong;
        
        if(relativeOrder.contains(".")){
            Double dub = Double.parseDouble(relativeOrder);
            returnLong = Math.round(dub);    
        } else {
            returnLong = Long.valueOf(relativeOrder);
        }
        return returnLong;
    }

    @Deprecated
    public static EntityDescription buildConceptEntityDescription(String internalCodingSchemeName,
            String internalVersionString, String conceptCode) throws MissingResourceException, UnexpectedInternalError {
        EntityDescription result = new EntityDescription();
        SQLInterface si = ResourceManager.instance().getSQLInterface(internalCodingSchemeName, internalVersionString);
        PreparedStatement getEntityDescription = null;

        try {
            getEntityDescription = si.checkOutPreparedStatement("Select " + SQLTableConstants.TBLCOL_ENTITYDESCRIPTION
                    + " from " + si.getTableName(SQLTableConstants.ENTITY) + " where "
                    + si.getSQLTableConstants().entityCodeOrId + " = ? and "
                    + si.getSQLTableConstants().codingSchemeNameOrId + " = ?");
            getEntityDescription.setString(1, conceptCode);
            getEntityDescription.setString(2, internalCodingSchemeName);

            ResultSet results = getEntityDescription.executeQuery();
            if (results.next()) {
                result.setContent(results.getString(SQLTableConstants.TBLCOL_ENTITYDESCRIPTION));
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new UnexpectedInternalError("There was an unexpected internal error.", e);
        } finally {
            si.checkInPreparedStatement(getEntityDescription);
        }

        return result;
    }

    public static String getCodingSchemeCopyright(String internalCodingSchemeName, String internalVersionString)
            throws MissingResourceException, UnexpectedInternalError {

        String copyRight = "MISSING";
        SQLInterface si = ResourceManager.instance().getSQLInterface(internalCodingSchemeName, internalVersionString);

        PreparedStatement getCodingSchemeDetails = null;
        ResultSet results = null;
        try {
            getCodingSchemeDetails = si.checkOutPreparedStatement("select " + SQLTableConstants.TBLCOL_COPYRIGHT
                    + "  from " + si.getTableName(SQLTableConstants.CODING_SCHEME) + " where "
                    + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + " = ?");

            getCodingSchemeDetails.setString(1, internalCodingSchemeName);

            results = getCodingSchemeDetails.executeQuery();

            if (results.next()) {

                copyRight = results.getString(SQLTableConstants.TBLCOL_COPYRIGHT);

            } else {
                throw new MissingResourceException("The coding scheme " + internalCodingSchemeName
                        + " is not available.");
            }
        } catch (MissingResourceException e) {
            throw e;
        } catch (Exception e) {
            throw new UnexpectedInternalError("There was an unexpected internal error.", e);
        } finally {
            try {
                results.close();
            } catch (SQLException e) {
                // do nothing
            }
            si.checkInPreparedStatement(getCodingSchemeDetails);
        }
        return copyRight;
    }

  
    /**
     * Retrieves CodingScheme Properties from the Database.
     * 
     * @param internalCodingSchemeName CodingScheme name.
     * @param internalVersionString CodingScheme version.
     * @return The fully populated Properties object.
     * @throws MissingResourceException
     * @throws SQLException
     */
    private static Properties getCodingSchemeProperties(String internalCodingSchemeName, String internalVersionString) throws MissingResourceException, SQLException
    {
        SQLInterface si = ResourceManager.instance().getSQLInterface(internalCodingSchemeName, internalVersionString);
        SQLTableConstants stc = si.getSQLTableConstants();

        PreparedStatement selectFromCodingSchemeProp = si.checkOutPreparedStatement(
                " SELECT * " +
                " FROM " + si.getTableName(SQLTableConstants.CODING_SCHEME_PROP) + 
                " WHERE " + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + " = ?");

        PreparedStatement selectFromCodingSchemePropMultiAttributes = si.checkOutPreparedStatement(
                " SELECT * " +
                " FROM " + si.getTableName(SQLTableConstants.CODING_SCHEME_PROP_MULTI_ATTRIB) + 
                " WHERE " + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + " = ? AND " +
                SQLTableConstants.TBLCOL_PROPERTYID + " = ?");

        try {
            String codingSchemeString = internalCodingSchemeName;
            selectFromCodingSchemeProp.setString(1, codingSchemeString);

            ResultSet results = selectFromCodingSchemeProp.executeQuery();
            
            Properties properties = null;

            while (results.next()){
                properties = new Properties();
                Property codingSchemeProperty = new Property();

                String propertyId = results.getString(SQLTableConstants.TBLCOL_PROPERTYID);
                String propertyName = results.getString(stc.propertyOrPropertyName);
                String language = results.getString(SQLTableConstants.TBLCOL_LANGUAGE);

                Boolean isActive = DBUtility.getBooleanFromResultSet(results, SQLTableConstants.TBLCOL_ISACTIVE);

                Text txt = new Text();
                txt.setContent((String)results.getString(SQLTableConstants.TBLCOL_PROPERTYVALUE));
                txt.setDataType(results.getString(stc.formatOrPresentationFormat));

                codingSchemeProperty.setPropertyId(propertyId);
                codingSchemeProperty.setPropertyName(propertyName);
                codingSchemeProperty.setLanguage(language);
                codingSchemeProperty.setIsActive(isActive);
                codingSchemeProperty.setValue(txt);
              
                selectFromCodingSchemePropMultiAttributes.setString(1, codingSchemeString);
                selectFromCodingSchemePropMultiAttributes.setString(2, propertyId);

                ResultSet csPropMultiAttribResults = selectFromCodingSchemePropMultiAttributes.executeQuery();

                while (csPropMultiAttribResults.next()){
                    String attrName = csPropMultiAttribResults.getString(SQLTableConstants.TBLCOL_TYPENAME);
                    String attrValue = csPropMultiAttribResults.getString(SQLTableConstants.TBLCOL_ATTRIBUTEVALUE);
                    String val1 = csPropMultiAttribResults.getString(SQLTableConstants.TBLCOL_VAL1);
                    String val2 = csPropMultiAttribResults.getString(SQLTableConstants.TBLCOL_VAL2);
                    if (StringUtils.isBlank(val1))
                        val1 = null;
                    if (StringUtils.isBlank(val2))
                        val2 = null;

                    if (attrName.equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_SOURCE))
                    {
                        Source s = new Source();
                        s.setContent(attrValue);
                        s.setRole(val2);
                        s.setSubRef(val1);
                        codingSchemeProperty.addSource(s);
                    }
                    else if (attrName.equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_USAGECONTEXT))
                    {
                        codingSchemeProperty.addUsageContext(attrValue);
                    }
                    else if (attrName.equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_QUALIFIER))
                    {
                        PropertyQualifier pq = new PropertyQualifier();
                        pq.setPropertyQualifierType(attrName);
                        pq.setPropertyQualifierName(attrValue);
                        txt = new Text();
                        txt.setContent((String)val1);

                        pq.setValue(txt);
                        codingSchemeProperty.addPropertyQualifier(pq);
                    }
                } 
                csPropMultiAttribResults.close();
                properties.addProperty(codingSchemeProperty);
            } 
            results.close();    
            return properties;
        } finally {
            si.checkInPreparedStatement(selectFromCodingSchemeProp);
            si.checkInPreparedStatement(selectFromCodingSchemePropMultiAttributes);
        }
    }

    public static boolean validateLanguage(String internalCodingSchemeName, String internalVersionString,
            String language) throws UnexpectedInternalError, MissingResourceException, LBParameterException {
        return validateSupported(internalCodingSchemeName, internalVersionString,
                SQLTableConstants.TBLCOLVAL_SUPPTAG_LANGUAGE, language);
    }

    public static boolean validateSource(String internalCodingSchemeName, String internalVersionString, String source)
            throws UnexpectedInternalError, MissingResourceException, LBParameterException {
        return validateSupported(internalCodingSchemeName, internalVersionString,
                SQLTableConstants.TBLCOLVAL_SUPPTAG_SOURCE, source);
    }

    public static boolean validateContext(String internalCodingSchemeName, String internalVersionString, String context)
            throws UnexpectedInternalError, MissingResourceException, LBParameterException {
        return validateSupported(internalCodingSchemeName, internalVersionString,
                SQLTableConstants.TBLCOLVAL_SUPPTAG_CONTEXT, context);
    }

    public static boolean validatePropertyQualifier(String internalCodingSchemeName, String internalVersionString,
            String propertyQualifier) throws UnexpectedInternalError, MissingResourceException, LBParameterException {
        return validateSupported(internalCodingSchemeName, internalVersionString,
                SQLTableConstants.TBLCOLVAL_SUPPTAG_PROPERTYQUALIFIER, propertyQualifier);
    }

    public static boolean validateProperty(String internalCodingSchemeName, String internalVersionString,
            String property) throws UnexpectedInternalError, MissingResourceException, LBParameterException {
        return validateSupported(internalCodingSchemeName, internalVersionString,
                SQLTableConstants.TBLCOLVAL_SUPPTAG_PROPERTY, property);
    }

    private static boolean validateSupported(String internalCodingSchemeName, String internalVersionString,
            String supportedName, String supportedValue) throws UnexpectedInternalError, MissingResourceException,
            LBParameterException {
        if (supportedValue == null || supportedValue.length() == 0) {
            throw new LBParameterException("The parameter is required", "property");
        }

        // first, check the cache.
        String key = "valProp" + internalCodingSchemeName + ":" + internalVersionString + ":" + supportedName + ":"
                + supportedValue;

        Map cache = ResourceManager.instance().getCache();

        Boolean value = (Boolean) cache.get(key);
        if (value != null) {
            if (value.booleanValue()) {
                return true;
            } else {
                throw new LBParameterException("The value supplied for the parameter is invalid.", supportedName,
                        supportedValue);
            }
        }

        SQLInterface si = ResourceManager.instance().getSQLInterface(internalCodingSchemeName, internalVersionString);
        PreparedStatement checkProperty = null;

        try {
            // ok
            checkProperty = si.checkOutPreparedStatement("select count(" + SQLTableConstants.TBLCOL_CODINGSCHEMENAME
                    + ") " + " from " + si.getTableName(SQLTableConstants.CODING_SCHEME_SUPPORTED_ATTRIBUTES)
                    + " where " + SQLTableConstants.TBLCOL_SUPPORTEDATTRIBUTETAG + " = ? and "
                    + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + " = ? and " + SQLTableConstants.TBLCOL_ID + " = ?");

            checkProperty.setString(1, supportedName);
            checkProperty.setString(2, internalCodingSchemeName);
            checkProperty.setString(3, supportedValue);

            ResultSet results = checkProperty.executeQuery();

            if (results.next()) {
                int count = results.getInt(1);
                if (count > 0) {
                    // put the result in the cache
                    cache.put(key, new Boolean(true));
                    return true;
                }
            }
            // put the result in the cache
            cache.put(key, new Boolean(false));
            throw new LBParameterException("The value supplied for the parameter is invalid.", supportedName,
                    supportedValue);
        } catch (LBParameterException e) {
            throw e;
        } catch (Exception e) {
            throw new UnexpectedInternalError("There was an unexpected error while validating the property", e);
        } finally {
            si.checkInPreparedStatement(checkProperty);
        }
    }

    public static String[] getNativeRelations(String internalCodingScheme, String internalVersionString)
            throws MissingResourceException, UnexpectedInternalError, LBParameterException {
        // first, check the cache
        String key = "defaultRelation:" + internalCodingScheme + ":" + internalVersionString;

        Map cache = ResourceManager.instance().getCache();

        String[] value = (String[]) cache.get(key);

        if (value != null) {
            return value;
        }

        SQLInterface si = null;
        PreparedStatement getRelation = null;

        try {
            si = ResourceManager.instance().getSQLInterface(internalCodingScheme, internalVersionString);

            String relDcName = si.getSQLTableConstants().containerNameOrContainerDC;
            getRelation = si.checkOutPreparedStatement("SELECT " + relDcName + ", " + SQLTableConstants.TBLCOL_ISNATIVE
                    + " FROM " + si.getTableName(SQLTableConstants.RELATION) + " WHERE "
                    + si.getSQLTableConstants().codingSchemeNameOrId + " = ?");

            getRelation.setString(1, internalCodingScheme);

            ResultSet results = getRelation.executeQuery();

            // can't use orderby on the boolean column, because some databases
            // put trues first,
            // while others put falses first. instead, I'll just get them all,
            // and scan for a true.

            List<String> temp = new ArrayList<String>();
            while (results.next()) {
                boolean isNative = DBUtility.getbooleanFromResultSet(results, SQLTableConstants.TBLCOL_ISNATIVE);
                if (isNative)
                    temp.add(results.getString(relDcName));
            }
            results.close();
            // If I get here - none are marked as native. If I found any, just
            // use one.

            if (temp.isEmpty()) {
                // none were found
                throw new LBParameterException(
                        "The requested coding scheme does not have any relations to create a graph");
            } else {
                String[] val = temp.toArray(new String[temp.size()]);
                cache.put(key, val);
                return val;
            }
        } catch (SQLException e) {
            throw new UnexpectedInternalError("There was an unexpected error while getting the default relation", e);
        } finally {
            if (si != null) {
                si.checkInPreparedStatement(getRelation);
            }
        }
    }

    /**
     * Get the urn (registered name) for a code systems internal coding scheme
     * name. This is cached.
     */
    public static String getURNForInternalCodingSchemeName(String internalCodingSchemeName, String internalVersionString)
            throws UnexpectedInternalError, MissingResourceException {
        Map cache = ResourceManager.instance().getCache();
        String key = "codingSchemeToURN:" + internalCodingSchemeName + ":" + internalVersionString;
        String urn = (String) cache.get(key);

        if (urn != null) {
            return urn;
        }

        SQLInterface si = ResourceManager.instance().getSQLInterface(internalCodingSchemeName, internalVersionString);

        PreparedStatement getURNForInternalCodingScheme = null;

        try {
            getURNForInternalCodingScheme = si.checkOutPreparedStatement("Select "
                    + si.getSQLTableConstants().registeredNameOrCSURI + " from "
                    + si.getTableName(SQLTableConstants.CODING_SCHEME) + " Where "
                    + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + " = ?");

            getURNForInternalCodingScheme.setString(1, internalCodingSchemeName);

            ResultSet results = getURNForInternalCodingScheme.executeQuery();

            if (results.next()) {
                String temp = results.getString(si.getSQLTableConstants().registeredNameOrCSURI);
                cache.put(key, temp);
                return temp;
            }
            throw new MissingResourceException("Cannot map the name " + internalCodingSchemeName + " to a urn");
        } catch (SQLException e) {
            throw new UnexpectedInternalError(
                    "There was an unexpected error while mapping the internal coding scheme name to the urn", e);
        } finally {
            si.checkInPreparedStatement(getURNForInternalCodingScheme);
        }

    }

    /**
     * Get a the urn (registered name) for a coding scheme label used in the
     * relationship tables. This is cached.
     */
    public static String getURNForRelationshipCodingSchemeName(String supportedCodingScheme,
            String internalCodingSchemeName, String internalVersionString, boolean throwError)
            throws MissingResourceException, UnexpectedInternalError {
        Map cache = ResourceManager.instance().getCache();
        String key = "supportedCodingSchemeToURN:" + internalCodingSchemeName + ":" + internalVersionString + ":"
                + supportedCodingScheme;
        String urn = (String) cache.get(key);

        if (urn != null) {
            return urn;
        }

        SQLInterface si = ResourceManager.instance().getSQLInterface(internalCodingSchemeName, internalVersionString);

        PreparedStatement getURNForSupportedCodingScheme = null;

        try {
            getURNForSupportedCodingScheme = si.checkOutPreparedStatement("Select "
                    + si.getSQLTableConstants().urnOruri + " from "
                    + si.getTableName(SQLTableConstants.CODING_SCHEME_SUPPORTED_ATTRIBUTES) + " Where "
                    + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + " = ? AND "
                    + SQLTableConstants.TBLCOL_SUPPORTEDATTRIBUTETAG + " = ? and " + SQLTableConstants.TBLCOL_ID
                    + " = ? ");
            getURNForSupportedCodingScheme.setString(1, internalCodingSchemeName);
            getURNForSupportedCodingScheme.setString(2, SQLTableConstants.TBLCOLVAL_SUPPTAG_CODINGSCHEME);
            getURNForSupportedCodingScheme.setString(3, supportedCodingScheme);

            ResultSet results = getURNForSupportedCodingScheme.executeQuery();

            if (results.next()) {
                String temp = results.getString(si.getSQLTableConstants().urnOruri);
                if (temp == null || temp.length() == 0) {
                    if (throwError) {
                        throw new MissingResourceException("Cannot map the name " + supportedCodingScheme
                                + " in the coding scheme " + internalCodingSchemeName
                                + " to a urn.  You are missing the '" + SQLTableConstants.TBLCOL_URN
                                + "' attribute in your 'Supported" + SQLTableConstants.TBLCOLVAL_SUPPTAG_CODINGSCHEME
                                + "' field in this terminology.");

                    } else {
                        return supportedCodingScheme;
                    }
                }
                cache.put(key, temp);
                return temp;
            }
            if (throwError) {
                throw new MissingResourceException("Cannot map the name " + supportedCodingScheme
                        + " in the coding scheme " + internalCodingSchemeName
                        + " to a urn.  You are missing a 'Supported" + SQLTableConstants.TBLCOLVAL_SUPPTAG_CODINGSCHEME
                        + "' field in this terminology.");
            } else {
                return supportedCodingScheme;
            }
        } catch (SQLException e) {
            throw new UnexpectedInternalError(
                    "There was an unexpected error while mapping the supported coding scheme name to the urn", e);
        } finally {
            si.checkInPreparedStatement(getURNForSupportedCodingScheme);
        }
    }

    /**
     * Get a the urn (registered name) for an association label used in the
     * relationship tables. This is cached.
     */
    public static String getURNForAssociationName(String associationName, String internalCodingSchemeName,
            String internalVersionString) throws MissingResourceException, UnexpectedInternalError {
        Map cache = ResourceManager.instance().getCache();
        String key = "AssociationToURN:" + internalCodingSchemeName + ":" + internalVersionString + ":"
                + associationName;
        String urn = (String) cache.get(key);

        if (urn != null) {
            return urn;
        }

        SQLInterface si = ResourceManager.instance().getSQLInterface(internalCodingSchemeName, internalVersionString);

        PreparedStatement getURNForSupportedCodingScheme = null;

        try {
            getURNForSupportedCodingScheme = si.checkOutPreparedStatement("Select "
                    + si.getSQLTableConstants().urnOruri + " from "
                    + si.getTableName(SQLTableConstants.CODING_SCHEME_SUPPORTED_ATTRIBUTES) + " Where "
                    + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + " = ? AND "
                    + SQLTableConstants.TBLCOL_SUPPORTEDATTRIBUTETAG + " = ? and " + SQLTableConstants.TBLCOL_ID
                    + " = ? ");
            getURNForSupportedCodingScheme.setString(1, internalCodingSchemeName);
            getURNForSupportedCodingScheme.setString(2, SQLTableConstants.TBLCOLVAL_SUPPTAG_ASSOCIATION);
            getURNForSupportedCodingScheme.setString(3, associationName);

            ResultSet results = getURNForSupportedCodingScheme.executeQuery();

            if (results.next()) {
                String temp = results.getString(si.getSQLTableConstants().urnOruri);
                if (temp != null) {
                    // sometimes this looks like this:
                    // urn:oid:2.16.840.1.113883.3.26.1.1:hasSubtype
                    // want to strip off the last colon and everything after it.
                    // also the last # and
                    // everything following it.
                    int pos = temp.lastIndexOf(':');
                    if (pos > 7) {
                        temp = temp.substring(0, pos);
                    }
                    pos = temp.lastIndexOf('#');
                    if (pos > 7) {
                        temp = temp.substring(0, pos);
                    }
                }
                cache.put(key, temp);
                return temp;
            }
            throw new MissingResourceException("Cannot map the association " + associationName
                    + " in the coding scheme " + internalCodingSchemeName + " to a urn");
        } catch (SQLException e) {
            throw new UnexpectedInternalError(
                    "There was an unexpected error while mapping the supported coding scheme name to the urn", e);
        } finally {
            si.checkInPreparedStatement(getURNForSupportedCodingScheme);
        }
    }

    /**
     * Get a the urn (registered name) for a coding scheme label used in the
     * relationship tables. This is cached.
     */
    public static String getRelationshipCodingSchemeNameForURN(String urn, String internalCodingSchemeName,
            String internalVersionString) throws MissingResourceException, UnexpectedInternalError {
        Map cache = ResourceManager.instance().getCache();
        String key = "URNToSupportedCodingScheme:" + internalCodingSchemeName + ":" + internalVersionString + ":" + urn;
        String supportedCodingScheme = (String) cache.get(key);

        if (supportedCodingScheme != null) {
            return supportedCodingScheme;
        }

        SQLInterface si = ResourceManager.instance().getSQLInterface(internalCodingSchemeName, internalVersionString);

        PreparedStatement getSupportedCodingSchemeForURN = null;

        try {
            getSupportedCodingSchemeForURN = si.checkOutPreparedStatement("Select " + SQLTableConstants.TBLCOL_ID
                    + " from " + si.getTableName(SQLTableConstants.CODING_SCHEME_SUPPORTED_ATTRIBUTES) + " Where "
                    + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + " = ? AND "
                    + SQLTableConstants.TBLCOL_SUPPORTEDATTRIBUTETAG + " = ? and " + si.getSQLTableConstants().urnOruri
                    + " = ? ");

            getSupportedCodingSchemeForURN.setString(1, internalCodingSchemeName);
            getSupportedCodingSchemeForURN.setString(2, SQLTableConstants.TBLCOLVAL_SUPPTAG_CODINGSCHEME);
            getSupportedCodingSchemeForURN.setString(3, urn);

            ResultSet results = getSupportedCodingSchemeForURN.executeQuery();

            if (results.next()) {
                String temp = results.getString(SQLTableConstants.TBLCOL_ID);
                cache.put(key, temp);
                return temp;
            }
            throw new MissingResourceException("Cannot map the urn " + urn + " in the coding scheme "
                    + internalCodingSchemeName + " to a supportedCodingScheme");
        } catch (SQLException e) {
            throw new UnexpectedInternalError(
                    "There was an unexpected error while mapping the supported coding scheme name to the urn", e);
        } finally {
            si.checkInPreparedStatement(getSupportedCodingSchemeForURN);
        }
    }

    /**
     * This method is for checking if an association defined as
     * <codeSystem>:<association> is valid in a particular code system. This is
     * cached.
     */
    private static boolean isAssociationValidForCodeSystem(String internalCodeSystemName, String internalVersionString,
            String association, String associationURN) throws MissingResourceException, UnexpectedInternalError {
        Map cache = ResourceManager.instance().getCache();
        String key = "validAssociationForCodeSystem:" + internalCodeSystemName + ":" + internalVersionString + ":"
                + association + ":" + associationURN;
        Boolean valid = (Boolean) cache.get(key);

        if (valid != null && valid == true) {
            return valid.booleanValue();
        }

        SQLInterface si = ResourceManager.instance().getSQLInterface(internalCodeSystemName, internalVersionString);

        PreparedStatement validateAssociationInCodeSystem = null;

        String supportedAssociationURN = getURNForAssociationName(association, internalCodeSystemName,
                internalVersionString);
        try {
            boolean includeParam4 = supportedAssociationURN != null && supportedAssociationURN.equals(associationURN);
            validateAssociationInCodeSystem = si.checkOutPreparedStatement("Select "
                    + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + " from "
                    + si.getTableName(SQLTableConstants.CODING_SCHEME_SUPPORTED_ATTRIBUTES) + " Where "
                    + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + " = ? AND "
                    + SQLTableConstants.TBLCOL_SUPPORTEDATTRIBUTETAG + " = ? AND " + SQLTableConstants.TBLCOL_ID
                    + " = ? " + (includeParam4 ? " AND " + si.getSQLTableConstants().urnOruri + " LIKE ?" : ""));
            validateAssociationInCodeSystem.setString(1, internalCodeSystemName);
            validateAssociationInCodeSystem.setString(2, SQLTableConstants.TBLCOLVAL_SUPPTAG_ASSOCIATION);
            validateAssociationInCodeSystem.setString(3, association);
            if (includeParam4)
                validateAssociationInCodeSystem.setString(4, associationURN != null ? associationURN + "%"
                        : supportedAssociationURN + "%");
            ResultSet results = validateAssociationInCodeSystem.executeQuery();

            if (results.next()) {
                cache.put(key, new Boolean(true));
                return true;
            } else {
                // We have a confusing 'feature' in that when we put in a
                // "hasSubtype" relationship, we assign
                // it the oid of 'urn:oid:1.3.6.1.4.1.2114.108.1.8.1'. Nobody is
                // going to know that... if they
                // pass in any oid for an association name, its likely to be
                // that of the code system they are
                // currently in. So, I'll check that for them.

                try {
                    if (association.equals(SQLTableConstants.TBLCOLVAL_HASSUBTYPE_ASSOCIATION)
                            && associationURN.equals(ResourceManager.instance().getURNForInternalCodingSchemeName(
                                    internalCodeSystemName))) {
                        cache.put(key, new Boolean(true));
                        return true;
                    }
                } catch (LBParameterException e) {
                    // this can't happen.
                }
                cache.put(key, new Boolean(false));
                return false;
            }
        } catch (Exception e) {
            return true;
        } finally {
            si.checkInPreparedStatement(validateAssociationInCodeSystem);
        }

    }

    public static GAssociationInfo getAssociationInfo(String internalCodeSystemName, String internalVersionString,
            String associationName, String relationName) throws LBParameterException, UnexpectedInternalError,
            MissingResourceException {
        SQLInterface si = ResourceManager.instance().getSQLInterface(internalCodeSystemName, internalVersionString);

        PreparedStatement getAssociationInfo = null;

        try {
            String csName = si.getSQLTableConstants().codingSchemeNameOrId;
            String rdcName = si.getSQLTableConstants().containerNameOrContainerDC;
            String assnId = si.getSQLTableConstants().entityCodeOrId;

            getAssociationInfo = si.modifyAndCheckOutPreparedStatement("Select " + SQLTableConstants.TBLCOL_FORWARDNAME
                    + ", " + SQLTableConstants.TBLCOL_REVERSENAME + ", " + SQLTableConstants.TBLCOL_ISNAVIGABLE + ", "
                    + si.getSQLTableConstants().urnOruri + " from " + si.getTableName(SQLTableConstants.ASSOCIATION)
                    + " {AS} a, " + si.getTableName(SQLTableConstants.CODING_SCHEME_SUPPORTED_ATTRIBUTES) + " {AS} csa"
                    + "  Where a." + csName + " = ? AND " + "a." + rdcName + " = ? AND " + "a." + assnId + " = ? "
                    + " and " + "a." + csName + " = csa." + SQLTableConstants.TBLCOL_CODINGSCHEMENAME + " and " + "a."
                    + assnId + " = csa." + SQLTableConstants.TBLCOL_ID + " and " + "csa."
                    + SQLTableConstants.TBLCOL_SUPPORTEDATTRIBUTETAG + " = ?");

            getAssociationInfo.setString(1, internalCodeSystemName);
            getAssociationInfo.setString(2, relationName);
            getAssociationInfo.setString(3, associationName);
            getAssociationInfo.setString(4, SQLTableConstants.TBLCOLVAL_SUPPTAG_ASSOCIATION);

            ResultSet results = getAssociationInfo.executeQuery();

            if (results.next()) {
                String urn = results.getString(si.getSQLTableConstants().urnOruri);
                String forwardName = results.getString(SQLTableConstants.TBLCOL_FORWARDNAME);
                String reverseName = results.getString(SQLTableConstants.TBLCOL_REVERSENAME);
                Boolean isNavigable = DBUtility.getBooleanFromResultSet(results, SQLTableConstants.TBLCOL_ISNAVIGABLE);
                return new GAssociationInfo(internalCodeSystemName, urn, associationName, StringUtils
                        .isBlank(forwardName) ? null : forwardName, StringUtils.isBlank(reverseName) ? null
                        : reverseName, isNavigable);
            } else {
                throw new LBParameterException("The association '" + associationName + "' could not be found in "
                        + internalCodeSystemName + ", '" + relationName);
            }
        } catch (SQLException e) {
            throw new UnexpectedInternalError("There was an unexpected error while getting the association info for '"
                    + associationName + "', '" + internalCodeSystemName + "', '" + relationName, e);
        } finally {
            si.checkInPreparedStatement(getAssociationInfo);
        }
    }

    /*
     * From here down is methods that are used by the CodedNodeGraph
     * implementations
     */

    private static void setGraphQuestionQueryParameters(GraphQuestionQuery query, PreparedStatement statement)
            throws SQLException {
        for (int i = 0; i < query.parameters_.size(); i++) {
            statement.setString(i + 1, query.parameters_.get(i));
        }
    }

    private static GraphQuestionQuery buildGraphQuestionQuery(ArrayList<Operation> operations,
            String internalCodeSystemName, String internalVersionString, String containerName, boolean transitiveLookup)
            throws LBInvocationException, LBParameterException, MissingResourceException, UnexpectedInternalError {
        GraphQuestionQuery query = new GraphQuestionQuery();
        query.whereClause_ = new StringBuffer();
        query.parameters_ = new ArrayList<String>();

        // table prefix is null because we just want to get a SQLTableConstant
        // object with version
        // to see if we can find new column names introduced in 2008

        SQLInterface si = ResourceManager.instance().getSQLInterface(internalCodeSystemName, internalVersionString);
        SQLTableConstants stc = si.getSQLTableConstants();
        String associationColumnName = stc.entityCodeOrAssociationId;

        if (containerName== null) {
            query.whereClause_.append("ca." + stc.codingSchemeNameOrId + " = ? ");
               query.parameters_.add(internalCodeSystemName);
        } else {
           query.whereClause_.append("ca." + stc.codingSchemeNameOrId + " = ? AND " + stc.containerNameOrContainerDC
                + " = ?");
           query.parameters_.add(internalCodeSystemName);
           query.parameters_.add(containerName);
        }

        for (int operationIndex = 0; operationIndex < operations.size(); operationIndex++) {
            Operation current = operations.get(operationIndex);
            if (current instanceof RestrictToAssociations) {
                RestrictToAssociations r = (RestrictToAssociations) current;
                NameAndValueList assocations = r.getAssociations();
                NameAndValueList assocationQualifiers = r.getAssociationQualifiers();
                if (assocations != null && assocations.getNameAndValueCount() > 0) {
                    query.whereClause_.append(" AND (");
                    for (int j = 0; j < assocations.getNameAndValueCount(); j++) {
                        query.whereClause_.append(" ca." + associationColumnName + " = ? OR ");
                        String codingScheme = assocations.getNameAndValue(j).getContent();

                        String association = assocations.getNameAndValue(j).getName();
                        if (codingScheme != null && codingScheme.length() > 0) {
                            // make sure we have the right internal name
                            codingScheme = ResourceManager.instance().getRelationshipCodingSchemeNameForURNorName(
                                    codingScheme, internalCodeSystemName, false);
                            // now get the URN
                            codingScheme = SQLImplementedMethods.getURNForRelationshipCodingSchemeName(codingScheme,
                                    internalCodeSystemName, internalVersionString, false);
                            // codingScheme =
                            // SQLImplementedMethods.getURNForAssociationName(association,
                            // codingScheme, internalVersionString);
                            // if(codingScheme == null)
                            // if they supplied a coding scheme - then in order
                            // for us to use this
                            // association, their needs to be a mapping in the
                            // supportedAssociations
                            // from this association to the supplied
                            // codingScheme (URN) within the code
                            // system and version that we are working within.
                            if (!isAssociationValidForCodeSystem(internalCodeSystemName, internalVersionString,
                                    association, codingScheme)) {
                                association = "--ASSOCIATION-NOT-SUPPORTED-IN-CODE-SYSTEM--";
                            }
                        }
                        query.parameters_.add(association);
                    }

                    // trim the 'OR ', put on the trailing paren
                    query.whereClause_.replace(query.whereClause_.length() - 3, query.whereClause_.length(), ")");
                } else {
                    // they asked us to restrict to 0 associations - so add a
                    // clause to the query that
                    // will give 0 results.
                    query.whereClause_.append(" AND ca." + associationColumnName + " = ?");
                    query.parameters_.add("--INVALID-ASSOCIATION--");
                }
                if (!transitiveLookup && assocationQualifiers != null
                        && assocationQualifiers.getNameAndValueCount() > 0) {
                    // boolean is2006Model =
                    // SystemResourceService.instance().getSQLInterface(internalCodeSystemName,
                    // internalVersionString).supports2006ModelOnly();
                    query.qualiferTableRequired_ = true;
                    query.whereClause_.append(" AND (");
                    for (int j = 0; j < assocationQualifiers.getNameAndValueCount(); j++) {
                        NameAndValue nv = assocationQualifiers.getNameAndValue(j);
                        query.parameters_.add(nv.getName());
                        StringBuffer sb = query.whereClause_;
                        sb.append("(caa." + SQLTableConstants.TBLCOL_QUALIFIERNAME + " = ?");
                        if (StringUtils.isNotBlank(nv.getContent())) {
                            sb.append(" AND caa." + SQLTableConstants.TBLCOL_QUALIFIERVALUE + " = ?");
                            query.parameters_.add(nv.getContent());
                        }
                        sb.append(") OR ");
                    }

                    // trim the 'OR ', put on the trailing paren
                    query.whereClause_.replace(query.whereClause_.length() - 3, query.whereClause_.length(), ")");
                }
            } else if (current instanceof RestrictToDirectionalNames) {
                RestrictToDirectionalNames r = (RestrictToDirectionalNames) current;
                NameAndValueList vals = r.getDirectionalNames();

                if (vals != null && vals.getNameAndValueCount() > 0) {
                    query.whereClause_.append(" AND (");
                    for (int j = 0; j < vals.getNameAndValueCount(); j++) {

                        query.whereClause_.append(" ca." + associationColumnName + " in " + "(select "
                                + stc.associationNameOrId + " from " + si.getTableName(SQLTableConstants.ASSOCIATION)
                                + " where " + SQLTableConstants.TBLCOL_FORWARDNAME + " = ? OR "
                                + SQLTableConstants.TBLCOL_REVERSENAME + " = ? ) OR");

                        String codingScheme = vals.getNameAndValue(j).getContent();

                        String directionalName = vals.getNameAndValue(j).getName();
                        if (codingScheme != null && codingScheme.length() > 0) {
                            // make sure we have the right internal name
                            codingScheme = ResourceManager.instance().getRelationshipCodingSchemeNameForURNorName(
                                    codingScheme, internalCodeSystemName, false);
                            // now get the URN
                            codingScheme = SQLImplementedMethods.getURNForRelationshipCodingSchemeName(codingScheme,
                                    internalCodeSystemName, internalVersionString, false);

                        }
                        query.parameters_.add(directionalName);
                        query.parameters_.add(directionalName);
                    }

                    // trim the 'OR ', put on the trailing paren
                    query.whereClause_.replace(query.whereClause_.length() - 3, query.whereClause_.length(), ")");
                } else {
                    // they asked us to restrict to 0 associations - so add a
                    // clause to the query that
                    // will give 0 results.
                    query.whereClause_.append(" AND ca." + associationColumnName + " in " + "( select "
                            + stc.associationNameOrId + "  from " + si.getTableName(SQLTableConstants.ASSOCIATION)
                            + "  where " + SQLTableConstants.TBLCOL_FORWARDNAME + " = ? OR "
                            + SQLTableConstants.TBLCOL_REVERSENAME + " = ? ) ");
                    query.parameters_.add("--INVALID-DIRECTIONALNAME--");
                    query.parameters_.add("--INVALID-DIRECTIONALNAME--");
                }

                vals = r.getAssociationQualifiers();
                if (!transitiveLookup && vals != null && vals.getNameAndValueCount() > 0) {
                    // boolean is2006Model =
                    // SystemResourceService.instance().getSQLInterface(internalCodeSystemName,
                    // internalVersionString).supports2006ModelOnly();
                    query.qualiferTableRequired_ = true;
                    query.whereClause_.append(" AND (");
                    for (int j = 0; j < vals.getNameAndValueCount(); j++) {
                        NameAndValue nv = vals.getNameAndValue(j);
                        query.parameters_.add(nv.getName());
                        StringBuffer sb = query.whereClause_;
                        sb.append("(caa." + SQLTableConstants.TBLCOL_QUALIFIERNAME + " = ?");
                        if (StringUtils.isNotBlank(nv.getContent())) {
                            sb.append(" AND caa." + SQLTableConstants.TBLCOL_QUALIFIERVALUE + " = ?");
                            query.parameters_.add(nv.getContent());
                        }
                        sb.append(") OR ");
                    }

                    // trim the 'OR ', put on the trailing paren
                    query.whereClause_.replace(query.whereClause_.length() - 3, query.whereClause_.length(), ")");
                }
            }

            else if (current instanceof RestrictToCodes) {
                RestrictToCodes r = (RestrictToCodes) current;
                ConceptReference[] codes = r.getCodes();
                if (codes != null && codes.length > 0) {       
                    query.whereClause_.append(" AND ((");          
                    addNamespaceAndIdRestrictionToQuery(query, 
                            "ca." + stc.sourceCSIdOrEntityCodeNS,
                            "ca." + stc.sourceEntityCodeOrId,
                            codes,
                            internalCodeSystemName, internalVersionString,
                            stc.supports2009Model());
                      
                    // do it again for the targets.
                    query.whereClause_.append(" OR (");
                    addNamespaceAndIdRestrictionToQuery(query, 
                            "ca." + stc.targetCSIdOrEntityCodeNS,
                            "ca." + stc.targetEntityCodeOrId,
                            codes,
                            internalCodeSystemName, internalVersionString, 
                            stc.supports2009Model());                  
                    query.whereClause_.append(")");
                } else {
                    // they asked us to restrict to 0 concept codes - so add a
                    // clause to the query that
                    // will give 0 results.
                    query.whereClause_.append(" AND ca." + stc.sourceCSIdOrEntityCodeNS + " = ?");
                    query.parameters_.add("--INVALID-CODING--SCHEME--NAME--");
                }

            } else if (current instanceof RestrictToCodeSystem) {
                RestrictToCodeSystem r = (RestrictToCodeSystem) current;
                String codeSystem = ResourceManager.instance().getRelationshipCodingSchemeNameForURNorName(
                        r.getSupportedCodeSystemOrURN(), internalCodeSystemName, false);

                query.whereClause_.append(" AND (" + stc.sourceCSIdOrEntityCodeNS + " = ? OR "
                        + stc.targetCSIdOrEntityCodeNS + " = ?)");
                query.parameters_.add(codeSystem);
                query.parameters_.add(codeSystem);

            } else if (current instanceof RestrictToSourceCodes) {
                RestrictToSourceCodes r = (RestrictToSourceCodes) current;
                ConceptReference[] codes = r.getCodes();
                if (codes != null && codes.length > 0) {
                    // make sure the source codes are in the set provided.
                    query.whereClause_.append(" AND (");   
                    addNamespaceAndIdRestrictionToQuery(query, 
                            "ca." + stc.sourceCSIdOrEntityCodeNS,
                            "ca." + stc.sourceEntityCodeOrId,
                            codes,
                            internalCodeSystemName, internalVersionString,
                            stc.supports2009Model());
                } else {
                    // they asked us to restrict to 0 concept codes - so add a
                    // clause to the query that
                    // will give 0 results.
                    query.whereClause_.append(" AND ca." + stc.sourceCSIdOrEntityCodeNS + " = ?");
                    query.parameters_.add("--INVALID-CODING--SCHEME--NAME--");
                }
            } else if (current instanceof RestrictToSourceCodeSystem) {
                RestrictToSourceCodeSystem r = (RestrictToSourceCodeSystem) current;
                String codeSystem = ResourceManager.instance().getRelationshipCodingSchemeNameForURNorName(
                        r.getSupportedCodeSystemOrURN(), internalCodeSystemName, false);

                query.whereClause_.append(" AND " + stc.sourceCSIdOrEntityCodeNS + " = ? ");
                query.parameters_.add(codeSystem);
            } else if (current instanceof RestrictToTargetCodes) {
                RestrictToTargetCodes r = (RestrictToTargetCodes) current;
                
                ConceptReference[] codes = r.getCodes();
                if (codes != null && codes.length > 0) {
					query.whereClause_.append(" AND (");
                    addNamespaceAndIdRestrictionToQuery(query, 
                            "ca." + stc.targetCSIdOrEntityCodeNS,
                            "ca." + stc.targetEntityCodeOrId,
                            codes,
                            internalCodeSystemName, internalVersionString,
                            stc.supports2009Model());     
                } else {
                    // they asked us to restrict to 0 concept codes - so add a
                    // clause to the query that
                    // will give 0 results.
                    query.whereClause_.append(" AND ca." + stc.sourceCSIdOrEntityCodeNS + " = ?");
                    query.parameters_.add("--INVALID-CODING--SCHEME--NAME--");
                }
            } else if (current instanceof RestrictToTargetCodeSystem) {
                RestrictToTargetCodeSystem r = (RestrictToTargetCodeSystem) current;
                String codeSystem = ResourceManager.instance().getRelationshipCodingSchemeNameForURNorName(
                        r.getSupportedCodeSystemOrURN(), internalCodeSystemName, false);

                query.whereClause_.append(" AND " + stc.targetCSIdOrEntityCodeNS + " = ? ");
                query.parameters_.add(codeSystem);
            } else if (current instanceof Union || current instanceof Intersection) {
                // these should be at the end of all of the restrictions, so I'm
                // done.
                break;
            } else {
                getLogger().error("There is an illegal object in the pending operations list");
            }
        }
        return query;
    }
    
    /**
     * Helper method to add a EntityCodeId and Namespace restriction to a GraphQuestionQuery.
     * If not provided in the ConceptReference, this method will search for the Namespace 
     * in this order:
     * 
     * 1. Use the Namespace passed in by the ConceptReference.
     * 2. Check if the ConceptReference is a ResolvedConceptReference, and get the Namespace from there.
     * 3. Try to resolve the ConceptReference to a ResolvedConceptReference, and get the Namespace from there.
     * 4. If all else fails, set the namespace to the Default Namespace of the Coding Scheme
     * 
     * @param query GraphQuestionQuery to add the restriction to
     * @param csIdOrEntityCodeNSTable Name of the csIdOrEntityCodeNSTable table.
     * @param entityCodeOrIdTable Name of the entityCodeOrIdTable
     * @param codes ConceptReference Codes to process.
     * @param internalCodeSystemName The CodingScheme this query is from.
     * @param internalVersionString The CodingScheme version.
     * @param supports2009Model If true this will use Namespaces from the ConceptReferences to build the query.
     *                          If false, just use the CodingSchemeName.
     * @throws MissingResourceException
     */
    private static void addNamespaceAndIdRestrictionToQuery(GraphQuestionQuery query, String csIdOrEntityCodeNSTable,
            String entityCodeOrIdTable,  ConceptReference[] codes, 
            String internalCodeSystemName, String internalVersionString,
            boolean supports2009Model)
    throws MissingResourceException {  
        if(supports2009Model){             
            for (int i = 0; i < codes.length; i++) { 
                ConceptReference cr = codes[i];
                String namespace = null;
                //First, determine if the ConceptReference is a Root or Tail node. If it is,
                //we know we don't have to go looking for the namespace because
                //it will be the default
                if(!isConceptReferenceRootOrTail(cr)){  
                    //First check to see if the ConceptReference contains a namespace.
                    namespace = cr.getCodeNamespace();
                    getLogger().debug("Retrieving Namespace from the ConceptReference: " + namespace);
                }
                
                //namespace is still null - check to see if this is a ResolvedConceptReference,
                //we may be able to pull it from the Entity
                if(namespace == null){
                    if(cr instanceof ResolvedConceptReference){
                        ResolvedConceptReference ref = (ResolvedConceptReference)cr;
                        Entity entity = ref.getEntity();
                        if(entity != null){
                            namespace = entity.getEntityCodeNamespace();
                        }
                    }
                }
      
                if(namespace == null){
                    query.whereClause_.append(" (" + entityCodeOrIdTable + " = ?) OR "); 
                    query.parameters_.add(codes[i].getConceptCode()); 
                } else {
                    query.whereClause_.append(" (" + csIdOrEntityCodeNSTable + " = ? AND "
                            + entityCodeOrIdTable + " = ?) OR "); 
                    query.parameters_.add(namespace);
                    query.parameters_.add(codes[i].getConceptCode());       
                }              
            }
        } else {
            for (int i = 0; i < codes.length; i++) {
                query.whereClause_.append(" (" + csIdOrEntityCodeNSTable + " = ? AND "
                        + entityCodeOrIdTable + " = ?) OR ");
                if (codes[i] instanceof KnownConceptReference) {
                    // no translation required
                    query.parameters_.add(codes[i].getCodingSchemeName());
                } else {
                    query.parameters_.add(ResourceManager.instance()
                            .getRelationshipCodingSchemeNameForURNorName(codes[i].getCodingSchemeName(),
                                    internalCodeSystemName, false));
                }
                query.parameters_.add(codes[i].getConceptCode());
            }
        }
        // trim the 'OR ', put on the trailing paren
        query.whereClause_.replace(query.whereClause_.length() - 3, query.whereClause_.length(), ")");    
    }
      
    private static String getGraphQuestionQueryFromPart(GraphQuestionQuery query, SQLInterface si,
            boolean transitiveLookup) {

        StringBuffer queryString = new StringBuffer();
        if (transitiveLookup) {
            queryString.append(" FROM " + si.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY_TRANSITIVE)
                    + " {AS} ca");
        } else {
            queryString.append(" FROM " + si.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_ENTITY) + " {AS} ca");
        }
        if (query.qualiferTableRequired_) {
            queryString.append(
            " left join " + si.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_E_QUALS) + " {AS} caa");
            queryString.append(
            " on ca." + SQLTableConstants.TBLCOL_MULTIATTRIBUTESKEY);
            queryString.append(
            " = ");
            queryString.append(
            " caa." + SQLTableConstants.TBLCOL_MULTIATTRIBUTESKEY);
        }
       
        return queryString.toString();
    }
    
    private static String getGraphQuestionQueryWherePart(GraphQuestionQuery query, SQLInterface si) {
        StringBuffer whereString = new StringBuffer();
        
        whereString.append(" WHERE ");
        whereString.append(query.whereClause_);
        
        return whereString.toString();
    }

    public static Boolean isCodeInGraph(ConceptReference code, ArrayList<Operation> operations,
            String internalCodingSchemeName, String internalVersionString, String relationName) throws Exception {
        SQLInterface si = ResourceManager.instance().getSQLInterface(internalCodingSchemeName, internalVersionString);
        PreparedStatement isCodeInSet = null;
        PreparedStatement isCodeInSetT = null;

        try {
            RestrictToCodes r = new RestrictToCodes(code);

            ArrayList<Operation> modifiedOps = new ArrayList<Operation>();
            modifiedOps.add(r);
            modifiedOps.addAll(operations);

            GraphQuestionQuery query = buildGraphQuestionQuery(modifiedOps, internalCodingSchemeName,
                    internalVersionString, relationName, false);

            isCodeInSet = si.modifyAndCheckOutPreparedStatement("SELECT "
                    + si.getSQLTableConstants().sourceEntityCodeOrId + " "
                    + getGraphQuestionQueryFromPart(query, si, false)
                    + getGraphQuestionQueryWherePart(query, si));
            isCodeInSet.setMaxRows(1);

            setGraphQuestionQueryParameters(query, isCodeInSet);

            ResultSet results = isCodeInSet.executeQuery();

            // true if there is a result.
            boolean result = results.next();
            results.close();
            si.checkInPreparedStatement(isCodeInSet);

            for (int i = 0; i < operations.size(); i++) {
                Operation current = operations.get(i);
                if (!result && current instanceof Union) {
                    // check the unioned graph.
                    Union union = (Union) current;
                    Boolean bool = union.getGraph().isCodeInGraph(code);

                    // if the unioned graph says yes, we say yes.
                    if (bool.booleanValue()) {
                        result = true;
                    }
                } else if (result && current instanceof Intersection) {
                    // only keep the concept references if they are in both
                    // graphs. No need to process
                    // if we have already decided no.
                    Intersection intersection = (Intersection) current;
                    Boolean bool = intersection.getGraph().isCodeInGraph(code);

                    // if the intersected graph says no, we say no.
                    if (!bool.booleanValue()) {
                        result = false;
                    }
                }
            }

            return new Boolean(result);
        } catch (Exception e) {
            throw e;
        } finally {
            si.checkInPreparedStatement(isCodeInSet);
            si.checkInPreparedStatement(isCodeInSetT);
        }

    }

    public static Boolean areCodesRelated(NameAndValue association, ConceptReference sourceCode,
            ConceptReference targetCode, boolean directOnly, ArrayList<Operation> operations,
            String internalCodingSchemeName, String internalVersionString, String relationName) throws Exception {
        SQLInterface si = ResourceManager.instance().getSQLInterface(internalCodingSchemeName, internalVersionString);
        PreparedStatement areCodesRelated = null;
        PreparedStatement areCodesRelatedT = null;

        try {
            ArrayList<Operation> modifiedOps = new ArrayList<Operation>();
            modifiedOps.addAll(operations);

            RestrictToSourceCodes r1 = new RestrictToSourceCodes(sourceCode);
            modifiedOps.add(r1);

            RestrictToTargetCodes r2 = new RestrictToTargetCodes(targetCode);
            modifiedOps.add(r2);

            NameAndValueList associations = new NameAndValueList();
            associations.addNameAndValue(association);
            RestrictToAssociations r3 = new RestrictToAssociations(associations, null);
            modifiedOps.add(r3);

            GraphQuestionQuery query = buildGraphQuestionQuery(modifiedOps, internalCodingSchemeName,
                    internalVersionString, relationName, false);

            areCodesRelated = si.modifyAndCheckOutPreparedStatement("SELECT "
                    + si.getSQLTableConstants().sourceEntityCodeOrId + " "
                    + getGraphQuestionQueryFromPart(query, si, false) + " "
                    + getGraphQuestionQueryWherePart(query, si));
            areCodesRelated.setMaxRows(1);

            setGraphQuestionQueryParameters(query, areCodesRelated);

            ResultSet results = areCodesRelated.executeQuery();

            boolean related = results.next();
            results.close();
            si.checkInPreparedStatement(areCodesRelated);

            // check the transitive table
            if (!related && !directOnly) {
                // I don't need to check if the association is defined as
                // transitive, because only
                // transitive associations have entries in the transitive table.
                query = buildGraphQuestionQuery(modifiedOps, internalCodingSchemeName, internalVersionString,
                        relationName, true);

                areCodesRelatedT = si.modifyAndCheckOutPreparedStatement("SELECT "
                        + si.getSQLTableConstants().sourceEntityCodeOrId + " "
                        + getGraphQuestionQueryFromPart(query, si, true) + " "
                        + getGraphQuestionQueryWherePart(query, si));
                areCodesRelatedT.setMaxRows(1);

                setGraphQuestionQueryParameters(query, areCodesRelatedT);

                results = areCodesRelatedT.executeQuery();

                related = results.next();
                results.close();
                si.checkInPreparedStatement(areCodesRelatedT);

            }

            for (int i = 0; i < operations.size(); i++) {
                Operation current = operations.get(i);
                if (!related && current instanceof Union) {
                    // check the unioned graph.
                    Union union = (Union) current;
                    Boolean bool = union.getGraph().areCodesRelated(association, sourceCode, targetCode, directOnly);

                    // if the unioned graph says yes, we say yes.
                    if (bool.booleanValue()) {
                        related = true;
                    }
                } else if (related && current instanceof Intersection) {
                    // only keep the concept references if they are in both
                    // graphs. No need to process
                    // if we have already decided no.
                    Intersection intersection = (Intersection) current;
                    Boolean bool = intersection.getGraph().areCodesRelated(association, sourceCode, targetCode,
                            directOnly);

                    // if the intersected graph says no, we say no.
                    if (!bool.booleanValue()) {
                        related = false;
                    }
                }
            }

            return new Boolean(related);
        } catch (MissingResourceException e) {
            throw new LBParameterException("Either the source or the target code could not be properly resolved");
        } catch (Exception e) {
            throw e;
        } finally {
            si.checkInPreparedStatement(areCodesRelated);
            si.checkInPreparedStatement(areCodesRelatedT);
        }

    }

    public static List<String> listCodeRelationships(ConceptReference sourceCode, ConceptReference targetCode,
            boolean directOnly, ArrayList<Operation> operations, String internalCodingSchemeName,
            String internalVersionString, String relationName) throws Exception {
        SQLInterface si = ResourceManager.instance().getSQLInterface(internalCodingSchemeName, internalVersionString);
        PreparedStatement listCodeRelationships = null;
        PreparedStatement listCodeRelationshipsT = null;

        try {
            ArrayList<Operation> modifiedOps = new ArrayList<Operation>();

            RestrictToSourceCodes r1 = new RestrictToSourceCodes(sourceCode);
            modifiedOps.add(r1);

            RestrictToTargetCodes r2 = new RestrictToTargetCodes(targetCode);
            modifiedOps.add(r2);

            modifiedOps.addAll(operations);

            GraphQuestionQuery query = buildGraphQuestionQuery(modifiedOps, internalCodingSchemeName,
                    internalVersionString, relationName, false);

            listCodeRelationships = si.modifyAndCheckOutPreparedStatement("SELECT DISTINCT "
                    + si.getSQLTableConstants().entityCodeOrAssociationId + " "
                    + getGraphQuestionQueryFromPart(query, si, false)
                    + getGraphQuestionQueryWherePart(query, si));

            setGraphQuestionQueryParameters(query, listCodeRelationships);

            ResultSet results = listCodeRelationships.executeQuery();

            // hashtable to remove duplicates
            Set<String> associations = new HashSet<String>();

            while (results.next()) {
                String associationName = results.getString(si.getSQLTableConstants().entityCodeOrAssociationId);
               
                associations.add(associationName);
            }

            results.close();
            si.checkInPreparedStatement(listCodeRelationships);

            // transitive lookup.
            if (!directOnly) {
                query = buildGraphQuestionQuery(modifiedOps, internalCodingSchemeName, internalVersionString,
                        relationName, true);

                listCodeRelationshipsT = si.modifyAndCheckOutPreparedStatement("SELECT DISTINCT "
                        + si.getSQLTableConstants().entityCodeOrAssociationId + " "
                        + getGraphQuestionQueryFromPart(query, si, true)
                        + getGraphQuestionQueryWherePart(query, si));

                setGraphQuestionQueryParameters(query, listCodeRelationshipsT);

                results = listCodeRelationshipsT.executeQuery();

                while (results.next()) {
                    String associationName = results.getString(si.getSQLTableConstants().entityCodeOrAssociationId);
                   
                    associations.add(associationName);
                }
                results.close();
                si.checkInPreparedStatement(listCodeRelationshipsT);
            }

            for (int i = 0; i < operations.size(); i++) {

                Operation current = operations.get(i);
                if (current instanceof Union) {
                    // add everything from the graphed we have been unioned to.
                    Union union = (Union) current;
                    List<String> list = union.getGraph().listCodeRelationships(sourceCode, targetCode,
                            directOnly);
                    for (String association : list) {
                        associations.add(association);
                    }
                } else if (current instanceof Intersection) {
                    // only keep the concept references if they are in both
                    // graphs.
                    Intersection intersection = (Intersection) current;
                    List<String> list = intersection.getGraph().listCodeRelationships(sourceCode, targetCode,
                            directOnly);
                    HashSet<String> graph2 = new HashSet<String>();
                    for (String association : list) {
                        graph2.add(association);
                    }

                    // now I have all of graph 2 keyed into a hashtable - remove
                    // from one as necessary.
                   
                    for (String e : associations) {
                        if (!graph2.contains(e)) {
                            associations.remove(e);
                        }
                    }
                }
            }
      
            return new ArrayList<String>(associations);
        } catch (MissingResourceException e) {
            throw new LBParameterException("Either the source or the target code could not be properly resolved");
        } catch (Exception e) {
            throw e;
        } finally {
            si.checkInPreparedStatement(listCodeRelationships);
            si.checkInPreparedStatement(listCodeRelationshipsT);
        }

    }

    public static ConceptReference getAssociationReference(String associationName, String internalCodingSchemeName,
            String internalVersionString) throws MissingResourceException, UnexpectedInternalError {
        // get the URN for a associationName.
        String niceName = getURNForAssociationName(associationName, internalCodingSchemeName, internalVersionString);
        if (niceName != null && niceName.length() > 0) {
            if (!niceName.equals("urn:oid:1.3.6.1.4.1.2114.108.1.8.1")) {
                // this is a magic value used to globally define hasSubtype - if
                // it comes back as this,
                // there won't be a niceName mapping....
                // If it isn't the above oid, then lets try to get a "nice name"
                // for this urn
                niceName = ResourceManager.instance().getExternalCodingSchemeNameForUserCodingSchemeNameOrId(niceName,
                        null);
            }
        }

        ConceptReference cr = new ConceptReference();
        cr.setCodingSchemeName(niceName);
        cr.setCode(associationName);
        return cr;
    }

    public static GHolder resolveRelationships(ConceptReference graphFocus, boolean resolveForward,
            boolean resolveBackward, int resolveAssociationDepth, int maxToReturn, ArrayList<Operation> operations,
            String internalCodingSchemeName, String internalVersionString, String relationName,
            boolean keepLastAssociationLevelUnResolved) throws Exception {
        SQLInterface si = ResourceManager.instance().getSQLInterface(internalCodingSchemeName, internalVersionString);

        try {
            GHolder resultsToReturn = new GHolder(internalCodingSchemeName, internalVersionString, graphFocus,
                    resolveForward, resolveBackward);

            boolean noFocus = false;

            if (graphFocus == null || graphFocus.getConceptCode() == null || graphFocus.getConceptCode().length() == 0
                    || graphFocus.getConceptCode().equals("@") || graphFocus.getConceptCode().equals("@@")) {
                // start from root.
                if (resolveForward && resolveBackward) {
                    throw new LBParameterException(
                            "If you do not provide a focus node, you must choose resolve forward or resolve reverse, not both."
                                    + "  Choose resolve forward to start at root nodes.  Choose resolve reverse to start at tail nodes.");
                } else {
                    noFocus = true;
                }
            }

            boolean hasCodeRestriction = false;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i) instanceof CodeRestriction) {
                    hasCodeRestriction = true;
                    break;
                }
            }

            if (resolveForward) {
                ArrayList<Operation> localOps = new ArrayList<Operation>();

                int currentDepth = 0;

                // if they have not provided a focus code (and no other code
                // restrictions)
                // I need to add a restriction to the special top node.
                // If they have not provided a focus code, but they have
                // provided other code
                // restrictions, I'm going to return the top or bottom of what
                // ever tree results from their
                // restrictions.

                // if they provided a real focus node, add a restriction for
                // that.

                boolean roots = false;
                if (noFocus && !hasCodeRestriction) {
                    ConceptReference cr = new ConceptReference();
                    cr.setCode("@");
                    cr.setCodingSchemeName(getURNForInternalCodingSchemeName(internalCodingSchemeName,
                            internalVersionString));
                    RestrictToSourceCodes r = new RestrictToSourceCodes(cr);
                    localOps.add(r);
                    roots = true;

                } else if (!noFocus) {
                    RestrictToSourceCodes r = new RestrictToSourceCodes(graphFocus);
                    localOps.add(r);
                }
                localOps.addAll(operations);

                ConceptReferenceList crl = relationshipHandler(si, resultsToReturn, localOps, true,
                        internalCodingSchemeName, internalVersionString, maxToReturn, relationName,
                        resolveAssociationDepth, currentDepth, keepLastAssociationLevelUnResolved);
                if (resultsToReturn.getNodeCount() == 0 && roots) {
                    // they wanted top nodes, but we didn't find any for
                    // whatever association restriction
                    // they provided. Must not be calculated yet.
                    throw new LBParameterException(
                            "No top nodes could be located for the supplied restriction set in the requested direction. ");
                }

                if (!noFocus || hasCodeRestriction) {
                    // If they provide a focus node, the first query already
                    // returned that node plus the nodes it points to.
                    currentDepth = 1;
                }
                // here is the loop to continue walking down the graph.
                while (crl.getConceptReferenceCount() > 0
                        && (currentDepth < resolveAssociationDepth || resolveAssociationDepth < 0)) {
                    localOps.set(0, new RestrictToSourceCodes(crl));
                    crl = relationshipHandler(si, resultsToReturn, localOps, true, internalCodingSchemeName,
                            internalVersionString, maxToReturn, relationName, resolveAssociationDepth, currentDepth,
                            keepLastAssociationLevelUnResolved);
                    currentDepth++;
                }
            }
            if (resolveBackward) {
                // If they provide a focus node, the first query will return
                // that node plus the nodes it points to.
                int currentDepth = 0;

                ArrayList<Operation> localOps = new ArrayList<Operation>();

                boolean leafs = false;
                if (noFocus && !hasCodeRestriction) {
                    ConceptReference cr = new ConceptReference();
                    cr.setCode("@@");
                    cr.setCodingSchemeName(getURNForInternalCodingSchemeName(internalCodingSchemeName,
                            internalVersionString));
                    RestrictToTargetCodes r = new RestrictToTargetCodes(cr);
                    localOps.add(r);
                    leafs = true;

                } else if (!noFocus) {
                    RestrictToTargetCodes r = new RestrictToTargetCodes(graphFocus);
                    localOps.add(r);
                }
                localOps.addAll(operations);

                ConceptReferenceList crl = relationshipHandler(si, resultsToReturn, localOps, false,
                        internalCodingSchemeName, internalVersionString, maxToReturn, relationName,
                        resolveAssociationDepth, currentDepth, keepLastAssociationLevelUnResolved);

                if (resultsToReturn.getNodeCount() == 0 && leafs) {
                    // they wanted leaf nodes, but we didn't find any for
                    // whatever association restriction
                    // they provided. Must not be calculated yet.
                    throw new LBParameterException(
                            "No leaf nodes could be located for the supplied restriction set in the requested direction. ");
                }

                if (!noFocus || hasCodeRestriction) {
                    // If they provide a focus node, the first query already
                    // returned that node plus the nodes it points to.
                    currentDepth = 1;
                }
                // here is the loop to continue walking up the graph.
                while (crl.getConceptReferenceCount() > 0
                        && (currentDepth < resolveAssociationDepth || resolveAssociationDepth < 0)) {
                    localOps.set(0, new RestrictToTargetCodes(crl));
                    crl = relationshipHandler(si, resultsToReturn, localOps, false, internalCodingSchemeName,
                            internalVersionString, maxToReturn, relationName, resolveAssociationDepth, currentDepth,
                            keepLastAssociationLevelUnResolved);
                    currentDepth++;
                }
            }

            // TODO [performance] what about making faster by "unrolling" some
            // of these recursive queries on the DB?

            return resultsToReturn;
        } catch (MissingResourceException e) {
            throw new LBParameterException("Either the source or the target code could not be properly resolved");
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * This returns a list of ConceptReferences that are found to be new - or
     * unqueried.
     * 
     * @throws UnexpectedInternalError
     * @throws MissingResourceException
     */
    private static ConceptReferenceList relationshipHandler(SQLInterface si, GHolder resultsToReturn,
            ArrayList<Operation> operations, boolean forward, String internalCodingSchemeName,
            String internalVersionString, int maxToReturn, String relationName, int resolveAssociationDepth,
            int currentDepth, boolean keepLastAssociationLevelUnResolved) throws SQLException, LBInvocationException,
            LBParameterException, MissingResourceException, UnexpectedInternalError {
        ConceptReferenceList crl;
        if (keepLastAssociationLevelUnResolved && (currentDepth + 1 == resolveAssociationDepth)
                && resolveAssociationDepth >= 0) {
            crl = associationHelper(si, resultsToReturn, operations, forward, internalCodingSchemeName,
                    internalVersionString, maxToReturn, relationName);
        } else {
            crl = helper(si, resultsToReturn, operations, forward, internalCodingSchemeName, internalVersionString,
                    maxToReturn, relationName);
        }
        return crl;
    }

    /**
     * This returns a list of ConceptReferences that are found to be new - or
     * unqueried.
     * 
     * Note: As of LexGrid database tables version 1.8, the AssociationQualifiers
     * are loaded via a JOIN instead of separate SELECTs. To preserve backwards
     * compatibility, if the table does not implement version 1.8, separate
     * selects will still be used.
     * 
     * @throws UnexpectedInternalError
     * @throws MissingResourceException
     */
    protected static ConceptReferenceList helper(SQLInterface si, GHolder resultsToReturn,
            ArrayList<Operation> operations, boolean forward, String internalCodingSchemeName,
            String internalVersionString, int userMaxToReturn, String relationName) throws SQLException,
            LBInvocationException, LBParameterException, MissingResourceException, UnexpectedInternalError {
        PreparedStatement getRelations = null;
        PreparedStatement getQualifiers = null;

        try {
            GraphQuestionQuery query = buildGraphQuestionQuery(operations, internalCodingSchemeName,
                    internalVersionString, relationName, false);
            ConceptReferenceList next = new ConceptReferenceList();
            
            getQualifiers = si.checkOutPreparedStatement("Select " + SQLTableConstants.TBLCOL_QUALIFIERNAME + ", "
                    + SQLTableConstants.TBLCOL_QUALIFIERVALUE + " from "
                    + si.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_E_QUALS) + " where "
                    + si.getSQLTableConstants().codingSchemeNameOrId + " = ? and "
                    + SQLTableConstants.TBLCOL_MULTIATTRIBUTESKEY + " = ? ");

            getRelations = si.modifyAndCheckOutPreparedStatement("SELECT ca."
                    + si.getSQLTableConstants().entityCodeOrAssociationId + ", " + "ca."
                    + si.getSQLTableConstants().sourceCSIdOrEntityCodeNS + ", " + "ca."
                    + si.getSQLTableConstants().sourceEntityCodeOrId + ", " + "ca."
                    + si.getSQLTableConstants().targetCSIdOrEntityCodeNS + ", " + "ca."
                    + si.getSQLTableConstants().targetEntityCodeOrId + ", " + "ca."
                    + SQLTableConstants.TBLCOL_MULTIATTRIBUTESKEY + ", "
                    + " entitySource."+ SQLTableConstants.TBLCOL_ENTITYDESCRIPTION + " {AS} "
                    + " sourceEntityDescription" + ", " 
                    + " entityTarget."+ SQLTableConstants.TBLCOL_ENTITYDESCRIPTION + " {AS} "
                    + " targetEntityDescription" + 
                    
                    (isEntityAssnsToEQualsIndexPresent(si) && !query.qualiferTableRequired_ ? ", "
                    + "caa." + SQLTableConstants.TBLCOL_QUALIFIERNAME + ", " 
                    + "caa." + SQLTableConstants.TBLCOL_QUALIFIERVALUE : " ")
                    
                    + getGraphQuestionQueryFromPart(query, si, false)
                    
                    + " left join " + si.getTableName(SQLTableConstants.ENTITY)
                    + " {AS} entitySource on "
                    + " ca." + si.getSQLTableConstants().codingSchemeNameOrId
                    + " = entitySource." + si.getSQLTableConstants().codingSchemeNameOrId
                    + " and " + 
                          
                    (si.supports2009Model() ?       
                    " ca." + SQLTableConstants.TBLCOL_SOURCEENTITYCODENAMESPACE
                    + " = entitySource." + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE
                    + " and " : "")
                                  
                    + " ca." + si.getSQLTableConstants().sourceEntityCodeOrId
                    + " = entitySource." + si.getSQLTableConstants().entityCodeOrId
                      
                    + " left join " + si.getTableName(SQLTableConstants.ENTITY)
                    + " {AS} entityTarget on "
                    + " ca." + si.getSQLTableConstants().codingSchemeNameOrId
                    + " = entityTarget." + si.getSQLTableConstants().codingSchemeNameOrId
                    + " and " +
                    
                    (si.supports2009Model() ?       
                            " ca." + SQLTableConstants.TBLCOL_TARGETENTITYCODENAMESPACE
                            + " = entityTarget." + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE
                            + " and " : "")
                            
                    + " ca." + si.getSQLTableConstants().targetEntityCodeOrId
                    + " = entityTarget." + si.getSQLTableConstants().entityCodeOrId +
                    
                    (isEntityAssnsToEQualsIndexPresent(si) && !query.qualiferTableRequired_ ?
                        " left join " + si.getTableName(SQLTableConstants.ENTITY_ASSOCIATION_TO_E_QUALS)
                        + " {AS} caa on "
                        + " ca." + SQLTableConstants.TBLCOL_MULTIATTRIBUTESKEY
                        + " = caa." + SQLTableConstants.TBLCOL_MULTIATTRIBUTESKEY : "")          
                    
                    + getGraphQuestionQueryWherePart(query, si));

            setGraphQuestionQueryParameters(query, getRelations);
            int curCount = resultsToReturn.getNodeCount();
            int systemMaxToReturn = ResourceManager.instance().getSystemVariables().getMaxResultSize();
            if (curCount >= systemMaxToReturn) {
                resultsToReturn.setResultsSkipped(true);
                getLogger().info("Potential graph results are being skipped due to hitting system max limit");
                return next;
            }

            // add a small amount over the limit - this way a warning should be
            // generated farther down.
            getRelations.setMaxRows(systemMaxToReturn - curCount + 10);

            if (userMaxToReturn > 0) {
                if (curCount >= userMaxToReturn) {
                    resultsToReturn.setResultsSkipped(true);
                    getLogger().info("Potential graph results are being skipped due to hitting user requested limit");
                    return next;
                }

                if (userMaxToReturn < systemMaxToReturn) {
                    // add a small amount over the limit - this way a warning
                    // should be generated farther
                    // down.
                    getRelations.setMaxRows(userMaxToReturn - curCount + 10);
                }
            }

            ResultSet results = getRelations.executeQuery();
            Map<String, AssociationInfoHolder> relationsMap = new HashMap<String,AssociationInfoHolder>();

            while (results.next()) { 
                String multiAttributeKey = results.getString(SQLTableConstants.TBLCOL_MULTIATTRIBUTESKEY);
                
                //if the multiAttributesKey is blank, generate one. It is now the primary key, so it should
                //always be present... but if its not (in earlier table versions it could be null) then we 
                //want to generated a random one. Otherwise, we can't use it as a key for the relations map
                //(above). It is ok to generate a random one because if it is null, we know there are going
                //to be no Qualifiers associated with it.
                if(StringUtils.isBlank(multiAttributeKey)){
                    multiAttributeKey = UUID.randomUUID().toString();
                }
                
                String association = results.getString(si.getSQLTableConstants().entityCodeOrAssociationId);
                String sourceCodingSchemeIdOrNS = results.getString(si.getSQLTableConstants().sourceCSIdOrEntityCodeNS);
                String sourceCodingSchemeName = mapToCodingSchemeName(si, internalCodingSchemeName, sourceCodingSchemeIdOrNS); 
                String sourceConceptCode = results.getString(si.getSQLTableConstants().sourceEntityCodeOrId);
                String targetCodingSchemeIdOrNS = results.getString(si.getSQLTableConstants().targetCSIdOrEntityCodeNS);
                String targetCodingSchemeName = mapToCodingSchemeName(si, internalCodingSchemeName, targetCodingSchemeIdOrNS); 
                String targetConceptCode = results.getString(si.getSQLTableConstants().targetEntityCodeOrId);
                String sourceEntityDescription = results.getString("sourceEntityDescription");
                String targetEntityDescription = results.getString("targetEntityDescription");
                String qualifierName = null;
                String qualifierValue = null;
                
                if(isEntityAssnsToEQualsIndexPresent(si) && !query.qualiferTableRequired_){
                    qualifierName = results.getString(SQLTableConstants.TBLCOL_QUALIFIERNAME);
                    qualifierValue = results.getString(SQLTableConstants.TBLCOL_QUALIFIERVALUE);
                }
                
                if (StringUtils.isBlank(multiAttributeKey) || !relationsMap.containsKey(multiAttributeKey)) {

                    if (sourceConceptCode.equals("@")) {
                        if (forward) {
                            ConceptReference cr = resultsToReturn.addNode(
                                    targetCodingSchemeName,
                                    targetCodingSchemeIdOrNS,
                                    targetConceptCode,
                                    null,
                                    targetEntityDescription,
                                    internalCodingSchemeName,
                                    internalVersionString,
                                    true);
                            if (cr != null) {
                                next.addConceptReference(cr);
                            }
                        }
                    } else if (targetConceptCode.equals("@@")) {
                        if (!forward) {
                            ConceptReference cr = resultsToReturn.addNode(
                                    sourceCodingSchemeName,
                                    sourceCodingSchemeIdOrNS,
                                    sourceConceptCode,
                                    null,
                                    sourceEntityDescription,
                                    internalCodingSchemeName,
                                    internalVersionString,
                                    false);
                            if (cr != null) {
                                next.addConceptReference(cr);
                            }
                        }
                    } else {
                        NameAndValueList quals = null;

                        //check if the index on the multiAttributes key is present
                        //in the quals table. If it is is not, we want to get the 
                        //quals as seperate SELECT statements.
                        if ( (!isEntityAssnsToEQualsIndexPresent(si) || query.qualiferTableRequired_)
                                && multiAttributeKey != null 
                                && multiAttributeKey.length() > 0) {
                            quals = new NameAndValueList();
                            getQualifiers.setString(1, internalCodingSchemeName);
                            getQualifiers.setString(2, multiAttributeKey);

                            ResultSet qualifiers = getQualifiers.executeQuery();
                            while (qualifiers.next()) {
                                NameAndValue nv = new NameAndValue();
                                nv.setName(qualifiers.getString(SQLTableConstants.TBLCOL_QUALIFIERNAME));
                                nv.setContent(qualifiers.getString(SQLTableConstants.TBLCOL_QUALIFIERVALUE));
                                quals.addNameAndValue(nv);
                            }
                            qualifiers.close();
                        }             
                        
                        else{
                            if(StringUtils.isNotBlank(qualifierName)){
                                quals = new NameAndValueList();
                                NameAndValue nv = new NameAndValue();
                                nv.setName(qualifierName);
                                nv.setContent(qualifierValue);
                                quals.addNameAndValue(nv);
                            }
                        }
                        
                        AssociationInfoHolder infoHolder = new SQLImplementedMethods.AssociationInfoHolder();  
                        infoHolder.sourceCodingSchemeName = sourceCodingSchemeName;
                        infoHolder.sourceCodingSchemeIdOrNS = sourceCodingSchemeIdOrNS;
                        infoHolder.sourceConceptCode = sourceConceptCode;
                        infoHolder.sourceCodeTypes = null;
                        infoHolder.sourceEntityDescription = sourceEntityDescription;
                        infoHolder.relationName = relationName;
                        infoHolder.association = association;
                        infoHolder.targetCodingSchemeName = targetCodingSchemeName;
                        infoHolder.targetCodingSchemeIdOrNS = targetCodingSchemeIdOrNS;
                        infoHolder.targetConceptCode = targetConceptCode;
                        infoHolder.targetCodeTypes = null;
                        infoHolder.targetEntityDescription = targetEntityDescription;
                        infoHolder.quals = quals;
                        infoHolder.forward = forward;
                        infoHolder.internalCodingSchemeName = internalCodingSchemeName;
                        infoHolder.internalVersionString = internalVersionString; 
                        
                        relationsMap.put(multiAttributeKey, infoHolder);
                    }
                    
                } else {
                    AssociationInfoHolder assocInfo = relationsMap.get(multiAttributeKey);
                    NameAndValue nv = new NameAndValue();
                    nv.setName(qualifierName);
                    nv.setContent(qualifierValue);       
                    assocInfo.quals.addNameAndValue(nv);        
                }
                
                curCount = resultsToReturn.getNodeCount();
                if (curCount >= systemMaxToReturn) {
                    resultsToReturn.setResultsSkipped(true);
                    getLogger().info("Potential graph results are being skipped due to hitting system max limit");
                    next.removeAllConceptReference();

                    return next;
                }
                if (userMaxToReturn > 0 && curCount >= userMaxToReturn) {
                    resultsToReturn.setResultsSkipped(true);
                    getLogger().info("Potential graph results are being skipped due to hitting user requested limit");
                    next.removeAllConceptReference();
                    return next;
                }
            }
            
            results.close();
            for(AssociationInfoHolder infoHolder : relationsMap.values()){
                ConceptReference cr = resultsToReturn.addAssociation(
                        infoHolder.sourceCodingSchemeName,
                        infoHolder.sourceCodingSchemeIdOrNS,
                        infoHolder.sourceConceptCode,
                        infoHolder.sourceCodeTypes,
                        infoHolder.sourceEntityDescription,
                        infoHolder.relationName,
                        infoHolder.association,
                        infoHolder.targetCodingSchemeName,
                        infoHolder.targetCodingSchemeIdOrNS,
                        infoHolder.targetConceptCode,
                        infoHolder.targetCodeTypes,
                        infoHolder.targetEntityDescription,
                        infoHolder.quals,
                        infoHolder.forward,
                        infoHolder.internalCodingSchemeName,
                        infoHolder.internalVersionString);
                if (cr != null) {
                    next.addConceptReference(cr);
                }
            }   
            return next;
        }

        finally {
            si.checkInPreparedStatement(getRelations);
            si.checkInPreparedStatement(getQualifiers);
        }
    }
    
    private static class AssociationInfoHolder {
        private String sourceCodingSchemeName;
        private String sourceCodingSchemeIdOrNS;
        private String sourceConceptCode;
        private String[] sourceCodeTypes;
        private String sourceEntityDescription;
        private String relationName;
        private String association;
        private String targetCodingSchemeName;
        private String targetCodingSchemeIdOrNS;
        private String targetConceptCode;
        private String[] targetCodeTypes;
        private String targetEntityDescription;
        private NameAndValueList quals;
        private boolean forward;
        private String internalCodingSchemeName;
        private String internalVersionString;
    }

    /**
     * This returns a list of ConceptReferences that are found to be new - or
     * unqueried.
     * 
     * @throws UnexpectedInternalError
     * @throws MissingResourceException
     */
    private static ConceptReferenceList associationHelper(SQLInterface si, GHolder resultsToReturn,
            ArrayList<Operation> operations, boolean forward, String internalCodingSchemeName,
            String internalVersionString, int userMaxToReturn, String relationName) throws SQLException,
            LBInvocationException, LBParameterException, MissingResourceException, UnexpectedInternalError {
        PreparedStatement getRelations = null;
        try {
            GraphQuestionQuery query = buildGraphQuestionQuery(operations, internalCodingSchemeName,
                    internalVersionString, relationName, false);
            ConceptReferenceList next = new ConceptReferenceList();

            getRelations = si.modifyAndCheckOutPreparedStatement("SELECT ca."
                    + si.getSQLTableConstants().entityCodeOrAssociationId + ", " + "ca."
                    + si.getSQLTableConstants().sourceCSIdOrEntityCodeNS + ", " + "ca."
                    + si.getSQLTableConstants().sourceEntityCodeOrId + ", " + "ca."
                    + si.getSQLTableConstants().targetCSIdOrEntityCodeNS + ", " + "ca."
                    + si.getSQLTableConstants().targetEntityCodeOrId 
                    + getGraphQuestionQueryFromPart(query, si, false)
                    + getGraphQuestionQueryWherePart(query, si));

            setGraphQuestionQueryParameters(query, getRelations);

            ResultSet results = getRelations.executeQuery();

            while (results.next()) {
                String association = results.getString(si.getSQLTableConstants().entityCodeOrAssociationId);
                String sourceCodingSchemeIdOrNS = results.getString(si.getSQLTableConstants().sourceCSIdOrEntityCodeNS);
                String sourceCodingSchemeName = mapToCodingSchemeName(si, internalCodingSchemeName, sourceCodingSchemeIdOrNS); 
                String sourceConceptCode = results.getString(si.getSQLTableConstants().sourceEntityCodeOrId);
                String targetCodingSchemeIdOrNS = results.getString(si.getSQLTableConstants().targetCSIdOrEntityCodeNS);
                String targetCodingSchemeName = mapToCodingSchemeName(si, internalCodingSchemeName, targetCodingSchemeIdOrNS); 
                String targetConceptCode = results.getString(si.getSQLTableConstants().targetEntityCodeOrId);
                NameAndValueList quals = null;
                if (sourceConceptCode.equals("@")) {
                    if (forward) {
                        ConceptReference cr = resultsToReturn.addNode(
                                targetCodingSchemeName,
                                targetCodingSchemeIdOrNS,
                                targetConceptCode,
                                null,
                                null,
                                internalCodingSchemeName,
                                internalVersionString,
                                true);
                        if (cr != null) {
                            next.addConceptReference(cr);
                        }
                    }
                } else if (targetConceptCode.equals("@@")) {
                    if (!forward) {
                        ConceptReference cr = resultsToReturn.addNode(
                                sourceCodingSchemeName,
                                sourceCodingSchemeIdOrNS,
                                sourceConceptCode,
                                null,
                                null,
                                internalCodingSchemeName,
                                internalVersionString,
                                false);
                        if (cr != null) {
                            next.addConceptReference(cr);
                        }
                    }
                } else {
                    resultsToReturn.addAssociationInfo(
                            sourceCodingSchemeName,
                            sourceCodingSchemeIdOrNS,
                            sourceConceptCode,
                            null,
                            null,
                            relationName,
                            association,
                            targetCodingSchemeName,
                            targetCodingSchemeIdOrNS,
                            targetConceptCode,
                            null,
                            null,
                            quals,
                            forward,
                            internalCodingSchemeName,
                            internalVersionString);
                }
            }
            return next;
        }

        finally {
            si.checkInPreparedStatement(getRelations);
        }
    }

    /**
     * Maps from the give coding scheme identifier or namespace to
     * the coding scheme name.
     * <p>
     * If working under the 2009 model, the namespace must be mapped to
     * the coding scheme name through the SupportedNamespace metadata.
     * Otherwise no mapping is required, return the original value.
     * @param si
     * @param internalCodingSchemeName
     *          The scheme used to resolve supported namespace mappings.
     * @param idOrNamespace
     *          The value to map.
     * @return String
     */
    private static String mapToCodingSchemeName(SQLInterface si, String internalCodingSchemeName, String idOrNamespace) {
        String name = idOrNamespace;
        if (si.supports2009Model()) {
            // First check if we have had to map earlier ...
            String prefix = si.getTablePrefix();
            String mapKey = prefix + "[:]" + idOrNamespace;
            name = csNamespaceToName_.get(mapKey);

            if (name == null){
                try {
                    // Not mapped yet; attempt to resolve now ...
                    PreparedStatement ps =
                        si.checkOutPreparedStatement(new StringBuffer(256)
                        .append("select ").append(SQLTableConstants.TBLCOL_VAL1).append(" from ")
                        .append(si.getTableName(SQLTableConstants.CODING_SCHEME_SUPPORTED_ATTRIBUTES))
                        .append(" where ").append(SQLTableConstants.TBLCOL_CODINGSCHEMENAME).append(" = ?")
                        .append(" and ").append(SQLTableConstants.TBLCOL_SUPPORTEDATTRIBUTETAG)
                        .append(" = '").append(SQLTableConstants.TBLCOLVAL_SUPPTAG_NAMESPACE)
                        .append("' and ").append(SQLTableConstants.TBLCOL_ID).append(" = ?")
                        .toString());
                    ps.setString(1, internalCodingSchemeName);
                    ps.setString(2, idOrNamespace);
                    ResultSet results = null;
                    try {
                        results = ps.executeQuery();
                        if (results.next()) {
                            String uri = results.getString(SQLTableConstants.TBLCOL_VAL1);
                            if (StringUtils.isNotBlank(uri)) {
                                // URI should be sufficient; resolved to internal name/version
                                // as required ...
                                name = uri;
                            }
                        }
                    } finally {
                        if (results != null){
                            results.close();
                        }
                        ps.close();
                        si.checkInPreparedStatement(ps);
                    }
                } catch (Exception e) {
                    getLogger().warn("Unable to map namespace to name: " + idOrNamespace, e);
                } finally {
                    // If resolved, store the retrieved value for future reference.
                    // Otherwise, default to the current scheme.
                    if (name == null)
                        name = internalCodingSchemeName;
                    csNamespaceToName_.put(mapKey, name);                        
                }
            }
        }
        return name;
    }
    
    public static boolean isAssociationSymmetric(String internalCodingSchemeName, String internalVersionString,
            String relationName, String association) throws UnexpectedInternalError, MissingResourceException,
            LBParameterException {
        if (relationName == null || relationName.length() == 0 || association == null || association.length() == 0) {
            throw new LBParameterException("The parameters are required", "association, relationName");
        }

        // first, check the cache.
        String key = "isSymmetric" + internalCodingSchemeName + ":" + internalVersionString + ":" + relationName + ":"
                + association;

        Map cache = ResourceManager.instance().getCache();

        Boolean value = (Boolean) cache.get(key);
        if (value != null) {
            return value.booleanValue();
        }

        // not in the cache - go look it up.

        SQLInterface si = ResourceManager.instance().getSQLInterface(internalCodingSchemeName, internalVersionString);
        PreparedStatement isSymmetric = null;

        try {
            isSymmetric = si.checkOutPreparedStatement("select " + SQLTableConstants.TBLCOL_ISSYMMETRIC + " from "
                    + si.getTableName(SQLTableConstants.ASSOCIATION) + " where "
                    + si.getSQLTableConstants().codingSchemeNameOrId + " = ? and "
                    + si.getSQLTableConstants().containerNameOrContainerDC + " = ? and "
                    + si.getSQLTableConstants().associationNameOrId + " = ?");

            isSymmetric.setString(1, internalCodingSchemeName);
            isSymmetric.setString(2, relationName);
            isSymmetric.setString(3, association);

            ResultSet results = isSymmetric.executeQuery();

            if (results.next()) {
                Boolean symmetric = DBUtility.getBooleanFromResultSet(results, SQLTableConstants.TBLCOL_ISSYMMETRIC);
                if (symmetric == null)
                    symmetric = Boolean.FALSE;
                // put the result in the cache
                cache.put(key, symmetric);
                return symmetric.booleanValue();
            } else {
                throw new LBParameterException("There was a problem checking for symmetry.  The association "
                        + association + " couldn't be found.");
            }
        } catch (LBParameterException e) {
            throw e;
        } catch (Exception e) {
            throw new UnexpectedInternalError("There was an unexpected error while validating the language", e);
        } finally {
            si.checkInPreparedStatement(isSymmetric);
        }
    }
    
    /**
     * Resolve a ConceptReference.
     * 
     * @param conceptReference The ConceptReference to resolve.
     * @param codingScheme The CodingScheme Name that contains this ConceptReference.
     * @param version The Version of the containing CodingScheme.
     * @return The ResolvedConceptReferenece, null if not found.
     * @throws LBException
     *          Thrown the ConceptReference cannot be resolved, or more than one
     *          result is resolved (the ConceptReference is ambiguous).
     */
    public static ResolvedConceptReference resolveConceptReference(ConceptReference conceptReference, String version) throws LBParameterException, MissingResourceException {      
        LexBIGService lbs = LexBIGServiceImpl.defaultInstance(); 
        CodedNodeSet cns = null;
        CodingSchemeVersionOrTag csvt = null;

        if (version != null ) {
            csvt = new CodingSchemeVersionOrTag();
            csvt.setVersion(version);
        }
        ResolvedConceptReferenceList rcrl;
        try {
            cns = lbs.getCodingSchemeConcepts(conceptReference.getCodingSchemeName(), csvt);
            ConceptReferenceList crl = new ConceptReferenceList();
            crl.addConceptReference(conceptReference);
            cns.restrictToCodes(crl);
            rcrl = cns.resolveToList(null, null, null, null, 2);
        } catch (Exception e) {
            throw new LBRuntimeException("Unexpected Problem Resolving the Concept Reference.", e);
        } 

        ResolvedConceptReference[] rcr = rcrl.getResolvedConceptReference();
        if(rcr.length > 1){
            throw new LBParameterException("Resolution of the ConceptReference resulted in more than one " +
                    "ResolvedConceptReference. Please add additional information to the ConceptReference " +
            "being passed in.");
        }

        if(rcr.length == 0){
            getLogger().warn("Resolution of the ConceptReference with Code: " + conceptReference.getCode() +
                    " from Coding Scheme: " + conceptReference.getCodingSchemeName() +
            " returned no results.");
            return null;
        }
        return rcr[0];
    }  
    
    /**
     * Check if this ConceptReference represents a Root or Tail node.
     * 
     * @param cr The ConceptReference to check.
     * @return true if it is a Root or Tail node, false if not.
     */
    private static boolean isConceptReferenceRootOrTail(ConceptReference cr){
        String code = cr.getCode();
        if(code.equals("@") || code.equals("@@")){
            return true;
        } else {
            return false;
        }
    }
    
    /*
     * This should never (or very rarely) need to be used. The entryState table should be JOIN'ed.
     * Using individual 'SELECTS' for the entryState is bad for performance.
     */
    @Deprecated
    private static Map<Object, Object> getEntryState(String internalCodingSchemeName, String internalVersionString, 
            Integer propEntryStateId ) throws SQLException {
        
        SQLInterface si = null;
        ResultSet results = null;
        
        Map<Object, Object> entryStateMap = new HashMap<Object, Object>();
        
        try {
            si = ResourceManager.instance().getSQLInterface(internalCodingSchemeName,
                    internalVersionString);
        } catch (MissingResourceException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        PreparedStatement getEntryState = null;
        try {
            getEntryState = si.checkOutPreparedStatement("Select * " + " from "
                    + si.getTableName(SQLTableConstants.ENTRY_STATE) + " where "
                    + SQLTableConstants.TBLCOL_ENTRYSTATEID + " = ? and " + SQLTableConstants.TBLCOL_ENTRYTYPE
                    + " = ?");
            
            getEntryState.setInt(1, propEntryStateId);
            getEntryState.setString(2, SQLTableConstants.ENTRY_STATE_TYPE_PROPERTY);

            results = getEntryState.executeQuery();

            if (results.next()) {
                
                String owner = results.getString(SQLTableConstants.TBLCOL_OWNER);
                String status = results.getString(SQLTableConstants.TBLCOL_STATUS);
                Timestamp effectiveDate = results.getTimestamp(SQLTableConstants.TBLCOL_EFFECTIVEDATE);
                Timestamp expirationDate = results.getTimestamp(SQLTableConstants.TBLCOL_EXPIRATIONDATE);
                String revisionId = results.getString(SQLTableConstants.TBLCOL_REVISIONID);
                String prevRevisionId = results.getString(SQLTableConstants.TBLCOL_PREVREVISIONID);
                String changeType = results.getString(SQLTableConstants.TBLCOL_CHANGETYPE);
                String relativeOrder = results.getString(SQLTableConstants.TBLCOL_RELATIVEORDER);

                EntryState es = new EntryState();

                if (!StringUtils.isBlank(changeType)) {
                    es.setChangeType(org.LexGrid.versions.types.ChangeType.valueOf(changeType));
                }
                es.setContainingRevision(revisionId);
                es.setPrevRevision(prevRevisionId);
                es.setRelativeOrder(computeRelativeOrder(relativeOrder));
                
                entryStateMap.put(SQLTableConstants.TBL_ENTRY_STATE, es);
                entryStateMap.put(SQLTableConstants.TBLCOL_OWNER, owner);
                entryStateMap.put(SQLTableConstants.TBLCOL_STATUS, status);
                entryStateMap.put(SQLTableConstants.TBLCOL_EFFECTIVEDATE, effectiveDate);
                entryStateMap.put(SQLTableConstants.TBLCOL_EXPIRATIONDATE, expirationDate);
            }
            
        } catch (SQLException e) {
            throw e;
        } finally {
            results.close();
            si.checkInPreparedStatement(getEntryState);
        }
        
            
        return entryStateMap;
    }
    /**
     * This returns a list of ConceptReferences that are orphaned.
     * 
     * @throws UnexpectedInternalError
     * @throws MissingResourceException
     */
    public static ResolvedConceptReferenceList orphanedEntityQuery(SQLInterface si, ArrayList<Operation> operations,
            boolean forward, String internalCodingSchemeName, String internalVersionString, String containerName)
            throws LBException {
        String orphanedSQLString = null;
        String orphanedSubSelectSQLStringTran = null;
        String orphanedSubSelectSQLString= null;
        PreparedStatement orphanedPreparedStmt = null;

        SQLTableConstants stc = si.getSQLTableConstants();
        long startTime = System.currentTimeMillis();
        try {
            orphanedSQLString = "Select * from " + si.getTableName(SQLTableConstants.ENTITY) + 
                                " where ";
            String entityNS_and_enitityCode_subQuery= "("
                    + SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE + ", " + SQLTableConstants.TBLCOL_ENTITYCODE
                    + ") NOT IN  ";
            
            GraphQuestionQuery queryTran = buildGraphQuestionQuery(operations, internalCodingSchemeName,
                    internalVersionString, containerName, true);
            if (forward) {
                             orphanedSubSelectSQLStringTran = "SELECT ca." + stc.targetCSIdOrEntityCodeNS + " , ca." + stc.targetEntityCodeOrId + 
                getGraphQuestionQueryFromPart(queryTran, si, true) + getGraphQuestionQueryWherePart(queryTran, si);
            } else {
                orphanedSubSelectSQLStringTran = "SELECT ca." + stc.sourceCSIdOrEntityCodeNS + ", ca." + stc.sourceEntityCodeOrId + 
                 getGraphQuestionQueryFromPart(queryTran, si, true) + getGraphQuestionQueryWherePart(queryTran, si);
            }

            orphanedSQLString += entityNS_and_enitityCode_subQuery+ "( " + orphanedSubSelectSQLStringTran + " ) ";
            orphanedSQLString+= " AND "+  SQLTableConstants.TBLCOL_ENTITYCODE +" NOT IN ('@' , '@@') ";
            orphanedPreparedStmt = si.modifyAndCheckOutPreparedStatement(orphanedSQLString);
            setGraphQuestionQueryParameters(queryTran, orphanedPreparedStmt);
            int systemMaxToReturn = ResourceManager.instance().getSystemVariables().getMaxResultSize();
            // add a small amount over the limit - this way a warning should be
            // generated farther down.
            orphanedPreparedStmt.setMaxRows(systemMaxToReturn);
            //getLogger().debug("\nstatement= " + orphanedPreparedStmt);
            System.out.println("\nstatement= " + orphanedPreparedStmt);

            ResultSet results = orphanedPreparedStmt.executeQuery();
            ResolvedConceptReferenceList rcrl = new ResolvedConceptReferenceList();
            while (results.next()) {
                ResolvedConceptReference rcr = new ResolvedConceptReference();
                String codingSchemeIdOrNS = results.getString(si.getSQLTableConstants().codingSchemeNameOrId);

                String entityCode = results.getString(si.getSQLTableConstants().entityCodeOrId);
                EntityDescription ed = new EntityDescription();
                ed.setContent(results.getString(SQLTableConstants.TBLCOL_ENTITYDESCRIPTION));
                if (si.supports2009Model()) {
                    String namespace = results.getString(SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE);
                    rcr.setCodeNamespace(namespace);
                }

                rcr.setCodingSchemeName(codingSchemeIdOrNS);
                rcr.setConceptCode(entityCode);
                rcr.setEntityDescription(ed);
                rcrl.addResolvedConceptReference(rcr);

            }
             getLogger().debug("Time to execute orphanedQuery=" +  (System.currentTimeMillis() - startTime));
            return rcrl;
        } catch (Exception e) {
            String logId = getLogger().error("Unexpected Error", e);
            throw new LBException("Unexpected Internal Error", e);

        }

        finally {
            si.checkInPreparedStatement(orphanedPreparedStmt);
        }
    }
    
    
    /**
     * This returns a list of CountConceptReferences that hold the count of the concepts at the next/prev level depending
     * the value of the forward flag
     * @throws UnexpectedInternalError
     * @throws MissingResourceException
     */
    public static ConceptReferenceList countQuery(SQLInterface si, 
            ArrayList<Operation> operations, boolean forward, String internalCodingSchemeName,
            String internalVersionString, String containerName) throws LBException {
        PreparedStatement getCountStmt = null;
        PreparedStatement getQualifiers = null;
        SQLTableConstants stc= si.getSQLTableConstants();
        long startTime = System.currentTimeMillis();
        try {
            GraphQuestionQuery query = buildGraphQuestionQuery(operations, internalCodingSchemeName,
                    internalVersionString, containerName, false);           
            query.whereClause_.append(" AND "+ stc.sourceEntityCodeOrId +" NOT IN ('@' , '@@') "+
                                      " AND "+ stc.targetEntityCodeOrId +" NOT IN ('@' , '@@') ");
            
            ConceptReferenceList crl = new ConceptReferenceList();

           
            if (forward) {
               query.whereClause_.append(" group by ca."+ stc.sourceCSIdOrEntityCodeNS + ", ca." + stc.sourceEntityCodeOrId );
               getCountStmt = si.modifyAndCheckOutPreparedStatement("SELECT ca."
                    + stc.sourceCSIdOrEntityCodeNS + " {AS} codingSchemeIdOrNS, " + "ca."
                    + stc.sourceEntityCodeOrId + " {AS} entityCode, " + "count(distinct ca."
                    + stc.targetEntityCodeOrId + ") {AS} count " + " "
                    + getGraphQuestionQueryFromPart(query, si, false)
                    + getGraphQuestionQueryWherePart(query, si));
            } else {
                query.whereClause_.append(" group by ca."+ stc.targetCSIdOrEntityCodeNS + ", ca." + stc.targetEntityCodeOrId );
                getCountStmt = si.modifyAndCheckOutPreparedStatement("SELECT ca."
                        + stc.targetCSIdOrEntityCodeNS + " {AS} codingSchemeIdOrNS, " + "ca."
                        + stc.targetEntityCodeOrId + " {AS} entityCode, " + "count(distinct ca."                      
                        + stc.sourceEntityCodeOrId + ") {AS} count " + " "
                        + getGraphQuestionQueryFromPart(query, si, false)
                        + getGraphQuestionQueryWherePart(query, si));
            }

            setGraphQuestionQueryParameters(query, getCountStmt);
           
            int systemMaxToReturn = ResourceManager.instance().getSystemVariables().getMaxResultSize();
            
            // add a small amount over the limit - this way a warning should be
            // generated farther down.
            getCountStmt.setMaxRows(systemMaxToReturn);
           
            //getLogger().debug("\nstatement= "+getCountStmt);
            
            ResultSet results = getCountStmt.executeQuery();
            
            while (results.next()) {
               
                String codingSchemeIdOrNS = results.getString("codingSchemeIdOrNS");
              
                String entityCode = results.getString("entityCode");
                int count = results.getInt("count");
                
                CountConceptReference ccr= new CountConceptReference(internalCodingSchemeName,codingSchemeIdOrNS,entityCode, count);
                crl.addConceptReference(ccr);
               
                
               
            }
            //getLogger().debug("Time to execute countQuery=" + (System.currentTimeMillis() - startTime));
            return crl;
        } catch (Exception e) {
            String logId = getLogger().error("Unexpected Error", e);
            throw new LBException("Unexpected Internal Error", e);
            
        }
        

        finally {
            si.checkInPreparedStatement(getCountStmt);
            si.checkInPreparedStatement(getQualifiers);
        }
    }
    
    /**
     * Checks if the EntityAssnsToEQuals multiAttributesKey column index is present
     * 
     * @param si
     * @return if the multiAttributesKey column index is present
     */
    protected static boolean isEntityAssnsToEQualsIndexPresent(SQLInterface si){
        return parseFloatFromTableVersion(si) >= 1.8f;
    }
    
    /**
     * Checks if the current Association Table contains the EntryStateId column.
     * 
     * @param si
     * @return if the EntryStateId column is present.
     */
    protected static boolean isEntryStateIdInAssociationTable(SQLInterface si){
        return parseFloatFromTableVersion(si) >= 1.8f;
    }
    
    /**
     * Returns the float representation of the current table version.
     * 
     * @param si
     * @return the current (float) table version.
     */
    protected static float parseFloatFromTableVersion(SQLInterface si){
        return Float.parseFloat(si.getSQLTableConstants().getVersion());
    }
    
    /**
     * Generate a unique key for a Source object. We can't rely on the equals()
     * method being implemented correctly on the Source object (because model
     * objects can be generated with our without it depending on preferences), so
     * we have to construct a unique id here.
     * 
     * (This is to be used when putting Sources in Maps or Sets)
     * 
     * @param source
     * @return The id to uniquely identify this source within an Entity 
     */
    protected static String createUniqueKeyForSource(String value, String val1){
        return value + val1;
    }
}