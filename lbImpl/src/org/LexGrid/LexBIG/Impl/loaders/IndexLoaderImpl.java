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
package org.LexGrid.LexBIG.Impl.loaders;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Index.Index;
import org.LexGrid.LexBIG.Extensions.Index.IndexLoader;
import org.LexGrid.LexBIG.Impl.Extensions.ExtensionRegistryImpl;

/**
 * Loader for rebuilding lucene indexes.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class IndexLoaderImpl extends BaseLoader implements IndexLoader {
    private static final long serialVersionUID = 7286800973587921077L;
    public final static String name = "IndexLoader";
    private final static String description = "This loader reloads Indexes in the LexGrid system.";

    public IndexLoaderImpl() {
        super.name_ = IndexLoaderImpl.name;
        super.description_ = IndexLoaderImpl.description;
    }

    public static void register() throws LBParameterException, LBException {
        ExtensionDescription temp = new ExtensionDescription();
        temp.setExtensionBaseClass(IndexLoaderImpl.class.getInterfaces()[0].getName());
        temp.setExtensionClass(IndexLoaderImpl.class.getName());
        temp.setDescription(description);
        temp.setName(name);
        temp.setVersion(version_);

        // I'm registering them this way to avoid the lexBig service manager
        // API.
        // If you are writing an add-on extension, you should register them
        // through the
        // proper interface.
        ExtensionRegistryImpl.instance().registerLoadExtension(temp);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.Indexers.IndexLoader#clear(org.LexGrid.LexBIG.DataModel
     * .Core.AbsoluteCodingSchemeVersionReference)
     */
    public void clear(AbsoluteCodingSchemeVersionReference ref, Index index, boolean async)
            throws LBInvocationException {
        throw new java.lang.UnsupportedOperationException(
                "Index extensions are not currently supported, and clear is not allowed on the base indexes.  "
                        + "See 'rebuildBase' if you need to regenerate the base indexes.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.Indexers.IndexLoader#load(org.LexGrid.LexBIG.DataModel
     * .Core.AbsoluteCodingSchemeVersionReference, boolean, boolean)
     */
    public void load(AbsoluteCodingSchemeVersionReference ref, Index index, boolean stopOnErrors, boolean async)
            throws LBException {
        throw new java.lang.UnsupportedOperationException(
                "Method load not yet implemented - it is not yet used for base indexes - and we don't yet support Index Extensions.  "
                        + "Index loading happens automatically.  If you want to rebuild an index, call 'rebuildBase'");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.Indexers.IndexLoader#rebuildBase(org.LexGrid.LexBIG
     * .DataModel.Core.AbsoluteCodingSchemeVersionReference, boolean)
     */
    public void rebuild(AbsoluteCodingSchemeVersionReference ref, Index index, boolean async) throws LBException {
        reindexCodeSystem(ref, async);
    }
}