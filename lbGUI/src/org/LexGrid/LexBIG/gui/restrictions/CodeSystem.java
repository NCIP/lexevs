
package org.LexGrid.LexBIG.gui.restrictions;

/**
 * Holder to store the Code System restrictions.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class CodeSystem extends Restriction {
	public String codeSystem;
	public int type = 0;

	public static final int CODE_SYSTEM = 0;
	public static final int SOURCE_CODE_SYSTEM = 1;
	public static final int TARGET_CODE_SYSTEM = 2;

	public CodeSystem(int type) {
		this.type = type;
	}

	@Override
	public String toString() {
		if (type == CODE_SYSTEM) {
			return "Restrict to code system '" + codeSystem + "'";
		} else if (type == SOURCE_CODE_SYSTEM) {
			return "Restrict to source code system '" + codeSystem + "'";
		} else if (type == TARGET_CODE_SYSTEM) {
			return "Restrict to target system '" + codeSystem + "'";
		} else {
			return "Error - invalid type";
		}
	}
}