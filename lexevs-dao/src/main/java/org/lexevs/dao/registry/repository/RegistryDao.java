package org.lexevs.dao.registry.repository;

import java.util.Calendar;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.lexevs.dao.registry.model.CodingSchemeEntry;

public interface RegistryDao {

	public void updateLastUpdateTime(Calendar lastUpdateTime);
	
	public Calendar getLastUpdateTime();
	
	public CodingSchemeEntry getCodingSchemeEntry(AbsoluteCodingSchemeVersionReference entry);
	
	public void insertCodingSchemeEntry(CodingSchemeEntry entry);
	
	public void removeCodingSchemeEntry(AbsoluteCodingSchemeVersionReference entry);
	
	public void updateTag(AbsoluteCodingSchemeVersionReference entry, String newTag);
}
