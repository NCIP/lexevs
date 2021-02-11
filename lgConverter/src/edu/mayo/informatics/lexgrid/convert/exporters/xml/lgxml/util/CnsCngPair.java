
package edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.util;

import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;

public class CnsCngPair {
    
    private CodedNodeSet cns;
    private CodedNodeGraph cng;
    
    public CnsCngPair(CodedNodeSet cns, CodedNodeGraph cng) {
        this.cns = cns;
        this.cng = cng;
    }
    
    public CodedNodeSet getCns() { return this.cns; }
    public CodedNodeGraph getCng() { return this.cng; }    

}