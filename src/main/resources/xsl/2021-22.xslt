<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template
    	match="match[contains(homeTeam/@id, 'University') and contains(awayTeam/@id, 'University')]">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
            <unplayedMatch>
                <reason>Both teams failed to turn up</reason>
                <countAsPlayed>true</countAsPlayed>
            </unplayedMatch>
        </xsl:copy>
	</xsl:template>
    <xsl:template
        match="match[contains(homeTeam/@id, 'EmsworthA') and contains(awayTeam/@id, 'FriendsUnited')]">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
            <unplayedMatch>
                <reason>Players self-isolating due to Covid</reason>
                <countAsPlayed>false</countAsPlayed>
            </unplayedMatch>
        </xsl:copy>
    </xsl:template>
</xsl:stylesheet>
