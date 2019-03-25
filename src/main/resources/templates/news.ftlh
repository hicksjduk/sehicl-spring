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

<@news end="2019-04-07">
Congratulations to 
<b>Havant A</b>, 
<b>Waterlooville C</b>, 
<b>Gosport Borough Under-16s</b> and 
<b>Havant Under-13s</b> on clinching their respective
divisional championships. The <a href="presentation">end-of-season presentation evening</a>,
at which all seven champions will play exhibition games and award winners will
receive their trophies, is on <u>Sunday 7th April</u>.
</@news>

<#if started??>
	</div>
</#if>
