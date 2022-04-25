package org.lexevs.dao.database.ibatis.sqlmap;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

public class CSVtoStringListTypeHandler implements TypeHandler<List<String>> {
	

	    @Override
	    public void setParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType) throws SQLException {
	        if (parameter != null) {
	            ps.setString(i, parameter.toString());
	        }
	    }

	    @Override
	    public List<String> getResult(ResultSet rs, String columnName) throws SQLException {
	        String value = rs.getString(columnName);
	        if (value != null) {
	            return Arrays.asList(value.split(","));
	        }
	        return null;
	    }

	    @Override
	    public List<String> getResult(ResultSet rs, int index) throws SQLException {
	        String value = rs.getString(index);
	        if (value != null) {
	            return Arrays.asList(value.split(","));
	        }
	        return null;
	    }

	    @Override
	    public List<String> getResult(CallableStatement cs, int index) throws SQLException {
	        String value = cs.getString(index);
	        if (value != null) {
	            return Arrays.asList(value.split(","));
	        }
	        return null;
	    }

}
