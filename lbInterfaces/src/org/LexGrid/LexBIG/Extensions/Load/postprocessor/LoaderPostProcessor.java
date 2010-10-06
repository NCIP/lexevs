package org.LexGrid.LexBIG.Extensions.Load.postprocessor;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Extensions.Generic.GenericExtension;
import org.LexGrid.LexBIG.Extensions.Load.OntologyFormat;

public interface LoaderPostProcessor extends GenericExtension {

	public void runPostProcess(AbsoluteCodingSchemeVersionReference reference, OntologyFormat ontFormat);
}
