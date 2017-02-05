<#function highlight player performance>
	<#return {"player": player, "performance": performance}/>
</#function>

<#function innings team runs wickets overs highlights...>
	<#return {"team": team, "runs": runs, "overs": overs, "wickets": wickets, "highlights": highlights}/>
</#function>

<#macro match title innings1 innings2 maxWickets=6>
<tr>
	<td colspan="2" class="division">${title}</td>
</tr>
<tr>
<#list [innings1, innings2] as inns>
<td class="teamscore">${inns.team} ${inns.runs} 
${(inns.wickets == maxWickets)?then("all out", "for " + inns.wickets)} (10 ov)</td>
<td class="teamscore">Waterlooville 74 for 1 (8.3 ov)</td>
</tr>
</#macro>

<table id="reslist">
<@match "The Mayor of Fareham's VIII v Division 1 Champions' VIII"
	innings("Havant A", 161, 3, 12,
		highlight("P Hopson" "27*"), highlight("B Walker", "25*"), highlight("S Ransley", "24"),
		 highlight("S Matthews", "23"), highlight("B Mengham", "2/26"))
	innings("The Mayor of Fareham's VIII", 112, 4, 12, 
		highlight("J Spiers", "30*") 8) 
/>
</table>

