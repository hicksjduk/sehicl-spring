<h1>${team.name}</h1>
<#if batting.status == "UNSTARTED">
	<p>Averages will be available once the season has started.</p>
<#else>
	<#if current>
		<#include "status.ftl"/>
		<p class="statusMessage">
			<@statusMessage batting.status batting.lastIncludedDate batting.toCome/>
		</p>
	</#if>
	<h2><a name="Batting">Batting</a></h2>
	<#include "battingaverages.ftlh">
	<@battingaverages batting current false/>
	<h2><a name="Bowling">Bowling</a></h2>
	<#include "bowlingaverages.ftlh">
	<@bowlingaverages bowling current false/>
</#if>