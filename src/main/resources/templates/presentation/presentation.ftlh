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
	${(inns.wickets == maxWickets)?then("all out", "for " + inns.wickets)} 
	<#if inns.overs != "">(${inns.overs} ov)</#if></td>
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

<#macro section title>
<h2>${title}</h2>
<#nested/>
</#macro>

<#macro games>
<h3>Exhibition games</h3>
<table id="reslist">
<#nested/>
</table>
</#macro>

<#macro awards rubric="">
<h3>Presentation of awards</h3>
<#if rubric != "">
	<p>${rubric}</p>
</#if>
<dl>
<#nested/>
</dl>
</#macro>

<#assign award_info =
	{
		"d1": {"trophy": "Peter Faulkner Shield (Division 1 Champions)", "desc" : "Division 1 champions"},
		"d2": {"trophy": "Barretts Shield (Division 2 Champions)", "desc" : "Division 2 champions"},
		"d3": {"trophy": "Ferguson Shield (Division 3 Champions)", "desc" : "Division 3 champions"},
		"d4": {"trophy": "Joan Vause Shield (Division 4 Champions)", "desc" : "Division 4 champions"},
		"sbat": {"trophy": "A.R.Pay Batting Trophy", "desc" : "Senior leading run-scorer"},
		"sbowl": {"trophy": "Tony Dunster Memorial Bowling Cup", "desc" : "Senior leading wicket-taker"},
		"sfair": {"trophy": "Alastair MacDonald Award (Senior Sporting and Efficiency Trophy)", 
			"desc" : "Senior Sporting and Efficiency Trophy winners"},
		"u16": {"trophy": "Peter Faulkner Shield (Under-16 Champions)", "desc" : "Colts Under-16 champions"},
		"u13": {"trophy": "Ron Cross MBE Trophy (Under-13 Champions)", "desc" : "Colts Under-13 champions"},
		"u16bat": {"trophy": "Michael Austin Harlick Under-16 Batting Award", "desc" : "Colts Under-16 leading run-scorer"},
		"u16bowl": {"trophy": "Michael Austin Harlick Under-16 Bowling Award", "desc" : "Colts Under-16 leading wicket-taker"},
		"u13bat": {"trophy": "Michael Austin Harlick Under-13 Batting Award", "desc" : "Colts Under-13 leading run-scorer"},
		"u13bowl": {"trophy": "Michael Austin Harlick Under-13 Bowling Award", "desc" : "Colts Under-13 leading wicket-taker"},
		"cfair": {"trophy": "Roy Jones Award (Colts Sporting and Efficiency Trophy)", 
			"desc" : "Colts Sporting and Efficiency Trophy winners"}
	}
/>

<#macro award id winner picture="" text="">
	<#assign info = award_info[id]/>
	<dt>${info.trophy}</dt>
	<dd>
		<#if picture != "">
			<img border="0" src="${picture}" alt="${winner} - ${info.desc}"><br>${winner}
		<#else>
			${winner}
		</#if>
		<#if text != "">
			<br><span style="font-weight: normal; font-style: italic">(${text})</span>
		</#if>
	</dd>
</#macro>

<#macro result inns1 inns2 maxWkts>
	<#if inns1.runs == inns2.runs>
		Match tied
	<#elseif inns2.runs < inns1.runs>
		<#assign margin = inns1.runs - inns2.runs/>
		${inns1.team} won by ${margin} ${(margin > 1)?then("runs", "run")}
	<#else>
		<#assign margin = maxWkts - inns2.wickets/>
		${inns2.team} won by ${margin} ${(margin > 1)?then("wickets", "wicket")}
	</#if>
</#macro>