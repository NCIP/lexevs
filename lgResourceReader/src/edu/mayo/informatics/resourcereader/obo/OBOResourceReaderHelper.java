
package edu.mayo.informatics.resourcereader.obo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.LexGrid.LexBIG.Utility.logging.CachingMessageDirectorIF;

import edu.mayo.informatics.resourcereader.core.StringUtils;
import edu.mayo.informatics.resourcereader.core.IF.ResourceException;
import edu.mayo.informatics.resourcereader.core.IF.ResourceHeader;

/**
 * This class reads the OBO file and stores the information into a
 * ResourceHeader and ResourceContents object.
 * 
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @author <A HREF="mailto:kanjamala.pradip@mayo.edu">Pradip Kanjamala</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class OBOResourceReaderHelper extends OBO {
    private BufferedReader inputReader = null;

    public boolean showDeprecatedMessage = true;

    public OBOResourceReaderHelper(CachingMessageDirectorIF rLogger) {
        super(rLogger);
    }

    public OBOResourceReaderHelper(URI inputFile, CachingMessageDirectorIF rLogger) {
        super(rLogger);
        setStream(inputFile);
    }

    public OBOResourceReaderHelper(BufferedReader inputFileReader, CachingMessageDirectorIF rLogger) {
        super(rLogger);
        setStream(inputFileReader);
    }

    public OBOResourceReaderHelper(URL inputFileURL, CachingMessageDirectorIF rLogger) {
        super(rLogger);
        setStream(inputFileURL);
    }

    public void setStream(URI inputFile) {
        try {
            if (inputFile.getScheme().equals("file")) {
                this.inputReader = new BufferedReader(new FileReader(new File(inputFile)));
            } else {
                this.inputReader = new BufferedReader(new InputStreamReader(inputFile.toURL().openConnection()
                        .getInputStream()));
            }

        } catch (Exception e) {

            logger.fatal("Missing or bad OBO Input File Name." + inputFile + "\n", e);

        }
    }

    public void setStream(BufferedReader inputFileReader) {
        try {
            if (inputFileReader != null) {
                this.inputReader = inputFileReader;
            } else
                throw new Exception();
        } catch (Exception e) {
            logger.fatal("OBO File Reader is null.", e);

        }
    }

    public void setStream(URL inputFileURL) {
        try {
            if (inputFileURL != null) {

                this.inputReader = new BufferedReader(new InputStreamReader(inputFileURL.openStream()));

            } else
                throw new Exception("InputFileURL is null");
        } catch (Exception e) {
            logger.fatal("Error in opening OBO File Reader URL.\n", e);
        }
    }

    public BufferedReader getStream() {
        return this.inputReader;
    }

    // In Obo, lines ending with a \ followed by a newline character is
    // considered to line that continues on the next line. We need to read
    // all of it as one line.
    private String readOneOboLine(BufferedReader reader) throws Exception {
        String tmp;
        String oboLine = "";
        while ((tmp = reader.readLine()) != null) {
            // Check if the line ends with \ newline.
            if (Pattern.matches("(.*)(\\\\)(\\s)*$", tmp)) {
                // System.out.println("match found in"+tmp);
                tmp = tmp.replaceAll("(\\\\)(\\s)*$", " ");
                oboLine += tmp;
            } else {
                oboLine += tmp;
                break;
            }

        }

        if (oboLine.length() > 0) {
            return oboLine;
        } else {
            return tmp;
        }

    }

    public ResourceHeader readHeader() {
        OBOHeader oRH = null;
        if (inputReader == null) {
            logger.error("Can not readHeader() as inputReader is null");
            return oRH;
        }

        try {
            oRH = new OBOHeader(logger);
            String line = null;
            boolean toReadNextLine = true;
            while (((line = this.readOneOboLine(inputReader)) != null) && (toReadNextLine)) {
                String currentTag = "";

                // System.out.println("line=" + line);

                // If we encountered a stanza, we can break as we are
                // done reading the header
                if ((line.startsWith(OBOConstants.TAG_TERM)) || (line.startsWith(OBOConstants.TAG_TYPEDEF))
                        || (line.startsWith(OBOConstants.TAG_INSTANCE))) {
                    // if version has not been assigned or found in the OBO then
                    // assign it
                    // "UNASSIGNED"

                    if (oRH.getOntologyVersion() == null)
                        oRH.setOntologyVersion(OBOConstants.UNASSIGNED_LABEL);

                    break;
                }

                if (((!StringUtils.isNull(line)) && (!line.startsWith("!"))) && (!OBOConstants.isIgnoredTag(line))
                        && (OBOConstants.isHeaderTag(line))) {
                    // format-version:
                    if (line.startsWith(OBOConstants.TAG_FORMAT_VERSION)) {
                        currentTag = OBOConstants.TAG_FORMAT_VERSION;
                        String formatVersion = parseAsSimpleKeyValue(line, currentTag);
                        oRH.setFormatVersion(formatVersion);

                        // format version is the only required
                        // attribute in the header
                        // so as soon as we get a not-null value the
                        // header requirements
                        // are met to fill details. Other properties
                        // are optional
                        if (!StringUtils.isNull(formatVersion))
                            oRH.setHeaderFilled(true);
                        continue;
                    }

                    // data-version: or version:
                    if (line.startsWith(OBOConstants.TAG_DATAVERSION) || line.startsWith(OBOConstants.TAG_VERSION)) {
                        if (line.startsWith(OBOConstants.TAG_DATAVERSION))
                            currentTag = OBOConstants.TAG_DATAVERSION;
                        else {
                            currentTag = OBOConstants.TAG_VERSION;
                            showDepricatedMessageForTag(OBOConstants.TAG_VERSION, OBOConstants.TAG_DATAVERSION);
                        }
                        oRH.setOntologyVersion(parseAsSimpleKeyValueWithLimit(line, currentTag, 50));
                        continue;
                    }

                    // saved-by:
                    if (line.startsWith(OBOConstants.TAG_SAVED_BY)) {
                        currentTag = OBOConstants.TAG_SAVED_BY;
                        oRH.setSavedBy(parseAsSimpleKeyValue(line, currentTag));
                        continue;
                    }

                    // auto-generated-by:
                    if (line.startsWith(OBOConstants.TAG_AUTO_GENERATED_BY)) {
                        currentTag = OBOConstants.TAG_AUTO_GENERATED_BY;
                        showIgnoreValueMessage(currentTag);
                        continue;
                    }

                    // subsetdef:
                    if (line.startsWith(OBOConstants.TAG_SUBSETDEF)) {
                        currentTag = OBOConstants.TAG_SUBSETDEF;
                        String subsetDef = parseAsSimpleKeyValue(line, currentTag);
                        if (subsetDef != null)
                            oRH.getSupportedSubsets().addElement(subsetDef);
                        continue;
                    }

                    // import: or typeref:
                    if (line.startsWith(OBOConstants.TAG_IMPORT) || line.startsWith(OBOConstants.TAG_TYPEREF)) {
                        if (line.startsWith(OBOConstants.TAG_IMPORT)) {
                            currentTag = OBOConstants.TAG_IMPORT;
                        } else {
                            currentTag = OBOConstants.TAG_TYPEREF;
                            showDepricatedMessageForTag(OBOConstants.TAG_TYPEREF, OBOConstants.TAG_IMPORT);
                        }

                        String importedOntology = parseAsSimpleKeyValue(line, currentTag);
                        if (!StringUtils.isNull(importedOntology))
                            oRH.getImportedOntologies().addElement(new URI(importedOntology));
                        continue;
                    }

                    // synonymtypedef:
                    if (line.startsWith(OBOConstants.TAG_SYNONYMTYPEDEF)) {
                        currentTag = OBOConstants.TAG_SYNONYMTYPEDEF;
                        String userDefinedSynonym = parseAsSimpleKeyValue(line, currentTag);

                        if (!StringUtils.isNull(userDefinedSynonym))
                            oRH.getSupportedSynonyms().addElement(userDefinedSynonym);
                        continue;
                    }

                    // idspace:
                    if (line.startsWith(OBOConstants.TAG_IDSPACE)) {
                        currentTag = OBOConstants.TAG_IDSPACE;
                        String idSpaceMap = parseAsSimpleKeyValue(line, currentTag);

                        if (!StringUtils.isNull(idSpaceMap))
                            oRH.getIdSpaceMappings().addElement(idSpaceMap);
                        continue;
                    }

                    // map the OBO 1.0 default-namespace to OBO 1.2 idspace:
                    if (line.startsWith(OBOConstants.TAG_DEFAULT_NS)) {
                        currentTag = OBOConstants.TAG_DEFAULT_NS;
                        String defaultNameSpace = parseAsSimpleKeyValue(line, currentTag);

                        if (!StringUtils.isNull(defaultNameSpace)) {
                            oRH.getIdSpaceMappings().addElement(defaultNameSpace);
                            oRH.setDefaultNameSpace(defaultNameSpace);

                        }
                        continue;
                    }

                    // default-relationship-id:
                    if (line.startsWith(OBOConstants.TAG_DEFAULTRELATIONSHIPID)) {
                        currentTag = OBOConstants.TAG_DEFAULTRELATIONSHIPID;
                        oRH.setDefaultRelationshipIDPrefix(parseAsSimpleKeyValue(line, currentTag));
                        continue;
                    }

                    // id-mapping:
                    if (line.startsWith(OBOConstants.TAG_IDMAPPING)) {
                        currentTag = OBOConstants.TAG_IDMAPPING;
                        String idMap = parseAsSimpleKeyValue(line, currentTag);

                        if (!StringUtils.isNull(idMap))
                            oRH.getSupportedIdMappings().addElement(idMap);
                        continue;
                    }

                    // remark:
                    if (line.startsWith(OBOConstants.TAG_REMARK)) {
                        currentTag = OBOConstants.TAG_REMARK;
                        if (StringUtils.isNull(oRH.getRemarks()))
                            oRH.setRemarks(parseAsSimpleKeyValue(line, currentTag));
                        else
                            oRH.setRemarks(oRH.getRemarks() + "\n" + parseAsSimpleKeyValue(line, currentTag));
                        continue;
                    }

                    // date:
                    if (line.startsWith(OBOConstants.TAG_DATE)) {
                        currentTag = OBOConstants.TAG_DATE;
                        oRH.setUpdateDate(parseAsSimpleKeyValue(line, currentTag));
                        continue;
                    }

                    String msg = "The following unknown tag was skipped in the resource header:" + line + "\n";
                    logger.warn(msg);

                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return oRH;
    }

    public void readAndMergeContents(OBOContents existingContents, OBOHeader header) {
        // System.out.println("Printing existing content"+existingContents);
        try {
            if (inputReader != null) {
                if (existingContents == null)
                    existingContents = new OBOContents(logger);

                OBOAbbreviations sAbbs = existingContents.getOBOAbbreviations();
                OBORelations rels = existingContents.getOBORelations();
                OBOTerms terms = existingContents.getOBOTerms();
                OBOInstances instances = existingContents.getOBOInstances();
                // Current abbreviation being parsed
                OBOAbbreviation currentAbb = null;
                // List of abbreviation (being processed) synonyms
                Vector<OBOAbbreviation> currentAbbSyn = null;

                OBORelation currentRel = null;
                OBOTerm currentTerm = null;
                OBOInstance currentInstance = null;

                String line = null;
                int ctx = -1;
                while ((line = this.readOneOboLine(inputReader)) != null) {
                    String currentTag = "";
                    try {
                        if (((!StringUtils.isNull(line)) && (!line.startsWith("!")))
                                && (!OBOConstants.isHeaderTag(line)) && (!OBOConstants.isIgnoredTag(line))) {
                            if (line.startsWith(OBOConstants.TAG_ABBREVIATION)) {
                                currentTag = OBOConstants.TAG_ABBREVIATION;
                                ctx = OBOConstants.ABBREVIATION_CTX;

                                if (currentAbb != null) {
                                    // If there is an abbrevitaion already being
                                    // worked on (previous) commit it, if
                                    // possible
                                    if (!StringUtils.isNull(currentAbb.getAbbreviation())) {
                                        if (StringUtils.isNull(currentAbb.genericURL))
                                            currentAbb.genericURL = currentAbb.abbreviation;

                                        sAbbs.addMember(currentAbb);
                                    }
                                }

                                if (currentAbbSyn != null) {
                                    for (int m = 0; m < currentAbbSyn.size(); m++) {
                                        Object aso = currentAbbSyn.elementAt(m);

                                        if ((aso != null) && (aso instanceof OBOAbbreviation)) {
                                            OBOAbbreviation abbrev = (OBOAbbreviation) aso;
                                            if (!StringUtils.isNull(abbrev.getAbbreviation())) {
                                                abbrev.genericURL = currentAbb.genericURL;

                                                if (StringUtils.isNull(abbrev.genericURL))
                                                    abbrev.genericURL = currentAbb.abbreviation;

                                                sAbbs.addMember(abbrev);
                                            }
                                        }
                                    }
                                }

                                currentAbb = new OBOAbbreviation(logger);
                                currentAbbSyn = new Vector<OBOAbbreviation>();

                                String aVal = parseAsSimpleKeyValue(line, currentTag);
                                if (!StringUtils.isNull(aVal))
                                    currentAbb.abbreviation = aVal;
                            } else if (line.startsWith(OBOConstants.TAG_GENERICURL)) {
                                currentTag = OBOConstants.TAG_GENERICURL;
                                ctx = OBOConstants.ABBREVIATION_CTX;

                                String aVal = parseAsSimpleKeyValue(line, currentTag);

                                if (!StringUtils.isNull(aVal)) {
                                    if (currentAbb == null)
                                        currentAbb = new OBOAbbreviation(logger);

                                    currentAbb.genericURL = aVal;
                                }
                            } else if ((line.startsWith(OBOConstants.TAG_SYNONYM))
                                    || (line.startsWith(OBOConstants.TAG_SYNONYM_EXACT))
                                    || (line.startsWith(OBOConstants.TAG_SYNONYM_BROAD))
                                    || (line.startsWith(OBOConstants.TAG_SYNONYM_NARROW))
                                    || (line.startsWith(OBOConstants.TAG_SYNONYM_RELATED))) {
                                if (line.startsWith(OBOConstants.TAG_SYNONYM))
                                    currentTag = OBOConstants.TAG_SYNONYM;
                                else if (line.startsWith(OBOConstants.TAG_SYNONYM_EXACT))
                                    currentTag = OBOConstants.TAG_SYNONYM_EXACT;
                                else if (line.startsWith(OBOConstants.TAG_SYNONYM_BROAD))
                                    currentTag = OBOConstants.TAG_SYNONYM_BROAD;
                                else if (line.startsWith(OBOConstants.TAG_SYNONYM_NARROW))
                                    currentTag = OBOConstants.TAG_SYNONYM_NARROW;
                                else if (line.startsWith(OBOConstants.TAG_SYNONYM_RELATED))
                                    currentTag = OBOConstants.TAG_SYNONYM_RELATED;

                                if (ctx == OBOConstants.ABBREVIATION_CTX) {
                                    String aVal = parseAsSimpleKeyValue(line, currentTag);

                                    if (!StringUtils.isNull(aVal)) {
                                        if ((!StringUtils.isNull(currentAbb.abbreviation))
                                                && (!currentAbb.abbreviation.equalsIgnoreCase(aVal))) {
                                            if (currentAbbSyn == null)
                                                currentAbbSyn = new Vector<OBOAbbreviation>();

                                            OBOAbbreviation currentAbbS = new OBOAbbreviation(logger);
                                            currentAbbS.abbreviation = aVal;
                                            currentAbbS.genericURL = currentAbb.genericURL;
                                            currentAbbSyn.add(currentAbbS);
                                        }
                                    }
                                } else {

                                    String synonymStr = parseAsSimpleKeyValue(line, currentTag);
                                    OBOSynonym syn = OBOSynonym.createSynonym(line, header.getSupportedSynonyms());

                                    if (!StringUtils.isNull(synonymStr)) {
                                        if ((OBOConstants.RELATION_CTX == ctx) && (currentRel != null))
                                            currentRel.synonyms.add(syn);
                                        else if ((OBOConstants.TERM_CTX == ctx) && (currentTerm != null))
                                            currentTerm.synonyms.add(syn);
                                        else if ((OBOConstants.INSTANCE_CTX == ctx) && (currentInstance != null))
                                            currentInstance.synonyms.add(syn);
                                    }
                                }
                            } else if (line.startsWith(OBOConstants.TAG_TERM)) {
                                currentTag = OBOConstants.TAG_TERM;
                                ctx = OBOConstants.TERM_CTX;

                                addTermToTerms(terms, currentTerm);
                                currentTerm = new OBOTerm(logger);

                                addRelationToRelations(rels, currentRel);
                                currentRel = null;

                                addInstanceToInstances(instances, currentInstance);
                                currentInstance = null;
                            } else if (line.startsWith(OBOConstants.TAG_TYPEDEF)) {
                                currentTag = OBOConstants.TAG_TYPEDEF;

                                addTermToTerms(terms, currentTerm);
                                currentTerm = null;

                                addRelationToRelations(rels, currentRel);
                                currentRel = new OBORelation(logger);

                                addInstanceToInstances(instances, currentInstance);
                                currentInstance = null;

                                ctx = OBOConstants.RELATION_CTX;
                            } else if (line.startsWith(OBOConstants.TAG_INSTANCE)) {
                                currentTag = OBOConstants.TAG_INSTANCE;
                                ctx = OBOConstants.INSTANCE_CTX;

                                addInstanceToInstances(instances, currentInstance);
                                currentInstance = new OBOInstance(logger);

                                addRelationToRelations(rels, currentRel);
                                currentRel = null;

                                addTermToTerms(terms, currentTerm);
                                currentTerm = null;
                            } else if (line.startsWith(OBOConstants.TAG_ID)) {
                                currentTag = OBOConstants.TAG_ID;
                                String aVal = parseAsSimpleKeyValue(line, currentTag);

                                if (!StringUtils.isNull(aVal)) {
                                    String prefix = null;
                                    
                                    if (aVal.indexOf(":") != -1) {
                                        String[] tokens = aVal.split(":");
                                        prefix = tokens[0];                                       
                                    }

                                    if (OBOConstants.TERM_CTX == ctx) {
                                        if (currentTerm != null) {
                                            currentTerm.id = aVal;
                                            currentTerm.prefix = prefix;
                                        }
                                    } else if (OBOConstants.RELATION_CTX == ctx) {
                                        if (currentRel != null) {
                                            currentRel.id = aVal;
                                            currentRel.prefix = prefix;
                                        }
                                    } else if (OBOConstants.INSTANCE_CTX == ctx) {
                                        if (currentInstance != null) {
                                            currentInstance.id = aVal;
                                            currentInstance.prefix = prefix;
                                        }
                                    }
                                }
                            } else if (line.startsWith(OBOConstants.TAG_NAME)) {
                                currentTag = OBOConstants.TAG_NAME;
                                String aVal = parseAsSimpleKeyValue(line, currentTag);

                                if (!StringUtils.isNull(aVal)) {
                                    if ((OBOConstants.TERM_CTX == ctx) && (currentTerm != null)) {
                                        if (StringUtils.isNull(currentTerm.name))
                                            currentTerm.name = aVal;
                                        else {
                                            String msg = "More than one name found:" + aVal + " for Term:"
                                                    + currentTerm.name;
                                            logger.warn(msg);
                                        }
                                    } else if ((OBOConstants.RELATION_CTX == ctx) && (currentRel != null)) {
                                        if (StringUtils.isNull(currentRel.name))
                                            currentRel.name = aVal;
                                        else {
                                            String msg = "More than one name found:" + aVal + " for Relation:"
                                                    + currentRel.name;
                                            logger.warn(msg);
                                        }
                                    } else if ((OBOConstants.INSTANCE_CTX == ctx) && (currentInstance != null)) {
                                        if (StringUtils.isNull(currentInstance.name))
                                            currentInstance.name = aVal;
                                        else {
                                            String msg = "More than one name found:" + aVal + " for Instance:"
                                                    + currentInstance.name;
                                            logger.warn(msg);
                                        }
                                    }
                                }
                            } else if (line.startsWith(OBOConstants.TAG_NAMESPACE)) {
                                currentTag = OBOConstants.TAG_NAMESPACE;
                                String aVal = parseAsSimpleKeyValue(line, currentTag);

                                if (!StringUtils.isNull(aVal)) {
                                    if ((OBOConstants.TERM_CTX == ctx) && (currentTerm != null)) {
                                        if (StringUtils.isNull(currentTerm.namespace))
                                            currentTerm.namespace = aVal;
                                        else {
                                            String msg = "More than one namespace found:" + aVal + " for Term:"
                                                    + currentTerm.name;
                                            logger.warn(msg);
                                        }
                                    } else if ((OBOConstants.RELATION_CTX == ctx) && (currentRel != null)) {
                                        if (StringUtils.isNull(currentRel.namespace))
                                            currentRel.namespace = aVal;
                                        else {
                                            String msg = "More than one namespace found:" + aVal + " for Relation:"
                                                    + currentRel.name;
                                            logger.warn(msg);
                                        }
                                    } else if ((OBOConstants.INSTANCE_CTX == ctx) && (currentInstance != null)) {
                                        if (StringUtils.isNull(currentInstance.namespace))
                                            currentInstance.namespace = aVal;
                                        else {
                                            String msg = "More than one namespace found:" + aVal + " for Instance:"
                                                    + currentInstance.name;
                                            logger.warn(msg);
                                        }
                                    }
                                }
                            } else if (line.startsWith(OBOConstants.TAG_ISANONYMOUS)) {
                                currentTag = OBOConstants.TAG_ISANONYMOUS;
                                String aVal = parseAsSimpleKeyValue(line, currentTag);

                                if (!StringUtils.isNull(aVal)) {
                                    if ((OBOConstants.TERM_CTX == ctx) && (currentTerm != null)) {
                                        if ("true".equalsIgnoreCase(aVal))
                                            currentTerm.isAnonymous = true;
                                    } else if ((OBOConstants.RELATION_CTX == ctx) && (currentRel != null)) {
                                        if ("true".equalsIgnoreCase(aVal))
                                            currentRel.isAnonymous = true;
                                    } else if ((OBOConstants.INSTANCE_CTX == ctx) && (currentInstance != null)) {
                                        if ("true".equalsIgnoreCase(aVal))
                                            currentInstance.isAnonymous = true;
                                    }
                                }
                            } else if (line.startsWith(OBOConstants.TAG_ALTID)) {
                                currentTag = OBOConstants.TAG_ALTID;
                                String aVal = parseAsSimpleKeyValue(line, currentTag);

                                if (!StringUtils.isNull(aVal)) {
                                    if ((OBOConstants.TERM_CTX == ctx) && (currentTerm != null))
                                        currentTerm.altIds.add(aVal);
                                    else if ((OBOConstants.RELATION_CTX == ctx) && (currentRel != null))
                                        currentRel.altIds.add(aVal);
                                    else if ((OBOConstants.INSTANCE_CTX == ctx) && (currentInstance != null))
                                        currentInstance.altIds.add(aVal);
                                }
                            } else if (line.startsWith(OBOConstants.TAG_CREATED_BY)) {
                                currentTag = OBOConstants.TAG_CREATED_BY;
                                String aVal = parseAsSimpleKeyValue(line, currentTag);

                                if (!StringUtils.isNull(aVal)) {
                                    if ((OBOConstants.TERM_CTX == ctx) && (currentTerm != null))
                                        currentTerm.setCreated_by(aVal);
                                    else if ((OBOConstants.RELATION_CTX == ctx) && (currentRel != null))
                                        currentRel.setCreated_by(aVal);
                                    else if ((OBOConstants.INSTANCE_CTX == ctx) && (currentInstance != null))
                                        currentInstance.setCreated_by(aVal);
                                }
                            } else if (line.startsWith(OBOConstants.TAG_CREATION_DATE)) {
                                currentTag = OBOConstants.TAG_CREATION_DATE;
                                String aVal = parseAsSimpleKeyValue(line, currentTag);

                                if (!StringUtils.isNull(aVal)) {
                                    if ((OBOConstants.TERM_CTX == ctx) && (currentTerm != null))
                                        currentTerm.setCreation_date(aVal);
                                    else if ((OBOConstants.RELATION_CTX == ctx) && (currentRel != null))
                                        currentRel.setCreation_date(aVal);
                                    else if ((OBOConstants.INSTANCE_CTX == ctx) && (currentInstance != null))
                                        currentInstance.setCreation_date(aVal);
                                }
                            } else if (line.startsWith(OBOConstants.TAG_DEF)) {
                                currentTag = OBOConstants.TAG_DEF;
                                ArrayList<String> def = parseLineWithXRefs(line);

                                String definition = null;
                                String definitionSource = null;

                                if ((def != null) && (!def.isEmpty())) {
                                    definition = (String) def.get(0);

                                    if (def.size() > 1) {
                                        definitionSource = (String) def.get(1);
                                    }
                                }

                                boolean addSrc = true;

                                if (!StringUtils.isNull(definition)) {
                                    if ((OBOConstants.TERM_CTX == ctx) && (currentTerm != null)) {
                                        if (StringUtils.isNull(currentTerm.definition))
                                            currentTerm.definition = definition;
                                        else {
                                            addSrc = false;
                                            String msg = "More than one definition found:" + definition + " for Term:"
                                                    + currentTerm.name;
                                            logger.warn(msg);
                                        }
                                    } else if ((OBOConstants.RELATION_CTX == ctx) && (currentRel != null)) {
                                        if (StringUtils.isNull(currentRel.definition))
                                            currentRel.definition = definition;
                                        else {
                                            addSrc = false;
                                            String msg = "More than one definition found:" + definition
                                                    + " for Relation:" + currentRel.name;
                                            logger.warn(msg);
                                        }
                                    }
                                }

                                if ((addSrc == true) && (!StringUtils.isNull(definitionSource))) {

                                    if ((OBOConstants.TERM_CTX == ctx) && (currentTerm != null))
                                        currentTerm.definitionSources = OBODbxref.parse(definitionSource);
                                    else if ((OBOConstants.RELATION_CTX == ctx) && (currentRel != null))
                                        currentRel.definitionSources = OBODbxref.parse(definitionSource);

                                }
                            } else if (line.startsWith(OBOConstants.TAG_COMMENT)) {
                                currentTag = OBOConstants.TAG_COMMENT;
                                String aVal = parseAsSimpleKeyValue(line, currentTag);

                                if (!StringUtils.isNull(aVal)) {
                                    if ((OBOConstants.TERM_CTX == ctx) && (currentTerm != null)) {
                                        if (StringUtils.isNull(currentTerm.comment))
                                            currentTerm.comment = aVal;
                                        else {
                                            String msg = "More than one comment found:" + aVal + " for Term:"
                                                    + currentTerm.name;
                                            logger.warn(msg);
                                        }
                                    } else if ((OBOConstants.RELATION_CTX == ctx) && (currentRel != null)) {
                                        if (StringUtils.isNull(currentRel.comment))
                                            currentRel.comment = aVal;
                                        else {
                                            String msg = "More than one comment found:" + aVal + " for Relation:"
                                                    + currentRel.name;
                                            logger.warn(msg);
                                        }
                                    } else if ((OBOConstants.INSTANCE_CTX == ctx) && (currentInstance != null)) {
                                        if (StringUtils.isNull(currentInstance.comment))
                                            currentInstance.comment = aVal;
                                        else {
                                            String msg = "More than one comment found:" + aVal + " for Instance:"
                                                    + currentInstance.name;
                                            logger.warn(msg);
                                        }
                                    }
                                }
                            } else if (line.startsWith(OBOConstants.TAG_SUBSET)) {
                                currentTag = OBOConstants.TAG_SUBSET;
                                String aVal = parseAsSimpleKeyValue(line, currentTag);

                                if (!StringUtils.isNull(aVal)) {
                                    if ((OBOConstants.TERM_CTX == ctx) && (currentTerm != null)) {
                                        if (header.containsSubsetName(aVal)) {
                                            currentTerm.subset.add(aVal);
                                        } else {
                                            String msg = "Unknown subset referred:" + aVal + " in Term:"
                                                    + currentTerm.name;
                                            logger.warn(msg);
                                        }
                                    }
                                }
                            } else if ((line.startsWith(OBOConstants.TAG_XREF))
                                    || (line.startsWith(OBOConstants.TAG_XREFANALOG))
                                    || (line.startsWith(OBOConstants.TAG_XREFUNK))) {
                                if (line.startsWith(OBOConstants.TAG_XREF))
                                    currentTag = OBOConstants.TAG_XREF;
                                else if (line.startsWith(OBOConstants.TAG_XREFANALOG))
                                    currentTag = OBOConstants.TAG_XREFANALOG;
                                else if (line.startsWith(OBOConstants.TAG_XREFUNK))
                                    currentTag = OBOConstants.TAG_XREFUNK;

                                String aVal = parseAsSimpleKeyValue(line, currentTag);

                                if (!StringUtils.isNull(aVal)) {
                                    Vector<OBODbxref> xref_vec= OBODbxref.parse(aVal);
                                    if ((OBOConstants.TERM_CTX == ctx) && (currentTerm != null))
                                        currentTerm.dbXrefs.addAll(xref_vec);
                                    else if ((OBOConstants.RELATION_CTX == ctx) && (currentRel != null))
                                        currentRel.dbXrefs.addAll(xref_vec);
                                    else if ((OBOConstants.INSTANCE_CTX == ctx) && (currentInstance != null))
                                        currentInstance.dbXrefs.addAll(xref_vec);
                                }
                            } else if (line.startsWith(OBOConstants.TAG_ISOBSOLETE)) {
                                currentTag = OBOConstants.TAG_ISOBSOLETE;
                                String aVal = parseAsSimpleKeyValue(line, currentTag);

                                if (!StringUtils.isNull(aVal)) {
                                    if ((OBOConstants.TERM_CTX == ctx) && (currentTerm != null)) {
                                        if ("true".equalsIgnoreCase(aVal))
                                            currentTerm.isObsolete = true;
                                    } else if ((OBOConstants.INSTANCE_CTX == ctx) && (currentInstance != null)) {
                                        if ("true".equalsIgnoreCase(aVal))
                                            currentInstance.isObsolete = true;
                                    }
                                }
                            } else if (line.startsWith(OBOConstants.TAG_REPLACEDBY)) {
                                currentTag = OBOConstants.TAG_REPLACEDBY;
                                String aVal = parseAsSimpleKeyValue(line, currentTag);

                                if (!StringUtils.isNull(aVal)) {
                                    if ((OBOConstants.TERM_CTX == ctx) && (currentTerm != null))
                                        currentTerm.replacedBy = aVal;
                                    else if ((OBOConstants.INSTANCE_CTX == ctx) && (currentInstance != null))
                                        currentInstance.replacedBy = aVal;
                                }
                            } else if ((line.startsWith(OBOConstants.TAG_CONSIDER))
                                    || (line.startsWith(OBOConstants.TAG_USETERM))) {
                                if (line.startsWith(OBOConstants.TAG_CONSIDER))
                                    currentTag = OBOConstants.TAG_CONSIDER;
                                else if (line.startsWith(OBOConstants.TAG_USETERM))
                                    currentTag = OBOConstants.TAG_USETERM;

                                String aVal = parseAsSimpleKeyValue(line, currentTag);

                                if (!StringUtils.isNull(aVal)) {
                                    if ((OBOConstants.TERM_CTX == ctx) && (currentTerm != null))
                                        currentTerm.consider = aVal;
                                    else if ((OBOConstants.INSTANCE_CTX == ctx) && (currentInstance != null))
                                        currentInstance.consider = aVal;
                                }
                            } else if (line.startsWith(OBOConstants.TAG_RELATIONSHIP)) {
                                currentTag = OBOConstants.TAG_RELATIONSHIP;
                                String aVal = parseAsSimpleKeyValue(line, currentTag);

                                if (!StringUtils.isNull(aVal)) {
                                    if ((OBOConstants.TERM_CTX == ctx) && (currentTerm != null)) {
                                        String[] tokens = aVal.split(" ");

                                        if (tokens.length > 1) {
                                            String relation = tokens[0].trim();
                                            String target = tokens[1].trim();
                                            currentTerm.addRelationship(relation, target);
                                            OBORelation uRel = rels.getMemberById(relation);
                                            if (uRel != null)
                                                uRel.isUsed = true;
                                        } else {
                                            String msg = "Relationship not defined correctly for term "
                                                    + currentTerm.id + ": " + aVal;
                                            logger.warn(msg);
                                        }
                                    }
                                }
                            } else if ((line.startsWith(OBOConstants.TAG_ISA))
                                    || (line.startsWith(OBOConstants.TAG_INTERSECTIONOF))
                                    || (line.startsWith(OBOConstants.TAG_UNIONOF))
                                    || (line.startsWith(OBOConstants.TAG_DISJOINTFROM))) {
                                String relation = null;
                                if (line.startsWith(OBOConstants.TAG_ISA)) {
                                    currentTag = OBOConstants.TAG_ISA;
                                    relation = OBOConstants.REL_ISA;
                                } else if (line.startsWith(OBOConstants.TAG_INTERSECTIONOF)) {
                                    currentTag = OBOConstants.TAG_INTERSECTIONOF;
                                    relation = OBOConstants.REL_INTERSECTIONOF;
                                } else if (line.startsWith(OBOConstants.TAG_UNIONOF)) {
                                    currentTag = OBOConstants.TAG_UNIONOF;
                                    relation = OBOConstants.REL_UNIONOF;
                                } else if (line.startsWith(OBOConstants.TAG_DISJOINTFROM)) {
                                    currentTag = OBOConstants.TAG_DISJOINTFROM;
                                    relation = OBOConstants.REL_DISJOINTFROM;
                                }

                                String aVal = parseAsSimpleKeyValue(line, currentTag);

                                if (!StringUtils.isNull(aVal)) {

                                    // String id=
                                    // getTermIDFromPrefixAndTermId(aVal);

                                    if ((OBOConstants.TERM_CTX == ctx) && (currentTerm != null)) {
                                        // currentTerm.addRelationship(relation,
                                        // id);
                                        currentTerm.addRelationship(relation, aVal);
                                        OBORelation uRel = rels.getMemberByName(relation);
                                        if (uRel != null)
                                            uRel.isUsed = true;
                                    }
                                }
                            } else if (line.startsWith(OBOConstants.TAG_ISCYCLIC)) {
                                currentTag = OBOConstants.TAG_ISCYCLIC;
                                String aVal = parseAsSimpleKeyValue(line, currentTag);

                                if (!StringUtils.isNull(aVal)) {
                                    if ((OBOConstants.RELATION_CTX == ctx) && (currentRel != null)) {
                                        if ("true".equalsIgnoreCase(aVal))
                                            currentRel.setCyclic(true);
                                    }
                                }
                            } else if (line.startsWith(OBOConstants.TAG_ISREFLEXIVE)) {
                                currentTag = OBOConstants.TAG_ISREFLEXIVE;
                                String aVal = parseAsSimpleKeyValue(line, currentTag);

                                if (!StringUtils.isNull(aVal)) {
                                    if ((OBOConstants.RELATION_CTX == ctx) && (currentRel != null)) {
                                        if ("true".equalsIgnoreCase(aVal))
                                            currentRel.setReflexive(true);
                                    }
                                }
                            } else if (line.startsWith(OBOConstants.TAG_ISSYMMETRIC)) {
                                currentTag = OBOConstants.TAG_ISSYMMETRIC;
                                String aVal = parseAsSimpleKeyValue(line, currentTag);

                                if (!StringUtils.isNull(aVal)) {
                                    if ((OBOConstants.RELATION_CTX == ctx) && (currentRel != null)) {
                                        if ("true".equalsIgnoreCase(aVal))
                                            currentRel.setSymmetric(true);
                                    }
                                }
                            } else if (line.startsWith(OBOConstants.TAG_ISANTISYMMETRIC)) {
                                currentTag = OBOConstants.TAG_ISANTISYMMETRIC;
                                String aVal = parseAsSimpleKeyValue(line, currentTag);

                                if (!StringUtils.isNull(aVal)) {
                                    if ((OBOConstants.RELATION_CTX == ctx) && (currentRel != null)) {
                                        if ("true".equalsIgnoreCase(aVal))
                                            currentRel.setAntiSymmetric(true);
                                    }
                                }
                            } else if (line.startsWith(OBOConstants.TAG_ISTRANSITIVE)) {
                                currentTag = OBOConstants.TAG_ISTRANSITIVE;
                                String aVal = parseAsSimpleKeyValue(line, currentTag);

                                if (!StringUtils.isNull(aVal)) {
                                    if ((OBOConstants.RELATION_CTX == ctx) && (currentRel != null)) {
                                        if ("true".equalsIgnoreCase(aVal))
                                            currentRel.setTransitive(true);
                                    }
                                }
                            } else if (line.startsWith(OBOConstants.TAG_INVERSEOF)) {
                                currentTag = OBOConstants.TAG_INVERSEOF;
                                String aVal = parseAsSimpleKeyValue(line, currentTag);

                                if (!StringUtils.isNull(aVal)) {
                                    if ((OBOConstants.RELATION_CTX == ctx) && (currentRel != null)) {
                                        currentRel.inverseOf = getIDFromPrefixAndId(aVal);
                                    }
                                }
                            } else if (line.startsWith(OBOConstants.TAG_DOMAIN)) {
                                currentTag = OBOConstants.TAG_DOMAIN;
                                String aVal = parseAsSimpleKeyValue(line, currentTag);

                                if (!StringUtils.isNull(aVal)) {
                                    if ((OBOConstants.RELATION_CTX == ctx) && (currentRel != null)) {
                                        currentRel.domain.add(aVal);
                                    }
                                }
                            } else if (line.startsWith(OBOConstants.TAG_RANGE)) {
                                currentTag = OBOConstants.TAG_RANGE;
                                String aVal = parseAsSimpleKeyValue(line, currentTag);

                                if (!StringUtils.isNull(aVal)) {
                                    if ((OBOConstants.RELATION_CTX == ctx) && (currentRel != null)) {
                                        currentRel.range.add(aVal);
                                    }
                                }
                            } else if (line.startsWith(OBOConstants.TAG_INSTANCEOF)) {
                                currentTag = OBOConstants.TAG_INSTANCEOF;
                                String aVal = parseAsSimpleKeyValue(line, currentTag);

                                if (!StringUtils.isNull(aVal)) {
                                    if ((OBOConstants.INSTANCE_CTX == ctx) && (currentInstance != null)) {
                                        String instanceOfTermPrefix = null;
                                        String instanceOfTermId = aVal;

                                        if (aVal.indexOf(":") != -1) {
                                            String[] tokens = aVal.split(":");
                                            instanceOfTermPrefix = tokens[0].trim();
                                            instanceOfTermId = tokens[1].trim();
                                        }

                                        currentInstance.instanceOfTermId = instanceOfTermId;
                                        currentInstance.instanceOfTermPrefix = instanceOfTermPrefix;
                                    }
                                }
                            } else if (line.startsWith(OBOConstants.TAG_PROPERTYVALUE)) {
                                currentTag = OBOConstants.TAG_PROPERTYVALUE;
                                String aVal = parseAsSimpleKeyValue(line, currentTag);

                                if (!StringUtils.isNull(aVal)) {
                                    if ((OBOConstants.INSTANCE_CTX == ctx) && (currentInstance != null))
                                        currentInstance.addProperty(aVal);
                                }
                            } else {
                                String msg = "The following unknown tag was skipped in the resource header:" + line
                                        + "\n";
                                logger.warn(msg);
                            }
                        }
                    } catch (Exception e) {
                        logger.warn("Failed to set content value for tag '" + currentTag + "'. ", e);
                    }
                }

                // Add the last abbreviation, if not already added
                /** ***********LAST ABBREVIATION ADD START****************** */
                if (currentAbb != null) {
                    // If there is an abbrevitaion already being worked on
                    // (previous)
                    // commit it, if possible
                    if (!StringUtils.isNull(currentAbb.abbreviation)) {
                        if (StringUtils.isNull(currentAbb.genericURL))
                            currentAbb.genericURL = currentAbb.abbreviation;

                        sAbbs.addMember(currentAbb);
                    }
                }

                if (currentAbbSyn != null) {
                    for (int m = 0; m < currentAbbSyn.size(); m++) {
                        Object aso = currentAbbSyn.elementAt(m);

                        if ((aso != null) && (aso instanceof OBOAbbreviation)) {
                            OBOAbbreviation currentAbbSynV = (OBOAbbreviation) aso;
                            if (!StringUtils.isNull(currentAbbSynV.abbreviation)) {
                                currentAbbSynV.genericURL = currentAbb.genericURL;

                                if (StringUtils.isNull(currentAbbSynV.genericURL))
                                    currentAbbSynV.genericURL = currentAbb.abbreviation;

                                sAbbs.addMember(currentAbbSynV);
                            }
                        }
                    }
                }
                /** ***********LAST ABBREVIATION ADD END****************** */

                // Last Term left unsaved
                addTermToTerms(terms, currentTerm);
                // Last Relation left unsaved
                addRelationToRelations(rels, currentRel);
                // Last Instance left unsaved
                addInstanceToInstances(instances, currentInstance);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void addTermToTerms(OBOTerms terms, OBOTerm term) {
        if ((terms == null) || (term == null))
            return;

        // TODO: check for intersecion_of should have two entries
        // TODO: check for is_obsolete vs. replaced_by, consider etc. (see page
        // 13)
        // replaced_by, consider and other attributes are only valid for
        // obsolete
        // term

        if ((term != null) && (term.isReady())) {
            try {
                terms.addMember(term);
            } catch (ResourceException e) {
                logger.warn(e.getMessage(), e);
            }
        }
    }

    private void addRelationToRelations(OBORelations relations, OBORelation relation) {
        if ((relations == null) || (relation == null))
            return;

        if ((relation != null) && (relation.isReady())) {
            try {
                relations.addMergeMember(relation);
            } catch (ResourceException e) {
                logger.warn(e.getMessage(), e);
            }
        }
    }

    private void addInstanceToInstances(OBOInstances instances, OBOInstance instance) {
        if ((instances == null) || (instance == null))
            return;

        if ((instance != null) && (instance.isReady())) {
            try {
                instances.addMember(instance);
            } catch (ResourceException e) {
                logger.warn(e.getMessage(), e);
            }
        }
    }

    public static String parseAsSimpleKeyValueWithLimit(String source, String keyString, int max_length) {
        String temp = parseAsSimpleKeyValue(source, keyString);
        if (temp != null && temp.length() > max_length) {
            return temp.substring(0, max_length);
        }
        return temp;
    }

    public static String parseAsSimpleKeyValue(String source, String keyString) {
        if ((!StringUtils.isNull(source)) && (!StringUtils.isNull(keyString)) && (source.startsWith(keyString)))
            return removeComments(source.substring(keyString.length()).trim());

        return null;
    }

    private void showDepricatedMessageForTag(String oldTag, String newTag) {
        if (showDeprecatedMessage)
            logger.info("Tag '" + oldTag + "' is deprecated in OBO Format Version " + OBOConstants.OBO_CURRENT_FORMAT
                    + ".  Please use '" + newTag + "'.");
    }

    private void showIgnoreValueMessage(String tag) {
        logger.info("Value of the tag '" + tag + "' is ignored/not-processed.");
    }

    /*
     * private String parseSynonymLine(String line) { StringTokenizer tok = new
     * StringTokenizer(line, "\""); tok.nextToken(); // Gets rid of "synonym: ")
     * 
     * return removeEscapes(tok.nextToken()); }
     */

    // This method removes the espcape characters form the input String
    public static String removeEscapes(String input) {
        String retVal = input;
        while (retVal.indexOf('\\') >= 0) {
            int index = retVal.indexOf('\\');
            retVal = retVal.substring(0, index) + retVal.substring(index + 1);
        }

        return retVal;
    }

    public static String removeComments(String str) {
        if (str != null) {
            if (str.indexOf("!") != -1) {
                str = (str.split("!")[0]).trim();
            }
        }

        return str;
    }

    // This method returns an ArrayList containing both the definition[0] and
    // the reference[1]
    public ArrayList<String> parseLineWithXRefs(String line) {
        ArrayList<String> retVal = new ArrayList<String>();

        int secondQuoteIndex = line.indexOf("\"", 6); // This will be the
        // closing quote for the
        // definition

        boolean done = false;
        String refr = line.substring(secondQuoteIndex + 1);

        while ((!StringUtils.isNull(refr)) && (!done)) {
            if ((refr.trim().startsWith("[")) && (refr.trim().endsWith("]")))
                done = true;
            else {
                if (refr.indexOf("\"") != -1) {
                    if (refr.indexOf("\"") == 0)
                        secondQuoteIndex++;
                    else
                        secondQuoteIndex += refr.indexOf("\"");
                } else {
                    if (refr.indexOf("[") != -1)
                        secondQuoteIndex += (refr.indexOf("["));
                    else
                        secondQuoteIndex += (refr.length());
                    done = true;
                }
                refr = line.substring(secondQuoteIndex + 1);
            }
        }

        String def = line.substring(6, secondQuoteIndex);
        String reference = "";
        if (!StringUtils.isNull(refr)) // There is a definition reference
        {
            reference = refr;
        }

        retVal.add(removeEscapes(def.trim()));
        retVal.add(removeEscapes(reference.trim()));
        return retVal;
    }

    String getIDFromPrefixAndId(String prefix_and_termid) {
        String termId = prefix_and_termid;
        if (prefix_and_termid.indexOf(":") != -1) {
            String[] tokens = prefix_and_termid.split(":");
            termId = tokens[1].trim();
        }
        return removeComments(termId).trim();
    }

    public static void main(String arg[]) {
        try {
            File f = new File("C:\\temp\\junk.txt");
            System.out.println("String: \\(  becomes " + Pattern.quote("\\("));
            Pattern p = Pattern.compile("(.*)(\\\\)(\\s)*");
            Matcher m = p.matcher(" test \\ ");
            System.out.println(m.matches());

            BufferedReader buf = new BufferedReader(new FileReader(f));
            String tmp;
            OBOResourceReaderHelper orh = new OBOResourceReaderHelper(null);
            while ((tmp = orh.readOneOboLine(buf)) != null) {
                System.out.println(tmp);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}