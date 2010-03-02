package org.lexevs.dao.index.access;

import java.util.List;

import org.lexevs.dao.index.version.LexEvsIndexFormatVersion;

public abstract class AbstractBaseIndexDao implements LexEvsIndexFormatVersionAwareDao{

	public boolean supportsLexEvsIndexFormatVersion(LexEvsIndexFormatVersion version) {
		for(LexEvsIndexFormatVersion supportedVersion : doGetSupportedLexEvsIndexFormatVersions()){
			if(version.isEqualVersion(supportedVersion)){
				return true;
			}
		}
		return false;
	}
	
	public abstract List<LexEvsIndexFormatVersion> doGetSupportedLexEvsIndexFormatVersions();
}
