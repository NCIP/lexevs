
package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.search;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.annotations.LgClientSideSafe;
import org.apache.commons.codec.binary.Base64;
import org.apache.lucene.search.ScoreDoc;
import org.objenesis.strategy.StdInstantiatorStrategy;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import de.javakaffee.kryoserializers.ArraysAsListSerializer;
import de.javakaffee.kryoserializers.SynchronizedCollectionsSerializer;
import de.javakaffee.kryoserializers.UnmodifiableCollectionsSerializer;

/**
 * The Class SearchScoreDocIterator.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 * @author <a href="mailto:scott.bauer@mayo.edu">Scott Bauer</a>
 */
@LgClientSideSafe
public class SearchScoreDocIterator implements ResolvedConceptReferencesIterator {
    protected int pos = 0;
    private ScoreDocTransformerExecutor transformerExecutor = new ScoreDocTransformerExecutor();
    transient protected List<ScoreDoc> list;
    protected ScoreDocTransformer transformer;
    protected Set<AbsoluteCodingSchemeVersionReference> codeSystemsToInclude;
    
    protected SearchScoreDocIterator(Set<AbsoluteCodingSchemeVersionReference> codeSystemRefs, List<ScoreDoc> list) {       
       this(codeSystemRefs,list,new ScoreDocTransformer());
    }
    
    protected SearchScoreDocIterator(Set<AbsoluteCodingSchemeVersionReference> codeSystemRefs, List<ScoreDoc> list,
          ScoreDocTransformer transformer) {
        super();
        this.list = list;
        this.transformer = transformer;
        this.codeSystemsToInclude = codeSystemRefs;
    }

    private static final long serialVersionUID = -7112239106786189568L;

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Output output = new Output(baos);
        Kryo kryo = new Kryo();
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
        UnmodifiableCollectionsSerializer.registerSerializers(kryo);
        SynchronizedCollectionsSerializer.registerSerializers(kryo);
        kryo.register(Arrays.asList("").getClass(), new ArraysAsListSerializer());
        kryo.register(ScoreDoc.class);
        kryo.writeClassAndObject(output, (List<ScoreDoc>)list);
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
        kryo.register(ScoreDoc.class);
        List<ScoreDoc> queryObject = (List<ScoreDoc>) kryo.readClassAndObject(input);
        this.list = queryObject;;
        input.close();
}


    @Override
    public boolean hasNext() throws LBResourceUnavailableException {
        return this.pos < this.list.size();
    }

    @Override
    public void release() throws LBResourceUnavailableException {
        // no-op
    }

    @Override
    public int numberRemaining() throws LBResourceUnavailableException {
        return this.list.size() - this.pos;
    }


    @Override
    public ResolvedConceptReference next() throws LBResourceUnavailableException, LBInvocationException {
        ResolvedConceptReferenceList result = this.next(1);
        
        if(result == null || result.getResolvedConceptReferenceCount() == 0){
            return null;
        } else {
            if(result.getResolvedConceptReferenceCount() != 1){
                throw new IllegalStateException("Must have one and only one result.");
            }
            
            return result.getResolvedConceptReference(0);
        }
    }


    @Override
    public ResolvedConceptReferenceList next(int maxToReturn) throws LBResourceUnavailableException,
            LBInvocationException {
        ResolvedConceptReferenceList results = 
                this.transformerExecutor.transform(codeSystemsToInclude,
                       this.transformer, 
                       new ArrayList<ScoreDoc>(this.list.subList(pos, this.adjustEndPos(pos + maxToReturn))));
         
         pos += results.getResolvedConceptReferenceCount();
         
         return results;
    }


    @Override
    public ResolvedConceptReferenceList get(int start, int end) throws LBResourceUnavailableException,
            LBInvocationException, LBParameterException {
        List<ScoreDoc> subList = this.list.subList(start, this.adjustEndPos(end));
        //TODO Adapt to multiple code systems.
        return this.transformerExecutor.transform(this.codeSystemsToInclude, this.transformer, subList);
    }
    
    protected int adjustEndPos(int requestedEnd){
        if(requestedEnd < this.list.size()){
            return requestedEnd;
        } else {
            return this.list.size();
        }
    }


    @Override
    public ResolvedConceptReferencesIterator scroll(int maxToReturn) throws LBResourceUnavailableException,
            LBInvocationException {
        throw new UnsupportedOperationException("Scroll unsupported.");
    }


    @Override
    public ResolvedConceptReferenceList getNext() {
        throw new UnsupportedOperationException("GetNext unsupported.");
    }
    
}