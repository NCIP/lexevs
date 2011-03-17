package org.cts2.internal.profile.read;

import java.net.URI;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.cts2.core.NameOrURI;
import org.cts2.core.NameOrURIList;
import org.cts2.core.VersionTagReference;
import org.cts2.internal.model.resource.factory.ValueSetDefinitionFactory;
import org.cts2.profile.read.ValueSetDefinitionReadService;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;
import org.cts2.valueset.ResolvedValueSetDirectory;
import org.cts2.valueset.ValueSetDefinition;

public class LexEvsValueSetDefinitionReadService extends AbstractBaseReadService<ValueSetDefinition> 
	implements ValueSetDefinitionReadService {

	/** The code system version factory. */
	private ValueSetDefinitionFactory valueSetDefinitionFactory;
	
	@Override
	public Boolean exists(URI valueSetDefinitionURI, ReadContext context) {
		org.LexGrid.valueSets.ValueSetDefinition vsd = null;
		try {
			 vsd = this.getLexEVSValueSetDefinitionService().getValueSetDefinition(valueSetDefinitionURI, null);
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (vsd != null)
		{
			if (context != null)
				return this.inContext(vsd, context);
			else
				return true;
		}

		return false;
	}
	
	private Boolean inContext(org.LexGrid.valueSets.ValueSetDefinition vsd, ReadContext context){
		// check for active or not active
		
//		context.getActive().
		
		// check for changeSetURI
		
		// check for referenceLanguage
		
		// check for referenceTime
		
		return false;
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
	public ValueSetDefinition read(URI valueSetDefinitionURI,
			QueryControl queryControl, ReadContext context) {
		return this.valueSetDefinitionFactory.getValueSetDefinition(valueSetDefinitionURI);
		
		// TODO Auto-generated method stub
//		return null;
	}

	@Override
	public ResolvedValueSetDirectory resolve(URI valueSetDefinitionURI,
			NameOrURIList codeSystemVersionList, VersionTagReference tag,
			QueryControl queryControl, ReadContext context) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * @return the valueSetDefinitionFactory
	 */
	public ValueSetDefinitionFactory getValueSetDefinitionFactory() {
		return valueSetDefinitionFactory;
	}

	/**
	 * @param valueSetDefinitionFactory the valueSetDefinitionFactory to set
	 */
	public void setValueSetDefinitionFactory(
			ValueSetDefinitionFactory valueSetDefinitionFactory) {
		this.valueSetDefinitionFactory = valueSetDefinitionFactory;
	}
}
