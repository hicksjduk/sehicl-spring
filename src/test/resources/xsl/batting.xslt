<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="text"/>
	<xsl:variable name="quote">'</xsl:variable>
	<xsl:template match="/">
		<xsl:text>drop table if exists batting;
</xsl:text>
		<xsl:text>create table batting(leagueid,playerid,playername,teamid,teamname,runs,out);
</xsl:text>
		<xsl:apply-templates select="//batsman"/>
	</xsl:template>
	<xsl:template match="batsman">
		<xsl:variable name="id" select="@player"/>
		<xsl:variable name="teamId" select="ancestor::teamInMatch/teamRef/@id"/>
		<xsl:text>insert into batting values('</xsl:text>
		<xsl:value-of select="ancestor::league/@id"/>
		<xsl:text>','</xsl:text>
		<xsl:value-of select="$id"/>
		<xsl:text>','</xsl:text>
		<xsl:call-template name="doublequotes">
			<xsl:with-param name="str" select="//player[@id=$id]/name"/>
		</xsl:call-template>
		<xsl:text>','</xsl:text>
		<xsl:value-of select="$teamId"/>
		<xsl:text>','</xsl:text>
		<xsl:value-of select="//team[@id=$teamId]/name"/>
		<xsl:text>',</xsl:text>
		<xsl:value-of select="runs"/>
		<xsl:text>,'</xsl:text>
		<xsl:value-of select="out"/>
		<xsl:text>');
</xsl:text>
	</xsl:template>
	<xsl:template name="doublequotes">
		<xsl:param name="str"/>
		<xsl:variable name="quote">'</xsl:variable>
		<xsl:choose>
			<xsl:when test="contains($str, $quote)">
				<xsl:value-of select="substring-before($str, $quote)"/>
				<xsl:value-of select="concat($quote, $quote)"/>
				<xsl:call-template name="doublequotes">
					<xsl:with-param name="str" select="substring-after($str, $quote)"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$str"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>