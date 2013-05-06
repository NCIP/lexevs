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

package edu.mayo.informatics.lexgrid.convert.directConversions.medDRA;

import java.io.Serializable;


/**
 *  @author <a href="mailto:hardie.linda@mayo.edu">Linda Hardie</a>
 *
*/
public class MedDRA_record_hlt implements Serializable, DatabaseRecord{

	private static final long serialVersionUID = 1L;
	
	private String hlt_code;
	private String hlt_name;
	private String hlt_whoart_code;
	private String hlt_harts_code;
	private String hlt_costart_sym;
	private String hlt_icd9_code;
	private String hlt_icd9cm_code;
	private String hlt_icd10_code;
	private String hlt_jart_code;
	
	public String getHlt_code() {
		return hlt_code;
	}

	public void setHlt_code(String hlt_code) {
		this.hlt_code = hlt_code;
	}

	public String getHlt_name() {
		return hlt_name;
	}

	public void setHlt_name(String hlt_name) {
		this.hlt_name = hlt_name;
	}

	public String getHlt_whoart_code() {
		return hlt_whoart_code;
	}

	public void setHlt_whoart_code(String hlt_whoart_code) {
		this.hlt_whoart_code = hlt_whoart_code;
	}

	public String getHlt_harts_code() {
		return hlt_harts_code;
	}

	public void setHlt_harts_code(String hlt_harts_code) {
		this.hlt_harts_code = hlt_harts_code;
	}

	public String getHlt_costart_sym() {
		return hlt_costart_sym;
	}

	public void setHlt_costart_sym(String hlt_costart_sym) {
		this.hlt_costart_sym = hlt_costart_sym;
	}

	public String getHlt_icd9_code() {
		return hlt_icd9_code;
	}

	public void setHlt_icd9_code(String hlt_icd9_code) {
		this.hlt_icd9_code = hlt_icd9_code;
	}

	public String getHlt_icd9cm_code() {
		return hlt_icd9cm_code;
	}

	public void setHlt_icd9cm_code(String hlt_icd9cm_code) {
		this.hlt_icd9cm_code = hlt_icd9cm_code;
	}

	public String getHlt_icd10_code() {
		return hlt_icd10_code;
	}

	public void setHlt_icd10_code(String hlt_icd10_code) {
		this.hlt_icd10_code = hlt_icd10_code;
	}

	public String getHlt_jart_code() {
		return hlt_jart_code;
	}

	public void setHlt_jart_code(String hlt_jart_code) {
		this.hlt_jart_code = hlt_jart_code;
	}

}
