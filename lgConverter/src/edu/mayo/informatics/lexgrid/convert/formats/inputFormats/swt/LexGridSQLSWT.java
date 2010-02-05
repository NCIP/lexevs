/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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
package edu.mayo.informatics.lexgrid.convert.formats.inputFormats.swt;

import java.sql.Connection;
import java.sql.SQLException;

import org.LexGrid.util.sql.DBUtility;
import org.LexGrid.util.sql.lgTables.SQLTableUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import edu.mayo.informatics.lexgrid.convert.exceptions.ConnectionFailure;
import edu.mayo.informatics.lexgrid.convert.formats.InputFormatSWTInterface;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.LexGridSQL;
import edu.mayo.informatics.lexgrid.convert.swt.DialogHandler;
import edu.mayo.informatics.lexgrid.convert.swt.formatPanels.SQLEntryComposite;
import edu.mayo.informatics.lexgrid.convert.utility.Constants;

/**
 * Details for connecting to SQL.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 7198 $ checked in on $Date: 2008-02-15
 *          18:08:36 +0000 (Fri, 15 Feb 2008) $
 */
public class LexGridSQLSWT extends LexGridSQL implements InputFormatSWTInterface {
    protected SQLEntryComposite sec;

    public String testConnection() throws ConnectionFailure {
        getCompositeValues();
        return super.testConnection();
    }

    public Menu createToolsMenu(final Shell shell, final DialogHandler errorHandler) {
        Menu toolsMenu = new Menu(shell, SWT.DROP_DOWN);

        MenuItem addConstraintsItem = new MenuItem(toolsMenu, SWT.CASCADE);
        addConstraintsItem.setText("&Add Table Constraints");
        addConstraintsItem.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event arg0) {
                MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                messageBox.setText("Add Constraints?");
                messageBox.setMessage("Add table constraints to the database?");
                boolean success;
                if (messageBox.open() == SWT.YES) {
                    success = addConstraints(errorHandler);
                    if (success) {
                        messageBox = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
                        messageBox.setText("Added Constraints");
                        messageBox.setMessage("Table constraints were added to the database");
                        messageBox.open();
                    }
                }
            }
        });

        MenuItem removeConstraintsItem = new MenuItem(toolsMenu, SWT.CASCADE);
        removeConstraintsItem.setText("&Remove Table Constraints");
        removeConstraintsItem.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event arg0) {
                MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                messageBox.setText("Remove Constraints?");
                messageBox.setMessage("Remove table constraints from the database?");
                boolean success;
                if (messageBox.open() == SWT.YES) {
                    success = removeTableConstraints(errorHandler);
                    if (success) {
                        messageBox = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
                        messageBox.setText("Removed Constraints");
                        messageBox.setMessage("Table constraints were removed from the database");
                        messageBox.open();
                    }
                }
            }
        });

        MenuItem createTablesItem = new MenuItem(toolsMenu, SWT.CASCADE);
        createTablesItem.setText("&Create Empty Tables");
        createTablesItem.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event arg0) {
                MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                messageBox.setText("Create Tables?");
                messageBox.setMessage("Create LexGrid tables in the database?");
                boolean success;
                if (messageBox.open() == SWT.YES) {
                    success = createTables(errorHandler);
                    if (success) {
                        messageBox = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
                        messageBox.setText("Created Empty Tables");
                        messageBox.setMessage("Empty LexGrid tables were created in the database");
                        messageBox.open();
                    }
                }
            }
        });

        MenuItem dropTablesItem = new MenuItem(toolsMenu, SWT.CASCADE);
        dropTablesItem.setText("&Drop LexGrid Tables");
        dropTablesItem.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event arg0) {
                MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                messageBox.setText("Drop Tables?");
                messageBox.setMessage("Drop all LexGrid tables from the database?");
                boolean success;
                if (messageBox.open() == SWT.YES) {
                    success = dropTables(errorHandler);
                    if (success) {
                        messageBox = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
                        messageBox.setText("Dropped Tables");
                        messageBox.setMessage("All LexGrid tabled have been dropped from the database");
                        messageBox.open();
                    }
                }
            }
        });

        return toolsMenu;
    }

    private boolean addConstraints(DialogHandler errorHandler) {
        try {
            getCompositeValues();
            String warning = super.testConnection();
            if (warning != null && warning.length() > 0) {
                errorHandler.showWarning("Connection Warning", warning);
            }
        } catch (ConnectionFailure e) {
            errorHandler.showError("Invalid Parameters", e.toString());
            return false;
        }
        Connection c = null;
        try {
            c = DBUtility.connectToDatabase(server, driver, username, password);

            SQLTableUtilities utility = new SQLTableUtilities(c, tablePrefix);
            utility.createDefaultTableConstraints();
            return true;
        } catch (Exception e) {
            log.error("problem creating constraints", e);
            errorHandler.showError("Unexpected Error",
                    "There was a problem creating the table constraints.\nSee the log for details.");
        } finally {
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException e) {
                    // do nothing
                }
            }
        }
        return false;
    }

    private boolean createTables(DialogHandler errorHandler) {
        try {
            getCompositeValues();
            String warning = super.testConnection();
            if (warning != null && warning.length() > 0) {
                errorHandler.showWarning("Connection Warning", warning);
            }
        } catch (ConnectionFailure e) {
            errorHandler.showError("Invalid Parameters", e.toString());
            return false;
        }

        Connection c = null;
        try {
            c = DBUtility.connectToDatabase(server, driver, username, password);

            SQLTableUtilities utility = new SQLTableUtilities(c, tablePrefix);
            utility.createDefaultTables();
            return true;
        } catch (Exception e) {
            log.error("problem creating tables", e);
            errorHandler.showError("Unexpected Error",
                    "There was a problem creating the tables.\nSee the log for details.");
        } finally {
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException e) {
                    // do nothing
                }
            }
        }
        return false;
    }

    private boolean dropTables(DialogHandler errorHandler) {
        try {
            getCompositeValues();
            String warning = super.testConnection();
            if (warning != null && warning.length() > 0) {
                errorHandler.showWarning("Connection Warning", warning);
            }
        } catch (ConnectionFailure e) {
            errorHandler.showError("Invalid Parameters", e.toString());
            return false;
        }

        Connection c = null;
        try {
            c = DBUtility.connectToDatabase(server, driver, username, password);

            SQLTableUtilities utility = new SQLTableUtilities(c, tablePrefix);
            utility.dropTables();
            return true;
        } catch (Exception e) {
            log.error("problem dropping tables", e);
            errorHandler.showError("Unexpected Error",
                    "There was a problem dropping the tables.\nSee the log for details.");
        } finally {
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException e) {
                    // do nothing
                }
            }
        }
        return false;
    }

    private boolean removeTableConstraints(DialogHandler errorHandler) {
        try {
            getCompositeValues();
            String warning = super.testConnection();
            if (warning != null && warning.length() > 0) {
                errorHandler.showWarning("Connection Warning", warning);
            }
        } catch (ConnectionFailure e) {
            errorHandler.showError("Invalid Parameters", e.toString());
            return false;
        }

        Connection c = null;
        try {
            c = DBUtility.connectToDatabase(server, driver, username, password);

            SQLTableUtilities utility = new SQLTableUtilities(c, tablePrefix);
            utility.dropDefaultTableConstraints();
            return true;
        } catch (Exception e) {
            log.error("problem removing constraints", e);
            errorHandler.showError("Unexpected Error",
                    "There was a problem removing the table constraints.\nSee the log for details.");
        } finally {
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException e) {
                    // do nothing
                }
            }
        }
        return false;
    }

    public Composite createComposite(Composite parent, int style, DialogHandler errorHandler) {
        sec = new SQLEntryComposite(parent, style, description, errorHandler);

        setCompositeVariables();
        return sec;
    }

    private void setCompositeVariables() {
        if (sec == null) {
            return;
        }
        sec.setUserNameSuggestions(new String[] {});
        sec.setDriverSuggestions(Constants.sqlDrivers);
        sec.setConnectionStringSuggestions(Constants.sqlServers);
    }

    private void getCompositeValues() {
        if (sec == null) {
            return;
        }
        driver = sec.getDriver();
        password = sec.getPassword();
        server = sec.getConnectionString();
        username = sec.getUsername();
        tablePrefix = sec.getTablePrefix();
    }

}