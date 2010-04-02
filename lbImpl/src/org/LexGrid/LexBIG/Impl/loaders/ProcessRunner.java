package org.LexGrid.LexBIG.Impl.loaders;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;

public interface ProcessRunner {

    public StatusReportingCallback runProcess(AbsoluteCodingSchemeVersionReference codingSchemeVersion)
        throws LBParameterException;
}
