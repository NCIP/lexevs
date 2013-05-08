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
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
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