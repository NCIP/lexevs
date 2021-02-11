
package org.lexgrid.loader.umls.launch;

import java.net.URI;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Extensions.Load.UmlsBatchLoader;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

/**
 * The Class UmlsBatchLoaderLauncher.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class UmlsBatchLoaderLauncher {
	
	 /** The rrf dir. */
 	@Option(name="-in")   
	 private String rrfDir;
	 
	 /** The sab. */
 	@Option(name="-s")   
	 private String sab;
	 
	 /** The uri. */
 	@Option(name="-uri")   
	 private String uri;
	 
	 /** The version. */
 	@Option(name="-version")   
	 private String version;

 	private AbsoluteCodingSchemeVersionReference[] codingSchemeRefs;
 	
	 /**
 	 * The main method.
 	 * 
 	 * @param args the arguments
 	 * 
 	 * @throws Exception the exception
 	 */
 	public static void main(String[] args) throws Exception{
		 UmlsBatchLoaderLauncher launcher = new UmlsBatchLoaderLauncher();
		 CmdLineParser parser = new CmdLineParser(launcher);
		 parser.parseArgument(args);	
		 
		 launcher.load();
 	}
	 
	/**
	 * Load.
	 * 
	 * @throws Exception the exception
	 */
	public void load() throws Exception {
		UmlsBatchLoader loader = (UmlsBatchLoader)LexBIGServiceImpl.defaultInstance().getServiceManager(null).getLoader("UmlsBatchLoader");
			
		if(uri == null && version == null){
			loader.loadUmls(new URI(rrfDir), sab);
		} else {
			loader.resumeUmls(new URI(rrfDir), sab,  uri, version);
		}	
		setCodingSchemeRefs(loader.getCodingSchemeReferences());
	}


	/**
	 * Gets the rrf dir.
	 * 
	 * @return the rrf dir
	 */
	public String getRrfDir() {
		return rrfDir;
	}


	/**
	 * Sets the rrf dir.
	 * 
	 * @param rrfDir the new rrf dir
	 */
	public void setRrfDir(String rrfDir) {
		this.rrfDir = rrfDir;
	}


	/**
	 * Gets the sab.
	 * 
	 * @return the sab
	 */
	public String getSab() {
		return sab;
	}


	/**
	 * Sets the sab.
	 * 
	 * @param sab the new sab
	 */
	public void setSab(String sab) {
		this.sab = sab;
	}
		
	/**
	 * Sets the AbsoluteCodingSchemeVersionReference.
	 * 
	 * @param refs the new AbsoluteCodingSchemeVersionReference
	 */
	private void setCodingSchemeRefs(AbsoluteCodingSchemeVersionReference[] refs ) {
		this.codingSchemeRefs = refs;
	}
	
	/**
	 * Gets the AbsoluteCodingSchemeVersionReference.
	 * 
	 * @return the AbsoluteCodingSchemeVersionReference
	 */
	public AbsoluteCodingSchemeVersionReference[] getCodingSchemeRefs() {
		return codingSchemeRefs;
	}

}