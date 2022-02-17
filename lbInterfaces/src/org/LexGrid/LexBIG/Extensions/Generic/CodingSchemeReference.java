
package org.LexGrid.LexBIG.Extensions.Generic;

import java.io.Serializable;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.codingSchemes.CodingScheme;

/**
 * A reference to a LexGrid {@link CodingScheme}.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 * @author <a href="mailto:scott.bauer@mayo.edu">Scott Bauer </a>
 */
public class CodingSchemeReference implements Serializable {

	private static final long serialVersionUID = -7922922055849944958L;

	private String codingScheme;
	
	private CodingSchemeVersionOrTag versionOrTag;

	public String getCodingScheme() {
		return codingScheme;
	}

	public void setCodingScheme(String codingScheme) {
		this.codingScheme = codingScheme;
	}

	public CodingSchemeVersionOrTag getVersionOrTag() {
		return versionOrTag;
	}

	public void setVersionOrTag(CodingSchemeVersionOrTag versionOrTag) {
		this.versionOrTag = versionOrTag;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codingScheme == null) ? 0 : codingScheme.hashCode());
		if(versionOrTag == null){
		return result = prime * result + 0;
		}
		else{
		result = prime * result + ((versionOrTag.getTag() == null) ? 0 : versionOrTag.getTag().hashCode());
		result = prime * result + ((versionOrTag.getVersion() == null ? 0 : versionOrTag.getVersion().hashCode()));
		}
		return result;
	}

	@Override
	public boolean equals(Object o){
		
		if(o == null){return false;}
		if(! (o instanceof CodingSchemeReference)){ return false;}
		CodingSchemeReference ref = (CodingSchemeReference)o;
		if(!this.getCodingScheme().equals(ref.getCodingScheme())){return false;}
		if(this.versionOrTag.getTag() == null && ref.getVersionOrTag().getTag() != null){return false;}
		if(this.versionOrTag.getTag() != null && ref.getVersionOrTag().getTag() == null){return false;}
		if(this.versionOrTag.getTag() != null && ref.getVersionOrTag().getTag() != null){
		if(!this.versionOrTag.getTag().equals(ref.getVersionOrTag().getTag())){return false;};
		}
		if(this.versionOrTag.getVersion() == null && ref.getVersionOrTag().getVersion() != null){return false;}
		if(this.versionOrTag.getVersion() != null && ref.getVersionOrTag().getVersion() == null){return false;}
		if(this.versionOrTag.getVersion() != null && ref.getVersionOrTag().getVersion() != null){
		if(!this.versionOrTag.getVersion().equals(ref.getVersionOrTag().getVersion())){return false;}
		}

		return true;
	}
	

}