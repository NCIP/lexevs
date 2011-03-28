package org.cts2.profile.query;

import org.cts2.map.MapVersionDirectory;
import org.cts2.map.MapVersionList;
import org.cts2.service.core.NameOrURIList;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;
import org.cts2.uri.MapVersionDirectoryURI;

/**
 * The Interface CodeSystemVersionQuery.
 * 
 * @author <a href="mailto:lian.zonghui@mayo.edu">Zonghui Lian</a>
 */
public interface MapVersionQueryService extends
		BaseQueryService<MapVersionDirectoryURI> {
	public MapVersionDirectory resolve(MapVersionDirectoryURI directory,
			QueryControl queryControl, ReadContext readContext);

	public MapVersionList resolveAsList(MapVersionDirectoryURI directory,
			QueryControl queryControl, ReadContext readContext);

	public void restrictToCodeSystems(MapVersionDirectoryURI directory,
			NameOrURIList codeSystems);
	
	
}
