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
package org.LexGrid.util.sql.sqlReconnect;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * This class is just a hack so that I can get the driver manager to connect to
 * a sql database with a driver that was loaded using a different class loader.
 * 
 * This class needs to be loaded by the same class loader that registered the
 * sql driver(s).
 * 
 * @author armbrust
 */
public interface ConnectionHelperIF {
    public Connection getConnection(String url, Properties properties) throws SQLException;

}