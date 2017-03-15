<#include "ordinaldate.ftl"/>
<#include "results.ftlh"/>

<#if !(results.date??)>
	<h1>Results>
	<p>Results will be available once the season has started.</p>

	<#else>

	<h1>Results: <@ordinalDate results.date "MMMM" "yyyy"/></h1>


	<#if results.allDates?size != 0>
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
		<#if res.league.id != (lastRes.league.id)!"">
			<tr>
				<td class="division" colspan="2">${res.league.name}</td>
			</tr>
		</#if>
		<#assign lastRes = res/>
		<tr>
			<#if (res.match.playedMatch)??>
				<@match res.firstInnings res.secondInnings res.maxWickets/>
			<#else>
				<td class="teamscore" colspan="2">
					<a name="${res.match.homeTeamId}v${res.match.awayTeamId}"></a>
					<a name="${res.match.awayTeamId}v${res.match.homeTeamId}"></a>
					<a href="/fixtures/team/${res.winnerId}">${res.winnerName}</a> beat 
					<a href="/fixtures/team/${res.loserId}">${res.loserName}</a>  
					by default (${res.reason})
				</td>
			</#if>
		<tr>
	</#list>

	</table>
</#if>