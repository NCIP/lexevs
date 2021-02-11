
package org.lexgrid.loader.processor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.ItemProcessor;

/**
 * The Class CombiningProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class CombiningProcessor<I,O> implements ItemProcessor<I,List<O>> {

	private List<ItemProcessor<I,O>> processors;
	
	public List<O> process(I item) throws Exception {
		List<O> returnList = new ArrayList<O>();
		for(ItemProcessor<I,O> processor : processors) {
			returnList.add(processor.process(item));
		}
		return returnList;
	}

	public List<ItemProcessor<I, O>> getProcessors() {
		return processors;
	}

	public void setProcessors(List<ItemProcessor<I, O>> processors) {
		this.processors = processors;
	}
}