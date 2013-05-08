=====================================
Notes for running the example scripts
=====================================  
- The examples here are intended to provide a simple interactive demo
  showing some of the capabilities of the LexBIG runtime.  For a more
  exhaustive verification test of the environment, it is recommended that
  you use the TestRunner script available in the /test subdirectory
  (in the LexBIG install root).  You can also use the graphical user
  interface support (in the /gui directory) to load content and perform
  interactive queries.

- Before invoking any LexBIG services, you must first modify the
  /resources/config/config.props file to establish database connection
  criteria and settings for runtime behavior.  Refer to instructions in
  the administration guide for additional information on available
  config settings.

- The examples were originally written to assume either the sample or
  full version of the NCI_Thesaurus as a target.  However, changes have
  been made to allow running against other loaded code systems.  The
  choice of code system is presented on the command line using a basic
  selection menu.
  
- To load the sample content, invoke one of the following commands
  available in the /example directory:
  

  Windows:
  -------
  LoadSampleData.bat

  Linux:
  -----
  LoadSampleData.sh

- Once loaded, you can invoke the example programs through the provided
  scripts (.bat for windows, .sh for linux). Scripts must be executed from
  the examples directory.
  
- Code System Metadata example (MetaDataSearch) requires to 
  load sample HL7 content by invoking the following command 
  in the /example directory:
  
  Windows:
  -------
  LoadSampleMetaDataData.bat
  
  No Linux command exists since the HL7 loader is only available for Windows.