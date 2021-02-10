
package edu.mayo.informatics.lexgrid.convert.directConversions.owlapi;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * XML parsing for complex properties blocks in terminologies produced in OWL by NCI
 * @author Scott Bauer (scott.bauer@mayo.edu)
 *
 */
public class BasicXMLParser extends DefaultHandler {
    private StringBuffer accumulator = new StringBuffer();
    private Map<String, String> tagAndValue = null;
    private SAXParserFactory spf;
    private SAXParser sp;
    private String language;
    /**
     * @param args
     */
    public BasicXMLParser() {
        super();
        tagAndValue = new HashMap<String, String>();
         spf = SAXParserFactory.newInstance();
         try {
            sp =  spf.newSAXParser();
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //Method allowing LexEVS to call the parser
    public Map<String, String> parseDocument(String text, LgMessageDirectorIF messages) {
        tagAndValue.clear();
        language = null;
        try {
            Reader reader = new CharArrayReader(text.toCharArray());
            sp.parse(new org.xml.sax.InputSource(reader), this);

        } catch (SAXException se) {
            //Not a complex property, catch the exception and exit quietly. 
            //messages.debug("Either not a complex property or ill formed embedded xml source");
        } catch (IOException ie) {
            ie.printStackTrace();
        }
        return tagAndValue;
    }
    
    //place holder
    public void startDocument(){     
    }

    //appending characters from the tag content to a buffer
    public void characters(char[] buffer, int start, int length) {
        accumulator.append(buffer, start, length);
    }

    //Parser starts tag processing
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) {
        accumulator.setLength(0);
        language = atts.getValue("xml:lang");
    }

    //Parser action at the end of the tag.
    public void endElement(String namespaceURI, String localName, String qName) {
        String value = accumulator.toString().trim();
        
        if (qName.equals(OwlApi2LGConstants.COMP_PROP_NAMESPACE + ":"
                + OwlApi2LGConstants.COMP_PROP_TAG_TERM_NAME)) {
            tagAndValue.put(OwlApi2LGConstants.COMP_PROP_TAG_TERM_NAME, value);
        } else if (qName.equals(OwlApi2LGConstants.COMP_PROP_NAMESPACE + ":"
                + OwlApi2LGConstants.COMP_PROP_TAG_TERM_GROUP)) {
            tagAndValue.put(OwlApi2LGConstants.COMP_PROP_TAG_TERM_GROUP, value);
        } else if (qName.equals(OwlApi2LGConstants.COMP_PROP_NAMESPACE + ":"
                + OwlApi2LGConstants.COMP_PROP_TAG_TERM_SOURCE)) {
            tagAndValue.put(OwlApi2LGConstants.COMP_PROP_TAG_TERM_SOURCE, value);
        } else if (qName.equals(OwlApi2LGConstants.COMP_PROP_NAMESPACE + ":"
                + OwlApi2LGConstants.COMP_PROP_TAG_DEF_DEFINITION)) {
            tagAndValue.put(OwlApi2LGConstants.COMP_PROP_TAG_DEF_DEFINITION, value);
        } else if (qName.equals(OwlApi2LGConstants.COMP_PROP_NAMESPACE + ":"
                + OwlApi2LGConstants.COMP_PROP_TAG_DEF_SOURCE)) {
            tagAndValue.put(OwlApi2LGConstants.COMP_PROP_TAG_DEF_SOURCE, value);
        } else if (qName.equals(OwlApi2LGConstants.COMP_PROP_NAMESPACE + ":"
                + OwlApi2LGConstants.COMP_PROP_TAG_DEF_REV_NAME)) {
            tagAndValue.put(OwlApi2LGConstants.COMP_PROP_TAG_DEF_REV_NAME, value);
        } else if (qName.equals(OwlApi2LGConstants.COMP_PROP_NAMESPACE + ":"
                + OwlApi2LGConstants.COMP_PROP_TAG_DEF_REV_DATE)) {
            tagAndValue.put(OwlApi2LGConstants.COMP_PROP_TAG_DEF_REV_DATE, value);
        } else if (qName.equals(OwlApi2LGConstants.COMP_PROP_NAMESPACE + ":"
                + OwlApi2LGConstants.COMP_PROP_TAG_GO_ID)) {
            tagAndValue.put(OwlApi2LGConstants.COMP_PROP_TAG_GO_ID, value);
        } else if (qName.equals(OwlApi2LGConstants.COMP_PROP_NAMESPACE + ":"
                + OwlApi2LGConstants.COMP_PROP_TAG_GO_TERM)) {
            tagAndValue.put(OwlApi2LGConstants.COMP_PROP_TAG_GO_TERM, value);
        } else if (qName.equals(OwlApi2LGConstants.COMP_PROP_NAMESPACE + ":"
                + OwlApi2LGConstants.COMP_PROP_TAG_GO_EVI)) {
            tagAndValue.put(OwlApi2LGConstants.COMP_PROP_TAG_GO_EVI, value);
        } else if (qName.equals(OwlApi2LGConstants.COMP_PROP_NAMESPACE + ":"
                + OwlApi2LGConstants.COMP_PROP_TAG_GO_SOURCE)) {
            tagAndValue.put(OwlApi2LGConstants.COMP_PROP_TAG_GO_SOURCE, value);
        } else if (qName.equals(OwlApi2LGConstants.COMP_PROP_NAMESPACE + ":"
                + OwlApi2LGConstants.COMP_PROP_TAG_GO_SOURCE_DATE)) {
            tagAndValue.put(OwlApi2LGConstants.COMP_PROP_TAG_GO_SOURCE_DATE, value);
        } else if (qName.equals(OwlApi2LGConstants.COMP_PROP_NAMESPACE + ":"
                + OwlApi2LGConstants.COMP_PROP_TAG_COMPLEXDEFINITION)) {
            // Top Level Tag -- do nothing
        } else if (qName.equals(OwlApi2LGConstants.COMP_PROP_NAMESPACE + ":"
                + OwlApi2LGConstants.COMP_PROP_TAG_COMPLEXANNOTATION)) {
            // Top Level Tag -- do nothing
        } else if (qName.equals(OwlApi2LGConstants.COMP_PROP_NAMESPACE + ":"
                + OwlApi2LGConstants.COMP_PROP_TAG_COMPLEXTERM)) {
            // Top Level Tag -- do nothing
        } else {
            // tag is undefined as property type -- load as potential generic
            // property qualifier
            // Stripping namespace from any string that is a qName
            qName = qName.replace(OwlApi2LGConstants.COMP_PROP_NAMESPACE + ":", "");
            tagAndValue.put(qName, value);
        }
        if(language != null){
        tagAndValue.put("language", language);
        }
    }
    //place holder
    public void fatalError(SAXParseException e)throws SAXException{
       //place holder for debugging 
    }
}