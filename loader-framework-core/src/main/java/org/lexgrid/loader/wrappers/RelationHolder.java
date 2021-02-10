
package org.lexgrid.loader.wrappers;

/**
 * The Class RelationHolder.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class RelationHolder {
	
	/** The source. */
	private String source;
	
	/** The source namespace. */
	private String sourceNamespace;
	
	/** The target. */
	private String target;
	
	/** The target namespace. */
	private String targetNamespace;
	
	/** The relation. */
	private String relation;
	
	/** The relation namespace. */
	private String relationNamespace;
	
	/** The source scheme. */
	private String sourceScheme;
	
	/**
	 * Gets the source namespace.
	 * 
	 * @return the source namespace
	 */
	public String getSourceNamespace() {
		return sourceNamespace;
	}
	
	/**
	 * Sets the source namespace.
	 * 
	 * @param sourceNamespace the new source namespace
	 */
	public void setSourceNamespace(String sourceNamespace) {
		this.sourceNamespace = sourceNamespace;
	}
	
	/**
	 * Gets the target namespace.
	 * 
	 * @return the target namespace
	 */
	public String getTargetNamespace() {
		return targetNamespace;
	}
	
	/**
	 * Sets the target namespace.
	 * 
	 * @param targetNamespace the new target namespace
	 */
	public void setTargetNamespace(String targetNamespace) {
		this.targetNamespace = targetNamespace;
	}
	
	/**
	 * Gets the relation namespace.
	 * 
	 * @return the relation namespace
	 */
	public String getRelationNamespace() {
		return relationNamespace;
	}
	
	/**
	 * Sets the relation namespace.
	 * 
	 * @param relationNamespace the new relation namespace
	 */
	public void setRelationNamespace(String relationNamespace) {
		this.relationNamespace = relationNamespace;
	}
	
	/**
	 * Gets the source.
	 * 
	 * @return the source
	 */
	public String getSource() {
		return source;
	}
	
	/**
	 * Sets the source.
	 * 
	 * @param source the new source
	 */
	public void setSource(String source) {
		this.source = source;
	}
	
	/**
	 * Gets the target.
	 * 
	 * @return the target
	 */
	public String getTarget() {
		return target;
	}
	
	/**
	 * Sets the target.
	 * 
	 * @param target the new target
	 */
	public void setTarget(String target) {
		this.target = target;
	}
	
	/**
	 * Gets the relation.
	 * 
	 * @return the relation
	 */
	public String getRelation() {
		return relation;
	}
	
	/**
	 * Sets the relation.
	 * 
	 * @param relation the new relation
	 */
	public void setRelation(String relation) {
		this.relation = relation;
	}
	
	/**
	 * Gets the source scheme.
	 * 
	 * @return the source scheme
	 */
	public String getSourceScheme() {
		return sourceScheme;
	}
	
	/**
	 * Sets the source scheme.
	 * 
	 * @param sourceScheme the new source scheme
	 */
	public void setSourceScheme(String sourceScheme) {
		this.sourceScheme = sourceScheme;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((relation == null) ? 0 : relation.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		result = prime * result
				+ ((sourceScheme == null) ? 0 : sourceScheme.hashCode());
		result = prime * result + ((target == null) ? 0 : target.hashCode());
		return result;
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
		final RelationHolder other = (RelationHolder) obj;
		if (relation == null) {
			if (other.relation != null)
				return false;
		} else if (!relation.equals(other.relation))
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		if (sourceScheme == null) {
			if (other.sourceScheme != null)
				return false;
		} else if (!sourceScheme.equals(other.sourceScheme))
			return false;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		return true;
	}
	
	
}