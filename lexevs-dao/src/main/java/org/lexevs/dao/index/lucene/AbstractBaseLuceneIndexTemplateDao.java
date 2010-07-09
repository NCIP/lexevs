package org.lexevs.dao.index.lucene;

import org.lexevs.dao.index.access.AbstractBaseIndexDao;
import org.lexevs.dao.index.lucenesupport.LuceneIndexTemplate;

public abstract class AbstractBaseLuceneIndexTemplateDao extends AbstractBaseIndexDao {

	protected abstract LuceneIndexTemplate getLuceneIndexTemplate(String codingSchemeUri, String version);
}
