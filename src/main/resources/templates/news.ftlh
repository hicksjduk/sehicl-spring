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
<br>It is hoped to run a full season in 2021/22, starting on 10th October. However, 
the start date is dependent on the completion of construction work at the Leisure Centre,
which may be complete by then but might not. The fixture list is currently under construction. 
<br>Spaces are still available; any clubs who are interested in entering one or more teams during the coming
season are invited to contact <@mailto address="sehiclsecretary" domain="outlook.com" description="SEHICL Secretary">
    the Secretary</@mailto>. 
</@news>


<#if started??>
	</div>
</#if>
