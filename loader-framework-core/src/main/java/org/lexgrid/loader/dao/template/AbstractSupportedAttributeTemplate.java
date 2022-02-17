
package org.lexgrid.loader.dao.template;

import java.util.List;

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
import org.LexGrid.naming.SupportedPropertyQualifier;
import org.LexGrid.naming.SupportedPropertyType;
import org.LexGrid.naming.SupportedRepresentationalForm;
import org.LexGrid.naming.SupportedSortOrder;
import org.LexGrid.naming.SupportedSource;
import org.LexGrid.naming.SupportedSourceRole;
import org.LexGrid.naming.SupportedStatus;
import org.LexGrid.naming.URIMap;
import org.lexgrid.loader.logging.LoggingBean;

/**
 * The Class AbstractSupportedAttributeTemplate.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractSupportedAttributeTemplate extends LoggingBean implements SupportedAttributeTemplate{
	
	private int idValueMaxSize = 200;
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.dao.template.SupportedAttributeTemplate#addSupportedAssociation(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void addSupportedAssociation(String codingSchemeName, String codingSchemeVersion, String localId, String uri,
			String content) {	
		SupportedAssociation sa = new SupportedAssociation();
		sa.setContent(content);
		sa.setLocalId(localId);
		sa.setUri(uri);
		this.insert(codingSchemeName, codingSchemeVersion, sa);
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.dao.template.SupportedAttributeTemplate#addSupportedAssociationQualifier(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void addSupportedAssociationQualifier(String codingSchemeName, String codingSchemeVersion, String localId, String uri,
			String content) {
		SupportedAssociationQualifier saq = new SupportedAssociationQualifier();
		saq.setContent(content);
		saq.setLocalId(localId);
		saq.setUri(uri);
		this.insert(codingSchemeName, codingSchemeVersion, saq);
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.dao.template.SupportedAttributeTemplate#addSupportedCodingScheme(java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	public void addSupportedCodingScheme(String codingSchemeName, String codingSchemeVersion, String localId, String uri,
			String content, boolean isImported) {
		SupportedCodingScheme scs = new SupportedCodingScheme();
		scs.setContent(content);
		scs.setIsImported(isImported);
		scs.setLocalId(localId);
		scs.setUri(uri);
		this.insert(codingSchemeName, codingSchemeVersion, scs);
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.dao.template.SupportedAttributeTemplate#addSupportedContainerName(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void addSupportedContainerName(String codingSchemeName, String codingSchemeVersion, String localId, String uri,
			String content) {
		SupportedContainerName scn = new SupportedContainerName();
		scn.setContent(content);
		scn.setLocalId(localId);
		scn.setUri(uri);
		this.insert(codingSchemeName, codingSchemeVersion, scn);
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.dao.template.SupportedAttributeTemplate#addSupportedContext(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void addSupportedContext(String codingSchemeName, String codingSchemeVersion, String localId, String uri, String content) {
		SupportedContext sc = new SupportedContext();
		sc.setContent(content);
		sc.setLocalId(localId);
		sc.setUri(uri);
		this.insert(codingSchemeName, codingSchemeVersion, sc);
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.dao.template.SupportedAttributeTemplate#addSupportedDataType(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void addSupportedDataType(String codingSchemeName, String codingSchemeVersion, String localId, String uri,
			String content) {
		SupportedDataType sdt = new SupportedDataType();
		sdt.setContent(content);
		sdt.setLocalId(localId);
		sdt.setUri(uri);
		this.insert(codingSchemeName, codingSchemeVersion, sdt);
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.dao.template.SupportedAttributeTemplate#addSupportedDegreeOfFidelity(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void addSupportedDegreeOfFidelity(String codingSchemeName, String codingSchemeVersion, String localId, String uri,
			String content) {
		SupportedDegreeOfFidelity sdf = new SupportedDegreeOfFidelity();
		sdf.setContent(content);
		sdf.setLocalId(localId);
		sdf.setUri(uri);
		this.insert(codingSchemeName, codingSchemeVersion, sdf);
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.dao.template.SupportedAttributeTemplate#addSupportedEntityType(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void addSupportedEntityType(String codingSchemeName, String codingSchemeVersion, String localId, String uri,
			String content) {
		SupportedEntityType set = new SupportedEntityType();
		set.setContent(content);
		set.setLocalId(localId);
		set.setUri(uri);
		this.insert(codingSchemeName, codingSchemeVersion, set);
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.dao.template.SupportedAttributeTemplate#addSupportedHierarchy(java.lang.String, java.lang.String, java.lang.String, java.util.List, boolean, java.lang.String)
	 */
	public void addSupportedHierarchy(String codingSchemeName, String codingSchemeVersion, String localId, String uri,
			List<String> associationNames,
			boolean isForwardNavigable, String rootCode) {
		SupportedHierarchy sh = new SupportedHierarchy();
		for(String name : associationNames){
			sh.addAssociationNames(name);
		}
		sh.setIsForwardNavigable(isForwardNavigable);
		sh.setLocalId(localId);
		sh.setRootCode(rootCode);
		sh.setUri(uri);
		this.insert(codingSchemeName, codingSchemeVersion, sh);
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.dao.template.SupportedAttributeTemplate#addSupportedLanguage(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void addSupportedLanguage(String codingSchemeName, String codingSchemeVersion, String localId, String uri,
			String content) {
		SupportedLanguage sl = new SupportedLanguage();
		sl.setContent(content);
		sl.setLocalId(localId);
		sl.setUri(uri);
		this.insert(codingSchemeName, codingSchemeVersion, sl);
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.dao.template.SupportedAttributeTemplate#addSupportedNamespace(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void addSupportedNamespace(String codingSchemeName, String codingSchemeVersion, String localId, String uri,
			String content, String equivalentCodingScheme) {
		SupportedNamespace sn = new SupportedNamespace();
		sn.setContent(content);
		sn.setEquivalentCodingScheme(equivalentCodingScheme);
		sn.setLocalId(localId);
		sn.setUri(uri);
		this.insert(codingSchemeName, codingSchemeVersion, sn);
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.dao.template.SupportedAttributeTemplate#addSupportedProperty(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void addSupportedProperty(String codingSchemeName, String codingSchemeVersion, String localId, String uri,
			String content) {
		SupportedProperty sp = new SupportedProperty();
		sp.setLocalId(localId);
		sp.setContent(content);
		sp.setUri(uri);
		this.insert(codingSchemeName, codingSchemeVersion, sp);
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.dao.template.SupportedAttributeTemplate#addSupportedPropertyQualifier(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void addSupportedPropertyQualifier(String codingSchemeName, String codingSchemeVersion, String localId, String uri,
			String content) {
		SupportedPropertyQualifier spq = new SupportedPropertyQualifier();
		spq.setContent(content);
		spq.setLocalId(localId);
		spq.setUri(uri);
		this.insert(codingSchemeName, codingSchemeVersion, spq);
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.dao.template.SupportedAttributeTemplate#addSupportedPropertyType(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void addSupportedPropertyType(String codingSchemeName, String codingSchemeVersion, String localId, String uri,
			String content) {
		SupportedPropertyType spt = new SupportedPropertyType();
		spt.setContent(content);
		spt.setLocalId(localId);
		spt.setUri(uri);
		this.insert(codingSchemeName, codingSchemeVersion, spt);
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.dao.template.SupportedAttributeTemplate#addSupportedRepresentationalForm(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void addSupportedRepresentationalForm(String codingSchemeName, String codingSchemeVersion, String localId, String uri,
			String content) {
		SupportedRepresentationalForm srf = new SupportedRepresentationalForm();
		srf.setContent(content);
		srf.setLocalId(localId);
		srf.setUri(uri);
		this.insert(codingSchemeName, codingSchemeVersion, srf);
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.dao.template.SupportedAttributeTemplate#addSupportedSortOrder(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void addSupportedSortOrder(String codingSchemeName, String codingSchemeVersion, String localId, String uri,
			String content) {
		SupportedSortOrder sso = new SupportedSortOrder();
		sso.setContent(content);
		sso.setLocalId(localId);
		sso.setUri(uri);
		this.insert(codingSchemeName, codingSchemeVersion, sso);
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.dao.template.SupportedAttributeTemplate#addSupportedSource(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void addSupportedSource(String codingSchemeName, String codingSchemeVersion, String localId, String uri, String content,
			String assemblyRule) {
		SupportedSource ss = new SupportedSource();
		ss.setAssemblyRule(assemblyRule);
		ss.setContent(content);
		ss.setLocalId(localId);
		ss.setUri(uri);
		this.insert(codingSchemeName, codingSchemeVersion, ss);
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.dao.template.SupportedAttributeTemplate#addSupportedSourceRole(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void addSupportedSourceRole(String codingSchemeName, String codingSchemeVersion, String localId, String uri,
			String content) {
		SupportedSourceRole ssr = new SupportedSourceRole();
		ssr.setContent(content);
		ssr.setLocalId(localId);
		ssr.setUri(uri);
		this.insert(codingSchemeName, codingSchemeVersion, ssr);
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.dao.template.SupportedAttributeTemplate#addSupportedStatus(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void addSupportedStatus(String codingSchemeName, String codingSchemeVersion, String localId, String uri, String content) {
		SupportedStatus ss = new SupportedStatus();
		ss.setContent(content);
		ss.setLocalId(localId);
		ss.setUri(uri);
		this.insert(codingSchemeName, codingSchemeVersion, ss);
	}
	
	/**
	 * Insert.
	 * 
	 * @param attrib the attrib
	 */
	protected abstract void insert(String codingSchemeName, String codingSchemeVersion, URIMap attrib);

	/**
	 * Change null to blank string.
	 * 
	 * @param value the value
	 * 
	 * @return the string
	 */
	private String changeNullToBlankString(String value){
		if(value == null){
			return " ";
		} else {
			return value;
		}
	}
	
	private String truncateIfNecessary(String value, int maxLength){
		if(value.length() > maxLength){
			getLogger().info("Truncating: " + value);
			return value.substring(0, maxLength);
		} else {
			return value;
		}
	}

	/**
	 * List to csv.
	 * 
	 * @param list the list
	 * 
	 * @return the string
	 */
	private String listToCSV(List<String> list){
		String returnString = null;
		for(String value : list){
			if(returnString == null){
				returnString = value;
			} else {
				returnString = returnString + "," + value;
			}
		}
		return returnString;
	}

	public int getIdValueMaxSize() {
		return idValueMaxSize;
	}

	public void setIdValueMaxSize(int idValueMaxSize) {
		this.idValueMaxSize = idValueMaxSize;
	}	
}