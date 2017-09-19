<#include "status.ftl"/>

<#list (tables??)?then(tables, [table]) as table>

<div class="nobreak">
<h1><#if tables??>${table.league.name}<#else>${title?replace("Table", "")}</#if></h1>
<#if current>
<p class="statusMessage">
	<@statusMessage table.status table.lastIncludedDate!"" table.toCome "table"/>
</p>
<p class="noprint">Click on a team to see all matches for that team.</p>
</#if>
<table id="table">
<thead>
<tr>
<th class="position number"></th>
<th class="teamname">
</th><th class="played number">P</th>
<th class="won number">W</th><th class="tied number">T</th>
<th class="lost number">L</th><th class="batpoints number">Bat</th>
<th class="bowlpoints number">Bowl</th>
<th class="runrate number">RR</th>
<th colspan="2">Ded</th>
<th class="points number">Pts</th>
</tr>
</thead>
<tbody>
<#list table.rows as row>
<tr>
<td class="position number">
<#if (!lastRow??) || (row.compareSortFields(lastRow) != 0)>${row?counter}</#if>
</td>
<td class="teamname ">
<#if current>
<a href="../fixtures/team/${row.team.id}">${row.team.name}</a>
<#else>
${row.team.name}
</#if>
</td>
<td class="played number">${row.played}</td>
<td class="won number">${row.won}</td>
<td class="tied number">${row.tied}</td>
<td class="lost number">${row.lost}</td>
<td class="batpoints number">${row.battingPoints}</td>
<td class="bowlpoints number">${row.bowlingPoints}</td>
<td class="runrate number">${(row.runRate?string("0.00"))!""}</td>
<td class="dedpoints"><#if row.pointsDeducted != 0>${row.pointsDeducted}</#if></td>
<td class="dedkeys"><#if row.deductionKeys?size != 0>(${row.deductionKeys?join(", ")})</#if>
</td>
<td class="points number">${row.points}</td>
</tr>
<#if current && (row?counter == table.league.teamsPromoted ||
(table.league.teamsRelegated != 0 && 
row?counter == table.league.teams?size - table.league.teamsRelegated))>
<tr><td colspan="12" class="linebelow">&nbsp;</td></tr>
</#if>
<#assign lastRow = row/>
</#list>
</tbody>
</table>
</div>
<#if table.deductionStrings?size != 0>
<ul class="deductions">
<#list table.deductionStrings as deduction>
<li><span class="dedkeys">(${deduction?counter})</span> ${deduction}</li>
</#list>
</ul>
</#if>
<#if table.league.tableNotes??>
<p><b>${table.league.tableNotes}</b></p>
</#if>

</#list>
<#if current><#include "pointscalc.ftlh"/></#if>
	
