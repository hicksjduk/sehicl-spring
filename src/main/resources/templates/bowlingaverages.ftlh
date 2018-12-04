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
					<tr>
						<td class="position number">
							<#if (row?counter == 1) || (row.compareSortKeys(lastRow) != 0)>${row?counter}</#if>
						</td>
						<td class="name">
							<a href="javascript: toggleVisible('bowling${row?counter}')">${row.player.name}</a></a>
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
					<tr class="expandedBowling" id="bowling${row?counter}" hidden="true">
						<td></td>
						<td colspan="${showTeam?then(8, 7)}">
							<table>							
							<#list row.performances as perf>
								<tr>
								<td class="figures number">${perf.performance.wicketsTaken}/${perf.performance.runsConceded}</td>
								<td class="overs number">(${perf.overs} ov)</td>
								<td class="match">${perf.matchDate?string("d MMM yy")}
								v ${perf.opponent.name}
								</td>
								</tr>
							</#list>
							</table>
						</td>
					</tr>
					<#assign lastRow = row/>
				</#list>
			</tbody>
		</table>
	</#if>
</#macro>