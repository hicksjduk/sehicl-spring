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
	</#if>
</#macro>

<@news>
<b>The 2018-19 season</b> will commence on Sunday 30th September. The fixtures will be
published on or before the first weekend in September.
</@news>

<@news end="2018-10-01">
<b>The League's <a href="/dp">Data Protection Policy</a> is now available.</b>
Please contact <@mailto "dpo">the Data Protection Officer</@mailto> 
if you have any concerns or questions regarding the League's processing of personal data. 
</@news>

<#if started??>
	</div>
</#if>
