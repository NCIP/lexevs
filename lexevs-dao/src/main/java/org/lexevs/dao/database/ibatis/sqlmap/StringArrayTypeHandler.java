
package org.lexevs.dao.database.ibatis.sqlmap;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;

/**
 * An iBATIS type handler callback for String Array types
 */
public class StringArrayTypeHandler implements TypeHandlerCallback{

    /**
     * From Java to DB.
     */
    public void setParameter(ParameterSetter setter, Object parameter) 
        throws SQLException
    {
        if (parameter == null) {
            setter.setString(null);
            return;
        } 
        
        List<String> associationList = Arrays.asList(parameter.toString().split(","));
        String associations = null;
        for (int i = 0; i < associationList.size(); i++) {
			String assoc = (String) associationList.get(i);
			associations = i == 0 ? assoc
					: (associations += ("," + assoc));
		}
        setter.setString(associations);
    }

    /**
     * From DB to Java.
     */
    public Object getResult(ResultGetter getter) throws SQLException {
        final String dbValue = getter.getString();
        
        final Object dbResult = valueOf(dbValue);
        
        String[] result = null;
        
        if (dbResult != null)
        {
        	result = dbResult.toString().trim().split(",");
        } else {
        	result = new String[0];
        }

        return result;
    }

	@Override
	public Object valueOf(String s) {
		if(s == null) {
            return null;
        }
		
        final String value = s;
       
        return value;
	}
}