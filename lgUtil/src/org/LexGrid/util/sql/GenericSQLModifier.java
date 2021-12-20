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
package org.LexGrid.util.sql;

import java.sql.Connection;
import java.util.Hashtable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A class to read my quasi-sql and convert it to database specific sql.
 * 
 * Currently supports MySQL, MS Access, PostgreSQL, DB2 (various flavors),
 * HyperSonic SQL and Microsoft SQL Server
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 */
public class GenericSQLModifier {
    private String databaseType_;
    private Hashtable datatypeConversion_;
    private String[] customDatatypes_;
    private String properQuote_;

    private static Logger log = LogManager.getLogger("convert.sqlMod");

    // CTS needs to override the LIKE replacement with a different string
    // to get case insentive queries.
    public static String mySqlLikeOverride;

    /**
     * @param connection
     *            the connection
     * @param sqlLite
     *            true if connection is SQLLite, false if SQL
     * @throws Exception
     */
    public GenericSQLModifier(Connection connection, boolean sqlLite) throws Exception {
        this(connection.getMetaData().getDatabaseProductName(), sqlLite);
    }

    /**
     * @param connection
     *            the connection
     * @throws Exception
     */
    public GenericSQLModifier(Connection connection) throws Exception {
        this(connection.getMetaData().getDatabaseProductName(), false);
    }

    /**
     * 
     * @param databaseType
     *            may be "MySQL", "ACCESS", "PostgreSQL", "DB2*",
     *            "Microsoft SQL Server", "HSQL Database Engine"
     * @param sqlLite
     *            if true, database is SQLLite, if false database is SQL
     * @throws Exception
     */
    public GenericSQLModifier(String databaseType, boolean sqlLite) throws Exception {
        // need the sqlLite table, because it uses access booleans, while the
        // others don't
        // because access booleans don't support null
        databaseType_ = databaseType;

        datatypeConversion_ = new Hashtable();
        customDatatypes_ = new String[] { "{boolean}", "{IF NOT EXISTS}", "{limitedText}", "{unlimitedText}", "{blob}",
                "{bigInt}", "{TYPE}", "{DROPFOREIGNKEY}", "{true}", "{false}", "{LIMIT}", "{DEFAULT_INDEX_SIZE}",
                "{LIKE}", "{BINARY}", "{CASCADE}", "{lgCharSet}", "{lgTableCharSet}", "{dateTime}", "{AS}",
                "{tempTableAs}", "{tempTable}" };
        if (databaseType_.equals("MySQL")) {
            properQuote_ = "`";
            datatypeConversion_.put("{boolean}", "tinyint(1)");
            datatypeConversion_.put("{IF NOT EXISTS}", "IF NOT EXISTS");
            datatypeConversion_.put("{limitedText}", "varchar");
            datatypeConversion_.put("{unlimitedText}", "text");
            datatypeConversion_.put("{blob}", "BLOB");
            datatypeConversion_.put("{bigInt}", "bigint(20)");
            datatypeConversion_.put("{TYPE}", "TYPE=InnoDB");
            datatypeConversion_.put("{DROPFOREIGNKEY}", "DROP FOREIGN KEY");
            datatypeConversion_.put("{true}", "1");
            datatypeConversion_.put("{false}", "0");
            datatypeConversion_.put("{LIMIT}", "LIMIT ?, ?");
            datatypeConversion_.put("{DEFAULT_INDEX_SIZE}", "(3)");
            datatypeConversion_.put("{LIKE}", (mySqlLikeOverride == null || mySqlLikeOverride.length() == 0 ? "LIKE"
                    : mySqlLikeOverride));
            datatypeConversion_.put("{BINARY}", "BINARY");
            datatypeConversion_.put("{CASCADE}", "CASCADE");
            datatypeConversion_.put("{lgCharSet}", "CHARACTER SET utf8 COLLATE utf8_bin");
            datatypeConversion_.put("{lgTableCharSet}", "CHARACTER SET utf8 COLLATE utf8_bin");
            datatypeConversion_.put("{dateTime}", "DATETIME");
            datatypeConversion_.put("{AS}", "AS");
            datatypeConversion_.put("{tempTableAs}", "AS");
            datatypeConversion_.put("{tempTable}", "TEMPORARY");
        } else if (databaseType_.equals("ACCESS")) {
            properQuote_ = "`";
            if (sqlLite) {
                datatypeConversion_.put("{boolean}", "yesno");
            } else {
                datatypeConversion_.put("{boolean}", "Text(5)"); // access
                                                                 // booleans
                                                                 // don't allow
                                                                 // null...
            }
            datatypeConversion_.put("{IF NOT EXISTS}", "");
            datatypeConversion_.put("{limitedText}", "Text");
            datatypeConversion_.put("{unlimitedText}", "memo");
            datatypeConversion_.put("{bigInt}", "Number");
            datatypeConversion_.put("{TYPE}", "");
            datatypeConversion_.put("{DROPFOREIGNKEY}", "DROP CONSTRAINT");
            datatypeConversion_.put("{true}", "true");
            datatypeConversion_.put("{false}", "false");
            datatypeConversion_.put("{LIMIT}", "");
            datatypeConversion_.put("{DEFAULT_INDEX_SIZE}", "");
            datatypeConversion_.put("{LIKE}", "LIKE");
            datatypeConversion_.put("{BINARY}", "");
            datatypeConversion_.put("{CASCADE}", "CASCADE");
            datatypeConversion_.put("{lgCharSet}", "");
            datatypeConversion_.put("{lgTableCharSet}", "");
            datatypeConversion_.put("{dateTime}", "DATETIME");
            datatypeConversion_.put("{AS}", "AS");
            datatypeConversion_.put("{tempTableAs}", "");
            datatypeConversion_.put("{tempTable}", "");
            datatypeConversion_.put("[limitedTextLimit]", "255"); // automatically
                                                                  // switch
                                                                  // limitedText
                                                                  // requests to
                                                                  // unlimitedText
                                                                  // types if
                                                                  // the size is
                                                                  // > that
                                                                  // this.
        } else if (databaseType_.equals("PostgreSQL")) {
            properQuote_ = "";
            datatypeConversion_.put("{boolean}", "bool");
            datatypeConversion_.put("{IF NOT EXISTS}", "");
            datatypeConversion_.put("{limitedText}", "varchar");
            datatypeConversion_.put("{unlimitedText}", "text");
            datatypeConversion_.put("{blob}", "bytea");
            datatypeConversion_.put("{bigInt}", "int8");
            datatypeConversion_.put("{TYPE}", "");
            datatypeConversion_.put("{DROPFOREIGNKEY}", "DROP CONSTRAINT");
            datatypeConversion_.put("{true}", "'true'");
            datatypeConversion_.put("{false}", "'false'");
            datatypeConversion_.put("{LIMIT}", "");
            datatypeConversion_.put("{DEFAULT_INDEX_SIZE}", "");
            datatypeConversion_.put("{LIKE}", "ILIKE");
            datatypeConversion_.put("{BINARY}", "");
            datatypeConversion_.put("{CASCADE}", "CASCADE");
            datatypeConversion_.put("{lgCharSet}", "ENCODING='UTF8'");
            datatypeConversion_.put("{lgTableCharSet}", "");
            datatypeConversion_.put("{dateTime}", "TIMESTAMP");
            datatypeConversion_.put("{AS}", "AS");
            datatypeConversion_.put("{tempTableAs}", "AS");
            datatypeConversion_.put("{tempTable}", "TEMPORARY");
        } else if (databaseType_.startsWith("DB2")) {
            properQuote_ = "";
            datatypeConversion_.put("{boolean}", "smallint");
            datatypeConversion_.put("{IF NOT EXISTS}", "");
            datatypeConversion_.put("{limitedText}", "varchar");
            datatypeConversion_.put("{unlimitedText}", "long varchar");
            datatypeConversion_.put("{blob}", "BLOB");
            datatypeConversion_.put("{bigInt}", "integer");
            datatypeConversion_.put("{TYPE}", "");
            datatypeConversion_.put("{DROPFOREIGNKEY}", "DROP CONSTRAINT");
            datatypeConversion_.put("{true}", "1");
            datatypeConversion_.put("{false}", "0");
            datatypeConversion_.put("{LIMIT}", "");
            datatypeConversion_.put("{DEFAULT_INDEX_SIZE}", "");
            datatypeConversion_.put("{LIKE}", "LIKE");
            datatypeConversion_.put("{BINARY}", "");
            datatypeConversion_.put("{CASCADE}", "");
            datatypeConversion_.put("{lgCharSet}", "");
            datatypeConversion_.put("{lgTableCharSet}", "");
            datatypeConversion_.put("{dateTime}", "TIMESTAMP");
            datatypeConversion_.put("{AS}", "AS");
            datatypeConversion_.put("{tempTableAs}", "");
            datatypeConversion_.put("{tempTable}", "TEMPORARY");
        } else if (databaseType_.equals("Microsoft SQL Server")) {
            properQuote_ = "\"";
            datatypeConversion_.put("{boolean}", "tinyint");
            datatypeConversion_.put("{IF NOT EXISTS}", "");
            datatypeConversion_.put("{limitedText}", "varchar");
            datatypeConversion_.put("{unlimitedText}", "varchar(8000)");
            datatypeConversion_.put("{bigInt}", "bigint");
            datatypeConversion_.put("{TYPE}", "");
            datatypeConversion_.put("{DROPFOREIGNKEY}", "DROP CONSTRAINT");
            datatypeConversion_.put("{true}", "1");
            datatypeConversion_.put("{false}", "0");
            datatypeConversion_.put("{LIMIT}", "");
            datatypeConversion_.put("{DEFAULT_INDEX_SIZE}", "");
            datatypeConversion_.put("{LIKE}", "LIKE");
            datatypeConversion_.put("{BINARY}", "");
            datatypeConversion_.put("{CASCADE}", "CASCADE");
            datatypeConversion_.put("{lgCharSet}", "");
            datatypeConversion_.put("{lgTableCharSet}", "");
            datatypeConversion_.put("{dateTime}", "DATETIME");
            datatypeConversion_.put("{AS}", "AS");
            datatypeConversion_.put("{tempTableAs}", "AS");
            datatypeConversion_.put("{tempTable}", "TEMPORARY");
        } else if (databaseType_.equals("HSQL Database Engine") || databaseType_.equals("HSQL")) {
            properQuote_ = "";
            datatypeConversion_.put("{boolean}", "boolean");
            datatypeConversion_.put("{IF NOT EXISTS}", "");
            datatypeConversion_.put("{limitedText}", "varchar");
            datatypeConversion_.put("{unlimitedText}", "varchar_ignorecase");
            datatypeConversion_.put("{bigInt}", "bigint");
            datatypeConversion_.put("{TYPE}", "");
            datatypeConversion_.put("{DROPFOREIGNKEY}", "DROP CONSTRAINT");
            datatypeConversion_.put("{true}", "'true'");
            datatypeConversion_.put("{false}", "'false'");
            datatypeConversion_.put("{LIMIT}", "");
            datatypeConversion_.put("{DEFAULT_INDEX_SIZE}", "");
            datatypeConversion_.put("{LIKE}", "LIKE");
            datatypeConversion_.put("{BINARY}", "");
            datatypeConversion_.put("{CASCADE}", "CASCADE");
            datatypeConversion_.put("{dateTime}", "TIMESTAMP");
            datatypeConversion_.put("{lgCharSet}", "");
            datatypeConversion_.put("{lgTableCharSet}", "");
            datatypeConversion_.put("{AS}", "AS");
            datatypeConversion_.put("{tempTableAs}", "");
            datatypeConversion_.put("{tempTable}", "TEMPORARY");
        } else if (databaseType_.startsWith("Oracle")) {
            properQuote_ = "";
            datatypeConversion_.put("{boolean}", "CHAR");
            datatypeConversion_.put("{IF NOT EXISTS}", "");
            datatypeConversion_.put("{limitedText}", "VARCHAR2");
            datatypeConversion_.put("{unlimitedText}", "CLOB");
            datatypeConversion_.put("{blob}", "BLOB");
            datatypeConversion_.put("{bigInt}", "NUMBER(37)");
            datatypeConversion_.put("{TYPE}", "");
            datatypeConversion_.put("{DROPFOREIGNKEY}", "DROP CONSTRAINT");
            datatypeConversion_.put("{true}", "1");
            datatypeConversion_.put("{false}", "0");
            datatypeConversion_.put("{LIMIT}", "");
            datatypeConversion_.put("{DEFAULT_INDEX_SIZE}", "");
            datatypeConversion_.put("{LIKE}", "LIKE");
            datatypeConversion_.put("{BINARY}", "");
            datatypeConversion_.put("{CASCADE}", "");
            datatypeConversion_.put("{lgCharSet}", "");
            datatypeConversion_.put("{lgTableCharSet}", "");
            datatypeConversion_.put("{dateTime}", "TIMESTAMP");
            datatypeConversion_.put("{AS}", "");
            datatypeConversion_.put("{tempTableAs}", "AS");
            datatypeConversion_.put("{tempTable}", "GLOBAL TEMPORARY");
            datatypeConversion_.put("[limitedTextLimit]", "4000");
        } else {
            throw new Exception("Unsupported database type '" + databaseType_ + "' in the GenericSQLModifier.");
        }
    }

    /**
     * 
     * @return what type of database it is.
     */
    public String getDatabaseType() {
        return databaseType_;
    }

    /**
     * 
     * @param sql
     * @param logResult
     *            log result if true
     * @return the modified sql statement
     */
    public String modifySQL(String sql, boolean logResult) {
        StringBuffer result = new StringBuffer(sql);

        // fix the quotes
        int pos = result.indexOf("^");
        while (pos != -1) {
            result.replace(pos, pos + 1, properQuote_);
            pos = result.indexOf("^");
        }

        // UCase DB2 requires UCASE (which requires a varchar cast).
        if (databaseType_.startsWith("DB2")) {
            // get rid of multiple spaces
            pos = result.indexOf("  ");
            while (pos != -1) {
                result.replace(pos, pos + 2, " ");
                pos = result.indexOf("  ");
            }

            // need to shove 'UCASE(varchar(' onto the front of the variable
            // name preceding the LIKE,
            // and '))' onto the back
            pos = 0;
            int pos2 = result.indexOf("{LIKE}", pos);
            while (pos2 != -1) {
                StringBuffer temp = new StringBuffer(result.substring(0, pos2));
                int lastSpace = temp.lastIndexOf(" ");
                temp.setLength(lastSpace);
                int secondLastSpace = temp.lastIndexOf(" ") + 1;

                // don't wrap open parens
                while (temp.charAt(secondLastSpace) == '(') {
                    secondLastSpace++;
                }

                result.insert(secondLastSpace, "UCASE(varchar(");
                lastSpace += "UCASE(varchar(".length();
                result.insert(lastSpace, "))");
                pos = pos2 + "UCASE(varchar())".length() + "{LIKE}".length();
                pos2 = result.indexOf("{LIKE}", pos);
            }
        }

        // Oracle requires UPPER.
        else if (databaseType_.startsWith("Oracle")) {
            // get rid of 'AS' table aliases
            pos = result.indexOf("{AS}");
            while (pos != -1) {
                result.replace(pos, pos + 4, "");
                pos = result.indexOf("{AS}");
            }
            
            // get rid of multiple spaces
            pos = result.indexOf("  ");
            while (pos != -1) {
                result.replace(pos, pos + 2, " ");
                pos = result.indexOf("  ");
            }

            // need to shove 'UPPER(' onto the front of the variable name
            // preceding the LIKE,
            // and ')' onto the back
            pos = 0;
            int pos2 = result.indexOf("{LIKE}", pos);
            while (pos2 != -1) {
                StringBuffer temp = new StringBuffer(result.substring(0, pos2));
                int lastSpace = temp.lastIndexOf(" ");
                temp.setLength(lastSpace);
                int secondLastSpace = temp.lastIndexOf(" ") + 1;

                // don't wrap open parens
                while (temp.charAt(secondLastSpace) == '(') {
                    secondLastSpace++;
                }

                result.insert(secondLastSpace, "UPPER(");
                lastSpace += "UPPER(".length();
                result.insert(lastSpace, ")");
                pos = pos2 + "UPPER()".length() + "{LIKE}".length();
                pos2 = result.indexOf("{LIKE}", pos);
            }
        }

        // modify the datatypes
        for (int i = 0; i < customDatatypes_.length; i++) {
            pos = result.indexOf(customDatatypes_[i]);
            while (pos != -1) {
                String dataType = (String) datatypeConversion_.get(customDatatypes_[i]);
                if (customDatatypes_[i].equals("{limitedText}")
                        && datatypeConversion_.get("[limitedTextLimit]") != null) {
                    try {
                        // automatically change limitedText into unlimited text
                        // if the size is greater
                        // than the limit.
                        int max = Integer.parseInt((String) datatypeConversion_.get("[limitedTextLimit]"));
                        // get the (optional) "(100)" size that follows the
                        // datatype specification.
                        int start = pos + customDatatypes_[i].length();
                        int end = result.indexOf(")", start);
                        String temp = result.substring(start, end).trim();
                        if (temp.startsWith("(")) {
                            temp = temp.substring(1);
                        }
                        int length = Integer.parseInt(temp);
                        if (length > max) {
                            dataType = (String) datatypeConversion_.get("{unlimitedText}");
                            // remove the size variable
                            result.replace(start, end + 1, "");
                        }
                    } catch (Exception e) {
                        // don't want to fail here. Just do what it used to do.
                        dataType = (String) datatypeConversion_.get(customDatatypes_[i]);
                    }

                }
                result.replace(pos, pos + customDatatypes_[i].length(), dataType);
                pos = result.indexOf(customDatatypes_[i]);
            }
        }

        // remove all the defaults...
        if (databaseType_.equals("ACCESS")) {
            pos = result.indexOf(" default ");
            while (pos != -1) {
                int pos2 = result.indexOf(",", pos);
                if (pos2 == -1) {
                    // not a comma, maybe a close paren?
                    pos2 = result.indexOf(")", pos);
                }
                result.replace(pos, pos2, "");
                pos = result.indexOf(" default ");
            }
            pos = result.indexOf("!=");
            while (pos != -1) {
                result.replace(pos, pos + 2, "<>");
                pos = result.indexOf("!=");
            }
        }

        if (logResult) {
            log.debug(result.toString());
        }
        return result.toString();
    }

    public String modifySQL(String sql) {
        return modifySQL(sql, true);
    }

    public boolean requiresLikeQueryTextToBeUpperCased() {
        if (databaseType_.startsWith("DB2") || databaseType_.startsWith("Oracle")) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) throws Exception {
        GenericSQLModifier foo = new GenericSQLModifier("DB2/NT", false);
        System.out.println(foo.modifySQL("Select  * from a where b.c {LIKE} ?"));
        System.out.println(foo.modifySQL("Select * from a where foobar.me {LIKE} ? AND barnone {LIKE} ?"));
        System.out.println(foo.modifySQL("Select * from a where a = ? AND (barnone {LIKE} OR foo {LIKE} ?)"));

    }

}