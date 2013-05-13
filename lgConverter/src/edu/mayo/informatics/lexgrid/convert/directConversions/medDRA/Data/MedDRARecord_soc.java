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

package edu.mayo.informatics.lexgrid.convert.directConversions.medDRA.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.LexGrid.commonTypes.Text;
import org.LexGrid.concepts.Comment;
import org.LexGrid.concepts.Definition;
import org.LexGrid.concepts.Presentation;



/**
 *  @author <a href="mailto:hardie.linda@mayo.edu">Linda Hardie</a>
 *
*/
public class MedDRARecord_soc implements Serializable, DatabaseRecord{

	private static final long serialVersionUID = 1L;
	
	private String soc_code;
	private String soc_name;
	private String soc_abbrev;
	private String soc_whoart_code;
	private String soc_harts_code;
	private String soc_costart_sym;
	private String soc_icd9_code;
	private String soc_icd9cm_code;
	private String soc_icd10_code;
	private String soc_jart_code;
	
	public String getSoc_code() {
		return soc_code;
	}

	public void setSoc_code(String soc_code) {
		this.soc_code = soc_code;
	}

	public String getSoc_name() {
		return soc_name;
	}

	public void setSoc_name(String soc_name) {
		this.soc_name = soc_name;
	}

	public String getSoc_abbrev() {
		return soc_abbrev;
	}

	public void setSoc_abbrev(String soc_abbrev) {
		this.soc_abbrev = soc_abbrev;
	}

	public String getSoc_whoart_code() {
		return soc_whoart_code;
	}

	public void setSoc_whoart_code(String soc_whoart_code) {
		this.soc_whoart_code = soc_whoart_code;
	}

	public String getSoc_harts_code() {
		return soc_harts_code;
	}

	public void setSoc_harts_code(String soc_harts_code) {
		this.soc_harts_code = soc_harts_code;
	}

	public String getSoc_costart_sym() {
		return soc_costart_sym;
	}

	public void setSoc_costart_sym(String soc_costart_sym) {
		this.soc_costart_sym = soc_costart_sym;
	}

	public String getSoc_icd9_code() {
		return soc_icd9_code;
	}

	public void setSoc_icd9_code(String soc_icd9_code) {
		this.soc_icd9_code = soc_icd9_code;
	}

	public String getSoc_icd9cm_code() {
		return soc_icd9cm_code;
	}

	public void setSoc_icd9cm_code(String soc_icd9cm_code) {
		this.soc_icd9cm_code = soc_icd9cm_code;
	}

	public String getSoc_icd10_code() {
		return soc_icd10_code;
	}

	public void setSoc_icd10_code(String soc_icd10_code) {
		this.soc_icd10_code = soc_icd10_code;
	}

	public String getSoc_jart_code() {
		return soc_jart_code;
	}

	public void setSoc_jart_code(String soc_jart_code) {
		this.soc_jart_code = soc_jart_code;
	}

    @Override
    public boolean isMap() {
        return false;
    }

    @Override
    public boolean isEntity() {
        return true;
    }

    @Override
    public String getCode() {
        return soc_code;
    }

    @Override
    public List<Presentation> getPresentations() {
        List<Presentation> presentations = new ArrayList<Presentation>();
        Text txt;
        
        Presentation abbreviation = new Presentation();
        abbreviation.setIsPreferred(true);
        abbreviation.setIsActive(true);
        abbreviation.setPropertyName("Abbreviation");
        txt = new Text();
        txt.setContent((String) this.soc_abbrev);
        abbreviation.setValue(txt);
        
        Presentation name = new Presentation();
        name.setIsPreferred(false);
        name.setIsActive(true);
        name.setPropertyName("Name");
        txt = new Text();
        txt.setContent((String) this.soc_name);
        name.setValue(txt);
        
        presentations.add(abbreviation);
        presentations.add(name);
        
        return presentations;
    }

    @Override
    public List<Definition> getDefinitions() {
        return null;
    }

    @Override
    public List<Comment> getComments() {
        return null;
    }

}
