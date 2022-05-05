/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.betwixt.io.BeanReader
 *  org.apache.commons.betwixt.io.BeanWriter
 *  org.apache.commons.betwixt.strategy.HyphenatedNameMapper
 *  org.apache.commons.betwixt.strategy.NameMapper
 */
package org.apache.ddlutils.io;

import java.beans.IntrospectionException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;
import org.apache.commons.betwixt.strategy.HyphenatedNameMapper;
import org.apache.commons.betwixt.strategy.NameMapper;
import org.apache.ddlutils.DdlUtilsException;
import org.apache.ddlutils.io.LocalEntityResolver;
import org.apache.ddlutils.model.Database;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class DatabaseIO {
    public static final String BASE64_ATTR_NAME = "base64";
    private boolean _validateXml = true;
    private boolean _useInternalDtd = true;

    public boolean isValidateXml() {
        return this._validateXml;
    }

    public void setValidateXml(boolean validateXml) {
        this._validateXml = validateXml;
    }

    public boolean isUseInternalDtd() {
        return this._useInternalDtd;
    }

    public void setUseInternalDtd(boolean useInternalDtd) {
        this._useInternalDtd = useInternalDtd;
    }

    protected InputSource getBetwixtMapping() {
        return new InputSource(this.getClass().getResourceAsStream("/mapping.xml"));
    }

    protected BeanReader getReader() throws IntrospectionException, SAXException, IOException {
        BeanReader reader = new BeanReader();
        reader.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(true);
        reader.getXMLIntrospector().getConfiguration().setWrapCollectionsInElement(false);
        reader.getXMLIntrospector().getConfiguration().setElementNameMapper((NameMapper)new HyphenatedNameMapper());
        reader.setValidating(this.isValidateXml());
        if (this.isUseInternalDtd()) {
            reader.setEntityResolver((EntityResolver)new LocalEntityResolver());
        }
        reader.registerMultiMapping(this.getBetwixtMapping());
        return reader;
    }

    protected BeanWriter getWriter(Writer output) throws DdlUtilsException {
        try {
            BeanWriter writer = new BeanWriter(output);
            writer.getXMLIntrospector().register(this.getBetwixtMapping());
            writer.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(true);
            writer.getXMLIntrospector().getConfiguration().setWrapCollectionsInElement(false);
            writer.getXMLIntrospector().getConfiguration().setElementNameMapper((NameMapper)new HyphenatedNameMapper());
            writer.getBindingConfiguration().setMapIDs(false);
            writer.enablePrettyPrint();
            return writer;
        }
        catch (Exception ex) {
            throw new DdlUtilsException(ex);
        }
    }

    public Database read(String filename) throws DdlUtilsException {
        Database model = null;
        try {
            model = (Database)this.getReader().parse(filename);
        }
        catch (Exception ex) {
            throw new DdlUtilsException(ex);
        }
        model.initialize();
        return model;
    }

    public Database read(File file) throws DdlUtilsException {
        Database model = null;
        try {
            model = (Database)this.getReader().parse(file);
        }
        catch (Exception ex) {
            throw new DdlUtilsException(ex);
        }
        model.initialize();
        return model;
    }

    public Database read(Reader reader) throws DdlUtilsException {
        Database model = null;
        try {
            model = (Database)this.getReader().parse(reader);
        }
        catch (Exception ex) {
            throw new DdlUtilsException(ex);
        }
        model.initialize();
        return model;
    }

    public Database read(InputSource source) throws DdlUtilsException {
        Database model = null;
        try {
            model = (Database)this.getReader().parse(source);
        }
        catch (Exception ex) {
            throw new DdlUtilsException(ex);
        }
        model.initialize();
        return model;
    }

    public void write(Database model, String filename) throws DdlUtilsException {
        try {
            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter(filename));
                this.write(model, writer);
                writer.flush();
            }
            finally {
                if (writer != null) {
                    writer.close();
                }
            }
        }
        catch (Exception ex) {
            throw new DdlUtilsException(ex);
        }
    }

    public void write(Database model, OutputStream output) throws DdlUtilsException {
        this.write(model, this.getWriter(new OutputStreamWriter(output)));
    }

    public void write(Database model, Writer output) throws DdlUtilsException {
        this.write(model, this.getWriter(output));
    }

    private void write(Database model, BeanWriter writer) throws DdlUtilsException {
        try {
            writer.writeXmlDeclaration("<?xml version=\"1.0\"?>\n<!DOCTYPE database SYSTEM \"http://db.apache.org/torque/dtd/database\">");
            writer.write((Object)model);
        }
        catch (Exception ex) {
            throw new DdlUtilsException(ex);
        }
    }
}

