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
    	if(StringUtils.isNotBlank(getter.getString())) {
    		return BooleanUtils.toBoolean(getter.getString());
    	} else {
    		return getter.getBoolean();
    	}
    }

	@Override
	public Object valueOf(String value) {
		return value;
	}
}