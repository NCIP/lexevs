<?xml version="1.0" encoding="UTF-8"?>
<!--
Transforms documents defined against LexGrid model revision 2009/01
to LexGrid model revision 2008/01.
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:lgBuiltin="http://LexGrid.org/schema/2008/01/LexGrid/builtins" xmlns:n="http://LexGrid.org/schema/2008/01/LexGrid/codingSchemes" xmlns:lgCommon="http://LexGrid.org/schema/2008/01/LexGrid/commonTypes" xmlns:lgCon="http://LexGrid.org/schema/2008/01/LexGrid/concepts" xmlns:ldap="http://LexGrid.org/schema/2008/01/LexGrid/ldap" xmlns:lgNaming="http://LexGrid.org/schema/2008/01/LexGrid/naming" xmlns:lgRel="http://LexGrid.org/schema/2008/01/LexGrid/relations" xmlns:lgVer="http://LexGrid.org/schema/2008/01/LexGrid/versions" xmlns:lgBuiltin2="http://LexGrid.org/schema/2009/01/LexGrid/builtins" xmlns:n2="http://LexGrid.org/schema/2009/01/LexGrid/codingSchemes" xmlns:lgCommon2="http://LexGrid.org/schema/2009/01/LexGrid/commonTypes" xmlns:lgCon2="http://LexGrid.org/schema/2009/01/LexGrid/concepts" xmlns:lgNaming2="http://LexGrid.org/schema/2009/01/LexGrid/naming" xmlns:lgRel2="http://LexGrid.org/schema/2009/01/LexGrid/relations" xmlns:lgVD="http://LexGrid.org/schema/2009/01/LexGrid/valueDomains" xmlns:lgVer2="http://LexGrid.org/schema/2009/01/LexGrid/versions" xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="lgBuiltin2 lgCommon2 lgCon2 lgNaming2 lgRel2 lgVD lgVer2 n2 xs xsi xsl" xmlns="http://LexGrid.org/schema/2008/01/LexGrid/codingSchemes">
	<xsl:namespace-alias stylesheet-prefix="n" result-prefix="#default"/>
	<xsl:output method="xml" encoding="UTF-8" indent="yes"/>
	<xsl:template match="/">
		<n:codingScheme>
			<xsl:attribute name="xsi:schemaLocation">
				<xsl:value-of select="'http://LexGrid.org/schema/2008/01/LexGrid/codingSchemes http://LexGrid.org/schema/2008/01/LexGrid/codingSchemes.xsd'"/>
			</xsl:attribute>
			<xsl:variable name="var1_instance" select="."/>
			<xsl:for-each select="$var1_instance/n2:codingScheme">
				<xsl:variable name="var2_codingScheme" select="."/>
				<xsl:attribute name="codingScheme">
					<xsl:value-of select="string(@codingSchemeName)"/>
				</xsl:attribute>
				<xsl:if test="$var2_codingScheme/@formalName">
					<xsl:attribute name="formalName">
						<xsl:value-of select="string(@formalName)"/>
					</xsl:attribute>
				</xsl:if>
				<xsl:attribute name="registeredName">
					<xsl:value-of select="string(@codingSchemeURI)"/>
				</xsl:attribute>
				<xsl:if test="$var2_codingScheme/@defaultLanguage">
					<xsl:attribute name="defaultLanguage">
						<xsl:value-of select="string(@defaultLanguage)"/>
					</xsl:attribute>
				</xsl:if>
				<xsl:if test="$var2_codingScheme/@representsVersion">
					<xsl:attribute name="representsVersion">
						<xsl:value-of select="string(@representsVersion)"/>
					</xsl:attribute>
				</xsl:if>
				<xsl:if test="$var2_codingScheme/@approxNumConcepts">
					<xsl:attribute name="approxNumConcepts">
						<xsl:value-of select="number(string(@approxNumConcepts))"/>
					</xsl:attribute>
				</xsl:if>
				<xsl:for-each select="lgCommon2:entityDescription">
					<lgCommon:entityDescription>
						<xsl:for-each select="node()">
							<xsl:if test="self::text()">
								<xsl:copy-of select="."/>
							</xsl:if>
						</xsl:for-each>
					</lgCommon:entityDescription>
				</xsl:for-each>
				<xsl:for-each select="n2:localName">
					<n:localName>
						<xsl:value-of select="string(.)"/>
					</n:localName>
				</xsl:for-each>
				<xsl:for-each select="n2:source">
					<xsl:variable name="var10_source" select="."/>
					<n:source>
						<xsl:if test="$var10_source/@subRef">
							<xsl:attribute name="subRef">
								<xsl:value-of select="string(@subRef)"/>
							</xsl:attribute>
						</xsl:if>
						<xsl:if test="$var10_source/@role">
							<xsl:attribute name="role">
								<xsl:value-of select="string(@role)"/>
							</xsl:attribute>
						</xsl:if>
						<xsl:value-of select="string(.)"/>
					</n:source>
				</xsl:for-each>
				<xsl:for-each select="n2:copyright">
					<n:copyright>
						<xsl:for-each select="node()">
							<xsl:if test="self::text()">
								<xsl:copy-of select="."/>
							</xsl:if>
						</xsl:for-each>
					</n:copyright>
				</xsl:for-each>
				<n:mappings>
					<xsl:attribute name="dc">
						<xsl:value-of select="'mappings'"/>
					</xsl:attribute>
					<xsl:for-each select="n2:mappings/lgNaming2:supportedLanguage">
						<xsl:variable name="var16_supportedLanguage" select="."/>
						<n:supportedLanguage>
							<xsl:attribute name="localId">
								<xsl:value-of select="string(@localId)"/>
							</xsl:attribute>
							<xsl:if test="$var16_supportedLanguage/@uri">
								<xsl:attribute name="urn">
									<xsl:value-of select="string(@uri)"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:value-of select="string(.)"/>
						</n:supportedLanguage>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/lgNaming2:supportedDataType">
						<xsl:variable name="var18_supportedDataType" select="."/>
						<n:supportedFormat>
							<xsl:attribute name="localId">
								<xsl:value-of select="string(@localId)"/>
							</xsl:attribute>
							<xsl:if test="$var18_supportedDataType/@uri">
								<xsl:attribute name="urn">
									<xsl:value-of select="string(@uri)"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:value-of select="string(.)"/>
						</n:supportedFormat>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/lgNaming2:supportedProperty">
						<xsl:variable name="var20_supportedProperty" select="."/>
						<n:supportedProperty>
							<xsl:attribute name="localId">
								<xsl:value-of select="string(@localId)"/>
							</xsl:attribute>
							<xsl:if test="$var20_supportedProperty/@uri">
								<xsl:attribute name="urn">
									<xsl:value-of select="string(@uri)"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:value-of select="string(.)"/>
						</n:supportedProperty>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/lgNaming2:supportedCodingScheme">
						<xsl:variable name="var22_supportedCodingScheme" select="."/>
						<n:supportedCodingScheme>
							<xsl:attribute name="localId">
								<xsl:value-of select="string(@localId)"/>
							</xsl:attribute>
							<xsl:if test="$var22_supportedCodingScheme/@uri">
								<xsl:attribute name="urn">
									<xsl:value-of select="string(@uri)"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:if test="$var22_supportedCodingScheme/@isImported">
								<xsl:attribute name="isImported">
									<xsl:value-of select="(('0' != @isImported) and ('false' != @isImported))"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:value-of select="string(.)"/>
						</n:supportedCodingScheme>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/lgNaming2:supportedSource">
						<xsl:variable name="var24_supportedSource" select="."/>
						<n:supportedSource>
							<xsl:attribute name="localId">
								<xsl:value-of select="string(@localId)"/>
							</xsl:attribute>
							<xsl:if test="$var24_supportedSource/@uri">
								<xsl:attribute name="urn">
									<xsl:value-of select="string(@uri)"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:if test="$var24_supportedSource/@assemblyRule">
								<xsl:attribute name="assemblyRule">
									<xsl:value-of select="string(@assemblyRule)"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:value-of select="string(.)"/>
						</n:supportedSource>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/lgNaming2:supportedAssociation">
						<xsl:variable name="var26_supportedAssociation" select="."/>
						<n:supportedAssociation>
							<xsl:attribute name="localId">
								<xsl:value-of select="string(@localId)"/>
							</xsl:attribute>
							<xsl:if test="$var26_supportedAssociation/@uri">
								<xsl:attribute name="urn">
									<xsl:value-of select="string(@uri)"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:value-of select="string(.)"/>
						</n:supportedAssociation>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/lgNaming2:supportedContext">
						<xsl:variable name="var28_supportedContext" select="."/>
						<n:supportedContext>
							<xsl:attribute name="localId">
								<xsl:value-of select="string(@localId)"/>
							</xsl:attribute>
							<xsl:if test="$var28_supportedContext/@uri">
								<xsl:attribute name="urn">
									<xsl:value-of select="string(@uri)"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:value-of select="string(.)"/>
						</n:supportedContext>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/lgNaming2:supportedAssociationQualifier">
						<xsl:variable name="var30_supportedAssociationQualifier" select="."/>
						<n:supportedAssociationQualifier>
							<xsl:attribute name="localId">
								<xsl:value-of select="string(@localId)"/>
							</xsl:attribute>
							<xsl:if test="$var30_supportedAssociationQualifier/@uri">
								<xsl:attribute name="urn">
									<xsl:value-of select="string(@uri)"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:value-of select="string(.)"/>
						</n:supportedAssociationQualifier>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/lgNaming2:supportedStatus">
						<xsl:variable name="var32_supportedStatus" select="."/>
						<n:supportedConceptStatus>
							<xsl:attribute name="localId">
								<xsl:value-of select="string(@localId)"/>
							</xsl:attribute>
							<xsl:if test="$var32_supportedStatus/@uri">
								<xsl:attribute name="urn">
									<xsl:value-of select="string(@uri)"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:value-of select="string(.)"/>
						</n:supportedConceptStatus>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/lgNaming2:supportedRepresentationalForm">
						<xsl:variable name="var34_supportedRepresentationalForm" select="."/>
						<n:supportedRepresentationalForm>
							<xsl:attribute name="localId">
								<xsl:value-of select="string(@localId)"/>
							</xsl:attribute>
							<xsl:if test="$var34_supportedRepresentationalForm/@uri">
								<xsl:attribute name="urn">
									<xsl:value-of select="string(@uri)"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:value-of select="string(.)"/>
						</n:supportedRepresentationalForm>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/lgNaming2:supportedPropertyLink">
						<xsl:variable name="var36_supportedPropertyLink" select="."/>
						<n:supportedPropertyLink>
							<xsl:attribute name="localId">
								<xsl:value-of select="string(@localId)"/>
							</xsl:attribute>
							<xsl:if test="$var36_supportedPropertyLink/@uri">
								<xsl:attribute name="urn">
									<xsl:value-of select="string(@uri)"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:value-of select="string(.)"/>
						</n:supportedPropertyLink>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/lgNaming2:supportedDegreeOfFidelity">
						<xsl:variable name="var38_supportedDegreeOfFidelity" select="."/>
						<n:supportedDegreeOfFidelity>
							<xsl:attribute name="localId">
								<xsl:value-of select="string(@localId)"/>
							</xsl:attribute>
							<xsl:if test="$var38_supportedDegreeOfFidelity/@uri">
								<xsl:attribute name="urn">
									<xsl:value-of select="string(@uri)"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:value-of select="string(.)"/>
						</n:supportedDegreeOfFidelity>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/lgNaming2:supportedPropertyQualifier">
						<xsl:variable name="var40_supportedPropertyQualifier" select="."/>
						<n:supportedPropertyQualifier>
							<xsl:attribute name="localId">
								<xsl:value-of select="string(@localId)"/>
							</xsl:attribute>
							<xsl:if test="$var40_supportedPropertyQualifier/@uri">
								<xsl:attribute name="urn">
									<xsl:value-of select="string(@uri)"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:value-of select="string(.)"/>
						</n:supportedPropertyQualifier>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/lgNaming2:supportedHierarchy">
						<xsl:variable name="var42_supportedHierarchy" select="."/>
						<n:supportedHierarchy>
							<xsl:attribute name="localId">
								<xsl:value-of select="string(@localId)"/>
							</xsl:attribute>
							<xsl:if test="$var42_supportedHierarchy/@uri">
								<xsl:attribute name="urn">
									<xsl:value-of select="string(@uri)"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:attribute name="associationIds">
								<xsl:value-of select="string(@associationNames)"/>
							</xsl:attribute>
							<xsl:attribute name="rootCode">
								<xsl:value-of select="string(@rootCode)"/>
							</xsl:attribute>
							<xsl:attribute name="isForwardNavigable">
								<xsl:value-of select="(('0' != @isForwardNavigable) and ('false' != @isForwardNavigable))"/>
							</xsl:attribute>
							<xsl:value-of select="string(.)"/>
						</n:supportedHierarchy>
					</xsl:for-each>
				</n:mappings>
				<xsl:for-each select="n2:properties">
					<n:properties>
						<xsl:attribute name="dc">
							<xsl:value-of select="'properties'"/>
						</xsl:attribute>
						<xsl:for-each select="lgCommon2:property">
							<xsl:variable name="var46_property" select="."/>
							<lgCommon:property>
								<xsl:attribute name="propertyName">
									<xsl:value-of select="string(@propertyName)"/>
								</xsl:attribute>
								<xsl:if test="$var46_property/@propertyId">
									<xsl:attribute name="propertyId">
										<xsl:value-of select="string(@propertyId)"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var46_property/@language">
									<xsl:attribute name="language">
										<xsl:value-of select="string(@language)"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var46_property/@propertyType">
									<xsl:attribute name="format">
										<xsl:value-of select="string(@propertyType)"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:for-each select="lgCommon2:source">
									<xsl:variable name="var48_source" select="."/>
									<lgCommon:source>
										<xsl:if test="$var48_source/@subRef">
											<xsl:attribute name="subRef">
												<xsl:value-of select="string(@subRef)"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:if test="$var48_source/@role">
											<xsl:attribute name="role">
												<xsl:value-of select="string(@role)"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:value-of select="string(.)"/>
									</lgCommon:source>
								</xsl:for-each>
								<xsl:for-each select="lgCommon2:usageContext">
									<lgCommon:usageContext>
										<xsl:value-of select="string(.)"/>
									</lgCommon:usageContext>
								</xsl:for-each>
								<xsl:for-each select="lgCommon2:propertyQualifier">
									<lgCommon:propertyQualifier>
										<xsl:attribute name="propertyQualifierId">
											<xsl:value-of select="string(@propertyQualifierName)"/>
										</xsl:attribute>
									</lgCommon:propertyQualifier>
								</xsl:for-each>
								<xsl:for-each select="lgCommon2:value">
									<lgCommon:text>
										<xsl:for-each select="node()">
											<xsl:if test="self::text()">
												<xsl:copy-of select="."/>
											</xsl:if>
										</xsl:for-each>
									</lgCommon:text>
								</xsl:for-each>
							</lgCommon:property>
						</xsl:for-each>
					</n:properties>
				</xsl:for-each>
				<xsl:for-each select="n2:entities">
					<n:concepts>
						<xsl:attribute name="dc">
							<xsl:value-of select="'concepts'"/>
						</xsl:attribute>
						<xsl:for-each select="lgCon2:entity">
							<xsl:variable name="var60_entity" select="."/>
							<lgCon:concept>
								<xsl:attribute name="id">
									<xsl:value-of select="string(@entityCode)"/>
								</xsl:attribute>
								<xsl:if test="$var60_entity/@entityCodeNamespace">
									<xsl:attribute name="codingSchemeId">
										<xsl:value-of select="string(@entityCodeNamespace)"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var60_entity/@isActive">
									<xsl:attribute name="isActive">
										<xsl:value-of select="(('0' != @isActive) and ('false' != @isActive))"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var60_entity/@status">
									<xsl:attribute name="conceptStatus">
										<xsl:value-of select="string(@status)"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var60_entity/@isAnonymous">
									<xsl:attribute name="isAnonymous">
										<xsl:value-of select="(('0' != @isAnonymous) and ('false' != @isAnonymous))"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var60_entity/@isDefined">
									<xsl:attribute name="isDefined">
										<xsl:value-of select="(('0' != @isDefined) and ('false' != @isDefined))"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:for-each select="lgCommon2:entityDescription">
									<lgCommon:entityDescription>
										<xsl:for-each select="node()">
											<xsl:if test="self::text()">
												<xsl:copy-of select="."/>
											</xsl:if>
										</xsl:for-each>
									</lgCommon:entityDescription>
								</xsl:for-each>
								<xsl:for-each select="lgCon2:presentation">
									<xsl:variable name="var66_presentation" select="."/>
									<lgCon:presentation>
										<xsl:attribute name="propertyName">
											<xsl:value-of select="string(@propertyName)"/>
										</xsl:attribute>
										<xsl:if test="$var66_presentation/@propertyId">
											<xsl:attribute name="propertyId">
												<xsl:value-of select="string(@propertyId)"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:if test="$var66_presentation/@language">
											<xsl:attribute name="language">
												<xsl:value-of select="string(@language)"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:for-each select="lgCommon2:value">
											<xsl:variable name="var68_value" select="."/>
											<xsl:if test="$var68_value/@dataType">
												<xsl:attribute name="format">
													<xsl:value-of select="string(@dataType)"/>
												</xsl:attribute>
											</xsl:if>
										</xsl:for-each>
										<xsl:if test="$var66_presentation/@isPreferred">
											<xsl:attribute name="isPreferred">
												<xsl:value-of select="(('0' != @isPreferred) and ('false' != @isPreferred))"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:if test="$var66_presentation/@degreeOfFidelity">
											<xsl:attribute name="degreeOfFidelity">
												<xsl:value-of select="string(@degreeOfFidelity)"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:if test="$var66_presentation/@matchIfNoContext">
											<xsl:attribute name="matchIfNoContext">
												<xsl:value-of select="(('0' != @matchIfNoContext) and ('false' != @matchIfNoContext))"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:if test="$var66_presentation/@representationalForm">
											<xsl:attribute name="representationalForm">
												<xsl:value-of select="string(@representationalForm)"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:for-each select="lgCommon2:source">
											<xsl:variable name="var70_source" select="."/>
											<lgCommon:source>
												<xsl:if test="$var70_source/@subRef">
													<xsl:attribute name="subRef">
														<xsl:value-of select="string(@subRef)"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:if test="$var70_source/@role">
													<xsl:attribute name="role">
														<xsl:value-of select="string(@role)"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:value-of select="string(.)"/>
											</lgCommon:source>
										</xsl:for-each>
										<xsl:for-each select="lgCommon2:usageContext">
											<lgCommon:usageContext>
												<xsl:value-of select="string(.)"/>
											</lgCommon:usageContext>
										</xsl:for-each>
										<xsl:for-each select="lgCommon2:propertyQualifier">
											<lgCommon:propertyQualifier>
												<xsl:attribute name="propertyQualifierId">
													<xsl:value-of select="string(@propertyQualifierName)"/>
												</xsl:attribute>
											</lgCommon:propertyQualifier>
										</xsl:for-each>
										<xsl:for-each select="lgCommon2:value">
											<lgCommon:text>
												<xsl:for-each select="node()">
													<xsl:if test="self::text()">
														<xsl:copy-of select="."/>
													</xsl:if>
												</xsl:for-each>
											</lgCommon:text>
										</xsl:for-each>
									</lgCon:presentation>
								</xsl:for-each>
								<xsl:for-each select="lgCon2:definition">
									<xsl:variable name="var80_definition" select="."/>
									<lgCon:definition>
										<xsl:attribute name="propertyName">
											<xsl:value-of select="string(@propertyName)"/>
										</xsl:attribute>
										<xsl:if test="$var80_definition/@propertyId">
											<xsl:attribute name="propertyId">
												<xsl:value-of select="string(@propertyId)"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:if test="$var80_definition/@language">
											<xsl:attribute name="language">
												<xsl:value-of select="string(@language)"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:for-each select="lgCommon2:value">
											<xsl:variable name="var82_value" select="."/>
											<xsl:if test="$var82_value/@dataType">
												<xsl:attribute name="format">
													<xsl:value-of select="string(@dataType)"/>
												</xsl:attribute>
											</xsl:if>
										</xsl:for-each>
										<xsl:if test="$var80_definition/@isPreferred">
											<xsl:attribute name="isPreferred">
												<xsl:value-of select="(('0' != @isPreferred) and ('false' != @isPreferred))"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:for-each select="lgCommon2:source">
											<xsl:variable name="var84_source" select="."/>
											<lgCommon:source>
												<xsl:if test="$var84_source/@subRef">
													<xsl:attribute name="subRef">
														<xsl:value-of select="string(@subRef)"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:if test="$var84_source/@role">
													<xsl:attribute name="role">
														<xsl:value-of select="string(@role)"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:value-of select="string(.)"/>
											</lgCommon:source>
										</xsl:for-each>
										<xsl:for-each select="lgCommon2:usageContext">
											<lgCommon:usageContext>
												<xsl:value-of select="string(.)"/>
											</lgCommon:usageContext>
										</xsl:for-each>
										<xsl:for-each select="lgCommon2:propertyQualifier">
											<lgCommon:propertyQualifier>
												<xsl:attribute name="propertyQualifierId">
													<xsl:value-of select="string(@propertyQualifierName)"/>
												</xsl:attribute>
											</lgCommon:propertyQualifier>
										</xsl:for-each>
										<xsl:for-each select="lgCommon2:propertyQualifier/lgCommon2:value">
											<lgCommon:text>
												<xsl:for-each select="node()">
													<xsl:if test="self::text()">
														<xsl:copy-of select="."/>
													</xsl:if>
												</xsl:for-each>
											</lgCommon:text>
										</xsl:for-each>
									</lgCon:definition>
								</xsl:for-each>
								<xsl:for-each select="lgCon2:comment">
									<xsl:variable name="var94_comment" select="."/>
									<lgCon:comment>
										<xsl:attribute name="propertyName">
											<xsl:value-of select="string(@propertyName)"/>
										</xsl:attribute>
										<xsl:if test="$var94_comment/@propertyId">
											<xsl:attribute name="propertyId">
												<xsl:value-of select="string(@propertyId)"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:if test="$var94_comment/@language">
											<xsl:attribute name="language">
												<xsl:value-of select="string(@language)"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:for-each select="lgCommon2:value">
											<xsl:variable name="var96_value" select="."/>
											<xsl:if test="$var96_value/@dataType">
												<xsl:attribute name="format">
													<xsl:value-of select="string(@dataType)"/>
												</xsl:attribute>
											</xsl:if>
										</xsl:for-each>
										<xsl:for-each select="lgCommon2:source">
											<xsl:variable name="var98_source" select="."/>
											<lgCommon:source>
												<xsl:if test="$var98_source/@subRef">
													<xsl:attribute name="subRef">
														<xsl:value-of select="string(@subRef)"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:if test="$var98_source/@role">
													<xsl:attribute name="role">
														<xsl:value-of select="string(@role)"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:value-of select="string(.)"/>
											</lgCommon:source>
										</xsl:for-each>
										<xsl:for-each select="lgCommon2:usageContext">
											<lgCommon:usageContext>
												<xsl:value-of select="string(.)"/>
											</lgCommon:usageContext>
										</xsl:for-each>
										<xsl:for-each select="lgCommon2:propertyQualifier">
											<lgCommon:propertyQualifier>
												<xsl:attribute name="propertyQualifierId">
													<xsl:value-of select="string(@propertyQualifierName)"/>
												</xsl:attribute>
											</lgCommon:propertyQualifier>
										</xsl:for-each>
										<xsl:for-each select="lgCommon2:value">
											<lgCommon:text>
												<xsl:for-each select="node()">
													<xsl:if test="self::text()">
														<xsl:copy-of select="."/>
													</xsl:if>
												</xsl:for-each>
											</lgCommon:text>
										</xsl:for-each>
									</lgCon:comment>
								</xsl:for-each>
								<xsl:for-each select="lgCon2:property">
									<xsl:variable name="var108_property" select="."/>
									<lgCon:conceptProperty>
										<xsl:attribute name="propertyName">
											<xsl:value-of select="string(@propertyName)"/>
										</xsl:attribute>
										<xsl:if test="$var108_property/@propertyId">
											<xsl:attribute name="propertyId">
												<xsl:value-of select="string(@propertyId)"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:if test="$var108_property/@language">
											<xsl:attribute name="language">
												<xsl:value-of select="string(@language)"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:for-each select="lgCommon2:value">
											<xsl:variable name="var110_value" select="."/>
											<xsl:if test="$var110_value/@dataType">
												<xsl:attribute name="format">
													<xsl:value-of select="string(@dataType)"/>
												</xsl:attribute>
											</xsl:if>
										</xsl:for-each>
										<xsl:for-each select="lgCommon2:source">
											<xsl:variable name="var112_source" select="."/>
											<lgCommon:source>
												<xsl:if test="$var112_source/@subRef">
													<xsl:attribute name="subRef">
														<xsl:value-of select="string(@subRef)"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:if test="$var112_source/@role">
													<xsl:attribute name="role">
														<xsl:value-of select="string(@role)"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:value-of select="string(.)"/>
											</lgCommon:source>
										</xsl:for-each>
										<xsl:for-each select="lgCommon2:usageContext">
											<lgCommon:usageContext>
												<xsl:value-of select="string(.)"/>
											</lgCommon:usageContext>
										</xsl:for-each>
										<xsl:for-each select="lgCommon2:propertyQualifier">
											<lgCommon:propertyQualifier>
												<xsl:attribute name="propertyQualifierId">
													<xsl:value-of select="string(@propertyQualifierName)"/>
												</xsl:attribute>
											</lgCommon:propertyQualifier>
										</xsl:for-each>
										<xsl:for-each select="lgCommon2:value">
											<lgCommon:text>
												<xsl:for-each select="node()">
													<xsl:if test="self::text()">
														<xsl:copy-of select="."/>
													</xsl:if>
												</xsl:for-each>
											</lgCommon:text>
										</xsl:for-each>
									</lgCon:conceptProperty>
								</xsl:for-each>
								<xsl:for-each select="lgCon2:propertyLink">
									<lgCon:propertyLink>
										<xsl:attribute name="sourceProperty">
											<xsl:value-of select="string(@sourceProperty)"/>
										</xsl:attribute>
										<xsl:attribute name="link">
											<xsl:value-of select="string(@propertyLink)"/>
										</xsl:attribute>
										<xsl:attribute name="targetProperty">
											<xsl:value-of select="string(@targetProperty)"/>
										</xsl:attribute>
									</lgCon:propertyLink>
								</xsl:for-each>
							</lgCon:concept>
						</xsl:for-each>
					</n:concepts>
				</xsl:for-each>
				<xsl:for-each select="n2:relations">
					<xsl:variable name="var124_relations" select="."/>
					<n:relations>
						<xsl:if test="$var124_relations/@containerName">
							<xsl:attribute name="dc">
								<xsl:value-of select="string(@containerName)"/>
							</xsl:attribute>
						</xsl:if>
						<xsl:if test="$var124_relations/@isNative">
							<xsl:attribute name="isNative">
								<xsl:value-of select="(('0' != @isNative) and ('false' != @isNative))"/>
							</xsl:attribute>
						</xsl:if>
						<xsl:for-each select="lgCommon2:entityDescription">
							<lgCommon:entityDescription>
								<xsl:for-each select="node()">
									<xsl:if test="self::text()">
										<xsl:copy-of select="."/>
									</xsl:if>
								</xsl:for-each>
							</lgCommon:entityDescription>
						</xsl:for-each>
						<xsl:for-each select="lgRel2:source">
							<xsl:variable name="var130_source" select="."/>
							<lgRel:source>
								<xsl:if test="$var130_source/@subRef">
									<xsl:attribute name="subRef">
										<xsl:value-of select="string(@subRef)"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var130_source/@role">
									<xsl:attribute name="role">
										<xsl:value-of select="string(@role)"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:value-of select="string(.)"/>
							</lgRel:source>
						</xsl:for-each>
						<xsl:for-each select="lgRel2:association">
							<xsl:variable name="var132_association" select="."/>
							<lgRel:association>
								<xsl:attribute name="id">
									<xsl:value-of select="string(@entityCode)"/>
								</xsl:attribute>
								<xsl:if test="$var132_association/@entityCodeNamespace">
									<xsl:attribute name="codingSchemeId">
										<xsl:value-of select="string(@entityCodeNamespace)"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var132_association/@forwardName">
									<xsl:attribute name="forwardName">
										<xsl:value-of select="string(@forwardName)"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var132_association/@reverseName">
									<xsl:attribute name="reverseName">
										<xsl:value-of select="string(@reverseName)"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var132_association/@inverse">
									<xsl:attribute name="inverse">
										<xsl:value-of select="string(@inverse)"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var132_association/@isNavigable">
									<xsl:attribute name="isNavigable">
										<xsl:value-of select="(('0' != @isNavigable) and ('false' != @isNavigable))"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var132_association/@isTransitive">
									<xsl:attribute name="isTransitive">
										<xsl:value-of select="(('0' != @isTransitive) and ('false' != @isTransitive))"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var132_association/@isAntiTransitive">
									<xsl:attribute name="isAntiTransitive">
										<xsl:value-of select="(('0' != @isAntiTransitive) and ('false' != @isAntiTransitive))"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var132_association/@isSymmetric">
									<xsl:attribute name="isSymmetric">
										<xsl:value-of select="(('0' != @isSymmetric) and ('false' != @isSymmetric))"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var132_association/@isAntiSymmetric">
									<xsl:attribute name="isAntiSymmetric">
										<xsl:value-of select="(('0' != @isAntiSymmetric) and ('false' != @isAntiSymmetric))"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var132_association/@isReflexive">
									<xsl:attribute name="isReflexive">
										<xsl:value-of select="(('0' != @isReflexive) and ('false' != @isReflexive))"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var132_association/@isAntiReflexive">
									<xsl:attribute name="isAntiReflexive">
										<xsl:value-of select="(('0' != @isAntiReflexive) and ('false' != @isAntiReflexive))"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var132_association/@isFunctional">
									<xsl:attribute name="isFunctional">
										<xsl:value-of select="(('0' != @isFunctional) and ('false' != @isFunctional))"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var132_association/@isReverseFunctional">
									<xsl:attribute name="isReverseFunctional">
										<xsl:value-of select="(('0' != @isReverseFunctional) and ('false' != @isReverseFunctional))"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:for-each select="lgCommon2:entityDescription">
									<lgCommon:entityDescription>
										<xsl:for-each select="node()">
											<xsl:if test="self::text()">
												<xsl:copy-of select="."/>
											</xsl:if>
										</xsl:for-each>
									</lgCommon:entityDescription>
								</xsl:for-each>
								<xsl:for-each select="lgRel2:source">
									<xsl:variable name="var138_source" select="."/>
									<lgRel:sourceEntity>
										<xsl:if test="$var138_source/@sourceEntityCodeNamespace">
											<xsl:attribute name="sourceCodingScheme">
												<xsl:value-of select="string(@sourceEntityCodeNamespace)"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:attribute name="sourceId">
											<xsl:value-of select="string(@sourceEntityCode)"/>
										</xsl:attribute>
										<xsl:for-each select="lgRel2:target">
											<xsl:variable name="var140_target" select="."/>
											<lgRel:targetEntity>
												<xsl:if test="$var140_target/@targetEntityCodeNamespace">
													<xsl:attribute name="targetCodingScheme">
														<xsl:value-of select="string(@targetEntityCodeNamespace)"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:attribute name="targetId">
													<xsl:value-of select="string(@targetEntityCode)"/>
												</xsl:attribute>
												<xsl:for-each select="lgRel2:associationQualification">
													<lgRel:associationQualification>
														<xsl:attribute name="associationQualifier">
															<xsl:value-of select="string(@associationQualifier)"/>
														</xsl:attribute>
													</lgRel:associationQualification>
												</xsl:for-each>
											</lgRel:targetEntity>
										</xsl:for-each>
										<xsl:for-each select="lgRel2:targetData">
											<xsl:variable name="var144_targetData" select="."/>
											<lgRel:targetDataValue>
												<xsl:if test="$var144_targetData/@associationInstanceId">
													<xsl:attribute name="dataId">
														<xsl:value-of select="string(@associationInstanceId)"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:for-each select="lgRel2:associationQualification">
													<lgRel:associationQualification>
														<xsl:attribute name="associationQualifier">
															<xsl:value-of select="string(@associationQualifier)"/>
														</xsl:attribute>
													</lgRel:associationQualification>
												</xsl:for-each>
											</lgRel:targetDataValue>
										</xsl:for-each>
									</lgRel:sourceEntity>
								</xsl:for-each>
								<xsl:for-each select="lgCon2:property">
									<xsl:variable name="var148_property" select="."/>
									<lgRel:associationProperty>
										<xsl:attribute name="propertyName">
											<xsl:value-of select="string(@propertyName)"/>
										</xsl:attribute>
										<xsl:if test="$var148_property/@propertyId">
											<xsl:attribute name="propertyId">
												<xsl:value-of select="string(@propertyId)"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:if test="$var148_property/@language">
											<xsl:attribute name="language">
												<xsl:value-of select="string(@language)"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:for-each select="lgCommon2:source">
											<xsl:variable name="var150_source" select="."/>
											<lgCommon:source>
												<xsl:if test="$var150_source/@subRef">
													<xsl:attribute name="subRef">
														<xsl:value-of select="string(@subRef)"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:if test="$var150_source/@role">
													<xsl:attribute name="role">
														<xsl:value-of select="string(@role)"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:value-of select="string(.)"/>
											</lgCommon:source>
										</xsl:for-each>
										<xsl:for-each select="lgCommon2:usageContext">
											<lgCommon:usageContext>
												<xsl:value-of select="string(.)"/>
											</lgCommon:usageContext>
										</xsl:for-each>
										<xsl:for-each select="lgCommon2:propertyQualifier">
											<lgCommon:propertyQualifier>
												<xsl:attribute name="propertyQualifierId">
													<xsl:value-of select="string(@propertyQualifierName)"/>
												</xsl:attribute>
											</lgCommon:propertyQualifier>
										</xsl:for-each>
										<xsl:for-each select="lgCommon2:value">
											<lgCommon:text>
												<xsl:for-each select="node()">
													<xsl:if test="self::text()">
														<xsl:copy-of select="."/>
													</xsl:if>
												</xsl:for-each>
											</lgCommon:text>
										</xsl:for-each>
									</lgRel:associationProperty>
								</xsl:for-each>
							</lgRel:association>
						</xsl:for-each>
					</n:relations>
				</xsl:for-each>
			</xsl:for-each>
		</n:codingScheme>
	</xsl:template>
</xsl:stylesheet>
