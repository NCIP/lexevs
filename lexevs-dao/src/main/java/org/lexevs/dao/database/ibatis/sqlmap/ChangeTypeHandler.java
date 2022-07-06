package org.lexevs.dao.database.ibatis.sqlmap;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.LexGrid.LexBIG.DataModel.NCIHistory.types.ChangeType;
import org.apache.ibatis.type.JdbcType;

public class ChangeTypeHandler implements org.apache.ibatis.type.TypeHandler<ChangeType>{
	
	public ChangeTypeHandler() {super();}
	

	@Override
	public ChangeType getResult(ResultSet rs, String columnName) throws SQLException {
		return convert(rs.getString(columnName));
	}

	@Override
	public ChangeType getResult(ResultSet rs, int columnIndex) throws SQLException {
		 return convert(rs.getNString(columnIndex));
	}

	@Override
	public ChangeType getResult(CallableStatement stmt, int columnIndex) throws SQLException {
		 return convert(stmt.getNString(columnIndex));
	}

	@Override
	public void setParameter(PreparedStatement stmt, int columnIndex, ChangeType type, JdbcType jdbcType)
			throws SQLException {
		stmt.setString(columnIndex,type.value());
		
	}
	
	private ChangeType convert(String dbValue){
        if (dbValue == null) {
        	return null;
        }
        
       return ChangeType.fromValue(dbValue);
		}

}
