<#include "ordinaldate.ftl"/>
<#include "resultutils.ftlh"/>

<h1>
	<#if results.date??>
		Results: <@ordinalDate results.date "MMMM" "yyyy"/>
	<#elseif results.league??>
		${results.league.name}
	<#else>
		Results
	</#if>
</h1>
<#if results.results?size == 0>
	<p>Results will be available once the season has started.</p>
<#else>

	<#if ((results.allDates![])?size > 1)>
		<ul id="datenav" class="noprint">
			<li>Results on other dates:</li>
			<#list results.allDates as date>
				<#if date?string("yyyyMMdd") != results.date?string("yyyyMMdd")>
					<li>
						<#if date?counter == 1>
							<a href="/results">
						<#else>
							<a href="/results/date/${date?string("yyyyMMdd")}">
						</#if>
						<@ordinalDate date "MMM" ""/>
						</a>
					</li>
				</#if>
			</#list>
		</ul>
	</#if>

	<p class="noprint">Click on a team to see all matches for that team.</p>
	
	<table id="reslist">
	
	<#list results.results as res>
		<#if (results.date??) && (res.league.id != (lastRes.league.id)!"")>
			<tr>
				<td class="division" colspan="2">${res.league.name}</td>
			</tr>
		</#if>
		<#if results.league??>
			<#assign dateStr = (res.match.dateTime?string("yyyyMMdd"))!"TBA"/>
			<#assign lastDateStr = (lastRes??)?then((lastRes.match.dateTime?string("yyyyMMdd"))!"TBA", "")/>
			<#if dateStr != lastDateStr>
				<tr>
					<td class="date" colspan="2">
						<#if dateStr == "TBA">
							${dateStr}
						<#else>
							<@ordinalDate res.match.dateTime "MMMM" "yyyy"/>
						</#if>
					</td>
				</tr>
			</#if>
		</#if>
		<#assign lastRes = res/>
		<#if res.match.playedMatch??>
			<@match res.firstInnings res.secondInnings res.maxWickets/>
		<#else>
			<@walkover res/>
		</#if>
	</#list>

	</table>
</#if>