<?xml version="1.0" encoding="UTF-8"?>
<!--
Transforms documents defined against LexGrid model revision 2008/01
to LexGrid model revision 2009/01.
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:lgBuiltin2="http://LexGrid.org/schema/2008/01/LexGrid/builtins" xmlns:n2="http://LexGrid.org/schema/2008/01/LexGrid/codingSchemes" xmlns:lgCommon2="http://LexGrid.org/schema/2008/01/LexGrid/commonTypes" xmlns:lgCon2="http://LexGrid.org/schema/2008/01/LexGrid/concepts" xmlns:ldap="http://LexGrid.org/schema/2008/01/LexGrid/ldap" xmlns:lgNaming2="http://LexGrid.org/schema/2008/01/LexGrid/naming" xmlns:lgRel2="http://LexGrid.org/schema/2008/01/LexGrid/relations" xmlns:lgVer2="http://LexGrid.org/schema/2008/01/LexGrid/versions" xmlns:lgBuiltin="http://LexGrid.org/schema/2009/01/LexGrid/builtins" xmlns:n="http://LexGrid.org/schema/2009/01/LexGrid/codingSchemes" xmlns:lgCommon="http://LexGrid.org/schema/2009/01/LexGrid/commonTypes" xmlns:lgCon="http://LexGrid.org/schema/2009/01/LexGrid/concepts" xmlns:lgNaming="http://LexGrid.org/schema/2009/01/LexGrid/naming" xmlns:lgRel="http://LexGrid.org/schema/2009/01/LexGrid/relations" xmlns:lgVD="http://LexGrid.org/schema/2009/01/LexGrid/valueDomains" xmlns:lgVer="http://LexGrid.org/schema/2009/01/LexGrid/versions" xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="ldap lgBuiltin2 lgCommon2 lgCon2 lgNaming2 lgRel2 lgVer2 n2 xs xsi xsl" xmlns="http://LexGrid.org/schema/2009/01/LexGrid/codingSchemes">
	<xsl:namespace-alias stylesheet-prefix="n" result-prefix="#default"/>
	<xsl:output method="xml" encoding="UTF-8" indent="yes"/>
	<xsl:template match="/">
		<n:codingScheme>
			<xsl:attribute name="xsi:schemaLocation"><xsl:value-of select="'http://LexGrid.org/schema/2009/01/LexGrid/codingSchemes http://LexGrid.org/schema/2009/01/LexGrid/codingSchemes.xsd'"/></xsl:attribute>
			<xsl:variable name="var1_instance" select="."/>
			<xsl:for-each select="$var1_instance/n2:codingScheme">
				<xsl:variable name="var2_codingScheme" select="."/>
				<xsl:if test="$var2_codingScheme/@codingScheme">
					<xsl:attribute name="codingSchemeName"><xsl:value-of select="string(@codingScheme)"/></xsl:attribute>
				</xsl:if>
				<xsl:if test="$var2_codingScheme/@registeredName">
					<xsl:attribute name="codingSchemeURI"><xsl:value-of select="string(@registeredName)"/></xsl:attribute>
				</xsl:if>
				<xsl:if test="$var2_codingScheme/@formalName">
					<xsl:attribute name="formalName"><xsl:value-of select="string(@formalName)"/></xsl:attribute>
				</xsl:if>
				<xsl:if test="$var2_codingScheme/@defaultLanguage">
					<xsl:attribute name="defaultLanguage"><xsl:value-of select="string(@defaultLanguage)"/></xsl:attribute>
				</xsl:if>
				<xsl:if test="$var2_codingScheme/@approxNumConcepts">
					<xsl:attribute name="approxNumConcepts"><xsl:value-of select="number(string(@approxNumConcepts))"/></xsl:attribute>
				</xsl:if>
				<xsl:if test="$var2_codingScheme/@representsVersion">
					<xsl:attribute name="representsVersion"><xsl:value-of select="string(@representsVersion)"/></xsl:attribute>
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
							<xsl:attribute name="subRef"><xsl:value-of select="string(@subRef)"/></xsl:attribute>
						</xsl:if>
						<xsl:if test="$var10_source/@role">
							<xsl:attribute name="role"><xsl:value-of select="string(@role)"/></xsl:attribute>
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
					<xsl:for-each select="n2:mappings/n2:supportedAssociation">
						<xsl:variable name="var16_supportedAssociation" select="."/>
						<lgNaming:supportedAssociation>
							<xsl:if test="$var16_supportedAssociation/@localId">
								<xsl:attribute name="localId"><xsl:value-of select="string(@localId)"/></xsl:attribute>
							</xsl:if>
							<xsl:if test="$var16_supportedAssociation/@urn">
								<xsl:attribute name="uri"><xsl:value-of select="string(@urn)"/></xsl:attribute>
							</xsl:if>
							<xsl:value-of select="string(.)"/>
						</lgNaming:supportedAssociation>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/n2:supportedAssociationQualifier">
						<xsl:variable name="var18_supportedAssociationQualifier" select="."/>
						<lgNaming:supportedAssociationQualifier>
							<xsl:if test="$var18_supportedAssociationQualifier/@localId">
								<xsl:attribute name="localId"><xsl:value-of select="string(@localId)"/></xsl:attribute>
							</xsl:if>
							<xsl:if test="$var18_supportedAssociationQualifier/@urn">
								<xsl:attribute name="uri"><xsl:value-of select="string(@urn)"/></xsl:attribute>
							</xsl:if>
							<xsl:value-of select="string(.)"/>
						</lgNaming:supportedAssociationQualifier>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/n2:supportedCodingScheme">
						<xsl:variable name="var20_supportedCodingScheme" select="."/>
						<lgNaming:supportedCodingScheme>
							<xsl:if test="$var20_supportedCodingScheme/@localId">
								<xsl:attribute name="localId"><xsl:value-of select="string(@localId)"/></xsl:attribute>
							</xsl:if>
							<xsl:if test="$var20_supportedCodingScheme/@urn">
								<xsl:attribute name="uri"><xsl:value-of select="string(@urn)"/></xsl:attribute>
							</xsl:if>
							<xsl:if test="$var20_supportedCodingScheme/@isImported">
								<xsl:attribute name="isImported"><xsl:value-of select="(('0' != @isImported) and ('false' != @isImported))"/></xsl:attribute>
							</xsl:if>
							<xsl:value-of select="string(.)"/>
						</lgNaming:supportedCodingScheme>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/n2:supportedContext">
						<xsl:variable name="var22_supportedContext" select="."/>
						<lgNaming:supportedContext>
							<xsl:if test="$var22_supportedContext/@localId">
								<xsl:attribute name="localId"><xsl:value-of select="string(@localId)"/></xsl:attribute>
							</xsl:if>
							<xsl:if test="$var22_supportedContext/@urn">
								<xsl:attribute name="uri"><xsl:value-of select="string(@urn)"/></xsl:attribute>
							</xsl:if>
							<xsl:value-of select="string(.)"/>
						</lgNaming:supportedContext>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/n2:supportedDegreeOfFidelity">
						<xsl:variable name="var24_supportedDegreeOfFidelity" select="."/>
						<lgNaming:supportedDegreeOfFidelity>
							<xsl:if test="$var24_supportedDegreeOfFidelity/@localId">
								<xsl:attribute name="localId"><xsl:value-of select="string(@localId)"/></xsl:attribute>
							</xsl:if>
							<xsl:if test="$var24_supportedDegreeOfFidelity/@urn">
								<xsl:attribute name="uri"><xsl:value-of select="string(@urn)"/></xsl:attribute>
							</xsl:if>
							<xsl:value-of select="string(.)"/>
						</lgNaming:supportedDegreeOfFidelity>
					</xsl:for-each>
					<lgNaming:supportedEntityType>
						<xsl:attribute name="localId"><xsl:value-of select="'concept'"/></xsl:attribute>
						<xsl:value-of select="'concept'"/>
					</lgNaming:supportedEntityType>
					<xsl:for-each select="n2:mappings/n2:supportedHierarchy">
						<xsl:variable name="var26_supportedHierarchy" select="."/>
						<lgNaming:supportedHierarchy>
							<xsl:if test="$var26_supportedHierarchy/@localId">
								<xsl:attribute name="localId"><xsl:value-of select="string(@localId)"/></xsl:attribute>
							</xsl:if>
							<xsl:if test="$var26_supportedHierarchy/@urn">
								<xsl:attribute name="uri"><xsl:value-of select="string(@urn)"/></xsl:attribute>
							</xsl:if>
							<xsl:if test="$var26_supportedHierarchy/@associationIds">
								<xsl:attribute name="associationNames"><xsl:value-of select="string(@associationIds)"/></xsl:attribute>
							</xsl:if>
							<xsl:if test="$var26_supportedHierarchy/@rootCode">
								<xsl:attribute name="rootCode"><xsl:value-of select="string(@rootCode)"/></xsl:attribute>
							</xsl:if>
							<xsl:if test="$var26_supportedHierarchy/@isForwardNavigable">
								<xsl:attribute name="isForwardNavigable"><xsl:value-of select="(('0' != @isForwardNavigable) and ('false' != @isForwardNavigable))"/></xsl:attribute>
							</xsl:if>
							<xsl:value-of select="string(.)"/>
						</lgNaming:supportedHierarchy>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/n2:supportedLanguage">
						<xsl:variable name="var28_supportedLanguage" select="."/>
						<lgNaming:supportedLanguage>
							<xsl:if test="$var28_supportedLanguage/@localId">
								<xsl:attribute name="localId"><xsl:value-of select="string(@localId)"/></xsl:attribute>
							</xsl:if>
							<xsl:if test="$var28_supportedLanguage/@urn">
								<xsl:attribute name="uri"><xsl:value-of select="string(@urn)"/></xsl:attribute>
							</xsl:if>
							<xsl:value-of select="string(.)"/>
						</lgNaming:supportedLanguage>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/n2:supportedProperty">
						<xsl:variable name="var30_supportedProperty" select="."/>
						<lgNaming:supportedProperty>
							<xsl:if test="$var30_supportedProperty/@localId">
								<xsl:attribute name="localId"><xsl:value-of select="string(@localId)"/></xsl:attribute>
							</xsl:if>
							<xsl:if test="$var30_supportedProperty/@urn">
								<xsl:attribute name="uri"><xsl:value-of select="string(@urn)"/></xsl:attribute>
							</xsl:if>
							<xsl:value-of select="string(.)"/>
						</lgNaming:supportedProperty>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/n2:supportedPropertyLink">
						<xsl:variable name="var32_supportedPropertyLink" select="."/>
						<lgNaming:supportedPropertyLink>
							<xsl:if test="$var32_supportedPropertyLink/@localId">
								<xsl:attribute name="localId"><xsl:value-of select="string(@localId)"/></xsl:attribute>
							</xsl:if>
							<xsl:if test="$var32_supportedPropertyLink/@urn">
								<xsl:attribute name="uri"><xsl:value-of select="string(@urn)"/></xsl:attribute>
							</xsl:if>
							<xsl:value-of select="string(.)"/>
						</lgNaming:supportedPropertyLink>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/n2:supportedPropertyQualifier">
						<xsl:variable name="var34_supportedPropertyQualifier" select="."/>
						<lgNaming:supportedPropertyQualifier>
							<xsl:if test="$var34_supportedPropertyQualifier/@localId">
								<xsl:attribute name="localId"><xsl:value-of select="string(@localId)"/></xsl:attribute>
							</xsl:if>
							<xsl:if test="$var34_supportedPropertyQualifier/@urn">
								<xsl:attribute name="uri"><xsl:value-of select="string(@urn)"/></xsl:attribute>
							</xsl:if>
							<xsl:value-of select="string(.)"/>
						</lgNaming:supportedPropertyQualifier>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/n2:supportedRepresentationalForm">
						<xsl:variable name="var36_supportedRepresentationalForm" select="."/>
						<lgNaming:supportedRepresentationalForm>
							<xsl:if test="$var36_supportedRepresentationalForm/@localId">
								<xsl:attribute name="localId"><xsl:value-of select="string(@localId)"/></xsl:attribute>
							</xsl:if>
							<xsl:if test="$var36_supportedRepresentationalForm/@urn">
								<xsl:attribute name="uri"><xsl:value-of select="string(@urn)"/></xsl:attribute>
							</xsl:if>
							<xsl:value-of select="string(.)"/>
						</lgNaming:supportedRepresentationalForm>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/n2:supportedSource">
						<xsl:variable name="var38_supportedSource" select="."/>
						<lgNaming:supportedSource>
							<xsl:if test="$var38_supportedSource/@localId">
								<xsl:attribute name="localId"><xsl:value-of select="string(@localId)"/></xsl:attribute>
							</xsl:if>
							<xsl:if test="$var38_supportedSource/@urn">
								<xsl:attribute name="uri"><xsl:value-of select="string(@urn)"/></xsl:attribute>
							</xsl:if>
							<xsl:if test="$var38_supportedSource/@assemblyRule">
								<xsl:attribute name="assemblyRule"><xsl:value-of select="string(@assemblyRule)"/></xsl:attribute>
							</xsl:if>
							<xsl:value-of select="string(.)"/>
						</lgNaming:supportedSource>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/n2:supportedConceptStatus">
						<xsl:variable name="var40_supportedConceptStatus" select="."/>
						<lgNaming:supportedStatus>
							<xsl:if test="$var40_supportedConceptStatus/@localId">
								<xsl:attribute name="localId"><xsl:value-of select="string(@localId)"/></xsl:attribute>
							</xsl:if>
							<xsl:if test="$var40_supportedConceptStatus/@urn">
								<xsl:attribute name="uri"><xsl:value-of select="string(@urn)"/></xsl:attribute>
							</xsl:if>
							<xsl:value-of select="string(.)"/>
						</lgNaming:supportedStatus>
					</xsl:for-each>
				</n:mappings>
				<xsl:for-each select="n2:properties">
					<n:properties>
						<xsl:for-each select="lgCommon2:property">
							<xsl:variable name="var44_property" select="."/>
							<lgCommon:property>
								<xsl:if test="$var44_property/@propertyName">
									<xsl:attribute name="propertyName"><xsl:value-of select="string(@propertyName)"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var44_property/@propertyId">
									<xsl:attribute name="propertyId"><xsl:value-of select="string(@propertyId)"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var44_property/@language">
									<xsl:attribute name="language"><xsl:value-of select="string(@language)"/></xsl:attribute>
								</xsl:if>
								<xsl:for-each select="lgCommon2:source">
									<xsl:variable name="var46_source" select="."/>
									<lgCommon:source>
										<xsl:if test="$var46_source/@subRef">
											<xsl:attribute name="subRef"><xsl:value-of select="string(@subRef)"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var46_source/@role">
											<xsl:attribute name="role"><xsl:value-of select="string(@role)"/></xsl:attribute>
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
									<xsl:variable name="var50_propertyQualifier" select="."/>
									<lgCommon:propertyQualifier>
										<xsl:if test="$var50_propertyQualifier/@propertyQualifierId">
											<xsl:attribute name="propertyQualifierName"><xsl:value-of select="string(@propertyQualifierId)"/></xsl:attribute>
										</xsl:if>
										<lgCommon:value>
											<xsl:for-each select="node()">
												<xsl:if test="self::text()">
													<xsl:copy-of select="."/>
												</xsl:if>
											</xsl:for-each>
										</lgCommon:value>
									</lgCommon:propertyQualifier>
								</xsl:for-each>
								<lgCommon:value>
									<xsl:for-each select="lgCommon2:text/node()">
										<xsl:if test="self::text()">
											<xsl:copy-of select="."/>
										</xsl:if>
									</xsl:for-each>
								</lgCommon:value>
							</lgCommon:property>
						</xsl:for-each>
					</n:properties>
				</xsl:for-each>
				<xsl:for-each select="n2:concepts">
					<n:entities>
						<xsl:for-each select="lgCon2:concept">
							<xsl:variable name="var56_concept" select="."/>
							<lgCon:entity>
								<xsl:if test="$var56_concept/@isActive">
									<xsl:attribute name="isActive"><xsl:value-of select="(('0' != @isActive) and ('false' != @isActive))"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var56_concept/@conceptStatus">
									<xsl:attribute name="status"><xsl:value-of select="string(@conceptStatus)"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var56_concept/@id">
									<xsl:attribute name="entityCode"><xsl:value-of select="string(@id)"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var56_concept/@codingSchemeId">
									<xsl:attribute name="entityCodeNamespace"><xsl:value-of select="string(@codingSchemeId)"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var56_concept/@isAnonymous">
									<xsl:attribute name="isAnonymous"><xsl:value-of select="(('0' != @isAnonymous) and ('false' != @isAnonymous))"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var56_concept/@isDefined">
									<xsl:attribute name="isDefined"><xsl:value-of select="(('0' != @isDefined) and ('false' != @isDefined))"/></xsl:attribute>
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
								<lgCon:entityType>
									<xsl:value-of select="'concept'"/>
								</lgCon:entityType>
								<xsl:for-each select="lgCon2:presentation">
									<xsl:variable name="var62_presentation" select="."/>
									<lgCon:presentation>
										<xsl:if test="$var62_presentation/@propertyName">
											<xsl:attribute name="propertyName"><xsl:value-of select="string(@propertyName)"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var62_presentation/@propertyId">
											<xsl:attribute name="propertyId"><xsl:value-of select="string(@propertyId)"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var62_presentation/@language">
											<xsl:attribute name="language"><xsl:value-of select="string(@language)"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var62_presentation/@isPreferred">
											<xsl:attribute name="isPreferred"><xsl:value-of select="(('0' != @isPreferred) and ('false' != @isPreferred))"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var62_presentation/@degreeOfFidelity">
											<xsl:attribute name="degreeOfFidelity"><xsl:value-of select="string(@degreeOfFidelity)"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var62_presentation/@matchIfNoContext">
											<xsl:attribute name="matchIfNoContext"><xsl:value-of select="(('0' != @matchIfNoContext) and ('false' != @matchIfNoContext))"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var62_presentation/@representationalForm">
											<xsl:attribute name="representationalForm"><xsl:value-of select="string(@representationalForm)"/></xsl:attribute>
										</xsl:if>
										<xsl:for-each select="lgCommon2:source">
											<xsl:variable name="var64_source" select="."/>
											<lgCommon:source>
												<xsl:if test="$var64_source/@subRef">
													<xsl:attribute name="subRef"><xsl:value-of select="string(@subRef)"/></xsl:attribute>
												</xsl:if>
												<xsl:if test="$var64_source/@role">
													<xsl:attribute name="role"><xsl:value-of select="string(@role)"/></xsl:attribute>
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
											<xsl:variable name="var68_propertyQualifier" select="."/>
											<lgCommon:propertyQualifier>
												<xsl:if test="$var68_propertyQualifier/@propertyQualifierId">
													<xsl:attribute name="propertyQualifierName"><xsl:value-of select="string(@propertyQualifierId)"/></xsl:attribute>
												</xsl:if>
												<lgCommon:value>
													<xsl:for-each select="node()">
														<xsl:if test="self::text()">
															<xsl:copy-of select="."/>
														</xsl:if>
													</xsl:for-each>
												</lgCommon:value>
											</lgCommon:propertyQualifier>
										</xsl:for-each>
										<lgCommon:value>
											<xsl:for-each select="lgCommon2:text/node()">
												<xsl:if test="self::text()">
													<xsl:copy-of select="."/>
												</xsl:if>
											</xsl:for-each>
										</lgCommon:value>
									</lgCon:presentation>
								</xsl:for-each>
								<xsl:for-each select="lgCon2:definition">
									<xsl:variable name="var72_definition" select="."/>
									<lgCon:definition>
										<xsl:if test="$var72_definition/@propertyName">
											<xsl:attribute name="propertyName"><xsl:value-of select="string(@propertyName)"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var72_definition/@propertyId">
											<xsl:attribute name="propertyId"><xsl:value-of select="string(@propertyId)"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var72_definition/@language">
											<xsl:attribute name="language"><xsl:value-of select="string(@language)"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var72_definition/@isPreferred">
											<xsl:attribute name="isPreferred"><xsl:value-of select="(('0' != @isPreferred) and ('false' != @isPreferred))"/></xsl:attribute>
										</xsl:if>
										<xsl:for-each select="lgCommon2:source">
											<xsl:variable name="var74_source" select="."/>
											<lgCommon:source>
												<xsl:if test="$var74_source/@subRef">
													<xsl:attribute name="subRef"><xsl:value-of select="string(@subRef)"/></xsl:attribute>
												</xsl:if>
												<xsl:if test="$var74_source/@role">
													<xsl:attribute name="role"><xsl:value-of select="string(@role)"/></xsl:attribute>
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
											<xsl:variable name="var78_propertyQualifier" select="."/>
											<lgCommon:propertyQualifier>
												<xsl:if test="$var78_propertyQualifier/@propertyQualifierId">
													<xsl:attribute name="propertyQualifierName"><xsl:value-of select="string(@propertyQualifierId)"/></xsl:attribute>
												</xsl:if>
												<lgCommon:value>
													<xsl:for-each select="node()">
														<xsl:if test="self::text()">
															<xsl:copy-of select="."/>
														</xsl:if>
													</xsl:for-each>
												</lgCommon:value>
											</lgCommon:propertyQualifier>
										</xsl:for-each>
										<lgCommon:value>
											<xsl:for-each select="lgCommon2:text/node()">
												<xsl:if test="self::text()">
													<xsl:copy-of select="."/>
												</xsl:if>
											</xsl:for-each>
										</lgCommon:value>
									</lgCon:definition>
								</xsl:for-each>
								<xsl:for-each select="lgCon2:comment">
									<xsl:variable name="var82_comment" select="."/>
									<lgCon:comment>
										<xsl:if test="$var82_comment/@propertyName">
											<xsl:attribute name="propertyName"><xsl:value-of select="string(@propertyName)"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var82_comment/@propertyId">
											<xsl:attribute name="propertyId"><xsl:value-of select="string(@propertyId)"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var82_comment/@language">
											<xsl:attribute name="language"><xsl:value-of select="string(@language)"/></xsl:attribute>
										</xsl:if>
										<xsl:for-each select="lgCommon2:source">
											<xsl:variable name="var84_source" select="."/>
											<lgCommon:source>
												<xsl:if test="$var84_source/@subRef">
													<xsl:attribute name="subRef"><xsl:value-of select="string(@subRef)"/></xsl:attribute>
												</xsl:if>
												<xsl:if test="$var84_source/@role">
													<xsl:attribute name="role"><xsl:value-of select="string(@role)"/></xsl:attribute>
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
											<xsl:variable name="var88_propertyQualifier" select="."/>
											<lgCommon:propertyQualifier>
												<xsl:if test="$var88_propertyQualifier/@propertyQualifierId">
													<xsl:attribute name="propertyQualifierName"><xsl:value-of select="string(@propertyQualifierId)"/></xsl:attribute>
												</xsl:if>
												<lgCommon:value>
													<xsl:for-each select="node()">
														<xsl:if test="self::text()">
															<xsl:copy-of select="."/>
														</xsl:if>
													</xsl:for-each>
												</lgCommon:value>
											</lgCommon:propertyQualifier>
										</xsl:for-each>
										<lgCommon:value>
											<xsl:for-each select="lgCommon2:text/node()">
												<xsl:if test="self::text()">
													<xsl:copy-of select="."/>
												</xsl:if>
											</xsl:for-each>
										</lgCommon:value>
									</lgCon:comment>
								</xsl:for-each>
								<xsl:for-each select="lgCon2:conceptProperty">
									<xsl:variable name="var92_conceptProperty" select="."/>
									<lgCon:property>
										<xsl:if test="$var92_conceptProperty/@propertyName">
											<xsl:attribute name="propertyName"><xsl:value-of select="string(@propertyName)"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var92_conceptProperty/@propertyId">
											<xsl:attribute name="propertyId"><xsl:value-of select="string(@propertyId)"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var92_conceptProperty/@language">
											<xsl:attribute name="language"><xsl:value-of select="string(@language)"/></xsl:attribute>
										</xsl:if>
										<xsl:for-each select="lgCommon2:source">
											<xsl:variable name="var94_source" select="."/>
											<lgCommon:source>
												<xsl:if test="$var94_source/@subRef">
													<xsl:attribute name="subRef"><xsl:value-of select="string(@subRef)"/></xsl:attribute>
												</xsl:if>
												<xsl:if test="$var94_source/@role">
													<xsl:attribute name="role"><xsl:value-of select="string(@role)"/></xsl:attribute>
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
											<xsl:variable name="var98_propertyQualifier" select="."/>
											<lgCommon:propertyQualifier>
												<xsl:if test="$var98_propertyQualifier/@propertyQualifierId">
													<xsl:attribute name="propertyQualifierName"><xsl:value-of select="string(@propertyQualifierId)"/></xsl:attribute>
												</xsl:if>
												<lgCommon:value>
													<xsl:for-each select="node()">
														<xsl:if test="self::text()">
															<xsl:copy-of select="."/>
														</xsl:if>
													</xsl:for-each>
												</lgCommon:value>
											</lgCommon:propertyQualifier>
										</xsl:for-each>
										<lgCommon:value>
											<xsl:for-each select="lgCommon2:text/node()">
												<xsl:if test="self::text()">
													<xsl:copy-of select="."/>
												</xsl:if>
											</xsl:for-each>
										</lgCommon:value>
									</lgCon:property>
								</xsl:for-each>
								<xsl:for-each select="lgCon2:propertyLink">
									<xsl:variable name="var102_propertyLink" select="."/>
									<lgCon:propertyLink>
										<xsl:if test="$var102_propertyLink/@sourceProperty">
											<xsl:attribute name="sourceProperty"><xsl:value-of select="string(@sourceProperty)"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var102_propertyLink/@link">
											<xsl:attribute name="propertyLink"><xsl:value-of select="string(@link)"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var102_propertyLink/@targetProperty">
											<xsl:attribute name="targetProperty"><xsl:value-of select="string(@targetProperty)"/></xsl:attribute>
										</xsl:if>
									</lgCon:propertyLink>
								</xsl:for-each>
							</lgCon:entity>
						</xsl:for-each>
					</n:entities>
				</xsl:for-each>
				<xsl:for-each select="n2:relations">
					<xsl:variable name="var104_relations" select="."/>
					<n:relations>
						<xsl:if test="$var104_relations/@dc">
							<xsl:attribute name="containerName"><xsl:value-of select="string(@dc)"/></xsl:attribute>
						</xsl:if>
						<xsl:if test="$var104_relations/@isNative">
							<xsl:attribute name="isNative"><xsl:value-of select="(('0' != @isNative) and ('false' != @isNative))"/></xsl:attribute>
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
							<xsl:variable name="var110_source" select="."/>
							<lgRel:source>
								<xsl:if test="$var110_source/@subRef">
									<xsl:attribute name="subRef"><xsl:value-of select="string(@subRef)"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var110_source/@role">
									<xsl:attribute name="role"><xsl:value-of select="string(@role)"/></xsl:attribute>
								</xsl:if>
								<xsl:value-of select="string(.)"/>
							</lgRel:source>
						</xsl:for-each>
						<xsl:for-each select="lgRel2:association">
							<xsl:variable name="var112_association" select="."/>
							<lgRel:association>
								<xsl:if test="$var112_association/@id">
									<xsl:attribute name="entityCode"><xsl:value-of select="string(@id)"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var112_association/@codingSchemeId">
									<xsl:attribute name="entityCodeNamespace"><xsl:value-of select="string(@codingSchemeId)"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var112_association/@id">
									<xsl:attribute name="associationName"><xsl:value-of select="string(@id)"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var112_association/@forwardName">
									<xsl:attribute name="forwardName"><xsl:value-of select="string(@forwardName)"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var112_association/@reverseName">
									<xsl:attribute name="reverseName"><xsl:value-of select="string(@reverseName)"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var112_association/@inverse">
									<xsl:attribute name="inverse"><xsl:value-of select="string(@inverse)"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var112_association/@isNavigable">
									<xsl:attribute name="isNavigable"><xsl:value-of select="(('0' != @isNavigable) and ('false' != @isNavigable))"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var112_association/@isTransitive">
									<xsl:attribute name="isTransitive"><xsl:value-of select="(('0' != @isTransitive) and ('false' != @isTransitive))"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var112_association/@isAntiTransitive">
									<xsl:attribute name="isAntiTransitive"><xsl:value-of select="(('0' != @isAntiTransitive) and ('false' != @isAntiTransitive))"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var112_association/@isSymmetric">
									<xsl:attribute name="isSymmetric"><xsl:value-of select="(('0' != @isSymmetric) and ('false' != @isSymmetric))"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var112_association/@isAntiSymmetric">
									<xsl:attribute name="isAntiSymmetric"><xsl:value-of select="(('0' != @isAntiSymmetric) and ('false' != @isAntiSymmetric))"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var112_association/@isReflexive">
									<xsl:attribute name="isReflexive"><xsl:value-of select="(('0' != @isReflexive) and ('false' != @isReflexive))"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var112_association/@isAntiReflexive">
									<xsl:attribute name="isAntiReflexive"><xsl:value-of select="(('0' != @isAntiReflexive) and ('false' != @isAntiReflexive))"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var112_association/@isFunctional">
									<xsl:attribute name="isFunctional"><xsl:value-of select="(('0' != @isFunctional) and ('false' != @isFunctional))"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var112_association/@isReverseFunctional">
									<xsl:attribute name="isReverseFunctional"><xsl:value-of select="(('0' != @isReverseFunctional) and ('false' != @isReverseFunctional))"/></xsl:attribute>
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
								<xsl:for-each select="lgRel2:sourceEntity">
									<xsl:variable name="var118_sourceEntity" select="."/>
									<lgRel:source>
										<xsl:if test="$var118_sourceEntity/@sourceCodingScheme">
											<xsl:attribute name="sourceEntityCodeNamespace"><xsl:value-of select="string(@sourceCodingScheme)"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var118_sourceEntity/@sourceId">
											<xsl:attribute name="sourceEntityCode"><xsl:value-of select="string(@sourceId)"/></xsl:attribute>
										</xsl:if>
										<xsl:for-each select="lgRel2:targetEntity">
											<xsl:variable name="var120_targetEntity" select="."/>
											<lgRel:target>
												<xsl:if test="$var120_targetEntity/@targetId">
													<xsl:attribute name="targetEntityCode"><xsl:value-of select="string(@targetId)"/></xsl:attribute>
												</xsl:if>
												<xsl:if test="$var120_targetEntity/@targetCodingScheme">
													<xsl:attribute name="targetEntityCodeNamespace"><xsl:value-of select="string(@targetCodingScheme)"/></xsl:attribute>
												</xsl:if>
												<xsl:for-each select="lgRel2:associationQualification">
													<xsl:variable name="var122_associationQualification" select="."/>
													<lgRel:associationQualification>
														<xsl:if test="$var122_associationQualification/@associationQualifier">
															<xsl:attribute name="associationQualifier"><xsl:value-of select="string(@associationQualifier)"/></xsl:attribute>
														</xsl:if>
													</lgRel:associationQualification>
												</xsl:for-each>
											</lgRel:target>
										</xsl:for-each>
										<xsl:for-each select="lgRel2:targetDataValue">
											<xsl:variable name="var124_targetDataValue" select="."/>
											<lgRel:targetData>
												<xsl:if test="$var124_targetDataValue/@dataId">
													<xsl:attribute name="associationInstanceId"><xsl:value-of select="string(@dataId)"/></xsl:attribute>
												</xsl:if>
												<xsl:for-each select="lgRel2:associationQualification">
													<xsl:variable name="var126_associationQualification" select="."/>
													<lgRel:associationQualification>
														<xsl:if test="$var126_associationQualification/@associationQualifier">
															<xsl:attribute name="associationQualifier"><xsl:value-of select="string(@associationQualifier)"/></xsl:attribute>
														</xsl:if>
													</lgRel:associationQualification>
												</xsl:for-each>
											</lgRel:targetData>
										</xsl:for-each>
									</lgRel:source>
								</xsl:for-each>
							</lgRel:association>
						</xsl:for-each>
					</n:relations>
				</xsl:for-each>
			</xsl:for-each>
		</n:codingScheme>
	</xsl:template>
</xsl:stylesheet>
