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

import edu.mayo.informatics.lexgrid.convert.directConversions.medDRA.MedDRA2LGConstants.MedDRA_METADATA;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;


/**
 *  @author <a href="mailto:hardie.linda@mayo.edu">Linda Hardie</a>
 *
*/
public class MedDRA_ImportFiles {
	
	private static String medDRADataDirectory = "C:/Users/m113216/Downloads/MedDRA_16_0_English/MedAscii/";
	private static MedDRA_METADATA [] meddraMetaData = MedDRA_METADATA.values();
	public static void main(String [] args){
	//	testAndPrintCSVFiles();
		Database meddraDatabase = createDatabase();
		print(meddraDatabase);
	}

    private static void print(Database database) {
		for(int i=0; i < meddraMetaData.length; i++){
			database.print(meddraMetaData[i].classname());
		}
	}

	public static void testAndPrintCSVFiles(){
		CSVReader reader;
		String [] nextLine;
		String input;
		
		try {
			for(int i=0; i < meddraMetaData.length; i++){
				input = medDRADataDirectory + meddraMetaData[i].filename();
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
	
    public static Database createDatabase(){
		Database meddraDatabase = new Database();
		String input;
		
		for(int i=0; i < meddraMetaData.length; i++){
			input = medDRADataDirectory + meddraMetaData[i].filename();
			try {
				CSVReader reader = new CSVReader(new FileReader(input), '$');
				ColumnPositionMappingStrategy<DatabaseRecord> strat = new ColumnPositionMappingStrategy<DatabaseRecord>();
				strat.setType(meddraMetaData[i].classname());
				String[] columns = getFields(meddraMetaData[i].classname()); 
			
				strat.setColumnMapping(columns);
	
				CsvToBean<DatabaseRecord> csv = new CsvToBean<DatabaseRecord>();
				List<DatabaseRecord> list = csv.parse(strat, reader);
				meddraDatabase.add(meddraMetaData[i].classname().getName(), list);
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
