<h1>${pageHeading}</h1>
<#if averages.status == "UNSTARTED">
	<p>Averages will be available once the season has started.</p>
<#else>
	<#if current>
		<#include "status.ftl"/>
		<p class="statusMessage">
			<@statusMessage averages.status averages.lastIncludedDate averages.toCome "averages"/>
		</p>
		<p class="noprint">Click on a team to see the batting averages for that team.</p>
	</#if>
	<#include "bowlingaverages.ftlh">
	<@bowlingaverages averages current true/>
</#if>