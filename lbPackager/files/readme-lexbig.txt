===============================================================================
Overview
===============================================================================
Provides materials created by Mayo Clinic Division of Biomedical Informatics
in fulfillment of the following projects:

- LexGrid Vocabulary Services for caBIG® (LexBIG/EVS)
- LexGrid Vocabulary Services for NCBO™  (LexBIO)

LexEVS provides core application programming interfaces (APIs) and
tools used to access and maintain vocabulary content based on the LexGrid
model.  The LexEVS API and underlying repository serve as infrastructure
for NCI Enterprise Vocabulary Services (EVS).
 
The LexEVS architecture is enhanced and extended as required to meet
specific needs of NCBO initiatives, and is incorporated into the architecture
of the NCBO BioPortal.

Note that changes for both projects feed into the common code base, but
reflect specific work done in support of these different communities and
respective contracts.

===============================================================================
Additional resources for LexEVS are available at:
===============================================================================
- The caBIG® Vocabulary Knowledge Center:
  http://cabig-kc.nci.nih.gov/Vocab/KC
  
  This site provides general help and support for download, installation, and
  use of this software package.  It is the recommended starting point for those
  not directly involved in the development of the LexEVS or EVS software.
 
- The caBIG® LexEVS Project:
  http://gforge.nci.nih.gov/projects/lexevs
 
  This site serves as the primary home for development of new LexEVS and EVS
  releases.  It is recommended as starting point for developers working on the
  LexEVS and EVS projects, as well as anyone needing access to detailed
  project information or pre-production code releases.

- The Mayo Informatics site:
  http://informatics.mayo.edu

===============================================================================
Getting Started
===============================================================================
- For detailed information regarding LexEVS system requirements, installation,
  administration, and configuration refer to the Administration Guide.
  This guide is available as a PDF file (LexEVS_Installation_Admin_Guide.pdf)
  from the download site.  Once installed, it is also available in the
  product /doc directory.

- Installation of the LexEVS software is performed by downloading and invoking
  an executable jar file from the Vocabulary Knowledge Center or LexEVS GForge
  sites.  The installer name will reflect the version being installed, for
  example LexEVS-install-2.3.0.jar.
  
  By default, invoking the jar will result in a graphical user interface being
  presented to select install options.  Alternatively, an xml-based options
  file can be specified to bypass graphical display and provide an automated
  or command-line driven installation.  An example options file is available
  for reference from the download site.

- For details on programming to the LexEVS API, refer to the
  http://cabig-kc.nci.nih.gov/Vocab/KC for release documents for the latest 
  LexEVS Version
  
- Before invoking vocabulary services, you must first modify the
  /resources/config/config.props file to establish database connection criteria
  and settings for runtime behavior.  Refer to instructions in the admin
  guide for additional information on available config settings.

- To perform installation/build verification tests, switch to the /test
  subdirectory and invoke the TestRunner script.  Enter TestRunner without
  parameters for available reporting options.  Note that the test suite
  is only available if the test framework has been installed.
  
- Public API-level interfaces are available in the Javadoc.
  Refer to /doc/javadoc/index.html in the installation directory.

===============================================================================
Migrating from previous releases
===============================================================================
- The numbering convention for packaged releases is in the following format:
  <Version.Revision.PatchLevel> (e.g. 2.3.0).  New versions are introduced on
  release of significant new function or structural change to the supported
  model, repository, or API.  Revisions are provided to introduce incremental
  enhancements or structural changes.  Patches are introduced to incorporate
  bug fixes or minor enhancements. 

- For vocabulary content, the general intent is to provide n-1 compatibility
  between release revisions.  For example, release 2.3.x code should be able to
  be installed and configured to run against a data repository populated by
  release 2.2.x.  Specific care must be taken, however, to preserve vocabulary
  metadata and indexes across release boundaries.  Refer to the Knowledge
  Center resources for specific information and guidance on release to release
  migration.
  
===============================================================================
Release Notes & Status
===============================================================================
-2.3.0 (10/07/2008)
        -----------------------------------------------------------------------
        General
        -----------------------------------------------------------------------
      - 2.3.0 final release.  Please direct any questions or feedback regarding
        this release to the caBIG® Vocabulary Knowledge Center at
        http://cabig-kc.nci.nih.gov/Vocab/KC.
      - Changes were made to the izpack installer xml configuration to prevent
        error in some operating systems.
      - Includes updated versions of administration and programmer guides.
        -----------------------------------------------------------------------
        GUI tools, Examples, and Command-line administration
        -----------------------------------------------------------------------
      - Memory heap settings increased in GUI launch script for 64-bit Linux.
        -----------------------------------------------------------------------
        Information Model, Load, and Export 
        -----------------------------------------------------------------------
      - GForge #16728: LexBIG NCI Thesaurus loader fails while loading source
        concept C6724.
        http://gforge.nci.nih.gov/tracker/index.php?func=detail&aid=16728&
        group_id=491&atid=1850
      - Updates to prototype Protégé-based generic OWL loader.
      
-2.3.0rc3 (09/22/2008)
        -----------------------------------------------------------------------
        General
        -----------------------------------------------------------------------
      - Release candidate. Full notes for the release will be provided in the
        final 2.3.0 packaged distribution.  In the interim, please direct any
        questions regarding this release to informatics@mayo.edu.
        -----------------------------------------------------------------------
        API Runtime Implementation
        -----------------------------------------------------------------------
      - GForge #16545: Fix to increase NCI MetaThesaurus searching performance.
        https://gforge.nci.nih.gov/tracker/index.php?func=detail&aid=16545
        &group_id=491&atid=1850
        -----------------------------------------------------------------------
        Information Model, Load, and Export 
        -----------------------------------------------------------------------
      - Property name for HL7 designations has been change from 
        "textualpresentation" to "print_name".
        
-2.3.0rc2 (08/20/2008)
        -----------------------------------------------------------------------
        General
        -----------------------------------------------------------------------
      - Candidate for release to NCI QA tier. Code and interfaces are frozen,
        other than critical fixes.
      - Updates to administrator guide, programmer guide, and license text for
        currency with 2.3 release are pending, to be completed in 2.3.0 final.
        -----------------------------------------------------------------------
        API Runtime Implementation
        -----------------------------------------------------------------------
      - GForge #15015: Fix to correctly match the special case 'conceptCode'
        property when mixed with other property names in a single restriction.
        http://gforge.nci.nih.gov/tracker/index.php?func=detail&aid=15015
        &group_id=14&atid=134
      - GForge #15071: Make copyright information accessible without requiring
        EVS secure login.
        http://gforge.nci.nih.gov/tracker/index.php?func=detail&aid=15071
        &group_id=491&atid=1853
      - BioPortal Sup: Fix to ResourceManager mapping of code system name and
        version identifiers in order to prevent errors disambiguating coding
        schemes that carry the same name but different version identifiers.  
        http://bmir-gforge.stanford.edu/gf/project/bioportal_core/tracker/
        ?action=TrackerItemEdit&tracker_item_id=617
      - BioPortal Sup: Added resolve methods to CodedNodeSet allowing option
        to return results without fully resolving concept references, thereby
        eliminating database access.
      - BioPortal Sup: Fixed hierarchy path to root convenience method to
        ensure the resolveConcept flag is recognized when populating concept
        details.
      - MultiDatabase: Fixed cursor problem when running against Oracle.
      - Internal refactoring of database and index code to support changes to
        LexGrid and LexBIG models.
        http://gforge.nci.nih.gov/tracker/index.php?func=detail&aid=15474
        &group_id=491&atid=1850 (enumerates model changes)
        -----------------------------------------------------------------------
        GUI tools, Examples, and Command-line administration
        -----------------------------------------------------------------------
      - GForge #12572: Enhance ProfileScheme program & package as example.
        http://gforge.nci.nih.gov/tracker/index.php?func=detail&aid=12572
        &group_id=14&atid=134
      - GForge #12995: Add database information to the GUI display.
        http://gforge.nci.nih.gov/tracker/index.php?func=detail&aid=12995
        &group_id=14&atid=137
      - Allow options (on both GUI and administrative scripts) for loaders
        to accept an optional manifest file to be applied during the load.
        Also provide GUI option and administrative script to apply a manifest
        to an already loaded vocabulary.  Correlates to GForge item #13193,
        detailed in the 'Load' section below.
      - Allow options (on both GUI and administrative scripts) for loaders
        to accept preferences where applicable.  Correlates to GForge item
        #12357, detailed in the 'Load' section below.
      - Provide GUI options and administrative scripts to support the new
        HL7 RIM and prototype OWL loaders, as detailed in the 'Load' section
        below.
        -----------------------------------------------------------------------   	
        Build & Test
        -----------------------------------------------------------------------
      - GForge #16155: Create manifest XML for NCI test sources based on
        changes to the manifest XSD schema.
        http://gforge.nci.nih.gov/tracker/index.php?func=detail&aid=16155
        &group_id=14&atid=134
      - Provide simplified packaging and distribution of LexBIG 1st party
        code as a single jar file.
      - Enhance JUnit tests for new functions and improved coverage of
        previous functions (e.g. hierarchy API).
      - Update example and test resources (e.g. Automobiles.xml) as required
        to reflect model changes.
      - Refreshed 3rd party jar files as required for currency.
        -----------------------------------------------------------------------
        Information Model, Load, and Export 
        -----------------------------------------------------------------------
      - GForge #8670 : Add support to import Standardized MedDRA Query (SMQ)
        attributes as association qualifiers.
        http://gforge.nci.nih.gov/tracker/index.php?func=detail&aid=8670
        &group_id=14&atid=134
      - GForge #8671 : Honor definition of additional complex properties,
        represented as embedded XML fragments, during import of NCI OWL sources.
        http://gforge.nci.nih.gov/tracker/index.php?func=detail&aid=8671
        &group_id=14&atid=135
      - GForge #12357: Provide externally configurable preferences for loaders
        accepting optional parameters, either as a program generated object or
        XML file capable of validation against a formal schema (XSD).
        http://gforge.nci.nih.gov/tracker/index.php?func=detail&aid=12357
        &group_id=14&atid=137
      - GForge #12997: Fix to remove extraneous addition of 'is_a' as source on
        load of OWL sources.
        http://gforge.nci.nih.gov/tracker/index.php?func=detail&aid=12997
        &group_id=14&atid=134
      - GForge #13175: Fix to propertyLink creation on UMLS load to honor
        representational form on import of AUI text.
        http://gforge.nci.nih.gov/tracker/index.php?func=detail&aid=13175
        &group_id=14&atid=134
      - GForge #13193: Allow optional specification of a manifest file (xml)
        on all loaders (other than native LexGrid XML) to allow customization
        of coding scheme metadata.  Manifests can be applied at time of load 
        as part of a post-load process.
        http://gforge.nci.nih.gov/tracker/index.php?func=detail&aid=13193
        &group_id=14&atid=137
      - GForge #13501: New loader for direct import from HL7 RIM database.
        http://gforge.nci.nih.gov/tracker/index.php?func=detail&aid=13501
        &group_id=14&atid=137
      - GForge #13565: Utilize MRRANK RRF file during load of LOINC (and
        potentially other) UMLS sources.
        http://gforge.nci.nih.gov/tracker/index.php?func=detail&aid=13565
        &group_id=14&atid=137
      - GForge #13954: Fix to populate SupportedProperty mapping on UMLS load.
        http://gforge.nci.nih.gov/tracker/index.php?func=detail&aid=13954
        &group_id=14&atid=134
      - GForge #14252: Add ability to load and query NCI Metathesaurus history.
        http://gforge.nci.nih.gov/tracker/index.php?func=detail&aid=14252
        &group_id=491&atid=1850
      - GForge #14483: Fix to populate SupportedHierarchy mapping on UMLS load.
        http://gforge.nci.nih.gov/tracker/index.php?func=detail&aid=14483
        &group_id=14&atid=134
      - GForge #14485: Add support on load of the UMLS Semantic Network to
        optionally import inferred relationships, defined through inheritance.
        http://gforge.nci.nih.gov/tracker/index.php?func=detail&aid=14485
        &group_id=14&atid=134
      - GForge #14767: Import all synonyms on OBO load.
        http://gforge.nci.nih.gov/tracker/index.php?func=detail&aid=14767
        &group_id=491&atid=1850
      - BioPortal Sup: Various fixes to OBO export.
      - BioPortal Sup: Fixed OBO parsing to correctly extract properties based
        on source and subref information.
      - Deliver prototype of new generic OWL loader based on the Protege
        engine (provided in 2.3.0 for evaluation purposes).  This loader
        replaces the previous 'generic' (non-NCI) OWL loader.
      - Incorporate model changes as introduced for ongoing support, silver
        submission, and prototype OWL functionality.
        http://gforge.nci.nih.gov/tracker/index.php?func=detail&aid=15474
        &group_id=491&atid=1850 (enumerates model changes)
      - Update published manifest schema (CodingSchemeManifest.xsd) as required
        to remain current with model changes.

-2.3.0rc1 (07/29/2008)
        -----------------------------------------------------------------------
        General
        -----------------------------------------------------------------------
      - Release candidate. Full notes for the release will be provided in the
        final 2.3.0 packaged distribution.  In the interim, please direct any
        questions regarding this release to informatics@mayo.edu.

-2.2.0 (03/05/2008)
        -----------------------------------------------------------------------
        General
        -----------------------------------------------------------------------
      - Refer to left-hand columns to indicate work specifically targeted
        at or funded by a particular project. (* items indicate potential
        impact to code)
      - Use of the new hierarchy convenience API is currently tested and
        supported against sources loaded through XML, OBO, NCI OWL, UMLS,
        and NCI MetaThesaurus loaders.  Support for generic OWL loads
        will be evaluated as part of pending changes to the generic OWL
        load process.
|NCI (col 1)
| |NCBO (col 2)
| | |
        -----------------------------------------------------------------------
        Documentation 
        -----------------------------------------------------------------------
|x|x| - License was updated to reflect 2008 and inclusion of jdbm.
        -----------------------------------------------------------------------
        API Implementation & Test 
        -----------------------------------------------------------------------
|x| | - GForge #7880: A method was added to the LexBIGServiceConvenienceMethods
        interface to allow return of a CodedNodeSet as opposed to resolved
        references. The CodedNodeSet can then be further restricted (e.g. by
        property) before resolve.
|x| | - The getHierarchyPathToRoot method has been changed from the Beta to
        include a new parameter allowing the user to control the amount of
        information resolved (all paths, one path, or one per hierarchy/root).
|x| | - The getHierarchy* convenience methods now support specification of
        null for the hierarchy ID to allow generic navigation of hierarchical
        associations independent of root nodes.
|x|x| - Re-implemented LexBIGServiceImpl to provide for a single instance of 
        the service for a more centralized instantiation.
|x| | *	NOTE for LexBIG distributed implementation.  Use of the LexBIGService-
        ConvenienceMethodsImpl class requires the use of method 
        setLexBIGService(LexBIGService lbs) to set the class to point to the 
        EVSApplicationService for distributed LexBIG calls.  Assignment of 
        client side coded node graph and set objects after restriction method
        calls on those objects is required to insure client side execution.
        Ex:
        For some instantiated object of CodedNodeSet, cns --
        cns = cns.restrictTo(....  )
|x|x| - Changes to SQLImplementedMethods allow broader validation of URNs
        where different formats of supported association urns occur for a given
        terminology source type. 
        -----------------------------------------------------------------------
        Information Model, Load, and Export 
        -----------------------------------------------------------------------    
|x|x| - GForge #7880: Corrected OBO loader problem that prevented hierarchy 
        traversal. Updated LexBIGServiceConvenienceMethodsImpl with annotations
        coded node graph and set assignments to facilitate LexBIG distributed
        calls from that class 
|x| | -	OBO loader now sets all associations in a Supported Hierarchy to
		Transitive. This will override any settings in the relationship file or
		the OBO file itself. OBO loader now loads only those associations that
		are present in the source files to the Supported Hierarchy associations
		eliminating extra associations previously brought in by default.
|x| | - The UMLS loaders now only add a Supported Hierarchy
		if the association is 'PAR' or 'CHD'. The eliminates redundant
		Supported Hierarchies.
|x| | * NOTE: If MetaThesaurus was loaded in the 2.2 Beta timeframe, there is
        no need to do a full reload.  However, the database administrator
        should remove rows in the 'codingSchemeSupportedAttrib' table where
        codingSchemeName = 'NCI MetaThesaurus' and supportedAttribTag =
        'Hierarchy', *except* for those with 'PAR' or 'CHD' in the 'idValue'
        column.  This is required for the hierarchy navigation to work as
        expected.  Please contact the LexBIG team if you have any questions
        migrating a previous load of NCI Meta for use with hierarchy APIs.
        
===============================================================================
Notes & Status
===============================================================================
-2.2.beta (02/22/2008)
        -----------------------------------------------------------------------
        General
        -----------------------------------------------------------------------
      - Please note that this is an beta release and is not intended for
        production use.  Use at your own risk!
      - Refer to left-hand columns to indicate work specifically targeted
        at or funded by a particular project. (* items indicate potential
        impact to code)
    
|NCI (col 1)
| |NCBO (col 2)
| | |
        -----------------------------------------------------------------------
        Documentation 
        -----------------------------------------------------------------------
|x|x| - Programmer and administration guides were updated for the 2.2 release,
        incorporating many comments from the GForge WIKI, corrections, etc.
        -----------------------------------------------------------------------
        Information Model, Load, and Export 
        -----------------------------------------------------------------------
|x| | - GForge #10524: Additional memory optimization; refer to the GForge
        tracker for details. 
        -----------------------------------------------------------------------
        API Implementation & Test 
        -----------------------------------------------------------------------
|x|x| - GForge #7880:  Updates to hierarchy convenience methods to handle
        issues discovered during test against additional ontologies.  Known
        issues include a getHierarchyRoots method failure against cell obo.
  
===============================================================================
Notes & Status
===============================================================================
-2.2.alpha3 (02/15/2008)
        -----------------------------------------------------------------------
        General
        -----------------------------------------------------------------------
      - Please note that this is an alpha release and is not intended for
        production use.  Use at your own risk!
      - Refer to left-hand columns to indicate work specifically targeted
        at or funded by a particular project. (* items indicate potential
        impact to code)
    
|NCI (col 1)
| |NCBO (col 2)
| | |
        -----------------------------------------------------------------------
        Information Model, Load, and Export 
        -----------------------------------------------------------------------
|x|x| - GForge #7880:  Registration of supported hierarchies is handled at 
        load time. For the alpha 3 release, support was provided for the 
        generic OWL loader, the RRF loader, the UMLS Semnet Loader, and the 
        OBO loader.  The NCI OWL loader and generic OWL loader can now also
        load supported hierarchy metadata from a manifest file.
|x| | - GForge #10524: Internal changes were made to improve the memory
        footprint during EMF-based loads (OWL, OBO, SemNet, XML).  Specific
        changes were made to the NCI OWL load process to page information to
        temporary storage, preventing the need to have full Protege and EMF
        models in memory at the same time.  Changes were also introduced to
        allow content to be streamed between input and output classes,
        preventing the need to rebuild the EMF model before writing to
        database.  Testing this solution will be a focus in the next week. 
        -----------------------------------------------------------------------
        API Implementation & Test 
        -----------------------------------------------------------------------
|x|x| - Support for all supported hierarchy example code relies on the 
        registration of hierarchy info at load time.  Support for the 
        new supported attribute will require a new load of a given terminology.
        -----------------------------------------------------------------------
        GUI tools, Examples, and Command-line administration
        -----------------------------------------------------------------------
|x| | - MRHIER option was removed from UMLS loads (GUI and command line).
        Due to recent changes, performance impact was now negligible enough to
        allow the option to always be turned on.
        -----------------------------------------------------------------------   	
        Build & Test
        -----------------------------------------------------------------------
|x| | - This build will be stress tested, with the Alpha test period concluding
        on Feb 22.  This will be followed by a beta build and additional week
        of test.  The final 2.2 release is slated to be delivered by the first
        week of March.  
  
===============================================================================
Notes & Status
===============================================================================
-2.2.alpha2 (01/22/2007)
        -----------------------------------------------------------------------
        General
        -----------------------------------------------------------------------
      - Please note that this is an alpha release and is not intended for
        production use.  Use at your own risk!
      - Refer to left-hand columns to indicate work specifically targeted
        at or funded by a particular project. (* items indicate potential
        impact to code)
    
|NCI (col 1)
| |NCBO (col 2)
| | |
        -----------------------------------------------------------------------
        Information Model, Load, and Export 
        -----------------------------------------------------------------------
|x|x| - GForge #7880:  Added support to model registration of hierarchical
        information on a per coding scheme basis.  Each coding scheme can now
        identify multiple supported hierarchies (e.g. is_a, part_of).  Each
        hierarchy is further defined in terms of root node(s), participating
        associations (e.g. hasSubtype) and direction (forward/reverse) of
        navigation on those associations to traverse the tree.
        
        Registration of supported hierarchies is handled at load time.
        For the alpha 2 release, only the NCI OWL loader is currently taking
        advantage of this new support.  Support for additional formats will
        be added in the next code drop.
        
        EMF and Castor java bean representations were regenerated to carry
        and enable load/export of hierarchy info.  Manifest support was added
        to allow override of supported hierarchies on load of OWL sources.
        
|x| | - GForge #10220: Circular associations between CUIs encountered during
        load of the MetaThesaurus are now represented as Property Links.
        Links will be added at the CUI level (on affected coded entries).
        The value of the property link ID will be the association (e.g. RQ)
        and the source and target will point to the concept properties (AUI
        level terms) involved.  This provides the ability to navigate the
        intra-concept relationship between terms without needing to carry a
        formal association.  Removing the association in turn eliminates
        the cyclical concept reference.
        
|x| | - MRHIER context processing is now enabled on the Metathesaurus loader.
        Context processing allows discovery and navigation of UMLS-defined
        hierarchy chains.  By querying association and property qualifiers
        the API allows finer-grained discovery of links between between AUI-
        level terms in addition to CUI (CodedEntry to CodedEntry) level.
        In addition, processing overhead of context relationships was improved
        by copying only the required subset of the RRF file required for
        processing of HCD chains.

|x| | - Re-enable export to XML (broken in alpha 1)

        -----------------------------------------------------------------------
        API Implementation & Test 
        -----------------------------------------------------------------------
|x|x| * New convenience methods were added for hierarchical processing to the
        LexBIGServiceConvenienceMethods extension interface.  These methods
        enable simplified discovery and navigation of hierarchies independent
        of source-specific relationships.  New method names start with the
        prefix getHierarchy*.  Old methods used for source-specific navigation
        of hierarchies (getTopNodes, getEndNodes, getParentsOf, getChildrenOf)
        have been deprecated.  See GForge #7880 above.
        
        NOTE: These new methods rely on the registration of hierarchy info at
        load time.  Only the NCI OWL loader has been modified so far to
        perform registration.  Additional loaders will be modified in the
        next code release.
        
|x| | - GForge #10363: Fixed query by conceptCode property, supporting an
        initial wildcard as described by 'contains' match algorithm.
      
|x| | - GForge #10178: Connection pools are now closed in a timely manner and
        running one load after another will now result in no extra, or hanging
        connections to a database.

        -----------------------------------------------------------------------
        GUI tools, Examples, and Command-line administration
        -----------------------------------------------------------------------
|x| | - Added the ability to view circular concept references (represented as
        property links) when visualizing CodedNodeSets, per GForge #10220.
      
|x| | - Added option for MRHIER processing on Meta load.

        -----------------------------------------------------------------------   	
        Build & Test
        -----------------------------------------------------------------------
|x| | - Subsumption examples were removed in favor of new examples to demonstrate
        improved convenience methods for hierarchical processing.  The new
        example programs are ListHierarchy, ListHierarchyByCode, and
        ListHierarchyPathToRoot.
  

-2.2.alpha1 (12/18/2007)
        -----------------------------------------------------------------------
        General
        -----------------------------------------------------------------------
      - This is a minor point release intended primarily to address issues
        identified by NCI for the caCORE 4.0 release.  Please note that this is
        an alpha release and is not intended for production use.
        Use at your own risk!
      - Refer to left-hand columns to indicate work specifically targeted
        at or funded by a particular project. (* items indicate potential
        impact to code)
    
|NCI (col 1)
| |NCBO (col 2)
| | |
        -----------------------------------------------------------------------
        Load and Export 
        -----------------------------------------------------------------------
|x| | - GForge #10560: The NCI OWL loader was changed so that load of sources
        other than the NCI Thesaurus (utilizing an optional manifest file) did
        not carry forward defaulted attributes specific to the Thesaurus.
|x| | - GForge #10760: Additional content providers and corresponding URNs
        were added to the default supported sources for the NCI Thesaurus.
|x| | - GForge #10780: Any default supportedXXX properties can be overridden
        by using an externally configurable manifest file in conjunction with
        the NCI OWL loader.
|x| | * GForge #10524: For the NCI OWL loader, an option was added to allow
        the underlying Protege API to cache information to a database during
        the load.  This reduces the overall memory footprint, potentially
        allowing larger files to be loaded without failure, but significantly
        degrades performance.  It is considered work in progress and only part
        of OWL changes targeted to the March release.
|x| | - GForge #10516: HL7 Loader prototype supported.  Loads directly from
        the HL7 RIM database with each vocabulary (e.g. ActClass) as a
        separate coding scheme.  Due to the large number of coding schemes
        loaded (~250), this loader is not currently recommended in multi-
        database configurations since each code system will allocate separate
        connections when accessed (see GForge #10178).
|x|x| - Add option for recalculation of root nodes to NCI Meta load.
|x| | - Updated copyright information for NCI Thesaurus and Metathesaurus. 
        -----------------------------------------------------------------------
        API Implementation & Test 
        -----------------------------------------------------------------------
|x| | * GForge #8896: Four convenience methods (getTopNodes, getEndNodes, 
        getParentsOf, getChildrenOf) now have a NameAndValue qualifiers
        parameter. This allows a user to specify things like a specific source
        within the MetaThesaurus as the only valid source for a query.
|x| | - GForge #8440: Added a method to ConceptsUtil that returns *all* 
        properties of a concept rather than having to call five methods to get 
        the whole set.
        -----------------------------------------------------------------------
        GUI tools, Examples, and Command-line administration
        -----------------------------------------------------------------------
|x| | * Added parameter to GUI and Admin wrappers for using Protege database to 
        load to NCI OWL loader.
        ----------------------------------------------------------------------- 	
        Build & Test
        -----------------------------------------------------------------------
|x| | - GForge #6721: To eliminate some circular dependencies in the build,
        there has been a restructuring of the jars in the "runtime-components"
        directory and the "source" directory. Each source jar can now be    	 
        expanded into its own project without circular dependency issues.
  
  
-2.1.1 (9/20/2007)
      -------------------------------------------------------------------------
      General
      -------------------------------------------------------------------------
    - This is a minor point release intended primarily to address issues
      identified by NCI for the caCORE 4.0 release.  However, This build
      is still provided as codebase for other LexBIG-based deliverables.
      Refer to left-hand columns to indicate work specifically targeted at
      or funded by a particular project.   
    
|NCI (col 1)
| |NCBO (col 2)
| | |
|x|x|    

        -----------------------------------------------------------------------
        Load and Export
        -----------------------------------------------------------------------
|x| | * UMLS root node detection, import, and query.
      - GForge #8669: The RRF load will now process hierarchical relations 
        starting at the UMLS-designated Root Hierarchical Term (RHT). Typically 
        the root term will be in the form 'V-{SAB}' (e.g. V-MDR or V-ICD9CM). 
      - Links will be introduced to the special '@' and '@@' nodes in the 
        LexBIG system for each concept referenced by the V-{SAB} node.
      - Handle cases where 'PAR' and 'CHD' are not used as the UMLS relation
        name ('REL' field) but 'isa' or 'inverse_isa' are still defined as the
        source relation name ('RELA' field).  As an example MeSH maps 'isa'
        and 'inverse_isa' against relation narrower (RN) and relation broader
        (RB). In addition, RELA names are now interpreted as hierarchical on a
        case-by-case basis (e.g. 'part_of' may be mapped against 'PAR' in one
        source but not another).
      - Support multiple coding schemes per root code and multiple relation
        containers on root node resolution.  
      - Add support to detect and build root nodes and transitivity based on 
        hierarchical relations (e.g. PAR, CHD) in addition to hasSubtype during
        load of the NCI Metathesarus.
|x| | * For UMLS sources, optionally load MRHIER file to the intermediate
        database based on the load option specified for contextual link 
        processing (improves performance by bypassing intermediate load of
        the MRHIER table when not referenced).
|x| | * Misc fixes for UMLS load:
      - Fix null pointer exception mapping CUI to code.
      - Interpret blank DIR field in MRREL as 'N' instead of 'Y' by default,
        based on review of Meta distribution.
| | | * Add support for loading relations in the text loader.
|x|x| * Support to load OWL files other than the NCI Meta Thesaurus with the
        NCI OWL loader.
      - Support to optionally specify a manifest file on load of NCI-specific
        or generic OWL formats.  The manifest file is an XML file that allows
        for code system metadata to be customized or extended via a
        standardized XML configuration conforming to the following schema...
        http://LexGrid.org/schema/2006/01/LexOnt/CodingSchemeManifestList.xsd
      - Manifest files created and tested for GO, HL7, and VANDFRT.
      - See "http://gforge.nci.nih.gov/plugins/wiki/index.php?
        Configuring%20Manifest%20Files&id=14&type=g" for details on how to
        use the OWL/OBO Manifests for the NCI OWL, OWL and OBO loaders.
|x| | * GForge #8671: Support for handling of additional complex properties 
        embedded in XML annotations within NCI-curated OWL sources.
        
        -----------------------------------------------------------------------
        Implementation & Test (* items indicate potential impact to code)
        -----------------------------------------------------------------------
|x| | * Implemented #8441, modified parameters for resolveToList() in 
        getTopNodes() and getEndNodes() to include properties for the top and
        end nodes.     
        
        -----------------------------------------------------------------------
        GUI tools, Examples, and Command-line administration
        -----------------------------------------------------------------------
|x|x| * Prevent error and GUI exit on default resolution of reverse hierarchy
        from '@@', and improve the error message text used for GUI exit. 
|x|x| * For code set display, change labels of top or end nodes to '@' and '@@'
        instead of 'top-thing' and 'leaf-thing' to be consistent with actual
        IDs used and documented as part of the API.
|x|x| * Removed the 'hasSubtype' label from '@' and '@@' node connections; 
        the label could be misleading if the subsumption relation for the
        ontology uses a different name.
|x|x| * Update the default relationships recognized by the ListSubsumption
        example.

-2.1.0 (8/3/2007)
      -------------------------------------------------------------------------
      Items of special interest
      -------------------------------------------------------------------------
    - Several changes were made to allow for cacore to remotely interface with
      the LexGrid API.  These include annotating classes and methods to 
      indicate they are safe to run on a remote system from the API or if they
      are admin functions that should not be allowed by a remote client.

|NCI (col 1)
| |NCBO (col 2)
| | |
|x|x|

      -------------------------------------------------------------------------
      Load and Export
      -------------------------------------------------------------------------
| | | Fix 'off by 1' error preventing MRSAB entries that begin with '|' from
      being detected and allowed on load.  This prevented certain SABs, such
      as 'SRC', from being identified and loaded from RRF.
| |x| OBO export now adds type definitions to the OBO output.
| |x| OBO loader stores dbxref information as subrefs within the LexGrid model.
|x|x| Change date format registered to metadata xml to prevent invalid 
      characters from being written in internationalized versions of Windows OS.
|x| | Change NCI Thesaurus formal name to not include underscore.
| |x| GForge #7024.  For semantic net load, a single local name is set to the 
      same name as the coding scheme to satisfy cardinality described under the 
      LexGrid model.
|x| | GForge #7402, Properties stemming from CTV3 based on AUI as opposed to CUI
      are now handled properly.
|x| | Added OWL Manifest support for OWL files. Please see more details at NCI 
      GForge Wiki. Goto http://gforge.nci.nih.gov/, select link "LexGrid 
      Vocabulary Services for caBIG" Project from project list, select "Wiki" 
      tab, select "Administrator Tips, Tricks & Gotchas" link and select 
      "Configuring manifest files" link.
|x| | Provide more consistent navigation of subsumptive relations (GForge #7880)
      while preserving general compatibility.  Added automatic build of root 
      relation on UMLS load for "UMLS_Relations" container, based on common 
      navigable associations such as CHD (has child) and RB (relation broader).  

      -------------------------------------------------------------------------
      Implementation & Test (* items indicate potential impact to code)
      -------------------------------------------------------------------------
| |x| Added a restrictToDirectionalName method for CodedNodeGraphs.
| |x| Added Convenience Methods isForwardName and isReserveName to help 
      determine directionality of the directionalName within an association.
|x|x| Added Convenience methods getAssociationForwardName and 
      getAssociationReverseName for simplified lookup of association
      directional (forward/reverse) labels.
| |x| Added new boolean field keepLastAssociationLevelUnresolved to the 
      resolveAsList method of the CodedNodeGraph that could be used to 
      determine if a node has further children under it.(Child indicator)      
|x| | Fixed #7895, the work around for Lucene's lack of serialization in a 
      class was implemented on a temporary basis.
|x| | Change clone methods in CodedNodeSetImpl and CodedNodeGraphImpl from 
      protected to public to allow these methods to be proxied under cacore's 
      RMI interface.
|x| | Void methods in LexBIGServiceMetadataImpl now return this to insure 
      changes to object are properly remoted.  These changes are reflected in 
      the interface class in lbInterfaces.
|x| | CodeToReturn, CodeHolder, ResolvedConceptReferencesIteratorImpl and 
      ConvenienceMethods classes now implement Serializable.  
      ConvenienceMethods now has a no arg constructor that provides an immediate
      instantiation of a new LexBIGService object.  
      ResolvedConceptReferencesIteratorImpl has a new scroll() method that 
      prevents a inifinite loop problem cause by use of the next, hasNext 
      methods in client side RMI. This class also now has transient added to the
      thread data members to prevent serialization errors. (Reference Bugs #s 
      7910, 7912).

      -------------------------------------------------------------------------
      GUI tools, Examples, and Command-line administration
      -------------------------------------------------------------------------
| | | Added missing -cp command keyword to LoadRadLex.bat script.
|x| | Added ListSubsumption and ListSubsumptionMetaBySource examples and
      supporting scripts (per GForge #7880).
|x| | Added MetaMatch example (per GForge #7008).
| | | Allow resolution of graph for the selected entry to contain items up to 
      the number allowed by the config.props.
|x| | Added informational log entry instead of System.out for tree view.
|x|x| Ensure ordering of patch jar in front of runtime in custom class loader.

      -------------------------------------------------------------------------
      Modeling & Docs (* items indicate potential impact to code)
      -------------------------------------------------------------------------
|x| | The collections package of the model is annotated as client side safe.

-2.0.0 (4/23/2007)
      -------------------------------------------------------------------------
      Items of special interest
      -------------------------------------------------------------------------
    - This build provides a codebase for multiple LexBIG-based deliverables.
      Refer to items with 'x' in the margin for work specifically targeted at
      NCI (LexBIG) or NCBO (LexBIO) project requirements or interests. 

|NCI (col 1)
| |NCBO (col 2)
| | |
|x|x|
      -------------------------------------------------------------------------
      Configuration & Logging (* items indicate potential action items)
      -------------------------------------------------------------------------
      -------------------------------------------------------------------------
      Load and Export
      -------------------------------------------------------------------------
|x| | Improved load and representation of UMLS relationships that rely on
      additional information specified in MRHIER file (GForge #4105).
|x|x| The generic OWL loader now models the anonymous concepts and relations
      represented by RDF constructs such as lists, intersections, and unions.
|x| | Improved handling of role groups by NCI Thesaurus OWL loader to create
      anonymous concepts and navigable associations to component elements.
| |x| Handle representation of OBO intersection the same way we deal with
      OWL intersection (using anonymous concepts and navigable associations).
| |x| Allow generic OWL load to continue if scheme is not registered to 
      RDFDefaults.xml.
|x|x| Updated the generic OWL loader implementation to remove dependencies
      on the RDFDefaults.xml file.
| |x| Updated the generic OWL loader to pull version information from the
      versionInfo field in the OWL source, if available.   
| |x| Removed registration of forward names and reverse names that are not 
      part of OWL or OBO ontologies.
| |x| Modified the Protege loader for Radlex to bring in Synonym classes
      as concepts.
|x| | Fix for NCI load to register 'textualPresentation' as supported property.
| |x| Fix for character encoding problem when passing xml stream from generic
      OWL load to the EMF loader.
| |x| Fix to allow resolution of all branches on FMA load.
      -------------------------------------------------------------------------
      Implementation & Test (* items indicate potential impact to code)
      -------------------------------------------------------------------------
|x| | Classes exposed through API are now serializable.
|x|x| Fixed problems in CodedNodeGraph resolution which could result in
      incorrect graph representations (e.g. top nodes being droped) or cause
      some resolved nodes to drop the coding scheme version or tag.
| |x| Added a convenience method getAssociationForwardAndReverseNames() to 
      get the list of associations present in an ontology (Bugzilla #401).
| |x| Added a convenience method getCodingSchemesWithSupportedAssociation() to
      get a list of ontologies in which a relation exists (Bugzilla #400). 
| | | Added JUnit tests to check the correctness of the new methods.
|x| | Added new example FindRelatedNodesForTermAndAssoc.
|x|x| Fix/improve support for storage and query of association qualifications
      that utilize both key and value.
| |x| Fixed Bug #5970.  Config.props file DB_PARAM value now correctly passes 
	  parameters to the jdbc driver.
      -------------------------------------------------------------------------
      GUI tools, Examples, and Command-line administration
      -------------------------------------------------------------------------
|x| | Allow instantiation of LB_GUI class with specific LexBIGService instance.
|x| | Add support for MRHIER processing options (UMLS loads).
| | | Add association qualifier(s) to edge text when available.
| | | Modified the edge text to default to association name when a directional
      name is unavialable.
| | | Display [R] in edge label (with association name) if directional name is
      is not available and navigation direction is reversed.
|x| | Change preferred font in graph view to 'Arial Unicode MS' to assist with
      display of multilingual characters (when available).
| | | Tweaks to improve spacing and visualization of graphs with many items.
| | | Provide visual cues for previously selected items; visited items will be
      listed in blue.  Items that have been automatically added to the graph
      will be italicized (until paged in normally).
| | | Provide optional display of relations between secondary nodes in graphs.
      -------------------------------------------------------------------------
      Modeling & Docs (* items indicate potential impact to code)
      -------------------------------------------------------------------------
|x| | Interface classes were made serializable.

  
-2.0Beta5 (2/28/2007)
    ---------------------------------------------------------------------------
    Items of special interest
    ---------------------------------------------------------------------------
  - This build shares a common codebase with other LexBIG-based deliverables.
    Refer to items with '>' in the margin for work specifically targeted at
    caBIG requirements or areas of interest. 
    ---------------------------------------------------------------------------
    Configuration & Logging (* items indicate potential action items)
    ---------------------------------------------------------------------------
    ---------------------------------------------------------------------------
    Implementation & Test (* items indicate potential impact to code)
    ---------------------------------------------------------------------------
> - Tentative fix for bug in coded node graph queries when resolving top nodes
    participating in multiple associations; this could result in tope nodes
    being 'dropped' in some circumstances.
  - Adjusted automated test cases per model changes.
    ---------------------------------------------------------------------------
    Load and Export
    ---------------------------------------------------------------------------
  - Updated RDFDefaults.xml file to allow proper loading of additional OWL
    terminologies released through BIOPortal (e.g. snpontology_full.owl); note
    future direction is to remove dependency on this file entirely.
  - Fixed RDF/OWL load to ensure that for every new property created, content
    is never assigned to database as empty string (resulted in Oracle problem).
  - Minor changes to RDF/OWL processing (e.g. made a preferred presentation
    property to match the entity description for each anonymous concept).
    
    ---------------------------------------------------------------------------
    GUI
    ---------------------------------------------------------------------------
    ---------------------------------------------------------------------------
    Modeling & Docs (* items indicate potential impact to code)
    ---------------------------------------------------------------------------
  - Additional changes made to:
 	- Collections.xsd
 	- Core.xsd
 	- InferfaceElements.xsd
 	- NCIHistory.xsd
 	- TerminologyServiceMetadata.xsd
   		1) Updated schema references to refer to the 2006 model.
   		2) References to localName changed to localId.
  	- InterfaceElements.xsd
    	1) Changed ExtensionDescription.extensionProvider a complextype lgCommon:source
 
-2.0Beta4 (2/15/2007)
    ---------------------------------------------------------------------------
    Items of special interest
    ---------------------------------------------------------------------------
  - This build shares a common codebase with other LexBIG-based deliverables.
    Refer to items with '>' in the margin for work specifically targeted at
    caBIG requirements or areas of interest. 
    ---------------------------------------------------------------------------
    Configuration & Logging (* items indicate potential action items)
    ---------------------------------------------------------------------------
    ---------------------------------------------------------------------------
    Implementation & Test (* items indicate potential impact to code)
    ---------------------------------------------------------------------------
> - When getting a CodedNodeGraph from the service, it is possible to specify
    the name of a relations container to scope the graph results.  If no
    container is specified, relations tagged as 'native' (defined by the code
    system authors) are searched.  However, the implementation was only
    allowing for definition of a single 'native' relations container.  This
    was changed to honor multiple native containers, if available.
    ---------------------------------------------------------------------------
    Load and Export
    ---------------------------------------------------------------------------
> - For load of the NCI Thesaurus OWL, roles and associations are partitioned
    into their own containers; both are defined as native relation containers.
    Separate query of roles or associations can be performed by specifying
    either 'roles' or 'associations' as the relations name when acquiring the
    graph from the LexBIGService.  Combined query of all partitions is
    supported if the relations name is left unspecified.
  - Fixed Bugzilla Bug 381 that causes RadLex graphs to look funny. Explicit 
    parent child relationship is being ignored, but the implicit parent child 
    relationship is being loaded as a hasSubtype relation.
  - Fixed bugzilla 389 reported against FMA where some concepts had no concept 
    properties. 
  - Corrected how the top thing is computed for OWL.
> - Association name of hasSubtype is now used instead of subClassOf in the 
    generic OWL loader to make it consistent with the NCI OWL loader.  
  - Fixed a bug in the Indexer metadata class that was causing metadata to get
    lost in certain multithreaded applications.
> - Reduced the amount of information that was being stored in the index -
    this cuts the index size down by approximately 50% - at the expense of ease
    of debugging.  There is a slight performance increase as well when indexing
    and searching large terminologies.  This is controlled by a software
    switch in the ResourceManager class (LuceneLoaderCode.storeLexBIGMinimum).
  - Added SPARQL queries to more accurately resolve the root nodes of OWL 
    ontologies.  Addtional jena jars to support this inclue arq.jar and
    concurrent.jar
    ---------------------------------------------------------------------------
    GUI
    ---------------------------------------------------------------------------
> - When displaying coded node sets, all associations are displayed for the
    selected item (distance of 1), regardless of the relations container that
    defines the association.  This provides for improved viewing of UMLS
    ontologies and the NCI Thesaurus, which partition relations by container.
    ---------------------------------------------------------------------------
    Modeling & Docs (* items indicate potential impact to code)
    ---------------------------------------------------------------------------
  - Updated vocabulary revision model to support generic properties for 
    value domain entries.
  - Additions to model:  
  - addPropertyToValueDomainType.property
  - addPropertyToValueDomainType.language
  - addPropertyToValueDomainType.presentationFormat
  - addPropertyToValueDomainType.source
  - addPropertyToValueDomainType.usageContext
  - addPropertyToValueDomainType.propertyQualifier
  - addPropertyToValueDomainType.text
  - addPropertyToValueDomainEntryType.property
  - addPropertyToValueDomainEntryType.language
  - addPropertyToValueDomainEntryType.presentationFormat
  - addPropertyToValueDomainEntryType.source
  - addPropertyToValueDomainEntryType.usageContext
  - addPropertyToValueDomainEntryType.propertyQualifier
  - addPropertyToValueDomainEntryType.text

-2.0Beta3 
    ---------------------------------------------------------------------------
    Configuration & Logging (* items indicate potential action items)
    ---------------------------------------------------------------------------
  * The configuration format has changed - you will need to manually transfer
    your configuration into the new format.
    ---------------------------------------------------------------------------
    Implementation & Test (* items indicate potential impact to code)
    ---------------------------------------------------------------------------
  - Added loader scripts and implementation for admin command line wrappers
    to allow convenient command line loading of FMA and Radlex vocabularies.
  * Fixed a bug in how Property Qualifiers were being returned - the property
	qualifier id's and property qualifier content were being reversed when
	the return object was populated.
  - Added a new NameAndValueList convenience method constructor.
  - Fixed a bug that occurred if an index was missing, and you tried to run
	the rebuild index command to rebuild the missing index.
  - Upped the maxClauseCount to 16000 to prevent errors when querying for
	.*cell.* in some obo terminologies.
  * Corrected the behavior of hasNext() on the 
	ResolvedConceptReferencesIterator when Filters are applied.
	Previously, it could potentially return 'true' when it should have
	returned false if filters were applied.
  - (Re)fixed to make it properly show as many root (or leaf) nodes as
	it finds if it hits a limit, rather than returning none.
  - Fixed the clean up utility so it doesn't suggest removing the metadata
	index.
  - Added an (optional) API method logger - when enabled, this will print
	each public API method that was called along with the given parameters.
  - Updates to SQL Connection failure handling code to make it more robust.  
  - Added support for the match algorithm 'RegExp' when searching
	'conceptCode' properties.
  * Added the missing 'RegExp' type to the MatchAlgorithms class
  - Fix for LexBIG bug number 3629 (problems with newer mysql drivers)
  - Fix for LexBIG bug number 3686 (problems with querying and viewing
	association qualifiers)
  - Fix for LexBIG bug number 3545 (toString issue)
  - Fixed a bug where indexes weren't being properly reopened after a reindex
	operation took place.
  * Made a change to the RegExp query algorithm - previously, regular
	expressions were evaluated against the tokenized, lowercased property
	value.  Now, they are evaluated against the untokenized lower cased
	property value.  In practice, this change means that you should think of
	your regular expressions as being evaluated against the entire property
	value, rather than against individual words inside the property value.
  * Regular expressions against "conceptCode" will now evaluate against the 
	lower cased value of the concept code, rather than the case sensitive
	value of concept code. This makes the Regular Expression behavior the 
	same for concept codes as it is for property values.  
  - Fixed a bug that occurred if you asked for a graph of depth one - and you 
  	had two nodes that were linked to each other with different relationships, 
  	it would return things to a depth of 2 instead of one.	
  * API updates:
	  - Changed the way you specify active / inactive codes - removed from
		constructor, added a new RestrictToStatus(...) method.
	  - Changed the RestrictTo*Properties(...) methods - added
		another parameter that allows you to restrict based on the type of
		property, rather than the property name.  
	  - Changed the way that you do a status restriction.
		Previously, it was a hack on the RestrictToMatchingProperties
		method - now it is done through the RestrictToStatus method.
	  - Changed all of the resolve*(...) methods (codedNodeSet and Graph) - an 
		additional parameter was added that allows you to restrict which
		presentations are returned by type.
	  - Added a get(start, end) method to the 
		ResolvedConceptReferencesIterator. LexBIO feature request 323.
    ---------------------------------------------------------------------------
    Load and Export
    ---------------------------------------------------------------------------
  - Added FMA Loader.
  - Added generic OWL loader.
  - Fixed a problem generating SQL indexes with newer MySQL drivers.
  * NCI Metathesaurus loader changed as requested in LexBIG feature request 
    #3983.  This changes how individual source concept codes are stored. Source
    codes are now stored as qualifiers on individual presentations, rather than
    properties on concepts. See the feature request for more details.
  * The NCI Owl loader was changed so that all of the provided property names
	(such as dDEFININITION, synonym, etc) are now preserved in the
	"propertyName" field.  Previously, the property name field was reserved
	for "textualPresentation", "definition", etc.  
  - Added a warning and a hard coded value for code system version if one
	is not read from the UMLS RRF file.
  - Fixed a bug in how files are being read out of a jar file using a
	classloader that occurred when running inside of JBoss (this was 
	affecting the generic owl loader)
  * Remove PropertyLinks for synonyms in the OBO Loader
  - Eliminate duplicate Source information in OBO Terms
  - Add supportedProperties information for the OBO Loads
  - Fix parsing of relationship targets for the OBO loader
    ---------------------------------------------------------------------------
    Modeling & Docs (* items indicate potential impact to code)
    ---------------------------------------------------------------------------
    ---------------------------------------------------------------------------
    GUI
    ---------------------------------------------------------------------------
  - Added the ability to restrict on property qualifiers.
	minor corrections and cleanup of display of sources, property qualifiers.
  - Added a viewer for code system details (double click on a code system)
  - Fixed a bug that was populating the sources and qualifiers lists
	incorrectly when code systems were unioned.
  - Added a box to set maximum nodes to resolve in graph.
  - Organized the load and export menus
  - Fixed a bug that caused a GUI crash when multiple association
	restrictions were attempted.
  - Fixed a bug that threw errors when displaying instructions.
  - Added support restricting to the special properties 'conceptCode' and
	'conceptStatus'
  - Added ability to remove all admin functions from display with command
	line parameter
  - Added support for new API calls in LexBIG API.
  - Removed calls to deprecated methods.

-1.0.1 10/30/06
    ---------------------------------------------------------------------------
    Configuration & Logging (* items indicate potential action items)
    ---------------------------------------------------------------------------
  * Be sure to backup and restore the config.props file if overwriting a
    previous installation.
    ---------------------------------------------------------------------------
    Implementation & Test (* items indicate potential impact to code)
    ---------------------------------------------------------------------------
  - Updates to make storage to mySQL be case sensitive and prevent Collation
    issues.
  - Added a RegExp match algorithm.  Supports full regular expression queries.
  - Implementation of enhancements to the metadata API (load, remove, list).
  - Added JUnit tests for the MetaData load/remove/search api.
  * New signature for restrictToMatchingDesignations - now you can specify
    preferred, non-preferred or all types of designations.  The old
    restrictToMatchingDesignations (which only has a true/false flag for
    preferred only) was deprecated but will continue to be supported in the
    1.0 release.
  - Fix to return as many root nodes as are found, up to the limit set in the
    config.props, if the limit is exceeded (rather than returning none).
  - Improvements to config.props file parser to improve error messages when
    encountering a missing required variable.
  - Updates to help prevent running out of table identifiers in single DB Mode.
    Previously, several problems were likely if you loaded more than 936
    terminologies into an environment. Now, you can load an infinite number
    of terminologies - but a max of 936 at one time.  MultiDB Mode does not
    have any limitation on the number of terminologies.
  - Changed the way that we build tables on MySQL - we now set the charset
    and the collation on each table as it is built.  The new collation is a
    case-sensitive collation - so MySQL will now be case sensitive.
    Also, booleans on MySQL were changed to a tinyint(1) type, with 0 being
    false, and 1 being true.
    ---------------------------------------------------------------------------
    Load and Export
    ---------------------------------------------------------------------------
  - Fix to NCI Thesaurus load to always carry forward rdf id as CONCEPT_NAME
    property regardless of OWL version ('by name' or 'by code').
  - Consistent tagging of CodedEntry isActive flag based on deprecated class
    definition on NCI Thesaurus load ('by name' or 'by code' version).
    ---------------------------------------------------------------------------
    Modeling & Docs (* items indicate potential impact to code)
    ---------------------------------------------------------------------------
  - Enhancements to the metadata API (load, remove, list)
  - A new signature was added for restrictToMatchingDesignations.
  - Refresh of Programmer Guide/Technical Reference (version 1.0.1)
  - Refresh of Administrator's Guide/Install Reference (version 1.0.1)
  - Improvements to the documentation in the config.props file.
    ---------------------------------------------------------------------------
    GUI
    ---------------------------------------------------------------------------
  - Basic GUI documentation added to the Administrator and Programmer Guides.
  
- 1.0.0 (10/03/06) 
    ---------------------------------------------------------------------------
    Configuration & Logging (* items indicate potential action items)
    ---------------------------------------------------------------------------
  * Be sure to backup and restore the config.props file if overwriting a
    previous installation.
    ---------------------------------------------------------------------------
    Implementation (* items indicate potential impact to code)
    ---------------------------------------------------------------------------
  - Fixed a bug (regression) that occurred if you provided a language
    restriction to a CodedNodeSet.  A regression test wsa added to prevent
    future recurrence.
  - Implemented the Filter interfaces.
  - Implemented the isIncomplete() method on the ResolvedConceptReferenceList
    as originally intended.  This flag is set whenever a user or system limit
    removes potential results from the list being returned.
  - Added automated stemming support to the system-built lucene indexes.  A
    corresponding match algorithm ("StemmedLuceneQuery") can be used to
    take advantage of this support on query operations.
  - Removed the NormalizedLuceneQuery match algorithm.
  - Added support to index coding scheme metadata (key/value information) and
    perform centralized queries across metadata for all registered schemes.
    This support is currently integrated into the OBO loader, and is
    anticipated to be enhanced in when we move to the 2006 version of the
    LexGrid model (which supports assignment of arbitrary metadata properties
    to coding schemes).
    ---------------------------------------------------------------------------
    Load and Export
    ---------------------------------------------------------------------------
  - A new NCI Thesaurus loader was included in this build.  This version 
    includes the ability to read recent NCI format changes.  While backward
    compatibility is generally supported, it is recommended to load the
    most recent 'by code' version from the NCI GForge site (currently
    Thesaurus-ByCode-060905 at http://gforge.nci.nih.gov/frs/?group_id=14).
    Changes include:
    o Load of concept properties whether described by full name or
      meaningless code.
    o Load and registration of enumerated datatypes (e.g. Semantic Net).
    o Import of role/association information from self-defining concepts
      ('R' types); codes for related 'C' concepts are included as an
      additional property if available.
    o Recognition of changes to concept status registration and similar
      changes to recognize and carry forward property and qualifier names
      from the OWL source.
    o Preliminary support for registering 'role group' information as
      anonymous concepts.
    o Support to distinguish Thesaurus roles from associations.  The process
      for determining whether a LexGrid association with a given name
      represents a NCI role or association is a two step process.  First the
      association name must be resolved to a matching concept code.  Second,
      the concept must be evaluated to see if it is a subtype of the OWL
      ObjectProperty or AnnotationProperty types. For example, the code 'R79'
      (which represents the role with name 'EO_Models_Human_Disease') is
      registered as a subclass of the 'owl:ObjectProperty' association in the
      LexGrid model.  In comparison, NCI Thesaurus associations such as 'A1'
      (representing 'Role_Has_Domain') are registered as a subclass of both
      the 'owl:ObjectProperty' and 'owl:AnnotationProperty' association in
      the LexGrid model.

- 1.0.0rc9 (09/22/06)  
    ---------------------------------------------------------------------------
    Configuration & Logging (* items indicate potential action items)
    ---------------------------------------------------------------------------
  * Be sure to backup and restore the config.props file if overwriting a
    previous installation.
    ---------------------------------------------------------------------------
    Modeling & Docs (* items indicate potential impact to code)
    ---------------------------------------------------------------------------
  - API changes and enhancements were made to support pre-calculation
    and navigation of relations from end nodes.  Changes are anticipated to
    impact only the internal implementation at this time (e.g. specific
    loader implementations).
  - JavaDoc (with embedded model diagrams) is now generated for fewer classes.
    Classes still included are the API interfaces, the data model, and select
    public classes that are anticipated to be referenced by programmers.
    Other implementation classes were dropped to reduce the size of the build
    package and the 'noise' introduced when navigating the docs. Source is
    still distributed for all code.
    ---------------------------------------------------------------------------
    Implementation (* items indicate potential impact to code)
    ---------------------------------------------------------------------------
  - Added the ability to have multiple jar file locations in the config file 
    (see updated documentation in config file).
  - Implemented the Extension Registry.  Changes were made to internal code
    to now use the extension registry where appropriate (sorts, etc).
    Programmers now have the ability to dynamically register load, export,
    generic, and sort extensions.
  - Improved handling of relations to support pre-calculation and retrieval
    of ending leaf nodes.  This is intended to improve the ability to load and
    work with relations that are naturally (or solely) navigable from leaf to
    root (e.g. part-of, is-a).  Changes affected specific loaders, convenience
    methods, and the internal implementation of CodedNodeGraph.
  - Bug fixes for handling of coding scheme local names on codedNodeGraph
    operations.
  - CodedNodeSet restrictToProperties method now supports the 'contains'
    match algorithm when restricting to conceptCodes.
  - The colon ':' character is no longer treated as a white space character
    for the purposes of indexing.  Previously, the string 'GO:4562' would
    have been indexed as two separate words - 'GO' and '4562'.  Now it is
    indexed as 'GO:4562'.
  - Various other minor bug fixes.
    ---------------------------------------------------------------------------
    Load and Export
    ---------------------------------------------------------------------------
  - Changed the UMLS and MetaThesaurus loaders so that the UMLS version number 
    is now parsed from the MRDOC file, rather than the RELEASE_INFO file.  
    RELEASE_INFO is no longer required.
  - Removed code from the NCI Thesaurus loader that caused a regression error
    in the rc8 release (a junit test was also added to the bvt bucket for
    future verification).  Changes to accomodate alternate role/association
    identification and recent format changes (self-defining concepts for
    documenting roles/properties/etc, changed specification of retired
    concepts, ability to load 'by-code' version of source, etc) are not yet
    included but are being combined into a new version of the loader.
  - Added http:// URI support to the OBO loader (previously you had to have
    the obo files local on the machine, now you can just point to a web url
    to load obo content).
  - Bug fixes to all loaders to prevent them from leaving orphaned resources
    behind in certain failure cases.
  - XML and OBO Exporters now honor the overwrite flag correctly.
  - Fixed a threading problem in the Indexer affecting read/write of metadata.
  - Import of LexGrid XML format was 'relaxed' to allow import of content
    defined to the 2006 LexGrid model.  Note that currently this results in
    some potential loss of information as content is automatically
    'down-converted' to the 2005 LexGrid model.  Additional support for the
    2006 model is forthcoming.
    ---------------------------------------------------------------------------
    GUI
    ---------------------------------------------------------------------------
  - General improvements to user interaction.
  - Better handling of OBO reversed graphs.
  - Minor bug fixes.
    
- 1.0.0rc8 (09/01/06)
    ---------------------------------------------------------------------------
    Configuration & Logging (* items indicate potential action items)
    ---------------------------------------------------------------------------
  * The Transfer utility has been delivered for evaluation.  This is to provide
    semi-automated instructions and help in tweaking registration files when
    moving LexBIG software and content between systems.  You can access this
    function using the TransferScheme admin script (command-line driven).
    Please evaluated and send feedback.
  * Be sure to backup and restore the config.props file if overwriting a
    previous installation.
    ---------------------------------------------------------------------------
    Modeling (* items indicate potential impact to code)
    ---------------------------------------------------------------------------
  - Changes were made to the model in several areas; focus on completion of
    portions of the API that were accounted for but incomplete.  The intent
    was to harden the API as much as possible in this release, and is now
    considered frozen (for deployment timeframe) barring critical fixes ...
  * An extension mechanism was formalized for registration of externally
    packaged extensions.  This includes filter/match, sort, load, export,
    index, and general (application-specific) extensions.  In addition,
    specific API modifications were put in place to incorporate provided
    extensions into the runtime framework.
  * Exporters were specifically introduced in this iteration (counterpart
    to loaders).
  * In addition to the JavaDoc UML, a full publication of the model will
    and associated schema will be posted to the informatics.mayo.edu site by
    end of day (http://informatics.mayo.edu/LexGrid/index.php?page=lexex).
  * Some package and signature changes were introduced due to this work.
    For the most part changes did not affect typical queries.  However,
    there was a change made to the CodedNodeGraph restrictToAssociation()
    and areCodesRelated() methods to prevent overloading of the
    ConceptReference structure.
  * Signature for the getTopNodes() convenience method was changed to allow
    for top node processing of associations other than 'hasSubtype' that
    have a natural ordering.  To take advantage of this, root nodes must be
    pre-calculated for the relation in question.  This will be done during
    the load for well-known associations, but can also be introduced after
    the fact as required (e.g. similar to introducing an index on a database
    table).  This is invoked in the API through the addRootRelationNodes()
    method on LexBIGServiceManager.
    ---------------------------------------------------------------------------
    Implementation (* items indicate potential impact to code)
    ---------------------------------------------------------------------------
  - Generalized solution for distinguishing Thesaurus roles and associations is
    in progress but only partially implemented.  Roles are now grouped under
    owl:ObjectProperty and Associations under owl:AnnotationType but not all
    dots have been connected yet to make it useful.
  - Added UMLS Loaders (an UMLS vocabulary can now be loaded to the LexGrid
    repository and automatically registered/indexed for use by the LexBIG API.
    Content can be loaded from a UMLS relational database or RRF files.
  - Added support to export loaded vocabularies to LexGrid XML format.
  - Added load, validation, and export support for OBO vocabularies
    (introduced for NCBO, but merged to common codebase).
  - A loader was also introduced to import the latest version of the UMLS
    SemanticNet (note: by specification of URI, the content can be streamed
    directly from the NLM web site).  This is intended as the strategic
    solution to enable the getSemanticTypes() Metaphrase method.
  - Added missing version numbers to resolved AssociatedConcept references.
  - Updated the Lucene indexing doc to account for property qualifiers, sources,
    and usageContexts.
  - Improved root node calulation updates for increased flexibility.
  * Implemented addRootRelationNodes() method (per model notes above).
  - Tweaked the root node relation generator so that it doesn't generate
    relations if a provided association name is unused in the loaded data.
  - Fixed a bug where it wasn't sorting the top level of a graph when requested.
  * API will now throw an exception if you request top nodes on an association
    that doesn't have top nodes calculated.
  * Added convenience method to get scheme rendering details (version, etc).
    ---------------------------------------------------------------------------
    GUI
    ---------------------------------------------------------------------------
  - Beta History Browser added!  Please evaluate and send feedback.
  - Added confirmation dialog on re-index operations.
  - Fixed to make it deal with patched in (unregistered) code systems better
  - Sorted the code systems in the list.
  - Disabled all admin functions by default - added a toggle for admin functions.
  - Integrated support for additional loaders and exporters (UMLS, LexGrid, OBO).
  - Added button to remove history.
    ---------------------------------------------------------------------------
    Command line utilities
    ---------------------------------------------------------------------------
  - Command line support for additional loaders & exporters (UMLS, LexGrid, OBO).
  - Command line support for transfer utility (see above).
  - Misc cleanup for consist specification of params across commands.
  - SemanticNet files are now loaded as part of Example content (should provide
    for more complete demonstration in some cases compared to the truncated OWL
    file, though the OWL is still retained).

- 1.0.0rc7 (08/18/06)
    -----------------------------------------------------------------
    Configuration & Logging (* items indicate potential action items)
    -----------------------------------------------------------------
  * Be sure to backup and restore the config.props file if overwriting
    a previous installation.
  * Following installation of this build, all indexes should be rebuilt for
    pre-existing content.  This can be done via the GUI interface or the
    RebuildIndex admin script.
    ---------------------------------------------------------------------
    Modeling & Implementation (* items indicate potential impact to code)
    ---------------------------------------------------------------------
  * Change to CodedNodeGraph->toNodeList method to allow greater control
    in restricting the scope and number of returned nodes.
  * Minor changes were made to the signatures of some convenience methods.
  * Method numberRemaining() added to ResolvedConceptReferencesIterator.
  - Phonetix replaced by Apache Commons Codec for handling of the
    DoubleMetaphoneLuceneQuery match algorithm.
  - Added default constructors for reference by Apache Axis in preparation
    for support of web services.
  - Added support to search on property sources, usageContexts and qualifiers
    (see restrictToMatchingProperties() and restrictToProperties() on
    CodedNodeSet.
  - Added code system version to ResolvedConceptReference.
  - Misc fixes to metathesaurus load process (REL and RELA data included to
    support retrieval of e.g. narrower_than/broader_than/related_to relations
    in addition to more specific relationships specified by various sources).
  - Misc fixes for database support (e.g. getting the default relationship
    on postgreSQL).
    ---
    GUI
    ---
  - Added support for restricting properties based on source or context.
  - Added admin function for clearing orphaned resources.
  - Added confirmation when admin function is selected to remove a vocabulary.
  - Added number remaining count to result browser.
  - Improved relationship visualization (e.g. representation of multiple
    relations between same concept nodes, added [Ctrl]'+' and [Ctrl]'-' to
    push nodes in the graph viewer closer and farther apart).
  - No longer required to set code system for focus node in graph view.
  - Misc updates for usability (e.g. all drop down losts are sorted),
    performance, and bug fixes.
    ----------------------
    Command line utilities
    ----------------------
  - New example provided of scoring algorithm.

- 1.0.0rc6 (07/20/06)
    -----------------------------------------------------------------
    Configuration & Logging (* items indicate potential action items)
    -----------------------------------------------------------------
  - Severe startup failures now print to System.out and System.err
    (instead of just System.err).
  - Changed the system Variable 'PropFileLocation' to 'LG_CONFIG_FILE' for
    consistency.
  - Added an optional System Variable and a config.props option of
    'LG_BASE_PATH'.  When provided, relative paths are resolved relative to
    this value.
    ------------------------------------------------------------------------
    API & model changes (* items indicate potential impact to existing code)
    ------------------------------------------------------------------------
  - Changed the behavior if you try to restrict a CodedNodeGraph to an empty
    CodedNodeSet.  Previously it threw an exception if you did this.
    Now, you will just get an empty CodedNodeGraph as a result.
    ---
    GUI
    ---
  - Support has been added for query and visualization of CodedNodeGraphs.
  - Visualization of immediate relations has also been added for selected
    items in resolved CodedNodeSets.
  - Support to join CodedNodeSets via union, intersection, and difference.
  - Support to join CodedNodeGraphs via union and intersection.
  - Support to restrict resolution of CodedNodeGraphs to the source codes,
    target codes, or all codes as defined by a simple or joined node set.
  - Support to restrict resolution of CodedNodeGraphs by associations, 
    code system, source code system, and target code system.
  - Several features and visual effects are available in the graph views:
    * Point and click navigation across nodes.
    * Zoom in/out (hold right mouse button and scroll up or down)
    * Zoom to fit (right click on blank area)
    * Ctrl + 1, Ctrl + 2, Ctrl + 3, Ctrl + 4 (rotate view)
    * Move graph (click and drag on blank area)
  - Several minor tweaks and modifications.
    ----------------------
    Command line utilities
    ----------------------
  - Admin scripts that work against a previously loaded code system (e.g.
    ActivateScheme, DeactivateScheme, RemoveScheme, etc) will now present
    a list and prompt the user for a target coding scheme if the command
    parameter for urn and/or version is not provided or incorrect.
    ------------
    Build & Test
    ------------
  - Minor updates were introduced to the Automobiles and German Made Parts
    sample terminologies.
  - Starting with this release, all source files and supporting materials
    will be placed in the GForge CVS repository (host cbiocvs2.nci.nih.gov)
    at path /share/content/gforge/lexbig.  This location is maintained as
    a mirror to the primary development repository (which is hosted on
    internal servers at the Mayo Clinic).  Files will be refreshed at each
    milestone release.
  - An ant script has also been committed to allow build of a complete
    LexBIG installer from the CVS file structure.
    ---------
    Bug fixes
    ---------
  - Minor fixes for resolving coded node sets created from graphs.
        
- 1.0.0rc5 (06/26/06)
    -------------
    Configuration
    -------------
  ! Configuration options have changed.  Most notably, we have changed the way
    relative file paths are resolved from the config file.  If you provide a
    relative path (e.g. when specifying the location of the registry or index),
    the path is now resolved relative to the location of the config.props file.
    This is a departure from previous versions, where relative paths were
    resolved relative to the JVM launch point.
  - The JAR_FILE_LOCATION option was added to specify the folder where extra
    jar files can be located and integrated into the runtime (primarily for sql
    drivers at this point).
  - By default, log files are now centralized to the /logs subdirectory of the
    root installation path.  Before logs would potentially end up in multiple
    locations, relative to jvm launch point.  This is also configurable in the
    config.props file.
  - Refer to comments in the config.props for additional detail on these
    changes.
    ------------------------------------------------------------------------
    API & model changes (* items indicate potential impact to existing code)
    ------------------------------------------------------------------------
  * All load functions can now be performed in a synchronous or asynchronous
    manner.  This is provided by an extra parameter (async flag) on each of
    the load methods.  Code directly invoking the load methods will need to be
    updated to specify this new parameter.
  * Reindex API methods were moved from the LexBIGServiceManager to the
    IndexLoader class.  This change allows for async/sync invocation and
    retrieval of status similar to basic load functions.  Refer to source for
    the RebuildIndex.java admin class for an example of how to access and
    invoke the new API.
  * Log level has been restricted, with possible values now enumerated by the
    LogLevel class.  In addition, requests for log messages from the loader
    now accept a new log level parameter, allowing for returned values to be
    constrained to errors, warnings, etc.
  - The LoadStatus object has been enhanced to include an overall indication
    of process state (e.g. pending, processing, failed, completed).  Possible
    values are provided by the ProcessState class.
  * Support was added to accomodate ascending and descending sort options.
    This will impact existing code that calls the resolve* methods on the
    CodedNodeGraph and CodedNodeSet classes.  Sort options will be passed
    as a SortOptionsList rather than a LocalNameList.  The Constructors
    class now contains convenience methods to quickly instantiate lists of
    SortOptions; please substitute for LocalNameList as appropriate.
    Ascending and descending sort constants are defined by the SortOptions
    class.
  * Use of sort algorithms is now enforced for context sensitivity.  The
    definition of each sort algorithm (via an extension description) has
    been extended to allow restrictions on usage context.  For example, some
    algorithms can be applied when resolving coded node sets but not coded
    node graphs.  As a result, sort algorithms can now be looked up
    according to context on the LexBIGServiceManager (possible contexts are
    defined by the SortContext class).
  - Method was added to service manager allowing removal of history service.
  - New isCodeRetired() convenience method was added.
  - New methods were added to the HistoryService to resolve change events
    related to code descendants and ancestors.
    -----------------
    GUI (!!! NEW !!!)
    -----------------
  - A new Graphical User Interface has been introduced in this release to
    view configuration options, perform administration tasks, and exercise
    the API.
  - Represents work in progress but currently quite functional.  Note that
    configuration options are intended to be read-only at this point.
  - Launch by selecting the appropriate executable from the /gui folder.
    ----------------------
    Command line utilities
    ----------------------
  - Examples are no longer constrained to work against the NCI Thesaurus,
    and instead now interactively prompt for an available coding scheme.
    However, testing of the examples against some loaded sources (e.g. OBO)
    are incomplete at this point.
    ----------------
    Bug fixes for...
    ----------------
  - Null pointer in the registry when updating scheme tags.
  - Entity descriptions not populated when resolving to an iterator.
  - CodedNodeSet Iterators not properly cleaned up.
  - CPU wasted by frequent polling following code system disablement.
  - Fixed bug returning inactive codes when querying for active codes with
    no other restrictions.
  - Several problems fixed in the OBO load process...
    Handled duplicate SupportedSource information properly.
    Removed property links for synonyms.
    Added degreeOfFidelity information in presentation for synonyms.

- 1.0.0rc4 (06/02/06)
  - This was primarily a maintenance release.
  - Tweaks and fixes were incorporated for database support, including
    additional Oracle and DB2 support and resource cleanup.
  - Additional database configuration options were introduced.  Support was
    added for 'single database' mode, where multiple coding scheme versions
    can be loaded to the same database instance.  Support was also introduced
    to use add an administrator-specified prefix to generated table names.
    Refer to comments in the config.props file for more information.
    Note: multi-database mode remains the default for LexBIG environments.
  - Many 3rd party software components used by the runtime were updated to
    their most recent level (e.g. Apache Commons, Log4j, Lucene, Eclipse EMF,
    Sun activation & mail, etc). This resulted in minor changes to the code.
    License terms and conditions were updated to match these new versions as
    required, though in general terms remain consistent with prior releases.
  - Automated junit tests were reorganized and divided as appropriate to
    meet different test requirements.  The build verification test (bvt)
    bucket was refactored to serve as the most direct and performant way to
    act as a 'sanity check' for the installed code in the installed
    environment.  More extensive functional tests were packaged with the
    lbImpl codebase in order to to make it easy to introduce more extensive
    functional tests and multiple server configurations.
  - Classloader changes.  This has an immediate impact on the packaging of
    database drivers.  JDBC drivers are now decoupled from the runtime and
    shipped in the /runtime/sqlDrivers directory.  This allows for system
    administrators to introduce new drivers, or update existing drivers
    without requiring a new build.  Similar classloader changes are
    being introduced to prepare for the introduction of externally-packaged
    extensions (match algorithms, sort algorithms, etc).
  - Fixes were made when loading the NCI Thesaurus to prevent duplicate
    entries of relation concepts under both 'R' and 'C' codes, and to
    handle cases where upper/lower case was inconsistently used in the
    source in defining the concept and role entries.
  - Additional support was added to the MetaThesaurus loader (e.g. added
    two new localNames and the loader now returns a list of tables loaded).
  - Miscellaneous bug fixes and code cleanup.

- 1.0.0rc3 (05/05/06)
  - Added preliminary support for import of OBO vocabularies.
  - Fixed bug #841 (populate conceptReference in associations).
  - Fixed bug #842 (bi-di checks of areCodesRelated on symmetric assoc).
  - Fixed bug #843 (sorting of coded node graph).
  - Fixed parts 1 and 3 of bug #1032.
  - Allow anything as a registered name, instead of just urn:oid's.
  - Fixed additional bugs in coded node graph that occurred when there were
    relationships to concepts that don't exist.
  - Added a CleanUpUtility class and corresponding 'ClearOrphanedRelations'
    admin script to assist with the removal of unused / orphaned indexes and
    databases.
  - Fixed a bug in codeToName and nameToCode (not working with inactive codes).
  - Made the codeToName and nameToCode methods fall back onto preferred
    designation matching (if there is no CONCEPT_NAME property).
    
- 1.0.0rc2 (04/04/06)
  - Updates to fix / prevent issues when doing concurrent operations on a single terminology.
  - Improved debugging messages on startup / configuration failure.
  
- 1.0.0rc1 (03/31/06)
  - Changes were introduced to reduce the memory footprint during load of the
    NCI Metathesaurus.
  - Logging enhancements were introduced to control rollover based on time
    period (daily, weekly, monthly) or size.  In addition, e-mail notification
    is provided for logged errors and warnings.  Control is also provided
    for cleanup of inactive logs. Refer to the Installation and Admin Guide
    for instructions on how to configure this support.
  - Final versions of the Install/Admin and Programmer Guides were introduced.
  - An additional option was added to the ListSchemes administration script
    to provide basic or full output.  The basic mode (default) provides
    improved readability.
  - An additional example program was added to perform a "sounds like" match
    against the sample vocabulary (or production NCI Thesaurus, if loaded).
  - Standard file paths and spaces are now allowed on Load scripts.
  - Fixes to NCI OWL conversion to accomodate 'd' properties and other fixes.
  - Indication of elapsed time added to ProfileScheme script.
  - Fixed a threading issue (bug # 963).
  - Other misc bug fixes and enhancements, per tracked GForge items.

- 0.9.0b (03/24/06)
  - PropertyQualifier support was completed and tested, per NCI criteria.
    Currently 'source-code' is the only qualifier detected (that was not
    already incorporated in other parts of the model), but solution is
    generic and should detect/register additional qualifiers if introduced.
  - Admin scripts were updated to accomodate increased heap sizes required for
    load of the MetaThesaurus.  Current sizes are anticipated to be large
    enough, but may be adjusted further based on the results of ongoing
    NCI and Mayo test efforts.  Code was also updated to reduce memory
    consumption and avoid generation of duplicate keys during MetaThesaurus
    load.
  - Revision to Admin/Install guide (Admin guide now considered complete other
    than perhaps minor touchup; final revision of both Admin and Programmer
    guides scheduled for next week).
  - Additional infrastructure support for enhanced administrative control
    over log file retention and rollover criteria.  This will be fully exposed
    and configurable in the 1.0 build next week.
  - Introduced the ProfileScheme admin script.  This currently provides basic
    information regarding number of loaded concepts and relation depth, and
    will be further enhanced in the next build.
  - Additional logging and formatting enhancements for admin scripts.
  - Misc bug fixes and enhancements, as a result of feedback and ongoing
    test activities.

- 0.8.0b (03/17/06)
  - Added support for scheduled deactivation.
  - Added history method to support 'batch' queries for a system release.
  - Added property qualifier support to the implementation (individual loaders
    still need to populate the values into the db; additional clarification
    from NCI also required regarding inclusion in OWL source).
  - Added support for cumulative patch jar to all admin and test scripts, to
    support fixes without requiring full build.
  - Additional tests introduced to automated build verification suite.
  - Performance improvements when populating coded entries.
  - Fixed bug 813 (wrong size on returned property array in a concept).
  - Fixed bug in the coded node set iterator that would have held the JVM
    open if trying to shutdown the JVM without closing all iterators.
  - Fixed bug in MetaThesaurus load that allowed the same code system to be
    loaded more than once.
  - Fixed bug in the MetaThesaurus loader that was chopping the version
    string off at 10 chars instead of 50. 
  - Fixed bugs with timestamps in the registry file.  Activate wasn't clearing
    the deactivate time when it should and lastUpdated timestamps were
    not being set.
  - Bug fixes in coding scheme version tagging (correcting cache issues)

- 0.7.0b (03/10/06)
  - Updated admin-install guide
  - Added version numbers to all jar files.
  - Updated Lucene to 1.9.1 final.
  - More general code clean up (removal of compiler warnings, etc).
  - NCI MetaThesaurus converter bug fixes (clashing property qualifier unique ids, missing 
    registered names, incorrect loading of some relationships)
  - Performance improvements on the MetaThesaurus load (about 20% faster now).
  - Fixed bugs in SQL code where it was doing case sensitive comparisons where it shouldn't.
  - Modified the History API to better meet requirements.
  
- 0.6.0b (03/03/06)
  - NCI Thesaurus History API is now fully implemented; Mayo to follow-up
    with additional information via e-mail.
  - Programmer guide is included in /doc directory (80%).
  - Fixed code to throw an exception when an invalid tag is passed in
    (previously, it fell through to the PRODUCTION version.
  - Loader now issues an error message if a code system with an invalid
    registered name is loaded. Activate method will refuse to activate
    a code system with an invalid registered name.  (invalid names
    cause unexpected errors in the api)
  - Metathesaurus loader now checks for the existance of all required files
    (and validates their formats) on the validate step.
  - Bug fixes for running on Hypersonic database
  - Additional tests added to automated test bucket.
  - General code clean up (imports, compiler warnings, etc)

- 0.5.0b (02/28/06)
  - MetaThesaurus load
    Functionally complete; testing of all sub-processes complete; end-to-end
    test of full MetaThesaurus load in progress.
  - Transitivity is generally supported, but not currently supported
    for specific requests against the MetaThesaurus (areCodesRelated and
    listCodeRelationships) due to space requirements and computational
    intensity involved with producing the underlying database table.
  - Iterator support complete (CodedNodeSet)
  - Additional admin scripts and functionality introduced:
    LoadLgXML (Load vocabulary provided in LexGrid XML format)
    LoadNCIMeta (Load meta-thesaurus from RRF files in specified directory)
    LoadNCIHistory (Load concept history)
  - Support for content validation without load (implemented in API and
    externalized via admin scripts).
  - Introduced verification test suite, launched via /test/TestRunner script
    with several reporting options including plain text, xml, and html formats.
  - Introduction of build.properties file into install directory, providing
    product id/time/etc to aid serviceability
  - History load functions and model changes complete; remaining history
    functions on target for delivery in 3/10 milestone as scheduled.
  - Milestone draft of Admin Guide added to /doc directory (90% overall)
  - Milestone draft of Programmer Guide will be added in next drop
  - Misc bug fixes/enhancements (recorded via GForge bug/feature trackers)

- 0.4.0b (02/14/06)
  - Bug fixes and minor enhancements to CodedNodeSet/CodedNodeGraph
  - Additional convenience methods
    (see org.LexGrid.LexBIG.Utility.ConvenienceMethods)
  - Integration of code system load/index/activation/deactivation/removal
  - Simplified configuration (will be sent via e-mail and added to
    documentation)
  - Resolution of misc items from face to face meeting (NC_NAME changed
    to CONCEPT_NAME; remove NCI intermediate node in subtype tree; etc)
  - Introduction of tag assignment/functionality
  - Full transitivity (verified, but light on testing at this point)
  - Updated command line interfaces for administration
    (see /admin subdirectory for interfaces to load/activate/deactivate
    and list coding schemes, list extensions, rebuild indexes,
    and assign tags)
  - Resolve bug #438 (problem does not occur under new build; unclear of
    trigger for changed behavior).  Note that NCI environment still
    requires location of Java runtime.  It is recommended that users run
    the following command prior to executing any of the admin or
    example scripts:  export PATH=/usr/jdk1.5.0_04/bin:$PATH 

- 0.3.0a (01/30/06)
  - Cleanup of repository configuration and infrastructure code; groundwork for
    completion of load/index API implementation.
  - Relaxed version parameter specification when specifing a coding scheme
  - Additional coded node graph functionality (not 100%, but close to final)
  - Introduction of several convenience methods to API:
    see org.LexGrid.LexBIG.Utility.ConvenienceMethods
  - Transitivity support (limited; ability to walk down a node tree)
  - Significant speed improvements when converting/loading NCI OWL format
  - Improved programmer javadoc (/doc/javadoc subdirectory)
  - Minor build/package refactoring; component code is now consolidated and
    logically grouped as follows (/runtime-components subdirectory):
    * lbInterfaces.jar - LexBIG service-level interfaces
    * lbImpl.jar - Implementation of LexBIG services
    * lgModel.jar - LexGrid core data model
    * lbModel.jar - LexBIG data model extensions
    * lgUtility.jar - LexGrid core utilities (conversion, indexing, etc)
    * lbUtility.jar - Additional LexBIG utilities
    * lbExamples.jar - LexBIG example programs
    * lbTests.jar - For automated validation of LexBIG runtime
  - Individual source jars are now provided (/sources subdirectory)
  - Additional examples are included (refer to the /examples/readme.txt)
  - *** NOTE: IF YOU HAVE PREVIOUSLY POPULATED A MYSQL DATABASE WITH LEXBIG
        DATA ON THE PREVIOUS DROP, YOU WILL NEED TO DROP THE DATABASE AND
        CREATE A NEW/EMPTY ONE, AS THE DATABASE FORMAT HAS CHANGED.

- 0.2.0a (01/16/06)
  Additional function added to this release:
  - Database support
  - Increased function for coded node sets
  - Exception handling
  - Logging
  - Relations (limited)
  
  JavaDoc (see /doc/javadoc/index.html) has been enhanced to include
  navigable diagrams to assist with general readability.
  
  Selective install of LexBIG materials is now handled by a wizard-based
  install program, shipped as an executable jar file.  The installer is cross-
  platform, will scale to handle additional materials, and allows for future
  development of automated install scripts, desktop shortcuts, etc.

- 0.1.0a (12/08/05)
  Initial drop; work in progress.  The primary goal is to disclose public
  interfaces that will be used for direct Java invocation of LexBIG services.
  In addition, a partial implementation, datastore, and test program are
  included to provide context for using the interfaces.  The current demo is
  self contained (sample data is included; support for general load and
  query of user-specified vocabularies is not provided).

===============================================================================
