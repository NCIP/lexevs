
package org.lexgrid.loader.meta.staging.processor;

import java.util.List;

import org.lexgrid.loader.rrf.staging.MrconsoStagingDao;

public interface MetaMrconsoStagingDao extends MrconsoStagingDao {

	public List<String> getMetaRootCuis();
}