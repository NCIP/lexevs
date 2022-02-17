
package org.LexGrid.LexBIG.Extensions.Load;

import java.net.URI;

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.Exceptions.LBException;

/**
 * Load one or more coding schemes from UMLS RRF format stored in a SQL
 * database.  This requires that RRF source generation tool be set up to 
 * "output versionless source abbreviations" for any given subset of the UMLS.
 */
public interface UMLS_Loader extends Loader {
	/**
	 * Load content from a database repository. This will also result in
	 * implicit generation of standard indices required by the LexBIG runtime.
	 * <p>
	 * An exception is raised if resources cannot be accessed or another load
	 * operation is already in progress.
	 * 
	 * @param source
	 *            Location of the source database. Typically this is specified
	 *            in the form of a URL that indicates the database server, port,
	 *            name, and optional properties. An example (items in square
	 *            brackets are optional) is as follows: <tt>
	 * jdbc:mysql://[host][,failoverhost...][:port]/[database]
	 *   [?propertyName1][=propertyValue1][&propertyName2][=propertyValue2]...
	 * </tt>
	 * @param uid
	 *            User ID for authenticated access, if required and not
	 *            specified as part of the database URL parameters.
	 * @param pwd
	 *            Password for authenticated access, if required and not
	 *            specified as part of the database URL parameters.
	 * @param driver
	 *            Name of the JDBC driver to use when accessing the database.
	 * @param targetTerminologies
	 *            The list of UMLS terminologies to load. If null, all available
	 *            terminologies will be loaded. The terminology name should be
	 *            the SAB name of the terminology - or values from the RSAB
	 *            column of the MRSAB file.
	 * @param hierarchyOpt
	 *            Process hierarchical relationships stored by the MRHIER file.
	 *            Supported values are: 0 = Do not process information from
	 *            MRHIER; basic associations are calculated from MRREL without
	 *            adding atom-specific contexts. 1 = Process atom chains that
	 *            have been tagged with context identifiers (HCD). The HCD value
	 *            is assigned to participating text properties and associations
	 *            in the LexGrid repository. If not specified, this is the
	 *            assumed default.
	 * @param stopOnErrors
	 *            True means stop if any load error is detected. False means
	 *            attempt to load what can be loaded if recoverable errors are
	 *            encountered.
	 * @param async
	 *            Flag controlling whether load occurs in the calling thread. If
	 *            true, the load will occur in a separate asynchronous process.
	 *            If false, this method blocks until the load operation
	 *            completes or fails. Regardless of setting, the getStatus and
	 *            getLog calls are used to fetch results.
	 * @throws LBException
	 */
	public void load(URI source, String uid, String pwd, String driver,
			LocalNameList targetTerminologies, int hierarchyOpt,
			boolean stopOnErrors, boolean async) throws LBException;
	/**
	 * Load content from a database repository. This will also result in
	 * implicit generation of standard indices required by the LexBIG runtime.
	 * <p>
	 * An exception is raised if resources cannot be accessed or another load
	 * operation is already in progress.
	 * 
	 * @param source
	 *            Location of the source database. Typically this is specified
	 *            in the form of a URL that indicates the database server, port,
	 *            name, and optional properties. An example (items in square
	 *            brackets are optional) is as follows:
	 * <tt>
	 * jdbc:mysql://[host][,failoverhost...][:port]/[database]
	 *   [?propertyName1][=propertyValue1][&propertyName2][=propertyValue2]...
	 * </tt>
	 * @param uid
	 *            User ID for authenticated access, if required and not
	 *            specified as part of the database URL parameters.
	 * @param pwd
	 *            Password for authenticated access, if required and not
	 *            specified as part of the database URL parameters.
	 * @param driver
	 *            Name of the JDBC driver to use when accessing the database.
	 * @param targetTerminologies
	 *            The list of UMLS terminologies to load. If null, all available
	 *            terminologies will be loaded. The terminology name should be
	 *            the SAB name of the terminology - or values from the RSAB
	 *            column of the MRSAB file.
	 * @param stopOnErrors
	 *            True means stop if any load error is detected. False means
	 *            attempt to load what can be loaded if recoverable errors are
	 *            encountered.
	 * @param async
	 *            Flag controlling whether load occurs in the calling thread. If
	 *            true, the load will occur in a separate asynchronous process.
	 *            If false, this method blocks until the load operation
	 *            completes or fails. Regardless of setting, the getStatus and
	 *            getLog calls are used to fetch results.
	 * @throws LBException
	 * @Deprecated
	 */
	public void load(URI source, String uid, String pwd, String driver,
			LocalNameList targetTerminologies, boolean stopOnErrors,
			boolean async) throws LBException;

	/**
	 * Load content from RRF files. This will also result in implicit generation
	 * of standard indices required by the LexBIG runtime.
	 * <p>
	 * An exception is raised if resources cannot be accessed or another load
	 * operation is already in progress.
	 * 
	 * @param source
	 *            URI corresponding to the directory containing the RRF files as
	 *            provided by the NLM or pruned through MetamorphoSys.
	 * @param targetTerminologies
	 *            The list of UMLS terminologies to load. If null, all available
	 *            terminologies will be loaded. The terminology name should be
	 *            the SAB name of the terminology - or values from the RSAB
	 *            column of the MRSAB file.
	 * @param stopOnErrors
	 *            True means stop if any load error is detected. False means
	 *            attempt to load what can be loaded if recoverable errors are
	 *            encountered.
	 * @param async
	 *            Flag controlling whether load occurs in the calling thread. If
	 *            true, the load will occur in a separate asynchronous process.
	 *            If false, this method blocks until the load operation
	 *            completes or fails. Regardless of setting, the getStatus and
	 *            getLog calls are used to fetch results.
	 * @throws LBException
	 * @Deprecated
	 */
	public void load(URI source, LocalNameList targetTerminologies,
			boolean stopOnErrors, boolean async) throws LBException;

	/**
	 * Load content for the UMLS semantic network from files distributed by the
	 * NLM (see http://semanticnetwork.nlm.nih.gov/Download/index.html). This
	 * will also result in implicit generation of standard indices required by
	 * the LexBIG runtime.
	 * <p>
	 * An exception is raised if resources cannot be accessed or another load
	 * operation is already in progress.
	 * 
	 * @param source
	 *            URI corresponding to the directory containing the semantic
	 *            network files.
	 * @param stopOnErrors
	 *            True means stop if any load error is detected. False means
	 *            attempt to load what can be loaded if recoverable errors are
	 *            encountered.
	 * @param async
	 *            Flag controlling whether load occurs in the calling thread. If
	 *            true, the load will occur in a separate asynchronous process.
	 *            If false, this method blocks until the load operation
	 *            completes or fails. Regardless of setting, the getStatus and
	 *            getLog calls are used to fetch results.
	 * @throws LBException
	 */
	public void loadSemnet(URI source, boolean stopOnErrors, boolean async)
			throws LBException;

	/**
	 * Validate content for a candidate resource without performing a load.
	 * <p>
	 * Returns without exception if validation succeeds.
	 * 
	 * @param source
	 *            Location of the source database. Typically this is specified
	 *            in the form of a URL that indicates the database server, port,
	 *            name, and optional properties. An example (items in square
	 *            brakets are optional) is as follows: <tt>
	 * jdbc:mysql://[host][,failoverhost...][:
	 * port]/[database][?propertyName1][=propertyValue1][&propertyName2][=propertyValue
	 * 
	 * 2]...
	 * </tt>
	 * @param uid
	 *            User ID for authenticated access, if required and not
	 *            specified as part of the source URI parameters.
	 * @param pwd
	 *            Password for authenticated access, if required and not
	 *            specified as part of the source URI parameters.
	 * @param driver
	 *            Name of the JDBC driver to use when accessing the database.
	 * @param targetTerminologies
	 *            The list of UMLS terminologies to load. If null, all available
	 *            terminologies will be loaded.
	 * @param hierarchyOpt
	 *            Process hierarchical relationships stored by the MRHIER file.
	 *            Supported values are: 0 = Do not process information from
	 *            MRHIER; basic associations are calculated from MRREL without
	 *            adding atom-specific contexts. 1 = Process atom chains that
	 *            have been tagged with context identifiers (HCD). The HCD value
	 *            is assigned to participating text properties and associations
	 *            in the LexGrid repository. If not specified, this is the
	 *            assumed default.
	 * @param validationLevel
	 *            Loader-specific level of validation; 0 = verify that the
	 *            database and target terminologies are present and accessible.
	 * @throws LBException
	 */
	public void validate(URI source, String uid, String pwd, String driver,
			LocalNameList targetTerminologies, int hierarchyOpt, int validationLevel)
			throws LBException;

	/**
	 * Validate content for a candidate resource without performing a load.
	 * <p>
	 * Returns without exception if validation succeeds.
	 * 
	 * @param source
	 *            Location of the source database. Typically this is specified
	 *            in the form of a URL that indicates the database server, port,
	 *            name, and optional properties. An example (items in square
	 *            brakets are optional) is as follows: <tt>
	 * jdbc:mysql://[host][,failoverhost...][:
	 * port]/[database][?propertyName1][=propertyValue1][&propertyName2][=propertyValue
	 * 
	 * 2]...
	 * </tt>
	 * @param uid
	 *            User ID for authenticated access, if required and not
	 *            specified as part of the source URI parameters.
	 * @param pwd
	 *            Password for authenticated access, if required and not
	 *            specified as part of the source URI parameters.
	 * @param driver
	 *            Name of the JDBC driver to use when accessing the database.
	 * @param targetTerminologies
	 *            The list of UMLS terminologies to load. If null, all available
	 *            terminologies will be loaded.
	 * @param validationLevel
	 *            Loader-specific level of validation; 0 = verify that the
	 *            database and target terminologies are present and accessible.
	 * @throws LBException
	 * @Deprecated
	 */
	public void validate(URI source, String uid, String pwd, String driver,
			LocalNameList targetTerminologies, int validationLevel)
			throws LBException;

	/**
	 * Validate content from RRF files. This will also result in implicit
	 * generation of standard indices required by the LexBIG runtime.
	 * <p>
	 * Returns without exception if validation succeeds.
	 * 
	 * @param source
	 *            URI corresponding to the directory containing the RRF files as
	 *            provided by the NLM or pruned through MetamorphoSys.
	 * @param targetTerminologies
	 *            The list of UMLS terminologies to load. If null, all available
	 *            terminologies will be validated. The terminology name should be
	 *            the SAB name of the terminology - or values from the RSAB
	 *            column of the MRSAB file.
	 * @param hierarchyOpt
	 *            Process hierarchical relationships stored by the MRHIER file.
	 *            Supported values are: 0 = Do not process information from
	 *            MRHIER; basic associations are calculated from MRREL without
	 *            adding atom-specific contexts. 1 = Process atom chains that
	 *            have been tagged with context identifiers (HCD). The HCD value
	 *            is assigned to participating text properties and associations
	 *            in the LexGrid repository. If not specified, this is the
	 *            assumed default.
	 * @param validationLevel
	 *            Loader-specific level of validation; 0 = verify all files are
	 *            present for the target terminologies and conform to the
	 *            anticipated format.
	 * @throws LBException
	 */
	public void validate(URI source, LocalNameList targetTerminologies,
			int hierarchyOpt, int validationLevel) throws LBException;

	/**
	 * Validate content from RRF files. This will also result in implicit
	 * generation of standard indices required by the LexBIG runtime.
	 * <p>
	 * Returns without exception if validation succeeds.
	 * 
	 * @param source
	 *            URI corresponding to the directory containing the RRF files as
	 *            provided by the NLM or pruned through MetamorphoSys.
	 * @param targetTerminologies
	 *            The list of UMLS terminologies to load. If null, all available
	 *            terminologies will be validated. The terminology name should be
	 *            the SAB name of the terminology - or values from the RSAB
	 *            column of the MRSAB file.
	 * @param validationLevel
	 *            Loader-specific level of validation; 0 = verify all files are
	 *            present for the target terminologies and conform to the
	 *            anticipated format.
	 * @throws LBException
	 * @Deprecated
	 */
	public void validate(URI source, LocalNameList targetTerminologies,
			int validationLevel) throws LBException;

	/**
	 * Validate UMLS semantic network files prior to performing a load operation.
	 * <p>
	 * Returns without exception if validation succeeds.
	 * 
	 * @param source
	 *            URI corresponding to the directory containing the semantic
	 *            network files.
	 * @param validationLevel
	 *            Loader-specific level of validation; 0 = verify all files are
	 *            present for the target terminologies and conform to the
	 *            anticipated format.
	 * @throws LBException
	 */
	public void validateSemnet(URI source, int validationLevel)
			throws LBException;

}