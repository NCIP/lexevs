package org.lexgrid.loader.writer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lexgrid.loader.data.codingScheme.CodingSchemeIdSetter;
import org.lexgrid.loader.wrappers.CodingSchemeIdHolder;
import org.springframework.batch.item.ItemWriter;

public abstract class AbstractCodingSchemeIdHolderWriter<T> extends AbstractDatabaseServiceWriter implements ItemWriter<CodingSchemeIdHolder<T>>{

	public void write(List<? extends CodingSchemeIdHolder<T>> list)
			throws Exception {
		Map<CodingSchemeUriVersionPair, List<T>> map = groupByCodingSchemeId(list);
		for(CodingSchemeUriVersionPair codingSchemeId : map.keySet()){
			doWrite(codingSchemeId, map.get(codingSchemeId));
		}
	}

	public abstract void doWrite(CodingSchemeUriVersionPair codingSchemeId, List<T> items);
	
	public Map<CodingSchemeUriVersionPair,List<T>> groupByCodingSchemeId(List<? extends CodingSchemeIdHolder<T>> list){
		Map<CodingSchemeUriVersionPair,List<T>> returnMap = new HashMap<CodingSchemeUriVersionPair,List<T>>();
		
		for(CodingSchemeIdHolder<T> holder : list){
			CodingSchemeUriVersionPair csId = 
				new CodingSchemeUriVersionPair(
						holder.getCodingSchemeIdSetter().getCodingSchemeUri(),
						holder.getCodingSchemeIdSetter().getCodingSchemeVersion());
			if(! returnMap.containsKey(csId)){
				returnMap.put(csId, new ArrayList<T>());
			}
			returnMap.get(csId).add(holder.getItem());
		}
		return returnMap;
	}
	
	protected class CodingSchemeUriVersionPair {
		private String uri;
		private String version;
		
		protected CodingSchemeUriVersionPair(String uri, String version){
			this.uri = uri;
			this.version = version;
		}
	
		public String getUri() {
			return uri;
		}

		public void setUri(String uri) {
			this.uri = uri;
		}

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((uri == null) ? 0 : uri.hashCode());
			result = prime * result
					+ ((version == null) ? 0 : version.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CodingSchemeUriVersionPair other = (CodingSchemeUriVersionPair) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (uri == null) {
				if (other.uri != null)
					return false;
			} else if (!uri.equals(other.uri))
				return false;
			if (version == null) {
				if (other.version != null)
					return false;
			} else if (!version.equals(other.version))
				return false;
			return true;
		}

		private AbstractCodingSchemeIdHolderWriter getOuterType() {
			return AbstractCodingSchemeIdHolderWriter.this;
		}
		
		
	}
	
}
