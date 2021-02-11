
package org.lexgrid.loader.meta.processor;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;
import org.apache.commons.collections.CollectionUtils;
import org.lexgrid.loader.reader.support.CompositeReaderChunk;
import org.lexgrid.loader.rrf.model.Mrconso;
import org.lexgrid.loader.rrf.model.Mrdef;
import org.lexgrid.loader.rrf.model.Mrsat;
import org.lexgrid.loader.rrf.model.Mrsty;
import org.lexgrid.loader.rrf.processor.MrconsoGroupEntityProcessor;
import org.lexgrid.loader.wrappers.CodingSchemeIdHolder;
import org.lexgrid.loader.wrappers.ParentIdHolder;
import org.springframework.batch.item.ItemProcessor;

public class MetaTotalEntityProcessor implements 
	ItemProcessor<CompositeReaderChunk<CompositeReaderChunk<Mrconso,Mrsat>,CompositeReaderChunk<Mrsty,Mrdef>>, CodingSchemeIdHolder<Entity>>{
	
	private MrconsoGroupEntityProcessor mrconsoGroupEntityProcessor;
	private ItemProcessor<Mrdef,ParentIdHolder<Property>> mrdefItemProcessor;
	private ItemProcessor<Mrsty,ParentIdHolder<Property>> mrstyItemProcessor;
	private ItemProcessor<Mrsat,ParentIdHolder<Property>> mrsatItemProcessor;

	@Override
	public CodingSchemeIdHolder<Entity> process(
			CompositeReaderChunk<CompositeReaderChunk<Mrconso, Mrsat>, CompositeReaderChunk<Mrsty, Mrdef>> item)
			throws Exception {
		
		if(item.getItem1List() == null || item.getItem1List().get(0) == null || item.getItem1List().size() == 0) {
			return null;
		}
		
		CompositeReaderChunk<Mrconso, Mrsat> composite1 = item.getItem1List().get(0);

		CodingSchemeIdHolder<Entity> entityHolder = this.mrconsoGroupEntityProcessor.process(composite1.getItem1List());
		
		if(entityHolder == null || entityHolder.getItem() == null) {
			return null;
		}
		
		Entity entity = entityHolder.getItem();
		
		if(CollectionUtils.isNotEmpty(composite1.getItem2List())) {
			for(Mrsat mrsat : composite1.getItem2List()){
				entity.addAnyProperty(
						this.mrsatItemProcessor.process(mrsat).getItem());
			}
		}

		if(CollectionUtils.isNotEmpty(item.getItem2List())) {
			CompositeReaderChunk<Mrsty, Mrdef> composite2 = item.getItem2List().get(0);
			
			if(CollectionUtils.isNotEmpty(composite2.getItem1List())){
				for(Mrsty mrsty : composite2.getItem1List()){
					entity.addAnyProperty(
							this.mrstyItemProcessor.process(mrsty).getItem());
				}
			}

			if(CollectionUtils.isNotEmpty(composite2.getItem2List())){
				for(Mrdef mrdef : composite2.getItem2List()){
					entity.addAnyProperty(
							this.mrdefItemProcessor.process(mrdef).getItem());
				}
			}
		}
		
		return entityHolder;
	}

	public void setMrconsoGroupEntityProcessor(
			MrconsoGroupEntityProcessor mrconsoGroupEntityProcessor) {
		this.mrconsoGroupEntityProcessor = mrconsoGroupEntityProcessor;
	}

	public MrconsoGroupEntityProcessor getMrconsoGroupEntityProcessor() {
		return mrconsoGroupEntityProcessor;
	}


	public ItemProcessor<Mrdef, ParentIdHolder<Property>> getMrdefItemProcessor() {
		return mrdefItemProcessor;
	}


	public void setMrdefItemProcessor(
			ItemProcessor<Mrdef, ParentIdHolder<Property>> mrdefItemProcessor) {
		this.mrdefItemProcessor = mrdefItemProcessor;
	}


	public ItemProcessor<Mrsty, ParentIdHolder<Property>> getMrstyItemProcessor() {
		return mrstyItemProcessor;
	}


	public void setMrstyItemProcessor(
			ItemProcessor<Mrsty, ParentIdHolder<Property>> mrstyItemProcessor) {
		this.mrstyItemProcessor = mrstyItemProcessor;
	}


	public ItemProcessor<Mrsat, ParentIdHolder<Property>> getMrsatItemProcessor() {
		return mrsatItemProcessor;
	}


	public void setMrsatItemProcessor(
			ItemProcessor<Mrsat, ParentIdHolder<Property>> mrsatItemProcessor) {
		this.mrsatItemProcessor = mrsatItemProcessor;
	}
}