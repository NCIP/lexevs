
package org.lexevs.dao.database.sqlimplementedmethods;

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

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.concepts.Comment;
import org.LexGrid.concepts.Definition;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.concepts.PropertyLink;
import org.LexGrid.custom.concepts.EntityFactory;
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
import org.LexGrid.relations.AssociationEntity;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.Relations;
import org.LexGrid.util.sql.DBUtility;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.connection.SQLInterface;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.exceptions.MissingResourceException;
import org.lexevs.exceptions.UnexpectedInternalError;
import org.lexevs.system.ResourceManager;
import org.springframework.transaction.annotation.Transactional;

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
@SuppressWarnings( {"deprecation", "unchecked"} )
public class SQLImplementedMethodsDao {
	
	/** The cs namespace to name_. */
	protected static Map<String, String> csNamespaceToName_ = new HashMap<String, String>();

	/** The resource manager. */
	private ResourceManager resourceManager;
	
	/** The logger. */
	private LgLoggerIF logger;
	
	/**
	 * Builds the coding scheme.
	 * 
	 * @param internalCodingSchemeName the internal coding scheme name
	 * @param internalVersionString the internal version string
	 * 
	 * @return the coding scheme
	 * 
	 * @throws MissingResourceException the missing resource exception
	 * @throws UnexpectedInternalError the unexpected internal error
	 */
	@Transactional
	public CodingScheme buildCodingScheme(String internalCodingSchemeName, String internalVersionString)
	throws MissingResourceException, UnexpectedInternalError {
		try {
			CodingScheme cs = new CodingScheme();
			ArrayList<SupportedAssociation> supportedAssociations = new ArrayList<SupportedAssociation>();
			ArrayList<SupportedAssociationQualifier> supportedAssociationQualifiers = new ArrayList<SupportedAssociationQualifier>();
			ArrayList<SupportedCodingScheme> supportedCodingSchemes = new ArrayList<SupportedCodingScheme>();
			ArrayList<SupportedContainerName> supportedRelContainers = new ArrayList<SupportedContainerName>();
			ArrayList<SupportedContext> supportedContexts = new ArrayList<SupportedContext>();
			ArrayList<SupportedDataType> supportedDataTypes = new ArrayList<SupportedDataType>();
			ArrayList<SupportedDegreeOfFidelity> supportedDegreesOfFidelity = new ArrayList<SupportedDegreeOfFidelity>();
			ArrayList<SupportedEntityType> supportedEntityTypes = new ArrayList<SupportedEntityType>();
			ArrayList<SupportedHierarchy> supportedHierarchy = new ArrayList<SupportedHierarchy>();
			ArrayList<SupportedLanguage> supportedLanguages = new ArrayList<SupportedLanguage>();
			ArrayList<SupportedNamespace> supportedNameSpaces = new ArrayList<SupportedNamespace>();
			ArrayList<SupportedProperty> supportedProperties = new ArrayList<SupportedProperty>();
			ArrayList<SupportedPropertyLink> supportedPropertyLinks = new ArrayList<SupportedPropertyLink>();
			ArrayList<SupportedPropertyQualifier> supportedPropertyQualifiers = new ArrayList<SupportedPropertyQualifier>();
			ArrayList<SupportedPropertyQualifierType> supportedPropertyQualifierTypes = new ArrayList<SupportedPropertyQualifierType>();
			ArrayList<SupportedPropertyType> supportedPropertyTypes = new ArrayList<SupportedPropertyType>();
			ArrayList<SupportedRepresentationalForm> supportedRepresentationalForms = new ArrayList<SupportedRepresentationalForm>();
			ArrayList<SupportedSortOrder> supportedSortOrders = new ArrayList<SupportedSortOrder>();
			ArrayList<SupportedSource> supportedSources = new ArrayList<SupportedSource>();
			ArrayList<SupportedSourceRole> supportedSourceRoles = new ArrayList<SupportedSourceRole>();
			ArrayList<SupportedStatus> supportedConceptStatus = new ArrayList<SupportedStatus>();
			ArrayList<Relations> relations = new ArrayList<Relations>();

			SQLInterface si = resourceManager.getSQLInterface(internalCodingSchemeName,
					internalVersionString);

			PreparedStatement getCodingSchemeDetails = null;
			PreparedStatement getCodingSchemeMultiDetails = null;
			PreparedStatement getCodingSchemeSupportedDetails = null;
			PreparedStatement getRelations = null;
			PreparedStatement getAssociations = null;
			PreparedStatement getEntryState = null;

			try {
				getCodingSchemeDetails = si.checkOutPreparedStatement("select * " + "  from "
						+ si.getTableName(SQLTableConstants.CODING_SCHEME) + " where "
						+ SQLTableConstants.TBLCOL_CODINGSCHEMENAME + " = ?");

				getCodingSchemeDetails.setString(1, internalCodingSchemeName);

				ResultSet results = getCodingSchemeDetails.executeQuery();

				if (results.next()) {
					String codingSchemeUriOrRegisteredName_value = results
					.getString(si.getSQLTableConstants().registeredNameOrCSURI);
					cs.setCodingSchemeName(resourceManager
							.getExternalCodingSchemeNameForUserCodingSchemeNameOrId(
									codingSchemeUriOrRegisteredName_value, internalVersionString));
					cs.setApproxNumConcepts((long) results.getInt(SQLTableConstants.TBLCOL_APPROXNUMCONCEPTS));
					Text copyright = new Text();
					copyright.setContent(results.getString(SQLTableConstants.TBLCOL_COPYRIGHT));
					cs.setCopyright(copyright);
					cs.setDefaultLanguage(results.getString(SQLTableConstants.TBLCOL_DEFAULTLANGUAGE));
					EntityDescription description = new EntityDescription();
					description.setContent(results.getString(SQLTableConstants.TBLCOL_ENTITYDESCRIPTION));
					cs.setEntityDescription(description);
					cs.setFormalName(results.getString(SQLTableConstants.TBLCOL_FORMALNAME));

					if (si.supports2009Model())
						cs.setIsActive(DBUtility.getBooleanFromResultSet(results, SQLTableConstants.TBLCOL_ISACTIVE));

					cs.setCodingSchemeURI(codingSchemeUriOrRegisteredName_value);
					cs.setRepresentsVersion(results.getString(SQLTableConstants.TBLCOL_REPRESENTSVERSION));
				} else {
					throw new MissingResourceException("The coding scheme " + internalCodingSchemeName
							+ " is not available.");
				}

				results.close();
				si.checkInPreparedStatement(getCodingSchemeDetails);

				getCodingSchemeMultiDetails = si.checkOutPreparedStatement("select * " + " from "
						+ si.getTableName(SQLTableConstants.CODING_SCHEME_MULTI_ATTRIBUTES) + " where "
						+ SQLTableConstants.TBLCOL_CODINGSCHEMENAME + " = ?");

				getCodingSchemeMultiDetails.setString(1, internalCodingSchemeName);

				results = getCodingSchemeMultiDetails.executeQuery();

				ArrayList<Source> sources = new ArrayList<Source>();
				ArrayList<String> localNames = new ArrayList<String>();

				while (results.next()) {
					String attributeName = results.getString(SQLTableConstants.TBLCOL_TYPENAME);
					String attributeValue = results.getString(SQLTableConstants.TBLCOL_ATTRIBUTEVALUE);

					if (attributeName.equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_LOCALNAME)) {
						localNames.add(attributeValue);
					} else if (attributeName.equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_SOURCE)) {
						Source s = new Source();
						s.setContent(attributeValue);
						s.setRole(results.getString(SQLTableConstants.TBLCOL_VAL2));
						s.setSubRef(results.getString(SQLTableConstants.TBLCOL_VAL1));
						sources.add(s);
					} else {
						getLogger().warn(
								"There is invalid data in the '" + SQLTableConstants.TBLCOL_TYPENAME
								+ "' column in the table "
								+ si.getTableName(SQLTableConstants.CODING_SCHEME_MULTI_ATTRIBUTES)
								+ " for the coding scheme: " + internalCodingSchemeName);
					}
				}

				results.close();
				si.checkInPreparedStatement(getCodingSchemeMultiDetails);

				cs.setSource(sources.toArray(new Source[sources.size()]));
				cs.setLocalName(localNames.toArray(new String[localNames.size()]));
				cs.setEntities(null);

				getCodingSchemeSupportedDetails = si.checkOutPreparedStatement("select *" + " from "
						+ si.getTableName(SQLTableConstants.CODING_SCHEME_SUPPORTED_ATTRIBUTES) + " where "
						+ SQLTableConstants.TBLCOL_CODINGSCHEMENAME + " = ?");

				getCodingSchemeSupportedDetails.setString(1, internalCodingSchemeName);

				results = getCodingSchemeSupportedDetails.executeQuery();

				while (results.next()) {
					String value = null;
					String attributeName = results.getString(SQLTableConstants.TBLCOL_SUPPORTEDATTRIBUTETAG);
					String id = results.getString(SQLTableConstants.TBLCOL_ID);
					String urn = results.getString(si.getSQLTableConstants().urnOruri);
					value = results.getString(SQLTableConstants.TBLCOL_IDVALUE);

					if (attributeName.equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_SUPPTAG_ASSOCIATION)) {
						SupportedAssociation temp = new SupportedAssociation();
						temp.setUri(urn);
						temp.setLocalId(id);
						temp.setContent(value);
						supportedAssociations.add(temp);
					} else if (attributeName.equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_SUPPTAG_ASSOCIATIONQUALIFIER)) {
						SupportedAssociationQualifier temp = new SupportedAssociationQualifier();
						temp.setUri(urn);
						temp.setLocalId(id);
						temp.setContent(value);
						supportedAssociationQualifiers.add(temp);
					} else if (attributeName.equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_SUPPTAG_CODINGSCHEME)) {
						SupportedCodingScheme temp = new SupportedCodingScheme();
						temp.setUri(urn);
						temp.setLocalId(id);
						temp.setContent(value);
						if (si.supports2009Model()) {
							temp.setIsImported("true".equalsIgnoreCase(
									results.getString(SQLTableConstants.TBLCOL_VAL1)));
						}
						supportedCodingSchemes.add(temp);
					} else if (attributeName.equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_SUPPTAG_CONCEPTSTATUS)) {
						SupportedStatus temp = new SupportedStatus();
						temp.setUri(urn);
						temp.setLocalId(id);
						temp.setContent(value);
						supportedConceptStatus.add(temp);
					} else if (attributeName.equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_SUPPTAG_CONTAINERNAME)) {
						SupportedContainerName temp = new SupportedContainerName();
						temp.setUri(urn);
						temp.setLocalId(id);
						temp.setContent(value);
						supportedRelContainers.add(temp);
					} else if (attributeName.equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_SUPPTAG_CONTEXT)) {
						SupportedContext temp = new SupportedContext();
						temp.setUri(urn);
						temp.setLocalId(id);
						temp.setContent(value);
						supportedContexts.add(temp);
					} else if (attributeName.equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_SUPPTAG_DATATYPE)) {
						SupportedDataType temp = new SupportedDataType();
						temp.setUri(urn);
						temp.setLocalId(id);
						temp.setContent(value);
						supportedDataTypes.add(temp);
					} else if (attributeName.equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_SUPPTAG_DEGREEOFFIDELITY)) {
						SupportedDegreeOfFidelity temp = new SupportedDegreeOfFidelity();
						temp.setUri(urn);
						temp.setLocalId(id);
						temp.setContent(value);
						supportedDegreesOfFidelity.add(temp);
					} else if (attributeName.equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_SUPPTAG_ENTITYTYPE)) {
						SupportedEntityType temp = new SupportedEntityType();
						temp.setUri(urn);
						temp.setLocalId(id);
						temp.setContent(value);
						supportedEntityTypes.add(temp);
					} else if (attributeName.equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_SUPPTAG_FORMAT)) {
						SupportedDataType temp = new SupportedDataType();
						temp.setUri(urn);
						temp.setLocalId(id);
						temp.setContent(value);
						supportedDataTypes.add(temp);
					} else if (attributeName.equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_SUPPTAG_HIERARCHY)) {
						SupportedHierarchy temp = new SupportedHierarchy();
						temp.setUri(urn);
						temp.setLocalId(id);
						temp.setContent(value);
						temp.setIsForwardNavigable(Boolean
								.valueOf(results.getString(SQLTableConstants.TBLCOL_VAL2)));
						temp.setRootCode(results.getString(SQLTableConstants.TBLCOL_VAL1));
						temp.setAssociationNames(value.split(","));
						supportedHierarchy.add(temp);
					} else if (attributeName.equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_SUPPTAG_LANGUAGE)) {
						SupportedLanguage temp = new SupportedLanguage();
						temp.setUri(urn);
						temp.setLocalId(id);
						temp.setContent(value);
						supportedLanguages.add(temp);
					} else if (attributeName.equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_SUPPTAG_NAMESPACE)) {
						SupportedNamespace temp = new SupportedNamespace();
						temp.setUri(urn);
						temp.setLocalId(id);
						temp.setContent(value);
						if (si.supports2009Model()) {
							temp.setEquivalentCodingScheme(results.getString(SQLTableConstants.TBLCOL_VAL1));
						}
						supportedNameSpaces.add(temp);
					} else if (attributeName.equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_SUPPTAG_PROPERTY)) {
						SupportedProperty temp = new SupportedProperty();
						temp.setUri(urn);
						temp.setLocalId(id);
						temp.setContent(value);
						supportedProperties.add(temp);
					} else if (attributeName.equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_SUPPTAG_PROPERTYLINK)) {
						SupportedPropertyLink temp = new SupportedPropertyLink();
						temp.setUri(urn);
						temp.setLocalId(id);
						temp.setContent(value);
						supportedPropertyLinks.add(temp);
					} else if (attributeName.equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_SUPPTAG_PROPERTYQUALIFIER)) {
						SupportedPropertyQualifier temp = new SupportedPropertyQualifier();
						temp.setUri(urn);
						temp.setLocalId(id);
						temp.setContent(value);
						supportedPropertyQualifiers.add(temp);
					} else if (attributeName.equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_SUPPTAG_PROPERTYQUALIFIERTYPE)) {
						SupportedPropertyQualifierType temp = new SupportedPropertyQualifierType();
						temp.setUri(urn);
						temp.setLocalId(id);
						temp.setContent(value);
						supportedPropertyQualifierTypes.add(temp);
					} else if (attributeName.equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_SUPPTAG_PROPERTYTYPE)) {
						SupportedPropertyType temp = new SupportedPropertyType();
						temp.setUri(urn);
						temp.setLocalId(id);
						temp.setContent(value);
						supportedPropertyTypes.add(temp);
					} else if (attributeName.equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_SUPPTAG_REPRESENTATIONALFORM)) {
						SupportedRepresentationalForm temp = new SupportedRepresentationalForm();
						temp.setUri(urn);
						temp.setLocalId(id);
						temp.setContent(value);
						supportedRepresentationalForms.add(temp);
					} else if (attributeName.equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_SUPPTAG_ROLEGROUP)) {
						SupportedSourceRole temp = new SupportedSourceRole();
						temp.setUri(urn);
						temp.setLocalId(id);
						temp.setContent(value);
						supportedSourceRoles.add(temp);
					} else if (attributeName.equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_SUPPTAG_SORTORDER)) {
						SupportedSortOrder temp = new SupportedSortOrder();
						temp.setUri(urn);
						temp.setLocalId(id);
						temp.setContent(value);
						supportedSortOrders.add(temp);
					} else if (attributeName.equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_SUPPTAG_SOURCE)) {
						SupportedSource temp = new SupportedSource();
						temp.setUri(urn);
						temp.setLocalId(id);
						temp.setContent(value);
						if (si.supports2009Model()) {
							temp.setAssemblyRule(results.getString(SQLTableConstants.TBLCOL_VAL1));
						}
						supportedSources.add(temp);
					} else if (attributeName.equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_SUPPTAG_STATUS)) {
						SupportedStatus temp = new SupportedStatus();
						temp.setUri(urn);
						temp.setLocalId(id);
						temp.setContent(value);
						supportedConceptStatus.add(temp);
					} else {
						getLogger().warn(
								"There is invalid data in the '" + SQLTableConstants.TBLCOL_SUPPORTEDATTRIBUTETAG
								+ "' column in the table "
								+ si.getTableName(SQLTableConstants.CODING_SCHEME_SUPPORTED_ATTRIBUTES)
								+ " for the coding scheme: " + internalCodingSchemeName + " I found ' "
								+ attributeName + "'");
					}
				}

				results.close();
				si.checkInPreparedStatement(getCodingSchemeSupportedDetails);

				// get the relations and associations

				String cdSchName = si.getSQLTableConstants().codingSchemeNameOrId;
				String relDcName = si.getSQLTableConstants().containerNameOrContainerDC;
				getRelations = si.modifyAndCheckOutPreparedStatement("Select " + cdSchName + ", " + relDcName + ", "
						+ SQLTableConstants.TBLCOL_ISNATIVE + ", " + SQLTableConstants.TBLCOL_ENTITYDESCRIPTION
						+ " from " + si.getTableName(SQLTableConstants.RELATION) + " where " + cdSchName + " = ?");

				Relations currentRelation = null;

				getRelations.setString(1, internalCodingSchemeName);

				results = getRelations.executeQuery();
				sources = new ArrayList<Source>();

				while (results.next()) {
					String relName = results.getString(relDcName);
					// If it is not the same - we are on a new relation. add the
					// current one to the
					// list, and create a new one.
					if (currentRelation == null || !currentRelation.getContainerName().equals(relName)) {
						if (currentRelation != null) {
							// I'll get the associations later. Hold less sql
							// connections open that way.
							relations.add(currentRelation);
						}

						currentRelation = new Relations();
						sources = new ArrayList<Source>();

						currentRelation.setContainerName(relName);
						EntityDescription ed = new EntityDescription();
						ed.setContent(results
								.getString(SQLTableConstants.TBLCOL_ENTITYDESCRIPTION));
						
						currentRelation.setEntityDescription(ed); 
					}
				}

				// add the last one (if present)
				if (currentRelation != null) {
					// I'll get the associations later. Hold less sql
					// connections open that way.
					relations.add(currentRelation);
				}
				results.close();
				si.checkInPreparedStatement(getRelations);

				// populate the associations.
				// TODO: Fix this for the new model

        getAssociations = si.modifyAndCheckOutPreparedStatement("Select * " + " from "
                + si.getTableName(SQLTableConstants.ASSOCIATION) + " where " + cdSchName + " = ? and "
                + relDcName + " = ?");

        getAssociations.setString(1, internalCodingSchemeName);

        for (int i = 0; i < relations.size(); i++) {
            getAssociations.setString(2, relations.get(i).getContainerName());
            results = getAssociations.executeQuery();

            while (results.next()) {
            	AssociationPredicate as = new AssociationPredicate();
            	AssociationEntity ae = EntityFactory.createAssociation();

            	if(si.supports2009Model()){
            		ae.setEntityCode(results.getString(SQLTableConstants.TBLCOL_ENTITYCODE));  
            		ae.setEntityCodeNamespace(results.getString(SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE));     
            	} else {
            		ae.setEntityCode(results.getString(si.getSQLTableConstants().associationNameOrId));
            		ae.setEntityCodeNamespace(internalCodingSchemeName);
            	}
            	as.setAssociationName(results.getString(si.getSQLTableConstants().associationNameOrId));

            	EntityDescription ed = new EntityDescription();
            	ed.setContent(results
            			.getString(SQLTableConstants.TBLCOL_ENTITYDESCRIPTION));
            	ae.setEntityDescription(ed);
     
            	ae.setForwardName(results.getString(SQLTableConstants.TBLCOL_FORWARDNAME));
            	ae.setIsTransitive(DBUtility.getBooleanFromResultSet(results,
            			SQLTableConstants.TBLCOL_ISTRANSITIVE));
            	ae.setReverseName(results.getString(SQLTableConstants.TBLCOL_REVERSENAME));
            	
            	//Put the entity code/namespace in the supported association
            	for(SupportedAssociation assoc : supportedAssociations){
            		if(assoc.getLocalId().equals(ae.getEntityCode())){
            			assoc.setContent(as.getAssociationName());
            			assoc.setEntityCode(ae.getEntityCode());
            			assoc.setEntityCodeNamespace(ae.getEntityCodeNamespace());
            		}
            	}

            	if( isEntryStateIdInAssociationTable(si) ) {
            		int entryStateId = results.getInt(SQLTableConstants.TBLCOL_ENTRYSTATEID);

            		StringBuffer query = new StringBuffer();
            		query.append("SELECT * FROM ");
            		query.append(si.getTableName(SQLTableConstants.ENTRY_STATE));
            		query.append(" WHERE entryStateId = ?");

            		getEntryState = si.checkOutPreparedStatement(query.toString());
            		getEntryState.setInt(1, entryStateId);

            		ResultSet entryResult = getEntryState.executeQuery();

            		if( entryResult.next() ) {

            			String sOwner = entryResult.getString(SQLTableConstants.TBLCOL_OWNER);

            			ae.setOwner(sOwner);
            			ae.setStatus(entryResult.getString(SQLTableConstants.TBLCOL_STATUS));
            			ae.setEffectiveDate(entryResult.getTimestamp(SQLTableConstants.TBLCOL_EFFECTIVEDATE));
            			ae.setExpirationDate(entryResult.getTimestamp(SQLTableConstants.TBLCOL_EXPIRATIONDATE));

            			EntryState entryState = new EntryState();
            			String sChangeType = entryResult.getString(SQLTableConstants.TBLCOL_CHANGETYPE);

            			if (!StringUtils.isBlank(sChangeType)) {
            				entryState.setChangeType(ChangeType.valueOf(sChangeType));
            			}
            			entryState.setContainingRevision(entryResult.getString(SQLTableConstants.TBLCOL_REVISIONID));
            			entryState.setPrevRevision(entryResult.getString(SQLTableConstants.TBLCOL_PREVREVISIONID));
            			entryState.setRelativeOrder(entryResult.getLong(SQLTableConstants.TBLCOL_RELATIVEORDER));

            			ae.setEntryState(entryState);
            		}

            		if( entryResult != null )
            			entryResult.close();

            		si.checkInPreparedStatement(getEntryState);
            	}
            	relations.get(i).addAssociationPredicate(as);
            	if(cs.getEntities() == null) {
            		cs.setEntities(new Entities());
            	}
            	cs.getEntities().addAssociationEntity(ae);
            }
            results.close();
        }

        si.checkInPreparedStatement(getAssociations);
			} finally {
				si.checkInPreparedStatement(getCodingSchemeDetails);
				si.checkInPreparedStatement(getCodingSchemeMultiDetails);
				si.checkInPreparedStatement(getCodingSchemeSupportedDetails);
				si.checkInPreparedStatement(getRelations);
				si.checkInPreparedStatement(getAssociations);
				if( isEntryStateIdInAssociationTable(si) )
					si.checkInPreparedStatement(getEntryState);
			}


			Mappings mappings = new Mappings();

			mappings.setSupportedAssociation(supportedAssociations.toArray(new SupportedAssociation[supportedAssociations.size()]));
			mappings.setSupportedAssociationQualifier(supportedAssociationQualifiers.toArray(new SupportedAssociationQualifier[supportedAssociationQualifiers.size()]));
			mappings.setSupportedCodingScheme(supportedCodingSchemes.toArray(new SupportedCodingScheme[supportedCodingSchemes.size()]));
			mappings.setSupportedContainerName(supportedRelContainers.toArray(new SupportedContainerName[supportedRelContainers.size()]));
			mappings.setSupportedContext(supportedContexts.toArray(new SupportedContext[supportedContexts.size()]));
			mappings.setSupportedDataType(supportedDataTypes.toArray(new SupportedDataType[supportedDataTypes.size()]));
			mappings.setSupportedDegreeOfFidelity(supportedDegreesOfFidelity.toArray(new SupportedDegreeOfFidelity[supportedDegreesOfFidelity.size()]));
			mappings.setSupportedEntityType(supportedEntityTypes.toArray(new SupportedEntityType[supportedEntityTypes.size()]));
			mappings.setSupportedHierarchy(supportedHierarchy.toArray(new SupportedHierarchy[supportedHierarchy.size()]));
			mappings.setSupportedLanguage(supportedLanguages.toArray(new SupportedLanguage[supportedLanguages.size()]));
			mappings.setSupportedNamespace(supportedNameSpaces.toArray(new SupportedNamespace[supportedNameSpaces.size()]));
			mappings.setSupportedProperty(supportedProperties.toArray(new SupportedProperty[supportedProperties.size()]));
			mappings.setSupportedPropertyLink(supportedPropertyLinks.toArray(new SupportedPropertyLink[supportedPropertyLinks.size()]));
			mappings.setSupportedPropertyQualifier(supportedPropertyQualifiers.toArray(new SupportedPropertyQualifier[supportedPropertyQualifiers.size()]));
			mappings.setSupportedPropertyQualifierType(supportedPropertyQualifierTypes.toArray(new SupportedPropertyQualifierType[supportedPropertyQualifierTypes.size()]));
			mappings.setSupportedPropertyType(supportedPropertyTypes.toArray(new SupportedPropertyType[supportedPropertyTypes.size()]));
			mappings.setSupportedRepresentationalForm(supportedRepresentationalForms.toArray(new SupportedRepresentationalForm[supportedRepresentationalForms.size()]));
			mappings.setSupportedSortOrder(supportedSortOrders.toArray(new SupportedSortOrder[supportedSortOrders.size()]));
			mappings.setSupportedSource(supportedSources.toArray(new SupportedSource[supportedSources.size()]));
			mappings.setSupportedSourceRole(supportedSourceRoles.toArray(new SupportedSourceRole[supportedSourceRoles.size()]));           
			mappings.setSupportedStatus(supportedConceptStatus.toArray(new SupportedStatus[supportedConceptStatus.size()]));

			cs.setMappings(mappings);

			Properties codingSchemeProperties = getCodingSchemeProperties(internalCodingSchemeName, internalVersionString);
			cs.setProperties(codingSchemeProperties);

			cs.setRelations(relations.toArray(new Relations[relations.size()]));

			return cs;
		} catch (MissingResourceException e) {
			throw e;
		} catch (Exception e) {
			throw new UnexpectedInternalError("There was an unexpected internal error.", e);
		}

	}

	/**
	 * Builds the coded entry.
	 * 
	 * @param internalCodingSchemeName the internal coding scheme name
	 * @param internalVersionString the internal version string
	 * @param code the code
	 * @param namespace the namespace
	 * @param restrictToProperties the restrict to properties
	 * @param restrictToPropertyTypes the restrict to property types
	 * 
	 * @return the entity
	 * 
	 * @throws UnexpectedInternalError the unexpected internal error
	 * @throws MissingResourceException the missing resource exception
	 */
	public Entity buildCodedEntry(String internalCodingSchemeName, String internalVersionString,
			String code, String namespace, LocalNameList restrictToProperties, PropertyType[] restrictToPropertyTypes)
	throws UnexpectedInternalError, MissingResourceException {

		try {   
			Entity concept = new Entity();
			concept.setEntityCode(code);

			SQLInterface si = resourceManager.getSQLInterface(internalCodingSchemeName,
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
						String pts = DaoUtility.propertyTypeToStringMap.get(restrictToPropertyTypes[j]);
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
	 * 
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

	/**
	 * Builds the concept entity description.
	 * 
	 * @param internalCodingSchemeName the internal coding scheme name
	 * @param internalVersionString the internal version string
	 * @param conceptCode the concept code
	 * 
	 * @return the entity description
	 * 
	 * @throws MissingResourceException the missing resource exception
	 * @throws UnexpectedInternalError the unexpected internal error
	 */
	@Deprecated
	public EntityDescription buildConceptEntityDescription(String internalCodingSchemeName,
			String internalVersionString, String conceptCode) throws MissingResourceException, UnexpectedInternalError {
		EntityDescription result = new EntityDescription();
		SQLInterface si = resourceManager.getSQLInterface(internalCodingSchemeName, internalVersionString);
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

	/**
	 * Gets the coding scheme copyright.
	 * 
	 * @param internalCodingSchemeName the internal coding scheme name
	 * @param internalVersionString the internal version string
	 * 
	 * @return the coding scheme copyright
	 * 
	 * @throws MissingResourceException the missing resource exception
	 * @throws UnexpectedInternalError the unexpected internal error
	 */
	public String getCodingSchemeCopyright(String internalCodingSchemeName, String internalVersionString)
	throws MissingResourceException, UnexpectedInternalError {

		String copyRight = "MISSING";
		SQLInterface si = resourceManager.getSQLInterface(internalCodingSchemeName, internalVersionString);

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
	 * 
	 * @return The fully populated Properties object.
	 * 
	 * @throws MissingResourceException the missing resource exception
	 * @throws SQLException the SQL exception
	 */
	private Properties getCodingSchemeProperties(String internalCodingSchemeName, String internalVersionString) throws MissingResourceException, SQLException
	{
		SQLInterface si = resourceManager.getSQLInterface(internalCodingSchemeName, internalVersionString);
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

	/**
	 * Validate language.
	 * 
	 * @param internalCodingSchemeName the internal coding scheme name
	 * @param internalVersionString the internal version string
	 * @param language the language
	 * 
	 * @return true, if successful
	 * 
	 * @throws UnexpectedInternalError the unexpected internal error
	 * @throws MissingResourceException the missing resource exception
	 * @throws LBParameterException the LB parameter exception
	 */
	public boolean validateLanguage(String internalCodingSchemeName, String internalVersionString,
			String language) throws UnexpectedInternalError, MissingResourceException, LBParameterException {
		return validateSupported(internalCodingSchemeName, internalVersionString,
				SQLTableConstants.TBLCOLVAL_SUPPTAG_LANGUAGE, language);
	}

	/**
	 * Validate source.
	 * 
	 * @param internalCodingSchemeName the internal coding scheme name
	 * @param internalVersionString the internal version string
	 * @param source the source
	 * 
	 * @return true, if successful
	 * 
	 * @throws UnexpectedInternalError the unexpected internal error
	 * @throws MissingResourceException the missing resource exception
	 * @throws LBParameterException the LB parameter exception
	 */
	public boolean validateSource(String internalCodingSchemeName, String internalVersionString, String source)
	throws UnexpectedInternalError, MissingResourceException, LBParameterException {
		return validateSupported(internalCodingSchemeName, internalVersionString,
				SQLTableConstants.TBLCOLVAL_SUPPTAG_SOURCE, source);
	}

	/**
	 * Validate context.
	 * 
	 * @param internalCodingSchemeName the internal coding scheme name
	 * @param internalVersionString the internal version string
	 * @param context the context
	 * 
	 * @return true, if successful
	 * 
	 * @throws UnexpectedInternalError the unexpected internal error
	 * @throws MissingResourceException the missing resource exception
	 * @throws LBParameterException the LB parameter exception
	 */
	public boolean validateContext(String internalCodingSchemeName, String internalVersionString, String context)
	throws UnexpectedInternalError, MissingResourceException, LBParameterException {
		return validateSupported(internalCodingSchemeName, internalVersionString,
				SQLTableConstants.TBLCOLVAL_SUPPTAG_CONTEXT, context);
	}

	/**
	 * Validate property qualifier.
	 * 
	 * @param internalCodingSchemeName the internal coding scheme name
	 * @param internalVersionString the internal version string
	 * @param propertyQualifier the property qualifier
	 * 
	 * @return true, if successful
	 * 
	 * @throws UnexpectedInternalError the unexpected internal error
	 * @throws MissingResourceException the missing resource exception
	 * @throws LBParameterException the LB parameter exception
	 */
	public boolean validatePropertyQualifier(String internalCodingSchemeName, String internalVersionString,
			String propertyQualifier) throws UnexpectedInternalError, MissingResourceException, LBParameterException {
		return validateSupported(internalCodingSchemeName, internalVersionString,
				SQLTableConstants.TBLCOLVAL_SUPPTAG_PROPERTYQUALIFIER, propertyQualifier);
	}

	/**
	 * Validate property.
	 * 
	 * @param internalCodingSchemeName the internal coding scheme name
	 * @param internalVersionString the internal version string
	 * @param property the property
	 * 
	 * @return true, if successful
	 * 
	 * @throws UnexpectedInternalError the unexpected internal error
	 * @throws MissingResourceException the missing resource exception
	 * @throws LBParameterException the LB parameter exception
	 */
	public boolean validateProperty(String internalCodingSchemeName, String internalVersionString,
			String property) throws UnexpectedInternalError, MissingResourceException, LBParameterException {
		return validateSupported(internalCodingSchemeName, internalVersionString,
				SQLTableConstants.TBLCOLVAL_SUPPTAG_PROPERTY, property);
	}

	/**
	 * Validate supported.
	 * 
	 * @param internalCodingSchemeName the internal coding scheme name
	 * @param internalVersionString the internal version string
	 * @param supportedName the supported name
	 * @param supportedValue the supported value
	 * 
	 * @return true, if successful
	 * 
	 * @throws UnexpectedInternalError the unexpected internal error
	 * @throws MissingResourceException the missing resource exception
	 * @throws LBParameterException the LB parameter exception
	 */
	private boolean validateSupported(String internalCodingSchemeName, String internalVersionString,
			String supportedName, String supportedValue) throws UnexpectedInternalError, MissingResourceException,
			LBParameterException {
		if (supportedValue == null || supportedValue.length() == 0) {
			throw new LBParameterException("The parameter is required", "property");
		}

		// first, check the cache.
		String key = "valProp" + internalCodingSchemeName + ":" + internalVersionString + ":" + supportedName + ":"
		+ supportedValue;

		Map cache = resourceManager.getCache();

		Boolean value = (Boolean) cache.get(key);
		if (value != null) {
			if (value.booleanValue()) {
				return true;
			} else {
				throw new LBParameterException("The value supplied for the parameter is invalid.", supportedName,
						supportedValue);
			}
		}

		SQLInterface si = resourceManager.getSQLInterface(internalCodingSchemeName, internalVersionString);
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

	/**
	 * Gets the native relations.
	 * 
	 * @param internalCodingScheme the internal coding scheme
	 * @param internalVersionString the internal version string
	 * 
	 * @return the native relations
	 * 
	 * @throws MissingResourceException the missing resource exception
	 * @throws UnexpectedInternalError the unexpected internal error
	 * @throws LBParameterException the LB parameter exception
	 */
	public String[] getNativeRelations(String internalCodingScheme, String internalVersionString)
	throws MissingResourceException, UnexpectedInternalError, LBParameterException {
		// first, check the cache
		String key = "defaultRelation:" + internalCodingScheme + ":" + internalVersionString;

		Map cache = resourceManager.getCache();

		String[] value = (String[]) cache.get(key);

		if (value != null) {
			return value;
		}

		SQLInterface si = null;
		PreparedStatement getRelation = null;

		try {
			si = resourceManager.getSQLInterface(internalCodingScheme, internalVersionString);

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
	 * 
	 * @param internalCodingSchemeName the internal coding scheme name
	 * @param internalVersionString the internal version string
	 * 
	 * @return the URN for internal coding scheme name
	 * 
	 * @throws UnexpectedInternalError the unexpected internal error
	 * @throws MissingResourceException the missing resource exception
	 */
	public String getURNForInternalCodingSchemeName(String internalCodingSchemeName, String internalVersionString)
	throws UnexpectedInternalError, MissingResourceException {
		Map cache = resourceManager.getCache();
		String key = "codingSchemeToURN:" + internalCodingSchemeName + ":" + internalVersionString;
		String urn = (String) cache.get(key);

		if (urn != null) {
			return urn;
		}

		SQLInterface si = resourceManager.getSQLInterface(internalCodingSchemeName, internalVersionString);

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
	 * 
	 * @param supportedCodingScheme the supported coding scheme
	 * @param internalCodingSchemeName the internal coding scheme name
	 * @param internalVersionString the internal version string
	 * @param throwError the throw error
	 * 
	 * @return the URN for relationship coding scheme name
	 * 
	 * @throws MissingResourceException the missing resource exception
	 * @throws UnexpectedInternalError the unexpected internal error
	 */
	public String getURNForRelationshipCodingSchemeName(String supportedCodingScheme,
			String internalCodingSchemeName, String internalVersionString, boolean throwError)
	throws MissingResourceException, UnexpectedInternalError {
		Map cache = resourceManager.getCache();
		String key = "supportedCodingSchemeToURN:" + internalCodingSchemeName + ":" + internalVersionString + ":"
		+ supportedCodingScheme;
		String urn = (String) cache.get(key);

		if (urn != null) {
			return urn;
		}

		SQLInterface si = resourceManager.getSQLInterface(internalCodingSchemeName, internalVersionString);

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
	 * 
	 * @param associationName the association name
	 * @param internalCodingSchemeName the internal coding scheme name
	 * @param internalVersionString the internal version string
	 * 
	 * @return the URN for association name
	 * 
	 * @throws MissingResourceException the missing resource exception
	 * @throws UnexpectedInternalError the unexpected internal error
	 */
	public String getURNForAssociationName(String associationName, String internalCodingSchemeName,
			String internalVersionString) throws MissingResourceException, UnexpectedInternalError {
		Map cache = resourceManager.getCache();
		String key = "AssociationToURN:" + internalCodingSchemeName + ":" + internalVersionString + ":"
		+ associationName;
		String urn = (String) cache.get(key);

		if (urn != null) {
			return urn;
		}

		SQLInterface si = resourceManager.getSQLInterface(internalCodingSchemeName, internalVersionString);

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
	 * 
	 * @param urn the urn
	 * @param internalCodingSchemeName the internal coding scheme name
	 * @param internalVersionString the internal version string
	 * 
	 * @return the relationship coding scheme name for urn
	 * 
	 * @throws MissingResourceException the missing resource exception
	 * @throws UnexpectedInternalError the unexpected internal error
	 */
	public String getRelationshipCodingSchemeNameForURN(String urn, String internalCodingSchemeName,
			String internalVersionString) throws MissingResourceException, UnexpectedInternalError {
		Map cache = resourceManager.getCache();
		String key = "URNToSupportedCodingScheme:" + internalCodingSchemeName + ":" + internalVersionString + ":" + urn;
		String supportedCodingScheme = (String) cache.get(key);

		if (supportedCodingScheme != null) {
			return supportedCodingScheme;
		}

		SQLInterface si = resourceManager.getSQLInterface(internalCodingSchemeName, internalVersionString);

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
	 * 
	 * @param internalCodeSystemName the internal code system name
	 * @param internalVersionString the internal version string
	 * @param association the association
	 * @param associationURN the association urn
	 * 
	 * @return true, if checks if is association valid for code system
	 * 
	 * @throws MissingResourceException the missing resource exception
	 * @throws UnexpectedInternalError the unexpected internal error
	 */
	private boolean isAssociationValidForCodeSystem(String internalCodeSystemName, String internalVersionString,
			String association, String associationURN) throws MissingResourceException, UnexpectedInternalError {
		Map cache = resourceManager.getCache();
		String key = "validAssociationForCodeSystem:" + internalCodeSystemName + ":" + internalVersionString + ":"
		+ association + ":" + associationURN;
		Boolean valid = (Boolean) cache.get(key);

		if (valid != null && valid == true) {
			return valid.booleanValue();
		}

		SQLInterface si = resourceManager.getSQLInterface(internalCodeSystemName, internalVersionString);

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
							&& associationURN.equals(resourceManager.getURNForInternalCodingSchemeName(
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

	/**
	 * Gets the association reference.
	 * 
	 * @param associationName the association name
	 * @param internalCodingSchemeName the internal coding scheme name
	 * @param internalVersionString the internal version string
	 * 
	 * @return the association reference
	 * 
	 * @throws MissingResourceException the missing resource exception
	 * @throws UnexpectedInternalError the unexpected internal error
	 */
	public ConceptReference getAssociationReference(String associationName, String internalCodingSchemeName,
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
				niceName = resourceManager.getExternalCodingSchemeNameForUserCodingSchemeNameOrId(niceName,
						null);
			}
		}

		ConceptReference cr = new ConceptReference();
		cr.setCodingSchemeName(niceName);
		cr.setCode(associationName);
		return cr;
	}



	/**
	 * Maps from the give coding scheme identifier or namespace to
	 * the coding scheme name.
	 * <p>
	 * If working under the 2009 model, the namespace must be mapped to
	 * the coding scheme name through the SupportedNamespace metadata.
	 * Otherwise no mapping is required, return the original value.
	 * 
	 * @param si the si
	 * @param internalCodingSchemeName The scheme used to resolve supported namespace mappings.
	 * @param idOrNamespace The value to map.
	 * 
	 * @return String
	 */
	private String mapToCodingSchemeName(SQLInterface si, String internalCodingSchemeName, String idOrNamespace) {
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

	/**
	 * Checks if is association symmetric.
	 * 
	 * @param internalCodingSchemeName the internal coding scheme name
	 * @param internalVersionString the internal version string
	 * @param relationName the relation name
	 * @param association the association
	 * 
	 * @return true, if is association symmetric
	 * 
	 * @throws UnexpectedInternalError the unexpected internal error
	 * @throws MissingResourceException the missing resource exception
	 * @throws LBParameterException the LB parameter exception
	 */
	public boolean isAssociationSymmetric(String internalCodingSchemeName, String internalVersionString,
			String relationName, String association) throws UnexpectedInternalError, MissingResourceException,
			LBParameterException {
		if (relationName == null || relationName.length() == 0 || association == null || association.length() == 0) {
			throw new LBParameterException("The parameters are required", "association, relationName");
		}

		// first, check the cache.
		String key = "isSymmetric" + internalCodingSchemeName + ":" + internalVersionString + ":" + relationName + ":"
		+ association;

		Map cache = resourceManager.getCache();

		Boolean value = (Boolean) cache.get(key);
		if (value != null) {
			return value.booleanValue();
		}

		// not in the cache - go look it up.

		SQLInterface si = resourceManager.getSQLInterface(internalCodingSchemeName, internalVersionString);
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
	 * Checks if the EntityAssnsToEQuals multiAttributesKey column index is present.
	 * 
	 * @param si the si
	 * 
	 * @return if the multiAttributesKey column index is present
	 */
	protected static boolean isEntityAssnsToEQualsIndexPresent(SQLInterface si){
		return parseFloatFromTableVersion(si) >= 1.8f;
	}

	/**
	 * Checks if the current Association Table contains the EntryStateId column.
	 * 
	 * @param si the si
	 * 
	 * @return if the EntryStateId column is present.
	 */
	protected static boolean isEntryStateIdInAssociationTable(SQLInterface si){
		return parseFloatFromTableVersion(si) >= 1.8f;
	}

	/**
	 * Returns the float representation of the current table version.
	 * 
	 * @param si the si
	 * 
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
	 * @param value the value
	 * @param val1 the val1
	 * 
	 * @return The id to uniquely identify this source within an Entity
	 */
	protected static String createUniqueKeyForSource(String value, String val1){
		return value + val1;
	}

	/**
	 * Sets the resource manager.
	 * 
	 * @param resourceManager the new resource manager
	 */
	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}

	/**
	 * Gets the resource manager.
	 * 
	 * @return the resource manager
	 */
	public ResourceManager getResourceManager() {
		return resourceManager;
	}

	/**
	 * Sets the logger.
	 * 
	 * @param logger the new logger
	 */
	public void setLogger(LgLoggerIF logger) {
		this.logger = logger;
	}

	/**
	 * Gets the logger.
	 * 
	 * @return the logger
	 */
	public LgLoggerIF getLogger() {
		return logger;
	}
}