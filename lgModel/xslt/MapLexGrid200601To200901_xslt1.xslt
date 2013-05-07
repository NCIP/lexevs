<?xml version="1.0" encoding="UTF-8"?>
<!--
Transforms documents defined against LexGrid model revision 2006/01
to LexGrid model revision 2009/01.
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:lgBuiltin2="http://LexGrid.org/schema/2006/01/LexGrid/builtins" xmlns:n2="http://LexGrid.org/schema/2006/01/LexGrid/codingSchemes" xmlns:lgCommon2="http://LexGrid.org/schema/2006/01/LexGrid/commonTypes" xmlns:lgCon2="http://LexGrid.org/schema/2006/01/LexGrid/concepts" xmlns:ldap="http://LexGrid.org/schema/2006/01/LexGrid/ldap" xmlns:lgNaming2="http://LexGrid.org/schema/2006/01/LexGrid/naming" xmlns:lgRel2="http://LexGrid.org/schema/2006/01/LexGrid/relations" xmlns:lgVer2="http://LexGrid.org/schema/2006/01/LexGrid/versions" xmlns:lgBuiltin="http://LexGrid.org/schema/2009/01/LexGrid/builtins" xmlns:n="http://LexGrid.org/schema/2009/01/LexGrid/codingSchemes" xmlns:lgCommon="http://LexGrid.org/schema/2009/01/LexGrid/commonTypes" xmlns:lgCon="http://LexGrid.org/schema/2009/01/LexGrid/concepts" xmlns:lgNaming="http://LexGrid.org/schema/2009/01/LexGrid/naming" xmlns:lgRel="http://LexGrid.org/schema/2009/01/LexGrid/relations" xmlns:lgVD="http://LexGrid.org/schema/2009/01/LexGrid/valueDomains" xmlns:lgVer="http://LexGrid.org/schema/2009/01/LexGrid/versions" xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="ldap lgBuiltin2 lgCommon2 lgCon2 lgNaming2 lgRel2 lgVer2 n2 xs xsi xsl" xmlns="http://LexGrid.org/schema/2009/01/LexGrid/codingSchemes">
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
						<xsl:if test="$var16_supportedAssociation/@localId">
							<lgNaming:supportedAssociation>
								<xsl:attribute name="localId"><xsl:value-of select="string(@localId)"/></xsl:attribute>
								<xsl:if test="$var16_supportedAssociation/@urn">
									<xsl:attribute name="uri"><xsl:value-of select="string(@urn)"/></xsl:attribute>
								</xsl:if>
								<xsl:value-of select="string(@localId)"/>
							</lgNaming:supportedAssociation>
						</xsl:if>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/n2:supportedAssociationQualifier">
						<xsl:variable name="var18_supportedAssociationQualifier" select="."/>
						<xsl:if test="$var18_supportedAssociationQualifier/@localId">
							<lgNaming:supportedAssociationQualifier>
								<xsl:attribute name="localId"><xsl:value-of select="string(@localId)"/></xsl:attribute>
								<xsl:if test="$var18_supportedAssociationQualifier/@urn">
									<xsl:attribute name="uri"><xsl:value-of select="string(@urn)"/></xsl:attribute>
								</xsl:if>
								<xsl:value-of select="string(@localId)"/>
							</lgNaming:supportedAssociationQualifier>
						</xsl:if>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/n2:supportedCodingScheme">
						<xsl:variable name="var20_supportedCodingScheme" select="."/>
						<xsl:if test="$var20_supportedCodingScheme/@localId">
							<lgNaming:supportedCodingScheme>
								<xsl:attribute name="localId"><xsl:value-of select="string(@localId)"/></xsl:attribute>
								<xsl:if test="$var20_supportedCodingScheme/@urn">
									<xsl:attribute name="uri"><xsl:value-of select="string(@urn)"/></xsl:attribute>
								</xsl:if>
								<xsl:value-of select="string(@localId)"/>
							</lgNaming:supportedCodingScheme>
						</xsl:if>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/n2:supportedContext">
						<xsl:variable name="var22_supportedContext" select="."/>
						<xsl:if test="$var22_supportedContext/@localId">
							<lgNaming:supportedContext>
								<xsl:attribute name="localId"><xsl:value-of select="string(@localId)"/></xsl:attribute>
								<xsl:if test="$var22_supportedContext/@urn">
									<xsl:attribute name="uri"><xsl:value-of select="string(@urn)"/></xsl:attribute>
								</xsl:if>
								<xsl:value-of select="string(@localId)"/>
							</lgNaming:supportedContext>
						</xsl:if>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/n2:supportedDegreeOfFidelity">
						<xsl:variable name="var24_supportedDegreeOfFidelity" select="."/>
						<xsl:if test="$var24_supportedDegreeOfFidelity/@localId">
							<lgNaming:supportedDegreeOfFidelity>
								<xsl:attribute name="localId"><xsl:value-of select="string(@localId)"/></xsl:attribute>
								<xsl:if test="$var24_supportedDegreeOfFidelity/@urn">
									<xsl:attribute name="uri"><xsl:value-of select="string(@urn)"/></xsl:attribute>
								</xsl:if>
								<xsl:value-of select="string(@localId)"/>
							</lgNaming:supportedDegreeOfFidelity>
						</xsl:if>
					</xsl:for-each>
					<lgNaming:supportedEntityType>
						<xsl:attribute name="localId"><xsl:value-of select="'concept'"/></xsl:attribute>
						<xsl:value-of select="'concept'"/>
					</lgNaming:supportedEntityType>
					<xsl:for-each select="n2:mappings/n2:supportedLanguage">
						<xsl:variable name="var26_supportedLanguage" select="."/>
						<xsl:if test="$var26_supportedLanguage/@localId">
							<lgNaming:supportedLanguage>
								<xsl:attribute name="localId"><xsl:value-of select="string(@localId)"/></xsl:attribute>
								<xsl:if test="$var26_supportedLanguage/@urn">
									<xsl:attribute name="uri"><xsl:value-of select="string(@urn)"/></xsl:attribute>
								</xsl:if>
								<xsl:value-of select="string(@localId)"/>
							</lgNaming:supportedLanguage>
						</xsl:if>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/n2:supportedProperty">
						<xsl:variable name="var28_supportedProperty" select="."/>
						<xsl:if test="$var28_supportedProperty/@localId">
							<lgNaming:supportedProperty>
								<xsl:attribute name="localId"><xsl:value-of select="string(@localId)"/></xsl:attribute>
								<xsl:if test="$var28_supportedProperty/@urn">
									<xsl:attribute name="uri"><xsl:value-of select="string(@urn)"/></xsl:attribute>
								</xsl:if>
								<xsl:value-of select="string(@localId)"/>
							</lgNaming:supportedProperty>
						</xsl:if>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/n2:supportedPropertyLink">
						<xsl:variable name="var30_supportedPropertyLink" select="."/>
						<xsl:if test="$var30_supportedPropertyLink/@localId">
							<lgNaming:supportedPropertyLink>
								<xsl:attribute name="localId"><xsl:value-of select="string(@localId)"/></xsl:attribute>
								<xsl:if test="$var30_supportedPropertyLink/@urn">
									<xsl:attribute name="uri"><xsl:value-of select="string(@urn)"/></xsl:attribute>
								</xsl:if>
								<xsl:value-of select="string(@localId)"/>
							</lgNaming:supportedPropertyLink>
						</xsl:if>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/n2:supportedPropertyQualifier">
						<xsl:variable name="var32_supportedPropertyQualifier" select="."/>
						<xsl:if test="$var32_supportedPropertyQualifier/@localId">
							<lgNaming:supportedPropertyQualifier>
								<xsl:attribute name="localId"><xsl:value-of select="string(@localId)"/></xsl:attribute>
								<xsl:if test="$var32_supportedPropertyQualifier/@urn">
									<xsl:attribute name="uri"><xsl:value-of select="string(@urn)"/></xsl:attribute>
								</xsl:if>
								<xsl:value-of select="string(@localId)"/>
							</lgNaming:supportedPropertyQualifier>
						</xsl:if>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/n2:supportedRepresentationalForm">
						<xsl:variable name="var34_supportedRepresentationalForm" select="."/>
						<xsl:if test="$var34_supportedRepresentationalForm/@localId">
							<lgNaming:supportedRepresentationalForm>
								<xsl:attribute name="localId"><xsl:value-of select="string(@localId)"/></xsl:attribute>
								<xsl:if test="$var34_supportedRepresentationalForm/@urn">
									<xsl:attribute name="uri"><xsl:value-of select="string(@urn)"/></xsl:attribute>
								</xsl:if>
								<xsl:value-of select="string(@localId)"/>
							</lgNaming:supportedRepresentationalForm>
						</xsl:if>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/n2:supportedSource">
						<xsl:variable name="var36_supportedSource" select="."/>
						<xsl:if test="$var36_supportedSource/@localId">
							<lgNaming:supportedSource>
								<xsl:attribute name="localId"><xsl:value-of select="string(@localId)"/></xsl:attribute>
								<xsl:if test="$var36_supportedSource/@urn">
									<xsl:attribute name="uri"><xsl:value-of select="string(@urn)"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var36_supportedSource/@assemblyRule">
									<xsl:attribute name="assemblyRule"><xsl:value-of select="string(@assemblyRule)"/></xsl:attribute>
								</xsl:if>
								<xsl:value-of select="string(@localId)"/>
							</lgNaming:supportedSource>
						</xsl:if>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/n2:supportedConceptStatus">
						<xsl:variable name="var38_supportedConceptStatus" select="."/>
						<xsl:if test="$var38_supportedConceptStatus/@localId">
							<lgNaming:supportedStatus>
								<xsl:attribute name="localId"><xsl:value-of select="string(@localId)"/></xsl:attribute>
								<xsl:if test="$var38_supportedConceptStatus/@urn">
									<xsl:attribute name="uri"><xsl:value-of select="string(@urn)"/></xsl:attribute>
								</xsl:if>
								<xsl:value-of select="string(@localId)"/>
							</lgNaming:supportedStatus>
						</xsl:if>
					</xsl:for-each>
				</n:mappings>
				<xsl:for-each select="n2:properties">
					<n:properties>
						<xsl:for-each select="lgCommon2:property">
							<xsl:variable name="var42_property" select="."/>
							<lgCommon:property>
								<xsl:if test="$var42_property/@property">
									<xsl:attribute name="propertyName"><xsl:value-of select="string(@property)"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var42_property/@propertyId">
									<xsl:attribute name="propertyId"><xsl:value-of select="string(@propertyId)"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var42_property/@language">
									<xsl:attribute name="language"><xsl:value-of select="string(@language)"/></xsl:attribute>
								</xsl:if>
								<xsl:for-each select="lgCommon2:source">
									<xsl:variable name="var44_source" select="."/>
									<lgCommon:source>
										<xsl:if test="$var44_source/@subRef">
											<xsl:attribute name="subRef"><xsl:value-of select="string(@subRef)"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var44_source/@role">
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
									<xsl:variable name="var48_propertyQualifier" select="."/>
									<lgCommon:propertyQualifier>
										<xsl:if test="$var48_propertyQualifier/@propertyQualifierId">
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
							<xsl:variable name="var54_concept" select="."/>
							<lgCon:entity>
								<xsl:if test="$var54_concept/@isActive">
									<xsl:attribute name="isActive"><xsl:value-of select="(('0' != @isActive) and ('false' != @isActive))"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var54_concept/@conceptStatus">
									<xsl:attribute name="status"><xsl:value-of select="string(@conceptStatus)"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var54_concept/@conceptCode">
									<xsl:attribute name="entityCode"><xsl:value-of select="string(@conceptCode)"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var54_concept/@isAnonymous">
									<xsl:attribute name="isAnonymous"><xsl:value-of select="(('0' != @isAnonymous) and ('false' != @isAnonymous))"/></xsl:attribute>
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
									<xsl:variable name="var60_presentation" select="."/>
									<lgCon:presentation>
										<xsl:if test="$var60_presentation/@property">
											<xsl:attribute name="propertyName"><xsl:value-of select="string(@property)"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var60_presentation/@propertyId">
											<xsl:attribute name="propertyId"><xsl:value-of select="string(@propertyId)"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var60_presentation/@language">
											<xsl:attribute name="language"><xsl:value-of select="string(@language)"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var60_presentation/@isPreferred">
											<xsl:attribute name="isPreferred"><xsl:value-of select="(('0' != @isPreferred) and ('false' != @isPreferred))"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var60_presentation/@degreeOfFidelity">
											<xsl:attribute name="degreeOfFidelity"><xsl:value-of select="string(@degreeOfFidelity)"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var60_presentation/@matchIfNoContext">
											<xsl:attribute name="matchIfNoContext"><xsl:value-of select="(('0' != @matchIfNoContext) and ('false' != @matchIfNoContext))"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var60_presentation/@representationalForm">
											<xsl:attribute name="representationalForm"><xsl:value-of select="string(@representationalForm)"/></xsl:attribute>
										</xsl:if>
										<xsl:for-each select="lgCommon2:source">
											<xsl:variable name="var62_source" select="."/>
											<lgCommon:source>
												<xsl:if test="$var62_source/@subRef">
													<xsl:attribute name="subRef"><xsl:value-of select="string(@subRef)"/></xsl:attribute>
												</xsl:if>
												<xsl:if test="$var62_source/@role">
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
											<xsl:variable name="var66_propertyQualifier" select="."/>
											<lgCommon:propertyQualifier>
												<xsl:if test="$var66_propertyQualifier/@propertyQualifierId">
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
									<xsl:variable name="var70_definition" select="."/>
									<lgCon:definition>
										<xsl:if test="$var70_definition/@property">
											<xsl:attribute name="propertyName"><xsl:value-of select="string(@property)"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var70_definition/@propertyId">
											<xsl:attribute name="propertyId"><xsl:value-of select="string(@propertyId)"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var70_definition/@language">
											<xsl:attribute name="language"><xsl:value-of select="string(@language)"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var70_definition/@isPreferred">
											<xsl:attribute name="isPreferred"><xsl:value-of select="(('0' != @isPreferred) and ('false' != @isPreferred))"/></xsl:attribute>
										</xsl:if>
										<xsl:for-each select="lgCommon2:source">
											<xsl:variable name="var72_source" select="."/>
											<lgCommon:source>
												<xsl:if test="$var72_source/@subRef">
													<xsl:attribute name="subRef"><xsl:value-of select="string(@subRef)"/></xsl:attribute>
												</xsl:if>
												<xsl:if test="$var72_source/@role">
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
											<xsl:variable name="var76_propertyQualifier" select="."/>
											<lgCommon:propertyQualifier>
												<xsl:if test="$var76_propertyQualifier/@propertyQualifierId">
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
									<xsl:variable name="var80_comment" select="."/>
									<lgCon:comment>
										<xsl:if test="$var80_comment/@property">
											<xsl:attribute name="propertyName"><xsl:value-of select="string(@property)"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var80_comment/@propertyId">
											<xsl:attribute name="propertyId"><xsl:value-of select="string(@propertyId)"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var80_comment/@language">
											<xsl:attribute name="language"><xsl:value-of select="string(@language)"/></xsl:attribute>
										</xsl:if>
										<xsl:for-each select="lgCommon2:source">
											<xsl:variable name="var82_source" select="."/>
											<lgCommon:source>
												<xsl:if test="$var82_source/@subRef">
													<xsl:attribute name="subRef"><xsl:value-of select="string(@subRef)"/></xsl:attribute>
												</xsl:if>
												<xsl:if test="$var82_source/@role">
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
											<xsl:variable name="var86_propertyQualifier" select="."/>
											<lgCommon:propertyQualifier>
												<xsl:if test="$var86_propertyQualifier/@propertyQualifierId">
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
									<xsl:variable name="var90_conceptProperty" select="."/>
									<lgCon:property>
										<xsl:if test="$var90_conceptProperty/@property">
											<xsl:attribute name="propertyName"><xsl:value-of select="string(@property)"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var90_conceptProperty/@propertyId">
											<xsl:attribute name="propertyId"><xsl:value-of select="string(@propertyId)"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var90_conceptProperty/@language">
											<xsl:attribute name="language"><xsl:value-of select="string(@language)"/></xsl:attribute>
										</xsl:if>
										<xsl:for-each select="lgCommon2:source">
											<xsl:variable name="var92_source" select="."/>
											<lgCommon:source>
												<xsl:if test="$var92_source/@subRef">
													<xsl:attribute name="subRef"><xsl:value-of select="string(@subRef)"/></xsl:attribute>
												</xsl:if>
												<xsl:if test="$var92_source/@role">
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
											<xsl:variable name="var96_propertyQualifier" select="."/>
											<lgCommon:propertyQualifier>
												<xsl:if test="$var96_propertyQualifier/@propertyQualifierId">
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
									<xsl:variable name="var100_propertyLink" select="."/>
									<lgCon:propertyLink>
										<xsl:if test="$var100_propertyLink/@sourceProperty">
											<xsl:attribute name="sourceProperty"><xsl:value-of select="string(@sourceProperty)"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var100_propertyLink/@link">
											<xsl:attribute name="propertyLink"><xsl:value-of select="string(@link)"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var100_propertyLink/@targetProperty">
											<xsl:attribute name="targetProperty"><xsl:value-of select="string(@targetProperty)"/></xsl:attribute>
										</xsl:if>
									</lgCon:propertyLink>
								</xsl:for-each>
							</lgCon:entity>
						</xsl:for-each>
					</n:entities>
				</xsl:for-each>
				<xsl:for-each select="n2:relations">
					<xsl:variable name="var102_relations" select="."/>
					<n:relations>
						<xsl:if test="$var102_relations/@dc">
							<xsl:attribute name="containerName"><xsl:value-of select="string(@dc)"/></xsl:attribute>
						</xsl:if>
						<xsl:if test="$var102_relations/@isNative">
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
							<xsl:variable name="var108_source" select="."/>
							<lgRel:source>
								<xsl:if test="$var108_source/@subRef">
									<xsl:attribute name="subRef"><xsl:value-of select="string(@subRef)"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var108_source/@role">
									<xsl:attribute name="role"><xsl:value-of select="string(@role)"/></xsl:attribute>
								</xsl:if>
								<xsl:value-of select="string(.)"/>
							</lgRel:source>
						</xsl:for-each>
						<xsl:for-each select="lgRel2:association">
							<xsl:variable name="var110_association" select="."/>
							<lgRel:association>
								<xsl:if test="$var110_association/@association">
									<xsl:attribute name="entityCode"><xsl:value-of select="string(@association)"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var110_association/@association">
									<xsl:attribute name="associationName"><xsl:value-of select="string(@association)"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var110_association/@forwardName">
									<xsl:attribute name="forwardName"><xsl:value-of select="string(@forwardName)"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var110_association/@reverseName">
									<xsl:attribute name="reverseName"><xsl:value-of select="string(@reverseName)"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var110_association/@inverse">
									<xsl:attribute name="inverse"><xsl:value-of select="string(@inverse)"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var110_association/@isNavigable">
									<xsl:attribute name="isNavigable"><xsl:value-of select="(('0' != @isNavigable) and ('false' != @isNavigable))"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var110_association/@isTransitive">
									<xsl:attribute name="isTransitive"><xsl:value-of select="(('0' != @isTransitive) and ('false' != @isTransitive))"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var110_association/@isAntiTransitive">
									<xsl:attribute name="isAntiTransitive"><xsl:value-of select="(('0' != @isAntiTransitive) and ('false' != @isAntiTransitive))"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var110_association/@isSymmetric">
									<xsl:attribute name="isSymmetric"><xsl:value-of select="(('0' != @isSymmetric) and ('false' != @isSymmetric))"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var110_association/@isAntiSymmetric">
									<xsl:attribute name="isAntiSymmetric"><xsl:value-of select="(('0' != @isAntiSymmetric) and ('false' != @isAntiSymmetric))"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var110_association/@isReflexive">
									<xsl:attribute name="isReflexive"><xsl:value-of select="(('0' != @isReflexive) and ('false' != @isReflexive))"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var110_association/@isAntiReflexive">
									<xsl:attribute name="isAntiReflexive"><xsl:value-of select="(('0' != @isAntiReflexive) and ('false' != @isAntiReflexive))"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var110_association/@isFunctional">
									<xsl:attribute name="isFunctional"><xsl:value-of select="(('0' != @isFunctional) and ('false' != @isFunctional))"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var110_association/@isReverseFunctional">
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
								<xsl:for-each select="lgRel2:sourceConcept">
									<xsl:variable name="var116_sourceConcept" select="."/>
									<lgRel:source>
										<xsl:if test="$var116_sourceConcept/@sourceCodingScheme">
											<xsl:attribute name="sourceEntityCodeNamespace"><xsl:value-of select="string(@sourceCodingScheme)"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var116_sourceConcept/@sourceConcept">
											<xsl:attribute name="sourceEntityCode"><xsl:value-of select="string(@sourceConcept)"/></xsl:attribute>
										</xsl:if>
										<xsl:for-each select="lgRel2:targetConcept">
											<xsl:variable name="var118_targetConcept" select="."/>
											<lgRel:target>
												<xsl:if test="$var118_targetConcept/@targetConcept">
													<xsl:attribute name="targetEntityCode"><xsl:value-of select="string(@targetConcept)"/></xsl:attribute>
												</xsl:if>
												<xsl:if test="$var118_targetConcept/@targetCodingScheme">
													<xsl:attribute name="targetEntityCodeNamespace"><xsl:value-of select="string(@targetCodingScheme)"/></xsl:attribute>
												</xsl:if>
												<xsl:for-each select="lgRel2:associationQualification">
													<xsl:variable name="var120_associationQualification" select="."/>
													<lgRel:associationQualification>
														<xsl:if test="$var120_associationQualification/@associationQualifier">
															<xsl:attribute name="associationQualifier"><xsl:value-of select="string(@associationQualifier)"/></xsl:attribute>
														</xsl:if>
													</lgRel:associationQualification>
												</xsl:for-each>
											</lgRel:target>
										</xsl:for-each>
										<xsl:for-each select="lgRel2:targetDataValue">
											<xsl:variable name="var122_targetDataValue" select="."/>
											<lgRel:targetData>
												<xsl:if test="$var122_targetDataValue/@id">
													<xsl:attribute name="associationInstanceId"><xsl:value-of select="string(@id)"/></xsl:attribute>
												</xsl:if>
												<xsl:for-each select="lgRel2:associationQualification">
													<xsl:variable name="var124_associationQualification" select="."/>
													<lgRel:associationQualification>
														<xsl:if test="$var124_associationQualification/@associationQualifier">
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
