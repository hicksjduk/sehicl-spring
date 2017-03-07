<#include "status.ftl"/>

<#macro bowlingaverages averages current>
	<#if averages.status == "UNSTARTED">
		<p>Averages will be available once the season has started.</p>
	<#else>
		<#if current>
			<p class="statusMessage">
				<@statusMessage averages.status averages.lastIncludedDate averages.toCome/>
			</p>
			<p class="noprint">Click on a team to see the bowling averages for that team.</p>
		</#if>
		<p>Players (and bowling performances, for determining best bowling)	are ranked by wickets 
			taken, then average runs per over.</p>
		<table id="bowlav">
			<thead>
				<tr>
					<th class="position number"></th>
					<th class="name">Name</th>
					<th class="teamName name">Team</th>
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
							<#if (!lastRow??) || (row.compareSortKeys(lastRow) != 0)>${row?counter}</#if>
						</td>
						<td class="name">${row.player.name}</td>
						<td class="teamName name">
							<#if current>
								<a href="/averages/team/${row.team.id}#Bowling">${row.team.name}</a>
							<#else>
								${row.team.name}
							</#if>
						</td>
						<td class="overs number">${row.overs}</td>
						<td class="runs number">${row.runs}</td>
						<td class="wickets number">${row.wickets}</td>
						<td class="bestBowling number">${row.best.wicketsTaken}/${row.best.runsConceded}</td>
						<td class="averagePerWicket number"><#if row.averagePerWicket??>${row.averagePerWicket?string(".00")}</#if></td>
						<td class="averagePerOver number"><#if row.economyRate??>${row.economyRate?string(".00")}</#if></td>					
					</tr>
					<#assign lastRow = row/>
				</#list>
			</tbody>
		</table>
	</#if>
</#macro>