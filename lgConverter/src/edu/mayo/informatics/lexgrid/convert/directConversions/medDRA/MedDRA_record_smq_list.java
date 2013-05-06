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
public class MedDRA_record_smq_list implements Serializable, DatabaseRecord{

	private static final long serialVersionUID = 1L;
	
	private String smq_code;
	private String smq_name;
	private String smq_level;
	private String smq_description;
	private String smq_source;
	private String smq_note;
	private String MedDRA_version;
	public String getSmq_code() {
		return smq_code;
	}

	public void setSmq_code(String smq_code) {
		this.smq_code = smq_code;
	}

	public String getSmq_name() {
		return smq_name;
	}

	public void setSmq_name(String smq_name) {
		this.smq_name = smq_name;
	}

	public String getSmq_level() {
		return smq_level;
	}

	public void setSmq_level(String smq_level) {
		this.smq_level = smq_level;
	}

	public String getSmq_description() {
		return smq_description;
	}

	public void setSmq_description(String smq_description) {
		this.smq_description = smq_description;
	}

	public String getSmq_source() {
		return smq_source;
	}

	public void setSmq_source(String smq_source) {
		this.smq_source = smq_source;
	}

	public String getSmq_note() {
		return smq_note;
	}

	public void setSmq_note(String smq_note) {
		this.smq_note = smq_note;
	}

	public String getMedDRA_version() {
		return MedDRA_version;
	}

	public void setMedDRA_version(String medDRA_version) {
		MedDRA_version = medDRA_version;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSmq_algorithm() {
		return smq_algorithm;
	}

	public void setSmq_algorithm(String smq_algorithm) {
		this.smq_algorithm = smq_algorithm;
	}

	private String status;
	private String smq_algorithm;

}
