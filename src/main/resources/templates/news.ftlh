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

<@news end="2018-10-31">
<b>Players are reminded that kit bags should <u>not</u> be left in the corridors or changing
rooms while matches are in progress.</b> Teams playing on court A should leave their bags in
the disabled toilets next to the entrance to court A; those playing on court B should use the 
studio opposite the entrance to court B. Thank you.
</@news>

<@news start="2018-10-08" end="2018-10-26">
Please note that <b>the website will not be updated</b> between 12th and 26th October</b>. Results submitted
between those dates will be acknowledged automatically (if submitted by email), but will not appear on the
website until 27th October at the earliest. 
</@news>

<#if started??>
	</div>
</#if>
