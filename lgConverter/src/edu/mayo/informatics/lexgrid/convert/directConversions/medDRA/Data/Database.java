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

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

/**
 *  @author <a href="mailto:hardie.linda@mayo.edu">Linda Hardie</a>
 *
*/
public class Database {
	private HashMap<String, List<DatabaseRecord>> tables;
	
	public Database(){
		tables = new HashMap<String, List<DatabaseRecord>>();
	}
	
	public void add(String tablename, List<DatabaseRecord> record){
	    tables.put(tablename, record);
	}
		
    public List<DatabaseRecord> get(String tablename){
        return tables.get(tablename);
    }
    
    private void print(Field[] declaredFields) {
        System.out.println("Field values...");
    }

	public void print(Class<?> class1){
		String tablename = class1.getName();
		List<DatabaseRecord> records = tables.get(tablename);
		if(records == null){
			return;
		}
		
		System.out.println("Printing table: " + tablename);
		print(class1.getDeclaredFields());
		
		for(DatabaseRecord record : records){
			print(record);
		}
	}
	
	public void print(DatabaseRecord record){
		for(Field field : record.getClass().getDeclaredFields()){
			try {
				field.setAccessible(true);
				String name = field.getName();
				Object value;
				value = field.get(record);
				System.out.print(name + "\t" + value);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		System.out.println();
	}
	
}
