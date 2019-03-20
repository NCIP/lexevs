package org.LexGrid.LexBIG.admin;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

import static org.mockito.Matchers.startsWith;
import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestEhcacheStartUpOutputRemoval {

    private PrintStream sysOut;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    	@Before
        public void setUpStreams() {
            sysOut = System.out;
            System.setOut(new PrintStream(outContent));
        }
    	
    	   @After
    	    public void revertStreams() {
    	        System.setOut(sysOut);
    	    }
	
		@Test
		public void out() throws LBInvocationException {
	        ListSchemes.main(new String[]{});
	        assertTrue(outContent.toString().contains("Local Name") ||
	        		outContent.toString().contains("No coding schemes found"));
	        assertFalse(outContent.toString().contains("ehcache"));
		}

}
