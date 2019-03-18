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

<@news end="2019-04-01">
Congratulations to <b>Havant Under-13s</b> on clinching the first of the seven
divisional championships for the season. The end-of-season presentation evening,
at which all seven champions will play exhibition games and award winners will
receive their trophies, is on <u>Sunday 7th April</u>.
</@news>

<#if started??>
	</div>
</#if>
