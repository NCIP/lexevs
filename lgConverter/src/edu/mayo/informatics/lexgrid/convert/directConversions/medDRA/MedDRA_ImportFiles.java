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

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;


/**
 *  @author <a href="mailto:hardie.linda@mayo.edu">Linda Hardie</a>
 *
*/
public class MedDRA_ImportFiles {
	
	private static String medDRADataDirectory = "C:/Users/m113216/Downloads/MedDRA_16_0_English/MedAscii/";

	public static void main(String [] args){
	//	testAndPrintCSVFiles();
		Database meddraDatabase = createDatabase();
		print(meddraDatabase);
	}

	@SuppressWarnings("unchecked")
    private static void print(Database database) {
		for(int i=0; i < MedDRA2LGConstants.medDRAClasses.length; i++){
			database.print(MedDRA2LGConstants.medDRAClasses[i]);
		}
		
	}

	public static void testAndPrintCSVFiles(){
		CSVReader reader;
		String [] nextLine;
		String input;
		
		try {
			for(int i=0; i < MedDRA2LGConstants.medDRADataFiles.length; i++){
				input = medDRADataDirectory + MedDRA2LGConstants.medDRADataFiles[i];
				reader = new CSVReader(new FileReader(input), '$');
				
				while ((nextLine = reader.readNext()) != null) {
				    // nextLine[] is an array of values from the line
				    System.out.println(nextLine[0] + ", " + nextLine[1] + "etc...");
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	@SuppressWarnings("unchecked")
    public static Database createDatabase(){
		Database meddraDatabase = new Database();
		String input;
		
		for(int i=0; i < MedDRA2LGConstants.medDRADataFiles.length; i++){
			input = medDRADataDirectory + MedDRA2LGConstants.medDRADataFiles[i];
			try {
				CSVReader reader = new CSVReader(new FileReader(input), '$');
				ColumnPositionMappingStrategy<DatabaseRecord> strat = new ColumnPositionMappingStrategy<DatabaseRecord>();
				strat.setType(MedDRA2LGConstants.medDRAClasses[i]);
				String[] columns = getFields(MedDRA2LGConstants.medDRAClasses[i]); 
			
				strat.setColumnMapping(columns);
	
				CsvToBean<DatabaseRecord> csv = new CsvToBean<DatabaseRecord>();
				List<DatabaseRecord> list = csv.parse(strat, reader);
				meddraDatabase.add(MedDRA2LGConstants.medDRAClasses[i].getName(), list);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		return meddraDatabase;
	}

	@SuppressWarnings("rawtypes")
    private static String[] getFields(Class class1) {
		Field [] fields = class1.getDeclaredFields();	
		String [] fieldnames = new String[fields.length - 1];
		
		for(int i=1; i < fields.length; i++){
			fieldnames[i-1] = fields[i].getName();
		}
		return fieldnames;
	}
}
