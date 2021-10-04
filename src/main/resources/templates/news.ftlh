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
We would like to remind all teams that <b>playing in coloured clothing is now permitted in the League</b>. 
The provisions
in the rules that require the wearing of white kit have been set aside, but due to an administrative
oversight the rules have not been changed. The kit worn must be proper cricket
kit (not, for example, football or rugby shirts, and no shorts), and the colour of shirts and trousers must
be uniform for all members of a team. The wearing of white kit is still perfectly acceptable, but is no
longer mandatory. 
</@news>

<@news>
The League is now closed to new entries for the coming season. However, any clubs who are interested 
in entering one or more new teams in the 2022/23
season are invited to contact <@mailto address="secretary" description="SEHICL Secretary">
    the Secretary</@mailto> to join the waiting list. 
</@news>

<#if started??>
	</div>
</#if>
