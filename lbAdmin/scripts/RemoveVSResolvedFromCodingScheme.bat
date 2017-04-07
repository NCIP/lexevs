@echo off
REM Removes resolved value sets based on coding scheme and version that was used for its 
REM resolution.
REM
REM Options:
REM  	-l, List of coding scheme versions to match when removing the ResolvedValueSet. 
REM		 -f,--force Force de-activation and removal without confirmation.
REM
REM 	Example: RemoveVSResolvedFromCodingSchemes  -l \"source.coding.scheme.uri::version1, second.source.uri::version2\" -f
REM
java -Xmx1000m -cp "..\runtime\lbPatch.jar;..\runtime-components\extLib\*" org.lexgrid.valuesets.admin.RemoveVSResolvedFromCodingSchemes %*