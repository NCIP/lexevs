
package org.LexGrid.LexBIG.gui.load;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Extensions.ExtensionRegistry;
import org.LexGrid.LexBIG.Extensions.Load.postprocessor.LoaderPostProcessor;
import org.LexGrid.LexBIG.Impl.loaders.LoaderPostProcessRunner;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.gui.DialogHandler;
import org.LexGrid.LexBIG.gui.LB_GUI;
import org.LexGrid.LexBIG.gui.LoadExportBaseShell;
import org.apache.commons.lang.ClassUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.lexevs.system.utility.MyClassLoader;

public class PostProcessorLauncher extends LoadExportBaseShell {
    private AbsoluteCodingSchemeVersionReference ref;
    
	public PostProcessorLauncher(LB_GUI lb_gui,
			AbsoluteCodingSchemeVersionReference ref) {
		super(lb_gui);
		this.ref = ref;
		try {
			Shell shell = new Shell(lb_gui_.getShell().getDisplay());
			shell.setSize(500, 400);
			shell.setImage(new Image(shell.getDisplay(), this.getClass()
					.getResourceAsStream("/icons/load.gif")));

			dialog_ = new DialogHandler(shell);

			shell.setText("Post Processor");
			
			buildGUI(shell);
	
			shell.open();

			shell.addShellListener(shellListener);

			
		} catch (Exception e) {
			dialog_.showError("Unexpected Error", e.toString());
		}

	}

	private void buildGUI(final Shell shell) {
		shell.setLayout(new GridLayout());
		
		Composite getExtensionComposite = new Composite(shell, SWT.BORDER);
		getExtensionComposite.setLayout(new GridLayout(2, false));
		getExtensionComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		final Combo propertyCombo = new Combo(getExtensionComposite, SWT.DROP_DOWN);
        propertyCombo.setTextLimit(100);
        propertyCombo.setText("Select a Post Processor...");
        propertyCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        try {
            ExtensionRegistry extensionRegistry = 
                lb_gui_.getLbs().getServiceManager(null).getExtensionRegistry();
       
            for(ExtensionDescription ed : extensionRegistry.getGenericExtensions().getExtensionDescription()) {
                Class<?> clazz = (Class<?>) Class.forName(ed.getExtensionClass(), true, MyClassLoader.instance());
            
                if(ClassUtils.isAssignable(clazz, LoaderPostProcessor.class)){
                    propertyCombo.add(ed.getName());
                }
            }   
        
        } catch (Exception e1) {
            dialog_.showError("Error", "Error Loading Post Processor Extension");
            return;
        }
        
        final StyledText text = getStatusText(shell);
        
        
        Button runPostProcessor = new Button(getExtensionComposite, SWT.BUTTON1);
        runPostProcessor.setText("Run");
        runPostProcessor.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent arg0) {
               //
            }

            public void widgetSelected(SelectionEvent arg0) {
                if(propertyCombo.getSelectionIndex() < 0) {
                    dialog_.showError("Error", "Please select an Extension Name");
                    return;
                }
                
                String extensionName = propertyCombo.getText();
                
                LexBIGService lbs = lb_gui_.getLbs();
                
                try {
                    LoaderPostProcessor postProcessor = 
                        lbs.getServiceManager(null).getExtensionRegistry().
                        getGenericExtension(extensionName, LoaderPostProcessor.class);
                    
                    LoaderPostProcessRunner loaderPostProcessRunner = new LoaderPostProcessRunner(postProcessor);
                        startLogging(text, loaderPostProcessRunner.runProcess(ref, null));
                   
                    
                } catch (Exception e) {
                    dialog_.showError("Error", "Error Loading Post Processor Extension: " + extensionName);
                    return;
                } 
            }
            
        });
	}
}