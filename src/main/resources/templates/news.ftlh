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
<b>2021/22 season</b>
<br>We now have confirmation that the 2021/22 season will start on <b>Sunday 10th October</b>. 
<br>Spaces are still available; any clubs who are interested in entering one or more teams during the coming
season are invited to contact <@mailto address="sehiclsecretary" domain="outlook.com" description="SEHICL Secretary">
    the Secretary</@mailto>. 
</@news>


<#if started??>
	</div>
</#if>
