<xsl:stylesheet  version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:pom="http://maven.apache.org/POM/4.0.0" exclude-result-prefixes="pom">

<xsl:output method="text" encoding="utf-8" />
<xsl:strip-space elements="*"/>

<xsl:template match="pom:project">
<xsl:value-of select="pom:version"/>
</xsl:template>

</xsl:stylesheet>
