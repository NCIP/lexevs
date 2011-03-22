package org.cts2.profile.read;

import org.cts2.codesystemversion.CodeSystemVersion;
import org.cts2.core.EntityReference;
import org.cts2.entity.EntityDescription;
import org.cts2.entity.EntityList;
import org.cts2.service.core.EntityNameOrURI;
import org.cts2.service.core.NameOrURI;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;

public interface EntityDescriptionReadService extends BaseReadService<CodeSystemVersion> {

	public EntityDescription read(
			EntityNameOrURI id, 
			NameOrURI codeSystemVersion, 
			QueryControl queryControl,
			ReadContext readContext);

	public boolean exists(
			EntityNameOrURI id, 
			NameOrURI codeSystemVersion, 
			QueryControl queryControl,
			ReadContext readContext);
	
	public EntityList readEntityDescriptions(
			EntityNameOrURI id,
			QueryControl queryControl,
			ReadContext context
			);
	
	public EntityReference availableDescriptions(
			EntityNameOrURI id,
			ReadContext context);
	
	public EntityDescription readByCodeSystem(
			EntityNameOrURI id,
			NameOrURI codeSystem,
			NameOrURI tag,
			QueryControl queryControl,
			ReadContext context);
	
	public boolean existsInCodeSystem(
			EntityNameOrURI id,
			NameOrURI codeSystem,
			NameOrURI tag,
			ReadContext context);
}
