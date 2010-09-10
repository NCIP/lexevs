<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
	<!-- Use this transform to take the output of HyperModel and create a model that can be imported into EA -->
	<!-- Stupid EA requirement:  In order to show up in the notes section, documentation on attributes has to be coupled with
		 the tag "description", while elsewhere, documentation has to be coupled with "documentation
	
		 The following stylesheet locates all "documentation" tagged values and, if associated with attributes, changes them
		 to "description".  Did I mention that this was really, really stupid?
	-->
	<xsl:template match="Foundation.Extension_Mechanisms.TaggedValue.tag">
		<xsl:element name="Foundation.Extension_Mechanisms.TaggedValue.tag">
			<xsl:copy-of select="@*"/>
			<xsl:choose>
				<xsl:when test="text()='documentation'">
					<xsl:choose>
						<xsl:when test="count(//Foundation.Core.Attribute[@xmi.id=current()/../Foundation.Extension_Mechanisms.TaggedValue.modelElement/Foundation.Core.ModelElement/@xmi.idref])>0">description</xsl:when>
						<xsl:otherwise>documentation</xsl:otherwise>
					</xsl:choose>
					<xsl:apply-templates select="*"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:apply-templates/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:element>
	</xsl:template>
	<!-- 
		Direct copy 
	-->
	<xsl:template match="*">
		<xsl:copy>
			<xsl:copy-of select="@*"/>
			<xsl:apply-templates/>
		</xsl:copy>
	</xsl:template>
	
</xsl:stylesheet>