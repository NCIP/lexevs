package org.cts2.profile.read;

import org.cts2.codesystemversion.CodeSystemVersion;
import org.cts2.entity.EntityDescription;
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
}
