
package edu.mayo.informatics.lexgrid.convert.directConversions.owlapi;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.util.OntologyIRIShortFormProvider;
import org.semanticweb.owlapi.util.ShortFormProvider;

import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterOWLSyntaxObjectRenderer;
import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterOWLSyntaxPrefixNameShortFormProvider;

public class MachesterOWLSyntaxLexGridRenderer  implements OWLObjectRenderer{
    private OntologyIRIShortFormProvider ontologyShortFormProvider;
    private WriterDelegate writerDelegate;
    private ManchesterOWLSyntaxObjectRenderer renderer;
    ShortFormProvider sfp;
    public MachesterOWLSyntaxLexGridRenderer(OWLOntology ontology, 
            ShortFormProvider entityShortFormProvider) {
        writerDelegate = new WriterDelegate();
        sfp= entityShortFormProvider;
        renderer = new ManchesterOWLSyntaxObjectRenderer(writerDelegate, entityShortFormProvider);
        ontologyShortFormProvider = new OntologyIRIShortFormProvider();

    }

    public OntologyIRIShortFormProvider getOntologyShortFormProvider() {
        return ontologyShortFormProvider;
    }

    public void setOntologyShortFormProvider(OntologyIRIShortFormProvider ontologyShortFormProvider) {
        this.ontologyShortFormProvider = ontologyShortFormProvider;
    }

    public ManchesterOWLSyntaxPrefixNameShortFormProvider getPrefixNameShortFormProvider() {
        if (!(sfp instanceof ManchesterOWLSyntaxPrefixNameShortFormProvider))
            return null;
        ManchesterOWLSyntaxPrefixNameShortFormProvider prov = (ManchesterOWLSyntaxPrefixNameShortFormProvider) sfp;
        return prov;
    }


    @Override
    public synchronized String render(OWLObject object) {
        writerDelegate.reset();
        object.accept(renderer);
        return writerDelegate.toString();
    }


    @Override
    public synchronized void setShortFormProvider(ShortFormProvider shortFormProvider) {
        renderer = new ManchesterOWLSyntaxObjectRenderer(writerDelegate, shortFormProvider);
    }
    
    private static class WriterDelegate extends Writer {

        private StringWriter delegate;

        public WriterDelegate() {
            // TODO Auto-generated constructor stub
        }


        protected void reset() {
            delegate = new StringWriter();
        }


        @Override
        public String toString() {
            return delegate.getBuffer().toString();
        }


        @Override
        public void close() throws IOException {
            delegate.close();
        }


        @Override
        public void flush() throws IOException {
            delegate.flush();
        }


        @Override
        public void write(char cbuf[], int off, int len) throws IOException {
            delegate.write(cbuf, off, len);
        }
    }

}