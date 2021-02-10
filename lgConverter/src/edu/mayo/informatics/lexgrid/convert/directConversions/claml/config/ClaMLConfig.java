
package edu.mayo.informatics.lexgrid.convert.directConversions.claml.config;

import java.util.Arrays;
import java.util.List;

import org.LexGrid.LexBIG.Preferences.loader.LoadPreferences.LoaderPreferences;

public class ClaMLConfig {

	private String clamlPackageName = "org.LexGrid.LexBIG.claml";
	private String metaLangName = "lang";
	private String clamlDefaultLang = "en";
	
	private String subclassAssoc = "hasSubClass";
	private String superclassAssoc = "hasSuperClass";
	private String modifiesAssoc = "modifedBy";
	private String includesAssoc = "inclusion";
	private String excludesAssoc = "exclusion";
	private String subcodesAssoc = "subCodedBy";
	
	private String subclassAssocReverse = "subClassOf";
	private String superclassAssocReverse = "superClassOf";
	private String modifiesAssocReverse = "modifies";
	private String includesAssocReverse = "isIncludedBy";
	private String excludesAssocReverse = "isExcludedBy";
	private String subcodesAssocReverse = "subCodes";
	
	private String conceptDC = "concepts";
	private List<String> definitions = Arrays.asList("definition");
	private List<String> presentations = Arrays.asList("preferred", "preferredLong", "preferredShort", "text");
	private String preferrredPresentation = "preferred";
	private String classAssociationQualifier = "classQualifier";
	private String usageAssociationQualifier = "usageQualifier";
	private String modifiedByProperty = "modifier";
	
	
	public ClaMLConfig(LoaderPreferences loaderPrefs){
		//Handle Loader Preferences here.
	}
	
	public ClaMLConfig(){
		//Default Constructor if no Loader Preferences.
	}
	
	public String getClamlPackageName() {
		return clamlPackageName;
	}
	public void setClamlPackageName(String clamlPackageName) {
		this.clamlPackageName = clamlPackageName;
	}
	public String getMetaLangName() {
		return metaLangName;
	}
	public void setMetaLangName(String metaLangName) {
		this.metaLangName = metaLangName;
	}
	public String getClamlDefaultLang() {
		return clamlDefaultLang;
	}
	public void setClamlDefaultLang(String clamlDefaultLang) {
		this.clamlDefaultLang = clamlDefaultLang;
	}
	public String getSubclassAssoc() {
		return subclassAssoc;
	}
	public void setSubclassAssoc(String subclassAssoc) {
		this.subclassAssoc = subclassAssoc;
	}
	
	public String getModifiesAssoc() {
		return modifiesAssoc;
	}
	public void setModifiedsAssoc(String modifiesAssoc) {
		this.modifiesAssoc = modifiesAssoc;
	}
	public String getIncludesAssoc() {
		return includesAssoc;
	}
	public void setIncludesAssoc(String includesAssoc) {
		this.includesAssoc = includesAssoc;
	}
	public String getExcludesAssoc() {
		return excludesAssoc;
	}
	public void setExcludesAssoc(String excludesAssoc) {
		this.excludesAssoc = excludesAssoc;
	}
	public String getConceptDC() {
		return conceptDC;
	}
	public void setConceptDC(String conceptDC) {
		this.conceptDC = conceptDC;
	}
	public List<String> getDefinitions() {
		return definitions;
	}
	public void setDefinitions(List<String> definitions) {
		this.definitions = definitions;
	}
	public List<String> getPresentations() {
		return presentations;
	}
	public void setPresentations(List<String> presentations) {
		this.presentations = presentations;
	}
	public String getPreferrredPresentation() {
		return preferrredPresentation;
	}
	public void setPreferrredPresentation(String preferrredPresentation) {
		this.preferrredPresentation = preferrredPresentation;
	}

	public String getClassAssociationQualifier() {
		return classAssociationQualifier;
	}

	public void setClassAssociationQualifier(String classAssociationQualifier) {
		this.classAssociationQualifier = classAssociationQualifier;
	}

	public String getUsageAssociationQualifier() {
		return usageAssociationQualifier;
	}

	public void setUsageAssociationQualifier(String usageAssociationQualifier) {
		this.usageAssociationQualifier = usageAssociationQualifier;
	}

	public String getModifiedByProperty() {
		return modifiedByProperty;
	}

	public void setModifiedByProperty(String modifiedByProperty) {
		this.modifiedByProperty = modifiedByProperty;
	}

	public String getSubcodesAssoc() {
		return subcodesAssoc;
	}

	public void setSubcodesAssoc(String subcodesAssoc) {
		this.subcodesAssoc = subcodesAssoc;
	}

	public String getSubclassAssocReverse() {
		return subclassAssocReverse;
	}

	public void setSubclassAssocReverse(String subclassAssocReverse) {
		this.subclassAssocReverse = subclassAssocReverse;
	}

	public String getModifiesAssocReverse() {
		return modifiesAssocReverse;
	}

	public void setModifiesAssocReverse(String modifiesAssocReverse) {
		this.modifiesAssocReverse = modifiesAssocReverse;
	}

	public String getIncludesAssocReverse() {
		return includesAssocReverse;
	}

	public void setIncludesAssocReverse(String includesAssocReverse) {
		this.includesAssocReverse = includesAssocReverse;
	}

	public String getExcludesAssocReverse() {
		return excludesAssocReverse;
	}

	public void setExcludesAssocReverse(String excludesAssocReverse) {
		this.excludesAssocReverse = excludesAssocReverse;
	}

	public String getSubcodesAssocReverse() {
		return subcodesAssocReverse;
	}

	public void setSubcodesAssocReverse(String subcodesAssocReverse) {
		this.subcodesAssocReverse = subcodesAssocReverse;
	}

	public void setModifiesAssoc(String modifiesAssoc) {
		this.modifiesAssoc = modifiesAssoc;
	}

	public String getSuperclassAssoc() {
		return superclassAssoc;
	}

	public void setSuperclassAssoc(String superclassAssoc) {
		this.superclassAssoc = superclassAssoc;
	}

	public String getSuperclassAssocReverse() {
		return superclassAssocReverse;
	}

	public void setSuperclassAssocReverse(String superclassAssocReverse) {
		this.superclassAssocReverse = superclassAssocReverse;
	}
	
	
}