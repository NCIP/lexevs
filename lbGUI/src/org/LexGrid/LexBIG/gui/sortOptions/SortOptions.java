
package org.LexGrid.LexBIG.gui.sortOptions;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.SortDescription;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.SortContext;
import org.LexGrid.LexBIG.gui.DialogHandler;
import org.LexGrid.LexBIG.gui.LB_GUI;
import org.LexGrid.LexBIG.gui.Utility;
import org.LexGrid.LexBIG.gui.codeSet.CodeSet;
import org.LexGrid.LexBIG.gui.codeSet.CodedNodeGraph;
import org.LexGrid.LexBIG.gui.codeSet.CodedNodeSet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

/**
 * A GUI Shell to let you choose what sort order you want to retrieve thing in.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class SortOptions {
	LB_GUI lb_gui_;

	SortDescription[] sd_;
	CodeSet cs_;
	DialogHandler dialog_;

	public SortOptions(LB_GUI lb_gui, CodedNodeGraph cng) {
		lb_gui_ = lb_gui;
		cs_ = cng;
		sd_ = lb_gui_.getLbs().getSortAlgorithms(SortContext.GRAPH)
				.getSortDescription();

		Shell shell = init();
		shell.open();
	}

	public SortOptions(LB_GUI lb_gui, CodedNodeSet cns) {
		lb_gui_ = lb_gui;
		cs_ = cns;
		sd_ = lb_gui_.getLbs().getSortAlgorithms(SortContext.SETITERATION)
				.getSortDescription();

		Shell shell = init();
		shell.open();
	}

	private Shell init() {
		final Shell shell = new Shell(lb_gui_.getShell(), SWT.APPLICATION_MODAL
				| SWT.DIALOG_TRIM | SWT.RESIZE);
		shell.setSize(400, 350);

		shell.setText("Set Sort Options");

		dialog_ = new DialogHandler(shell);

		shell.setLayout(new GridLayout(1, true));

		Group current = new Group(shell, SWT.None);
		current.setLayout(new GridLayout(2, false));
		current.setText("Current Sort Order");
		GridData gd = new GridData(GridData.FILL_BOTH);
		current.setLayoutData(gd);

		final List currentList = new List(current, SWT.V_SCROLL | SWT.H_SCROLL);
		gd = new GridData(GridData.FILL_BOTH);
		currentList.setLayoutData(gd);
		if (cs_.sortOptions != null) {
			currentList.setItems(cs_.sortOptions);
		}

		Composite buttons = new Composite(current, SWT.NONE);
		gd = new GridData(GridData.VERTICAL_ALIGN_CENTER);
		gd.horizontalIndent = 0;
		gd.verticalIndent = 0;
		buttons.setLayoutData(gd);
		buttons.setLayout(new GridLayout(1, true));

		Button up = Utility.makeButton("Move Up", buttons,
				GridData.FILL_HORIZONTAL);
		up.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				int i = currentList.getSelectionIndex();
				if (i > 0) {
					String a = currentList.getItem(i);
					currentList.remove(i);
					currentList.add(a, i - 1);
				}
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				//
			}

		});
		Button down = Utility.makeButton("Move Down", buttons,
				GridData.FILL_HORIZONTAL);
		down.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				int i = currentList.getSelectionIndex();
				if (i < currentList.getItemCount() - 1) {
					String a = currentList.getItem(i);
					currentList.remove(i);
					currentList.add(a, i + 1);
				}
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				//
			}

		});
		Button remove = Utility.makeButton("Remove", buttons,
				GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
		remove.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				int i = currentList.getSelectionIndex();
				if (i != -1) {
					currentList.remove(i);
				}
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				//
			}

		});

		Group available = new Group(shell, SWT.NONE);
		available.setText("Available Sort Options");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		available.setLayoutData(gd);
		available.setLayout(new GridLayout(3, false));

		final Combo availableList = new Combo(available, SWT.SINGLE
				| SWT.READ_ONLY);
		String[] temp = new String[sd_.length];
		for (int i = 0; i < temp.length; i++) {
			temp[i] = sd_[i].getName();
		}
		availableList.setItems(temp);
		availableList.setVisibleItemCount(10);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		availableList.setLayoutData(gd);

		Label d = Utility.makeLabel(available, "Algorithm Description: ");
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		d.setLayoutData(gd);

		final Label description = Utility.makeWrappingLabel(available, "", 2);
		availableList.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				int i = availableList.getSelectionIndex();
				description.setText(sd_[i].getDescription());
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				//
			}

		});
		availableList.select(0);
		description.setText(sd_[0].getDescription());

		final Button asc = new Button(available, SWT.RADIO);
		asc.setText("Ascending");
		asc.setSelection(true);

		Button desc = new Button(available, SWT.RADIO);
		desc.setText("Descending");

		Button add = Utility.makeButton("Add", available,
				GridData.HORIZONTAL_ALIGN_CENTER);

		add.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				String toAdd = availableList.getText();
				boolean alreadyAdded = false;
				for (int i = 0; i < currentList.getItemCount(); i++) {
					if (currentList.getItem(i).startsWith(toAdd)) {
						alreadyAdded = true;
						break;
					}
				}
				if (!alreadyAdded) {
					currentList.add(toAdd
							+ (asc.getSelection() ? " - Ascending"
									: " - Descending"));
				}
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				//            
			}

		});

		// Ok / cancel buttons

		Composite okCancel = new Composite(shell, SWT.NONE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		okCancel.setLayoutData(gd);
		okCancel.setLayout(new GridLayout(2, false));

		Button ok = new Button(okCancel, SWT.PUSH);
		ok.setText("Ok");
		gd = new GridData(GridData.GRAB_HORIZONTAL
				| GridData.HORIZONTAL_ALIGN_END);
		gd.widthHint = 70;
		ok.setLayoutData(gd);

		ok.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {

				cs_.sortOptions = currentList.getItems();
				shell.dispose();

			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				//
			}

		});

		Button cancel = new Button(okCancel, SWT.PUSH);
		cancel.setText("Cancel");
		gd = new GridData(GridData.HORIZONTAL_ALIGN_END);
		gd.widthHint = 70;
		cancel.setLayoutData(gd);

		cancel.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				shell.dispose();
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				//
			}

		});

		return shell;
	}
}