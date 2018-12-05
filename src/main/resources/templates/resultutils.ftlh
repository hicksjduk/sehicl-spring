<#function innings team runs wickets overs highlights...>
	<#return {"team": {"name": team}, "runs": runs, "overs": overs, "wickets": wickets, "highlights": highlights}/>
</#function>

<#macro match innings1 innings2 maxWickets=6>
<tr>
<#list [innings1, innings2] as inns>
	<td class="teamscore">
		<#if (inns.team.id)??>
			<a href="/fixtures/team/${inns.team.id}">${inns.team.name}</a>
		<#else>
			${inns.team.name}
		</#if> 
		${inns.runs} 
		${(inns.wickets == maxWickets)?then("all out", "for " + inns.wickets)} 
		<#if (inns.overs >= 0)>(${inns.overs} ov)</#if></td>
</#list>
</tr>
<tr>
<#list [innings1, innings2] as inns>
	<td class="highlights">
		<#list inns.highlights as h>
			<span class="nolinewrap">${h}${h?is_last?then("", ",")}</span>
		</#list>
	</td>
</#list>
</tr>
<tr>
	<td class="result" colspan="2"><@result innings1 innings2 maxWickets/></td>
</tr>
</#macro>

<#macro result inns1 inns2 maxWkts>
	<#if inns1.runs == inns2.runs>
		Match tied
	<#elseif inns2.runs < inns1.runs>
		<#assign margin = inns1.runs - inns2.runs/>
		${inns1.team.name} won by ${margin} ${(margin > 1)?then("runs", "run")}
	<#else>
		<#assign margin = maxWkts - inns2.wickets/>
		${inns2.team.name} won by ${margin} ${(margin > 1)?then("wickets", "wicket")}
	</#if>
</#macro>

<#macro walkover res>
	<td class="teamscore" colspan="2">
		<a href="/fixtures/team/${res.winnerId}">${res.winnerName}</a> beat 
		<a href="/fixtures/team/${res.loserId}">${res.loserName}</a>  
			by default (${res.reason})
	</td>
</#macro>