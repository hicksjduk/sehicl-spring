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
<b>The Fixture Secretary will be out of electronic contact from <u>Saturday 12th to Friday 25th October</u>
inclusive.</b> During this time, the website will not be updated. For urgent matters, please contact one of
the other committee members, whose contact details can be found on <a href="/contacts">the Contacts page</a>.
</@news>

<@news>
<b>The League is looking for a new Secretary</b>. Anyone interested in taking on this role is requested to
contact <@mailto address="secretary" description="SEHICL Secretary">
	Adam Smith, the current Secretary,
	</@mailto> to find out what the job entails. 
</@news>

<#if started??>
	</div>
</#if>
