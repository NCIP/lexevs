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
 *      http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.LexGrid.LexBIG.gui;

import org.LexGrid.LexBIG.DataModel.Core.LogEntry;
import org.LexGrid.LexBIG.Utility.logging.StatusReporter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

/**
 * This shell allows for loading terminologies into LexBIG, and displays the
 * progress as they load.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public abstract class LoadExportBaseShell {
    public LB_GUI lb_gui_;
    public LB_VSD_GUI lb_vd_gui_;
    
    protected boolean loadingPL_ = false;
    protected boolean loadingVD_ = false;
    protected StyledText statusText_;
    protected Thread statusMonitorThread_;
    /*
     * monitorLoader needs to have synchronized access (or be volatile). The UI
     * thread was accessing it directly, as was the StatusMonitor thread. Even
     * though it's atomic, this can still fail.
     */
    private boolean monitorLoader_ = true;
    private boolean loading = false;

    protected DialogHandler dialog_;
    protected final ShellListener shellListener = new ShellAdapter() {
        public void shellClosed(ShellEvent e) {
            /*
             * To prevent undefined (or damaged) state created by the load
             * process not completing, prevent the window from closing before
             * the load process is complete. (If "doit" is false, the "close"
             * action is aborted.)
             */
            e.doit = !isLoading();
        }
    };

    protected LoadExportBaseShell(LB_GUI lb_gui) {
        lb_gui_ = lb_gui;
    }
    
    protected LoadExportBaseShell(LB_VSD_GUI lb_vd_gui) {
        lb_vd_gui_ = lb_vd_gui;
    }

    protected synchronized boolean getMonitorLoader() {
        return monitorLoader_;
    }

    protected synchronized void setMonitorLoader(boolean monitorLoader) {
        this.monitorLoader_ = monitorLoader;
    }

    protected Composite getStatusComposite(Composite parent) {
        return getStatusComposite(parent, null);
    }
    
    protected Composite getStatusCompositeForPickList(Composite parent, StatusReporter reporter) {
        this.loadingPL_ = true;
        this.loadingVD_ = false;
        return getStatusComposite(parent, reporter);
    }
    
    protected Composite getStatusCompositeForValueSets(Composite parent, StatusReporter reporter) {
        this.loadingVD_ = true;
        this.loadingPL_ = false;
        return getStatusComposite(parent, reporter);
    }

    protected StyledText getStatusText(Composite parent) {
        Group status = new Group(parent, SWT.NONE);
        status.setLayout(new GridLayout());
        status.setLayoutData(new GridData(GridData.FILL_BOTH));
        status.setText("Output");
        

        StyledText text = new StyledText(status, SWT.MULTI | SWT.READ_ONLY
                | SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
        GridData gd = new GridData(GridData.FILL_BOTH);
        text.setLayoutData(gd);

        return text;
    }
    
    protected void startLogging(StyledText text, StatusReporter reporter ) {
        StatusMonitor sm = new StatusMonitor(text, reporter);
        Thread t = new Thread(sm);
        t.setDaemon(true);
        t.start();
    }

    protected Composite getStatusComposite(Composite parent, StatusReporter reporter) {
        Group status = new Group(parent, SWT.NONE);
        status.setLayout(new GridLayout());
        status.setText("Output");

        statusText_ = new StyledText(status, SWT.MULTI | SWT.READ_ONLY
                | SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.heightHint = 200;
        statusText_.setLayoutData(gd);
        
        setStatusMonitor(statusText_, reporter);
        return status;
    }
    
    protected void setStatusMonitor(StyledText statusText, StatusReporter reporter) {
        if (statusMonitorThread_ != null){
            statusMonitorThread_.interrupt();
            statusMonitorThread_ = null;
        }
        setMonitorLoader(true);
        
        StatusMonitor sm = new StatusMonitor(statusText, reporter);
        statusMonitorThread_ = new Thread(sm);
        statusMonitorThread_.setDaemon(true);
        statusMonitorThread_.start();
    }

    protected class StatusMonitor implements Runnable {
        StyledText text_;
        StatusReporter statusReporter;

        public StatusMonitor(StyledText text, StatusReporter statusReporter) {
            text_ = text;
            this.statusReporter = statusReporter;
        }

        public void run() {
            try {
                int logMessageCount = 0;
                final StringBuffer stuffToAppend = new StringBuffer();
                while (getMonitorLoader()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // do nothing.
                    }
                    // see if the log messages need updating.

                    LogEntry[] le = statusReporter.getLog(null);
                    
                    if (le.length > logMessageCount) {
                        for (; logMessageCount < le.length; logMessageCount++) {
                            stuffToAppend.append(le[logMessageCount]
                                    .getMessageNumber()
                                    + " - "
                                    + le[logMessageCount].getEntryLevel()
                                    + " - "
                                    + le[logMessageCount].getEntryTime()
                                    + " - "
                                    + le[logMessageCount].getMessage()
                                    + "\n ");
                        }
                    }
                     
                    if (statusReporter != null && statusReporter.getStatus() != null
                            && statusReporter.getStatus().getEndTime() != null) {
                        stuffToAppend.append("Load process finished - "
                                + statusReporter.getStatus().getEndTime() + "\n");
                        stuffToAppend.append("Errors Logged: "
                                + statusReporter.getStatus().getErrorsLogged() + "\n");
                        stuffToAppend.append("Warnings Logged: "
                                + statusReporter.getStatus().getWarningsLogged()
                                + "\n");
                        stuffToAppend.append("End State: "
                                + statusReporter.getStatus().getState() + "\n");
                        setMonitorLoader(false);
                        if (LoadExportBaseShell.this.lb_gui_ != null)
                            LoadExportBaseShell.this.lb_gui_.refreshCodingSchemeList();
                        if (LoadExportBaseShell.this.lb_vd_gui_ != null)
                        {
                            if (loadingVD_)
                                LoadExportBaseShell.this.lb_vd_gui_.refreshValueSetDefList();
                            else
                                LoadExportBaseShell.this.lb_vd_gui_.refreshPickListList();
                        }
                    }

                    if (stuffToAppend.length() > 0) {
                        text_.getDisplay().syncExec(new Runnable() {
                            public void run() {
                                text_.append(stuffToAppend.toString());
                                stuffToAppend.setLength(0);
                                text_.setSelection(text_.getText().length());
                            }
                        });
                    }
                }
            } finally {
                setMonitorLoader(false);
                setLoading(false);
                
            }
        }
    }

    protected synchronized boolean isLoading() {
        return loading;
    }

    protected synchronized void setLoading(boolean loading) {
        this.loading = loading;
    }
}