<#macro bowlingaverages averages current showTeam>
	<#if averages.status != "UNSTARTED">
		<p class="noprint">Click on a player to see that player's bowling performances match-by-match,
		or to hide them if displayed.</p>
		<p>Players (and bowling performances, for determining best bowling)	are ranked by wickets 
			taken, then average runs per over.</p>
		<table id="bowlav">
			<thead>
				<tr>
					<th class="position number"></th>
					<th class="name">Name</th>
					<#if showTeam>
						<th class="teamName name">Team</th>
					</#if>
					<th class="overs number">Overs</th>
					<th class="runs number">Runs</th>
					<th class="wickets number">Wickets</th>
					<th class="bestBowling number">Best</th>
					<th class="averagePerWicket number">Runs/wkt</th>
					<th class="averagePerOver number">Runs/over</th>
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
								<a href="?expandBowling=${row.player.id}#expanded">${row.player.name}</a>
							</#if>
						</td>
						<#if showTeam>
							<td class="teamName name">
								<#if current>
									<a href="/averages/team/${row.team.id}#Bowling">${row.team.name}</a>
								<#else>
									${row.team.name}
								</#if>
							</td>
						</#if>
						<td class="overs number">${row.overs}</td>
						<td class="runs number">${row.runs}</td>
						<td class="wickets number">${row.wickets}</td>
						<td class="bestBowling number">${row.best.wicketsTaken}/${row.best.runsConceded}</td>
						<td class="averagePerWicket number"><#if row.averagePerWicket??>${row.averagePerWicket?string("0.00")}</#if></td>
						<td class="averagePerOver number"><#if row.economyRate??>${row.economyRate?string("0.00")}</#if></td>					
					</tr>
					<#if expand>
						<tr class="expandedBowling">
							<td></td>
							<td colspan="${showTeam?then(8, 7)}">
								<ul>							
								<#list row.performances as perf>
									<li>${perf.matchDate?string("d MMM yy")}:
									${perf.performance.wicketsTaken}/${perf.performance.runsConceded}
									(${perf.overs} ov)
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