
package org.LexGrid.LexBIG.Impl.loaders;

import java.util.Date;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ProcessStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Load.OntologyFormat;
import org.LexGrid.LexBIG.Utility.logging.CachingMessageDirectorIF;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.lexevs.logging.messaging.impl.CachingMessageDirectorImpl;

public abstract class AbstractProcessRunner implements ProcessRunner {

    public StatusReportingCallback runProcess(
            AbsoluteCodingSchemeVersionReference codingSchemeVersion,
            OntologyFormat format)
        throws LBParameterException {

        if (codingSchemeVersion.getCodingSchemeURN() == null
                || codingSchemeVersion.getCodingSchemeURN().length() == 0
                || codingSchemeVersion.getCodingSchemeVersion() == null
                || codingSchemeVersion.getCodingSchemeVersion().length() == 0) {

            throw new LBParameterException(
            "If you supply a codingSchemeVersion, it needs to contain both the coding scheme and the version");
        } else {

            ProcessStatus status = new ProcessStatus();

            status.setStartTime(new Date(System.currentTimeMillis()));
            CachingMessageDirectorIF md = new CachingMessageDirectorImpl( new MessageDirector("", status));

            Thread runProcess = new Thread(new RunProcess(codingSchemeVersion, format, md, status));
            runProcess.start();
            
            return new StatusReportingCallback(md, status);
        }
    }

    private class RunProcess implements Runnable {
        private AbsoluteCodingSchemeVersionReference codingSchemeVersion;
        private ProcessStatus status;
        private LgMessageDirectorIF md;
        private OntologyFormat format;

        private RunProcess(
                AbsoluteCodingSchemeVersionReference codingSchemeVersion, 
                OntologyFormat format,
                LgMessageDirectorIF md, 
                ProcessStatus status) {
            this.codingSchemeVersion = codingSchemeVersion;
            this.status = status;
            this.md = md;
            this.format = format;
        }

        public void run() {
            status.setState(ProcessState.PROCESSING);

            try {

                md.info("beginning process");

                doRunProcess(codingSchemeVersion, format, md, status);

                md.info("Finished processes ");

            } catch (Exception e) {
                md.fatal("Failed while running the process", e);
                status.setState(ProcessState.FAILED);
                System.out.println(e.getCause());
            } 

            status.setState(ProcessState.COMPLETED);
            status.setEndTime(new Date(System.currentTimeMillis()));
        }
    }
    
    protected abstract void doRunProcess(
            AbsoluteCodingSchemeVersionReference codingSchemeVersion,
            OntologyFormat format,
            LgMessageDirectorIF md, 
            ProcessStatus status);
}