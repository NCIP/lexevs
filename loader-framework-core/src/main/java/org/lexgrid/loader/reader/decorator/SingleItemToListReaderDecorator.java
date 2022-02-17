
package org.lexgrid.loader.reader.decorator;

import java.util.Arrays;
import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class SingleItemToListReaderDecorator<I> implements ItemReader<List<I>>{

	private ItemReader<I> decoratedItemReader;
	

	public SingleItemToListReaderDecorator(ItemReader<I> decoratedItemReader){
		this.decoratedItemReader = decoratedItemReader;		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<I> read() throws Exception, UnexpectedInputException, ParseException {
		I item = decoratedItemReader.read();
		if(item == null) {
			return null;
		}
		return Arrays.asList(item);
	}
}