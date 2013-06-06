<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:hl7="urn:hl7-org:v3/mif2"
    version="1.0" >
    <xsl:output method="xml" omit-xml-declaration="yes" indent="yes"/>
    <xsl:strip-space elements="*"/>
    
    <xsl:template match="node()|@*">
        <xsl:copy>
            <xsl:apply-templates select="node()|@*"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="hl7:p | hl7:b | hl7:li | hl7:ul | hl7:i">
        <xsl:apply-templates/>
    </xsl:template>
</xsl:stylesheet>