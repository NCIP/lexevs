
package org.LexGrid.LexBIG.gui.load;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Impl.loaders.IndexLoaderImpl;
import org.LexGrid.LexBIG.Impl.loaders.ProcessRunner;
import org.LexGrid.LexBIG.Utility.logging.StatusReporter;
import org.LexGrid.LexBIG.gui.DialogHandler;
import org.LexGrid.LexBIG.gui.LB_GUI;
import org.LexGrid.LexBIG.gui.LoadExportBaseShell;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public class ReIndexerLoader extends LoadExportBaseShell {
	public ReIndexerLoader(LB_GUI lb_gui,
			AbsoluteCodingSchemeVersionReference ref) {
		super(lb_gui);
		try {
			Shell shell = new Shell(lb_gui_.getShell().getDisplay());
			shell.setSize(500, 400);
			shell.setImage(new Image(shell.getDisplay(), this.getClass()
					.getResourceAsStream("/icons/load.gif")));

			dialog_ = new DialogHandler(shell);

			shell.setText("Reindexer");
			
			ProcessRunner processRunner = new IndexLoaderImpl();

			buildGUI(shell, processRunner.runProcess(ref, null));

			shell.open();

			shell.addShellListener(shellListener);

			
		} catch (Exception e) {
			dialog_.showError("Unexpected Error", e.toString());
		}

	}

	private void buildGUI(Shell shell, StatusReporter reporter) {
		shell.setLayout(new GridLayout());
		Composite status = getStatusComposite(shell, reporter);
		GridData gd = new GridData(GridData.FILL_BOTH);
		status.setLayoutData(gd);
	}
}