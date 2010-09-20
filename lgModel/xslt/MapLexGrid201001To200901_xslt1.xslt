<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ns0="http://LexGrid.org/schema/2010/01/LexGrid/codingSchemes" xmlns:ns1="http://LexGrid.org/schema/2010/01/LexGrid/commonTypes" xmlns:ns2="http://LexGrid.org/schema/2010/01/LexGrid/concepts" xmlns:ns3="http://LexGrid.org/schema/2010/01/LexGrid/naming" xmlns:ns4="http://LexGrid.org/schema/2010/01/LexGrid/valueSets" xmlns:ns5="http://LexGrid.org/schema/2010/01/LexGrid/versions" xmlns:tbf="http://www.altova.com/MapForce/UDF/tbf" xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="ns0 ns1 ns2 ns3 ns4 ns5 tbf xs">
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
	<xsl:template name="tbf:tbf3_supportedAssociationQualifier">
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
	<xsl:template name="tbf:tbf4_supportedCodingScheme">
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
	<xsl:template name="tbf:tbf5_supportedContainerName">
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
	<xsl:template name="tbf:tbf6_supportedContext">
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
	<xsl:template name="tbf:tbf7_supportedDataType">
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
	<xsl:template name="tbf:tbf8_supportedDegreeOfFidelity">
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
	<xsl:template name="tbf:tbf9_supportedEntityType">
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
	<xsl:template name="tbf:tbf10_supportedHierarchy">
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
	<xsl:template name="tbf:tbf11_supportedLanguage">
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
	<xsl:template name="tbf:tbf12_supportedNamespace">
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
	<xsl:template name="tbf:tbf13_supportedPropertyType">
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
	<xsl:template name="tbf:tbf14_supportedPropertyLink">
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
	<xsl:template name="tbf:tbf15_supportedPropertyQualifier">
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
	<xsl:template name="tbf:tbf16_supportedPropertyQualifierType">
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
	<xsl:template name="tbf:tbf17_supportedRepresentationalForm">
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
	<xsl:template name="tbf:tbf18_supportedSortOrder">
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
	<xsl:template name="tbf:tbf19_supportedSource">
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
	<xsl:template name="tbf:tbf20_supportedSourceRole">
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
	<xsl:template name="tbf:tbf21_supportedStatus">
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
	<xsl:template name="tbf:tbf22_propertyLink">
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
	<xsl:template name="tbf:tbf23_text">
		<xsl:param name="input" select="/.."/>
		<xsl:for-each select="$input/@dataType">
			<xsl:attribute name="dataType">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:value-of select="string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf24_entityReference">
		<xsl:param name="input" select="/.."/>
		<xsl:for-each select="$input/@entityCode">
			<xsl:attribute name="entityCode">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:for-each select="$input/@entityCodeNamespace">
			<xsl:attribute name="entityCodeNamespace">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:for-each select="$input/@referenceAssociation">
			<xsl:attribute name="referenceAssociation">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:for-each select="$input/@transitiveClosure">
			<xsl:attribute name="transitiveClosure">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:for-each select="$input/@leafOnly">
			<xsl:attribute name="leafOnly">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:for-each select="$input/@targetToSource">
			<xsl:attribute name="targetToSource">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="tbf:tbf25_codingSchemeReference">
		<xsl:param name="input" select="/.."/>
		<xsl:for-each select="$input/@codingScheme">
			<xsl:attribute name="codingScheme">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="tbf:tbf26_pickListEntryExclusion">
		<xsl:param name="input" select="/.."/>
		<xsl:for-each select="$input/@entityCode">
			<xsl:attribute name="entityCode">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
		<xsl:for-each select="$input/@entityCodeNamespace">
			<xsl:attribute name="entityCodeNamespace">
				<xsl:value-of select="string(.)"/>
			</xsl:attribute>
		</xsl:for-each>
	</xsl:template>
	<xsl:output method="xml" encoding="UTF-8" indent="yes"/>
	<xsl:template match="/">
		<systemRelease xmlns="http://LexGrid.org/schema/2009/01/LexGrid/versions" xmlns:lgBuiltin="http://LexGrid.org/schema/2009/01/LexGrid/builtins" xmlns:lgCS="http://LexGrid.org/schema/2009/01/LexGrid/codingSchemes" xmlns:lgCommon="http://LexGrid.org/schema/2009/01/LexGrid/commonTypes" xmlns:lgCon="http://LexGrid.org/schema/2009/01/LexGrid/concepts" xmlns:lgNaming="http://LexGrid.org/schema/2009/01/LexGrid/naming" xmlns:lgRel="http://LexGrid.org/schema/2009/01/LexGrid/relations" xmlns:lgVD="http://LexGrid.org/schema/2009/01/LexGrid/valueDomains">
			<xsl:attribute name="xsi:schemaLocation" namespace="http://www.w3.org/2001/XMLSchema-instance">
				<xsl:value-of select="'http://LexGrid.org/schema/2009/01/LexGrid/versions http://LexGrid.org/schema/2010/01/LexGrid/codingSchemes.xsd'"/>
			</xsl:attribute>
			<xsl:for-each select="ns5:systemRelease">
				<xsl:variable name="var1_releaseAgency" select="@releaseAgency"/>
				<xsl:variable name="var2_releaseId" select="@releaseId"/>
				<xsl:variable name="var3_basedOnRelease" select="@basedOnRelease"/>
				<xsl:if test="string(boolean($var2_releaseId)) != 'false'">
					<xsl:attribute name="releaseId" namespace="">
						<xsl:value-of select="string($var2_releaseId)"/>
					</xsl:attribute>
				</xsl:if>
				<xsl:attribute name="releaseURI" namespace="">
					<xsl:value-of select="string(@releaseURI)"/>
				</xsl:attribute>
				<xsl:attribute name="releaseDate" namespace="">
					<xsl:value-of select="string(@releaseDate)"/>
				</xsl:attribute>
				<xsl:if test="string(boolean($var1_releaseAgency)) != 'false'">
					<xsl:attribute name="releaseAgency" namespace="">
						<xsl:value-of select="string($var1_releaseAgency)"/>
					</xsl:attribute>
				</xsl:if>
				<xsl:if test="string(boolean($var3_basedOnRelease)) != 'false'">
					<xsl:attribute name="basedOnRelease" namespace="">
						<xsl:value-of select="string($var3_basedOnRelease)"/>
					</xsl:attribute>
				</xsl:if>
				<xsl:for-each select="ns1:entityDescription">
					<lgCommon:entityDescription xsl:exclude-result-prefixes="lgCommon">
						<xsl:for-each select="node()[boolean(self::text())]">
							<xsl:value-of select="string(.)"/>
						</xsl:for-each>
					</lgCommon:entityDescription>
				</xsl:for-each>
				<xsl:for-each select="ns5:codingSchemes">
					<codingSchemes>
						<xsl:for-each select="ns0:codingScheme">
							<xsl:variable name="var4_isActive" select="@isActive"/>
							<xsl:variable name="var5_approxNumConcepts" select="@approxNumConcepts"/>
							<xsl:variable name="var6_expirationDate" select="@expirationDate"/>
							<xsl:variable name="var7_formalName" select="@formalName"/>
							<xsl:variable name="var8_effectiveDate" select="@effectiveDate"/>
							<xsl:variable name="var9_status" select="@status"/>
							<xsl:variable name="var10_defaultLanguage" select="@defaultLanguage"/>
							<xsl:variable name="var11_mappings" select="ns0:mappings"/>
							<lgCS:codingScheme xsl:exclude-result-prefixes="lgCS">
								<xsl:if test="string(boolean($var4_isActive)) != 'false'">
									<xsl:attribute name="isActive" namespace="">
										<xsl:value-of select="string(((normalize-space(string($var4_isActive)) = 'true') or (normalize-space(string($var4_isActive)) = '1')))"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="string(boolean($var9_status)) != 'false'">
									<xsl:attribute name="status" namespace="">
										<xsl:value-of select="string($var9_status)"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="string(boolean($var8_effectiveDate)) != 'false'">
									<xsl:attribute name="effectiveDate" namespace="">
										<xsl:value-of select="string($var8_effectiveDate)"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="string(boolean($var6_expirationDate)) != 'false'">
									<xsl:attribute name="expirationDate" namespace="">
										<xsl:value-of select="string($var6_expirationDate)"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:attribute name="codingSchemeName" namespace="">
									<xsl:value-of select="string(@codingSchemeName)"/>
								</xsl:attribute>
								<xsl:attribute name="codingSchemeURI" namespace="">
									<xsl:value-of select="string(@codingSchemeURI)"/>
								</xsl:attribute>
								<xsl:if test="string(boolean($var7_formalName)) != 'false'">
									<xsl:attribute name="formalName" namespace="">
										<xsl:value-of select="string($var7_formalName)"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="string(boolean($var10_defaultLanguage)) != 'false'">
									<xsl:attribute name="defaultLanguage" namespace="">
										<xsl:value-of select="string($var10_defaultLanguage)"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="string(boolean($var5_approxNumConcepts)) != 'false'">
									<xsl:attribute name="approxNumConcepts" namespace="">
										<xsl:value-of select="string(number(string($var5_approxNumConcepts)))"/>
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
									<lgCS:localName xsl:exclude-result-prefixes="lgCS">
										<xsl:value-of select="string(.)"/>
									</lgCS:localName>
								</xsl:for-each>
								<xsl:for-each select="ns0:source">
									<lgCS:source xsl:exclude-result-prefixes="lgCS">
										<xsl:call-template name="tbf:tbf2_source">
											<xsl:with-param name="input" select="."/>
										</xsl:call-template>
									</lgCS:source>
								</xsl:for-each>
								<xsl:for-each select="ns0:copyright">
									<xsl:variable name="var12_dataType" select="@dataType"/>
									<lgCS:copyright xsl:exclude-result-prefixes="lgCS">
										<xsl:if test="string(boolean($var12_dataType)) != 'false'">
											<xsl:attribute name="dataType" namespace="">
												<xsl:value-of select="string($var12_dataType)"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:for-each select="node()[boolean(self::text())]">
											<xsl:value-of select="string(.)"/>
										</xsl:for-each>
									</lgCS:copyright>
								</xsl:for-each>
								<lgCS:mappings xsl:exclude-result-prefixes="lgCS">
									<xsl:for-each select="$var11_mappings/ns3:supportedAssociation">
										<xsl:variable name="var13_uri" select="@uri"/>
										<lgNaming:supportedAssociation xsl:exclude-result-prefixes="lgNaming">
											<xsl:attribute name="localId" namespace="">
												<xsl:value-of select="string(@localId)"/>
											</xsl:attribute>
											<xsl:if test="string(boolean($var13_uri)) != 'false'">
												<xsl:attribute name="uri" namespace="">
													<xsl:value-of select="string($var13_uri)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:value-of select="string(.)"/>
										</lgNaming:supportedAssociation>
									</xsl:for-each>
									<xsl:for-each select="$var11_mappings/ns3:supportedAssociationQualifier">
										<lgNaming:supportedAssociationQualifier xsl:exclude-result-prefixes="lgNaming">
											<xsl:call-template name="tbf:tbf3_supportedAssociationQualifier">
												<xsl:with-param name="input" select="."/>
											</xsl:call-template>
										</lgNaming:supportedAssociationQualifier>
									</xsl:for-each>
									<xsl:for-each select="$var11_mappings/ns3:supportedCodingScheme">
										<lgNaming:supportedCodingScheme xsl:exclude-result-prefixes="lgNaming">
											<xsl:call-template name="tbf:tbf4_supportedCodingScheme">
												<xsl:with-param name="input" select="."/>
											</xsl:call-template>
										</lgNaming:supportedCodingScheme>
									</xsl:for-each>
									<xsl:for-each select="$var11_mappings/ns3:supportedContainerName">
										<lgNaming:supportedContainer xsl:exclude-result-prefixes="lgNaming">
											<xsl:call-template name="tbf:tbf5_supportedContainerName">
												<xsl:with-param name="input" select="."/>
											</xsl:call-template>
										</lgNaming:supportedContainer>
									</xsl:for-each>
									<xsl:for-each select="$var11_mappings/ns3:supportedContext">
										<lgNaming:supportedContext xsl:exclude-result-prefixes="lgNaming">
											<xsl:call-template name="tbf:tbf6_supportedContext">
												<xsl:with-param name="input" select="."/>
											</xsl:call-template>
										</lgNaming:supportedContext>
									</xsl:for-each>
									<xsl:for-each select="$var11_mappings/ns3:supportedDataType">
										<lgNaming:supportedDataType xsl:exclude-result-prefixes="lgNaming">
											<xsl:call-template name="tbf:tbf7_supportedDataType">
												<xsl:with-param name="input" select="."/>
											</xsl:call-template>
										</lgNaming:supportedDataType>
									</xsl:for-each>
									<xsl:for-each select="$var11_mappings/ns3:supportedDegreeOfFidelity">
										<lgNaming:supportedDegreeOfFidelity xsl:exclude-result-prefixes="lgNaming">
											<xsl:call-template name="tbf:tbf8_supportedDegreeOfFidelity">
												<xsl:with-param name="input" select="."/>
											</xsl:call-template>
										</lgNaming:supportedDegreeOfFidelity>
									</xsl:for-each>
									<xsl:for-each select="$var11_mappings/ns3:supportedEntityType">
										<lgNaming:supportedEntityType xsl:exclude-result-prefixes="lgNaming">
											<xsl:call-template name="tbf:tbf9_supportedEntityType">
												<xsl:with-param name="input" select="."/>
											</xsl:call-template>
										</lgNaming:supportedEntityType>
									</xsl:for-each>
									<xsl:for-each select="$var11_mappings/ns3:supportedHierarchy">
										<lgNaming:supportedHierarchy xsl:exclude-result-prefixes="lgNaming">
											<xsl:call-template name="tbf:tbf10_supportedHierarchy">
												<xsl:with-param name="input" select="."/>
											</xsl:call-template>
										</lgNaming:supportedHierarchy>
									</xsl:for-each>
									<xsl:for-each select="$var11_mappings/ns3:supportedLanguage">
										<lgNaming:supportedLanguage xsl:exclude-result-prefixes="lgNaming">
											<xsl:call-template name="tbf:tbf11_supportedLanguage">
												<xsl:with-param name="input" select="."/>
											</xsl:call-template>
										</lgNaming:supportedLanguage>
									</xsl:for-each>
									<xsl:for-each select="$var11_mappings/ns3:supportedNamespace">
										<lgNaming:supportedNamespace xsl:exclude-result-prefixes="lgNaming">
											<xsl:call-template name="tbf:tbf12_supportedNamespace">
												<xsl:with-param name="input" select="."/>
											</xsl:call-template>
										</lgNaming:supportedNamespace>
									</xsl:for-each>
									<xsl:for-each select="$var11_mappings/ns3:supportedProperty">
										<xsl:variable name="var14_uri" select="@uri"/>
										<lgNaming:supportedProperty xsl:exclude-result-prefixes="lgNaming">
											<xsl:attribute name="localId" namespace="">
												<xsl:value-of select="string(@localId)"/>
											</xsl:attribute>
											<xsl:if test="string(boolean($var14_uri)) != 'false'">
												<xsl:attribute name="uri" namespace="">
													<xsl:value-of select="string($var14_uri)"/>
												</xsl:attribute>
											</xsl:if>
											<xsl:value-of select="string(.)"/>
										</lgNaming:supportedProperty>
									</xsl:for-each>
									<xsl:for-each select="$var11_mappings/ns3:supportedPropertyType">
										<lgNaming:supportedPropertyType xsl:exclude-result-prefixes="lgNaming">
											<xsl:call-template name="tbf:tbf13_supportedPropertyType">
												<xsl:with-param name="input" select="."/>
											</xsl:call-template>
										</lgNaming:supportedPropertyType>
									</xsl:for-each>
									<xsl:for-each select="$var11_mappings/ns3:supportedPropertyLink">
										<lgNaming:supportedPropertyLink xsl:exclude-result-prefixes="lgNaming">
											<xsl:call-template name="tbf:tbf14_supportedPropertyLink">
												<xsl:with-param name="input" select="."/>
											</xsl:call-template>
										</lgNaming:supportedPropertyLink>
									</xsl:for-each>
									<xsl:for-each select="$var11_mappings/ns3:supportedPropertyQualifier">
										<lgNaming:supportedPropertyQualifier xsl:exclude-result-prefixes="lgNaming">
											<xsl:call-template name="tbf:tbf15_supportedPropertyQualifier">
												<xsl:with-param name="input" select="."/>
											</xsl:call-template>
										</lgNaming:supportedPropertyQualifier>
									</xsl:for-each>
									<xsl:for-each select="$var11_mappings/ns3:supportedPropertyQualifierType">
										<lgNaming:supportedPropertyQualifierType xsl:exclude-result-prefixes="lgNaming">
											<xsl:call-template name="tbf:tbf16_supportedPropertyQualifierType">
												<xsl:with-param name="input" select="."/>
											</xsl:call-template>
										</lgNaming:supportedPropertyQualifierType>
									</xsl:for-each>
									<xsl:for-each select="$var11_mappings/ns3:supportedRepresentationalForm">
										<lgNaming:supportedRepresentationalForm xsl:exclude-result-prefixes="lgNaming">
											<xsl:call-template name="tbf:tbf17_supportedRepresentationalForm">
												<xsl:with-param name="input" select="."/>
											</xsl:call-template>
										</lgNaming:supportedRepresentationalForm>
									</xsl:for-each>
									<xsl:for-each select="$var11_mappings/ns3:supportedSortOrder">
										<lgNaming:supportedSortOrder xsl:exclude-result-prefixes="lgNaming">
											<xsl:call-template name="tbf:tbf18_supportedSortOrder">
												<xsl:with-param name="input" select="."/>
											</xsl:call-template>
										</lgNaming:supportedSortOrder>
									</xsl:for-each>
									<xsl:for-each select="$var11_mappings/ns3:supportedSource">
										<lgNaming:supportedSource xsl:exclude-result-prefixes="lgNaming">
											<xsl:call-template name="tbf:tbf19_supportedSource">
												<xsl:with-param name="input" select="."/>
											</xsl:call-template>
										</lgNaming:supportedSource>
									</xsl:for-each>
									<xsl:for-each select="$var11_mappings/ns3:supportedSourceRole">
										<lgNaming:supportedSourceRole xsl:exclude-result-prefixes="lgNaming">
											<xsl:call-template name="tbf:tbf20_supportedSourceRole">
												<xsl:with-param name="input" select="."/>
											</xsl:call-template>
										</lgNaming:supportedSourceRole>
									</xsl:for-each>
									<xsl:for-each select="$var11_mappings/ns3:supportedStatus">
										<lgNaming:supportedStatus xsl:exclude-result-prefixes="lgNaming">
											<xsl:call-template name="tbf:tbf21_supportedStatus">
												<xsl:with-param name="input" select="."/>
											</xsl:call-template>
										</lgNaming:supportedStatus>
									</xsl:for-each>
								</lgCS:mappings>
								<xsl:for-each select="ns0:properties">
									<lgCS:properties xsl:exclude-result-prefixes="lgCS">
										<xsl:for-each select="ns1:property">
											<xsl:variable name="var15_propertyType" select="@propertyType"/>
											<xsl:variable name="var16_status" select="@status"/>
											<xsl:variable name="var17_propertyId" select="@propertyId"/>
											<xsl:variable name="var18_effectiveDate" select="@effectiveDate"/>
											<xsl:variable name="var19_isActive" select="@isActive"/>
											<xsl:variable name="var20_language" select="@language"/>
											<xsl:variable name="var21_expirationDate" select="@expirationDate"/>
											<lgCommon:property xsl:exclude-result-prefixes="lgCommon">
												<xsl:if test="string(boolean($var19_isActive)) != 'false'">
													<xsl:attribute name="isActive" namespace="">
														<xsl:value-of select="string(((normalize-space(string($var19_isActive)) = 'true') or (normalize-space(string($var19_isActive)) = '1')))"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:if test="string(boolean($var16_status)) != 'false'">
													<xsl:attribute name="status" namespace="">
														<xsl:value-of select="string($var16_status)"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:if test="string(boolean($var18_effectiveDate)) != 'false'">
													<xsl:attribute name="effectiveDate" namespace="">
														<xsl:value-of select="string($var18_effectiveDate)"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:if test="string(boolean($var21_expirationDate)) != 'false'">
													<xsl:attribute name="expirationDate" namespace="">
														<xsl:value-of select="string($var21_expirationDate)"/>
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
												<xsl:if test="string(boolean($var15_propertyType)) != 'false'">
													<xsl:attribute name="propertyType" namespace="">
														<xsl:value-of select="string($var15_propertyType)"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:if test="string(boolean($var20_language)) != 'false'">
													<xsl:attribute name="language" namespace="">
														<xsl:value-of select="string($var20_language)"/>
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
													<xsl:variable name="var22_propertyQualifierType" select="@propertyQualifierType"/>
													<lgCommon:propertyQualifier xsl:exclude-result-prefixes="lgCommon">
														<xsl:attribute name="propertyQualifierName" namespace="">
															<xsl:value-of select="string(@propertyQualifierName)"/>
														</xsl:attribute>
														<xsl:if test="string(boolean($var22_propertyQualifierType)) != 'false'">
															<xsl:attribute name="propertyQualifierType" namespace="">
																<xsl:value-of select="string($var22_propertyQualifierType)"/>
															</xsl:attribute>
														</xsl:if>
														<xsl:for-each select="ns1:value">
															<xsl:variable name="var23_dataType" select="@dataType"/>
															<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
																<xsl:if test="string(boolean($var23_dataType)) != 'false'">
																	<xsl:attribute name="dataType" namespace="">
																		<xsl:value-of select="string($var23_dataType)"/>
																	</xsl:attribute>
																</xsl:if>
																<xsl:for-each select="node()[boolean(self::text())]">
																	<xsl:value-of select="string(.)"/>
																</xsl:for-each>
															</lgCommon:value>
														</xsl:for-each>
													</lgCommon:propertyQualifier>
												</xsl:for-each>
												<xsl:for-each select="ns1:value">
													<xsl:variable name="var24_dataType" select="@dataType"/>
													<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
														<xsl:if test="string(boolean($var24_dataType)) != 'false'">
															<xsl:attribute name="dataType" namespace="">
																<xsl:value-of select="string($var24_dataType)"/>
															</xsl:attribute>
														</xsl:if>
														<xsl:for-each select="node()[boolean(self::text())]">
															<xsl:value-of select="string(.)"/>
														</xsl:for-each>
													</lgCommon:value>
												</xsl:for-each>
											</lgCommon:property>
										</xsl:for-each>
									</lgCS:properties>
								</xsl:for-each>
								<xsl:for-each select="ns0:entities">
									<lgCS:entities xsl:exclude-result-prefixes="lgCS">
										<xsl:for-each select="ns2:entity">
											<xsl:variable name="var25_status" select="@status"/>
											<xsl:variable name="var26_expirationDate" select="@expirationDate"/>
											<xsl:variable name="var27_isAnonymous" select="@isAnonymous"/>
											<xsl:variable name="var28_isActive" select="@isActive"/>
											<xsl:variable name="var29_entityCodeNamespace" select="@entityCodeNamespace"/>
											<xsl:variable name="var30_isDefined" select="@isDefined"/>
											<xsl:variable name="var31_effectiveDate" select="@effectiveDate"/>
											<lgCon:entity xsl:exclude-result-prefixes="lgCon">
												<xsl:if test="string(boolean($var28_isActive)) != 'false'">
													<xsl:attribute name="isActive" namespace="">
														<xsl:value-of select="string(((normalize-space(string($var28_isActive)) = 'true') or (normalize-space(string($var28_isActive)) = '1')))"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:if test="string(boolean($var25_status)) != 'false'">
													<xsl:attribute name="status" namespace="">
														<xsl:value-of select="string($var25_status)"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:if test="string(boolean($var31_effectiveDate)) != 'false'">
													<xsl:attribute name="effectiveDate" namespace="">
														<xsl:value-of select="string($var31_effectiveDate)"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:if test="string(boolean($var26_expirationDate)) != 'false'">
													<xsl:attribute name="expirationDate" namespace="">
														<xsl:value-of select="string($var26_expirationDate)"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:attribute name="entityCode" namespace="">
													<xsl:value-of select="string(@entityCode)"/>
												</xsl:attribute>
												<xsl:if test="string(boolean($var29_entityCodeNamespace)) != 'false'">
													<xsl:attribute name="entityCodeNamespace" namespace="">
														<xsl:value-of select="string($var29_entityCodeNamespace)"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:if test="string(boolean($var27_isAnonymous)) != 'false'">
													<xsl:attribute name="isAnonymous" namespace="">
														<xsl:value-of select="string(((normalize-space(string($var27_isAnonymous)) = 'true') or (normalize-space(string($var27_isAnonymous)) = '1')))"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:if test="string(boolean($var30_isDefined)) != 'false'">
													<xsl:attribute name="isDefined" namespace="">
														<xsl:value-of select="string(((normalize-space(string($var30_isDefined)) = 'true') or (normalize-space(string($var30_isDefined)) = '1')))"/>
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
													<xsl:variable name="var32_effectiveDate" select="@effectiveDate"/>
													<xsl:variable name="var33_language" select="@language"/>
													<xsl:variable name="var34_status" select="@status"/>
													<xsl:variable name="var35_matchIfNoContext" select="@matchIfNoContext"/>
													<xsl:variable name="var36_isPreferred" select="@isPreferred"/>
													<xsl:variable name="var37_degreeOfFidelity" select="@degreeOfFidelity"/>
													<xsl:variable name="var38_expirationDate" select="@expirationDate"/>
													<xsl:variable name="var39_representationalForm" select="@representationalForm"/>
													<xsl:variable name="var40_propertyType" select="@propertyType"/>
													<xsl:variable name="var41_propertyId" select="@propertyId"/>
													<xsl:variable name="var42_isActive" select="@isActive"/>
													<lgCon:presentation xsl:exclude-result-prefixes="lgCon">
														<xsl:if test="string(boolean($var42_isActive)) != 'false'">
															<xsl:attribute name="isActive" namespace="">
																<xsl:value-of select="string(((normalize-space(string($var42_isActive)) = 'true') or (normalize-space(string($var42_isActive)) = '1')))"/>
															</xsl:attribute>
														</xsl:if>
														<xsl:if test="string(boolean($var34_status)) != 'false'">
															<xsl:attribute name="status" namespace="">
																<xsl:value-of select="string($var34_status)"/>
															</xsl:attribute>
														</xsl:if>
														<xsl:if test="string(boolean($var32_effectiveDate)) != 'false'">
															<xsl:attribute name="effectiveDate" namespace="">
																<xsl:value-of select="string($var32_effectiveDate)"/>
															</xsl:attribute>
														</xsl:if>
														<xsl:if test="string(boolean($var38_expirationDate)) != 'false'">
															<xsl:attribute name="expirationDate" namespace="">
																<xsl:value-of select="string($var38_expirationDate)"/>
															</xsl:attribute>
														</xsl:if>
														<xsl:attribute name="propertyName" namespace="">
															<xsl:value-of select="string(@propertyName)"/>
														</xsl:attribute>
														<xsl:if test="string(boolean($var41_propertyId)) != 'false'">
															<xsl:attribute name="propertyId" namespace="">
																<xsl:value-of select="string($var41_propertyId)"/>
															</xsl:attribute>
														</xsl:if>
														<xsl:if test="string(boolean($var40_propertyType)) != 'false'">
															<xsl:attribute name="propertyType" namespace="">
																<xsl:value-of select="string($var40_propertyType)"/>
															</xsl:attribute>
														</xsl:if>
														<xsl:if test="string(boolean($var33_language)) != 'false'">
															<xsl:attribute name="language" namespace="">
																<xsl:value-of select="string($var33_language)"/>
															</xsl:attribute>
														</xsl:if>
														<xsl:if test="string(boolean($var36_isPreferred)) != 'false'">
															<xsl:attribute name="isPreferred" namespace="">
																<xsl:value-of select="string(((normalize-space(string($var36_isPreferred)) = 'true') or (normalize-space(string($var36_isPreferred)) = '1')))"/>
															</xsl:attribute>
														</xsl:if>
														<xsl:if test="string(boolean($var37_degreeOfFidelity)) != 'false'">
															<xsl:attribute name="degreeOfFidelity" namespace="">
																<xsl:value-of select="string($var37_degreeOfFidelity)"/>
															</xsl:attribute>
														</xsl:if>
														<xsl:if test="string(boolean($var35_matchIfNoContext)) != 'false'">
															<xsl:attribute name="matchIfNoContext" namespace="">
																<xsl:value-of select="string(((normalize-space(string($var35_matchIfNoContext)) = 'true') or (normalize-space(string($var35_matchIfNoContext)) = '1')))"/>
															</xsl:attribute>
														</xsl:if>
														<xsl:if test="string(boolean($var39_representationalForm)) != 'false'">
															<xsl:attribute name="representationalForm" namespace="">
																<xsl:value-of select="string($var39_representationalForm)"/>
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
															<xsl:variable name="var43_propertyQualifierType" select="@propertyQualifierType"/>
															<lgCommon:propertyQualifier xsl:exclude-result-prefixes="lgCommon">
																<xsl:attribute name="propertyQualifierName" namespace="">
																	<xsl:value-of select="string(@propertyQualifierName)"/>
																</xsl:attribute>
																<xsl:if test="string(boolean($var43_propertyQualifierType)) != 'false'">
																	<xsl:attribute name="propertyQualifierType" namespace="">
																		<xsl:value-of select="string($var43_propertyQualifierType)"/>
																	</xsl:attribute>
																</xsl:if>
																<xsl:for-each select="ns1:value">
																	<xsl:variable name="var44_dataType" select="@dataType"/>
																	<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
																		<xsl:if test="string(boolean($var44_dataType)) != 'false'">
																			<xsl:attribute name="dataType" namespace="">
																				<xsl:value-of select="string($var44_dataType)"/>
																			</xsl:attribute>
																		</xsl:if>
																		<xsl:for-each select="node()[boolean(self::text())]">
																			<xsl:value-of select="string(.)"/>
																		</xsl:for-each>
																	</lgCommon:value>
																</xsl:for-each>
															</lgCommon:propertyQualifier>
														</xsl:for-each>
														<xsl:for-each select="ns1:value">
															<xsl:variable name="var45_dataType" select="@dataType"/>
															<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
																<xsl:if test="string(boolean($var45_dataType)) != 'false'">
																	<xsl:attribute name="dataType" namespace="">
																		<xsl:value-of select="string($var45_dataType)"/>
																	</xsl:attribute>
																</xsl:if>
																<xsl:for-each select="node()[boolean(self::text())]">
																	<xsl:value-of select="string(.)"/>
																</xsl:for-each>
															</lgCommon:value>
														</xsl:for-each>
													</lgCon:presentation>
												</xsl:for-each>
												<xsl:for-each select="ns2:definition">
													<xsl:variable name="var46_status" select="@status"/>
													<xsl:variable name="var47_language" select="@language"/>
													<xsl:variable name="var48_isPreferred" select="@isPreferred"/>
													<xsl:variable name="var49_isActive" select="@isActive"/>
													<xsl:variable name="var50_effectiveDate" select="@effectiveDate"/>
													<xsl:variable name="var51_expirationDate" select="@expirationDate"/>
													<xsl:variable name="var52_propertyType" select="@propertyType"/>
													<xsl:variable name="var53_propertyId" select="@propertyId"/>
													<lgCon:definition xsl:exclude-result-prefixes="lgCon">
														<xsl:if test="string(boolean($var49_isActive)) != 'false'">
															<xsl:attribute name="isActive" namespace="">
																<xsl:value-of select="string(((normalize-space(string($var49_isActive)) = 'true') or (normalize-space(string($var49_isActive)) = '1')))"/>
															</xsl:attribute>
														</xsl:if>
														<xsl:if test="string(boolean($var46_status)) != 'false'">
															<xsl:attribute name="status" namespace="">
																<xsl:value-of select="string($var46_status)"/>
															</xsl:attribute>
														</xsl:if>
														<xsl:if test="string(boolean($var50_effectiveDate)) != 'false'">
															<xsl:attribute name="effectiveDate" namespace="">
																<xsl:value-of select="string($var50_effectiveDate)"/>
															</xsl:attribute>
														</xsl:if>
														<xsl:if test="string(boolean($var51_expirationDate)) != 'false'">
															<xsl:attribute name="expirationDate" namespace="">
																<xsl:value-of select="string($var51_expirationDate)"/>
															</xsl:attribute>
														</xsl:if>
														<xsl:attribute name="propertyName" namespace="">
															<xsl:value-of select="string(@propertyName)"/>
														</xsl:attribute>
														<xsl:if test="string(boolean($var53_propertyId)) != 'false'">
															<xsl:attribute name="propertyId" namespace="">
																<xsl:value-of select="string($var53_propertyId)"/>
															</xsl:attribute>
														</xsl:if>
														<xsl:if test="string(boolean($var52_propertyType)) != 'false'">
															<xsl:attribute name="propertyType" namespace="">
																<xsl:value-of select="string($var52_propertyType)"/>
															</xsl:attribute>
														</xsl:if>
														<xsl:if test="string(boolean($var47_language)) != 'false'">
															<xsl:attribute name="language" namespace="">
																<xsl:value-of select="string($var47_language)"/>
															</xsl:attribute>
														</xsl:if>
														<xsl:if test="string(boolean($var48_isPreferred)) != 'false'">
															<xsl:attribute name="isPreferred" namespace="">
																<xsl:value-of select="string(((normalize-space(string($var48_isPreferred)) = 'true') or (normalize-space(string($var48_isPreferred)) = '1')))"/>
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
															<xsl:variable name="var54_propertyQualifierType" select="@propertyQualifierType"/>
															<lgCommon:propertyQualifier xsl:exclude-result-prefixes="lgCommon">
																<xsl:attribute name="propertyQualifierName" namespace="">
																	<xsl:value-of select="string(@propertyQualifierName)"/>
																</xsl:attribute>
																<xsl:if test="string(boolean($var54_propertyQualifierType)) != 'false'">
																	<xsl:attribute name="propertyQualifierType" namespace="">
																		<xsl:value-of select="string($var54_propertyQualifierType)"/>
																	</xsl:attribute>
																</xsl:if>
																<xsl:for-each select="ns1:value">
																	<xsl:variable name="var55_dataType" select="@dataType"/>
																	<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
																		<xsl:if test="string(boolean($var55_dataType)) != 'false'">
																			<xsl:attribute name="dataType" namespace="">
																				<xsl:value-of select="string($var55_dataType)"/>
																			</xsl:attribute>
																		</xsl:if>
																		<xsl:for-each select="node()[boolean(self::text())]">
																			<xsl:value-of select="string(.)"/>
																		</xsl:for-each>
																	</lgCommon:value>
																</xsl:for-each>
															</lgCommon:propertyQualifier>
														</xsl:for-each>
														<xsl:for-each select="ns1:value">
															<xsl:variable name="var56_dataType" select="@dataType"/>
															<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
																<xsl:if test="string(boolean($var56_dataType)) != 'false'">
																	<xsl:attribute name="dataType" namespace="">
																		<xsl:value-of select="string($var56_dataType)"/>
																	</xsl:attribute>
																</xsl:if>
																<xsl:for-each select="node()[boolean(self::text())]">
																	<xsl:value-of select="string(.)"/>
																</xsl:for-each>
															</lgCommon:value>
														</xsl:for-each>
													</lgCon:definition>
												</xsl:for-each>
												<xsl:for-each select="ns2:comment">
													<xsl:variable name="var57_effectiveDate" select="@effectiveDate"/>
													<xsl:variable name="var58_expirationDate" select="@expirationDate"/>
													<xsl:variable name="var59_language" select="@language"/>
													<xsl:variable name="var60_isActive" select="@isActive"/>
													<xsl:variable name="var61_status" select="@status"/>
													<xsl:variable name="var62_propertyType" select="@propertyType"/>
													<xsl:variable name="var63_propertyId" select="@propertyId"/>
													<lgCon:comment xsl:exclude-result-prefixes="lgCon">
														<xsl:if test="string(boolean($var60_isActive)) != 'false'">
															<xsl:attribute name="isActive" namespace="">
																<xsl:value-of select="string(((normalize-space(string($var60_isActive)) = 'true') or (normalize-space(string($var60_isActive)) = '1')))"/>
															</xsl:attribute>
														</xsl:if>
														<xsl:if test="string(boolean($var61_status)) != 'false'">
															<xsl:attribute name="status" namespace="">
																<xsl:value-of select="string($var61_status)"/>
															</xsl:attribute>
														</xsl:if>
														<xsl:if test="string(boolean($var57_effectiveDate)) != 'false'">
															<xsl:attribute name="effectiveDate" namespace="">
																<xsl:value-of select="string($var57_effectiveDate)"/>
															</xsl:attribute>
														</xsl:if>
														<xsl:if test="string(boolean($var58_expirationDate)) != 'false'">
															<xsl:attribute name="expirationDate" namespace="">
																<xsl:value-of select="string($var58_expirationDate)"/>
															</xsl:attribute>
														</xsl:if>
														<xsl:attribute name="propertyName" namespace="">
															<xsl:value-of select="string(@propertyName)"/>
														</xsl:attribute>
														<xsl:if test="string(boolean($var63_propertyId)) != 'false'">
															<xsl:attribute name="propertyId" namespace="">
																<xsl:value-of select="string($var63_propertyId)"/>
															</xsl:attribute>
														</xsl:if>
														<xsl:if test="string(boolean($var62_propertyType)) != 'false'">
															<xsl:attribute name="propertyType" namespace="">
																<xsl:value-of select="string($var62_propertyType)"/>
															</xsl:attribute>
														</xsl:if>
														<xsl:if test="string(boolean($var59_language)) != 'false'">
															<xsl:attribute name="language" namespace="">
																<xsl:value-of select="string($var59_language)"/>
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
															<xsl:variable name="var64_propertyQualifierType" select="@propertyQualifierType"/>
															<lgCommon:propertyQualifier xsl:exclude-result-prefixes="lgCommon">
																<xsl:attribute name="propertyQualifierName" namespace="">
																	<xsl:value-of select="string(@propertyQualifierName)"/>
																</xsl:attribute>
																<xsl:if test="string(boolean($var64_propertyQualifierType)) != 'false'">
																	<xsl:attribute name="propertyQualifierType" namespace="">
																		<xsl:value-of select="string($var64_propertyQualifierType)"/>
																	</xsl:attribute>
																</xsl:if>
																<xsl:for-each select="ns1:value">
																	<xsl:variable name="var65_dataType" select="@dataType"/>
																	<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
																		<xsl:if test="string(boolean($var65_dataType)) != 'false'">
																			<xsl:attribute name="dataType" namespace="">
																				<xsl:value-of select="string($var65_dataType)"/>
																			</xsl:attribute>
																		</xsl:if>
																		<xsl:for-each select="node()[boolean(self::text())]">
																			<xsl:value-of select="string(.)"/>
																		</xsl:for-each>
																	</lgCommon:value>
																</xsl:for-each>
															</lgCommon:propertyQualifier>
														</xsl:for-each>
														<xsl:for-each select="ns1:value">
															<xsl:variable name="var66_dataType" select="@dataType"/>
															<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
																<xsl:if test="string(boolean($var66_dataType)) != 'false'">
																	<xsl:attribute name="dataType" namespace="">
																		<xsl:value-of select="string($var66_dataType)"/>
																	</xsl:attribute>
																</xsl:if>
																<xsl:for-each select="node()[boolean(self::text())]">
																	<xsl:value-of select="string(.)"/>
																</xsl:for-each>
															</lgCommon:value>
														</xsl:for-each>
													</lgCon:comment>
												</xsl:for-each>
												<xsl:for-each select="ns2:property">
													<xsl:variable name="var67_status" select="@status"/>
													<xsl:variable name="var68_propertyType" select="@propertyType"/>
													<xsl:variable name="var69_isActive" select="@isActive"/>
													<xsl:variable name="var70_propertyId" select="@propertyId"/>
													<xsl:variable name="var71_expirationDate" select="@expirationDate"/>
													<xsl:variable name="var72_language" select="@language"/>
													<xsl:variable name="var73_effectiveDate" select="@effectiveDate"/>
													<lgCon:property xsl:exclude-result-prefixes="lgCon">
														<xsl:if test="string(boolean($var69_isActive)) != 'false'">
															<xsl:attribute name="isActive" namespace="">
																<xsl:value-of select="string(((normalize-space(string($var69_isActive)) = 'true') or (normalize-space(string($var69_isActive)) = '1')))"/>
															</xsl:attribute>
														</xsl:if>
														<xsl:if test="string(boolean($var67_status)) != 'false'">
															<xsl:attribute name="status" namespace="">
																<xsl:value-of select="string($var67_status)"/>
															</xsl:attribute>
														</xsl:if>
														<xsl:if test="string(boolean($var73_effectiveDate)) != 'false'">
															<xsl:attribute name="effectiveDate" namespace="">
																<xsl:value-of select="string($var73_effectiveDate)"/>
															</xsl:attribute>
														</xsl:if>
														<xsl:if test="string(boolean($var71_expirationDate)) != 'false'">
															<xsl:attribute name="expirationDate" namespace="">
																<xsl:value-of select="string($var71_expirationDate)"/>
															</xsl:attribute>
														</xsl:if>
														<xsl:attribute name="propertyName" namespace="">
															<xsl:value-of select="string(@propertyName)"/>
														</xsl:attribute>
														<xsl:if test="string(boolean($var70_propertyId)) != 'false'">
															<xsl:attribute name="propertyId" namespace="">
																<xsl:value-of select="string($var70_propertyId)"/>
															</xsl:attribute>
														</xsl:if>
														<xsl:if test="string(boolean($var68_propertyType)) != 'false'">
															<xsl:attribute name="propertyType" namespace="">
																<xsl:value-of select="string($var68_propertyType)"/>
															</xsl:attribute>
														</xsl:if>
														<xsl:if test="string(boolean($var72_language)) != 'false'">
															<xsl:attribute name="language" namespace="">
																<xsl:value-of select="string($var72_language)"/>
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
															<xsl:variable name="var74_propertyQualifierType" select="@propertyQualifierType"/>
															<lgCommon:propertyQualifier xsl:exclude-result-prefixes="lgCommon">
																<xsl:attribute name="propertyQualifierName" namespace="">
																	<xsl:value-of select="string(@propertyQualifierName)"/>
																</xsl:attribute>
																<xsl:if test="string(boolean($var74_propertyQualifierType)) != 'false'">
																	<xsl:attribute name="propertyQualifierType" namespace="">
																		<xsl:value-of select="string($var74_propertyQualifierType)"/>
																	</xsl:attribute>
																</xsl:if>
																<xsl:for-each select="ns1:value">
																	<xsl:variable name="var75_dataType" select="@dataType"/>
																	<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
																		<xsl:if test="string(boolean($var75_dataType)) != 'false'">
																			<xsl:attribute name="dataType" namespace="">
																				<xsl:value-of select="string($var75_dataType)"/>
																			</xsl:attribute>
																		</xsl:if>
																		<xsl:for-each select="node()[boolean(self::text())]">
																			<xsl:value-of select="string(.)"/>
																		</xsl:for-each>
																	</lgCommon:value>
																</xsl:for-each>
															</lgCommon:propertyQualifier>
														</xsl:for-each>
														<xsl:for-each select="ns1:value">
															<xsl:variable name="var76_dataType" select="@dataType"/>
															<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
																<xsl:if test="string(boolean($var76_dataType)) != 'false'">
																	<xsl:attribute name="dataType" namespace="">
																		<xsl:value-of select="string($var76_dataType)"/>
																	</xsl:attribute>
																</xsl:if>
																<xsl:for-each select="node()[boolean(self::text())]">
																	<xsl:value-of select="string(.)"/>
																</xsl:for-each>
															</lgCommon:value>
														</xsl:for-each>
													</lgCon:property>
												</xsl:for-each>
												<xsl:for-each select="ns2:propertyLink">
													<lgCon:propertyLink xsl:exclude-result-prefixes="lgCon">
														<xsl:call-template name="tbf:tbf22_propertyLink">
															<xsl:with-param name="input" select="."/>
														</xsl:call-template>
													</lgCon:propertyLink>
												</xsl:for-each>
											</lgCon:entity>
										</xsl:for-each>
									</lgCS:entities>
								</xsl:for-each>
								<xsl:for-each select="ns0:relations">
									<xsl:variable name="var77_containerName" select="@containerName"/>
									<lgCS:relations xsl:exclude-result-prefixes="lgCS">
										<xsl:if test="string(boolean($var77_containerName)) != 'false'">
											<xsl:attribute name="containerName" namespace="">
												<xsl:value-of select="string($var77_containerName)"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:for-each select="ns1:entityDescription">
											<lgCommon:entityDescription xsl:exclude-result-prefixes="lgCommon">
												<xsl:for-each select="node()[boolean(self::text())]">
													<xsl:value-of select="string(.)"/>
												</xsl:for-each>
											</lgCommon:entityDescription>
										</xsl:for-each>
									</lgCS:relations>
								</xsl:for-each>
							</lgCS:codingScheme>
						</xsl:for-each>
					</codingSchemes>
				</xsl:for-each>
				<xsl:for-each select="ns5:valueSetDefinitions">
					<xsl:variable name="var78_mappings" select="ns4:mappings"/>
					<valueDomains>
						<lgVD:mappings xsl:exclude-result-prefixes="lgVD">
							<xsl:for-each select="$var78_mappings/ns3:supportedAssociation">
								<xsl:variable name="var79_uri" select="@uri"/>
								<lgNaming:supportedAssociation xsl:exclude-result-prefixes="lgNaming">
									<xsl:attribute name="localId" namespace="">
										<xsl:value-of select="string(@localId)"/>
									</xsl:attribute>
									<xsl:if test="string(boolean($var79_uri)) != 'false'">
										<xsl:attribute name="uri" namespace="">
											<xsl:value-of select="string($var79_uri)"/>
										</xsl:attribute>
									</xsl:if>
									<xsl:value-of select="string(.)"/>
								</lgNaming:supportedAssociation>
							</xsl:for-each>
							<xsl:for-each select="$var78_mappings/ns3:supportedAssociationQualifier">
								<lgNaming:supportedAssociationQualifier xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf3_supportedAssociationQualifier">
										<xsl:with-param name="input" select="."/>
									</xsl:call-template>
								</lgNaming:supportedAssociationQualifier>
							</xsl:for-each>
							<xsl:for-each select="$var78_mappings/ns3:supportedCodingScheme">
								<lgNaming:supportedCodingScheme xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf4_supportedCodingScheme">
										<xsl:with-param name="input" select="."/>
									</xsl:call-template>
								</lgNaming:supportedCodingScheme>
							</xsl:for-each>
							<xsl:for-each select="$var78_mappings/ns3:supportedContainerName">
								<lgNaming:supportedContainer xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf5_supportedContainerName">
										<xsl:with-param name="input" select="."/>
									</xsl:call-template>
								</lgNaming:supportedContainer>
							</xsl:for-each>
							<xsl:for-each select="$var78_mappings/ns3:supportedContext">
								<lgNaming:supportedContext xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf6_supportedContext">
										<xsl:with-param name="input" select="."/>
									</xsl:call-template>
								</lgNaming:supportedContext>
							</xsl:for-each>
							<xsl:for-each select="$var78_mappings/ns3:supportedDataType">
								<lgNaming:supportedDataType xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf7_supportedDataType">
										<xsl:with-param name="input" select="."/>
									</xsl:call-template>
								</lgNaming:supportedDataType>
							</xsl:for-each>
							<xsl:for-each select="$var78_mappings/ns3:supportedDegreeOfFidelity">
								<lgNaming:supportedDegreeOfFidelity xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf8_supportedDegreeOfFidelity">
										<xsl:with-param name="input" select="."/>
									</xsl:call-template>
								</lgNaming:supportedDegreeOfFidelity>
							</xsl:for-each>
							<xsl:for-each select="$var78_mappings/ns3:supportedEntityType">
								<lgNaming:supportedEntityType xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf9_supportedEntityType">
										<xsl:with-param name="input" select="."/>
									</xsl:call-template>
								</lgNaming:supportedEntityType>
							</xsl:for-each>
							<xsl:for-each select="$var78_mappings/ns3:supportedHierarchy">
								<lgNaming:supportedHierarchy xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf10_supportedHierarchy">
										<xsl:with-param name="input" select="."/>
									</xsl:call-template>
								</lgNaming:supportedHierarchy>
							</xsl:for-each>
							<xsl:for-each select="$var78_mappings/ns3:supportedLanguage">
								<lgNaming:supportedLanguage xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf11_supportedLanguage">
										<xsl:with-param name="input" select="."/>
									</xsl:call-template>
								</lgNaming:supportedLanguage>
							</xsl:for-each>
							<xsl:for-each select="$var78_mappings/ns3:supportedNamespace">
								<lgNaming:supportedNamespace xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf12_supportedNamespace">
										<xsl:with-param name="input" select="."/>
									</xsl:call-template>
								</lgNaming:supportedNamespace>
							</xsl:for-each>
							<xsl:for-each select="$var78_mappings/ns3:supportedProperty">
								<xsl:variable name="var80_uri" select="@uri"/>
								<lgNaming:supportedProperty xsl:exclude-result-prefixes="lgNaming">
									<xsl:attribute name="localId" namespace="">
										<xsl:value-of select="string(@localId)"/>
									</xsl:attribute>
									<xsl:if test="string(boolean($var80_uri)) != 'false'">
										<xsl:attribute name="uri" namespace="">
											<xsl:value-of select="string($var80_uri)"/>
										</xsl:attribute>
									</xsl:if>
									<xsl:value-of select="string(.)"/>
								</lgNaming:supportedProperty>
							</xsl:for-each>
							<xsl:for-each select="$var78_mappings/ns3:supportedPropertyType">
								<lgNaming:supportedPropertyType xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf13_supportedPropertyType">
										<xsl:with-param name="input" select="."/>
									</xsl:call-template>
								</lgNaming:supportedPropertyType>
							</xsl:for-each>
							<xsl:for-each select="$var78_mappings/ns3:supportedPropertyLink">
								<lgNaming:supportedPropertyLink xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf14_supportedPropertyLink">
										<xsl:with-param name="input" select="."/>
									</xsl:call-template>
								</lgNaming:supportedPropertyLink>
							</xsl:for-each>
							<xsl:for-each select="$var78_mappings/ns3:supportedPropertyQualifier">
								<lgNaming:supportedPropertyQualifier xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf15_supportedPropertyQualifier">
										<xsl:with-param name="input" select="."/>
									</xsl:call-template>
								</lgNaming:supportedPropertyQualifier>
							</xsl:for-each>
							<xsl:for-each select="$var78_mappings/ns3:supportedPropertyQualifierType">
								<lgNaming:supportedPropertyQualifierType xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf16_supportedPropertyQualifierType">
										<xsl:with-param name="input" select="."/>
									</xsl:call-template>
								</lgNaming:supportedPropertyQualifierType>
							</xsl:for-each>
							<xsl:for-each select="$var78_mappings/ns3:supportedRepresentationalForm">
								<lgNaming:supportedRepresentationalForm xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf17_supportedRepresentationalForm">
										<xsl:with-param name="input" select="."/>
									</xsl:call-template>
								</lgNaming:supportedRepresentationalForm>
							</xsl:for-each>
							<xsl:for-each select="$var78_mappings/ns3:supportedSortOrder">
								<lgNaming:supportedSortOrder xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf18_supportedSortOrder">
										<xsl:with-param name="input" select="."/>
									</xsl:call-template>
								</lgNaming:supportedSortOrder>
							</xsl:for-each>
							<xsl:for-each select="$var78_mappings/ns3:supportedSource">
								<lgNaming:supportedSource xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf19_supportedSource">
										<xsl:with-param name="input" select="."/>
									</xsl:call-template>
								</lgNaming:supportedSource>
							</xsl:for-each>
							<xsl:for-each select="$var78_mappings/ns3:supportedSourceRole">
								<lgNaming:supportedSourceRole xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf20_supportedSourceRole">
										<xsl:with-param name="input" select="."/>
									</xsl:call-template>
								</lgNaming:supportedSourceRole>
							</xsl:for-each>
							<xsl:for-each select="$var78_mappings/ns3:supportedStatus">
								<lgNaming:supportedStatus xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf21_supportedStatus">
										<xsl:with-param name="input" select="."/>
									</xsl:call-template>
								</lgNaming:supportedStatus>
							</xsl:for-each>
						</lgVD:mappings>
						<xsl:for-each select="ns4:valueSetDefinition">
							<xsl:variable name="var81_isActive" select="@isActive"/>
							<xsl:variable name="var82_effectiveDate" select="@effectiveDate"/>
							<xsl:variable name="var83_expirationDate" select="@expirationDate"/>
							<xsl:variable name="var84_status" select="@status"/>
							<xsl:variable name="var85_valueSetDefinitionName" select="@valueSetDefinitionName"/>
							<xsl:variable name="var86_defaultCodingScheme" select="@defaultCodingScheme"/>
							<lgVD:valueDomainDefinition xsl:exclude-result-prefixes="lgVD">
								<xsl:if test="string(boolean($var81_isActive)) != 'false'">
									<xsl:attribute name="isActive" namespace="">
										<xsl:value-of select="string(((normalize-space(string($var81_isActive)) = 'true') or (normalize-space(string($var81_isActive)) = '1')))"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="string(boolean($var84_status)) != 'false'">
									<xsl:attribute name="status" namespace="">
										<xsl:value-of select="string($var84_status)"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="string(boolean($var82_effectiveDate)) != 'false'">
									<xsl:attribute name="effectiveDate" namespace="">
										<xsl:value-of select="string($var82_effectiveDate)"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="string(boolean($var83_expirationDate)) != 'false'">
									<xsl:attribute name="expirationDate" namespace="">
										<xsl:value-of select="string($var83_expirationDate)"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:attribute name="valueDomainURI" namespace="">
									<xsl:value-of select="string(@valueSetDefinitionURI)"/>
								</xsl:attribute>
								<xsl:if test="string(boolean($var85_valueSetDefinitionName)) != 'false'">
									<xsl:attribute name="valueDomainName" namespace="">
										<xsl:value-of select="string($var85_valueSetDefinitionName)"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="string(boolean($var86_defaultCodingScheme)) != 'false'">
									<xsl:attribute name="defaultCodingScheme" namespace="">
										<xsl:value-of select="string($var86_defaultCodingScheme)"/>
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
										<xsl:value-of select="string(.)"/>
									</lgCommon:entityDescription>
								</xsl:for-each>
								<xsl:for-each select="ns4:mappings">
									<lgVD:mappings xsl:exclude-result-prefixes="lgVD">
										<xsl:for-each select="ns3:supportedAssociation">
											<xsl:variable name="var87_uri" select="@uri"/>
											<lgNaming:supportedAssociation xsl:exclude-result-prefixes="lgNaming">
												<xsl:attribute name="localId" namespace="">
													<xsl:value-of select="string(@localId)"/>
												</xsl:attribute>
												<xsl:if test="string(boolean($var87_uri)) != 'false'">
													<xsl:attribute name="uri" namespace="">
														<xsl:value-of select="string($var87_uri)"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:value-of select="string(.)"/>
											</lgNaming:supportedAssociation>
										</xsl:for-each>
										<xsl:for-each select="ns3:supportedAssociationQualifier">
											<lgNaming:supportedAssociationQualifier xsl:exclude-result-prefixes="lgNaming">
												<xsl:call-template name="tbf:tbf3_supportedAssociationQualifier">
													<xsl:with-param name="input" select="."/>
												</xsl:call-template>
											</lgNaming:supportedAssociationQualifier>
										</xsl:for-each>
										<xsl:for-each select="ns3:supportedCodingScheme">
											<lgNaming:supportedCodingScheme xsl:exclude-result-prefixes="lgNaming">
												<xsl:call-template name="tbf:tbf4_supportedCodingScheme">
													<xsl:with-param name="input" select="."/>
												</xsl:call-template>
											</lgNaming:supportedCodingScheme>
										</xsl:for-each>
										<xsl:for-each select="ns3:supportedContext">
											<lgNaming:supportedContext xsl:exclude-result-prefixes="lgNaming">
												<xsl:call-template name="tbf:tbf6_supportedContext">
													<xsl:with-param name="input" select="."/>
												</xsl:call-template>
											</lgNaming:supportedContext>
										</xsl:for-each>
										<xsl:for-each select="ns3:supportedDataType">
											<lgNaming:supportedDataType xsl:exclude-result-prefixes="lgNaming">
												<xsl:call-template name="tbf:tbf7_supportedDataType">
													<xsl:with-param name="input" select="."/>
												</xsl:call-template>
											</lgNaming:supportedDataType>
										</xsl:for-each>
										<xsl:for-each select="ns3:supportedDegreeOfFidelity">
											<lgNaming:supportedDegreeOfFidelity xsl:exclude-result-prefixes="lgNaming">
												<xsl:call-template name="tbf:tbf8_supportedDegreeOfFidelity">
													<xsl:with-param name="input" select="."/>
												</xsl:call-template>
											</lgNaming:supportedDegreeOfFidelity>
										</xsl:for-each>
										<xsl:for-each select="ns3:supportedEntityType">
											<lgNaming:supportedEntityType xsl:exclude-result-prefixes="lgNaming">
												<xsl:call-template name="tbf:tbf9_supportedEntityType">
													<xsl:with-param name="input" select="."/>
												</xsl:call-template>
											</lgNaming:supportedEntityType>
										</xsl:for-each>
										<xsl:for-each select="ns3:supportedHierarchy">
											<lgNaming:supportedHierarchy xsl:exclude-result-prefixes="lgNaming">
												<xsl:call-template name="tbf:tbf10_supportedHierarchy">
													<xsl:with-param name="input" select="."/>
												</xsl:call-template>
											</lgNaming:supportedHierarchy>
										</xsl:for-each>
										<xsl:for-each select="ns3:supportedLanguage">
											<lgNaming:supportedLanguage xsl:exclude-result-prefixes="lgNaming">
												<xsl:call-template name="tbf:tbf11_supportedLanguage">
													<xsl:with-param name="input" select="."/>
												</xsl:call-template>
											</lgNaming:supportedLanguage>
										</xsl:for-each>
										<xsl:for-each select="ns3:supportedNamespace">
											<lgNaming:supportedNamespace xsl:exclude-result-prefixes="lgNaming">
												<xsl:call-template name="tbf:tbf12_supportedNamespace">
													<xsl:with-param name="input" select="."/>
												</xsl:call-template>
											</lgNaming:supportedNamespace>
										</xsl:for-each>
										<xsl:for-each select="ns3:supportedProperty">
											<xsl:variable name="var88_uri" select="@uri"/>
											<lgNaming:supportedProperty xsl:exclude-result-prefixes="lgNaming">
												<xsl:attribute name="localId" namespace="">
													<xsl:value-of select="string(@localId)"/>
												</xsl:attribute>
												<xsl:if test="string(boolean($var88_uri)) != 'false'">
													<xsl:attribute name="uri" namespace="">
														<xsl:value-of select="string($var88_uri)"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:value-of select="string(.)"/>
											</lgNaming:supportedProperty>
										</xsl:for-each>
										<xsl:for-each select="ns3:supportedPropertyType">
											<lgNaming:supportedPropertyType xsl:exclude-result-prefixes="lgNaming">
												<xsl:call-template name="tbf:tbf13_supportedPropertyType">
													<xsl:with-param name="input" select="."/>
												</xsl:call-template>
											</lgNaming:supportedPropertyType>
										</xsl:for-each>
										<xsl:for-each select="ns3:supportedPropertyLink">
											<lgNaming:supportedPropertyLink xsl:exclude-result-prefixes="lgNaming">
												<xsl:call-template name="tbf:tbf14_supportedPropertyLink">
													<xsl:with-param name="input" select="."/>
												</xsl:call-template>
											</lgNaming:supportedPropertyLink>
										</xsl:for-each>
										<xsl:for-each select="ns3:supportedPropertyQualifier">
											<lgNaming:supportedPropertyQualifier xsl:exclude-result-prefixes="lgNaming">
												<xsl:call-template name="tbf:tbf15_supportedPropertyQualifier">
													<xsl:with-param name="input" select="."/>
												</xsl:call-template>
											</lgNaming:supportedPropertyQualifier>
										</xsl:for-each>
										<xsl:for-each select="ns3:supportedPropertyQualifierType">
											<lgNaming:supportedPropertyQualifierType xsl:exclude-result-prefixes="lgNaming">
												<xsl:call-template name="tbf:tbf16_supportedPropertyQualifierType">
													<xsl:with-param name="input" select="."/>
												</xsl:call-template>
											</lgNaming:supportedPropertyQualifierType>
										</xsl:for-each>
										<xsl:for-each select="ns3:supportedRepresentationalForm">
											<lgNaming:supportedRepresentationalForm xsl:exclude-result-prefixes="lgNaming">
												<xsl:call-template name="tbf:tbf17_supportedRepresentationalForm">
													<xsl:with-param name="input" select="."/>
												</xsl:call-template>
											</lgNaming:supportedRepresentationalForm>
										</xsl:for-each>
										<xsl:for-each select="ns3:supportedSortOrder">
											<lgNaming:supportedSortOrder xsl:exclude-result-prefixes="lgNaming">
												<xsl:call-template name="tbf:tbf18_supportedSortOrder">
													<xsl:with-param name="input" select="."/>
												</xsl:call-template>
											</lgNaming:supportedSortOrder>
										</xsl:for-each>
										<xsl:for-each select="ns3:supportedSource">
											<lgNaming:supportedSource xsl:exclude-result-prefixes="lgNaming">
												<xsl:call-template name="tbf:tbf19_supportedSource">
													<xsl:with-param name="input" select="."/>
												</xsl:call-template>
											</lgNaming:supportedSource>
										</xsl:for-each>
										<xsl:for-each select="ns3:supportedSourceRole">
											<lgNaming:supportedSourceRole xsl:exclude-result-prefixes="lgNaming">
												<xsl:call-template name="tbf:tbf20_supportedSourceRole">
													<xsl:with-param name="input" select="."/>
												</xsl:call-template>
											</lgNaming:supportedSourceRole>
										</xsl:for-each>
										<xsl:for-each select="ns3:supportedStatus">
											<lgNaming:supportedStatus xsl:exclude-result-prefixes="lgNaming">
												<xsl:call-template name="tbf:tbf21_supportedStatus">
													<xsl:with-param name="input" select="."/>
												</xsl:call-template>
											</lgNaming:supportedStatus>
										</xsl:for-each>
									</lgVD:mappings>
								</xsl:for-each>
								<xsl:for-each select="ns4:source">
									<lgVD:source xsl:exclude-result-prefixes="lgVD">
										<xsl:call-template name="tbf:tbf2_source">
											<xsl:with-param name="input" select="."/>
										</xsl:call-template>
									</lgVD:source>
								</xsl:for-each>
								<xsl:for-each select="ns4:representsRealmOrContext">
									<lgVD:representsRealmOrContext xsl:exclude-result-prefixes="lgVD">
										<xsl:value-of select="string(.)"/>
									</lgVD:representsRealmOrContext>
								</xsl:for-each>
								<xsl:for-each select="ns4:properties">
									<lgVD:properties xsl:exclude-result-prefixes="lgVD">
										<xsl:for-each select="ns1:property">
											<xsl:variable name="var89_status" select="@status"/>
											<xsl:variable name="var90_propertyType" select="@propertyType"/>
											<xsl:variable name="var91_propertyId" select="@propertyId"/>
											<xsl:variable name="var92_language" select="@language"/>
											<xsl:variable name="var93_isActive" select="@isActive"/>
											<xsl:variable name="var94_effectiveDate" select="@effectiveDate"/>
											<xsl:variable name="var95_expirationDate" select="@expirationDate"/>
											<lgCommon:property xsl:exclude-result-prefixes="lgCommon">
												<xsl:if test="string(boolean($var93_isActive)) != 'false'">
													<xsl:attribute name="isActive" namespace="">
														<xsl:value-of select="string(((normalize-space(string($var93_isActive)) = 'true') or (normalize-space(string($var93_isActive)) = '1')))"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:if test="string(boolean($var89_status)) != 'false'">
													<xsl:attribute name="status" namespace="">
														<xsl:value-of select="string($var89_status)"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:if test="string(boolean($var94_effectiveDate)) != 'false'">
													<xsl:attribute name="effectiveDate" namespace="">
														<xsl:value-of select="string($var94_effectiveDate)"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:if test="string(boolean($var95_expirationDate)) != 'false'">
													<xsl:attribute name="expirationDate" namespace="">
														<xsl:value-of select="string($var95_expirationDate)"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:attribute name="propertyName" namespace="">
													<xsl:value-of select="string(@propertyName)"/>
												</xsl:attribute>
												<xsl:if test="string(boolean($var91_propertyId)) != 'false'">
													<xsl:attribute name="propertyId" namespace="">
														<xsl:value-of select="string($var91_propertyId)"/>
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
													<xsl:variable name="var96_propertyQualifierType" select="@propertyQualifierType"/>
													<lgCommon:propertyQualifier xsl:exclude-result-prefixes="lgCommon">
														<xsl:attribute name="propertyQualifierName" namespace="">
															<xsl:value-of select="string(@propertyQualifierName)"/>
														</xsl:attribute>
														<xsl:if test="string(boolean($var96_propertyQualifierType)) != 'false'">
															<xsl:attribute name="propertyQualifierType" namespace="">
																<xsl:value-of select="string($var96_propertyQualifierType)"/>
															</xsl:attribute>
														</xsl:if>
														<xsl:for-each select="ns1:value">
															<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
																<xsl:value-of select="string(.)"/>
															</lgCommon:value>
														</xsl:for-each>
													</lgCommon:propertyQualifier>
												</xsl:for-each>
												<xsl:for-each select="ns1:value">
													<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
														<xsl:call-template name="tbf:tbf23_text">
															<xsl:with-param name="input" select="."/>
														</xsl:call-template>
													</lgCommon:value>
												</xsl:for-each>
											</lgCommon:property>
										</xsl:for-each>
									</lgVD:properties>
								</xsl:for-each>
								<xsl:for-each select="ns4:definitionEntry">
									<xsl:variable name="var97_operator" select="@operator"/>
									<lgVD:definitionEntry xsl:exclude-result-prefixes="lgVD">
										<xsl:attribute name="ruleOrder" namespace="">
											<xsl:value-of select="string(number(string(@ruleOrder)))"/>
										</xsl:attribute>
										<xsl:if test="string(boolean($var97_operator)) != 'false'">
											<xsl:attribute name="operator" namespace="">
												<xsl:value-of select="string($var97_operator)"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:for-each select="ns4:entityReference">
											<lgVD:entityReference xsl:exclude-result-prefixes="lgVD">
												<xsl:call-template name="tbf:tbf24_entityReference">
													<xsl:with-param name="input" select="."/>
												</xsl:call-template>
											</lgVD:entityReference>
										</xsl:for-each>
										<xsl:for-each select="ns4:valueSetDefinitionReference">
											<lgVD:valueDomainReference xsl:exclude-result-prefixes="lgVD">
												<xsl:value-of select="/.."/>
											</lgVD:valueDomainReference>
										</xsl:for-each>
										<xsl:for-each select="ns4:codingSchemeReference">
											<lgVD:codingSchemeReference xsl:exclude-result-prefixes="lgVD">
												<xsl:call-template name="tbf:tbf25_codingSchemeReference">
													<xsl:with-param name="input" select="."/>
												</xsl:call-template>
											</lgVD:codingSchemeReference>
										</xsl:for-each>
									</lgVD:definitionEntry>
								</xsl:for-each>
							</lgVD:valueDomainDefinition>
						</xsl:for-each>
					</valueDomains>
				</xsl:for-each>
				<xsl:for-each select="ns5:pickListDefinitions">
					<xsl:variable name="var98_mappings" select="ns4:mappings"/>
					<pickLists>
						<lgVD:mappings xsl:exclude-result-prefixes="lgVD">
							<xsl:for-each select="$var98_mappings/ns3:supportedAssociation">
								<xsl:variable name="var99_uri" select="@uri"/>
								<lgNaming:supportedAssociation xsl:exclude-result-prefixes="lgNaming">
									<xsl:attribute name="localId" namespace="">
										<xsl:value-of select="string(@localId)"/>
									</xsl:attribute>
									<xsl:if test="string(boolean($var99_uri)) != 'false'">
										<xsl:attribute name="uri" namespace="">
											<xsl:value-of select="string($var99_uri)"/>
										</xsl:attribute>
									</xsl:if>
									<xsl:value-of select="string(.)"/>
								</lgNaming:supportedAssociation>
							</xsl:for-each>
							<xsl:for-each select="$var98_mappings/ns3:supportedAssociationQualifier">
								<lgNaming:supportedAssociationQualifier xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf3_supportedAssociationQualifier">
										<xsl:with-param name="input" select="."/>
									</xsl:call-template>
								</lgNaming:supportedAssociationQualifier>
							</xsl:for-each>
							<xsl:for-each select="$var98_mappings/ns3:supportedCodingScheme">
								<lgNaming:supportedCodingScheme xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf4_supportedCodingScheme">
										<xsl:with-param name="input" select="."/>
									</xsl:call-template>
								</lgNaming:supportedCodingScheme>
							</xsl:for-each>
							<xsl:for-each select="$var98_mappings/ns3:supportedContainerName">
								<lgNaming:supportedContainer xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf5_supportedContainerName">
										<xsl:with-param name="input" select="."/>
									</xsl:call-template>
								</lgNaming:supportedContainer>
							</xsl:for-each>
							<xsl:for-each select="$var98_mappings/ns3:supportedContext">
								<lgNaming:supportedContext xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf6_supportedContext">
										<xsl:with-param name="input" select="."/>
									</xsl:call-template>
								</lgNaming:supportedContext>
							</xsl:for-each>
							<xsl:for-each select="$var98_mappings/ns3:supportedDataType">
								<lgNaming:supportedDataType xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf7_supportedDataType">
										<xsl:with-param name="input" select="."/>
									</xsl:call-template>
								</lgNaming:supportedDataType>
							</xsl:for-each>
							<xsl:for-each select="$var98_mappings/ns3:supportedDegreeOfFidelity">
								<lgNaming:supportedDegreeOfFidelity xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf8_supportedDegreeOfFidelity">
										<xsl:with-param name="input" select="."/>
									</xsl:call-template>
								</lgNaming:supportedDegreeOfFidelity>
							</xsl:for-each>
							<xsl:for-each select="$var98_mappings/ns3:supportedEntityType">
								<lgNaming:supportedEntityType xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf9_supportedEntityType">
										<xsl:with-param name="input" select="."/>
									</xsl:call-template>
								</lgNaming:supportedEntityType>
							</xsl:for-each>
							<xsl:for-each select="$var98_mappings/ns3:supportedHierarchy">
								<lgNaming:supportedHierarchy xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf10_supportedHierarchy">
										<xsl:with-param name="input" select="."/>
									</xsl:call-template>
								</lgNaming:supportedHierarchy>
							</xsl:for-each>
							<xsl:for-each select="$var98_mappings/ns3:supportedLanguage">
								<lgNaming:supportedLanguage xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf11_supportedLanguage">
										<xsl:with-param name="input" select="."/>
									</xsl:call-template>
								</lgNaming:supportedLanguage>
							</xsl:for-each>
							<xsl:for-each select="$var98_mappings/ns3:supportedNamespace">
								<lgNaming:supportedNamespace xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf12_supportedNamespace">
										<xsl:with-param name="input" select="."/>
									</xsl:call-template>
								</lgNaming:supportedNamespace>
							</xsl:for-each>
							<xsl:for-each select="$var98_mappings/ns3:supportedProperty">
								<xsl:variable name="var100_uri" select="@uri"/>
								<lgNaming:supportedProperty xsl:exclude-result-prefixes="lgNaming">
									<xsl:attribute name="localId" namespace="">
										<xsl:value-of select="string(@localId)"/>
									</xsl:attribute>
									<xsl:if test="string(boolean($var100_uri)) != 'false'">
										<xsl:attribute name="uri" namespace="">
											<xsl:value-of select="string($var100_uri)"/>
										</xsl:attribute>
									</xsl:if>
									<xsl:value-of select="string(.)"/>
								</lgNaming:supportedProperty>
							</xsl:for-each>
							<xsl:for-each select="$var98_mappings/ns3:supportedPropertyType">
								<lgNaming:supportedPropertyType xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf13_supportedPropertyType">
										<xsl:with-param name="input" select="."/>
									</xsl:call-template>
								</lgNaming:supportedPropertyType>
							</xsl:for-each>
							<xsl:for-each select="$var98_mappings/ns3:supportedPropertyLink">
								<lgNaming:supportedPropertyLink xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf14_supportedPropertyLink">
										<xsl:with-param name="input" select="."/>
									</xsl:call-template>
								</lgNaming:supportedPropertyLink>
							</xsl:for-each>
							<xsl:for-each select="$var98_mappings/ns3:supportedPropertyQualifier">
								<lgNaming:supportedPropertyQualifier xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf15_supportedPropertyQualifier">
										<xsl:with-param name="input" select="."/>
									</xsl:call-template>
								</lgNaming:supportedPropertyQualifier>
							</xsl:for-each>
							<xsl:for-each select="$var98_mappings/ns3:supportedPropertyQualifierType">
								<lgNaming:supportedPropertyQualifierType xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf16_supportedPropertyQualifierType">
										<xsl:with-param name="input" select="."/>
									</xsl:call-template>
								</lgNaming:supportedPropertyQualifierType>
							</xsl:for-each>
							<xsl:for-each select="$var98_mappings/ns3:supportedRepresentationalForm">
								<lgNaming:supportedRepresentationalForm xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf17_supportedRepresentationalForm">
										<xsl:with-param name="input" select="."/>
									</xsl:call-template>
								</lgNaming:supportedRepresentationalForm>
							</xsl:for-each>
							<xsl:for-each select="$var98_mappings/ns3:supportedSortOrder">
								<lgNaming:supportedSortOrder xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf18_supportedSortOrder">
										<xsl:with-param name="input" select="."/>
									</xsl:call-template>
								</lgNaming:supportedSortOrder>
							</xsl:for-each>
							<xsl:for-each select="$var98_mappings/ns3:supportedSource">
								<lgNaming:supportedSource xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf19_supportedSource">
										<xsl:with-param name="input" select="."/>
									</xsl:call-template>
								</lgNaming:supportedSource>
							</xsl:for-each>
							<xsl:for-each select="$var98_mappings/ns3:supportedSourceRole">
								<lgNaming:supportedSourceRole xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf20_supportedSourceRole">
										<xsl:with-param name="input" select="."/>
									</xsl:call-template>
								</lgNaming:supportedSourceRole>
							</xsl:for-each>
							<xsl:for-each select="$var98_mappings/ns3:supportedStatus">
								<lgNaming:supportedStatus xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf21_supportedStatus">
										<xsl:with-param name="input" select="."/>
									</xsl:call-template>
								</lgNaming:supportedStatus>
							</xsl:for-each>
						</lgVD:mappings>
						<xsl:for-each select="ns4:pickListDefinition">
							<xsl:variable name="var101_status" select="@status"/>
							<xsl:variable name="var102_isActive" select="@isActive"/>
							<xsl:variable name="var103_defaultSortOrder" select="@defaultSortOrder"/>
							<xsl:variable name="var104_effectiveDate" select="@effectiveDate"/>
							<xsl:variable name="var105_expirationDate" select="@expirationDate"/>
							<xsl:variable name="var106_defaultLanguage" select="@defaultLanguage"/>
							<xsl:variable name="var107_defaultEntityCodeNamespace" select="@defaultEntityCodeNamespace"/>
							<lgVD:pickListDefinition xsl:exclude-result-prefixes="lgVD">
								<xsl:if test="string(boolean($var102_isActive)) != 'false'">
									<xsl:attribute name="isActive" namespace="">
										<xsl:value-of select="string(((normalize-space(string($var102_isActive)) = 'true') or (normalize-space(string($var102_isActive)) = '1')))"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="string(boolean($var101_status)) != 'false'">
									<xsl:attribute name="status" namespace="">
										<xsl:value-of select="string($var101_status)"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="string(boolean($var104_effectiveDate)) != 'false'">
									<xsl:attribute name="effectiveDate" namespace="">
										<xsl:value-of select="string($var104_effectiveDate)"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="string(boolean($var105_expirationDate)) != 'false'">
									<xsl:attribute name="expirationDate" namespace="">
										<xsl:value-of select="string($var105_expirationDate)"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:attribute name="pickListId" namespace="">
									<xsl:value-of select="string(@pickListId)"/>
								</xsl:attribute>
								<xsl:if test="string(boolean($var107_defaultEntityCodeNamespace)) != 'false'">
									<xsl:attribute name="defaultEntityCodeNamespace" namespace="">
										<xsl:value-of select="string($var107_defaultEntityCodeNamespace)"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="string(boolean($var106_defaultLanguage)) != 'false'">
									<xsl:attribute name="defaultLanguage" namespace="">
										<xsl:value-of select="string($var106_defaultLanguage)"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="string(boolean($var103_defaultSortOrder)) != 'false'">
									<xsl:attribute name="defaultSortOrder" namespace="">
										<xsl:value-of select="string($var103_defaultSortOrder)"/>
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
										<xsl:value-of select="string(.)"/>
									</lgCommon:entityDescription>
								</xsl:for-each>
							</lgVD:pickListDefinition>
						</xsl:for-each>
					</pickLists>
				</xsl:for-each>
				<xsl:for-each select="ns5:editHistory">
					<editHistory>
						<xsl:for-each select="ns5:revision">
							<xsl:variable name="var108_editOrder" select="@editOrder"/>
							<xsl:variable name="var109_revisionDate" select="@revisionDate"/>
							<revision>
								<xsl:attribute name="revisionId" namespace="">
									<xsl:value-of select="string(@revisionId)"/>
								</xsl:attribute>
								<xsl:if test="string(boolean($var109_revisionDate)) != 'false'">
									<xsl:attribute name="revisionDate" namespace="">
										<xsl:value-of select="string($var109_revisionDate)"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="string(boolean($var108_editOrder)) != 'false'">
									<xsl:attribute name="editOrder" namespace="">
										<xsl:value-of select="string(number(string($var108_editOrder)))"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:for-each select="ns1:entityDescription">
									<lgCommon:entityDescription xsl:exclude-result-prefixes="lgCommon">
										<xsl:for-each select="node()[boolean(self::text())]">
											<xsl:value-of select="string(.)"/>
										</xsl:for-each>
									</lgCommon:entityDescription>
								</xsl:for-each>
								<xsl:for-each select="ns5:changeAgent">
									<changeAgent>
										<xsl:value-of select="string(.)"/>
									</changeAgent>
								</xsl:for-each>
								<xsl:for-each select="ns5:changeInstructions">
									<xsl:variable name="var110_dataType" select="@dataType"/>
									<changeInstructions>
										<xsl:if test="string(boolean($var110_dataType)) != 'false'">
											<xsl:attribute name="dataType" namespace="">
												<xsl:value-of select="string($var110_dataType)"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:for-each select="node()[boolean(self::text())]">
											<xsl:value-of select="string(.)"/>
										</xsl:for-each>
									</changeInstructions>
								</xsl:for-each>
								<xsl:for-each select="ns5:changedEntry">
									<changedEntry>
										<xsl:for-each select="ns5:changedCodingSchemeEntry">
											<xsl:variable name="var111_isActive" select="@isActive"/>
											<xsl:variable name="var112_effectiveDate" select="@effectiveDate"/>
											<xsl:variable name="var113_expirationDate" select="@expirationDate"/>
											<xsl:variable name="var114_formalName" select="@formalName"/>
											<xsl:variable name="var115_defaultLanguage" select="@defaultLanguage"/>
											<xsl:variable name="var116_status" select="@status"/>
											<xsl:variable name="var117_approxNumConcepts" select="@approxNumConcepts"/>
											<xsl:variable name="var118_mappings" select="ns0:mappings"/>
											<changedCodingSchemeEntry>
												<xsl:if test="string(boolean($var111_isActive)) != 'false'">
													<xsl:attribute name="isActive" namespace="">
														<xsl:value-of select="string(((normalize-space(string($var111_isActive)) = 'true') or (normalize-space(string($var111_isActive)) = '1')))"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:if test="string(boolean($var116_status)) != 'false'">
													<xsl:attribute name="status" namespace="">
														<xsl:value-of select="string($var116_status)"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:if test="string(boolean($var112_effectiveDate)) != 'false'">
													<xsl:attribute name="effectiveDate" namespace="">
														<xsl:value-of select="string($var112_effectiveDate)"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:if test="string(boolean($var113_expirationDate)) != 'false'">
													<xsl:attribute name="expirationDate" namespace="">
														<xsl:value-of select="string($var113_expirationDate)"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:attribute name="codingSchemeName" namespace="">
													<xsl:value-of select="string(@codingSchemeName)"/>
												</xsl:attribute>
												<xsl:attribute name="codingSchemeURI" namespace="">
													<xsl:value-of select="string(@codingSchemeURI)"/>
												</xsl:attribute>
												<xsl:if test="string(boolean($var114_formalName)) != 'false'">
													<xsl:attribute name="formalName" namespace="">
														<xsl:value-of select="string($var114_formalName)"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:if test="string(boolean($var115_defaultLanguage)) != 'false'">
													<xsl:attribute name="defaultLanguage" namespace="">
														<xsl:value-of select="string($var115_defaultLanguage)"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:if test="string(boolean($var117_approxNumConcepts)) != 'false'">
													<xsl:attribute name="approxNumConcepts" namespace="">
														<xsl:value-of select="string(number(string($var117_approxNumConcepts)))"/>
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
													<lgCS:localName xsl:exclude-result-prefixes="lgCS">
														<xsl:value-of select="string(.)"/>
													</lgCS:localName>
												</xsl:for-each>
												<xsl:for-each select="ns0:source">
													<lgCS:source xsl:exclude-result-prefixes="lgCS">
														<xsl:call-template name="tbf:tbf2_source">
															<xsl:with-param name="input" select="."/>
														</xsl:call-template>
													</lgCS:source>
												</xsl:for-each>
												<xsl:for-each select="ns0:copyright">
													<xsl:variable name="var119_dataType" select="@dataType"/>
													<lgCS:copyright xsl:exclude-result-prefixes="lgCS">
														<xsl:if test="string(boolean($var119_dataType)) != 'false'">
															<xsl:attribute name="dataType" namespace="">
																<xsl:value-of select="string($var119_dataType)"/>
															</xsl:attribute>
														</xsl:if>
														<xsl:for-each select="node()[boolean(self::text())]">
															<xsl:value-of select="string(.)"/>
														</xsl:for-each>
													</lgCS:copyright>
												</xsl:for-each>
												<lgCS:mappings xsl:exclude-result-prefixes="lgCS">
													<xsl:for-each select="$var118_mappings/ns3:supportedAssociation">
														<xsl:variable name="var120_uri" select="@uri"/>
														<lgNaming:supportedAssociation xsl:exclude-result-prefixes="lgNaming">
															<xsl:attribute name="localId" namespace="">
																<xsl:value-of select="string(@localId)"/>
															</xsl:attribute>
															<xsl:if test="string(boolean($var120_uri)) != 'false'">
																<xsl:attribute name="uri" namespace="">
																	<xsl:value-of select="string($var120_uri)"/>
																</xsl:attribute>
															</xsl:if>
															<xsl:value-of select="string(.)"/>
														</lgNaming:supportedAssociation>
													</xsl:for-each>
													<xsl:for-each select="$var118_mappings/ns3:supportedAssociationQualifier">
														<lgNaming:supportedAssociationQualifier xsl:exclude-result-prefixes="lgNaming">
															<xsl:call-template name="tbf:tbf3_supportedAssociationQualifier">
																<xsl:with-param name="input" select="."/>
															</xsl:call-template>
														</lgNaming:supportedAssociationQualifier>
													</xsl:for-each>
													<xsl:for-each select="$var118_mappings/ns3:supportedCodingScheme">
														<lgNaming:supportedCodingScheme xsl:exclude-result-prefixes="lgNaming">
															<xsl:call-template name="tbf:tbf4_supportedCodingScheme">
																<xsl:with-param name="input" select="."/>
															</xsl:call-template>
														</lgNaming:supportedCodingScheme>
													</xsl:for-each>
													<xsl:for-each select="$var118_mappings/ns3:supportedContext">
														<lgNaming:supportedContext xsl:exclude-result-prefixes="lgNaming">
															<xsl:call-template name="tbf:tbf6_supportedContext">
																<xsl:with-param name="input" select="."/>
															</xsl:call-template>
														</lgNaming:supportedContext>
													</xsl:for-each>
													<xsl:for-each select="$var118_mappings/ns3:supportedDataType">
														<lgNaming:supportedDataType xsl:exclude-result-prefixes="lgNaming">
															<xsl:call-template name="tbf:tbf7_supportedDataType">
																<xsl:with-param name="input" select="."/>
															</xsl:call-template>
														</lgNaming:supportedDataType>
													</xsl:for-each>
													<xsl:for-each select="$var118_mappings/ns3:supportedDegreeOfFidelity">
														<lgNaming:supportedDegreeOfFidelity xsl:exclude-result-prefixes="lgNaming">
															<xsl:call-template name="tbf:tbf8_supportedDegreeOfFidelity">
																<xsl:with-param name="input" select="."/>
															</xsl:call-template>
														</lgNaming:supportedDegreeOfFidelity>
													</xsl:for-each>
													<xsl:for-each select="$var118_mappings/ns3:supportedEntityType">
														<lgNaming:supportedEntityType xsl:exclude-result-prefixes="lgNaming">
															<xsl:call-template name="tbf:tbf9_supportedEntityType">
																<xsl:with-param name="input" select="."/>
															</xsl:call-template>
														</lgNaming:supportedEntityType>
													</xsl:for-each>
													<xsl:for-each select="$var118_mappings/ns3:supportedHierarchy">
														<lgNaming:supportedHierarchy xsl:exclude-result-prefixes="lgNaming">
															<xsl:call-template name="tbf:tbf10_supportedHierarchy">
																<xsl:with-param name="input" select="."/>
															</xsl:call-template>
														</lgNaming:supportedHierarchy>
													</xsl:for-each>
													<xsl:for-each select="$var118_mappings/ns3:supportedLanguage">
														<lgNaming:supportedLanguage xsl:exclude-result-prefixes="lgNaming">
															<xsl:call-template name="tbf:tbf11_supportedLanguage">
																<xsl:with-param name="input" select="."/>
															</xsl:call-template>
														</lgNaming:supportedLanguage>
													</xsl:for-each>
													<xsl:for-each select="$var118_mappings/ns3:supportedNamespace">
														<lgNaming:supportedNamespace xsl:exclude-result-prefixes="lgNaming">
															<xsl:call-template name="tbf:tbf12_supportedNamespace">
																<xsl:with-param name="input" select="."/>
															</xsl:call-template>
														</lgNaming:supportedNamespace>
													</xsl:for-each>
													<xsl:for-each select="$var118_mappings/ns3:supportedProperty">
														<xsl:variable name="var121_uri" select="@uri"/>
														<lgNaming:supportedProperty xsl:exclude-result-prefixes="lgNaming">
															<xsl:attribute name="localId" namespace="">
																<xsl:value-of select="string(@localId)"/>
															</xsl:attribute>
															<xsl:if test="string(boolean($var121_uri)) != 'false'">
																<xsl:attribute name="uri" namespace="">
																	<xsl:value-of select="string($var121_uri)"/>
																</xsl:attribute>
															</xsl:if>
															<xsl:value-of select="string(.)"/>
														</lgNaming:supportedProperty>
													</xsl:for-each>
													<xsl:for-each select="$var118_mappings/ns3:supportedPropertyType">
														<lgNaming:supportedPropertyType xsl:exclude-result-prefixes="lgNaming">
															<xsl:call-template name="tbf:tbf13_supportedPropertyType">
																<xsl:with-param name="input" select="."/>
															</xsl:call-template>
														</lgNaming:supportedPropertyType>
													</xsl:for-each>
													<xsl:for-each select="$var118_mappings/ns3:supportedPropertyLink">
														<lgNaming:supportedPropertyLink xsl:exclude-result-prefixes="lgNaming">
															<xsl:call-template name="tbf:tbf14_supportedPropertyLink">
																<xsl:with-param name="input" select="."/>
															</xsl:call-template>
														</lgNaming:supportedPropertyLink>
													</xsl:for-each>
													<xsl:for-each select="$var118_mappings/ns3:supportedPropertyQualifier">
														<lgNaming:supportedPropertyQualifier xsl:exclude-result-prefixes="lgNaming">
															<xsl:call-template name="tbf:tbf15_supportedPropertyQualifier">
																<xsl:with-param name="input" select="."/>
															</xsl:call-template>
														</lgNaming:supportedPropertyQualifier>
													</xsl:for-each>
													<xsl:for-each select="$var118_mappings/ns3:supportedPropertyQualifierType">
														<lgNaming:supportedPropertyQualifierType xsl:exclude-result-prefixes="lgNaming">
															<xsl:call-template name="tbf:tbf16_supportedPropertyQualifierType">
																<xsl:with-param name="input" select="."/>
															</xsl:call-template>
														</lgNaming:supportedPropertyQualifierType>
													</xsl:for-each>
													<xsl:for-each select="$var118_mappings/ns3:supportedRepresentationalForm">
														<lgNaming:supportedRepresentationalForm xsl:exclude-result-prefixes="lgNaming">
															<xsl:call-template name="tbf:tbf17_supportedRepresentationalForm">
																<xsl:with-param name="input" select="."/>
															</xsl:call-template>
														</lgNaming:supportedRepresentationalForm>
													</xsl:for-each>
													<xsl:for-each select="$var118_mappings/ns3:supportedSortOrder">
														<lgNaming:supportedSortOrder xsl:exclude-result-prefixes="lgNaming">
															<xsl:call-template name="tbf:tbf18_supportedSortOrder">
																<xsl:with-param name="input" select="."/>
															</xsl:call-template>
														</lgNaming:supportedSortOrder>
													</xsl:for-each>
													<xsl:for-each select="$var118_mappings/ns3:supportedSource">
														<lgNaming:supportedSource xsl:exclude-result-prefixes="lgNaming">
															<xsl:call-template name="tbf:tbf19_supportedSource">
																<xsl:with-param name="input" select="."/>
															</xsl:call-template>
														</lgNaming:supportedSource>
													</xsl:for-each>
													<xsl:for-each select="$var118_mappings/ns3:supportedSourceRole">
														<lgNaming:supportedSourceRole xsl:exclude-result-prefixes="lgNaming">
															<xsl:call-template name="tbf:tbf20_supportedSourceRole">
																<xsl:with-param name="input" select="."/>
															</xsl:call-template>
														</lgNaming:supportedSourceRole>
													</xsl:for-each>
													<xsl:for-each select="$var118_mappings/ns3:supportedStatus">
														<lgNaming:supportedStatus xsl:exclude-result-prefixes="lgNaming">
															<xsl:call-template name="tbf:tbf21_supportedStatus">
																<xsl:with-param name="input" select="."/>
															</xsl:call-template>
														</lgNaming:supportedStatus>
													</xsl:for-each>
												</lgCS:mappings>
												<xsl:for-each select="ns0:properties">
													<lgCS:properties xsl:exclude-result-prefixes="lgCS">
														<xsl:for-each select="ns1:property">
															<xsl:variable name="var122_propertyType" select="@propertyType"/>
															<xsl:variable name="var123_propertyId" select="@propertyId"/>
															<xsl:variable name="var124_isActive" select="@isActive"/>
															<xsl:variable name="var125_effectiveDate" select="@effectiveDate"/>
															<xsl:variable name="var126_expirationDate" select="@expirationDate"/>
															<xsl:variable name="var127_language" select="@language"/>
															<xsl:variable name="var128_status" select="@status"/>
															<lgCommon:property xsl:exclude-result-prefixes="lgCommon">
																<xsl:if test="string(boolean($var124_isActive)) != 'false'">
																	<xsl:attribute name="isActive" namespace="">
																		<xsl:value-of select="string(((normalize-space(string($var124_isActive)) = 'true') or (normalize-space(string($var124_isActive)) = '1')))"/>
																	</xsl:attribute>
																</xsl:if>
																<xsl:if test="string(boolean($var128_status)) != 'false'">
																	<xsl:attribute name="status" namespace="">
																		<xsl:value-of select="string($var128_status)"/>
																	</xsl:attribute>
																</xsl:if>
																<xsl:if test="string(boolean($var125_effectiveDate)) != 'false'">
																	<xsl:attribute name="effectiveDate" namespace="">
																		<xsl:value-of select="string($var125_effectiveDate)"/>
																	</xsl:attribute>
																</xsl:if>
																<xsl:if test="string(boolean($var126_expirationDate)) != 'false'">
																	<xsl:attribute name="expirationDate" namespace="">
																		<xsl:value-of select="string($var126_expirationDate)"/>
																	</xsl:attribute>
																</xsl:if>
																<xsl:attribute name="propertyName" namespace="">
																	<xsl:value-of select="string(@propertyName)"/>
																</xsl:attribute>
																<xsl:if test="string(boolean($var123_propertyId)) != 'false'">
																	<xsl:attribute name="propertyId" namespace="">
																		<xsl:value-of select="string($var123_propertyId)"/>
																	</xsl:attribute>
																</xsl:if>
																<xsl:if test="string(boolean($var122_propertyType)) != 'false'">
																	<xsl:attribute name="propertyType" namespace="">
																		<xsl:value-of select="string($var122_propertyType)"/>
																	</xsl:attribute>
																</xsl:if>
																<xsl:if test="string(boolean($var127_language)) != 'false'">
																	<xsl:attribute name="language" namespace="">
																		<xsl:value-of select="string($var127_language)"/>
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
																	<xsl:variable name="var129_propertyQualifierType" select="@propertyQualifierType"/>
																	<lgCommon:propertyQualifier xsl:exclude-result-prefixes="lgCommon">
																		<xsl:attribute name="propertyQualifierName" namespace="">
																			<xsl:value-of select="string(@propertyQualifierName)"/>
																		</xsl:attribute>
																		<xsl:if test="string(boolean($var129_propertyQualifierType)) != 'false'">
																			<xsl:attribute name="propertyQualifierType" namespace="">
																				<xsl:value-of select="string($var129_propertyQualifierType)"/>
																			</xsl:attribute>
																		</xsl:if>
																		<xsl:for-each select="ns1:value">
																			<xsl:variable name="var130_dataType" select="@dataType"/>
																			<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
																				<xsl:if test="string(boolean($var130_dataType)) != 'false'">
																					<xsl:attribute name="dataType" namespace="">
																						<xsl:value-of select="string($var130_dataType)"/>
																					</xsl:attribute>
																				</xsl:if>
																				<xsl:for-each select="node()[boolean(self::text())]">
																					<xsl:value-of select="string(.)"/>
																				</xsl:for-each>
																			</lgCommon:value>
																		</xsl:for-each>
																	</lgCommon:propertyQualifier>
																</xsl:for-each>
																<xsl:for-each select="ns1:value">
																	<xsl:variable name="var131_dataType" select="@dataType"/>
																	<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
																		<xsl:if test="string(boolean($var131_dataType)) != 'false'">
																			<xsl:attribute name="dataType" namespace="">
																				<xsl:value-of select="string($var131_dataType)"/>
																			</xsl:attribute>
																		</xsl:if>
																		<xsl:for-each select="node()[boolean(self::text())]">
																			<xsl:value-of select="string(.)"/>
																		</xsl:for-each>
																	</lgCommon:value>
																</xsl:for-each>
															</lgCommon:property>
														</xsl:for-each>
													</lgCS:properties>
												</xsl:for-each>
												<xsl:for-each select="ns0:entities">
													<lgCS:entities xsl:exclude-result-prefixes="lgCS">
														<xsl:for-each select="ns2:entity">
															<xsl:variable name="var132_status" select="@status"/>
															<xsl:variable name="var133_isAnonymous" select="@isAnonymous"/>
															<xsl:variable name="var134_isDefined" select="@isDefined"/>
															<xsl:variable name="var135_isActive" select="@isActive"/>
															<xsl:variable name="var136_effectiveDate" select="@effectiveDate"/>
															<xsl:variable name="var137_expirationDate" select="@expirationDate"/>
															<xsl:variable name="var138_entityCodeNamespace" select="@entityCodeNamespace"/>
															<lgCon:entity xsl:exclude-result-prefixes="lgCon">
																<xsl:if test="string(boolean($var135_isActive)) != 'false'">
																	<xsl:attribute name="isActive" namespace="">
																		<xsl:value-of select="string(((normalize-space(string($var135_isActive)) = 'true') or (normalize-space(string($var135_isActive)) = '1')))"/>
																	</xsl:attribute>
																</xsl:if>
																<xsl:if test="string(boolean($var132_status)) != 'false'">
																	<xsl:attribute name="status" namespace="">
																		<xsl:value-of select="string($var132_status)"/>
																	</xsl:attribute>
																</xsl:if>
																<xsl:if test="string(boolean($var136_effectiveDate)) != 'false'">
																	<xsl:attribute name="effectiveDate" namespace="">
																		<xsl:value-of select="string($var136_effectiveDate)"/>
																	</xsl:attribute>
																</xsl:if>
																<xsl:if test="string(boolean($var137_expirationDate)) != 'false'">
																	<xsl:attribute name="expirationDate" namespace="">
																		<xsl:value-of select="string($var137_expirationDate)"/>
																	</xsl:attribute>
																</xsl:if>
																<xsl:attribute name="entityCode" namespace="">
																	<xsl:value-of select="string(@entityCode)"/>
																</xsl:attribute>
																<xsl:if test="string(boolean($var138_entityCodeNamespace)) != 'false'">
																	<xsl:attribute name="entityCodeNamespace" namespace="">
																		<xsl:value-of select="string($var138_entityCodeNamespace)"/>
																	</xsl:attribute>
																</xsl:if>
																<xsl:if test="string(boolean($var133_isAnonymous)) != 'false'">
																	<xsl:attribute name="isAnonymous" namespace="">
																		<xsl:value-of select="string(((normalize-space(string($var133_isAnonymous)) = 'true') or (normalize-space(string($var133_isAnonymous)) = '1')))"/>
																	</xsl:attribute>
																</xsl:if>
																<xsl:if test="string(boolean($var134_isDefined)) != 'false'">
																	<xsl:attribute name="isDefined" namespace="">
																		<xsl:value-of select="string(((normalize-space(string($var134_isDefined)) = 'true') or (normalize-space(string($var134_isDefined)) = '1')))"/>
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
																	<xsl:variable name="var139_representationalForm" select="@representationalForm"/>
																	<xsl:variable name="var140_matchIfNoContext" select="@matchIfNoContext"/>
																	<xsl:variable name="var141_propertyId" select="@propertyId"/>
																	<xsl:variable name="var142_propertyType" select="@propertyType"/>
																	<xsl:variable name="var143_isActive" select="@isActive"/>
																	<xsl:variable name="var144_effectiveDate" select="@effectiveDate"/>
																	<xsl:variable name="var145_expirationDate" select="@expirationDate"/>
																	<xsl:variable name="var146_language" select="@language"/>
																	<xsl:variable name="var147_isPreferred" select="@isPreferred"/>
																	<xsl:variable name="var148_degreeOfFidelity" select="@degreeOfFidelity"/>
																	<xsl:variable name="var149_status" select="@status"/>
																	<lgCon:presentation xsl:exclude-result-prefixes="lgCon">
																		<xsl:if test="string(boolean($var143_isActive)) != 'false'">
																			<xsl:attribute name="isActive" namespace="">
																				<xsl:value-of select="string(((normalize-space(string($var143_isActive)) = 'true') or (normalize-space(string($var143_isActive)) = '1')))"/>
																			</xsl:attribute>
																		</xsl:if>
																		<xsl:if test="string(boolean($var149_status)) != 'false'">
																			<xsl:attribute name="status" namespace="">
																				<xsl:value-of select="string($var149_status)"/>
																			</xsl:attribute>
																		</xsl:if>
																		<xsl:if test="string(boolean($var144_effectiveDate)) != 'false'">
																			<xsl:attribute name="effectiveDate" namespace="">
																				<xsl:value-of select="string($var144_effectiveDate)"/>
																			</xsl:attribute>
																		</xsl:if>
																		<xsl:if test="string(boolean($var145_expirationDate)) != 'false'">
																			<xsl:attribute name="expirationDate" namespace="">
																				<xsl:value-of select="string($var145_expirationDate)"/>
																			</xsl:attribute>
																		</xsl:if>
																		<xsl:attribute name="propertyName" namespace="">
																			<xsl:value-of select="string(@propertyName)"/>
																		</xsl:attribute>
																		<xsl:if test="string(boolean($var141_propertyId)) != 'false'">
																			<xsl:attribute name="propertyId" namespace="">
																				<xsl:value-of select="string($var141_propertyId)"/>
																			</xsl:attribute>
																		</xsl:if>
																		<xsl:if test="string(boolean($var142_propertyType)) != 'false'">
																			<xsl:attribute name="propertyType" namespace="">
																				<xsl:value-of select="string($var142_propertyType)"/>
																			</xsl:attribute>
																		</xsl:if>
																		<xsl:if test="string(boolean($var146_language)) != 'false'">
																			<xsl:attribute name="language" namespace="">
																				<xsl:value-of select="string($var146_language)"/>
																			</xsl:attribute>
																		</xsl:if>
																		<xsl:if test="string(boolean($var147_isPreferred)) != 'false'">
																			<xsl:attribute name="isPreferred" namespace="">
																				<xsl:value-of select="string(((normalize-space(string($var147_isPreferred)) = 'true') or (normalize-space(string($var147_isPreferred)) = '1')))"/>
																			</xsl:attribute>
																		</xsl:if>
																		<xsl:if test="string(boolean($var148_degreeOfFidelity)) != 'false'">
																			<xsl:attribute name="degreeOfFidelity" namespace="">
																				<xsl:value-of select="string($var148_degreeOfFidelity)"/>
																			</xsl:attribute>
																		</xsl:if>
																		<xsl:if test="string(boolean($var140_matchIfNoContext)) != 'false'">
																			<xsl:attribute name="matchIfNoContext" namespace="">
																				<xsl:value-of select="string(((normalize-space(string($var140_matchIfNoContext)) = 'true') or (normalize-space(string($var140_matchIfNoContext)) = '1')))"/>
																			</xsl:attribute>
																		</xsl:if>
																		<xsl:if test="string(boolean($var139_representationalForm)) != 'false'">
																			<xsl:attribute name="representationalForm" namespace="">
																				<xsl:value-of select="string($var139_representationalForm)"/>
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
																			<xsl:variable name="var150_propertyQualifierType" select="@propertyQualifierType"/>
																			<lgCommon:propertyQualifier xsl:exclude-result-prefixes="lgCommon">
																				<xsl:attribute name="propertyQualifierName" namespace="">
																					<xsl:value-of select="string(@propertyQualifierName)"/>
																				</xsl:attribute>
																				<xsl:if test="string(boolean($var150_propertyQualifierType)) != 'false'">
																					<xsl:attribute name="propertyQualifierType" namespace="">
																						<xsl:value-of select="string($var150_propertyQualifierType)"/>
																					</xsl:attribute>
																				</xsl:if>
																				<xsl:for-each select="ns1:value">
																					<xsl:variable name="var151_dataType" select="@dataType"/>
																					<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
																						<xsl:if test="string(boolean($var151_dataType)) != 'false'">
																							<xsl:attribute name="dataType" namespace="">
																								<xsl:value-of select="string($var151_dataType)"/>
																							</xsl:attribute>
																						</xsl:if>
																						<xsl:for-each select="node()[boolean(self::text())]">
																							<xsl:value-of select="string(.)"/>
																						</xsl:for-each>
																					</lgCommon:value>
																				</xsl:for-each>
																			</lgCommon:propertyQualifier>
																		</xsl:for-each>
																		<xsl:for-each select="ns1:value">
																			<xsl:variable name="var152_dataType" select="@dataType"/>
																			<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
																				<xsl:if test="string(boolean($var152_dataType)) != 'false'">
																					<xsl:attribute name="dataType" namespace="">
																						<xsl:value-of select="string($var152_dataType)"/>
																					</xsl:attribute>
																				</xsl:if>
																				<xsl:for-each select="node()[boolean(self::text())]">
																					<xsl:value-of select="string(.)"/>
																				</xsl:for-each>
																			</lgCommon:value>
																		</xsl:for-each>
																	</lgCon:presentation>
																</xsl:for-each>
																<xsl:for-each select="ns2:definition">
																	<xsl:variable name="var153_status" select="@status"/>
																	<xsl:variable name="var154_propertyType" select="@propertyType"/>
																	<xsl:variable name="var155_propertyId" select="@propertyId"/>
																	<xsl:variable name="var156_isActive" select="@isActive"/>
																	<xsl:variable name="var157_language" select="@language"/>
																	<xsl:variable name="var158_isPreferred" select="@isPreferred"/>
																	<xsl:variable name="var159_effectiveDate" select="@effectiveDate"/>
																	<xsl:variable name="var160_expirationDate" select="@expirationDate"/>
																	<lgCon:definition xsl:exclude-result-prefixes="lgCon">
																		<xsl:if test="string(boolean($var156_isActive)) != 'false'">
																			<xsl:attribute name="isActive" namespace="">
																				<xsl:value-of select="string(((normalize-space(string($var156_isActive)) = 'true') or (normalize-space(string($var156_isActive)) = '1')))"/>
																			</xsl:attribute>
																		</xsl:if>
																		<xsl:if test="string(boolean($var153_status)) != 'false'">
																			<xsl:attribute name="status" namespace="">
																				<xsl:value-of select="string($var153_status)"/>
																			</xsl:attribute>
																		</xsl:if>
																		<xsl:if test="string(boolean($var159_effectiveDate)) != 'false'">
																			<xsl:attribute name="effectiveDate" namespace="">
																				<xsl:value-of select="string($var159_effectiveDate)"/>
																			</xsl:attribute>
																		</xsl:if>
																		<xsl:if test="string(boolean($var160_expirationDate)) != 'false'">
																			<xsl:attribute name="expirationDate" namespace="">
																				<xsl:value-of select="string($var160_expirationDate)"/>
																			</xsl:attribute>
																		</xsl:if>
																		<xsl:attribute name="propertyName" namespace="">
																			<xsl:value-of select="string(@propertyName)"/>
																		</xsl:attribute>
																		<xsl:if test="string(boolean($var155_propertyId)) != 'false'">
																			<xsl:attribute name="propertyId" namespace="">
																				<xsl:value-of select="string($var155_propertyId)"/>
																			</xsl:attribute>
																		</xsl:if>
																		<xsl:if test="string(boolean($var154_propertyType)) != 'false'">
																			<xsl:attribute name="propertyType" namespace="">
																				<xsl:value-of select="string($var154_propertyType)"/>
																			</xsl:attribute>
																		</xsl:if>
																		<xsl:if test="string(boolean($var157_language)) != 'false'">
																			<xsl:attribute name="language" namespace="">
																				<xsl:value-of select="string($var157_language)"/>
																			</xsl:attribute>
																		</xsl:if>
																		<xsl:if test="string(boolean($var158_isPreferred)) != 'false'">
																			<xsl:attribute name="isPreferred" namespace="">
																				<xsl:value-of select="string(((normalize-space(string($var158_isPreferred)) = 'true') or (normalize-space(string($var158_isPreferred)) = '1')))"/>
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
																			<xsl:variable name="var161_propertyQualifierType" select="@propertyQualifierType"/>
																			<lgCommon:propertyQualifier xsl:exclude-result-prefixes="lgCommon">
																				<xsl:attribute name="propertyQualifierName" namespace="">
																					<xsl:value-of select="string(@propertyQualifierName)"/>
																				</xsl:attribute>
																				<xsl:if test="string(boolean($var161_propertyQualifierType)) != 'false'">
																					<xsl:attribute name="propertyQualifierType" namespace="">
																						<xsl:value-of select="string($var161_propertyQualifierType)"/>
																					</xsl:attribute>
																				</xsl:if>
																				<xsl:for-each select="ns1:value">
																					<xsl:variable name="var162_dataType" select="@dataType"/>
																					<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
																						<xsl:if test="string(boolean($var162_dataType)) != 'false'">
																							<xsl:attribute name="dataType" namespace="">
																								<xsl:value-of select="string($var162_dataType)"/>
																							</xsl:attribute>
																						</xsl:if>
																						<xsl:for-each select="node()[boolean(self::text())]">
																							<xsl:value-of select="string(.)"/>
																						</xsl:for-each>
																					</lgCommon:value>
																				</xsl:for-each>
																			</lgCommon:propertyQualifier>
																		</xsl:for-each>
																		<xsl:for-each select="ns1:value">
																			<xsl:variable name="var163_dataType" select="@dataType"/>
																			<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
																				<xsl:if test="string(boolean($var163_dataType)) != 'false'">
																					<xsl:attribute name="dataType" namespace="">
																						<xsl:value-of select="string($var163_dataType)"/>
																					</xsl:attribute>
																				</xsl:if>
																				<xsl:for-each select="node()[boolean(self::text())]">
																					<xsl:value-of select="string(.)"/>
																				</xsl:for-each>
																			</lgCommon:value>
																		</xsl:for-each>
																	</lgCon:definition>
																</xsl:for-each>
																<xsl:for-each select="ns2:comment">
																	<xsl:variable name="var164_propertyId" select="@propertyId"/>
																	<xsl:variable name="var165_expirationDate" select="@expirationDate"/>
																	<xsl:variable name="var166_isActive" select="@isActive"/>
																	<xsl:variable name="var167_status" select="@status"/>
																	<xsl:variable name="var168_propertyType" select="@propertyType"/>
																	<xsl:variable name="var169_language" select="@language"/>
																	<xsl:variable name="var170_effectiveDate" select="@effectiveDate"/>
																	<lgCon:comment xsl:exclude-result-prefixes="lgCon">
																		<xsl:if test="string(boolean($var166_isActive)) != 'false'">
																			<xsl:attribute name="isActive" namespace="">
																				<xsl:value-of select="string(((normalize-space(string($var166_isActive)) = 'true') or (normalize-space(string($var166_isActive)) = '1')))"/>
																			</xsl:attribute>
																		</xsl:if>
																		<xsl:if test="string(boolean($var167_status)) != 'false'">
																			<xsl:attribute name="status" namespace="">
																				<xsl:value-of select="string($var167_status)"/>
																			</xsl:attribute>
																		</xsl:if>
																		<xsl:if test="string(boolean($var170_effectiveDate)) != 'false'">
																			<xsl:attribute name="effectiveDate" namespace="">
																				<xsl:value-of select="string($var170_effectiveDate)"/>
																			</xsl:attribute>
																		</xsl:if>
																		<xsl:if test="string(boolean($var165_expirationDate)) != 'false'">
																			<xsl:attribute name="expirationDate" namespace="">
																				<xsl:value-of select="string($var165_expirationDate)"/>
																			</xsl:attribute>
																		</xsl:if>
																		<xsl:attribute name="propertyName" namespace="">
																			<xsl:value-of select="string(@propertyName)"/>
																		</xsl:attribute>
																		<xsl:if test="string(boolean($var164_propertyId)) != 'false'">
																			<xsl:attribute name="propertyId" namespace="">
																				<xsl:value-of select="string($var164_propertyId)"/>
																			</xsl:attribute>
																		</xsl:if>
																		<xsl:if test="string(boolean($var168_propertyType)) != 'false'">
																			<xsl:attribute name="propertyType" namespace="">
																				<xsl:value-of select="string($var168_propertyType)"/>
																			</xsl:attribute>
																		</xsl:if>
																		<xsl:if test="string(boolean($var169_language)) != 'false'">
																			<xsl:attribute name="language" namespace="">
																				<xsl:value-of select="string($var169_language)"/>
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
																			<xsl:variable name="var171_propertyQualifierType" select="@propertyQualifierType"/>
																			<lgCommon:propertyQualifier xsl:exclude-result-prefixes="lgCommon">
																				<xsl:attribute name="propertyQualifierName" namespace="">
																					<xsl:value-of select="string(@propertyQualifierName)"/>
																				</xsl:attribute>
																				<xsl:if test="string(boolean($var171_propertyQualifierType)) != 'false'">
																					<xsl:attribute name="propertyQualifierType" namespace="">
																						<xsl:value-of select="string($var171_propertyQualifierType)"/>
																					</xsl:attribute>
																				</xsl:if>
																				<xsl:for-each select="ns1:value">
																					<xsl:variable name="var172_dataType" select="@dataType"/>
																					<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
																						<xsl:if test="string(boolean($var172_dataType)) != 'false'">
																							<xsl:attribute name="dataType" namespace="">
																								<xsl:value-of select="string($var172_dataType)"/>
																							</xsl:attribute>
																						</xsl:if>
																						<xsl:for-each select="node()[boolean(self::text())]">
																							<xsl:value-of select="string(.)"/>
																						</xsl:for-each>
																					</lgCommon:value>
																				</xsl:for-each>
																			</lgCommon:propertyQualifier>
																		</xsl:for-each>
																		<xsl:for-each select="ns1:value">
																			<xsl:variable name="var173_dataType" select="@dataType"/>
																			<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
																				<xsl:if test="string(boolean($var173_dataType)) != 'false'">
																					<xsl:attribute name="dataType" namespace="">
																						<xsl:value-of select="string($var173_dataType)"/>
																					</xsl:attribute>
																				</xsl:if>
																				<xsl:for-each select="node()[boolean(self::text())]">
																					<xsl:value-of select="string(.)"/>
																				</xsl:for-each>
																			</lgCommon:value>
																		</xsl:for-each>
																	</lgCon:comment>
																</xsl:for-each>
																<xsl:for-each select="ns2:property">
																	<xsl:variable name="var174_status" select="@status"/>
																	<xsl:variable name="var175_propertyType" select="@propertyType"/>
																	<xsl:variable name="var176_propertyId" select="@propertyId"/>
																	<xsl:variable name="var177_language" select="@language"/>
																	<xsl:variable name="var178_isActive" select="@isActive"/>
																	<xsl:variable name="var179_effectiveDate" select="@effectiveDate"/>
																	<xsl:variable name="var180_expirationDate" select="@expirationDate"/>
																	<lgCon:property xsl:exclude-result-prefixes="lgCon">
																		<xsl:if test="string(boolean($var178_isActive)) != 'false'">
																			<xsl:attribute name="isActive" namespace="">
																				<xsl:value-of select="string(((normalize-space(string($var178_isActive)) = 'true') or (normalize-space(string($var178_isActive)) = '1')))"/>
																			</xsl:attribute>
																		</xsl:if>
																		<xsl:if test="string(boolean($var174_status)) != 'false'">
																			<xsl:attribute name="status" namespace="">
																				<xsl:value-of select="string($var174_status)"/>
																			</xsl:attribute>
																		</xsl:if>
																		<xsl:if test="string(boolean($var179_effectiveDate)) != 'false'">
																			<xsl:attribute name="effectiveDate" namespace="">
																				<xsl:value-of select="string($var179_effectiveDate)"/>
																			</xsl:attribute>
																		</xsl:if>
																		<xsl:if test="string(boolean($var180_expirationDate)) != 'false'">
																			<xsl:attribute name="expirationDate" namespace="">
																				<xsl:value-of select="string($var180_expirationDate)"/>
																			</xsl:attribute>
																		</xsl:if>
																		<xsl:attribute name="propertyName" namespace="">
																			<xsl:value-of select="string(@propertyName)"/>
																		</xsl:attribute>
																		<xsl:if test="string(boolean($var176_propertyId)) != 'false'">
																			<xsl:attribute name="propertyId" namespace="">
																				<xsl:value-of select="string($var176_propertyId)"/>
																			</xsl:attribute>
																		</xsl:if>
																		<xsl:if test="string(boolean($var175_propertyType)) != 'false'">
																			<xsl:attribute name="propertyType" namespace="">
																				<xsl:value-of select="string($var175_propertyType)"/>
																			</xsl:attribute>
																		</xsl:if>
																		<xsl:if test="string(boolean($var177_language)) != 'false'">
																			<xsl:attribute name="language" namespace="">
																				<xsl:value-of select="string($var177_language)"/>
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
																			<xsl:variable name="var181_propertyQualifierType" select="@propertyQualifierType"/>
																			<lgCommon:propertyQualifier xsl:exclude-result-prefixes="lgCommon">
																				<xsl:attribute name="propertyQualifierName" namespace="">
																					<xsl:value-of select="string(@propertyQualifierName)"/>
																				</xsl:attribute>
																				<xsl:if test="string(boolean($var181_propertyQualifierType)) != 'false'">
																					<xsl:attribute name="propertyQualifierType" namespace="">
																						<xsl:value-of select="string($var181_propertyQualifierType)"/>
																					</xsl:attribute>
																				</xsl:if>
																				<xsl:for-each select="ns1:value">
																					<xsl:variable name="var182_dataType" select="@dataType"/>
																					<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
																						<xsl:if test="string(boolean($var182_dataType)) != 'false'">
																							<xsl:attribute name="dataType" namespace="">
																								<xsl:value-of select="string($var182_dataType)"/>
																							</xsl:attribute>
																						</xsl:if>
																						<xsl:for-each select="node()[boolean(self::text())]">
																							<xsl:value-of select="string(.)"/>
																						</xsl:for-each>
																					</lgCommon:value>
																				</xsl:for-each>
																			</lgCommon:propertyQualifier>
																		</xsl:for-each>
																		<xsl:for-each select="ns1:value">
																			<xsl:variable name="var183_dataType" select="@dataType"/>
																			<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
																				<xsl:if test="string(boolean($var183_dataType)) != 'false'">
																					<xsl:attribute name="dataType" namespace="">
																						<xsl:value-of select="string($var183_dataType)"/>
																					</xsl:attribute>
																				</xsl:if>
																				<xsl:for-each select="node()[boolean(self::text())]">
																					<xsl:value-of select="string(.)"/>
																				</xsl:for-each>
																			</lgCommon:value>
																		</xsl:for-each>
																	</lgCon:property>
																</xsl:for-each>
																<xsl:for-each select="ns2:propertyLink">
																	<lgCon:propertyLink xsl:exclude-result-prefixes="lgCon">
																		<xsl:call-template name="tbf:tbf22_propertyLink">
																			<xsl:with-param name="input" select="."/>
																		</xsl:call-template>
																	</lgCon:propertyLink>
																</xsl:for-each>
															</lgCon:entity>
														</xsl:for-each>
													</lgCS:entities>
												</xsl:for-each>
												<xsl:for-each select="ns0:relations">
													<xsl:variable name="var184_containerName" select="@containerName"/>
													<lgCS:relations xsl:exclude-result-prefixes="lgCS">
														<xsl:if test="string(boolean($var184_containerName)) != 'false'">
															<xsl:attribute name="containerName" namespace="">
																<xsl:value-of select="string($var184_containerName)"/>
															</xsl:attribute>
														</xsl:if>
														<xsl:for-each select="ns1:entityDescription">
															<lgCommon:entityDescription xsl:exclude-result-prefixes="lgCommon">
																<xsl:for-each select="node()[boolean(self::text())]">
																	<xsl:value-of select="string(.)"/>
																</xsl:for-each>
															</lgCommon:entityDescription>
														</xsl:for-each>
													</lgCS:relations>
												</xsl:for-each>
											</changedCodingSchemeEntry>
										</xsl:for-each>
										<xsl:for-each select="ns5:changedPickListDefinitionEntry">
											<xsl:variable name="var185_status" select="@status"/>
											<xsl:variable name="var186_defaultSortOrder" select="@defaultSortOrder"/>
											<xsl:variable name="var187_isActive" select="@isActive"/>
											<xsl:variable name="var188_expirationDate" select="@expirationDate"/>
											<xsl:variable name="var189_defaultLanguage" select="@defaultLanguage"/>
											<xsl:variable name="var190_effectiveDate" select="@effectiveDate"/>
											<xsl:variable name="var191_defaultEntityCodeNamespace" select="@defaultEntityCodeNamespace"/>
											<changedPickListDefinitionEntry>
												<xsl:if test="string(boolean($var187_isActive)) != 'false'">
													<xsl:attribute name="isActive" namespace="">
														<xsl:value-of select="string(((normalize-space(string($var187_isActive)) = 'true') or (normalize-space(string($var187_isActive)) = '1')))"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:if test="string(boolean($var185_status)) != 'false'">
													<xsl:attribute name="status" namespace="">
														<xsl:value-of select="string($var185_status)"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:if test="string(boolean($var190_effectiveDate)) != 'false'">
													<xsl:attribute name="effectiveDate" namespace="">
														<xsl:value-of select="string($var190_effectiveDate)"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:if test="string(boolean($var188_expirationDate)) != 'false'">
													<xsl:attribute name="expirationDate" namespace="">
														<xsl:value-of select="string($var188_expirationDate)"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:attribute name="pickListId" namespace="">
													<xsl:value-of select="string(@pickListId)"/>
												</xsl:attribute>
												<xsl:if test="string(boolean($var191_defaultEntityCodeNamespace)) != 'false'">
													<xsl:attribute name="defaultEntityCodeNamespace" namespace="">
														<xsl:value-of select="string($var191_defaultEntityCodeNamespace)"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:if test="string(boolean($var189_defaultLanguage)) != 'false'">
													<xsl:attribute name="defaultLanguage" namespace="">
														<xsl:value-of select="string($var189_defaultLanguage)"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:if test="string(boolean($var186_defaultSortOrder)) != 'false'">
													<xsl:attribute name="defaultSortOrder" namespace="">
														<xsl:value-of select="string($var186_defaultSortOrder)"/>
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
												<xsl:for-each select="ns4:mappings">
													<lgVD:mappings xsl:exclude-result-prefixes="lgVD">
														<xsl:for-each select="ns3:supportedAssociation">
															<xsl:variable name="var192_uri" select="@uri"/>
															<lgNaming:supportedAssociation xsl:exclude-result-prefixes="lgNaming">
																<xsl:attribute name="localId" namespace="">
																	<xsl:value-of select="string(@localId)"/>
																</xsl:attribute>
																<xsl:if test="string(boolean($var192_uri)) != 'false'">
																	<xsl:attribute name="uri" namespace="">
																		<xsl:value-of select="string($var192_uri)"/>
																	</xsl:attribute>
																</xsl:if>
																<xsl:value-of select="string(.)"/>
															</lgNaming:supportedAssociation>
														</xsl:for-each>
														<xsl:for-each select="ns3:supportedAssociationQualifier">
															<lgNaming:supportedAssociationQualifier xsl:exclude-result-prefixes="lgNaming">
																<xsl:call-template name="tbf:tbf3_supportedAssociationQualifier">
																	<xsl:with-param name="input" select="."/>
																</xsl:call-template>
															</lgNaming:supportedAssociationQualifier>
														</xsl:for-each>
														<xsl:for-each select="ns3:supportedCodingScheme">
															<lgNaming:supportedCodingScheme xsl:exclude-result-prefixes="lgNaming">
																<xsl:call-template name="tbf:tbf4_supportedCodingScheme">
																	<xsl:with-param name="input" select="."/>
																</xsl:call-template>
															</lgNaming:supportedCodingScheme>
														</xsl:for-each>
														<xsl:for-each select="ns3:supportedContext">
															<lgNaming:supportedContext xsl:exclude-result-prefixes="lgNaming">
																<xsl:call-template name="tbf:tbf6_supportedContext">
																	<xsl:with-param name="input" select="."/>
																</xsl:call-template>
															</lgNaming:supportedContext>
														</xsl:for-each>
														<xsl:for-each select="ns3:supportedDataType">
															<lgNaming:supportedDataType xsl:exclude-result-prefixes="lgNaming">
																<xsl:call-template name="tbf:tbf7_supportedDataType">
																	<xsl:with-param name="input" select="."/>
																</xsl:call-template>
															</lgNaming:supportedDataType>
														</xsl:for-each>
														<xsl:for-each select="ns3:supportedDegreeOfFidelity">
															<lgNaming:supportedDegreeOfFidelity xsl:exclude-result-prefixes="lgNaming">
																<xsl:call-template name="tbf:tbf8_supportedDegreeOfFidelity">
																	<xsl:with-param name="input" select="."/>
																</xsl:call-template>
															</lgNaming:supportedDegreeOfFidelity>
														</xsl:for-each>
														<xsl:for-each select="ns3:supportedEntityType">
															<lgNaming:supportedEntityType xsl:exclude-result-prefixes="lgNaming">
																<xsl:call-template name="tbf:tbf9_supportedEntityType">
																	<xsl:with-param name="input" select="."/>
																</xsl:call-template>
															</lgNaming:supportedEntityType>
														</xsl:for-each>
														<xsl:for-each select="ns3:supportedHierarchy">
															<lgNaming:supportedHierarchy xsl:exclude-result-prefixes="lgNaming">
																<xsl:call-template name="tbf:tbf10_supportedHierarchy">
																	<xsl:with-param name="input" select="."/>
																</xsl:call-template>
															</lgNaming:supportedHierarchy>
														</xsl:for-each>
														<xsl:for-each select="ns3:supportedLanguage">
															<lgNaming:supportedLanguage xsl:exclude-result-prefixes="lgNaming">
																<xsl:call-template name="tbf:tbf11_supportedLanguage">
																	<xsl:with-param name="input" select="."/>
																</xsl:call-template>
															</lgNaming:supportedLanguage>
														</xsl:for-each>
														<xsl:for-each select="ns3:supportedNamespace">
															<lgNaming:supportedNamespace xsl:exclude-result-prefixes="lgNaming">
																<xsl:call-template name="tbf:tbf12_supportedNamespace">
																	<xsl:with-param name="input" select="."/>
																</xsl:call-template>
															</lgNaming:supportedNamespace>
														</xsl:for-each>
														<xsl:for-each select="ns3:supportedProperty">
															<xsl:variable name="var193_uri" select="@uri"/>
															<lgNaming:supportedProperty xsl:exclude-result-prefixes="lgNaming">
																<xsl:attribute name="localId" namespace="">
																	<xsl:value-of select="string(@localId)"/>
																</xsl:attribute>
																<xsl:if test="string(boolean($var193_uri)) != 'false'">
																	<xsl:attribute name="uri" namespace="">
																		<xsl:value-of select="string($var193_uri)"/>
																	</xsl:attribute>
																</xsl:if>
																<xsl:value-of select="string(.)"/>
															</lgNaming:supportedProperty>
														</xsl:for-each>
														<xsl:for-each select="ns3:supportedPropertyType">
															<lgNaming:supportedPropertyType xsl:exclude-result-prefixes="lgNaming">
																<xsl:call-template name="tbf:tbf13_supportedPropertyType">
																	<xsl:with-param name="input" select="."/>
																</xsl:call-template>
															</lgNaming:supportedPropertyType>
														</xsl:for-each>
														<xsl:for-each select="ns3:supportedPropertyLink">
															<lgNaming:supportedPropertyLink xsl:exclude-result-prefixes="lgNaming">
																<xsl:call-template name="tbf:tbf14_supportedPropertyLink">
																	<xsl:with-param name="input" select="."/>
																</xsl:call-template>
															</lgNaming:supportedPropertyLink>
														</xsl:for-each>
														<xsl:for-each select="ns3:supportedPropertyQualifier">
															<lgNaming:supportedPropertyQualifier xsl:exclude-result-prefixes="lgNaming">
																<xsl:call-template name="tbf:tbf15_supportedPropertyQualifier">
																	<xsl:with-param name="input" select="."/>
																</xsl:call-template>
															</lgNaming:supportedPropertyQualifier>
														</xsl:for-each>
														<xsl:for-each select="ns3:supportedPropertyQualifierType">
															<lgNaming:supportedPropertyQualifierType xsl:exclude-result-prefixes="lgNaming">
																<xsl:call-template name="tbf:tbf16_supportedPropertyQualifierType">
																	<xsl:with-param name="input" select="."/>
																</xsl:call-template>
															</lgNaming:supportedPropertyQualifierType>
														</xsl:for-each>
														<xsl:for-each select="ns3:supportedRepresentationalForm">
															<lgNaming:supportedRepresentationalForm xsl:exclude-result-prefixes="lgNaming">
																<xsl:call-template name="tbf:tbf17_supportedRepresentationalForm">
																	<xsl:with-param name="input" select="."/>
																</xsl:call-template>
															</lgNaming:supportedRepresentationalForm>
														</xsl:for-each>
														<xsl:for-each select="ns3:supportedSortOrder">
															<lgNaming:supportedSortOrder xsl:exclude-result-prefixes="lgNaming">
																<xsl:call-template name="tbf:tbf18_supportedSortOrder">
																	<xsl:with-param name="input" select="."/>
																</xsl:call-template>
															</lgNaming:supportedSortOrder>
														</xsl:for-each>
														<xsl:for-each select="ns3:supportedSource">
															<lgNaming:supportedSource xsl:exclude-result-prefixes="lgNaming">
																<xsl:call-template name="tbf:tbf19_supportedSource">
																	<xsl:with-param name="input" select="."/>
																</xsl:call-template>
															</lgNaming:supportedSource>
														</xsl:for-each>
														<xsl:for-each select="ns3:supportedSourceRole">
															<lgNaming:supportedSourceRole xsl:exclude-result-prefixes="lgNaming">
																<xsl:call-template name="tbf:tbf20_supportedSourceRole">
																	<xsl:with-param name="input" select="."/>
																</xsl:call-template>
															</lgNaming:supportedSourceRole>
														</xsl:for-each>
														<xsl:for-each select="ns3:supportedStatus">
															<lgNaming:supportedStatus xsl:exclude-result-prefixes="lgNaming">
																<xsl:call-template name="tbf:tbf21_supportedStatus">
																	<xsl:with-param name="input" select="."/>
																</xsl:call-template>
															</lgNaming:supportedStatus>
														</xsl:for-each>
													</lgVD:mappings>
												</xsl:for-each>
												<xsl:for-each select="ns4:pickListEntryNode">
													<xsl:variable name="var194_effectiveDate" select="@effectiveDate"/>
													<xsl:variable name="var195_expirationDate" select="@expirationDate"/>
													<xsl:variable name="var196_status" select="@status"/>
													<xsl:variable name="var197_isActive" select="@isActive"/>
													<lgVD:pickListEntryNode xsl:exclude-result-prefixes="lgVD">
														<xsl:if test="string(boolean($var197_isActive)) != 'false'">
															<xsl:attribute name="isActive" namespace="">
																<xsl:value-of select="string(((normalize-space(string($var197_isActive)) = 'true') or (normalize-space(string($var197_isActive)) = '1')))"/>
															</xsl:attribute>
														</xsl:if>
														<xsl:if test="string(boolean($var196_status)) != 'false'">
															<xsl:attribute name="status" namespace="">
																<xsl:value-of select="string($var196_status)"/>
															</xsl:attribute>
														</xsl:if>
														<xsl:if test="string(boolean($var194_effectiveDate)) != 'false'">
															<xsl:attribute name="effectiveDate" namespace="">
																<xsl:value-of select="string($var194_effectiveDate)"/>
															</xsl:attribute>
														</xsl:if>
														<xsl:if test="string(boolean($var195_expirationDate)) != 'false'">
															<xsl:attribute name="expirationDate" namespace="">
																<xsl:value-of select="string($var195_expirationDate)"/>
															</xsl:attribute>
														</xsl:if>
														<xsl:attribute name="pickListEntryId" namespace="">
															<xsl:value-of select="string(@pickListEntryId)"/>
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
														<xsl:for-each select="ns4:exclusionEntry">
															<lgVD:exclusionEntry xsl:exclude-result-prefixes="lgVD">
																<xsl:call-template name="tbf:tbf26_pickListEntryExclusion">
																	<xsl:with-param name="input" select="."/>
																</xsl:call-template>
															</lgVD:exclusionEntry>
														</xsl:for-each>
														<xsl:for-each select="ns4:inclusionEntry">
															<xsl:variable name="var198_propertyId" select="@propertyId"/>
															<xsl:variable name="var199_entryOrder" select="@entryOrder"/>
															<xsl:variable name="var200_isDefault" select="@isDefault"/>
															<xsl:variable name="var201_matchIfNoContext" select="@matchIfNoContext"/>
															<xsl:variable name="var202_entityCodeNamespace" select="@entityCodeNamespace"/>
															<xsl:variable name="var203_language" select="@language"/>
															<lgVD:inclusionEntry xsl:exclude-result-prefixes="lgVD">
																<xsl:if test="string(boolean($var199_entryOrder)) != 'false'">
																	<xsl:attribute name="entryOrder" namespace="">
																		<xsl:value-of select="string(number(string($var199_entryOrder)))"/>
																	</xsl:attribute>
																</xsl:if>
																<xsl:attribute name="entityCode" namespace="">
																	<xsl:value-of select="string(@entityCode)"/>
																</xsl:attribute>
																<xsl:if test="string(boolean($var202_entityCodeNamespace)) != 'false'">
																	<xsl:attribute name="entityCodeNamespace" namespace="">
																		<xsl:value-of select="string($var202_entityCodeNamespace)"/>
																	</xsl:attribute>
																</xsl:if>
																<xsl:if test="string(boolean($var198_propertyId)) != 'false'">
																	<xsl:attribute name="propertyId" namespace="">
																		<xsl:value-of select="string($var198_propertyId)"/>
																	</xsl:attribute>
																</xsl:if>
																<xsl:if test="string(boolean($var200_isDefault)) != 'false'">
																	<xsl:attribute name="isDefault" namespace="">
																		<xsl:value-of select="string(((normalize-space(string($var200_isDefault)) = 'true') or (normalize-space(string($var200_isDefault)) = '1')))"/>
																	</xsl:attribute>
																</xsl:if>
																<xsl:if test="string(boolean($var201_matchIfNoContext)) != 'false'">
																	<xsl:attribute name="matchIfNoContext" namespace="">
																		<xsl:value-of select="string(((normalize-space(string($var201_matchIfNoContext)) = 'true') or (normalize-space(string($var201_matchIfNoContext)) = '1')))"/>
																	</xsl:attribute>
																</xsl:if>
																<xsl:if test="string(boolean($var203_language)) != 'false'">
																	<xsl:attribute name="language" namespace="">
																		<xsl:value-of select="string($var203_language)"/>
																	</xsl:attribute>
																</xsl:if>
																<lgVD:pickText xsl:exclude-result-prefixes="lgVD">
																	<xsl:value-of select="string(ns4:pickText)"/>
																</lgVD:pickText>
																<xsl:for-each select="ns4:pickContext">
																	<lgVD:pickContext xsl:exclude-result-prefixes="lgVD">
																		<xsl:value-of select="string(.)"/>
																	</lgVD:pickContext>
																</xsl:for-each>
															</lgVD:inclusionEntry>
														</xsl:for-each>
														<xsl:for-each select="ns4:properties">
															<lgVD:properties xsl:exclude-result-prefixes="lgVD">
																<xsl:for-each select="ns1:property">
																	<xsl:variable name="var204_status" select="@status"/>
																	<xsl:variable name="var205_isActive" select="@isActive"/>
																	<xsl:variable name="var206_propertyType" select="@propertyType"/>
																	<xsl:variable name="var207_propertyId" select="@propertyId"/>
																	<xsl:variable name="var208_effectiveDate" select="@effectiveDate"/>
																	<xsl:variable name="var209_expirationDate" select="@expirationDate"/>
																	<xsl:variable name="var210_language" select="@language"/>
																	<lgCommon:property xsl:exclude-result-prefixes="lgCommon">
																		<xsl:if test="string(boolean($var205_isActive)) != 'false'">
																			<xsl:attribute name="isActive" namespace="">
																				<xsl:value-of select="string(((normalize-space(string($var205_isActive)) = 'true') or (normalize-space(string($var205_isActive)) = '1')))"/>
																			</xsl:attribute>
																		</xsl:if>
																		<xsl:if test="string(boolean($var204_status)) != 'false'">
																			<xsl:attribute name="status" namespace="">
																				<xsl:value-of select="string($var204_status)"/>
																			</xsl:attribute>
																		</xsl:if>
																		<xsl:if test="string(boolean($var208_effectiveDate)) != 'false'">
																			<xsl:attribute name="effectiveDate" namespace="">
																				<xsl:value-of select="string($var208_effectiveDate)"/>
																			</xsl:attribute>
																		</xsl:if>
																		<xsl:if test="string(boolean($var209_expirationDate)) != 'false'">
																			<xsl:attribute name="expirationDate" namespace="">
																				<xsl:value-of select="string($var209_expirationDate)"/>
																			</xsl:attribute>
																		</xsl:if>
																		<xsl:attribute name="propertyName" namespace="">
																			<xsl:value-of select="string(@propertyName)"/>
																		</xsl:attribute>
																		<xsl:if test="string(boolean($var207_propertyId)) != 'false'">
																			<xsl:attribute name="propertyId" namespace="">
																				<xsl:value-of select="string($var207_propertyId)"/>
																			</xsl:attribute>
																		</xsl:if>
																		<xsl:if test="string(boolean($var206_propertyType)) != 'false'">
																			<xsl:attribute name="propertyType" namespace="">
																				<xsl:value-of select="string($var206_propertyType)"/>
																			</xsl:attribute>
																		</xsl:if>
																		<xsl:if test="string(boolean($var210_language)) != 'false'">
																			<xsl:attribute name="language" namespace="">
																				<xsl:value-of select="string($var210_language)"/>
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
																			<xsl:variable name="var211_propertyQualifierType" select="@propertyQualifierType"/>
																			<lgCommon:propertyQualifier xsl:exclude-result-prefixes="lgCommon">
																				<xsl:attribute name="propertyQualifierName" namespace="">
																					<xsl:value-of select="string(@propertyQualifierName)"/>
																				</xsl:attribute>
																				<xsl:if test="string(boolean($var211_propertyQualifierType)) != 'false'">
																					<xsl:attribute name="propertyQualifierType" namespace="">
																						<xsl:value-of select="string($var211_propertyQualifierType)"/>
																					</xsl:attribute>
																				</xsl:if>
																				<xsl:for-each select="ns1:value">
																					<xsl:variable name="var212_dataType" select="@dataType"/>
																					<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
																						<xsl:if test="string(boolean($var212_dataType)) != 'false'">
																							<xsl:attribute name="dataType" namespace="">
																								<xsl:value-of select="string($var212_dataType)"/>
																							</xsl:attribute>
																						</xsl:if>
																						<xsl:for-each select="node()[boolean(self::text())]">
																							<xsl:value-of select="string(.)"/>
																						</xsl:for-each>
																					</lgCommon:value>
																				</xsl:for-each>
																			</lgCommon:propertyQualifier>
																		</xsl:for-each>
																		<xsl:for-each select="ns1:value">
																			<xsl:variable name="var213_dataType" select="@dataType"/>
																			<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
																				<xsl:if test="string(boolean($var213_dataType)) != 'false'">
																					<xsl:attribute name="dataType" namespace="">
																						<xsl:value-of select="string($var213_dataType)"/>
																					</xsl:attribute>
																				</xsl:if>
																				<xsl:for-each select="node()[boolean(self::text())]">
																					<xsl:value-of select="string(.)"/>
																				</xsl:for-each>
																			</lgCommon:value>
																		</xsl:for-each>
																	</lgCommon:property>
																</xsl:for-each>
															</lgVD:properties>
														</xsl:for-each>
													</lgVD:pickListEntryNode>
												</xsl:for-each>
												<xsl:for-each select="ns4:source">
													<lgVD:source xsl:exclude-result-prefixes="lgVD">
														<xsl:call-template name="tbf:tbf2_source">
															<xsl:with-param name="input" select="."/>
														</xsl:call-template>
													</lgVD:source>
												</xsl:for-each>
												<xsl:for-each select="ns4:defaultPickContext">
													<lgVD:defaultPickContext xsl:exclude-result-prefixes="lgVD">
														<xsl:value-of select="string(.)"/>
													</lgVD:defaultPickContext>
												</xsl:for-each>
												<xsl:for-each select="ns4:properties">
													<lgVD:properties xsl:exclude-result-prefixes="lgVD">
														<xsl:for-each select="ns1:property">
															<xsl:variable name="var214_isActive" select="@isActive"/>
															<xsl:variable name="var215_effectiveDate" select="@effectiveDate"/>
															<xsl:variable name="var216_expirationDate" select="@expirationDate"/>
															<xsl:variable name="var217_language" select="@language"/>
															<xsl:variable name="var218_status" select="@status"/>
															<xsl:variable name="var219_propertyType" select="@propertyType"/>
															<xsl:variable name="var220_propertyId" select="@propertyId"/>
															<lgCommon:property xsl:exclude-result-prefixes="lgCommon">
																<xsl:if test="string(boolean($var214_isActive)) != 'false'">
																	<xsl:attribute name="isActive" namespace="">
																		<xsl:value-of select="string(((normalize-space(string($var214_isActive)) = 'true') or (normalize-space(string($var214_isActive)) = '1')))"/>
																	</xsl:attribute>
																</xsl:if>
																<xsl:if test="string(boolean($var218_status)) != 'false'">
																	<xsl:attribute name="status" namespace="">
																		<xsl:value-of select="string($var218_status)"/>
																	</xsl:attribute>
																</xsl:if>
																<xsl:if test="string(boolean($var215_effectiveDate)) != 'false'">
																	<xsl:attribute name="effectiveDate" namespace="">
																		<xsl:value-of select="string($var215_effectiveDate)"/>
																	</xsl:attribute>
																</xsl:if>
																<xsl:if test="string(boolean($var216_expirationDate)) != 'false'">
																	<xsl:attribute name="expirationDate" namespace="">
																		<xsl:value-of select="string($var216_expirationDate)"/>
																	</xsl:attribute>
																</xsl:if>
																<xsl:attribute name="propertyName" namespace="">
																	<xsl:value-of select="string(@propertyName)"/>
																</xsl:attribute>
																<xsl:if test="string(boolean($var220_propertyId)) != 'false'">
																	<xsl:attribute name="propertyId" namespace="">
																		<xsl:value-of select="string($var220_propertyId)"/>
																	</xsl:attribute>
																</xsl:if>
																<xsl:if test="string(boolean($var219_propertyType)) != 'false'">
																	<xsl:attribute name="propertyType" namespace="">
																		<xsl:value-of select="string($var219_propertyType)"/>
																	</xsl:attribute>
																</xsl:if>
																<xsl:if test="string(boolean($var217_language)) != 'false'">
																	<xsl:attribute name="language" namespace="">
																		<xsl:value-of select="string($var217_language)"/>
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
																	<xsl:variable name="var221_propertyQualifierType" select="@propertyQualifierType"/>
																	<lgCommon:propertyQualifier xsl:exclude-result-prefixes="lgCommon">
																		<xsl:attribute name="propertyQualifierName" namespace="">
																			<xsl:value-of select="string(@propertyQualifierName)"/>
																		</xsl:attribute>
																		<xsl:if test="string(boolean($var221_propertyQualifierType)) != 'false'">
																			<xsl:attribute name="propertyQualifierType" namespace="">
																				<xsl:value-of select="string($var221_propertyQualifierType)"/>
																			</xsl:attribute>
																		</xsl:if>
																		<xsl:for-each select="ns1:value">
																			<xsl:variable name="var222_dataType" select="@dataType"/>
																			<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
																				<xsl:if test="string(boolean($var222_dataType)) != 'false'">
																					<xsl:attribute name="dataType" namespace="">
																						<xsl:value-of select="string($var222_dataType)"/>
																					</xsl:attribute>
																				</xsl:if>
																				<xsl:for-each select="node()[boolean(self::text())]">
																					<xsl:value-of select="string(.)"/>
																				</xsl:for-each>
																			</lgCommon:value>
																		</xsl:for-each>
																	</lgCommon:propertyQualifier>
																</xsl:for-each>
																<xsl:for-each select="ns1:value">
																	<xsl:variable name="var223_dataType" select="@dataType"/>
																	<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
																		<xsl:if test="string(boolean($var223_dataType)) != 'false'">
																			<xsl:attribute name="dataType" namespace="">
																				<xsl:value-of select="string($var223_dataType)"/>
																			</xsl:attribute>
																		</xsl:if>
																		<xsl:for-each select="node()[boolean(self::text())]">
																			<xsl:value-of select="string(.)"/>
																		</xsl:for-each>
																	</lgCommon:value>
																</xsl:for-each>
															</lgCommon:property>
														</xsl:for-each>
													</lgVD:properties>
												</xsl:for-each>
											</changedPickListDefinitionEntry>
										</xsl:for-each>
									</changedEntry>
								</xsl:for-each>
							</revision>
						</xsl:for-each>
					</editHistory>
				</xsl:for-each>
			</xsl:for-each>
		</systemRelease>
	</xsl:template>
</xsl:stylesheet>
