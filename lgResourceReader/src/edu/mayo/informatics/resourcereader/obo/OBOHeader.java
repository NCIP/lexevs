
package edu.mayo.informatics.resourcereader.obo;

import java.net.URI;
import java.util.Vector;

import org.LexGrid.LexBIG.Utility.logging.CachingMessageDirectorIF;

import edu.mayo.informatics.resourcereader.core.StringUtils;
import edu.mayo.informatics.resourcereader.core.IF.ResourceHeader;
import edu.mayo.informatics.resourcereader.core.IF.ResourceType;

/**
 * The class is used to store the header information found in a OBO version 1.2
 * format file.
 * 
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class OBOHeader extends OBO implements ResourceHeader {
    private String formatVersion = null;
    private String ontologyVersion = null;
    private String updateDate = null;
    private String savedBy = null;
    private String remarks = "";
    private String defaultNameSpace;
    private String defaultRelationshipIDPrefix = null;
    private Vector<URI> importedOntologies = new Vector<URI>();
    private Vector<String> supportedSubsets = new Vector<String>();
    private Vector<String> supportedSynonyms = new Vector<String>();
    private Vector<String> idSpaceMappings = new Vector<String>();
    private Vector<String> supportedIdMappings = new Vector<String>();
    private boolean isHeaderFilled = false;

    public OBOHeader(CachingMessageDirectorIF rLogger) {
        super(rLogger);
    }

    /**
     * @return Returns the idSpaceMappings.
     */
    public Vector<String> getIdSpaceMappings() {
        return idSpaceMappings;
    }

    /**
     * @param idSpaceMappings
     *            The supportedIdSpaceMappings to set.
     */
    public void setSupportedIdSpaceMappings(Vector<String> idSpaceMappings) {
        this.idSpaceMappings = idSpaceMappings;
    }

    /**
     * @return Returns the formatVersion.
     */
    public String getFormatVersion() {
        return formatVersion;
    }

    /**
     * @param formatVersion
     *            The formatVersion to set.
     */
    public void setFormatVersion(String formatVersion) {
        this.formatVersion = formatVersion;
    }

    /**
     * @return Returns the ontologyVersion.
     */
    public String getOntologyVersion() {
        return ontologyVersion;
    }

    /**
     * @param ontologyVersion
     *            The ontologyVersion to set.
     */
    public void setOntologyVersion(String ontologyVersion) {
        this.ontologyVersion = ontologyVersion;
    }

    /**
     * @return Returns the savedBy.
     */
    public String getSavedBy() {
        return savedBy;
    }

    /**
     * @param savedBy
     *            The savedBy to set.
     */
    public void setSavedBy(String savedBy) {
        this.savedBy = savedBy;
    }

    /**
     * @return Returns the supportedSubsets.
     */
    public Vector<String> getSupportedSubsets() {
        return supportedSubsets;
    }

    /**
     * @param supportedSubsets
     *            The supportedSubsets to set.
     */
    public void setSupportedSubsets(Vector<String> supportedSubsets) {
        this.supportedSubsets = supportedSubsets;
    }

    /**
     * @return Returns the supportedSynonyms.
     */
    public Vector<String> getSupportedSynonyms() {
        return supportedSynonyms;
    }

    /**
     * @param supportedSynonyms
     *            The supportedSynonyms to set.
     */
    public void setSupportedSynonyms(Vector<String> supportedSynonyms) {
        this.supportedSynonyms = supportedSynonyms;
    }

    /**
     * @return Returns the updateDate.
     */
    public String getUpdateDate() {
        return updateDate;
    }

    /**
     * @param updateDate
     *            The updateDate to set.
     */
    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public ResourceType getResourceType() {
        return OBOResourceType.getResourceType();
    }

    /**
     * @return Returns the importedOntologies.
     */
    public Vector<URI> getImportedOntologies() {
        return importedOntologies;
    }

    /**
     * @param importedOntologies
     *            The importedOntologies to set.
     */
    public void setImportedOntologies(Vector<URI> importedOntologies) {
        this.importedOntologies = importedOntologies;
    }

    /**
     * @return Returns the defaultRelationshipIDPrefix.
     */
    public String getDefaultRelationshipIDPrefix() {
        return defaultRelationshipIDPrefix;
    }

    /**
     * @param defaultRelationshipIDPrefix
     *            The defaultRelationshipIDPrefix to set.
     */
    public void setDefaultRelationshipIDPrefix(String defaultRelationshipIDPrefix) {
        this.defaultRelationshipIDPrefix = defaultRelationshipIDPrefix;
    }

    /**
     * @return Returns the supportedIdMappings.
     */
    public Vector<String> getSupportedIdMappings() {
        return supportedIdMappings;
    }

    /**
     * @param supportedIdMappings
     *            The supportedIdMappings to set.
     */
    public void setSupportedIdMappings(Vector<String> supportedIdMappings) {
        this.supportedIdMappings = supportedIdMappings;
    }

    /**
     * @return Returns the remarks.
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks
     *            The remarks to set.
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * @return Returns the isHeaderFilled.
     */
    public boolean isHeaderFilled() {
        return isHeaderFilled;
    }

    /**
     * @param isHeaderFilled
     *            The isHeaderFilled to set.
     */
    public void setHeaderFilled(boolean isHeaderFilled) {
        this.isHeaderFilled = isHeaderFilled;
    }

    public boolean containsSubsetName(String subsetName) {
        if ((!StringUtils.isNull(subsetName)) && (supportedSubsets != null) && (supportedSubsets.size() > 0)) {
            try {
                for (int i = 0; i < supportedSubsets.size(); i++) {
                    if (subsetName.equals(((((String) supportedSubsets.elementAt(i)).split(" "))[0]).trim()))
                        return true;
                }
            } catch (Exception e) {

            }
        }

        return false;
    }

    private String para(String str) {
        if (str != null)
            return "[" + str + "]\n";

        return "";
    }

    private String printVector(Vector v) {
        String val = null;
        try {
            if (v != null) {
                for (int i = 0; i < v.size(); i++) {
                    if (val == null)
                        val = "{" + v.elementAt(i).toString() + "}";
                    else
                        val += "{" + v.elementAt(i).toString() + "}";
                }
            }
        } catch (Exception e) {
        }

        return val;
    }

    public String toString() {
        return para(OBOConstants.TAG_FORMAT_VERSION + formatVersion) + para(OBOConstants.TAG_DATE + updateDate)
                + para(OBOConstants.TAG_SAVED_BY + savedBy) + para(OBOConstants.TAG_DATAVERSION + ontologyVersion)
                + para(OBOConstants.TAG_DEFAULTRELATIONSHIPID + defaultRelationshipIDPrefix)
                + para(OBOConstants.TAG_REMARK + remarks) + para("isHeaderFilled:" + isHeaderFilled)
                + para(OBOConstants.TAG_IMPORT + printVector(importedOntologies))
                + para(OBOConstants.TAG_SYNONYMTYPEDEF + printVector(supportedSynonyms))
                + para(OBOConstants.TAG_SUBSETDEF + printVector(supportedSubsets))
                + para(OBOConstants.TAG_IDSPACE + printVector(idSpaceMappings))
                + para(OBOConstants.TAG_IDMAPPING + printVector(supportedIdMappings)) + "\n";
    }

    public String getDefaultNameSpace() {
        return defaultNameSpace;
    }

    public void setDefaultNameSpace(String defaultNameSpace) {
        this.defaultNameSpace = defaultNameSpace;
    }
}