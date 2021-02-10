
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