
package org.LexGrid.LexBIG.Impl.export.common.util;

import java.io.File;

public abstract class AbstractOutputDir {
	
	private String S_outDir;	
	private File F_outDir;
	
	public AbstractOutputDir(String outDir) {
		this.S_outDir = outDir;
		this.createOutputDir();
	}
	
	public String getOutputDirAsString() {
		return this.S_outDir;
	}
	
	private boolean createOutputDir() {
		this.F_outDir = new File(this.S_outDir);
		 boolean rv = this.F_outDir.mkdir();
		 if(rv == false) {
			 Logger.log("OutputDir: createOutputDir: WARNING:could not create directory: " + this.F_outDir.getAbsolutePath());
		 } else {
			 Logger.log("OutputDir: createOutputDir: directory: " + this.F_outDir.getAbsolutePath() + " created successfully");
		 }
		 return rv;
	}
	
	public boolean deleteOutputDir() {
		if(this.F_outDir == null) {
			Logger.log("OutputDir: deleteOutputDir: WARNING: outDir is null. could not delete");
			return false;
		}
		boolean rv = this.F_outDir.delete();
		if(rv == false) {
			Logger.log("OutputDir: deleteOutputDir: WARNING: failed to delete outputDir: " + this.F_outDir.getAbsolutePath());
			this.F_outDir.deleteOnExit();
		} else {
			Logger.log("OutputDir: deleteOutputDir: directory: " + this.F_outDir.getAbsolutePath() + " deleted successfully");
		}
		return rv;
	}
}