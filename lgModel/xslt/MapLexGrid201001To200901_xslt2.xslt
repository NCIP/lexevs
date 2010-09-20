<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ns0="http://LexGrid.org/schema/2010/01/LexGrid/codingSchemes" xmlns:ns1="http://LexGrid.org/schema/2010/01/LexGrid/commonTypes" xmlns:ns2="http://LexGrid.org/schema/2010/01/LexGrid/concepts" xmlns:ns3="http://LexGrid.org/schema/2010/01/LexGrid/naming" xmlns:ns4="http://LexGrid.org/schema/2010/01/LexGrid/valueSets" xmlns:ns5="http://LexGrid.org/schema/2010/01/LexGrid/versions" xmlns:tbf="http://www.altova.com/MapForce/UDF/tbf" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions" exclude-result-prefixes="ns0 ns1 ns2 ns3 ns4 ns5 tbf xs fn">
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
	<xsl:template name="tbf:tbf3_supportedAssociationQualifier">
		<xsl:param name="input" select="()"/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:sequence select="fn:string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf4_supportedCodingScheme">
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
	<xsl:template name="tbf:tbf5_supportedContainerName">
		<xsl:param name="input" select="()"/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:sequence select="fn:string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf6_supportedContext">
		<xsl:param name="input" select="()"/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:sequence select="fn:string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf7_supportedDataType">
		<xsl:param name="input" select="()"/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:sequence select="fn:string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf8_supportedDegreeOfFidelity">
		<xsl:param name="input" select="()"/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:sequence select="fn:string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf9_supportedEntityType">
		<xsl:param name="input" select="()"/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:sequence select="fn:string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf10_supportedHierarchy">
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
	<xsl:template name="tbf:tbf11_supportedLanguage">
		<xsl:param name="input" select="()"/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:sequence select="fn:string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf12_supportedNamespace">
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
	<xsl:template name="tbf:tbf13_supportedPropertyType">
		<xsl:param name="input" select="()"/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:sequence select="fn:string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf14_supportedPropertyLink">
		<xsl:param name="input" select="()"/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:sequence select="fn:string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf15_supportedPropertyQualifier">
		<xsl:param name="input" select="()"/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:sequence select="fn:string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf16_supportedPropertyQualifierType">
		<xsl:param name="input" select="()"/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:sequence select="fn:string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf17_supportedRepresentationalForm">
		<xsl:param name="input" select="()"/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:sequence select="fn:string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf18_supportedSortOrder">
		<xsl:param name="input" select="()"/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:sequence select="fn:string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf19_supportedSource">
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
	<xsl:template name="tbf:tbf20_supportedSourceRole">
		<xsl:param name="input" select="()"/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:sequence select="fn:string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf21_supportedStatus">
		<xsl:param name="input" select="()"/>
		<xsl:for-each select="$input/@localId">
			<xsl:attribute name="localId" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@uri">
			<xsl:attribute name="uri" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:sequence select="fn:string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf22_propertyLink">
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
	<xsl:template name="tbf:tbf23_text">
		<xsl:param name="input" select="()"/>
		<xsl:for-each select="$input/@dataType">
			<xsl:attribute name="dataType" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:sequence select="fn:string($input)"/>
	</xsl:template>
	<xsl:template name="tbf:tbf24_entityReference">
		<xsl:param name="input" select="()"/>
		<xsl:for-each select="$input/@entityCode">
			<xsl:attribute name="entityCode" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@entityCodeNamespace">
			<xsl:attribute name="entityCodeNamespace" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@referenceAssociation">
			<xsl:attribute name="referenceAssociation" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@transitiveClosure">
			<xsl:attribute name="transitiveClosure" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@leafOnly">
			<xsl:attribute name="leafOnly" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@targetToSource">
			<xsl:attribute name="targetToSource" select="fn:string(.)"/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="tbf:tbf25_codingSchemeReference">
		<xsl:param name="input" select="()"/>
		<xsl:for-each select="$input/@codingScheme">
			<xsl:attribute name="codingScheme" select="fn:string(.)"/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="tbf:tbf26_pickListEntryExclusion">
		<xsl:param name="input" select="()"/>
		<xsl:for-each select="$input/@entityCode">
			<xsl:attribute name="entityCode" select="fn:string(.)"/>
		</xsl:for-each>
		<xsl:for-each select="$input/@entityCodeNamespace">
			<xsl:attribute name="entityCodeNamespace" select="fn:string(.)"/>
		</xsl:for-each>
	</xsl:template>
	<xsl:output method="xml" encoding="UTF-8" indent="yes"/>
	<xsl:template match="/">
		<systemRelease xmlns="http://LexGrid.org/schema/2009/01/LexGrid/versions" xmlns:lgBuiltin="http://LexGrid.org/schema/2009/01/LexGrid/builtins" xmlns:lgCS="http://LexGrid.org/schema/2009/01/LexGrid/codingSchemes" xmlns:lgCommon="http://LexGrid.org/schema/2009/01/LexGrid/commonTypes" xmlns:lgCon="http://LexGrid.org/schema/2009/01/LexGrid/concepts" xmlns:lgNaming="http://LexGrid.org/schema/2009/01/LexGrid/naming" xmlns:lgRel="http://LexGrid.org/schema/2009/01/LexGrid/relations" xmlns:lgVD="http://LexGrid.org/schema/2009/01/LexGrid/valueDomains">
			<xsl:attribute name="xsi:schemaLocation" namespace="http://www.w3.org/2001/XMLSchema-instance" select="'http://LexGrid.org/schema/2009/01/LexGrid/versions http://LexGrid.org/schema/2010/01/LexGrid/codingSchemes.xsd'"/>
			<xsl:for-each select="ns5:systemRelease">
				<xsl:variable name="var1_releaseId" as="item()*" select="@releaseId"/>
				<xsl:variable name="var2_basedOnRelease" as="item()*" select="@basedOnRelease"/>
				<xsl:variable name="var3_releaseAgency" as="item()*" select="@releaseAgency"/>
				<xsl:if test="fn:exists($var1_releaseId)">
					<xsl:attribute name="releaseId" namespace="" select="fn:string($var1_releaseId)"/>
				</xsl:if>
				<xsl:attribute name="releaseURI" namespace="" select="xs:string(xs:anyURI(fn:string(@releaseURI)))"/>
				<xsl:attribute name="releaseDate" namespace="" select="xs:string(xs:dateTime(fn:string(@releaseDate)))"/>
				<xsl:if test="fn:exists($var3_releaseAgency)">
					<xsl:attribute name="releaseAgency" namespace="" select="xs:string(xs:anyURI(fn:string($var3_releaseAgency)))"/>
				</xsl:if>
				<xsl:if test="fn:exists($var2_basedOnRelease)">
					<xsl:attribute name="basedOnRelease" namespace="" select="xs:string(xs:anyURI(fn:string($var2_basedOnRelease)))"/>
				</xsl:if>
				<xsl:for-each select="ns1:entityDescription">
					<lgCommon:entityDescription xsl:exclude-result-prefixes="lgCommon">
						<xsl:for-each select="node()[fn:boolean(self::text())]">
							<xsl:sequence select="fn:string(.)"/>
						</xsl:for-each>
					</lgCommon:entityDescription>
				</xsl:for-each>
				<xsl:for-each select="ns5:codingSchemes">
					<codingSchemes>
						<xsl:for-each select="ns0:codingScheme">
							<xsl:variable name="var4_approxNumConcepts" as="item()*" select="@approxNumConcepts"/>
							<xsl:variable name="var5_effectiveDate" as="item()*" select="@effectiveDate"/>
							<xsl:variable name="var6_defaultLanguage" as="item()*" select="@defaultLanguage"/>
							<xsl:variable name="var7_isActive" as="item()*" select="@isActive"/>
							<xsl:variable name="var8_formalName" as="item()*" select="@formalName"/>
							<xsl:variable name="var9_status" as="item()*" select="@status"/>
							<xsl:variable name="var10_expirationDate" as="item()*" select="@expirationDate"/>
							<xsl:variable name="var11_mappings" as="node()" select="ns0:mappings"/>
							<lgCS:codingScheme xsl:exclude-result-prefixes="lgCS">
								<xsl:if test="fn:exists($var7_isActive)">
									<xsl:attribute name="isActive" namespace="" select="xs:string(xs:boolean(fn:string($var7_isActive)))"/>
								</xsl:if>
								<xsl:if test="fn:exists($var9_status)">
									<xsl:attribute name="status" namespace="" select="fn:string($var9_status)"/>
								</xsl:if>
								<xsl:if test="fn:exists($var5_effectiveDate)">
									<xsl:attribute name="effectiveDate" namespace="" select="xs:string(xs:dateTime(fn:string($var5_effectiveDate)))"/>
								</xsl:if>
								<xsl:if test="fn:exists($var10_expirationDate)">
									<xsl:attribute name="expirationDate" namespace="" select="xs:string(xs:dateTime(fn:string($var10_expirationDate)))"/>
								</xsl:if>
								<xsl:attribute name="codingSchemeName" namespace="" select="fn:string(@codingSchemeName)"/>
								<xsl:attribute name="codingSchemeURI" namespace="" select="xs:string(xs:anyURI(fn:string(@codingSchemeURI)))"/>
								<xsl:if test="fn:exists($var8_formalName)">
									<xsl:attribute name="formalName" namespace="" select="fn:string($var8_formalName)"/>
								</xsl:if>
								<xsl:if test="fn:exists($var6_defaultLanguage)">
									<xsl:attribute name="defaultLanguage" namespace="" select="fn:string($var6_defaultLanguage)"/>
								</xsl:if>
								<xsl:if test="fn:exists($var4_approxNumConcepts)">
									<xsl:attribute name="approxNumConcepts" namespace="" select="xs:string(xs:integer(fn:string($var4_approxNumConcepts)))"/>
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
									<lgCS:localName xsl:exclude-result-prefixes="lgCS">
										<xsl:sequence select="fn:string(.)"/>
									</lgCS:localName>
								</xsl:for-each>
								<xsl:for-each select="ns0:source">
									<lgCS:source xsl:exclude-result-prefixes="lgCS">
										<xsl:call-template name="tbf:tbf2_source">
											<xsl:with-param name="input" select="." as="node()"/>
										</xsl:call-template>
									</lgCS:source>
								</xsl:for-each>
								<xsl:for-each select="ns0:copyright">
									<xsl:variable name="var12_dataType" as="item()*" select="@dataType"/>
									<lgCS:copyright xsl:exclude-result-prefixes="lgCS">
										<xsl:if test="fn:exists($var12_dataType)">
											<xsl:attribute name="dataType" namespace="" select="fn:string($var12_dataType)"/>
										</xsl:if>
										<xsl:for-each select="node()[fn:boolean(self::text())]">
											<xsl:sequence select="fn:string(.)"/>
										</xsl:for-each>
									</lgCS:copyright>
								</xsl:for-each>
								<lgCS:mappings xsl:exclude-result-prefixes="lgCS">
									<xsl:for-each select="$var11_mappings/ns3:supportedAssociation">
										<xsl:variable name="var13_uri" as="item()*" select="@uri"/>
										<lgNaming:supportedAssociation xsl:exclude-result-prefixes="lgNaming">
											<xsl:attribute name="localId" namespace="" select="fn:string(@localId)"/>
											<xsl:if test="fn:exists($var13_uri)">
												<xsl:attribute name="uri" namespace="" select="xs:string(xs:anyURI(fn:string($var13_uri)))"/>
											</xsl:if>
											<xsl:sequence select="fn:string(.)"/>
										</lgNaming:supportedAssociation>
									</xsl:for-each>
									<xsl:for-each select="$var11_mappings/ns3:supportedAssociationQualifier">
										<lgNaming:supportedAssociationQualifier xsl:exclude-result-prefixes="lgNaming">
											<xsl:call-template name="tbf:tbf3_supportedAssociationQualifier">
												<xsl:with-param name="input" select="." as="node()"/>
											</xsl:call-template>
										</lgNaming:supportedAssociationQualifier>
									</xsl:for-each>
									<xsl:for-each select="$var11_mappings/ns3:supportedCodingScheme">
										<lgNaming:supportedCodingScheme xsl:exclude-result-prefixes="lgNaming">
											<xsl:call-template name="tbf:tbf4_supportedCodingScheme">
												<xsl:with-param name="input" select="." as="node()"/>
											</xsl:call-template>
										</lgNaming:supportedCodingScheme>
									</xsl:for-each>
									<xsl:for-each select="$var11_mappings/ns3:supportedContainerName">
										<lgNaming:supportedContainer xsl:exclude-result-prefixes="lgNaming">
											<xsl:call-template name="tbf:tbf5_supportedContainerName">
												<xsl:with-param name="input" select="." as="node()"/>
											</xsl:call-template>
										</lgNaming:supportedContainer>
									</xsl:for-each>
									<xsl:for-each select="$var11_mappings/ns3:supportedContext">
										<lgNaming:supportedContext xsl:exclude-result-prefixes="lgNaming">
											<xsl:call-template name="tbf:tbf6_supportedContext">
												<xsl:with-param name="input" select="." as="node()"/>
											</xsl:call-template>
										</lgNaming:supportedContext>
									</xsl:for-each>
									<xsl:for-each select="$var11_mappings/ns3:supportedDataType">
										<lgNaming:supportedDataType xsl:exclude-result-prefixes="lgNaming">
											<xsl:call-template name="tbf:tbf7_supportedDataType">
												<xsl:with-param name="input" select="." as="node()"/>
											</xsl:call-template>
										</lgNaming:supportedDataType>
									</xsl:for-each>
									<xsl:for-each select="$var11_mappings/ns3:supportedDegreeOfFidelity">
										<lgNaming:supportedDegreeOfFidelity xsl:exclude-result-prefixes="lgNaming">
											<xsl:call-template name="tbf:tbf8_supportedDegreeOfFidelity">
												<xsl:with-param name="input" select="." as="node()"/>
											</xsl:call-template>
										</lgNaming:supportedDegreeOfFidelity>
									</xsl:for-each>
									<xsl:for-each select="$var11_mappings/ns3:supportedEntityType">
										<lgNaming:supportedEntityType xsl:exclude-result-prefixes="lgNaming">
											<xsl:call-template name="tbf:tbf9_supportedEntityType">
												<xsl:with-param name="input" select="." as="node()"/>
											</xsl:call-template>
										</lgNaming:supportedEntityType>
									</xsl:for-each>
									<xsl:for-each select="$var11_mappings/ns3:supportedHierarchy">
										<lgNaming:supportedHierarchy xsl:exclude-result-prefixes="lgNaming">
											<xsl:call-template name="tbf:tbf10_supportedHierarchy">
												<xsl:with-param name="input" select="." as="node()"/>
											</xsl:call-template>
										</lgNaming:supportedHierarchy>
									</xsl:for-each>
									<xsl:for-each select="$var11_mappings/ns3:supportedLanguage">
										<lgNaming:supportedLanguage xsl:exclude-result-prefixes="lgNaming">
											<xsl:call-template name="tbf:tbf11_supportedLanguage">
												<xsl:with-param name="input" select="." as="node()"/>
											</xsl:call-template>
										</lgNaming:supportedLanguage>
									</xsl:for-each>
									<xsl:for-each select="$var11_mappings/ns3:supportedNamespace">
										<lgNaming:supportedNamespace xsl:exclude-result-prefixes="lgNaming">
											<xsl:call-template name="tbf:tbf12_supportedNamespace">
												<xsl:with-param name="input" select="." as="node()"/>
											</xsl:call-template>
										</lgNaming:supportedNamespace>
									</xsl:for-each>
									<xsl:for-each select="$var11_mappings/ns3:supportedProperty">
										<xsl:variable name="var14_uri" as="item()*" select="@uri"/>
										<lgNaming:supportedProperty xsl:exclude-result-prefixes="lgNaming">
											<xsl:attribute name="localId" namespace="" select="fn:string(@localId)"/>
											<xsl:if test="fn:exists($var14_uri)">
												<xsl:attribute name="uri" namespace="" select="xs:string(xs:anyURI(fn:string($var14_uri)))"/>
											</xsl:if>
											<xsl:sequence select="fn:string(.)"/>
										</lgNaming:supportedProperty>
									</xsl:for-each>
									<xsl:for-each select="$var11_mappings/ns3:supportedPropertyType">
										<lgNaming:supportedPropertyType xsl:exclude-result-prefixes="lgNaming">
											<xsl:call-template name="tbf:tbf13_supportedPropertyType">
												<xsl:with-param name="input" select="." as="node()"/>
											</xsl:call-template>
										</lgNaming:supportedPropertyType>
									</xsl:for-each>
									<xsl:for-each select="$var11_mappings/ns3:supportedPropertyLink">
										<lgNaming:supportedPropertyLink xsl:exclude-result-prefixes="lgNaming">
											<xsl:call-template name="tbf:tbf14_supportedPropertyLink">
												<xsl:with-param name="input" select="." as="node()"/>
											</xsl:call-template>
										</lgNaming:supportedPropertyLink>
									</xsl:for-each>
									<xsl:for-each select="$var11_mappings/ns3:supportedPropertyQualifier">
										<lgNaming:supportedPropertyQualifier xsl:exclude-result-prefixes="lgNaming">
											<xsl:call-template name="tbf:tbf15_supportedPropertyQualifier">
												<xsl:with-param name="input" select="." as="node()"/>
											</xsl:call-template>
										</lgNaming:supportedPropertyQualifier>
									</xsl:for-each>
									<xsl:for-each select="$var11_mappings/ns3:supportedPropertyQualifierType">
										<lgNaming:supportedPropertyQualifierType xsl:exclude-result-prefixes="lgNaming">
											<xsl:call-template name="tbf:tbf16_supportedPropertyQualifierType">
												<xsl:with-param name="input" select="." as="node()"/>
											</xsl:call-template>
										</lgNaming:supportedPropertyQualifierType>
									</xsl:for-each>
									<xsl:for-each select="$var11_mappings/ns3:supportedRepresentationalForm">
										<lgNaming:supportedRepresentationalForm xsl:exclude-result-prefixes="lgNaming">
											<xsl:call-template name="tbf:tbf17_supportedRepresentationalForm">
												<xsl:with-param name="input" select="." as="node()"/>
											</xsl:call-template>
										</lgNaming:supportedRepresentationalForm>
									</xsl:for-each>
									<xsl:for-each select="$var11_mappings/ns3:supportedSortOrder">
										<lgNaming:supportedSortOrder xsl:exclude-result-prefixes="lgNaming">
											<xsl:call-template name="tbf:tbf18_supportedSortOrder">
												<xsl:with-param name="input" select="." as="node()"/>
											</xsl:call-template>
										</lgNaming:supportedSortOrder>
									</xsl:for-each>
									<xsl:for-each select="$var11_mappings/ns3:supportedSource">
										<lgNaming:supportedSource xsl:exclude-result-prefixes="lgNaming">
											<xsl:call-template name="tbf:tbf19_supportedSource">
												<xsl:with-param name="input" select="." as="node()"/>
											</xsl:call-template>
										</lgNaming:supportedSource>
									</xsl:for-each>
									<xsl:for-each select="$var11_mappings/ns3:supportedSourceRole">
										<lgNaming:supportedSourceRole xsl:exclude-result-prefixes="lgNaming">
											<xsl:call-template name="tbf:tbf20_supportedSourceRole">
												<xsl:with-param name="input" select="." as="node()"/>
											</xsl:call-template>
										</lgNaming:supportedSourceRole>
									</xsl:for-each>
									<xsl:for-each select="$var11_mappings/ns3:supportedStatus">
										<lgNaming:supportedStatus xsl:exclude-result-prefixes="lgNaming">
											<xsl:call-template name="tbf:tbf21_supportedStatus">
												<xsl:with-param name="input" select="." as="node()"/>
											</xsl:call-template>
										</lgNaming:supportedStatus>
									</xsl:for-each>
								</lgCS:mappings>
								<xsl:for-each select="ns0:properties">
									<lgCS:properties xsl:exclude-result-prefixes="lgCS">
										<xsl:for-each select="ns1:property">
											<xsl:variable name="var15_expirationDate" as="item()*" select="@expirationDate"/>
											<xsl:variable name="var16_propertyId" as="item()*" select="@propertyId"/>
											<xsl:variable name="var17_propertyType" as="item()*" select="@propertyType"/>
											<xsl:variable name="var18_language" as="item()*" select="@language"/>
											<xsl:variable name="var19_status" as="item()*" select="@status"/>
											<xsl:variable name="var20_effectiveDate" as="item()*" select="@effectiveDate"/>
											<xsl:variable name="var21_isActive" as="item()*" select="@isActive"/>
											<lgCommon:property xsl:exclude-result-prefixes="lgCommon">
												<xsl:if test="fn:exists($var21_isActive)">
													<xsl:attribute name="isActive" namespace="" select="xs:string(xs:boolean(fn:string($var21_isActive)))"/>
												</xsl:if>
												<xsl:if test="fn:exists($var19_status)">
													<xsl:attribute name="status" namespace="" select="fn:string($var19_status)"/>
												</xsl:if>
												<xsl:if test="fn:exists($var20_effectiveDate)">
													<xsl:attribute name="effectiveDate" namespace="" select="xs:string(xs:dateTime(fn:string($var20_effectiveDate)))"/>
												</xsl:if>
												<xsl:if test="fn:exists($var15_expirationDate)">
													<xsl:attribute name="expirationDate" namespace="" select="xs:string(xs:dateTime(fn:string($var15_expirationDate)))"/>
												</xsl:if>
												<xsl:attribute name="propertyName" namespace="" select="fn:string(@propertyName)"/>
												<xsl:if test="fn:exists($var16_propertyId)">
													<xsl:attribute name="propertyId" namespace="" select="fn:string($var16_propertyId)"/>
												</xsl:if>
												<xsl:if test="fn:exists($var17_propertyType)">
													<xsl:attribute name="propertyType" namespace="" select="fn:string($var17_propertyType)"/>
												</xsl:if>
												<xsl:if test="fn:exists($var18_language)">
													<xsl:attribute name="language" namespace="" select="fn:string($var18_language)"/>
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
													<xsl:variable name="var22_propertyQualifierType" as="item()*" select="@propertyQualifierType"/>
													<lgCommon:propertyQualifier xsl:exclude-result-prefixes="lgCommon">
														<xsl:attribute name="propertyQualifierName" namespace="" select="fn:string(@propertyQualifierName)"/>
														<xsl:if test="fn:exists($var22_propertyQualifierType)">
															<xsl:attribute name="propertyQualifierType" namespace="" select="fn:string($var22_propertyQualifierType)"/>
														</xsl:if>
														<xsl:for-each select="ns1:value">
															<xsl:variable name="var23_dataType" as="item()*" select="@dataType"/>
															<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
																<xsl:if test="fn:exists($var23_dataType)">
																	<xsl:attribute name="dataType" namespace="" select="fn:string($var23_dataType)"/>
																</xsl:if>
																<xsl:for-each select="node()[fn:boolean(self::text())]">
																	<xsl:sequence select="fn:string(.)"/>
																</xsl:for-each>
															</lgCommon:value>
														</xsl:for-each>
													</lgCommon:propertyQualifier>
												</xsl:for-each>
												<xsl:for-each select="ns1:value">
													<xsl:variable name="var24_dataType" as="item()*" select="@dataType"/>
													<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
														<xsl:if test="fn:exists($var24_dataType)">
															<xsl:attribute name="dataType" namespace="" select="fn:string($var24_dataType)"/>
														</xsl:if>
														<xsl:for-each select="node()[fn:boolean(self::text())]">
															<xsl:sequence select="fn:string(.)"/>
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
											<xsl:variable name="var25_expirationDate" as="item()*" select="@expirationDate"/>
											<xsl:variable name="var26_isActive" as="item()*" select="@isActive"/>
											<xsl:variable name="var27_status" as="item()*" select="@status"/>
											<xsl:variable name="var28_effectiveDate" as="item()*" select="@effectiveDate"/>
											<xsl:variable name="var29_isAnonymous" as="item()*" select="@isAnonymous"/>
											<xsl:variable name="var30_isDefined" as="item()*" select="@isDefined"/>
											<xsl:variable name="var31_entityCodeNamespace" as="item()*" select="@entityCodeNamespace"/>
											<lgCon:entity xsl:exclude-result-prefixes="lgCon">
												<xsl:if test="fn:exists($var26_isActive)">
													<xsl:attribute name="isActive" namespace="" select="xs:string(xs:boolean(fn:string($var26_isActive)))"/>
												</xsl:if>
												<xsl:if test="fn:exists($var27_status)">
													<xsl:attribute name="status" namespace="" select="fn:string($var27_status)"/>
												</xsl:if>
												<xsl:if test="fn:exists($var28_effectiveDate)">
													<xsl:attribute name="effectiveDate" namespace="" select="xs:string(xs:dateTime(fn:string($var28_effectiveDate)))"/>
												</xsl:if>
												<xsl:if test="fn:exists($var25_expirationDate)">
													<xsl:attribute name="expirationDate" namespace="" select="xs:string(xs:dateTime(fn:string($var25_expirationDate)))"/>
												</xsl:if>
												<xsl:attribute name="entityCode" namespace="" select="fn:string(@entityCode)"/>
												<xsl:if test="fn:exists($var31_entityCodeNamespace)">
													<xsl:attribute name="entityCodeNamespace" namespace="" select="fn:string($var31_entityCodeNamespace)"/>
												</xsl:if>
												<xsl:if test="fn:exists($var29_isAnonymous)">
													<xsl:attribute name="isAnonymous" namespace="" select="xs:string(xs:boolean(fn:string($var29_isAnonymous)))"/>
												</xsl:if>
												<xsl:if test="fn:exists($var30_isDefined)">
													<xsl:attribute name="isDefined" namespace="" select="xs:string(xs:boolean(fn:string($var30_isDefined)))"/>
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
													<xsl:variable name="var32_status" as="item()*" select="@status"/>
													<xsl:variable name="var33_matchIfNoContext" as="item()*" select="@matchIfNoContext"/>
													<xsl:variable name="var34_effectiveDate" as="item()*" select="@effectiveDate"/>
													<xsl:variable name="var35_expirationDate" as="item()*" select="@expirationDate"/>
													<xsl:variable name="var36_isPreferred" as="item()*" select="@isPreferred"/>
													<xsl:variable name="var37_degreeOfFidelity" as="item()*" select="@degreeOfFidelity"/>
													<xsl:variable name="var38_isActive" as="item()*" select="@isActive"/>
													<xsl:variable name="var39_representationalForm" as="item()*" select="@representationalForm"/>
													<xsl:variable name="var40_propertyId" as="item()*" select="@propertyId"/>
													<xsl:variable name="var41_language" as="item()*" select="@language"/>
													<xsl:variable name="var42_propertyType" as="item()*" select="@propertyType"/>
													<lgCon:presentation xsl:exclude-result-prefixes="lgCon">
														<xsl:if test="fn:exists($var38_isActive)">
															<xsl:attribute name="isActive" namespace="" select="xs:string(xs:boolean(fn:string($var38_isActive)))"/>
														</xsl:if>
														<xsl:if test="fn:exists($var32_status)">
															<xsl:attribute name="status" namespace="" select="fn:string($var32_status)"/>
														</xsl:if>
														<xsl:if test="fn:exists($var34_effectiveDate)">
															<xsl:attribute name="effectiveDate" namespace="" select="xs:string(xs:dateTime(fn:string($var34_effectiveDate)))"/>
														</xsl:if>
														<xsl:if test="fn:exists($var35_expirationDate)">
															<xsl:attribute name="expirationDate" namespace="" select="xs:string(xs:dateTime(fn:string($var35_expirationDate)))"/>
														</xsl:if>
														<xsl:attribute name="propertyName" namespace="" select="fn:string(@propertyName)"/>
														<xsl:if test="fn:exists($var40_propertyId)">
															<xsl:attribute name="propertyId" namespace="" select="fn:string($var40_propertyId)"/>
														</xsl:if>
														<xsl:if test="fn:exists($var42_propertyType)">
															<xsl:attribute name="propertyType" namespace="" select="fn:string($var42_propertyType)"/>
														</xsl:if>
														<xsl:if test="fn:exists($var41_language)">
															<xsl:attribute name="language" namespace="" select="fn:string($var41_language)"/>
														</xsl:if>
														<xsl:if test="fn:exists($var36_isPreferred)">
															<xsl:attribute name="isPreferred" namespace="" select="xs:string(xs:boolean(fn:string($var36_isPreferred)))"/>
														</xsl:if>
														<xsl:if test="fn:exists($var37_degreeOfFidelity)">
															<xsl:attribute name="degreeOfFidelity" namespace="" select="fn:string($var37_degreeOfFidelity)"/>
														</xsl:if>
														<xsl:if test="fn:exists($var33_matchIfNoContext)">
															<xsl:attribute name="matchIfNoContext" namespace="" select="xs:string(xs:boolean(fn:string($var33_matchIfNoContext)))"/>
														</xsl:if>
														<xsl:if test="fn:exists($var39_representationalForm)">
															<xsl:attribute name="representationalForm" namespace="" select="fn:string($var39_representationalForm)"/>
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
															<xsl:variable name="var43_propertyQualifierType" as="item()*" select="@propertyQualifierType"/>
															<lgCommon:propertyQualifier xsl:exclude-result-prefixes="lgCommon">
																<xsl:attribute name="propertyQualifierName" namespace="" select="fn:string(@propertyQualifierName)"/>
																<xsl:if test="fn:exists($var43_propertyQualifierType)">
																	<xsl:attribute name="propertyQualifierType" namespace="" select="fn:string($var43_propertyQualifierType)"/>
																</xsl:if>
																<xsl:for-each select="ns1:value">
																	<xsl:variable name="var44_dataType" as="item()*" select="@dataType"/>
																	<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
																		<xsl:if test="fn:exists($var44_dataType)">
																			<xsl:attribute name="dataType" namespace="" select="fn:string($var44_dataType)"/>
																		</xsl:if>
																		<xsl:for-each select="node()[fn:boolean(self::text())]">
																			<xsl:sequence select="fn:string(.)"/>
																		</xsl:for-each>
																	</lgCommon:value>
																</xsl:for-each>
															</lgCommon:propertyQualifier>
														</xsl:for-each>
														<xsl:for-each select="ns1:value">
															<xsl:variable name="var45_dataType" as="item()*" select="@dataType"/>
															<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
																<xsl:if test="fn:exists($var45_dataType)">
																	<xsl:attribute name="dataType" namespace="" select="fn:string($var45_dataType)"/>
																</xsl:if>
																<xsl:for-each select="node()[fn:boolean(self::text())]">
																	<xsl:sequence select="fn:string(.)"/>
																</xsl:for-each>
															</lgCommon:value>
														</xsl:for-each>
													</lgCon:presentation>
												</xsl:for-each>
												<xsl:for-each select="ns2:definition">
													<xsl:variable name="var46_language" as="item()*" select="@language"/>
													<xsl:variable name="var47_propertyId" as="item()*" select="@propertyId"/>
													<xsl:variable name="var48_status" as="item()*" select="@status"/>
													<xsl:variable name="var49_isPreferred" as="item()*" select="@isPreferred"/>
													<xsl:variable name="var50_expirationDate" as="item()*" select="@expirationDate"/>
													<xsl:variable name="var51_propertyType" as="item()*" select="@propertyType"/>
													<xsl:variable name="var52_effectiveDate" as="item()*" select="@effectiveDate"/>
													<xsl:variable name="var53_isActive" as="item()*" select="@isActive"/>
													<lgCon:definition xsl:exclude-result-prefixes="lgCon">
														<xsl:if test="fn:exists($var53_isActive)">
															<xsl:attribute name="isActive" namespace="" select="xs:string(xs:boolean(fn:string($var53_isActive)))"/>
														</xsl:if>
														<xsl:if test="fn:exists($var48_status)">
															<xsl:attribute name="status" namespace="" select="fn:string($var48_status)"/>
														</xsl:if>
														<xsl:if test="fn:exists($var52_effectiveDate)">
															<xsl:attribute name="effectiveDate" namespace="" select="xs:string(xs:dateTime(fn:string($var52_effectiveDate)))"/>
														</xsl:if>
														<xsl:if test="fn:exists($var50_expirationDate)">
															<xsl:attribute name="expirationDate" namespace="" select="xs:string(xs:dateTime(fn:string($var50_expirationDate)))"/>
														</xsl:if>
														<xsl:attribute name="propertyName" namespace="" select="fn:string(@propertyName)"/>
														<xsl:if test="fn:exists($var47_propertyId)">
															<xsl:attribute name="propertyId" namespace="" select="fn:string($var47_propertyId)"/>
														</xsl:if>
														<xsl:if test="fn:exists($var51_propertyType)">
															<xsl:attribute name="propertyType" namespace="" select="fn:string($var51_propertyType)"/>
														</xsl:if>
														<xsl:if test="fn:exists($var46_language)">
															<xsl:attribute name="language" namespace="" select="fn:string($var46_language)"/>
														</xsl:if>
														<xsl:if test="fn:exists($var49_isPreferred)">
															<xsl:attribute name="isPreferred" namespace="" select="xs:string(xs:boolean(fn:string($var49_isPreferred)))"/>
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
															<xsl:variable name="var54_propertyQualifierType" as="item()*" select="@propertyQualifierType"/>
															<lgCommon:propertyQualifier xsl:exclude-result-prefixes="lgCommon">
																<xsl:attribute name="propertyQualifierName" namespace="" select="fn:string(@propertyQualifierName)"/>
																<xsl:if test="fn:exists($var54_propertyQualifierType)">
																	<xsl:attribute name="propertyQualifierType" namespace="" select="fn:string($var54_propertyQualifierType)"/>
																</xsl:if>
																<xsl:for-each select="ns1:value">
																	<xsl:variable name="var55_dataType" as="item()*" select="@dataType"/>
																	<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
																		<xsl:if test="fn:exists($var55_dataType)">
																			<xsl:attribute name="dataType" namespace="" select="fn:string($var55_dataType)"/>
																		</xsl:if>
																		<xsl:for-each select="node()[fn:boolean(self::text())]">
																			<xsl:sequence select="fn:string(.)"/>
																		</xsl:for-each>
																	</lgCommon:value>
																</xsl:for-each>
															</lgCommon:propertyQualifier>
														</xsl:for-each>
														<xsl:for-each select="ns1:value">
															<xsl:variable name="var56_dataType" as="item()*" select="@dataType"/>
															<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
																<xsl:if test="fn:exists($var56_dataType)">
																	<xsl:attribute name="dataType" namespace="" select="fn:string($var56_dataType)"/>
																</xsl:if>
																<xsl:for-each select="node()[fn:boolean(self::text())]">
																	<xsl:sequence select="fn:string(.)"/>
																</xsl:for-each>
															</lgCommon:value>
														</xsl:for-each>
													</lgCon:definition>
												</xsl:for-each>
												<xsl:for-each select="ns2:comment">
													<xsl:variable name="var57_propertyType" as="item()*" select="@propertyType"/>
													<xsl:variable name="var58_propertyId" as="item()*" select="@propertyId"/>
													<xsl:variable name="var59_effectiveDate" as="item()*" select="@effectiveDate"/>
													<xsl:variable name="var60_language" as="item()*" select="@language"/>
													<xsl:variable name="var61_isActive" as="item()*" select="@isActive"/>
													<xsl:variable name="var62_status" as="item()*" select="@status"/>
													<xsl:variable name="var63_expirationDate" as="item()*" select="@expirationDate"/>
													<lgCon:comment xsl:exclude-result-prefixes="lgCon">
														<xsl:if test="fn:exists($var61_isActive)">
															<xsl:attribute name="isActive" namespace="" select="xs:string(xs:boolean(fn:string($var61_isActive)))"/>
														</xsl:if>
														<xsl:if test="fn:exists($var62_status)">
															<xsl:attribute name="status" namespace="" select="fn:string($var62_status)"/>
														</xsl:if>
														<xsl:if test="fn:exists($var59_effectiveDate)">
															<xsl:attribute name="effectiveDate" namespace="" select="xs:string(xs:dateTime(fn:string($var59_effectiveDate)))"/>
														</xsl:if>
														<xsl:if test="fn:exists($var63_expirationDate)">
															<xsl:attribute name="expirationDate" namespace="" select="xs:string(xs:dateTime(fn:string($var63_expirationDate)))"/>
														</xsl:if>
														<xsl:attribute name="propertyName" namespace="" select="fn:string(@propertyName)"/>
														<xsl:if test="fn:exists($var58_propertyId)">
															<xsl:attribute name="propertyId" namespace="" select="fn:string($var58_propertyId)"/>
														</xsl:if>
														<xsl:if test="fn:exists($var57_propertyType)">
															<xsl:attribute name="propertyType" namespace="" select="fn:string($var57_propertyType)"/>
														</xsl:if>
														<xsl:if test="fn:exists($var60_language)">
															<xsl:attribute name="language" namespace="" select="fn:string($var60_language)"/>
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
															<xsl:variable name="var64_propertyQualifierType" as="item()*" select="@propertyQualifierType"/>
															<lgCommon:propertyQualifier xsl:exclude-result-prefixes="lgCommon">
																<xsl:attribute name="propertyQualifierName" namespace="" select="fn:string(@propertyQualifierName)"/>
																<xsl:if test="fn:exists($var64_propertyQualifierType)">
																	<xsl:attribute name="propertyQualifierType" namespace="" select="fn:string($var64_propertyQualifierType)"/>
																</xsl:if>
																<xsl:for-each select="ns1:value">
																	<xsl:variable name="var65_dataType" as="item()*" select="@dataType"/>
																	<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
																		<xsl:if test="fn:exists($var65_dataType)">
																			<xsl:attribute name="dataType" namespace="" select="fn:string($var65_dataType)"/>
																		</xsl:if>
																		<xsl:for-each select="node()[fn:boolean(self::text())]">
																			<xsl:sequence select="fn:string(.)"/>
																		</xsl:for-each>
																	</lgCommon:value>
																</xsl:for-each>
															</lgCommon:propertyQualifier>
														</xsl:for-each>
														<xsl:for-each select="ns1:value">
															<xsl:variable name="var66_dataType" as="item()*" select="@dataType"/>
															<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
																<xsl:if test="fn:exists($var66_dataType)">
																	<xsl:attribute name="dataType" namespace="" select="fn:string($var66_dataType)"/>
																</xsl:if>
																<xsl:for-each select="node()[fn:boolean(self::text())]">
																	<xsl:sequence select="fn:string(.)"/>
																</xsl:for-each>
															</lgCommon:value>
														</xsl:for-each>
													</lgCon:comment>
												</xsl:for-each>
												<xsl:for-each select="ns2:property">
													<xsl:variable name="var67_propertyType" as="item()*" select="@propertyType"/>
													<xsl:variable name="var68_expirationDate" as="item()*" select="@expirationDate"/>
													<xsl:variable name="var69_status" as="item()*" select="@status"/>
													<xsl:variable name="var70_isActive" as="item()*" select="@isActive"/>
													<xsl:variable name="var71_propertyId" as="item()*" select="@propertyId"/>
													<xsl:variable name="var72_effectiveDate" as="item()*" select="@effectiveDate"/>
													<xsl:variable name="var73_language" as="item()*" select="@language"/>
													<lgCon:property xsl:exclude-result-prefixes="lgCon">
														<xsl:if test="fn:exists($var70_isActive)">
															<xsl:attribute name="isActive" namespace="" select="xs:string(xs:boolean(fn:string($var70_isActive)))"/>
														</xsl:if>
														<xsl:if test="fn:exists($var69_status)">
															<xsl:attribute name="status" namespace="" select="fn:string($var69_status)"/>
														</xsl:if>
														<xsl:if test="fn:exists($var72_effectiveDate)">
															<xsl:attribute name="effectiveDate" namespace="" select="xs:string(xs:dateTime(fn:string($var72_effectiveDate)))"/>
														</xsl:if>
														<xsl:if test="fn:exists($var68_expirationDate)">
															<xsl:attribute name="expirationDate" namespace="" select="xs:string(xs:dateTime(fn:string($var68_expirationDate)))"/>
														</xsl:if>
														<xsl:attribute name="propertyName" namespace="" select="fn:string(@propertyName)"/>
														<xsl:if test="fn:exists($var71_propertyId)">
															<xsl:attribute name="propertyId" namespace="" select="fn:string($var71_propertyId)"/>
														</xsl:if>
														<xsl:if test="fn:exists($var67_propertyType)">
															<xsl:attribute name="propertyType" namespace="" select="fn:string($var67_propertyType)"/>
														</xsl:if>
														<xsl:if test="fn:exists($var73_language)">
															<xsl:attribute name="language" namespace="" select="fn:string($var73_language)"/>
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
															<xsl:variable name="var74_propertyQualifierType" as="item()*" select="@propertyQualifierType"/>
															<lgCommon:propertyQualifier xsl:exclude-result-prefixes="lgCommon">
																<xsl:attribute name="propertyQualifierName" namespace="" select="fn:string(@propertyQualifierName)"/>
																<xsl:if test="fn:exists($var74_propertyQualifierType)">
																	<xsl:attribute name="propertyQualifierType" namespace="" select="fn:string($var74_propertyQualifierType)"/>
																</xsl:if>
																<xsl:for-each select="ns1:value">
																	<xsl:variable name="var75_dataType" as="item()*" select="@dataType"/>
																	<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
																		<xsl:if test="fn:exists($var75_dataType)">
																			<xsl:attribute name="dataType" namespace="" select="fn:string($var75_dataType)"/>
																		</xsl:if>
																		<xsl:for-each select="node()[fn:boolean(self::text())]">
																			<xsl:sequence select="fn:string(.)"/>
																		</xsl:for-each>
																	</lgCommon:value>
																</xsl:for-each>
															</lgCommon:propertyQualifier>
														</xsl:for-each>
														<xsl:for-each select="ns1:value">
															<xsl:variable name="var76_dataType" as="item()*" select="@dataType"/>
															<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
																<xsl:if test="fn:exists($var76_dataType)">
																	<xsl:attribute name="dataType" namespace="" select="fn:string($var76_dataType)"/>
																</xsl:if>
																<xsl:for-each select="node()[fn:boolean(self::text())]">
																	<xsl:sequence select="fn:string(.)"/>
																</xsl:for-each>
															</lgCommon:value>
														</xsl:for-each>
													</lgCon:property>
												</xsl:for-each>
												<xsl:for-each select="ns2:propertyLink">
													<lgCon:propertyLink xsl:exclude-result-prefixes="lgCon">
														<xsl:call-template name="tbf:tbf22_propertyLink">
															<xsl:with-param name="input" select="." as="node()"/>
														</xsl:call-template>
													</lgCon:propertyLink>
												</xsl:for-each>
											</lgCon:entity>
										</xsl:for-each>
									</lgCS:entities>
								</xsl:for-each>
								<xsl:for-each select="ns0:relations">
									<xsl:variable name="var77_containerName" as="item()*" select="@containerName"/>
									<lgCS:relations xsl:exclude-result-prefixes="lgCS">
										<xsl:if test="fn:exists($var77_containerName)">
											<xsl:attribute name="containerName" namespace="" select="fn:string($var77_containerName)"/>
										</xsl:if>
										<xsl:for-each select="ns1:entityDescription">
											<lgCommon:entityDescription xsl:exclude-result-prefixes="lgCommon">
												<xsl:for-each select="node()[fn:boolean(self::text())]">
													<xsl:sequence select="fn:string(.)"/>
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
					<xsl:variable name="var78_mappings" as="node()" select="ns4:mappings"/>
					<valueDomains>
						<lgVD:mappings xsl:exclude-result-prefixes="lgVD">
							<xsl:for-each select="$var78_mappings/ns3:supportedAssociation">
								<xsl:variable name="var79_uri" as="item()*" select="@uri"/>
								<lgNaming:supportedAssociation xsl:exclude-result-prefixes="lgNaming">
									<xsl:attribute name="localId" namespace="" select="fn:string(@localId)"/>
									<xsl:if test="fn:exists($var79_uri)">
										<xsl:attribute name="uri" namespace="" select="xs:string(xs:anyURI(fn:string($var79_uri)))"/>
									</xsl:if>
									<xsl:sequence select="fn:string(.)"/>
								</lgNaming:supportedAssociation>
							</xsl:for-each>
							<xsl:for-each select="$var78_mappings/ns3:supportedAssociationQualifier">
								<lgNaming:supportedAssociationQualifier xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf3_supportedAssociationQualifier">
										<xsl:with-param name="input" select="." as="node()"/>
									</xsl:call-template>
								</lgNaming:supportedAssociationQualifier>
							</xsl:for-each>
							<xsl:for-each select="$var78_mappings/ns3:supportedCodingScheme">
								<lgNaming:supportedCodingScheme xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf4_supportedCodingScheme">
										<xsl:with-param name="input" select="." as="node()"/>
									</xsl:call-template>
								</lgNaming:supportedCodingScheme>
							</xsl:for-each>
							<xsl:for-each select="$var78_mappings/ns3:supportedContainerName">
								<lgNaming:supportedContainer xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf5_supportedContainerName">
										<xsl:with-param name="input" select="." as="node()"/>
									</xsl:call-template>
								</lgNaming:supportedContainer>
							</xsl:for-each>
							<xsl:for-each select="$var78_mappings/ns3:supportedContext">
								<lgNaming:supportedContext xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf6_supportedContext">
										<xsl:with-param name="input" select="." as="node()"/>
									</xsl:call-template>
								</lgNaming:supportedContext>
							</xsl:for-each>
							<xsl:for-each select="$var78_mappings/ns3:supportedDataType">
								<lgNaming:supportedDataType xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf7_supportedDataType">
										<xsl:with-param name="input" select="." as="node()"/>
									</xsl:call-template>
								</lgNaming:supportedDataType>
							</xsl:for-each>
							<xsl:for-each select="$var78_mappings/ns3:supportedDegreeOfFidelity">
								<lgNaming:supportedDegreeOfFidelity xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf8_supportedDegreeOfFidelity">
										<xsl:with-param name="input" select="." as="node()"/>
									</xsl:call-template>
								</lgNaming:supportedDegreeOfFidelity>
							</xsl:for-each>
							<xsl:for-each select="$var78_mappings/ns3:supportedEntityType">
								<lgNaming:supportedEntityType xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf9_supportedEntityType">
										<xsl:with-param name="input" select="." as="node()"/>
									</xsl:call-template>
								</lgNaming:supportedEntityType>
							</xsl:for-each>
							<xsl:for-each select="$var78_mappings/ns3:supportedHierarchy">
								<lgNaming:supportedHierarchy xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf10_supportedHierarchy">
										<xsl:with-param name="input" select="." as="node()"/>
									</xsl:call-template>
								</lgNaming:supportedHierarchy>
							</xsl:for-each>
							<xsl:for-each select="$var78_mappings/ns3:supportedLanguage">
								<lgNaming:supportedLanguage xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf11_supportedLanguage">
										<xsl:with-param name="input" select="." as="node()"/>
									</xsl:call-template>
								</lgNaming:supportedLanguage>
							</xsl:for-each>
							<xsl:for-each select="$var78_mappings/ns3:supportedNamespace">
								<lgNaming:supportedNamespace xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf12_supportedNamespace">
										<xsl:with-param name="input" select="." as="node()"/>
									</xsl:call-template>
								</lgNaming:supportedNamespace>
							</xsl:for-each>
							<xsl:for-each select="$var78_mappings/ns3:supportedProperty">
								<xsl:variable name="var80_uri" as="item()*" select="@uri"/>
								<lgNaming:supportedProperty xsl:exclude-result-prefixes="lgNaming">
									<xsl:attribute name="localId" namespace="" select="fn:string(@localId)"/>
									<xsl:if test="fn:exists($var80_uri)">
										<xsl:attribute name="uri" namespace="" select="xs:string(xs:anyURI(fn:string($var80_uri)))"/>
									</xsl:if>
									<xsl:sequence select="fn:string(.)"/>
								</lgNaming:supportedProperty>
							</xsl:for-each>
							<xsl:for-each select="$var78_mappings/ns3:supportedPropertyType">
								<lgNaming:supportedPropertyType xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf13_supportedPropertyType">
										<xsl:with-param name="input" select="." as="node()"/>
									</xsl:call-template>
								</lgNaming:supportedPropertyType>
							</xsl:for-each>
							<xsl:for-each select="$var78_mappings/ns3:supportedPropertyLink">
								<lgNaming:supportedPropertyLink xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf14_supportedPropertyLink">
										<xsl:with-param name="input" select="." as="node()"/>
									</xsl:call-template>
								</lgNaming:supportedPropertyLink>
							</xsl:for-each>
							<xsl:for-each select="$var78_mappings/ns3:supportedPropertyQualifier">
								<lgNaming:supportedPropertyQualifier xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf15_supportedPropertyQualifier">
										<xsl:with-param name="input" select="." as="node()"/>
									</xsl:call-template>
								</lgNaming:supportedPropertyQualifier>
							</xsl:for-each>
							<xsl:for-each select="$var78_mappings/ns3:supportedPropertyQualifierType">
								<lgNaming:supportedPropertyQualifierType xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf16_supportedPropertyQualifierType">
										<xsl:with-param name="input" select="." as="node()"/>
									</xsl:call-template>
								</lgNaming:supportedPropertyQualifierType>
							</xsl:for-each>
							<xsl:for-each select="$var78_mappings/ns3:supportedRepresentationalForm">
								<lgNaming:supportedRepresentationalForm xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf17_supportedRepresentationalForm">
										<xsl:with-param name="input" select="." as="node()"/>
									</xsl:call-template>
								</lgNaming:supportedRepresentationalForm>
							</xsl:for-each>
							<xsl:for-each select="$var78_mappings/ns3:supportedSortOrder">
								<lgNaming:supportedSortOrder xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf18_supportedSortOrder">
										<xsl:with-param name="input" select="." as="node()"/>
									</xsl:call-template>
								</lgNaming:supportedSortOrder>
							</xsl:for-each>
							<xsl:for-each select="$var78_mappings/ns3:supportedSource">
								<lgNaming:supportedSource xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf19_supportedSource">
										<xsl:with-param name="input" select="." as="node()"/>
									</xsl:call-template>
								</lgNaming:supportedSource>
							</xsl:for-each>
							<xsl:for-each select="$var78_mappings/ns3:supportedSourceRole">
								<lgNaming:supportedSourceRole xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf20_supportedSourceRole">
										<xsl:with-param name="input" select="." as="node()"/>
									</xsl:call-template>
								</lgNaming:supportedSourceRole>
							</xsl:for-each>
							<xsl:for-each select="$var78_mappings/ns3:supportedStatus">
								<lgNaming:supportedStatus xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf21_supportedStatus">
										<xsl:with-param name="input" select="." as="node()"/>
									</xsl:call-template>
								</lgNaming:supportedStatus>
							</xsl:for-each>
						</lgVD:mappings>
						<xsl:for-each select="ns4:valueSetDefinition">
							<xsl:variable name="var81_valueSetDefinitionName" as="item()*" select="@valueSetDefinitionName"/>
							<xsl:variable name="var82_isActive" as="item()*" select="@isActive"/>
							<xsl:variable name="var83_status" as="item()*" select="@status"/>
							<xsl:variable name="var84_defaultCodingScheme" as="item()*" select="@defaultCodingScheme"/>
							<xsl:variable name="var85_effectiveDate" as="item()*" select="@effectiveDate"/>
							<xsl:variable name="var86_expirationDate" as="item()*" select="@expirationDate"/>
							<lgVD:valueDomainDefinition xsl:exclude-result-prefixes="lgVD">
								<xsl:if test="fn:exists($var82_isActive)">
									<xsl:attribute name="isActive" namespace="" select="xs:string(xs:boolean(fn:string($var82_isActive)))"/>
								</xsl:if>
								<xsl:if test="fn:exists($var83_status)">
									<xsl:attribute name="status" namespace="" select="fn:string($var83_status)"/>
								</xsl:if>
								<xsl:if test="fn:exists($var85_effectiveDate)">
									<xsl:attribute name="effectiveDate" namespace="" select="xs:string(xs:dateTime(fn:string($var85_effectiveDate)))"/>
								</xsl:if>
								<xsl:if test="fn:exists($var86_expirationDate)">
									<xsl:attribute name="expirationDate" namespace="" select="xs:string(xs:dateTime(fn:string($var86_expirationDate)))"/>
								</xsl:if>
								<xsl:attribute name="valueDomainURI" namespace="" select="xs:string(xs:anyURI(fn:string(@valueSetDefinitionURI)))"/>
								<xsl:if test="fn:exists($var81_valueSetDefinitionName)">
									<xsl:attribute name="valueDomainName" namespace="" select="fn:string($var81_valueSetDefinitionName)"/>
								</xsl:if>
								<xsl:if test="fn:exists($var84_defaultCodingScheme)">
									<xsl:attribute name="defaultCodingScheme" namespace="" select="fn:string($var84_defaultCodingScheme)"/>
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
										<xsl:sequence select="fn:string(.)"/>
									</lgCommon:entityDescription>
								</xsl:for-each>
								<xsl:for-each select="ns4:mappings">
									<lgVD:mappings xsl:exclude-result-prefixes="lgVD">
										<xsl:for-each select="ns3:supportedAssociation">
											<xsl:variable name="var87_uri" as="item()*" select="@uri"/>
											<lgNaming:supportedAssociation xsl:exclude-result-prefixes="lgNaming">
												<xsl:attribute name="localId" namespace="" select="fn:string(@localId)"/>
												<xsl:if test="fn:exists($var87_uri)">
													<xsl:attribute name="uri" namespace="" select="xs:string(xs:anyURI(fn:string($var87_uri)))"/>
												</xsl:if>
												<xsl:sequence select="fn:string(.)"/>
											</lgNaming:supportedAssociation>
										</xsl:for-each>
										<xsl:for-each select="ns3:supportedAssociationQualifier">
											<lgNaming:supportedAssociationQualifier xsl:exclude-result-prefixes="lgNaming">
												<xsl:call-template name="tbf:tbf3_supportedAssociationQualifier">
													<xsl:with-param name="input" select="." as="node()"/>
												</xsl:call-template>
											</lgNaming:supportedAssociationQualifier>
										</xsl:for-each>
										<xsl:for-each select="ns3:supportedCodingScheme">
											<lgNaming:supportedCodingScheme xsl:exclude-result-prefixes="lgNaming">
												<xsl:call-template name="tbf:tbf4_supportedCodingScheme">
													<xsl:with-param name="input" select="." as="node()"/>
												</xsl:call-template>
											</lgNaming:supportedCodingScheme>
										</xsl:for-each>
										<xsl:for-each select="ns3:supportedContext">
											<lgNaming:supportedContext xsl:exclude-result-prefixes="lgNaming">
												<xsl:call-template name="tbf:tbf6_supportedContext">
													<xsl:with-param name="input" select="." as="node()"/>
												</xsl:call-template>
											</lgNaming:supportedContext>
										</xsl:for-each>
										<xsl:for-each select="ns3:supportedDataType">
											<lgNaming:supportedDataType xsl:exclude-result-prefixes="lgNaming">
												<xsl:call-template name="tbf:tbf7_supportedDataType">
													<xsl:with-param name="input" select="." as="node()"/>
												</xsl:call-template>
											</lgNaming:supportedDataType>
										</xsl:for-each>
										<xsl:for-each select="ns3:supportedDegreeOfFidelity">
											<lgNaming:supportedDegreeOfFidelity xsl:exclude-result-prefixes="lgNaming">
												<xsl:call-template name="tbf:tbf8_supportedDegreeOfFidelity">
													<xsl:with-param name="input" select="." as="node()"/>
												</xsl:call-template>
											</lgNaming:supportedDegreeOfFidelity>
										</xsl:for-each>
										<xsl:for-each select="ns3:supportedEntityType">
											<lgNaming:supportedEntityType xsl:exclude-result-prefixes="lgNaming">
												<xsl:call-template name="tbf:tbf9_supportedEntityType">
													<xsl:with-param name="input" select="." as="node()"/>
												</xsl:call-template>
											</lgNaming:supportedEntityType>
										</xsl:for-each>
										<xsl:for-each select="ns3:supportedHierarchy">
											<lgNaming:supportedHierarchy xsl:exclude-result-prefixes="lgNaming">
												<xsl:call-template name="tbf:tbf10_supportedHierarchy">
													<xsl:with-param name="input" select="." as="node()"/>
												</xsl:call-template>
											</lgNaming:supportedHierarchy>
										</xsl:for-each>
										<xsl:for-each select="ns3:supportedLanguage">
											<lgNaming:supportedLanguage xsl:exclude-result-prefixes="lgNaming">
												<xsl:call-template name="tbf:tbf11_supportedLanguage">
													<xsl:with-param name="input" select="." as="node()"/>
												</xsl:call-template>
											</lgNaming:supportedLanguage>
										</xsl:for-each>
										<xsl:for-each select="ns3:supportedNamespace">
											<lgNaming:supportedNamespace xsl:exclude-result-prefixes="lgNaming">
												<xsl:call-template name="tbf:tbf12_supportedNamespace">
													<xsl:with-param name="input" select="." as="node()"/>
												</xsl:call-template>
											</lgNaming:supportedNamespace>
										</xsl:for-each>
										<xsl:for-each select="ns3:supportedProperty">
											<xsl:variable name="var88_uri" as="item()*" select="@uri"/>
											<lgNaming:supportedProperty xsl:exclude-result-prefixes="lgNaming">
												<xsl:attribute name="localId" namespace="" select="fn:string(@localId)"/>
												<xsl:if test="fn:exists($var88_uri)">
													<xsl:attribute name="uri" namespace="" select="xs:string(xs:anyURI(fn:string($var88_uri)))"/>
												</xsl:if>
												<xsl:sequence select="fn:string(.)"/>
											</lgNaming:supportedProperty>
										</xsl:for-each>
										<xsl:for-each select="ns3:supportedPropertyType">
											<lgNaming:supportedPropertyType xsl:exclude-result-prefixes="lgNaming">
												<xsl:call-template name="tbf:tbf13_supportedPropertyType">
													<xsl:with-param name="input" select="." as="node()"/>
												</xsl:call-template>
											</lgNaming:supportedPropertyType>
										</xsl:for-each>
										<xsl:for-each select="ns3:supportedPropertyLink">
											<lgNaming:supportedPropertyLink xsl:exclude-result-prefixes="lgNaming">
												<xsl:call-template name="tbf:tbf14_supportedPropertyLink">
													<xsl:with-param name="input" select="." as="node()"/>
												</xsl:call-template>
											</lgNaming:supportedPropertyLink>
										</xsl:for-each>
										<xsl:for-each select="ns3:supportedPropertyQualifier">
											<lgNaming:supportedPropertyQualifier xsl:exclude-result-prefixes="lgNaming">
												<xsl:call-template name="tbf:tbf15_supportedPropertyQualifier">
													<xsl:with-param name="input" select="." as="node()"/>
												</xsl:call-template>
											</lgNaming:supportedPropertyQualifier>
										</xsl:for-each>
										<xsl:for-each select="ns3:supportedPropertyQualifierType">
											<lgNaming:supportedPropertyQualifierType xsl:exclude-result-prefixes="lgNaming">
												<xsl:call-template name="tbf:tbf16_supportedPropertyQualifierType">
													<xsl:with-param name="input" select="." as="node()"/>
												</xsl:call-template>
											</lgNaming:supportedPropertyQualifierType>
										</xsl:for-each>
										<xsl:for-each select="ns3:supportedRepresentationalForm">
											<lgNaming:supportedRepresentationalForm xsl:exclude-result-prefixes="lgNaming">
												<xsl:call-template name="tbf:tbf17_supportedRepresentationalForm">
													<xsl:with-param name="input" select="." as="node()"/>
												</xsl:call-template>
											</lgNaming:supportedRepresentationalForm>
										</xsl:for-each>
										<xsl:for-each select="ns3:supportedSortOrder">
											<lgNaming:supportedSortOrder xsl:exclude-result-prefixes="lgNaming">
												<xsl:call-template name="tbf:tbf18_supportedSortOrder">
													<xsl:with-param name="input" select="." as="node()"/>
												</xsl:call-template>
											</lgNaming:supportedSortOrder>
										</xsl:for-each>
										<xsl:for-each select="ns3:supportedSource">
											<lgNaming:supportedSource xsl:exclude-result-prefixes="lgNaming">
												<xsl:call-template name="tbf:tbf19_supportedSource">
													<xsl:with-param name="input" select="." as="node()"/>
												</xsl:call-template>
											</lgNaming:supportedSource>
										</xsl:for-each>
										<xsl:for-each select="ns3:supportedSourceRole">
											<lgNaming:supportedSourceRole xsl:exclude-result-prefixes="lgNaming">
												<xsl:call-template name="tbf:tbf20_supportedSourceRole">
													<xsl:with-param name="input" select="." as="node()"/>
												</xsl:call-template>
											</lgNaming:supportedSourceRole>
										</xsl:for-each>
										<xsl:for-each select="ns3:supportedStatus">
											<lgNaming:supportedStatus xsl:exclude-result-prefixes="lgNaming">
												<xsl:call-template name="tbf:tbf21_supportedStatus">
													<xsl:with-param name="input" select="." as="node()"/>
												</xsl:call-template>
											</lgNaming:supportedStatus>
										</xsl:for-each>
									</lgVD:mappings>
								</xsl:for-each>
								<xsl:for-each select="ns4:source">
									<lgVD:source xsl:exclude-result-prefixes="lgVD">
										<xsl:call-template name="tbf:tbf2_source">
											<xsl:with-param name="input" select="." as="node()"/>
										</xsl:call-template>
									</lgVD:source>
								</xsl:for-each>
								<xsl:for-each select="ns4:representsRealmOrContext">
									<lgVD:representsRealmOrContext xsl:exclude-result-prefixes="lgVD">
										<xsl:sequence select="fn:string(.)"/>
									</lgVD:representsRealmOrContext>
								</xsl:for-each>
								<xsl:for-each select="ns4:properties">
									<lgVD:properties xsl:exclude-result-prefixes="lgVD">
										<xsl:for-each select="ns1:property">
											<xsl:variable name="var89_propertyType" as="item()*" select="@propertyType"/>
											<xsl:variable name="var90_propertyId" as="item()*" select="@propertyId"/>
											<xsl:variable name="var91_expirationDate" as="item()*" select="@expirationDate"/>
											<xsl:variable name="var92_effectiveDate" as="item()*" select="@effectiveDate"/>
											<xsl:variable name="var93_isActive" as="item()*" select="@isActive"/>
											<xsl:variable name="var94_status" as="item()*" select="@status"/>
											<xsl:variable name="var95_language" as="item()*" select="@language"/>
											<lgCommon:property xsl:exclude-result-prefixes="lgCommon">
												<xsl:if test="fn:exists($var93_isActive)">
													<xsl:attribute name="isActive" namespace="" select="xs:string(xs:boolean(fn:string($var93_isActive)))"/>
												</xsl:if>
												<xsl:if test="fn:exists($var94_status)">
													<xsl:attribute name="status" namespace="" select="fn:string($var94_status)"/>
												</xsl:if>
												<xsl:if test="fn:exists($var92_effectiveDate)">
													<xsl:attribute name="effectiveDate" namespace="" select="xs:string(xs:dateTime(fn:string($var92_effectiveDate)))"/>
												</xsl:if>
												<xsl:if test="fn:exists($var91_expirationDate)">
													<xsl:attribute name="expirationDate" namespace="" select="xs:string(xs:dateTime(fn:string($var91_expirationDate)))"/>
												</xsl:if>
												<xsl:attribute name="propertyName" namespace="" select="fn:string(@propertyName)"/>
												<xsl:if test="fn:exists($var90_propertyId)">
													<xsl:attribute name="propertyId" namespace="" select="fn:string($var90_propertyId)"/>
												</xsl:if>
												<xsl:if test="fn:exists($var89_propertyType)">
													<xsl:attribute name="propertyType" namespace="" select="fn:string($var89_propertyType)"/>
												</xsl:if>
												<xsl:if test="fn:exists($var95_language)">
													<xsl:attribute name="language" namespace="" select="fn:string($var95_language)"/>
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
													<xsl:variable name="var96_propertyQualifierType" as="item()*" select="@propertyQualifierType"/>
													<lgCommon:propertyQualifier xsl:exclude-result-prefixes="lgCommon">
														<xsl:attribute name="propertyQualifierName" namespace="" select="fn:string(@propertyQualifierName)"/>
														<xsl:if test="fn:exists($var96_propertyQualifierType)">
															<xsl:attribute name="propertyQualifierType" namespace="" select="fn:string($var96_propertyQualifierType)"/>
														</xsl:if>
														<xsl:for-each select="ns1:value">
															<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
																<xsl:sequence select="fn:string(.)"/>
															</lgCommon:value>
														</xsl:for-each>
													</lgCommon:propertyQualifier>
												</xsl:for-each>
												<xsl:for-each select="ns1:value">
													<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
														<xsl:call-template name="tbf:tbf23_text">
															<xsl:with-param name="input" select="." as="node()"/>
														</xsl:call-template>
													</lgCommon:value>
												</xsl:for-each>
											</lgCommon:property>
										</xsl:for-each>
									</lgVD:properties>
								</xsl:for-each>
								<xsl:for-each select="ns4:definitionEntry">
									<xsl:variable name="var97_operator" as="item()*" select="@operator"/>
									<lgVD:definitionEntry xsl:exclude-result-prefixes="lgVD">
										<xsl:attribute name="ruleOrder" namespace="" select="xs:string(xs:integer(fn:string(@ruleOrder)))"/>
										<xsl:if test="fn:exists($var97_operator)">
											<xsl:attribute name="operator" namespace="" select="fn:string($var97_operator)"/>
										</xsl:if>
										<xsl:for-each select="ns4:entityReference">
											<lgVD:entityReference xsl:exclude-result-prefixes="lgVD">
												<xsl:call-template name="tbf:tbf24_entityReference">
													<xsl:with-param name="input" select="." as="node()"/>
												</xsl:call-template>
											</lgVD:entityReference>
										</xsl:for-each>
										<xsl:for-each select="ns4:valueSetDefinitionReference">
											<lgVD:valueDomainReference xsl:exclude-result-prefixes="lgVD">
												<xsl:sequence select="()"/>
											</lgVD:valueDomainReference>
										</xsl:for-each>
										<xsl:for-each select="ns4:codingSchemeReference">
											<lgVD:codingSchemeReference xsl:exclude-result-prefixes="lgVD">
												<xsl:call-template name="tbf:tbf25_codingSchemeReference">
													<xsl:with-param name="input" select="." as="node()"/>
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
					<xsl:variable name="var98_mappings" as="node()" select="ns4:mappings"/>
					<pickLists>
						<lgVD:mappings xsl:exclude-result-prefixes="lgVD">
							<xsl:for-each select="$var98_mappings/ns3:supportedAssociation">
								<xsl:variable name="var99_uri" as="item()*" select="@uri"/>
								<lgNaming:supportedAssociation xsl:exclude-result-prefixes="lgNaming">
									<xsl:attribute name="localId" namespace="" select="fn:string(@localId)"/>
									<xsl:if test="fn:exists($var99_uri)">
										<xsl:attribute name="uri" namespace="" select="xs:string(xs:anyURI(fn:string($var99_uri)))"/>
									</xsl:if>
									<xsl:sequence select="fn:string(.)"/>
								</lgNaming:supportedAssociation>
							</xsl:for-each>
							<xsl:for-each select="$var98_mappings/ns3:supportedAssociationQualifier">
								<lgNaming:supportedAssociationQualifier xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf3_supportedAssociationQualifier">
										<xsl:with-param name="input" select="." as="node()"/>
									</xsl:call-template>
								</lgNaming:supportedAssociationQualifier>
							</xsl:for-each>
							<xsl:for-each select="$var98_mappings/ns3:supportedCodingScheme">
								<lgNaming:supportedCodingScheme xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf4_supportedCodingScheme">
										<xsl:with-param name="input" select="." as="node()"/>
									</xsl:call-template>
								</lgNaming:supportedCodingScheme>
							</xsl:for-each>
							<xsl:for-each select="$var98_mappings/ns3:supportedContainerName">
								<lgNaming:supportedContainer xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf5_supportedContainerName">
										<xsl:with-param name="input" select="." as="node()"/>
									</xsl:call-template>
								</lgNaming:supportedContainer>
							</xsl:for-each>
							<xsl:for-each select="$var98_mappings/ns3:supportedContext">
								<lgNaming:supportedContext xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf6_supportedContext">
										<xsl:with-param name="input" select="." as="node()"/>
									</xsl:call-template>
								</lgNaming:supportedContext>
							</xsl:for-each>
							<xsl:for-each select="$var98_mappings/ns3:supportedDataType">
								<lgNaming:supportedDataType xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf7_supportedDataType">
										<xsl:with-param name="input" select="." as="node()"/>
									</xsl:call-template>
								</lgNaming:supportedDataType>
							</xsl:for-each>
							<xsl:for-each select="$var98_mappings/ns3:supportedDegreeOfFidelity">
								<lgNaming:supportedDegreeOfFidelity xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf8_supportedDegreeOfFidelity">
										<xsl:with-param name="input" select="." as="node()"/>
									</xsl:call-template>
								</lgNaming:supportedDegreeOfFidelity>
							</xsl:for-each>
							<xsl:for-each select="$var98_mappings/ns3:supportedEntityType">
								<lgNaming:supportedEntityType xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf9_supportedEntityType">
										<xsl:with-param name="input" select="." as="node()"/>
									</xsl:call-template>
								</lgNaming:supportedEntityType>
							</xsl:for-each>
							<xsl:for-each select="$var98_mappings/ns3:supportedHierarchy">
								<lgNaming:supportedHierarchy xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf10_supportedHierarchy">
										<xsl:with-param name="input" select="." as="node()"/>
									</xsl:call-template>
								</lgNaming:supportedHierarchy>
							</xsl:for-each>
							<xsl:for-each select="$var98_mappings/ns3:supportedLanguage">
								<lgNaming:supportedLanguage xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf11_supportedLanguage">
										<xsl:with-param name="input" select="." as="node()"/>
									</xsl:call-template>
								</lgNaming:supportedLanguage>
							</xsl:for-each>
							<xsl:for-each select="$var98_mappings/ns3:supportedNamespace">
								<lgNaming:supportedNamespace xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf12_supportedNamespace">
										<xsl:with-param name="input" select="." as="node()"/>
									</xsl:call-template>
								</lgNaming:supportedNamespace>
							</xsl:for-each>
							<xsl:for-each select="$var98_mappings/ns3:supportedProperty">
								<xsl:variable name="var100_uri" as="item()*" select="@uri"/>
								<lgNaming:supportedProperty xsl:exclude-result-prefixes="lgNaming">
									<xsl:attribute name="localId" namespace="" select="fn:string(@localId)"/>
									<xsl:if test="fn:exists($var100_uri)">
										<xsl:attribute name="uri" namespace="" select="xs:string(xs:anyURI(fn:string($var100_uri)))"/>
									</xsl:if>
									<xsl:sequence select="fn:string(.)"/>
								</lgNaming:supportedProperty>
							</xsl:for-each>
							<xsl:for-each select="$var98_mappings/ns3:supportedPropertyType">
								<lgNaming:supportedPropertyType xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf13_supportedPropertyType">
										<xsl:with-param name="input" select="." as="node()"/>
									</xsl:call-template>
								</lgNaming:supportedPropertyType>
							</xsl:for-each>
							<xsl:for-each select="$var98_mappings/ns3:supportedPropertyLink">
								<lgNaming:supportedPropertyLink xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf14_supportedPropertyLink">
										<xsl:with-param name="input" select="." as="node()"/>
									</xsl:call-template>
								</lgNaming:supportedPropertyLink>
							</xsl:for-each>
							<xsl:for-each select="$var98_mappings/ns3:supportedPropertyQualifier">
								<lgNaming:supportedPropertyQualifier xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf15_supportedPropertyQualifier">
										<xsl:with-param name="input" select="." as="node()"/>
									</xsl:call-template>
								</lgNaming:supportedPropertyQualifier>
							</xsl:for-each>
							<xsl:for-each select="$var98_mappings/ns3:supportedPropertyQualifierType">
								<lgNaming:supportedPropertyQualifierType xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf16_supportedPropertyQualifierType">
										<xsl:with-param name="input" select="." as="node()"/>
									</xsl:call-template>
								</lgNaming:supportedPropertyQualifierType>
							</xsl:for-each>
							<xsl:for-each select="$var98_mappings/ns3:supportedRepresentationalForm">
								<lgNaming:supportedRepresentationalForm xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf17_supportedRepresentationalForm">
										<xsl:with-param name="input" select="." as="node()"/>
									</xsl:call-template>
								</lgNaming:supportedRepresentationalForm>
							</xsl:for-each>
							<xsl:for-each select="$var98_mappings/ns3:supportedSortOrder">
								<lgNaming:supportedSortOrder xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf18_supportedSortOrder">
										<xsl:with-param name="input" select="." as="node()"/>
									</xsl:call-template>
								</lgNaming:supportedSortOrder>
							</xsl:for-each>
							<xsl:for-each select="$var98_mappings/ns3:supportedSource">
								<lgNaming:supportedSource xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf19_supportedSource">
										<xsl:with-param name="input" select="." as="node()"/>
									</xsl:call-template>
								</lgNaming:supportedSource>
							</xsl:for-each>
							<xsl:for-each select="$var98_mappings/ns3:supportedSourceRole">
								<lgNaming:supportedSourceRole xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf20_supportedSourceRole">
										<xsl:with-param name="input" select="." as="node()"/>
									</xsl:call-template>
								</lgNaming:supportedSourceRole>
							</xsl:for-each>
							<xsl:for-each select="$var98_mappings/ns3:supportedStatus">
								<lgNaming:supportedStatus xsl:exclude-result-prefixes="lgNaming">
									<xsl:call-template name="tbf:tbf21_supportedStatus">
										<xsl:with-param name="input" select="." as="node()"/>
									</xsl:call-template>
								</lgNaming:supportedStatus>
							</xsl:for-each>
						</lgVD:mappings>
						<xsl:for-each select="ns4:pickListDefinition">
							<xsl:variable name="var101_expirationDate" as="item()*" select="@expirationDate"/>
							<xsl:variable name="var102_status" as="item()*" select="@status"/>
							<xsl:variable name="var103_defaultSortOrder" as="item()*" select="@defaultSortOrder"/>
							<xsl:variable name="var104_defaultLanguage" as="item()*" select="@defaultLanguage"/>
							<xsl:variable name="var105_defaultEntityCodeNamespace" as="item()*" select="@defaultEntityCodeNamespace"/>
							<xsl:variable name="var106_isActive" as="item()*" select="@isActive"/>
							<xsl:variable name="var107_effectiveDate" as="item()*" select="@effectiveDate"/>
							<lgVD:pickListDefinition xsl:exclude-result-prefixes="lgVD">
								<xsl:if test="fn:exists($var106_isActive)">
									<xsl:attribute name="isActive" namespace="" select="xs:string(xs:boolean(fn:string($var106_isActive)))"/>
								</xsl:if>
								<xsl:if test="fn:exists($var102_status)">
									<xsl:attribute name="status" namespace="" select="fn:string($var102_status)"/>
								</xsl:if>
								<xsl:if test="fn:exists($var107_effectiveDate)">
									<xsl:attribute name="effectiveDate" namespace="" select="xs:string(xs:dateTime(fn:string($var107_effectiveDate)))"/>
								</xsl:if>
								<xsl:if test="fn:exists($var101_expirationDate)">
									<xsl:attribute name="expirationDate" namespace="" select="xs:string(xs:dateTime(fn:string($var101_expirationDate)))"/>
								</xsl:if>
								<xsl:attribute name="pickListId" namespace="" select="fn:string(@pickListId)"/>
								<xsl:if test="fn:exists($var105_defaultEntityCodeNamespace)">
									<xsl:attribute name="defaultEntityCodeNamespace" namespace="" select="fn:string($var105_defaultEntityCodeNamespace)"/>
								</xsl:if>
								<xsl:if test="fn:exists($var104_defaultLanguage)">
									<xsl:attribute name="defaultLanguage" namespace="" select="fn:string($var104_defaultLanguage)"/>
								</xsl:if>
								<xsl:if test="fn:exists($var103_defaultSortOrder)">
									<xsl:attribute name="defaultSortOrder" namespace="" select="fn:string($var103_defaultSortOrder)"/>
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
										<xsl:sequence select="fn:string(.)"/>
									</lgCommon:entityDescription>
								</xsl:for-each>
							</lgVD:pickListDefinition>
						</xsl:for-each>
					</pickLists>
				</xsl:for-each>
				<xsl:for-each select="ns5:editHistory">
					<editHistory>
						<xsl:for-each select="ns5:revision">
							<xsl:variable name="var108_editOrder" as="item()*" select="@editOrder"/>
							<xsl:variable name="var109_revisionDate" as="item()*" select="@revisionDate"/>
							<revision>
								<xsl:attribute name="revisionId" namespace="" select="fn:string(@revisionId)"/>
								<xsl:if test="fn:exists($var109_revisionDate)">
									<xsl:attribute name="revisionDate" namespace="" select="xs:string(xs:dateTime(fn:string($var109_revisionDate)))"/>
								</xsl:if>
								<xsl:if test="fn:exists($var108_editOrder)">
									<xsl:attribute name="editOrder" namespace="" select="xs:string(xs:integer(fn:string($var108_editOrder)))"/>
								</xsl:if>
								<xsl:for-each select="ns1:entityDescription">
									<lgCommon:entityDescription xsl:exclude-result-prefixes="lgCommon">
										<xsl:for-each select="node()[fn:boolean(self::text())]">
											<xsl:sequence select="fn:string(.)"/>
										</xsl:for-each>
									</lgCommon:entityDescription>
								</xsl:for-each>
								<xsl:for-each select="ns5:changeAgent">
									<changeAgent>
										<xsl:sequence select="fn:string(.)"/>
									</changeAgent>
								</xsl:for-each>
								<xsl:for-each select="ns5:changeInstructions">
									<xsl:variable name="var110_dataType" as="item()*" select="@dataType"/>
									<changeInstructions>
										<xsl:if test="fn:exists($var110_dataType)">
											<xsl:attribute name="dataType" namespace="" select="fn:string($var110_dataType)"/>
										</xsl:if>
										<xsl:for-each select="node()[fn:boolean(self::text())]">
											<xsl:sequence select="fn:string(.)"/>
										</xsl:for-each>
									</changeInstructions>
								</xsl:for-each>
								<xsl:for-each select="ns5:changedEntry">
									<changedEntry>
										<xsl:for-each select="ns5:changedCodingSchemeEntry">
											<xsl:variable name="var111_isActive" as="item()*" select="@isActive"/>
											<xsl:variable name="var112_status" as="item()*" select="@status"/>
											<xsl:variable name="var113_defaultLanguage" as="item()*" select="@defaultLanguage"/>
											<xsl:variable name="var114_effectiveDate" as="item()*" select="@effectiveDate"/>
											<xsl:variable name="var115_approxNumConcepts" as="item()*" select="@approxNumConcepts"/>
											<xsl:variable name="var116_formalName" as="item()*" select="@formalName"/>
											<xsl:variable name="var117_expirationDate" as="item()*" select="@expirationDate"/>
											<xsl:variable name="var118_mappings" as="node()" select="ns0:mappings"/>
											<changedCodingSchemeEntry>
												<xsl:if test="fn:exists($var111_isActive)">
													<xsl:attribute name="isActive" namespace="" select="xs:string(xs:boolean(fn:string($var111_isActive)))"/>
												</xsl:if>
												<xsl:if test="fn:exists($var112_status)">
													<xsl:attribute name="status" namespace="" select="fn:string($var112_status)"/>
												</xsl:if>
												<xsl:if test="fn:exists($var114_effectiveDate)">
													<xsl:attribute name="effectiveDate" namespace="" select="xs:string(xs:dateTime(fn:string($var114_effectiveDate)))"/>
												</xsl:if>
												<xsl:if test="fn:exists($var117_expirationDate)">
													<xsl:attribute name="expirationDate" namespace="" select="xs:string(xs:dateTime(fn:string($var117_expirationDate)))"/>
												</xsl:if>
												<xsl:attribute name="codingSchemeName" namespace="" select="fn:string(@codingSchemeName)"/>
												<xsl:attribute name="codingSchemeURI" namespace="" select="xs:string(xs:anyURI(fn:string(@codingSchemeURI)))"/>
												<xsl:if test="fn:exists($var116_formalName)">
													<xsl:attribute name="formalName" namespace="" select="fn:string($var116_formalName)"/>
												</xsl:if>
												<xsl:if test="fn:exists($var113_defaultLanguage)">
													<xsl:attribute name="defaultLanguage" namespace="" select="fn:string($var113_defaultLanguage)"/>
												</xsl:if>
												<xsl:if test="fn:exists($var115_approxNumConcepts)">
													<xsl:attribute name="approxNumConcepts" namespace="" select="xs:string(xs:integer(fn:string($var115_approxNumConcepts)))"/>
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
													<lgCS:localName xsl:exclude-result-prefixes="lgCS">
														<xsl:sequence select="fn:string(.)"/>
													</lgCS:localName>
												</xsl:for-each>
												<xsl:for-each select="ns0:source">
													<lgCS:source xsl:exclude-result-prefixes="lgCS">
														<xsl:call-template name="tbf:tbf2_source">
															<xsl:with-param name="input" select="." as="node()"/>
														</xsl:call-template>
													</lgCS:source>
												</xsl:for-each>
												<xsl:for-each select="ns0:copyright">
													<xsl:variable name="var119_dataType" as="item()*" select="@dataType"/>
													<lgCS:copyright xsl:exclude-result-prefixes="lgCS">
														<xsl:if test="fn:exists($var119_dataType)">
															<xsl:attribute name="dataType" namespace="" select="fn:string($var119_dataType)"/>
														</xsl:if>
														<xsl:for-each select="node()[fn:boolean(self::text())]">
															<xsl:sequence select="fn:string(.)"/>
														</xsl:for-each>
													</lgCS:copyright>
												</xsl:for-each>
												<lgCS:mappings xsl:exclude-result-prefixes="lgCS">
													<xsl:for-each select="$var118_mappings/ns3:supportedAssociation">
														<xsl:variable name="var120_uri" as="item()*" select="@uri"/>
														<lgNaming:supportedAssociation xsl:exclude-result-prefixes="lgNaming">
															<xsl:attribute name="localId" namespace="" select="fn:string(@localId)"/>
															<xsl:if test="fn:exists($var120_uri)">
																<xsl:attribute name="uri" namespace="" select="xs:string(xs:anyURI(fn:string($var120_uri)))"/>
															</xsl:if>
															<xsl:sequence select="fn:string(.)"/>
														</lgNaming:supportedAssociation>
													</xsl:for-each>
													<xsl:for-each select="$var118_mappings/ns3:supportedAssociationQualifier">
														<lgNaming:supportedAssociationQualifier xsl:exclude-result-prefixes="lgNaming">
															<xsl:call-template name="tbf:tbf3_supportedAssociationQualifier">
																<xsl:with-param name="input" select="." as="node()"/>
															</xsl:call-template>
														</lgNaming:supportedAssociationQualifier>
													</xsl:for-each>
													<xsl:for-each select="$var118_mappings/ns3:supportedCodingScheme">
														<lgNaming:supportedCodingScheme xsl:exclude-result-prefixes="lgNaming">
															<xsl:call-template name="tbf:tbf4_supportedCodingScheme">
																<xsl:with-param name="input" select="." as="node()"/>
															</xsl:call-template>
														</lgNaming:supportedCodingScheme>
													</xsl:for-each>
													<xsl:for-each select="$var118_mappings/ns3:supportedContext">
														<lgNaming:supportedContext xsl:exclude-result-prefixes="lgNaming">
															<xsl:call-template name="tbf:tbf6_supportedContext">
																<xsl:with-param name="input" select="." as="node()"/>
															</xsl:call-template>
														</lgNaming:supportedContext>
													</xsl:for-each>
													<xsl:for-each select="$var118_mappings/ns3:supportedDataType">
														<lgNaming:supportedDataType xsl:exclude-result-prefixes="lgNaming">
															<xsl:call-template name="tbf:tbf7_supportedDataType">
																<xsl:with-param name="input" select="." as="node()"/>
															</xsl:call-template>
														</lgNaming:supportedDataType>
													</xsl:for-each>
													<xsl:for-each select="$var118_mappings/ns3:supportedDegreeOfFidelity">
														<lgNaming:supportedDegreeOfFidelity xsl:exclude-result-prefixes="lgNaming">
															<xsl:call-template name="tbf:tbf8_supportedDegreeOfFidelity">
																<xsl:with-param name="input" select="." as="node()"/>
															</xsl:call-template>
														</lgNaming:supportedDegreeOfFidelity>
													</xsl:for-each>
													<xsl:for-each select="$var118_mappings/ns3:supportedEntityType">
														<lgNaming:supportedEntityType xsl:exclude-result-prefixes="lgNaming">
															<xsl:call-template name="tbf:tbf9_supportedEntityType">
																<xsl:with-param name="input" select="." as="node()"/>
															</xsl:call-template>
														</lgNaming:supportedEntityType>
													</xsl:for-each>
													<xsl:for-each select="$var118_mappings/ns3:supportedHierarchy">
														<lgNaming:supportedHierarchy xsl:exclude-result-prefixes="lgNaming">
															<xsl:call-template name="tbf:tbf10_supportedHierarchy">
																<xsl:with-param name="input" select="." as="node()"/>
															</xsl:call-template>
														</lgNaming:supportedHierarchy>
													</xsl:for-each>
													<xsl:for-each select="$var118_mappings/ns3:supportedLanguage">
														<lgNaming:supportedLanguage xsl:exclude-result-prefixes="lgNaming">
															<xsl:call-template name="tbf:tbf11_supportedLanguage">
																<xsl:with-param name="input" select="." as="node()"/>
															</xsl:call-template>
														</lgNaming:supportedLanguage>
													</xsl:for-each>
													<xsl:for-each select="$var118_mappings/ns3:supportedNamespace">
														<lgNaming:supportedNamespace xsl:exclude-result-prefixes="lgNaming">
															<xsl:call-template name="tbf:tbf12_supportedNamespace">
																<xsl:with-param name="input" select="." as="node()"/>
															</xsl:call-template>
														</lgNaming:supportedNamespace>
													</xsl:for-each>
													<xsl:for-each select="$var118_mappings/ns3:supportedProperty">
														<xsl:variable name="var121_uri" as="item()*" select="@uri"/>
														<lgNaming:supportedProperty xsl:exclude-result-prefixes="lgNaming">
															<xsl:attribute name="localId" namespace="" select="fn:string(@localId)"/>
															<xsl:if test="fn:exists($var121_uri)">
																<xsl:attribute name="uri" namespace="" select="xs:string(xs:anyURI(fn:string($var121_uri)))"/>
															</xsl:if>
															<xsl:sequence select="fn:string(.)"/>
														</lgNaming:supportedProperty>
													</xsl:for-each>
													<xsl:for-each select="$var118_mappings/ns3:supportedPropertyType">
														<lgNaming:supportedPropertyType xsl:exclude-result-prefixes="lgNaming">
															<xsl:call-template name="tbf:tbf13_supportedPropertyType">
																<xsl:with-param name="input" select="." as="node()"/>
															</xsl:call-template>
														</lgNaming:supportedPropertyType>
													</xsl:for-each>
													<xsl:for-each select="$var118_mappings/ns3:supportedPropertyLink">
														<lgNaming:supportedPropertyLink xsl:exclude-result-prefixes="lgNaming">
															<xsl:call-template name="tbf:tbf14_supportedPropertyLink">
																<xsl:with-param name="input" select="." as="node()"/>
															</xsl:call-template>
														</lgNaming:supportedPropertyLink>
													</xsl:for-each>
													<xsl:for-each select="$var118_mappings/ns3:supportedPropertyQualifier">
														<lgNaming:supportedPropertyQualifier xsl:exclude-result-prefixes="lgNaming">
															<xsl:call-template name="tbf:tbf15_supportedPropertyQualifier">
																<xsl:with-param name="input" select="." as="node()"/>
															</xsl:call-template>
														</lgNaming:supportedPropertyQualifier>
													</xsl:for-each>
													<xsl:for-each select="$var118_mappings/ns3:supportedPropertyQualifierType">
														<lgNaming:supportedPropertyQualifierType xsl:exclude-result-prefixes="lgNaming">
															<xsl:call-template name="tbf:tbf16_supportedPropertyQualifierType">
																<xsl:with-param name="input" select="." as="node()"/>
															</xsl:call-template>
														</lgNaming:supportedPropertyQualifierType>
													</xsl:for-each>
													<xsl:for-each select="$var118_mappings/ns3:supportedRepresentationalForm">
														<lgNaming:supportedRepresentationalForm xsl:exclude-result-prefixes="lgNaming">
															<xsl:call-template name="tbf:tbf17_supportedRepresentationalForm">
																<xsl:with-param name="input" select="." as="node()"/>
															</xsl:call-template>
														</lgNaming:supportedRepresentationalForm>
													</xsl:for-each>
													<xsl:for-each select="$var118_mappings/ns3:supportedSortOrder">
														<lgNaming:supportedSortOrder xsl:exclude-result-prefixes="lgNaming">
															<xsl:call-template name="tbf:tbf18_supportedSortOrder">
																<xsl:with-param name="input" select="." as="node()"/>
															</xsl:call-template>
														</lgNaming:supportedSortOrder>
													</xsl:for-each>
													<xsl:for-each select="$var118_mappings/ns3:supportedSource">
														<lgNaming:supportedSource xsl:exclude-result-prefixes="lgNaming">
															<xsl:call-template name="tbf:tbf19_supportedSource">
																<xsl:with-param name="input" select="." as="node()"/>
															</xsl:call-template>
														</lgNaming:supportedSource>
													</xsl:for-each>
													<xsl:for-each select="$var118_mappings/ns3:supportedSourceRole">
														<lgNaming:supportedSourceRole xsl:exclude-result-prefixes="lgNaming">
															<xsl:call-template name="tbf:tbf20_supportedSourceRole">
																<xsl:with-param name="input" select="." as="node()"/>
															</xsl:call-template>
														</lgNaming:supportedSourceRole>
													</xsl:for-each>
													<xsl:for-each select="$var118_mappings/ns3:supportedStatus">
														<lgNaming:supportedStatus xsl:exclude-result-prefixes="lgNaming">
															<xsl:call-template name="tbf:tbf21_supportedStatus">
																<xsl:with-param name="input" select="." as="node()"/>
															</xsl:call-template>
														</lgNaming:supportedStatus>
													</xsl:for-each>
												</lgCS:mappings>
												<xsl:for-each select="ns0:properties">
													<lgCS:properties xsl:exclude-result-prefixes="lgCS">
														<xsl:for-each select="ns1:property">
															<xsl:variable name="var122_propertyId" as="item()*" select="@propertyId"/>
															<xsl:variable name="var123_status" as="item()*" select="@status"/>
															<xsl:variable name="var124_language" as="item()*" select="@language"/>
															<xsl:variable name="var125_isActive" as="item()*" select="@isActive"/>
															<xsl:variable name="var126_expirationDate" as="item()*" select="@expirationDate"/>
															<xsl:variable name="var127_effectiveDate" as="item()*" select="@effectiveDate"/>
															<xsl:variable name="var128_propertyType" as="item()*" select="@propertyType"/>
															<lgCommon:property xsl:exclude-result-prefixes="lgCommon">
																<xsl:if test="fn:exists($var125_isActive)">
																	<xsl:attribute name="isActive" namespace="" select="xs:string(xs:boolean(fn:string($var125_isActive)))"/>
																</xsl:if>
																<xsl:if test="fn:exists($var123_status)">
																	<xsl:attribute name="status" namespace="" select="fn:string($var123_status)"/>
																</xsl:if>
																<xsl:if test="fn:exists($var127_effectiveDate)">
																	<xsl:attribute name="effectiveDate" namespace="" select="xs:string(xs:dateTime(fn:string($var127_effectiveDate)))"/>
																</xsl:if>
																<xsl:if test="fn:exists($var126_expirationDate)">
																	<xsl:attribute name="expirationDate" namespace="" select="xs:string(xs:dateTime(fn:string($var126_expirationDate)))"/>
																</xsl:if>
																<xsl:attribute name="propertyName" namespace="" select="fn:string(@propertyName)"/>
																<xsl:if test="fn:exists($var122_propertyId)">
																	<xsl:attribute name="propertyId" namespace="" select="fn:string($var122_propertyId)"/>
																</xsl:if>
																<xsl:if test="fn:exists($var128_propertyType)">
																	<xsl:attribute name="propertyType" namespace="" select="fn:string($var128_propertyType)"/>
																</xsl:if>
																<xsl:if test="fn:exists($var124_language)">
																	<xsl:attribute name="language" namespace="" select="fn:string($var124_language)"/>
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
																	<xsl:variable name="var129_propertyQualifierType" as="item()*" select="@propertyQualifierType"/>
																	<lgCommon:propertyQualifier xsl:exclude-result-prefixes="lgCommon">
																		<xsl:attribute name="propertyQualifierName" namespace="" select="fn:string(@propertyQualifierName)"/>
																		<xsl:if test="fn:exists($var129_propertyQualifierType)">
																			<xsl:attribute name="propertyQualifierType" namespace="" select="fn:string($var129_propertyQualifierType)"/>
																		</xsl:if>
																		<xsl:for-each select="ns1:value">
																			<xsl:variable name="var130_dataType" as="item()*" select="@dataType"/>
																			<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
																				<xsl:if test="fn:exists($var130_dataType)">
																					<xsl:attribute name="dataType" namespace="" select="fn:string($var130_dataType)"/>
																				</xsl:if>
																				<xsl:for-each select="node()[fn:boolean(self::text())]">
																					<xsl:sequence select="fn:string(.)"/>
																				</xsl:for-each>
																			</lgCommon:value>
																		</xsl:for-each>
																	</lgCommon:propertyQualifier>
																</xsl:for-each>
																<xsl:for-each select="ns1:value">
																	<xsl:variable name="var131_dataType" as="item()*" select="@dataType"/>
																	<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
																		<xsl:if test="fn:exists($var131_dataType)">
																			<xsl:attribute name="dataType" namespace="" select="fn:string($var131_dataType)"/>
																		</xsl:if>
																		<xsl:for-each select="node()[fn:boolean(self::text())]">
																			<xsl:sequence select="fn:string(.)"/>
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
															<xsl:variable name="var132_effectiveDate" as="item()*" select="@effectiveDate"/>
															<xsl:variable name="var133_expirationDate" as="item()*" select="@expirationDate"/>
															<xsl:variable name="var134_status" as="item()*" select="@status"/>
															<xsl:variable name="var135_isDefined" as="item()*" select="@isDefined"/>
															<xsl:variable name="var136_entityCodeNamespace" as="item()*" select="@entityCodeNamespace"/>
															<xsl:variable name="var137_isActive" as="item()*" select="@isActive"/>
															<xsl:variable name="var138_isAnonymous" as="item()*" select="@isAnonymous"/>
															<lgCon:entity xsl:exclude-result-prefixes="lgCon">
																<xsl:if test="fn:exists($var137_isActive)">
																	<xsl:attribute name="isActive" namespace="" select="xs:string(xs:boolean(fn:string($var137_isActive)))"/>
																</xsl:if>
																<xsl:if test="fn:exists($var134_status)">
																	<xsl:attribute name="status" namespace="" select="fn:string($var134_status)"/>
																</xsl:if>
																<xsl:if test="fn:exists($var132_effectiveDate)">
																	<xsl:attribute name="effectiveDate" namespace="" select="xs:string(xs:dateTime(fn:string($var132_effectiveDate)))"/>
																</xsl:if>
																<xsl:if test="fn:exists($var133_expirationDate)">
																	<xsl:attribute name="expirationDate" namespace="" select="xs:string(xs:dateTime(fn:string($var133_expirationDate)))"/>
																</xsl:if>
																<xsl:attribute name="entityCode" namespace="" select="fn:string(@entityCode)"/>
																<xsl:if test="fn:exists($var136_entityCodeNamespace)">
																	<xsl:attribute name="entityCodeNamespace" namespace="" select="fn:string($var136_entityCodeNamespace)"/>
																</xsl:if>
																<xsl:if test="fn:exists($var138_isAnonymous)">
																	<xsl:attribute name="isAnonymous" namespace="" select="xs:string(xs:boolean(fn:string($var138_isAnonymous)))"/>
																</xsl:if>
																<xsl:if test="fn:exists($var135_isDefined)">
																	<xsl:attribute name="isDefined" namespace="" select="xs:string(xs:boolean(fn:string($var135_isDefined)))"/>
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
																	<xsl:variable name="var139_status" as="item()*" select="@status"/>
																	<xsl:variable name="var140_isActive" as="item()*" select="@isActive"/>
																	<xsl:variable name="var141_degreeOfFidelity" as="item()*" select="@degreeOfFidelity"/>
																	<xsl:variable name="var142_propertyType" as="item()*" select="@propertyType"/>
																	<xsl:variable name="var143_propertyId" as="item()*" select="@propertyId"/>
																	<xsl:variable name="var144_representationalForm" as="item()*" select="@representationalForm"/>
																	<xsl:variable name="var145_matchIfNoContext" as="item()*" select="@matchIfNoContext"/>
																	<xsl:variable name="var146_effectiveDate" as="item()*" select="@effectiveDate"/>
																	<xsl:variable name="var147_expirationDate" as="item()*" select="@expirationDate"/>
																	<xsl:variable name="var148_language" as="item()*" select="@language"/>
																	<xsl:variable name="var149_isPreferred" as="item()*" select="@isPreferred"/>
																	<lgCon:presentation xsl:exclude-result-prefixes="lgCon">
																		<xsl:if test="fn:exists($var140_isActive)">
																			<xsl:attribute name="isActive" namespace="" select="xs:string(xs:boolean(fn:string($var140_isActive)))"/>
																		</xsl:if>
																		<xsl:if test="fn:exists($var139_status)">
																			<xsl:attribute name="status" namespace="" select="fn:string($var139_status)"/>
																		</xsl:if>
																		<xsl:if test="fn:exists($var146_effectiveDate)">
																			<xsl:attribute name="effectiveDate" namespace="" select="xs:string(xs:dateTime(fn:string($var146_effectiveDate)))"/>
																		</xsl:if>
																		<xsl:if test="fn:exists($var147_expirationDate)">
																			<xsl:attribute name="expirationDate" namespace="" select="xs:string(xs:dateTime(fn:string($var147_expirationDate)))"/>
																		</xsl:if>
																		<xsl:attribute name="propertyName" namespace="" select="fn:string(@propertyName)"/>
																		<xsl:if test="fn:exists($var143_propertyId)">
																			<xsl:attribute name="propertyId" namespace="" select="fn:string($var143_propertyId)"/>
																		</xsl:if>
																		<xsl:if test="fn:exists($var142_propertyType)">
																			<xsl:attribute name="propertyType" namespace="" select="fn:string($var142_propertyType)"/>
																		</xsl:if>
																		<xsl:if test="fn:exists($var148_language)">
																			<xsl:attribute name="language" namespace="" select="fn:string($var148_language)"/>
																		</xsl:if>
																		<xsl:if test="fn:exists($var149_isPreferred)">
																			<xsl:attribute name="isPreferred" namespace="" select="xs:string(xs:boolean(fn:string($var149_isPreferred)))"/>
																		</xsl:if>
																		<xsl:if test="fn:exists($var141_degreeOfFidelity)">
																			<xsl:attribute name="degreeOfFidelity" namespace="" select="fn:string($var141_degreeOfFidelity)"/>
																		</xsl:if>
																		<xsl:if test="fn:exists($var145_matchIfNoContext)">
																			<xsl:attribute name="matchIfNoContext" namespace="" select="xs:string(xs:boolean(fn:string($var145_matchIfNoContext)))"/>
																		</xsl:if>
																		<xsl:if test="fn:exists($var144_representationalForm)">
																			<xsl:attribute name="representationalForm" namespace="" select="fn:string($var144_representationalForm)"/>
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
																			<xsl:variable name="var150_propertyQualifierType" as="item()*" select="@propertyQualifierType"/>
																			<lgCommon:propertyQualifier xsl:exclude-result-prefixes="lgCommon">
																				<xsl:attribute name="propertyQualifierName" namespace="" select="fn:string(@propertyQualifierName)"/>
																				<xsl:if test="fn:exists($var150_propertyQualifierType)">
																					<xsl:attribute name="propertyQualifierType" namespace="" select="fn:string($var150_propertyQualifierType)"/>
																				</xsl:if>
																				<xsl:for-each select="ns1:value">
																					<xsl:variable name="var151_dataType" as="item()*" select="@dataType"/>
																					<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
																						<xsl:if test="fn:exists($var151_dataType)">
																							<xsl:attribute name="dataType" namespace="" select="fn:string($var151_dataType)"/>
																						</xsl:if>
																						<xsl:for-each select="node()[fn:boolean(self::text())]">
																							<xsl:sequence select="fn:string(.)"/>
																						</xsl:for-each>
																					</lgCommon:value>
																				</xsl:for-each>
																			</lgCommon:propertyQualifier>
																		</xsl:for-each>
																		<xsl:for-each select="ns1:value">
																			<xsl:variable name="var152_dataType" as="item()*" select="@dataType"/>
																			<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
																				<xsl:if test="fn:exists($var152_dataType)">
																					<xsl:attribute name="dataType" namespace="" select="fn:string($var152_dataType)"/>
																				</xsl:if>
																				<xsl:for-each select="node()[fn:boolean(self::text())]">
																					<xsl:sequence select="fn:string(.)"/>
																				</xsl:for-each>
																			</lgCommon:value>
																		</xsl:for-each>
																	</lgCon:presentation>
																</xsl:for-each>
																<xsl:for-each select="ns2:definition">
																	<xsl:variable name="var153_propertyId" as="item()*" select="@propertyId"/>
																	<xsl:variable name="var154_effectiveDate" as="item()*" select="@effectiveDate"/>
																	<xsl:variable name="var155_expirationDate" as="item()*" select="@expirationDate"/>
																	<xsl:variable name="var156_language" as="item()*" select="@language"/>
																	<xsl:variable name="var157_isPreferred" as="item()*" select="@isPreferred"/>
																	<xsl:variable name="var158_isActive" as="item()*" select="@isActive"/>
																	<xsl:variable name="var159_status" as="item()*" select="@status"/>
																	<xsl:variable name="var160_propertyType" as="item()*" select="@propertyType"/>
																	<lgCon:definition xsl:exclude-result-prefixes="lgCon">
																		<xsl:if test="fn:exists($var158_isActive)">
																			<xsl:attribute name="isActive" namespace="" select="xs:string(xs:boolean(fn:string($var158_isActive)))"/>
																		</xsl:if>
																		<xsl:if test="fn:exists($var159_status)">
																			<xsl:attribute name="status" namespace="" select="fn:string($var159_status)"/>
																		</xsl:if>
																		<xsl:if test="fn:exists($var154_effectiveDate)">
																			<xsl:attribute name="effectiveDate" namespace="" select="xs:string(xs:dateTime(fn:string($var154_effectiveDate)))"/>
																		</xsl:if>
																		<xsl:if test="fn:exists($var155_expirationDate)">
																			<xsl:attribute name="expirationDate" namespace="" select="xs:string(xs:dateTime(fn:string($var155_expirationDate)))"/>
																		</xsl:if>
																		<xsl:attribute name="propertyName" namespace="" select="fn:string(@propertyName)"/>
																		<xsl:if test="fn:exists($var153_propertyId)">
																			<xsl:attribute name="propertyId" namespace="" select="fn:string($var153_propertyId)"/>
																		</xsl:if>
																		<xsl:if test="fn:exists($var160_propertyType)">
																			<xsl:attribute name="propertyType" namespace="" select="fn:string($var160_propertyType)"/>
																		</xsl:if>
																		<xsl:if test="fn:exists($var156_language)">
																			<xsl:attribute name="language" namespace="" select="fn:string($var156_language)"/>
																		</xsl:if>
																		<xsl:if test="fn:exists($var157_isPreferred)">
																			<xsl:attribute name="isPreferred" namespace="" select="xs:string(xs:boolean(fn:string($var157_isPreferred)))"/>
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
																			<xsl:variable name="var161_propertyQualifierType" as="item()*" select="@propertyQualifierType"/>
																			<lgCommon:propertyQualifier xsl:exclude-result-prefixes="lgCommon">
																				<xsl:attribute name="propertyQualifierName" namespace="" select="fn:string(@propertyQualifierName)"/>
																				<xsl:if test="fn:exists($var161_propertyQualifierType)">
																					<xsl:attribute name="propertyQualifierType" namespace="" select="fn:string($var161_propertyQualifierType)"/>
																				</xsl:if>
																				<xsl:for-each select="ns1:value">
																					<xsl:variable name="var162_dataType" as="item()*" select="@dataType"/>
																					<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
																						<xsl:if test="fn:exists($var162_dataType)">
																							<xsl:attribute name="dataType" namespace="" select="fn:string($var162_dataType)"/>
																						</xsl:if>
																						<xsl:for-each select="node()[fn:boolean(self::text())]">
																							<xsl:sequence select="fn:string(.)"/>
																						</xsl:for-each>
																					</lgCommon:value>
																				</xsl:for-each>
																			</lgCommon:propertyQualifier>
																		</xsl:for-each>
																		<xsl:for-each select="ns1:value">
																			<xsl:variable name="var163_dataType" as="item()*" select="@dataType"/>
																			<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
																				<xsl:if test="fn:exists($var163_dataType)">
																					<xsl:attribute name="dataType" namespace="" select="fn:string($var163_dataType)"/>
																				</xsl:if>
																				<xsl:for-each select="node()[fn:boolean(self::text())]">
																					<xsl:sequence select="fn:string(.)"/>
																				</xsl:for-each>
																			</lgCommon:value>
																		</xsl:for-each>
																	</lgCon:definition>
																</xsl:for-each>
																<xsl:for-each select="ns2:comment">
																	<xsl:variable name="var164_propertyId" as="item()*" select="@propertyId"/>
																	<xsl:variable name="var165_status" as="item()*" select="@status"/>
																	<xsl:variable name="var166_propertyType" as="item()*" select="@propertyType"/>
																	<xsl:variable name="var167_expirationDate" as="item()*" select="@expirationDate"/>
																	<xsl:variable name="var168_isActive" as="item()*" select="@isActive"/>
																	<xsl:variable name="var169_language" as="item()*" select="@language"/>
																	<xsl:variable name="var170_effectiveDate" as="item()*" select="@effectiveDate"/>
																	<lgCon:comment xsl:exclude-result-prefixes="lgCon">
																		<xsl:if test="fn:exists($var168_isActive)">
																			<xsl:attribute name="isActive" namespace="" select="xs:string(xs:boolean(fn:string($var168_isActive)))"/>
																		</xsl:if>
																		<xsl:if test="fn:exists($var165_status)">
																			<xsl:attribute name="status" namespace="" select="fn:string($var165_status)"/>
																		</xsl:if>
																		<xsl:if test="fn:exists($var170_effectiveDate)">
																			<xsl:attribute name="effectiveDate" namespace="" select="xs:string(xs:dateTime(fn:string($var170_effectiveDate)))"/>
																		</xsl:if>
																		<xsl:if test="fn:exists($var167_expirationDate)">
																			<xsl:attribute name="expirationDate" namespace="" select="xs:string(xs:dateTime(fn:string($var167_expirationDate)))"/>
																		</xsl:if>
																		<xsl:attribute name="propertyName" namespace="" select="fn:string(@propertyName)"/>
																		<xsl:if test="fn:exists($var164_propertyId)">
																			<xsl:attribute name="propertyId" namespace="" select="fn:string($var164_propertyId)"/>
																		</xsl:if>
																		<xsl:if test="fn:exists($var166_propertyType)">
																			<xsl:attribute name="propertyType" namespace="" select="fn:string($var166_propertyType)"/>
																		</xsl:if>
																		<xsl:if test="fn:exists($var169_language)">
																			<xsl:attribute name="language" namespace="" select="fn:string($var169_language)"/>
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
																			<xsl:variable name="var171_propertyQualifierType" as="item()*" select="@propertyQualifierType"/>
																			<lgCommon:propertyQualifier xsl:exclude-result-prefixes="lgCommon">
																				<xsl:attribute name="propertyQualifierName" namespace="" select="fn:string(@propertyQualifierName)"/>
																				<xsl:if test="fn:exists($var171_propertyQualifierType)">
																					<xsl:attribute name="propertyQualifierType" namespace="" select="fn:string($var171_propertyQualifierType)"/>
																				</xsl:if>
																				<xsl:for-each select="ns1:value">
																					<xsl:variable name="var172_dataType" as="item()*" select="@dataType"/>
																					<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
																						<xsl:if test="fn:exists($var172_dataType)">
																							<xsl:attribute name="dataType" namespace="" select="fn:string($var172_dataType)"/>
																						</xsl:if>
																						<xsl:for-each select="node()[fn:boolean(self::text())]">
																							<xsl:sequence select="fn:string(.)"/>
																						</xsl:for-each>
																					</lgCommon:value>
																				</xsl:for-each>
																			</lgCommon:propertyQualifier>
																		</xsl:for-each>
																		<xsl:for-each select="ns1:value">
																			<xsl:variable name="var173_dataType" as="item()*" select="@dataType"/>
																			<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
																				<xsl:if test="fn:exists($var173_dataType)">
																					<xsl:attribute name="dataType" namespace="" select="fn:string($var173_dataType)"/>
																				</xsl:if>
																				<xsl:for-each select="node()[fn:boolean(self::text())]">
																					<xsl:sequence select="fn:string(.)"/>
																				</xsl:for-each>
																			</lgCommon:value>
																		</xsl:for-each>
																	</lgCon:comment>
																</xsl:for-each>
																<xsl:for-each select="ns2:property">
																	<xsl:variable name="var174_propertyId" as="item()*" select="@propertyId"/>
																	<xsl:variable name="var175_status" as="item()*" select="@status"/>
																	<xsl:variable name="var176_language" as="item()*" select="@language"/>
																	<xsl:variable name="var177_isActive" as="item()*" select="@isActive"/>
																	<xsl:variable name="var178_effectiveDate" as="item()*" select="@effectiveDate"/>
																	<xsl:variable name="var179_expirationDate" as="item()*" select="@expirationDate"/>
																	<xsl:variable name="var180_propertyType" as="item()*" select="@propertyType"/>
																	<lgCon:property xsl:exclude-result-prefixes="lgCon">
																		<xsl:if test="fn:exists($var177_isActive)">
																			<xsl:attribute name="isActive" namespace="" select="xs:string(xs:boolean(fn:string($var177_isActive)))"/>
																		</xsl:if>
																		<xsl:if test="fn:exists($var175_status)">
																			<xsl:attribute name="status" namespace="" select="fn:string($var175_status)"/>
																		</xsl:if>
																		<xsl:if test="fn:exists($var178_effectiveDate)">
																			<xsl:attribute name="effectiveDate" namespace="" select="xs:string(xs:dateTime(fn:string($var178_effectiveDate)))"/>
																		</xsl:if>
																		<xsl:if test="fn:exists($var179_expirationDate)">
																			<xsl:attribute name="expirationDate" namespace="" select="xs:string(xs:dateTime(fn:string($var179_expirationDate)))"/>
																		</xsl:if>
																		<xsl:attribute name="propertyName" namespace="" select="fn:string(@propertyName)"/>
																		<xsl:if test="fn:exists($var174_propertyId)">
																			<xsl:attribute name="propertyId" namespace="" select="fn:string($var174_propertyId)"/>
																		</xsl:if>
																		<xsl:if test="fn:exists($var180_propertyType)">
																			<xsl:attribute name="propertyType" namespace="" select="fn:string($var180_propertyType)"/>
																		</xsl:if>
																		<xsl:if test="fn:exists($var176_language)">
																			<xsl:attribute name="language" namespace="" select="fn:string($var176_language)"/>
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
																			<xsl:variable name="var181_propertyQualifierType" as="item()*" select="@propertyQualifierType"/>
																			<lgCommon:propertyQualifier xsl:exclude-result-prefixes="lgCommon">
																				<xsl:attribute name="propertyQualifierName" namespace="" select="fn:string(@propertyQualifierName)"/>
																				<xsl:if test="fn:exists($var181_propertyQualifierType)">
																					<xsl:attribute name="propertyQualifierType" namespace="" select="fn:string($var181_propertyQualifierType)"/>
																				</xsl:if>
																				<xsl:for-each select="ns1:value">
																					<xsl:variable name="var182_dataType" as="item()*" select="@dataType"/>
																					<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
																						<xsl:if test="fn:exists($var182_dataType)">
																							<xsl:attribute name="dataType" namespace="" select="fn:string($var182_dataType)"/>
																						</xsl:if>
																						<xsl:for-each select="node()[fn:boolean(self::text())]">
																							<xsl:sequence select="fn:string(.)"/>
																						</xsl:for-each>
																					</lgCommon:value>
																				</xsl:for-each>
																			</lgCommon:propertyQualifier>
																		</xsl:for-each>
																		<xsl:for-each select="ns1:value">
																			<xsl:variable name="var183_dataType" as="item()*" select="@dataType"/>
																			<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
																				<xsl:if test="fn:exists($var183_dataType)">
																					<xsl:attribute name="dataType" namespace="" select="fn:string($var183_dataType)"/>
																				</xsl:if>
																				<xsl:for-each select="node()[fn:boolean(self::text())]">
																					<xsl:sequence select="fn:string(.)"/>
																				</xsl:for-each>
																			</lgCommon:value>
																		</xsl:for-each>
																	</lgCon:property>
																</xsl:for-each>
																<xsl:for-each select="ns2:propertyLink">
																	<lgCon:propertyLink xsl:exclude-result-prefixes="lgCon">
																		<xsl:call-template name="tbf:tbf22_propertyLink">
																			<xsl:with-param name="input" select="." as="node()"/>
																		</xsl:call-template>
																	</lgCon:propertyLink>
																</xsl:for-each>
															</lgCon:entity>
														</xsl:for-each>
													</lgCS:entities>
												</xsl:for-each>
												<xsl:for-each select="ns0:relations">
													<xsl:variable name="var184_containerName" as="item()*" select="@containerName"/>
													<lgCS:relations xsl:exclude-result-prefixes="lgCS">
														<xsl:if test="fn:exists($var184_containerName)">
															<xsl:attribute name="containerName" namespace="" select="fn:string($var184_containerName)"/>
														</xsl:if>
														<xsl:for-each select="ns1:entityDescription">
															<lgCommon:entityDescription xsl:exclude-result-prefixes="lgCommon">
																<xsl:for-each select="node()[fn:boolean(self::text())]">
																	<xsl:sequence select="fn:string(.)"/>
																</xsl:for-each>
															</lgCommon:entityDescription>
														</xsl:for-each>
													</lgCS:relations>
												</xsl:for-each>
											</changedCodingSchemeEntry>
										</xsl:for-each>
										<xsl:for-each select="ns5:changedPickListDefinitionEntry">
											<xsl:variable name="var185_defaultEntityCodeNamespace" as="item()*" select="@defaultEntityCodeNamespace"/>
											<xsl:variable name="var186_defaultSortOrder" as="item()*" select="@defaultSortOrder"/>
											<xsl:variable name="var187_effectiveDate" as="item()*" select="@effectiveDate"/>
											<xsl:variable name="var188_expirationDate" as="item()*" select="@expirationDate"/>
											<xsl:variable name="var189_defaultLanguage" as="item()*" select="@defaultLanguage"/>
											<xsl:variable name="var190_isActive" as="item()*" select="@isActive"/>
											<xsl:variable name="var191_status" as="item()*" select="@status"/>
											<changedPickListDefinitionEntry>
												<xsl:if test="fn:exists($var190_isActive)">
													<xsl:attribute name="isActive" namespace="" select="xs:string(xs:boolean(fn:string($var190_isActive)))"/>
												</xsl:if>
												<xsl:if test="fn:exists($var191_status)">
													<xsl:attribute name="status" namespace="" select="fn:string($var191_status)"/>
												</xsl:if>
												<xsl:if test="fn:exists($var187_effectiveDate)">
													<xsl:attribute name="effectiveDate" namespace="" select="xs:string(xs:dateTime(fn:string($var187_effectiveDate)))"/>
												</xsl:if>
												<xsl:if test="fn:exists($var188_expirationDate)">
													<xsl:attribute name="expirationDate" namespace="" select="xs:string(xs:dateTime(fn:string($var188_expirationDate)))"/>
												</xsl:if>
												<xsl:attribute name="pickListId" namespace="" select="fn:string(@pickListId)"/>
												<xsl:if test="fn:exists($var185_defaultEntityCodeNamespace)">
													<xsl:attribute name="defaultEntityCodeNamespace" namespace="" select="fn:string($var185_defaultEntityCodeNamespace)"/>
												</xsl:if>
												<xsl:if test="fn:exists($var189_defaultLanguage)">
													<xsl:attribute name="defaultLanguage" namespace="" select="fn:string($var189_defaultLanguage)"/>
												</xsl:if>
												<xsl:if test="fn:exists($var186_defaultSortOrder)">
													<xsl:attribute name="defaultSortOrder" namespace="" select="fn:string($var186_defaultSortOrder)"/>
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
												<xsl:for-each select="ns4:mappings">
													<lgVD:mappings xsl:exclude-result-prefixes="lgVD">
														<xsl:for-each select="ns3:supportedAssociation">
															<xsl:variable name="var192_uri" as="item()*" select="@uri"/>
															<lgNaming:supportedAssociation xsl:exclude-result-prefixes="lgNaming">
																<xsl:attribute name="localId" namespace="" select="fn:string(@localId)"/>
																<xsl:if test="fn:exists($var192_uri)">
																	<xsl:attribute name="uri" namespace="" select="xs:string(xs:anyURI(fn:string($var192_uri)))"/>
																</xsl:if>
																<xsl:sequence select="fn:string(.)"/>
															</lgNaming:supportedAssociation>
														</xsl:for-each>
														<xsl:for-each select="ns3:supportedAssociationQualifier">
															<lgNaming:supportedAssociationQualifier xsl:exclude-result-prefixes="lgNaming">
																<xsl:call-template name="tbf:tbf3_supportedAssociationQualifier">
																	<xsl:with-param name="input" select="." as="node()"/>
																</xsl:call-template>
															</lgNaming:supportedAssociationQualifier>
														</xsl:for-each>
														<xsl:for-each select="ns3:supportedCodingScheme">
															<lgNaming:supportedCodingScheme xsl:exclude-result-prefixes="lgNaming">
																<xsl:call-template name="tbf:tbf4_supportedCodingScheme">
																	<xsl:with-param name="input" select="." as="node()"/>
																</xsl:call-template>
															</lgNaming:supportedCodingScheme>
														</xsl:for-each>
														<xsl:for-each select="ns3:supportedContext">
															<lgNaming:supportedContext xsl:exclude-result-prefixes="lgNaming">
																<xsl:call-template name="tbf:tbf6_supportedContext">
																	<xsl:with-param name="input" select="." as="node()"/>
																</xsl:call-template>
															</lgNaming:supportedContext>
														</xsl:for-each>
														<xsl:for-each select="ns3:supportedDataType">
															<lgNaming:supportedDataType xsl:exclude-result-prefixes="lgNaming">
																<xsl:call-template name="tbf:tbf7_supportedDataType">
																	<xsl:with-param name="input" select="." as="node()"/>
																</xsl:call-template>
															</lgNaming:supportedDataType>
														</xsl:for-each>
														<xsl:for-each select="ns3:supportedDegreeOfFidelity">
															<lgNaming:supportedDegreeOfFidelity xsl:exclude-result-prefixes="lgNaming">
																<xsl:call-template name="tbf:tbf8_supportedDegreeOfFidelity">
																	<xsl:with-param name="input" select="." as="node()"/>
																</xsl:call-template>
															</lgNaming:supportedDegreeOfFidelity>
														</xsl:for-each>
														<xsl:for-each select="ns3:supportedEntityType">
															<lgNaming:supportedEntityType xsl:exclude-result-prefixes="lgNaming">
																<xsl:call-template name="tbf:tbf9_supportedEntityType">
																	<xsl:with-param name="input" select="." as="node()"/>
																</xsl:call-template>
															</lgNaming:supportedEntityType>
														</xsl:for-each>
														<xsl:for-each select="ns3:supportedHierarchy">
															<lgNaming:supportedHierarchy xsl:exclude-result-prefixes="lgNaming">
																<xsl:call-template name="tbf:tbf10_supportedHierarchy">
																	<xsl:with-param name="input" select="." as="node()"/>
																</xsl:call-template>
															</lgNaming:supportedHierarchy>
														</xsl:for-each>
														<xsl:for-each select="ns3:supportedLanguage">
															<lgNaming:supportedLanguage xsl:exclude-result-prefixes="lgNaming">
																<xsl:call-template name="tbf:tbf11_supportedLanguage">
																	<xsl:with-param name="input" select="." as="node()"/>
																</xsl:call-template>
															</lgNaming:supportedLanguage>
														</xsl:for-each>
														<xsl:for-each select="ns3:supportedNamespace">
															<lgNaming:supportedNamespace xsl:exclude-result-prefixes="lgNaming">
																<xsl:call-template name="tbf:tbf12_supportedNamespace">
																	<xsl:with-param name="input" select="." as="node()"/>
																</xsl:call-template>
															</lgNaming:supportedNamespace>
														</xsl:for-each>
														<xsl:for-each select="ns3:supportedProperty">
															<xsl:variable name="var193_uri" as="item()*" select="@uri"/>
															<lgNaming:supportedProperty xsl:exclude-result-prefixes="lgNaming">
																<xsl:attribute name="localId" namespace="" select="fn:string(@localId)"/>
																<xsl:if test="fn:exists($var193_uri)">
																	<xsl:attribute name="uri" namespace="" select="xs:string(xs:anyURI(fn:string($var193_uri)))"/>
																</xsl:if>
																<xsl:sequence select="fn:string(.)"/>
															</lgNaming:supportedProperty>
														</xsl:for-each>
														<xsl:for-each select="ns3:supportedPropertyType">
															<lgNaming:supportedPropertyType xsl:exclude-result-prefixes="lgNaming">
																<xsl:call-template name="tbf:tbf13_supportedPropertyType">
																	<xsl:with-param name="input" select="." as="node()"/>
																</xsl:call-template>
															</lgNaming:supportedPropertyType>
														</xsl:for-each>
														<xsl:for-each select="ns3:supportedPropertyLink">
															<lgNaming:supportedPropertyLink xsl:exclude-result-prefixes="lgNaming">
																<xsl:call-template name="tbf:tbf14_supportedPropertyLink">
																	<xsl:with-param name="input" select="." as="node()"/>
																</xsl:call-template>
															</lgNaming:supportedPropertyLink>
														</xsl:for-each>
														<xsl:for-each select="ns3:supportedPropertyQualifier">
															<lgNaming:supportedPropertyQualifier xsl:exclude-result-prefixes="lgNaming">
																<xsl:call-template name="tbf:tbf15_supportedPropertyQualifier">
																	<xsl:with-param name="input" select="." as="node()"/>
																</xsl:call-template>
															</lgNaming:supportedPropertyQualifier>
														</xsl:for-each>
														<xsl:for-each select="ns3:supportedPropertyQualifierType">
															<lgNaming:supportedPropertyQualifierType xsl:exclude-result-prefixes="lgNaming">
																<xsl:call-template name="tbf:tbf16_supportedPropertyQualifierType">
																	<xsl:with-param name="input" select="." as="node()"/>
																</xsl:call-template>
															</lgNaming:supportedPropertyQualifierType>
														</xsl:for-each>
														<xsl:for-each select="ns3:supportedRepresentationalForm">
															<lgNaming:supportedRepresentationalForm xsl:exclude-result-prefixes="lgNaming">
																<xsl:call-template name="tbf:tbf17_supportedRepresentationalForm">
																	<xsl:with-param name="input" select="." as="node()"/>
																</xsl:call-template>
															</lgNaming:supportedRepresentationalForm>
														</xsl:for-each>
														<xsl:for-each select="ns3:supportedSortOrder">
															<lgNaming:supportedSortOrder xsl:exclude-result-prefixes="lgNaming">
																<xsl:call-template name="tbf:tbf18_supportedSortOrder">
																	<xsl:with-param name="input" select="." as="node()"/>
																</xsl:call-template>
															</lgNaming:supportedSortOrder>
														</xsl:for-each>
														<xsl:for-each select="ns3:supportedSource">
															<lgNaming:supportedSource xsl:exclude-result-prefixes="lgNaming">
																<xsl:call-template name="tbf:tbf19_supportedSource">
																	<xsl:with-param name="input" select="." as="node()"/>
																</xsl:call-template>
															</lgNaming:supportedSource>
														</xsl:for-each>
														<xsl:for-each select="ns3:supportedSourceRole">
															<lgNaming:supportedSourceRole xsl:exclude-result-prefixes="lgNaming">
																<xsl:call-template name="tbf:tbf20_supportedSourceRole">
																	<xsl:with-param name="input" select="." as="node()"/>
																</xsl:call-template>
															</lgNaming:supportedSourceRole>
														</xsl:for-each>
														<xsl:for-each select="ns3:supportedStatus">
															<lgNaming:supportedStatus xsl:exclude-result-prefixes="lgNaming">
																<xsl:call-template name="tbf:tbf21_supportedStatus">
																	<xsl:with-param name="input" select="." as="node()"/>
																</xsl:call-template>
															</lgNaming:supportedStatus>
														</xsl:for-each>
													</lgVD:mappings>
												</xsl:for-each>
												<xsl:for-each select="ns4:pickListEntryNode">
													<xsl:variable name="var194_status" as="item()*" select="@status"/>
													<xsl:variable name="var195_isActive" as="item()*" select="@isActive"/>
													<xsl:variable name="var196_effectiveDate" as="item()*" select="@effectiveDate"/>
													<xsl:variable name="var197_expirationDate" as="item()*" select="@expirationDate"/>
													<lgVD:pickListEntryNode xsl:exclude-result-prefixes="lgVD">
														<xsl:if test="fn:exists($var195_isActive)">
															<xsl:attribute name="isActive" namespace="" select="xs:string(xs:boolean(fn:string($var195_isActive)))"/>
														</xsl:if>
														<xsl:if test="fn:exists($var194_status)">
															<xsl:attribute name="status" namespace="" select="fn:string($var194_status)"/>
														</xsl:if>
														<xsl:if test="fn:exists($var196_effectiveDate)">
															<xsl:attribute name="effectiveDate" namespace="" select="xs:string(xs:dateTime(fn:string($var196_effectiveDate)))"/>
														</xsl:if>
														<xsl:if test="fn:exists($var197_expirationDate)">
															<xsl:attribute name="expirationDate" namespace="" select="xs:string(xs:dateTime(fn:string($var197_expirationDate)))"/>
														</xsl:if>
														<xsl:attribute name="pickListEntryId" namespace="" select="fn:string(@pickListEntryId)"/>
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
														<xsl:for-each select="ns4:exclusionEntry">
															<lgVD:exclusionEntry xsl:exclude-result-prefixes="lgVD">
																<xsl:call-template name="tbf:tbf26_pickListEntryExclusion">
																	<xsl:with-param name="input" select="." as="node()"/>
																</xsl:call-template>
															</lgVD:exclusionEntry>
														</xsl:for-each>
														<xsl:for-each select="ns4:inclusionEntry">
															<xsl:variable name="var198_language" as="item()*" select="@language"/>
															<xsl:variable name="var199_entityCodeNamespace" as="item()*" select="@entityCodeNamespace"/>
															<xsl:variable name="var200_isDefault" as="item()*" select="@isDefault"/>
															<xsl:variable name="var201_matchIfNoContext" as="item()*" select="@matchIfNoContext"/>
															<xsl:variable name="var202_entryOrder" as="item()*" select="@entryOrder"/>
															<xsl:variable name="var203_propertyId" as="item()*" select="@propertyId"/>
															<lgVD:inclusionEntry xsl:exclude-result-prefixes="lgVD">
																<xsl:if test="fn:exists($var202_entryOrder)">
																	<xsl:attribute name="entryOrder" namespace="" select="xs:string(xs:integer(fn:string($var202_entryOrder)))"/>
																</xsl:if>
																<xsl:attribute name="entityCode" namespace="" select="fn:string(@entityCode)"/>
																<xsl:if test="fn:exists($var199_entityCodeNamespace)">
																	<xsl:attribute name="entityCodeNamespace" namespace="" select="fn:string($var199_entityCodeNamespace)"/>
																</xsl:if>
																<xsl:if test="fn:exists($var203_propertyId)">
																	<xsl:attribute name="propertyId" namespace="" select="fn:string($var203_propertyId)"/>
																</xsl:if>
																<xsl:if test="fn:exists($var200_isDefault)">
																	<xsl:attribute name="isDefault" namespace="" select="xs:string(xs:boolean(fn:string($var200_isDefault)))"/>
																</xsl:if>
																<xsl:if test="fn:exists($var201_matchIfNoContext)">
																	<xsl:attribute name="matchIfNoContext" namespace="" select="xs:string(xs:boolean(fn:string($var201_matchIfNoContext)))"/>
																</xsl:if>
																<xsl:if test="fn:exists($var198_language)">
																	<xsl:attribute name="language" namespace="" select="fn:string($var198_language)"/>
																</xsl:if>
																<lgVD:pickText xsl:exclude-result-prefixes="lgVD">
																	<xsl:sequence select="fn:string(ns4:pickText)"/>
																</lgVD:pickText>
																<xsl:for-each select="ns4:pickContext">
																	<lgVD:pickContext xsl:exclude-result-prefixes="lgVD">
																		<xsl:sequence select="fn:string(.)"/>
																	</lgVD:pickContext>
																</xsl:for-each>
															</lgVD:inclusionEntry>
														</xsl:for-each>
														<xsl:for-each select="ns4:properties">
															<lgVD:properties xsl:exclude-result-prefixes="lgVD">
																<xsl:for-each select="ns1:property">
																	<xsl:variable name="var204_effectiveDate" as="item()*" select="@effectiveDate"/>
																	<xsl:variable name="var205_propertyType" as="item()*" select="@propertyType"/>
																	<xsl:variable name="var206_propertyId" as="item()*" select="@propertyId"/>
																	<xsl:variable name="var207_status" as="item()*" select="@status"/>
																	<xsl:variable name="var208_expirationDate" as="item()*" select="@expirationDate"/>
																	<xsl:variable name="var209_isActive" as="item()*" select="@isActive"/>
																	<xsl:variable name="var210_language" as="item()*" select="@language"/>
																	<lgCommon:property xsl:exclude-result-prefixes="lgCommon">
																		<xsl:if test="fn:exists($var209_isActive)">
																			<xsl:attribute name="isActive" namespace="" select="xs:string(xs:boolean(fn:string($var209_isActive)))"/>
																		</xsl:if>
																		<xsl:if test="fn:exists($var207_status)">
																			<xsl:attribute name="status" namespace="" select="fn:string($var207_status)"/>
																		</xsl:if>
																		<xsl:if test="fn:exists($var204_effectiveDate)">
																			<xsl:attribute name="effectiveDate" namespace="" select="xs:string(xs:dateTime(fn:string($var204_effectiveDate)))"/>
																		</xsl:if>
																		<xsl:if test="fn:exists($var208_expirationDate)">
																			<xsl:attribute name="expirationDate" namespace="" select="xs:string(xs:dateTime(fn:string($var208_expirationDate)))"/>
																		</xsl:if>
																		<xsl:attribute name="propertyName" namespace="" select="fn:string(@propertyName)"/>
																		<xsl:if test="fn:exists($var206_propertyId)">
																			<xsl:attribute name="propertyId" namespace="" select="fn:string($var206_propertyId)"/>
																		</xsl:if>
																		<xsl:if test="fn:exists($var205_propertyType)">
																			<xsl:attribute name="propertyType" namespace="" select="fn:string($var205_propertyType)"/>
																		</xsl:if>
																		<xsl:if test="fn:exists($var210_language)">
																			<xsl:attribute name="language" namespace="" select="fn:string($var210_language)"/>
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
																			<xsl:variable name="var211_propertyQualifierType" as="item()*" select="@propertyQualifierType"/>
																			<lgCommon:propertyQualifier xsl:exclude-result-prefixes="lgCommon">
																				<xsl:attribute name="propertyQualifierName" namespace="" select="fn:string(@propertyQualifierName)"/>
																				<xsl:if test="fn:exists($var211_propertyQualifierType)">
																					<xsl:attribute name="propertyQualifierType" namespace="" select="fn:string($var211_propertyQualifierType)"/>
																				</xsl:if>
																				<xsl:for-each select="ns1:value">
																					<xsl:variable name="var212_dataType" as="item()*" select="@dataType"/>
																					<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
																						<xsl:if test="fn:exists($var212_dataType)">
																							<xsl:attribute name="dataType" namespace="" select="fn:string($var212_dataType)"/>
																						</xsl:if>
																						<xsl:for-each select="node()[fn:boolean(self::text())]">
																							<xsl:sequence select="fn:string(.)"/>
																						</xsl:for-each>
																					</lgCommon:value>
																				</xsl:for-each>
																			</lgCommon:propertyQualifier>
																		</xsl:for-each>
																		<xsl:for-each select="ns1:value">
																			<xsl:variable name="var213_dataType" as="item()*" select="@dataType"/>
																			<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
																				<xsl:if test="fn:exists($var213_dataType)">
																					<xsl:attribute name="dataType" namespace="" select="fn:string($var213_dataType)"/>
																				</xsl:if>
																				<xsl:for-each select="node()[fn:boolean(self::text())]">
																					<xsl:sequence select="fn:string(.)"/>
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
															<xsl:with-param name="input" select="." as="node()"/>
														</xsl:call-template>
													</lgVD:source>
												</xsl:for-each>
												<xsl:for-each select="ns4:defaultPickContext">
													<lgVD:defaultPickContext xsl:exclude-result-prefixes="lgVD">
														<xsl:sequence select="fn:string(.)"/>
													</lgVD:defaultPickContext>
												</xsl:for-each>
												<xsl:for-each select="ns4:properties">
													<lgVD:properties xsl:exclude-result-prefixes="lgVD">
														<xsl:for-each select="ns1:property">
															<xsl:variable name="var214_language" as="item()*" select="@language"/>
															<xsl:variable name="var215_effectiveDate" as="item()*" select="@effectiveDate"/>
															<xsl:variable name="var216_expirationDate" as="item()*" select="@expirationDate"/>
															<xsl:variable name="var217_propertyType" as="item()*" select="@propertyType"/>
															<xsl:variable name="var218_propertyId" as="item()*" select="@propertyId"/>
															<xsl:variable name="var219_status" as="item()*" select="@status"/>
															<xsl:variable name="var220_isActive" as="item()*" select="@isActive"/>
															<lgCommon:property xsl:exclude-result-prefixes="lgCommon">
																<xsl:if test="fn:exists($var220_isActive)">
																	<xsl:attribute name="isActive" namespace="" select="xs:string(xs:boolean(fn:string($var220_isActive)))"/>
																</xsl:if>
																<xsl:if test="fn:exists($var219_status)">
																	<xsl:attribute name="status" namespace="" select="fn:string($var219_status)"/>
																</xsl:if>
																<xsl:if test="fn:exists($var215_effectiveDate)">
																	<xsl:attribute name="effectiveDate" namespace="" select="xs:string(xs:dateTime(fn:string($var215_effectiveDate)))"/>
																</xsl:if>
																<xsl:if test="fn:exists($var216_expirationDate)">
																	<xsl:attribute name="expirationDate" namespace="" select="xs:string(xs:dateTime(fn:string($var216_expirationDate)))"/>
																</xsl:if>
																<xsl:attribute name="propertyName" namespace="" select="fn:string(@propertyName)"/>
																<xsl:if test="fn:exists($var218_propertyId)">
																	<xsl:attribute name="propertyId" namespace="" select="fn:string($var218_propertyId)"/>
																</xsl:if>
																<xsl:if test="fn:exists($var217_propertyType)">
																	<xsl:attribute name="propertyType" namespace="" select="fn:string($var217_propertyType)"/>
																</xsl:if>
																<xsl:if test="fn:exists($var214_language)">
																	<xsl:attribute name="language" namespace="" select="fn:string($var214_language)"/>
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
																	<xsl:variable name="var221_propertyQualifierType" as="item()*" select="@propertyQualifierType"/>
																	<lgCommon:propertyQualifier xsl:exclude-result-prefixes="lgCommon">
																		<xsl:attribute name="propertyQualifierName" namespace="" select="fn:string(@propertyQualifierName)"/>
																		<xsl:if test="fn:exists($var221_propertyQualifierType)">
																			<xsl:attribute name="propertyQualifierType" namespace="" select="fn:string($var221_propertyQualifierType)"/>
																		</xsl:if>
																		<xsl:for-each select="ns1:value">
																			<xsl:variable name="var222_dataType" as="item()*" select="@dataType"/>
																			<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
																				<xsl:if test="fn:exists($var222_dataType)">
																					<xsl:attribute name="dataType" namespace="" select="fn:string($var222_dataType)"/>
																				</xsl:if>
																				<xsl:for-each select="node()[fn:boolean(self::text())]">
																					<xsl:sequence select="fn:string(.)"/>
																				</xsl:for-each>
																			</lgCommon:value>
																		</xsl:for-each>
																	</lgCommon:propertyQualifier>
																</xsl:for-each>
																<xsl:for-each select="ns1:value">
																	<xsl:variable name="var223_dataType" as="item()*" select="@dataType"/>
																	<lgCommon:value xsl:exclude-result-prefixes="lgCommon">
																		<xsl:if test="fn:exists($var223_dataType)">
																			<xsl:attribute name="dataType" namespace="" select="fn:string($var223_dataType)"/>
																		</xsl:if>
																		<xsl:for-each select="node()[fn:boolean(self::text())]">
																			<xsl:sequence select="fn:string(.)"/>
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
