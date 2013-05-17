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

import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Comment;
import org.LexGrid.concepts.Definition;
import org.LexGrid.concepts.Presentation;



/**
 *  @author <a href="mailto:hardie.linda@mayo.edu">Linda Hardie</a>
 *
*/
public class MedDRARecord_hlgt implements Serializable, DatabaseEntityRecord{
	
	private static final long serialVersionUID = 1L;
	
	private String hlgt_code;
	private String hlgt_name;
	private String htgt_whoart_code;
	private String hlgt_harts_code;
	private String hlgt_costart_sym;
	private String hlgt_icd9_code;
	private String hlgt_icd9cm_code;
	private String hlgt_icd10_code;
	private String hlgt_jart_code;
	
	public String getHtgt_whoart_code() {
		return htgt_whoart_code;
	}
	public void setHtgt_whoart_code(String htgt_whoart_code) {
		this.htgt_whoart_code = htgt_whoart_code;
	}
	public String getHlgt_harts_code() {
		return hlgt_harts_code;
	}
	public void setHlgt_harts_code(String hlgt_harts_code) {
		this.hlgt_harts_code = hlgt_harts_code;
	}
	public String getHlgt_costart_sym() {
		return hlgt_costart_sym;
	}
	public void setHlgt_costart_sym(String hlgt_costart_sym) {
		this.hlgt_costart_sym = hlgt_costart_sym;
	}
	public String getHlgt_icd9_code() {
		return hlgt_icd9_code;
	}
	public void setHlgt_icd9_code(String hlgt_icd9_code) {
		this.hlgt_icd9_code = hlgt_icd9_code;
	}
	public String getHlgt_icd9cm_code() {
		return hlgt_icd9cm_code;
	}
	public void setHlgt_icd9cm_code(String hlgt_icd9cm_code) {
		this.hlgt_icd9cm_code = hlgt_icd9cm_code;
	}
	public String getHlgt_icd10_code() {
		return hlgt_icd10_code;
	}
	public void setHlgt_icd10_code(String hlgt_icd10_code) {
		this.hlgt_icd10_code = hlgt_icd10_code;
	}
	public String getHlgt_jart_code() {
		return hlgt_jart_code;
	}
	public void setHlgt_jart_code(String hlgt_jart_code) {
		this.hlgt_jart_code = hlgt_jart_code;
	}
	public String getHlgt_code() {
		return hlgt_code;
	}
	public void setHlgt_code(String hlgt_code) {
		this.hlgt_code = hlgt_code;
	}
	public String getHlgt_name() {
		return hlgt_name;
	}
	public void setHlgt_name(String hlgt_name) {
		this.hlgt_name = hlgt_name;
	}
	
	public String toString(){
		return "hlgt_code: " + this.hlgt_code + ", hlgt_name: " + this.hlgt_name;
	}
	
    @Override
    public String getCode() {
        return this.hlgt_code;
    }
    
    @Override
    public String getName() {
        return hlgt_name;
    }

    @Override
    public List<Presentation> getPresentations() {
        List<Presentation> presentations = new ArrayList<Presentation>();

        presentations.add(MedDRARecord_Utils.createPresentation("T-1", this.hlgt_name, "HG", true));
        
        return presentations;
    }
    
    @Override
    public List<Definition> getDefinitions() {
        List<Definition> definitions = new ArrayList<Definition>();
        
        return definitions;
    }

    @Override
    public List<Comment> getComments() {
        List<Comment> comments = new ArrayList<Comment>();
        
        return comments;
    }

    @Override
    public List<Property> getProperties() {
        List<Property> properties = new ArrayList<Property>();

        return properties;
    }
}
