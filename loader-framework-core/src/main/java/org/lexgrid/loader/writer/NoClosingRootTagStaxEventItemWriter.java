
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