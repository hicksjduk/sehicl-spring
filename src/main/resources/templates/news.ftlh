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

<@news end="2026-03-29">
<b>Congratulations to last year's League champions Waterlooville</b>, who have reached the
finals of the national indoor championships. They will head to Lord's on Sunday 29th March,
and we wish them every success. 
</@news>

<@news end="2026-03-29">
We have been asked to inform all players that <b>all kit bags should be stored, while games are in
progress, in Studio 3</b>, which is the room immediately opposite the hall entrance that leads to the
walkway between the courts. They must not be left in corridors, changing rooms or toilets. 
</@news>

<#if started??>
	</div>
</#if>
