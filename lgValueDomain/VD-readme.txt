----------------------------------------------------------------------------------
1. Software and Hardware requirements. 
----------------------------------------------------------------------------------

	i) This extension requires LexEVS release 5.01 or above.
 	ii) Development system are required to install the Sun Java Development Kit (SDK) or Java Runtime Environment (JRE)
 		version 1.5.0_11 or above.
 	iii) For software and hardware dependencies for the system hosting the LexEVS runtime, refer to https://cabig-kc.nci.nih.gov/Vocab/KC/index.php/LexEVS_5.0_Documentation.

----------------------------------------------------------------------------------
2. Instructions to build lgValueDomain extension from lgValueDomain java project in gForge SVN(https://gforge.nci.nih.gov/svnroot/lexevs).
----------------------------------------------------------------------------------

	i) Make sure runtime/lbRuntime.jar matches with the lbRuntime.jar of the lexEVS instance on ValueDomain is being run. 

	ii) Run build.xml (default target) located in root directory 

		- Source files get compiled and wrapped into lgValueDomain.jar. under "dist" folder.
	
		- Source files get zipped into lgValueDomain-src.jar under "dist" folder.
	
		- Intermediate class files get deleted.
	
		- Test Suite and Test Cases are compiled and wrapped into lgValueDomain-test.jar under "dist" folder.
	
		- JavaDoc gets generated under doc/valueDomain folder.
	
		- lgValueDomain.jar, lgValueDomain-test.jar, ValueDomain-GenerateReport.xml, ValueDomain-TestRunner.sh, ValueDomain-TestRunner.bat, 
	  		required external jars, test data and JavaDocs are bundled into "ValueDomainDeploy.zip" file.

----------------------------------------------------------------------------------
3. Instructions to deploy lgValueDomain extension into LexEVS environment.
----------------------------------------------------------------------------------
	  
	i) Extract the contents of "ValueDomainDeploy.zip" into the root of lexBIG installation (ex: C:/Program Files/LexGrid/LexBIG/5.0.0a/)      

		- ValueDomain is integrated with your lexBIG instance. 
		
		- Binary archive file, lgValueDomain.jar will be extracted to /runtime folder.
		
		- Source archive file, lgValueDomain-src.jar will be extracted to /source folder.
		
		- Test related files, lgValueDomain-test.jar, valueDomain-GenerateReport.xml, valueDomain-TestRunner.bat and valueDomain-testRunner.sh will be extracted to /test folder.
		
		- Test source files, Automobiles.xml, AutomobilesV2.xml, fungal_anatomy.obo, pickListTestData.xml and vdTestData.xml will be extracted to /test/resources/valueDomain folder.
		
		- JavaDoc will be extracted to /doc/javadoc/valueDomain folder.
	
----------------------------------------------------------------------------------
4. Instructions to test deployed lgValueDomain extension in LexEVS environment.
----------------------------------------------------------------------------------

	i) To test successful installation of ValueDomain extension, go to "test" folder in your lexBIG instance and run 
	
		- ValueDomain-TestRunner.bat file in windows environment or 
		
		- ValueDomain-TestRunner.sh file in unix/Linux environment.

	ii) Test Results can be viewed under "test/ValueDomain-Reports" folder.


----------------------------------------------------------------------------------
5. LexEVSValueDomainServices.
----------------------------------------------------------------------------------

	- This interface provides centralized access to all ValueDomain services.
	
	- Two ways to instantiate LexEVSValueDomainServices extension :
 		i. Using Generic Extension :
 			LexEVSValueDomainServices vdService = (LexEVSValueDomainServices) lbService.getGenericExtension("LexEVSValueDomainExtension");
 		ii. Direct :
 			LexEVSValueDomainServices vdService = new LexEVSValueDomainServicesImpl();
 			
 	- Service details can be found in javadoc located at <your LexEVS install folder>/docs/javadoc/valueDomain folder. 
 	
----------------------------------------------------------------------------------
6. LexEVSPickListServices.
----------------------------------------------------------------------------------
	
	- This interface provides centralized access to all PickList services.
	
 	- Two ways to instantiate LexEVSPickListServices extension :
		 i. Using Generic Extension :
		 	LexEVSPickListServices plService = (LexEVSPickListServices) lbService.getGenericExtension("LexEVSPickListExtension");
		 ii. Direct :
		 	LexEVSPickListServices plService = new LexEVSPickListServicesImpl();
 	
 	- Service details can be found in javadoc located at <your LexEVS install folder>/docs/javadoc/valueDomain folder. 
 
