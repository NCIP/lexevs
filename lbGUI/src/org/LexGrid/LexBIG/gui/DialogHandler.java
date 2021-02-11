
package org.LexGrid.LexBIG.gui;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/**
 * Class to simplify putting error messages into dialogs.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 1052 $ checked in on $Date: 2006-01-30
 *          11:42:24 +0000 (Mon, 30 Jan 2006) $
 */
public class DialogHandler {
	Shell shell;
	private static Logger log = Logger.getLogger("LB_GUI");

	public DialogHandler(Shell shell) {
		this.shell = shell;
	}

	public void showError(String title, String errorMessage) {
		log.error(title + " - " + errorMessage);
		ErrorHelper temp = new ErrorHelper(title, errorMessage, SWT.ICON_ERROR);
		shell.getDisplay().syncExec(temp);
	}

	public void showWarning(String title, String errorMessage) {
		log.error(title + " - " + errorMessage);
		ErrorHelper temp = new ErrorHelper(title, errorMessage,
				SWT.ICON_WARNING);
		shell.getDisplay().syncExec(temp);
	}

	private class ErrorHelper implements Runnable {
		String title;
		String errorMessage;
		int iconType;

		public ErrorHelper(String title, String errorMessage, int iconType) {
			this.title = title;
			this.errorMessage = errorMessage;
			this.iconType = iconType;
		}

		public void run() {
			MessageBox messageBox = new MessageBox(shell, iconType | SWT.OK);
			messageBox.setMessage(errorMessage);
			messageBox.setText(title);
			messageBox.open();
		}

	}
	
	public void showInfo(String title, String errorMessage, boolean async) {
	    log.info(title + " - " + errorMessage);
        ErrorHelper temp = new ErrorHelper(title, errorMessage,
                SWT.ICON_INFORMATION);
        if(async) {
            shell.getDisplay().asyncExec(temp);
        } else {
            shell.getDisplay().syncExec(temp);
        }
	}

	public void showInfo(String title, String errorMessage) {
		this.showInfo(title, errorMessage, false);
	}
}