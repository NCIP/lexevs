<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ns0="http://LexGrid.org/schema/2009/01/LexGrid/codingSchemes" xmlns:ns1="http://LexGrid.org/schema/2009/01/LexGrid/commonTypes" xmlns:ns2="http://LexGrid.org/schema/2009/01/LexGrid/concepts" xmlns:ns3="http://LexGrid.org/schema/2009/01/LexGrid/naming" xmlns:ns4="http://LexGrid.org/schema/2009/01/LexGrid/relations" xmlns:tbf="http://www.altova.com/MapForce/UDF/tbf" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions" exclude-result-prefixes="ns0 ns1 ns2 ns3 ns4 tbf xs fn">
	<xsl:template name="tbf:tbf1_entryState">
		<xsl:param name="input" select="()"/>
		<xsl:for-each select="$input/@containingRevision">
			<xsl:attribute name="containingRevision" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@relativeOrder">
			<xsl:attribute name="relativeOrder" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@changeType">
			<xsl:attribute name="changeType" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@prevRevision">
			<xsl:attribute name="prevRevision" select="fn:string(.)"/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="tbf:tbf2_source">
		<xsl:param name="input" select="()"/>
		<xsl:for-each select="$input/@subRef">
			<xsl:attribute name="subRef" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@role">
			<xsl:attribute name="role" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:sequence select="fn:string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf3_text">
		<xsl:param name="input" select="()"/>
		<xsl:for-each select="$input/@dataType">
			<xsl:attribute name="dataType" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:sequence select="fn:string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf4_supportedAssociationQualifier">
		<xsl:param name="input" select="()"/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:sequence select="fn:string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf5_supportedCodingScheme">
		<xsl:param name="input" select="()"/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@isImported">
			<xsl:attribute name="isImported" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:sequence select="fn:string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf6_supportedContainerName">
		<xsl:param name="input" select="()"/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:sequence select="fn:string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf7_supportedContext">
		<xsl:param name="input" select="()"/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:sequence select="fn:string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf8_supportedDataType">
		<xsl:param name="input" select="()"/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:sequence select="fn:string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf9_supportedDegreeOfFidelity">
		<xsl:param name="input" select="()"/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:sequence select="fn:string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf10_supportedEntityType">
		<xsl:param name="input" select="()"/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:sequence select="fn:string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf11_supportedHierarchy">
		<xsl:param name="input" select="()"/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@associationNames">
			<xsl:attribute name="associationNames" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@rootCode">
			<xsl:attribute name="rootCode" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@isForwardNavigable">
			<xsl:attribute name="isForwardNavigable" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:sequence select="fn:string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf12_supportedLanguage">
		<xsl:param name="input" select="()"/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:sequence select="fn:string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf13_supportedNamespace">
		<xsl:param name="input" select="()"/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@equivalentCodingScheme">
			<xsl:attribute name="equivalentCodingScheme" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:sequence select="fn:string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf14_supportedPropertyType">
		<xsl:param name="input" select="()"/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:sequence select="fn:string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf15_supportedPropertyLink">
		<xsl:param name="input" select="()"/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:sequence select="fn:string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf16_supportedPropertyQualifier">
		<xsl:param name="input" select="()"/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:sequence select="fn:string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf17_supportedPropertyQualifierType">
		<xsl:param name="input" select="()"/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:sequence select="fn:string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf18_supportedRepresentationalForm">
		<xsl:param name="input" select="()"/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:sequence select="fn:string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf19_supportedSortOrder">
		<xsl:param name="input" select="()"/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:sequence select="fn:string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf20_supportedSource">
		<xsl:param name="input" select="()"/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@assemblyRule">
			<xsl:attribute name="assemblyRule" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:sequence select="fn:string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf21_supportedSourceRole">
		<xsl:param name="input" select="()"/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:sequence select="fn:string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf22_supportedStatus">
		<xsl:param name="input" select="()"/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:sequence select="fn:string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf23_propertyLink">
		<xsl:param name="input" select="()"/>
		<xsl:for-each select="$input/@sourceProperty">
			<xsl:attribute name="sourceProperty" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@propertyLink">
			<xsl:attribute name="propertyLink" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@targetProperty">
			<xsl:attribute name="targetProperty" select="fn:string(.)"/>
		</xsl:for-each>
	</xsl:template>
	<xsl:output method="xml" encoding="UTF-8" indent="yes"/>
	<xsl:template match="/">
		<codingSchemes xmlns="http://LexGrid.org/schema/2010/01/LexGrid/codingSchemes" xmlns:lgBuiltin="http://LexGrid.org/schema/2010/01/LexGrid/builtins" xmlns:lgCommon="http://LexGrid.org/schema/2010/01/LexGrid/commonTypes" xmlns:lgCon="http://LexGrid.org/schema/2010/01/LexGrid/concepts" xmlns:lgNaming="http://LexGrid.org/schema/2010/01/LexGrid/naming" xmlns:lgRel="http://LexGrid.org/schema/2010/01/LexGrid/relations" xmlns:lgVD="http://LexGrid.org/schema/2010/01/LexGrid/valueSets" xmlns:lgVer="http://LexGrid.org/schema/2010/01/LexGrid/versions">
			<xsl:attribute name="xsi:schemaLocation" namespace="http://www.w3.org/2001/XMLSchema-instance" select="'http://LexGrid.org/schema/2010/01/LexGrid/codingSchemes http://LexGrid.org/schema/2010/01/LexGrid/codingSchemes.xsd'"/>
			<xsl:for-each select="ns0:codingSchemes/ns0:codingScheme">
				<xsl:variable name="var1_defaultLanguage" as="item()*" select="@defaultLanguage"/>
				<xsl:variable name="var2_approxNumConcepts" as="item()*" select="@approxNumConcepts"/>
				<xsl:variable name="var3_isActive" as="item()*" select="@isActive"/>
				<xsl:variable name="var4_status" as="item()*" select="@status"/>
				<xsl:variable name="var5_formalName" as="item()*" select="@formalName"/>
				<xsl:variable name="var6_effectiveDate" as="item()*" select="@effectiveDate"/>
				<xsl:variable name="var7_expirationDate" as="item()*" select="@expirationDate"/>
				<xsl:variable name="var8_relations" as="node()*" select="ns0:relations"/>
				<xsl:variable name="var9_mappings" as="node()" select="ns0:mappings"/>
				<codingScheme>
					<xsl:if test="fn:exists($var3_isActive)">
						<xsl:attribute name="isActive" namespace="" select="xs:string(xs:boolean(fn:string($var3_isActive)))"/>
					</xsl:if>
					<xsl:if test="fn:exists($var4_status)">
						<xsl:attribute name="status" namespace="" select="fn:string($var4_status)"/>
					</xsl:if>
					<xsl:if test="fn:exists($var6_effectiveDate)">
						<xsl:attribute name="effectiveDate" namespace="" select="xs:string(xs:dateTime(fn:string($var6_effectiveDate)))"/>
					</xsl:if>
					<xsl:if test="fn:exists($var7_expirationDate)">
						<xsl:attribute name="expirationDate" namespace="" select="xs:string(xs:dateTime(fn:string($var7_expirationDate)))"/>
					</xsl:if>
					<xsl:attribute name="codingSchemeName" namespace="" select="fn:string(@codingSchemeName)"/>
					<xsl:attribute name="codingSchemeURI" namespace="" select="xs:string(xs:anyURI(fn:string(@codingSchemeURI)))"/>
					<xsl:if test="fn:exists($var5_formalName)">
						<xsl:attribute name="formalName" namespace="" select="fn:string($var5_formalName)"/>
					</xsl:if>
					<xsl:if test="fn:exists($var1_defaultLanguage)">
						<xsl:attribute name="defaultLanguage" namespace="" select="fn:string($var1_defaultLanguage)"/>
					</xsl:if>
					<xsl:if test="fn:exists($var2_approxNumConcepts)">
						<xsl:attribute name="approxNumConcepts" namespace="" select="xs:string(xs:integer(fn:string($var2_approxNumConcepts)))"/>
					</xsl:if>
					<xsl:attribute name="representsVersion" namespace="" select="fn:string(@representsVersion)"/>
					<xsl:for-each select="ns1:owner">
						<lgCommon:owner xsl:exclude-result-prefixes="lgCommon">
							<xsl:sequence select="fn:string(.)"/>
						</lgCommon:owner>
					</xsl:for-each>
					<xsl:for-each select="ns1:entryState">
						<lgCommon:entryState xsl:exclude-result-prefixes="lgCommon">
							<xsl:call-template name="tbf:tbf1_entryState">
								<xsl:with-param name="input" select="." as="node()"/>
							</xsl:call-template>
						</lgCommon:entryState>
					</xsl:for-each>
					<xsl:for-each select="ns1:entityDescription">
						<lgCommon:entityDescription xsl:exclude-result-prefixes="lgCommon">
							<xsl:for-each select="node()[fn:boolean(self::text())]">
								<xsl:sequence select="fn:string(.)"/>
							</xsl:for-each>
						</lgCommon:entityDescription>
					</xsl:for-each>
					<xsl:for-each select="ns0:localName">
						<localName>
							<xsl:sequence select="fn:string(.)"/>
						</localName>
					</xsl:for-each>
					<xsl:for-each select="ns0:source">
						<source>
							<xsl:call-template name="tbf:tbf2_source">
								<xsl:with-param name="input" select="." as="node()"/>
							</xsl:call-template>
						</source>
					</xsl:for-each>
					<xsl:for-each select="ns0:copyright">
						<copyright>
							<xsl:call-template name="tbf:tbf3_text">
								<xsl:with-param name="input" select="." as="node()"/>
							</xsl:call-template>
						</copyright>
					</xsl:for-each>
					<mappings>
						<xsl:for-each select="$var9_mappings/ns3:supportedAssociation">
							<xsl:variable name="var10_uri" as="item()*" select="@uri"/>
							<lgNaming:supportedAssociation xsl:exclude-result-prefixes="lgNaming">
								<xsl:attribute name="localId" namespace="" select="fn:string(@localId)"/>
								<xsl:if test="fn:exists($var10_uri)">
									<xsl:attribute name="uri" namespace="" select="xs:string(xs:anyURI(fn:string($var10_uri)))"/>
								</xsl:if>
								<xsl:sequence select="fn:string(.)"/>
							</lgNaming:supportedAssociation>
						</xsl:for-each>
						<xsl:for-each select="$var9_mappings/ns3:supportedAssociationQualifier">
							<lgNaming:supportedAssociationQualifier xsl:exclude-result-prefixes="lgNaming">
								<xsl:call-template name="tbf:tbf4_supportedAssociationQualifier">
									<xsl:with-param name="input" select="." as="node()"/>
								</xsl:call-template>
							</lgNaming:supportedAssociationQualifier>
						</xsl:for-each>
						<xsl:for-each select="$var9_mappings/ns3:supportedCodingScheme">
							<lgNaming:supportedCodingScheme xsl:exclude-result-prefixes="lgNaming">
								<xsl:call-template name="tbf:tbf5_supportedCodingScheme">
									<xsl:with-param name="input" select="." as="node()"/>
								</xsl:call-template>
							</lgNaming:supportedCodingScheme>
						</xsl:for-each>
						<xsl:for-each select="$var9_mappings/ns3:supportedContainer">
							<lgNaming:supportedContainerName xsl:exclude-result-prefixes="lgNaming">
								<xsl:call-template name="tbf:tbf6_supportedContainerName">
									<xsl:with-param name="input" select="." as="node()"/>
								</xsl:call-template>
							</lgNaming:supportedContainerName>
						</xsl:for-each>
						<xsl:for-each select="$var9_mappings/ns3:supportedContext">
							<lgNaming:supportedContext xsl:exclude-result-prefixes="lgNaming">
								<xsl:call-template name="tbf:tbf7_supportedContext">
									<xsl:with-param name="input" select="." as="node()"/>
								</xsl:call-template>
							</lgNaming:supportedContext>
						</xsl:for-each>
						<xsl:for-each select="$var9_mappings/ns3:supportedDataType">
							<lgNaming:supportedDataType xsl:exclude-result-prefixes="lgNaming">
								<xsl:call-template name="tbf:tbf8_supportedDataType">
									<xsl:with-param name="input" select="." as="node()"/>
								</xsl:call-template>
							</lgNaming:supportedDataType>
						</xsl:for-each>
						<xsl:for-each select="$var9_mappings/ns3:supportedDegreeOfFidelity">
							<lgNaming:supportedDegreeOfFidelity xsl:exclude-result-prefixes="lgNaming">
								<xsl:call-template name="tbf:tbf9_supportedDegreeOfFidelity">
									<xsl:with-param name="input" select="." as="node()"/>
								</xsl:call-template>
							</lgNaming:supportedDegreeOfFidelity>
						</xsl:for-each>
						<xsl:for-each select="$var9_mappings/ns3:supportedEntityType">
							<lgNaming:supportedEntityType xsl:exclude-result-prefixes="lgNaming">
								<xsl:call-template name="tbf:tbf10_supportedEntityType">
									<xsl:with-param name="input" select="." as="node()"/>
								</xsl:call-template>
							</lgNaming:supportedEntityType>
						</xsl:for-each>
						<xsl:for-each select="$var9_mappings/ns3:supportedHierarchy">
							<lgNaming:supportedHierarchy xsl:exclude-result-prefixes="lgNaming">
								<xsl:call-template name="tbf:tbf11_supportedHierarchy">
									<xsl:with-param name="input" select="." as="node()"/>
								</xsl:call-template>
							</lgNaming:supportedHierarchy>
						</xsl:for-each>
						<xsl:for-each select="$var9_mappings/ns3:supportedLanguage">
							<lgNaming:supportedLanguage xsl:exclude-result-prefixes="lgNaming">
								<xsl:call-template name="tbf:tbf12_supportedLanguage">
									<xsl:with-param name="input" select="." as="node()"/>
								</xsl:call-template>
							</lgNaming:supportedLanguage>
						</xsl:for-each>
						<xsl:for-each select="$var9_mappings/ns3:supportedNamespace">
							<lgNaming:supportedNamespace xsl:exclude-result-prefixes="lgNaming">
								<xsl:call-template name="tbf:tbf13_supportedNamespace">
									<xsl:with-param name="input" select="." as="node()"/>
								</xsl:call-template>
							</lgNaming:supportedNamespace>
						</xsl:for-each>
						<xsl:for-each select="$var9_mappings/ns3:supportedProperty">
							<xsl:variable name="var11_uri" as="item()*" select="@uri"/>
							<lgNaming:supportedProperty xsl:exclude-result-prefixes="lgNaming">
								<xsl:attribute name="localId" namespace="" select="fn:string(@localId)"/>
								<xsl:if test="fn:exists($var11_uri)">
									<xsl:attribute name="uri" namespace="" select="xs:string(xs:anyURI(fn:string($var11_uri)))"/>
								</xsl:if>
								<xsl:sequence select="fn:string(.)"/>
							</lgNaming:supportedProperty>
						</xsl:for-each>
						<xsl:for-each select="$var9_mappings/ns3:supportedPropertyType">
							<lgNaming:supportedPropertyType xsl:exclude-result-prefixes="lgNaming">
								<xsl:call-template name="tbf:tbf14_supportedPropertyType">
									<xsl:with-param name="input" select="." as="node()"/>
								</xsl:call-template>
							</lgNaming:supportedPropertyType>
						</xsl:for-each>
						<xsl:for-each select="$var9_mappings/ns3:supportedPropertyLink">
							<lgNaming:supportedPropertyLink xsl:exclude-result-prefixes="lgNaming">
								<xsl:call-template name="tbf:tbf15_supportedPropertyLink">
									<xsl:with-param name="input" select="." as="node()"/>
								</xsl:call-template>
							</lgNaming:supportedPropertyLink>
						</xsl:for-each>
						<xsl:for-each select="$var9_mappings/ns3:supportedPropertyQualifier">
							<lgNaming:supportedPropertyQualifier xsl:exclude-result-prefixes="lgNaming">
								<xsl:call-template name="tbf:tbf16_supportedPropertyQualifier">
									<xsl:with-param name="input" select="." as="node()"/>
								</xsl:call-template>
							</lgNaming:supportedPropertyQualifier>
						</xsl:for-each>
						<xsl:for-each select="$var9_mappings/ns3:supportedPropertyQualifierType">
							<lgNaming:supportedPropertyQualifierType xsl:exclude-result-prefixes="lgNaming">
								<xsl:call-template name="tbf:tbf17_supportedPropertyQualifierType">
									<xsl:with-param name="input" select="." as="node()"/>
								</xsl:call-template>
							</lgNaming:supportedPropertyQualifierType>
						</xsl:for-each>
						<xsl:for-each select="$var9_mappings/ns3:supportedRepresentationalForm">
							<lgNaming:supportedRepresentationalForm xsl:exclude-result-prefixes="lgNaming">
								<xsl:call-template name="tbf:tbf18_supportedRepresentationalForm">
									<xsl:with-param name="input" select="." as="node()"/>
								</xsl:call-template>
							</lgNaming:supportedRepresentationalForm>
						</xsl:for-each>
						<xsl:for-each select="$var9_mappings/ns3:supportedSortOrder">
							<lgNaming:supportedSortOrder xsl:exclude-result-prefixes="lgNaming">
								<xsl:call-template name="tbf:tbf19_supportedSortOrder">
									<xsl:with-param name="input" select="." as="node()"/>
								</xsl:call-template>
							</lgNaming:supportedSortOrder>
						</xsl:for-each>
						<xsl:for-each select="$var9_mappings/ns3:supportedSource">
							<lgNaming:supportedSource xsl:exclude-result-prefixes="lgNaming">
								<xsl:call-template name="tbf:tbf20_supportedSource">
									<xsl:with-param name="input" select="." as="node()"/>
								</xsl:call-template>
							</lgNaming:supportedSource>
						</xsl:for-each>
						<xsl:for-each select="$var9_mappings/ns3:supportedSourceRole">
							<lgNaming:supportedSourceRole xsl:exclude-result-prefixes="lgNaming">
								<xsl:call-template name="tbf:tbf21_supportedSourceRole">
									<xsl:with-param name="input" select="." as="node()"/>
								</xsl:call-template>
							</lgNaming:supportedSourceRole>
						</xsl:for-each>
						<xsl:for-each select="$var9_mappings/ns3:supportedStatus">
							<lgNaming:supportedStatus xsl:exclude-result-prefixes="lgNaming">
								<xsl:call-template name="tbf:tbf22_supportedStatus">
									<xsl:with-param name="input" select="." as="node()"/>
								</xsl:call-template>
							</lgNaming:supportedStatus>
						</xsl:for-each>
					</mappings>
					<xsl:for-each select="ns0:properties">
						<properties>
							<xsl:for-each select="ns1:property">
								<xsl:variable name="var12_propertyId" as="item()*" select="@propertyId"/>
								<xsl:variable name="var13_effectiveDate" as="item()*" select="@effectiveDate"/>
								<xsl:variable name="var14_language" as="item()*" select="@language"/>
								<xsl:variable name="var15_propertyType" as="item()*" select="@propertyType"/>
								<xsl:variable name="var16_status" as="item()*" select="@status"/>
								<xsl:variable name="var17_expirationDate" as="item()*" select="@expirationDate"/>
								<xsl:variable name="var18_isActive" as="item()*" select="@isActive"/>
								<lgCommon:property xsl:exclude-result-prefixes="lgCommon">
									<xsl:if test="fn:exists($var18_isActive)">
										<xsl:attribute name="isActive" namespace="" select="xs:string(xs:boolean(fn:string($var18_isActive)))"/>
									</xsl:if>
									<xsl:if test="fn:exists($var16_status)">
										<xsl:attribute name="status" namespace="" select="fn:string($var16_status)"/>
									</xsl:if>
									<xsl:if test="fn:exists($var13_effectiveDate)">
										<xsl:attribute name="effectiveDate" namespace="" select="xs:string(xs:dateTime(fn:string($var13_effectiveDate)))"/>
									</xsl:if>
									<xsl:if test="fn:exists($var17_expirationDate)">
										<xsl:attribute name="expirationDate" namespace="" select="xs:string(xs:dateTime(fn:string($var17_expirationDate)))"/>
									</xsl:if>
									<xsl:attribute name="propertyName" namespace="" select="fn:string(@propertyName)"/>
									<xsl:if test="fn:exists($var12_propertyId)">
										<xsl:attribute name="propertyId" namespace="" select="fn:string($var12_propertyId)"/>
									</xsl:if>
									<xsl:if test="fn:exists($var15_propertyType)">
										<xsl:attribute name="propertyType" namespace="" select="fn:string($var15_propertyType)"/>
									</xsl:if>
									<xsl:if test="fn:exists($var14_language)">
										<xsl:attribute name="language" namespace="" select="fn:string($var14_language)"/>
									</xsl:if>
									<xsl:for-each select="ns1:owner">
										<lgCommon:owner xsl:exclude-result-prefixes="lgCommon">
											<xsl:sequence select="fn:string(.)"/>
										</lgCommon:owner>
									</xsl:for-each>
									<xsl:for-each select="ns1:entryState">
										<lgCommon:entryState xsl:exclude-result-prefixes="lgCommon">
											<xsl:call-template name="tbf:tbf1_entryState">
												<xsl:with-param name="input" select="." as="node()"/>
											</xsl:call-template>
										</lgCommon:entryState>
									</xsl:for-each>
									<xsl:for-each select="ns1:source">
										<lgCommon:source xsl:exclude-result-prefixes="lgCommon">
											<xsl:call-template name="tbf:tbf2_source">
												<xsl:with-param name="input" select="." as="node()"/>
											</xsl:call-template>
										</lgCommon:source>
									</xsl:for-each>
									<xsl:for-each select="ns1:usageContext">
										<lgCommon:usageContext xsl:exclude-result-prefixes="lgCommon">
											<xsl:sequence select="fn:string(.)"/>
										</lgCommon:usageContext>
									</xsl:for-each>
									<xsl:for-each select="ns1:propertyQualifier">
										<xsl:variable name="var19_propertyQualifierType" as="item()*" select="@propertyQualifierType"/>
										<lgCommon:propertyQualifier xsl:exclude-result-prefixes="lgCommon">
											<xsl:attribute name="propertyQualifierName" namespace="" select="fn:string(@propertyQualifierName)"/>
											<xsl:if test="fn:exists($var19_propertyQualifierType)">
												<xsl:attribute name="propertyQualifierType" namespace="" select="fn:string($var19_propertyQualifierType)"/>
											</xsl:if>
											<xsl:for-each select="ns1:value">
												<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf3_text">
														<xsl:with-param name="input" select="." as="node()"/>
													</xsl:call-template>
												</lgCommon:value>
											</xsl:for-each>
										</lgCommon:propertyQualifier>
									</xsl:for-each>
									<xsl:for-each select="ns1:value">
										<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
											<xsl:call-template name="tbf:tbf3_text">
												<xsl:with-param name="input" select="." as="node()"/>
											</xsl:call-template>
										</lgCommon:value>
									</xsl:for-each>
								</lgCommon:property>
							</xsl:for-each>
						</properties>
					</xsl:for-each>
					<xsl:for-each select="ns0:entities">
						<entities>
							<xsl:for-each select="ns2:entity">
								<xsl:variable name="var20_effectiveDate" as="item()*" select="@effectiveDate"/>
								<xsl:variable name="var21_isDefined" as="item()*" select="@isDefined"/>
								<xsl:variable name="var22_expirationDate" as="item()*" select="@expirationDate"/>
								<xsl:variable name="var23_status" as="item()*" select="@status"/>
								<xsl:variable name="var24_entityCodeNamespace" as="item()*" select="@entityCodeNamespace"/>
								<xsl:variable name="var25_isActive" as="item()*" select="@isActive"/>
								<xsl:variable name="var26_isAnonymous" as="item()*" select="@isAnonymous"/>
								<lgCon:entity xsl:exclude-result-prefixes="lgCon">
									<xsl:if test="fn:exists($var25_isActive)">
										<xsl:attribute name="isActive" namespace="" select="xs:string(xs:boolean(fn:string($var25_isActive)))"/>
									</xsl:if>
									<xsl:if test="fn:exists($var23_status)">
										<xsl:attribute name="status" namespace="" select="fn:string($var23_status)"/>
									</xsl:if>
									<xsl:if test="fn:exists($var20_effectiveDate)">
										<xsl:attribute name="effectiveDate" namespace="" select="xs:string(xs:dateTime(fn:string($var20_effectiveDate)))"/>
									</xsl:if>
									<xsl:if test="fn:exists($var22_expirationDate)">
										<xsl:attribute name="expirationDate" namespace="" select="xs:string(xs:dateTime(fn:string($var22_expirationDate)))"/>
									</xsl:if>
									<xsl:attribute name="entityCode" namespace="" select="fn:string(@entityCode)"/>
									<xsl:if test="fn:exists($var24_entityCodeNamespace)">
										<xsl:attribute name="entityCodeNamespace" namespace="" select="fn:string($var24_entityCodeNamespace)"/>
									</xsl:if>
									<xsl:if test="fn:exists($var26_isAnonymous)">
										<xsl:attribute name="isAnonymous" namespace="" select="xs:string(xs:boolean(fn:string($var26_isAnonymous)))"/>
									</xsl:if>
									<xsl:if test="fn:exists($var21_isDefined)">
										<xsl:attribute name="isDefined" namespace="" select="xs:string(xs:boolean(fn:string($var21_isDefined)))"/>
									</xsl:if>
									<xsl:for-each select="ns1:owner">
										<lgCommon:owner xsl:exclude-result-prefixes="lgCommon">
											<xsl:sequence select="fn:string(.)"/>
										</lgCommon:owner>
									</xsl:for-each>
									<xsl:for-each select="ns1:entryState">
										<lgCommon:entryState xsl:exclude-result-prefixes="lgCommon">
											<xsl:call-template name="tbf:tbf1_entryState">
												<xsl:with-param name="input" select="." as="node()"/>
											</xsl:call-template>
										</lgCommon:entryState>
									</xsl:for-each>
									<xsl:for-each select="ns1:entityDescription">
										<lgCommon:entityDescription xsl:exclude-result-prefixes="lgCommon">
											<xsl:for-each select="node()[fn:boolean(self::text())]">
												<xsl:sequence select="fn:string(.)"/>
											</xsl:for-each>
										</lgCommon:entityDescription>
									</xsl:for-each>
									<xsl:for-each select="ns2:entityType">
										<lgCon:entityType xsl:exclude-result-prefixes="lgCon">
											<xsl:sequence select="fn:string(.)"/>
										</lgCon:entityType>
									</xsl:for-each>
									<xsl:for-each select="ns2:presentation">
										<xsl:variable name="var27_isPreferred" as="item()*" select="@isPreferred"/>
										<xsl:variable name="var28_language" as="item()*" select="@language"/>
										<xsl:variable name="var29_isActive" as="item()*" select="@isActive"/>
										<xsl:variable name="var30_status" as="item()*" select="@status"/>
										<xsl:variable name="var31_propertyType" as="item()*" select="@propertyType"/>
										<xsl:variable name="var32_expirationDate" as="item()*" select="@expirationDate"/>
										<xsl:variable name="var33_propertyId" as="item()*" select="@propertyId"/>
										<xsl:variable name="var34_effectiveDate" as="item()*" select="@effectiveDate"/>
										<xsl:variable name="var35_degreeOfFidelity" as="item()*" select="@degreeOfFidelity"/>
										<xsl:variable name="var36_matchIfNoContext" as="item()*" select="@matchIfNoContext"/>
										<xsl:variable name="var37_representationalForm" as="item()*" select="@representationalForm"/>
										<lgCon:presentation xsl:exclude-result-prefixes="lgCon">
											<xsl:if test="fn:exists($var29_isActive)">
												<xsl:attribute name="isActive" namespace="" select="xs:string(xs:boolean(fn:string($var29_isActive)))"/>
											</xsl:if>
											<xsl:if test="fn:exists($var30_status)">
												<xsl:attribute name="status" namespace="" select="fn:string($var30_status)"/>
											</xsl:if>
											<xsl:if test="fn:exists($var34_effectiveDate)">
												<xsl:attribute name="effectiveDate" namespace="" select="xs:string(xs:dateTime(fn:string($var34_effectiveDate)))"/>
											</xsl:if>
											<xsl:if test="fn:exists($var32_expirationDate)">
												<xsl:attribute name="expirationDate" namespace="" select="xs:string(xs:dateTime(fn:string($var32_expirationDate)))"/>
											</xsl:if>
											<xsl:attribute name="propertyName" namespace="" select="fn:string(@propertyName)"/>
											<xsl:if test="fn:exists($var33_propertyId)">
												<xsl:attribute name="propertyId" namespace="" select="fn:string($var33_propertyId)"/>
											</xsl:if>
											<xsl:if test="fn:exists($var31_propertyType)">
												<xsl:attribute name="propertyType" namespace="" select="fn:string($var31_propertyType)"/>
											</xsl:if>
											<xsl:if test="fn:exists($var28_language)">
												<xsl:attribute name="language" namespace="" select="fn:string($var28_language)"/>
											</xsl:if>
											<xsl:if test="fn:exists($var27_isPreferred)">
												<xsl:attribute name="isPreferred" namespace="" select="xs:string(xs:boolean(fn:string($var27_isPreferred)))"/>
											</xsl:if>
											<xsl:if test="fn:exists($var35_degreeOfFidelity)">
												<xsl:attribute name="degreeOfFidelity" namespace="" select="fn:string($var35_degreeOfFidelity)"/>
											</xsl:if>
											<xsl:if test="fn:exists($var36_matchIfNoContext)">
												<xsl:attribute name="matchIfNoContext" namespace="" select="xs:string(xs:boolean(fn:string($var36_matchIfNoContext)))"/>
											</xsl:if>
											<xsl:if test="fn:exists($var37_representationalForm)">
												<xsl:attribute name="representationalForm" namespace="" select="fn:string($var37_representationalForm)"/>
											</xsl:if>
											<xsl:for-each select="ns1:owner">
												<lgCommon:owner xsl:exclude-result-prefixes="lgCommon">
													<xsl:sequence select="fn:string(.)"/>
												</lgCommon:owner>
											</xsl:for-each>
											<xsl:for-each select="ns1:entryState">
												<lgCommon:entryState xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf1_entryState">
														<xsl:with-param name="input" select="." as="node()"/>
													</xsl:call-template>
												</lgCommon:entryState>
											</xsl:for-each>
											<xsl:for-each select="ns1:source">
												<lgCommon:source xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf2_source">
														<xsl:with-param name="input" select="." as="node()"/>
													</xsl:call-template>
												</lgCommon:source>
											</xsl:for-each>
											<xsl:for-each select="ns1:usageContext">
												<lgCommon:usageContext xsl:exclude-result-prefixes="lgCommon">
													<xsl:sequence select="fn:string(.)"/>
												</lgCommon:usageContext>
											</xsl:for-each>
											<xsl:for-each select="ns1:propertyQualifier">
												<xsl:variable name="var38_propertyQualifierType" as="item()*" select="@propertyQualifierType"/>
												<lgCommon:propertyQualifier xsl:exclude-result-prefixes="lgCommon">
													<xsl:attribute name="propertyQualifierName" namespace="" select="fn:string(@propertyQualifierName)"/>
													<xsl:if test="fn:exists($var38_propertyQualifierType)">
														<xsl:attribute name="propertyQualifierType" namespace="" select="fn:string($var38_propertyQualifierType)"/>
													</xsl:if>
													<xsl:for-each select="ns1:value">
														<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
															<xsl:call-template name="tbf:tbf3_text">
																<xsl:with-param name="input" select="." as="node()"/>
															</xsl:call-template>
														</lgCommon:value>
													</xsl:for-each>
												</lgCommon:propertyQualifier>
											</xsl:for-each>
											<xsl:for-each select="ns1:value">
												<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf3_text">
														<xsl:with-param name="input" select="." as="node()"/>
													</xsl:call-template>
												</lgCommon:value>
											</xsl:for-each>
										</lgCon:presentation>
									</xsl:for-each>
									<xsl:for-each select="ns2:definition">
										<xsl:variable name="var39_isActive" as="item()*" select="@isActive"/>
										<xsl:variable name="var40_language" as="item()*" select="@language"/>
										<xsl:variable name="var41_status" as="item()*" select="@status"/>
										<xsl:variable name="var42_propertyId" as="item()*" select="@propertyId"/>
										<xsl:variable name="var43_effectiveDate" as="item()*" select="@effectiveDate"/>
										<xsl:variable name="var44_expirationDate" as="item()*" select="@expirationDate"/>
										<xsl:variable name="var45_propertyType" as="item()*" select="@propertyType"/>
										<xsl:variable name="var46_isPreferred" as="item()*" select="@isPreferred"/>
										<lgCon:definition xsl:exclude-result-prefixes="lgCon">
											<xsl:if test="fn:exists($var39_isActive)">
												<xsl:attribute name="isActive" namespace="" select="xs:string(xs:boolean(fn:string($var39_isActive)))"/>
											</xsl:if>
											<xsl:if test="fn:exists($var41_status)">
												<xsl:attribute name="status" namespace="" select="fn:string($var41_status)"/>
											</xsl:if>
											<xsl:if test="fn:exists($var43_effectiveDate)">
												<xsl:attribute name="effectiveDate" namespace="" select="xs:string(xs:dateTime(fn:string($var43_effectiveDate)))"/>
											</xsl:if>
											<xsl:if test="fn:exists($var44_expirationDate)">
												<xsl:attribute name="expirationDate" namespace="" select="xs:string(xs:dateTime(fn:string($var44_expirationDate)))"/>
											</xsl:if>
											<xsl:attribute name="propertyName" namespace="" select="fn:string(@propertyName)"/>
											<xsl:if test="fn:exists($var42_propertyId)">
												<xsl:attribute name="propertyId" namespace="" select="fn:string($var42_propertyId)"/>
											</xsl:if>
											<xsl:if test="fn:exists($var45_propertyType)">
												<xsl:attribute name="propertyType" namespace="" select="fn:string($var45_propertyType)"/>
											</xsl:if>
											<xsl:if test="fn:exists($var40_language)">
												<xsl:attribute name="language" namespace="" select="fn:string($var40_language)"/>
											</xsl:if>
											<xsl:if test="fn:exists($var46_isPreferred)">
												<xsl:attribute name="isPreferred" namespace="" select="xs:string(xs:boolean(fn:string($var46_isPreferred)))"/>
											</xsl:if>
											<xsl:for-each select="ns1:owner">
												<lgCommon:owner xsl:exclude-result-prefixes="lgCommon">
													<xsl:sequence select="fn:string(.)"/>
												</lgCommon:owner>
											</xsl:for-each>
											<xsl:for-each select="ns1:entryState">
												<lgCommon:entryState xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf1_entryState">
														<xsl:with-param name="input" select="." as="node()"/>
													</xsl:call-template>
												</lgCommon:entryState>
											</xsl:for-each>
											<xsl:for-each select="ns1:source">
												<lgCommon:source xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf2_source">
														<xsl:with-param name="input" select="." as="node()"/>
													</xsl:call-template>
												</lgCommon:source>
											</xsl:for-each>
											<xsl:for-each select="ns1:usageContext">
												<lgCommon:usageContext xsl:exclude-result-prefixes="lgCommon">
													<xsl:sequence select="fn:string(.)"/>
												</lgCommon:usageContext>
											</xsl:for-each>
											<xsl:for-each select="ns1:propertyQualifier">
												<xsl:variable name="var47_propertyQualifierType" as="item()*" select="@propertyQualifierType"/>
												<lgCommon:propertyQualifier xsl:exclude-result-prefixes="lgCommon">
													<xsl:attribute name="propertyQualifierName" namespace="" select="fn:string(@propertyQualifierName)"/>
													<xsl:if test="fn:exists($var47_propertyQualifierType)">
														<xsl:attribute name="propertyQualifierType" namespace="" select="fn:string($var47_propertyQualifierType)"/>
													</xsl:if>
													<xsl:for-each select="ns1:value">
														<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
															<xsl:call-template name="tbf:tbf3_text">
																<xsl:with-param name="input" select="." as="node()"/>
															</xsl:call-template>
														</lgCommon:value>
													</xsl:for-each>
												</lgCommon:propertyQualifier>
											</xsl:for-each>
											<xsl:for-each select="ns1:value">
												<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf3_text">
														<xsl:with-param name="input" select="." as="node()"/>
													</xsl:call-template>
												</lgCommon:value>
											</xsl:for-each>
										</lgCon:definition>
									</xsl:for-each>
									<xsl:for-each select="ns2:comment">
										<xsl:variable name="var48_status" as="item()*" select="@status"/>
										<xsl:variable name="var49_isActive" as="item()*" select="@isActive"/>
										<xsl:variable name="var50_language" as="item()*" select="@language"/>
										<xsl:variable name="var51_expirationDate" as="item()*" select="@expirationDate"/>
										<xsl:variable name="var52_effectiveDate" as="item()*" select="@effectiveDate"/>
										<xsl:variable name="var53_propertyId" as="item()*" select="@propertyId"/>
										<xsl:variable name="var54_propertyType" as="item()*" select="@propertyType"/>
										<lgCon:comment xsl:exclude-result-prefixes="lgCon">
											<xsl:if test="fn:exists($var49_isActive)">
												<xsl:attribute name="isActive" namespace="" select="xs:string(xs:boolean(fn:string($var49_isActive)))"/>
											</xsl:if>
											<xsl:if test="fn:exists($var48_status)">
												<xsl:attribute name="status" namespace="" select="fn:string($var48_status)"/>
											</xsl:if>
											<xsl:if test="fn:exists($var52_effectiveDate)">
												<xsl:attribute name="effectiveDate" namespace="" select="xs:string(xs:dateTime(fn:string($var52_effectiveDate)))"/>
											</xsl:if>
											<xsl:if test="fn:exists($var51_expirationDate)">
												<xsl:attribute name="expirationDate" namespace="" select="xs:string(xs:dateTime(fn:string($var51_expirationDate)))"/>
											</xsl:if>
											<xsl:attribute name="propertyName" namespace="" select="fn:string(@propertyName)"/>
											<xsl:if test="fn:exists($var53_propertyId)">
												<xsl:attribute name="propertyId" namespace="" select="fn:string($var53_propertyId)"/>
											</xsl:if>
											<xsl:if test="fn:exists($var54_propertyType)">
												<xsl:attribute name="propertyType" namespace="" select="fn:string($var54_propertyType)"/>
											</xsl:if>
											<xsl:if test="fn:exists($var50_language)">
												<xsl:attribute name="language" namespace="" select="fn:string($var50_language)"/>
											</xsl:if>
											<xsl:for-each select="ns1:owner">
												<lgCommon:owner xsl:exclude-result-prefixes="lgCommon">
													<xsl:sequence select="fn:string(.)"/>
												</lgCommon:owner>
											</xsl:for-each>
											<xsl:for-each select="ns1:entryState">
												<lgCommon:entryState xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf1_entryState">
														<xsl:with-param name="input" select="." as="node()"/>
													</xsl:call-template>
												</lgCommon:entryState>
											</xsl:for-each>
											<xsl:for-each select="ns1:source">
												<lgCommon:source xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf2_source">
														<xsl:with-param name="input" select="." as="node()"/>
													</xsl:call-template>
												</lgCommon:source>
											</xsl:for-each>
											<xsl:for-each select="ns1:usageContext">
												<lgCommon:usageContext xsl:exclude-result-prefixes="lgCommon">
													<xsl:sequence select="fn:string(.)"/>
												</lgCommon:usageContext>
											</xsl:for-each>
											<xsl:for-each select="ns1:propertyQualifier">
												<xsl:variable name="var55_propertyQualifierType" as="item()*" select="@propertyQualifierType"/>
												<lgCommon:propertyQualifier xsl:exclude-result-prefixes="lgCommon">
													<xsl:attribute name="propertyQualifierName" namespace="" select="fn:string(@propertyQualifierName)"/>
													<xsl:if test="fn:exists($var55_propertyQualifierType)">
														<xsl:attribute name="propertyQualifierType" namespace="" select="fn:string($var55_propertyQualifierType)"/>
													</xsl:if>
													<xsl:for-each select="ns1:value">
														<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
															<xsl:call-template name="tbf:tbf3_text">
																<xsl:with-param name="input" select="." as="node()"/>
															</xsl:call-template>
														</lgCommon:value>
													</xsl:for-each>
												</lgCommon:propertyQualifier>
											</xsl:for-each>
											<xsl:for-each select="ns1:value">
												<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf3_text">
														<xsl:with-param name="input" select="." as="node()"/>
													</xsl:call-template>
												</lgCommon:value>
											</xsl:for-each>
										</lgCon:comment>
									</xsl:for-each>
									<xsl:for-each select="ns2:property">
										<xsl:variable name="var56_isActive" as="item()*" select="@isActive"/>
										<xsl:variable name="var57_language" as="item()*" select="@language"/>
										<xsl:variable name="var58_effectiveDate" as="item()*" select="@effectiveDate"/>
										<xsl:variable name="var59_expirationDate" as="item()*" select="@expirationDate"/>
										<xsl:variable name="var60_status" as="item()*" select="@status"/>
										<xsl:variable name="var61_propertyId" as="item()*" select="@propertyId"/>
										<xsl:variable name="var62_propertyType" as="item()*" select="@propertyType"/>
										<lgCon:property xsl:exclude-result-prefixes="lgCon">
											<xsl:if test="fn:exists($var56_isActive)">
												<xsl:attribute name="isActive" namespace="" select="xs:string(xs:boolean(fn:string($var56_isActive)))"/>
											</xsl:if>
											<xsl:if test="fn:exists($var60_status)">
												<xsl:attribute name="status" namespace="" select="fn:string($var60_status)"/>
											</xsl:if>
											<xsl:if test="fn:exists($var58_effectiveDate)">
												<xsl:attribute name="effectiveDate" namespace="" select="xs:string(xs:dateTime(fn:string($var58_effectiveDate)))"/>
											</xsl:if>
											<xsl:if test="fn:exists($var59_expirationDate)">
												<xsl:attribute name="expirationDate" namespace="" select="xs:string(xs:dateTime(fn:string($var59_expirationDate)))"/>
											</xsl:if>
											<xsl:attribute name="propertyName" namespace="" select="fn:string(@propertyName)"/>
											<xsl:if test="fn:exists($var61_propertyId)">
												<xsl:attribute name="propertyId" namespace="" select="fn:string($var61_propertyId)"/>
											</xsl:if>
											<xsl:if test="fn:exists($var62_propertyType)">
												<xsl:attribute name="propertyType" namespace="" select="fn:string($var62_propertyType)"/>
											</xsl:if>
											<xsl:if test="fn:exists($var57_language)">
												<xsl:attribute name="language" namespace="" select="fn:string($var57_language)"/>
											</xsl:if>
											<xsl:for-each select="ns1:owner">
												<lgCommon:owner xsl:exclude-result-prefixes="lgCommon">
													<xsl:sequence select="fn:string(.)"/>
												</lgCommon:owner>
											</xsl:for-each>
											<xsl:for-each select="ns1:entryState">
												<lgCommon:entryState xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf1_entryState">
														<xsl:with-param name="input" select="." as="node()"/>
													</xsl:call-template>
												</lgCommon:entryState>
											</xsl:for-each>
											<xsl:for-each select="ns1:source">
												<lgCommon:source xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf2_source">
														<xsl:with-param name="input" select="." as="node()"/>
													</xsl:call-template>
												</lgCommon:source>
											</xsl:for-each>
											<xsl:for-each select="ns1:usageContext">
												<lgCommon:usageContext xsl:exclude-result-prefixes="lgCommon">
													<xsl:sequence select="fn:string(.)"/>
												</lgCommon:usageContext>
											</xsl:for-each>
											<xsl:for-each select="ns1:propertyQualifier">
												<xsl:variable name="var63_propertyQualifierType" as="item()*" select="@propertyQualifierType"/>
												<lgCommon:propertyQualifier xsl:exclude-result-prefixes="lgCommon">
													<xsl:attribute name="propertyQualifierName" namespace="" select="fn:string(@propertyQualifierName)"/>
													<xsl:if test="fn:exists($var63_propertyQualifierType)">
														<xsl:attribute name="propertyQualifierType" namespace="" select="fn:string($var63_propertyQualifierType)"/>
													</xsl:if>
													<xsl:for-each select="ns1:value">
														<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
															<xsl:call-template name="tbf:tbf3_text">
																<xsl:with-param name="input" select="." as="node()"/>
															</xsl:call-template>
														</lgCommon:value>
													</xsl:for-each>
												</lgCommon:propertyQualifier>
											</xsl:for-each>
											<xsl:for-each select="ns1:value">
												<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf3_text">
														<xsl:with-param name="input" select="." as="node()"/>
													</xsl:call-template>
												</lgCommon:value>
											</xsl:for-each>
										</lgCon:property>
									</xsl:for-each>
									<xsl:for-each select="ns2:propertyLink">
										<lgCon:propertyLink xsl:exclude-result-prefixes="lgCon">
											<xsl:call-template name="tbf:tbf23_propertyLink">
												<xsl:with-param name="input" select="." as="node()"/>
											</xsl:call-template>
										</lgCon:propertyLink>
									</xsl:for-each>
								</lgCon:entity>
							</xsl:for-each>
							<xsl:for-each select="$var8_relations/ns4:association">
								<xsl:variable name="var64_isTransitive" as="item()*" select="@isTransitive"/>
								<xsl:variable name="var65_isNavigable" as="item()*" select="@isNavigable"/>
								<xsl:variable name="var66_reverseName" as="item()*" select="@reverseName"/>
								<xsl:variable name="var67_status" as="item()*" select="@status"/>
								<xsl:variable name="var68_expirationDate" as="item()*" select="@expirationDate"/>
								<xsl:variable name="var69_isDefined" as="item()*" select="@isDefined"/>
								<xsl:variable name="var70_isActive" as="item()*" select="@isActive"/>
								<xsl:variable name="var71_entityCodeNamespace" as="item()*" select="@entityCodeNamespace"/>
								<xsl:variable name="var72_isAnonymous" as="item()*" select="@isAnonymous"/>
								<xsl:variable name="var73_effectiveDate" as="item()*" select="@effectiveDate"/>
								<xsl:variable name="var74_forwardName" as="item()*" select="@forwardName"/>
								<lgCon:associationEntity xsl:exclude-result-prefixes="lgCon">
									<xsl:if test="fn:exists($var70_isActive)">
										<xsl:attribute name="isActive" namespace="" select="xs:string(xs:boolean(fn:string($var70_isActive)))"/>
									</xsl:if>
									<xsl:if test="fn:exists($var67_status)">
										<xsl:attribute name="status" namespace="" select="fn:string($var67_status)"/>
									</xsl:if>
									<xsl:if test="fn:exists($var73_effectiveDate)">
										<xsl:attribute name="effectiveDate" namespace="" select="xs:string(xs:dateTime(fn:string($var73_effectiveDate)))"/>
									</xsl:if>
									<xsl:if test="fn:exists($var68_expirationDate)">
										<xsl:attribute name="expirationDate" namespace="" select="xs:string(xs:dateTime(fn:string($var68_expirationDate)))"/>
									</xsl:if>
									<xsl:attribute name="entityCode" namespace="" select="fn:string(@entityCode)"/>
									<xsl:if test="fn:exists($var71_entityCodeNamespace)">
										<xsl:attribute name="entityCodeNamespace" namespace="" select="fn:string($var71_entityCodeNamespace)"/>
									</xsl:if>
									<xsl:if test="fn:exists($var72_isAnonymous)">
										<xsl:attribute name="isAnonymous" namespace="" select="xs:string(xs:boolean(fn:string($var72_isAnonymous)))"/>
									</xsl:if>
									<xsl:if test="fn:exists($var69_isDefined)">
										<xsl:attribute name="isDefined" namespace="" select="xs:string(xs:boolean(fn:string($var69_isDefined)))"/>
									</xsl:if>
									<xsl:if test="fn:exists($var74_forwardName)">
										<xsl:attribute name="forwardName" namespace="" select="fn:string($var74_forwardName)"/>
									</xsl:if>
									<xsl:if test="fn:exists($var66_reverseName)">
										<xsl:attribute name="reverseName" namespace="" select="fn:string($var66_reverseName)"/>
									</xsl:if>
									<xsl:if test="fn:exists($var65_isNavigable)">
										<xsl:attribute name="isNavigable" namespace="" select="xs:string(xs:boolean(fn:string($var65_isNavigable)))"/>
									</xsl:if>
									<xsl:if test="fn:exists($var64_isTransitive)">
										<xsl:attribute name="isTransitive" namespace="" select="xs:string(xs:boolean(fn:string($var64_isTransitive)))"/>
									</xsl:if>
									<xsl:for-each select="ns1:owner">
										<lgCommon:owner xsl:exclude-result-prefixes="lgCommon">
											<xsl:sequence select="fn:string(.)"/>
										</lgCommon:owner>
									</xsl:for-each>
									<xsl:for-each select="ns1:entryState">
										<lgCommon:entryState xsl:exclude-result-prefixes="lgCommon">
											<xsl:call-template name="tbf:tbf1_entryState">
												<xsl:with-param name="input" select="." as="node()"/>
											</xsl:call-template>
										</lgCommon:entryState>
									</xsl:for-each>
									<xsl:for-each select="ns1:entityDescription">
										<lgCommon:entityDescription xsl:exclude-result-prefixes="lgCommon">
											<xsl:for-each select="node()[fn:boolean(self::text())]">
												<xsl:sequence select="fn:string(.)"/>
											</xsl:for-each>
										</lgCommon:entityDescription>
									</xsl:for-each>
									<xsl:for-each select="ns2:entityType">
										<lgCon:entityType xsl:exclude-result-prefixes="lgCon">
											<xsl:sequence select="fn:string(.)"/>
										</lgCon:entityType>
									</xsl:for-each>
									<xsl:for-each select="ns2:presentation">
										<xsl:variable name="var75_matchIfNoContext" as="item()*" select="@matchIfNoContext"/>
										<xsl:variable name="var76_isActive" as="item()*" select="@isActive"/>
										<xsl:variable name="var77_status" as="item()*" select="@status"/>
										<xsl:variable name="var78_representationalForm" as="item()*" select="@representationalForm"/>
										<xsl:variable name="var79_language" as="item()*" select="@language"/>
										<xsl:variable name="var80_isPreferred" as="item()*" select="@isPreferred"/>
										<xsl:variable name="var81_effectiveDate" as="item()*" select="@effectiveDate"/>
										<xsl:variable name="var82_expirationDate" as="item()*" select="@expirationDate"/>
										<xsl:variable name="var83_propertyType" as="item()*" select="@propertyType"/>
										<xsl:variable name="var84_degreeOfFidelity" as="item()*" select="@degreeOfFidelity"/>
										<xsl:variable name="var85_propertyId" as="item()*" select="@propertyId"/>
										<lgCon:presentation xsl:exclude-result-prefixes="lgCon">
											<xsl:if test="fn:exists($var76_isActive)">
												<xsl:attribute name="isActive" namespace="" select="xs:string(xs:boolean(fn:string($var76_isActive)))"/>
											</xsl:if>
											<xsl:if test="fn:exists($var77_status)">
												<xsl:attribute name="status" namespace="" select="fn:string($var77_status)"/>
											</xsl:if>
											<xsl:if test="fn:exists($var81_effectiveDate)">
												<xsl:attribute name="effectiveDate" namespace="" select="xs:string(xs:dateTime(fn:string($var81_effectiveDate)))"/>
											</xsl:if>
											<xsl:if test="fn:exists($var82_expirationDate)">
												<xsl:attribute name="expirationDate" namespace="" select="xs:string(xs:dateTime(fn:string($var82_expirationDate)))"/>
											</xsl:if>
											<xsl:attribute name="propertyName" namespace="" select="fn:string(@propertyName)"/>
											<xsl:if test="fn:exists($var85_propertyId)">
												<xsl:attribute name="propertyId" namespace="" select="fn:string($var85_propertyId)"/>
											</xsl:if>
											<xsl:if test="fn:exists($var83_propertyType)">
												<xsl:attribute name="propertyType" namespace="" select="fn:string($var83_propertyType)"/>
											</xsl:if>
											<xsl:if test="fn:exists($var79_language)">
												<xsl:attribute name="language" namespace="" select="fn:string($var79_language)"/>
											</xsl:if>
											<xsl:if test="fn:exists($var80_isPreferred)">
												<xsl:attribute name="isPreferred" namespace="" select="xs:string(xs:boolean(fn:string($var80_isPreferred)))"/>
											</xsl:if>
											<xsl:if test="fn:exists($var84_degreeOfFidelity)">
												<xsl:attribute name="degreeOfFidelity" namespace="" select="fn:string($var84_degreeOfFidelity)"/>
											</xsl:if>
											<xsl:if test="fn:exists($var75_matchIfNoContext)">
												<xsl:attribute name="matchIfNoContext" namespace="" select="xs:string(xs:boolean(fn:string($var75_matchIfNoContext)))"/>
											</xsl:if>
											<xsl:if test="fn:exists($var78_representationalForm)">
												<xsl:attribute name="representationalForm" namespace="" select="fn:string($var78_representationalForm)"/>
											</xsl:if>
											<xsl:for-each select="ns1:owner">
												<lgCommon:owner xsl:exclude-result-prefixes="lgCommon">
													<xsl:sequence select="fn:string(.)"/>
												</lgCommon:owner>
											</xsl:for-each>
											<xsl:for-each select="ns1:entryState">
												<lgCommon:entryState xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf1_entryState">
														<xsl:with-param name="input" select="." as="node()"/>
													</xsl:call-template>
												</lgCommon:entryState>
											</xsl:for-each>
											<xsl:for-each select="ns1:source">
												<lgCommon:source xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf2_source">
														<xsl:with-param name="input" select="." as="node()"/>
													</xsl:call-template>
												</lgCommon:source>
											</xsl:for-each>
											<xsl:for-each select="ns1:usageContext">
												<lgCommon:usageContext xsl:exclude-result-prefixes="lgCommon">
													<xsl:sequence select="fn:string(.)"/>
												</lgCommon:usageContext>
											</xsl:for-each>
											<xsl:for-each select="ns1:propertyQualifier">
												<xsl:variable name="var86_propertyQualifierType" as="item()*" select="@propertyQualifierType"/>
												<lgCommon:propertyQualifier xsl:exclude-result-prefixes="lgCommon">
													<xsl:attribute name="propertyQualifierName" namespace="" select="fn:string(@propertyQualifierName)"/>
													<xsl:if test="fn:exists($var86_propertyQualifierType)">
														<xsl:attribute name="propertyQualifierType" namespace="" select="fn:string($var86_propertyQualifierType)"/>
													</xsl:if>
													<xsl:for-each select="ns1:value">
														<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
															<xsl:call-template name="tbf:tbf3_text">
																<xsl:with-param name="input" select="." as="node()"/>
															</xsl:call-template>
														</lgCommon:value>
													</xsl:for-each>
												</lgCommon:propertyQualifier>
											</xsl:for-each>
											<xsl:for-each select="ns1:value">
												<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf3_text">
														<xsl:with-param name="input" select="." as="node()"/>
													</xsl:call-template>
												</lgCommon:value>
											</xsl:for-each>
										</lgCon:presentation>
									</xsl:for-each>
									<xsl:for-each select="ns2:definition">
										<xsl:variable name="var87_language" as="item()*" select="@language"/>
										<xsl:variable name="var88_isPreferred" as="item()*" select="@isPreferred"/>
										<xsl:variable name="var89_expirationDate" as="item()*" select="@expirationDate"/>
										<xsl:variable name="var90_status" as="item()*" select="@status"/>
										<xsl:variable name="var91_isActive" as="item()*" select="@isActive"/>
										<xsl:variable name="var92_effectiveDate" as="item()*" select="@effectiveDate"/>
										<xsl:variable name="var93_propertyId" as="item()*" select="@propertyId"/>
										<xsl:variable name="var94_propertyType" as="item()*" select="@propertyType"/>
										<lgCon:definition xsl:exclude-result-prefixes="lgCon">
											<xsl:if test="fn:exists($var91_isActive)">
												<xsl:attribute name="isActive" namespace="" select="xs:string(xs:boolean(fn:string($var91_isActive)))"/>
											</xsl:if>
											<xsl:if test="fn:exists($var90_status)">
												<xsl:attribute name="status" namespace="" select="fn:string($var90_status)"/>
											</xsl:if>
											<xsl:if test="fn:exists($var92_effectiveDate)">
												<xsl:attribute name="effectiveDate" namespace="" select="xs:string(xs:dateTime(fn:string($var92_effectiveDate)))"/>
											</xsl:if>
											<xsl:if test="fn:exists($var89_expirationDate)">
												<xsl:attribute name="expirationDate" namespace="" select="xs:string(xs:dateTime(fn:string($var89_expirationDate)))"/>
											</xsl:if>
											<xsl:attribute name="propertyName" namespace="" select="fn:string(@propertyName)"/>
											<xsl:if test="fn:exists($var93_propertyId)">
												<xsl:attribute name="propertyId" namespace="" select="fn:string($var93_propertyId)"/>
											</xsl:if>
											<xsl:if test="fn:exists($var94_propertyType)">
												<xsl:attribute name="propertyType" namespace="" select="fn:string($var94_propertyType)"/>
											</xsl:if>
											<xsl:if test="fn:exists($var87_language)">
												<xsl:attribute name="language" namespace="" select="fn:string($var87_language)"/>
											</xsl:if>
											<xsl:if test="fn:exists($var88_isPreferred)">
												<xsl:attribute name="isPreferred" namespace="" select="xs:string(xs:boolean(fn:string($var88_isPreferred)))"/>
											</xsl:if>
											<xsl:for-each select="ns1:owner">
												<lgCommon:owner xsl:exclude-result-prefixes="lgCommon">
													<xsl:sequence select="fn:string(.)"/>
												</lgCommon:owner>
											</xsl:for-each>
											<xsl:for-each select="ns1:entryState">
												<lgCommon:entryState xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf1_entryState">
														<xsl:with-param name="input" select="." as="node()"/>
													</xsl:call-template>
												</lgCommon:entryState>
											</xsl:for-each>
											<xsl:for-each select="ns1:source">
												<lgCommon:source xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf2_source">
														<xsl:with-param name="input" select="." as="node()"/>
													</xsl:call-template>
												</lgCommon:source>
											</xsl:for-each>
											<xsl:for-each select="ns1:usageContext">
												<lgCommon:usageContext xsl:exclude-result-prefixes="lgCommon">
													<xsl:sequence select="fn:string(.)"/>
												</lgCommon:usageContext>
											</xsl:for-each>
											<xsl:for-each select="ns1:propertyQualifier">
												<xsl:variable name="var95_propertyQualifierType" as="item()*" select="@propertyQualifierType"/>
												<lgCommon:propertyQualifier xsl:exclude-result-prefixes="lgCommon">
													<xsl:attribute name="propertyQualifierName" namespace="" select="fn:string(@propertyQualifierName)"/>
													<xsl:if test="fn:exists($var95_propertyQualifierType)">
														<xsl:attribute name="propertyQualifierType" namespace="" select="fn:string($var95_propertyQualifierType)"/>
													</xsl:if>
													<xsl:for-each select="ns1:value">
														<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
															<xsl:call-template name="tbf:tbf3_text">
																<xsl:with-param name="input" select="." as="node()"/>
															</xsl:call-template>
														</lgCommon:value>
													</xsl:for-each>
												</lgCommon:propertyQualifier>
											</xsl:for-each>
											<xsl:for-each select="ns1:value">
												<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf3_text">
														<xsl:with-param name="input" select="." as="node()"/>
													</xsl:call-template>
												</lgCommon:value>
											</xsl:for-each>
										</lgCon:definition>
									</xsl:for-each>
									<xsl:for-each select="ns2:comment">
										<xsl:variable name="var96_expirationDate" as="item()*" select="@expirationDate"/>
										<xsl:variable name="var97_propertyId" as="item()*" select="@propertyId"/>
										<xsl:variable name="var98_isActive" as="item()*" select="@isActive"/>
										<xsl:variable name="var99_propertyType" as="item()*" select="@propertyType"/>
										<xsl:variable name="var100_language" as="item()*" select="@language"/>
										<xsl:variable name="var101_effectiveDate" as="item()*" select="@effectiveDate"/>
										<xsl:variable name="var102_status" as="item()*" select="@status"/>
										<lgCon:comment xsl:exclude-result-prefixes="lgCon">
											<xsl:if test="fn:exists($var98_isActive)">
												<xsl:attribute name="isActive" namespace="" select="xs:string(xs:boolean(fn:string($var98_isActive)))"/>
											</xsl:if>
											<xsl:if test="fn:exists($var102_status)">
												<xsl:attribute name="status" namespace="" select="fn:string($var102_status)"/>
											</xsl:if>
											<xsl:if test="fn:exists($var101_effectiveDate)">
												<xsl:attribute name="effectiveDate" namespace="" select="xs:string(xs:dateTime(fn:string($var101_effectiveDate)))"/>
											</xsl:if>
											<xsl:if test="fn:exists($var96_expirationDate)">
												<xsl:attribute name="expirationDate" namespace="" select="xs:string(xs:dateTime(fn:string($var96_expirationDate)))"/>
											</xsl:if>
											<xsl:attribute name="propertyName" namespace="" select="fn:string(@propertyName)"/>
											<xsl:if test="fn:exists($var97_propertyId)">
												<xsl:attribute name="propertyId" namespace="" select="fn:string($var97_propertyId)"/>
											</xsl:if>
											<xsl:if test="fn:exists($var99_propertyType)">
												<xsl:attribute name="propertyType" namespace="" select="fn:string($var99_propertyType)"/>
											</xsl:if>
											<xsl:if test="fn:exists($var100_language)">
												<xsl:attribute name="language" namespace="" select="fn:string($var100_language)"/>
											</xsl:if>
											<xsl:for-each select="ns1:owner">
												<lgCommon:owner xsl:exclude-result-prefixes="lgCommon">
													<xsl:sequence select="fn:string(.)"/>
												</lgCommon:owner>
											</xsl:for-each>
											<xsl:for-each select="ns1:entryState">
												<lgCommon:entryState xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf1_entryState">
														<xsl:with-param name="input" select="." as="node()"/>
													</xsl:call-template>
												</lgCommon:entryState>
											</xsl:for-each>
											<xsl:for-each select="ns1:source">
												<lgCommon:source xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf2_source">
														<xsl:with-param name="input" select="." as="node()"/>
													</xsl:call-template>
												</lgCommon:source>
											</xsl:for-each>
											<xsl:for-each select="ns1:usageContext">
												<lgCommon:usageContext xsl:exclude-result-prefixes="lgCommon">
													<xsl:sequence select="fn:string(.)"/>
												</lgCommon:usageContext>
											</xsl:for-each>
											<xsl:for-each select="ns1:propertyQualifier">
												<xsl:variable name="var103_propertyQualifierType" as="item()*" select="@propertyQualifierType"/>
												<lgCommon:propertyQualifier xsl:exclude-result-prefixes="lgCommon">
													<xsl:attribute name="propertyQualifierName" namespace="" select="fn:string(@propertyQualifierName)"/>
													<xsl:if test="fn:exists($var103_propertyQualifierType)">
														<xsl:attribute name="propertyQualifierType" namespace="" select="fn:string($var103_propertyQualifierType)"/>
													</xsl:if>
													<xsl:for-each select="ns1:value">
														<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
															<xsl:call-template name="tbf:tbf3_text">
																<xsl:with-param name="input" select="." as="node()"/>
															</xsl:call-template>
														</lgCommon:value>
													</xsl:for-each>
												</lgCommon:propertyQualifier>
											</xsl:for-each>
											<xsl:for-each select="ns1:value">
												<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf3_text">
														<xsl:with-param name="input" select="." as="node()"/>
													</xsl:call-template>
												</lgCommon:value>
											</xsl:for-each>
										</lgCon:comment>
									</xsl:for-each>
									<xsl:for-each select="ns2:property">
										<xsl:variable name="var104_propertyType" as="item()*" select="@propertyType"/>
										<xsl:variable name="var105_language" as="item()*" select="@language"/>
										<xsl:variable name="var106_effectiveDate" as="item()*" select="@effectiveDate"/>
										<xsl:variable name="var107_isActive" as="item()*" select="@isActive"/>
										<xsl:variable name="var108_status" as="item()*" select="@status"/>
										<xsl:variable name="var109_expirationDate" as="item()*" select="@expirationDate"/>
										<xsl:variable name="var110_propertyId" as="item()*" select="@propertyId"/>
										<lgCon:property xsl:exclude-result-prefixes="lgCon">
											<xsl:if test="fn:exists($var107_isActive)">
												<xsl:attribute name="isActive" namespace="" select="xs:string(xs:boolean(fn:string($var107_isActive)))"/>
											</xsl:if>
											<xsl:if test="fn:exists($var108_status)">
												<xsl:attribute name="status" namespace="" select="fn:string($var108_status)"/>
											</xsl:if>
											<xsl:if test="fn:exists($var106_effectiveDate)">
												<xsl:attribute name="effectiveDate" namespace="" select="xs:string(xs:dateTime(fn:string($var106_effectiveDate)))"/>
											</xsl:if>
											<xsl:if test="fn:exists($var109_expirationDate)">
												<xsl:attribute name="expirationDate" namespace="" select="xs:string(xs:dateTime(fn:string($var109_expirationDate)))"/>
											</xsl:if>
											<xsl:attribute name="propertyName" namespace="" select="fn:string(@propertyName)"/>
											<xsl:if test="fn:exists($var110_propertyId)">
												<xsl:attribute name="propertyId" namespace="" select="fn:string($var110_propertyId)"/>
											</xsl:if>
											<xsl:if test="fn:exists($var104_propertyType)">
												<xsl:attribute name="propertyType" namespace="" select="fn:string($var104_propertyType)"/>
											</xsl:if>
											<xsl:if test="fn:exists($var105_language)">
												<xsl:attribute name="language" namespace="" select="fn:string($var105_language)"/>
											</xsl:if>
											<xsl:for-each select="ns1:owner">
												<lgCommon:owner xsl:exclude-result-prefixes="lgCommon">
													<xsl:sequence select="fn:string(.)"/>
												</lgCommon:owner>
											</xsl:for-each>
											<xsl:for-each select="ns1:entryState">
												<lgCommon:entryState xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf1_entryState">
														<xsl:with-param name="input" select="." as="node()"/>
													</xsl:call-template>
												</lgCommon:entryState>
											</xsl:for-each>
											<xsl:for-each select="ns1:source">
												<lgCommon:source xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf2_source">
														<xsl:with-param name="input" select="." as="node()"/>
													</xsl:call-template>
												</lgCommon:source>
											</xsl:for-each>
											<xsl:for-each select="ns1:usageContext">
												<lgCommon:usageContext xsl:exclude-result-prefixes="lgCommon">
													<xsl:sequence select="fn:string(.)"/>
												</lgCommon:usageContext>
											</xsl:for-each>
											<xsl:for-each select="ns1:propertyQualifier">
												<xsl:variable name="var111_propertyQualifierType" as="item()*" select="@propertyQualifierType"/>
												<lgCommon:propertyQualifier xsl:exclude-result-prefixes="lgCommon">
													<xsl:attribute name="propertyQualifierName" namespace="" select="fn:string(@propertyQualifierName)"/>
													<xsl:if test="fn:exists($var111_propertyQualifierType)">
														<xsl:attribute name="propertyQualifierType" namespace="" select="fn:string($var111_propertyQualifierType)"/>
													</xsl:if>
													<xsl:for-each select="ns1:value">
														<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
															<xsl:call-template name="tbf:tbf3_text">
																<xsl:with-param name="input" select="." as="node()"/>
															</xsl:call-template>
														</lgCommon:value>
													</xsl:for-each>
												</lgCommon:propertyQualifier>
											</xsl:for-each>
											<xsl:for-each select="ns1:value">
												<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf3_text">
														<xsl:with-param name="input" select="." as="node()"/>
													</xsl:call-template>
												</lgCommon:value>
											</xsl:for-each>
										</lgCon:property>
									</xsl:for-each>
									<xsl:for-each select="ns2:propertyLink">
										<lgCon:propertyLink xsl:exclude-result-prefixes="lgCon">
											<xsl:call-template name="tbf:tbf23_propertyLink">
												<xsl:with-param name="input" select="." as="node()"/>
											</xsl:call-template>
										</lgCon:propertyLink>
									</xsl:for-each>
								</lgCon:associationEntity>
							</xsl:for-each>
						</entities>
					</xsl:for-each>
					<xsl:for-each select="$var8_relations">
						<xsl:variable name="var112_containerName" as="item()*" select="@containerName"/>
						<relations>
							<xsl:if test="fn:exists($var112_containerName)">
								<xsl:attribute name="containerName" namespace="" select="fn:string($var112_containerName)"/>
							</xsl:if>
							<xsl:for-each select="ns1:entityDescription">
								<lgCommon:entityDescription xsl:exclude-result-prefixes="lgCommon">
									<xsl:for-each select="node()[fn:boolean(self::text())]">
										<xsl:sequence select="fn:string(.)"/>
									</xsl:for-each>
								</lgCommon:entityDescription>
							</xsl:for-each>
							<xsl:for-each select="ns4:association">
								<lgRel:associationPredicate xsl:exclude-result-prefixes="lgRel">
									<xsl:attribute name="associationName" namespace="" select="fn:string(@associationName)"/>
									<xsl:for-each select="ns4:source">
										<xsl:variable name="var113_sourceEntityCodeNamespace" as="item()*" select="@sourceEntityCodeNamespace"/>
										<lgRel:source xsl:exclude-result-prefixes="lgRel">
											<xsl:if test="fn:exists($var113_sourceEntityCodeNamespace)">
												<xsl:attribute name="sourceEntityCodeNamespace" namespace="" select="fn:string($var113_sourceEntityCodeNamespace)"/>
											</xsl:if>
											<xsl:attribute name="sourceEntityCode" namespace="" select="fn:string(@sourceEntityCode)"/>
											<xsl:for-each select="ns4:target">
												<xsl:variable name="var114_status" as="item()*" select="@status"/>
												<xsl:variable name="var115_associationInstanceId" as="item()*" select="@associationInstanceId"/>
												<xsl:variable name="var116_isInferred" as="item()*" select="@isInferred"/>
												<xsl:variable name="var117_expirationDate" as="item()*" select="@expirationDate"/>
												<xsl:variable name="var118_targetEntityCodeNamespace" as="item()*" select="@targetEntityCodeNamespace"/>
												<xsl:variable name="var119_isActive" as="item()*" select="@isActive"/>
												<xsl:variable name="var120_effectiveDate" as="item()*" select="@effectiveDate"/>
												<xsl:variable name="var121_isDefining" as="item()*" select="@isDefining"/>
												<lgRel:target xsl:exclude-result-prefixes="lgRel">
													<xsl:if test="fn:exists($var119_isActive)">
														<xsl:attribute name="isActive" namespace="" select="xs:string(xs:boolean(fn:string($var119_isActive)))"/>
													</xsl:if>
													<xsl:if test="fn:exists($var114_status)">
														<xsl:attribute name="status" namespace="" select="fn:string($var114_status)"/>
													</xsl:if>
													<xsl:if test="fn:exists($var120_effectiveDate)">
														<xsl:attribute name="effectiveDate" namespace="" select="xs:string(xs:dateTime(fn:string($var120_effectiveDate)))"/>
													</xsl:if>
													<xsl:if test="fn:exists($var117_expirationDate)">
														<xsl:attribute name="expirationDate" namespace="" select="xs:string(xs:dateTime(fn:string($var117_expirationDate)))"/>
													</xsl:if>
													<xsl:if test="fn:exists($var115_associationInstanceId)">
														<xsl:attribute name="associationInstanceId" namespace="" select="fn:string($var115_associationInstanceId)"/>
													</xsl:if>
													<xsl:if test="fn:exists($var116_isInferred)">
														<xsl:attribute name="isInferred" namespace="" select="xs:string(xs:boolean(fn:string($var116_isInferred)))"/>
													</xsl:if>
													<xsl:if test="fn:exists($var121_isDefining)">
														<xsl:attribute name="isDefining" namespace="" select="xs:string(xs:boolean(fn:string($var121_isDefining)))"/>
													</xsl:if>
													<xsl:attribute name="targetEntityCode" namespace="" select="fn:string(@targetEntityCode)"/>
													<xsl:if test="fn:exists($var118_targetEntityCodeNamespace)">
														<xsl:attribute name="targetEntityCodeNamespace" namespace="" select="fn:string($var118_targetEntityCodeNamespace)"/>
													</xsl:if>
													<xsl:for-each select="ns1:owner">
														<lgCommon:owner xsl:exclude-result-prefixes="lgCommon">
															<xsl:sequence select="fn:string(.)"/>
														</lgCommon:owner>
													</xsl:for-each>
													<xsl:for-each select="ns1:entryState">
														<lgCommon:entryState xsl:exclude-result-prefixes="lgCommon">
															<xsl:call-template name="tbf:tbf1_entryState">
																<xsl:with-param name="input" select="." as="node()"/>
															</xsl:call-template>
														</lgCommon:entryState>
													</xsl:for-each>
													<xsl:for-each select="ns4:usageContext">
														<lgRel:usageContext xsl:exclude-result-prefixes="lgRel">
															<xsl:sequence select="fn:string(.)"/>
														</lgRel:usageContext>
													</xsl:for-each>
													<xsl:for-each select="ns4:associationQualification">
														<lgRel:associationQualification xsl:exclude-result-prefixes="lgRel">
															<xsl:attribute name="associationQualifier" namespace="" select="fn:string(@associationQualifier)"/>
															<xsl:for-each select="ns4:qualifierText">
																<lgRel:qualifierText xsl:exclude-result-prefixes="lgRel">
																	<xsl:call-template name="tbf:tbf3_text">
																		<xsl:with-param name="input" select="." as="node()"/>
																	</xsl:call-template>
																</lgRel:qualifierText>
															</xsl:for-each>
														</lgRel:associationQualification>
													</xsl:for-each>
												</lgRel:target>
											</xsl:for-each>
											<xsl:for-each select="ns4:targetData">
												<xsl:variable name="var122_isActive" as="item()*" select="@isActive"/>
												<xsl:variable name="var123_effectiveDate" as="item()*" select="@effectiveDate"/>
												<xsl:variable name="var124_isDefining" as="item()*" select="@isDefining"/>
												<xsl:variable name="var125_associationInstanceId" as="item()*" select="@associationInstanceId"/>
												<xsl:variable name="var126_isInferred" as="item()*" select="@isInferred"/>
												<xsl:variable name="var127_expirationDate" as="item()*" select="@expirationDate"/>
												<xsl:variable name="var128_status" as="item()*" select="@status"/>
												<lgRel:targetData xsl:exclude-result-prefixes="lgRel">
													<xsl:if test="fn:exists($var122_isActive)">
														<xsl:attribute name="isActive" namespace="" select="xs:string(xs:boolean(fn:string($var122_isActive)))"/>
													</xsl:if>
													<xsl:if test="fn:exists($var128_status)">
														<xsl:attribute name="status" namespace="" select="fn:string($var128_status)"/>
													</xsl:if>
													<xsl:if test="fn:exists($var123_effectiveDate)">
														<xsl:attribute name="effectiveDate" namespace="" select="xs:string(xs:dateTime(fn:string($var123_effectiveDate)))"/>
													</xsl:if>
													<xsl:if test="fn:exists($var127_expirationDate)">
														<xsl:attribute name="expirationDate" namespace="" select="xs:string(xs:dateTime(fn:string($var127_expirationDate)))"/>
													</xsl:if>
													<xsl:if test="fn:exists($var125_associationInstanceId)">
														<xsl:attribute name="associationInstanceId" namespace="" select="fn:string($var125_associationInstanceId)"/>
													</xsl:if>
													<xsl:if test="fn:exists($var126_isInferred)">
														<xsl:attribute name="isInferred" namespace="" select="xs:string(xs:boolean(fn:string($var126_isInferred)))"/>
													</xsl:if>
													<xsl:if test="fn:exists($var124_isDefining)">
														<xsl:attribute name="isDefining" namespace="" select="xs:string(xs:boolean(fn:string($var124_isDefining)))"/>
													</xsl:if>
													<xsl:for-each select="ns1:owner">
														<lgCommon:owner xsl:exclude-result-prefixes="lgCommon">
															<xsl:sequence select="fn:string(.)"/>
														</lgCommon:owner>
													</xsl:for-each>
													<xsl:for-each select="ns1:entryState">
														<lgCommon:entryState xsl:exclude-result-prefixes="lgCommon">
															<xsl:call-template name="tbf:tbf1_entryState">
																<xsl:with-param name="input" select="." as="node()"/>
															</xsl:call-template>
														</lgCommon:entryState>
													</xsl:for-each>
													<xsl:for-each select="ns4:usageContext">
														<lgRel:usageContext xsl:exclude-result-prefixes="lgRel">
															<xsl:sequence select="fn:string(.)"/>
														</lgRel:usageContext>
													</xsl:for-each>
													<xsl:for-each select="ns4:associationQualification">
														<lgRel:associationQualification xsl:exclude-result-prefixes="lgRel">
															<xsl:attribute name="associationQualifier" namespace="" select="fn:string(@associationQualifier)"/>
															<xsl:for-each select="ns4:qualifierText">
																<lgRel:qualifierText xsl:exclude-result-prefixes="lgRel">
																	<xsl:call-template name="tbf:tbf3_text">
																		<xsl:with-param name="input" select="." as="node()"/>
																	</xsl:call-template>
																</lgRel:qualifierText>
															</xsl:for-each>
														</lgRel:associationQualification>
													</xsl:for-each>
													<xsl:for-each select="ns4:associationDataText">
														<lgRel:associationDataText xsl:exclude-result-prefixes="lgRel">
															<xsl:call-template name="tbf:tbf3_text">
																<xsl:with-param name="input" select="." as="node()"/>
															</xsl:call-template>
														</lgRel:associationDataText>
													</xsl:for-each>
												</lgRel:targetData>
											</xsl:for-each>
										</lgRel:source>
									</xsl:for-each>
								</lgRel:associationPredicate>
							</xsl:for-each>
						</relations>
					</xsl:for-each>
				</codingScheme>
			</xsl:for-each>
		</codingSchemes>
	</xsl:template>
</xsl:stylesheet>
