*************************************************************
* Biomedical Informatics LexGrid Converter Package
* http://informatics.mayo.edu/index.php?page=convert
*************************************************************

This converter requires that Java be properly installed (available on the path) on your computer.

To launch the GUI (recommended method):

Execute the Converter script (or executeable) in the bin subfolder for your OS platform.

*************************************
*** UMLS Converter Required Steps ***
*************************************

You must choose the "versionless source identifiers" when you use Memamorphosys to extract your RRF data.
If you use the versioned source identifiers, it will fail.

Conversion to LexGrid is a two step process - first - load RRF Text Files -> SQL.  Then SQL RRF
to LexGrid.

It is recommended that you use the load RRF Text Files -> SQL to get the first step done, rather than 
the scripts that the UMLS provides (this builds the proper indexes).

Loading RRF Text Files to SQL on DB2 will require a 16K page size in the table space.


*******************************
*** Text File format 		***
*******************************
The text files that can be imported must be one of the following formats - items surrounded by <> are 
the variables to fill in.  Items further surrounded by [] are optional.  \t represents a tab - the default
delimiter - however other delimiters may be used.
Lines beginning with # are comments.

Format a: 

<codingSchemeName>\t<codingSchemeId>\t<defaultLanguage>\t<formalName>[\t<version>][\t<source>][\t<description>][\t<copyright>]
<name1>[\t <description>] 
\t <name2>[\t <description>] 
\t\t <name3>[\t <description>]
\t\t <name4>[\t <description>]


The leading tabs represent hierarchical hasSubtype relationship nesting (name1 hasSubtype name2 and name2
hasSubtype name3)

Concept Codes will be automatically generated.
If a name is used more than once - it will be assigned the same code.  
If a description is used more than once (for a given name) only the first description will be stored.

Format b:

<code>\t<name>[\t<description>]

Same as (a) except that the concept codes are part of the input.  
If the same code occurs twice, the names must match.  Description rules same as above


******************************
*** Command Line Execution ***
******************************

If you want to run the converter from the command line, you will need to use one of the following commands:

java -Xmx300M -cp antBuild/lib/converter.jar cl.LdapToSQLLite
java -Xmx300M -cp antBuild/lib/converter.jar cl.LdapToSQL
java -Xmx300M -cp antBuild/lib/converter.jar cl.SQLLiteToLdap
java -Xmx300M -cp antBuild/lib/converter.jar cl.SQLToSQLLite
java -Xmx300M -cp antBuild/lib/converter.jar cl.UMLSToSQL
java -Xmx300M -cp antBuild/lib/converter.jar cl.SQLToLdap
java -Xmx300M -cp antBuild/lib/converter.jar cl.TextToSQLLite
java -Xmx300M -cp antBuild/lib/converter.jar cl.LgXMLToSQL


Each of these commands will prompt you for a different set of parameters, depending on which conversion 
you are trying to run.

Some of them will require a sql database driver and connection string.  Postgres, mysql, and MS Access drivers
are included in the distribution.  If you have a different driver you want to use, you will have to add it 
to the classpath.  See the webpage listed above for further documentation.

Here are some examples of what is expected for the driver string and the connection string:

Postgres: 
Driver - org.postgresql.Driver
Connection String - jdbc:postgresql://localhost/LexGridLite

MySQL: 
Driver - org.gjt.mm.mysql.Driver
Connection String - jdbc:mysql://localhost/LexGrid

MSAccess:
Driver - sun.jdbc.odbc.JdbcOdbcDriver
Connection String - jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=C:\\Temp\\LexGrid.mdb

DB2:
You will need to supply this driver (its not included in the distribution)
Driver - com.ibm.db2.jcc.DB2Driver
Connection String - jdbc:db2://server:50002/LexGrid:currentSchema=foo;

MS Sql Server:
Download drivers from http://www.microsoft.com/downloads/details.aspx?FamilyID=4f8f2f01-1ed7-4c4d-8f7b-3d47969e66ae&displaylang=en
Driver - com.microsoft.jdbc.sqlserver.SQLServerDriver
Connection String - jdbc:microsoft:sqlserver://localhost:1433;DatabaseName=LexGrid


*******************************
*** Contact for Support		***
*******************************

If you get stuck, e-mail informatics@mayo.edu

