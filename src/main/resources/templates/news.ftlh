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
<b>The 2026-27 season of the League will commence on Sunday 20th September.</b>
The fixtures will be published not later than the weekend of 5th and 6th 
September.
<br>Any teams that have Sunday outdoor matches on 20th and/or 27th September,
and would therefore prefer not to play indoor on those dates, are requested
to inform <@mailto address="fixturesec" description="SEHICL Fixture Secretary">
    the Fixture Secretary
    </@mailto> of this not later than Friday 28th August. 
</@news>

<#if started??>
	</div>
</#if>
