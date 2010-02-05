<?xml version="1.0"?>
<?xml-stylesheet type="text/xsl" href="C:\vocabulary\cni\xml\xsl\cni_note2lucene_CEn.xsl"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="xml" indent="yes"/>
	<xsl:strip-space elements="*"/>
	<xsl:key name="ce_key" match="ce_entry" use="type" />
	<xsl:template match="document">
		<document>
		<xsl:attribute name="documentID"><xsl:value-of select="@noteID"/></xsl:attribute>
			<intField>
				<xsl:attribute name="name">note_id</xsl:attribute>
				<xsl:attribute name="store">true</xsl:attribute>
				<xsl:attribute name="index">true</xsl:attribute>
				<xsl:attribute name="padTo">12</xsl:attribute>
				<xsl:value-of select="@noteID"/>
			</intField>
			<xsl:apply-templates select="demographics"/>
			<xsl:apply-templates select="section/ce_entry[count(. | key('ce_key', type)[1]) = 1]"/>
			<xsl:apply-templates select="section"/>
		</document>
	</xsl:template>
	
	<xsl:template match="demographics">
		<xsl:for-each select="child::node()">
			<xsl:choose>
				<xsl:when test="name()='birthdate' or name()='note_date'">
					<dateField>
						<xsl:attribute name="name"><xsl:value-of select="name()"/></xsl:attribute>
						<xsl:attribute name="store">true</xsl:attribute>
						<xsl:attribute name="index">true</xsl:attribute>
						<xsl:value-of select="text()"/>
					</dateField>
				</xsl:when>
				<xsl:otherwise>
					<textField>
						<xsl:attribute name="name"><xsl:value-of select="name()"/></xsl:attribute>
						<xsl:attribute name="store">true</xsl:attribute>
						<xsl:attribute name="index">true</xsl:attribute>
						<xsl:attribute name="tokenize">true</xsl:attribute>
						<xsl:choose>
							<xsl:when test="name()='age_years'">
								<xsl:value-of select="format-number(.,'##0')"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="text()"/>
							</xsl:otherwise>
						</xsl:choose>
					</textField>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template match="section/ce_entry">
		<xsl:variable name="temp" select="type"/>
		<!-- Only execute if we haven't seen the current ce_entry type yet-->
		<textField>
			<xsl:attribute name="name">
				<xsl:text>CE</xsl:text>
				<xsl:value-of select="$temp"/>
			</xsl:attribute>
			<xsl:attribute name="store">false</xsl:attribute>
			<xsl:attribute name="index">true</xsl:attribute>
			<xsl:attribute name="tokenize">true</xsl:attribute>
			<!-- Collect all of the ce_entry elements of this type -->
			<xsl:for-each select="//section/ce_entry[type=$temp]">
				<xsl:value-of select="code"/><xsl:text> </xsl:text>
				<xsl:value-of select="desc"/><xsl:text> </xsl:text>
			</xsl:for-each>
		</textField>
	</xsl:template>

	<xsl:template match="section">
		<xsl:if test="@id!='CE'">		
			<textField>
				<xsl:attribute name="name"><xsl:value-of select="@id"/></xsl:attribute>
				<xsl:attribute name="store">false</xsl:attribute>
				<xsl:attribute name="index">true</xsl:attribute>
				<xsl:attribute name="tokenize">true</xsl:attribute>
				<xsl:for-each select="descendant-or-self::text()">
					<xsl:value-of select="."/>
					<xsl:text> </xsl:text>
				</xsl:for-each>
			</textField>
		</xsl:if>
		<textField>
			<xsl:attribute name="name"><xsl:value-of select="@id"/><xsl:text>_phrase_text</xsl:text></xsl:attribute>
			<xsl:attribute name="store">false</xsl:attribute>
			<xsl:attribute name="index">true</xsl:attribute>
			<xsl:attribute name="tokenize">true</xsl:attribute>
			<xsl:for-each select="descendant::node()">
				<xsl:if test="name()='phrase' and @det='1'">
					<xsl:value-of select="text()"/>
					<xsl:text> </xsl:text>
				</xsl:if>
			</xsl:for-each>
		</textField>
		<textField>
			<xsl:attribute name="name"><xsl:value-of select="@id"/><xsl:text>_phrase_id</xsl:text></xsl:attribute>
			<xsl:attribute name="store">false</xsl:attribute>
			<xsl:attribute name="index">true</xsl:attribute>
			<xsl:attribute name="tokenize">true</xsl:attribute>
			<xsl:for-each select="descendant::node()">
				<xsl:if test="name()='phrase' and @det='1'">
					<xsl:value-of select="@tid"/>
					<xsl:text> </xsl:text>
				</xsl:if>
			</xsl:for-each>
		</textField>
	</xsl:template>
</xsl:stylesheet>
