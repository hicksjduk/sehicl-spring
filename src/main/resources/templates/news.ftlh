<#assign today = .now?date?string.iso/>
<#macro news start=today end=today>
	<!-- News start: ${start}, end: ${end}, today: ${today} -->
	<#if start?date.iso <= today?date.iso && today?date.iso <= end?date.iso>
		<#if !started??>
			<div id="news">
			<#assign started = true/>
		</#if>
		<p>
			<#nested/>
		</p>
	<#else>
		<!-- 
			<#nested/>
		-->
	</#if>
</#macro>

<@news end="2026-10-05">
Everyone is reminded that there are <b>no matches on Sunday 4th October</b> as
the Leisure Centre is in use for a model railway show. The League schedule resumes
on 11th October.
</@news>

<@news start="2026-10-05" end="2026-10-31">
Due to the absence of the Fixture Secretary, the results from the matches on 11th, 18th and 25th
October will be handled by the League Secretary, Sally Priddy, and her husband Richard.
We are grateful to them for stepping into the breach. Results should still be submitted to
the usual address,  
<@mailto address="fixturesec"/>, not later than the Tuesday following the match.
</@news>

<#if started??>
	</div>
</#if>
