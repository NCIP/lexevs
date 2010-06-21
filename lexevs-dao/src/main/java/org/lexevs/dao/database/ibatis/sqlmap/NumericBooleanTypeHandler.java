package org.lexevs.dao.database.ibatis.sqlmap;

import java.sql.SQLException;
import java.sql.Types;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;

public class NumericBooleanTypeHandler implements TypeHandlerCallback {
	
	public void setParameter(ParameterSetter setter, Object parameter) {
		if(parameter == null) {
			try {
				setter.setNull(Types.CHAR);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		} else {
			try {
				setter.setBoolean(BooleanUtils.toBoolean(parameter.toString()));
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}

    public Object getResult(ResultGetter getter) throws SQLException {
    	String booleanString = getter.getString();
    	if(StringUtils.isNotBlank(booleanString)) {
    		if(StringUtils.isNumeric(booleanString)){
    		 return BooleanUtils.toBoolean(Integer.valueOf(getter.getString()));
    		} else {
    			return BooleanUtils.toBoolean(getter.getString());
    		}
    	} else {
    		return getter.getBoolean();
    	}
    }

	@Override
	public Object valueOf(String value) {
		return value;
	}
}