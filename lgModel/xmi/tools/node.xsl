<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:node="http://xsltsl.org/node"
	extension-element-prefixes="node">


  <xsl:template name="node:xpath">
    <xsl:param name="node" select="."/>

    <xsl:choose>

      <xsl:when test="$node">

        <xsl:for-each select="$node[1]/ancestor-or-self::*">
          <xsl:text/>/<xsl:value-of select="name()"/>
          <xsl:text/>[<xsl:choose>
        		<xsl:when test="@name">
        			<xsl:value-of select='concat("@name=&apos;",@name,"&apos;")'/>
        		</xsl:when>
        		<xsl:otherwise><xsl:value-of select="count(preceding-sibling::*[name() = name(current())]) + 1"/></xsl:otherwise>
          </xsl:choose>]<xsl:text/>
       </xsl:for-each>

        <xsl:choose>

          <xsl:when test="$node[1]/self::comment()">
            <xsl:text>/comment()</xsl:text>
            <xsl:text/>[<xsl:value-of select="count($node[1]/preceding-sibling::comment()) + 1" />]<xsl:text/>
          </xsl:when>

          <xsl:when test="$node[1]/self::processing-instruction()">
            <xsl:text>/processing-instruction()</xsl:text>
            <xsl:text/>[<xsl:value-of select="count($node[1]/preceding-sibling::processing-instruction()) + 1" />]<xsl:text/>
          </xsl:when>

          <xsl:when test="$node[1]/self::text()">
            <xsl:text>/text()</xsl:text>
            <xsl:text/>[<xsl:value-of select="count($node[1]/preceding-sibling::text()) + 1" />]<xsl:text/>
          </xsl:when>

          <xsl:when test="not($node[1]/..)">
            <xsl:text>/</xsl:text>
          </xsl:when>

          <xsl:when test="count($node[1]/../namespace::* | $node[1]) = count($node[1]/../namespace::*)">
            <xsl:text/>/namespace::<xsl:value-of select="name($node[1])" />
          </xsl:when>

          <xsl:when test="count($node[1]/../@* | $node[1]) = count($node[1]/../@*)">
            <xsl:text/>/@<xsl:value-of select="name($node[1])" />
          </xsl:when>

        </xsl:choose>      
      </xsl:when>

      <xsl:otherwise>
        <xsl:text>/..</xsl:text>
      </xsl:otherwise>

    </xsl:choose>

  </xsl:template>

  <xsl:template name="node:type">
    <xsl:param name="node" select="."/>

    <xsl:choose>
      <xsl:when test="not($node)"/>
      <xsl:when test="$node[1]/self::*">
	<xsl:text>element</xsl:text>
      </xsl:when>
      <xsl:when test="$node[1]/self::text()">
	<xsl:text>text</xsl:text>
      </xsl:when>
      <xsl:when test="$node[1]/self::comment()">
	<xsl:text>comment</xsl:text>
      </xsl:when>
      <xsl:when test="$node[1]/self::processing-instruction()">
	<xsl:text>processing instruction</xsl:text>
      </xsl:when>
      <xsl:when test="not($node[1]/parent::*)">
        <xsl:text>root</xsl:text>
      </xsl:when>
      <xsl:when test="count($node[1] | $node[1]/../namespace::*) = count($node[1]/../namespace::*)">
        <xsl:text>namespace</xsl:text>
      </xsl:when>
      <xsl:when test="count($node[1] | $node[1]/../@*) = count($node[1]/../@*)">
        <xsl:text>attribute</xsl:text>
      </xsl:when>
    </xsl:choose>
  </xsl:template>

  <xsl:template name='node:copy'>
    <xsl:param name='nodes' select='.'/>

    <xsl:for-each select='$nodes'>
      <xsl:copy>
        <xsl:for-each select='@*'>
          <xsl:copy/>
        </xsl:for-each>

        <xsl:for-each select='node()'>
          <xsl:call-template name='node:copy'/>
        </xsl:for-each>
      </xsl:copy>
    </xsl:for-each>
  </xsl:template>
</xsl:stylesheet>

