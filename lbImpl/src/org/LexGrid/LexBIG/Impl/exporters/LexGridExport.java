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
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.LexGrid.LexBIG.Impl.exporters;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Export.LexGrid_Exporter;
import org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.Extensions.ExtensionRegistryImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.valueSets.PickListDefinition;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.lexevs.dao.database.service.valuesets.PickListDefinitionService;
import org.lexevs.dao.database.service.valuesets.ValueSetDefinitionService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.logging.LoggerFactory;

import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.constants.LexGridConstants;
import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.formatters.XmlContentWriter;
import edu.mayo.informatics.lexgrid.convert.options.BooleanOption;

/**
 * Exporter for LexGrid XML files.
 * 
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @author <A HREF="mailto:turk.michael@mayo.edu">Michael Turk</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class LexGridExport extends BaseExporter implements LexGrid_Exporter {

    private static final long serialVersionUID = -97175077552869283L;
    public final static String name = "LexGridExport";
    private final static String description = "This loader exports LexGrid XML files";
    private final int pageSize = 5;

    protected LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }

    public LexGridExport() {
        super.name_ = LexGridExport.name;
        super.description_ = LexGridExport.description;
    }

    public static void register() throws LBParameterException, LBException {
        ExtensionDescription temp = new ExtensionDescription();
        temp.setExtensionBaseClass(LexGridExport.class.getInterfaces()[0].getName());
        temp.setExtensionClass(LexGridExport.class.getName());
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
        if (super.getSource() != null)
            exportCodingSchemeData();
        if (super.getValueSetDefinitionURI() != null)
            exportValueSetDefinitionData();
        if (super.getPickListId() != null)
            exportPickListDefinitionData();
    }
    
    protected void exportCodingSchemeData(){
        URI destination = super.getResourceUri();
        AbsoluteCodingSchemeVersionReference source = super.getSource();
        
        BooleanOption temp = (BooleanOption)super.getOptions().getBooleanOption(LexGridConstants.OPTION_FORCE);
        
        boolean overwrite = super.getOptions().getBooleanOption(LexGridConstants.OPTION_FORCE).getOptionValue().booleanValue();
        String outFileName = destination.getPath();
        String codingSchemeUri = source.getCodingSchemeURN();
        String codingSchemeVersion = source.getCodingSchemeVersion();
        
        
        File outFile = new File(outFileName);
        
        // if file does not end with xml, exit
        boolean endsWithXmlLc = outFile.getName().endsWith(".xml");
        boolean endsWithXmlUc = outFile.getName().endsWith(".XML");
        if(endsWithXmlLc == true || endsWithXmlUc == true) {
            // do nothing
        } else {
            String msg = "File should end with .xml";
            this.getLogger().fatal(msg);
            this.getStatus().setErrorsLogged(true);
            throw new RuntimeException(msg);                        
        }
        
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
        
        Writer w = null;
        BufferedWriter out = null;
        LexBIGService lbsvc = null;
        CodingScheme codingScheme = null;
        CodedNodeGraph cng = null;
        CodedNodeSet cns = null;
        try {
            w = new FileWriter(outFile, false);
            out = new BufferedWriter(w);

            lbsvc = LexBIGServiceImpl.defaultInstance();
            codingScheme = lbsvc.resolveCodingScheme(codingSchemeUri, 
                                        Constructors.createCodingSchemeVersionOrTagFromVersion(codingSchemeVersion));
            
            // create coded node graph
            cng = lbsvc.getNodeGraph(codingScheme.getCodingSchemeURI(), 
                    Constructors.createCodingSchemeVersionOrTagFromVersion(codingScheme.getRepresentsVersion()),null);
            
            // TEST TEST TEST
            cng.restrictToAssociations(Constructors.createNameAndValueList("uses"), null);
            
            // create coded node set
            cns = lbsvc.getCodingSchemeConcepts(codingScheme.getCodingSchemeURI(), 
                    Constructors.createCodingSchemeVersionOrTagFromVersion(codingScheme.getRepresentsVersion()) );
            
            // TEST TEST TEST            
            cns.restrictToMatchingDesignations("G*", SearchDesignationOption.ALL, "LuceneQuery", null);
            
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LBException e) {
            e.printStackTrace();
        }
        
        Entities entities = new Entities();
        Entity entity = new Entity();
        entity.setEntityCode(LexGridConstants.MR_FLAG);
        entities.addEntity(entity);
        codingScheme.setEntities(entities);
        
        
        XmlContentWriter xmlContentWriter = new XmlContentWriter();
        xmlContentWriter.marshalToXml(codingScheme, cng, cns, out, this.pageSize);
    }
    
    protected void exportValueSetDefinitionData(){
        URI destination = super.getResourceUri();
        URI vsdURI = super.getValueSetDefinitionURI();
        
        boolean overwrite = super.getOptions().getBooleanOption(LexGridConstants.OPTION_FORCE).getOptionValue().booleanValue();
        String outFileName = destination.getPath();

        File outFile = new File(outFileName);
        
        // if file does not end with XML, exit
        boolean endsWithXmlLc = outFile.getName().endsWith(".xml");
        boolean endsWithXmlUc = outFile.getName().endsWith(".XML");
        if(endsWithXmlLc == true || endsWithXmlUc == true) {
            // do nothing
        } else {
            String msg = "File should end with .xml";
            this.getLogger().fatal(msg);
            this.getStatus().setErrorsLogged(true);
            throw new RuntimeException(msg);                        
        }
        
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
        
        Writer w = null;
        BufferedWriter out = null;
        
        try {
            w = new FileWriter(outFile, false);
            out = new BufferedWriter(w);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        
        ValueSetDefinitionService vsdSer = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getValueSetDefinitionService();
        ValueSetDefinition vsd = null;
        vsd = vsdSer.getValueSetDefinitionByUri(vsdURI);
        
        XmlContentWriter xmlContentWriter = new XmlContentWriter();
        xmlContentWriter.marshalToXml(vsd, null, null, out, this.pageSize);
    }
    
    protected void exportPickListDefinitionData(){
        URI destination = super.getResourceUri();
        String pickListId = super.getPickListId();
        
        boolean overwrite = super.getOptions().getBooleanOption(LexGridConstants.OPTION_FORCE).getOptionValue().booleanValue();
        String outFileName = destination.getPath();

        File outFile = new File(outFileName);
        
        // if file does not end with xml, exit
        boolean endsWithXmlLc = outFile.getName().endsWith(".xml");
        boolean endsWithXmlUc = outFile.getName().endsWith(".XML");
        if(endsWithXmlLc == true || endsWithXmlUc == true) {
            // do nothing
        } else {
            String msg = "File should end with .xml";
            this.getLogger().fatal(msg);
            this.getStatus().setErrorsLogged(true);
            throw new RuntimeException(msg);                        
        }
        
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
        
        Writer w = null;
        BufferedWriter out = null;
        
        try {
            w = new FileWriter(outFile, false);
            out = new BufferedWriter(w);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        PickListDefinitionService pldSer = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getPickListDefinitionService();
        PickListDefinition pld = null;
        pld = pldSer.getPickListDefinitionByPickListId(pickListId);
        
        XmlContentWriter xmlContentWriter = new XmlContentWriter();
        xmlContentWriter.marshalToXml(pld, null, null, out, this.pageSize);
    }

    public URI getSchemaURL() {
        try {
            return new URI("http://LexGrid.org/schema/" + getSchemaVersion() + "/LexGrid/service.xsd");
        } catch (URISyntaxException e) {
            getLogger().error("Unexpected Error", e);
            return null;
        }
    }

    public String getSchemaVersion() {
        return "2005/01";
    }
    
    @Override
    protected OptionHolder declareAllowedOptions(OptionHolder holder) {
       holder.getResourceUriAllowedFileTypes().add("xml");
       holder.setIsResourceUriFolder(false);
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
        //       - currently, async gets set to true by the super
        //       - not sure how stopOneErrors is used
        super.getOptions().getBooleanOption(LexGridConstants.OPTION_FORCE).setOptionValue(overwrite);
        super.export(source, destination);

        
    }

    @Override
    public void exportPickListDefinition(String pickListId, URI destination, boolean overwrite, boolean stopOnErros,
            boolean async) throws LBException {
        super.getOptions().getBooleanOption(LexGridConstants.OPTION_FORCE).setOptionValue(overwrite);
        super.exportPickListDefinition(pickListId, destination);
    }

    @Override
    public void exportValueSetDefinition(URI valueSetDefinitionURI, URI destination, boolean overwrite,
            boolean stopOnErros, boolean async) throws LBException {
        super.getOptions().getBooleanOption(LexGridConstants.OPTION_FORCE).setOptionValue(overwrite);
        super.exportValueSetDefinition(valueSetDefinitionURI, destination);
    }    
    
}