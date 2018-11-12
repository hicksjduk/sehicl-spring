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

<@news end="2018-11-25">
<b>A blue helmet</b> was found in the area between the courts after the last games on 11th November.
It is now in the equipment store. If you think it might be yours, please ask one of the umpires the
next time you are at the Leisure Centre.
</@news>

<#if started??>
	</div>
</#if>
