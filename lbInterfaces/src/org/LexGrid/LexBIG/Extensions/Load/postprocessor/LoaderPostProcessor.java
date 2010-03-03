package org.LexGrid.LexBIG.Extensions.Load.postprocessor;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Extensions.Generic.GenericExtension;

public interface LoaderPostProcessor extends GenericExtension {

	public void runPostProcess(AbsoluteCodingSchemeVersionReference reference);
}
