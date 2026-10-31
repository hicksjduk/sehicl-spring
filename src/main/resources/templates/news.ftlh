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

<#if started??>
	</div>
</#if>
