package edu.mayo.informatics.lexgrid.convert.directConversions.owlapi;

import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.OWLEntityVisitor;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.util.OntologyIRIShortFormProvider;
import org.semanticweb.owlapi.util.ShortFormProvider;

import uk.ac.manchester.cs.owl.owlapi.mansyntaxrenderer.ManchesterOWLSyntaxObjectRenderer;
import uk.ac.manchester.cs.owl.owlapi.mansyntaxrenderer.ManchesterOWLSyntaxPrefixNameShortFormProvider;

public class MachesterOWLSyntaxLexGridRenderer extends ManchesterOWLSyntaxObjectRenderer implements OWLEntityVisitor {
    private OntologyIRIShortFormProvider ontologyShortFormProvider;

    public MachesterOWLSyntaxLexGridRenderer(OWLOntology ontology, Writer writer,
            ShortFormProvider entityShortFormProvider) {
        super(writer, entityShortFormProvider);
        ontologyShortFormProvider = new OntologyIRIShortFormProvider();

    }

    public OntologyIRIShortFormProvider getOntologyShortFormProvider() {
        return ontologyShortFormProvider;
    }

    public void setOntologyShortFormProvider(OntologyIRIShortFormProvider ontologyShortFormProvider) {
        this.ontologyShortFormProvider = ontologyShortFormProvider;
    }

    public ManchesterOWLSyntaxPrefixNameShortFormProvider getPrefixNameShortFormProvider() {
        ShortFormProvider sfp = getShortFormProvider();

        if (!(sfp instanceof ManchesterOWLSyntaxPrefixNameShortFormProvider))
            return null;
        ManchesterOWLSyntaxPrefixNameShortFormProvider prov = (ManchesterOWLSyntaxPrefixNameShortFormProvider) sfp;
        return prov;
    }

    public void writePrefixMap() {
        ShortFormProvider sfp = getShortFormProvider();
        if (!(sfp instanceof ManchesterOWLSyntaxPrefixNameShortFormProvider))
            return;
        ManchesterOWLSyntaxPrefixNameShortFormProvider prov = (ManchesterOWLSyntaxPrefixNameShortFormProvider) sfp;
        Map prefixMap = new HashMap();
        for (Iterator i$ = prov.getPrefixManager().getPrefixName2PrefixMap().keySet().iterator(); i$.hasNext(); writeNewLine()) {
            String prefixName = (String) i$.next();
            String prefix = prov.getPrefixManager().getPrefix(prefixName);
            prefixMap.put(prefixName, prefix);
            write(ManchesterOWLSyntax.PREFIX.toString());
            write(": ");
            write(prefixName);
            write(" ");
            write(prefix);
        }

        if (!prefixMap.isEmpty()) {
            writeNewLine();
            writeNewLine();
        }
    }
}
