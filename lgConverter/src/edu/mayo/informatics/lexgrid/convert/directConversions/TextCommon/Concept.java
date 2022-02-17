
package edu.mayo.informatics.lexgrid.convert.directConversions.TextCommon;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Holder for concepts loaded from Text files.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 7190 $ checked in on $Date: 2008-02-15
 *          17:25:58 +0000 (Fri, 15 Feb 2008) $
 */
public class Concept {
    public String code;
    public String name;
    public String description;
    public Set<String> alternateDescriptions = new HashSet<String>();
    public int depth;

    public Concept(String code, String name, String description, int depth) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.depth = depth;
    }

    public Concept(String line, String token, CodingScheme codingScheme) {
        depth = 0;
        while (true) {
            if ((line.length() > depth + 1) && line.substring(depth, depth + 1).equals(token)) {
                depth++;
            } else {
                break;
            }
        }

        StringTokenizer tokenizer = new StringTokenizer(line, token);
        if (tokenizer.hasMoreElements()) {
            name = tokenizer.nextToken();
        }
        if (tokenizer.hasMoreElements()) {
            description = tokenizer.nextToken();
        }
        if (tokenizer.hasMoreElements()) {
            // if they provided 3, then the format is B.
            code = name;
            name = description;
            description = tokenizer.nextToken();
            codingScheme.isTypeB = true;
        }
        while( tokenizer.hasMoreElements()) {
            alternateDescriptions.add(tokenizer.nextToken());
        }
    }

    public String toString() {
        return "Code: " + code + "\nName: " + name + "\nDescription: " + description + "\nDepth: " + depth;
    }
    
    public boolean equals(Object o) {
        if (o instanceof Concept == true)
            return code.equals(((Concept)o).code);
        return false;
    }
}