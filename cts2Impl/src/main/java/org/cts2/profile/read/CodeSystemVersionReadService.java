package org.cts2.profile.read;

import org.cts2.codesystemversion.CodeSystemVersion;
import org.cts2.service.core.NameOrURI;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;

public interface CodeSystemVersionReadService extends BaseReadService<CodeSystemVersion> {

	public boolean existsCodeSystemVersionForCodeSystem(NameOrURI codeSystem, NameOrURI tag, ReadContext context);
	
	public boolean existsVersionId(NameOrURI codeSystem, String officialResourceVersionId, ReadContext context);
	
	public boolean getCodeSystemVersionByVersionId(NameOrURI codeSystem, String officialResourceVersionId, QueryControl queryControl);
	
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
