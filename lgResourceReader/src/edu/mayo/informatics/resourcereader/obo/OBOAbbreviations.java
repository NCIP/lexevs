
package edu.mayo.informatics.resourcereader.obo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Collection;
import java.util.Hashtable;

import org.LexGrid.LexBIG.Utility.logging.CachingMessageDirectorIF;

import edu.mayo.informatics.resourcereader.core.StringUtils;
import edu.mayo.informatics.resourcereader.core.IF.ResourceEntity;

/**
 * The class stores the list of obo abbreviations
 * 
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class OBOAbbreviations extends OBOCollection {
    private Hashtable<String, OBOAbbreviation> abbreviationsbyName = new Hashtable<String, OBOAbbreviation>();
    private Hashtable<String, OBOAbbreviation> abbreviationsbyURL = new Hashtable<String, OBOAbbreviation>();

    public OBOAbbreviations(CachingMessageDirectorIF rLogger) {
        super(rLogger);
    }

    public void addMember(ResourceEntity abbreviation) {
        if ((abbreviation != null) && (abbreviation instanceof OBOAbbreviation)) {
            OBOAbbreviation abs = (OBOAbbreviation) abbreviation;

            if ((!StringUtils.isNull(abs.getAbbreviation())) && (!StringUtils.isNull(abs.getGenericURL()))) {
                if (!abbreviationsbyName.containsKey(abs.getAbbreviation()))
                    abbreviationsbyName.put(abs.getAbbreviation(), abs);

                if (!abbreviationsbyURL.containsKey(abs.getGenericURL()))
                    abbreviationsbyURL.put(abs.getGenericURL(), abs);
            }
        }
    }

    public OBOAbbreviation getMemberByName(String name) {
        return abbreviationsbyName.get(name);
    }

    public OBOAbbreviation getOBOAbbreviationByURL(String url) {
        return abbreviationsbyURL.get(url);
    }

    public Collection<OBOAbbreviation> getAllMembers() {
        return abbreviationsbyName.values();
    }

    public long getMembersCount() {
        return abbreviationsbyName.size();
    }

    public String toString() {
        return abbreviationsbyName.toString();
    }

    public void readAbbreviationsFile(OBOAbbreviations sAbbs, String inputFile) {
        String line = "";
        OBOAbbreviation currentAbb = null;
        if (sAbbs == null) {
            sAbbs = new OBOAbbreviations(logger);
        }

        try {
            BufferedReader inputReader = new BufferedReader(new FileReader(inputFile));

            while ((line = inputReader.readLine()) != null) {
                String currentTag = "";

                if (((!StringUtils.isNull(line)) && (!line.startsWith("!"))) && (!OBOConstants.isHeaderTag(line))
                        && (!OBOConstants.isIgnoredTag(line))) {
                    if (line.startsWith(OBOConstants.TAG_ABBREVIATION)) {
                        currentTag = OBOConstants.TAG_ABBREVIATION;
                        if (currentAbb != null) {
                            // If there is an abbrevitaion already being
                            // worked on (previous) commit it, if possible
                            if (!StringUtils.isNull(currentAbb.getAbbreviation())) {
                                if (StringUtils.isNull(currentAbb.getGenericURL())) {
                                    currentAbb.setGenericURL(currentAbb.getAbbreviation());
                                }

                                sAbbs.addMember(currentAbb);
                            }
                        }

                        currentAbb = new OBOAbbreviation(logger);
                        String aVal = StringUtils.parseAsSimpleKeyValue(line, currentTag);
                        if (!StringUtils.isNull(aVal))
                            currentAbb.setAbbreviation(aVal);
                    } else if (line.startsWith(OBOConstants.TAG_GENERICURL)) {
                        currentTag = OBOConstants.TAG_GENERICURL;
                        String aVal = StringUtils.parseAsSimpleKeyValue(line, currentTag);

                        if (!StringUtils.isNull(aVal)) {
                            if (currentAbb == null) {
                                currentAbb = new OBOAbbreviation(logger);
                            }

                            currentAbb.setGenericURL(aVal);
                        }
                    }
                }
            }

            // Process the last abbreviation in the file
            if (currentAbb != null) {
                // If there is an abbrevitaion already being
                // worked on (previous) commit it, if possible
                if (!StringUtils.isNull(currentAbb.getAbbreviation())) {
                    if (StringUtils.isNull(currentAbb.getGenericURL())) {
                        currentAbb.setGenericURL(currentAbb.getAbbreviation());
                    }

                    sAbbs.addMember(currentAbb);
                }
            }
        } catch (Exception e) {
            logger.warn("Failed while reading OBO Abbreviations File", e);
        }

    }
}