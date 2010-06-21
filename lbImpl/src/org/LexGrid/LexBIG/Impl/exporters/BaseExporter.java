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

import java.net.URI;
import java.util.Date;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.LogEntry;
import org.LexGrid.LexBIG.DataModel.Core.types.LogLevel;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExportStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder;
import org.LexGrid.LexBIG.Impl.loaders.MessageDirector;

import edu.mayo.informatics.lexgrid.convert.formats.Option;
import edu.mayo.informatics.lexgrid.convert.options.BooleanOption;
import edu.mayo.informatics.lexgrid.convert.options.DefaultOptionHolder;

/**
 * Base class with common methods for exporters.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public abstract class BaseExporter {
    boolean inUse = false;
    public String name_;
    public static String version_ = edu.mayo.informatics.lexgrid.convert.utility.Constants.version;
    public String description_;
    public String provider_ = "MAYO";

    private MessageDirector md_;
    private ExportStatus status_;
    
    private URI resourceUri;
    private AbsoluteCodingSchemeVersionReference source;
    private URI valueSetDefinitionURI;
    private String pickListId;

    public static String ASYNC_OPTION = "Async Load";
    public static String FAIL_ON_ERROR_OPTION = Option.getNameForType(Option.FAIL_ON_ERROR);
    
    private OptionHolder holder = new DefaultOptionHolder();
    
    protected BaseExporter() {
        holder.getBooleanOptions().add(new BooleanOption(ASYNC_OPTION));
        holder.getBooleanOptions().add(new BooleanOption(FAIL_ON_ERROR_OPTION));
        this.holder = this.declareAllowedOptions(holder);
    }

    protected void baseExport(boolean async) {
        status_ = new ExportStatus();
        status_.setState(ProcessState.PROCESSING);
        status_.setStartTime(new Date(System.currentTimeMillis()));
        md_ = new MessageDirector(getName(), status_);

        if (async) {
            Thread conversion = new Thread(new DoConversion());
            conversion.start();
        } else {
            new DoConversion().run();
        }
    }

    private class DoConversion implements Runnable {
        public void run() {
            
            try {
                doExport();
                status_.setState(ProcessState.COMPLETED);
                md_.info("Export process completed without error");
            } catch (Exception e) {
                status_.setState(ProcessState.FAILED);
                md_.fatal("Failed while running the export", e);
            } finally {
                if (status_.getState() == null || !status_.getState().equals(ProcessState.COMPLETED)) {
                    status_.setState(ProcessState.FAILED);
                }
                status_.setEndTime(new Date(System.currentTimeMillis()));
                inUse = false;
            }
        }
    }
    
    protected abstract void doExport() throws Exception;
    
    protected abstract OptionHolder declareAllowedOptions(OptionHolder holder);
    
    public void export(AbsoluteCodingSchemeVersionReference source, URI destination) {
        try {
            setInUse();
            this.setResourceUri(destination);
            this.setSource(source);
        } catch (LBInvocationException e) {
           throw new RuntimeException(e);
        }
        baseExport(this.getOptions().getBooleanOption(ASYNC_OPTION).getOptionValue());
    }
    
    public void exportValueSetDefinition(URI valueSetDefinitionURI, URI destination) {
        try {
            setInUse();
            this.setResourceUri(destination);
            this.setValueSetDefinitionURI(valueSetDefinitionURI);
        } catch (LBInvocationException e) {
           throw new RuntimeException(e);
        }
        baseExport(this.getOptions().getBooleanOption(ASYNC_OPTION).getOptionValue());
    }
    
    public void exportPickListDefinition(String pickListId, URI destination) {
        try {
            setInUse();
            this.setResourceUri(destination);
            this.setPickListId(pickListId);
        } catch (LBInvocationException e) {
           throw new RuntimeException(e);
        }
        baseExport(this.getOptions().getBooleanOption(ASYNC_OPTION).getOptionValue());
    }

    private void setInUse() throws LBInvocationException {
        if (inUse) {
            throw new LBInvocationException(
                    "This loader is already in use.  Construct a new loader to do two operations at the same time", "");
        }
        inUse = true;
    }
    
    

    public ExportStatus getStatus() {
        return status_;
    }

    public String getProvider() {
        return provider_;
    }

    public String getName() {
        return name_;
    }

    public String getDescription() {
        return description_;
    }

    public String getVersion() {
        return version_;
    }

    public LogEntry[] getLog(LogLevel level) {
        if (md_ == null) {
            return new LogEntry[] {};
        }
        return md_.getLogEntries(level);
    }

    public void clearLog() {
        if (md_ != null) {
            md_.clearMessages();
        }
    }

    public URI[] getReferences() {
        try {
            return new URI[] { new URI(status_.getDestination()) };
        } catch (Exception e) {
            return new URI[] {};
        }
    }

    public void setResourceUri(URI resourceUri) {
        this.resourceUri = resourceUri;
    }

    public URI getResourceUri() {
        return resourceUri;
    }

    public void setSource(AbsoluteCodingSchemeVersionReference source) {
        this.source = source;
    }

    public AbsoluteCodingSchemeVersionReference getSource() {
        return source;
    }
    
    public OptionHolder getOptions() {
        return holder;
    }

    /**
     * @return the valueSetDefinitionURI
     */
    public URI getValueSetDefinitionURI() {
        return valueSetDefinitionURI;
    }

    /**
     * @param valueSetDefinitionURI the valueSetDefinitionURI to set
     */
    public void setValueSetDefinitionURI(URI valueSetDefinitionURI) {
        this.valueSetDefinitionURI = valueSetDefinitionURI;
    }

    /**
     * @return the pickListId
     */
    public String getPickListId() {
        return pickListId;
    }

    /**
     * @param pickListId the pickListId to set
     */
    public void setPickListId(String pickListId) {
        this.pickListId = pickListId;
    }
    
}