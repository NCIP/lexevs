
package edu.mayo.informatics.lexgrid.convert.directConversions.TextCommon;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.LexGrid.LexBIG.Extensions.Load.Text_Loader;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;

/**
 * Common bits for the Text -> foo loaders.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 5882 $ checked in on $Date: 2007-05-16
 *          21:55:43 +0000 (Wed, 16 May 2007) $
 */
public class TextUtility {
    
    public static Concept getParent(Concept[] concepts, int curPos) {
        int depth = concepts[curPos].depth;
        for (int i = curPos; i >= 0; i--) {
            if (concepts[i].depth < depth) {
                return concepts[i];
            }
        }
        return null;
    }

    public static CodingScheme readAndVerifyConcepts(URI fileLocation, LgMessageDirectorIF messages, String token,
            boolean forceTypeB) throws Exception {
        CodingScheme codingScheme = new CodingScheme();
        codingScheme.isTypeB = forceTypeB;
        int uniqueCodeIdentifer = 1;
        messages.info("Reading file into memory");
        try {
            List<Concept> concepts = new ArrayList<Concept>();

               
            BufferedReader reader;
            if(fileLocation.toString().equals(Text_Loader.STD_IN_URI)){
                reader = new BufferedReader(new InputStreamReader(System.in));
            } else {
                reader = new BufferedReader(new FileReader(fileLocation.getPath()));
            }
            
            String line = reader.readLine();

            int lineNo = 1;
            while (line != null) {
                if (line.length() > 0 && line.charAt(0) != '#') {
                    // read the "special" first line
                    StringTokenizer tokenizer = new StringTokenizer(line, token);

                    if (tokenizer.countTokens() < 4) {
                        messages
                                .fatalAndThrowException("FATAL ERROR - The beginning of the file must contain a token seperated"
                                        + " line that contain (in this order) 'codingSchemeName,codingSchemeId,defaultLanguage,formalName'"
                                        + "\nThe line may contain the following values (in this order) 'version,source,description,copyright'");
                    }
                    codingScheme.codingSchemeName = tokenizer.nextToken().trim();
                    codingScheme.codingSchemeId = tokenizer.nextToken().trim();
                    codingScheme.defaultLanguage = tokenizer.nextToken().trim();
                    codingScheme.formalName = tokenizer.nextToken().trim();

                    if (tokenizer.hasMoreTokens()) {
                        codingScheme.representsVersion = tokenizer.nextToken().trim();
                    }
                    if (tokenizer.hasMoreTokens()) {
                        codingScheme.source = tokenizer.nextToken().trim();
                    }
                    if (tokenizer.hasMoreTokens()) {
                        codingScheme.entityDescription = tokenizer.nextToken().trim();
                    }
                    if (tokenizer.hasMoreTokens()) {
                        codingScheme.copyright = tokenizer.nextToken().trim();
                    }

                    lineNo++;
                    line = reader.readLine();
                    break;
                }
                lineNo++;
                line = reader.readLine();
            }

            // read the rest of the lines
            while (line != null) {
                if (line.length() > 0 && line.charAt(0) != '#') {
                    Concept temp = new Concept(line, token, codingScheme);
                    if (temp.name != null) {
                        concepts.add(temp);
                    } else {
                        messages.info("WARNING - Line number " + lineNo
                                + " is missing required information.  Skipping.");
                    }
                    

                }
                lineNo++;
                line = reader.readLine();
            }

            Concept[] allConcepts = (Concept[]) concepts.toArray(new Concept[concepts.size()]);
            concepts = null;
            List<Association> associations = new ArrayList<Association>();
            List<Association> associationSet = new ArrayList<Association>();
            
            for (int i = 0; i < allConcepts.length; i++) {
                if (codingScheme.isTypeB) {
                    if (allConcepts[i].code == null || allConcepts[i].code.length() == 0) {
                        // move them to the correct places for type B.
                        allConcepts[i].code = allConcepts[i].name;
                        allConcepts[i].name = allConcepts[i].description;
                        allConcepts[i].description = null;
                    }
                    // see if this code already exists - if so, the names must
                    // match.
                    for (int j = 0; j < i; j++) {
                        if (allConcepts[j].code.equals(allConcepts[i].code)
                                && (((allConcepts[i].name == null && allConcepts[j].name != null) || (allConcepts[i].name != null && allConcepts[j].name == null)) || ((allConcepts[i].name != null && allConcepts[j].name != null) && (!allConcepts[i].name
                                        .equals(allConcepts[j].name))))) {
                            // codes match, names don't, fatal error.
                            messages.fatalAndThrowException("FATAL ERROR - Concept code " + allConcepts[i].code
                                    + " occurs twice with different names.  This is illegal.");

                        }
                    }

                } else {
                    // type A - need to generate codes.
                    boolean found = false;
                    // see if this name already exists - if so, use that code.
                    for (int j = 0; j < i; j++) {
                        if (allConcepts[j].name.equals(allConcepts[i].name)) {
                            allConcepts[i].code = allConcepts[j].code;
                            found = true;
                            break;
                        }
                    }
                    // doesn't exist = generate a new code.
                    if (!found) {
                        allConcepts[i].code = uniqueCodeIdentifer++ + "";
                    }
                }
                
                // generate associations. keep all concepts in allConcepts, even redundant concepts.
                if (i -1 < 0) {//root
                    //do nothing for the first concept
                }
                // if the current concept is deeper than the previous one, 
                // create an association, the previous is the source and the
                // current concept should be the target
                else if (allConcepts[i].depth > allConcepts[i-1].depth) {
                    Association a = new Association();
                    a.setRelationName("PAR");
                    a.setSourceConcept(allConcepts[i-1]);
                    a.addTargetConcept(allConcepts[i]);
                    associations.add(a);
                }
                // if the current concept's depth is the same as the previous one
                // it should be a new target concept in the last association
                else if(allConcepts[i].depth == allConcepts[i-1].depth && associations.size() > 0) {
                    associations.get(associations.size()-1).addTargetConcept(allConcepts[i]);
                }
                // if the current concept's depth is smaller than the previous one,
                else {
                    for (int j = associations.size()-1; j >=0; j-- ) {
                        if (associations.get(j).getSourceConcept().depth >= allConcepts[i].depth &&
                            associationSet.contains(associations.get(j)) == false ) {
                            associationSet.add(associations.get(j));
                        }
                    }
                    for (Association a : associationSet)
                        associations.remove(a);
                    if (associations.size()>0)
                        associations.get(associations.size()-1).addTargetConcept(allConcepts[i]);
                }
            }
            //when loop is done, move all associations in stack to the set
            for (Association a : associations){
                if (associationSet.contains(a) == false)
                    associationSet.add(a);
            }

            codingScheme.concepts = allConcepts;
            codingScheme.associations = (Association[]) associationSet.toArray(new Association[associationSet.size()]);

            return codingScheme;
        } catch (FileNotFoundException e) {
            messages.fatalAndThrowException("File not found", e);
            // this is actually unreachable
            return null;
        }
    }
}