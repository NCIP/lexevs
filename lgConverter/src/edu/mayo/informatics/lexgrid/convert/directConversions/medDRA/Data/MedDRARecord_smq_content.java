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



/**
 *  @author <a href="mailto:hardie.linda@mayo.edu">Linda Hardie</a>
 *
*/
public class MedDRARecord_smq_content implements Serializable, DatabaseRecord{

	private static final long serialVersionUID = 1L;
	
	private String smq_code;
	private String term_code;
	private String term_level;
	private String term_scope;
	private String term_category;
	private String term_weight;
    private String term_status;
    private String term_addition_version;
    private String term_last_modified_version;

    public String getSmq_code() {
		return smq_code;
	}

	public void setSmq_code(String smq_code) {
		this.smq_code = smq_code;
	}

	public String getTerm_code() {
		return term_code;
	}

	public void setTerm_code(String term_code) {
		this.term_code = term_code;
	}

	public String getTerm_level() {
		return term_level;
	}

	public void setTerm_level(String term_level) {
		this.term_level = term_level;
	}

	public String getTerm_scope() {
		return term_scope;
	}

	public void setTerm_scope(String term_scope) {
		this.term_scope = term_scope;
	}

	public String getTerm_category() {
		return term_category;
	}

	public void setTerm_category(String term_category) {
		this.term_category = term_category;
	}

	public String getTerm_weight() {
		return term_weight;
	}

	public void setTerm_weight(String term_weight) {
		this.term_weight = term_weight;
	}

	public String getTerm_status() {
		return term_status;
	}

	public void setTerm_status(String term_status) {
		this.term_status = term_status;
	}

	public String getTerm_addition_version() {
		return term_addition_version;
	}

	public void setTerm_addition_version(String term_addition_version) {
		this.term_addition_version = term_addition_version;
	}

	public String getTerm_last_modified_version() {
		return term_last_modified_version;
	}

	public void setTerm_last_modified_version(String term_last_modified_version) {
		this.term_last_modified_version = term_last_modified_version;
	}
}
