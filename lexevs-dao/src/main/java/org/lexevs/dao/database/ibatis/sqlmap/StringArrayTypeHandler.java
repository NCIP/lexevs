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