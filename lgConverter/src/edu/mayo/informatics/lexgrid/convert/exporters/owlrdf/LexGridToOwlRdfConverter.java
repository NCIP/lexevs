package edu.mayo.informatics.lexgrid.convert.exporters.owlrdf;

import java.io.Writer;

import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.codingSchemes.CodingScheme;

public class LexGridToOwlRdfConverter {
    
    public static void convert(CodingScheme cs, CodedNodeGraph cng, CodedNodeSet cns, Writer writer, LgMessageDirectorIF messenger) {
        messenger.error("I'm not implemented.");
    }

}
