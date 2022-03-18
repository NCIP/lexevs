
package org.LexGrid.LexBIG.Impl.loaders;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Load.OntologyFormat;

public interface ProcessRunner {

    public StatusReportingCallback runProcess(
            AbsoluteCodingSchemeVersionReference codingSchemeVersion,
            OntologyFormat format)
        throws LBParameterException;
}