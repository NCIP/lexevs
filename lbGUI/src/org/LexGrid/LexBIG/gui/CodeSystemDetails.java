
package org.LexGrid.LexBIG.gui;

import org.LexGrid.LexBIG.Utility.ObjectToString;
import org.LexGrid.codingSchemes.CodingScheme;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

/**
 * Class for displaying code system details.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class CodeSystemDetails {
	private Shell shell_;
	private StyledText codeSystemResults_;
	private StyledText metaDataResults_;

	public CodeSystemDetails(Shell parent, LB_GUI lbGui, CodingScheme codeSystemDetails, String mdResults) {
		shell_ = new Shell(parent.getDisplay());
		shell_.setText("Code System Viewer");
		shell_.setSize(700, 550);
		shell_.setImage(new Image(shell_.getDisplay(), this.getClass()
				.getResourceAsStream("/icons/icon.gif")));
		
		buildComponents(codeSystemDetails, mdResults);

		shell_.setVisible(true);
		codeSystemResults_.setText(ObjectToString.toString(codeSystemDetails));
		metaDataResults_.setText(mdResults);
	}

	public void buildComponents(final CodingScheme codeSystemDetails, final String mdResults) {
	    shell_.setLayout(new FillLayout());
	    final TabFolder tabFolder = new TabFolder(shell_, SWT.NONE);

	    TabItem one = new TabItem(tabFolder, SWT.NONE);
	    one.setText("CODE SYSTEM METADATA");
	    one.setToolTipText("MetaData asserted by this code system");
	    one.setControl(getTabOneControl(tabFolder));

	    TabItem two = new TabItem(tabFolder, SWT.NONE);
	    two.setText("USER DEFINED METADATA");
	    two.setToolTipText("Metadata authored by the code system user");
	    two.setControl(getTabTwoControl(tabFolder));
	}
	
    private Control getTabOneControl(TabFolder tabFolder) {
        codeSystemResults_ = new StyledText(tabFolder,
                SWT.RESIZE | SWT.BORDER | SWT.MULTI | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL);
        return codeSystemResults_;
    }

    private Control getTabTwoControl(TabFolder tabFolder) {
        metaDataResults_ = new StyledText(tabFolder,
                SWT.RESIZE | SWT.BORDER | SWT.MULTI | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL);
        return metaDataResults_;
    }
}