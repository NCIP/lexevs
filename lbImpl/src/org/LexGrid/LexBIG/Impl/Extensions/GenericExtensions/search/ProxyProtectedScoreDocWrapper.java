package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.search;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;
import org.apache.lucene.search.ScoreDoc;
import org.objenesis.strategy.StdInstantiatorStrategy;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import de.javakaffee.kryoserializers.ArraysAsListSerializer;

public class ProxyProtectedScoreDocWrapper implements Serializable{
    
    /**
     * @author <a href="mailto:scott.bauer@mayo.edu">Scott Bauer</a>
     */
    private static final long serialVersionUID = 2945119591207458772L;
    private transient ScoreDoc scoreDoc;
    
    
    public ScoreDoc getScoreDoc() {
        return scoreDoc;
    }
    public void setScoreDoc(ScoreDoc scoreDoc) {
        this.scoreDoc = scoreDoc;
    }
    
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Output output = new Output(baos);
        Kryo kryo = new Kryo();
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
        kryo.register(Arrays.asList("").getClass(), new ArraysAsListSerializer());
        kryo.register(ScoreDoc.class);
        kryo.writeClassAndObject(output, getScoreDoc());
        output.close();
        String outputString = Base64.encodeBase64String(baos.toByteArray());
        out.writeObject(outputString);
    }


    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {

        in.defaultReadObject();
        String inputString = (String) in.readObject();
        ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decodeBase64(inputString));
        Input input = new Input(bais);
        Kryo kryo = new Kryo();
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
        kryo.register(Arrays.asList("").getClass(), new ArraysAsListSerializer());
        kryo.register(ScoreDoc.class);
        ScoreDoc queryObject = (ScoreDoc) kryo.readClassAndObject(input);
        setScoreDoc(queryObject);
        input.close();
}
    
    
    

}
