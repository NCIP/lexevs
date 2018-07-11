@echo off
REM NOTE: This example is intended to run only against code systems
REM representing the entire Metathesaurus.  It depends on the presence of
REM concepts based on CUIs and the import of source and hierarchical
REM definitions as property and association qualifiers.  These
REM attributes are only populated by the NCI Meta loader.
REM 
REM Attempts to provide a tree, based on a focus code, that includes the
REM following information:
REM 
REM - All paths from the hierarchy root to one or more focus codes.
REM - Immediate children of every node in path to root
REM - Indicator to show whether any unexpanded node can be further expanded
REM 
REM This example accepts two parameters... The first parameter is required, and
REM must contain at least one code in a comma-delimited list. A tree is produced
REM for each code. Time to produce the tree for each code is printed in
REM milliseconds. In order to factor out costs of startup and shutdown, resolving
REM multiple codes may offer a better overall estimate performance.
REM 
REM The second parameter is also required.  It should provide a
REM source abbreviation (SAB) used to constrain the relationships navigated
REM to a single Meta source.  If not specified, all sources are navigated.
REM 
REM Note that this example does not print intra-CUI associations (links
REM that might exist between individual terms on a single concept).
REM 
REM The selected code system must represent the full Metathesaurus.
REM
java -Xmx1000m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.LexGrid.LexBIG.example.BuildTreeForMetaCodeBySource %*