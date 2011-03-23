package org.cts2.internal.profile.read;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.apache.commons.lang.StringUtils;
import org.cts2.core.NameOrURI;
import org.cts2.internal.model.resource.factory.ValueSetDefinitionFactory;
import org.cts2.profile.read.ValueSetDefinitionReadService;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;
import org.cts2.utility.ExecutionUtils;
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
		
		return this.inContext(vsd, context);
	}
	
	private Boolean inContext(org.LexGrid.valueSets.ValueSetDefinition vsd, ReadContext context){
		if (vsd == null)
			return null;
		
		// check for active or not active
				
		// check for changeSetURI
		String changeSetContext = context.getChangeSetContext();
		if (StringUtils.isNotEmpty(changeSetContext))
		{
			if (vsd.getEntryState() == null)
				return false;
			else if (StringUtils.isEmpty(vsd.getEntryState().getContainingRevision()) 
					|| !vsd.getEntryState().getContainingRevision().equalsIgnoreCase(changeSetContext))
				return false;
		}
		// check for referenceLanguage
		
		// check for referenceTime
		
		return true;
	}

	@Override
	public Boolean existsDefinitionForValueSet(NameOrURI valueSet,
			NameOrURI tag, ReadContext context) {
		org.LexGrid.valueSets.ValueSetDefinition vsd = null;
		
		if (valueSet == null)
			return null;
		
		try {
			if (valueSet.getUri() != null)
			{
				vsd = this.getLexEVSValueSetDefinitionService().getValueSetDefinition(new URI(valueSet.getUri()), null);
				if (vsd != null)
					return true;
			}
			else
			{
				List<String> vsds = this.getLexEVSValueSetDefinitionService().listValueSetDefinitions(valueSet.getName());
				if (!vsds.isEmpty())
					return true;
			}
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		

		return null;
	}

	@Override
	public ValueSetDefinition getDefinitionForValueSet(final NameOrURI valueSet,
			NameOrURI tag, QueryControl queryControl, ReadContext context) {
		if (valueSet == null)
			return null;
		
		return ExecutionUtils.callWithTimeout(new Callable<ValueSetDefinition>(){

			@Override
			public ValueSetDefinition call() throws Exception {
				return valueSetDefinitionFactory.getValueSetDefinition(valueSet);
			}
			
		}, queryControl, TimeUnit.MILLISECONDS);
	}

	/*
	 * (non-Javadoc)
	 * @see org.cts2.profile.read.ValueSetDefinitionReadService#read(java.net.URI, org.cts2.service.core.QueryControl, org.cts2.service.core.ReadContext)
	 */
	@Override
	public ValueSetDefinition read(final URI valueSetDefinitionURI,
			QueryControl queryControl, ReadContext context) {
		
		return ExecutionUtils.callWithTimeout(new Callable<ValueSetDefinition>(){

			@Override
			public ValueSetDefinition call() throws Exception {
				return valueSetDefinitionFactory.getValueSetDefinition(valueSetDefinitionURI);
			}
			
		}, queryControl, TimeUnit.MILLISECONDS);	
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
