
package edu.mayo.informatics.lexgrid.convert.directConversions.claml.interfaces;

import org.LexGrid.LexBIG.claml.Rubric;
import org.LexGrid.commonTypes.Property;

public interface RubricProcessor {

	public Property processRubric(Rubric rubric);
}