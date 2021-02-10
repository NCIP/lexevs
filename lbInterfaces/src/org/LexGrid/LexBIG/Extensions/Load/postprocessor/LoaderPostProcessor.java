
package org.LexGrid.LexBIG.Extensions.Load.postprocessor;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Extensions.Generic.GenericExtension;
import org.LexGrid.LexBIG.Extensions.Load.OntologyFormat;

/**
 * The Interface LoaderPostProcessor allows user defined process logic 
 * to be injected into a Loader load process in the form of an Extension.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface LoaderPostProcessor extends GenericExtension {

	/**
	 * Executes a Loader post process. Loader post processes can be used to modify
	 * database content, do extra cleanup, or send notifications, for example.
	 * 
	 * NOTE: Post Process error/exception conditions will not effect Loader status.
	 * 
	 * Implementors can assume that database content has been loaded at the point of
	 * this call, but the load is not yet in a completed state and Lucene indexing
	 * has not been done.
	 * 
	 * @param reference the uri/version of the requesting Coding Scheme
	 * @param ontFormat the OntologyFormat of the requesting Loader
	 */
	public void runPostProcess(AbsoluteCodingSchemeVersionReference reference, OntologyFormat ontFormat);
}