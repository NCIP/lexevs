package org.LexGrid.LexBIG.admin;

import java.lang.reflect.Field;
import java.rmi.UnexpectedException;

import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.logging.LoggerFactory;


public class TestAdminUtilDisplayAndLog extends LexBIGServiceTestCase {
	LgLoggerIF logger;
	Logger forjlogger;
	
	@Before
	public void setUp() throws Exception {
		Field field = Util.class.getDeclaredField("_logger");
		field.setAccessible(true);
		logger = (LgLoggerIF) field.get(null);
		Field forjfield = logger.getClass().getDeclaredField("info_");
		forjfield.setAccessible(true);
		forjlogger = (Logger) forjfield.get(logger);
	}

	@Test
	public void test() throws Exception {
//		LgLoggerIF logger = LoggerFactory.getLogger();
//		logger.fatalAndThrowException("very bad exception", new UnexpectedException("Fatal Unexpected Exception"));
		
	}

	@Override
	protected String getTestID() {
		return "Logging Tests";
	}

}
