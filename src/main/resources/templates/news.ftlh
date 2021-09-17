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
The 2021/22 season will start on <b>Sunday 10th October</b>. The fixture list will be published in the next
few days. 
<br>The League is now closed to new entries for the coming season. However, any clubs who are interested 
in entering one or more new teams in the 2022/23
season are invited to contact <@mailto address="secretary" description="SEHICL Secretary">
    the Secretary</@mailto> to join the waiting list. 
</@news>


<#if started??>
	</div>
</#if>
