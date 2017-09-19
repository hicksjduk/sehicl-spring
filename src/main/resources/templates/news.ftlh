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

<@news end="2017-10-01">
The 2017-18 season is now under way.
We welcome as new or returning teams Afghan Society Portsmouth, Droxford, Emsworth Under-13s,
Hambledon C and United Services. 
</@news>

<#if started??>
	</div>
</#if>
