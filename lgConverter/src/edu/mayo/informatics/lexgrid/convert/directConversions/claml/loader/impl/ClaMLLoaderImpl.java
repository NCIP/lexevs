package edu.mayo.informatics.lexgrid.convert.directConversions.claml.loader.impl;

import java.net.URI;
import java.util.Date;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.LoadStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.dataAccess.CleanUpUtility;
import org.LexGrid.LexBIG.Impl.dataAccess.ResourceManager;
import org.LexGrid.LexBIG.Impl.dataAccess.SQLInterfaceBase;
import org.LexGrid.LexBIG.Impl.dataAccess.SystemVariables;
import org.LexGrid.LexBIG.Impl.helpers.SQLConnectionInfo;
import org.LexGrid.LexBIG.Impl.loaders.BaseLoader;
import org.LexGrid.LexBIG.Impl.loaders.MessageDirector;
import org.LexGrid.emf.codingSchemes.CodingScheme;

import edu.mayo.informatics.lexgrid.convert.emfConversions.SQLReadWrite;
import edu.mayo.informatics.lexgrid.convert.emfConversions.claml.ClaML2EMFMain;
import edu.mayo.informatics.lexgrid.convert.emfConversions.claml.format.ClaML;
import edu.mayo.informatics.lexgrid.convert.emfConversions.claml.loader.ClaMLLoader;
import edu.mayo.informatics.lexgrid.convert.exceptions.ConnectionFailure;
import edu.mayo.informatics.lexgrid.convert.formats.Option;
import edu.mayo.informatics.lexgrid.convert.formats.outputFormats.LexGridSQLOut;
import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;


public class ClaMLLoaderImpl extends BaseLoader implements ClaMLLoader {

	private static final long serialVersionUID = 5274007008171769984L;
	
	private SystemVariables sv = ResourceManager.instance().getSystemVariables();
	
	public void load(URI source) throws LBException {
		setInUse();
		setOptions();
		
		SQLConnectionInfo sci = ResourceManager.instance().getSQLConnectionInfoForLoad();
		out_ = new LexGridSQLOut(sci.username, sci.password, sci.server, sci.driver, sci.prefix);
		
		try {
			ClaML claml = new ClaML();
			claml.setFileLocation(source);

			claml.setCodingSchemeManifest(this.getCodingSchemeManifest());
			claml.setLoaderPreferences(this.getLoaderPreferences());
			in_ = claml;
			in_.testConnection();
		} catch (ConnectionFailure e) {
			inUse = false;
			throw new LBParameterException("The ClaML file path appears to be invalid - " + e);
		}

		try{
			status_ = new LoadStatus();
			status_.setLoadSource(getStringFromURI(source));
			
			md_ = new MessageDirector(this.getClass().getName(), status_);

			ClaML2LGMain loader = new ClaML2LGMain();
			CodingScheme codingScheme = loader.map(source, null, false, md_);
			URNVersionPair versionPair = new URNVersionPair(codingScheme.getCodingSchemeName(),
					codingScheme.getRepresentsVersion());

			boolean loaded = true;
			try {
				 ResourceManager.instance().getInternalCodingSchemeNameForUserCodingSchemeName(
						 codingScheme.getCodingSchemeURI(), codingScheme.getRepresentsVersion());
			} catch (Exception e) {
				loaded = false;
			}
			if(loaded){
				// delete the DB we just created - end in error.
				md_.fatal("Cannot load a terminology that is already loaded.");
				throw new Exception("Cannot load a terminology that is already loaded.");
			}
             
			SQLReadWrite emfOut = new SQLReadWrite(out_.getServer(), out_.getDriver(), out_.getUsername(), out_.getPassword(),
		    		   out_.getTablePrefix(), false, md_, new URNVersionPair[]{versionPair});

			emfOut.clearCodingScheme(versionPair.getUrn());
			emfOut.writeCodingScheme(codingScheme);

			if (status_.getErrorsLogged() != null && !status_.getErrorsLogged().booleanValue()) {
				md_.info("Finished loading the DB");

				doTransitiveTable(URNVersionPair.getCodingSchemeNames(new URNVersionPair[]{versionPair}), sci);

				doIndex(URNVersionPair.getCodingSchemeNames(new URNVersionPair[]{versionPair}), sci);

				if (sv.getAutoLoadSingleDBMode()) {
					// some databases (access in particular) won't see new
					// tables unless you open a
					// new connection to them.
					// if we are in single db mode, there may already be
					// open connections. close them.
					SQLInterfaceBase sib = ResourceManager.instance().getSQLInterfaceBase(sci.username,
							sci.password, sci.server, sci.driver);
					sib.closeUnusedConnections();
				}

				register(sci);

				status_.setState(ProcessState.COMPLETED);
				md_.info("Load process completed without error");

			}
		} catch (Exception e) {
			status_.setState(ProcessState.FAILED);
			e.printStackTrace();
			md_.fatal("Failed while running the conversion", e);
		} finally {
			try {
				if (status_.getState() == null || status_.getState().getType() != ProcessState.COMPLETED_TYPE) {
					status_.setState(ProcessState.FAILED);
					try {
						getLogger().warn("Load failed.  Removing temporary resources...");
						CleanUpUtility.removeUnusedDatabase(sv.getAutoLoadSingleDBMode() ? out_.getTablePrefix()
								: sci.dbName);
						CleanUpUtility.removeUnusedIndex(sv.getAutoLoadSingleDBMode() ? out_.getTablePrefix()
								: sci.dbName);
					} catch (LBParameterException e) {
						// do nothing - means that the requested delete item
						// didn't exist.
					} catch (Exception e) {
						getLogger().warn("Problem removing temporary resources", e);
					}
				}
			} finally {
				status_.setEndTime(new Date(System.currentTimeMillis()));
				inUse = false;
			}
		}
	}

	private void setOptions(){
		Option overrideOption = new Option(Option.DO_WITH_EMF, new Boolean(true));
		super.options_.add(overrideOption);
	}
}
