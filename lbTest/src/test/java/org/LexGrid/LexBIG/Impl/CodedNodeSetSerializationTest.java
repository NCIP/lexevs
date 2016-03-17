package org.LexGrid.LexBIG.Impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.junit.Test;

import junit.framework.TestCase;

public class CodedNodeSetSerializationTest extends TestCase {

	@Test
	public void test() throws IOException {
		CodedNodeSetImpl impl = new CodedNodeSetImpl();
		new ObjectOutputStream(new ByteArrayOutputStream()).writeObject(impl);
	}

}
