package org.lexevs.dao.index.access;

import org.lexevs.dao.index.version.LexEvsIndexFormatVersion;

public interface LexEvsIndexFormatVersionAwareDao {

	public boolean supportsLexEvsIndexFormatVersion(LexEvsIndexFormatVersion version);

}
