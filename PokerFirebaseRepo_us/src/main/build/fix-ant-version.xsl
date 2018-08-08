<xsl:stylesheet  version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"  xmlns:artifact="antlib:org.apache.maven.artifact.ant" exclude-result-prefixes="artifact">

<xsl:template match="node() | @*">
   <xsl:copy>
      <xsl:apply-templates select="node() | @*"/>
   </xsl:copy>
</xsl:template>

<xsl:template match="//property[@name='version']/@value">
   <xsl:value-of select="$version"/>
</xsl:template>

</xsl:stylesheet>
