package org.cts2.internal.profile.read;

import java.net.URI;
import java.util.List;

import org.cts2.core.NameOrURI;
import org.cts2.core.NameOrURIList;
import org.cts2.core.VersionTagReference;
import org.cts2.profile.read.ValueSetDefinitionReadService;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;
import org.cts2.uri.DirectoryURI;
import org.cts2.valueset.ResolvedValueSetDirectory;
import org.cts2.valueset.ValueSetDefinition;

public class LexEvsValueSetDefinitionReadService extends AbstractBaseReadService<ValueSetDefinition> 
	implements ValueSetDefinitionReadService {

	@Override
	public Boolean exists(URI valueSetDefinitionURI, ReadContext context) {
		
		List<String> uris = this.getLexEVSValueSetDefinitionService().listValueSetDefinitionURIs();
		for (String uri : uris)
		{
			System.out.println("uri : " + uri);
		}
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean existsDefinitionForValueSet(NameOrURI valueSet,
			NameOrURI tag, ReadContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValueSetDefinition getDefinitionForValueSet(NameOrURI valueSet,
			NameOrURI tag, QueryControl queryControl, ReadContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DirectoryURI getEntities(URI valueSetDefinitionURI,
			NameOrURIList codeSystemVersionList, VersionTagReference tag,
			QueryControl queryControl, ReadContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValueSetDefinition read(URI valueSetDefinitionURI,
			QueryControl queryControl, ReadContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResolvedValueSetDirectory resolve(URI valueSetDefinitionURI,
			NameOrURIList codeSystemVersionList, VersionTagReference tag,
			QueryControl queryControl, ReadContext context) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static void main(String[] args){
		ValueSetDefinitionReadService vsdrs = new LexEvsValueSetDefinitionReadService();
		
		vsdrs.exists(null, null);
	}
}
