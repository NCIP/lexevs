
package edu.mayo.informatics.lexgrid.convert.directConversions.TextCommon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.naming.Mappings;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.relations.Relations;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;

/**
 * CodingScheme holder for Text loaders.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 5882 $ checked in on $Date: 2007-09-10
 *          15:50:31 +0000 (Mon, 10 Sep 2007) $
 */
public class CodingScheme {
    public String codingSchemeName = "";
    public String codingSchemeId = "";
    public String defaultLanguage = "";
    public String representsVersion = "";
    public String formalName = "";
    public String source = "";
    public String entityDescription = "";
    public String copyright = "Copyright: (c) 2004-2007 Mayo Foundation for Medical Education and Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the triple-shield Mayo logo are trademarks and service marks of MFMER.Except as contained in the copyright notice above, or as used to identify MFMER as the author of this software, the trade names, trademarks, service marks, or product names of the copyright holder shall not be used in advertising, promotion or otherwise in connection with this software without prior written authorization of the copyright holder. Licensed under the Eclipse Public License, Version 1.0 (the \"License\"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.eclipse.org/legal/epl-v10.html.";
    public Concept[] concepts;
    public Association[] associations;
    public boolean isTypeB = false;
    public static final String PAR = "PAR"; // constant variable for "PAR"

    public static org.LexGrid.codingSchemes.CodingScheme toCodingScheme(CodingScheme codingScheme) {
        org.LexGrid.codingSchemes.CodingScheme cs = new org.LexGrid.codingSchemes.CodingScheme();
        cs.setCodingSchemeName(codingScheme.codingSchemeName);
        cs.setCodingSchemeURI(codingScheme.codingSchemeId);
        cs.setDefaultLanguage(codingScheme.defaultLanguage);
        cs.setRepresentsVersion(codingScheme.representsVersion);
        cs.setFormalName(codingScheme.formalName);

        EntityDescription ed = new EntityDescription();
        ed.setContent(codingScheme.entityDescription);
        cs.setEntityDescription(ed);

        Text text = new Text();
        text.setContent(codingScheme.copyright);
        cs.setCopyright(text);
        
        Source src = new Source();
        src.setContent(codingScheme.source);
        src.setRole(codingScheme.codingSchemeId);
        cs.addSource(src);
        cs.setMappings(new Mappings());
        
        // add entities
        Collection<String> existedEntities = new ArrayList<String>();
        cs.setEntities(new Entities());
        for (Concept c : codingScheme.concepts) {
            if (existedEntities.contains(c.code) == false) {
                Entity e = new Entity();
                e.setEntityCode(c.code);
                e.setEntityCodeNamespace(codingScheme.codingSchemeName);
                e.setIsAnonymous(false);
                e.setIsDefined(true);
                e.addEntityType(SQLTableConstants.TBL_CONCEPT);

                // add presentations to entity
                // if both name & desc exist, desc is prefered. otherwise use name
                EntityDescription enDesc = new EntityDescription();

                Presentation preferred = null;
                List<Presentation> alternates = new ArrayList<Presentation>();
                for(String description : Arrays.asList(c.description,c.name,c.code)){
                    if(description != null){
                        if(preferred == null){
                            preferred = createPresentation(description, true);
                            enDesc.setContent(description);
                        } else {
                            alternates.add(createPresentation(description, false));
                        }
                    }
                }

                e.addPresentation(preferred);
                for(Presentation alternate : alternates){
                    e.addPresentation(alternate);
                }
               
                e.setEntityDescription(enDesc);

                cs.getEntities().addEntity(e);
                existedEntities.add(c.code);
            }
            
        }
        
        // add associations
        Relations relations = new Relations();
        relations.setContainerName(codingScheme.codingSchemeName + "Relation");
        
        AssociationPredicate ap = new AssociationPredicate();
        ap.setAssociationName(PAR);  
        
        for (Association a : codingScheme.associations) {
            AssociationSource as = new AssociationSource();
            as.setSourceEntityCode(a.getSourceConcept().code);
            as.setSourceEntityCodeNamespace(codingScheme.codingSchemeName);
            for (Concept c : a.getTargetConceptSet()) {
                AssociationTarget at = new AssociationTarget();
                at.setTargetEntityCode(c.code);
                at.setTargetEntityCodeNamespace(codingScheme.codingSchemeName);
                as.addTarget(at);
            }
            ap.addSource(as);
        }
        
        relations.addAssociationPredicate(ap);
        
        cs.addRelations(relations);

        return cs;
    }
    
    private static Presentation createPresentation(String text, boolean perferred){
        Presentation p = new Presentation();

        p.setPropertyType(SQLTableConstants.TBLCOLVAL_PRESENTATION);
        p.setPropertyName("textPresentation");
        p.setIsPreferred(perferred);
        
        Text presentationText = new Text();
        presentationText.setContent(text);
        
        p.setValue(presentationText);
        
        return p;
    }
}