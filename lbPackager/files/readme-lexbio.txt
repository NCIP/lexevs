===============================================================================
Overview
===============================================================================
Provides materials created by Mayo Clinic in fulfillment of the LexGrid 
Vocabulary Services for NCBO� (LexBIO) project.

This product incorporates code and other materials developed for the LexGrid
Vocabulary Services for caBIG� (LexBIG) project.  The LexBIG service layer, as
referenced below, provides core application programming interfaces (APIs) and
tools used to access and maintain vocabulary content based on the LexGrid
model.  The LexBIG architecture is enhanced and extended as required to meet
specific needs of NCBO initiatives.  These changes will typically feed back
into the common codebase, but reflect specific work done in support of
core 1.  Default configuration options, etc, also typically differ from the
LexBIG packaging to better match anticipated deployment in NCBO environments.

===============================================================================
Getting Started
===============================================================================
- By default, vocabulary data is imported to a hypersonic (HSQL) database
  automatically created in directory C:/LexBIOData.  This location should be
  adjusted as desired and must be set to something else if running on Linux
  (e.g. /home/LexBIOData).  The database location, along with additional
  configuration settings, are controlled by the properties file
  {install-path}/resources/config/config.props.
  
  The configuration must also be changed to declare an alternate database
  location or prefix if multiple LexBIO nodes are installed.  Refer to
  instructions in the administration guide for additional information on
  configuration options.

- To perform installation/build verification tests, switch to the /test
  subdirectory and invoke the TestRunner script.  Enter TestRunner without
  parameters for available reporting options.  Note that the test suite
  is only available if the test framework has been installed.
  
- To perform an OBO test, load the sample 'cell' ontology by switching to
  the /admin directory and invoking the following commend (substituting
  the actual install-path for {install-path}):
  
  LoadOBO -in "file:///{install-path}/examples/resources/cell.obo" -nf -a
           -t "SAMPLE"
  
  After load the ontology should appear in the list of available coding
  schemes (use the 'ListSchemes' command).  An example program to query the
  loaded content can be run immediately via the '{install-path}/examples/
  GeneralSoundsLike' command.  Note that the other examples are still
  hard-coded to work with the NCI Thesuarus; this will be changed in a
  future build.
  
  Additional OBO content can be downloaded and added in a similar fashion.
  Once loaded, the content is available for full programmatic access via the
  LexBIG API (refer to the programmers guide and javadoc).
    
- Public API-level interfaces are available in the Javadoc.
  Refer to /doc/javadoc/index.html in the installation directory.

- For details on LexBIG administration and configuration, refer to the
  Administration Guide (LexBIG_Installation_Admin_Guide.pdf) in the /doc
  directory.
  
- For details on programming to the LexBIG API, refer to the
  Programmer Guide (LexBIG_Programmer_Guide.pdf) in the /doc directory and
  Javadoc in the /doc/javadoc directory.
  
===============================================================================
Additional Requirements
===============================================================================
Refer to the Installation and Administration Guide.

===============================================================================
Additional resources for LexBIO are available at:
===============================================================================
- The Mayo Informatics site
  (http://informatics.mayo.edu/LexGrid/index.php?page=lexbio)

===============================================================================
Additional resources for LexBIG are available at:
===============================================================================
- The caBIG CVS repository
  (http://cabigcvs.nci.nih.gov/viewcvs/viewcvs.cgi/lexgrid/)
 
  This repository is used to store materials related to project administration
  and contractual milestones.
 
- The caBIG GForge site
  (http://gforge.nci.nih.gov/projects/lexbig/)
 
  This site is used to disseminate code, formal models, and related
  documentation for the LexBIG project.  All software deliverables and related
  documentation are available for download at the GForge site.
  GForge is also used to monitor and track bugs, feature requests, and
  public discussions related to the LexBIG software.

- The Mayo Informatics site
  (http://informatics.mayo.edu/LexGrid/index.php?page=lexbig)

===============================================================================
Migrating from previous releases  (ignore if installing from scratch)
===============================================================================
- Migration is not generally supported over prior releases, but may be
  accomplished if necessary through a series of manual steps and updates to the
  configuration properties.  Please contact the development team if migration
  is required.  In general, we expect to provide support for N-1 migration once
  we have exited release candidate status to a 1.0.0(Final) version.
  
===============================================================================
Known issues
===============================================================================  
===============================================================================
Notes & Status
===============================================================================
-6.4 (1/19/2016)
	Removed Radlex Loader and its components. No longer needed.
	
-2.0Beta6 (4/20/2007)
    ---------------------------------------------------------------------------
    Items of special interest
    ---------------------------------------------------------------------------
  - This build shares a common codebase with other LexBIG-based deliverables.
    Refer to items with '>' in the margin for work specifically targeted at
    NCBO requirements or interests. 
    ---------------------------------------------------------------------------
    Load and Export
    ---------------------------------------------------------------------------
> - Updated the generic OWL loader implementation to remove dependies on the
    RDFDefaults.xml file.
> - Pull version information of an OWL ontology from the versionInfo field.    
> - Handle OBO intersection the same way we deal with OWL intersection.
> - Remove all association forward names and reverse names that is not part of 
    the ontology.
> - Modified the Protege loader for Radlex to bring in Synonym classes as concepts.
> - Modified the generic OWL loader to handle rdf lists correctly.
   ---------------------------------------------------------------------------
    Implementation & Test (* items indicate potential impact to code)
    ---------------------------------------------------------------------------
> - Added a convenience method getAssociationForwardAndReverseNames to get the 
    list of relations that are present in an ontology.
> - Added a new convenience method to getCodingSchemesWithSupportedAssociation
    to get a list on ontologies in which a perticular relation exists.    
  - Added JUnit tests to check the correctness of the new methods. 
      
    ---------------------------------------------------------------------------
    GUI
    ---------------------------------------------------------------------------
  - Modified the edge label to default to association_name when the forward_name
    of the association is null. Edge label is set to [R]association_name when 
    reverse_name of the association is null.
    ---------------------------------------------------------------------------
    Modeling & Docs (* items indicate potential impact to code)
    ---------------------------------------------------------------------------
 
 



-2.0Beta5 (2/28/2007)
    ---------------------------------------------------------------------------
    Items of special interest
    ---------------------------------------------------------------------------
  - This build shares a common codebase with other LexBIG-based deliverables.
    Refer to items with '>' in the margin for work specifically targeted at
    NCBO requirements or interests. 
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
> - Updated RDFDefaults.xml file to allow proper loading of additional OWL
    terminologies released through BIOPortal (e.g. snpontology_full.owl); note
    future direction is to remove dependency on this file entirely.
> - Fixed RDF/OWL load to ensure that for every new property created, content
    is never assigned to database as empty string (resulted in Oracle problem).
> - Minor changes to RDF/OWL processing (e.g. made a preferred presentation
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
    NCBO requirements or interests. 
    ---------------------------------------------------------------------------
    Configuration & Logging (* items indicate potential action items)
    ---------------------------------------------------------------------------
    ---------------------------------------------------------------------------
    Implementation & Test (* items indicate potential impact to code)
    ---------------------------------------------------------------------------
  - When getting a CodedNodeGraph from the service, it is possible to specify
    the name of a relations container to scope the graph results.  If no
    container is specified, relations tagged as 'native' (defined by the code
    system authors) are searched.  However, the implementation was only
    allowing for definition of a single 'native' relations container.  This
    was changed to honor multiple native containers, if available.
    ---------------------------------------------------------------------------
    Load and Export
    ---------------------------------------------------------------------------
  - For load of the NCI Thesaurus OWL, roles and associations are partitioned
    into their own containers; both are defined as native relation containers.
    Separate query of roles or associations can be performed by specifying
    either 'roles' or 'associations' as the relations name when acquiring the
    graph from the LexBIGService.  Combined query of all partitions is
    supported if the relations name is left unspecified.
> - Fixed Bugzilla Bug 381 that causes RadLex graphs to look funny. Explicit 
    parent child relationship is being ignored, but the implicit parent child 
    relationship is being loaded as a hasSubtype relation.
> - Fixed bugzilla 389 reported against FMA where some concepts had no concept 
    properties. 
> - Corrected how the top thing is computed for OWL.
> - Association name of hasSubtype is now used instead of subClassOf in the 
    generic OWL loader to make it consistent with the NCI OWL loader.  
> - Fixed a bug in the Indexer metadata class that was causing metadata to get
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
 
-2.0Beta3 01/17/2007
    ---------------------------------------------------------------------------
    Specific to Core 1 support (e.g. deliverables & bugzilla items)
    ---------------------------------------------------------------------------  
  * NCI Metathesaurus loader changed as requested in LexBIG feature request 
    #3983.  This changes how individual source concept codes are stored.  See
    the feature request for more details.
  * The NCI Owl loader was changed so that all of the provided property names
    (such as dDEFININITION, synonom, etc) are now preserved in the 
    "propertyName" field.  Previously, the property name field was reserved
    for "textualPresentation", "definitition", etc.
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
  - Fixed a bug where it was not creating the correct type of return
    CodedEntries when reading from a 2005 version database.
  - Fixed a bug that occurred if you asked for a graph of depth one - and you 
    had two nodes that were linked to each other with different relationships, 
    it would return things to a depth of 2 instead of one.    
  * API updates:
    - Added a new parameter to all resolve methods that allows you to restrict
      the properties being returned by their type.
    - Added a get(start, end) method to the ResolvedConceptReferencesIterator.
      LexBIO feature request 323.
    ---------------------------------------------------------------------------
    Configuration & Logging (* items indicate potential action items)
    ---------------------------------------------------------------------------
    ---------------------------------------------------------------------------
    GUI
    ---------------------------------------------------------------------------
  - Added the ability to restrict on property qualifiers.
  - minor corrections and cleanup of display of sources, property qualifiers.
  - Added a viewer for code system details (double click on a code system)
  - Fixed a bug that was populating the sources and qualifiers lists incorrectly 
    when code systems were unioned.
    ---------------------------------------------------------------------------
    Load and Export
    ---------------------------------------------------------------------------
  - Fixed top node issue for the FMA loader.
  - Added a loader for RadLex.
  - Modified the OBO loader to add synonym as a presentation with property name
  - synonym instead of textualPresentation.
  - Modified OBO loading to add intersection_of, disjoint_with and union_of as
    Concept Properties and not as an association.
  - Added "synonym" to the supportedProperties list. Should fix bug where
    searches were not looking at synonym.
  - Added approxNumOfConcept value for FMA and RadLex.
  - The error "URI is not absolute" that is encountered while using the generic
    owl loader fixed.
         
-2.0Beta1 12/15/2006
    ---------------------------------------------------------------------------
    Specific to Core 1 support (e.g. deliverables & bugzilla items)
    ---------------------------------------------------------------------------
  - Introduction of LexGrid 2006 model into the infrastructure, which allows us
    to integrate the generic OWL loader into the common conversion toolkit,
    provides support for additional metadata to be included in the definition
    of a code system, etc.
  - Introduced ability to query obsolete terms as single query in support of
    BioPortal functionality.
  - Several changes to regular expression processing were introduced to resolve
    case sensitivity issues in support of BioPortal functions.
  - Introduced support to load the FMA (integrated into API and GUI).  This is
    provided though the Prot�g� Frames Loader extension, which will be enhanced
    to include other Prot�g� conversions (e.g. RadLex) in future releases.
    NOTE: To load FMA, download the fma.pprj file and the sql dump file
    following the setup instructions found at
    http://sig.biostr.washington.edu/cgi-bin/fma_register.cgi.
    The fma.pprj file can then be loaded using the ProtegeFrames Loader.
  - Integrated support for generic OWL as a standard Loader extension.
  - OBO top nodes are now computed after taking all associations into account.
  - Fixes for Bugzilla items #191 and #262 in support of OBO load.

 ******************************************************************************
 * This release introduces significant internal changes to the code base,
 * and therefore constitutes the start of a second major version (2.x).
 * As of this release the 2.x codebase becomes the focus of all new development
 * and enhancements.  Critical fixes to the 1.0.x codebase will be provided
 * only as required to meet needs of caCORE production deployment.
 ******************************************************************************
    ---------------------------------------------------------------------------
    Compatibility changes (compared to Version 1.x)
    ---------------------------------------------------------------------------
 ** Note that while compatibility with the 1.x codebase is a goal, API changes
    will be introduced during the 2.0 development cycle.  Due to these changes,
    any code that references the API will need to be recompiled.  
 ** The SQL storage formats have changed.  Changes are backwards compatible in
    that version 2.x will read data that was loaded by earlier versions.
    However, content loaded under the 2.x codebase is not compatible for query
    by version 1.x.
 ** The index format has also changed - any index that was created with an
    earlier release will not work properly with version 2.0.  You MUST reindex
    all code systems that were loaded by an earlier release before use.
    ---------------------------------------------------------------------------
    Configuration & Logging (* items indicate potential action items)
    ---------------------------------------------------------------------------
  * Be sure to backup and restore the config.props file if overwriting a
    previous installation. NOTE that if the config.props is restored from a
    version 1.x install, additional properties must be introduced.  There is a
    new required value of 'API_LOG_ENABLED' in this release.  Possible values
    are 'true' and 'false'.  If you enable the API log, the debug 
    log will contain ever single call passed into the API, along with the
    provided parameters.
    ---------------------------------------------------------------------------
    Infrastructure Impl & Test (* items indicate potential impact to code)
    ---------------------------------------------------------------------------
  * This is the first release that works off of the 2006 LexGrid model.  Moving
    to the 2006 model has brought changes in the API for returned objects.
    Please ensure that you recompile all of your code against this new release.
  * API updates:
  - Changed the way you query active/inactive codes - the active/inactive flag
    was removed from the getCodingSchemeConcepts().  Instead, enhanced support
    is provided via the restrictToStatus() method on the CodedNodeSet.  The
    version of getCodingSchemeConcepts() that includes the active/inactive flag
    was deprecated, and will be removed in a future release.
  - Changed specification of coded node restrictions based on concept status.
    Previously restrictions based on conceptStatus were specified as a
    well-known 'meta-property' on the restrictToMatchingProperties() method.
    Now it is handled as a separate restriction using the restrictToStatus()
    method.
  - Changed the restrictTo*Properties(...) methods - added another parameter
    that allows restriction based on the type of property, rather than the
    property name.  Previously, if you wanted to search for all definitions
    you had to restrict your query to properties that were named "definition".
    Now you may restrict your query to all properties of type 'definition',
    and the query will resolve all properties defined as definitions
    regardless of their assigned name.  This allows us to have definition
    names such as "dorlands definition", etc,  rather than having to name
    all definitions "definition".
  - Added an (optional) API method logger - when enabled, this will print each
    public API method called along with the given parameters.  This is
    controlled from the config.props file with the 'API_LOG_ENABLED' parameter.
  - Updates to code handling SQL Connection failures to make it more robust.   
  - Made a change to the RegExp query processing - previously regular
    expressions were evaluated against the tokenized lowercased property
    value. Now, they are evaluated against the untokenized lowercased property
    value. In practice, this change means that you should think of regular
    expressions as being evaluated against the entire property value, rather
    than against individual words inside the property value.
  - Regular expressions against "conceptCode" are now supported and will
    evaluate against the lowercased value of the concept code, rather than the
    case-sensitive concept code.  This makes regular expression behavior the
    same for concept codes as for other property values.
  - Added the missing 'RegExp' type to the getMatchAlgorithms() enumeration
    provided by the LexBIGService.
  - Fixed API for CodedNodeGraph resolution to return as many root (or leaf)
    nodes as it finds if it hits a limit, rather than returning none.
  - Fixed the CleanUpUtility (exposed by GUI via 'Clean up' command and admin
    script 'ClearOrphanedRelations') so it doesn't suggest removing the
    metadata index.
  - Fix for LexBIG GForge bug #3686 (problems with querying and viewing
    association qualifiers).
  - Fix for LexBIG GForge bug #3545 (toString issue).
  - Fix for LexBIG GForge bug #3685 (FindPropsAndAssocForCode does not return
    object-valued annotation properties).
  - Fix for LexBIG GForge bug #3629 (Problem with current MySQL drivers).
    ---------------------------------------------------------------------------
    GUI
    ---------------------------------------------------------------------------
  - Added new scripts to launch GUI without administrative functions available
    or visible, thereby provides a browse-only version of the GUI.  Scripts to
    launch the browser are included in the '/GUI' directory and specific to
    the operating system platform from which the script is launched...
    o Windows-lbGUI-browser.bat (Windows XP/NT/200x)
    o Linux-lbGUI-browser.sh (Linux 32-bit)
    o Linux_64-lbGUI-browser.sh (Linux 64-bit)
    o OSX-lbGUI-browser.command (Mac OSX)
    ---------------------------------------------------------------------------
    Load and Export
    ---------------------------------------------------------------------------
  - Introduced support to load the FMA (integrated into API and GUI).  This is
    provided though the Prot�g� Frames Loader extension, which will be enhanced
    to include other Prot�g� conversions (e.g. RadLex) in future releases.
    NOTE: To load FMA, download the fma.pprj file and the sql dump file
    following the setup instructions found at
    http://sig.biostr.washington.edu/cgi-bin/fma_register.cgi.
    The fma.pprj file can then be loaded using the ProtegeFrames Loader.
  - Fixes for Bugzilla items #191 and #262 in support of OBO load.
  - OBO top nodes are now computed after taking all associations into account.
  - Integrated support for generic OWL as a standard Loader extension.
  - Fixed a problem generating SQL indexes with newer MySQL drivers (LexBIG
    GForge bug #3629).
  - Created the new 2.x LexGrid SQL table format.  Updated all SQL code for
    2.x API (which supports the 2006 model) and added backwards compatibility
    for older SQL formats.
  - Fixed a bug where indexes weren't being properly reopened after a reindex
    operation took place.
 
 -1.0.1 10/30/06
    ---------------------------------------------------------------------------
    Specific to Core 1 support (e.g. deliverables & bugzilla items)
    ---------------------------------------------------------------------------
  - Enhancements to the LexBIGServiceMetadata API (load, remove, list) in
    support of BioPortal function.
  - Added RegExp match algorithm in support of BioPortal functions.
  - Added ability to restrict query of concept designations to non-preferred
    representations only; also in support of BioPortal.
  - Fixed bug to load psi-mod.obo (bugzilla Bug# 164).
  - Enhanced the LexGridRDFConverter (separately packaged jar) to transform the
    galen.owl file into lexgrid xml.
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
    restriction to a CodedNodeSet.  A regression test was added to prevent
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
    This support is integrated into the OBO loader, and is
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
    Specific to Core 1 support (e.g. deliverables & bugzilla items)
    ---------------------------------------------------------------------------
  - Improved handling of relations to support pre-calculation and retrieval
    of ending leaf nodes.  This is intended to improve the ability to load and
    work with relations that are naturally (or solely) navigable from leaf to
    root (e.g. part-of, is-a).  Changes affected specific loaders, convenience
    methods, and the internal implementation of CodedNodeGraph.
  - Added http:// URI support to the OBO loader (previously you had to have
    the obo files local on the machine, now you can just point to a web url
    to load obo content).
  - Bug fix for missing synonyms on OBO load.
  - The colon ':' character is no longer treated as a white space character
    for the purposes of indexing.  Previously, the concept code 'GO:4562'
    would have been indexed as two separate words - 'GO' and '4562'. Now it
    is indexed as 'GO:4562'.
  - A new alpha version of the general owl loader is available as a bundled but
    separately run application.  It takes in a owl file and generates 2006
    LexGrid XML, which can then be imported to LexBIO using the LexGrid XML
    loader.
  - CodedNodeSet restrictToProperties method now supports the 'contains'
    match algorithm when restricting to conceptCodes.
  - XML and OBO Exporters now honor the overwrite flag correctly.
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
    ---------------------------------------------------------------------------
    Implementation (* items indicate potential impact to code)
    ---------------------------------------------------------------------------
  - Added the ability to have multiple jar file locations in the config file 
    (see updated documentation in config file).
  - Implemented the Extension Registry.  Changes were made to internal code
    to now use the extension registry where appropriate (sorts, etc).
    Programmers now have the ability to dynamically register load, export,
    generic, and sort extensions.
  - Bug fixes for handling of coding scheme local names on codedNodeGraph
    operations.
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
  - Bug fixes to all loaders to prevent them from leaving orphaned resources
    behind in certain failure cases.
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
  - Minor bug fixes.

- 1.0.0rc8 (09/01/06)
    ---------------------------------------------------------------------------
    Core 1 support (e.g. communicated deliverables & bugzilla items)
    ---------------------------------------------------------------------------
  * Replaced the OBO 1.0 loader with a brand new OBO loader that can handle
    both 1.0 and the new 1.2 OBO formats.
  * Support was introduced for validation of OBO files prior to load.
  * An early version of an OBO exporter is now available.
  * Replaced the is_a, part_of and develops_from OBO relations with hasSubtype,
    has_part and develops_to respectively to facilitate easier tree traversal.
  * Top node traversal & processing for additional relations is now supported.
  * Inclusion of UMLS loaders (includes RRF, db, and SemanticNet); more below.
    
    ------------------------------------
    Shared caBIG (LexBIG) infrastructure
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
    -----------------------------------
    Core 1 support (e.g. Bugzilla items)
    -----------------------------------
  - Refinements were made to the way OBO files get parsed and loaded into
    Lexgrid. The concept codes in Lexgrid are now exactly the same as the term
    id values in OBO, etc.
  - OBO file format validation has been added.
  - Improved visualization of OBO-based content in GUI Interface.
  - Bugzilla #107 Generation of a unique global namespace for the obo files.
  - Bugzilla #139 Added counts to node iterator.
  - Bugzilla #140 Added a top node for the hasSubtype relation.
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
    -----------------------------------
    Core 1 support (e.g. Bugzilla items)
    -----------------------------------
  - Bugzilla #99 Files that failed to load.
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
    ---------
    Bug fixes
    ---------
  - Minor fixes for resolving coded node sets created from graphs.

- 1.0.0rc5 (06/26/06)
    -----------------------------------
    Core 1 support (e.g. Bugzilla items)
    -----------------------------------
  - Bugzilla #79  Synchronous process for parsing and indexing.
  - Bugzilla #100 MyClassLoader loading jars from ../runtime.
  - Bugzilla #101 LoadStatus to give more information.
    -------------
    Configuration
    -------------
  - Configuration options have changed.  Most notably, we have changed the way
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
    -----------------------------------
    Core 1 support (e.g. Bugzilla items)
    -----------------------------------
  - An initial implementation was delivered for import of OBO terminologies
    (1.0 format) to LexGrid repositories and index the resulting content for
    later queries.  Basic validation is carried out through the Prot�g� APIs.
    Both load and query capabilities are externalized through the LexBIG/BIO
    Java API.  Load and validation APIs return a load status to the caller.
    Load and validation functions are also available as command-line scripts.
  - Database administration interfaces provided by the runtime API, originally
    implemented to work with one database per vocabulary, are also being
    reworked in response to Stanford requirements for a single-database
    configuration. In addition, investigation and prototyping work has been
    done to enable support of Oracle database backends. Storage estimates
    (for the vocabulary database and other required indexes) were also
    provided to Stanford, targeting Oracle, in order to prepare for deployment.

    -------
    General
    -------
  - Primarily a maintenance release; extends the LexBIG 1.0.0rc4 base.
  - Tweaks and fixes were incorporated for database support, including
    additional Oracle and DB2 support and resource cleanup.
  - Additional database configuration options were introduced.  Support was
    added for 'single database' mode, where multiple coding scheme versions
    can be loaded to the same database instance.  Support was also introduced
    to use add an administrator-specified prefix to generated table names.
    Refer to comments in the config.props file for more information.
    NOTE: SINGLE DATABASE MODE IS NOW THE DEFAULT FOR NEW LexBIO ENVIRONMENTS.
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

- 04/24/05 
    -----------------------------------
    Core 1 support (e.g. Bugzilla items)
    -----------------------------------
  - Introduction of NCBO-specific product installer.
  - Extends the LexBIG 1.0.0rc2 base, including bug fixes and preliminary
    support for the import of OBO vocabularies.

===============================================================================
Please post questions to the LexBIG GForge or the following email address:
informatics@mayo.edu
