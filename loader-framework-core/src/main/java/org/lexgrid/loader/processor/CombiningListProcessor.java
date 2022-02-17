
package org.lexgrid.loader.processor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.ItemProcessor;

/**
 * The Class CombiningListProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class CombiningListProcessor<I,O> implements ItemProcessor<I,List<O>> {

	private List<ItemProcessor<I,? extends List<O>>> processors;
	
	public List<O> process(I item) throws Exception {
		List<O> returnList = new ArrayList<O>();
		for(ItemProcessor<I,? extends List<O>> processor : processors) {
			returnList.addAll(processor.process(item));
		}
		return returnList;
	}

	public List<ItemProcessor<I, ? extends List<O>>> getProcessors() {
		return processors;
	}

	public void setProcessors(List<ItemProcessor<I, ? extends List<O>>> processors) {
		this.processors = processors;
	}
}