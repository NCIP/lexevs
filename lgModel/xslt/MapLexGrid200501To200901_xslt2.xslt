<?xml version="1.0" encoding="UTF-8"?>
<!--
Transforms documents defined against LexGrid model revision 2005/01
to LexGrid model revision 2009/01.
-->
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:lgBuiltin2="http://LexGrid.org/schema/2005/01/LexGrid/builtins" xmlns:n2="http://LexGrid.org/schema/2005/01/LexGrid/codingSchemes" xmlns:lgCommon2="http://LexGrid.org/schema/2005/01/LexGrid/commonTypes" xmlns:lgCon2="http://LexGrid.org/schema/2005/01/LexGrid/concepts" xmlns:ldap="http://LexGrid.org/schema/2005/01/LexGrid/ldap" xmlns:lgNaming2="http://LexGrid.org/schema/2005/01/LexGrid/naming" xmlns:lgRel2="http://LexGrid.org/schema/2005/01/LexGrid/relations" xmlns:lgVer2="http://LexGrid.org/schema/2005/01/LexGrid/versions" xmlns:lgBuiltin="http://LexGrid.org/schema/2009/01/LexGrid/builtins" xmlns:n="http://LexGrid.org/schema/2009/01/LexGrid/codingSchemes" xmlns:lgCommon="http://LexGrid.org/schema/2009/01/LexGrid/commonTypes" xmlns:lgCon="http://LexGrid.org/schema/2009/01/LexGrid/concepts" xmlns:lgNaming="http://LexGrid.org/schema/2009/01/LexGrid/naming" xmlns:lgRel="http://LexGrid.org/schema/2009/01/LexGrid/relations" xmlns:lgVD="http://LexGrid.org/schema/2009/01/LexGrid/valueDomains" xmlns:lgVer="http://LexGrid.org/schema/2009/01/LexGrid/versions" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions" exclude-result-prefixes="fn ldap lgBuiltin2 lgCommon2 lgCon2 lgNaming2 lgRel2 lgVer2 n2 xs xsi xsl" xmlns="http://LexGrid.org/schema/2009/01/LexGrid/codingSchemes">
	<xsl:namespace-alias stylesheet-prefix="n" result-prefix="#default"/>
	<xsl:output method="xml" encoding="UTF-8" indent="yes"/>
	<xsl:template match="/">
		<n:codingScheme>
			<xsl:attribute name="xsi:schemaLocation" separator=" ">
				<xsl:sequence select="'http://LexGrid.org/schema/2009/01/LexGrid/codingSchemes C:/DOCUME~1/johnt1/MYDOCU~1/schema/lexgrid/200901/codingSchemes.xsd'"/>
			</xsl:attribute>
			<xsl:variable name="var1_instance" as="node()" select="."/>
			<xsl:for-each select="$var1_instance/n2:codingScheme">
				<xsl:variable name="var2_codingScheme" as="node()" select="."/>
				<xsl:if test="$var2_codingScheme/@codingScheme">
					<xsl:attribute name="codingSchemeName">
						<xsl:sequence select="xs:string(xs:NCName(xs:string(@codingScheme)))"/>
					</xsl:attribute>
				</xsl:if>
				<xsl:if test="$var2_codingScheme/@registeredName">
					<xsl:attribute name="codingSchemeURI">
						<xsl:sequence select="xs:string(xs:anyURI(@registeredName))"/>
					</xsl:attribute>
				</xsl:if>
				<xsl:if test="$var2_codingScheme/@formalName">
					<xsl:attribute name="formalName">
						<xsl:sequence select="xs:string(@formalName)"/>
					</xsl:attribute>
				</xsl:if>
				<xsl:if test="$var2_codingScheme/@defaultLanguage">
					<xsl:attribute name="defaultLanguage">
						<xsl:sequence select="xs:string(xs:NCName(xs:string(@defaultLanguage)))"/>
					</xsl:attribute>
				</xsl:if>
				<xsl:if test="$var2_codingScheme/@approxNumConcepts">
					<xsl:attribute name="approxNumConcepts">
						<xsl:sequence select="xs:string(xs:integer(@approxNumConcepts))"/>
					</xsl:attribute>
				</xsl:if>
				<xsl:if test="$var2_codingScheme/@representsVersion">
					<xsl:attribute name="representsVersion">
						<xsl:sequence select="xs:string(@representsVersion)"/>
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
					<n:source>
						<xsl:sequence select="xs:string(xs:NCName(xs:string(.)))"/>
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
					<xsl:for-each select="n2:supportedAssociation">
						<xsl:variable name="var16_supportedAssociation" as="node()" select="."/>
						<lgNaming:supportedAssociation>
							<xsl:attribute name="localId">
								<xsl:sequence select="xs:string(xs:NCName(xs:string(.)))"/>
							</xsl:attribute>
							<xsl:if test="$var16_supportedAssociation/@urn">
								<xsl:attribute name="uri">
									<xsl:sequence select="xs:string(xs:anyURI(@urn))"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:sequence select="xs:string(.)"/>
						</lgNaming:supportedAssociation>
					</xsl:for-each>
					<xsl:for-each select="n2:supportedAssociationQualifier">
						<xsl:variable name="var18_supportedAssociationQualifier" as="node()" select="."/>
						<lgNaming:supportedAssociationQualifier>
							<xsl:attribute name="localId">
								<xsl:sequence select="xs:string(xs:NCName(xs:string(.)))"/>
							</xsl:attribute>
							<xsl:if test="$var18_supportedAssociationQualifier/@urn">
								<xsl:attribute name="uri">
									<xsl:sequence select="xs:string(xs:anyURI(@urn))"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:sequence select="xs:string(.)"/>
						</lgNaming:supportedAssociationQualifier>
					</xsl:for-each>
					<xsl:for-each select="n2:supportedCodingScheme">
						<xsl:variable name="var20_supportedCodingScheme" as="node()" select="."/>
						<lgNaming:supportedCodingScheme>
							<xsl:attribute name="localId">
								<xsl:sequence select="xs:string(xs:NCName(xs:string(.)))"/>
							</xsl:attribute>
							<xsl:if test="$var20_supportedCodingScheme/@urn">
								<xsl:attribute name="uri">
									<xsl:sequence select="xs:string(xs:anyURI(@urn))"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:sequence select="xs:string(.)"/>
						</lgNaming:supportedCodingScheme>
					</xsl:for-each>
					<xsl:for-each select="n2:supportedContext">
						<xsl:variable name="var22_supportedContext" as="node()" select="."/>
						<lgNaming:supportedContext>
							<xsl:attribute name="localId">
								<xsl:sequence select="xs:string(xs:NCName(xs:string(.)))"/>
							</xsl:attribute>
							<xsl:if test="$var22_supportedContext/@urn">
								<xsl:attribute name="uri">
									<xsl:sequence select="xs:string(xs:anyURI(@urn))"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:sequence select="xs:string(.)"/>
						</lgNaming:supportedContext>
					</xsl:for-each>
					<xsl:for-each select="n2:supportedDataType">
						<xsl:variable name="var24_supportedDataType" as="node()" select="."/>
						<lgNaming:supportedDataType>
							<xsl:attribute name="localId">
								<xsl:sequence select="xs:string(xs:NCName(xs:string(.)))"/>
							</xsl:attribute>
							<xsl:if test="$var24_supportedDataType/@urn">
								<xsl:attribute name="uri">
									<xsl:sequence select="xs:string(xs:anyURI(@urn))"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:sequence select="xs:string(.)"/>
						</lgNaming:supportedDataType>
					</xsl:for-each>
					<lgNaming:supportedEntityType>
						<xsl:attribute name="localId">
							<xsl:sequence select="xs:string(xs:NCName('concept'))"/>
						</xsl:attribute>
						<xsl:sequence select="'concept'"/>
					</lgNaming:supportedEntityType>
					<xsl:for-each select="n2:supportedLanguage">
						<xsl:variable name="var26_supportedLanguage" as="node()" select="."/>
						<lgNaming:supportedLanguage>
							<xsl:attribute name="localId">
								<xsl:sequence select="xs:string(xs:NCName(xs:string(.)))"/>
							</xsl:attribute>
							<xsl:if test="$var26_supportedLanguage/@urn">
								<xsl:attribute name="uri">
									<xsl:sequence select="xs:string(xs:anyURI(@urn))"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:sequence select="xs:string(.)"/>
						</lgNaming:supportedLanguage>
					</xsl:for-each>
					<xsl:for-each select="n2:supportedProperty">
						<xsl:variable name="var28_supportedProperty" as="node()" select="."/>
						<lgNaming:supportedProperty>
							<xsl:attribute name="localId">
								<xsl:sequence select="xs:string(xs:NCName(xs:string(.)))"/>
							</xsl:attribute>
							<xsl:if test="$var28_supportedProperty/@urn">
								<xsl:attribute name="uri">
									<xsl:sequence select="xs:string(xs:anyURI(@urn))"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:sequence select="xs:string(.)"/>
						</lgNaming:supportedProperty>
					</xsl:for-each>
					<xsl:for-each select="n2:supportedPropertyLink">
						<xsl:variable name="var30_supportedPropertyLink" as="node()" select="."/>
						<lgNaming:supportedPropertyLink>
							<xsl:attribute name="localId">
								<xsl:sequence select="xs:string(xs:NCName(xs:string(.)))"/>
							</xsl:attribute>
							<xsl:if test="$var30_supportedPropertyLink/@urn">
								<xsl:attribute name="uri">
									<xsl:sequence select="xs:string(xs:anyURI(@urn))"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:sequence select="xs:string(.)"/>
						</lgNaming:supportedPropertyLink>
					</xsl:for-each>
					<xsl:for-each select="n2:supportedRepresentationalForm">
						<xsl:variable name="var32_supportedRepresentationalForm" as="node()" select="."/>
						<lgNaming:supportedRepresentationalForm>
							<xsl:attribute name="localId">
								<xsl:sequence select="xs:string(xs:NCName(xs:string(.)))"/>
							</xsl:attribute>
							<xsl:if test="$var32_supportedRepresentationalForm/@urn">
								<xsl:attribute name="uri">
									<xsl:sequence select="xs:string(xs:anyURI(@urn))"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:sequence select="xs:string(.)"/>
						</lgNaming:supportedRepresentationalForm>
					</xsl:for-each>
					<xsl:for-each select="n2:supportedSource">
						<xsl:variable name="var34_supportedSource" as="node()" select="."/>
						<lgNaming:supportedSource>
							<xsl:attribute name="localId">
								<xsl:sequence select="xs:string(xs:NCName(xs:string(.)))"/>
							</xsl:attribute>
							<xsl:if test="$var34_supportedSource/@urn">
								<xsl:attribute name="uri">
									<xsl:sequence select="xs:string(xs:anyURI(@urn))"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:sequence select="xs:string(.)"/>
						</lgNaming:supportedSource>
					</xsl:for-each>
					<xsl:for-each select="n2:supportedConceptStatus">
						<xsl:variable name="var36_supportedConceptStatus" as="node()" select="."/>
						<lgNaming:supportedStatus>
							<xsl:attribute name="localId">
								<xsl:sequence select="xs:string(xs:NCName(xs:string(.)))"/>
							</xsl:attribute>
							<xsl:if test="$var36_supportedConceptStatus/@urn">
								<xsl:attribute name="uri">
									<xsl:sequence select="xs:string(xs:anyURI(@urn))"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:sequence select="xs:string(.)"/>
						</lgNaming:supportedStatus>
					</xsl:for-each>
				</n:mappings>
				<xsl:for-each select="n2:concepts">
					<n:entities>
						<xsl:for-each select="lgCon2:concept">
							<xsl:variable name="var40_concept" as="node()" select="."/>
							<lgCon:entity>
								<xsl:if test="$var40_concept/@isActive">
									<xsl:attribute name="isActive">
										<xsl:sequence select="xs:string(xs:boolean(@isActive))"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var40_concept/@conceptStatus">
									<xsl:attribute name="status">
										<xsl:sequence select="xs:string(xs:NCName(xs:string(@conceptStatus)))"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var40_concept/@conceptCode">
									<xsl:attribute name="entityCode">
										<xsl:sequence select="xs:string(@conceptCode)"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var40_concept/@isAnonymous">
									<xsl:attribute name="isAnonymous">
										<xsl:sequence select="xs:string(xs:boolean(@isAnonymous))"/>
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
								<lgCon:entityType>
									<xsl:sequence select="xs:string(xs:NCName('concept'))"/>
								</lgCon:entityType>
								<xsl:for-each select="lgCon2:presentation">
									<xsl:variable name="var46_presentation" as="node()" select="."/>
									<lgCon:presentation>
										<xsl:if test="$var46_presentation/@property">
											<xsl:attribute name="propertyName">
												<xsl:sequence select="xs:string(xs:NCName(xs:string(@property)))"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:if test="$var46_presentation/@propertyId">
											<xsl:attribute name="propertyId">
												<xsl:sequence select="xs:string(@propertyId)"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:if test="$var46_presentation/@language">
											<xsl:attribute name="language">
												<xsl:sequence select="xs:string(xs:NCName(xs:string(@language)))"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:if test="$var46_presentation/@isPreferred">
											<xsl:attribute name="isPreferred">
												<xsl:sequence select="xs:string(xs:boolean(@isPreferred))"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:if test="$var46_presentation/@degreeOfFidelity">
											<xsl:attribute name="degreeOfFidelity">
												<xsl:sequence select="xs:string(xs:NCName(xs:string(@degreeOfFidelity)))"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:if test="$var46_presentation/@matchIfNoContext">
											<xsl:attribute name="matchIfNoContext">
												<xsl:sequence select="xs:string(xs:boolean(@matchIfNoContext))"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:if test="$var46_presentation/@representationalForm">
											<xsl:attribute name="representationalForm">
												<xsl:sequence select="xs:string(xs:NCName(xs:string(@representationalForm)))"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:for-each select="lgCon2:source">
											<lgCommon:source>
												<xsl:sequence select="xs:string(xs:NCName(xs:string(.)))"/>
											</lgCommon:source>
										</xsl:for-each>
										<xsl:for-each select="lgCon2:usageContext">
											<lgCommon:usageContext>
												<xsl:sequence select="xs:string(xs:NCName(xs:string(.)))"/>
											</lgCommon:usageContext>
										</xsl:for-each>
										<xsl:for-each select="lgCon2:text">
											<lgCommon:value>
												<xsl:for-each select="node()">
													<xsl:if test="self::text()">
														<xsl:sequence select="."/>
													</xsl:if>
												</xsl:for-each>
											</lgCommon:value>
										</xsl:for-each>
									</lgCon:presentation>
								</xsl:for-each>
								<xsl:for-each select="lgCon2:definition">
									<xsl:variable name="var56_definition" as="node()" select="."/>
									<lgCon:definition>
										<xsl:if test="$var56_definition/@property">
											<xsl:attribute name="propertyName">
												<xsl:sequence select="xs:string(xs:NCName(xs:string(@property)))"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:if test="$var56_definition/@propertyId">
											<xsl:attribute name="propertyId">
												<xsl:sequence select="xs:string(@propertyId)"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:if test="$var56_definition/@language">
											<xsl:attribute name="language">
												<xsl:sequence select="xs:string(xs:NCName(xs:string(@language)))"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:if test="$var56_definition/@isPreferred">
											<xsl:attribute name="isPreferred">
												<xsl:sequence select="xs:string(xs:boolean(@isPreferred))"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:for-each select="lgCon2:source">
											<lgCommon:source>
												<xsl:sequence select="xs:string(xs:NCName(xs:string(.)))"/>
											</lgCommon:source>
										</xsl:for-each>
										<xsl:for-each select="lgCon2:usageContext">
											<lgCommon:usageContext>
												<xsl:sequence select="xs:string(xs:NCName(xs:string(.)))"/>
											</lgCommon:usageContext>
										</xsl:for-each>
										<xsl:for-each select="lgCon2:text">
											<lgCommon:value>
												<xsl:for-each select="node()">
													<xsl:if test="self::text()">
														<xsl:sequence select="."/>
													</xsl:if>
												</xsl:for-each>
											</lgCommon:value>
										</xsl:for-each>
									</lgCon:definition>
								</xsl:for-each>
								<xsl:for-each select="lgCon2:comment">
									<xsl:variable name="var66_comment" as="node()" select="."/>
									<lgCon:comment>
										<xsl:if test="$var66_comment/@property">
											<xsl:attribute name="propertyName">
												<xsl:sequence select="xs:string(xs:NCName(xs:string(@property)))"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:if test="$var66_comment/@propertyId">
											<xsl:attribute name="propertyId">
												<xsl:sequence select="xs:string(@propertyId)"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:if test="$var66_comment/@language">
											<xsl:attribute name="language">
												<xsl:sequence select="xs:string(xs:NCName(xs:string(@language)))"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:for-each select="lgCon2:source">
											<lgCommon:source>
												<xsl:sequence select="xs:string(xs:NCName(xs:string(.)))"/>
											</lgCommon:source>
										</xsl:for-each>
										<xsl:for-each select="lgCon2:usageContext">
											<lgCommon:usageContext>
												<xsl:sequence select="xs:string(xs:NCName(xs:string(.)))"/>
											</lgCommon:usageContext>
										</xsl:for-each>
										<xsl:for-each select="lgCon2:text">
											<lgCommon:value>
												<xsl:for-each select="node()">
													<xsl:if test="self::text()">
														<xsl:sequence select="."/>
													</xsl:if>
												</xsl:for-each>
											</lgCommon:value>
										</xsl:for-each>
									</lgCon:comment>
								</xsl:for-each>
								<xsl:for-each select="lgCon2:property">
									<xsl:variable name="var76_property" as="node()" select="."/>
									<lgCon:property>
										<xsl:if test="$var76_property/@property">
											<xsl:attribute name="propertyName">
												<xsl:sequence select="xs:string(xs:NCName(xs:string(@property)))"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:if test="$var76_property/@propertyId">
											<xsl:attribute name="propertyId">
												<xsl:sequence select="xs:string(@propertyId)"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:if test="$var76_property/@language">
											<xsl:attribute name="language">
												<xsl:sequence select="xs:string(xs:NCName(xs:string(@language)))"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:for-each select="lgCon2:source">
											<lgCommon:source>
												<xsl:sequence select="xs:string(xs:NCName(xs:string(.)))"/>
											</lgCommon:source>
										</xsl:for-each>
										<xsl:for-each select="lgCon2:usageContext">
											<lgCommon:usageContext>
												<xsl:sequence select="xs:string(xs:NCName(xs:string(.)))"/>
											</lgCommon:usageContext>
										</xsl:for-each>
										<xsl:for-each select="lgCon2:text">
											<lgCommon:value>
												<xsl:for-each select="node()">
													<xsl:if test="self::text()">
														<xsl:sequence select="."/>
													</xsl:if>
												</xsl:for-each>
											</lgCommon:value>
										</xsl:for-each>
									</lgCon:property>
								</xsl:for-each>
								<xsl:for-each select="lgCon2:links">
									<xsl:variable name="var86_links" as="node()" select="."/>
									<lgCon:propertyLink>
										<xsl:if test="$var86_links/@sourceProperty">
											<xsl:attribute name="sourceProperty">
												<xsl:sequence select="xs:string(@sourceProperty)"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:if test="$var86_links/@link">
											<xsl:attribute name="propertyLink">
												<xsl:sequence select="xs:string(xs:NCName(xs:string(@link)))"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:if test="$var86_links/@targetProperty">
											<xsl:attribute name="targetProperty">
												<xsl:sequence select="xs:string(@targetProperty)"/>
											</xsl:attribute>
										</xsl:if>
									</lgCon:propertyLink>
								</xsl:for-each>
							</lgCon:entity>
						</xsl:for-each>
					</n:entities>
				</xsl:for-each>
				<xsl:for-each select="n2:relations">
					<xsl:variable name="var88_relations" as="node()" select="."/>
					<n:relations>
						<xsl:if test="$var88_relations/@dc">
							<xsl:attribute name="containerName">
								<xsl:sequence select="xs:string(xs:NCName(xs:string(@dc)))"/>
							</xsl:attribute>
						</xsl:if>
						<xsl:if test="$var88_relations/@isNative">
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
							<lgRel:source>
								<xsl:sequence select="xs:string(xs:NCName(xs:string(.)))"/>
							</lgRel:source>
						</xsl:for-each>
						<xsl:for-each select="lgRel2:association">
							<xsl:variable name="var96_association" as="node()" select="."/>
							<lgRel:association>
								<xsl:if test="$var96_association/@association">
									<xsl:attribute name="entityCode">
										<xsl:sequence select="xs:string(@association)"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var96_association/@association">
									<xsl:attribute name="associationName">
										<xsl:sequence select="xs:string(xs:NCName(xs:string(@association)))"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var96_association/@forwardName">
									<xsl:attribute name="forwardName">
										<xsl:sequence select="xs:string(@forwardName)"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var96_association/@reverseName">
									<xsl:attribute name="reverseName">
										<xsl:sequence select="xs:string(@reverseName)"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var96_association/@inverse">
									<xsl:attribute name="inverse">
										<xsl:sequence select="xs:string(xs:NCName(xs:string(@inverse)))"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var96_association/@isNavigable">
									<xsl:attribute name="isNavigable">
										<xsl:sequence select="xs:string(xs:boolean(@isNavigable))"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var96_association/@isTransitive">
									<xsl:attribute name="isTransitive">
										<xsl:sequence select="xs:string(xs:boolean(@isTransitive))"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var96_association/@isAntiTransitive">
									<xsl:attribute name="isAntiTransitive">
										<xsl:sequence select="xs:string(xs:boolean(@isAntiTransitive))"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var96_association/@isSymmetric">
									<xsl:attribute name="isSymmetric">
										<xsl:sequence select="xs:string(xs:boolean(@isSymmetric))"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var96_association/@isAntiSymmetric">
									<xsl:attribute name="isAntiSymmetric">
										<xsl:sequence select="xs:string(xs:boolean(@isAntiSymmetric))"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var96_association/@isReflexive">
									<xsl:attribute name="isReflexive">
										<xsl:sequence select="xs:string(xs:boolean(@isReflexive))"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var96_association/@isAntiReflexive">
									<xsl:attribute name="isAntiReflexive">
										<xsl:sequence select="xs:string(xs:boolean(@isAntiReflexive))"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var96_association/@isFunctional">
									<xsl:attribute name="isFunctional">
										<xsl:sequence select="xs:string(xs:boolean(@isFunctional))"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="$var96_association/@isReverseFunctional">
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
								<xsl:for-each select="lgRel2:sourceConcept">
									<xsl:variable name="var102_sourceConcept" as="node()" select="."/>
									<lgRel:source>
										<xsl:if test="$var102_sourceConcept/@sourceCodingScheme">
											<xsl:attribute name="sourceEntityCodeNamespace">
												<xsl:sequence select="xs:string(xs:NCName(xs:string(@sourceCodingScheme)))"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:if test="$var102_sourceConcept/@sourceConcept">
											<xsl:attribute name="sourceEntityCode">
												<xsl:sequence select="xs:string(@sourceConcept)"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:for-each select="lgRel2:targetConcept">
											<xsl:variable name="var104_targetConcept" as="node()" select="."/>
											<lgRel:target>
												<xsl:if test="$var104_targetConcept/@targetConcept">
													<xsl:attribute name="targetEntityCode">
														<xsl:sequence select="xs:string(@targetConcept)"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:if test="$var104_targetConcept/@targetCodingScheme">
													<xsl:attribute name="targetEntityCodeNamespace">
														<xsl:sequence select="xs:string(xs:NCName(xs:string(@targetCodingScheme)))"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:for-each select="lgRel2:associationQualification">
													<xsl:variable name="var106_associationQualification" as="node()" select="."/>
													<lgRel:associationQualification>
														<xsl:if test="$var106_associationQualification/@associationQualifier">
															<xsl:attribute name="associationQualifier">
																<xsl:sequence select="xs:string(xs:NCName(xs:string(@associationQualifier)))"/>
															</xsl:attribute>
														</xsl:if>
														<xsl:for-each select="lgRel2:associationQualifierValue">
															<lgRel:qualifierText>
																<xsl:if test="$var106_associationQualification/@dataType">
																	<xsl:attribute name="dataType">
																		<xsl:sequence select="xs:string(xs:NCName(xs:string($var106_associationQualification/@dataType)))"/>
																	</xsl:attribute>
																</xsl:if>
																<xsl:for-each select="node()">
																	<xsl:if test="self::text()">
																		<xsl:sequence select="."/>
																	</xsl:if>
																</xsl:for-each>
															</lgRel:qualifierText>
														</xsl:for-each>
													</lgRel:associationQualification>
												</xsl:for-each>
											</lgRel:target>
										</xsl:for-each>
										<xsl:for-each select="lgRel2:targetDataValue">
											<xsl:variable name="var112_targetDataValue" as="node()" select="."/>
											<lgRel:targetData>
												<xsl:if test="$var112_targetDataValue/@id">
													<xsl:attribute name="associationInstanceId">
														<xsl:sequence select="xs:string(@id)"/>
													</xsl:attribute>
												</xsl:if>
												<xsl:for-each select="lgRel2:associationQualification">
													<xsl:variable name="var114_associationQualification" as="node()" select="."/>
													<lgRel:associationQualification>
														<xsl:if test="$var114_associationQualification/@associationQualifier">
															<xsl:attribute name="associationQualifier">
																<xsl:sequence select="xs:string(xs:NCName(xs:string(@associationQualifier)))"/>
															</xsl:attribute>
														</xsl:if>
														<xsl:for-each select="lgRel2:associationQualifierValue">
															<lgRel:qualifierText>
																<xsl:for-each select="node()">
																	<xsl:if test="self::text()">
																		<xsl:sequence select="."/>
																	</xsl:if>
																</xsl:for-each>
															</lgRel:qualifierText>
														</xsl:for-each>
													</lgRel:associationQualification>
												</xsl:for-each>
												<lgRel:associationDataText>
													<xsl:for-each select="lgRel2:dataValue/node()">
														<xsl:if test="self::text()">
															<xsl:sequence select="."/>
														</xsl:if>
													</xsl:for-each>
												</lgRel:associationDataText>
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
