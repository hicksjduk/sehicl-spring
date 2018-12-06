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
<b>Lost property</b>
<br>A blue helmet was found in the area between the courts after the last games on 11th November.
It is now in the League's cupboard in the equipment store. If you think it might be yours, 
please ask one of the umpires the
next time you are at the Leisure Centre.
<br>Also, the Leisure Centre has a helmet that was handed in after the games on 4th November,
and a bat found on 11th November. For these please ask at reception. 
</@news>

<#if started??>
	</div>
</#if>
