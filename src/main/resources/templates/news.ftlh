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

<@news start="2024-10-15" end="2024-10-24">
Please be aware that <b>there may be a delay in some results from Sundays 13th and 20th October appearing on the website</b>.
All results received should be there by Friday 25th.
</@news>

<@news end="2024-10-13">
<b><a href="/fixtures">The fixtures are now available</a></b> for the 2024/25 season, which will commence on Sunday 13th October. 
</@news>

<@news end="2024-10-31">
We have received some new stocks of indoor cricket balls, so normal arrangements should be resumed this
season with all Division 1 and Division 2 games using a new ball. 
Many thanks to 
<a href="http://www.gsam.co.uk/">Game Set and Match</a>
for their continued support.
</@news>


<#if started??>
	</div>
</#if>
