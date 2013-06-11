package edu.mayo.informatics.lexgrid.convert.directConversions.hl7.mif.vocabulary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.Source;
import javax.xml.transform.Result;
import javax.xml.transform.OutputKeys;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;

public class MifHtmlStripper {
    private URI source;
    private LgMessageDirectorIF messages;

    public MifHtmlStripper(LgMessageDirectorIF messages, URI source) {
        this.source = source;
        this.messages = messages;
    }

    public URI transformXML() {
      URI uri = null;
        System.setProperty("com.icl.saxon.TransformerFactoryImpl", "className");
        
        try {
            uri = getURI();

            File newFile = new File(uri);
            FileOutputStream fileStream = new FileOutputStream(newFile);
            Source input = new StreamSource(source.getPath());
            Source xsl = new StreamSource(MifHtmlStripper.class.getResourceAsStream("/Striphtml.xsl"));
            Result output = new StreamResult(fileStream);

            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(xsl);
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(input, output);
            
        } catch (TransformerException te) {
            System.out.println("Transformer exception: " + te.getMessage());
            messages.fatal(te.getMessage());
            throw new RuntimeException(te);
        } catch (FileNotFoundException e) {
            messages.fatal("Adequate permissions to write to this file may not be available" + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            messages.fatal("The path to this file has a problem and may need to be formatted as a URI" + e.getMessage());
           throw new RuntimeException(e);
        }

        return uri;
    }

    private URI getURI() throws URISyntaxException {
     String path =  source.toString();
      File file = new File(source);
      path = path.replace(file.getName(), "TransformedMif.xml");
      return new URI(path);
    }

    public static void main(String[] argv) {

        if (argv.length != 1) {
            System.err.println("Source file argument required");
            System.exit(1);
        }
        URI uri = null;
        try {
            uri = new URI(argv[0]);
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        MifHtmlStripper transformer = new MifHtmlStripper(null, uri);
            System.out.println(transformer.transformXML().getPath());

    } // main

}
