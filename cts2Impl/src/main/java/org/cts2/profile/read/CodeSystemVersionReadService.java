package org.cts2.profile.read;

import org.cts2.codesystemversion.CodeSystemVersion;
import org.cts2.service.core.NameOrURI;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;

public interface CodeSystemVersionReadService extends BaseReadService<CodeSystemVersion> {

	public boolean existsCodeSystemVersionForCodeSystem(NameOrURI codeSystem, NameOrURI tag, ReadContext context);
	
	public boolean existsExternalId(NameOrURI codeSystem, String externalIdentifier, ReadContext context);
	
	public boolean getCodeSystemVersionByExternalId(NameOrURI codeSystem, String externalIdentifier, QueryControl queryControl);
	
	public boolean getCodeSystemVersionForCodeSystem(NameOrURI codeSystem, NameOrURI tag, QueryControl queryControl);
	
	public CodeSystemVersion read(
			NameOrURI id, 
			QueryControl queryControl, 
			ReadContext readContext);

	public boolean exists(
			NameOrURI id, 
			QueryControl queryControl,
			ReadContext readContext);
}
