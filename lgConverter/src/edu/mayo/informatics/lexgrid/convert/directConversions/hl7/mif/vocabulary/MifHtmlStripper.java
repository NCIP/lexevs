package edu.mayo.informatics.lexgrid.convert.directConversions.hl7.mif.vocabulary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.Source;
import javax.xml.transform.Result;
import javax.xml.transform.OutputKeys;

import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;

public class MifHtmlStripper {
    private String source;
    private LgMessageDirectorIF messages;

    public MifHtmlStripper(LgMessageDirectorIF messages, String source) {
        this.source = source;
        this.messages = messages;
    }

    public String transformXML() {
        String filePath = null;
        System.setProperty("com.icl.saxon.TransformerFactoryImpl", "className");
        
        try {
            filePath = getFilePath();
            File file = new File(filePath);
            FileOutputStream fileStream = new FileOutputStream(file);
            Source input = new StreamSource(source);
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
        }

        return filePath;
    }

    private String getFilePath() throws FileNotFoundException {
        File file = new File(source);
        if (!file.exists()) {
            throw new FileNotFoundException("No Mif source file is found at: " + file.getAbsolutePath());
        }

        return source.replace(file.getName(), "TransformedMif.xml");
    }

    public static void main(String[] argv) {

        if (argv.length != 1) {
            System.err.println("Source file argument required");
            System.exit(1);
        }
        
        MifHtmlStripper transformer = new MifHtmlStripper(null, argv[0]);
        System.out.println(transformer.transformXML());
    } // main

}
