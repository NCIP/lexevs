package org.lexevs.dao.registry.operation;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Impl.helpers.SQLConnectionInfo;

public interface Registry {
	
	public SQLConnectionInfo getSQLConnectionInfoForCodeSystem(AbsoluteCodingSchemeVersionReference codingSchemeVersion);
}
