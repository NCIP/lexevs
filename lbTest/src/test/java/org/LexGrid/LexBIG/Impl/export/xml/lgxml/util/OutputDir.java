package org.LexGrid.LexBIG.Impl.export.xml.lgxml.util;

import java.io.File;

public class OutputDir {
	
	private String OUTPUT_DIR = "lgxmlout";
	private File outDir;
	
	public OutputDir() {
		this.createOutputDir();
	}
	
	public String getOutputDirAsString() {
		return this.OUTPUT_DIR;
	}
	
	private boolean createOutputDir() {
		this.outDir = new File(this.OUTPUT_DIR);
		 boolean rv = this.outDir.mkdir();
		 if(rv == false) {
			 Logger.log("OutputDir: createOutputDir: WARNING:could not create directory: " + this.outDir.getAbsolutePath());
		 } else {
			 Logger.log("OutputDir: createOutputDir: directory: " + this.outDir.getAbsolutePath() + " created successfully");
		 }
		 return rv;
	}
	
	public boolean deleteOutputDir() {
		if(this.outDir == null) {
			Logger.log("OutputDir: deleteOutputDir: WARNING: outDir is null. could not delete");
			return false;
		}
		boolean rv = this.outDir.delete();
		if(rv == false) {
			Logger.log("OutputDir: deleteOutputDir: WARNING: failed to delete outputDir: " + this.outDir.getAbsolutePath());
			this.outDir.deleteOnExit();
		} else {
			Logger.log("OutputDir: deleteOutputDir: directory: " + this.outDir.getAbsolutePath() + " deleted successfully");
		}
		return rv;
	}
}
