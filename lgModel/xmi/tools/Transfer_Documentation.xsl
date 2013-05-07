<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:uml="http://schema.omg.org/spec/UML/2.1" xmlns:xmi="http://schema.omg.org/spec/XMI/2.1" xmlns:EAUML="http://www.sparxsystems.com/profiles/EAUML/1.0" xmlns:thecustomprofile="http://www.sparxsystems.com/profiles/thecustomprofile/1.0" version="2.0">
        <xsl:output method="xml" indent="yes"/>
        <!-- Transfer the documentation from the source document to the target document 
                To use this transform:
                1) Create a new EA project (we are currently using EA 7.1)
                2) Import all of the LexGrid XML Schema (note: EA doesn't import documentation with imbedded markup correctly...)
                3) Export the project as XML - this is the source
                4) Load the UML project
                5) Export the project as XML - this is the target
                6) Run this transform.  Check for "ERROR" - if there are any, fix them.
                7) Save the output as an XMI file.  Import it into a UML model and validate.
                
                NOTES: There is still an issue with things that are both types and elements.  At the moment, the documentation should be transferred manually.
                                There is some sort of bug in the XML Schema import code that doesn't transfer documentation if the type or base isn't qualified.  Make sure that all XML types and bases are qualified!!!

        -->
        <xsl:param name="SourceDocumentName">../LexGrid200901_xsd.xmi</xsl:param>
        <xsl:param name="TargetDocumentName">../LexGrid200901.xmi</xsl:param>
        <!-- Set this to "true" if you want MODE tags in the documentation -->
        <xsl:param name="trace" select="false()"/>

        <xsl:template match="/">
                <xsl:apply-templates select="document($TargetDocumentName)" mode="in"/>
        </xsl:template>

        <!-- Documentation element can occur as:
                1)  a component of an attribute
                2) The source or target roles in a connector
                3) The connector itself
        -->
        <xsl:template match="documentation" mode="in">
                <xsl:variable name="docElement" select="."/>
                <xsl:variable name="parentElement" select="$docElement/.."/>
                
                <xsl:element name="documentation">

                        <xsl:choose>

                                <!-- Attribute Documentation -->
                                <xsl:when test="$parentElement/name() = 'attribute'">
                                        <xsl:variable name="containingElement" select="ancestor::node()[name()='element']"/>
                                        <xsl:variable name="sourceNode" select="document($SourceDocumentName)//element[@name=$containingElement/@name]//attribute[@name=$parentElement/@name]"/>
                                        <xsl:choose>

                                                <!-- Can't locate the attribute in the XML Schema -->
                                                <xsl:when test="not($sourceNode)">
                                                        <xsl:choose>
                                                                <xsl:when test="string-length($docElement/@value) = 0"/>
                                                                <xsl:otherwise>
                                                                        <xsl:attribute name="ERROR" select="concat('attribute: ',$parentElement/@name,' not found for ',$containingElement/@name)"/>
                                                                </xsl:otherwise>
                                                        </xsl:choose>
                                                        <xsl:copy-of select="@*[name()!='MODE' and name()!='ERROR']"/>
                                                </xsl:when>

                                                <!-- Found it in the XML Schema -->
                                                <xsl:when test="count($sourceNode) = 1">
                                                        <xsl:choose>
                                                                <xsl:when test="$sourceNode/documentation/@value=$docElement/@value">
                                                                        <xsl:attribute name="value" select="$docElement/@value"/>
                                                                        <xsl:if test="$trace">
                                                                                <xsl:attribute name="MODE">unchanged</xsl:attribute>
                                                                        </xsl:if>
                                                                </xsl:when>
                                                                <xsl:otherwise>
                                                                        <xsl:attribute name="value" select="$sourceNode/documentation/@value"/>
                                                                        <xsl:if test="$trace">
                                                                                <xsl:attribute name="MODE">updated</xsl:attribute>
                                                                        </xsl:if>
                                                                </xsl:otherwise>
                                                        </xsl:choose>
                                                        <xsl:copy-of select="@*[name()!='value' and name()!='MODE' and name()!='ERROR']"/>
                                                </xsl:when>
                                                <xsl:otherwise/>
                                        </xsl:choose>
                                </xsl:when>

                                <!-- Source documentation -->
                                <xsl:when test="$parentElement/name()='source'">

                                        <!-- Translate the connector into a combination of package / class / attribute name -->
                                        <xsl:variable name="connectorId" select="$parentElement/../@xmi:idref"/>
                                        <xsl:variable name="conOwnedAttribute" select="document($TargetDocumentName)//ownedAttribute[@association=$connectorId]"/>
                                        <xsl:variable name="conSrcName" select="$conOwnedAttribute/parent::node()/@name"/>
                                        <xsl:variable name="conSrcType" select="$conOwnedAttribute/parent::node()/@xmi:type"/>
                                        <xsl:variable name="conPackageName" select="$conOwnedAttribute/parent::node()/parent::node()/@name"/>
                                       
                                        <!-- The XML Schema mapping puts this in element/attribute -->
                                        <!--xsl:variable name="srcConnectorId" select="document($SourceDocumentName)//packagedElement[@xmi:type='uml:Package' and upper-case(@name)=upper-case($conPackageName)]/packagedElement[@xmi:type=$conSrcType and @name=$conSrcName and ownedAttribute]/ownedAttribute[@name=$conOwnedAttribute/@name]/@association"/-->
                                        <xsl:variable name="target" select="document($SourceDocumentName)//element[@xmi:type=$conSrcType and @name=$conSrcName]/attributes/attribute[@name=$conOwnedAttribute/@name]"/>
                                        <xsl:choose>
                                                <xsl:when test="not($target)">
                                                        <xsl:if test="$docElement/@value">
                                                                <xsl:attribute name="ERROR">Unable to located corresponding documentation</xsl:attribute>
                                                        </xsl:if>
                                                </xsl:when>
                                                <xsl:when test="not($target/documentation/@value)">
                                                        <xsl:if test="$docElement/@value">
                                                                <xsl:attribute name="ERROR">No documentation for target</xsl:attribute>
                                                        </xsl:if>
                                                </xsl:when>
                                                <xsl:when test="$target/documentation/@value = $docElement/@value">
                                                        <xsl:attribute name="value" select="$target/documentation/@value"/>
                                                        <xsl:if test="$trace">
                                                                <xsl:attribute name="MODE">unchanged</xsl:attribute>
                                                        </xsl:if>
                                                </xsl:when>
                                                <xsl:otherwise>
                                                        <xsl:attribute name="value" select="$target/documentation/@value"/>
                                                        <xsl:if test="$trace">
                                                                <xsl:attribute name="MODE">updated</xsl:attribute>
                                                        </xsl:if>
                                                </xsl:otherwise>
                                        </xsl:choose>
                                        <xsl:copy-of select="@*[name() != 'value' and name()!='MODE' and name()!='ERROR']"/>
                                </xsl:when>

                                <!-- Connector documentation -->
                                <xsl:when test="$parentElement/name()='connector'">
                                        <xsl:if test="$docElement/@value">
                                                <xsl:attribute name="ERROR" select="$parentElement/name()"/>
                                        </xsl:if>
                                        <xsl:copy-of select="@*[name()!='MODE' and name()!='ERROR']"/>
                                </xsl:when>

                                <!-- Unknown documentation -->
                                <xsl:otherwise>
                                        <xsl:if test="$docElement/@value">
                                                <xsl:attribute name="ERROR">Unrecognized documentation entry</xsl:attribute>
                                        </xsl:if>
                                        <xsl:copy-of select="@*[name()!='MODE' and name()!='ERROR']"/>
                                </xsl:otherwise>
                        </xsl:choose>

                </xsl:element>

        </xsl:template>

        <xsl:template match="properties" mode="in">
                <xsl:choose>
                        <xsl:when test="../name()!='element'">
                                <xsl:copy>
                                        <xsl:apply-templates select="@*|node()" mode="in"/>
                                </xsl:copy>
                        </xsl:when>
                        <xsl:otherwise>

                                <xsl:variable name="docElement" select="."/>
                                <xsl:variable name="parentElement" select="$docElement/.."/>
                                <xsl:variable name="sourceNode" select="document($SourceDocumentName)//element[@xmi:type=$parentElement/@xmi:type and @name=$parentElement/@name]"/>
                                <properties>
                                        <xsl:copy-of select="@*[name()!='documentation' and name()!='MODE' and name()!='ERROR']"/>

                                        <xsl:choose>
                                                <!-- Case A: element to be documented doesn't exist in the XML Schema -->
                                                <xsl:when test="not($sourceNode)">
                                                        <xsl:choose>
                                                                <!-- Notes and text fragments are not transferred to the model -->
                                                                <xsl:when test="not($docElement/@documentation) or $parentElement/@xmi:type = 'uml:Note' or $parentElement/@xmi:type = 'uml:Text'"/>
                                                                <xsl:otherwise>
                                                                        <xsl:attribute name="ERROR">NODE for <xsl:value-of select="$parentElement/@name"/> not found in source</xsl:attribute>
                                                                        <xsl:if test="$docElement/@documentation">
                                                                                <xsl:attribute name="documentation" select="$docElement/@documentation"/>
                                                                        </xsl:if>
                                                                </xsl:otherwise>
                                                        </xsl:choose>
                                                </xsl:when>

                                                <!-- Case B: There is a source node, but it has no documentation -->
                                                <xsl:when test="not($sourceNode/properties/@documentation)"/>



                                                <!-- Case C: source has documentation, but target doesn't -->
                                                <xsl:when test="not($docElement/@documentation)">
                                                        <xsl:attribute name="documentation" select="$sourceNode/*[name()=$docElement/name()]/@documentation"/>
                                                        <xsl:if test="$trace">
                                                                <xsl:attribute name="MODE">added</xsl:attribute>
                                                        </xsl:if>
                                                </xsl:when>

                                                <!-- Case D: element exists, but the documentation is already current -->
                                                <xsl:when test="$sourceNode/*[name()=$docElement/name()]/@documentation=$docElement/@documentation">
                                                        <xsl:attribute name="documentation" select="$docElement/@documentation"/>
                                                        <xsl:if test="$trace">
                                                                <xsl:attribute name="MODE">current</xsl:attribute>
                                                        </xsl:if>
                                                </xsl:when>

                                                <xsl:otherwise>
                                                        <xsl:attribute name="documentation" select="$sourceNode/*[name()=$docElement/name()]/@documentation"/>
                                                        <xsl:if test="$trace">
                                                                <xsl:attribute name="MODE">updated</xsl:attribute>
                                                        </xsl:if>
                                                </xsl:otherwise>
                                        </xsl:choose>
                                </properties>
                        </xsl:otherwise>

                </xsl:choose>
        </xsl:template>

        <xsl:template match="@*|node()" mode="in">
                <xsl:copy>
                        <xsl:apply-templates select="@*|node()" mode="in"/>
                </xsl:copy>
        </xsl:template>

</xsl:stylesheet>
