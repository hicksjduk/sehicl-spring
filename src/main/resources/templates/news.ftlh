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
We have received reports of players being rude to Centre reception and other staff.  
This is wholly unacceptable.  Players do not have to pay to enter the Centre, 
but you may have to be patient to wait for them to open the gate to let you in.  
Any specific instances brought to the League’s attention will be investigated. 
<br>Richard Matthews, Chairman
</@news>

<#if started??>
	</div>
</#if>
