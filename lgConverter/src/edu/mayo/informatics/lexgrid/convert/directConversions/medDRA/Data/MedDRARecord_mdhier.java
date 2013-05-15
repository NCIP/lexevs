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
public class MedDRARecord_mdhier implements Serializable, DatabaseRecord{

	private static final long serialVersionUID = 1L;
	
	private String pt_code;
	private String hlt_code;
	private String hlgt_code;
	private String soc_code;
	private String pt_name;
	private String hlt_name;
	private String hlgt_name;
	private String soc_name;
	private String soc_abbrev;
	private String null_field;
	private String pt_soc_code;
	private String primary_soc_fg;
	
	public String getPt_code() {
		return pt_code;
	}

	public void setPt_code(String pt_code) {
		this.pt_code = pt_code;
	}

	public String getHlt_code() {
		return hlt_code;
	}

	public void setHlt_code(String hlt_code) {
		this.hlt_code = hlt_code;
	}

	public String getHlgt_code() {
		return hlgt_code;
	}

	public void setHlgt_code(String hlgt_code) {
		this.hlgt_code = hlgt_code;
	}

	public String getSoc_code() {
		return soc_code;
	}

	public void setSoc_code(String soc_code) {
		this.soc_code = soc_code;
	}

	public String getPt_name() {
		return pt_name;
	}

	public void setPt_name(String pt_name) {
		this.pt_name = pt_name;
	}

	public String getHlt_name() {
		return hlt_name;
	}

	public void setHlt_name(String hlt_name) {
		this.hlt_name = hlt_name;
	}

	public String getHlgt_name() {
		return hlgt_name;
	}

	public void setHlgt_name(String hlgt_name) {
		this.hlgt_name = hlgt_name;
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

	public String getNull_field() {
		return null_field;
	}

	public void setNull_field(String null_field) {
		this.null_field = null_field;
	}

	public String getPt_soc_code() {
		return pt_soc_code;
	}

	public void setPt_soc_code(String pt_soc_code) {
		this.pt_soc_code = pt_soc_code;
	}

	public String getPrimary_soc_fg() {
		return primary_soc_fg;
	}

	public void setPrimary_soc_fg(String primary_soc_fg) {
		this.primary_soc_fg = primary_soc_fg;
	}
}
