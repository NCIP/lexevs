
package org.LexGrid.LexBIG.gui.load;

import java.net.URI;
import java.net.URISyntaxException;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Load.UMLS_Loader;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.gui.DialogHandler;
import org.LexGrid.LexBIG.gui.LB_GUI;
import org.LexGrid.LexBIG.gui.LoadExportBaseShell;
import org.LexGrid.LexBIG.gui.Utility;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import edu.mayo.informatics.lexgrid.convert.utility.Constants;

/**
 * GUI that allows loading UMLS RRF files into LexBIG.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class UMLSSQLLoader extends LoadExportBaseShell {
	public UMLSSQLLoader(LB_GUI lb_gui) {
		super(lb_gui);
		try {
			Shell shell = new Shell(lb_gui_.getShell().getDisplay());
			shell.setSize(500, 400);
			shell.setImage(new Image(shell.getDisplay(), this.getClass()
					.getResourceAsStream("/icons/load.gif")));

			dialog_ = new DialogHandler(shell);

			UMLS_Loader loader = (UMLS_Loader) lb_gui_.getLbs()
					.getServiceManager(null).getLoader("UMLSLoader");

			shell.setText(loader.getName());

			buildGUI(shell, loader);

			shell.open();

			shell.addShellListener(shellListener);
		} catch (Exception e) {
			dialog_.showError("Unexpected Error", e.toString());
		}

	}

	private void buildGUI(Shell shell, final UMLS_Loader loader) {
		Group options = new Group(shell, SWT.NONE);
		options.setText("Load Options");
		shell.setLayout(new GridLayout());

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		options.setLayoutData(gd);

		GridLayout layout = new GridLayout(3, false);
		options.setLayout(layout);

		Utility.makeLabel(options, "UMLS database URL");
		final Combo server = new Combo(options, SWT.SINGLE);
		server.setItems(Constants.umlsServers);
		server.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
				| GridData.FILL_HORIZONTAL));
		server.setVisibleItemCount(10);
		Utility.makeLabel(options, "");

		Utility.makeLabel(options, "JDBC driver");
		final Combo driver = new Combo(options, SWT.SINGLE);
		driver.setItems(Constants.sqlDrivers);
		driver.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
				| GridData.FILL_HORIZONTAL));
		driver.setVisibleItemCount(10);
		Utility.makeLabel(options, "");

		Utility.makeLabel(options, "Database username");
		final Text username = new Text(options, SWT.BORDER);
		username.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
				| GridData.FILL_HORIZONTAL));
		Utility.makeLabel(options, "");

		Utility.makeLabel(options, "Database password");
		final Text password = new Text(options, SWT.BORDER);
		password.setEchoChar('*');
		password.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
				| GridData.FILL_HORIZONTAL));
		Utility.makeLabel(options, "");

		Label sabLabel = new Label(options, SWT.NONE);
		sabLabel.setText("Root source abbreviation (RSAB)");

		final Text sab = new Text(options, SWT.BORDER);
		gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
		sab.setLayoutData(gd);
		Utility.makeLabel(options, "");

		final Button load = new Button(options, SWT.PUSH);
		load.setText("Load");
		gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_END);
		gd.horizontalSpan = 3;
		gd.widthHint = 60;
		load.setLayoutData(gd);

		load.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				if (server.getText() == null || server.getText().length() == 0) {
					dialog_.showError("Parameter Error",
							"The UMLS server is required");
					server.setFocus();
					return;
				}

				if (driver.getText() == null || driver.getText().length() == 0) {
					dialog_.showError("Parameter Error",
							"The UMLS driver is required");
					driver.setFocus();
					return;
				}

				if (sab.getText() == null || sab.getText().length() == 0) {
					dialog_.showError("Parameter Error",
							"The UMLS RSAB value is required");
					sab.setFocus();
					return;
				}

				try {
					setLoading(true);
					loader.load(new URI(server.getText()), username.getText(),
							password.getText(), driver.getText(), Constructors
									.createLocalNameList(sab.getText()), 1,
							false, true);
					load.setEnabled(false);
				}

				catch (LBException e) {
					dialog_.showError("Loader Error", e.toString());
					load.setEnabled(true);
					setLoading(false);
					return;
				} catch (URISyntaxException e) {
					dialog_.showError("Loader Error", e.toString());
					load.setEnabled(true);
					setLoading(false);
					return;
				}
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				// 
			}

		});

		Composite status = getStatusComposite(shell, loader);
		gd = new GridData(GridData.FILL_BOTH);
		status.setLayoutData(gd);

	}

}