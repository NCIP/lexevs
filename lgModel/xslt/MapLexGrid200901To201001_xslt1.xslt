<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ns0="http://LexGrid.org/schema/2009/01/LexGrid/codingSchemes" xmlns:ns1="http://LexGrid.org/schema/2009/01/LexGrid/commonTypes" xmlns:ns2="http://LexGrid.org/schema/2009/01/LexGrid/concepts" xmlns:ns3="http://LexGrid.org/schema/2009/01/LexGrid/naming" xmlns:ns4="http://LexGrid.org/schema/2009/01/LexGrid/relations" xmlns:tbf="http://www.altova.com/MapForce/UDF/tbf" xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="ns0 ns1 ns2 ns3 ns4 tbf xs">
	<xsl:template name="tbf:tbf1_entryState">
		<xsl:param name="input" select="/.."/>
		<xsl:for-each select="$input/@containingRevision">
			<xsl:attribute name="containingRevision">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:for-each select="$input/@relativeOrder">
			<xsl:attribute name="relativeOrder">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:for-each select="$input/@changeType">
			<xsl:attribute name="changeType">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:for-each select="$input/@prevRevision">
			<xsl:attribute name="prevRevision">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="tbf:tbf2_source">
		<xsl:param name="input" select="/.."/>
		<xsl:for-each select="$input/@subRef">
			<xsl:attribute name="subRef">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:for-each select="$input/@role">
			<xsl:attribute name="role">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:value-of select="string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf3_text">
		<xsl:param name="input" select="/.."/>
		<xsl:for-each select="$input/@dataType">
			<xsl:attribute name="dataType">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:value-of select="string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf4_supportedAssociationQualifier">
		<xsl:param name="input" select="/.."/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:value-of select="string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf5_supportedCodingScheme">
		<xsl:param name="input" select="/.."/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:for-each select="$input/@isImported">
			<xsl:attribute name="isImported">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:value-of select="string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf6_supportedContainerName">
		<xsl:param name="input" select="/.."/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:value-of select="string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf7_supportedContext">
		<xsl:param name="input" select="/.."/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:value-of select="string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf8_supportedDataType">
		<xsl:param name="input" select="/.."/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:value-of select="string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf9_supportedDegreeOfFidelity">
		<xsl:param name="input" select="/.."/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:value-of select="string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf10_supportedEntityType">
		<xsl:param name="input" select="/.."/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:value-of select="string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf11_supportedHierarchy">
		<xsl:param name="input" select="/.."/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:for-each select="$input/@associationNames">
			<xsl:attribute name="associationNames">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:for-each select="$input/@rootCode">
			<xsl:attribute name="rootCode">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:for-each select="$input/@isForwardNavigable">
			<xsl:attribute name="isForwardNavigable">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:value-of select="string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf12_supportedLanguage">
		<xsl:param name="input" select="/.."/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:value-of select="string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf13_supportedNamespace">
		<xsl:param name="input" select="/.."/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:for-each select="$input/@equivalentCodingScheme">
			<xsl:attribute name="equivalentCodingScheme">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:value-of select="string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf14_supportedPropertyType">
		<xsl:param name="input" select="/.."/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:value-of select="string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf15_supportedPropertyLink">
		<xsl:param name="input" select="/.."/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:value-of select="string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf16_supportedPropertyQualifier">
		<xsl:param name="input" select="/.."/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:value-of select="string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf17_supportedPropertyQualifierType">
		<xsl:param name="input" select="/.."/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:value-of select="string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf18_supportedRepresentationalForm">
		<xsl:param name="input" select="/.."/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:value-of select="string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf19_supportedSortOrder">
		<xsl:param name="input" select="/.."/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:value-of select="string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf20_supportedSource">
		<xsl:param name="input" select="/.."/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:for-each select="$input/@assemblyRule">
			<xsl:attribute name="assemblyRule">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:value-of select="string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf21_supportedSourceRole">
		<xsl:param name="input" select="/.."/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:value-of select="string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf22_supportedStatus">
		<xsl:param name="input" select="/.."/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:value-of select="string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf23_propertyLink">
		<xsl:param name="input" select="/.."/>
		<xsl:for-each select="$input/@sourceProperty">
			<xsl:attribute name="sourceProperty">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:for-each select="$input/@propertyLink">
			<xsl:attribute name="propertyLink">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:for-each select="$input/@targetProperty">
			<xsl:attribute name="targetProperty">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
	</xsl:template>
	<xsl:output method="xml" encoding="UTF-8" indent="yes"/>
	<xsl:template match="/">
		<codingSchemes xmlns="http://LexGrid.org/schema/2010/01/LexGrid/codingSchemes" xmlns:lgBuiltin="http://LexGrid.org/schema/2010/01/LexGrid/builtins" xmlns:lgCommon="http://LexGrid.org/schema/2010/01/LexGrid/commonTypes" xmlns:lgCon="http://LexGrid.org/schema/2010/01/LexGrid/concepts" xmlns:lgNaming="http://LexGrid.org/schema/2010/01/LexGrid/naming" xmlns:lgRel="http://LexGrid.org/schema/2010/01/LexGrid/relations" xmlns:lgVD="http://LexGrid.org/schema/2010/01/LexGrid/valueSets" xmlns:lgVer="http://LexGrid.org/schema/2010/01/LexGrid/versions">
			<xsl:attribute name="xsi:schemaLocation" namespace="http://www.w3.org/2001/XMLSchema-instance">
				<xsl:value-of select="'http://LexGrid.org/schema/2010/01/LexGrid/codingSchemes http://LexGrid.org/schema/2010/01/LexGrid/codingSchemes.xsd'"/>
			</xsl:attribute>
			<xsl:for-each select="ns0:codingSchemes/ns0:codingScheme">
				<xsl:variable name="var1_approxNumConcepts" select="@approxNumConcepts"/>
				<xsl:variable name="var2_expirationDate" select="@expirationDate"/>
				<xsl:variable name="var3_defaultLanguage" select="@defaultLanguage"/>
				<xsl:variable name="var4_isActive" select="@isActive"/>
				<xsl:variable name="var5_status" select="@status"/>
				<xsl:variable name="var6_formalName" select="@formalName"/>
				<xsl:variable name="var7_effectiveDate" select="@effectiveDate"/>
				<xsl:variable name="var8_relations" select="ns0:relations"/>
				<xsl:variable name="var9_mappings" select="ns0:mappings"/>
				<codingScheme>
					<xsl:if test="string(boolean($var4_isActive)) != 'false'">
						<xsl:attribute name="isActive" namespace="">
							<xsl:value-of select="string(((normalize-space(string($var4_isActive)) = 'true') or (normalize-space(string($var4_isActive)) = '1')))"/>
						</xsl:attribute>
					</xsl:if>
					<xsl:if test="string(boolean($var5_status)) != 'false'">
						<xsl:attribute name="status" namespace="">
							<xsl:value-of select="string($var5_status)"/>
						</xsl:attribute>
					</xsl:if>
					<xsl:if test="string(boolean($var7_effectiveDate)) != 'false'">
						<xsl:attribute name="effectiveDate" namespace="">
							<xsl:value-of select="string($var7_effectiveDate)"/>
						</xsl:attribute>
					</xsl:if>
					<xsl:if test="string(boolean($var2_expirationDate)) != 'false'">
						<xsl:attribute name="expirationDate" namespace="">
							<xsl:value-of select="string($var2_expirationDate)"/>
						</xsl:attribute>
					</xsl:if>
					<xsl:attribute name="codingSchemeName" namespace="">
						<xsl:value-of select="string(@codingSchemeName)"/>
					</xsl:attribute>
					<xsl:attribute name="codingSchemeURI" namespace="">
						<xsl:value-of select="string(@codingSchemeURI)"/>
					</xsl:attribute>
					<xsl:if test="string(boolean($var6_formalName)) != 'false'">
						<xsl:attribute name="formalName" namespace="">
							<xsl:value-of select="string($var6_formalName)"/>
						</xsl:attribute>
					</xsl:if>
					<xsl:if test="string(boolean($var3_defaultLanguage)) != 'false'">
						<xsl:attribute name="defaultLanguage" namespace="">
							<xsl:value-of select="string($var3_defaultLanguage)"/>
						</xsl:attribute>
					</xsl:if>
					<xsl:if test="string(boolean($var1_approxNumConcepts)) != 'false'">
						<xsl:attribute name="approxNumConcepts" namespace="">
							<xsl:value-of select="string(number(string($var1_approxNumConcepts)))"/>
						</xsl:attribute>
					</xsl:if>
					<xsl:attribute name="representsVersion" namespace="">
						<xsl:value-of select="string(@representsVersion)"/>
					</xsl:attribute>
					<xsl:for-each select="ns1:owner">
						<lgCommon:owner xsl:exclude-result-prefixes="lgCommon">
							<xsl:value-of select="string(.)"/>
						</lgCommon:owner>
					</xsl:for-each>
					<xsl:for-each select="ns1:entryState">
						<lgCommon:entryState xsl:exclude-result-prefixes="lgCommon">
							<xsl:call-template name="tbf:tbf1_entryState">
								<xsl:with-param name="input" select="."/>
							</xsl:call-template>
						</lgCommon:entryState>
					</xsl:for-each>
					<xsl:for-each select="ns1:entityDescription">
						<lgCommon:entityDescription xsl:exclude-result-prefixes="lgCommon">
							<xsl:for-each select="node()[boolean(self::text())]">
								<xsl:value-of select="string(.)"/>
							</xsl:for-each>
						</lgCommon:entityDescription>
					</xsl:for-each>
					<xsl:for-each select="ns0:localName">
						<localName>
							<xsl:value-of select="string(.)"/>
						</localName>
					</xsl:for-each>
					<xsl:for-each select="ns0:source">
						<source>
							<xsl:call-template name="tbf:tbf2_source">
								<xsl:with-param name="input" select="."/>
							</xsl:call-template>
						</source>
					</xsl:for-each>
					<xsl:for-each select="ns0:copyright">
						<copyright>
							<xsl:call-template name="tbf:tbf3_text">
								<xsl:with-param name="input" select="."/>
							</xsl:call-template>
						</copyright>
					</xsl:for-each>
					<mappings>
						<xsl:for-each select="$var9_mappings/ns3:supportedAssociation">
							<xsl:variable name="var10_uri" select="@uri"/>
							<lgNaming:supportedAssociation xsl:exclude-result-prefixes="lgNaming">
								<xsl:attribute name="localId" namespace="">
									<xsl:value-of select="string(@localId)"/>
								</xsl:attribute>
								<xsl:if test="string(boolean($var10_uri)) != 'false'">
									<xsl:attribute name="uri" namespace="">
										<xsl:value-of select="string($var10_uri)"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:value-of select="string(.)"/>
							</lgNaming:supportedAssociation>
						</xsl:for-each>
						<xsl:for-each select="$var9_mappings/ns3:supportedAssociationQualifier">
							<lgNaming:supportedAssociationQualifier xsl:exclude-result-prefixes="lgNaming">
								<xsl:call-template name="tbf:tbf4_supportedAssociationQualifier">
									<xsl:with-param name="input" select="."/>
								</xsl:call-template>
							</lgNaming:supportedAssociationQualifier>
						</xsl:for-each>
						<xsl:for-each select="$var9_mappings/ns3:supportedCodingScheme">
							<lgNaming:supportedCodingScheme xsl:exclude-result-prefixes="lgNaming">
								<xsl:call-template name="tbf:tbf5_supportedCodingScheme">
									<xsl:with-param name="input" select="."/>
								</xsl:call-template>
							</lgNaming:supportedCodingScheme>
						</xsl:for-each>
						<xsl:for-each select="$var9_mappings/ns3:supportedContainer">
							<lgNaming:supportedContainerName xsl:exclude-result-prefixes="lgNaming">
								<xsl:call-template name="tbf:tbf6_supportedContainerName">
									<xsl:with-param name="input" select="."/>
								</xsl:call-template>
							</lgNaming:supportedContainerName>
						</xsl:for-each>
						<xsl:for-each select="$var9_mappings/ns3:supportedContext">
							<lgNaming:supportedContext xsl:exclude-result-prefixes="lgNaming">
								<xsl:call-template name="tbf:tbf7_supportedContext">
									<xsl:with-param name="input" select="."/>
								</xsl:call-template>
							</lgNaming:supportedContext>
						</xsl:for-each>
						<xsl:for-each select="$var9_mappings/ns3:supportedDataType">
							<lgNaming:supportedDataType xsl:exclude-result-prefixes="lgNaming">
								<xsl:call-template name="tbf:tbf8_supportedDataType">
									<xsl:with-param name="input" select="."/>
								</xsl:call-template>
							</lgNaming:supportedDataType>
						</xsl:for-each>
						<xsl:for-each select="$var9_mappings/ns3:supportedDegreeOfFidelity">
							<lgNaming:supportedDegreeOfFidelity xsl:exclude-result-prefixes="lgNaming">
								<xsl:call-template name="tbf:tbf9_supportedDegreeOfFidelity">
									<xsl:with-param name="input" select="."/>
								</xsl:call-template>
							</lgNaming:supportedDegreeOfFidelity>
						</xsl:for-each>
						<xsl:for-each select="$var9_mappings/ns3:supportedEntityType">
							<lgNaming:supportedEntityType xsl:exclude-result-prefixes="lgNaming">
								<xsl:call-template name="tbf:tbf10_supportedEntityType">
									<xsl:with-param name="input" select="."/>
								</xsl:call-template>
							</lgNaming:supportedEntityType>
						</xsl:for-each>
						<xsl:for-each select="$var9_mappings/ns3:supportedHierarchy">
							<lgNaming:supportedHierarchy xsl:exclude-result-prefixes="lgNaming">
								<xsl:call-template name="tbf:tbf11_supportedHierarchy">
									<xsl:with-param name="input" select="."/>
								</xsl:call-template>
							</lgNaming:supportedHierarchy>
						</xsl:for-each>
						<xsl:for-each select="$var9_mappings/ns3:supportedLanguage">
							<lgNaming:supportedLanguage xsl:exclude-result-prefixes="lgNaming">
								<xsl:call-template name="tbf:tbf12_supportedLanguage">
									<xsl:with-param name="input" select="."/>
								</xsl:call-template>
							</lgNaming:supportedLanguage>
						</xsl:for-each>
						<xsl:for-each select="$var9_mappings/ns3:supportedNamespace">
							<lgNaming:supportedNamespace xsl:exclude-result-prefixes="lgNaming">
								<xsl:call-template name="tbf:tbf13_supportedNamespace">
									<xsl:with-param name="input" select="."/>
								</xsl:call-template>
							</lgNaming:supportedNamespace>
						</xsl:for-each>
						<xsl:for-each select="$var9_mappings/ns3:supportedProperty">
							<xsl:variable name="var11_uri" select="@uri"/>
							<lgNaming:supportedProperty xsl:exclude-result-prefixes="lgNaming">
								<xsl:attribute name="localId" namespace="">
									<xsl:value-of select="string(@localId)"/>
								</xsl:attribute>
								<xsl:if test="string(boolean($var11_uri)) != 'false'">
									<xsl:attribute name="uri" namespace="">
										<xsl:value-of select="string($var11_uri)"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:value-of select="string(.)"/>
							</lgNaming:supportedProperty>
						</xsl:for-each>
						<xsl:for-each select="$var9_mappings/ns3:supportedPropertyType">
							<lgNaming:supportedPropertyType xsl:exclude-result-prefixes="lgNaming">
								<xsl:call-template name="tbf:tbf14_supportedPropertyType">
									<xsl:with-param name="input" select="."/>
								</xsl:call-template>
							</lgNaming:supportedPropertyType>
						</xsl:for-each>
						<xsl:for-each select="$var9_mappings/ns3:supportedPropertyLink">
							<lgNaming:supportedPropertyLink xsl:exclude-result-prefixes="lgNaming">
								<xsl:call-template name="tbf:tbf15_supportedPropertyLink">
									<xsl:with-param name="input" select="."/>
								</xsl:call-template>
							</lgNaming:supportedPropertyLink>
						</xsl:for-each>
						<xsl:for-each select="$var9_mappings/ns3:supportedPropertyQualifier">
							<lgNaming:supportedPropertyQualifier xsl:exclude-result-prefixes="lgNaming">
								<xsl:call-template name="tbf:tbf16_supportedPropertyQualifier">
									<xsl:with-param name="input" select="."/>
								</xsl:call-template>
							</lgNaming:supportedPropertyQualifier>
						</xsl:for-each>
						<xsl:for-each select="$var9_mappings/ns3:supportedPropertyQualifierType">
							<lgNaming:supportedPropertyQualifierType xsl:exclude-result-prefixes="lgNaming">
								<xsl:call-template name="tbf:tbf17_supportedPropertyQualifierType">
									<xsl:with-param name="input" select="."/>
								</xsl:call-template>
							</lgNaming:supportedPropertyQualifierType>
						</xsl:for-each>
						<xsl:for-each select="$var9_mappings/ns3:supportedRepresentationalForm">
							<lgNaming:supportedRepresentationalForm xsl:exclude-result-prefixes="lgNaming">
								<xsl:call-template name="tbf:tbf18_supportedRepresentationalForm">
									<xsl:with-param name="input" select="."/>
								</xsl:call-template>
							</lgNaming:supportedRepresentationalForm>
						</xsl:for-each>
						<xsl:for-each select="$var9_mappings/ns3:supportedSortOrder">
							<lgNaming:supportedSortOrder xsl:exclude-result-prefixes="lgNaming">
								<xsl:call-template name="tbf:tbf19_supportedSortOrder">
									<xsl:with-param name="input" select="."/>
								</xsl:call-template>
							</lgNaming:supportedSortOrder>
						</xsl:for-each>
						<xsl:for-each select="$var9_mappings/ns3:supportedSource">
							<lgNaming:supportedSource xsl:exclude-result-prefixes="lgNaming">
								<xsl:call-template name="tbf:tbf20_supportedSource">
									<xsl:with-param name="input" select="."/>
								</xsl:call-template>
							</lgNaming:supportedSource>
						</xsl:for-each>
						<xsl:for-each select="$var9_mappings/ns3:supportedSourceRole">
							<lgNaming:supportedSourceRole xsl:exclude-result-prefixes="lgNaming">
								<xsl:call-template name="tbf:tbf21_supportedSourceRole">
									<xsl:with-param name="input" select="."/>
								</xsl:call-template>
							</lgNaming:supportedSourceRole>
						</xsl:for-each>
						<xsl:for-each select="$var9_mappings/ns3:supportedStatus">
							<lgNaming:supportedStatus xsl:exclude-result-prefixes="lgNaming">
								<xsl:call-template name="tbf:tbf22_supportedStatus">
									<xsl:with-param name="input" select="."/>
								</xsl:call-template>
							</lgNaming:supportedStatus>
						</xsl:for-each>
					</mappings>
					<xsl:for-each select="ns0:properties">
						<properties>
							<xsl:for-each select="ns1:property">
								<xsl:variable name="var12_effectiveDate" select="@effectiveDate"/>
								<xsl:variable name="var13_propertyType" select="@propertyType"/>
								<xsl:variable name="var14_isActive" select="@isActive"/>
								<xsl:variable name="var15_language" select="@language"/>
								<xsl:variable name="var16_expirationDate" select="@expirationDate"/>
								<xsl:variable name="var17_propertyId" select="@propertyId"/>
								<xsl:variable name="var18_status" select="@status"/>
								<lgCommon:property xsl:exclude-result-prefixes="lgCommon">
									<xsl:if test="string(boolean($var14_isActive)) != 'false'">
										<xsl:attribute name="isActive" namespace="">
											<xsl:value-of select="string(((normalize-space(string($var14_isActive)) = 'true') or (normalize-space(string($var14_isActive)) = '1')))"/>
										</xsl:attribute>
									</xsl:if>
									<xsl:if test="string(boolean($var18_status)) != 'false'">
										<xsl:attribute name="status" namespace="">
											<xsl:value-of select="string($var18_status)"/>
										</xsl:attribute>
									</xsl:if>
									<xsl:if test="string(boolean($var12_effectiveDate)) != 'false'">
										<xsl:attribute name="effectiveDate" namespace="">
											<xsl:value-of select="string($var12_effectiveDate)"/>
										</xsl:attribute>
									</xsl:if>
									<xsl:if test="string(boolean($var16_expirationDate)) != 'false'">
										<xsl:attribute name="expirationDate" namespace="">
											<xsl:value-of select="string($var16_expirationDate)"/>
										</xsl:attribute>
									</xsl:if>
									<xsl:attribute name="propertyName" namespace="">
										<xsl:value-of select="string(@propertyName)"/>
									</xsl:attribute>
									<xsl:if test="string(boolean($var17_propertyId)) != 'false'">
										<xsl:attribute name="propertyId" namespace="">
											<xsl:value-of select="string($var17_propertyId)"/>
										</xsl:attribute>
									</xsl:if>
									<xsl:if test="string(boolean($var13_propertyType)) != 'false'">
										<xsl:attribute name="propertyType" namespace="">
											<xsl:value-of select="string($var13_propertyType)"/>
										</xsl:attribute>
									</xsl:if>
									<xsl:if test="string(boolean($var15_language)) != 'false'">
										<xsl:attribute name="language" namespace="">
											<xsl:value-of select="string($var15_language)"/>
										</xsl:attribute>
									</xsl:if>
									<xsl:for-each select="ns1:owner">
										<lgCommon:owner xsl:exclude-result-prefixes="lgCommon">
											<xsl:value-of select="string(.)"/>
										</lgCommon:owner>
									</xsl:for-each>
									<xsl:for-each select="ns1:entryState">
										<lgCommon:entryState xsl:exclude-result-prefixes="lgCommon">
											<xsl:call-template name="tbf:tbf1_entryState">
												<xsl:with-param name="input" select="."/>
											</xsl:call-template>
										</lgCommon:entryState>
									</xsl:for-each>
									<xsl:for-each select="ns1:source">
										<lgCommon:source xsl:exclude-result-prefixes="lgCommon">
											<xsl:call-template name="tbf:tbf2_source">
												<xsl:with-param name="input" select="."/>
											</xsl:call-template>
										</lgCommon:source>
									</xsl:for-each>
									<xsl:for-each select="ns1:usageContext">
										<lgCommon:usageContext xsl:exclude-result-prefixes="lgCommon">
											<xsl:value-of select="string(.)"/>
										</lgCommon:usageContext>
									</xsl:for-each>
									<xsl:for-each select="ns1:propertyQualifier">
										<xsl:variable name="var19_propertyQualifierType" select="@propertyQualifierType"/>
										<lgCommon:propertyQualifier xsl:exclude-result-prefixes="lgCommon">
											<xsl:attribute name="propertyQualifierName" namespace="">
												<xsl:value-of select="string(@propertyQualifierName)"/>
											</xsl:attribute>
											<xsl:if test="string(boolean($var19_propertyQualifierType)) != 'false'">
												<xsl:attribute name="propertyQualifierType" namespace="">
													<xsl:value-of select="string($var19_propertyQualifierType)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:for-each select="ns1:value">
												<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf3_text">
														<xsl:with-param name="input" select="."/>
													</xsl:call-template>
												</lgCommon:value>
											</xsl:for-each>
										</lgCommon:propertyQualifier>
									</xsl:for-each>
									<xsl:for-each select="ns1:value">
										<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
											<xsl:call-template name="tbf:tbf3_text">
												<xsl:with-param name="input" select="."/>
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
								<xsl:variable name="var20_isAnonymous" select="@isAnonymous"/>
								<xsl:variable name="var21_isDefined" select="@isDefined"/>
								<xsl:variable name="var22_expirationDate" select="@expirationDate"/>
								<xsl:variable name="var23_status" select="@status"/>
								<xsl:variable name="var24_isActive" select="@isActive"/>
								<xsl:variable name="var25_effectiveDate" select="@effectiveDate"/>
								<xsl:variable name="var26_entityCodeNamespace" select="@entityCodeNamespace"/>
								<lgCon:entity xsl:exclude-result-prefixes="lgCon">
									<xsl:if test="string(boolean($var24_isActive)) != 'false'">
										<xsl:attribute name="isActive" namespace="">
											<xsl:value-of select="string(((normalize-space(string($var24_isActive)) = 'true') or (normalize-space(string($var24_isActive)) = '1')))"/>
										</xsl:attribute>
									</xsl:if>
									<xsl:if test="string(boolean($var23_status)) != 'false'">
										<xsl:attribute name="status" namespace="">
											<xsl:value-of select="string($var23_status)"/>
										</xsl:attribute>
									</xsl:if>
									<xsl:if test="string(boolean($var25_effectiveDate)) != 'false'">
										<xsl:attribute name="effectiveDate" namespace="">
											<xsl:value-of select="string($var25_effectiveDate)"/>
										</xsl:attribute>
									</xsl:if>
									<xsl:if test="string(boolean($var22_expirationDate)) != 'false'">
										<xsl:attribute name="expirationDate" namespace="">
											<xsl:value-of select="string($var22_expirationDate)"/>
										</xsl:attribute>
									</xsl:if>
									<xsl:attribute name="entityCode" namespace="">
										<xsl:value-of select="string(@entityCode)"/>
									</xsl:attribute>
									<xsl:if test="string(boolean($var26_entityCodeNamespace)) != 'false'">
										<xsl:attribute name="entityCodeNamespace" namespace="">
											<xsl:value-of select="string($var26_entityCodeNamespace)"/>
										</xsl:attribute>
									</xsl:if>
									<xsl:if test="string(boolean($var20_isAnonymous)) != 'false'">
										<xsl:attribute name="isAnonymous" namespace="">
											<xsl:value-of select="string(((normalize-space(string($var20_isAnonymous)) = 'true') or (normalize-space(string($var20_isAnonymous)) = '1')))"/>
										</xsl:attribute>
									</xsl:if>
									<xsl:if test="string(boolean($var21_isDefined)) != 'false'">
										<xsl:attribute name="isDefined" namespace="">
											<xsl:value-of select="string(((normalize-space(string($var21_isDefined)) = 'true') or (normalize-space(string($var21_isDefined)) = '1')))"/>
										</xsl:attribute>
									</xsl:if>
									<xsl:for-each select="ns1:owner">
										<lgCommon:owner xsl:exclude-result-prefixes="lgCommon">
											<xsl:value-of select="string(.)"/>
										</lgCommon:owner>
									</xsl:for-each>
									<xsl:for-each select="ns1:entryState">
										<lgCommon:entryState xsl:exclude-result-prefixes="lgCommon">
											<xsl:call-template name="tbf:tbf1_entryState">
												<xsl:with-param name="input" select="."/>
											</xsl:call-template>
										</lgCommon:entryState>
									</xsl:for-each>
									<xsl:for-each select="ns1:entityDescription">
										<lgCommon:entityDescription xsl:exclude-result-prefixes="lgCommon">
											<xsl:for-each select="node()[boolean(self::text())]">
												<xsl:value-of select="string(.)"/>
											</xsl:for-each>
										</lgCommon:entityDescription>
									</xsl:for-each>
									<xsl:for-each select="ns2:entityType">
										<lgCon:entityType xsl:exclude-result-prefixes="lgCon">
											<xsl:value-of select="string(.)"/>
										</lgCon:entityType>
									</xsl:for-each>
									<xsl:for-each select="ns2:presentation">
										<xsl:variable name="var27_expirationDate" select="@expirationDate"/>
										<xsl:variable name="var28_isPreferred" select="@isPreferred"/>
										<xsl:variable name="var29_language" select="@language"/>
										<xsl:variable name="var30_isActive" select="@isActive"/>
										<xsl:variable name="var31_degreeOfFidelity" select="@degreeOfFidelity"/>
										<xsl:variable name="var32_effectiveDate" select="@effectiveDate"/>
										<xsl:variable name="var33_propertyType" select="@propertyType"/>
										<xsl:variable name="var34_propertyId" select="@propertyId"/>
										<xsl:variable name="var35_matchIfNoContext" select="@matchIfNoContext"/>
										<xsl:variable name="var36_status" select="@status"/>
										<xsl:variable name="var37_representationalForm" select="@representationalForm"/>
										<lgCon:presentation xsl:exclude-result-prefixes="lgCon">
											<xsl:if test="string(boolean($var30_isActive)) != 'false'">
												<xsl:attribute name="isActive" namespace="">
													<xsl:value-of select="string(((normalize-space(string($var30_isActive)) = 'true') or (normalize-space(string($var30_isActive)) = '1')))"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var36_status)) != 'false'">
												<xsl:attribute name="status" namespace="">
													<xsl:value-of select="string($var36_status)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var32_effectiveDate)) != 'false'">
												<xsl:attribute name="effectiveDate" namespace="">
													<xsl:value-of select="string($var32_effectiveDate)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var27_expirationDate)) != 'false'">
												<xsl:attribute name="expirationDate" namespace="">
													<xsl:value-of select="string($var27_expirationDate)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:attribute name="propertyName" namespace="">
												<xsl:value-of select="string(@propertyName)"/>
											</xsl:attribute>
											<xsl:if test="string(boolean($var34_propertyId)) != 'false'">
												<xsl:attribute name="propertyId" namespace="">
													<xsl:value-of select="string($var34_propertyId)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var33_propertyType)) != 'false'">
												<xsl:attribute name="propertyType" namespace="">
													<xsl:value-of select="string($var33_propertyType)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var29_language)) != 'false'">
												<xsl:attribute name="language" namespace="">
													<xsl:value-of select="string($var29_language)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var28_isPreferred)) != 'false'">
												<xsl:attribute name="isPreferred" namespace="">
													<xsl:value-of select="string(((normalize-space(string($var28_isPreferred)) = 'true') or (normalize-space(string($var28_isPreferred)) = '1')))"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var31_degreeOfFidelity)) != 'false'">
												<xsl:attribute name="degreeOfFidelity" namespace="">
													<xsl:value-of select="string($var31_degreeOfFidelity)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var35_matchIfNoContext)) != 'false'">
												<xsl:attribute name="matchIfNoContext" namespace="">
													<xsl:value-of select="string(((normalize-space(string($var35_matchIfNoContext)) = 'true') or (normalize-space(string($var35_matchIfNoContext)) = '1')))"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var37_representationalForm)) != 'false'">
												<xsl:attribute name="representationalForm" namespace="">
													<xsl:value-of select="string($var37_representationalForm)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:for-each select="ns1:owner">
												<lgCommon:owner xsl:exclude-result-prefixes="lgCommon">
													<xsl:value-of select="string(.)"/>
												</lgCommon:owner>
											</xsl:for-each>
											<xsl:for-each select="ns1:entryState">
												<lgCommon:entryState xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf1_entryState">
														<xsl:with-param name="input" select="."/>
													</xsl:call-template>
												</lgCommon:entryState>
											</xsl:for-each>
											<xsl:for-each select="ns1:source">
												<lgCommon:source xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf2_source">
														<xsl:with-param name="input" select="."/>
													</xsl:call-template>
												</lgCommon:source>
											</xsl:for-each>
											<xsl:for-each select="ns1:usageContext">
												<lgCommon:usageContext xsl:exclude-result-prefixes="lgCommon">
													<xsl:value-of select="string(.)"/>
												</lgCommon:usageContext>
											</xsl:for-each>
											<xsl:for-each select="ns1:propertyQualifier">
												<xsl:variable name="var38_propertyQualifierType" select="@propertyQualifierType"/>
												<lgCommon:propertyQualifier xsl:exclude-result-prefixes="lgCommon">
													<xsl:attribute name="propertyQualifierName" namespace="">
														<xsl:value-of select="string(@propertyQualifierName)"/>
													</xsl:attribute>
													<xsl:if test="string(boolean($var38_propertyQualifierType)) != 'false'">
														<xsl:attribute name="propertyQualifierType" namespace="">
															<xsl:value-of select="string($var38_propertyQualifierType)"/>
														</xsl:attribute>
													</xsl:if>
													<xsl:for-each select="ns1:value">
														<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
															<xsl:call-template name="tbf:tbf3_text">
																<xsl:with-param name="input" select="."/>
															</xsl:call-template>
														</lgCommon:value>
													</xsl:for-each>
												</lgCommon:propertyQualifier>
											</xsl:for-each>
											<xsl:for-each select="ns1:value">
												<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf3_text">
														<xsl:with-param name="input" select="."/>
													</xsl:call-template>
												</lgCommon:value>
											</xsl:for-each>
										</lgCon:presentation>
									</xsl:for-each>
									<xsl:for-each select="ns2:definition">
										<xsl:variable name="var39_expirationDate" select="@expirationDate"/>
										<xsl:variable name="var40_isActive" select="@isActive"/>
										<xsl:variable name="var41_status" select="@status"/>
										<xsl:variable name="var42_effectiveDate" select="@effectiveDate"/>
										<xsl:variable name="var43_isPreferred" select="@isPreferred"/>
										<xsl:variable name="var44_propertyType" select="@propertyType"/>
										<xsl:variable name="var45_language" select="@language"/>
										<xsl:variable name="var46_propertyId" select="@propertyId"/>
										<lgCon:definition xsl:exclude-result-prefixes="lgCon">
											<xsl:if test="string(boolean($var40_isActive)) != 'false'">
												<xsl:attribute name="isActive" namespace="">
													<xsl:value-of select="string(((normalize-space(string($var40_isActive)) = 'true') or (normalize-space(string($var40_isActive)) = '1')))"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var41_status)) != 'false'">
												<xsl:attribute name="status" namespace="">
													<xsl:value-of select="string($var41_status)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var42_effectiveDate)) != 'false'">
												<xsl:attribute name="effectiveDate" namespace="">
													<xsl:value-of select="string($var42_effectiveDate)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var39_expirationDate)) != 'false'">
												<xsl:attribute name="expirationDate" namespace="">
													<xsl:value-of select="string($var39_expirationDate)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:attribute name="propertyName" namespace="">
												<xsl:value-of select="string(@propertyName)"/>
											</xsl:attribute>
											<xsl:if test="string(boolean($var46_propertyId)) != 'false'">
												<xsl:attribute name="propertyId" namespace="">
													<xsl:value-of select="string($var46_propertyId)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var44_propertyType)) != 'false'">
												<xsl:attribute name="propertyType" namespace="">
													<xsl:value-of select="string($var44_propertyType)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var45_language)) != 'false'">
												<xsl:attribute name="language" namespace="">
													<xsl:value-of select="string($var45_language)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var43_isPreferred)) != 'false'">
												<xsl:attribute name="isPreferred" namespace="">
													<xsl:value-of select="string(((normalize-space(string($var43_isPreferred)) = 'true') or (normalize-space(string($var43_isPreferred)) = '1')))"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:for-each select="ns1:owner">
												<lgCommon:owner xsl:exclude-result-prefixes="lgCommon">
													<xsl:value-of select="string(.)"/>
												</lgCommon:owner>
											</xsl:for-each>
											<xsl:for-each select="ns1:entryState">
												<lgCommon:entryState xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf1_entryState">
														<xsl:with-param name="input" select="."/>
													</xsl:call-template>
												</lgCommon:entryState>
											</xsl:for-each>
											<xsl:for-each select="ns1:source">
												<lgCommon:source xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf2_source">
														<xsl:with-param name="input" select="."/>
													</xsl:call-template>
												</lgCommon:source>
											</xsl:for-each>
											<xsl:for-each select="ns1:usageContext">
												<lgCommon:usageContext xsl:exclude-result-prefixes="lgCommon">
													<xsl:value-of select="string(.)"/>
												</lgCommon:usageContext>
											</xsl:for-each>
											<xsl:for-each select="ns1:propertyQualifier">
												<xsl:variable name="var47_propertyQualifierType" select="@propertyQualifierType"/>
												<lgCommon:propertyQualifier xsl:exclude-result-prefixes="lgCommon">
													<xsl:attribute name="propertyQualifierName" namespace="">
														<xsl:value-of select="string(@propertyQualifierName)"/>
													</xsl:attribute>
													<xsl:if test="string(boolean($var47_propertyQualifierType)) != 'false'">
														<xsl:attribute name="propertyQualifierType" namespace="">
															<xsl:value-of select="string($var47_propertyQualifierType)"/>
														</xsl:attribute>
													</xsl:if>
													<xsl:for-each select="ns1:value">
														<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
															<xsl:call-template name="tbf:tbf3_text">
																<xsl:with-param name="input" select="."/>
															</xsl:call-template>
														</lgCommon:value>
													</xsl:for-each>
												</lgCommon:propertyQualifier>
											</xsl:for-each>
											<xsl:for-each select="ns1:value">
												<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf3_text">
														<xsl:with-param name="input" select="."/>
													</xsl:call-template>
												</lgCommon:value>
											</xsl:for-each>
										</lgCon:definition>
									</xsl:for-each>
									<xsl:for-each select="ns2:comment">
										<xsl:variable name="var48_language" select="@language"/>
										<xsl:variable name="var49_expirationDate" select="@expirationDate"/>
										<xsl:variable name="var50_status" select="@status"/>
										<xsl:variable name="var51_propertyType" select="@propertyType"/>
										<xsl:variable name="var52_propertyId" select="@propertyId"/>
										<xsl:variable name="var53_isActive" select="@isActive"/>
										<xsl:variable name="var54_effectiveDate" select="@effectiveDate"/>
										<lgCon:comment xsl:exclude-result-prefixes="lgCon">
											<xsl:if test="string(boolean($var53_isActive)) != 'false'">
												<xsl:attribute name="isActive" namespace="">
													<xsl:value-of select="string(((normalize-space(string($var53_isActive)) = 'true') or (normalize-space(string($var53_isActive)) = '1')))"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var50_status)) != 'false'">
												<xsl:attribute name="status" namespace="">
													<xsl:value-of select="string($var50_status)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var54_effectiveDate)) != 'false'">
												<xsl:attribute name="effectiveDate" namespace="">
													<xsl:value-of select="string($var54_effectiveDate)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var49_expirationDate)) != 'false'">
												<xsl:attribute name="expirationDate" namespace="">
													<xsl:value-of select="string($var49_expirationDate)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:attribute name="propertyName" namespace="">
												<xsl:value-of select="string(@propertyName)"/>
											</xsl:attribute>
											<xsl:if test="string(boolean($var52_propertyId)) != 'false'">
												<xsl:attribute name="propertyId" namespace="">
													<xsl:value-of select="string($var52_propertyId)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var51_propertyType)) != 'false'">
												<xsl:attribute name="propertyType" namespace="">
													<xsl:value-of select="string($var51_propertyType)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var48_language)) != 'false'">
												<xsl:attribute name="language" namespace="">
													<xsl:value-of select="string($var48_language)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:for-each select="ns1:owner">
												<lgCommon:owner xsl:exclude-result-prefixes="lgCommon">
													<xsl:value-of select="string(.)"/>
												</lgCommon:owner>
											</xsl:for-each>
											<xsl:for-each select="ns1:entryState">
												<lgCommon:entryState xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf1_entryState">
														<xsl:with-param name="input" select="."/>
													</xsl:call-template>
												</lgCommon:entryState>
											</xsl:for-each>
											<xsl:for-each select="ns1:source">
												<lgCommon:source xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf2_source">
														<xsl:with-param name="input" select="."/>
													</xsl:call-template>
												</lgCommon:source>
											</xsl:for-each>
											<xsl:for-each select="ns1:usageContext">
												<lgCommon:usageContext xsl:exclude-result-prefixes="lgCommon">
													<xsl:value-of select="string(.)"/>
												</lgCommon:usageContext>
											</xsl:for-each>
											<xsl:for-each select="ns1:propertyQualifier">
												<xsl:variable name="var55_propertyQualifierType" select="@propertyQualifierType"/>
												<lgCommon:propertyQualifier xsl:exclude-result-prefixes="lgCommon">
													<xsl:attribute name="propertyQualifierName" namespace="">
														<xsl:value-of select="string(@propertyQualifierName)"/>
													</xsl:attribute>
													<xsl:if test="string(boolean($var55_propertyQualifierType)) != 'false'">
														<xsl:attribute name="propertyQualifierType" namespace="">
															<xsl:value-of select="string($var55_propertyQualifierType)"/>
														</xsl:attribute>
													</xsl:if>
													<xsl:for-each select="ns1:value">
														<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
															<xsl:call-template name="tbf:tbf3_text">
																<xsl:with-param name="input" select="."/>
															</xsl:call-template>
														</lgCommon:value>
													</xsl:for-each>
												</lgCommon:propertyQualifier>
											</xsl:for-each>
											<xsl:for-each select="ns1:value">
												<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf3_text">
														<xsl:with-param name="input" select="."/>
													</xsl:call-template>
												</lgCommon:value>
											</xsl:for-each>
										</lgCon:comment>
									</xsl:for-each>
									<xsl:for-each select="ns2:property">
										<xsl:variable name="var56_isActive" select="@isActive"/>
										<xsl:variable name="var57_status" select="@status"/>
										<xsl:variable name="var58_propertyType" select="@propertyType"/>
										<xsl:variable name="var59_propertyId" select="@propertyId"/>
										<xsl:variable name="var60_language" select="@language"/>
										<xsl:variable name="var61_effectiveDate" select="@effectiveDate"/>
										<xsl:variable name="var62_expirationDate" select="@expirationDate"/>
										<lgCon:property xsl:exclude-result-prefixes="lgCon">
											<xsl:if test="string(boolean($var56_isActive)) != 'false'">
												<xsl:attribute name="isActive" namespace="">
													<xsl:value-of select="string(((normalize-space(string($var56_isActive)) = 'true') or (normalize-space(string($var56_isActive)) = '1')))"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var57_status)) != 'false'">
												<xsl:attribute name="status" namespace="">
													<xsl:value-of select="string($var57_status)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var61_effectiveDate)) != 'false'">
												<xsl:attribute name="effectiveDate" namespace="">
													<xsl:value-of select="string($var61_effectiveDate)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var62_expirationDate)) != 'false'">
												<xsl:attribute name="expirationDate" namespace="">
													<xsl:value-of select="string($var62_expirationDate)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:attribute name="propertyName" namespace="">
												<xsl:value-of select="string(@propertyName)"/>
											</xsl:attribute>
											<xsl:if test="string(boolean($var59_propertyId)) != 'false'">
												<xsl:attribute name="propertyId" namespace="">
													<xsl:value-of select="string($var59_propertyId)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var58_propertyType)) != 'false'">
												<xsl:attribute name="propertyType" namespace="">
													<xsl:value-of select="string($var58_propertyType)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var60_language)) != 'false'">
												<xsl:attribute name="language" namespace="">
													<xsl:value-of select="string($var60_language)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:for-each select="ns1:owner">
												<lgCommon:owner xsl:exclude-result-prefixes="lgCommon">
													<xsl:value-of select="string(.)"/>
												</lgCommon:owner>
											</xsl:for-each>
											<xsl:for-each select="ns1:entryState">
												<lgCommon:entryState xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf1_entryState">
														<xsl:with-param name="input" select="."/>
													</xsl:call-template>
												</lgCommon:entryState>
											</xsl:for-each>
											<xsl:for-each select="ns1:source">
												<lgCommon:source xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf2_source">
														<xsl:with-param name="input" select="."/>
													</xsl:call-template>
												</lgCommon:source>
											</xsl:for-each>
											<xsl:for-each select="ns1:usageContext">
												<lgCommon:usageContext xsl:exclude-result-prefixes="lgCommon">
													<xsl:value-of select="string(.)"/>
												</lgCommon:usageContext>
											</xsl:for-each>
											<xsl:for-each select="ns1:propertyQualifier">
												<xsl:variable name="var63_propertyQualifierType" select="@propertyQualifierType"/>
												<lgCommon:propertyQualifier xsl:exclude-result-prefixes="lgCommon">
													<xsl:attribute name="propertyQualifierName" namespace="">
														<xsl:value-of select="string(@propertyQualifierName)"/>
													</xsl:attribute>
													<xsl:if test="string(boolean($var63_propertyQualifierType)) != 'false'">
														<xsl:attribute name="propertyQualifierType" namespace="">
															<xsl:value-of select="string($var63_propertyQualifierType)"/>
														</xsl:attribute>
													</xsl:if>
													<xsl:for-each select="ns1:value">
														<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
															<xsl:call-template name="tbf:tbf3_text">
																<xsl:with-param name="input" select="."/>
															</xsl:call-template>
														</lgCommon:value>
													</xsl:for-each>
												</lgCommon:propertyQualifier>
											</xsl:for-each>
											<xsl:for-each select="ns1:value">
												<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf3_text">
														<xsl:with-param name="input" select="."/>
													</xsl:call-template>
												</lgCommon:value>
											</xsl:for-each>
										</lgCon:property>
									</xsl:for-each>
									<xsl:for-each select="ns2:propertyLink">
										<lgCon:propertyLink xsl:exclude-result-prefixes="lgCon">
											<xsl:call-template name="tbf:tbf23_propertyLink">
												<xsl:with-param name="input" select="."/>
											</xsl:call-template>
										</lgCon:propertyLink>
									</xsl:for-each>
								</lgCon:entity>
							</xsl:for-each>
							<xsl:for-each select="$var8_relations/ns4:association">
								<xsl:variable name="var64_reverseName" select="@reverseName"/>
								<xsl:variable name="var65_isDefined" select="@isDefined"/>
								<xsl:variable name="var66_isActive" select="@isActive"/>
								<xsl:variable name="var67_entityCodeNamespace" select="@entityCodeNamespace"/>
								<xsl:variable name="var68_isNavigable" select="@isNavigable"/>
								<xsl:variable name="var69_effectiveDate" select="@effectiveDate"/>
								<xsl:variable name="var70_forwardName" select="@forwardName"/>
								<xsl:variable name="var71_status" select="@status"/>
								<xsl:variable name="var72_isAnonymous" select="@isAnonymous"/>
								<xsl:variable name="var73_expirationDate" select="@expirationDate"/>
								<xsl:variable name="var74_isTransitive" select="@isTransitive"/>
								<lgCon:associationEntity xsl:exclude-result-prefixes="lgCon">
									<xsl:if test="string(boolean($var66_isActive)) != 'false'">
										<xsl:attribute name="isActive" namespace="">
											<xsl:value-of select="string(((normalize-space(string($var66_isActive)) = 'true') or (normalize-space(string($var66_isActive)) = '1')))"/>
										</xsl:attribute>
									</xsl:if>
									<xsl:if test="string(boolean($var71_status)) != 'false'">
										<xsl:attribute name="status" namespace="">
											<xsl:value-of select="string($var71_status)"/>
										</xsl:attribute>
									</xsl:if>
									<xsl:if test="string(boolean($var69_effectiveDate)) != 'false'">
										<xsl:attribute name="effectiveDate" namespace="">
											<xsl:value-of select="string($var69_effectiveDate)"/>
										</xsl:attribute>
									</xsl:if>
									<xsl:if test="string(boolean($var73_expirationDate)) != 'false'">
										<xsl:attribute name="expirationDate" namespace="">
											<xsl:value-of select="string($var73_expirationDate)"/>
										</xsl:attribute>
									</xsl:if>
									<xsl:attribute name="entityCode" namespace="">
										<xsl:value-of select="string(@entityCode)"/>
									</xsl:attribute>
									<xsl:if test="string(boolean($var67_entityCodeNamespace)) != 'false'">
										<xsl:attribute name="entityCodeNamespace" namespace="">
											<xsl:value-of select="string($var67_entityCodeNamespace)"/>
										</xsl:attribute>
									</xsl:if>
									<xsl:if test="string(boolean($var72_isAnonymous)) != 'false'">
										<xsl:attribute name="isAnonymous" namespace="">
											<xsl:value-of select="string(((normalize-space(string($var72_isAnonymous)) = 'true') or (normalize-space(string($var72_isAnonymous)) = '1')))"/>
										</xsl:attribute>
									</xsl:if>
									<xsl:if test="string(boolean($var65_isDefined)) != 'false'">
										<xsl:attribute name="isDefined" namespace="">
											<xsl:value-of select="string(((normalize-space(string($var65_isDefined)) = 'true') or (normalize-space(string($var65_isDefined)) = '1')))"/>
										</xsl:attribute>
									</xsl:if>
									<xsl:if test="string(boolean($var70_forwardName)) != 'false'">
										<xsl:attribute name="forwardName" namespace="">
											<xsl:value-of select="string($var70_forwardName)"/>
										</xsl:attribute>
									</xsl:if>
									<xsl:if test="string(boolean($var64_reverseName)) != 'false'">
										<xsl:attribute name="reverseName" namespace="">
											<xsl:value-of select="string($var64_reverseName)"/>
										</xsl:attribute>
									</xsl:if>
									<xsl:if test="string(boolean($var68_isNavigable)) != 'false'">
										<xsl:attribute name="isNavigable" namespace="">
											<xsl:value-of select="string(((normalize-space(string($var68_isNavigable)) = 'true') or (normalize-space(string($var68_isNavigable)) = '1')))"/>
										</xsl:attribute>
									</xsl:if>
									<xsl:if test="string(boolean($var74_isTransitive)) != 'false'">
										<xsl:attribute name="isTransitive" namespace="">
											<xsl:value-of select="string(((normalize-space(string($var74_isTransitive)) = 'true') or (normalize-space(string($var74_isTransitive)) = '1')))"/>
										</xsl:attribute>
									</xsl:if>
									<xsl:for-each select="ns1:owner">
										<lgCommon:owner xsl:exclude-result-prefixes="lgCommon">
											<xsl:value-of select="string(.)"/>
										</lgCommon:owner>
									</xsl:for-each>
									<xsl:for-each select="ns1:entryState">
										<lgCommon:entryState xsl:exclude-result-prefixes="lgCommon">
											<xsl:call-template name="tbf:tbf1_entryState">
												<xsl:with-param name="input" select="."/>
											</xsl:call-template>
										</lgCommon:entryState>
									</xsl:for-each>
									<xsl:for-each select="ns1:entityDescription">
										<lgCommon:entityDescription xsl:exclude-result-prefixes="lgCommon">
											<xsl:for-each select="node()[boolean(self::text())]">
												<xsl:value-of select="string(.)"/>
											</xsl:for-each>
										</lgCommon:entityDescription>
									</xsl:for-each>
									<xsl:for-each select="ns2:entityType">
										<lgCon:entityType xsl:exclude-result-prefixes="lgCon">
											<xsl:value-of select="string(.)"/>
										</lgCon:entityType>
									</xsl:for-each>
									<xsl:for-each select="ns2:presentation">
										<xsl:variable name="var75_matchIfNoContext" select="@matchIfNoContext"/>
										<xsl:variable name="var76_propertyType" select="@propertyType"/>
										<xsl:variable name="var77_degreeOfFidelity" select="@degreeOfFidelity"/>
										<xsl:variable name="var78_propertyId" select="@propertyId"/>
										<xsl:variable name="var79_status" select="@status"/>
										<xsl:variable name="var80_isActive" select="@isActive"/>
										<xsl:variable name="var81_isPreferred" select="@isPreferred"/>
										<xsl:variable name="var82_representationalForm" select="@representationalForm"/>
										<xsl:variable name="var83_language" select="@language"/>
										<xsl:variable name="var84_expirationDate" select="@expirationDate"/>
										<xsl:variable name="var85_effectiveDate" select="@effectiveDate"/>
										<lgCon:presentation xsl:exclude-result-prefixes="lgCon">
											<xsl:if test="string(boolean($var80_isActive)) != 'false'">
												<xsl:attribute name="isActive" namespace="">
													<xsl:value-of select="string(((normalize-space(string($var80_isActive)) = 'true') or (normalize-space(string($var80_isActive)) = '1')))"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var79_status)) != 'false'">
												<xsl:attribute name="status" namespace="">
													<xsl:value-of select="string($var79_status)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var85_effectiveDate)) != 'false'">
												<xsl:attribute name="effectiveDate" namespace="">
													<xsl:value-of select="string($var85_effectiveDate)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var84_expirationDate)) != 'false'">
												<xsl:attribute name="expirationDate" namespace="">
													<xsl:value-of select="string($var84_expirationDate)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:attribute name="propertyName" namespace="">
												<xsl:value-of select="string(@propertyName)"/>
											</xsl:attribute>
											<xsl:if test="string(boolean($var78_propertyId)) != 'false'">
												<xsl:attribute name="propertyId" namespace="">
													<xsl:value-of select="string($var78_propertyId)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var76_propertyType)) != 'false'">
												<xsl:attribute name="propertyType" namespace="">
													<xsl:value-of select="string($var76_propertyType)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var83_language)) != 'false'">
												<xsl:attribute name="language" namespace="">
													<xsl:value-of select="string($var83_language)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var81_isPreferred)) != 'false'">
												<xsl:attribute name="isPreferred" namespace="">
													<xsl:value-of select="string(((normalize-space(string($var81_isPreferred)) = 'true') or (normalize-space(string($var81_isPreferred)) = '1')))"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var77_degreeOfFidelity)) != 'false'">
												<xsl:attribute name="degreeOfFidelity" namespace="">
													<xsl:value-of select="string($var77_degreeOfFidelity)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var75_matchIfNoContext)) != 'false'">
												<xsl:attribute name="matchIfNoContext" namespace="">
													<xsl:value-of select="string(((normalize-space(string($var75_matchIfNoContext)) = 'true') or (normalize-space(string($var75_matchIfNoContext)) = '1')))"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var82_representationalForm)) != 'false'">
												<xsl:attribute name="representationalForm" namespace="">
													<xsl:value-of select="string($var82_representationalForm)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:for-each select="ns1:owner">
												<lgCommon:owner xsl:exclude-result-prefixes="lgCommon">
													<xsl:value-of select="string(.)"/>
												</lgCommon:owner>
											</xsl:for-each>
											<xsl:for-each select="ns1:entryState">
												<lgCommon:entryState xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf1_entryState">
														<xsl:with-param name="input" select="."/>
													</xsl:call-template>
												</lgCommon:entryState>
											</xsl:for-each>
											<xsl:for-each select="ns1:source">
												<lgCommon:source xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf2_source">
														<xsl:with-param name="input" select="."/>
													</xsl:call-template>
												</lgCommon:source>
											</xsl:for-each>
											<xsl:for-each select="ns1:usageContext">
												<lgCommon:usageContext xsl:exclude-result-prefixes="lgCommon">
													<xsl:value-of select="string(.)"/>
												</lgCommon:usageContext>
											</xsl:for-each>
											<xsl:for-each select="ns1:propertyQualifier">
												<xsl:variable name="var86_propertyQualifierType" select="@propertyQualifierType"/>
												<lgCommon:propertyQualifier xsl:exclude-result-prefixes="lgCommon">
													<xsl:attribute name="propertyQualifierName" namespace="">
														<xsl:value-of select="string(@propertyQualifierName)"/>
													</xsl:attribute>
													<xsl:if test="string(boolean($var86_propertyQualifierType)) != 'false'">
														<xsl:attribute name="propertyQualifierType" namespace="">
															<xsl:value-of select="string($var86_propertyQualifierType)"/>
														</xsl:attribute>
													</xsl:if>
													<xsl:for-each select="ns1:value">
														<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
															<xsl:call-template name="tbf:tbf3_text">
																<xsl:with-param name="input" select="."/>
															</xsl:call-template>
														</lgCommon:value>
													</xsl:for-each>
												</lgCommon:propertyQualifier>
											</xsl:for-each>
											<xsl:for-each select="ns1:value">
												<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf3_text">
														<xsl:with-param name="input" select="."/>
													</xsl:call-template>
												</lgCommon:value>
											</xsl:for-each>
										</lgCon:presentation>
									</xsl:for-each>
									<xsl:for-each select="ns2:definition">
										<xsl:variable name="var87_propertyId" select="@propertyId"/>
										<xsl:variable name="var88_isPreferred" select="@isPreferred"/>
										<xsl:variable name="var89_isActive" select="@isActive"/>
										<xsl:variable name="var90_propertyType" select="@propertyType"/>
										<xsl:variable name="var91_status" select="@status"/>
										<xsl:variable name="var92_language" select="@language"/>
										<xsl:variable name="var93_effectiveDate" select="@effectiveDate"/>
										<xsl:variable name="var94_expirationDate" select="@expirationDate"/>
										<lgCon:definition xsl:exclude-result-prefixes="lgCon">
											<xsl:if test="string(boolean($var89_isActive)) != 'false'">
												<xsl:attribute name="isActive" namespace="">
													<xsl:value-of select="string(((normalize-space(string($var89_isActive)) = 'true') or (normalize-space(string($var89_isActive)) = '1')))"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var91_status)) != 'false'">
												<xsl:attribute name="status" namespace="">
													<xsl:value-of select="string($var91_status)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var93_effectiveDate)) != 'false'">
												<xsl:attribute name="effectiveDate" namespace="">
													<xsl:value-of select="string($var93_effectiveDate)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var94_expirationDate)) != 'false'">
												<xsl:attribute name="expirationDate" namespace="">
													<xsl:value-of select="string($var94_expirationDate)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:attribute name="propertyName" namespace="">
												<xsl:value-of select="string(@propertyName)"/>
											</xsl:attribute>
											<xsl:if test="string(boolean($var87_propertyId)) != 'false'">
												<xsl:attribute name="propertyId" namespace="">
													<xsl:value-of select="string($var87_propertyId)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var90_propertyType)) != 'false'">
												<xsl:attribute name="propertyType" namespace="">
													<xsl:value-of select="string($var90_propertyType)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var92_language)) != 'false'">
												<xsl:attribute name="language" namespace="">
													<xsl:value-of select="string($var92_language)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var88_isPreferred)) != 'false'">
												<xsl:attribute name="isPreferred" namespace="">
													<xsl:value-of select="string(((normalize-space(string($var88_isPreferred)) = 'true') or (normalize-space(string($var88_isPreferred)) = '1')))"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:for-each select="ns1:owner">
												<lgCommon:owner xsl:exclude-result-prefixes="lgCommon">
													<xsl:value-of select="string(.)"/>
												</lgCommon:owner>
											</xsl:for-each>
											<xsl:for-each select="ns1:entryState">
												<lgCommon:entryState xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf1_entryState">
														<xsl:with-param name="input" select="."/>
													</xsl:call-template>
												</lgCommon:entryState>
											</xsl:for-each>
											<xsl:for-each select="ns1:source">
												<lgCommon:source xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf2_source">
														<xsl:with-param name="input" select="."/>
													</xsl:call-template>
												</lgCommon:source>
											</xsl:for-each>
											<xsl:for-each select="ns1:usageContext">
												<lgCommon:usageContext xsl:exclude-result-prefixes="lgCommon">
													<xsl:value-of select="string(.)"/>
												</lgCommon:usageContext>
											</xsl:for-each>
											<xsl:for-each select="ns1:propertyQualifier">
												<xsl:variable name="var95_propertyQualifierType" select="@propertyQualifierType"/>
												<lgCommon:propertyQualifier xsl:exclude-result-prefixes="lgCommon">
													<xsl:attribute name="propertyQualifierName" namespace="">
														<xsl:value-of select="string(@propertyQualifierName)"/>
													</xsl:attribute>
													<xsl:if test="string(boolean($var95_propertyQualifierType)) != 'false'">
														<xsl:attribute name="propertyQualifierType" namespace="">
															<xsl:value-of select="string($var95_propertyQualifierType)"/>
														</xsl:attribute>
													</xsl:if>
													<xsl:for-each select="ns1:value">
														<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
															<xsl:call-template name="tbf:tbf3_text">
																<xsl:with-param name="input" select="."/>
															</xsl:call-template>
														</lgCommon:value>
													</xsl:for-each>
												</lgCommon:propertyQualifier>
											</xsl:for-each>
											<xsl:for-each select="ns1:value">
												<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf3_text">
														<xsl:with-param name="input" select="."/>
													</xsl:call-template>
												</lgCommon:value>
											</xsl:for-each>
										</lgCon:definition>
									</xsl:for-each>
									<xsl:for-each select="ns2:comment">
										<xsl:variable name="var96_language" select="@language"/>
										<xsl:variable name="var97_isActive" select="@isActive"/>
										<xsl:variable name="var98_effectiveDate" select="@effectiveDate"/>
										<xsl:variable name="var99_expirationDate" select="@expirationDate"/>
										<xsl:variable name="var100_propertyId" select="@propertyId"/>
										<xsl:variable name="var101_propertyType" select="@propertyType"/>
										<xsl:variable name="var102_status" select="@status"/>
										<lgCon:comment xsl:exclude-result-prefixes="lgCon">
											<xsl:if test="string(boolean($var97_isActive)) != 'false'">
												<xsl:attribute name="isActive" namespace="">
													<xsl:value-of select="string(((normalize-space(string($var97_isActive)) = 'true') or (normalize-space(string($var97_isActive)) = '1')))"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var102_status)) != 'false'">
												<xsl:attribute name="status" namespace="">
													<xsl:value-of select="string($var102_status)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var98_effectiveDate)) != 'false'">
												<xsl:attribute name="effectiveDate" namespace="">
													<xsl:value-of select="string($var98_effectiveDate)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var99_expirationDate)) != 'false'">
												<xsl:attribute name="expirationDate" namespace="">
													<xsl:value-of select="string($var99_expirationDate)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:attribute name="propertyName" namespace="">
												<xsl:value-of select="string(@propertyName)"/>
											</xsl:attribute>
											<xsl:if test="string(boolean($var100_propertyId)) != 'false'">
												<xsl:attribute name="propertyId" namespace="">
													<xsl:value-of select="string($var100_propertyId)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var101_propertyType)) != 'false'">
												<xsl:attribute name="propertyType" namespace="">
													<xsl:value-of select="string($var101_propertyType)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var96_language)) != 'false'">
												<xsl:attribute name="language" namespace="">
													<xsl:value-of select="string($var96_language)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:for-each select="ns1:owner">
												<lgCommon:owner xsl:exclude-result-prefixes="lgCommon">
													<xsl:value-of select="string(.)"/>
												</lgCommon:owner>
											</xsl:for-each>
											<xsl:for-each select="ns1:entryState">
												<lgCommon:entryState xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf1_entryState">
														<xsl:with-param name="input" select="."/>
													</xsl:call-template>
												</lgCommon:entryState>
											</xsl:for-each>
											<xsl:for-each select="ns1:source">
												<lgCommon:source xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf2_source">
														<xsl:with-param name="input" select="."/>
													</xsl:call-template>
												</lgCommon:source>
											</xsl:for-each>
											<xsl:for-each select="ns1:usageContext">
												<lgCommon:usageContext xsl:exclude-result-prefixes="lgCommon">
													<xsl:value-of select="string(.)"/>
												</lgCommon:usageContext>
											</xsl:for-each>
											<xsl:for-each select="ns1:propertyQualifier">
												<xsl:variable name="var103_propertyQualifierType" select="@propertyQualifierType"/>
												<lgCommon:propertyQualifier xsl:exclude-result-prefixes="lgCommon">
													<xsl:attribute name="propertyQualifierName" namespace="">
														<xsl:value-of select="string(@propertyQualifierName)"/>
													</xsl:attribute>
													<xsl:if test="string(boolean($var103_propertyQualifierType)) != 'false'">
														<xsl:attribute name="propertyQualifierType" namespace="">
															<xsl:value-of select="string($var103_propertyQualifierType)"/>
														</xsl:attribute>
													</xsl:if>
													<xsl:for-each select="ns1:value">
														<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
															<xsl:call-template name="tbf:tbf3_text">
																<xsl:with-param name="input" select="."/>
															</xsl:call-template>
														</lgCommon:value>
													</xsl:for-each>
												</lgCommon:propertyQualifier>
											</xsl:for-each>
											<xsl:for-each select="ns1:value">
												<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf3_text">
														<xsl:with-param name="input" select="."/>
													</xsl:call-template>
												</lgCommon:value>
											</xsl:for-each>
										</lgCon:comment>
									</xsl:for-each>
									<xsl:for-each select="ns2:property">
										<xsl:variable name="var104_expirationDate" select="@expirationDate"/>
										<xsl:variable name="var105_language" select="@language"/>
										<xsl:variable name="var106_status" select="@status"/>
										<xsl:variable name="var107_propertyType" select="@propertyType"/>
										<xsl:variable name="var108_effectiveDate" select="@effectiveDate"/>
										<xsl:variable name="var109_propertyId" select="@propertyId"/>
										<xsl:variable name="var110_isActive" select="@isActive"/>
										<lgCon:property xsl:exclude-result-prefixes="lgCon">
											<xsl:if test="string(boolean($var110_isActive)) != 'false'">
												<xsl:attribute name="isActive" namespace="">
													<xsl:value-of select="string(((normalize-space(string($var110_isActive)) = 'true') or (normalize-space(string($var110_isActive)) = '1')))"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var106_status)) != 'false'">
												<xsl:attribute name="status" namespace="">
													<xsl:value-of select="string($var106_status)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var108_effectiveDate)) != 'false'">
												<xsl:attribute name="effectiveDate" namespace="">
													<xsl:value-of select="string($var108_effectiveDate)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var104_expirationDate)) != 'false'">
												<xsl:attribute name="expirationDate" namespace="">
													<xsl:value-of select="string($var104_expirationDate)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:attribute name="propertyName" namespace="">
												<xsl:value-of select="string(@propertyName)"/>
											</xsl:attribute>
											<xsl:if test="string(boolean($var109_propertyId)) != 'false'">
												<xsl:attribute name="propertyId" namespace="">
													<xsl:value-of select="string($var109_propertyId)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var107_propertyType)) != 'false'">
												<xsl:attribute name="propertyType" namespace="">
													<xsl:value-of select="string($var107_propertyType)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:if test="string(boolean($var105_language)) != 'false'">
												<xsl:attribute name="language" namespace="">
													<xsl:value-of select="string($var105_language)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:for-each select="ns1:owner">
												<lgCommon:owner xsl:exclude-result-prefixes="lgCommon">
													<xsl:value-of select="string(.)"/>
												</lgCommon:owner>
											</xsl:for-each>
											<xsl:for-each select="ns1:entryState">
												<lgCommon:entryState xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf1_entryState">
														<xsl:with-param name="input" select="."/>
													</xsl:call-template>
												</lgCommon:entryState>
											</xsl:for-each>
											<xsl:for-each select="ns1:source">
												<lgCommon:source xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf2_source">
														<xsl:with-param name="input" select="."/>
													</xsl:call-template>
												</lgCommon:source>
											</xsl:for-each>
											<xsl:for-each select="ns1:usageContext">
												<lgCommon:usageContext xsl:exclude-result-prefixes="lgCommon">
													<xsl:value-of select="string(.)"/>
												</lgCommon:usageContext>
											</xsl:for-each>
											<xsl:for-each select="ns1:propertyQualifier">
												<xsl:variable name="var111_propertyQualifierType" select="@propertyQualifierType"/>
												<lgCommon:propertyQualifier xsl:exclude-result-prefixes="lgCommon">
													<xsl:attribute name="propertyQualifierName" namespace="">
														<xsl:value-of select="string(@propertyQualifierName)"/>
													</xsl:attribute>
													<xsl:if test="string(boolean($var111_propertyQualifierType)) != 'false'">
														<xsl:attribute name="propertyQualifierType" namespace="">
															<xsl:value-of select="string($var111_propertyQualifierType)"/>
														</xsl:attribute>
													</xsl:if>
													<xsl:for-each select="ns1:value">
														<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
															<xsl:call-template name="tbf:tbf3_text">
																<xsl:with-param name="input" select="."/>
															</xsl:call-template>
														</lgCommon:value>
													</xsl:for-each>
												</lgCommon:propertyQualifier>
											</xsl:for-each>
											<xsl:for-each select="ns1:value">
												<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
													<xsl:call-template name="tbf:tbf3_text">
														<xsl:with-param name="input" select="."/>
													</xsl:call-template>
												</lgCommon:value>
											</xsl:for-each>
										</lgCon:property>
									</xsl:for-each>
									<xsl:for-each select="ns2:propertyLink">
										<lgCon:propertyLink xsl:exclude-result-prefixes="lgCon">
											<xsl:call-template name="tbf:tbf23_propertyLink">
												<xsl:with-param name="input" select="."/>
											</xsl:call-template>
										</lgCon:propertyLink>
									</xsl:for-each>
								</lgCon:associationEntity>
							</xsl:for-each>
						</entities>
					</xsl:for-each>
					<xsl:for-each select="$var8_relations">
						<xsl:variable name="var112_containerName" select="@containerName"/>
						<relations>
							<xsl:if test="string(boolean($var112_containerName)) != 'false'">
								<xsl:attribute name="containerName" namespace="">
									<xsl:value-of select="string($var112_containerName)"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:for-each select="ns1:entityDescription">
								<lgCommon:entityDescription xsl:exclude-result-prefixes="lgCommon">
									<xsl:for-each select="node()[boolean(self::text())]">
										<xsl:value-of select="string(.)"/>
									</xsl:for-each>
								</lgCommon:entityDescription>
							</xsl:for-each>
							<xsl:for-each select="ns4:association">
								<lgRel:associationPredicate xsl:exclude-result-prefixes="lgRel">
									<xsl:attribute name="associationName" namespace="">
										<xsl:value-of select="string(@associationName)"/>
									</xsl:attribute>
									<xsl:for-each select="ns4:source">
										<xsl:variable name="var113_sourceEntityCodeNamespace" select="@sourceEntityCodeNamespace"/>
										<lgRel:source xsl:exclude-result-prefixes="lgRel">
											<xsl:if test="string(boolean($var113_sourceEntityCodeNamespace)) != 'false'">
												<xsl:attribute name="sourceEntityCodeNamespace" namespace="">
													<xsl:value-of select="string($var113_sourceEntityCodeNamespace)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:attribute name="sourceEntityCode" namespace="">
												<xsl:value-of select="string(@sourceEntityCode)"/>
											</xsl:attribute>
											<xsl:for-each select="ns4:target">
												<xsl:variable name="var114_effectiveDate" select="@effectiveDate"/>
												<xsl:variable name="var115_status" select="@status"/>
												<xsl:variable name="var116_expirationDate" select="@expirationDate"/>
												<xsl:variable name="var117_associationInstanceId" select="@associationInstanceId"/>
												<xsl:variable name="var118_isDefining" select="@isDefining"/>
												<xsl:variable name="var119_targetEntityCodeNamespace" select="@targetEntityCodeNamespace"/>
												<xsl:variable name="var120_isInferred" select="@isInferred"/>
												<xsl:variable name="var121_isActive" select="@isActive"/>
												<lgRel:target xsl:exclude-result-prefixes="lgRel">
													<xsl:if test="string(boolean($var121_isActive)) != 'false'">
														<xsl:attribute name="isActive" namespace="">
															<xsl:value-of select="string(((normalize-space(string($var121_isActive)) = 'true') or (normalize-space(string($var121_isActive)) = '1')))"/>
														</xsl:attribute>
													</xsl:if>
													<xsl:if test="string(boolean($var115_status)) != 'false'">
														<xsl:attribute name="status" namespace="">
															<xsl:value-of select="string($var115_status)"/>
														</xsl:attribute>
													</xsl:if>
													<xsl:if test="string(boolean($var114_effectiveDate)) != 'false'">
														<xsl:attribute name="effectiveDate" namespace="">
															<xsl:value-of select="string($var114_effectiveDate)"/>
														</xsl:attribute>
													</xsl:if>
													<xsl:if test="string(boolean($var116_expirationDate)) != 'false'">
														<xsl:attribute name="expirationDate" namespace="">
															<xsl:value-of select="string($var116_expirationDate)"/>
														</xsl:attribute>
													</xsl:if>
													<xsl:if test="string(boolean($var117_associationInstanceId)) != 'false'">
														<xsl:attribute name="associationInstanceId" namespace="">
															<xsl:value-of select="string($var117_associationInstanceId)"/>
														</xsl:attribute>
													</xsl:if>
													<xsl:if test="string(boolean($var120_isInferred)) != 'false'">
														<xsl:attribute name="isInferred" namespace="">
															<xsl:value-of select="string(((normalize-space(string($var120_isInferred)) = 'true') or (normalize-space(string($var120_isInferred)) = '1')))"/>
														</xsl:attribute>
													</xsl:if>
													<xsl:if test="string(boolean($var118_isDefining)) != 'false'">
														<xsl:attribute name="isDefining" namespace="">
															<xsl:value-of select="string(((normalize-space(string($var118_isDefining)) = 'true') or (normalize-space(string($var118_isDefining)) = '1')))"/>
														</xsl:attribute>
													</xsl:if>
													<xsl:attribute name="targetEntityCode" namespace="">
														<xsl:value-of select="string(@targetEntityCode)"/>
													</xsl:attribute>
													<xsl:if test="string(boolean($var119_targetEntityCodeNamespace)) != 'false'">
														<xsl:attribute name="targetEntityCodeNamespace" namespace="">
															<xsl:value-of select="string($var119_targetEntityCodeNamespace)"/>
														</xsl:attribute>
													</xsl:if>
													<xsl:for-each select="ns1:owner">
														<lgCommon:owner xsl:exclude-result-prefixes="lgCommon">
															<xsl:value-of select="string(.)"/>
														</lgCommon:owner>
													</xsl:for-each>
													<xsl:for-each select="ns1:entryState">
														<lgCommon:entryState xsl:exclude-result-prefixes="lgCommon">
															<xsl:call-template name="tbf:tbf1_entryState">
																<xsl:with-param name="input" select="."/>
															</xsl:call-template>
														</lgCommon:entryState>
													</xsl:for-each>
													<xsl:for-each select="ns4:usageContext">
														<lgRel:usageContext xsl:exclude-result-prefixes="lgRel">
															<xsl:value-of select="string(.)"/>
														</lgRel:usageContext>
													</xsl:for-each>
													<xsl:for-each select="ns4:associationQualification">
														<lgRel:associationQualification xsl:exclude-result-prefixes="lgRel">
															<xsl:attribute name="associationQualifier" namespace="">
																<xsl:value-of select="string(@associationQualifier)"/>
															</xsl:attribute>
															<xsl:for-each select="ns4:qualifierText">
																<lgRel:qualifierText xsl:exclude-result-prefixes="lgRel">
																	<xsl:call-template name="tbf:tbf3_text">
																		<xsl:with-param name="input" select="."/>
																	</xsl:call-template>
																</lgRel:qualifierText>
															</xsl:for-each>
														</lgRel:associationQualification>
													</xsl:for-each>
												</lgRel:target>
											</xsl:for-each>
											<xsl:for-each select="ns4:targetData">
												<xsl:variable name="var122_status" select="@status"/>
												<xsl:variable name="var123_effectiveDate" select="@effectiveDate"/>
												<xsl:variable name="var124_isActive" select="@isActive"/>
												<xsl:variable name="var125_expirationDate" select="@expirationDate"/>
												<xsl:variable name="var126_associationInstanceId" select="@associationInstanceId"/>
												<xsl:variable name="var127_isInferred" select="@isInferred"/>
												<xsl:variable name="var128_isDefining" select="@isDefining"/>
												<lgRel:targetData xsl:exclude-result-prefixes="lgRel">
													<xsl:if test="string(boolean($var124_isActive)) != 'false'">
														<xsl:attribute name="isActive" namespace="">
															<xsl:value-of select="string(((normalize-space(string($var124_isActive)) = 'true') or (normalize-space(string($var124_isActive)) = '1')))"/>
														</xsl:attribute>
													</xsl:if>
													<xsl:if test="string(boolean($var122_status)) != 'false'">
														<xsl:attribute name="status" namespace="">
															<xsl:value-of select="string($var122_status)"/>
														</xsl:attribute>
													</xsl:if>
													<xsl:if test="string(boolean($var123_effectiveDate)) != 'false'">
														<xsl:attribute name="effectiveDate" namespace="">
															<xsl:value-of select="string($var123_effectiveDate)"/>
														</xsl:attribute>
													</xsl:if>
													<xsl:if test="string(boolean($var125_expirationDate)) != 'false'">
														<xsl:attribute name="expirationDate" namespace="">
															<xsl:value-of select="string($var125_expirationDate)"/>
														</xsl:attribute>
													</xsl:if>
													<xsl:if test="string(boolean($var126_associationInstanceId)) != 'false'">
														<xsl:attribute name="associationInstanceId" namespace="">
															<xsl:value-of select="string($var126_associationInstanceId)"/>
														</xsl:attribute>
													</xsl:if>
													<xsl:if test="string(boolean($var127_isInferred)) != 'false'">
														<xsl:attribute name="isInferred" namespace="">
															<xsl:value-of select="string(((normalize-space(string($var127_isInferred)) = 'true') or (normalize-space(string($var127_isInferred)) = '1')))"/>
														</xsl:attribute>
													</xsl:if>
													<xsl:if test="string(boolean($var128_isDefining)) != 'false'">
														<xsl:attribute name="isDefining" namespace="">
															<xsl:value-of select="string(((normalize-space(string($var128_isDefining)) = 'true') or (normalize-space(string($var128_isDefining)) = '1')))"/>
														</xsl:attribute>
													</xsl:if>
													<xsl:for-each select="ns1:owner">
														<lgCommon:owner xsl:exclude-result-prefixes="lgCommon">
															<xsl:value-of select="string(.)"/>
														</lgCommon:owner>
													</xsl:for-each>
													<xsl:for-each select="ns1:entryState">
														<lgCommon:entryState xsl:exclude-result-prefixes="lgCommon">
															<xsl:call-template name="tbf:tbf1_entryState">
																<xsl:with-param name="input" select="."/>
															</xsl:call-template>
														</lgCommon:entryState>
													</xsl:for-each>
													<xsl:for-each select="ns4:usageContext">
														<lgRel:usageContext xsl:exclude-result-prefixes="lgRel">
															<xsl:value-of select="string(.)"/>
														</lgRel:usageContext>
													</xsl:for-each>
													<xsl:for-each select="ns4:associationQualification">
														<lgRel:associationQualification xsl:exclude-result-prefixes="lgRel">
															<xsl:attribute name="associationQualifier" namespace="">
																<xsl:value-of select="string(@associationQualifier)"/>
															</xsl:attribute>
															<xsl:for-each select="ns4:qualifierText">
																<lgRel:qualifierText xsl:exclude-result-prefixes="lgRel">
																	<xsl:call-template name="tbf:tbf3_text">
																		<xsl:with-param name="input" select="."/>
																	</xsl:call-template>
																</lgRel:qualifierText>
															</xsl:for-each>
														</lgRel:associationQualification>
													</xsl:for-each>
													<xsl:for-each select="ns4:associationDataText">
														<lgRel:associationDataText xsl:exclude-result-prefixes="lgRel">
															<xsl:call-template name="tbf:tbf3_text">
																<xsl:with-param name="input" select="."/>
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
