<#macro battingaverages averages current showTeam>
	<#if averages.status != "UNSTARTED">
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
				<#local lastRow="">
				<#list averages.rows as row>
					<tr>
						<td class="position number">
							<#if (lastRow == "") || (row.compareSortKeys(lastRow) != 0)>${row?counter}</#if>
						</td>
						<td class="name">${row.player.name}</td>
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
						<td class="average number"><#if row.average??>${row.average?string(".00")}</#if></td>
					</tr>
					<#assign lastRow = row/>
				</#list>
			</tbody>
		</table>
	</#if>
</#macro>