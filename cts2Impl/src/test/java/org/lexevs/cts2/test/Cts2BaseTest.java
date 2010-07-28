package org.lexevs.cts2.test;

import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;

public class Cts2BaseTest {

	protected LexBIGService getLexBIGService() {
		return LexBIGServiceImpl.defaultInstance();
	}
}
