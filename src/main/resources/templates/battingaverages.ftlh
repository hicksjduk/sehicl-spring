<#macro battingaverages averages current showTeam>
	<#if averages.status != "UNSTARTED">
		<p class="noprint">Click on a player to see that player's batting performances match-by-match,
		or to hide them if displayed.</p>
		<p>Players are ranked by runs scored.</p>
		<table id="batav">
			<thead>
				<tr>
					<th class="position number"></th>
					<th class="name">Name</th>
					<#if showTeam>
						<th class="teamName name">Team</th>
					</#if>
					<th class="innings number">Inns</th>
					<th class="notout number">NO</th>
					<th class="runs number">Runs</th>
					<th class="highscore number">HS</th>
					<th class="average number">Average</th>
				</tr>
			</thead>
			<tbody>
				<#list averages.rows as row>
					<#assign expand=row.performances?size != 0/>
					<tr>
						<td class="position number">
							<#if (row?counter == 1) || (row.compareSortKeys(lastRow) != 0)>${row?counter}</#if>
						</td>
						<td class="name">
							<#if expand>
								<a id="expanded"><a href="?">${row.player.name}</a></a>
							<#else>						
								<a href="?expandBatting=${row.player.id}#expanded">${row.player.name}</a>
							</#if>
						</td>
						<#if showTeam>
							<td class="teamName name">
								<#if current>
									<a href="/averages/team/${row.team.id}#Batting">${row.team.name}</a>
								<#else>
									${row.team.name}
								</#if>
							</td>
						</#if>
						<td class="innings number">${row.innings}</td>
						<td class="notout number">${row.notOut}</td>
						<td class="runs number">${row.runs}</td>
						<td class="highscore number">${row.best.runsScored}${row.best.out?then("","*")}</td>
						<td class="average number"><#if row.average??>${row.average?string("0.00")}</#if></td>
					</tr>
					<#if expand>
						<tr class="expandedBatting">
							<td></td>
							<td colspan="${showTeam?then(7, 6)}">
								<ul>							
								<#list row.performances as perf>
									<li>${perf.matchDate?string("d MMM yy")}:
									${perf.performance.runsScored}${perf.performance.out?then("","*")}
									v ${perf.opponent.name}
								</#list>
								</ul>
							</td>
						</tr>
					</#if>
					<#assign lastRow = row/>
				</#list>
			</tbody>
		</table>
	</#if>
</#macro>