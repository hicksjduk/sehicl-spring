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
We are still hoping to <b>start the 2020/21 season</b> on the scheduled date of 4th October.
However, we have been advised that the Leisure Centre is not currently in a position to accept
bookings, so our planned schedule cannot be confirmed.
<br>Further information will be communicated as soon as we have it.
</@news>

<@news>
<b>Our sponsors Game Set and Match are offering a 10% discount to participants in the League.</b> Click
on the image below to find out more.
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
