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

<@news end="2018-09-30">
<b>The <a href="/fixtures">fixtures</a> and <a href="/dutyRota">duty rota</a> are now available for the 2018-19 season</b> which will commence on Sunday 30th September.
</@news>

<@news end="2018-10-01">
<b>The League's <a href="/dp">Data Protection Policy</a> is now available.</b>
Please contact <@mailto "dpo">the Data Protection Officer</@mailto> 
if you have any concerns or questions regarding the League's processing of personal data. 
</@news>

<@news start="2018-10-08" end="2018-10-26">
Please note that <b>the website will not be updated</b> between 12th and 26th October</b>. Results submitted
between those dates will be acknowledged automatically (if submitted by email), but will not appear on the
website until 27th October at the earliest. 
</@news>

<#if started??>
	</div>
</#if>
