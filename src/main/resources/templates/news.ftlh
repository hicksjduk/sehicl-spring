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
<b>Cancellation of the 2020/21 season</b>
<br>The committee met on 30th November, to consider the possibilities for running some kind of 
truncated 2020/21 season after Christmas. It is clear that other for under-18s, it is completely
impossible for indoor cricket to be played in conformance with the current Covid-19 regulations, for any
tier.
<br>We did consider trying
to run a Colts league from the beginning of January, but have since been informed that the Leisure
Centre is undertaking a refurbishment, as a result of which use of the hall is severely restricted
and they are unable to let us use it. This is expected to continue until the summer of 2021.
<br>Consequently, we have reluctantly taken the decision to <b>cancel the 2020/21 season
entirely</b>. We share the disappointment that this will cause to many, and will now focus
our efforts on ensuring that the 2021/22 season is able to start in the autumn. 
<br>Thank you for your
continuing support.
</@news>

<@news>
<b>Our sponsors Game Set and Match are offering a 10% discount to participants in the League.</b> Click
on the image below to find out more.
</@news>

<@news>
<b>The League is looking for a new Secretary</b>. Anyone interested in taking on this role is requested to
contact <@mailto address="sehiclsecretary" domain="outlook.com" description="SEHICL Secretary">
	Adam Smith, the current Secretary,
	</@mailto> to find out what the job entails. 
</@news>

<#if started??>
	</div>
</#if>
