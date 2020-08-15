<#include "ordinaldate.ftl"/>
<#include "resultutils.ftlh"/>
<#assign season><#if fixtures.season??>/${fixtures.season}<#else></#if></#assign>
<h1>${fixtures.team.name} 
(<a href="/fixtures/league/${fixtures.league.id}${season}">${fixtures.league.name}</a>)
</h1>
<p class="noprint">Click on an opponent to see all matches for that team.</p>
<p class="noprint">Click on a result to see the match details,
or to hide them if displayed.</p>
<table id="teamfix">
	<thead>
		<tr>
			<th class="date">Date</th>
			<th class="time">Time</th>
			<th class="court">Court</th>
			<th class="opponent">Opponent</th>
			<th class="homeAway">H/A</th>
			<th class="result">Result</th>
		</tr>
	</thead>
	<tbody>
		<#list fixtures.rows as row>
			<tr>
				<td class="date"><@ordinalDate row.dateTime "MMM" "yy"/></td>
				<td class="time">${row.dateTime?string("h:mm")}</td>
				<td class="court">${row.court}</td>
				<td class="opponent"><a
					href="/fixtures/team/${row.opponent.id}${season}">${row.opponent.name}</a></td>
				<td class="homeAway">${row.home?then("H", "A")}</td>
				<td class="result">
					<#if row.result??>
						<#if row.resultDetails??>
							<a href="javascript:toggleVisible('result${row?counter}')">${row.result}</a>
						<#else>
							${row.result}
						</#if>
					</#if>
				</td>
			</tr>
			<#if row.resultDetails??>
				<#assign res = row.resultDetails/>
				<tr id="result${row?counter}" hidden="true">
					<td colspan="6" style="padding-left: 25px">
						<table id="reslist">
							<#if res.match.playedMatch??>
								<@match res.firstInnings res.secondInnings res.maxWickets/>
							<#else>
								<@walkover res/>
							</#if>
						</table>
					</td>
				</tr>
			</#if>
		</#list>
		</tbody>
</table>