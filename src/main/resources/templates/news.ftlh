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

<@news end="2019-01-20">
Congratulations to <b>James Gradwell of Swanmore</b> who took 5 for 11 against Droxford
on 16th December. This was a record for Division 5, and is probably the fourth-best bowling
analysis ever recorded in the League.
</@news>

<#if started??>
	</div>
</#if>
