<?xml version="1.0" encoding="UTF-8"?>
<!--
Transforms documents defined against LexGrid model revision 2008/01
to LexGrid model revision 2009/01.
-->
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:lgBuiltin2="http://LexGrid.org/schema/2008/01/LexGrid/builtins" xmlns:n2="http://LexGrid.org/schema/2008/01/LexGrid/codingSchemes" xmlns:lgCommon2="http://LexGrid.org/schema/2008/01/LexGrid/commonTypes" xmlns:lgCon2="http://LexGrid.org/schema/2008/01/LexGrid/concepts" xmlns:ldap="http://LexGrid.org/schema/2008/01/LexGrid/ldap" xmlns:lgNaming2="http://LexGrid.org/schema/2008/01/LexGrid/naming" xmlns:lgRel2="http://LexGrid.org/schema/2008/01/LexGrid/relations" xmlns:lgVer2="http://LexGrid.org/schema/2008/01/LexGrid/versions" xmlns:lgBuiltin="http://LexGrid.org/schema/2009/01/LexGrid/builtins" xmlns:n="http://LexGrid.org/schema/2009/01/LexGrid/codingSchemes" xmlns:lgCommon="http://LexGrid.org/schema/2009/01/LexGrid/commonTypes" xmlns:lgCon="http://LexGrid.org/schema/2009/01/LexGrid/concepts" xmlns:lgNaming="http://LexGrid.org/schema/2009/01/LexGrid/naming" xmlns:lgRel="http://LexGrid.org/schema/2009/01/LexGrid/relations" xmlns:lgVD="http://LexGrid.org/schema/2009/01/LexGrid/valueDomains" xmlns:lgVer="http://LexGrid.org/schema/2009/01/LexGrid/versions" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions" exclude-result-prefixes="fn ldap lgBuiltin2 lgCommon2 lgCon2 lgNaming2 lgRel2 lgVer2 n2 xs xsi xsl" xmlns="http://LexGrid.org/schema/2009/01/LexGrid/codingSchemes">
	<xsl:namespace-alias stylesheet-prefix="n" result-prefix="#default"/>
	<xsl:output method="xml" encoding="UTF-8" indent="yes"/>
	<xsl:template match="/">
		<n:codingScheme>
			<xsl:attribute name="xsi:schemaLocation" separator=" "><xsl:sequence select="'http://LexGrid.org/schema/2009/01/LexGrid/codingSchemes http://LexGrid.org/schema/2009/01/LexGrid/codingSchemes.xsd'"/></xsl:attribute>
			<xsl:variable name="var1_instance" as="node()" select="."/>
			<xsl:for-each select="$var1_instance/n2:codingScheme">
				<xsl:variable name="var2_codingScheme" as="node()" select="."/>
				<xsl:if test="$var2_codingScheme/@codingScheme">
					<xsl:attribute name="codingSchemeName"><xsl:sequence select="xs:string(xs:NCName(xs:token(@codingScheme)))"/></xsl:attribute>
				</xsl:if>
				<xsl:if test="$var2_codingScheme/@registeredName">
					<xsl:attribute name="codingSchemeURI"><xsl:sequence select="xs:string(xs:anyURI(@registeredName))"/></xsl:attribute>
				</xsl:if>
				<xsl:if test="$var2_codingScheme/@formalName">
					<xsl:attribute name="formalName"><xsl:sequence select="xs:string(@formalName)"/></xsl:attribute>
				</xsl:if>
				<xsl:if test="$var2_codingScheme/@defaultLanguage">
					<xsl:attribute name="defaultLanguage"><xsl:sequence select="xs:string(xs:NCName(xs:token(@defaultLanguage)))"/></xsl:attribute>
				</xsl:if>
				<xsl:if test="$var2_codingScheme/@approxNumConcepts">
					<xsl:attribute name="approxNumConcepts"><xsl:sequence select="xs:string(xs:integer(@approxNumConcepts))"/></xsl:attribute>
				</xsl:if>
				<xsl:if test="$var2_codingScheme/@representsVersion">
					<xsl:attribute name="representsVersion"><xsl:sequence select="xs:string(@representsVersion)"/></xsl:attribute>
				</xsl:if>
				<xsl:for-each select="lgCommon2:entityDescription">
					<lgCommon:entityDescription>
						<xsl:for-each select="node()">
							<xsl:if test="self::text()">
								<xsl:sequence select="."/>
							</xsl:if>
						</xsl:for-each>
					</lgCommon:entityDescription>
				</xsl:for-each>
				<xsl:for-each select="n2:localName">
					<n:localName>
						<xsl:sequence select="xs:string(.)"/>
					</n:localName>
				</xsl:for-each>
				<xsl:for-each select="n2:source">
					<xsl:variable name="var10_source" as="node()" select="."/>
					<n:source>
						<xsl:if test="$var10_source/@subRef">
							<xsl:attribute name="subRef"><xsl:sequence select="xs:string(@subRef)"/></xsl:attribute>
						</xsl:if>
						<xsl:if test="$var10_source/@role">
							<xsl:attribute name="role"><xsl:sequence select="xs:string(xs:NCName(xs:string(@role)))"/></xsl:attribute>
						</xsl:if>
						<xsl:sequence select="xs:string(xs:NCName(xs:token(.)))"/>
					</n:source>
				</xsl:for-each>
				<xsl:for-each select="n2:copyright">
					<n:copyright>
						<xsl:for-each select="node()">
							<xsl:if test="self::text()">
								<xsl:sequence select="."/>
							</xsl:if>
						</xsl:for-each>
					</n:copyright>
				</xsl:for-each>
				<n:mappings>
					<xsl:for-each select="n2:mappings/n2:supportedAssociation">
						<xsl:variable name="var16_supportedAssociation" as="node()" select="."/>
						<lgNaming:supportedAssociation>
							<xsl:if test="$var16_supportedAssociation/@localId">
								<xsl:attribute name="localId"><xsl:sequence select="xs:string(xs:NCName(xs:token(@localId)))"/></xsl:attribute>
							</xsl:if>
							<xsl:if test="$var16_supportedAssociation/@urn">
								<xsl:attribute name="uri"><xsl:sequence select="xs:string(xs:anyURI(@urn))"/></xsl:attribute>
							</xsl:if>
							<xsl:sequence select="xs:string(.)"/>
						</lgNaming:supportedAssociation>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/n2:supportedAssociationQualifier">
						<xsl:variable name="var18_supportedAssociationQualifier" as="node()" select="."/>
						<lgNaming:supportedAssociationQualifier>
							<xsl:if test="$var18_supportedAssociationQualifier/@localId">
								<xsl:attribute name="localId"><xsl:sequence select="xs:string(xs:NCName(xs:token(@localId)))"/></xsl:attribute>
							</xsl:if>
							<xsl:if test="$var18_supportedAssociationQualifier/@urn">
								<xsl:attribute name="uri"><xsl:sequence select="xs:string(xs:anyURI(@urn))"/></xsl:attribute>
							</xsl:if>
							<xsl:sequence select="xs:string(.)"/>
						</lgNaming:supportedAssociationQualifier>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/n2:supportedCodingScheme">
						<xsl:variable name="var20_supportedCodingScheme" as="node()" select="."/>
						<lgNaming:supportedCodingScheme>
							<xsl:if test="$var20_supportedCodingScheme/@localId">
								<xsl:attribute name="localId"><xsl:sequence select="xs:string(xs:NCName(xs:token(@localId)))"/></xsl:attribute>
							</xsl:if>
							<xsl:if test="$var20_supportedCodingScheme/@urn">
								<xsl:attribute name="uri"><xsl:sequence select="xs:string(xs:anyURI(@urn))"/></xsl:attribute>
							</xsl:if>
							<xsl:if test="$var20_supportedCodingScheme/@isImported">
								<xsl:attribute name="isImported"><xsl:sequence select="xs:string(xs:boolean(@isImported))"/></xsl:attribute>
							</xsl:if>
							<xsl:sequence select="xs:string(.)"/>
						</lgNaming:supportedCodingScheme>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/n2:supportedContext">
						<xsl:variable name="var22_supportedContext" as="node()" select="."/>
						<lgNaming:supportedContext>
							<xsl:if test="$var22_supportedContext/@localId">
								<xsl:attribute name="localId"><xsl:sequence select="xs:string(xs:NCName(xs:token(@localId)))"/></xsl:attribute>
							</xsl:if>
							<xsl:if test="$var22_supportedContext/@urn">
								<xsl:attribute name="uri"><xsl:sequence select="xs:string(xs:anyURI(@urn))"/></xsl:attribute>
							</xsl:if>
							<xsl:sequence select="xs:string(.)"/>
						</lgNaming:supportedContext>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/n2:supportedDegreeOfFidelity">
						<xsl:variable name="var24_supportedDegreeOfFidelity" as="node()" select="."/>
						<lgNaming:supportedDegreeOfFidelity>
							<xsl:if test="$var24_supportedDegreeOfFidelity/@localId">
								<xsl:attribute name="localId"><xsl:sequence select="xs:string(xs:NCName(xs:token(@localId)))"/></xsl:attribute>
							</xsl:if>
							<xsl:if test="$var24_supportedDegreeOfFidelity/@urn">
								<xsl:attribute name="uri"><xsl:sequence select="xs:string(xs:anyURI(@urn))"/></xsl:attribute>
							</xsl:if>
							<xsl:sequence select="xs:string(.)"/>
						</lgNaming:supportedDegreeOfFidelity>
					</xsl:for-each>
					<lgNaming:supportedEntityType>
						<xsl:attribute name="localId"><xsl:sequence select="xs:string(xs:NCName('concept'))"/></xsl:attribute>
						<xsl:sequence select="'concept'"/>
					</lgNaming:supportedEntityType>
					<xsl:for-each select="n2:mappings/n2:supportedHierarchy">
						<xsl:variable name="var26_supportedHierarchy" as="node()" select="."/>
						<lgNaming:supportedHierarchy>
							<xsl:if test="$var26_supportedHierarchy/@localId">
								<xsl:attribute name="localId"><xsl:sequence select="xs:string(xs:NCName(xs:token(@localId)))"/></xsl:attribute>
							</xsl:if>
							<xsl:if test="$var26_supportedHierarchy/@urn">
								<xsl:attribute name="uri"><xsl:sequence select="xs:string(xs:anyURI(@urn))"/></xsl:attribute>
							</xsl:if>
							<xsl:if test="$var26_supportedHierarchy/@associationIds">
								<xsl:attribute name="associationNames"><xsl:sequence select="xs:string(xs:string(xs:string(xs:string(@associationIds))))"/></xsl:attribute>
							</xsl:if>
							<xsl:if test="$var26_supportedHierarchy/@rootCode">
								<xsl:attribute name="rootCode"><xsl:sequence select="xs:string(@rootCode)"/></xsl:attribute>
							</xsl:if>
							<xsl:if test="$var26_supportedHierarchy/@isForwardNavigable">
								<xsl:attribute name="isForwardNavigable"><xsl:sequence select="xs:string(xs:boolean(@isForwardNavigable))"/></xsl:attribute>
							</xsl:if>
							<xsl:sequence select="xs:string(.)"/>
						</lgNaming:supportedHierarchy>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/n2:supportedLanguage">
						<xsl:variable name="var28_supportedLanguage" as="node()" select="."/>
						<lgNaming:supportedLanguage>
							<xsl:if test="$var28_supportedLanguage/@localId">
								<xsl:attribute name="localId"><xsl:sequence select="xs:string(xs:NCName(xs:token(@localId)))"/></xsl:attribute>
							</xsl:if>
							<xsl:if test="$var28_supportedLanguage/@urn">
								<xsl:attribute name="uri"><xsl:sequence select="xs:string(xs:anyURI(@urn))"/></xsl:attribute>
							</xsl:if>
							<xsl:sequence select="xs:string(.)"/>
						</lgNaming:supportedLanguage>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/n2:supportedProperty">
						<xsl:variable name="var30_supportedProperty" as="node()" select="."/>
						<lgNaming:supportedProperty>
							<xsl:if test="$var30_supportedProperty/@localId">
								<xsl:attribute name="localId"><xsl:sequence select="xs:string(xs:NCName(xs:token(@localId)))"/></xsl:attribute>
							</xsl:if>
							<xsl:if test="$var30_supportedProperty/@urn">
								<xsl:attribute name="uri"><xsl:sequence select="xs:string(xs:anyURI(@urn))"/></xsl:attribute>
							</xsl:if>
							<xsl:sequence select="xs:string(.)"/>
						</lgNaming:supportedProperty>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/n2:supportedPropertyLink">
						<xsl:variable name="var32_supportedPropertyLink" as="node()" select="."/>
						<lgNaming:supportedPropertyLink>
							<xsl:if test="$var32_supportedPropertyLink/@localId">
								<xsl:attribute name="localId"><xsl:sequence select="xs:string(xs:NCName(xs:token(@localId)))"/></xsl:attribute>
							</xsl:if>
							<xsl:if test="$var32_supportedPropertyLink/@urn">
								<xsl:attribute name="uri"><xsl:sequence select="xs:string(xs:anyURI(@urn))"/></xsl:attribute>
							</xsl:if>
							<xsl:sequence select="xs:string(.)"/>
						</lgNaming:supportedPropertyLink>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/n2:supportedPropertyQualifier">
						<xsl:variable name="var34_supportedPropertyQualifier" as="node()" select="."/>
						<lgNaming:supportedPropertyQualifier>
							<xsl:if test="$var34_supportedPropertyQualifier/@localId">
								<xsl:attribute name="localId"><xsl:sequence select="xs:string(xs:NCName(xs:token(@localId)))"/></xsl:attribute>
							</xsl:if>
							<xsl:if test="$var34_supportedPropertyQualifier/@urn">
								<xsl:attribute name="uri"><xsl:sequence select="xs:string(xs:anyURI(@urn))"/></xsl:attribute>
							</xsl:if>
							<xsl:sequence select="xs:string(.)"/>
						</lgNaming:supportedPropertyQualifier>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/n2:supportedRepresentationalForm">
						<xsl:variable name="var36_supportedRepresentationalForm" as="node()" select="."/>
						<lgNaming:supportedRepresentationalForm>
							<xsl:if test="$var36_supportedRepresentationalForm/@localId">
								<xsl:attribute name="localId"><xsl:sequence select="xs:string(xs:NCName(xs:token(@localId)))"/></xsl:attribute>
							</xsl:if>
							<xsl:if test="$var36_supportedRepresentationalForm/@urn">
								<xsl:attribute name="uri"><xsl:sequence select="xs:string(xs:anyURI(@urn))"/></xsl:attribute>
							</xsl:if>
							<xsl:sequence select="xs:string(.)"/>
						</lgNaming:supportedRepresentationalForm>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/n2:supportedSource">
						<xsl:variable name="var38_supportedSource" as="node()" select="."/>
						<lgNaming:supportedSource>
							<xsl:if test="$var38_supportedSource/@localId">
								<xsl:attribute name="localId"><xsl:sequence select="xs:string(xs:NCName(xs:token(@localId)))"/></xsl:attribute>
							</xsl:if>
							<xsl:if test="$var38_supportedSource/@urn">
								<xsl:attribute name="uri"><xsl:sequence select="xs:string(xs:anyURI(@urn))"/></xsl:attribute>
							</xsl:if>
							<xsl:if test="$var38_supportedSource/@assemblyRule">
								<xsl:attribute name="assemblyRule"><xsl:sequence select="xs:string(@assemblyRule)"/></xsl:attribute>
							</xsl:if>
							<xsl:sequence select="xs:string(.)"/>
						</lgNaming:supportedSource>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/n2:supportedConceptStatus">
						<xsl:variable name="var40_supportedConceptStatus" as="node()" select="."/>
						<lgNaming:supportedStatus>
							<xsl:if test="$var40_supportedConceptStatus/@localId">
								<xsl:attribute name="localId"><xsl:sequence select="xs:string(xs:NCName(xs:token(@localId)))"/></xsl:attribute>
							</xsl:if>
							<xsl:if test="$var40_supportedConceptStatus/@urn">
								<xsl:attribute name="uri"><xsl:sequence select="xs:string(xs:anyURI(@urn))"/></xsl:attribute>
							</xsl:if>
							<xsl:sequence select="xs:string(.)"/>
						</lgNaming:supportedStatus>
					</xsl:for-each>
				</n:mappings>
				<xsl:for-each select="n2:properties">
					<n:properties>
						<xsl:for-each select="lgCommon2:property">
							<xsl:variable name="var44_property" as="node()" select="."/>
							<lgCommon:property>
								<xsl:if test="$var44_property/@propertyName">
									<xsl:attribute name="propertyName"><xsl:sequence select="xs:string(xs:NCName(xs:token(@propertyName)))"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var44_property/@propertyId">
									<xsl:attribute name="propertyId"><xsl:sequence select="xs:string(@propertyId)"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var44_property/@language">
									<xsl:attribute name="language"><xsl:sequence select="xs:string(xs:NCName(xs:token(@language)))"/></xsl:attribute>
								</xsl:if>
								<xsl:for-each select="lgCommon2:source">
									<xsl:variable name="var46_source" as="node()" select="."/>
									<lgCommon:source>
										<xsl:if test="$var46_source/@subRef">
											<xsl:attribute name="subRef"><xsl:sequence select="xs:string(@subRef)"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var46_source/@role">
											<xsl:attribute name="role"><xsl:sequence select="xs:string(xs:NCName(xs:string(@role)))"/></xsl:attribute>
										</xsl:if>
										<xsl:sequence select="xs:string(xs:NCName(xs:token(.)))"/>
									</lgCommon:source>
								</xsl:for-each>
								<xsl:for-each select="lgCommon2:usageContext">
									<lgCommon:usageContext>
										<xsl:sequence select="xs:string(xs:NCName(xs:token(.)))"/>
									</lgCommon:usageContext>
								</xsl:for-each>
								<xsl:for-each select="lgCommon2:propertyQualifier">
									<xsl:variable name="var50_propertyQualifier" as="node()" select="."/>
									<lgCommon:propertyQualifier>
										<xsl:if test="$var50_propertyQualifier/@propertyQualifierId">
											<xsl:attribute name="propertyQualifierName"><xsl:sequence select="xs:string(xs:NCName(xs:token(@propertyQualifierId)))"/></xsl:attribute>
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
											<xsl:sequence select="."/>
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
							<xsl:variable name="var56_concept" as="node()" select="."/>
							<lgCon:entity>
								<xsl:if test="$var56_concept/@isActive">
									<xsl:attribute name="isActive"><xsl:sequence select="xs:string(xs:boolean(@isActive))"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var56_concept/@conceptStatus">
									<xsl:attribute name="status"><xsl:sequence select="xs:string(xs:NCName(xs:token(@conceptStatus)))"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var56_concept/@id">
									<xsl:attribute name="entityCode"><xsl:sequence select="xs:string(@id)"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var56_concept/@codingSchemeId">
									<xsl:attribute name="entityCodeNamespace"><xsl:sequence select="xs:string(xs:NCName(xs:token(@codingSchemeId)))"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var56_concept/@isAnonymous">
									<xsl:attribute name="isAnonymous"><xsl:sequence select="xs:string(xs:boolean(@isAnonymous))"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var56_concept/@isDefined">
									<xsl:attribute name="isDefined"><xsl:sequence select="xs:string(xs:boolean(@isDefined))"/></xsl:attribute>
								</xsl:if>
								<xsl:for-each select="lgCommon2:entityDescription">
									<lgCommon:entityDescription>
										<xsl:for-each select="node()">
											<xsl:if test="self::text()">
												<xsl:sequence select="."/>
											</xsl:if>
										</xsl:for-each>
									</lgCommon:entityDescription>
								</xsl:for-each>
								<lgCon:entityType>
									<xsl:sequence select="xs:string(xs:NCName('concept'))"/>
								</lgCon:entityType>
								<xsl:for-each select="lgCon2:presentation">
									<xsl:variable name="var62_presentation" as="node()" select="."/>
									<lgCon:presentation>
										<xsl:if test="$var62_presentation/@propertyName">
											<xsl:attribute name="propertyName"><xsl:sequence select="xs:string(xs:NCName(xs:token(@propertyName)))"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var62_presentation/@propertyId">
											<xsl:attribute name="propertyId"><xsl:sequence select="xs:string(@propertyId)"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var62_presentation/@language">
											<xsl:attribute name="language"><xsl:sequence select="xs:string(xs:NCName(xs:token(@language)))"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var62_presentation/@isPreferred">
											<xsl:attribute name="isPreferred"><xsl:sequence select="xs:string(xs:boolean(@isPreferred))"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var62_presentation/@degreeOfFidelity">
											<xsl:attribute name="degreeOfFidelity"><xsl:sequence select="xs:string(xs:NCName(xs:token(@degreeOfFidelity)))"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var62_presentation/@matchIfNoContext">
											<xsl:attribute name="matchIfNoContext"><xsl:sequence select="xs:string(xs:boolean(@matchIfNoContext))"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var62_presentation/@representationalForm">
											<xsl:attribute name="representationalForm"><xsl:sequence select="xs:string(xs:NCName(xs:token(@representationalForm)))"/></xsl:attribute>
										</xsl:if>
										<xsl:for-each select="lgCommon2:source">
											<xsl:variable name="var64_source" as="node()" select="."/>
											<lgCommon:source>
												<xsl:if test="$var64_source/@subRef">
													<xsl:attribute name="subRef"><xsl:sequence select="xs:string(@subRef)"/></xsl:attribute>
												</xsl:if>
												<xsl:if test="$var64_source/@role">
													<xsl:attribute name="role"><xsl:sequence select="xs:string(xs:NCName(xs:string(@role)))"/></xsl:attribute>
												</xsl:if>
												<xsl:sequence select="xs:string(xs:NCName(xs:token(.)))"/>
											</lgCommon:source>
										</xsl:for-each>
										<xsl:for-each select="lgCommon2:usageContext">
											<lgCommon:usageContext>
												<xsl:sequence select="xs:string(xs:NCName(xs:token(.)))"/>
											</lgCommon:usageContext>
										</xsl:for-each>
										<xsl:for-each select="lgCommon2:propertyQualifier">
											<xsl:variable name="var68_propertyQualifier" as="node()" select="."/>
											<lgCommon:propertyQualifier>
												<xsl:if test="$var68_propertyQualifier/@propertyQualifierId">
													<xsl:attribute name="propertyQualifierName"><xsl:sequence select="xs:string(xs:NCName(xs:token(@propertyQualifierId)))"/></xsl:attribute>
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
													<xsl:sequence select="."/>
												</xsl:if>
											</xsl:for-each>
										</lgCommon:value>
									</lgCon:presentation>
								</xsl:for-each>
								<xsl:for-each select="lgCon2:definition">
									<xsl:variable name="var72_definition" as="node()" select="."/>
									<lgCon:definition>
										<xsl:if test="$var72_definition/@propertyName">
											<xsl:attribute name="propertyName"><xsl:sequence select="xs:string(xs:NCName(xs:token(@propertyName)))"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var72_definition/@propertyId">
											<xsl:attribute name="propertyId"><xsl:sequence select="xs:string(@propertyId)"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var72_definition/@language">
											<xsl:attribute name="language"><xsl:sequence select="xs:string(xs:NCName(xs:token(@language)))"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var72_definition/@isPreferred">
											<xsl:attribute name="isPreferred"><xsl:sequence select="xs:string(xs:boolean(@isPreferred))"/></xsl:attribute>
										</xsl:if>
										<xsl:for-each select="lgCommon2:source">
											<xsl:variable name="var74_source" as="node()" select="."/>
											<lgCommon:source>
												<xsl:if test="$var74_source/@subRef">
													<xsl:attribute name="subRef"><xsl:sequence select="xs:string(@subRef)"/></xsl:attribute>
												</xsl:if>
												<xsl:if test="$var74_source/@role">
													<xsl:attribute name="role"><xsl:sequence select="xs:string(xs:NCName(xs:string(@role)))"/></xsl:attribute>
												</xsl:if>
												<xsl:sequence select="xs:string(xs:NCName(xs:token(.)))"/>
											</lgCommon:source>
										</xsl:for-each>
										<xsl:for-each select="lgCommon2:usageContext">
											<lgCommon:usageContext>
												<xsl:sequence select="xs:string(xs:NCName(xs:token(.)))"/>
											</lgCommon:usageContext>
										</xsl:for-each>
										<xsl:for-each select="lgCommon2:propertyQualifier">
											<xsl:variable name="var78_propertyQualifier" as="node()" select="."/>
											<lgCommon:propertyQualifier>
												<xsl:if test="$var78_propertyQualifier/@propertyQualifierId">
													<xsl:attribute name="propertyQualifierName"><xsl:sequence select="xs:string(xs:NCName(xs:token(@propertyQualifierId)))"/></xsl:attribute>
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
													<xsl:sequence select="."/>
												</xsl:if>
											</xsl:for-each>
										</lgCommon:value>
									</lgCon:definition>
								</xsl:for-each>
								<xsl:for-each select="lgCon2:comment">
									<xsl:variable name="var82_comment" as="node()" select="."/>
									<lgCon:comment>
										<xsl:if test="$var82_comment/@propertyName">
											<xsl:attribute name="propertyName"><xsl:sequence select="xs:string(xs:NCName(xs:token(@propertyName)))"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var82_comment/@propertyId">
											<xsl:attribute name="propertyId"><xsl:sequence select="xs:string(@propertyId)"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var82_comment/@language">
											<xsl:attribute name="language"><xsl:sequence select="xs:string(xs:NCName(xs:token(@language)))"/></xsl:attribute>
										</xsl:if>
										<xsl:for-each select="lgCommon2:source">
											<xsl:variable name="var84_source" as="node()" select="."/>
											<lgCommon:source>
												<xsl:if test="$var84_source/@subRef">
													<xsl:attribute name="subRef"><xsl:sequence select="xs:string(@subRef)"/></xsl:attribute>
												</xsl:if>
												<xsl:if test="$var84_source/@role">
													<xsl:attribute name="role"><xsl:sequence select="xs:string(xs:NCName(xs:string(@role)))"/></xsl:attribute>
												</xsl:if>
												<xsl:sequence select="xs:string(xs:NCName(xs:token(.)))"/>
											</lgCommon:source>
										</xsl:for-each>
										<xsl:for-each select="lgCommon2:usageContext">
											<lgCommon:usageContext>
												<xsl:sequence select="xs:string(xs:NCName(xs:token(.)))"/>
											</lgCommon:usageContext>
										</xsl:for-each>
										<xsl:for-each select="lgCommon2:propertyQualifier">
											<xsl:variable name="var88_propertyQualifier" as="node()" select="."/>
											<lgCommon:propertyQualifier>
												<xsl:if test="$var88_propertyQualifier/@propertyQualifierId">
													<xsl:attribute name="propertyQualifierName"><xsl:sequence select="xs:string(xs:NCName(xs:token(@propertyQualifierId)))"/></xsl:attribute>
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
													<xsl:sequence select="."/>
												</xsl:if>
											</xsl:for-each>
										</lgCommon:value>
									</lgCon:comment>
								</xsl:for-each>
								<xsl:for-each select="lgCon2:conceptProperty">
									<xsl:variable name="var92_conceptProperty" as="node()" select="."/>
									<lgCon:property>
										<xsl:if test="$var92_conceptProperty/@propertyName">
											<xsl:attribute name="propertyName"><xsl:sequence select="xs:string(xs:NCName(xs:token(@propertyName)))"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var92_conceptProperty/@propertyId">
											<xsl:attribute name="propertyId"><xsl:sequence select="xs:string(@propertyId)"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var92_conceptProperty/@language">
											<xsl:attribute name="language"><xsl:sequence select="xs:string(xs:NCName(xs:token(@language)))"/></xsl:attribute>
										</xsl:if>
										<xsl:for-each select="lgCommon2:source">
											<xsl:variable name="var94_source" as="node()" select="."/>
											<lgCommon:source>
												<xsl:if test="$var94_source/@subRef">
													<xsl:attribute name="subRef"><xsl:sequence select="xs:string(@subRef)"/></xsl:attribute>
												</xsl:if>
												<xsl:if test="$var94_source/@role">
													<xsl:attribute name="role"><xsl:sequence select="xs:string(xs:NCName(xs:string(@role)))"/></xsl:attribute>
												</xsl:if>
												<xsl:sequence select="xs:string(xs:NCName(xs:token(.)))"/>
											</lgCommon:source>
										</xsl:for-each>
										<xsl:for-each select="lgCommon2:usageContext">
											<lgCommon:usageContext>
												<xsl:sequence select="xs:string(xs:NCName(xs:token(.)))"/>
											</lgCommon:usageContext>
										</xsl:for-each>
										<xsl:for-each select="lgCommon2:propertyQualifier">
											<xsl:variable name="var98_propertyQualifier" as="node()" select="."/>
											<lgCommon:propertyQualifier>
												<xsl:if test="$var98_propertyQualifier/@propertyQualifierId">
													<xsl:attribute name="propertyQualifierName"><xsl:sequence select="xs:string(xs:NCName(xs:token(@propertyQualifierId)))"/></xsl:attribute>
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
													<xsl:sequence select="."/>
												</xsl:if>
											</xsl:for-each>
										</lgCommon:value>
									</lgCon:property>
								</xsl:for-each>
								<xsl:for-each select="lgCon2:propertyLink">
									<xsl:variable name="var102_propertyLink" as="node()" select="."/>
									<lgCon:propertyLink>
										<xsl:if test="$var102_propertyLink/@sourceProperty">
											<xsl:attribute name="sourceProperty"><xsl:sequence select="xs:string(@sourceProperty)"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var102_propertyLink/@link">
											<xsl:attribute name="propertyLink"><xsl:sequence select="xs:string(xs:NCName(xs:token(@link)))"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var102_propertyLink/@targetProperty">
											<xsl:attribute name="targetProperty"><xsl:sequence select="xs:string(@targetProperty)"/></xsl:attribute>
										</xsl:if>
									</lgCon:propertyLink>
								</xsl:for-each>
							</lgCon:entity>
						</xsl:for-each>
					</n:entities>
				</xsl:for-each>
				<xsl:for-each select="n2:relations">
					<xsl:variable name="var104_relations" as="node()" select="."/>
					<n:relations>
						<xsl:if test="$var104_relations/@dc">
							<xsl:attribute name="containerName"><xsl:sequence select="xs:string(xs:NCName(xs:string(@dc)))"/></xsl:attribute>
						</xsl:if>
						<xsl:if test="$var104_relations/@isNative">
							<xsl:attribute name="isNative"><xsl:sequence select="xs:string(xs:boolean(@isNative))"/></xsl:attribute>
						</xsl:if>
						<xsl:for-each select="lgCommon2:entityDescription">
							<lgCommon:entityDescription>
								<xsl:for-each select="node()">
									<xsl:if test="self::text()">
										<xsl:sequence select="."/>
									</xsl:if>
								</xsl:for-each>
							</lgCommon:entityDescription>
						</xsl:for-each>
						<xsl:for-each select="lgRel2:source">
							<xsl:variable name="var110_source" as="node()" select="."/>
							<lgRel:source>
								<xsl:if test="$var110_source/@subRef">
									<xsl:attribute name="subRef"><xsl:sequence select="xs:string(@subRef)"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var110_source/@role">
									<xsl:attribute name="role"><xsl:sequence select="xs:string(xs:NCName(xs:string(@role)))"/></xsl:attribute>
								</xsl:if>
								<xsl:sequence select="xs:string(xs:NCName(xs:token(.)))"/>
							</lgRel:source>
						</xsl:for-each>
						<xsl:for-each select="lgRel2:association">
							<xsl:variable name="var112_association" as="node()" select="."/>
							<lgRel:association>
								<xsl:if test="$var112_association/@id">
									<xsl:attribute name="entityCode"><xsl:sequence select="xs:string(@id)"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var112_association/@codingSchemeId">
									<xsl:attribute name="entityCodeNamespace"><xsl:sequence select="xs:string(xs:NCName(xs:token(@codingSchemeId)))"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var112_association/@id">
									<xsl:attribute name="associationName"><xsl:sequence select="xs:string(xs:NCName(xs:string(@id)))"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var112_association/@forwardName">
									<xsl:attribute name="forwardName"><xsl:sequence select="xs:string(@forwardName)"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var112_association/@reverseName">
									<xsl:attribute name="reverseName"><xsl:sequence select="xs:string(@reverseName)"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var112_association/@inverse">
									<xsl:attribute name="inverse"><xsl:sequence select="xs:string(xs:NCName(xs:string(@inverse)))"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var112_association/@isNavigable">
									<xsl:attribute name="isNavigable"><xsl:sequence select="xs:string(xs:boolean(@isNavigable))"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var112_association/@isTransitive">
									<xsl:attribute name="isTransitive"><xsl:sequence select="xs:string(xs:boolean(@isTransitive))"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var112_association/@isAntiTransitive">
									<xsl:attribute name="isAntiTransitive"><xsl:sequence select="xs:string(xs:boolean(@isAntiTransitive))"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var112_association/@isSymmetric">
									<xsl:attribute name="isSymmetric"><xsl:sequence select="xs:string(xs:boolean(@isSymmetric))"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var112_association/@isAntiSymmetric">
									<xsl:attribute name="isAntiSymmetric"><xsl:sequence select="xs:string(xs:boolean(@isAntiSymmetric))"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var112_association/@isReflexive">
									<xsl:attribute name="isReflexive"><xsl:sequence select="xs:string(xs:boolean(@isReflexive))"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var112_association/@isAntiReflexive">
									<xsl:attribute name="isAntiReflexive"><xsl:sequence select="xs:string(xs:boolean(@isAntiReflexive))"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var112_association/@isFunctional">
									<xsl:attribute name="isFunctional"><xsl:sequence select="xs:string(xs:boolean(@isFunctional))"/></xsl:attribute>
								</xsl:if>
								<xsl:if test="$var112_association/@isReverseFunctional">
									<xsl:attribute name="isReverseFunctional"><xsl:sequence select="xs:string(xs:boolean(@isReverseFunctional))"/></xsl:attribute>
								</xsl:if>
								<xsl:for-each select="lgCommon2:entityDescription">
									<lgCommon:entityDescription>
										<xsl:for-each select="node()">
											<xsl:if test="self::text()">
												<xsl:sequence select="."/>
											</xsl:if>
										</xsl:for-each>
									</lgCommon:entityDescription>
								</xsl:for-each>
								<xsl:for-each select="lgRel2:sourceEntity">
									<xsl:variable name="var118_sourceEntity" as="node()" select="."/>
									<lgRel:source>
										<xsl:if test="$var118_sourceEntity/@sourceCodingScheme">
											<xsl:attribute name="sourceEntityCodeNamespace"><xsl:sequence select="xs:string(xs:NCName(xs:token(@sourceCodingScheme)))"/></xsl:attribute>
										</xsl:if>
										<xsl:if test="$var118_sourceEntity/@sourceId">
											<xsl:attribute name="sourceEntityCode"><xsl:sequence select="xs:string(@sourceId)"/></xsl:attribute>
										</xsl:if>
										<xsl:for-each select="lgRel2:targetEntity">
											<xsl:variable name="var120_targetEntity" as="node()" select="."/>
											<lgRel:target>
												<xsl:if test="$var120_targetEntity/@targetId">
													<xsl:attribute name="targetEntityCode"><xsl:sequence select="xs:string(@targetId)"/></xsl:attribute>
												</xsl:if>
												<xsl:if test="$var120_targetEntity/@targetCodingScheme">
													<xsl:attribute name="targetEntityCodeNamespace"><xsl:sequence select="xs:string(xs:NCName(xs:token(@targetCodingScheme)))"/></xsl:attribute>
												</xsl:if>
												<xsl:for-each select="lgRel2:associationQualification">
													<xsl:variable name="var122_associationQualification" as="node()" select="."/>
													<lgRel:associationQualification>
														<xsl:if test="$var122_associationQualification/@associationQualifier">
															<xsl:attribute name="associationQualifier"><xsl:sequence select="xs:string(xs:NCName(xs:token(@associationQualifier)))"/></xsl:attribute>
														</xsl:if>
													</lgRel:associationQualification>
												</xsl:for-each>
											</lgRel:target>
										</xsl:for-each>
										<xsl:for-each select="lgRel2:targetDataValue">
											<xsl:variable name="var124_targetDataValue" as="node()" select="."/>
											<lgRel:targetData>
												<xsl:if test="$var124_targetDataValue/@dataId">
													<xsl:attribute name="associationInstanceId"><xsl:sequence select="xs:string(@dataId)"/></xsl:attribute>
												</xsl:if>
												<xsl:for-each select="lgRel2:associationQualification">
													<xsl:variable name="var126_associationQualification" as="node()" select="."/>
													<lgRel:associationQualification>
														<xsl:if test="$var126_associationQualification/@associationQualifier">
															<xsl:attribute name="associationQualifier"><xsl:sequence select="xs:string(xs:NCName(xs:token(@associationQualifier)))"/></xsl:attribute>
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
