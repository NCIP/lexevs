package org.lexevs.dao.database.access.association.model;

import com.arangodb.entity.DocumentField;
import com.arangodb.entity.DocumentField.Type;


public class NodeEdge {
	@DocumentField(Type.ID)
	private String id;

	@DocumentField(Type.KEY)
	private String predicateUid;

	@DocumentField(Type.REV)
	private String revision;

	@DocumentField(Type.FROM)
	private String from;

	@DocumentField(Type.TO)
	private String to;

	private Boolean theFalse;
	private Boolean theTruth;
	private String associationName;

	public NodeEdge(final String from, final String to, final Boolean theFalse, final Boolean theTruth,
		final String label) {
		this.from = from;
		this.to = to;
		this.theFalse = theFalse;
		this.theTruth = theTruth;
		this.associationName = label;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the predicateUid
	 */
	public String getPredicateUid() {
		return predicateUid;
	}

	/**
	 * @param predicateUid the predicateUid to set
	 */
	public void setPredicateUid(String predicateUid) {
		this.predicateUid = predicateUid;
	}

	/**
	 * @return the revision
	 */
	public String getRevision() {
		return revision;
	}

	/**
	 * @param revision the revision to set
	 */
	public void setRevision(String revision) {
		this.revision = revision;
	}

	/**
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * @param from the from to set
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * @return the to
	 */
	public String getTo() {
		return to;
	}

	/**
	 * @param to the to to set
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * @return the theFalse
	 */
	public Boolean getTheFalse() {
		return theFalse;
	}

	/**
	 * @param theFalse the theFalse to set
	 */
	public void setTheFalse(Boolean theFalse) {
		this.theFalse = theFalse;
	}

	/**
	 * @return the theTruth
	 */
	public Boolean getTheTruth() {
		return theTruth;
	}

	/**
	 * @param theTruth the theTruth to set
	 */
	public void setTheTruth(Boolean theTruth) {
		this.theTruth = theTruth;
	}

	/**
	 * @return the associationName
	 */
	public String getAssociationName() {
		return associationName;
	}

	/**
	 * @param associationName the associationName to set
	 */
	public void setAssociationName(String associationName) {
		this.associationName = associationName;
	}
}