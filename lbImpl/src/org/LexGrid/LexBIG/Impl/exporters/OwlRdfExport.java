/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *      http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.LexGrid.LexBIG.Impl.exporters;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URI;
import java.sql.SQLException;
import java.util.Iterator;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Export.OwlRdf_Exporter;
import org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.Extensions.ExtensionRegistryImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.codingSchemes.CodingScheme;
import org.lexevs.logging.LoggerFactory;

import com.hp.hpl.jena.db.DBConnection;
import com.hp.hpl.jena.db.IDBConnection;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.ontology.impl.OntologyImpl;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ModelMaker;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFWriter;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sdb.SDBFactory;
import com.hp.hpl.jena.sdb.Store;
import com.hp.hpl.jena.sdb.StoreDesc;
import com.hp.hpl.jena.sdb.sql.JDBC;
import com.hp.hpl.jena.sdb.sql.SDBConnection;
import com.hp.hpl.jena.sdb.store.DatabaseType;
import com.hp.hpl.jena.sdb.store.LayoutType;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;

import edu.mayo.informatics.lexgrid.convert.exporters.owlrdf.LexGridToOwlRdfConverter;
import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.constants.LexGridConstants;
import edu.mayo.informatics.lexgrid.convert.options.BooleanOption;

/**
 * Exports content to OWL/RDF format.
 */
public class OwlRdfExport extends BaseExporter implements OwlRdf_Exporter {

    private static final long serialVersionUID = -97175077552869283L;
    
    private static String LG_NS = "lg";
    private static String LG_NS_URI = "http://lexgrid.mayo.edu";
    
    
    public final static String name = "OwlRdfExport";
    private final static String description = "This loader exports LexGrid to OWL RDF";

    private CodedNodeGraph cng;
    private CodedNodeSet cns;

    public void setCns(CodedNodeSet cns) {
        this.cns = cns;
    }

    public void setCng(CodedNodeGraph cng) {
        this.cng = cng;
    }

    protected LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }

    public OwlRdfExport() {
        super.name_ = OwlRdfExport.name;
        super.description_ = OwlRdfExport.description;
    }

    public static void register() throws LBParameterException, LBException {
        ExtensionDescription temp = new ExtensionDescription();
        temp.setExtensionBaseClass(OwlRdfExport.class.getInterfaces()[0].getName());
        temp.setExtensionClass(OwlRdfExport.class.getName());
        temp.setDescription(description);
        temp.setName(name);
        temp.setVersion(version_);

        // I'm registering them this way to avoid the lexBig service manager
        // API.
        // If you are writing an add-on extension, you should register them
        // through the
        // proper interface.
        ExtensionRegistryImpl.instance().registerExportExtension(temp);
    }

    protected void doExport() {
        exportCodingSchemeData();
    }

    private void verifyOutputDirectory(String directory) {
        if (directory == null) {
            String msg = "Output location value is null.";
            this.getLogger().fatal(msg);
            this.getStatus().setErrorsLogged(true);
            throw new RuntimeException(msg);
        }

        File F_directory = new File(directory);

        if (F_directory.exists() == false) {
            String msg = F_directory.getAbsolutePath() + " does not exist.";
            this.getLogger().fatal(msg);
            this.getStatus().setErrorsLogged(true);
            throw new RuntimeException(msg);
        }

        if (F_directory.isDirectory() == false) {
            String msg = F_directory.getAbsolutePath() + " is not a directory.";
            this.getLogger().fatal(msg);
            this.getStatus().setErrorsLogged(true);
            throw new RuntimeException(msg);
        }
    }

    private String getCodingSchemeName(String csUri, String csVersion) {
        String rv = null;
        try {
            CodingScheme cs = LexBIGServiceImpl.defaultInstance().resolveCodingScheme(csUri,
                    Constructors.createCodingSchemeVersionOrTagFromVersion(csVersion));
            rv = cs.getCodingSchemeName();
        } catch (LBException e) {
            e.printStackTrace();
        }
        return rv;
    }

    protected void exportCodingSchemeData(){
        URI destination = super.getResourceUri();
        AbsoluteCodingSchemeVersionReference source = super.getSource();
        
        boolean overwrite = super.getOptions().getBooleanOption(LexGridConstants.OPTION_FORCE).getOptionValue().booleanValue();
        
        // construct out file name
        String separator = File.separator;
        String directory = destination.getPath();
        this.verifyOutputDirectory(directory);
        String outDirWithEndingPathSeparator = directory;
        if(outDirWithEndingPathSeparator.endsWith(separator) == false) {
            outDirWithEndingPathSeparator = outDirWithEndingPathSeparator + separator;
        }
        
        String codingSchemeUri = source.getCodingSchemeURN();
        String codingSchemeVersion = source.getCodingSchemeVersion();
        String codingSchemeName = this.getCodingSchemeName(codingSchemeUri, codingSchemeVersion);
        String outFileName = outDirWithEndingPathSeparator + codingSchemeName + 
                    "_" + codingSchemeVersion + ".owl";  
        
        File outFile = new File(outFileName);
        
        System.out.println("Content will be exported to file: " + outFile.getAbsolutePath());
                
        if(outFile.exists() == true && overwrite == true)
        {
            outFile.delete();
        } else if (outFile.exists() == true && overwrite == false) {
            String msg = "Output file \"" + outFileName + "\" already exists. Set force option to overwrite an existing file.";
            this.getLogger().fatal(msg);
            this.getStatus().setErrorsLogged(true);
            throw new RuntimeException(msg);
        } else {
            // outFile did not exist.  do nothing.
        }
        
        // enter exporter code here
        Writer w = null;
        BufferedWriter out = null;
        LexBIGService lbsvc = null;
        CodingScheme codingScheme = null;
        try {
            w = new FileWriter(outFile, false);
            out = new BufferedWriter(w);

            lbsvc = LexBIGServiceImpl.defaultInstance();
            codingScheme = lbsvc.resolveCodingScheme(codingSchemeUri, 
                                        Constructors.createCodingSchemeVersionOrTagFromVersion(codingSchemeVersion)); 
            
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LBException e) {
            e.printStackTrace();
        }
        
        // cng and cns MUST be set by setter methods
        // call code in lgConverter
        LexGridToOwlRdfConverter.convert(codingScheme, cng, cns, out, this.getMessageDirector());
        try {
            convert(codingScheme, cng, cns, out, this.getMessageDirector());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void convert(CodingScheme cs, CodedNodeGraph cng, CodedNodeSet cns, Writer writer,
            LgMessageDirectorIF messenger) throws ClassNotFoundException, SQLException {
        final String NS = "urn:oid:11.11.0.1";
        // HSQL config
//        String className = "org.hsqldb.jdbcDriver"; // path of driver class
//        Class.forName(className); // Load the Driver
//        String DB_URL = "jdbc:hsqldb:file:C:/temp/owlrdf-export"; // URL of database
//        String DB_USER = "sa"; // database user id
//        String DB_PASSWD = ""; // database password
//        String DB = "HSQL"; // database type
        
        // mySQL
        String className = "com.mysql.jdbc.Driver";
        Class.forName(className); // Load the Driver
        String DB_URL         = "jdbc:mysql://localhost:3306/jenadb";
        String DB_USER        = "root";
        String DB_PASSWD      = "root";
        String DB             = "MySQL";

        IDBConnection conn = new DBConnection(DB_URL, DB_USER, DB_PASSWD, DB);

        ModelMaker maker = ModelFactory.createModelRDBMaker(conn);
        OntDocumentManager mgr = new OntDocumentManager();
        OntModelSpec spec = new OntModelSpec(OntModelSpec.OWL_DL_MEM); //Owl type
        spec.setDocumentManager(mgr);
        OntModel model = ModelFactory.createOntologyModel(spec, maker, maker.createDefaultModel());
//        OntModel model = ModelFactory.createOntologyModel(spec);
        
        model.write(System.out);
        model.write(getStream());

        model.commit();
        model.close();
        conn.cleanDB();
        conn.close();

    }
    
    public void export() throws ClassNotFoundException {
        String className = "com.mysql.jdbc.Driver";
        Class.forName(className); // Load the Driver
        String DB_URL         = "jdbc:mysql://localhost:3306/jenadb?useCursorFetch=true";
        String DB_USER        = "root";
        String DB_PASSWD      = "root";
        String DB             = "MySQL";

        StoreDesc storeDesc = new StoreDesc(LayoutType.LayoutTripleNodesIndex, DatabaseType.MySQL);
        JDBC.loadDriverMySQL();
        SDBConnection conn = new SDBConnection(DB_URL, "root", "root");
        Store store = SDBFactory.connectStore(conn, storeDesc);

//        OntModel model = SDBFactory.connectDefaultModel(store);
        
         
     // process the codingscheme info
//        RDFWriter rdfWriter = model.getWriter("RDF/XML-ABBREV");
//        rdfWriter.setProperty(arg0, arg1)
        
    }
    private OutputStream getStream() {
        File f = new File("output.owl");
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return out;
    }
    
    @Override
    protected OptionHolder declareAllowedOptions(OptionHolder holder) {
        holder.setIsResourceUriFolder(true);
        holder.getBooleanOptions().add(new BooleanOption(LexGridConstants.OPTION_FORCE, (new Boolean(false))));
        return holder;
    }

    @Override
    public OptionHolder getOptions() {
        return super.getOptions();
    }

    @Override
    public void export(AbsoluteCodingSchemeVersionReference source, URI destination, boolean overwrite,
            boolean stopOnErrors, boolean async) throws LBException {
        // TODO: not sure how we should handle stopOnError and async
        // - currently, async gets set to true by the super
        // - not sure how stopOneErrors is used
        super.getOptions().getBooleanOption(LexGridConstants.OPTION_FORCE).setOptionValue(overwrite);
        super.export(source, destination);
    }

    public static void main(String[] arg) {
        String codingSchemeUri = "urn:oid:11.11.0.1", codingSchemeVersion = "1.0";

        OwlRdfExport export = new OwlRdfExport();

        CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
        versionOrTag.setVersion("1.0");
        LexBIGService lbsvc = LexBIGServiceImpl.defaultInstance();
        try {
            CodingScheme codingScheme = lbsvc.resolveCodingScheme(codingSchemeUri, Constructors
                    .createCodingSchemeVersionOrTagFromVersion(codingSchemeVersion));
            CodedNodeSet cns = lbsvc.getNodeSet(codingSchemeUri, versionOrTag, null);
            CodedNodeGraph cng = lbsvc.getNodeGraph(codingSchemeUri, versionOrTag, null);
            
            export.convert(codingScheme, cng, cns, null, null);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (LBException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}