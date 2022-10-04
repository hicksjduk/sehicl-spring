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

<@news end="2022-10-10">
<b><a href="/fixtures">The fixtures for the 2022-23 season</a> are now available.</b>
The first games of the season are on Sunday 9th October. 
</@news>

<#if started??>
	</div>
</#if>
