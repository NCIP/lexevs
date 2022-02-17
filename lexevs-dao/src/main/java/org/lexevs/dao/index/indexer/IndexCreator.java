
package org.lexevs.dao.index.indexer;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.concepts.Entity;

/**
 * The Interface IndexCreator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface IndexCreator {

	public enum IndexOption {ENTITY,SEARCH,BOTH}
	
	public String index(AbsoluteCodingSchemeVersionReference reference);

	public String index(AbsoluteCodingSchemeVersionReference reference, IndexOption option);
	
	public String index(AbsoluteCodingSchemeVersionReference reference, EntityIndexerProgressCallback callback);
	
	public String index(AbsoluteCodingSchemeVersionReference reference, EntityIndexerProgressCallback callback, IndexOption option);

	public String index(AbsoluteCodingSchemeVersionReference reference, EntityIndexerProgressCallback callback, boolean onlyRegister);
	
	public String index(AbsoluteCodingSchemeVersionReference reference, EntityIndexerProgressCallback callback, boolean onlyRegister, IndexOption option);
	
	public interface EntityIndexerProgressCallback {
		public void onEntityIndex(Entity entity);
	}
}