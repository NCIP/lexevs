
package org.lexevs.dao.index.lucene.v2009.entity;

import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.dao.index.access.entity.EntityDao;
import org.lexevs.dao.index.lucene.v2010.entity.LuceneEntityDao;
import org.lexevs.dao.index.lucenesupport.LuceneIndexTemplate;
import org.lexevs.dao.index.version.LexEvsIndexFormatVersion;

import java.util.List;

/**
 * The Class LuceneEntity2009Dao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LuceneEntity2009Dao extends LuceneEntityDao implements EntityDao {
	
	/** The supported index version2009. */
	public static LexEvsIndexFormatVersion supportedIndexVersion2009 = LexEvsIndexFormatVersion.parseStringToVersion("2009");

	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.lucene.v2010.entity.LuceneEntityDao#doGetSupportedLexEvsIndexFormatVersions()
	 */
	@Override
	public List<LexEvsIndexFormatVersion> doGetSupportedLexEvsIndexFormatVersions() {
		return DaoUtility.createList(LexEvsIndexFormatVersion.class, supportedIndexVersion2009);
	}

	@Override
	protected LuceneIndexTemplate getLuceneIndexTemplate(
			String codingSchemeUri, String version) {
		throw new UnsupportedOperationException("Lucene version 2009 no longer supported.");
	}

}