
package org.LexGrid.LexBIG.admin;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.WriterAppender;
import org.junit.Before;
import org.junit.Test;


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
	public void test() {
		Layout layout = new PatternLayout();
		StringWriter stringWriter = new StringWriter();
		WriterAppender writerAppender = new WriterAppender(layout, stringWriter);
		forjlogger.addAppender(writerAppender);
		Util.displayAndLogMessage("Not yet implemented");
		assertEquals(true, stringWriter.toString().contains("Not yet implemented"));
		System.out.println(stringWriter.toString());
	}

	@Override
	protected String getTestID() {
		return "Logging Tests";
	}

}