
package org.lexgrid.valuesets.dto;

import java.net.URI;
import java.util.List;

import org.LexGrid.commonTypes.Source;

/**
 * A reference to a value set definition.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class VSDSummary implements java.io.Serializable {

	// --------------------------/
	// - Class/Member Variables -/
	// --------------------------/

	/**
	 * The URI of the value Set Definition
	 */
	private URI valueSetDefinitionURI_;

	private List<Source> source_;

	private List<String> representsRealmOrContext_;

	private String defaultCodingScheme_;

	private String valueSetDefinitionName_;

	public VSDSummary() {
		super();
	}

	public URI getValueSetDefinitionURI() {
		return valueSetDefinitionURI_;
	}

	public void setValueSetDefinitionURI(URI valueSetDefinitionURI) {
		this.valueSetDefinitionURI_ = valueSetDefinitionURI;
	}

	public List<Source> getSource() {
		return source_;
	}

	public void setSource(List<Source> source) {
		this.source_ = source;
	}

	public List<String> getRepresentsRealmOrContext() {
		return representsRealmOrContext_;
	}

	public void setRepresentsRealmOrContext(
			List<String> representsRealmOrContext) {
		this.representsRealmOrContext_ = representsRealmOrContext;
	}

	public String getDefaultCodingScheme() {
		return defaultCodingScheme_;
	}

	public void setDefaultCodingScheme(String defaultCodingScheme) {
		this.defaultCodingScheme_ = defaultCodingScheme;
	}

	public String getValueSetDefinitionName() {
		return valueSetDefinitionName_;
	}

	public void setValueSetDefinitionName(String valueSetDefinitionName) {
		this.valueSetDefinitionName_ = valueSetDefinitionName;
	}

	// -----------/
	// - Methods -/
	// -----------/

}