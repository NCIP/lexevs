#!/bin/bash
#Load manager script that performs the following tasks:
# 	- Imports from an OWL-based XML file to the LexBIG repository.
# 	- Removes All Value Set Definitions
#   - Loads Asserted Value Set Definitions 
# 	- Builds indexes associated with the source asserted value sets of the specified coding scheme.
#   - Loads NCI History

# Options:
#   -ncit -ncitLoad Use this parameter to initiate the OWL2 load option of NCIt.
#   -in,--input &lt;uri&gt; URI or path specifying location of the source file
#   -mf,--manifest &lt;uri&gt; URI or path specifying location of the manifest file
#   -lp,--loader preferences &lt;uri&gt; URI or path specifying location of the loader preference file
#   -v, --validate &lt;int&gt; Perform validation of the candidate
#         resource without loading data.  If specified, the '-a' and '-t'
#         options are ignored.  Supported levels of validation include:
#         0 = Verify document is well-formed
#         1 = Verify document is valid
#   -a, --activate ActivateScheme on successful load; if unspecified the vocabulary is loaded but not activated
#   -t, --tag &lt;id&gt; An optional tag ID (e.g. 'PRODUCTION' or 'TEST') to assign. 
#   -meta --meatadata input &lt;uri&gt; URI or path specifying location of the metadata source file.  
#        metadata is applied to the code system and code system version being loaded.
#   -metav  --validate metadata &lt;int&gt; Perform validation of the metadata source file
#         without loading data. Supported levels of validation include:
#         0 = Verify document is valid.
#         metadata is validated against the code system and code system version being loaded.
#   -metao, --overwrite If specified, existing metadata for the code system
#         will be erased. Otherwise, new metadata will be appended to
#         existing metadata (if present).  
#   -metaf,--force Force overwrite (no confirmation).
# 
# 
# ------------- RemoveAllValueSetDefinitions Parameters -------------
#   -rvsd -RemoveAllValueSetDefinitions Remove All Value Set Definitions (no confirmation). 
#   		Use this parameter to initiate this option.
# 
# ------------- SourceAssertedValueSetDefinitionLoad Parameters -------------
#   -savsdl --SourceAssertedValueSetDefinitionLoad Load Asserted Value Set Definitions 
#   		Use this parameter to initiate this option.
#   -savsdl_v --SourceAssertedValueSetDefinitionLoad_version
#   
# ------------- BuildAssertedValueSetIndex Parameters -------------  
#   -bavsi --BuildAssertedValueSetIndex Build indexes associated with the source asserted value sets 
#		of the specified coding scheme.
#       Note: There is no confirmation/force option in this script.
#  		Use this parameter to initiate this option.
#   -bavsi_u --buildAssertedValueSetIndex_urn Coding Scheme URN
#   -bavsi_v --BuildAssertedValueSetIndex_version Coding Scheme Version
#   
# ------------- LoadNCIHistory Parameters -------------  
#   -lncih --LoadNCIHistory Load NCI History
#   		Use this parameter to initiate this option.
#   -lncih_in --LoadNCIHistory_input &lt;uri&gt; URI or path specifying location of the history file
#   -lncih_vf --LoadNCIHistory_versionFile &lt;uri&gt; URI or path specifying location of the file
#         containing version identifiers for the history to be loaded     
#   -lncih_r --LoadNCIHistory_replace replace if not specified, the provided history file will
#         be added into the current history database; otherwise the
#         current database will be replaced by the new content.
#        
# Example: LoadNCITMgr -
#  -ncit 
#  -in "file:///path/to/somefile.owl" 
#  -mf "file:///path/to/Thesaurus_MF_OWL2_20.09d.xml"
#  -lp "file:///path/to/Thesaurus_PF_OWL2.xml"
#  -a 
#  -v "20.09d" 
#  -t "PRODUCTION"
#  -meta --meatadata input <uri> URI or path specifying location of the metadata source file. 
#          metadata is applied to the code system and code system version being loaded.
#  -rvsd
#  -savsdl -savsdl_v "20.09d"
#  -bavsi
#  -bavsi_u "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#"
#  -bavsi_v "20.09d"
#  -lncih
#  -lncih_in "file:///path/to/cumulative_history_20.09d.txt"
#  -lncih_vf "file:///path/to/NCISystemReleaseHistory.txt"
#
java -Xmx8000m -Djava.awt.headless=true -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.lexgrid.valuesets.admin.LoadNCITMgr $@

