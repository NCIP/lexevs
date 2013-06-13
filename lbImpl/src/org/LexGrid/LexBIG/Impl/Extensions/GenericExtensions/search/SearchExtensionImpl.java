package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.search;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.ExtensionRegistry;
import org.LexGrid.LexBIG.Extensions.Generic.CodingSchemeReference;
import org.LexGrid.LexBIG.Extensions.Generic.GenericExtension;
import org.LexGrid.LexBIG.Extensions.Generic.SearchExtension;
import org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ServiceUtility;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.lexevs.dao.index.service.search.SearchIndexService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry.ResourceType;
import org.springframework.util.CollectionUtils;

public class SearchExtensionImpl extends AbstractExtendable implements SearchExtension {

    private static final long serialVersionUID = 8704782086137708226L;

    @Override
    public ResolvedConceptReferencesIterator search(String text) throws LBParameterException {
        return this.search(text, null, null);
    }

    @Override
    public ResolvedConceptReferencesIterator search(String text, Set<CodingSchemeReference> codeSystems) throws LBParameterException {
        return this.search(text, codeSystems, null);
    }

    @Override
    public ResolvedConceptReferencesIterator search(
            String text, 
            Set<CodingSchemeReference> codeSystemsToInclude,
            Set<CodingSchemeReference> codeSystemsToExclude) throws LBParameterException {
        
        LexEvsServiceLocator lexEvsServiceLocator = LexEvsServiceLocator.getInstance();
        List<RegistryEntry> entries = 
                lexEvsServiceLocator.getRegistry().getAllRegistryEntriesOfType(ResourceType.CODING_SCHEME);
        
        for(RegistryEntry entry : entries){
            if(! entry.getStatus().equals(CodingSchemeVersionStatus.ACTIVE.toString())){
                CodingSchemeReference ref = new CodingSchemeReference();
                ref.setCodingScheme(entry.getResourceUri());
                ref.setVersionOrTag(
                        Constructors.createCodingSchemeVersionOrTagFromVersion(entry.getResourceVersion()));
                
                if(codeSystemsToExclude == null){
                    codeSystemsToExclude = new HashSet<CodingSchemeReference>();
                }
                
                codeSystemsToExclude.add(ref);
            }
        }
        
        SearchIndexService service = LexEvsServiceLocator.getInstance().
                getIndexServiceManager().
                getSearchIndexService();
        
        Analyzer analyzer = service.getAnalyzer();

        List<ScoreDoc> scoreDocs = lexEvsServiceLocator.
                getIndexServiceManager().
                getSearchIndexService().
                query(this.resolveCodeSystemReferences(codeSystemsToInclude), 
                        this.resolveCodeSystemReferences(codeSystemsToExclude),
                        this.parseQuery(text, analyzer));

        return new SearchScoreDocIterator(scoreDocs);
    }
    
    protected Query parseQuery(String text, Analyzer analyzer) {
        if (StringUtils.isBlank(text)) {
            return new MatchAllDocsQuery();
        } else {
            QueryParser parser = this.createQueryParser(analyzer);

            try {
                return parser.parse(text);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    protected QueryParser createQueryParser(Analyzer analyzer){
        QueryParser parser = new QueryParser("description", analyzer);
        parser.setDefaultOperator(Operator.AND);
        
        return parser;
    }
    
    private Set<AbsoluteCodingSchemeVersionReference> 
        resolveCodeSystemReferences(Set<CodingSchemeReference> references) throws LBParameterException{
        if(CollectionUtils.isEmpty(references)){
            return null;
        }
        
        Set<AbsoluteCodingSchemeVersionReference> returnSet = new HashSet<AbsoluteCodingSchemeVersionReference>();
        
        for(CodingSchemeReference ref : references){
            returnSet.add(
                    ServiceUtility.getAbsoluteCodingSchemeVersionReference(
                            ref.getCodingScheme(),
                            ref.getVersionOrTag(), 
                            true));
        }
        
        return returnSet;
    }

    @Override
    protected ExtensionDescription buildExtensionDescription() {
        ExtensionDescription ed = new ExtensionDescription();
        ed.setDescription("Simple Search Extension for LexEVS.");
        ed.setExtensionBaseClass(GenericExtension.class.getName());
        ed.setExtensionClass(this.getClass().getName());
        ed.setName("SearchExtension");
        ed.setVersion("1.0");

        return ed;
    }

    @Override
    protected void doRegister(ExtensionRegistry registry, ExtensionDescription description) throws LBParameterException {
        registry.registerGenericExtension(description);
    }

}
