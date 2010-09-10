<?xml version="1.0" encoding="UTF-8"?>
<!--
Transforms documents defined against LexGrid model revision 2009/01
to LexGrid model revision 2008/01.
-->
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:lgBuiltin="http://LexGrid.org/schema/2008/01/LexGrid/builtins" xmlns:n="http://LexGrid.org/schema/2008/01/LexGrid/codingSchemes" xmlns:lgCommon="http://LexGrid.org/schema/2008/01/LexGrid/commonTypes" xmlns:lgCon="http://LexGrid.org/schema/2008/01/LexGrid/concepts" xmlns:ldap="http://LexGrid.org/schema/2008/01/LexGrid/ldap" xmlns:lgNaming="http://LexGrid.org/schema/2008/01/LexGrid/naming" xmlns:lgRel="http://LexGrid.org/schema/2008/01/LexGrid/relations" xmlns:lgVer="http://LexGrid.org/schema/2008/01/LexGrid/versions" xmlns:lgBuiltin2="http://LexGrid.org/schema/2009/01/LexGrid/builtins" xmlns:n2="http://LexGrid.org/schema/2009/01/LexGrid/codingSchemes" xmlns:lgCommon2="http://LexGrid.org/schema/2009/01/LexGrid/commonTypes" xmlns:lgCon2="http://LexGrid.org/schema/2009/01/LexGrid/concepts" xmlns:lgNaming2="http://LexGrid.org/schema/2009/01/LexGrid/naming" xmlns:lgRel2="http://LexGrid.org/schema/2009/01/LexGrid/relations" xmlns:lgVD="http://LexGrid.org/schema/2009/01/LexGrid/valueDomains" xmlns:lgVer2="http://LexGrid.org/schema/2009/01/LexGrid/versions" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:grp="http://www.altova.com/Mapforce/grouping" exclude-result-prefixes="fn grp lgBuiltin2 lgCommon2 lgCon2 lgNaming2 lgRel2 lgVD lgVer2 n2 xs xsi xsl" xmlns="http://LexGrid.org/schema/2008/01/LexGrid/codingSchemes">
	<xsl:namespace-alias stylesheet-prefix="n" result-prefix="#default"/>
	<xsl:output method="xml" encoding="UTF-8" indent="yes"/>
	<xsl:template match="/">
		<n:codingScheme>
			<xsl:attribute name="xsi:schemaLocation" separator=" ">
				<xsl:sequence select="'http://LexGrid.org/schema/2008/01/LexGrid/codingSchemes http://LexGrid.org/schema/2008/01/LexGrid/codingSchemes.xsd'"/>
			</xsl:attribute>
			<xsl:variable name="var1_instance" as="node()" select="."/>
			<xsl:for-each select="$var1_instance/n2:codingScheme">
				<xsl:variable name="var2_codingScheme" as="node()" select="."/>
				<xsl:attribute name="codingScheme">
					<xsl:sequence select="xs:string(xs:token(@codingSchemeName))"/>
				</xsl:attribute>
				<xsl:if test="$var2_codingScheme/@formalName">
					<xsl:attribute name="formalName">
						<xsl:sequence select="xs:string(@formalName)"/>
					</xsl:attribute>
				</xsl:if>
				<xsl:attribute name="registeredName">
					<xsl:sequence select="xs:string(xs:anyURI(@codingSchemeURI))"/>
				</xsl:attribute>
				<xsl:if test="$var2_codingScheme/@defaultLanguage">
					<xsl:attribute name="defaultLanguage">
						<xsl:sequence select="xs:string(xs:token(@defaultLanguage))"/>
					</xsl:attribute>
				</xsl:if>
				<xsl:if test="$var2_codingScheme/@representsVersion">
					<xsl:attribute name="representsVersion">
						<xsl:sequence select="xs:string(@representsVersion)"/>
					</xsl:attribute>
				</xsl:if>
				<xsl:if test="$var2_codingScheme/@approxNumConcepts">
					<xsl:attribute name="approxNumConcepts">
						<xsl:sequence select="xs:string(xs:integer(@approxNumConcepts))"/>
					</xsl:attribute>
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
							<xsl:attribute name="subRef">
								<xsl:sequence select="xs:string(@subRef)"/>
							</xsl:attribute>
						</xsl:if>
						<xsl:if test="$var10_source/@role">
							<xsl:attribute name="role">
								<xsl:sequence select="xs:string(xs:token(@role))"/>
							</xsl:attribute>
						</xsl:if>
						<xsl:sequence select="xs:string(xs:token(.))"/>
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
					<xsl:attribute name="dc">
						<xsl:sequence select="'mappings'"/>
					</xsl:attribute>
					<xsl:for-each select="n2:mappings/lgNaming2:supportedLanguage">
						<xsl:variable name="var16_supportedLanguage" as="node()" select="."/>
						<n:supportedLanguage>
							<xsl:attribute name="localId">
								<xsl:sequence select="xs:string(xs:token(@localId))"/>
							</xsl:attribute>
							<xsl:if test="$var16_supportedLanguage/@uri">
								<xsl:attribute name="urn">
									<xsl:sequence select="xs:string(xs:anyURI(@uri))"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:sequence select="xs:string(.)"/>
						</n:supportedLanguage>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/lgNaming2:supportedDataType">
						<xsl:variable name="var18_supportedDataType" as="node()" select="."/>
						<n:supportedFormat>
							<xsl:attribute name="localId">
								<xsl:sequence select="xs:string(xs:token(@localId))"/>
							</xsl:attribute>
							<xsl:if test="$var18_supportedDataType/@uri">
								<xsl:attribute name="urn">
									<xsl:sequence select="xs:string(xs:anyURI(@uri))"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:sequence select="xs:string(.)"/>
						</n:supportedFormat>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/lgNaming2:supportedProperty">
						<xsl:variable name="var20_supportedProperty" as="node()" select="."/>
						<n:supportedProperty>
							<xsl:attribute name="localId">
								<xsl:sequence select="xs:string(xs:token(@localId))"/>
							</xsl:attribute>
							<xsl:if test="$var20_supportedProperty/@uri">
								<xsl:attribute name="urn">
									<xsl:sequence select="xs:string(xs:anyURI(@uri))"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:sequence select="xs:string(.)"/>
						</n:supportedProperty>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/lgNaming2:supportedCodingScheme">
						<xsl:variable name="var22_supportedCodingScheme" as="node()" select="."/>
						<n:supportedCodingScheme>
							<xsl:attribute name="localId">
								<xsl:sequence select="xs:string(xs:token(@localId))"/>
							</xsl:attribute>
							<xsl:if test="$var22_supportedCodingScheme/@uri">
								<xsl:attribute name="urn">
									<xsl:sequence select="xs:string(xs:anyURI(@uri))"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:if test="$var22_supportedCodingScheme/@isImported">
								<xsl:attribute name="isImported">
									<xsl:sequence select="xs:string(xs:boolean(@isImported))"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:sequence select="xs:string(.)"/>
						</n:supportedCodingScheme>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/lgNaming2:supportedSource">
						<xsl:variable name="var24_supportedSource" as="node()" select="."/>
						<n:supportedSource>
							<xsl:attribute name="localId">
								<xsl:sequence select="xs:string(xs:token(@localId))"/>
							</xsl:attribute>
							<xsl:if test="$var24_supportedSource/@uri">
								<xsl:attribute name="urn">
									<xsl:sequence select="xs:string(xs:anyURI(@uri))"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:if test="$var24_supportedSource/@assemblyRule">
								<xsl:attribute name="assemblyRule">
									<xsl:sequence select="xs:string(@assemblyRule)"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:sequence select="xs:string(.)"/>
						</n:supportedSource>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/lgNaming2:supportedAssociation">
						<xsl:variable name="var26_supportedAssociation" as="node()" select="."/>
						<n:supportedAssociation>
							<xsl:attribute name="localId">
								<xsl:sequence select="xs:string(xs:token(@localId))"/>
							</xsl:attribute>
							<xsl:if test="$var26_supportedAssociation/@uri">
								<xsl:attribute name="urn">
									<xsl:sequence select="xs:string(xs:anyURI(@uri))"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:sequence select="xs:string(.)"/>
						</n:supportedAssociation>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/lgNaming2:supportedContext">
						<xsl:variable name="var28_supportedContext" as="node()" select="."/>
						<n:supportedContext>
							<xsl:attribute name="localId">
								<xsl:sequence select="xs:string(xs:token(@localId))"/>
							</xsl:attribute>
							<xsl:if test="$var28_supportedContext/@uri">
								<xsl:attribute name="urn">
									<xsl:sequence select="xs:string(xs:anyURI(@uri))"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:sequence select="xs:string(.)"/>
						</n:supportedContext>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/lgNaming2:supportedAssociationQualifier">
						<xsl:variable name="var30_supportedAssociationQualifier" as="node()" select="."/>
						<n:supportedAssociationQualifier>
							<xsl:attribute name="localId">
								<xsl:sequence select="xs:string(xs:token(@localId))"/>
							</xsl:attribute>
							<xsl:if test="$var30_supportedAssociationQualifier/@uri">
								<xsl:attribute name="urn">
									<xsl:sequence select="xs:string(xs:anyURI(@uri))"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:sequence select="xs:string(.)"/>
						</n:supportedAssociationQualifier>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/lgNaming2:supportedStatus">
						<xsl:variable name="var32_supportedStatus" as="node()" select="."/>
						<n:supportedConceptStatus>
							<xsl:attribute name="localId">
								<xsl:sequence select="xs:string(xs:token(@localId))"/>
							</xsl:attribute>
							<xsl:if test="$var32_supportedStatus/@uri">
								<xsl:attribute name="urn">
									<xsl:sequence select="xs:string(xs:anyURI(@uri))"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:sequence select="xs:string(.)"/>
						</n:supportedConceptStatus>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/lgNaming2:supportedRepresentationalForm">
						<xsl:variable name="var34_supportedRepresentationalForm" as="node()" select="."/>
						<n:supportedRepresentationalForm>
							<xsl:attribute name="localId">
								<xsl:sequence select="xs:string(xs:token(@localId))"/>
							</xsl:attribute>
							<xsl:if test="$var34_supportedRepresentationalForm/@uri">
								<xsl:attribute name="urn">
									<xsl:sequence select="xs:string(xs:anyURI(@uri))"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:sequence select="xs:string(.)"/>
						</n:supportedRepresentationalForm>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/lgNaming2:supportedPropertyLink">
						<xsl:variable name="var36_supportedPropertyLink" as="node()" select="."/>
						<n:supportedPropertyLink>
							<xsl:attribute name="localId">
								<xsl:sequence select="xs:string(xs:token(@localId))"/>
							</xsl:attribute>
							<xsl:if test="$var36_supportedPropertyLink/@uri">
								<xsl:attribute name="urn">
									<xsl:sequence select="xs:string(xs:anyURI(@uri))"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:sequence select="xs:string(.)"/>
						</n:supportedPropertyLink>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/lgNaming2:supportedDegreeOfFidelity">
						<xsl:variable name="var38_supportedDegreeOfFidelity" as="node()" select="."/>
						<n:supportedDegreeOfFidelity>
							<xsl:attribute name="localId">
								<xsl:sequence select="xs:string(xs:token(@localId))"/>
							</xsl:attribute>
							<xsl:if test="$var38_supportedDegreeOfFidelity/@uri">
								<xsl:attribute name="urn">
									<xsl:sequence select="xs:string(xs:anyURI(@uri))"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:sequence select="xs:string(.)"/>
						</n:supportedDegreeOfFidelity>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/lgNaming2:supportedPropertyQualifier">
						<xsl:variable name="var40_supportedPropertyQualifier" as="node()" select="."/>
						<n:supportedPropertyQualifier>
							<xsl:attribute name="localId">
								<xsl:sequence select="xs:string(xs:token(@localId))"/>
							</xsl:attribute>
							<xsl:if test="$var40_supportedPropertyQualifier/@uri">
								<xsl:attribute name="urn">
									<xsl:sequence select="xs:string(xs:anyURI(@uri))"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:sequence select="xs:string(.)"/>
						</n:supportedPropertyQualifier>
					</xsl:for-each>
					<xsl:for-each select="n2:mappings/lgNaming2:supportedHierarchy">
						<xsl:variable name="var42_supportedHierarchy" as="node()" select="."/>
						<n:supportedHierarchy>
							<xsl:attribute name="localId">
								<xsl:sequence select="xs:string(xs:token(@localId))"/>
							</xsl:attribute>
							<xsl:if test="$var42_supportedHierarchy/@uri">
								<xsl:attribute name="urn">
									<xsl:sequence select="xs:string(xs:anyURI(@uri))"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:attribute name="associationIds">
								<xsl:sequence select="xs:string(xs:string(xs:string(xs:string(@associationNames))))"/>
							</xsl:attribute>
							<xsl:attribute name="rootCode">
								<xsl:sequence select="xs:string(@rootCode)"/>
							</xsl:attribute>
							<xsl:attribute name="isForwardNavigable">
								<xsl:sequence select="xs:string(xs:boolean(@isForwardNavigable))"/>
							</xsl:attribute>
							<xsl:sequence select="xs:string(.)"/>
						</n:supportedHierarchy>
					</xsl:for-each>
				</n:mappings>
				<xsl:for-each select="n2:properties">
					<n:properties>
						<xsl:attribute name="dc">
							<xsl:sequence select="'properties'"/>
						</xsl:attribute>
						<xsl:for-each select="lgCommon2:property">
							<xsl:variable name="var46_property" as="node()" select="."/>
							<lgCommon:property>
								<xsl:attribute name="propertyName">
									<xsl:sequence select="xs:string(xs:token(@propertyName))"/>
								</xsl:attribute>
								<xsl:if test="$var46_property/@propertyId">
									<xsl:attribute name="propertyId">
										<xsl:sequence select="xs:string(@propertyId)"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var46_property/@language">
									<xsl:attribute name="language">
										<xsl:sequence select="xs:string(xs:token(@language))"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var46_property/@propertyType">
									<xsl:attribute name="format">
										<xsl:sequence select="xs:string(xs:token(@propertyType))"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:for-each select="lgCommon2:source">
									<xsl:variable name="var48_source" as="node()" select="."/>
									<lgCommon:source>
										<xsl:if test="$var48_source/@subRef">
											<xsl:attribute name="subRef">
												<xsl:sequence select="xs:string(@subRef)"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:if test="$var48_source/@role">
											<xsl:attribute name="role">
												<xsl:sequence select="xs:string(xs:token(@role))"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:sequence select="xs:string(xs:token(.))"/>
									</lgCommon:source>
								</xsl:for-each>
								<xsl:for-each select="lgCommon2:usageContext">
									<lgCommon:usageContext>
										<xsl:sequence select="xs:string(xs:token(.))"/>
									</lgCommon:usageContext>
								</xsl:for-each>
								<xsl:for-each select="lgCommon2:propertyQualifier">
									<lgCommon:propertyQualifier>
										<xsl:attribute name="propertyQualifierId">
											<xsl:sequence select="xs:string(xs:token(@propertyQualifierName))"/>
										</xsl:attribute>
									</lgCommon:propertyQualifier>
								</xsl:for-each>
								<xsl:for-each select="lgCommon2:value">
									<lgCommon:text>
										<xsl:for-each select="node()">
											<xsl:if test="self::text()">
												<xsl:sequence select="."/>
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
							<xsl:sequence select="'concepts'"/>
						</xsl:attribute>
						<xsl:for-each select="lgCon2:entity">
							<xsl:variable name="var60_entity" as="node()" select="."/>
							<lgCon:concept>
								<xsl:attribute name="id">
									<xsl:sequence select="xs:string(@entityCode)"/>
								</xsl:attribute>
								<xsl:if test="$var60_entity/@entityCodeNamespace">
									<xsl:attribute name="codingSchemeId">
										<xsl:sequence select="xs:string(xs:token(@entityCodeNamespace))"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var60_entity/@isActive">
									<xsl:attribute name="isActive">
										<xsl:sequence select="xs:string(xs:boolean(@isActive))"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var60_entity/@status">
									<xsl:attribute name="conceptStatus">
										<xsl:sequence select="xs:string(xs:token(@status))"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var60_entity/@isAnonymous">
									<xsl:attribute name="isAnonymous">
										<xsl:sequence select="xs:string(xs:boolean(@isAnonymous))"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var60_entity/@isDefined">
									<xsl:attribute name="isDefined">
										<xsl:sequence select="xs:string(xs:boolean(@isDefined))"/>
									</xsl:attribute>
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
								<xsl:for-each select="lgCon2:presentation">
									<xsl:variable name="var66_presentation" as="node()" select="."/>
									<lgCon:presentation>
										<xsl:attribute name="propertyName">
											<xsl:sequence select="xs:string(xs:token(@propertyName))"/>
										</xsl:attribute>
										<xsl:if test="$var66_presentation/@propertyId">
											<xsl:attribute name="propertyId">
												<xsl:sequence select="xs:string(@propertyId)"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:if test="$var66_presentation/@language">
											<xsl:attribute name="language">
												<xsl:sequence select="xs:string(xs:token(@language))"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:for-each select="lgCommon2:value">
											<xsl:variable name="var68_value" as="node()" select="."/>
											<xsl:if test="$var68_value/@dataType">
												<xsl:attribute name="format">
													<xsl:sequence select="xs:string(xs:token(@dataType))"/>
												</xsl:attribute>
											</xsl:if>
										</xsl:for-each>
										<xsl:if test="$var66_presentation/@isPreferred">
											<xsl:attribute name="isPreferred">
												<xsl:sequence select="xs:string(xs:boolean(@isPreferred))"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:if test="$var66_presentation/@degreeOfFidelity">
											<xsl:attribute name="degreeOfFidelity">
												<xsl:sequence select="xs:string(xs:token(@degreeOfFidelity))"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:if test="$var66_presentation/@matchIfNoContext">
											<xsl:attribute name="matchIfNoContext">
												<xsl:sequence select="xs:string(xs:boolean(@matchIfNoContext))"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:if test="$var66_presentation/@representationalForm">
											<xsl:attribute name="representationalForm">
												<xsl:sequence select="xs:string(xs:token(@representationalForm))"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:for-each select="lgCommon2:source">
											<xsl:variable name="var70_source" as="node()" select="."/>
											<lgCommon:source>
												<xsl:if test="$var70_source/@subRef">
													<xsl:attribute name="subRef">
														<xsl:sequence select="xs:string(@subRef)"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:if test="$var70_source/@role">
													<xsl:attribute name="role">
														<xsl:sequence select="xs:string(xs:token(@role))"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:sequence select="xs:string(xs:token(.))"/>
											</lgCommon:source>
										</xsl:for-each>
										<xsl:for-each select="lgCommon2:usageContext">
											<lgCommon:usageContext>
												<xsl:sequence select="xs:string(xs:token(.))"/>
											</lgCommon:usageContext>
										</xsl:for-each>
										<xsl:for-each select="lgCommon2:propertyQualifier">
											<lgCommon:propertyQualifier>
												<xsl:attribute name="propertyQualifierId">
													<xsl:sequence select="xs:string(xs:token(@propertyQualifierName))"/>
												</xsl:attribute>
											</lgCommon:propertyQualifier>
										</xsl:for-each>
										<xsl:for-each select="lgCommon2:value">
											<lgCommon:text>
												<xsl:for-each select="node()">
													<xsl:if test="self::text()">
														<xsl:sequence select="."/>
													</xsl:if>
												</xsl:for-each>
											</lgCommon:text>
										</xsl:for-each>
									</lgCon:presentation>
								</xsl:for-each>
								<xsl:for-each select="lgCon2:definition">
									<xsl:variable name="var80_definition" as="node()" select="."/>
									<lgCon:definition>
										<xsl:attribute name="propertyName">
											<xsl:sequence select="xs:string(xs:token(@propertyName))"/>
										</xsl:attribute>
										<xsl:if test="$var80_definition/@propertyId">
											<xsl:attribute name="propertyId">
												<xsl:sequence select="xs:string(@propertyId)"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:if test="$var80_definition/@language">
											<xsl:attribute name="language">
												<xsl:sequence select="xs:string(xs:token(@language))"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:for-each select="lgCommon2:value">
											<xsl:variable name="var82_value" as="node()" select="."/>
											<xsl:if test="$var82_value/@dataType">
												<xsl:attribute name="format">
													<xsl:sequence select="xs:string(xs:token(@dataType))"/>
												</xsl:attribute>
											</xsl:if>
										</xsl:for-each>
										<xsl:if test="$var80_definition/@isPreferred">
											<xsl:attribute name="isPreferred">
												<xsl:sequence select="xs:string(xs:boolean(@isPreferred))"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:for-each select="lgCommon2:source">
											<xsl:variable name="var84_source" as="node()" select="."/>
											<lgCommon:source>
												<xsl:if test="$var84_source/@subRef">
													<xsl:attribute name="subRef">
														<xsl:sequence select="xs:string(@subRef)"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:if test="$var84_source/@role">
													<xsl:attribute name="role">
														<xsl:sequence select="xs:string(xs:token(@role))"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:sequence select="xs:string(xs:token(.))"/>
											</lgCommon:source>
										</xsl:for-each>
										<xsl:for-each select="lgCommon2:usageContext">
											<lgCommon:usageContext>
												<xsl:sequence select="xs:string(xs:token(.))"/>
											</lgCommon:usageContext>
										</xsl:for-each>
										<xsl:for-each select="lgCommon2:propertyQualifier">
											<lgCommon:propertyQualifier>
												<xsl:attribute name="propertyQualifierId">
													<xsl:sequence select="xs:string(xs:token(@propertyQualifierName))"/>
												</xsl:attribute>
											</lgCommon:propertyQualifier>
										</xsl:for-each>
										<xsl:for-each select="lgCommon2:propertyQualifier/lgCommon2:value">
											<lgCommon:text>
												<xsl:for-each select="node()">
													<xsl:if test="self::text()">
														<xsl:sequence select="."/>
													</xsl:if>
												</xsl:for-each>
											</lgCommon:text>
										</xsl:for-each>
									</lgCon:definition>
								</xsl:for-each>
								<xsl:for-each select="lgCon2:comment">
									<xsl:variable name="var94_comment" as="node()" select="."/>
									<lgCon:comment>
										<xsl:attribute name="propertyName">
											<xsl:sequence select="xs:string(xs:token(@propertyName))"/>
										</xsl:attribute>
										<xsl:if test="$var94_comment/@propertyId">
											<xsl:attribute name="propertyId">
												<xsl:sequence select="xs:string(@propertyId)"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:if test="$var94_comment/@language">
											<xsl:attribute name="language">
												<xsl:sequence select="xs:string(xs:token(@language))"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:for-each select="lgCommon2:value">
											<xsl:variable name="var96_value" as="node()" select="."/>
											<xsl:if test="$var96_value/@dataType">
												<xsl:attribute name="format">
													<xsl:sequence select="xs:string(xs:token(@dataType))"/>
												</xsl:attribute>
											</xsl:if>
										</xsl:for-each>
										<xsl:for-each select="lgCommon2:source">
											<xsl:variable name="var98_source" as="node()" select="."/>
											<lgCommon:source>
												<xsl:if test="$var98_source/@subRef">
													<xsl:attribute name="subRef">
														<xsl:sequence select="xs:string(@subRef)"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:if test="$var98_source/@role">
													<xsl:attribute name="role">
														<xsl:sequence select="xs:string(xs:token(@role))"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:sequence select="xs:string(xs:token(.))"/>
											</lgCommon:source>
										</xsl:for-each>
										<xsl:for-each select="lgCommon2:usageContext">
											<lgCommon:usageContext>
												<xsl:sequence select="xs:string(xs:token(.))"/>
											</lgCommon:usageContext>
										</xsl:for-each>
										<xsl:for-each select="lgCommon2:propertyQualifier">
											<lgCommon:propertyQualifier>
												<xsl:attribute name="propertyQualifierId">
													<xsl:sequence select="xs:string(xs:token(@propertyQualifierName))"/>
												</xsl:attribute>
											</lgCommon:propertyQualifier>
										</xsl:for-each>
										<xsl:for-each select="lgCommon2:value">
											<lgCommon:text>
												<xsl:for-each select="node()">
													<xsl:if test="self::text()">
														<xsl:sequence select="."/>
													</xsl:if>
												</xsl:for-each>
											</lgCommon:text>
										</xsl:for-each>
									</lgCon:comment>
								</xsl:for-each>
								<xsl:for-each select="lgCon2:property">
									<xsl:variable name="var108_property" as="node()" select="."/>
									<lgCon:conceptProperty>
										<xsl:attribute name="propertyName">
											<xsl:sequence select="xs:string(xs:token(@propertyName))"/>
										</xsl:attribute>
										<xsl:if test="$var108_property/@propertyId">
											<xsl:attribute name="propertyId">
												<xsl:sequence select="xs:string(@propertyId)"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:if test="$var108_property/@language">
											<xsl:attribute name="language">
												<xsl:sequence select="xs:string(xs:token(@language))"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:for-each select="lgCommon2:value">
											<xsl:variable name="var110_value" as="node()" select="."/>
											<xsl:if test="$var110_value/@dataType">
												<xsl:attribute name="format">
													<xsl:sequence select="xs:string(xs:token(@dataType))"/>
												</xsl:attribute>
											</xsl:if>
										</xsl:for-each>
										<xsl:for-each select="lgCommon2:source">
											<xsl:variable name="var112_source" as="node()" select="."/>
											<lgCommon:source>
												<xsl:if test="$var112_source/@subRef">
													<xsl:attribute name="subRef">
														<xsl:sequence select="xs:string(@subRef)"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:if test="$var112_source/@role">
													<xsl:attribute name="role">
														<xsl:sequence select="xs:string(xs:token(@role))"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:sequence select="xs:string(xs:token(.))"/>
											</lgCommon:source>
										</xsl:for-each>
										<xsl:for-each select="lgCommon2:usageContext">
											<lgCommon:usageContext>
												<xsl:sequence select="xs:string(xs:token(.))"/>
											</lgCommon:usageContext>
										</xsl:for-each>
										<xsl:for-each select="lgCommon2:propertyQualifier">
											<lgCommon:propertyQualifier>
												<xsl:attribute name="propertyQualifierId">
													<xsl:sequence select="xs:string(xs:token(@propertyQualifierName))"/>
												</xsl:attribute>
											</lgCommon:propertyQualifier>
										</xsl:for-each>
										<xsl:for-each select="lgCommon2:value">
											<lgCommon:text>
												<xsl:for-each select="node()">
													<xsl:if test="self::text()">
														<xsl:sequence select="."/>
													</xsl:if>
												</xsl:for-each>
											</lgCommon:text>
										</xsl:for-each>
									</lgCon:conceptProperty>
								</xsl:for-each>
								<xsl:for-each select="lgCon2:propertyLink">
									<lgCon:propertyLink>
										<xsl:attribute name="sourceProperty">
											<xsl:sequence select="xs:string(@sourceProperty)"/>
										</xsl:attribute>
										<xsl:attribute name="link">
											<xsl:sequence select="xs:string(xs:token(@propertyLink))"/>
										</xsl:attribute>
										<xsl:attribute name="targetProperty">
											<xsl:sequence select="xs:string(@targetProperty)"/>
										</xsl:attribute>
									</lgCon:propertyLink>
								</xsl:for-each>
							</lgCon:concept>
						</xsl:for-each>
					</n:concepts>
				</xsl:for-each>
				<xsl:for-each select="n2:relations">
					<xsl:variable name="var124_relations" as="node()" select="."/>
					<n:relations>
						<xsl:if test="$var124_relations/@containerName">
							<xsl:attribute name="dc">
								<xsl:sequence select="xs:string(xs:token(@containerName))"/>
							</xsl:attribute>
						</xsl:if>
						<xsl:if test="$var124_relations/@isNative">
							<xsl:attribute name="isNative">
								<xsl:sequence select="xs:string(xs:boolean(@isNative))"/>
							</xsl:attribute>
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
							<xsl:variable name="var130_source" as="node()" select="."/>
							<lgRel:source>
								<xsl:if test="$var130_source/@subRef">
									<xsl:attribute name="subRef">
										<xsl:sequence select="xs:string(@subRef)"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var130_source/@role">
									<xsl:attribute name="role">
										<xsl:sequence select="xs:string(xs:token(@role))"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:sequence select="xs:string(xs:token(.))"/>
							</lgRel:source>
						</xsl:for-each>
						<xsl:for-each select="lgRel2:association">
							<xsl:variable name="var132_association" as="node()" select="."/>
							<lgRel:association>
								<xsl:attribute name="id">
									<xsl:sequence select="xs:string(@entityCode)"/>
								</xsl:attribute>
								<xsl:if test="$var132_association/@entityCodeNamespace">
									<xsl:attribute name="codingSchemeId">
										<xsl:sequence select="xs:string(xs:token(@entityCodeNamespace))"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var132_association/@forwardName">
									<xsl:attribute name="forwardName">
										<xsl:sequence select="xs:string(@forwardName)"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var132_association/@reverseName">
									<xsl:attribute name="reverseName">
										<xsl:sequence select="xs:string(@reverseName)"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var132_association/@inverse">
									<xsl:attribute name="inverse">
										<xsl:sequence select="xs:string(xs:token(@inverse))"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var132_association/@isNavigable">
									<xsl:attribute name="isNavigable">
										<xsl:sequence select="xs:string(xs:boolean(@isNavigable))"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var132_association/@isTransitive">
									<xsl:attribute name="isTransitive">
										<xsl:sequence select="xs:string(xs:boolean(@isTransitive))"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var132_association/@isAntiTransitive">
									<xsl:attribute name="isAntiTransitive">
										<xsl:sequence select="xs:string(xs:boolean(@isAntiTransitive))"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var132_association/@isSymmetric">
									<xsl:attribute name="isSymmetric">
										<xsl:sequence select="xs:string(xs:boolean(@isSymmetric))"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var132_association/@isAntiSymmetric">
									<xsl:attribute name="isAntiSymmetric">
										<xsl:sequence select="xs:string(xs:boolean(@isAntiSymmetric))"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var132_association/@isReflexive">
									<xsl:attribute name="isReflexive">
										<xsl:sequence select="xs:string(xs:boolean(@isReflexive))"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var132_association/@isAntiReflexive">
									<xsl:attribute name="isAntiReflexive">
										<xsl:sequence select="xs:string(xs:boolean(@isAntiReflexive))"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var132_association/@isFunctional">
									<xsl:attribute name="isFunctional">
										<xsl:sequence select="xs:string(xs:boolean(@isFunctional))"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var132_association/@isReverseFunctional">
									<xsl:attribute name="isReverseFunctional">
										<xsl:sequence select="xs:string(xs:boolean(@isReverseFunctional))"/>
									</xsl:attribute>
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
									<xsl:variable name="var138_source" as="node()" select="."/>
									<lgRel:sourceEntity>
										<xsl:if test="$var138_source/@sourceEntityCodeNamespace">
											<xsl:attribute name="sourceCodingScheme">
												<xsl:sequence select="xs:string(xs:token(@sourceEntityCodeNamespace))"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:attribute name="sourceId">
											<xsl:sequence select="xs:string(@sourceEntityCode)"/>
										</xsl:attribute>
										<xsl:for-each select="lgRel2:target">
											<xsl:variable name="var140_target" as="node()" select="."/>
											<lgRel:targetEntity>
												<xsl:if test="$var140_target/@targetEntityCodeNamespace">
													<xsl:attribute name="targetCodingScheme">
														<xsl:sequence select="xs:string(xs:token(@targetEntityCodeNamespace))"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:attribute name="targetId">
													<xsl:sequence select="xs:string(@targetEntityCode)"/>
												</xsl:attribute>
												<xsl:for-each select="lgRel2:associationQualification">
													<lgRel:associationQualification>
														<xsl:attribute name="associationQualifier">
															<xsl:sequence select="xs:string(xs:token(@associationQualifier))"/>
														</xsl:attribute>
													</lgRel:associationQualification>
												</xsl:for-each>
											</lgRel:targetEntity>
										</xsl:for-each>
										<xsl:for-each select="lgRel2:targetData">
											<xsl:variable name="var144_targetData" as="node()" select="."/>
											<lgRel:targetDataValue>
												<xsl:if test="$var144_targetData/@associationInstanceId">
													<xsl:attribute name="dataId">
														<xsl:sequence select="xs:string(@associationInstanceId)"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:for-each select="lgRel2:associationQualification">
													<lgRel:associationQualification>
														<xsl:attribute name="associationQualifier">
															<xsl:sequence select="xs:string(xs:token(@associationQualifier))"/>
														</xsl:attribute>
													</lgRel:associationQualification>
												</xsl:for-each>
											</lgRel:targetDataValue>
										</xsl:for-each>
									</lgRel:sourceEntity>
								</xsl:for-each>
								<xsl:for-each select="lgCon2:property">
									<xsl:variable name="var148_property" as="node()" select="."/>
									<lgRel:associationProperty>
										<xsl:attribute name="propertyName">
											<xsl:sequence select="xs:string(xs:token(@propertyName))"/>
										</xsl:attribute>
										<xsl:if test="$var148_property/@propertyId">
											<xsl:attribute name="propertyId">
												<xsl:sequence select="xs:string(@propertyId)"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:if test="$var148_property/@language">
											<xsl:attribute name="language">
												<xsl:sequence select="xs:string(xs:token(@language))"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:for-each select="lgCommon2:source">
											<xsl:variable name="var150_source" as="node()" select="."/>
											<lgCommon:source>
												<xsl:if test="$var150_source/@subRef">
													<xsl:attribute name="subRef">
														<xsl:sequence select="xs:string(@subRef)"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:if test="$var150_source/@role">
													<xsl:attribute name="role">
														<xsl:sequence select="xs:string(xs:token(@role))"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:sequence select="xs:string(xs:token(.))"/>
											</lgCommon:source>
										</xsl:for-each>
										<xsl:for-each select="lgCommon2:usageContext">
											<lgCommon:usageContext>
												<xsl:sequence select="xs:string(xs:token(.))"/>
											</lgCommon:usageContext>
										</xsl:for-each>
										<xsl:for-each select="lgCommon2:propertyQualifier">
											<lgCommon:propertyQualifier>
												<xsl:attribute name="propertyQualifierId">
													<xsl:sequence select="xs:string(xs:token(@propertyQualifierName))"/>
												</xsl:attribute>
											</lgCommon:propertyQualifier>
										</xsl:for-each>
										<xsl:for-each select="lgCommon2:value">
											<lgCommon:text>
												<xsl:for-each select="node()">
													<xsl:if test="self::text()">
														<xsl:sequence select="."/>
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
