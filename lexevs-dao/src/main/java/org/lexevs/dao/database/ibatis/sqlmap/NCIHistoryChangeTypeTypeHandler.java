
package org.lexevs.dao.database.ibatis.sqlmap;

import java.sql.SQLException;

import org.LexGrid.LexBIG.DataModel.NCIHistory.types.ChangeType;

import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;

public class NCIHistoryChangeTypeTypeHandler implements TypeHandlerCallback {

    public void setParameter(ParameterSetter setter, Object parameter) 
        throws SQLException
    {
        if (parameter == null) {
            setter.setString(null);
            return;
        } 
        
        ChangeType changeType = (ChangeType) parameter;
        setter.setString(changeType.toString());
    }

/**
     * From DB to Java.
     */
public Object getResult(ResultGetter getter) throws SQLException {
        final String dbValue = getter.getString();
        
        if (dbValue == null) {
        	return null;
        }
        
       return ChangeType.fromValue(dbValue);
    }

	@Override
	public Object valueOf(String s) {
		return s;
	}
}