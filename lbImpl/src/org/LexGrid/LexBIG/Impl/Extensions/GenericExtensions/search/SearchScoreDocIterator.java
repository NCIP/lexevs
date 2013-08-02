/*
* Copyright: (c) 2004-2013 Mayo Foundation for Medical Education and
* Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
* triple-shield Mayo logo are trademarks and service marks of MFMER.
*
* Except as contained in the copyright notice above, or as used to identify
* MFMER as the author of this software, the trade names, trademarks, service
* marks, or product names of the copyright holder shall not be used in
* advertising, promotion or otherwise in connection with this software without
* prior written authorization of the copyright holder.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.search;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Impl.helpers.AbstractListBackedResolvedConceptReferencesIterator;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.annotations.LgClientSideSafe;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.lexevs.locator.LexEvsServiceLocator;

/**
 * The Class SearchScoreDocIterator.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@LgClientSideSafe
public class SearchScoreDocIterator extends AbstractListBackedResolvedConceptReferencesIterator<ScoreDoc>{
        
    /**
     * Instantiates a new search score doc iterator.
     *
     * @param list the list
     */
    protected SearchScoreDocIterator(List<ScoreDoc> list) {
        super(list, new ScoreDocTransformer());
    }

    private static final long serialVersionUID = -7112239106786189568L;

    
    public static class ScoreDocTransformer implements Transformer<ScoreDoc> {

        private static final long serialVersionUID = 7176335324999288237L;

        @Override
        public ResolvedConceptReferenceList transform(Iterable<ScoreDoc> items) {
            ResolvedConceptReferenceList list = new ResolvedConceptReferenceList();
            for(ScoreDoc item : items){
                list.addResolvedConceptReference(this.doTransform(item));
            }
            
            return list;
        }
        
        protected ResolvedConceptReference doTransform(ScoreDoc item) {
            Document doc = 
                LexEvsServiceLocator.getInstance().
                    getIndexServiceManager().
                    getSearchIndexService().
                    getById(item.doc);
            
            String code = doc.get("code");
            String namespace = doc.get("namespace");
            String[] types = doc.getValues("type");
            String description = doc.get("entityDescription");
            String codingSchemeUri = doc.get("codingSchemeUri");
            String codingSchemeName = doc.get("codingSchemeName");
            String codingSchemeVersion = doc.get("codingSchemeVersion");
            
            ResolvedConceptReference ref = new ResolvedConceptReference();
            ref.setCode(code);
            ref.setCodeNamespace(namespace);
            ref.setEntityType(types);
            ref.setCodingSchemeName(codingSchemeName);
            ref.setCodingSchemeURI(codingSchemeUri);
            ref.setCodingSchemeVersion(codingSchemeVersion);
            if(StringUtils.isNotBlank(description)){
                ref.setEntityDescription(Constructors.createEntityDescription(description));
            }
            
            return ref;
        }
        
    }
    
}
