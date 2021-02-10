
package org.lexgrid.loader.umls.reader.support;

import org.lexgrid.loader.reader.support.GroupDiscriminator;
import org.lexgrid.loader.rrf.model.Mrrel;

/**
 * The Class UmlsMrrelGroupDiscriminator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class UmlsMrrelGroupDiscriminator implements GroupDiscriminator<Mrrel> {

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.reader.support.GroupDiscriminator#getDiscriminatingValue(java.lang.Object)
	 */
	public String getDiscriminatingValue(Mrrel item) {
		return item.getCui1();
	}
	
	/**
	 * The Class CuiAuiPair.
	 * 
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	private class CuiAuiPair {
		
		/** The cui. */
		private String cui;
		
		/** The aui. */
		private String aui;

		/**
		 * Gets the cui.
		 * 
		 * @return the cui
		 */
		public String getCui() {
			return cui;
		}

		/**
		 * Sets the cui.
		 * 
		 * @param cui the new cui
		 */
		public void setCui(String cui) {
			this.cui = cui;
		}

		/**
		 * Gets the aui.
		 * 
		 * @return the aui
		 */
		public String getAui() {
			return aui;
		}

		/**
		 * Sets the aui.
		 * 
		 * @param aui the new aui
		 */
		public void setAui(String aui) {
			this.aui = aui;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final CuiAuiPair other = (CuiAuiPair) obj;
			if (aui == null) {
				if (other.aui != null)
					return false;
			} else if (!aui.equals(other.aui))
				return false;
			if (cui == null) {
				if (other.cui != null)
					return false;
			} else if (!cui.equals(other.cui))
				return false;
			return true;
		}
	}
}