<#include "status.ftl"/>

<#macro battingaverages averages current>
	<#if averages.status == "UNSTARTED">
		<p>Averages will be available once the season has started.</p>
	<#else>
		<#if current>
			<p class="statusMessage">
				<@statusMessage averages.status averages.lastIncludedDate averages.toCome/>
			</p>
			<p class="noprint">Click on a team to see the batting averages for that team.</p>
		</#if>
		<p>Players are ranked by runs scored.</p>
		<table id="batav">
			<thead>
				<tr>
					<th class="position number"></th>
					<th class="name">Name</th>
					<th class="teamName name">Team</th>
					<th class="innings number">Inns</th>
					<th class="notout number">NO</th>
					<th class="runs number">Runs</th>
					<th class="highscore number">HS</th>
					<th class="average number">Average</th>
				</tr>
			</thead>
			<tbody>
				<#list averages.rows as row>
					<tr>
						<td class="position number">
							<#if (!lastRow??) || (row.compareSortKeys(lastRow) != 0)>${row?counter}</#if>
						</td>
						<td class="name">${row.player.name}</td>
						<td class="teamName name">
							<#if current>
								<a href="/averages/team/${row.team.id}#Batting">${row.team.name}</a>
							<#else>
								${row.team.name}
							</#if>
						</td>
						<td class="innings number">${row.innings}</td>
						<td class="notout number">${row.notOut}</td>
						<td class="runs number">${row.runs}</td>
						<td class="highscore number">${row.best.runsScored}${row.best.out?then("","*")}</td>
						<td class="average number"><#if row.average??>${row.average?string(".00")}</#if></td>
					</tr>
					<#assign lastRow = row/>
				</#list>
			</tbody>
		</table>
	</#if>
</#macro>