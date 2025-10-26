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

<@news end="2025-11-01">
Please note that the Fixture Secretary will have no access to his computer during the week commencing 27th October.
If you send in a result by email, you should receive an auto-response to confirm that it has been received,
but the result will not be checked, or uploaded to the website, until Friday evening at the earliest.
If you do not receive an auto-response, you should assume that it has not been received, and send it 
again.
</@news>

<#if started??>
	</div>
</#if>
