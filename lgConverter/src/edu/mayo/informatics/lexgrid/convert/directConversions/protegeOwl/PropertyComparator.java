
package edu.mayo.informatics.lexgrid.convert.directConversions.protegeOwl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Comment;
import org.LexGrid.concepts.Definition;
import org.LexGrid.concepts.Presentation;

/**
 *  Comparator used to sort concept properties by type and priority ...
 * 
 * @author Pradip Kanjamala (kanjamala.pradip@mayo.edu)
 */
public class PropertyComparator implements Comparator<Property>{
    
    List<Class<? extends Property>> prioritizedPropertyClasses;
    List<String> prioritizedPresentationNames;
    List<String> prioritizedDefinitionNames;
    PreferenceManager prefManager;
    
    public PropertyComparator(PreferenceManager prefManager) {
        this.prefManager= prefManager;
        prioritizedPresentationNames = prefManager.getPrioritized_presentation_names();
        prioritizedDefinitionNames = prefManager.getPrioritized_definition_names();
        prioritizedPropertyClasses = new ArrayList<Class<? extends Property>>();
        prioritizedPropertyClasses.add(Presentation.class);
        prioritizedPropertyClasses.add(Definition.class);
        prioritizedPropertyClasses.add(Comment.class);
        prioritizedPropertyClasses.add(Property.class);
    }

    public int compare(Property o1, Property o2) {
        if (o1 instanceof Property && o2 instanceof Property) {
            Property p1 = o1;
            Property p2 = o2;
            int i = prioritizedPropertyClasses.indexOf(p1.getClass()) - prioritizedPropertyClasses.indexOf(p2.getClass());
            if (i != 0)
                return i;
            if (p1 instanceof Presentation && p2 instanceof Presentation) {
                i = prioritizedPresentationNames.indexOf(p1.getPropertyName())
                        - prioritizedPresentationNames.indexOf(p2.getPropertyName());
            } else if (p1 instanceof Definition && p2 instanceof Definition) {
                i = prioritizedDefinitionNames.indexOf(p1.getPropertyName())
                        - prioritizedDefinitionNames.indexOf(p2.getPropertyName());
            }
            return (i != 0) ? i : p1.getPropertyId().compareTo(p2.getPropertyId());
        }
        // different types - just put one ahead of the other - otherwise,
        // when the treeMap uses this comparator, it throws everything
        // away...
        return 1;
    }


}