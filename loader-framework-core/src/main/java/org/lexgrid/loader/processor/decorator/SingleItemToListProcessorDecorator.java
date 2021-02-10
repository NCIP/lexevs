
package org.lexgrid.loader.processor.decorator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.util.Assert;

public class SingleItemToListProcessorDecorator<I,O> implements ItemProcessor<I,List<O>>{

	private ItemProcessor<List<I>,List<O>> decoratedItemProcessor;
	

	public SingleItemToListProcessorDecorator(ItemProcessor<List<I>,List<O>> decoratedItemProcessor){
		this.decoratedItemProcessor = decoratedItemProcessor;		
	}

	public List<O> process(I item) throws Exception {
		List<I> inputList = new ArrayList<I>();
		inputList.add(item);
		return decoratedItemProcessor.process(inputList);
		
	}
}