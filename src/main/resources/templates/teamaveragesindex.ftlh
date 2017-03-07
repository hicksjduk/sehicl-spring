<h1>Averages by team</h1>
<table id="avgeindex">
	<#list teamsByLeague as league, teams>
		<#if league?index % 2 == 0>
			<tr>
		</#if>
		<td>
			<h3 class="divheading">${league.name}</h3>
			<table>
				<#list teams as team>
					<tr>
						<td class="teamName"><a href="/averages/team/${team.id}">${team.name}</a></td>
						<td class="batLink"><a href="/averages/team/${team.id}#Batting">Batting</a></td>
						<td class="bowlLink"><a href="/averages/team/${team.id}#Bowling">Bowling</a></td>
					</tr>
				</#list>
			</table>
		</td>
		<#if league?counter % 2 == 0 || league?counter == teamsByLeague?size>
			</tr>
		</#if>
	</#list>
</table>