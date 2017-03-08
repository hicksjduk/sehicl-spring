<#include "ordinaldate.ftl"/>
<h1>${fixtures.team.name} 
(<a href="/fixtures/league/${fixtures.league.id}">${fixtures.league.name}</a>)
</h1>
<table id="teamfix">
	<thead>
		<tr>
			<th class="date">Date</th>
			<th class="time">Time</th>
			<th class="court">Court</th>
			<th class="opponent">Opponent</th>
			<th class="homeAway">H/A</th>
		</tr>
	</thead>
	<tbody>
		<#list fixtures.rows as row>
			<tr>
				<td class="date"><@ordinalDate row.dateTime "MMM" "yy"/></td>
				<td class="time">${row.dateTime?string("h:mm")}</td>
				<td class="court">${row.court}</td>
				<td class="opponent"><a
					href="/fixtures/team/${row.opponent.id}">${row.opponent.name}</a></td>
				<td class="homeAway">${row.home?then("H", "A")}</td>
				<#if row.result??>
					<td class="result">
						<a href="/results/league/${fixtures.league.id}#${fixtures.team.id}v${row.opponent.id}">${row.result}</a>
					</td>
				</#if>
			</tr>
		</#list>
		</tbody>
</table>