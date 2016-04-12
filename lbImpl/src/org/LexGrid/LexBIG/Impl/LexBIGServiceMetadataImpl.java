/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
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
package org.LexGrid.LexBIG.Impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.MetadataPropertyList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.dataAccess.MetaDataQuery;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceMetadata;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.apache.commons.codec.binary.Base64;
import org.apache.lucene.index.Term;
import org.apache.lucene.sandbox.queries.regex.RegexQuery;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.BooleanClause.Occur;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.logging.LoggerFactory;
import org.objenesis.strategy.StdInstantiatorStrategy;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import de.javakaffee.kryoserializers.ArraysAsListSerializer;
import de.javakaffee.kryoserializers.SynchronizedCollectionsSerializer;
import de.javakaffee.kryoserializers.UnmodifiableCollectionsSerializer;

/**
 * Lucene implementation of the LexBIGServiceMetadata interface.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class LexBIGServiceMetadataImpl implements LexBIGServiceMetadata {
    private static final long serialVersionUID = 3382129429728528566L;
    transient protected ArrayList<Query> queryClauses = new ArrayList<Query>();
    transient protected ArrayList<Term> termClauses = new ArrayList<Term>();

    private LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.LexBIGService.LexBIGServiceMetadata#restrictToCodingScheme
     * (org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference)
     */
    public LexBIGServiceMetadata restrictToCodingScheme(AbsoluteCodingSchemeVersionReference acsvr)
            throws LBParameterException {
        getLogger().logMethod(new Object[] { acsvr });
        queryClauses.add(MetaDataQuery.makeCodingSchemeRestriction(acsvr));
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.LexBIGService.LexBIGServiceMetadata#restrictToProperties
     * (java.lang.String[])
     */
    public LexBIGServiceMetadata restrictToProperties(String[] properties) throws LBParameterException {
        getLogger().logMethod(new Object[] { properties });
        queryClauses.add(MetaDataQuery.makePropertyRestriction(properties));
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.LexGrid.LexBIG.LexBIGService.LexBIGServiceMetadata#
     * restrictToPropertyParents(java.lang.String[])
     */
    public LexBIGServiceMetadata restrictToPropertyParents(String[] propertyParents) throws LBParameterException {
        getLogger().logMethod(new Object[] { propertyParents });
        queryClauses.add(MetaDataQuery.makePropertyParentRestriction(propertyParents));
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.LexBIGService.LexBIGServiceMetadata#restrictToValue
     * (java.lang.String, java.lang.String)
     */
    public LexBIGServiceMetadata restrictToValue(String matchText, String matchAlgorithm) throws LBParameterException {
        getLogger().logMethod(new Object[] { matchText, matchAlgorithm });
        queryClauses.add(MetaDataQuery.makeValueRestriction(matchText, matchAlgorithm));
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.LexGrid.LexBIG.LexBIGService.LexBIGServiceMetadata#resolve()
     */
    public MetadataPropertyList resolve() throws LBParameterException, LBInvocationException {
        getLogger().logMethod(new Object[] {});
        try {
            if (queryClauses.size() + termClauses.size() < 1) {
                throw new LBParameterException("At leat one restriction must be applied before resolving");
            }

            BooleanQuery.Builder builder = new BooleanQuery.Builder();
            for (int i = 0; i < queryClauses.size(); i++) {
                builder.add(queryClauses.get(i), Occur.MUST);
            }
            for (int i = 0; i < termClauses.size(); i++) {
                builder.add(new RegexQuery(termClauses.get(i)), Occur.MUST);
            }

            return LexEvsServiceLocator.getInstance().
                getIndexServiceManager().
                getMetadataIndexService().search(builder.build());
        } catch (LBParameterException e) {
            throw e;
        } catch (Exception e) {
            String id = getLogger().error("An unexpected error occurred resolving the MetaData search.", e);
            throw new LBInvocationException(
                    "An unexpected error occurred resolving the metadata search.  See the log for more details", id);
        }
    }

    public AbsoluteCodingSchemeVersionReferenceList listCodingSchemes() throws LBInvocationException {
        getLogger().logMethod(new Object[] {});
        try {
            return LexEvsServiceLocator.getInstance().
            getIndexServiceManager().
            getMetadataIndexService().listCodingSchemes();
        } catch (Exception e) {
            String id = getLogger().error("An unexpected error occurred while listing metadata coding schemes.", e);
            throw new LBInvocationException(
                    "An unexpected error occurred while listing metadata coding schemes.  See the log for more details",
                    id);
        }
    }
    
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Output output = new Output(baos);
        Kryo kryo = new Kryo();
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
        UnmodifiableCollectionsSerializer.registerSerializers(kryo);
        SynchronizedCollectionsSerializer.registerSerializers(kryo);
        kryo.register(Arrays.asList("").getClass(), new ArraysAsListSerializer());
        kryo.register(Query.class);
        kryo.register(Term.class);
        kryo.register(BooleanQuery.class);
        kryo.register(RegexQuery.class);
        kryo.writeClassAndObject(output, (ArrayList<Query>)queryClauses);
        kryo.writeClassAndObject(output, (ArrayList<Term>)termClauses);
        output.close();
        String outputString = Base64.encodeBase64String(baos.toByteArray());
        out.writeObject(outputString);
    }


    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {

        in.defaultReadObject();

        String inputString = (String) in.readObject();
        ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decodeBase64(inputString));
        Input input = new Input(bais);
        Kryo kryo = new Kryo();
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
        UnmodifiableCollectionsSerializer.registerSerializers(kryo);
        SynchronizedCollectionsSerializer.registerSerializers(kryo);
        kryo.register(Arrays.asList("").getClass(), new ArraysAsListSerializer());
        kryo.register(Query.class);
        kryo.register(Term.class);
        kryo.register(BooleanQuery.class);
        kryo.register(RegexQuery.class);
        ArrayList<Query> queryClauseObject = (ArrayList<Query>) kryo.readClassAndObject(input);
        ArrayList<Term> termClauseObject = (ArrayList<Term>) kryo.readClassAndObject(input);
        this.queryClauses = queryClauseObject;
        this.termClauses = termClauseObject;
        input.close();
}

}