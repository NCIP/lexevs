package org.LexGrid.LexBIG.example;

import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.search.join.QueryBitSetProducer;
import org.apache.lucene.search.join.ScoreMode;
import org.apache.lucene.search.join.ToParentBlockJoinQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;

import java.util.List;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.lexevs.locator.LexEvsServiceLocator;

public class LuceneQueryTest {

    public static void main(String[] args) {
        new LuceneQueryTest().run();

    }
    
    public void run(){
        Builder bd = new BooleanQuery.Builder();
        Builder innerETCS = new BooleanQuery.Builder();
        Builder innerET = new BooleanQuery.Builder();
        innerET.setMinimumNumberShouldMatch(1);
        TermQuery entityT = new TermQuery(new Term("entityType","concept"));
        innerET.add(entityT, Occur.SHOULD);
        //bd.add(innerET.build(), Occur.SHOULD);
        Builder innerCS = new BooleanQuery.Builder();
        TermQuery entityURI = new TermQuery(new Term("codingSchemeUri","http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl"));
        TermQuery entityVer = new TermQuery(new Term("codingSchemeVersion","0.1.5"));
        innerCS.add(entityURI, Occur.MUST);
        innerCS.add(entityVer, Occur.MUST);
        innerETCS.add(innerET.build(), Occur.SHOULD);
        innerETCS.add(innerCS.build(), Occur.MUST);
        bd.add(innerETCS.build(), Occur.SHOULD);
        Builder inneranon = new BooleanQuery.Builder();
        TermQuery anon = new TermQuery(new Term("isAnonymous", "F"));
        inneranon.add(anon, Occur.MUST);
        bd.add(inneranon.build(), Occur.MUST);
        TermQuery tq1 = new TermQuery(new Term("propertyType", "presentation"));
        bd.add(tq1, Occur.MUST);
        TermQuery tq2 = new TermQuery(new Term("hasSource", "source-code"));
        TermQuery tq3 = new TermQuery(new Term("sourceValue", "CTESTCODE"));
        BooleanQuery.Builder nestedQuery = new BooleanQuery.Builder();
        nestedQuery.add(tq2, Occur.MUST);
        nestedQuery.add(tq3, Occur.MUST);
        bd.add(nestedQuery.build(), Occur.MUST);
        Query query = bd.build();
        TermQuery termQuery = new TermQuery(new Term("isParentDoc", "true"));
        ToParentBlockJoinQuery blockJoinQuery = new ToParentBlockJoinQuery(
                query, new QueryBitSetProducer(termQuery), ScoreMode.Total);
        List<ScoreDoc> list = LexEvsServiceLocator.getInstance().getIndexServiceManager().getEntityIndexService().
        query("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5",blockJoinQuery);
        list.forEach(x -> System.out.println(x));       
        
    }

}
