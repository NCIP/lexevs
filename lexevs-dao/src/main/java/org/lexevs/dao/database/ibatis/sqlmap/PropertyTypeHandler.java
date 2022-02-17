
package org.lexevs.dao.database.ibatis.sqlmap;

import java.sql.SQLException;

import org.LexGrid.commonTypes.types.PropertyTypes;

import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;

/**
 * An iBATIS type handler callback for Property types
 */
public class PropertyTypeHandler implements TypeHandlerCallback{

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
        
        PropertyTypes propType = (PropertyTypes) parameter;
        setter.setString(propType.value().toLowerCase());
    }

    /**
     * From DB to Java.
     */
    public Object getResult(ResultGetter getter) throws SQLException {
        final String dbValue = getter.getString();
        
        if (dbValue == null)
        	return null;
        
        final Object dbResult = valueOf(dbValue);
        
        return PropertyTypes.fromValue(dbResult.toString().toLowerCase());
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