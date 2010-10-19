/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.lexgrid.loader.writer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.io.FileUtils;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.core.io.Resource;

public class NoClosingRootTagStaxEventItemWriter<T> extends StaxEventItemWriter<T> {

	private File xmlFile;	
	
	protected void endDocument(XMLEventWriter writer) throws XMLStreamException {
		//no-op -- take care of this on close
	}
	
	@Override
	public void close() {
		super.close();
		
		String rootTag = "</" + getRootTagName() + ">";
		
		String xml = null;
		try {
			 xml = FileUtils.readFileToString(xmlFile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		if(!xml.endsWith(rootTag)){
			try {
				FileOutputStream os = new FileOutputStream(xmlFile, true);
				FileChannel channel = os.getChannel();
				
				ByteBuffer byteBuffer = ByteBuffer.wrap(rootTag.getBytes());
				channel.write(byteBuffer);
				channel.close();
				
				xml = FileUtils.readFileToString(xmlFile);
			}
			catch (IOException ioe) {
				throw new RuntimeException(ioe);
			}
		}
	}
	
	//No public getter on the Resource -- so I have to steal it here.
	//Not sure why Spring Batch did it this way...
	@Override
	public void setResource(Resource resource){
		try {
			this.xmlFile = resource.getFile();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		super.setResource(resource);
	}
}