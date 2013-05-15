package edu.mayo.informatics.lexgrid.convert.directConversions.owlapi;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.util.SimpleRenderer;

public class MySimpleRenderer  extends SimpleRenderer {
    @Override
    public void visit(final OWLOntology ontology1) {
        
        append(ontology1.getOntologyID().toString());
        append("\n");
     
        Set<OWLAxiom> axioms = new HashSet<OWLAxiom>(ontology1.getAxioms());
        for (OWLImportsDeclaration decl : ontology1.getImportsDeclarations()) {
            append("Imports: ");
            append(decl.getURI().toString());
            append("\n");
        }
        append("Classes\n");
        writeCollection(ontology1.getClassesInSignature());
       
        append("<h2>Properties</h2>\n");
        writeCollection(ontology1.getObjectPropertiesInSignature());
        writeCollection(ontology1.getDataPropertiesInSignature());
        append("</div>\n");
        append("<div class='box'>\n");
        append("<h2>Individuals</h2>\n");
        writeCollection(ontology1.getIndividualsInSignature());
        append("</div>");
        append("<div>");
        append("<div class='box'>");
        append("<h2>Axioms</h2>\n");
        append("\n");
        for (OWLAxiom ax : ontology1.getAxioms()) {
            append("\n");
            ax.accept(this);
            append("\n");
        }
       
    }
    
    
    public void write(final String str, final OWLObject o) {
        append(str);
        append("(");
        o.accept(this);
        append(")");
    }

    private void write(final Collection<? extends OWLObject> objects,
            final String separator) {
        // int indent = getIndent();
        for (Iterator<? extends OWLObject> it = objects.iterator(); it.hasNext();) {
            it.next().accept(this);
            if (it.hasNext()) {
                append(" ");
                append(separator);
                append(" ");
                // writeIndent(indent);
            }
        }
    }

    private void write(final Collection<? extends OWLObject> objects) {
        write(objects, "");
    }

    public void writeSpace() {
        append(" ");
    }
    
    private void writeCollection(final Collection<? extends OWLObject> objects) {
        append("\n");
        for (Iterator<? extends OWLObject> it = objects.iterator(); it.hasNext();) {
            append("\n");
            it.next().accept(this);
            append("\n");
        }
        append("\n");
    }
    

}
