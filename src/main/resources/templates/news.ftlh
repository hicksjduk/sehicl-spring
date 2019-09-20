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
<b><a href="/fixtures">The fixtures for the 2019/20 season</a> are now available.</b>
All teams are asked to review the fixtures carefully, and communicate any concerns or queries
to <@mailto address="fixturesec">the Fixture Secretary</@mailto> as soon as possible.
Please note that the deadline for requesting changes to the fixture list is 
<u>Friday 27th September</u>; after that date, changes will only be made in exceptional
circumstances.
<br><b>The <a href="/dutyRota">duty rota</a> is also now available</b>.
</@news>

<@news>
<b>The League is looking for a new Secretary</b>. Anyone interested in taking on this role is requested to
contact <@mailto address="secretary" description="SEHICL Secretary">
	Adam Smith, the current Secretary
	</@mailto>, to find out what the job entails. 
</@news>

<#if started??>
	</div>
</#if>
