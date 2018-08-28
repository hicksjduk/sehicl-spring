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
	</#if>
</#macro>

<@news>
<b><a href="/fixtures">The fixtures are now available</a> for the 2018-19 season</b> which will commence on Sunday 30th September.
All clubs are asked to review their fixtures carefully, and communicate any concerns as soon as possible
to <@mailto "fixturesec">the Fixture Secretary</@mailto>. In accordance with <a href="/rules#sectionJ">the rules</a>, 
requests for changes
of court (so that players and/or scorers are not involved in consecutive matches on different courts) must be
received by the Fixture Secretary not later than two weeks after publication - that is, by <u>Tuesday 11th September</u>.
After that date, changes will be made only in exceptional circumstances.
<br>The duty rota will be published as soon as possible.
</@news>

<@news end="2018-10-01">
<b>The League's <a href="/dp">Data Protection Policy</a> is now available.</b>
Please contact <@mailto "dpo">the Data Protection Officer</@mailto> 
if you have any concerns or questions regarding the League's processing of personal data. 
</@news>

<#if started??>
	</div>
</#if>
