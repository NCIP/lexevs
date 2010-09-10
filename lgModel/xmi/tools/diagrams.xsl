<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"  xmlns:saxon="http://saxon.sf.net/" xmlns:node="http://xsltsl.org/node" version="2.0" xmlns:UML="omg.org/UML1.3">
	<!-- The purpose of this stylesheet is to preserve EA diagrams. 
		 This transform merges the diagrams from the "LexGrid" branch with the classes from the
		 "LexGrid_new" branch to form a new composite.
	-->
	<xsl:import href="node.xsl"/>
	<xsl:param name="oldBranch">LexGrid</xsl:param>
	<xsl:param name="newBranch">LexGrid_new</xsl:param>
	<xsl:template match="/">
		<xsl:apply-templates/>
	</xsl:template>
	<!-- Completely discard the old branch.  EA diagrams are owned by the root (content) node  -->
	<xsl:template match="UML:Package[@name=$oldBranch]"/>
	<xsl:template match="UML:ClassifierRole[@name=$oldBranch]"/>
	
	<!-- Everything not caught below gets copied verbatum -->
	<xsl:template match="*|text()">
		<xsl:copy>
			<xsl:copy-of select="@*"/>
			<xsl:apply-templates/>
		</xsl:copy>
	</xsl:template>
	<!-- Replace the diagram owners -->
	<xsl:template match="UML:Diagram">
		<xsl:copy>
			<xsl:copy-of select="@*[name()!='owner']"/>
			<xsl:attribute name="owner">
				<xsl:call-template name="mapOldIdToNew">
					<xsl:with-param name="oldId" select="@owner"/>
				</xsl:call-template>
			</xsl:attribute>
			<xsl:apply-templates/>
		</xsl:copy>
	</xsl:template>
	<!-- Replace the subject and object of diagram elements
	     Geometry kludge is to satisfy a bug in EA
	-->
	<xsl:template match="UML:DiagramElement">
		<xsl:variable name="cId">
			<xsl:call-template name="mapOldIdToNew">
				<xsl:with-param name="oldId" select="@subject"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:copy>
			<xsl:copy-of select="@geometry"/>
			<xsl:attribute name="subject" select="$cId"/>
			<xsl:copy-of select="@*[name()!='subject' and name()!='geometry']"/>
			<xsl:apply-templates/>
		</xsl:copy>
	</xsl:template>
	<!-- Replace new model style tags with old model equivalents -->
	<xsl:template match="UML:TaggedValue">
		<xsl:choose>
			<xsl:when test="@tag='style'">
				<xsl:variable name="newId" select="ancestor::*[@xmi.id][1]/@xmi.id"/>
				<xsl:variable name="oldId">
					<xsl:choose>
						<xsl:when test="string-length($newId) > 0">
							<xsl:call-template name="mapNewIdToOld">
								<xsl:with-param name="newId" select="$newId"/>
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise><xsl:value-of select="$newId"/></xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<xsl:choose>
					<xsl:when test="$oldId!=$newId">
						<xsl:copy-of select="//*[@xmi.id=$oldId]//UML:TaggedValue[@tag='style'][1]"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:copy>
							<xsl:copy-of select="@*"/>
							<xsl:apply-templates/>
						</xsl:copy>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:copy>
					<xsl:copy-of select="@*"/>
					<xsl:apply-templates/>
				</xsl:copy>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!-- Some dependencies are diagrammatic and have to be mapped -->
	<xsl:template match="UML:Dependency">
		<xsl:copy>
			<xsl:copy-of select="@*[name()!='client' and name()!='supplier']"/>
			<xsl:attribute name="client">
				<xsl:call-template name="mapOldIdToNew">
					<xsl:with-param name="oldId" select="@client"/>
				</xsl:call-template>
			</xsl:attribute>
			<xsl:attribute name="supplier">
				<xsl:call-template name="mapOldIdToNew">
					<xsl:with-param name="oldId" select="@supplier"/>
				</xsl:call-template>
			</xsl:attribute>
			<xsl:apply-templates/>
		</xsl:copy>
	</xsl:template>
	
	<!-- Map the supplied id from the new namespace to the corresponding id in the old -->
	<xsl:template name="mapNewIdToOld">
		<xsl:param name="newId"/>
		<xsl:call-template name="mapId">
			<xsl:with-param name="fromPackage" select="$newBranch"/>
			<xsl:with-param name="toPackage" select="$oldBranch"/>
			<xsl:with-param name="id" select="$newId"/>
		</xsl:call-template>
	</xsl:template>
	
	<!-- Map the supplied id from the new namespace to the corresponding id in the old -->
	<xsl:template name="mapOldIdToNew">
		<xsl:param name="oldId"/>
		<xsl:call-template name="mapId">
			<xsl:with-param name="fromPackage" select="$oldBranch"/>
			<xsl:with-param name="toPackage" select="$newBranch"/>
			<xsl:with-param name="id" select="$oldId"/>
		</xsl:call-template>
	</xsl:template>

	<!-- Map an identifier from one namespace to another 
		 If the identifier isn't within the old package, just copy it
		 If the identifier is within the old package, and is named, map it to
			the node with the same "type" and name in the new package. If none, map to MISSINGNODE(id)
		 Otherwise, calculate the xpath of the node in the old package and map it
			to the corresponding xpath in the new package.  If none, ma to MISSINGNODE(id) 
	-->
	<xsl:template name="mapId">
		<xsl:param name="fromPackage"/>
		<xsl:param name="toPackage"/>
		<xsl:param name="id"/>
		<xsl:variable name="inOldPackage" select="//UML:Package[@name=$fromPackage]//*[@xmi.id=$id]"/>
		<xsl:choose>
			<!-- In the old package, map it to the new -->
			<xsl:when test="$inOldPackage">
				<xsl:variable name="fromName">
					<xsl:value-of select="$inOldPackage/@name"/>
				</xsl:variable>
				<xsl:choose>
					<!-- Named node -->
					<xsl:when test="string-length($fromName) > 0">
						<xsl:variable name="fromType">
							<xsl:value-of select="$inOldPackage/name()"/>
						</xsl:variable>
						<xsl:choose>
							<xsl:when test="//UML:Package[@name=$toPackage]//*[name()=$fromType and @name=$fromName]/@xmi.id[1]">
								<xsl:value-of select="//UML:Package[@name=$toPackage]//*[name()=$fromType and @name=$fromName]/@xmi.id[1]"/>
							</xsl:when>
							<xsl:otherwise><xsl:value-of select="concat('MISSING_NEW_ID(',$id,')')"/></xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<!-- Unnamed node -->
					<xsl:otherwise>
						<xsl:variable name="nodePath">
							<xsl:call-template name="node:xpath">
								<xsl:with-param name="node" select="$inOldPackage"/>
							</xsl:call-template>
						</xsl:variable>
						<xsl:variable name="fromEx" select='concat("@name=&apos;",$fromPackage,"&apos;")'/>
						<xsl:variable name="newPath">
							<xsl:analyze-string select="$nodePath" regex="{$fromEx}">
								<xsl:matching-substring>
									<xsl:value-of select='concat("@name=&apos;",$toPackage,"&apos;")'/>
								</xsl:matching-substring>
								<xsl:non-matching-substring>
									<xsl:value-of select="."/>
								</xsl:non-matching-substring>
							</xsl:analyze-string><xsl:text>/@xmi.id</xsl:text>
						</xsl:variable>
						<!--xsl:value-of select="concat('PATH-MAP(',$newPath,')')"/-->
						<xsl:value-of select="saxon:evaluate($newPath)"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="validateId">
					<xsl:with-param name="id" select="$id"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- Check whether the id exists at all.  If it does, return it, otherwise return a missingID tag -->
	<xsl:template name="validateId">
		<xsl:param name="id"/>
		<xsl:variable name="destNode" select="//*[@xmi.id=$id]"/>
		<xsl:choose>
			<xsl:when test="$destNode">
				<xsl:for-each select="$destNode">
					<xsl:choose>
						<xsl:when test="ancestor::UML:Package[@name=$oldBranch]">
							<xsl:value-of select="concat('MISSINGID_OLDNODE1(',$id,')')"/>
						</xsl:when>
						<xsl:when test="ancestor::UML:ClassifierRole[@name=$oldBranch]">
							<xsl:value-of select="concat('MISSINGID_OLDNODE2(',$id,')')"/>
						</xsl:when>
						<xsl:otherwise><xsl:value-of select="$id"/></xsl:otherwise>
					</xsl:choose>
				</xsl:for-each>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="concat('MISSINGID(',$id,')')"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
