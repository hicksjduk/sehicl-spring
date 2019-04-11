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

<@news end="2019-04-30">
Congratulations to 
<b>Harry Hovey</b> of Bedhampton A, who scored 85 on 17th March and thus broke the record for the 
highest individual score made in Division 1 - a record that had stood for 20 years. 
</@news>

<#if started??>
	</div>
</#if>
