package org.lexevs.dao.index.lucene.v2009.entity;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.dao.index.access.entity.EntityDao;
import org.lexevs.dao.index.lucene.v2010.entity.LuceneEntityDao;
import org.lexevs.dao.index.version.LexEvsIndexFormatVersion;

public class LuceneEntity2009Dao extends LuceneEntityDao implements EntityDao {
	
	public static LexEvsIndexFormatVersion supportedIndexVersion2009 = LexEvsIndexFormatVersion.parseStringToVersion("2009");

	@Override
	public Query getMatchAllDocsQuery(
			AbsoluteCodingSchemeVersionReference reference) {
		return new MatchAllDocsQuery();
	}
	
	@Override
	public List<LexEvsIndexFormatVersion> doGetSupportedLexEvsIndexFormatVersions() {
		return DaoUtility.createList(LexEvsIndexFormatVersion.class, supportedIndexVersion2009);
	}
}
