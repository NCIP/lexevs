/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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
package edu.mayo.informatics.lexgrid.convert.swt;

import java.util.Enumeration;

import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.WriterAppender;
import org.apache.log4j.varia.LevelRangeFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

/**
 * A window to view the log4j log output.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 * @version subversion $Revision: 7198 $ checked in on $Date: 2008-02-15
 *          18:08:36 +0000 (Fri, 15 Feb 2008) $
 */
public class LogViewer {
    private static Logger log = Logger.getLogger("convert.gui");
    CustomStringWriter writer_;
    LogViewerOptions logViewerOptions_;
    protected Log[] logs_ = new Log[] { new Log("ROOT LOGGER", "Log everything", false, "DEBUG"),
            new Log("convert", "Converter messages.", true, "WARN"),
            new Log("convert.MessageUtility", "Messages sent to the screen", false, "DEBUG"),
            new Log("org.hl7.utility.sqlReconnect.WrappedPreparedStatement", "Executed SQL statements", false, "DEBUG") };

    private Shell shell_;

    public LogViewer(Shell parent) throws Exception {
        shell_ = new Shell(parent, SWT.DIALOG_TRIM | SWT.RESIZE);
        shell_.setText("Converter Log");
        shell_.setSize(600, 500);
        shell_.setImage(new Image(shell_.getDisplay(), this.getClass().getResourceAsStream("icons/log.gif")));

        shell_.addListener(SWT.Close, new Listener() {
            public void handleEvent(Event arg0) {
                arg0.doit = false;
                shell_.setVisible(false);
            }
        });

        buildComponents();
        writer_ = new CustomStringWriter(log_);
        LogManager.getRootLogger().setLevel(Level.DEBUG);
        configureLogs();
    }

    public void removeOpenLoggers() {
        Enumeration logEnum = LogManager.getCurrentLoggers();
        while (logEnum.hasMoreElements()) {
            ((Logger) logEnum.nextElement()).removeAllAppenders();
        }
        LogManager.getRootLogger().removeAllAppenders();
    }

    public void configureLogs() {
        removeOpenLoggers();

        for (int i = 0; i < logs_.length; i++) {
            if (logs_[i].enabled) {
                Logger tempLogger;
                if (logs_[i].log.equals("ROOT LOGGER")) {
                    tempLogger = LogManager.getRootLogger();
                } else {
                    tempLogger = LogManager.getLogger(logs_[i].log);
                }
                tempLogger.setAdditivity(true);
                Appender appender = new WriterAppender(new PatternLayout("%p %c - %m%n"), writer_);
                LevelRangeFilter tempFilter = new LevelRangeFilter();
                tempFilter.setLevelMin(Level.toLevel(logs_[i].level));
                appender.addFilter(tempFilter);
                tempLogger.addAppender(appender);
            }
        }
        log.info("Logger reconfigured");
    }

    StyledText log_;

    Button configure_;
    Button clear_;

    private void buildComponents() {
        GridLayout layout = new GridLayout(2, true);
        shell_.setLayout(layout);

        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = 2;

        log_ = new StyledText(shell_, SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
        log_.setEditable(false);
        log_.setLayoutData(gd);

        gd = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);

        configure_ = new Button(shell_, SWT.PUSH);
        configure_.setText("Configure");
        configure_.setLayoutData(gd);
        configure_.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent arg0) {
            }

            public void widgetSelected(SelectionEvent arg0) {
                if (logViewerOptions_ == null) {
                    try {
                        logViewerOptions_ = new LogViewerOptions(LogViewer.this, shell_);
                    } catch (Exception e1) {
                        log.error("Problem creating log configurator", e1);
                    }
                }
                logViewerOptions_.setVisible(true);
            }

        });

        clear_ = new Button(shell_, SWT.PUSH);
        clear_.setText("Clear");
        clear_.setLayoutData(gd);
        clear_.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent arg0) {

            }

            public void widgetSelected(SelectionEvent arg0) {
                writer_.getBuffer().setLength(0);
                log_.setText("");
            }

        });
    }

    public class Log {
        public String description;
        public String log;
        public boolean enabled;
        public String level;

        public Log(String log, String description) {
            this.log = log;
            this.description = description;
            this.enabled = false;
            this.level = "DEBUG";
        }

        public Log(String log, String description, boolean enabled) {
            this.log = log;
            this.description = description;
            this.enabled = enabled;
            this.level = "DEBUG";
        }

        public Log(String log, String description, boolean enabled, String level) {
            this.log = log;
            this.description = description;
            this.enabled = enabled;
            this.level = level;
        }
    }

    public void setVisible(boolean visible) {
        shell_.setVisible(visible);
    }
}