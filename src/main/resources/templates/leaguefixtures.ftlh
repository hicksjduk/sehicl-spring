<#include "ordinaldate.ftl"/>
<h1>${(fixtures.league.name)!"Fixtures"}</h1>
<#if fixtures.rows?size == 0>
	<#if fixtures.matchesExist>
		All matches have been played - there are no outstanding fixtures.
	<#else>
		Fixtures will be published shortly.
	</#if>
<#else>
    <#assign season><#if fixtures.season??>/${fixtures.season}<#else></#if></#assign>
	<p class="noprint">
		<#if fixtures.league??>
			Click on a team to see all matches for that team.
		<#else>
			Click on a team to see all matches for that team, or on a division to see all matches for that division.
		</#if>
	</p>
	<table id="fixlist">
		<#list fixtures.rows as row>
			<#assign date = (row.match.dateTime?string("yyyyMMdd"))!"TBA" />
			<#assign time = (row.match.dateTime?string("h:mm"))!""/>
			<#if date != lastDate!"">
				<#assign lastDate = date, lastTime = ""/>
				<tr>
					<td class="date" colspan="3">
						<#if row.match.dateTime??>
							<@ordinalDate row.match.dateTime "MMMM" "yyyy"/>
						<#else>
							To be announced
						</#if>
					</td>
				</tr>
			</#if>
			<tr>
				<td class="time">${(time != lastTime)?then(time, "")}</td>
				<#assign lastTime = time/>
				<td class="court">${row.match.court}</td>
				<td class="teams">
					<a href="/fixtures/team/${row.homeTeam.id}${season}">${row.homeTeam.name}</a> 
						v 
					<a href="/fixtures/team/${row.awayTeam.id}${season}">${row.awayTeam.name}</a>
				<#if !(fixtures.league??)>
					<td class="division">
						<a href="/fixtures/league/${row.league.id}${season}">${row.league.name}</a>
					</td>
				</#if>
			</tr>
		</#list>
	</table>
</#if>