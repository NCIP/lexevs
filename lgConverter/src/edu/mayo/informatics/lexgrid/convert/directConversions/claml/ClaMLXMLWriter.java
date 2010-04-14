package edu.mayo.informatics.lexgrid.convert.directConversions.claml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.LexGrid.LexBIG.claml.ClassKinds;
import org.LexGrid.LexBIG.claml.RubricKinds;

/**
 * Writes ClaML metadata to file
 */
public class ClaMLXMLWriter {

    private BufferedWriter writer_;

    // Placed at the end of each Header/Footer/Line
    private static final String LINE_RETURN = System.getProperty("line.separator");
    // Headers and Footers have a single tab
    private static final String TAB = "\t";
    // Lines are within Headers/Footers so have double tab
    private static final String TAB2 = TAB + TAB;

    /**
     * Instantiate the writer to file
     * 
     * @param outputFile
     * @throws IOException
     */
    public ClaMLXMLWriter(File outputFile) throws IOException {
        try {
            writer_ = new BufferedWriter(new FileWriter(outputFile));
        } catch (Exception e) {
            e.printStackTrace();
            if (writer_ != null)
                writer_.close();
        }
    }

    public void close() throws IOException {
        if (writer_ != null)
            writer_.close();
    }

    public void flush() throws IOException {
        writer_.flush();
    }

    public void newLine() throws IOException {
        writer_.newLine();
    }

    public void write(String output) throws IOException {
        writer_.write(output);
    }
    
    public void writeClaMLHeader(String lang) throws IOException {
        writeHeader(lang);
        writeClassKinds();
        writeUsageKinds();
        writeRubricKinds();
    }

    /*
	    <?xml version="1.0" encoding="UTF-8"?>
	    <ClaML version="2.0.0">
	    	<Meta value="en" name="lang"/>
	    	<Identifier uid="id-to-be-added-later" authority="WHO"/>
	    	<Title version="July 2007" name="ICD-10-CM" date="2009-07-14"/>
     */
    private void writeHeader(String lang) throws IOException {
        DateFormat dateFormat = new SimpleDateFormat(ClaMLConstants.DATE_FORMAT);
        java.util.Date date = new java.util.Date();

        StringBuffer str = new StringBuffer();
        str.append("<?xml version=\"");
        str.append(ClaMLConstants.XML_VERSION);
        str.append("\" encoding=\"");
        str.append(ClaMLConstants.XML_ENCODING);
        str.append("\"?>");
        str.append(LINE_RETURN);

        str.append("<ClaML version=\"");
        str.append(ClaMLConstants.CLAML_VERSION);
        str.append("\">");
        str.append(LINE_RETURN);

        str.append(TAB);
        str.append("<Meta value=\"");
        str.append(lang);
        str.append("\" name=\"lang\"/>");
        str.append(LINE_RETURN);

        str.append(TAB);
        str.append("<Identifier uid=\"");
        str.append(ClaMLConstants.ID_UID);
        str.append("\" authority=\"");
        str.append(ClaMLConstants.ID_AUTHORITY);
        str.append("\"/>");
        str.append(LINE_RETURN);

        str.append(TAB);
        str.append("<Title version=\"");
        str.append(ClaMLConstants.TITLE_VERSION);
        str.append("\" name=\"");
        str.append(ClaMLConstants.TITLE_NAME);
        str.append("\" date=\"");
        str.append(dateFormat.format(date));
        str.append("\"/>");
        str.append(LINE_RETURN);
        
        write(str.toString());
    }

    /*
		<ClassKinds>
			<ClassKind name="chapter"/>
			<ClassKind name="block"/>
			<ClassKind name="category"/>
		</ClassKinds>    
     */
    private void writeClassKinds() throws IOException {
        StringBuffer str = new StringBuffer();
        
        str.append(TAB);
        str.append("<ClassKinds>");
        str.append(LINE_RETURN);

        for (ClassKinds kind: ClassKinds.values()){   
            str.append(TAB2);
            str.append("<ClassKind name=\"");
            str.append(kind.description());
            str.append("\"/>");
            str.append(LINE_RETURN);
        }

        str.append(TAB);
        str.append("</ClassKinds>");
        str.append(LINE_RETURN);
        
        write(str.toString());
    }

    /*
		<UsageKinds>
			<UsageKind name="optional" mark="!"/>
		</UsageKinds>
     */
    private void writeUsageKinds() throws IOException {
        StringBuffer str = new StringBuffer();
        
        str.append(TAB);
        str.append("<UsageKinds>");
        str.append(LINE_RETURN);

        str.append(TAB2);
        str.append("<UsageKind name=\"optional\" mark=\"!\"/>");
        str.append(LINE_RETURN);

        str.append(TAB);
        str.append("</UsageKinds>");
        str.append(LINE_RETURN);

        write(str.toString());
    }

    /*
		<RubricKinds>
			<RubricKind name="preferred" inherited="false"/>
			<RubricKind name="inclusion" inherited="false"/>
			<RubricKind name="exclusion1" inherited="false"/>
			<RubricKind name="exclusion2" inherited="false"/>
			<RubricKind name="note" inherited="false"/>
		</RubricKinds>
     */
    private void writeRubricKinds() throws IOException {
        StringBuffer str = new StringBuffer();
        
        str.append(TAB);
        str.append("<RubricKinds>");
        str.append(LINE_RETURN);
        write(str.toString());

        for (RubricKinds kind: RubricKinds.values()){ 
            writeRubricKindLine(kind.description(), "false");
        }

        str = new StringBuffer();
        str.append(TAB);
        str.append("</RubricKinds>");
        str.append(LINE_RETURN);
        
        write(str.toString());
    }
    

    /*
		<Class kind="block" code="A15-A19">
     */    
    public void writeClassHeader(String kind, String code)  throws IOException {
        StringBuffer str = new StringBuffer();
        
        str.append(TAB);
        str.append("<Class kind=\"");
        str.append(kind);
        str.append("\" code=\"");
        str.append(code);
        str.append("\">");
        str.append(LINE_RETURN);
        
        write(str.toString());
    }

    /*
    	</Class>
     */
    public void writeClassFooter()  throws IOException {
        StringBuffer str = new StringBuffer();
        
        str.append(TAB);
        str.append("</Class>");
        str.append(LINE_RETURN);

        write(str.toString());
    }

    
    /*
		<Rubric kind="preferred" id="id-to-be-added-later-1000171">
     */
    public void writeRubricHeader(String kind, String id)  throws IOException {
        StringBuffer str = new StringBuffer();
        
        str.append(TAB2);
        str.append("<Rubric kind=\"");
        str.append(kind);
        str.append("\" id=\"");
        str.append(id);
        str.append("\">");
        str.append(LINE_RETURN);

        write(str.toString());
    }

    /*
    	</Rubric>
     */
    public void writeRubricFooter()  throws IOException {
        StringBuffer str = new StringBuffer();
        
        str.append(TAB2);
        str.append("</Rubric>");
        str.append(LINE_RETURN);

        write(str.toString());
    }

    
    /*
		<Label xml:space="default" xml:lang="en">pneumoconiosis associated with tuberculosis
     */
    public void writeLabelHeader(String xmlSpace, String xmlLang, String value)  throws IOException {
        StringBuffer str = new StringBuffer();
        
        str.append(TAB);
        str.append(TAB2);
        str.append("<Label xml:space=\"");
        str.append(xmlSpace);
        str.append("\" xml:lang=\"");
        str.append(xmlLang);
        str.append("\">");
        str.append(value);
        str.append(LINE_RETURN);

        write(str.toString());
    }

    /*
    	</Label>
     */
    public void writeLabelFooter()  throws IOException {
        StringBuffer str = new StringBuffer();
        
        str.append(TAB);
        str.append(TAB2);
        str.append("</Label>");
        str.append(LINE_RETURN);

        write(str.toString());
    }



    /*
        <RubricKind name="preferred" inherited="false"/>
     */
    private void writeRubricKindLine(String name, String inherited)  throws IOException {
        StringBuffer str = new StringBuffer();
        
        str.append(TAB2);
        str.append("<RubricKind name=\"");
        str.append(name);
        str.append("\" inherited=\"");
        str.append(inherited);
        str.append("\"/>");
        str.append(LINE_RETURN);
        
        write(str.toString());
    }
    
    /*
        <SuperClass code="A00-B99"/>
     */
    public void writeSuperClassLine(String code)  throws IOException {
        StringBuffer str = new StringBuffer();
        
        str.append(TAB2);
        str.append("<SuperClass code=\"");
        str.append(code);
        str.append("\"/>");
        str.append(LINE_RETURN);

        write(str.toString());
    }

    /*
        <SubClass code="A15"/>
     */
    public void writeSubClassLine(String code)  throws IOException {

        StringBuffer str = new StringBuffer();
        str.append(TAB2);
        str.append("<SubClass code=\"");
        
        // hack workaround for super class codes which do not validate
        if(code.equalsIgnoreCase("O80, O82"))
        {
            code = "O80-O82";
        }
        if(code.equalsIgnoreCase("X52, X58"))
        {
            code = "X52-X58";
        }
        str.append(code);
        str.append("\"/>");
        str.append(LINE_RETURN);

        write(str.toString());
    }
    
    /*
    	<Reference code="J65" class="in brackets">J65</Reference>
     */
    public void writeReferenceLine(String code, String classValue, String value)  throws IOException {
        StringBuffer str = new StringBuffer();
        
        str.append(TAB2);
        str.append(TAB2);
        str.append("<Reference code=\"");
        str.append(code);
        str.append("\" class=\"");
        str.append(classValue);
        str.append("\">");
        str.append(value);
        str.append("</Reference>");
        str.append(LINE_RETURN);

        write(str.toString());
    }

    /*
    	</ClaML>
     */
    public void writeClaMLFooter()  throws IOException {
        StringBuffer str = new StringBuffer();
        
        str.append("</ClaML>");

        write(str.toString());
    }



}
