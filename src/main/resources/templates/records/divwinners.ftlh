<div id="divwinners">
	<h1>Divisional winners</h1>
	<#list winnerLists as wl>
	<h2>${wl.division}</h2>
	<#if wl.note??>
		<p>(${wl.note})</p>
	</#if>
	<table border="0">
		<tbody>
			<#list wl.winners as w>
			<tr>
				<#assign prev = w.season - 1/>
				<#if (prev / 100)?int == (w.season / 100)?int>
					<#assign season = (w.season % 100)?string("00")/>
				<#else>
					<#assign season = w.season?string.computer/>
				</#if>
				<td class="year">${prev?string.computer}-${season}</td>
				<td class="winner">${w.winner}</td>
			</tr>
			</#list>
		</tbody>
	</table>
	</#list>
</div>