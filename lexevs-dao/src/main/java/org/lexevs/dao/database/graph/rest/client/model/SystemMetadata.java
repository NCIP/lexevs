package org.lexevs.dao.database.graph.rest.client.model;

import java.util.List;

public class SystemMetadata {
	private List<String> dataBases;

	/**
	 * @return the dataBases
	 */
	public List<String> getDataBases() {
		return dataBases;
	}

	/**
	 * @param dataBases the dataBases to set
	 */
	public void setDataBases(List<String> dataBases) {
		this.dataBases = dataBases;
	}

}
