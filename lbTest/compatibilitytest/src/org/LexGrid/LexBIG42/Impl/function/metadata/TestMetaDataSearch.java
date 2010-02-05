/*
 * Copyright: (c) 2004-2007 Mayo Foundation for Medical Education and 
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
package org.LexGrid.LexBIG42.Impl.function.metadata;

import java.util.HashSet;

import junit.framework.TestCase;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.MetadataProperty;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceMetadata;
import org.LexGrid.LexBIG.Utility.Constructors;

/**
 * Class to test the MetaData search capabilities.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class TestMetaDataSearch extends TestCase
{
    public void testListCodingSchemes() throws Exception
    {
        LexBIGServiceMetadata md = ServiceHolder.instance().getLexBIGService().getServiceMetadata();
        AbsoluteCodingSchemeVersionReference[] acsvrl = md.listCodingSchemes()
                .getAbsoluteCodingSchemeVersionReference();

        assertTrue(acsvrl.length >= 2);
        assertTrue(contains(acsvrl, Constructors.createAbsoluteCodingSchemeVersionReference("test.1", "1.0")));
        assertTrue(contains(acsvrl, Constructors.createAbsoluteCodingSchemeVersionReference("test.2", "2.0")));
    }

    private boolean contains(AbsoluteCodingSchemeVersionReference[] acsvr, AbsoluteCodingSchemeVersionReference acsvr2)
    {
        for (int i = 0; i < acsvr.length; i++)
        {
            if (acsvr[i].getCodingSchemeURN().equals(acsvr2.getCodingSchemeURN())
                    && acsvr[i].getCodingSchemeVersion().equals(acsvr2.getCodingSchemeVersion()))
            {
                return true;
            }
        }
        return false;
    }

    private boolean contains(MetadataProperty[] mdp, String urn, String version, String name, String value)
    {
        for (int i = 0; i < mdp.length; i++)
        {
            if (mdp[i].getCodingSchemeURI().equals(urn)
                    && mdp[i].getCodingSchemeVersion().equals(version)
                    && mdp[i].getName().equals(name)
                    && mdp[i].getValue().equals(value))
            {
                return true;
            }
        }
        return false;
    }

    public void testContainerRestrictionSearch() throws Exception
    {
        LexBIGServiceMetadata md = ServiceHolder.instance().getLexBIGService().getServiceMetadata();
        md.restrictToValue("obo-text", "LuceneQuery");
        md.restrictToCodingScheme(Constructors.createAbsoluteCodingSchemeVersionReference("test.1", "1.0"));

        MetadataProperty[] result = md.resolve().getMetadataProperty();
        assertTrue(result.length == 2);
        assertTrue(contains(result, "test.1", "1.0", "format", "OBO-TEXT"));
        assertTrue(contains(result, "test.1", "1.0", "download_format", "OBO-TEXT"));

        md = ServiceHolder.instance().getLexBIGService().getServiceMetadata();
        md.restrictToCodingScheme(Constructors.createAbsoluteCodingSchemeVersionReference("test.1", "1.0"));
        md.restrictToPropertyParents(new String[]{"core_format"});
        md.restrictToValue("obo-text", "LuceneQuery");
        result = md.resolve().getMetadataProperty();
        assertTrue(result.length == 1);
        assertTrue(contains(result, "test.1", "1.0", "format", "OBO-TEXT"));

    }
    
    public void testPropertyRestrictionSearch() throws Exception
    {
        LexBIGServiceMetadata md = ServiceHolder.instance().getLexBIGService().getServiceMetadata();
        md.restrictToValue("obo-text", "LuceneQuery");
        md.restrictToCodingScheme(Constructors.createAbsoluteCodingSchemeVersionReference("test.1", "1.0"));

        MetadataProperty[] result = md.resolve().getMetadataProperty();
        assertTrue(result.length == 2);
        assertTrue(contains(result, "test.1", "1.0", "format", "OBO-TEXT"));
        assertTrue(contains(result, "test.1", "1.0", "download_format", "OBO-TEXT"));

        md = ServiceHolder.instance().getLexBIGService().getServiceMetadata();
        md.restrictToCodingScheme(Constructors.createAbsoluteCodingSchemeVersionReference("test.1", "1.0"));
        md.restrictToProperties(new String[]{"format"});
        md.restrictToValue("obo-text", "LuceneQuery");
        result = md.resolve().getMetadataProperty();
        assertTrue(result.length == 1);
        assertTrue(contains(result, "test.1", "1.0", "format", "OBO-TEXT"));

    }
    
    public void testCodingSchemeRestrictionSearch() throws Exception
    {
        LexBIGServiceMetadata md = ServiceHolder.instance().getLexBIGService().getServiceMetadata();
        md.restrictToValue("English", "LuceneQuery");
        MetadataProperty[] result = md.resolve().getMetadataProperty();
        
        HashSet<String> temp = new HashSet<String>();
        for (int i = 0; i < result.length; i++)
        {
            temp.add(result[i].getCodingSchemeURI() + ":" + result[i].getCodingSchemeVersion());
        }
        
        //should be more than 1 unique code system.
        assertTrue(temp.size() >= 2);
        
        //should contain this
        assertTrue(temp.contains("test.2:2.0"));
        
        //now do the restriction, and retest.
        
        md = ServiceHolder.instance().getLexBIGService().getServiceMetadata();
        md.restrictToValue("English", "LuceneQuery");
        md.restrictToCodingScheme(Constructors.createAbsoluteCodingSchemeVersionReference("test.2", "2.0"));
        result = md.resolve().getMetadataProperty();
        
        temp = new HashSet<String>();
        for (int i = 0; i < result.length; i++)
        {
            temp.add(result[i].getCodingSchemeURI() + ":" + result[i].getCodingSchemeVersion());
        }
        
        //should be more than 1 unique code system.
        assertTrue(temp.size() == 1);
        
        //should contain this
        assertTrue(temp.contains("test.2:2.0"));
    }
    
    public void testValueRestriction() throws Exception
    {
        LexBIGServiceMetadata md = ServiceHolder.instance().getLexBIGService().getServiceMetadata();
        md.restrictToValue(".*animaldiversity.*", "RegExp");
 
        MetadataProperty[] result = md.resolve().getMetadataProperty();
        assertTrue(result.length >= 3);
        assertTrue(contains(result, "test.2", "2.0", "homepage", "http://www.animaldiversity.org"));
    }
}