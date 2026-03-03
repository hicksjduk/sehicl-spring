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

<@news>
We have been asked to inform all players that <b>all kit bags should be stored while games are in
progress in Studio 3</b>, which is the room immediately opposite the hall entrance that leads to the
walkway between the courts. They must not be left in corridors, changing rooms or toilets. 
</@news>

<#if started??>
	</div>
</#if>
