/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.lexevs.dao.database.ibatis.sqlmap;

import java.sql.SQLException;
import java.sql.Types;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;

public class NumericBooleanTypeHandler implements TypeHandlerCallback {
	
	private static String TRUE = "1";
	private static String FALSE = "0";
	
	public void setParameter(ParameterSetter setter, Object parameter) {
		if(parameter == null) {
			try {
				setter.setNull(Types.CHAR);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		} else {
			try {
				Boolean bool = (Boolean)parameter;
				if(bool) {
					setter.setString(TRUE);
				}else {
					setter.setString(FALSE);
				}
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
    		return null;
    	}
    }

	@Override
	public Object valueOf(String value) {
		return value;
	}
}