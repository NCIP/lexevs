package org.lexevs.dao.database.ibatis.sqlmap;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;

public class DateTypeHandler implements TypeHandlerCallback {


	@Override
	public Object getResult(ResultGetter getter) throws SQLException {
        final Date dbValue = getter.getDate();
        return dbValue;
	}

	@Override
	public void setParameter(ParameterSetter setter, Object dateString)
			throws SQLException {
		try {
		SimpleDateFormat formatter = new SimpleDateFormat("MMMM d, YYYY");
		java.util.Date jDate = formatter.parse(dateString.toString());
		java.sql.Date date = new java.sql.Date(jDate.getTime());
		setter.setDate(date);
		} catch (ParseException e) {
			System.out.println("Cannot format date string: " + dateString.toString());
			e.printStackTrace();
		}
	}

	@Override
	public Object valueOf(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
