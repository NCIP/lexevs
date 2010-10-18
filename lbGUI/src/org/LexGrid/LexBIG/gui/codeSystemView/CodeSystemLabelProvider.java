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
package org.LexGrid.LexBIG.gui.codeSystemView;

import java.text.SimpleDateFormat;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.registry.service.Registry;
import org.lexevs.system.constants.SystemVariables;

/**
 * Label provider for the Code System SWT table view.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:scott.bauer@mayo.edu">Scott Bauer</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class CodeSystemLabelProvider implements ITableLabelProvider {
	private static SimpleDateFormat formatter = new SimpleDateFormat(
			"h:mm:ss a 'on' MM/dd/yyyy");

	public Image getColumnImage(Object arg0, int arg1) {
		return null;
	}

	public String getColumnText(Object arg0, int col) {
		if (arg0 instanceof CodingSchemeRendering) {
			CodingSchemeRendering csr = (CodingSchemeRendering) arg0;
			try {
				switch (col) {
				case 0:
					return csr.getCodingSchemeSummary().getFormalName();

				case 1:
					return csr.getCodingSchemeSummary().getRepresentsVersion();

				case 2:
					return csr.getCodingSchemeSummary().getCodingSchemeURI();

				case 3:
					return (csr.getRenderingDetail().getVersionTags()
							.getTagCount() > 0 ? csr.getRenderingDetail()
							.getVersionTags().getTag(0) : "");

				case 4:
					return csr.getRenderingDetail().getVersionStatus()
							.toString();

				case 5:
					return formatter.format(csr.getRenderingDetail()
							.getLastUpdateTime());

				case 6:
					return (csr.getRenderingDetail().getDeactivateDate() == null ? ""
							: formatter.format(csr.getRenderingDetail()
									.getDeactivateDate()));
				case 7: {
					// depends on database configuration. Gets the table prefix
					// for single mode
					// Gets the database name for multiple mode
				    SystemVariables sv = LexEvsServiceLocator.getInstance().getSystemResourceService().getSystemVariables();
				    Registry registry = LexEvsServiceLocator.getInstance().getRegistry();
					try {
						String globalPrefix = sv.getAutoLoadDBPrefix();
						
						String csPrefix = registry.getCodingSchemeEntry(
						        Constructors.createAbsoluteCodingSchemeVersionReference(csr.getCodingSchemeSummary())).getPrefix();
						
						return globalPrefix + csPrefix;
					} catch (Exception e) {
						return "name not available";
					}
				}
				case 8:
					return csr.getCodingSchemeSummary()
							.getCodingSchemeDescription().getContent();

				default:
					return "";
				}
			} catch (RuntimeException e) {
				return "";
			}
		}
		return "";
	}

	public void addListener(ILabelProviderListener arg0) {
		// do nothing
	}

	public void dispose() {
		// do nothing
	}

	public boolean isLabelProperty(Object arg0, String arg1) {
		return false;
	}

	public void removeListener(ILabelProviderListener arg0) {
		// do nothing
	}

	public void setupColumns(Table table) {
		TableColumn tc = new TableColumn(table, SWT.LEFT, 0);
		tc.setText("Code System Name");
		tc.setWidth(140);

		tc = new TableColumn(table, SWT.LEFT, 1);
		tc.setText("Code System Version");
		tc.setWidth(115);

		tc = new TableColumn(table, SWT.LEFT, 2);
		tc.setText("URI");
		tc.setWidth(190);

		tc = new TableColumn(table, SWT.LEFT);
		tc.setText("Tag");
		tc.setWidth(80);

		tc = new TableColumn(table, SWT.LEFT);
		tc.setText("Status");
		tc.setWidth(100);

		tc = new TableColumn(table, SWT.LEFT);
		tc.setText("Last Update Time");
		tc.setWidth(150);

		tc = new TableColumn(table, SWT.LEFT);
		tc.setText("Deactivate Time");
		tc.setWidth(100);

		tc = new TableColumn(table, SWT.LEFT);
		tc.setText("prefix");
		tc.setWidth(150);

		tc = new TableColumn(table, SWT.LEFT);
		tc.setText("Description");
		tc.setWidth(300);
	}

}