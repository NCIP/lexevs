<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:uml="http://schema.omg.org/spec/UML/2.1" xmlns:xmi="http://schema.omg.org/spec/XMI/2.1" xmlns:EAUML="http://www.sparxsystems.com/profiles/EAUML/1.0" xmlns:thecustomprofile="http://www.sparxsystems.com/profiles/thecustomprofile/1.0" version="2.0">
        <xsl:output method="html" indent="yes"/>
        <!-- Compare two XMI 2.1 renderings of what is theoretically the same model.  At the moment, it is very LexGrid specific.
                To use this transform:
                1) Create a new EA project (we are currently using EA 7.1)
                2) Import all of the LexGrid XML Schema (note: EA doesn't import documentation with imbedded markup correctly...)
                3) Export the project as XML - this is the source
                4) Load the UML project
                5) Export the project as XML - this is the target
                
                Issues regarding model imports:
                1) wildCard does not transfer - this should be compared manually
                2) minLength does not transfer - this should be checked  manually
                3) Pretty diagram boxes turn out to be classes (!) - they won't match
                4) Attribute cardinality is ignored - all attributes are imported as 1:1, but we render them as [1:*]  - this is because the default upper bound is "0", which is "1" in attributes and "*" in associations (/)
                5) Sometimes generalizations get missed in the export - as an example, right now, versionableAndDescribable is definitely a parent of codingScheme. It shows in the model, but not in the XMI export (!)
        -->
        <xsl:param name="SourceDocumentName">../LexGrid200901.xmi</xsl:param>
        <xsl:param name="TargetDocumentName">../LexGrid200901_xsd.xmi</xsl:param>

        <!-- Start examining the source document -->
        <xsl:template match="/">
                <head>
                        <title>Comparison of <xsl:value-of select="$SourceDocumentName"/> and <xsl:value-of select="$TargetDocumentName"/></title>
                </head>
                <body>
                        <h2>Comparison of <xsl:value-of select="$SourceDocumentName"/> and <xsl:value-of select="$TargetDocumentName"/> on <xsl:value-of select="current-dateTime()"/></h2>
                                <xsl:for-each select="document($SourceDocumentName)/xmi:XMI//packagedElement[@xmi:type='uml:Package' and @name='Model']/packagedElement[@xmi:type='uml:Package' and @name='LexGrid200901']">
                                <xsl:apply-templates mode="packages" select="*">
                                <xsl:with-param name="SourceDocument" select="$SourceDocumentName"/>
                                <xsl:with-param name="TargetDocument" select="$TargetDocumentName"/>
                                <xsl:with-param name="reverse" select="true()"/>
                                </xsl:apply-templates>
                                </xsl:for-each>
                        <hr/>
                        <!-- Reverse comparison -->
                        <h2>Comparison of <xsl:value-of select="$TargetDocumentName"/> and <xsl:value-of select="$SourceDocumentName"/> on <xsl:value-of select="current-dateTime()"/></h2>
                        <xsl:for-each select="document($TargetDocumentName)/xmi:XMI//packagedElement[@xmi:type='uml:Package' and @name='Model']">
                                <xsl:apply-templates mode="packages" select="*">
                                        <xsl:with-param name="SourceDocument" select="$TargetDocumentName"/>
                                        <xsl:with-param name="TargetDocument" select="$SourceDocumentName"/>
                                        <xsl:with-param name="reverse" select="true()"/>
                                </xsl:apply-templates>
                        </xsl:for-each>
                </body>
        </xsl:template>

        <!-- Evaluate each package -->
        <xsl:template match="packagedElement" mode="packages">
                <xsl:param name="SourceDocument"/>
                <xsl:param name="TargetDocument"/>
                <xsl:param name="reverse"/>
                <table border="1">
                        <caption>Package: <xsl:value-of select="@name"/></caption>

                        <xsl:variable name="pkgName" select="upper-case(@name)"/>
                        <xsl:variable name="targetPackage" select="document($TargetDocument)//packagedElement[@xmi:type='uml:Package' and upper-case(@name) = $pkgName]"/>
                        <xsl:choose>
                                <xsl:when test="not($targetPackage)">
                                        <tr>
                                                <th>Package not found in target!</th>
                                        </tr>
                                </xsl:when>
                                <xsl:otherwise>
                                        <tr>
                                                <th>Type</th>
                                                <th>Name</th>
                                                <th>Error</th>
                                                <th>Detail</th>
                                        </tr>
                                </xsl:otherwise>
                        </xsl:choose>
                        <xsl:for-each select="packagedElement">
                                <xsl:choose>
                                        <xsl:when test="@xmi:type='uml:Class'">
                                                <xsl:call-template name="checkClass">
                                                        <xsl:with-param name="targetPackage" select="$targetPackage"/>
                                                        <xsl:with-param name="SourceDocument" select="$SourceDocument"/>
                                                        <xsl:with-param name="TargetDocument" select="$TargetDocument"/>
                                                        <xsl:with-param name="reverse" select="$reverse"/>
                                                </xsl:call-template>
                                        </xsl:when>
                                        <xsl:when test="@xmi:type='uml:Association'">
                                                <xsl:call-template name="checkAssociation">
                                                        <xsl:with-param name="targetPackage" select="$targetPackage"/>
                                                        <xsl:with-param name="SourceDocument" select="$SourceDocument"/>
                                                        <xsl:with-param name="TargetDocument" select="$TargetDocument"/>
                                                        <xsl:with-param name="reverse" select="$reverse"/>
                                                </xsl:call-template>
                                        </xsl:when>
                                        <xsl:when test="@xmi:type='uml:Dependency'">
                                                <xsl:call-template name="checkDependency">
                                                        <xsl:with-param name="targetPackage" select="$targetPackage"/>
                                                        <xsl:with-param name="SourceDocument" select="$SourceDocument"/>
                                                        <xsl:with-param name="TargetDocument" select="$TargetDocument"/>
                                                        <xsl:with-param name="reverse" select="$reverse"/>
                                                </xsl:call-template>
                                        </xsl:when>
                                        <xsl:when test="@xmi:type='uml:Enumeration'">
                                                <xsl:call-template name="checkEnumeration">
                                                        <xsl:with-param name="targetPackage" select="$targetPackage"/>
                                                        <xsl:with-param name="SourceDocument" select="$SourceDocument"/>
                                                        <xsl:with-param name="TargetDocument" select="$TargetDocument"/>
                                                        <xsl:with-param name="reverse" select="$reverse"/>
                                                </xsl:call-template>
                                        </xsl:when>
                                        <xsl:otherwise>
                                                <tr>
                                                        <td>
                                                                <xsl:value-of select="@name"/>
                                                        </td>
                                                        <td>
                                                                <xsl:value-of select="@xmi:type"/>
                                                        </td>
                                                        <xsl:if test="not($targetPackage/packagedElement[@xmi:type=current()/@xmi:type and @name=current/@name])">
                                                                <td/>
                                                                <td>missing in target</td>
                                                        </xsl:if>
                                                </tr>
                                        </xsl:otherwise>
                                </xsl:choose>
                        </xsl:for-each>
                </table>
        </xsl:template>

        <!-- Check the attributes, associations and cardinalities of the supplied class.
                State: Positioned on the class to check in the source document
                Param:  targetPackage - The package that should contain the class in the target document
          -->
        <xsl:template name="checkClass">
                <xsl:param name="targetPackage"/>
                <xsl:param name="SourceDocument"/>
                <xsl:param name="TargetDocument"/>
                <xsl:param name="reverse"/>
                <xsl:variable name="className" select="@name"/>
                <tr>
                        <td>Class</td>
                        <td>
                                <xsl:value-of select="@name"/>
                        </td>
                        <xsl:choose>
                                <xsl:when test="count($targetPackage/packagedElement[@xmi:type='uml:Class' and @name=$className]) = 0">
                                        <td>Missing in target package</td>
                                        <td/>
                                </xsl:when>
                                <xsl:otherwise>
                                        <td/>
                                        <td/>
                                </xsl:otherwise>
                        </xsl:choose>
                </tr>
                <xsl:choose>
                        <xsl:when test="count($targetPackage/packagedElement[@xmi:type='uml:Class' and @name=$className]) = 0"/>
                        <xsl:otherwise>
                                <xsl:variable name="targetClass" select="$targetPackage/packagedElement[@xmi:type='uml:Class' and @name=current()/@name]"/>
                                <xsl:for-each select="*">
                                        <xsl:choose>
                                                <xsl:when test="name()='ownedAttribute'">
                                                        <xsl:variable name="targetAttribute" select="$targetClass/ownedAttribute[@name=current()/@name and @isDerived='false']"/>
                                                        <xsl:choose>
                                                                <xsl:when test="string-length(@name) = 0"/>
                                                                <xsl:when test="@isDerived='true'"/>
                                                                <xsl:when test="count($targetAttribute) = 0">
                                                                        <tr>
                                                                                <td/>
                                                                                <td/>
                                                                                <td>
                                                                                        <xsl:value-of select="@name"/>
                                                                                </td>
                                                                                <td>Missing attribute</td>
                                                                        </tr>
                                                                </xsl:when>
                                                                <xsl:otherwise>
                                                                        <xsl:variable name="srcType">
                                                                                <xsl:call-template name="stripNamespace">
                                                                                        <xsl:with-param name="name">
                                                                                                <xsl:value-of select="document($SourceDocument)//packagedElement[(@xmi:type='uml:Class' or @xmi:type='uml:PrimitiveType') and @xmi:id=current()/type/@xmi:idref]/@name"/>
                                                                                        </xsl:with-param>
                                                                                </xsl:call-template>
                                                                        </xsl:variable>
                                                                        <xsl:variable name="targetType">
                                                                                <xsl:call-template name="stripNamespace">
                                                                                        <xsl:with-param name="name">
                                                                                                <xsl:value-of select="document($TargetDocument)//packagedElement[(@xmi:type='uml:Class' or @xmi:type='uml:PrimitiveType') and @xmi:id=$targetAttribute/type/@xmi:idref]/@name"/>
                                                                                        </xsl:with-param>
                                                                                </xsl:call-template>
                                                                        </xsl:variable>
                                                                        <xsl:if test="$srcType != $targetType">
                                                                                <tr>
                                                                                        <td/>
                                                                                        <td/>
                                                                                        <td>
                                                                                                <xsl:value-of select="@name"/>
                                                                                        </td>
                                                                                        <td>
                                                                                                <xsl:text>type mismatch - source type: </xsl:text>
                                                                                                <xsl:value-of select="$srcType"/>
                                                                                                <xsl:text> , target type: </xsl:text>
                                                                                                <xsl:value-of select="$targetType"/>
                                                                                        </td>
                                                                                </tr>
                                                                        </xsl:if>
                                                                        <xsl:if test="not($reverse)">
                                                                                <xsl:variable name="suv">
                                                                                        <xsl:call-template name="normalizeUV">
                                                                                                <xsl:with-param name="uv" select="upperValue/@value"/>
                                                                                        </xsl:call-template>
                                                                                </xsl:variable>
                                                                                <xsl:variable name="tuv">
                                                                                        <xsl:call-template name="normalizeUV">
                                                                                                <xsl:with-param name="uv" select="$targetAttribute/upperValue/@value"/>
                                                                                        </xsl:call-template>
                                                                                </xsl:variable>
                                                                                <xsl:if test="lowerValue/@value != $targetAttribute/lowerValue/@value or $suv != $tuv">
                                                                                        <tr>
                                                                                                <td/>
                                                                                                <td/>
                                                                                                <td>
                                                                                                        <xsl:value-of select="@name"/>
                                                                                                </td>
                                                                                                <td>
                                                                                                        <xsl:text>cardinality mismatch - source: </xsl:text>
                                                                                                        <xsl:value-of select="concat('[',lowerValue/@value,':',$suv,'] target: ')"/>
                                                                                                        <xsl:value-of select="concat('[',$targetAttribute/lowerValue/@value,':',$tuv,']')"/>
                                                                                                </td>
                                                                                        </tr>
                                                                                </xsl:if>
                                                                        </xsl:if>
                                                                </xsl:otherwise>
                                                        </xsl:choose>
                                                </xsl:when>
                                                <xsl:when test="name()='generalization'">
                                                        <xsl:variable name="srcGeneral">
                                                                <xsl:value-of select="document($SourceDocument)//packagedElement[(@xmi:type='uml:Class' or @xmi:type='uml:PrimitiveType') and @xmi:id=current()/@general]/@name"/>
                                                        </xsl:variable>
                                                        <xsl:variable name="targetGeneral">
                                                                <xsl:value-of select="document($TargetDocument)//packagedElement[(@xmi:type='uml:Class' or @xmi:type='uml:PrimitiveType') and @xmi:id=$targetClass/generalization/@general]/@name"/>
                                                        </xsl:variable>
                                                        <xsl:if test="$srcGeneral != $targetGeneral">
                                                                <tr>
                                                                        <td/>
                                                                        <td/>
                                                                        <td>parent: <xsl:value-of select="$srcGeneral"/>
                                                                        </td>
                                                                        <xsl:choose>
                                                                                <xsl:when test="string-length($targetGeneral) > 0">
                                                                                        <td>target parent mismatch: <xsl:value-of select="$targetGeneral"/></td>
                                                                                </xsl:when>
                                                                                <xsl:otherwise>
                                                                                        <td>no target generalization present</td>
                                                                                </xsl:otherwise>
                                                                        </xsl:choose>
                                                                </tr>
                                                        </xsl:if>
                                                </xsl:when>
                                                <xsl:otherwise>
                                                        <tr>
                                                                <td/>
                                                                <td/>
                                                                <td>
                                                                        <xsl:value-of select="name()"/>
                                                                </td>
                                                                <td>Unhandled type</td>
                                                        </tr>
                                                </xsl:otherwise>
                                        </xsl:choose>
                                </xsl:for-each>
                        </xsl:otherwise>
                </xsl:choose>
        </xsl:template>

        <xsl:template name="checkAssociation">
                <xsl:param name="targetPackage"/>
                <xsl:param name="SourceDocument"/>
                <xsl:param name="TargetDocument"/>
                <xsl:param name="reverse"/>

                <!-- To the best of our knowledge, everything in association is also checked in ownedAttribute -->
        </xsl:template>

        <xsl:template name="checkDependency">
                <xsl:param name="targetPackage"/>
                <xsl:param name="SourceDocument"/>
                <xsl:param name="TargetDocument"/>
                <xsl:param name="reverse"/>

                <!-- Not currently checked -->

        </xsl:template>

        <xsl:template name="checkEnumeration">
                <xsl:param name="targetPackage"/>
                <xsl:param name="SourceDocument"/>
                <xsl:param name="TargetDocument"/>
                <xsl:param name="reverse"/>

                <xsl:variable name="enumName" select="@name"/>
                <xsl:variable name="targetClass" select="$targetPackage//packagedElement[@xmi:type='uml:Enumeration' and @name=$enumName]"/>

                <xsl:for-each select="ownedLiteral">
                        <xsl:if test="count($targetClass/ownedLiteral[@name=current()/@name]) = 0">
                                <tr>
                                        <td>
                                                <xsl:value-of select="$targetClass/@name"/>
                                        </td>
                                        <td/>
                                        <td>Enumeration: <xsl:value-of select="$enumName"/></td>
                                        <td>Value: "<xsl:value-of select="@name"/>" missing in target</td>
                                </tr>
                        </xsl:if>
                </xsl:for-each>

        </xsl:template>

        <xsl:template name="stripNamespace">
                <xsl:param name="name"/>
                <xsl:choose>
                        <xsl:when test="contains($name,':')">
                                <xsl:value-of select="substring-after($name,':')"/>
                        </xsl:when>
                        <xsl:otherwise>
                                <xsl:value-of select="$name"/>
                        </xsl:otherwise>
                </xsl:choose>
        </xsl:template>

        <xsl:template name="normalizeUV">
                <xsl:param name="uv"/>
                <xsl:choose>
                        <xsl:when test="$uv = 0">1</xsl:when>
                        <xsl:when test="$uv = -1">*</xsl:when>
                        <xsl:otherwise>
                                <xsl:value-of select="$uv"/>
                        </xsl:otherwise>
                </xsl:choose>
        </xsl:template>

        <xsl:template match="text()" mode="#all"/>

</xsl:stylesheet>
