
package org.lexgrid.loader.umls.processor;

import org.lexgrid.loader.rrf.processor.AbstractMrhierProcessor;
import org.lexgrid.loader.rrf.staging.MrconsoStagingDao;

/**
 * The Class MrhierAssocAndPropQualProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrhierAssocAndPropQualProcessor extends AbstractMrhierProcessor {
	
	/** The mrconso staging dao. */
	private MrconsoStagingDao mrconsoStagingDao;

	/* TODO: Handle Mrhier
	@Override
	public String getInitalPathElementCode(Mrhier mrhier) {
		return mrconsoStagingDao.getCodeFromAui(mrhier.getAui());
	}

	@Override
	public List<String> getPathToRoot(String pathToRoot) {
		List<String> returnPtr = new ArrayList<String>();

		for(String aui : this.getPathToRootAuis(pathToRoot)) {
			returnPtr.add(mrconsoStagingDao.getCodeFromAui(aui));
		}
		return returnPtr;
	}

	@Override
	public String getPropertyId(Mrhier mrhier, int pos) {
		return this.getAuiFromPos(mrhier, pos);
	}
	*/

	public MrconsoStagingDao getMrconsoStagingDao() {
		return mrconsoStagingDao;
	}

	public void setMrconsoStagingDao(MrconsoStagingDao mrconsoStagingDao) {
		this.mrconsoStagingDao = mrconsoStagingDao;
	}
}