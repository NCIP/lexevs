
package org.LexGrid.LexBIG.gui.edit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.LexGrid.LexBIG.gui.DialogHandler;
import org.LexGrid.LexBIG.gui.LB_GUI;
import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
//import org.lexevs.cts2.core.update.RevisionInfo;

public abstract class AbstractEditDialog<T> implements ItemUpdateListener<T> {

    private Shell shell;
    private LB_GUI lbGui;
    
    private DialogHandler dialogHandler;
    private String defaultRevisionId;
    private boolean defaultEnableRevisionEditing;
    private Text revisionId;
    private Button enableRevisionEditing;
    
    private Map<String,Text> textBoxes = new HashMap<String,Text>();
    private Map<String,Button> checkBoxes = new HashMap<String,Button>();
    
    private boolean hasBeenEdited = false;
    
    public List<ItemUpdateListener<T>> listeners = new ArrayList<ItemUpdateListener<T>>();
    
//    protected RevisionInfo buildRevisionInfo(){
//        RevisionInfo revisionInfo = new RevisionInfo();
//        if(getRevisionEditing()) {
//            revisionInfo.setRevisionId(getRevisionId());
//        } else {
//            revisionInfo.setRevisionId(createRandomRevisionId());
//        }
//        return revisionInfo;
//    }

    private String createRandomRevisionId() {
        return UUID.randomUUID().toString();
    }

    public AbstractEditDialog(
            LB_GUI lbGui,
            String revisionId,
            boolean defaultEnableRevisionEditing,
            String title,
            Shell parent, 
            final DialogHandler dialogHandler) throws Exception {
        this.dialogHandler = dialogHandler;
        this.setLbGui(lbGui);
        
        shell = new Shell(parent);
        shell.setLayout(new GridLayout(1, true));
        shell.setSize(400,400);
        shell.setText(title); 
        defaultRevisionId = revisionId;
        this.defaultEnableRevisionEditing = defaultEnableRevisionEditing;
        
        shell.addShellListener(new ShellListener() {

            public void shellActivated(ShellEvent arg0) {
             //
            }

            public void shellClosed(ShellEvent e) {
                if(hasBeenEdited) {
                    MessageBox mb = new MessageBox(shell, SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
                    mb.setText("Confirm Close");
                    mb.setMessage("Are you sure you want to close? No updates will be made.");
                    int rc = mb.open();
                    e.doit = rc == SWT.OK;
                } else {
                    e.doit = true;
                }
            }

            public void shellDeactivated(ShellEvent arg0) {
              //
            }

            public void shellDeiconified(ShellEvent arg0) {
               //
            }

            public void shellIconified(ShellEvent arg0) {
              //
            }
            
        });
    }
    
//    protected abstract T updateItem();
    
    public void addItemUpdateListener(ItemUpdateListener<T> listener) {
        this.listeners.add(listener);
    }
    
    public void open() {    
        Composite editableComponentsComposite = new Composite(shell, SWT.BORDER);
        initComponents(editableComponentsComposite);
        
        Composite revisionInformationComposite = new Composite(shell, SWT.BORDER);
        revisionInformationComposite.setLayout(new GridLayout(1, true));
        revisionInformationComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        Composite nestedRevisionInformationButtonComposite = new Composite(revisionInformationComposite, SWT.BORDER);
        nestedRevisionInformationButtonComposite.setLayout(new GridLayout(1, false));
        nestedRevisionInformationButtonComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        enableRevisionEditing = new Button(nestedRevisionInformationButtonComposite, SWT.CHECK);
        enableRevisionEditing.setText("Enable Revision Editing");
        enableRevisionEditing.setSelection((defaultEnableRevisionEditing));
        enableRevisionEditing.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent arg0) {
              //
            }

            public void widgetSelected(SelectionEvent arg0) {
                revisionId.setEnabled(enableRevisionEditing.getSelection());
            }
            
        });
        
        Composite nestedRevisionInformationTextComposite = new Composite(revisionInformationComposite, SWT.BORDER);
        nestedRevisionInformationTextComposite.setLayout(new GridLayout(2, false));
        nestedRevisionInformationTextComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        Label revisionIdLabel = new Label(nestedRevisionInformationTextComposite, SWT.NONE);
        revisionIdLabel.setText("Revision Id: ");
        
        revisionId = new Text(nestedRevisionInformationTextComposite, SWT.BORDER | SWT.MULTI); 
        revisionId.setEnabled(enableRevisionEditing.getSelection());
        if(StringUtils.isNotBlank(defaultRevisionId)) {
            revisionId.setText(defaultRevisionId);
        }
        
        Composite buttonComposite = new Composite(shell, SWT.NULL);
        buttonComposite.setLayout(new GridLayout(2, false));
        
        Button cancelButton = new Button(buttonComposite, SWT.BUTTON1);
        cancelButton.setText("Cancel");
        cancelButton.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent arg0) {
               //
            }

            public void widgetSelected(SelectionEvent arg0) {
                if(hasBeenEdited) {
                    MessageBox mb = new MessageBox(shell, SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
                    mb.setText("Confirm Cancel");
                    mb.setMessage("Are you sure you want to cancel? No updates will be made.");
                    int rc = mb.open();
                    boolean confirmClose = rc == SWT.OK;

                    if(confirmClose) {
                        shell.dispose();
                    }
                } else {
                    shell.dispose();
                }
            } 
        });
        
//        Button saveButton = new Button(buttonComposite, SWT.BUTTON1);
//        saveButton.setText("Save");
//        saveButton.addSelectionListener(new SelectionListener() {
//
//            public void widgetDefaultSelected(SelectionEvent arg0) {
//               //
//            }
//
//            public void widgetSelected(SelectionEvent arg0) {
//                T updatedItem = updateItem();
//                
//                dialogHandler.showInfo("Update", "Item has been Updated sucessfully", true);
//                
//                for(ItemUpdateListener<T> listener : listeners) {
//                    listener.onItemUpdate(updatedItem);
//                }
//                
//                hasBeenEdited = false;
//            } 
//        });
        
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.HORIZONTAL_ALIGN_BEGINNING;
        gridData.verticalAlignment = GridData.VERTICAL_ALIGN_END;

        buttonComposite.setLayoutData(gridData);
        buttonComposite.setEnabled(true);
        buttonComposite.setVisible(true);
//        saveButton.setVisible(true);      
        
        this.hasBeenEdited = false;
        
        shell.setVisible(true);
        shell.setActive();
    }
    
    protected Text textBoxFactory(Composite composite) {
        Text text = new Text(composite, SWT.BORDER | SWT.MULTI);
        text.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent arg0) {
               hasBeenEdited = true;
            }  
        });
        return text;
    }
    
    protected Button checkBoxFactory(Composite composite) {
        Button checkBox = new Button(composite, SWT.CHECK);
        checkBox.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent arg0) {
              //
            }

            public void widgetSelected(SelectionEvent arg0) {
                hasBeenEdited = true;
            }
        });
        
        return checkBox;
    }
    
    protected Combo comboBoxFactory(Composite composite) {
        Combo combo = new Combo(composite, SWT.DROP_DOWN);
        combo.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent arg0) {
               hasBeenEdited = true;
            }  
        });
        return combo;
    }
    
    public void close() {
        shell.dispose();
    }

    public void onItemUpdate(T item) {
        for(ItemUpdateListener<T>  listener : listeners) {
            listener.onItemUpdate(item);
        }
    }

    protected Text addTextBox(String key, Composite composite, String labelName, String value) {
        Label label = new Label(composite, SWT.NONE);
        label.setText(labelName);
        Text text = textBoxFactory(composite);
        text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        if(StringUtils.isNotBlank(value)) {
            text.setText(value);
        }
        this.textBoxes.put(key, text);   
        
        return text;
    }
    
    protected Button addCheckBox(String key, Composite composite, String labelName, Boolean value) {
        Label label = new Label(composite, SWT.NONE);
        label.setText(labelName);
        Button button = checkBoxFactory(composite);
        
        if(value == null) {
            button.setSelection(false);
        } else {
            button.setSelection(value);
        }
        this.checkBoxes.put(key, button);   
        
        return button;
    }
    
    protected boolean getCheckBoxContent(String key) {
        return this.getCheckBox(key).getSelection();
     } 
    
    protected Button getCheckBox(String key) {
        return this.checkBoxes.get(key);
     } 
    
    protected String getTextBoxContent(String key) {
       return this.getTextBox(key).getText();
    }   
    
    protected Text getTextBox(String key) {
        return this.textBoxes.get(key);
     }  
    
    protected abstract void initComponents(Composite composite);
    
    public String getRevisionId() {
        return this.revisionId.getText();
    }
    
    public boolean getRevisionEditing() {
        return this.enableRevisionEditing.getSelection();
    }

    public Shell getShell() {
        return shell;
    }

    public void setShell(Shell shell) {
        this.shell = shell;
    }

    public DialogHandler getDialogHandler() {
        return dialogHandler;
    }

    public void setDialogHandler(DialogHandler dialogHandler) {
        this.dialogHandler = dialogHandler;
    }

    public void setLbGui(LB_GUI lbGui) {
        this.lbGui = lbGui;
    }

    public LB_GUI getLbGui() {
        return lbGui;
    }

    public boolean isHasBeenEdited() {
        return hasBeenEdited;
    } 
}