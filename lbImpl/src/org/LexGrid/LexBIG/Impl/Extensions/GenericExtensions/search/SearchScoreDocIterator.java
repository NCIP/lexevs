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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
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
 */
@LgClientSideSafe
public class SearchScoreDocIterator implements ResolvedConceptReferencesIterator {
        
    
    private int pos = 0;
    
    private ScoreDocTransformerExecutor transformerExecutor = new ScoreDocTransformerExecutor();
    transient protected List<ScoreDoc> list;
    protected ScoreDocTransformer transformer;
    
    protected SearchScoreDocIterator(List<ScoreDoc> list) {
        super();
        this.list = list;
        this.transformer = new ScoreDocTransformer();
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
//        kryo.writeClassAndObject(output, (ScoreDocTransformer)transformer);
//        kryo.writeClassAndObject(output, (ScoreDocTransformerExecutor)transformerExecutor);
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
        this.list = queryObject;
//        ScoreDocTransformer transformer = (ScoreDocTransformer) kryo.readClassAndObject(input);
//        this.transformer = transformer;
 //       transformerExecutor = (ScoreDocTransformerExecutor)kryo.readClassAndObject(input);
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
                this.transformerExecutor.transform(
                       this.transformer, 
                       new ArrayList<ScoreDoc>(this.list.subList(pos, this.adjustEndPos(pos + maxToReturn))));
         
         pos += results.getResolvedConceptReferenceCount();
         
         return results;
    }


    @Override
    public ResolvedConceptReferenceList get(int start, int end) throws LBResourceUnavailableException,
            LBInvocationException, LBParameterException {
        List<ScoreDoc> subList = this.list.subList(start, this.adjustEndPos(end));
        
        return this.transformerExecutor.transform(this.transformer, subList);
    }
    
    private int adjustEndPos(int requestedEnd){
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
