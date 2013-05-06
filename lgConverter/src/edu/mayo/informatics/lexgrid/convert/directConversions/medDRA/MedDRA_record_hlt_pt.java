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
public class MedDRA_record_hlt_pt implements Serializable, DatabaseRecord{

	private static final long serialVersionUID = 1L;
	
	private String htl_code;
	private String pt_code;
	
	public String getHtl_code() {
		return htl_code;
	}

	public void setHtl_code(String htl_code) {
		this.htl_code = htl_code;
	}

	public String getPt_code() {
		return pt_code;
	}

	public void setPt_code(String pt_code) {
		this.pt_code = pt_code;
	}
}
