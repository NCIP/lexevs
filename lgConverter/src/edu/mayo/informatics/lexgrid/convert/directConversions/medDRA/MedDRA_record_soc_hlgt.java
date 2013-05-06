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
public class MedDRA_record_soc_hlgt implements Serializable, DatabaseRecord{

	private static final long serialVersionUID = 1L;

	private String soc_code;
	private String hlgt_code;
	
	
	public String getSoc_code() {
		return soc_code;
	}


	public void setSoc_code(String soc_code) {
		this.soc_code = soc_code;
	}


	public String getHlgt_code() {
		return hlgt_code;
	}


	public void setHlgt_code(String hlgt_code) {
		this.hlgt_code = hlgt_code;
	}
}
