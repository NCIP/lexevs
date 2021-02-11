
package org.LexGrid.LexBIG.gui;

import java.io.File;
import java.net.URI;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Load.Loader;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import edu.mayo.informatics.lexgrid.convert.exceptions.LgConvertException;
import edu.mayo.informatics.lexgrid.convert.utility.loaderPreferences.PreferenceLoaderFactory;
import edu.mayo.informatics.lexgrid.convert.utility.loaderPreferences.interfaces.PreferenceLoader;

/**
 * SWT Utility methods to simplify building SWT GUIs - methods to create labels,
 * buttons, etc.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class Utility {
	public static Button getFileChooseButton(Composite parent,
			final Text whereToPutFileName, final String[] filterExtensions,
			final String[] filterNames) {
	    return getFileButton(parent, whereToPutFileName, filterExtensions, filterNames, SWT.OPEN);
	}
	
	public static Button getFileSaveButton(Composite parent,
	        final Text whereToPutFileName, final String[] filterExtensions,
	        final String[] filterNames) {
	   return getFileButton(parent, whereToPutFileName, filterExtensions, filterNames, SWT.SAVE);
	}
	
	private static Button getFileButton(Composite parent,
            final Text whereToPutFileName, final String[] filterExtensions,
            final String[] filterNames, final int swtType) {
        final Button fileBrowse = new Button(parent, SWT.PUSH);
        fileBrowse.setText("Browse");

        fileBrowse.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
                FileDialog fileChooser = new FileDialog(fileBrowse.getShell(),
                        swtType);
                fileChooser.setText("Choose File");
                fileChooser.setFilterExtensions(filterExtensions);
                fileChooser.setFilterNames(filterNames);
                String filename = fileChooser.open();
                if (filename != null) {
                    whereToPutFileName.setText(filename);
                }
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // 
            }

        });

        return fileBrowse;
    }

	public static Button getFolderChooseButton(Composite parent,
			final Text whereToPutFileName) {
		final Button fileBrowse = new Button(parent, SWT.PUSH);
		fileBrowse.setText("Browse");

		fileBrowse.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				DirectoryDialog fileChooser = new DirectoryDialog(fileBrowse
						.getShell(), SWT.OPEN);
				fileChooser.setText("Choose Folder");
				String filename = fileChooser.open();
				if (filename != null) {
					whereToPutFileName.setText(filename);
				}
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				// 
			}

		});

		return fileBrowse;

	}

	public static Label makeLabel(Composite parent, String text) {
		Label temp = new Label(parent, SWT.None);
		temp.setText(text);
		return temp;
	}

	public static Label makeLabel(Composite parent, String text, int hSpan) {
		Label temp = new Label(parent, SWT.None);
		temp.setText(text);
		GridData gd = new GridData(GridData.BEGINNING);
		gd.horizontalSpan = hSpan;
		temp.setLayoutData(gd);
		return temp;
	}

	public static Label makeWrappingLabel(Composite parent, String text,
			int hSpan) {
		Label temp = new Label(parent, SWT.WRAP);
		temp.setText(text);
		temp.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, hSpan,
				1));
		return temp;
	}

	public static Text makeText(Composite parent) {
		return makeText(parent, "", 1);
	}

	public static Text makeText(Composite parent, int hSpan) {
		return makeText(parent, "", hSpan);
	}

	public static Text makeText(Composite parent, String toolTipText, int hSpan) {
		Text temp = new Text(parent, SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = hSpan;
		temp.setLayoutData(gd);
		temp.setToolTipText(toolTipText);
		return temp;
	}

	public static Label makeSeperator(Composite parent, int hSpan) {
		Label temp = new Label(parent, SWT.HORIZONTAL | SWT.SEPARATOR);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = hSpan;
		temp.setLayoutData(gd);
		return temp;
	}

	public static Button makeButton(String text, Composite composite,
			int gridDataParams, int widthHint) {
		GridData gd = new GridData(gridDataParams);
		gd.widthHint = widthHint;
		Button button = new Button(composite, SWT.PUSH);
		button.setText(text);
		button.setLayoutData(gd);
		return button;
	}

	public static Button makeButton(String text, Composite composite,
			int gridDataParams) {
		GridData gd = new GridData(gridDataParams);
		Button button = new Button(composite, SWT.PUSH);
		button.setText(text);
		button.setLayoutData(gd);
		return button;
	}

	public static Button makeButton(String text, Composite composite,
			GridData gd) {
		Button button = new Button(composite, SWT.PUSH);
		button.setText(text);
		button.setLayoutData(gd);
		return button;
	}

	public static Label makeBoldLabel(Composite parent, int hSpan,
			int gridStyle, String text) {
		Label label = new Label(parent, SWT.None);
		label.setText(text);

		FontData data[] = label.getFont().getFontData();
		for (int i = 0; i < data.length; i++) {
			data[i].setStyle(data[i].getStyle() | SWT.BOLD);
		}
		Font newFont = new Font(parent.getShell().getDisplay(), data);
		label.setFont(newFont);
		label.addDisposeListener(new DisposeListener() {
			// must dispose of Fonts after creating them
			public void widgetDisposed(DisposeEvent arg0) {
				((Label) arg0.widget).getFont().dispose();
			}

		});
		GridData gd = new GridData(gridStyle);
		gd.horizontalSpan = hSpan;
		label.setLayoutData(gd);
		return label;
	}

	public static URI getAndVerifyURIFromTextField(Text file) throws Exception {
		// check if there is any input in the Text Area -- if it is an optional
		// element it may be null;
		if (file.getText() == null || file.getText().equals("")) {
			return null;
		}

		URI uri = null;
		// is this a local file?
		File theFile = new File(file.getText());

		if (theFile.exists()) {
			uri = theFile.toURI();
		} else {
			// is it a valid URI (like http://something)
			uri = new URI(file.getText());
			uri.toURL().openConnection();
		}
		return uri;
	}

	public static Text createChooseFileDialog(Group options, String labelText,
			String fileType) {
		Label fileLabel = new Label(options, SWT.NONE);
		fileLabel.setText(labelText);

		final Text file = new Text(options, SWT.BORDER);
		GridData gd = new GridData(GridData.GRAB_HORIZONTAL
				| GridData.FILL_HORIZONTAL);
		file.setLayoutData(gd);

		Button fileChooseButton = null;
		if (fileType == null) {
			fileChooseButton = Utility.getFolderChooseButton(options, file);
		} else {
			fileChooseButton = Utility.getFileChooseButton(options, file,
					new String[] { fileType }, new String[] { labelText });
		}

		gd = new GridData(SWT.CENTER);
		gd.widthHint = 60;
		fileChooseButton.setLayoutData(gd);

		return file;
	}
}